package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.util.Identifier;

public final class SnowflakeRenderer {
   private static int v = -1;
   private static final RenderLayer N = RenderLayer.of(
      "larp_snowflakes",
      RenderSetup.builder(
            RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_TEX_COLOR_SNIPPET})
               .withLocation(Identifier.of("larp-client", "snowflakes"))
               .withFragmentShader(Identifier.of("larp-client", "particle/snow"))
               .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
               .withDepthWrite(false)
               .withBlend(BlendFunction.TRANSLUCENT)
               .withCull(false)
               .build()
         )
         .texture("Sampler0", Identifier.of("larp-client", "textures/particles/effects.png"))
         .translucent()
         .build()
   );

   public static RenderLayer VAsRenderLayer() {
      if (v != BloomQuality.J) {
         WorldLabelLayout.DAsVoid(N.getRenderPipeline());
         v = BloomQuality.J;
      }

      return N;
   }

   private SnowflakeRenderer() {
   }
}
