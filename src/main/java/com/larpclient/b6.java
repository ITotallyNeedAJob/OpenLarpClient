package com.larpclient;

import net.minecraft.util.math.Vec3d;

public final class b6 {
   public static final double o = 1.5;
   public static final double Q = 1.2;
   public static final double G = 2.4;
   public static final double U = 1.22;
   public static final double g = 0.25;
   public static final double w = 0.42;
   public static final double J = 0.08;
   public static final double u = 0.98;
   public static final int M = 6;
   public static final double C = 0.3;

   public static Vec3d GAsVec3d(Vec3d var0, double var1, Vec3d var3) {
      double var4 = var0.distanceTo(var3) / 2.4;
      if (var4 > 1.0) {
         return Vec3d.ZERO;
      } else {
         Vec3d var6 = var0.add(0.0, var1, 0.0).subtract(var3);
         return var6.lengthSquared() < 1.0E-12 ? Vec3d.ZERO : var6.normalize().multiply((1.0 - var4) * 1.22);
      }
   }

   public static b6.Type_y bAsB6_Type_y(Vec3d var0, Vec3d var1, Vec3d var2, b6.Type_x var3) {
      Vec3d var4 = var1.normalize().multiply(1.5).add(var2);
      Vec3d var5 = var0;

      for (int var6 = 1; var6 <= 6; var6++) {
         Vec3d var7 = var5.add(var4);
         b6.Type_k var8 = var3.yAsB6_Type_k(var5, var7);
         if (var8 != null) {
            return new b6.Type_y(0.0F, 0.0F, var6, var8.point().add(var8.normal().multiply(0.25)), Vec3d.ZERO);
         }

         var5 = var7;
      }

      return null;
   }

   public static Vec3d tAsVec3d(Vec3d var0, Vec3d var1, boolean var2, boolean var3, int var4) {
      double var5 = var0.y;
      double var7 = var3 && var2 ? 0.42 : (var2 ? 0.0 : var1.y);

      for (int var9 = 0; var9 < var4; var9++) {
         var5 += var7;
         var7 = (var7 - 0.08) * 0.98;
         if (var2 && !var3) {
            var5 = var0.y;
            var7 = 0.0;
         }
      }

      return new Vec3d(var0.x + var1.x * (double)var4, var5, var0.z + var1.z * (double)var4);
   }

   public static b6.Type_y rAsB6_Type_y(Vec3d var0, double var1, Vec3d var3, boolean var4, boolean var5, float var6, boolean var7, b6.Type_x var8) {
      Vec3d var9 = var0.add(0.0, var1, 0.0);
      Vec3d var10 = var4 ? new Vec3d(var3.x, 0.0, var3.z) : var3;
      Vec3d var11 = Vec3d.fromPolar(0.0F, var6);
      b6.Type_y var12 = null;
      double var13 = Double.NEGATIVE_INFINITY;
      float var15 = var7 ? var6 + 180.0F : var6;

      for (int var16 = var7 ? 40 : 90; var16 <= 90; var16++) {
         Vec3d var17 = Vec3d.fromPolar((float)var16, var15);
         b6.Type_y var18 = bAsB6_Type_y(var9, var17, var10, var8);
         if (var18 != null) {
            Vec3d var19 = tAsVec3d(var0, var3, var4, var5, var18.ticks());
            Vec3d var20 = GAsVec3d(var19, var1, var18.burst());
            double var21 = var20.x * var11.x + var20.z * var11.z;
            double var23 = var7 ? (var20.y >= 0.15 ? var21 + 0.25 * var20.y : Double.NEGATIVE_INFINITY) : var20.y;
            if (var20.length() >= 0.3 && var23 > var13) {
               var13 = var23;
               var12 = new b6.Type_y(var15, (float)var16, var18.ticks(), var18.burst(), var20);
            }
         }
      }

      return var12;
   }

   public static int DAsInt(int var0, int var1, int var2) {
      int var3 = var2 > 0 ? var2 : var0 + 1 + Math.max(0, var1);
      return Math.max(0, var3 - 2);
   }

   public static double PAsDouble(double var0) {
      double var2 = 0.0;

      for (int var4 = 0; var4 < 200 && var0 > 0.0; var4++) {
         var2 += var0;
         var0 = (var0 - 0.08) * 0.98;
      }

      return var2;
   }

   private b6() {
   }

   public static record Type_k(Vec3d point, Vec3d normal) {
   }

   public interface Type_x {
      b6.Type_k yAsB6_Type_k(Vec3d var1, Vec3d var2);
   }

   public static record Type_y(float yaw, float pitch, int ticks, Vec3d burst, Vec3d push) {
   }
}
