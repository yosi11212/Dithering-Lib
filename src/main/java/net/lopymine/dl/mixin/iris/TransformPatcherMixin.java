package net.lopymine.dl.mixin.iris;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.*;
import net.irisshaders.iris.pipeline.transform.PatchShaderType;
import net.irisshaders.iris.pipeline.transform.TransformPatcher;
import net.lopymine.dl.dithering.iris.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TransformPatcher.class)
public class TransformPatcherMixin {

	@ModifyReturnValue(method = "patchVanilla", at = @At("RETURN"))
	private static Map<PatchShaderType, String> ditheringLib$patchFragment(Map<PatchShaderType, String> transformed) {
		if (!IrisDitheringShaderManager.creatingDitheringShader || transformed == null) {
			return transformed;
		}
		String fragment = transformed.get(PatchShaderType.FRAGMENT);
		if (fragment == null) {
			return transformed;
		}
		Map<PatchShaderType, String> patched = new EnumMap<>(transformed);
		patched.put(PatchShaderType.FRAGMENT, IrisShaderPatcher.patchFragmentShader(fragment));
		return patched;
	}
}
