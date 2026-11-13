package com.larpclient;

public final class w9 {
   public static final double a = 0.12;
   public static final double d = 0.48;
   private static final double f = 3.8;
   private static final double s = 0.14;
   private static final double S = 0.22;
   private final w9.Type_f w;
   private long T = -1L;
   private boolean P;
   private boolean Y;
   private double i;
   private double J;
   private double C;
   private double q;
   private double N;
   static final double G = 2.101;
   static final double o = 0.15963829787234074;

   public w9() {
      this(new w9.Type_f());
   }

   private w9(w9.Type_f var1) {
      this.w = var1;
   }

   public static w9.Type_i UAsW9_Type_i(RootMenuLayout var0) {
      return WAsW9_Type_i(var0, 0.0);
   }

   public static w9.Type_i WAsW9_Type_i(RootMenuLayout var0, double var1) {
      int var3 = var0.width();
      int var4 = var0.height();
      int var5 = var0.navigation(0).x() + var0.menuWidth() + 18;
      int var6 = Math.max(0, var3 - 16 - var5);
      if (var6 >= 64 && var4 >= 180) {
         int var7 = Math.min(240, var6);
         int var8 = (int)Math.min(360.0, Math.min((double)var4 * 0.59, (double)var7 * 1.5));
         int var9 = Math.clamp((long)((int)((double)var3 * 0.738 - (double)var7 / 2.0)), var5, var3 - 16 - var7);
         int var10 = Math.min(var4 - 18, (var4 + var8) / 2);
         int var11 = Math.max(58, var10 - var8);
         var8 = var10 - var11;
         float var12 = var1 > 0.15963829787234074 ? (float)((double)var8 / (2.101 + var1)) : 0.94F * (float)var8 / 2.125F;
         int var13 = Math.round((float)var10 - 0.101F * var12);
         return new w9.Type_i(new MenuLayout.Type_c(var9, var11, var7, var8), new MenuLayout.Type_c(var9, var11 - 24, var7, 18), var12, var13, true);
      } else {
         return new w9.Type_i(new MenuLayout.Type_c(var3, 0, 0, 0), new MenuLayout.Type_c(var3, 0, 0, 0), 0.0F, 0, false);
      }
   }

   public static double uAsDouble(w9.Type_i var0, int var1) {
      return (double)((float)var1 + var0.scale() + 8.0F - var0.centerX());
   }

   public static double hAsDouble(double var0) {
      return var0 / 3.8 + 0.18;
   }

   public w9.Type_m pAsW9_Type_m(long var1, boolean var3, boolean var4, boolean var5, boolean var6, w9.Type_i var7, int var8, int var9, int var10) {
      double var11 = this.T >= 0L && var1 >= this.T && var1 - this.T <= 500000000L ? (double)(var1 - this.T) / 1.0E9 : 0.0;
      this.T = var1;
      if (!var3 || !this.P || !var6) {
         var11 = 0.0;
      }

      this.P = var3 && var6;
      if (var6 && var7.visible()) {
         if (!var4 || !var5) {
            this.w.w = true;
         }

         if (!this.Y && var3) {
            this.Y = true;
            this.C = uAsDouble(var7, var8) / (double)var7.scale();
            var11 = 0.0;
         }

         if (var4) {
            this.J += var11;
            int var13 = var9 >= 0 && var10 >= 0 ? 1 : 0;
            double var14 = var13 != 0
               ? Math.clamp((double)((float)var9 - var7.centerX()) / Math.max(80.0, (double)var7.model().height() * 0.8), -1.0, 1.0)
               : 0.0;
            double var16 = var13 != 0
               ? Math.clamp(
                  ((double)(var10 - var7.model().y()) - (double)var7.model().height() * 0.15) / Math.max(60.0, (double)var7.model().height() * 0.7), -1.0, 1.0
               )
               : 0.0;
            double var18 = -Math.expm1(-var11 * 9.0);
            this.q = this.q + (var14 - this.q) * var18;
            this.N = this.N + (var16 - this.N) * var18;
         } else {
            this.J = this.q = this.N = 0.0;
         }

         float var30 = (float)(-16.0 + this.q * 4.0);
         float var31 = var4 ? (float)(-this.q * 0.3) : 0.0F;
         float var15 = (float)(this.N * 0.2);
         if (this.w.w) {
            return new w9.Type_m(w9.Type_c.IDLE, 0.0, 1.0, new w9.Type_w(var30, var31, var15, 0.0F, 0.0F, (float)(this.J * 20.0)));
         } else if (!this.Y) {
            return new w9.Type_m(w9.Type_c.HIDDEN, 0.0, 0.0, new w9.Type_w(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
         } else {
            double var32 = 0.12 + hAsDouble(this.C) + 0.48;
            this.i = Math.min(this.i + var11, var32);
            double var33 = this.i - 0.12;
            if (var33 < 0.0) {
               return new w9.Type_m(w9.Type_c.HIDDEN, 0.0, 0.0, new w9.Type_w(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, (float)(this.J * 20.0)));
            } else {
               w9.Type_q var20 = jAsW9_Type_q(var33, this.C);
               float var21 = (float)(var20.distance * 4.0);
               if (var33 < hAsDouble(this.C)) {
                  return new w9.Type_m(
                     w9.Type_c.WALKING,
                     uAsDouble(var7, var8) * (1.0 - var20.distance / this.C),
                     0.0,
                     new w9.Type_w(-90.0F, 0.0F, 0.0F, var21, (float)(var20.speed / 5.0), (float)(this.J * 20.0))
                  );
               } else if (this.i >= var32) {
                  this.w.w = true;
                  return new w9.Type_m(w9.Type_c.IDLE, 0.0, 1.0, new w9.Type_w(var30, var31, var15, 0.0F, 0.0F, (float)(this.J * 20.0)));
               } else {
                  double var22 = var33 - hAsDouble(this.C);
                  double var24 = VAsDouble(var22 / 0.48);
                  double var26 = VAsDouble(var22 / 0.33599999999999997);
                  float var28 = (float)(-90.0 + (double)(var30 + 90.0F) * var24);
                  float var29 = (float)(Math.clamp(Math.toRadians((double)(var28 - var30)) + (double)var31, -0.75, 0.75) * var26);
                  return new w9.Type_m(
                     w9.Type_c.TURNING, 0.0, 0.0, new w9.Type_w(var28, var29, (float)((double)var15 * var26), var21, 0.0F, (float)(this.J * 20.0))
                  );
               }
            }
         }
      } else {
         return new w9.Type_m(w9.Type_c.HIDDEN, 0.0, 0.0, new w9.Type_w(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
      }
   }

   private static w9.Type_q jAsW9_Type_q(double var0, double var2) {
      double var4 = Math.max(0.0, var2 / 3.8 - 0.18);
      if (var0 >= hAsDouble(var2)) {
         return new w9.Type_q(var2, 0.0);
      } else if (var0 < 0.14) {
         double var8 = Math.max(0.0, var0);
         return new w9.Type_q(3.8 * (var8 / 2.0 - 0.02228169203286535 * Math.sin(Math.PI * var8 / 0.14)), 3.8 * (0.5 - 0.5 * Math.cos(Math.PI * var8 / 0.14)));
      } else if (var0 < 0.14 + var4) {
         return new w9.Type_q(3.8 * (0.07 + var0 - 0.14), 3.8);
      } else {
         double var6 = var0 - 0.14 - var4;
         return new w9.Type_q(
            3.8 * (0.07 + var4 + var6 / 2.0 + 0.03501408748021698 * Math.sin(Math.PI * var6 / 0.22)), 3.8 * (0.5 + 0.5 * Math.cos(Math.PI * var6 / 0.22))
         );
      }
   }

   private static double VAsDouble(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   public void NAsVoid(boolean var1) {
      if (var1) {
         this.w.w = true;
      }

      this.rAsVoid();
   }

   public void lAsVoid() {
      this.w.w = this.Y = false;
      this.i = 0.0;
      this.rAsVoid();
   }

   public void rAsVoid() {
      this.T = -1L;
      this.P = false;
   }

   public static enum Type_c {
      HIDDEN,
      WALKING,
      TURNING,
      IDLE;

      private Type_c() {
      }
   }

   public static final class Type_f {
      private boolean w;

      public Type_f() {
      }

      public w9 GAsW9() {
         return new w9(this);
      }
   }

   public static record Type_i(MenuLayout.Type_c model, MenuLayout.Type_c name, float scale, int floorY, boolean visible) {
      public float centerX() {
         return (float)this.model.x() + (float)this.model.width() / 2.0F;
      }

      public int renderWidth() {
         return Math.max(this.model.width(), (int)Math.ceil((double)this.scale * 2.3));
      }
   }

   public static record Type_m(w9.Type_c stage, double xOffset, double nameOpacity, w9.Type_w pose) {
   }

   private static record Type_q(double distance, double speed) {
   }

   public static record Type_w(float bodyYaw, float headYaw, float headPitch, float walkPosition, float walkSpeed, float ageInTicks) {
   }
}
