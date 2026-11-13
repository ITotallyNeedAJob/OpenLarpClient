package com.larpclient;

public final class rb {
   public static final int T = 120;
   private final double[] G = new double[120];
   private int f;
   private int L;
   private double q;
   private double O;
   private double C;
   private Object K;
   private Object W;
   private boolean g;

   public rb() {
   }

   public void fAsVoid() {
      this.f = this.L = 0;
      this.q = 0.0;
      this.g = false;
      this.K = this.W = null;
   }

   public void SAsVoid() {
      this.g = false;
   }

   public void cAsVoid(Object var1, Object var2, double var3, double var5, double var7) {
      if (var1 != null && var2 != null && Double.isFinite(var3) && Double.isFinite(var5) && Double.isFinite(var7) && !(var7 <= 0.0)) {
         if (this.K != var1 || this.W != var2) {
            this.fAsVoid();
            this.K = var1;
            this.W = var2;
         }

         if (!this.g) {
            this.O = var3;
            this.C = var5;
            this.g = true;
         } else {
            double var9 = Math.hypot(var3 - this.O, var5 - this.C);
            this.O = var3;
            this.C = var5;
            if (var9 > 16.0) {
               this.f = this.L = 0;
               this.q = 0.0;
            } else {
               double var11 = var9 * var7;
               if (!Double.isFinite(var11)) {
                  this.fAsVoid();
               } else {
                  if (this.L == 120) {
                     this.q = this.q - this.G[this.f];
                     this.G[this.f] = var11;
                     this.f = (this.f + 1) % 120;
                  } else {
                     this.G[(this.f + this.L++) % 120] = var11;
                  }

                  this.q += var11;
               }
            }
         }
      } else {
         this.fAsVoid();
      }
   }

   public int xAsInt() {
      return this.L;
   }

   public double mAsDouble(int var1) {
      if (var1 >= 0 && var1 < this.L) {
         return this.G[(this.f + var1) % 120];
      } else {
         throw new IndexOutOfBoundsException(var1);
      }
   }

   public double IAsDouble() {
      return this.L == 0 ? 0.0 : this.mAsDouble(this.L - 1);
   }

   public double hAsDouble() {
      return this.L == 0 ? 0.0 : Math.max(0.0, this.q / (double)this.L);
   }
}
