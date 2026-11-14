package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.pipeline.RenderPipeline.UniformDescription;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public final class WorldLabelLayout {
   public static final RenderPipeline g = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_COLOR_SNIPPET})
      .withLocation(Identifier.of("larp-client", "world"))
      .build();
   public static final RenderPipeline c = BAsRenderPipeline("horizontal", "blur_h", false);
   public static final RenderPipeline B = BAsRenderPipeline("vertical", "blur_v", false);
   public static final RenderPipeline u = BAsRenderPipeline("copy", "copy", false);
   public static final RenderPipeline N = BAsRenderPipeline("composite", "bloom", true);

   public WorldLabelLayout() {
   }

   private static RenderPipeline BAsRenderPipeline(String var0, String var1, boolean var2) {
      Builder var3 = RenderPipeline.builder(new Snippet[0]).withSampler("Source");
      if (!var1.equals("copy")) {
         var3.withUniform("PostParameters", UniformType.UNIFORM_BUFFER);
      }

      if (var2) {
         var3.withBlend(BlendFunction.ADDITIVE);
      }

      return var3.withLocation(Identifier.of("larp-client", var0))
         .withVertexShader(Identifier.of("larp-client", "post/fullscreen"))
         .withFragmentShader(Identifier.of("larp-client", "post/" + var1))
         .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .build();
   }

   public static Builder GAsRenderPipeline_Builder(RenderPipeline var0) {
      Builder var1 = RenderPipeline.builder(new Snippet[0])
         .withVertexShader(var0.getVertexShader())
         .withFragmentShader(var0.getFragmentShader())
         .withVertexFormat(var0.getVertexFormat(), var0.getVertexFormatMode())
         .withDepthTestFunction(var0.getDepthTestFunction())
         .withDepthWrite(var0.isWriteDepth())
         .withColorWrite(var0.isWriteColor(), var0.isWriteAlpha())
         .withCull(var0.isCull())
         .withPolygonMode(var0.getPolygonMode())
         .withColorLogic(var0.getColorLogic())
         .withDepthBias(var0.getDepthBiasScaleFactor(), var0.getDepthBiasConstant());
      var0.getBlendFunction().ifPresentOrElse(var1::withBlend, var1::withoutBlend);
      var0.getSamplers().forEach(var1::withSampler);

      for (UniformDescription var3 : var0.getUniforms()) {
         if (var3.textureFormat() == null) {
            var1.withUniform(var3.name(), var3.type());
         } else {
            var1.withUniform(var3.name(), var3.type(), var3.textureFormat());
         }
      }

      var0.getShaderDefines().flags().forEach(var1::withShaderDefine);
      var0.getShaderDefines().values().forEach((var1xx, var2) -> var1.withShaderDefine(var1xx, Float.parseFloat(var2)));
      return var1;
   }

   public static void DAsVoid(RenderPipeline var0) {
      if (!RenderSystem.getDevice().precompilePipeline(var0).isValid()) {
         throw new IllegalStateException(
            "Pipeline "
               + var0.getLocation()
               + " failed; shader="
               + var0.getFragmentShader()
               + "; diagnostics="
               + RenderSystem.getDevice().getLastDebugMessages()
         );
      }
   }
}
