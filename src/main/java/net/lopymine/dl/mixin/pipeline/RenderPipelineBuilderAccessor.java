package net.lopymine.dl.mixin.pipeline;

//? if >=26.2 {
import com.mojang.blaze3d.PrimitiveTopology;
 //?}
import com.mojang.blaze3d.pipeline.*;
import com.mojang.blaze3d.pipeline.RenderPipeline.*;
import com.mojang.blaze3d.platform.*;
import com.mojang.blaze3d.vertex.VertexFormat;

import com.mojang.blaze3d.vertex.VertexFormat.*;
import java.util.*;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SuppressWarnings("unused")
@Mixin(RenderPipeline.Builder.class)
public interface RenderPipelineBuilderAccessor {

	//? if >=26.3 {
	/*@Accessor("location")
	Optional<Identifier> getLocation();

	@Accessor("shaders")
	Map<ShaderType, Identifier> getShaders();

	@Accessor("definesBuilder")
	Optional<ShaderDefines.Builder> getDefinesBuilder();

	@Accessor("bindGroupLayouts")
	Optional<Set<BindGroupLayout>> getBindGroupLayouts();

	@Accessor("depthStencilState")
	Optional<DepthStencilState> getDepthStencilState();

	@Accessor("polygonMode")
	Optional<PolygonMode> getPolygonMode();

	@Accessor("cull")
	Optional<Boolean> getCull();

	@Accessor("colorTargetStates")
	ColorTargetState[] getColorTargetStates();

	@Accessor("activeColorTargetStateCount")
	int getActiveColorTargetStateCount();

	@Accessor("vertexFormatPerBuffer")
	VertexFormat[] getVertexFormatPerBuffer();

	@Accessor("primitiveTopology")
	Optional<PrimitiveTopology> getPrimitiveTopology();

	@Accessor("pushConstantSize")
	int getPushConstantSize();

	*///?} elif >=26.2 {
	@Accessor("location")
	Optional<Identifier> getLocation();

	@Accessor("fragmentShader")
	Optional<Identifier> getFragmentShader();

	@Accessor("vertexShader")
	Optional<Identifier> getVertexShader();

	@Accessor("definesBuilder")
	Optional<ShaderDefines.Builder> getDefinesBuilder();

	@Accessor("bindGroupLayouts")
	Optional<List<BindGroupLayout>> getBindGroupLayouts();

	@Accessor("depthStencilState")
	Optional<DepthStencilState> getDepthStencilState();

	@Accessor("polygonMode")
	Optional<PolygonMode> getPolygonMode();

	@Accessor("cull")
	Optional<Boolean> getCull();

	@Accessor("colorTargetStates")
	ColorTargetState[] getColorTargetStates();

	@Accessor("activeColorTargetStateCount")
	int getActiveColorTargetStateCount();

	@Accessor("vertexFormatPerBuffer")
	VertexFormat[] getVertexFormatPerBuffer();

	@Accessor("primitiveTopology")
	Optional<PrimitiveTopology> getPrimitiveTopology();

	//?} else >=26.1 {
	/*@Accessor("location")
	Optional<Identifier> getLocation();

	@Accessor("fragmentShader")
	Optional<Identifier> getFragmentShader();

	@Accessor("vertexShader")
	Optional<Identifier> getVertexShader();

	@Accessor("definesBuilder")
	Optional<ShaderDefines.Builder> getDefinesBuilder();

	@Accessor("samplers")
	Optional<List<String>> getSamplers();

	@Accessor("uniforms")
	Optional<List<UniformDescription>> getUniforms();

    @Accessor("depthStencilState")
    Optional<DepthStencilState> getDepthStencilState();

    @Accessor("colorTargetState")
    Optional<ColorTargetState> getColorTargetState();

	@Accessor("polygonMode")
	Optional<PolygonMode> getPolygonMode();

	@Accessor("cull")
	Optional<Boolean> getCull();

	@Accessor("vertexFormat")
	Optional<VertexFormat> getVertexFormat();

	@Accessor("vertexFormatMode")
	Optional<Mode> getVertexFormatMode();
    *///?} else {

	/*@Accessor("location")
	Optional<Identifier> getLocation();

	@Accessor("fragmentShader")
	Optional<Identifier> getFragmentShader();

	@Accessor("vertexShader")
	Optional<Identifier> getVertexShader();

	@Accessor("definesBuilder")
	Optional<ShaderDefines.Builder> getDefinesBuilder();

	@Accessor("samplers")
	Optional<List<String>> getSamplers();

	@Accessor("uniforms")
	Optional<List<UniformDescription>> getUniforms();

	@Accessor("depthTestFunction")
	Optional<DepthTestFunction> getDepthTestFunction();

	@Accessor("writeDepth")
	Optional<Boolean> getWriteDepth();

	@Accessor("depthBiasScaleFactor")
	float getDepthBiasScaleFactor();

	@Accessor("depthBiasConstant")
	float getDepthBiasConstant();

	@Accessor("writeColor")
	Optional<Boolean> getWriteColor();

	@Accessor("writeAlpha")
	Optional<Boolean> getWriteAlpha();

	@Accessor("colorLogic")
	Optional<LogicOp> getColorLogic();

	@Accessor("blendFunction")
	Optional<BlendFunction> getBlendFunction();

	@Accessor("polygonMode")
	Optional<PolygonMode> getPolygonMode();

	@Accessor("cull")
	Optional<Boolean> getCull();

	@Accessor("vertexFormat")
	Optional<VertexFormat> getVertexFormat();

	@Accessor("vertexFormatMode")
	Optional<Mode> getVertexFormatMode();
	*///?}

}