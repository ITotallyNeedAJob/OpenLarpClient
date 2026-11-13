package com.larpclient;

public final class bj {
   private static final double B = 0.5;
   private Object t;
   private KnifeType q;
   private bj.Type_m J = bj.Type_m.IDLE;
   private double z;
   private double L = Double.NaN;
   private double H;
   private final bj.Type_i p = new bj.Type_i();

   public bj() {
   }

   public bj.Type_m DAsBj_Type_m() {
      return this.J;
   }

   public void QAsVoid() {
      this.t = null;
      this.q = null;
      this.L = Double.NaN;
      this.J = bj.Type_m.IDLE;
   }

   public void sAsVoid() {
      if (this.t != null) {
         this.J = bj.Type_m.ATTACK;
      }
   }

   public boolean oAsBoolean(double var1, double var3) {
      if (this.t != null && this.J == bj.Type_m.IDLE) {
         this.J = bj.Type_m.INSPECT;
         this.z = var1;
         this.H = this.q.I / (this.q.r * Math.clamp(Double.isFinite(var3) ? var3 : 2.0, 0.5, 2.0));
         return true;
      } else {
         return false;
      }
   }

   public bj.Type_i CAsBj_Type_i(Object var1, KnifeType var2, double var3, float var5, boolean var6) {
      int var7 = this.p.J == 2 ? 1 : 0;
      this.p.iAsVoid();
      if (var1 == null) {
         this.QAsVoid();
         return this.p;
      } else {
         if (!Double.isFinite(var3)) {
            var3 = 0.0;
         }

         if (var1 != this.t || var2 != this.q) {
            var7 = 0;
            this.t = var1;
            this.q = var2;
            this.J = bj.Type_m.EQUIP;
            this.z = var3;
         } else if (Double.isFinite(this.L) && (var3 < this.L || var3 - this.L > 0.500001)) {
            var7 = 0;
            this.J = bj.Type_m.IDLE;
         }

         this.L = var3;
         var5 = Float.isFinite(var5) ? Math.clamp(var5, 0.0F, 1.0F) : 0.0F;
         this.p.q = var7 != 0 && (this.J == bj.Type_m.ATTACK || var5 > 0.0F);
         if (var5 > 0.0F) {
            this.J = bj.Type_m.ATTACK;
         } else if (this.J == bj.Type_m.ATTACK) {
            this.J = bj.Type_m.IDLE;
         }

         if (this.J == bj.Type_m.EQUIP) {
            float var8 = (float)Math.clamp((var3 - this.z) / this.q.F, 0.0, 1.0);
            this.p.J = 1;
            this.p.L = var8;
            if (var8 >= 1.0F) {
               this.J = bj.Type_m.IDLE;
            }
         } else if (this.J == bj.Type_m.INSPECT) {
            float var10 = (float)Math.clamp((var3 - this.z) / this.H, 0.0, 1.0);
            this.p.J = 2;
            this.p.L = var10;
            if (var10 >= 1.0F) {
               this.J = bj.Type_m.IDLE;
               this.p.iAsVoid();
            }
         } else if (this.J == bj.Type_m.ATTACK) {
            this.p.J = 3;
            this.p.L = var5;
         }

         if (var6 && this.J == bj.Type_m.IDLE) {
            this.p.v = this.p.v + 0.003F * (float)Math.sin(var3 * 1.8);
         }

         return this.p;
      }
   }

   public static final class Type_i {
      public float v;
      public float L;
      public int J;
      public boolean q;

      public Type_i() {
      }

      private void iAsVoid() {
         this.v = this.L = 0.0F;
         this.J = 0;
         this.q = false;
      }
   }

   public static enum Type_m {
      EQUIP,
      IDLE,
      INSPECT,
      ATTACK;

      private Type_m() {
      }
   }
}
