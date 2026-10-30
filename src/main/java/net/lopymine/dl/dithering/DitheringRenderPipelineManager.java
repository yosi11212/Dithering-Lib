package net.lopymine.dl.dithering;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.shaders.ShaderType;
import java.util.*;
import java.util.Map.Entry;
import net.lopymine.dl.dithering.vanilla.*;
import net.minecraft.resources.Identifier;

public class DitheringRenderPipelineManager {

	private static final Map<RenderPipeline, Builder> MAP = new HashMap<>();

	public static void savePipelineBuilder(RenderPipeline pipeline, Builder builder) {
		MAP.put(pipeline, builder);
	}

	public static void registerAndClear() {
		for (Entry<RenderPipeline, Builder> entry : MAP.entrySet()) {
			RenderPipeline pipeline = entry.getKey();
			Builder builder = entry.getValue();
			//? if >=26.3 {
			/*Identifier fragmentShader = pipeline.getShaders().get(ShaderType.FRAGMENT);
			*///?} else {
			Identifier fragmentShader = pipeline.getFragmentShader();
			//?}
			if (VanillaDitheringTargets.isTarget(fragmentShader)) {
				VanillaDitheringShaderManager.createDitheringPipeline(pipeline, builder);
			}
		}
		MAP.clear();
	}
}
