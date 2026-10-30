package net.lopymine.dl.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
//? if >=26.3 {
/*import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline;
import com.mojang.renderpearl.frontend.FrontendRenderPipeline;
*///?}
import net.lopymine.dl.client.DitheringLibClient;
import net.lopymine.dl.compat.IrisAPI;
import net.lopymine.dl.dithering.vanilla.*;
import net.lopymine.dl.thing.RenderingMarker;
import net.lopymine.dl.utils.IrisDitheringMarker;
import net.minecraft.client.renderer.rendertype.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=26.2 {
@Mixin(PreparedRenderType.class)
//?} else {
/*@Mixin(RenderType.class)
 *///?}
public class RenderTypeMixin {

	@Unique
	private boolean ditheringLib$bl2;
	@Unique
	@Nullable
	private RenderPipeline ditheringLib$lastRenderPipeline = null;
	@Unique
	@Nullable
	private RenderPipeline ditheringLib$lastDitheringRenderPipeline = null;

	//? if >=26.3 {
	/*@WrapOperation(
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;getCompiledPipeline(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)Lcom/mojang/renderpearl/api/pipeline/CompiledRenderPipeline;"
			),
			method = "draw"
	)
	private CompiledRenderPipeline swapRenderPipeline(RenderPipeline pipeline, Operation<CompiledRenderPipeline> original) {
		CompiledRenderPipeline compiled = original.call(this.ditheringLib$choosePipeline(pipeline));
		// The GL pipeline no longer references its RenderPipeline, so pass the Iris dithering mark to it directly
		if (compiled instanceof FrontendRenderPipeline frontend && frontend.backendRenderPipeline() instanceof IrisDitheringMarker marker) {
			marker.ditheringLib$setDithering(((IrisDitheringMarker) pipeline).ditheringLib$isDithering());
		}
		return compiled;
	}
	*///?} else {
	@WrapOperation(
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V"
			),
			//? if >=26.2 {
			method = "drawFromBuffer(Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/IndexType;III)V"
			//?} else {
			/*method = "draw"
			*///?}
	)
	private void swapRenderPipeline(RenderPass instance, RenderPipeline pipeline, Operation<Void> original) {
		original.call(instance, this.ditheringLib$choosePipeline(pipeline));
	}
	//?}

	@Unique
	private RenderPipeline ditheringLib$choosePipeline(RenderPipeline pipeline) {
		boolean bl = IrisAPI.isShaderPackInUse();
		boolean ditheringEnabled = RenderingMarker.API_DITHERING_RENDERING.get().isEnabled();

		((IrisDitheringMarker) pipeline).ditheringLib$setDithering(ditheringEnabled && bl);
		if (!ditheringEnabled || !DitheringLibClient.isEnabled()) {
			this.ditheringLib$bl2 = false;
			return pipeline;
		}
		if (bl) {
			this.ditheringLib$bl2 = false;
			return pipeline;
		}
		if (this.ditheringLib$lastRenderPipeline == pipeline) {
			this.ditheringLib$bl2 = true;
			return this.ditheringLib$lastDitheringRenderPipeline;
		}
		RenderPipeline ditheringPipeline = VanillaDitheringShaderManager.getDitheringPipeline(pipeline);
		if (ditheringPipeline != null) {
			this.ditheringLib$lastRenderPipeline          = pipeline;
			this.ditheringLib$lastDitheringRenderPipeline = ditheringPipeline;
			this.ditheringLib$bl2 = true;
			return ditheringPipeline;
		}
		this.ditheringLib$bl2 = false;
		return pipeline;
	}

	@Inject(
			at = @At(
					value = "INVOKE",
					//? if >=26.3 {
					/*target = "Lcom/mojang/renderpearl/api/commands/RenderPass;setUniform(Ljava/lang/String;Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;)V"
					*///?} else {
					target = "Lcom/mojang/blaze3d/systems/RenderPass;setUniform(Ljava/lang/String;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"
					//?}
			),
			//? if >=26.3 {
			/*method = "draw"
			*///?} elif >=26.2 {
			method = "drawFromBuffer(Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/IndexType;III)V"
			//?} else {
			/*method = "draw"
			 *///?}
	 	)
	private void bindDitheringDataUniform(CallbackInfo ci, @Local RenderPass pass) {
		if (!DitheringLibClient.isEnabled()) {
			return;
		}
		if (!this.ditheringLib$bl2) {
			return;
		}
		//? if >=26.3 {
		/*GpuBufferSlice slice = VanillaDitheringDataBuffer.getSlice();
		if (slice != null) {
			pass.setUniform("DitheringLibData", slice);
		}
		*///?} else {
		pass.setUniform("DitheringLibData", VanillaDitheringDataBuffer.getBuffer());
		//?}
	}
}
