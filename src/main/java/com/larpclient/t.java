package com.larpclient;

import net.minecraft.util.math.MathHelper;

public final class t {
   private long W = Long.MIN_VALUE;
   private int I;

   public t() {
   }

   public static float[] JAsFloatArray(float var0, float var1, float var2, float var3, double var4) {
      float var6 = MathHelper.wrapDegrees(var2 - var0);
      float var7 = MathHelper.clamp(var3, -90.0F, 90.0F) - var1;
      double var8 = Math.hypot((double)var6, (double)var7);
      double var10 = var8 == 0.0 ? 1.0 : Math.min(1.0, Math.max(1.0, var4) / var8);
      return new float[]{var0 + (float)((double)var6 * var10), MathHelper.clamp(var1 + (float)((double)var7 * var10), -90.0F, 90.0F)};
   }

   public void hAsVoid(long var1, boolean var3) {
      if (var1 != this.W) {
         this.I = var3 ? (this.W == var1 - 1L ? Math.min(5, this.I + 1) : 1) : 0;
         this.W = var1;
      }
   }

   public boolean EAsBoolean(long var1, int var3) {
      return var1 == this.W && this.I >= Math.max(1, var3);
   }

   public void VAsVoid() {
      this.W = Long.MIN_VALUE;
      this.I = 0;
   }
}
