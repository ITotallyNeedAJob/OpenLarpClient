package com.larpclient;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class RibbonCapacity {
   private RibbonCapacity() {
   }

   public static Vec3d dAsVec3d(Vec3d var0, Vec3d var1, Vec3d var2, Vec3d var3, double var4) {
      if (var4 <= 0.0) {
         return var1;
      } else {
         return var4 >= 1.0
            ? var2
            : DAsVec3d(var0, var1, var2, var3, var4, Math.sqrt(var0.distanceTo(var1)), Math.sqrt(var1.distanceTo(var2)), Math.sqrt(var2.distanceTo(var3)));
      }
   }

   private static Vec3d DAsVec3d(Vec3d var0, Vec3d var1, Vec3d var2, Vec3d var3, double var4, double var6, double var8, double var10) {
      if (!(var6 < 1.0E-6) && !(var8 < 1.0E-6) && !(var10 < 1.0E-6)) {
         double var12 = var6 + var4 * var8;
         double var14 = var12 / var6;
         double var16 = (var12 - var6) / var8;
         double var18 = (var12 - var6 - var8) / var10;
         double var20 = var12 / (var6 + var8);
         double var22 = (var12 - var6) / (var8 + var10);
         return new Vec3d(
            MAsDouble(var0.x, var1.x, var2.x, var3.x, var14, var16, var18, var20, var22),
            MAsDouble(var0.y, var1.y, var2.y, var3.y, var14, var16, var18, var20, var22),
            MAsDouble(var0.z, var1.z, var2.z, var3.z, var14, var16, var18, var20, var22)
         );
      } else {
         return var1.lerp(var2, var4);
      }
   }

   private static double MAsDouble(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      double var18 = MathHelper.lerp(var8, var0, var2);
      double var20 = MathHelper.lerp(var10, var2, var4);
      double var22 = MathHelper.lerp(var12, var4, var6);
      double var24 = MathHelper.lerp(var14, var18, var20);
      double var26 = MathHelper.lerp(var16, var20, var22);
      return MathHelper.lerp(var10, var24, var26);
   }

   static final class Type_f {
      private Vec3d Z;
      private Vec3d V;
      private Vec3d k;
      private Vec3d W;
      private double D;
      private double f;
      private double e;

      Type_f() {
      }

      void BAsVoid(Vec3d var1, Vec3d var2, Vec3d var3, Vec3d var4) {
         this.Z = var1;
         this.V = var2;
         this.k = var3;
         this.W = var4;
         this.D = Math.sqrt(var1.distanceTo(var2));
         this.f = Math.sqrt(var2.distanceTo(var3));
         this.e = Math.sqrt(var3.distanceTo(var4));
      }

      Vec3d hAsVec3d(double var1) {
         if (var1 <= 0.0) {
            return this.V;
         } else {
            return var1 >= 1.0 ? this.k : RibbonCapacity.DAsVec3d(this.Z, this.V, this.k, this.W, var1, this.D, this.f, this.e);
         }
      }
   }

   public static final class Type_v {
      final Vec3d[] W;
      final Vec3d[] Q;
      final double[] D;
      final int[] O;
      int B;
      double i = Double.POSITIVE_INFINITY;

      public Type_v(int var1) {
         this.W = new Vec3d[var1];
         this.Q = new Vec3d[var1];
         this.D = new double[var1];
         this.O = new int[var1];
      }

      public void rAsVoid() {
         this.B = 0;
      }

      public int PAsInt() {
         return this.B;
      }

      public void bAsVoid(Vec3d var1, double var2, int var4) {
         if (this.B == this.W.length) {
            throw new IllegalStateException("Ribbon capacity exceeded");
         } else {
            this.W[this.B] = var1;
            this.D[this.B] = var2;
            this.O[this.B++] = var4;
         }
      }

      public void zAsVoid() {
         if (this.B >= 2) {
            double var1 = 0.0;

            for (int var3 = 1; var3 < this.B; var3++) {
               var1 += this.W[var3 - 1].distanceTo(this.W[var3]);
            }

            if (!(var1 < 1.0E-8)) {
               double var10 = var1 * 0.18;
               double var5 = 0.0;
               this.D[0] = 0.0;
               this.O[0] = this.O[0] & 16777215;

               for (int var7 = 1; var7 < this.B; var7++) {
                  var5 += this.W[var7 - 1].distanceTo(this.W[var7]);
                  if (var5 >= var10) {
                     break;
                  }

                  double var8 = FadeCurve.RAsDouble(var5 / var10);
                  this.D[var7] = this.D[var7] * var8;
                  this.O[var7] = ColorUtil.pAsInt(this.O[var7], var8);
               }
            }
         }
      }

      public void aAsVoid(Vec3d var1) {
         if (this.B >= 2) {
            Vec3d var2 = oAsVec3d(this.W[0], this.W[1], var1, new Vec3d(1.0, 0.0, 0.0));
            this.Q[0] = var2.multiply(this.D[0] * 0.5);

            for (int var3 = 1; var3 < this.B; var3++) {
               Vec3d var4 = var3 + 1 < this.B ? oAsVec3d(this.W[var3], this.W[var3 + 1], var1, var2) : var2;
               if (var4.dotProduct(var2) < 0.0) {
                  var4 = var4.multiply(-1.0);
               }

               Vec3d var5 = var2.add(var4);
               Vec3d var6 = var5.lengthSquared() < 1.0E-12 ? var2 : var5.normalize();
               double var7 = Math.min(2.0, 1.0 / Math.max(0.5, Math.abs(var6.dotProduct(var4))));
               this.Q[var3] = var6.multiply(this.D[var3] * 0.5 * var7);
               var2 = var4;
            }
         }
      }

      public Vec3d iAsVec3d(int var1) {
         return this.Q[var1];
      }

      private static Vec3d oAsVec3d(Vec3d var0, Vec3d var1, Vec3d var2, Vec3d var3) {
         Vec3d var4 = var1.subtract(var0);
         Vec3d var5 = var4.crossProduct(var2.subtract(var0));
         if (var5.lengthSquared() >= 1.0E-12) {
            return var5.normalize();
         } else if (var4.lengthSquared() < 1.0E-12) {
            return var3;
         } else {
            Vec3d var6 = var4.normalize();
            Vec3d var7 = var3.subtract(var6.multiply(var3.dotProduct(var6)));
            if (var7.lengthSquared() >= 1.0E-12) {
               return var7.normalize();
            } else {
               Vec3d var8 = Math.abs(var6.y) < 0.9 ? new Vec3d(0.0, 1.0, 0.0) : new Vec3d(0.0, 0.0, 1.0);
               return var6.crossProduct(var8).normalize();
            }
         }
      }
   }
}
