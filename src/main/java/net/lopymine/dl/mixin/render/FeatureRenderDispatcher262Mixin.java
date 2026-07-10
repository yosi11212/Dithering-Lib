package net.lopymine.dl.mixin.render;

//? if >=26.2 {

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import java.util.Iterator;
import net.lopymine.dl.client.DitheringLibClient;
import net.lopymine.dl.dithering.DitheringData;
import net.lopymine.dl.dithering.vanilla.VanillaDitheringDataBuffer;
import net.lopymine.dl.thing.RenderingMarker;
import net.lopymine.dl.utils.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher.PreparedFrame;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PreparedFrame.class)
public abstract class FeatureRenderDispatcher262Mixin {

	@Shadow
	public abstract void executeSolid();

	@Shadow
	public abstract void executeTranslucent();

	@Shadow
	public abstract void executeTranslucentAfterTerrain();

	@Shadow
	public abstract void executeOutline();

	@Shadow
	public abstract void executeAlwaysOnTop();

	@Unique
	private boolean executingDithering;

	@WrapOperation(
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Iterator;next()Ljava/lang/Object;"
			),
			method = {"executeSolid", "executeTranslucent", "executeTranslucentAfterTerrain", "executeAlwaysOnTop", "executeOutline"}
	)
	private Object pushData(Iterator<?> instance, Operation<Object> original) {
		if (!this.isExecutingDithering()) {
			return original.call(instance);
		}

		SubmitNodeCollection collection = (SubmitNodeCollection) original.call(instance);
		DitheringData data = ((DitheringDataContainer) collection).ditheringLib$getData();
		if (data == null) {
			DitheringLibClient.LOGGER.error("Activated dithering rendering but data hasn't been pushed!", new Throwable());
			return collection;
		}

		data.push();
		VanillaDitheringDataBuffer.update(data);

		return collection;
	}

	@Inject(at = @At("TAIL"), method = "executeSolid")
	private void executeSolidAgainButDithering(CallbackInfo ci, @Local SubmitNodeStorage submitNodeStorage) {
		this.executeDithering(submitNodeStorage, this::executeSolid);
	}

	@Inject(at = @At("TAIL"), method = "executeTranslucent")
	private void executeTranslucentAgainButDithering(CallbackInfo ci, @Local SubmitNodeStorage submitNodeStorage) {
		this.executeDithering(submitNodeStorage, this::executeTranslucent);
	}

	@Inject(at = @At("TAIL"), method = "executeTranslucentAfterTerrain")
	private void executeTranslucentAfterTerrainAgainButDithering(CallbackInfo ci, @Local SubmitNodeStorage submitNodeStorage) {
		this.executeDithering(submitNodeStorage, this::executeTranslucentAfterTerrain);
	}

	@Inject(at = @At("TAIL"), method = "executeOutline")
	private void executeOutlineAgainButDithering(CallbackInfo ci, @Local SubmitNodeStorage submitNodeStorage) {
		this.executeDithering(submitNodeStorage, this::executeOutline);
	}

	@Inject(at = @At("TAIL"), method = "executeAlwaysOnTop")
	private void executeAlwaysOnTopAgainButDithering(CallbackInfo ci, @Local SubmitNodeStorage submitNodeStorage) {
		this.executeDithering(submitNodeStorage, this::executeAlwaysOnTop);
	}

	@Unique
	private void executeDithering(SubmitNodeStorage submitNodeStorage, Runnable runnable) {
		if (!DitheringLibClient.isEnabled()) {
			return;
		}
		if (this.executingDithering) {
			return;
		}
		if (((DitheringMarker) submitNodeStorage).ditheringLib$getDitheringStorage().getSubmitsPerOrder().isEmpty()) {
			return;
		}

		RenderingMarker.API_DITHERING_RENDERING.get().setEnabled(true);
		this.executingDithering = true;
		runnable.run();
		this.executingDithering = false;
		RenderingMarker.API_DITHERING_RENDERING.get().setEnabled(false);
	}

	@WrapOperation(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/SubmitNodeStorage;getSubmitsPerOrder()Lit/unimi/dsi/fastutil/ints/Int2ObjectAVLTreeMap;"),
			method = {"executeSolid", "executeTranslucent", "executeTranslucentAfterTerrain", "executeAlwaysOnTop", "executeOutline"}
	)
	private Int2ObjectAVLTreeMap<SubmitNodeCollection> swapToDitheringModelSet(SubmitNodeStorage instance, Operation<Int2ObjectAVLTreeMap<SubmitNodeCollection>> original) {
		if (!this.isExecutingDithering()) {
			return original.call(instance);
		}
		return ((DitheringMarker) instance).ditheringLib$getDitheringStorage().getSubmitsPerOrder();
	}

	@Unique
	private boolean isExecutingDithering() {
		return this.executingDithering;
	}

}
//?}
