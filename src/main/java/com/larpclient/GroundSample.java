package com.larpclient;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public record GroundSample(Vec3d point, Vec3d normal, Vec3d ring, Vec3d ringNormal, double groundY) {
   public GroundSample(Vec3d var1, Vec3d var2, Vec3d var3, Vec3d var4) {
      this(var1, var2, var3, var4, Double.NaN);
   }

   public static GroundSample sample(Box var0, Vec3d var1, Vec3d var2, lq.Type_a var3) {
      Vec3d var4 = var0.getCenter();
      Vec3d var5 = var2.lengthSquared() < 1.0E-8 ? var4.subtract(var1).normalize() : var2.normalize();
      Vec3d var6 = var0.raycast(var1, var1.add(var5.multiply(64.0)))
         .orElseGet(
            () -> new Vec3d(Math.clamp(var1.x, var0.minX, var0.maxX), Math.clamp(var1.y, var0.minY, var0.maxY), Math.clamp(var1.z, var0.minZ, var0.maxZ))
         );
      Vec3d var7 = var6.subtract(var4).normalize();
      if (var7.lengthSquared() < 1.0E-8) {
         var7 = new Vec3d(0.0, 1.0, 0.0);
      }

      Vec3d var8 = new Vec3d(var4.x, var0.minY, var4.z);
      lq.Type_l var9 = var3.HAsLq_Type_l(var8.add(0.0, 0.25, 0.0), var8.add(0.0, -4.0, 0.0));
      return var9 != null && var9.normal().y > 0.5
         ? new GroundSample(var6.add(var7.multiply(0.025)), var7, var9.point().add(var9.normal().multiply(0.035)), var9.normal(), var9.point().y)
         : new GroundSample(var6.add(var7.multiply(0.025)), var7, var6.add(var7.multiply(0.04)), var7);
   }
}
