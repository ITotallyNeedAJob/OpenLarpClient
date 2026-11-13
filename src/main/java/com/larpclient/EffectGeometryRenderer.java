package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public final class EffectGeometryRenderer implements o_ {
   public static final RenderLayer x = RenderLayer.of("larp_effects", RenderSetup.builder(WorldLabelLayout.g).translucent().build());
   private static final RenderLayer[][] I = new RenderLayer[x4.Type_i.values().length][3];
   private static final RenderLayer[][] j = new RenderLayer[x4.Type_i.values().length][3];
   private static final RenderLayer m;
   private static final double[] c = new double[60];
   private final double[] Q = new double[18];
   private static final RenderLayer[] f = new RenderLayer[3];
   private static final RenderLayer O = RenderLayer.of(
      "larp_geometry_solid",
      RenderSetup.builder(
            RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_COLOR_SNIPPET})
               .withLocation(Identifier.of("larp-client", "geometry_solid"))
               .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
               .withDepthWrite(true)
               .withoutBlend()
               .withCull(false)
               .build()
         )
         .build()
   );
   private boolean C;
   private x4.Type_g B = x4.Type_g.ALPHA;
   private boolean W;
   private final EffectDrawBuffers u;
   private final MatrixStack e;
   private final Vector3f D;
   private final Vector3f h;
   private final Matrix4f H;
   private final Vec3d K;
   private static final double[] E;
   private static final double[] i;

   public void OAsVoid(Vec3d var1, double var2, float var4, float var5, int var6, int var7, boolean var8, float var9) {
      VertexConsumer var10 = this.u.XAsVertexConsumer(JumpRingRenderer.YAsRenderLayer(var7, var8));
      int var11 = Float.floatToRawIntBits(var4);
      int var12 = Float.floatToRawIntBits(var5);
      int var13 = Math.clamp((long)Math.round(var9 * 255.0F), 0, 255) << 24 | var6 & 16777215;

      for (int var14 = 0; var14 < 4; var14++) {
         float var15 = var14 != 0 && var14 != 3 ? 1.25F : -1.25F;
         float var16 = var14 < 2 ? -1.25F : 1.25F;
         var10.vertex(
               this.H, (float)(var1.x + (double)var15 * var2 - this.K.x), (float)(var1.y + 0.02 - this.K.y), (float)(var1.z + (double)var16 * var2 - this.K.z)
            )
            .color(var13)
            .texture(var15, var16)
            .overlay(var11 & 65535, var11 >>> 16)
            .light(var12 & 65535, var12 >>> 16)
            .normal(0.0F, 1.0F, 0.0F);
      }
   }

   public void QAsVoid(boolean var1) {
      this.C = var1 && SoftParticlePipeline.UAsBoolean();
   }

   public void CAsVoid() {
      this.u.CAsVoid();
   }

   @Override
   public void blend(x4.Type_g var1) {
      this.B = var1;
   }

   @Override
   public void solid(boolean var1) {
      this.W = var1;
   }

   public static RenderLayer fAsRenderLayer(x4.Type_g var0) {
      return f[var0.ordinal()];
   }

   public static RenderLayer pAsRenderLayer() {
      return O;
   }

   public static void YAsVoid() {
      for (RenderLayer var3 : I[x4.Type_i.DEFAULT.ordinal()]) {
         WorldLabelLayout.DAsVoid(var3.getRenderPipeline());
      }

      for (RenderLayer var7 : f) {
         WorldLabelLayout.DAsVoid(var7.getRenderPipeline());
      }

      WorldLabelLayout.DAsVoid(O.getRenderPipeline());
      WorldLabelLayout.DAsVoid(m.getRenderPipeline());
   }

   static ImmediateMaterialState aAsPw(ImmediateMaterialState var0) {
      for (int var1 = 0; var1 < 3; var1++) {
         if (var0.pipeline() == j[0][var1].getRenderPipeline()) {
            return new ImmediateMaterialState(
               I[0][var1].getRenderPipeline(), var0.outputTarget(), var0.dynamicTransforms(), var0.scissorState(), var0.textures()
            );
         }
      }

      throw new IllegalArgumentException("Unknown soft particle pipeline");
   }

   public static void aAsVoid() {
      for (RenderLayer var3 : j[x4.Type_i.DEFAULT.ordinal()]) {
         WorldLabelLayout.DAsVoid(var3.getRenderPipeline());
      }
   }

   public EffectGeometryRenderer(MatrixStack var1, EffectDrawBuffers var2) {
      this(
         var1,
         var2,
         MinecraftClient.getInstance().gameRenderer.getCamera().getCameraPos(),
         MinecraftClient.getInstance().gameRenderer.getCamera().getRotation()
      );
   }

   public EffectGeometryRenderer(MatrixStack var1, EffectDrawBuffers var2, CameraRenderState var3) {
      this(var1, var2, var3.pos, var3.orientation);
   }

   private EffectGeometryRenderer(MatrixStack var1, EffectDrawBuffers var2, Vec3d var3, Quaternionfc var4) {
      this.u = var2;
      this.e = var1;
      this.D = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var4);
      this.h = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var4);
      this.H = var1.peek().getPositionMatrix();
      this.K = var3;
   }

   public void dAsVoid(EntityRenderState var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
      Matrix4f var11 = CosmeticSubmissionHandler.dAsMatrix4f(var1, this.e);
      if (var11 != null) {
         xh.YAsVoid(this, var11, var2, var4, var6, var8, var9, var10);
      }
   }

   public void QAsVoid(EntityRenderState var1, HatMeshLoader.Type_l var2, double var3, double var5) {
      Matrix4f var7 = CosmeticSubmissionHandler.dAsMatrix4f(var1, this.e);
      if (var7 != null) {
         List var8 = HatMeshLoader.aAsList(var2);
         int var9 = !var8.isEmpty() && BitcoinPetMesh.nAsBoolean("ChinaHat") ? 1 : 0;
         HatMeshLoader.gAsVoid(this, var7, var2, var3, var5, var9 == 0);
         if (var9 != 0) {
            Matrix4f var10 = new Matrix4f();

            for (HatMeshLoader.Type_a var12 : (Iterable<HatMeshLoader.Type_a>) (var8)) {
               BitcoinPetMesh.rAsVoid(this, BitcoinPetMesh.h.get(var12.coin()), HatMeshLoader.kAsMatrix4f(var7, var12, var3, var10));
            }
         }
      }
   }

   public Matrix4f zAsMatrix4f(EntityRenderState var1, double var2) {
      HornsMeshRenderer.Type_s var4 = HornsMeshRenderer.RAsM5_Type_s();
      if (var4 == null) {
         return null;
      } else {
         Matrix4f var5 = CosmeticSubmissionHandler.dAsMatrix4f(var1, this.e);
         if (var5 == null) {
            return null;
         } else {
            HornsMeshRenderer.sAsVoid(this.u.XAsVertexConsumer(HornsMeshRenderer.oAsRenderLayer()), var5, var4, var2);
            return var5;
         }
      }
   }

   public void ZAsVoid(Matrix4f var1, double var2) {
      HornsMeshRenderer.Type_s var4 = HornsMeshRenderer.RAsM5_Type_s();
      if (var4 != null) {
         HornsMeshRenderer.vAsVoid(this.u.XAsVertexConsumer(HornsMeshRenderer.oAsRenderLayer()), var1, var4, var2);
      }
   }

   public void zAsVoid(ChinaHatFireRenderer var1, Matrix4f var2, double var3, double var5, double var7, int var9, int var10, double var11) {
      HornsMeshRenderer.Type_s var13 = HornsMeshRenderer.RAsM5_Type_s();
      if (var13 != null) {
         Vector3f var14 = new Matrix4f(this.H).invert().mul(var2).transformPosition(0.0F, -0.5F, var13.centerZ() * (float)var3, new Vector3f());
         var1.IAsVoid(
            var11, this.K.x + (double)var14.x, this.K.y + (double)var14.y + 0.24F * var3, this.K.z + (double)var14.z, 0.0, 1.0, 0.0, var3 * var5, var7
         );
         this.pAsVoid(var1, var9, var10, 1.0);
         op.YAsVoid("China Hat fire particles", (long)var1.aAsInt());
      }
   }

   public void pAsVoid(ChinaHatFireRenderer var1, int var2, int var3, double var4) {
      RenderLayer var6 = ChinaHatFireRenderer.UAsRenderLayer();
      var1.RAsVoid((var2x, var4x, var6xx, var8, var10, var12, var14) -> this.TAsVoid(var6, var2x, var4x, var6xx, var8, var10, var12, var14), var2, var3, var4);
   }

   private void TAsVoid(RenderLayer var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14) {
      VertexConsumer var15 = this.u.XAsVertexConsumer(var1);
      double var16 = Math.cos(var12);
      double var18 = Math.sin(var12);

      for (int var20 = 0; var20 < 4; var20++) {
         double var21 = var20 != 0 && var20 != 3 ? 1.0 : -1.0;
         double var23 = var20 < 2 ? -1.0 : 1.0;
         double var25 = (var21 * var16 - var23 * var10 * var18) * var8;
         double var27 = (var21 * var18 + var23 * var10 * var16) * var8;
         var15.vertex(
               this.H,
               (float)(var2 - this.K.x + (double)this.D.x * var25 + (double)this.h.x * var27),
               (float)(var4 - this.K.y + (double)this.D.y * var25 + (double)this.h.y * var27),
               (float)(var6 - this.K.z + (double)this.D.z * var25 + (double)this.h.z * var27)
            )
            .texture((float)((var21 + 1.0) * 0.5), (float)((var23 + 1.0) * 0.5))
            .color(var14);
      }
   }

   public void SAsVoid(RenderLayer var1, Matrix4f var2, float[] var3, int[] var4) {
      GAsVoid(this.u, var1, var2, var3, var4);
   }

   public void SAsVoid(EntityRenderState var1, CosmeticSubmissionHandler.Type_n var2, int var3, double var4) {
      CosmeticSubmissionHandler.AAsVoid(var1, this.e, this.u, var2, var3, var4, false);
   }

   @Override
   public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
      this.u.XAsVertexConsumer(this.W ? O : f[this.B.ordinal()]).vertex(var1, var2, var3, var4).color(var5);
   }

   public void oAsVoid(RenderLayer var1, Vec3d var2, Quaternionfc var3, float var4, float[] var5, int[] var6) {
      Matrix4f var7 = new Matrix4f(this.H)
         .translate((float)(var2.x - this.K.x), (float)(var2.y - this.K.y), (float)(var2.z - this.K.z))
         .rotate(var3)
         .scale(var4);
      GAsVoid(this.u, var1, var7, var5, var6);
   }

   public static void GAsVoid(EffectDrawBuffers var0, RenderLayer var1, Matrix4f var2, float[] var3, int[] var4) {
      VertexConsumer var5 = var0.XAsVertexConsumer(var1);

      for (int var6 = 0; var6 < var4.length; var6++) {
         int var7 = var6 * 5;
         var5.vertex(var2, var3[var7], var3[var7 + 1], var3[var7 + 2]).texture(var3[var7 + 3], var3[var7 + 4]).color(var4[var6]);
      }
   }

   @Override
   public void vertex(double var1, double var3, double var5, int var7) {
      this.u
         .XAsVertexConsumer(f[this.B.ordinal()])
         .vertex(this.H, (float)(var1 - this.K.x), (float)(var3 - this.K.y), (float)(var5 - this.K.z))
         .color(this.B == x4.Type_g.PREMULTIPLIED_ALPHA ? JAsInt(var7) : var7);
   }

   private void DAsVoid(VertexConsumer var1, double var2, double var4, double var6, int var8) {
      var1.vertex(this.H, (float)(var2 - this.K.x), (float)(var4 - this.K.y), (float)(var6 - this.K.z)).color(var8);
   }

   @Override
   public void ribbon(Vec3d var1, Vec3d var2, double var3, int var5, int var6) {
      this.ZAsVoid(var1.x, var1.y, var1.z, var2.x, var2.y, var2.z, var3, var3, var5, var6);
   }

   public void ZAsVoid(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      double var19 = var7 - var1;
      double var21 = var9 - var3;
      double var23 = var11 - var5;
      if (!(var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10)) {
         double var25 = this.K.x - var1;
         double var27 = this.K.y - var3;
         double var29 = this.K.z - var5;
         double var31 = var21 * var29 - var23 * var27;
         double var33 = var23 * var25 - var19 * var29;
         double var35 = var19 * var27 - var21 * var25;
         double var37 = Math.sqrt(var31 * var31 + var33 * var33 + var35 * var35);
         if (var37 < 1.0E-8) {
            var31 = (double)this.D.x;
            var33 = (double)this.D.y;
            var35 = (double)this.D.z;
            var37 = 1.0;
         }

         var31 /= var37 * 2.0;
         var33 /= var37 * 2.0;
         var35 /= var37 * 2.0;
         this.vertex(var1 + var31 * var13, var3 + var33 * var13, var5 + var35 * var13, var17);
         this.vertex(var7 + var31 * var15, var9 + var33 * var15, var11 + var35 * var15, var18);
         this.vertex(var7 - var31 * var15, var9 - var33 * var15, var11 - var35 * var15, var18);
         this.vertex(var1 - var31 * var13, var3 - var33 * var13, var5 - var35 * var13, var17);
      }
   }

   public void zAsVoid(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      this.ZAsVoid(var1, var3, var5, var7, var9, var11, var13 * 2.5, var15 * 2.5, bg.pAsInt(var17, 0.07), bg.pAsInt(var18, 0.07));
      this.ZAsVoid(var1, var3, var5, var7, var9, var11, var13 * 1.25, var15 * 1.25, bg.pAsInt(var17, 0.3), bg.pAsInt(var18, 0.3));
      this.ZAsVoid(
         var1,
         var3,
         var5,
         var7,
         var9,
         var11,
         var13 * 0.23,
         var15 * 0.23,
         bg.xAsInt(var17, var17 & 0xFF000000 | 16777215, 0.48),
         bg.xAsInt(var18, var18 & 0xFF000000 | 16777215, 0.48)
      );
   }

   public void hAsVoid(RibbonCapacity.Type_v var1, boolean var2) {
      for (int var3 = 0; var3 < (var2 ? 3 : 1); var3++) {
         double var4 = !var2 ? 1.0 : (var3 == 0 ? 2.5 : (var3 == 1 ? 1.25 : 0.23));

         for (int var6 = 1; var6 < var1.B; var6++) {
            Vec3d var7 = var1.W[var6 - 1];
            Vec3d var8 = var1.W[var6];
            if (!(var7.squaredDistanceTo(this.K) > var1.i) && !(var7.squaredDistanceTo(var8) < 1.0E-10)) {
               Vec3d var9 = var1.Q[var6 - 1];
               Vec3d var10 = var1.Q[var6];
               double var11 = var9.x * var4;
               double var13 = var9.y * var4;
               double var15 = var9.z * var4;
               double var17 = var10.x * var4;
               double var19 = var10.y * var4;
               double var21 = var10.z * var4;
               int var23 = CAsInt(var1.O[var6 - 1], var3, var2);
               int var24 = CAsInt(var1.O[var6], var3, var2);
               this.vertex(var7.x + var11, var7.y + var13, var7.z + var15, var23);
               this.vertex(var8.x + var17, var8.y + var19, var8.z + var21, var24);
               this.vertex(var8.x - var17, var8.y - var19, var8.z - var21, var24);
               this.vertex(var7.x - var11, var7.y - var13, var7.z - var15, var23);
            }
         }
      }
   }

   public void MAsVoid(RibbonCapacity.Type_v var1, double var2) {
      var2 = Math.clamp(var2, 0.0, 0.95);

      for (int var4 = 1; var4 < var1.B; var4++) {
         Vec3d var5 = var1.W[var4 - 1];
         Vec3d var6 = var1.W[var4];
         if (!(var5.squaredDistanceTo(this.K) > var1.i) && !(var5.squaredDistanceTo(var6) < 1.0E-10)) {
            Vec3d var7 = var1.Q[var4 - 1];
            Vec3d var8 = var1.Q[var4];
            int var9 = var1.O[var4 - 1];
            int var10 = var1.O[var4];
            if (var9 >>> 24 != 0 || var10 >>> 24 != 0) {
               int var11 = var9 & 16777215;
               int var12 = var10 & 16777215;
               if (var2 > 0.0) {
                  this.vertex(var5.x + var7.x * var2, var5.y + var7.y * var2, var5.z + var7.z * var2, var9);
                  this.vertex(var6.x + var8.x * var2, var6.y + var8.y * var2, var6.z + var8.z * var2, var10);
                  this.vertex(var6.x - var8.x * var2, var6.y - var8.y * var2, var6.z - var8.z * var2, var10);
                  this.vertex(var5.x - var7.x * var2, var5.y - var7.y * var2, var5.z - var7.z * var2, var9);
               }

               for (int var13 = -1; var13 <= 1; var13 += 2) {
                  double var14 = var2 * (double)var13;
                  this.vertex(var5.x + var7.x * var14, var5.y + var7.y * var14, var5.z + var7.z * var14, var9);
                  this.vertex(var6.x + var8.x * var14, var6.y + var8.y * var14, var6.z + var8.z * var14, var10);
                  this.vertex(var6.x + var8.x * (double)var13, var6.y + var8.y * (double)var13, var6.z + var8.z * (double)var13, var12);
                  this.vertex(var5.x + var7.x * (double)var13, var5.y + var7.y * (double)var13, var5.z + var7.z * (double)var13, var11);
               }
            }
         }
      }
   }

   private static int CAsInt(int var0, int var1, boolean var2) {
      if (!var2) {
         return var0;
      } else {
         return var1 < 2 ? bg.pAsInt(var0, var1 == 0 ? 0.07 : 0.3) : bg.xAsInt(var0, var0 & 0xFF000000 | 16777215, 0.48);
      }
   }

   public void UAsVoid(EntityRenderState var1, Vec3d var2, double var3, double var5, double var7, int var9, int var10, boolean var11) {
      Matrix4f var12 = CosmeticSubmissionHandler.dAsMatrix4f(var1, this.e);
      if (var12 != null) {
         mo.JAsVoid(this, var12, var3, var5, var7, var9, var10, var11);
      } else {
         this.bAsVoid(var2, var3, var5, var9, var10, var11);
      }
   }

   public void bAsVoid(Vec3d var1, double var2, double var4, int var6, int var7, boolean var8) {
      for (int var9 = 0; var9 < 64; var9++) {
         this.vertex(var1.x, var1.y + var4, var1.z, var6);
         this.vertex(var1.x + E[var9 + 1] * var2, var1.y, var1.z + i[var9 + 1] * var2, var7);
         this.vertex(var1.x + E[var9] * var2, var1.y, var1.z + i[var9] * var2, var7);
         this.vertex(var1.x, var1.y + var4, var1.z, var6);
      }

      if (var8) {
         this.oAsVoid(var1, var2, 0.009, bg.xAsInt(var7, var7 & 0xFF000000 | 16777215, 0.4));
      }
   }

   public void oAsVoid(Vec3d var1, double var2, double var4, int var6) {
      this.bAsVoid(var1.x, var1.y, var1.z, var2, var4, var6);
   }

   public void bAsVoid(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.SAsVoid(var1, var3, var5, var7, var9, var11, 0.0, 1.0, 0.0);
   }

   public void SAsVoid(double var1, double var3, double var5, double var7, double var9, int var11, double var12, double var14, double var16) {
      double var18 = Math.sqrt(var12 * var12 + var14 * var14 + var16 * var16);
      if (var18 < 1.0E-8) {
         var12 = 0.0;
         var14 = 1.0;
         var16 = 0.0;
      } else {
         var12 /= var18;
         var14 /= var18;
         var16 /= var18;
      }

      double var20 = Math.abs(var14) > 0.95 ? 1.0 : -var16;
      double var24 = Math.abs(var14) > 0.95 ? 0.0 : var12;
      double var26 = var20 * var12 + 0.0 * var14 + var24 * var16;
      var20 -= var12 * var26;
      double var22 = 0.0 - var14 * var26;
      var24 -= var16 * var26;
      double var28 = Math.sqrt(var20 * var20 + var22 * var22 + var24 * var24);
      var20 /= var28;
      var22 /= var28;
      var24 /= var28;
      double var30 = var22 * var16 - var24 * var14;
      double var32 = var24 * var12 - var20 * var16;
      double var34 = var20 * var14 - var22 * var12;
      VertexConsumer var36 = this.u.XAsVertexConsumer(f[this.B.ordinal()]);
      int var37 = this.B == x4.Type_g.PREMULTIPLIED_ALPHA ? 1 : 0;

      for (int var38 = 0; var38 < 4; var38++) {
         double var39 = Math.max(0.0, var7 + ((double)var38 / 2.0 - 1.0) * var9);
         double var41 = Math.max(0.0, var7 + ((double)(var38 + 1) / 2.0 - 1.0) * var9);
         double var43 = var38 == 0 ? 0.0 : (var38 == 2 ? 1.0 : 0.22);
         double var45 = var38 == 3 ? 0.0 : (var38 == 1 ? 1.0 : 0.22);
         int var47 = bg.pAsInt(var11, var43);
         int var48 = bg.pAsInt(var11, var45);
         if (var37 != 0) {
            var47 = JAsInt(var47);
            var48 = JAsInt(var48);
         }

         for (int var49 = 0; var49 < 64; var49++) {
            double var50 = var20 * E[var49] + var30 * i[var49];
            double var52 = var22 * E[var49] + var32 * i[var49];
            double var54 = var24 * E[var49] + var34 * i[var49];
            double var56 = var20 * E[var49 + 1] + var30 * i[var49 + 1];
            double var58 = var22 * E[var49 + 1] + var32 * i[var49 + 1];
            double var60 = var24 * E[var49 + 1] + var34 * i[var49 + 1];
            this.DAsVoid(var36, var1 + var50 * var39, var3 + var52 * var39, var5 + var54 * var39, var47);
            this.DAsVoid(var36, var1 + var56 * var39, var3 + var58 * var39, var5 + var60 * var39, var47);
            this.DAsVoid(var36, var1 + var56 * var41, var3 + var58 * var41, var5 + var60 * var41, var48);
            this.DAsVoid(var36, var1 + var50 * var41, var3 + var52 * var41, var5 + var54 * var41, var48);
         }
      }
   }

   public void IAsVoid(double var1, double var3, double var5, double var7, double var9, double var11, double var13, int var15) {
      bz.XAsVoid(this.Q, var7, var9, var11, var13);

      for (int var16 = 0; var16 < bz.uAsInt(); var16++) {
         int var17 = bz.lAsInt(this.Q, var16, var15);

         for (int var18 = 0; var18 < 4; var18++) {
            int var19 = bz.CAsInt(var16, var18) * 3;
            this.vertex(var1 + this.Q[var19], var3 + this.Q[var19 + 1], var5 + this.Q[var19 + 2], var17);
         }
      }
   }

   public void TAsVoid(double var1, double var3, double var5, double var7, double var9, int var11, int var12, boolean var13) {
      int var14 = var13 ? var12 * 2 : var12;

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = var9 + (double)var15 * Math.PI * 2.0 / (double)var14;
         double var18 = var9 + (double)(var15 + 1) * Math.PI * 2.0 / (double)var14;
         double var20 = var7 * (var13 && var15 % 2 == 1 ? 0.4 : 1.0);
         double var22 = var7 * (var13 && (var15 + 1) % 2 == 1 ? 0.4 : 1.0);
         this.vertex(var1, var3, var5, var11);
         this.vertex(
            var1 + (double)this.D.x * Math.cos(var16) * var20 + (double)this.h.x * Math.sin(var16) * var20,
            var3 + (double)this.D.y * Math.cos(var16) * var20 + (double)this.h.y * Math.sin(var16) * var20,
            var5 + (double)this.D.z * Math.cos(var16) * var20 + (double)this.h.z * Math.sin(var16) * var20,
            var11
         );
         this.vertex(
            var1 + (double)this.D.x * Math.cos(var18) * var22 + (double)this.h.x * Math.sin(var18) * var22,
            var3 + (double)this.D.y * Math.cos(var18) * var22 + (double)this.h.y * Math.sin(var18) * var22,
            var5 + (double)this.D.z * Math.cos(var18) * var22 + (double)this.h.z * Math.sin(var18) * var22,
            var11
         );
         this.vertex(var1, var3, var5, var11);
      }
   }

   private static int JAsInt(int var0) {
      double var1 = (double)(var0 >>> 24) / 255.0;
      return var0 & 0xFF000000
         | (int)((double)(var0 >> 16 & 0xFF) * var1) << 16
         | (int)((double)(var0 >> 8 & 0xFF) * var1) << 8
         | (int)((double)(var0 & 0xFF) * var1);
   }

   public void NAsVoid(RenderLayer var1, double var2, double var4, double var6, double var8, double var10, int var12) {
      VertexConsumer var13 = this.u.XAsVertexConsumer(var1);
      double var14 = Math.cos(var10);
      double var16 = Math.sin(var10);

      for (int var18 = 0; var18 < 4; var18++) {
         double var19 = var18 != 0 && var18 != 3 ? 1.0 : -1.0;
         double var21 = var18 < 2 ? -1.0 : 1.0;
         double var23 = (var19 * var14 - var21 * var16) * var8;
         double var25 = (var19 * var16 + var21 * var14) * var8;
         var13.vertex(
               this.H,
               (float)(var2 - this.K.x + (double)this.D.x * var23 + (double)this.h.x * var25),
               (float)(var4 - this.K.y + (double)this.D.y * var23 + (double)this.h.y * var25),
               (float)(var6 - this.K.z + (double)this.D.z * var23 + (double)this.h.z * var25)
            )
            .texture((float)((var19 + 1.0) * 0.5), (float)((var21 + 1.0) * 0.5))
            .color(var12);
      }
   }

   public void BAsVoid(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.cAsVoid(var1, var3, var5, var7, var9, var11, ParticleEmitterEngine.Type_a.Star, 0.0, 0.0, 0.0);
   }

   public void cAsVoid(
      double var1, double var3, double var5, double var7, double var9, int var11, ParticleEmitterEngine.Type_a var12, double var13, double var15, double var17
   ) {
      this.hAsVoid(var1, var3, var5, var7, var9, var11, var12, var13, var15, var17, x4.Type_i.DEFAULT);
   }

   public void hAsVoid(
      double var1,
      double var3,
      double var5,
      double var7,
      double var9,
      int var11,
      ParticleEmitterEngine.Type_a var12,
      double var13,
      double var15,
      double var17,
      x4.Type_i var19
   ) {
      byte var20 = switch (var12) {
         case Glow -> 0;
         case Circle -> 1;
         case Spark -> 2;
         case Star, Textured -> 3;
         case Diamond -> 4;
         case Shard, Triangle -> 5;
         case Petal -> 6;
         case Smoke -> 7;
         default -> 8;
      };
      double var21 = 1.0;
      if (var12 == ParticleEmitterEngine.Type_a.Streak || var12 == ParticleEmitterEngine.Type_a.Spark) {
         var9 = Math.atan2(
            var13 * (double)this.h.x + var15 * (double)this.h.y + var17 * (double)this.h.z,
            var13 * (double)this.D.x + var15 * (double)this.D.y + var17 * (double)this.D.z
         );
         var21 = var12 == ParticleEmitterEngine.Type_a.Streak ? 3.0 : 1.6;
      }

      VertexConsumer var23 = this.u.XAsVertexConsumer((this.C ? j : I)[var19.ordinal()][this.B.ordinal()]);
      double var24 = Math.cos(var9);
      double var26 = Math.sin(var9);

      for (int var28 = 0; var28 < 4; var28++) {
         double var29 = var28 != 0 && var28 != 3 ? 1.0 : -1.0;
         double var31 = var28 < 2 ? -1.0 : 1.0;
         double var33 = (var29 * var21 * var24 - var31 * var26) * var7;
         double var35 = (var29 * var21 * var26 + var31 * var24) * var7;
         var23.vertex(
               this.H,
               (float)(var1 - this.K.x + (double)this.D.x * var33 + (double)this.h.x * var35),
               (float)(var3 - this.K.y + (double)this.D.y * var33 + (double)this.h.y * var35),
               (float)(var5 - this.K.z + (double)this.D.z * var33 + (double)this.h.z * var35)
            )
            .texture((float)(((double)var20 + (var29 + 1.0) * 0.5) / 9.0), (float)((var31 + 1.0) * 0.5))
            .color(var11);
      }
   }

   public void gAsVoid(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17) {
      ol.cAsVoid(c, var7, var9, var11, var13, var15);
      VertexConsumer var18 = this.u.XAsVertexConsumer(m);

      for (int var19 = 0; var19 < 18; var19++) {
         int var20 = ol.UAsInt(c, var19, var17);

         for (int var21 = 0; var21 < 4; var21++) {
            int var22 = ol.LAsInt(var19, var21) * 3;
            var18.vertex(this.H, (float)(var1 - this.K.x + c[var22]), (float)(var3 - this.K.y + c[var22 + 1]), (float)(var5 - this.K.z + c[var22 + 2]))
               .texture((float)ol.IAsDouble(var19, var21), (float)ol.wAsDouble(var19, var21))
               .color(var20);
         }
      }
   }

   public void rAsVoid(Vec3d var1, double var2, double var4, double var6, double var8, int var10, int var11, double var12) {
      int var14 = Math.clamp((long)((int)Math.ceil(Math.abs(var8 - var6) * 12.0)), 4, 96);

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = (double)var15 / (double)var14;
         double var18 = (double)(var15 + 1) / (double)var14;
         double var20 = var6 + (var8 - var6) * var16;
         double var22 = var6 + (var8 - var6) * var18;
         int var24 = bg.xAsInt(var10, var11, var16);
         int var25 = bg.xAsInt(var10, var11, var18);
         double var26 = var1.y + Math.sin(var20 * 2.0) * var12;
         double var28 = var1.y + Math.sin(var22 * 2.0) * var12;
         this.vertex(var1.x + Math.cos(var20) * var2, var26, var1.z + Math.sin(var20) * var2, var24);
         this.vertex(var1.x + Math.cos(var22) * var2, var28, var1.z + Math.sin(var22) * var2, var25);
         this.vertex(var1.x + Math.cos(var22) * (var2 + var4), var28, var1.z + Math.sin(var22) * (var2 + var4), bg.pAsInt(var25, 0.15));
         this.vertex(var1.x + Math.cos(var20) * (var2 + var4), var26, var1.z + Math.sin(var20) * (var2 + var4), bg.pAsInt(var24, 0.15));
      }
   }

   public void KAsVoid(Vec3d var1, double var2, double var4, int var6) {
      for (int var7 = 0; var7 < 40; var7++) {
         double var8 = (double)var7 * Math.PI / 20.0;
         double var10 = (double)(var7 + 1) * Math.PI / 20.0;
         this.vertex(var1.x + Math.cos(var8) * var2, var1.y, var1.z + Math.sin(var8) * var2, var6);
         this.vertex(var1.x + Math.cos(var10) * var2, var1.y, var1.z + Math.sin(var10) * var2, var6);
         this.vertex(var1.x + Math.cos(var10) * (var2 + var4), var1.y, var1.z + Math.sin(var10) * (var2 + var4), var6);
         this.vertex(var1.x + Math.cos(var8) * (var2 + var4), var1.y, var1.z + Math.sin(var8) * (var2 + var4), var6);
      }
   }

   static {
      RenderPipeline var0 = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_TEX_COLOR_SNIPPET})
         .withLocation(Identifier.of("larp-client", "cash"))
         .withFragmentShader(Identifier.of("larp-client", "particle/sprite"))
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withCull(true)
         .build();
      m = RenderLayer.of(
         "larp_cash", RenderSetup.builder(var0).texture("Sampler0", Identifier.of("larp-client", "textures/particles/cash.png")).translucent().build()
      );

      for (x4.Type_g var4 : x4.Type_g.values()) {
         BlendFunction var5 = switch (var4) {
            case ALPHA -> BlendFunction.TRANSLUCENT;
            case ADDITIVE -> BlendFunction.LIGHTNING;
            case PREMULTIPLIED_ALPHA -> BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA;
         };
         Builder var6 = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_TEX_COLOR_SNIPPET})
            .withLocation(Identifier.of("larp-client", "sprites_" + var4.name().toLowerCase(Locale.ROOT)))
            .withFragmentShader(Identifier.of("larp-client", "particle/sprite"))
            .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
            .withDepthWrite(false)
            .withBlend(var5)
            .withCull(false);
         if (var4 == x4.Type_g.PREMULTIPLIED_ALPHA) {
            var6.withShaderDefine("PREMULTIPLIED");
         }

         RenderPipeline var7 = var6.build();
         SoftParticlePipeline.WAsVoid(var6);
         RenderPipeline var8 = var6.withLocation(Identifier.of("larp-client", "soft_sprites_" + var4.name().toLowerCase(Locale.ROOT))).build();

         for (x4.Type_i var12 : x4.Type_i.values()) {
            I[var12.ordinal()][var4.ordinal()] = RenderLayer.of(
               "larp_sprites_" + var12 + "_" + var4,
               RenderSetup.builder(var7).texture("Sampler0", Identifier.of("larp-client", "textures/particles/" + var12.k)).translucent().build()
            );
         }

         for (x4.Type_i var17 : x4.Type_i.values()) {
            j[var17.ordinal()][var4.ordinal()] = RenderLayer.of(
               "larp_soft_sprites_" + var17 + "_" + var4,
               RenderSetup.builder(var8).texture("Sampler0", Identifier.of("larp-client", "textures/particles/" + var17.k)).translucent().build()
            );
         }

         f[var4.ordinal()] = RenderLayer.of(
            "larp_geometry_" + var4,
            RenderSetup.builder(
                  RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_COLOR_SNIPPET})
                     .withLocation(Identifier.of("larp-client", "geometry_" + var4.name().toLowerCase(Locale.ROOT)))
                     .withBlend(var5)
                     .withCull(false)
                     .build()
               )
               .translucent()
               .build()
         );
      }

      E = new double[65];
      i = new double[65];

      for (int var13 = 0; var13 <= 64; var13++) {
         E[var13] = Math.cos((double)var13 * Math.PI / 32.0);
         i[var13] = Math.sin((double)var13 * Math.PI / 32.0);
      }
   }
}
