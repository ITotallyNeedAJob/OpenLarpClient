package com.larpclient;

public final class mg {
   public static final double H = 16.0;
   private boolean K;
   private boolean g;
   private boolean G;
   private boolean s;
   private double Y = Double.NaN;
   private double D = 4.0;
   private double DropletTrail = 1.0;
   private long h;

   public mg() {
   }

   public void YAsVoid(boolean var1) {
      this.K = var1;
      this.g = var1;
      this.G = this.s = false;
      this.Y = Double.NaN;
      this.l = 1.0;
      this.h = 0L;
   }

   public void LAsVoid(boolean var1, boolean var2, boolean var3, double var4, boolean var6) {
      if (!var1) {
         this.YAsVoid(true);
      } else {
         double var7 = LAsDouble(var4);
         if (this.Y != var7) {
            this.Y = var7;
            this.D = var7;
         }

         if (!var2) {
            this.K = false;
         }

         if (this.K) {
            this.g = var2;
            this.s = false;
         } else {
            int var9 = var2 && !this.g ? 1 : 0;
            if (!var3 && var9 != 0) {
               this.G = !this.G;
            }

            boolean var10 = var3 ? var2 : this.G;
            if (!var10 && this.s && !var6) {
               this.D = this.Y;
            }

            this.s = var10;
            this.g = var2;
         }
      }
   }

   public boolean OAsBoolean(double var1) {
      if (this.s && Double.isFinite(var1) && var1 != 0.0) {
         this.D = LAsDouble(this.D * Math.pow(1.2, Math.clamp(var1, -8.0, 8.0)));
         return true;
      } else {
         return false;
      }
   }

   public double BAsDouble(long var1, double var3, boolean var5) {
      double var6 = this.s ? 1.0 / this.D : 1.0;
      if (!var5 || var3 <= 0.0 || !Double.isFinite(var3)) {
         this.l = var6;
      } else if (this.h != 0L && var1 > this.h) {
         double var8 = Math.min((double)(var1 - this.h) * 1.0E-9, 0.05);
         this.l = this.l + (var6 - this.l) * -Math.expm1(-4.605170185988092 * var8 / var3);
         if (Math.abs(var6 - this.l) < 1.0E-4) {
            this.l = var6;
         }
      }

      this.h = var1;
      return this.l;
   }

   public boolean uAsBoolean() {
      return this.s;
   }

   public double SAsDouble() {
      return this.l;
   }

   public double UAsDouble() {
      return this.D;
   }

   public static double LAsDouble(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, 1.0, 16.0) : 4.0;
   }

   public static float AAsFloat(float var0, double var1) {
      if (Float.isFinite(var0) && !(var0 <= 0.0F)) {
         double var3 = (double)Math.clamp(var0, 1.0F, 179.0F);
         double var5 = Double.isFinite(var1) ? Math.clamp(var1, 0.0625, 1.0) : 1.0;
         return var5 == 1.0 ? var0 : (float)Math.max(1.0, Math.toDegrees(2.0 * Math.atan(Math.tan(Math.toRadians(var3) * 0.5) * var5)));
      } else {
         return var0;
      }
   }
}
