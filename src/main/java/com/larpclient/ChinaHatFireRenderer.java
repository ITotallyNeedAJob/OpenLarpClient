package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import java.util.SplittableRandom;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public final class ChinaHatFireRenderer {
   public static final int U = 288;
   static final double a = 120.0;
   static final double x = 90.0;
   public static final float Q = 0.24F;
   static final double s = 0.6;
   static final double R = 0.25;
   static final double L = 0.36;
   static final double F = 0.16;
   static final double k = 0.85;
   static final double H = 0.95;
   static final double r = 0.3;
   static final double n = 1.9;
   static final double g = 2.5;
   static final double C = 0.19;
   static final double S = 0.11;
   static final double I = 0.09;
   static final double d = 0.05;
   static final double y = 0.09;
   public static final double P = 1.8;
   public static final double T = 1.4;
   static final double t = 0.5;
   private final double[] X = new double[288];
   private final double[] e = new double[288];
   private final double[] V = new double[288];
   private final double[] c = new double[288];
   private final double[] D = new double[288];
   private final double[] z = new double[288];
   private final float[] Y = new float[288];
   private final float[] B = new float[288];
   private final float[] N = new float[288];
   private final float[] h = new float[288];
   private final float[] m = new float[288];
   private final float[] W = new float[288];
   private final boolean[] Z = new boolean[288];
   private final SplittableRandom v;
   private int J;
   private double E = Double.NaN;
   private double O;
   private double A;
   private double i;
   private double K;
   private double o;
   private double b;
   private double p;
   private double u;
   public final Matrix4f M = new Matrix4f();
   public long q = -1L;

   public ChinaHatFireRenderer(long var1) {
      this.v = new SplittableRandom(var1);
   }

   public int aAsInt() {
      return this.J;
   }

   public void gAsVoid() {
      this.J = 0;
      this.E = Double.NaN;
      this.O = this.A = this.b = this.p = this.u = 0.0;
   }

   public static double NAsDouble(double var0, double var2) {
      return 1.3267809374999997 * var2 * var0 + 0.1368 * var0;
   }

   public void IAsVoid(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      double var19 = var1 - this.E;
      double var21 = (var3 - this.i) * (var3 - this.i) + (var5 - this.K) * (var5 - this.K) + (var7 - this.o) * (var7 - this.o);
      if (!(var19 >= 0.0) || var19 > 0.25 || var21 > 4.0) {
         this.gAsVoid();
         this.E = var1;
         this.i = var3;
         this.K = var5;
         this.o = var7;
      } else if (var19 != 0.0) {
         float var23 = (float)var19;
         double var24 = 1.0 - Math.exp(-var19 * 12.0);
         this.b = this.b + ((var3 - this.i) / var19 - this.b) * var24;
         this.p = this.p + ((var5 - this.K) / var19 - this.p) * var24;
         this.u = this.u + ((var7 - this.o) / var19 - this.u) * var24;
         double var26 = Math.exp(-2.5 * var19);
         int var28 = 0;

         while (var28 < this.J) {
            this.Y[var28] = this.Y[var28] + var23;
            if (this.Y[var28] >= this.B[var28]) {
               this.kAsVoid(var28);
            } else {
               double var29 = (this.Z[var28] ? 2.5 : 1.9) * var17 * var15 * var19;
               double var31 = this.c[var28] * var9 + this.D[var28] * var11 + this.z[var28] * var13 + var29;
               this.c[var28] = (this.c[var28] - var9 * (var31 - var29)) * var26 + var9 * var31;
               this.D[var28] = (this.D[var28] - var11 * (var31 - var29)) * var26 + var11 * var31;
               this.z[var28] = (this.z[var28] - var13 * (var31 - var29)) * var26 + var13 * var31;
               this.X[var28] = this.X[var28] + this.c[var28] * var19;
               this.e[var28] = this.e[var28] + this.D[var28] * var19;
               this.V[var28] = this.V[var28] + this.z[var28] * var19;
               this.h[var28] = this.h[var28] + this.m[var28] * var23;
               var28++;
            }
         }

         double var62 = Math.abs(var11) < 0.9 ? 0.0 : 1.0;
         double var30 = Math.abs(var11) < 0.9 ? 1.0 : 0.0;
         double var32 = var30 * var13 - 0.0 * var11;
         double var34 = 0.0 * var9 - var62 * var13;
         double var36 = var62 * var11 - var30 * var9;
         double var38 = Math.sqrt(var32 * var32 + var34 * var34 + var36 * var36);
         var32 /= var38;
         var34 /= var38;
         var36 /= var38;
         double var40 = var11 * var36 - var13 * var34;
         double var42 = var13 * var32 - var9 * var36;
         double var44 = var9 * var34 - var11 * var32;
         this.O += 120.0 * var19;
         this.A += 90.0 * var19;

         while (this.O >= 1.0 || this.A >= 1.0) {
            int var46 = this.A >= this.O ? 1 : 0;
            if (var46 != 0) {
               this.A--;
            } else {
               this.O--;
            }

            if (this.J != 288) {
               double var47 = this.v.nextDouble();
               double var49 = (var46 != 0 ? 0.05 : 0.09) * var15 * Math.sqrt(this.v.nextDouble());
               double var51 = this.v.nextDouble() * Math.PI * 2.0;
               double var53 = Math.cos(var51);
               double var55 = Math.sin(var51);
               double var57 = ((var46 != 0 ? 0.95 : 0.85) + this.v.nextDouble() * 0.3) * var17 * var15;
               double var59 = 0.09 * var15 * this.v.nextDouble();
               int var61 = this.J++;
               this.Z[var61] = var46 != 0;
               this.X[var61] = this.i + (var3 - this.i) * var47 + (var32 * var53 + var40 * var55) * var49;
               this.e[var61] = this.K + (var5 - this.K) * var47 + (var34 * var53 + var42 * var55) * var49;
               this.V[var61] = this.o + (var7 - this.o) * var47 + (var36 * var53 + var44 * var55) * var49;
               this.c[var61] = var9 * var57 + (var32 * var53 + var40 * var55) * var59 + this.b * 0.5;
               this.D[var61] = var11 * var57 + (var34 * var53 + var42 * var55) * var59 + this.p * 0.5;
               this.z[var61] = var13 * var57 + (var36 * var53 + var44 * var55) * var59 + this.u * 0.5;
               this.B[var61] = (float)(var46 != 0 ? 0.36 + this.v.nextDouble() * 0.16 : 0.6 + this.v.nextDouble() * 0.25);
               this.Y[var61] = (float)((1.0 - var47) * var19);
               this.N[var61] = (float)((var46 != 0 ? 0.11 : 0.19) * var15 * (0.8 + this.v.nextDouble() * 0.4));
               this.h[var61] = (float)((this.v.nextDouble() - 0.5) * 0.5);
               this.m[var61] = (float)((this.v.nextDouble() - 0.5) * 1.2);
               this.W[var61] = (float)this.v.nextDouble();
            }
         }

         this.E = var1;
         this.i = var3;
         this.K = var5;
         this.o = var7;
      }
   }

   private void kAsVoid(int var1) {
      int var2 = --this.J;
      this.X[var1] = this.X[var2];
      this.e[var1] = this.e[var2];
      this.V[var1] = this.V[var2];
      this.c[var1] = this.c[var2];
      this.D[var1] = this.D[var2];
      this.z[var1] = this.z[var2];
      this.Y[var1] = this.Y[var2];
      this.B[var1] = this.B[var2];
      this.N[var1] = this.N[var2];
      this.h[var1] = this.h[var2];
      this.m[var1] = this.m[var2];
      this.W[var1] = this.W[var2];
      this.Z[var1] = this.Z[var2];
   }

   public void RAsVoid(ChinaHatFireRenderer.Type_v var1, int var2, int var3, double var4) {
      int var6 = ColorUtil.xAsInt(var2, -13631488, 0.3);
      int var7 = ColorUtil.xAsInt(var3, -1, 0.35);

      for (int var8 = 0; var8 < 2; var8++) {
         for (int var9 = 0; var9 < this.J; var9++) {
            if (this.Z[var9] == (var8 == 1)) {
               double var10 = (double)Math.clamp(this.Y[var9] / this.B[var9], 0.0F, 1.0F);
               double var12;
               double var14;
               int var16;
               if (this.Z[var9]) {
                  var12 = 1.0 - 0.55 * var10;
                  var14 = FAsDouble(var10, 0.18) * (1.0 - var10) * 0.9;
                  var16 = ColorUtil.xAsInt(var7, var3, var10);
               } else {
                  var12 = var10 < 0.15 ? 0.55 + 0.45 * var10 / 0.15 : 1.0 - 0.6 * (var10 - 0.15) / 0.85;
                  var14 = FAsDouble(var10, 0.22) * (1.0 - var10) * 0.62;
                  var16 = var10 < 0.4 ? ColorUtil.xAsInt(var3, var2, var10 / 0.4) : ColorUtil.xAsInt(var2, var6, (var10 - 0.4) / 0.6);
               }

               int var17 = (int)Math.round(Math.clamp(var14 * var4, 0.0, 1.0) * 255.0);
               if (var17 != 0) {
                  var1.GAsVoid(
                     this.X[var9],
                     this.e[var9],
                     this.V[var9],
                     (double)this.N[var9] * var12,
                     this.Z[var9] ? 1.4 : 1.8,
                     (double)this.h[var9],
                     var17 << 24 | var16 & 16777215
                  );
               }
            }
         }
      }
   }

   static double FAsDouble(double var0, double var2) {
      double var4 = Math.clamp(var0 / var2, 0.0, 1.0);
      return var4 * var4 * (3.0 - 2.0 * var4);
   }

   public static RenderLayer UAsRenderLayer() {
      if (ChinaHatFireRenderer.Type_b.z != (long)BloomQuality.J) {
         WorldLabelLayout.DAsVoid(ChinaHatFireRenderer.Type_b.V);
         ChinaHatFireRenderer.Type_b.z = (long)BloomQuality.J;
      }

      return ChinaHatFireRenderer.Type_b.Q;
   }

   private static final class Type_b {
      static final RenderPipeline V = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_TEX_COLOR_SNIPPET})
         .withLocation(Identifier.of("larp-client", "china_hat_fire"))
         .withFragmentShader(Identifier.of("larp-client", "particle/sprite"))
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withBlend(BlendFunction.LIGHTNING)
         .withCull(false)
         .build();
      static final RenderLayer Q = RenderLayer.of(
         "larp_china_hat_fire", RenderSetup.builder(V).texture("Sampler0", Identifier.of("larp-client", "textures/particles/flame.png")).build()
      );
      private static long z = -1L;

      private Type_b() {
      }
   }

   @FunctionalInterface
   public interface Type_v {
      void GAsVoid(double var1, double var3, double var5, double var7, double var9, double var11, int var13);
   }
}
