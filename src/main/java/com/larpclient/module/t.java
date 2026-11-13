package com.larpclient.module;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public final class t implements h {
   private final Random U = new Random();
   private final float[] M = new float[30];
   private int u;
   private int T;
   private int n;

   public t() {
      this.reset();
   }

   @Override
   public RotationAngles rotate(RotationAngles var1, RotationAngles var2, LivingEntity var3, boolean var4, boolean var5) {
      if (var1 != null && var2 != null) {
         MinecraftClient var6 = MinecraftClient.getInstance();
         System.arraycopy(this.M, 0, this.M, 1, 29);
         this.M[0] = var2.m;
         this.u++;
         if (var4) {
            this.n = 2;
         } else if (this.n > 0) {
            this.n--;
         }

         float var7 = (float)(var6.player != null ? var6.player.age : 0) + var6.getRenderTickCounter().getTickProgress(false);
         float var8 = (float)(Math.sin((double)var7 * 0.4000000008323731) * 3.0 + Math.sin((double)var7 * 0.9500002390239708 + 1.4000004888461306) * 2.0);
         float var9 = (float)(Math.cos((double)(var7 * 0.5F) + 0.7000001555309916) * 0.5 + Math.cos((double)var7 * 0.7800000620494261 + 3.10000031689524) * 1.5);
         int var10 = MathHelper.clamp(10 - this.u, 0, 29);
         float var11 = this.M[var10] + var9 * 1.5F;
         float var12 = this.UAsFloat(var1.m, var11, this.UAsFloat(0.1F, 0.5F));
         float var13 = this.UAsFloat(var1.n, var2.n + var8, this.UAsFloat(0.1F, 0.4F));
         if (var4 || this.n > 0) {
            if (!var5) {
               var13 = var2.n;
               var12 = var2.m;
            }

            var8 = MathHelper.clamp(var8, -0.05F, 0.05F);
            var9 = MathHelper.clamp(var9, -0.05F, 0.05F);
         }

         if (this.u <= 4 && this.T % 2 == 0 && var6.player != null && !var4) {
            var13 = Aura.FAsFloat();
         }

         if (var4) {
            this.T++;
            this.u = 0;
         }

         return new RotationAngles(var13 + var8, MathHelper.clamp(var12 + var9, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   private float UAsFloat(float var1, float var2, float var3) {
      float var4 = MathHelper.clamp(var3, 0.0F, 1.0F);
      float var5 = lk.WAsFloat(var1, var2);
      if (Math.abs(var5) < 0.5F) {
         return var2;
      } else {
         float var6 = lk.tAsFloat(var1, var1 + var5 * var4);
         return Math.abs(lk.WAsFloat(var6, var2)) < 0.5F ? var2 : var6;
      }
   }

   private float UAsFloat(float var1, float var2) {
      return var1 + this.U.nextFloat() * (var2 - var1);
   }

   @Override
   public int ticksToReach(RotationAngles var1, RotationAngles var2) {
      if (var1 != null && var2 != null) {
         float var3 = Math.abs(lk.WAsFloat(var1.n, var2.n));
         float var4 = Math.abs(var2.m - var1.m);
         return MathHelper.clamp((int)Math.ceil((double)Math.max(var3 / 45.0F, var4 / 25.0F)), 0, 8);
      } else {
         return 0;
      }
   }

   @Override
   public float yawSpeed() {
      return 45.0F;
   }

   @Override
   public float pitchSpeed() {
      return 25.0F;
   }

   @Override
   public void reset() {
      Arrays.fill(this.M, MAsBoolean() ? Aura.KAsFloat() : 0.0F);
      this.u = 0;
      this.T = 0;
      this.n = 0;
   }

   private static boolean MAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      return var0 != null && var0.player != null;
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      Arrays.fill(this.M, MAsBoolean() ? Aura.KAsFloat() : 0.0F);
      this.u = 0;
   }
}
