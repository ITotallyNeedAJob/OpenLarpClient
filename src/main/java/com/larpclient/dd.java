package com.larpclient;

import java.util.Random;

public final class dd {
   public static final int H = 160;
   private final dd.Type_r[] f = new dd.Type_r[160];
   private long N;
   private double B;

   public dd() {
      Random var1 = new Random(14044L);

      for (int var2 = 0; var2 < this.f.length; var2++) {
         this.f[var2] = new dd.Type_r(var1.nextDouble(), var1.nextDouble(), 0.25 + var1.nextDouble() * 0.75, var1.nextDouble() * Math.PI * 2.0);
      }
   }

   public void yAsVoid(long var1, boolean var3) {
      if (var3 && this.N != 0L && var1 > this.N && var1 - this.N <= 500000000L) {
         this.B = this.B + Math.min(0.1, (double)(var1 - this.N) / 1.0E9);
      }

      this.N = var1;
   }

   public double RAsDouble() {
      return this.B;
   }

   public dd.Type_r sAsDd_Type_r(int var1) {
      return this.f[var1];
   }

   public static double floor(double var0) {
      return var0 - Math.floor(var0);
   }

   public static record Type_r(double x, double y, double depth, double phase) {
   }
}
