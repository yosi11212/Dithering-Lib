package net.lopymine.dl.dithering.iris;

import com.mojang.blaze3d.opengl.GlProgram;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.irisshaders.iris.pipeline.programs.*;
import net.irisshaders.iris.shaderpack.loading.ProgramId;
import net.lopymine.dl.api.DitheringLibAPI;
import net.lopymine.dl.client.DitheringLibClient;
import net.lopymine.dl.utils.*;

public class IrisDitheringShaderManager {

	public static volatile boolean creatingDitheringShader = false;

	private static final Map<GlProgram, GlProgram> MAP = new IdentityHashMap<>();

	public static void register(GlProgram original, GlProgram dithering) {
		MAP.put(original, dithering);
		((ProgramContainer) original).ditheringLib$set(dithering);
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public static boolean isTargetShader(ShaderKey key) {
		ProgramId id = key.getProgram();
		return DitheringLibAPI.getInstance().getIrisTargets().contains(id.name().toLowerCase(Locale.ROOT));
	}

	public static void clear() {
		for (Entry<GlProgram, GlProgram> entry : MAP.entrySet()) {
			try {
				GlProgram original = entry.getKey();
				GlProgram dithering = entry.getValue();
				((ProgramContainer) original).ditheringLib$set(null);
				dithering.close();
			} catch (Exception ignored) { }
		}
		MAP.clear();
	}

	public static ShaderSupplier wrapWithDithering(ShaderSupplier original, Supplier<GlProgram> ditheringBuilder) {
		Supplier<GlProgram> originalSupplier = original.shader();

		Supplier<GlProgram> wrapped = () -> {
			GlProgram originalProgram = originalSupplier.get();
			creatingDitheringShader = true;

			//? if >=26.2 {
			Object id = original.key();
			//?} else {
			/*Object id = original.id();
			 *///?}

			try {
				GlProgram ditheringProgram = ditheringBuilder.get();
				if (ditheringProgram != null) {
					register(originalProgram, ditheringProgram);
					DitheringLibClient.LOGGER.info("Copied Iris GlProgram: \"{}\"", id);
				}
			} catch (Exception e) {
				DitheringLibClient.LOGGER.error("Failed to build dithering copy for shader \"{}\"", id, e);
			} finally {
				creatingDitheringShader = false;
			}
			return originalProgram;
		};

		return new ShaderSupplier(original.key(), original.id(), wrapped);
	}
}
