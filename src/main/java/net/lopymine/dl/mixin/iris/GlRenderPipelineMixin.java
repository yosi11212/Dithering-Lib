package net.lopymine.dl.mixin.iris;

import com.mojang.blaze3d.opengl.*;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.lopymine.dl.client.DitheringLibClient;
import net.lopymine.dl.compat.LoadedMods;
import net.lopymine.dl.utils.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >=26.3 {
/*// Since 26.3 GlRenderPipeline doesn't know its RenderPipeline, and bind() (with Iris' hook) reads the program field directly,
// so RenderTypeMixin marks the compiled pipeline and the program field itself is swapped
@Mixin(GlRenderPipeline.class)
public class GlRenderPipelineMixin implements IrisDitheringMarker {

	@Mutable
	@Shadow @Final private GlProgram program;

	@Unique
	private GlProgram ditheringLib$originalProgram;
	@Unique
	private boolean ditheringLib$bl;

	@Override
	public void ditheringLib$setDithering(boolean bl) {
		this.ditheringLib$bl = bl;
		if (this.ditheringLib$originalProgram == null) {
			this.ditheringLib$originalProgram = this.program;
		}
		GlProgram program = this.ditheringLib$originalProgram;
		if (bl && DitheringLibClient.isEnabled() && LoadedMods.IRIS_LOADED) {
			GlProgram ditheringProgram = ((ProgramContainer) program).ditheringLib$get();
			if (ditheringProgram != null) {
				program = ditheringProgram;
			}
		}
		this.program = program;
	}

	@Override
	public boolean ditheringLib$isDithering() {
		return this.ditheringLib$bl;
	}
}
*///?} else {
@Mixin(GlRenderPipeline.class)
public class GlRenderPipelineMixin {

	@Shadow @Final private RenderPipeline info;

	@Inject(method = "program", at = @At("RETURN"), cancellable = true)
	private void ditheringLib$swapDithering(CallbackInfoReturnable<GlProgram> cir) {
		if (!DitheringLibClient.isEnabled()) {
			return;
		}
		IrisDitheringMarker thing = (IrisDitheringMarker) this.info;
		if (!thing.ditheringLib$isDithering()) {
			return;
		}
		if (!LoadedMods.IRIS_LOADED) {
			return;
		}
		GlProgram ditheringProgram = ((ProgramContainer) cir.getReturnValue()).ditheringLib$get();
		if (ditheringProgram != null) {
			cir.setReturnValue(ditheringProgram);
		}
	}
}
//?}
