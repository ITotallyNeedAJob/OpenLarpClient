package com.larpclient;

public final class SmoothValueTracker {
   private double t;
   private double F;
   private double b;
   private double q;
   private int p;

   public SmoothValueTracker() {
   }

   public void oAsVoid(double var1, double var3) {
      this.t = this.b = var1;
      this.F = this.q = var3;
      this.p = 0;
   }

   public WidgetLayoutUtil.Type_d LAsD7_Type_d(double var1, double var3, boolean var5, double var6) {
      if (var5) {
         if (this.p == 0 && Math.max(Math.abs(var1 - this.b), Math.abs(var3 - this.q)) > 2.0) {
            this.p = Math.abs(var1 - this.b) >= Math.abs(var3 - this.q) ? 1 : 2;
         }

         if (this.p == 1) {
            var3 = this.q;
         }

         if (this.p == 2) {
            var1 = this.b;
         }
      } else {
         this.p = 0;
      }

      double var8 = 1.0 - Math.pow(0.85, Math.clamp(var6, 0.0, 0.25) * 60.0);
      this.t = this.t + (var1 - this.t) * var8;
      this.F = this.F + (var3 - this.F) * var8;
      if (Math.abs(var1 - this.t) < 0.25) {
         this.t = var1;
      }

      if (Math.abs(var3 - this.F) < 0.25) {
         this.F = var3;
      }

      return new WidgetLayoutUtil.Type_d((int)Math.round(this.t), (int)Math.round(this.F), -1, -1);
   }

   public static double YAsDouble(WidgetLayoutUtil.Type_q var0, double var1, double var3) {
      double var5 = (var1 - var0.mouseX()) / Math.max(20.0, (double)var0.bounds().width() / var0.initialScale());
      double var7 = (var3 - var0.mouseY()) / Math.max(20.0, (double)var0.bounds().height() / var0.initialScale());
      return Math.clamp((double)Math.round((var0.initialScale() + (Math.abs(var5) > Math.abs(var7) ? var5 : var7)) / 0.02) * 0.02, 0.5, 2.5);
   }
}
