package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public final class x implements h {
   private final SecureRandom E = new SecureRandom();
   private float j = 0.0F;
   private float R = 0.0F;

   public x() {
   }

   @Override
   public RotationAngles rotate(RotationAngles var1, RotationAngles var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         float var6 = lk.WAsFloat(var1.n, var2.n);
         float var7 = var2.m - var1.m;
         float var8 = (float)Math.hypot((double)var6, (double)var7);
         float var9;
         if (!var5) {
            var9 = this.AAsFloat(45.0F, 65.0F);
         } else if (var4) {
            var9 = this.AAsFloat(35.0F, 50.0F);
         } else {
            var9 = this.AAsFloat(28.0F, 42.0F);
         }

         this.j = MathHelper.lerp(0.35F, this.j, var9);
         this.R = MathHelper.lerp(0.3F, this.R, var9 * 0.75F);
         float var10 = MathHelper.clamp(var6, -this.j, this.j);
         float var11 = MathHelper.clamp(var7, -this.R, this.R);
         if (var8 < 8.0F) {
            float var12 = Math.max(0.45F, var8 / 8.0F);
            var10 *= var12;
            var11 *= var12;
         }

         return new RotationAngles(var1.n + var10, MathHelper.clamp(var1.m + var11, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float AAsFloat(float var1, float var2) {
      return var1 + this.E.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.j = 0.0F;
      this.R = 0.0F;
   }
}
