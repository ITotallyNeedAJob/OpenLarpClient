package com.larpclient;

public final class pq {
   private double u = Double.NaN;
   private double s;
   private double D;

   public pq() {
   }

   public double uAsDouble(double var1, double var3) {
      if (Double.isFinite(this.u)) {
         this.D = this.D + Math.max(0.0, var1 - this.u) * this.s * Math.PI * 2.0;
      }

      this.u = var1;
      this.s = var3;
      return this.D;
   }

   public void nAsVoid() {
      this.u = Double.NaN;
      this.D = 0.0;
      this.s = 0.0;
   }
}
