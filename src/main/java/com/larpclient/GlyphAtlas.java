package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class GlyphAtlas {
   private static final RenderLayer h = RenderLayer.of(
      "larp_glyphs",
      RenderSetup.builder(
            RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_COLOR_SNIPPET})
               .withLocation(Identifier.of("larp-client", "larp/glyphs"))
               .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
               .withDepthWrite(false)
               .withBlend(BlendFunction.LIGHTNING)
               .withCull(false)
               .build()
         )
         .build()
   );
   private static final RenderLayer BloomUtility = RenderLayer.of(
      "larp_pearls",
      RenderSetup.builder(
            RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_TEX_COLOR_SNIPPET})
               .withLocation(Identifier.of("larp-client", "larp/pearls"))
               .withFragmentShader(Identifier.of("larp-client", "larp/pearl"))
               .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
               .withDepthWrite(false)
               .withBlend(BlendFunction.LIGHTNING)
               .withCull(false)
               .build()
         )
         .texture("Sampler0", Identifier.of("larp-client", "shaders/larp/pearl_bloom.png"))
         .build()
   );
   private static final int[] S = new int[]{-1, 1, 1, -1};
   private static final int[] V = new int[]{1, 1, -1, -1};
   private static int M = -1;
   private final EffectDrawBuffers R;
   private final Matrix4f r;
   private final Vec3d l;
   private final Vector3f j;
   private final Vector3f X;

   public GlyphAtlas(MatrixStack var1, EffectDrawBuffers var2, CameraRenderState var3) {
      if (M != BloomQuality.J) {
         WorldLabelLayout.DAsVoid(h.getRenderPipeline());
         WorldLabelLayout.DAsVoid(BloomUtility.getRenderPipeline());
         M = BloomQuality.J;
      }

      this.R = var2;
      this.r = var1.peek().getPositionMatrix();
      this.l = var3.pos;
      this.j = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var3.orientation);
      this.X = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var3.orientation);
   }

   private void FAsVoid(VertexConsumer var1, double var2, double var4, double var6, int var8) {
      var1.vertex(this.r, (float)(var2 - this.l.x), (float)(var4 - this.l.y), (float)(var6 - this.l.z)).color(var8);
   }

   public void aAsVoid(Vec3d var1, Vec3d var2, double var3, int var5, int var6) {
      double var7 = var2.x + -var1.x;
      double var9 = var2.y + -var1.y;
      double var11 = var2.z + -var1.z;
      if (!(var7 * var7 + var9 * var9 + var11 * var11 < 1.0E-10)) {
         double var13 = this.l.x + -var1.x;
         double var15 = this.l.y + -var1.y;
         double var17 = this.l.z + -var1.z;
         double var19 = var9 * var17 - var11 * var15;
         double var21 = var11 * var13 - var7 * var17;
         double var23 = var7 * var15 - var9 * var13;
         if (var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10) {
            var19 = (double)this.j.x;
            var21 = (double)this.j.y;
            var23 = (double)this.j.z;
         }

         double var25 = Math.sqrt(var19 * var19 + var21 * var21 + var23 * var23);
         if (var25 < 1.0E-5F) {
            var23 = 0.0;
            var21 = 0.0;
            var19 = 0.0;
         } else {
            var19 /= var25;
            var21 /= var25;
            var23 /= var25;
         }

         double var27 = var3 * 0.5;
         var19 *= var27;
         var21 *= var27;
         var23 *= var27;
         VertexConsumer var29 = this.R.XAsVertexConsumer(h);
         this.FAsVoid(var29, var1.x + var19, var1.y + var21, var1.z + var23, var5);
         this.FAsVoid(var29, var2.x + var19, var2.y + var21, var2.z + var23, var6);
         this.FAsVoid(var29, var2.x + -var19, var2.y + -var21, var2.z + -var23, var6);
         this.FAsVoid(var29, var1.x + -var19, var1.y + -var21, var1.z + -var23, var5);
      }
   }

   public void aAsVoid(Vec3d var1, double var2, int var4) {
      VertexConsumer var5 = this.R.XAsVertexConsumer(b);
      float var6 = (float)(var2 * 0.5);

      for (int var7 = 0; var7 < 4; var7++) {
         float var8 = (float)(var1.x - this.l.x) + (this.j.x * (float)S[var7] + this.X.x * (float)V[var7]) * var6;
         float var9 = (float)(var1.y - this.l.y) + (this.j.y * (float)S[var7] + this.X.y * (float)V[var7]) * var6;
         float var10 = (float)(var1.z - this.l.z) + (this.j.z * (float)S[var7] + this.X.z * (float)V[var7]) * var6;
         var5.vertex(this.r, var8, var9, var10).texture(S[var7] < 0 ? 0.0F : 1.0F, V[var7] < 0 ? 0.0F : 1.0F).color(var4);
      }
   }
}
