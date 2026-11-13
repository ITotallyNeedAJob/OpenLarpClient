package com.larpclient;

public final class rp {
   private double S;
   private double Q;
   private long d;

   public rp() {
   }

   public void SAsVoid(double var1, double var3, long var5, boolean var7) {
      if (!var7) {
         this.S = this.Q = 0.0;
         this.d = var5;
      } else {
         double var8 = this.d != 0L && var5 > this.d && var5 - this.d <= 500000000L ? (double)(var5 - this.d) / 1.0E9 : 0.0;
         this.d = var5;
         double var10 = 1.0 - Math.exp(-Math.min(0.1, var8) / 0.16);
         this.S = this.S + (TAsDouble(var1) - this.S) * var10;
         this.Q = this.Q + (TAsDouble(var3) - this.Q) * var10;
      }
   }

   private static double TAsDouble(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, -1.0, 1.0) : 0.0;
   }

   public double VAsDouble() {
      return this.S;
   }

   public double mAsDouble() {
      return this.Q;
   }
}
