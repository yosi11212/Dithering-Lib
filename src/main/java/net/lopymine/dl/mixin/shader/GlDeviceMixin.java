package net.lopymine.dl.mixin.shader;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.mojang.blaze3d.shaders.ShaderSource;
import com.mojang.blaze3d.shaders.ShaderType;
import java.util.function.Function;
import net.lopymine.dl.client.DitheringLibClient;
import net.lopymine.dl.dithering.vanilla.*;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

// Since 26.3 shader sources are loaded by the backend-independent frontend (shared by OpenGL and Vulkan)
//? if >=26.3 {
/*@Mixin(targets = "com.mojang.renderpearl.frontend.shaders.PipelineBuilder")
*///?} else {
@Mixin(targets = "com.mojang.blaze3d.opengl.GlDevice")
//?}
public class GlDeviceMixin {

	//? if >=26.3 {
	/*@WrapOperation(
			method = "loadShaderSource",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/renderpearl/api/pipeline/ShaderSource;getShader(Lnet/minecraft/resources/Identifier;Lcom/mojang/renderpearl/api/pipeline/ShaderType;)Ljava/lang/String;"
			)
	)
	private static String ditheringLib$injectDitheringSource(ShaderSource instance, Identifier id, ShaderType type, Operation<String> original) {
		return ditheringLib$getSource(id, (shaderId) -> original.call(instance, shaderId, type));
	}
	*///?} else {
	@WrapOperation(
			method = "compileShader",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/shaders/ShaderSource;get(Lnet/minecraft/resources/Identifier;Lcom/mojang/blaze3d/shaders/ShaderType;)Ljava/lang/String;"
			)
	)
	private String ditheringLib$injectDitheringSource(ShaderSource instance, Identifier id, ShaderType type, Operation<String> original) {
		return ditheringLib$getSource(id, (shaderId) -> instance.get(shaderId, type));
	}
	//?}

	@Unique
	private static String ditheringLib$getSource(Identifier id, Function<Identifier, String> sourceGetter) {
		Identifier base = VanillaDitheringShaderManager.getDitheringShaderId(id);
		if (!DitheringLibClient.isEnabled() || base == null) {
			return sourceGetter.apply(id);
		}
		String baseSource = sourceGetter.apply(base);
		return baseSource == null ? null : VanillaDitherShaderPatcher.patchFragmentShader(baseSource);
	}
}
