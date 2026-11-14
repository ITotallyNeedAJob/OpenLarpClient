package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public final class HalloweenShockwaveGeometry {
   public static final double SkyChunkCache = 2.2;
   public static final double HexEdgeloop = 0.18;
   public static final double A = 0.56;
   public static final double Y = 0.78;
   public static final double C = 2.2;
   public static final double M = 0.035;
   public static final double D = 1.9042969F;

   private static double uAsDouble(double var0) {
      return ay.RAsDouble(var0);
   }

   public static double cAsDouble(double var0) {
      return Double.isFinite(var0) && !(var0 < 0.0) && !(var0 >= 2.2) ? uAsDouble(var0 / 0.12) * JAsDouble(var0) : 0.0;
   }

   public static double JAsDouble(double var0) {
      return Double.isFinite(var0) && !(var0 < 0.0) && !(var0 >= 2.2) ? uAsDouble((2.2 - var0) / 0.65) : 0.0;
   }

   public static double mAsDouble(double var0) {
      return Double.isFinite(var0) ? 1.0 - 0.9 * uAsDouble((var0 - 0.56) / 0.21999999999999997) : 1.0;
   }

   public static double BAsDouble(double var0) {
      if (var0 >= 0.56) {
         return 0.035 + 1.9042969F * mAsDouble(var0);
      } else {
         double var2 = Math.clamp((var0 - 0.18) / 0.38000000000000006, 0.0, 1.0);
         return 1.939296875 + 1.495703125 * (1.0 - var2 * var2);
      }
   }

   private static Vec3d TAsVec3d(Vec3d var0, Vec3d var1, double var2) {
      double var4 = Math.cos(var2);
      double var6 = Math.sin(var2);
      return var1.add(var0.x * var4 + var0.z * var6, var0.y, -var0.x * var6 + var0.z * var4);
   }

   public static HalloweenShockwaveGeometry.Type_k uAsWn_Type_k(Vec3d var0, double var1, double var3, CinematicQuality var5) {
      double var6 = cAsDouble(var3);
      if (!(var6 <= 0.0) && Double.isFinite(var1) && Double.isFinite(var0.x + var0.y + var0.z)) {
         ArrayList var8 = new ArrayList(var5.EAsInt() * 2);
         if (var3 >= 0.78 && var3 < 1.48) {
            double var9 = (var3 - 0.78) / 0.7;
            double var11 = 0.28 + 2.32 * uAsDouble(var9);
            double var13 = 0.1 + 0.07 * (1.0 - var9);
            int var15 = bg.XAsInt(-3046331, 0.95 * Math.pow(1.0 - var9, 1.4) * var5.n);
            int var16 = bg.XAsInt(var15, 0.0);
            int var17 = var5.EAsInt();

            for (int var18 = 0; var18 < 2; var18++) {
               double var19 = var11 + (double)(var18 - 1) * var13;
               double var21 = var19 + var13;

               for (int var23 = 0; var23 < var17; var23++) {
                  double var24 = (double)var23 * Math.PI * 2.0 / (double)var17;
                  double var26 = (double)(var23 + 1) * Math.PI * 2.0 / (double)var17;
                  var8.add(
                     new HalloweenShockwaveGeometry.Type_m(
                        TAsVec3d(new Vec3d(Math.cos(var24) * var19, 0.025, Math.sin(var24) * var19), var0, var1),
                        TAsVec3d(new Vec3d(Math.cos(var26) * var19, 0.025, Math.sin(var26) * var19), var0, var1),
                        TAsVec3d(new Vec3d(Math.cos(var26) * var21, 0.025, Math.sin(var26) * var21), var0, var1),
                        TAsVec3d(new Vec3d(Math.cos(var24) * var21, 0.025, Math.sin(var24) * var21), var0, var1),
                        var18 == 0 ? var16 : var15,
                        var18 == 0 ? var15 : var16
                     )
                  );
               }
            }
         }

         return new HalloweenShockwaveGeometry.Type_k(var0, var1, new HalloweenShockwaveGeometry.Type_j(BAsDouble(var3), var6), List.copyOf(var8));
      } else {
         return HalloweenShockwaveGeometry.Type_k.EMPTY;
      }
   }

   public static Matrix4f JAsMatrix4f(HalloweenShockwaveGeometry.Type_k var0, Vec3d var1) {
      return new Matrix4f()
         .translate((float)(var0.origin.x - var1.x), (float)(var0.origin.y - var1.y), (float)(var0.origin.z - var1.z))
         .rotateY((float)var0.yaw)
         .translate(0.0F, (float)var0.pumpkin.bottom, 0.0F)
         .scale(2.2F);
   }

   public static az.Type_x[] wAsAz_xArray(az var0, double var1) {
      az.Type_x[] var3 = new az.Type_x[6];
      double var4 = mAsDouble(var1);
      double var6 = 1.0 + 0.3 * (1.0 - var4);
      double var8 = JAsDouble(var1);
      Quaternionf var10 = new Quaternionf().rotateY((float)(Math.PI + var0.M));
      Vec3d var11 = new Vec3d(var6, var4, var6);

      for (int var12 = 0; var12 < var3.length; var12++) {
         double var13 = az.mAsDouble(var12, var0.k) / 16.0 * 0.9375;
         double var15 = (24.0 - az.A[var12][1]) / 16.0 * 0.9375;
         Vec3d var17 = TAsVec3d(new Vec3d(var13 * var6, 0.035 + var15 * var4, 0.0), var0.X, var0.M);
         var3[var12] = new az.Type_x(var17, 0.0, 0.0, 0.0, 1.0, var8, 0.0, var1 >= 0.78, var10, var11);
      }

      return var3;
   }

   public static void NAsVoid(
      EffectGeometryRenderer var0, MatrixStack var1, EffectDrawBuffers var2, HalloweenShockwaveGeometry.Type_k var3, Vec3d var4, boolean var5
   ) {
      PumpkinRenderer.gAsVoid(var1, var2, var3, var4, var5);
      var0.blend(x4.Type_g.ALPHA);
      double var6 = var5 ? 0.3 : 1.0;

      for (HalloweenShockwaveGeometry.Type_m var9 : var3.wave) {
         int var10 = bg.pAsInt(var9.inner, var6);
         int var11 = bg.pAsInt(var9.outer, var6);
         var0.vertex(var9.a.x, var9.a.y, var9.a.z, var10);
         var0.vertex(var9.b.x, var9.b.y, var9.b.z, var10);
         var0.vertex(var9.c.x, var9.c.y, var9.c.z, var11);
         var0.vertex(var9.d.x, var9.d.y, var9.d.z, var11);
      }

      op.FAsVoid("Halloween shockwave vertices", (long)var3.vertices());
   }

   private HalloweenShockwaveGeometry() {
   }

   public static record Type_j(double bottom, double alpha) {
   }

   public static record Type_k(Vec3d origin, double yaw, HalloweenShockwaveGeometry.Type_j pumpkin, List<HalloweenShockwaveGeometry.Type_m> wave) {
      public static final HalloweenShockwaveGeometry.Type_k EMPTY = new HalloweenShockwaveGeometry.Type_k(Vec3d.ZERO, 0.0, null, List.of());

      public int vertices() {
         return this.wave.size() * 4;
      }
   }

   public static record Type_m(Vec3d a, Vec3d b, Vec3d c, Vec3d d, int inner, int outer) {
   }
}
