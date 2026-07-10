package net.lopymine.dl.mixin.iris;

import com.mojang.blaze3d.opengl.GlProgram;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.irisshaders.iris.pipeline.programs.ExtendedShader;
import net.lopymine.dl.dithering.DitheringData;
import org.lwjgl.opengl.GL46C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExtendedShader.class)
public class ExtendedShaderMixin {

	@Unique private boolean ditheringLib$resolved = false;
	@Unique private int ditheringLib$far = -1;
	@Unique private int ditheringLib$near = -1;
	@Unique private int ditheringLib$minValue = -1;
	@Unique private int ditheringLib$fixedValue = -1;
	@Unique private int ditheringLib$pixelSize = -1;

	@Inject(method = "iris$setupState", at = @At("TAIL"))
	private void ditheringLib$uploadUniforms(CallbackInfo ci) {
		if (!this.ditheringLib$resolved) {
			int programId = ((GlProgram) (Object) this).getProgramId();
			this.ditheringLib$far = GlStateManager._glGetUniformLocation(programId, "DitheringLibFar");
			this.ditheringLib$near = GlStateManager._glGetUniformLocation(programId, "DitheringLibNear");
			this.ditheringLib$minValue = GlStateManager._glGetUniformLocation(programId, "DitheringLibMinValue");
			this.ditheringLib$fixedValue = GlStateManager._glGetUniformLocation(programId, "DitheringLibFixedValue");
			this.ditheringLib$pixelSize = GlStateManager._glGetUniformLocation(programId, "DitheringLibPixelSize");
			this.ditheringLib$resolved = true;
		}
		if (this.ditheringLib$pixelSize == -1) {
			return;
		}
		DitheringData data = DitheringData.CURRENT_DITHERING_DATA.get();
		if (data == null) {
			data = new DitheringData();
		}
		if (this.ditheringLib$far > -1)        GL46C.glUniform1f(this.ditheringLib$far, data.getFar());
		if (this.ditheringLib$near > -1)       GL46C.glUniform1f(this.ditheringLib$near, data.getNear());
		if (this.ditheringLib$minValue > -1)   GL46C.glUniform1f(this.ditheringLib$minValue, data.getMinValue());
		if (this.ditheringLib$fixedValue > -1) GL46C.glUniform1f(this.ditheringLib$fixedValue, data.getFixedValue());
		GL46C.glUniform1f(this.ditheringLib$pixelSize, data.getPixelSize());
	}
}
