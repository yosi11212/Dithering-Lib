package net.lopymine.dl.mixin.iris;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.irisshaders.iris.pipeline.fallback.ShaderSynthesizer;
import net.lopymine.dl.dithering.iris.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShaderSynthesizer.class)
public class ShaderSynthesizerMixin {

	@ModifyReturnValue(method = "fsh", at = @At("RETURN"))
	private static String ditheringLib$patchFallbackFragment(String fragment) {
		if (!IrisDitheringShaderManager.creatingDitheringShader || fragment == null) {
			return fragment;
		}
		return IrisFallbackShaderPatcher.patchFragmentShader(fragment);
	}
}
