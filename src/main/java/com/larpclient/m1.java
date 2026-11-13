package com.larpclient;

import net.minecraft.util.math.MathHelper;

public final class m1 {
   private float b;
   private float Y;
   private int T = Integer.MIN_VALUE;
   private float[] I;

   public m1() {
   }

   public void aAsVoid() {
      this.b = this.Y = 0.0F;
      this.T = Integer.MIN_VALUE;
      this.I = null;
   }

   public float[] DAsFloatArray(int var1, float var2, float var3, float var4, float var5, double var6) {
      if (var1 == this.T && this.I != null) {
         return (float[])this.I.clone();
      } else {
         if (var1 - this.T != 1) {
            this.b = this.Y = 0.0F;
         }

         this.T = var1;
         float var8 = (float)Math.clamp(var6, 30.0, 360.0) / 20.0F;
         float var9 = var8 / 4.0F;
         float var10 = MathHelper.wrapDegrees(var4 - var2);
         float var11 = MathHelper.clamp(var5, -90.0F, 90.0F) - var3;
         double var12 = Math.hypot((double)var10, (double)var11);
         double var14 = Math.sqrt((double)(var9 * var9) + (double)(2.0F * var9) * var12) - (double)var9;
         double var16 = Math.min((double)var8, var14);
         float var18 = var12 < 1.0E-9 ? 0.0F : (float)(var16 / var12);
         this.b = clamp(this.b, var10 * var18, var9);
         this.Y = clamp(this.Y, var11 * var18, var9);
         double var19 = Math.hypot((double)this.b, (double)this.Y);
         if (var19 > (double)var8) {
            this.b = (float)((double)this.b * ((double)var8 / var19));
            this.Y = (float)((double)this.Y * ((double)var8 / var19));
         }

         float var21 = iAsFloat(var10, this.b);
         float var22 = iAsFloat(var11, this.Y);
         if (var21 != this.b) {
            this.b = var21;
         }

         if (var22 != this.Y) {
            this.Y = var22;
         }

         this.I = new float[]{var2 + var21, MathHelper.clamp(var3 + var22, -90.0F, 90.0F)};
         return (float[])this.I.clone();
      }
   }

   private static float clamp(float var0, float var1, float var2) {
      return var0 + MathHelper.clamp(var1 - var0, -var2, var2);
   }

   private static float iAsFloat(float var0, float var1) {
      if (var0 == 0.0F) {
         return 0.0F;
      } else {
         return Math.signum(var0) == Math.signum(var1) && Math.abs(var1) > Math.abs(var0) ? var0 : var1;
      }
   }
}
