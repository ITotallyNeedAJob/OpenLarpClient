package com.larpclient;

public final class AngleMath {
   public static final double Q = 0.05;
   public static final double S = 0.35;
   private static final double B = 720.0;

   public static double iAsDouble(double var0) {
      return var0 - Math.floor((var0 + 180.0) / 360.0) * 360.0;
   }

   public static double ZAsDouble(double var0, double var2, boolean var4) {
      return Math.hypot(iAsDouble(var0), var4 ? var2 : 0.0);
   }

   public static boolean RAsBoolean(double var0, double var2) {
      return Double.isFinite(var0) && var0 <= Math.clamp(var2, 0.0, 180.0) / 2.0;
   }

   public static boolean XAsBoolean(double var0, int var2, double var3, int var5, boolean var6) {
      double var7 = var6 ? Math.max(2.0, var3 * 0.15) : 0.0;
      return var0 < var3 - var7 - 1.0E-7 || !var6 && Math.abs(var0 - var3) <= 1.0E-7 && var2 < var5;
   }

   public static boolean EAsBoolean(double var0, double var2, double var4, double var6, boolean var8) {
      return var0 * var4 < -1.0E-5 || var8 && var2 * var6 < -1.0E-5;
   }

   private AngleMath() {
   }

   public static final class Type_u {
      private double S;
      private double l;

      public Type_u() {
      }

      public double HAsDouble(double var1, double var3, double var5) {
         if (Double.isFinite(var1) && Double.isFinite(var3) && Double.isFinite(var5) && !(var3 <= 0.0) && !(var5 <= 0.0)) {
            double var7 = Math.min(var3, 0.05);
            if (Math.abs(var1) <= 0.35) {
               this.MAsVoid();
               return 0.0;
            } else {
               double var9 = Math.clamp(var1 * 8.0, -var5, var5);
               if (this.S * var1 < 0.0) {
                  this.S = 0.0;
               }

               this.S = Math.clamp(this.S, -var5, var5);
               double var11 = this.S;
               this.S = this.S + Math.clamp(var9 - this.S, -720.0 * var7, 720.0 * var7);
               double var13 = (var11 + this.S) * 0.5 * var7;
               return Math.copySign(Math.min(Math.min(Math.abs(var13), var5 * var7), Math.max(0.0, Math.abs(var1) - 0.35)), var1);
            }
         } else {
            this.MAsVoid();
            return 0.0;
         }
      }

      public float uAsFloat(float var1, double var2, boolean var4) {
         double var5 = (double)var1 + var2 + this.l;
         if (var4) {
            var5 = Math.clamp(var5, -90.0, 90.0);
         }

         float var7 = (float)var5;
         this.l = var5 - (double)var7;
         return var7;
      }

      public void MAsVoid() {
         this.S = this.l = 0.0;
      }

      public double mAsDouble() {
         return this.S;
      }
   }

   public static final class Type_z {
      private long w;
      private boolean S;

      public Type_z() {
      }

      public double qAsDouble(long var1) {
         long var3 = this.w == 0L ? 0L : var1 - this.w;
         this.S = this.w != 0L && (var3 <= 0L || var3 > 250000000L);
         this.w = var1;
         return var3 <= 0L ? 0.0 : Math.min((double)var3 / 1.0E9, 0.05);
      }

      public boolean mAsBoolean() {
         return this.S;
      }

      public void fAsVoid() {
         this.w = 0L;
         this.S = false;
      }
   }
}
