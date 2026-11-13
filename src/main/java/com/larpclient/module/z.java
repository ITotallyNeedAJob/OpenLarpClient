package com.larpclient.module;

import net.minecraft.util.math.MathHelper;

public final class z {
   private float F;
   private float B;
   private float G = Float.NaN;

   public z() {
   }

   public RotationAngles MAsL6(RotationAngles var1, RotationAngles var2) {
      if (var1 != null && var2 != null) {
         float var3 = lk.aAsFloat(var1.n, var2.n) - var1.n;
         float var4 = var2.m - var1.m;
         float var5 = lk.TAsFloat();
         if (Float.isFinite(var5) && !(var5 <= 1.0E-5F)) {
            if (!Float.isFinite(this.G) || Math.abs(this.G - var5) > 1.0E-5F) {
               this.F = 0.0F;
               this.B = 0.0F;
               this.G = var5;
            }

            float var6 = this.VAsFloat(var3, var5);
            float var7 = this.AAsFloat(var4, var5);
            float var8 = var1.m + var7;
            float var9 = MathHelper.clamp(var8, -90.0F, 90.0F);
            if (var9 != var8) {
               this.B = 0.0F;
            }

            return new RotationAngles(var1.n + var6, var9);
         } else {
            this.vAsVoid();
            return new RotationAngles(var1.n + qAsFloat(var3), MathHelper.clamp(var1.m + qAsFloat(var4), -90.0F, 90.0F));
         }
      } else {
         return var2 != null ? var2 : var1;
      }
   }

   private float VAsFloat(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.F = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.F;
         float var4 = (float)Math.round(var3 / var2) * var2;
         this.F = var3 - var4;
         return var4;
      }
   }

   private float AAsFloat(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.B = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.B;
         float var4 = (float)Math.round(var3 / var2) * var2;
         this.B = var3 - var4;
         return var4;
      }
   }

   private static float qAsFloat(float var0) {
      return Float.isFinite(var0) ? var0 : 0.0F;
   }

   public void vAsVoid() {
      this.F = 0.0F;
      this.B = 0.0F;
      this.G = Float.NaN;
   }
}
