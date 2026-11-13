package com.larpclient.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public final class v {
   private static final long J = 350L;
   private int K;
   private int e = 40;
   private int r = 35;
   private boolean s;
   private long n;
   private int u;
   private float Y;
   private float b;

   public v() {
   }

   public void AAsVoid() {
      this.K++;
   }

   public void GAsVoid(int var1) {
      this.e = Math.max(10, var1);
      if (this.r == 35 || this.r < 10) {
         this.TAsVoid();
      }
   }

   private void TAsVoid() {
      int var1 = Math.max(2, this.e / 5);
      this.r = this.e + (int)((Math.random() - 0.5) * 2.0 * (double)var1);
   }

   public void yAsVoid() {
      if (!this.s && this.K >= this.r) {
         this.s = true;
         this.n = System.currentTimeMillis();
         this.K = 0;
         this.u = 0;
         this.TAsVoid();
         float var1 = Math.random() < 0.5 ? -1.0F : 1.0F;
         float var2 = Math.random() < 0.5 ? -1.0F : 1.0F;
         this.Y = var1 * (2.2F + (float)(Math.random() * 2.3));
         this.b = var2 * (1.2F + (float)(Math.random() * 1.8));
      }

      if (this.s) {
         long var5 = System.currentTimeMillis() - this.n;
         MinecraftClient var3 = MinecraftClient.getInstance();
         ClientPlayerEntity var4 = var3 == null ? null : var3.player;
         if (var4 != null && this.u == 0 && var5 >= 60L) {
            var4.swingHand(Hand.MAIN_HAND);
            this.u++;
         }

         if (var5 >= 350L) {
            this.s = false;
            this.Y = 0.0F;
            this.b = 0.0F;
         }
      }
   }

   public RotationAngles SAsL6(RotationAngles var1) {
      if (var1 != null && this.s) {
         long var2 = System.currentTimeMillis() - this.n;
         float var4 = Math.min(1.0F, (float)var2 / 350.0F);
         float var5 = (float)Math.sin((double)var4 * Math.PI);
         return new RotationAngles(var1.n + this.Y * var5, MathHelper.clamp(var1.m + this.b * var5, -90.0F, 90.0F));
      } else {
         return var1;
      }
   }

   public void oAsVoid() {
      this.K = 0;
      this.s = false;
      this.u = 0;
      this.Y = 0.0F;
      this.b = 0.0F;
      this.TAsVoid();
   }

   public boolean UAsBoolean() {
      return this.s;
   }
}
