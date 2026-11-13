package com.larpclient.module;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class l {
   private l() {
   }

   public static Vec3d AAsVec3d(LivingEntity var0, double var1) {
      double var3 = MathHelper.clamp(var1, 0.0, 4.0);
      Vec3d var5 = var0.getEntityPos();
      if (var3 <= 1.0E-4F) {
         return var5;
      } else {
         Vec3d var6 = new Vec3d(var0.getX() - var0.lastX, var0.getY() - var0.lastY, var0.getZ() - var0.lastZ);
         Vec3d var7 = var6.multiply(0.7F).add(var0.getVelocity().multiply(0.3F));
         if (Double.isFinite(var7.x) && Double.isFinite(var7.y) && Double.isFinite(var7.z) && !(var7.lengthSquared() < 1.0E-6F)) {
            if (var0.isOnGround()) {
               var7 = new Vec3d(var7.x, 0.0, var7.z);
            }

            Vec3d var8 = var7.multiply(var3);
            if (var8.lengthSquared() > 36.0) {
               var8 = var8.normalize().multiply(6.0);
            }

            return var5.add(var8);
         } else {
            return var5;
         }
      }
   }
}
