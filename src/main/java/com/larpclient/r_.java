package com.larpclient;

import java.util.Random;

public final class r_ {
   public static final int M = 96;
   private static final double W = 48.0;
   private final dv[] K = new dv[96];
   private final Random b;
   private final pp r;
   private n8 H;
   private boolean Y;
   private double d;
   private double p;
   private double T;
   private double y;
   private int k;
   public double P;
   public double g;
   public double V;
   public double O;

   public r_(long var1, n8 var3) {
      this.b = new Random(var1);
      this.r = new pp(this.b);
      this.H = var3;

      for (int var4 = 0; var4 < 96; var4++) {
         this.K[var4] = new dv();
      }
   }

   public dv[] cAsDvArray() {
      return this.K;
   }

   public double HAsDouble() {
      return this.y;
   }

   public void nAsVoid(n8 var1) {
      this.H = var1;
      this.iAsVoid();
   }

   public void iAsVoid() {
      this.k++;
   }

   public void qAsVoid() {
      this.Y = false;
      this.y = 0.0;
      this.O = 0.0;

      for (dv var4 : this.K) {
         var4.T = false;
         var4.i = dv.Type_l.RECYCLE;
         var4.m = var4.I = 0.0;
         var4.V = "";
      }
   }

   public static double NAsDouble(double var0, double var2) {
      return -Math.expm1(-var0 * Math.max(0.0, var2));
   }

   public static double aAsDouble(double var0) {
      return Math.atan2(Math.sin(var0), Math.cos(var0));
   }

   public boolean VAsBoolean(d1 var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      var16 = Math.clamp(var16, 0.0, 0.1);
      double var18 = var2 - this.d;
      double var20 = var4 - this.p;
      double var22 = var6 - this.T;
      int var24 = this.Y && var18 * var18 + var20 * var20 + var22 * var22 > 2304.0 ? 1 : 0;
      if (!this.Y || var24 != 0) {
         this.qAsVoid();
         this.P = var2;
         this.g = var4;
         this.V = var6;
         this.Y = true;
         this.O = var1.z == d1.Type_n.PLAYER ? var8 : 0.0;
      }

      this.d = var2;
      this.p = var4;
      this.T = var6;
      double var25 = NAsDouble(var1.G, var16);
      this.P = this.P + (var2 - this.P) * var25;
      this.g = this.g + (var4 - this.g) * var25;
      this.V = this.V + (var6 - this.V) * var25;
      double var27 = var1.z == d1.Type_n.WORLD ? 0.0 : var8;
      this.O = aAsDouble(this.O + aAsDouble(var27 - this.O) * NAsDouble(2.5, var16));
      this.y += var16;
      int var29 = var12 * var12 + var14 * var14 > 1.0E-4 ? 1 : 0;
      int var30 = var1.c == d1.Type_k.LOOK_DIRECTION || var1.c == d1.Type_k.MOVEMENT_DIRECTION && var29 != 0 ? 1 : 0;
      double var31 = (var1.c == d1.Type_k.MOVEMENT_DIRECTION ? Math.atan2(var12, var14) : var10) - this.O;
      int var33 = Math.clamp((long)var1.X, 0, 96);

      for (int var34 = 0; var34 < 96; var34++) {
         dv var35 = this.K[var34];
         if (var34 >= var33 || var35.o != this.k) {
            var35.CAsVoid();
         }

         if (var35.i != dv.Type_l.RECYCLE) {
            a1.rAsVoid(var35, var1, var16);
         }

         if (var35.i == dv.Type_l.RECYCLE && var34 < var33) {
            var35.I -= var16;
            if (!(var35.I > 0.0)) {
               String var36 = this.H.next(this.y, this.b);
               if (var36 == null || var36.isBlank()) {
                  var35.I = 0.1;
               } else if (!this.r.QAsBoolean(var35, this.K, var1, this.b, var31, var30 != 0)) {
                  var35.I = 0.75 + this.b.nextDouble() * 0.5;
               } else {
                  var35.V = var36;
                  var35.E = this.b.nextDouble() * 2.0 - 1.0;
                  var35.j = this.b.nextDouble() * Math.PI * 2.0;
                  var35.Q = 0.5 + this.b.nextDouble() * 0.7;
                  var35.h = this.b.nextDouble() * Math.PI * 2.0;
                  var35.Y = (this.b.nextDouble() - 0.5) * 0.3;
                  var35.L = (this.b.nextDouble() - 0.5) * 0.16;
                  var35.D = var35.l = var35.m = 0.0;
                  var35.q = var1.k * (0.65 + this.b.nextDouble() * 0.7);
                  var35.o = this.k;
                  var35.T = true;
                  var35.i = dv.Type_l.SPAWNING;
                  a1.rAsVoid(var35, var1, 0.0);
               }
            }
         }
      }

      return var24 != 0;
   }
}
