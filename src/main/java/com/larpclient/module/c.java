package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public final class c implements h {
   private final SecureRandom X = new SecureRandom();
   private float D;
   private float u;
   private float p = 0.0F;
   private float T = 0.0F;

   public c() {
   }

   @Override
   public RotationAngles rotate(RotationAngles var1, RotationAngles var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         this.D += 0.065F;
         this.u += 0.045F;
         float var6 = (float)(Math.sin((double)this.D) * 0.8F + Math.cos((double)this.D * 0.5) * 0.4F);
         float var7 = (float)(Math.cos((double)this.u) * 0.5);
         float var8 = lk.aAsFloat(var1.n, var2.n + var6);
         float var9 = MathHelper.clamp(var2.m + var7, -90.0F, 90.0F);
         float var10 = var8 - var1.n;
         float var11 = var9 - var1.m;
         float var12 = (float)Math.hypot((double)var10, (double)var11);
         float var13 = var5 ? this.GAsFloat(40.0F, 60.0F) : this.GAsFloat(55.0F, 85.0F);
         float var14 = var5 ? this.GAsFloat(25.0F, 40.0F) : this.GAsFloat(35.0F, 55.0F);
         this.p = MathHelper.lerp(0.3F, this.p, var13);
         this.T = MathHelper.lerp(0.3F, this.T, var14);
         float var15 = MathHelper.clamp(var10, -this.p, this.p);
         float var16 = MathHelper.clamp(var11, -this.T, this.T);
         if (var12 < 6.0F) {
            float var17 = Math.max(0.4F, var12 / 6.0F);
            var15 *= var17;
            var16 *= var17;
         }

         return new RotationAngles(var1.n + var15, MathHelper.clamp(var1.m + var16, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float GAsFloat(float var1, float var2) {
      return var1 + this.X.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.D = this.u = this.p = this.T = 0.0F;
   }
}
