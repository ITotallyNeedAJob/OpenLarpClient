package com.larpclient;

import java.util.List;
import java.util.Optional;
import net.minecraft.item.BowItem;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class wu {
   public static final double C = 4.0;
   public static final double m = 1.0;
   public static final double Q = 0.1;

   public static double BAsDouble(int var0) {
      return (double)(BowItem.getPullProgress(Math.max(0, var0)) * 3.0F);
   }

   public static double HAsDouble(double var0, double var2) {
      return Double.isFinite(var0) && Double.isFinite(var2) ? 4.0 + 1.0 * Math.clamp(var2, 0.0, 1.0) * 1.5 * Math.min(Math.max(0.0, var0), 5.0) : 4.0;
   }

   public static double[] tAsDoubleArray2(double var0) {
      return new double[]{HAsDouble(var0, 0.0), HAsDouble(var0, 0.5), HAsDouble(var0, 1.0)};
   }

   public static double jAsDouble(float[] var0) {
      return (double)((var0[0] + 4.0F * var0[1] + var0[2]) / 6.0F);
   }

   public static boolean IAsBoolean(Vec3d var0, Vec3d var1, Box var2, List<Box> var3) {
      if (var2.contains(var0)) {
         return false;
      } else {
         Optional var4 = var2.raycast(var0, var1);
         if (var4.isEmpty()) {
            return false;
         } else {
            double var5 = var0.squaredDistanceTo((Vec3d)var4.get());

            for (Box var8 : var3) {
               if (var8.contains(var0)) {
                  return false;
               }

               Optional var9 = var8.raycast(var0, var1);
               if (var9.isPresent() && var0.squaredDistanceTo((Vec3d)var9.get()) <= var5) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private wu() {
   }
}
