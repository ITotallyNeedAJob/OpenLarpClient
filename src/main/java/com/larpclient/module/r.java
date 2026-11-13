package com.larpclient.module;

import java.security.SecureRandom;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public final class r implements h {
   private final SecureRandom q = new SecureRandom();
   private int l;
   private int V;
   private float d;
   private float I;

   public r() {
   }

   @Override
   public RotationAngles rotate(RotationAngles var1, RotationAngles var2, LivingEntity var3, boolean var4, boolean var5) {
      this.l++;
      this.V++;
      float var6 = lk.WAsFloat(var1.n, var2.n);
      float var7 = var2.m - var1.m;
      if (this.l % 5 == 0) {
         float var8 = var5 ? this.zAsFloat(-1.0F, 1.0F) : this.zAsFloat(-3.0F, 3.0F);
         float var9 = var5 ? this.zAsFloat(-0.5F, 0.5F) : this.zAsFloat(-1.5F, 1.5F);
         if (Math.abs(var6) < 8.0F && Math.abs(var7) < 6.0F) {
            var8 *= 0.2F;
            var9 *= 0.2F;
         }

         float var10 = this.V > 20 ? 0.45F : 0.25F;
         this.d = MathHelper.lerp(var10, this.d, var8);
         this.I = MathHelper.lerp(var10, this.I, var9);
      }

      float var12 = var4 ? this.zAsFloat(80.0F, 130.0F) : this.zAsFloat(50.0F, 80.0F);
      float var13 = var4 ? this.zAsFloat(50.0F, 80.0F) : this.zAsFloat(30.0F, 50.0F);
      float var14 = MathHelper.clamp(var6 + this.d, -var12, var12);
      float var11 = MathHelper.clamp(var7 + this.I, -var13, var13);
      return new RotationAngles(var1.n + var14, MathHelper.clamp(var1.m + var11, -90.0F, 90.0F));
   }

   private float zAsFloat(float var1, float var2) {
      return var1 + this.q.nextFloat() * (var2 - var1);
   }

   @Override
   public void reset() {
      this.l = 0;
      this.V = 0;
      this.d = 0.0F;
      this.I = 0.0F;
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      this.V = 0;
   }
}
