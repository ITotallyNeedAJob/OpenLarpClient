package com.larpclient;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class BoxUtils {
   public static double NAsDouble(double var0) {
      double var2 = Math.clamp(var0 - 0.5, 0.0, 1.0);
      return var2 * var2 * (3.0 - 2.0 * var2);
   }

   public static float[] qAsFloatArray(Box var0, Vec3d var1, Vec3d var2, float var3, float var4) {
      if (var0.contains(var1)) {
         return new float[]{var3, var4};
      } else {
         float[] var5 = ModuleDetector.QAsFloatArray(var1, var2);
         if (Math.hypot(var2.x - var1.x, var2.z - var1.z) < 1.0E-6) {
            var5[0] = var3;
         }

         return var5;
      }
   }

   public static float[] yAsFloatArray(Box var0, Vec3d var1, Vec3d var2, Box var3, Vec3d var4, float var5, float var6, double var7) {
      float[] var9 = qAsFloatArray(var0, var1, var2, var5, var6);
      if (var0.contains(var1)) {
         return var9;
      } else {
         float var10 = var5;
         float var11 = var6;
         if (var3 != null
            && var4 != null
            && !var3.contains(var4)
            && var1.squaredDistanceTo(var4) <= 1.0
            && var0.getCenter().squaredDistanceTo(var3.getCenter()) <= 1.0) {
            Vec3d var12 = gAsVec3d(var3, sAsVec3d(var0, var2));
            float[] var13 = ModuleDetector.QAsFloatArray(var4, var12);
            float var14 = MathHelper.wrapDegrees(var9[0] - var13[0]);
            float var15 = var9[1] - var13[1];
            if (Math.abs(var14) <= 60.0F && Math.abs(var15) <= 60.0F) {
               var10 = var5 + var14;
               var11 = var6 + var15;
            }
         }

         return ModuleDetector.kAsFloatArray(var10, var11, var9[0], var9[1], var7);
      }
   }

   public static Vec3d vAsVec3d(Box var0, Vec3d var1) {
      double var2 = Math.min(0.001, Math.min(var0.getLengthX(), Math.min(var0.getLengthY(), var0.getLengthZ())) / 4.0);
      return new Vec3d(
         Math.clamp(var1.x, var0.minX + var2, var0.maxX - var2),
         Math.clamp(var1.y, var0.minY + var2, var0.maxY - var2),
         Math.clamp(var1.z, var0.minZ + var2, var0.maxZ - var2)
      );
   }

   public static Vec3d sAsVec3d(Box var0, Vec3d var1) {
      return new Vec3d(
         (var1.x - var0.minX) / Math.max(0.001, var0.getLengthX()),
         (var1.y - var0.minY) / Math.max(0.001, var0.getLengthY()),
         (var1.z - var0.minZ) / Math.max(0.001, var0.getLengthZ())
      );
   }

   public static Vec3d gAsVec3d(Box var0, Vec3d var1) {
      return vAsVec3d(var0, new Vec3d(var0.minX + var1.x * var0.getLengthX(), var0.minY + var1.y * var0.getLengthY(), var0.minZ + var1.z * var0.getLengthZ()));
   }

   public static Optional<Vec3d> bAsOptional(Box var0, Vec3d var1, Vec3d var2, Vec3d var3, Predicate<Vec3d> var4) {
      Vec3d var5 = vAsVec3d(var0, var2);
      if (var4.test(var5)) {
         return Optional.of(var5);
      } else {
         if (var3 != null) {
            var5 = gAsVec3d(var0, var3);
            if (var4.test(var5)) {
               return Optional.of(var5);
            }
         }

         var5 = vAsVec3d(var0, var1);
         if (var4.test(var5)) {
            return Optional.of(var5);
         } else {
            for (double var9 : new double[]{0.5, 0.9, 0.1}) {
               for (double[] var14 : new double[][]{{0.5, 0.5}, {0.1, 0.1}, {0.1, 0.9}, {0.9, 0.1}, {0.9, 0.9}}) {
                  var5 = gAsVec3d(var0, new Vec3d(var14[0], var9, var14[1]));
                  if (var4.test(var5)) {
                     return Optional.of(var5);
                  }
               }
            }

            return Optional.empty();
         }
      }
   }

   public static Vec3d FAsVec3d(Box var0, Vec3d var1, Vec3d var2, double var3, Predicate<Vec3d> var5) {
      Vec3d var6 = new Vec3d(var2.x, 0.0, var2.z).multiply(var3);
      if (var6.lengthSquared() > 0.25) {
         var6 = var6.normalize().multiply(0.5);
      }

      Vec3d var7 = vAsVec3d(var0, var1.add(var6));
      return var5.test(var7) ? var7 : var1;
   }

   private BoxUtils() {
   }
}
