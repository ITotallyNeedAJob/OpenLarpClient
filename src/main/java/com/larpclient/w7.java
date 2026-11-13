package com.larpclient;

import java.util.List;
import java.util.Optional;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class w7 {
   public static final long S = 1500L;
   public static final long z = 150L;
   public static final double t = 0.5;
   public static final double K = 1.3;
   public static final double o = 0.05;

   public static boolean wAsBoolean(double var0, double var2, double var4, double var6) {
      return Double.isFinite(var0) && Double.isFinite(var2) && var0 >= 0.0 && var2 >= 0.0 && var0 <= var4 && var2 <= var6;
   }

   public static <T> T aAsObject(List<w7.Type_c<T>> var0, T var1, long var2, T var4, double var5, double var7) {
      double var9 = var7 / 2.0;

      for (w7.Type_c var12 : var0) {
         if (var12.entity == var1 && var2 >= 0L && var2 <= 1500L && wAsBoolean(var12.distance, var12.angle, var5, var9)) {
            return var1;
         }
      }

      for (w7.Type_c var18 : var0) {
         if (var18.entity == var4 && wAsBoolean(var18.distance, var18.angle, var5 + 0.5, var9 * 1.3)) {
            return var4;
         }
      }

      Object var17 = null;
      double var19 = var9;

      for (w7.Type_c var15 : var0) {
         if (wAsBoolean(var15.distance, var15.angle, var5, var19)) {
            var17 = var15.entity;
            var19 = var15.angle;
         }
      }

      return (T)var17;
   }

   public static Box YAsBox(Box var0) {
      return var0.contract(var0.getLengthX() * 0.25, var0.getLengthY() * 0.1, var0.getLengthX() * 0.25);
   }

   public static Vec3d gAsVec3d(Vec3d var0, Box var1) {
      return new Vec3d(Math.clamp(var0.x, var1.minX, var1.maxX), Math.clamp(var0.y, var1.minY, var1.maxY), Math.clamp(var0.z, var1.minZ, var1.maxZ));
   }

   public static float[] kAsFloatArray(float var0, float var1, double var2, double var4) {
      return new float[]{var0 + (float)var2 * 0.15F, Math.clamp(var1 + (float)var4 * 0.15F, -90.0F, 90.0F)};
   }

   public static Vec3d JAsVec3d(Vec3d var0, Vec3d var1, Box var2, double var3) {
      if (var2.contains(var0)) {
         return var0;
      } else {
         Optional<Vec3d> var5 = var2.raycast(var0, var0.add(var1.multiply(var3 + 1.0)));
         return var5.orElseGet(() -> gAsVec3d(var0.add(var1.multiply(var0.distanceTo(var2.getCenter()))), var2));
      }
   }

   public static float[] AAsFloatArray(Vec3d var0, Vec3d var1, Box var2, double var3) {
      if (!var2.contains(var0) && !var2.raycast(var0, var0.add(var1.multiply(var3 + 1.0))).isPresent()) {
         Vec3d var5 = JAsVec3d(var0, var1, var2, var3).subtract(var0);
         return new float[]{
            (float)l_.iAsDouble(Math.toDegrees(Math.atan2(var5.z, var5.x)) - 90.0),
            (float)Math.clamp(-Math.toDegrees(Math.atan2(var5.y, Math.hypot(var5.x, var5.z))), -90.0, 90.0)
         };
      } else {
         return null;
      }
   }

   public static double oAsDouble(float var0, float var1, float[] var2) {
      return var2 == null ? 0.0 : Math.hypot(l_.iAsDouble((double)(var2[0] - var0)), (double)(var2[1] - var1));
   }

   public static double KAsDouble(double var0, boolean var2, boolean var3) {
      double var4 = var0 * 0.6F + 0.2F;
      return var4 * var4 * var4 * (double)(var2 && var3 ? 1 : 8);
   }

   private w7() {
   }

   public static record Type_c<T>(T entity, double distance, double angle) {
   }

   public static record Type_h(double yaw, double pitch) {
      public static final w7.Type_h STILL = new w7.Type_h(0.0, 0.0);
   }

   public static final class Type_p {
      private double G;
      private double s;
      private double y;
      private double j;
      private long p;
      private long k;
      private long h;
      private boolean T;

      public Type_p() {
      }

      public w7.Type_h iAsW7_Type_h(
         double var1, double var3, double var5, double var7, boolean var9, boolean var10, double var11, double var13, double var15, double var17, long var19
      ) {
         if (Double.isFinite(var1)
            && Double.isFinite(var3)
            && Double.isFinite(var11)
            && !(var11 < 0.0)
            && Double.isFinite(var17)
            && !(var17 <= 0.0)
            && Double.isFinite(var5)
            && Double.isFinite(var7)
            && Double.isFinite(var13)
            && !(var13 <= 0.0)
            && Double.isFinite(var15)
            && !(var15 <= 0.0)) {
            if (var1 != 0.0 || var3 != 0.0) {
               this.p = var19;
               this.T = true;
            }

            if (var1 != 0.0) {
               this.y = Math.signum(var1);
               this.k = var19;
            } else if (this.y != 0.0 && (var19 < this.k || var19 - this.k > 150L)) {
               this.y = 0.0;
               this.G = 0.0;
            }

            if (var3 != 0.0) {
               this.j = Math.signum(var3);
               this.h = var19;
            } else if (this.j != 0.0 && (var19 < this.h || var19 - this.h > 150L)) {
               this.j = 0.0;
               this.s = 0.0;
            }

            if (var9 && this.T && var19 >= this.p && var19 - this.p <= 150L) {
               double var21 = Math.min(var11, 0.05);
               double var23 = var13 * var15 * var21;
               double var25 = -Math.expm1(-12.0 * var21);
               double var27 = (double)((float)var17 * 0.15F);
               double var29 = l_.iAsDouble(var5);
               if (this.y * var29 < 0.0) {
                  var29 = 0.0;
               }

               double var31 = var10 && this.j * var7 >= 0.0 ? var7 : 0.0;
               if (var29 == 0.0 || this.G * var29 < 0.0) {
                  this.G = 0.0;
               }

               if (var31 == 0.0 || this.s * var31 < 0.0) {
                  this.s = 0.0;
               }

               this.G = this.G + Math.clamp(var29 * var25, -var23, var23) / var27;
               this.s = this.s + Math.clamp(var31 * var25 * 0.6, -var23, var23) / var27;
               long var33 = (long)this.G;
               long var35 = (long)this.s;
               this.G -= (double)var33;
               this.s -= (double)var35;
               return new w7.Type_h((double)var33 * var17, (double)var35 * var17);
            } else {
               this.sAsVoid();
               this.y = this.j = 0.0;
               return w7.Type_h.STILL;
            }
         } else {
            this.BAsVoid();
            return w7.Type_h.STILL;
         }
      }

      public void sAsVoid() {
         this.G = this.s = 0.0;
      }

      public void BAsVoid() {
         this.sAsVoid();
         this.y = this.j = 0.0;
         this.p = 0L;
         this.k = this.h = 0L;
         this.T = false;
      }

      public double NAsDouble() {
         return this.G;
      }

      public double yAsDouble() {
         return this.s;
      }

      public boolean qAsBoolean(long var1) {
         return this.T && var1 >= this.p && var1 - this.p <= 150L;
      }

      public boolean DAsBoolean(double var1, double var3, boolean var5) {
         int var6 = var1 != 0.0 ? 1 : 0;
         int var7 = var5 && var3 != 0.0 ? 1 : 0;
         return (var6 != 0 || var7 != 0) && (var6 == 0 || !(this.y * var1 >= 0.0)) && (var7 == 0 || !(this.j * var3 >= 0.0));
      }
   }
}
