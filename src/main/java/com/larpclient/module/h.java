package com.larpclient.module;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public interface h {
   RotationAngles rotate(RotationAngles var1, RotationAngles var2, LivingEntity var3, boolean var4, boolean var5);

   default int ticksToReach(RotationAngles var1, RotationAngles var2) {
      if (var1 != null && var2 != null) {
         float var3 = Math.abs(lk.WAsFloat(var1.n, var2.n));
         float var4 = Math.abs(var2.m - var1.m);
         float var5 = Math.max(1.0F, this.yawSpeed());
         float var6 = Math.max(1.0F, this.pitchSpeed());
         return MathHelper.clamp((int)Math.ceil((double)Math.max(var3 / var5, var4 / var6)), 0, 8);
      } else {
         return 0;
      }
   }

   default float yawSpeed() {
      return 30.0F;
   }

   default float pitchSpeed() {
      return 18.0F;
   }

   default void reset() {
   }

   default void targetChanged(LivingEntity var1) {
   }
}
