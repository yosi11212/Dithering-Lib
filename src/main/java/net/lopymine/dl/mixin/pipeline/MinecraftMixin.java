package net.lopymine.dl.mixin.pipeline;

import net.lopymine.dl.client.DitheringLibClient;
import net.lopymine.dl.dithering.DitheringRenderPipelineManager;
import net.lopymine.dl.dithering.vanilla.VanillaDitheringDataBuffer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, priority = 2042)
public class MinecraftMixin {

	//? if >=26.3 {
	/*@Inject(at = @At("HEAD"), method = "renderFrame")
	private void startDitheringDataFrame(boolean advanceGameTime, CallbackInfo ci) {
		if (DitheringLibClient.isEnabled()) {
			VanillaDitheringDataBuffer.startFrame();
		}
	}
	*///?}

	//? if >=26.2 {
	@Inject(at = @At("HEAD"), method = "onGameLoadFinished")
	private void registerPipelines(CallbackInfo ci) {
		DitheringRenderPipelineManager.registerAndClear();
	}
	//?} else {
		/*@Inject(at = @At("HEAD"), method = "onGameLoadFinished(Lnet/minecraft/client/Minecraft$GameLoadCookie;)V")
		private void registerPipelines(CallbackInfo ci) {
			DitheringRenderPipelineManager.registerAndClear();
		}
	*///?}

}
