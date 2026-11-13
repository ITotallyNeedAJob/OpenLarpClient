package com.larpclient;

import java.util.Optional;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class o5 {
   public static Optional<Vec3d> iAsOptional(Vec3d var0, Vec3d var1, Box var2, double var3) {
      if (Double.isFinite(var3) && !(var3 < 0.0)) {
         return var2.contains(var0) ? Optional.of(var0) : var2.raycast(var0, var0.add(var1.normalize().multiply(var3)));
      } else {
         return Optional.empty();
      }
   }

   public static double lAsDouble(Vec3d var0, Box var1) {
      return var0.distanceTo(
         new Vec3d(Math.clamp(var0.x, var1.minX, var1.maxX), Math.clamp(var0.y, var1.minY, var1.maxY), Math.clamp(var0.z, var1.minZ, var1.maxZ))
      );
   }

   public static boolean EAsBoolean(Vec3d var0, Vec3d var1, Box var2) {
      double var3 = var0.squaredDistanceTo(var1);
      if (var2.contains(var0)) {
         return var3 > 1.0E-7;
      } else {
         Optional var5 = var2.raycast(var0, var1);
         return var5.isPresent() && var0.squaredDistanceTo((Vec3d)var5.get()) + 1.0E-7 < var3;
      }
   }

   private o5() {
   }
}
