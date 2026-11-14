package com.larpclient;

public final class WidgetPopup {
   public static final int v = 240;
   private long r = -1L;
   private double B;
   private boolean t;
   private boolean L;
   private aq.Type_x I = aq.Type_x.REST;

   public WidgetPopup() {
   }

   public aq.Type_x AAsAq_Type_x() {
      return this.I;
   }

   public double YAsDouble() {
      return this.B;
   }

   public boolean LAsBoolean() {
      return this.L;
   }

   public aq.Type_x XAsAq_Type_x(long var1, boolean var3, boolean var4) {
      if (this.L) {
         return this.I;
      } else if (!var4) {
         this.OAsVoid();
         return this.I;
      } else {
         if (var3 && this.t && this.r >= 0L && var1 >= this.r && var1 - this.r <= 500000000L) {
            this.B = this.B + (double)(var1 - this.r) / 1000000.0;
         }

         this.r = var1;
         this.t = var3;
         if (this.B >= 240.0) {
            this.OAsVoid();
            return this.I;
         } else {
            if (var3) {
               double var5 = w2.Type_e.BACK_OUT.zAsDouble(this.B / 240.0);
               this.I = new aq.Type_x(0.965 + 0.035 * var5, 7.0 * (1.0 - var5));
            }

            return this.I;
         }
      }
   }

   public void OAsVoid() {
      this.L = true;
      this.B = 240.0;
      this.I = aq.Type_x.REST;
   }
}
