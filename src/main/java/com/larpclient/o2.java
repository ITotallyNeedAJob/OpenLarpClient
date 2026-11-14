package com.larpclient;

public final class o2 {
   public static final o2 W = new o2();
   public static final int h = 900;
   public static final int v = 750;
   public static final int A = 75;
   public static final int I = 2000;
   public static final int ChinaHatCodec = 400;
   public static final int BoxUtils = 500;
   private long V = -1L;
   private long N = -1L;

   public o2() {
   }

   public void rAsVoid(long var1) {
      if (this.V < 0L) {
         this.V = var1;
      }
   }

   public void NAsVoid(long var1) {
      if (this.N < 0L) {
         this.N = var1;
      }
   }

   public boolean OAsBoolean() {
      return this.N >= 0L;
   }

   public double TAsDouble(long var1) {
      return this.V < 0L ? 0.0 : (double)Math.max(0L, var1 - this.V) / 1000000.0;
   }

   public double iAsDouble(long var1) {
      return this.V >= 0L && this.N >= 0L ? (double)Math.max(0L, var1 - Math.max(this.N, this.V + (long)I * 1000000L)) / 1000000.0 : 0.0;
   }

   public double ZAsDouble(long var1, boolean var3) {
      return var3 ? nAsDouble(this.TAsDouble(var1) / 450.0) : 1.0;
   }

   public double NAsDouble(long var1, boolean var3) {
      return var3 ? 0.82 + 0.18 * CAsDouble(this.TAsDouble(var1) / 900.0) : 1.0;
   }

   public double PAsDouble(long var1, boolean var3) {
      return var3 ? (-Math.PI * 2) * (1.0 - CAsDouble(this.TAsDouble(var1) / 900.0)) : 0.0;
   }

   public int DAsInt(long var1, boolean var3) {
      return var3 ? Math.clamp((long)((int)((this.TAsDouble(var1) - 750.0) / 75.0)), 0, 11) : 11;
   }

   public double jAsDouble(long var1, boolean var3) {
      return this.N < 0L ? 1.0 : (var3 ? 1.0 - nAsDouble(this.iAsDouble(var1) / 400.0) : 0.0);
   }

   public double nAsDouble(long var1, boolean var3) {
      if (this.N < 0L) {
         return 0.0;
      } else {
         return var3 ? nAsDouble((this.iAsDouble(var1) - 400.0) / 500.0) : 1.0;
      }
   }

   public boolean SAsBoolean(long var1, boolean var3) {
      return this.nAsDouble(var1, var3) >= 1.0;
   }

   public o2.Type_u RAsO2_Type_u(long var1, boolean var3) {
      if (this.V < 0L) {
         return o2.Type_u.STARTUP;
      } else if (this.N >= 0L && (!var3 || !(this.TAsDouble(var1) < (double)I))) {
         if (this.jAsDouble(var1, var3) > 0.0) {
            return o2.Type_u.BRAND_FADE;
         } else {
            return this.SAsBoolean(var1, var3) ? o2.Type_u.MENU_ACTIVE : o2.Type_u.BACKGROUND_REVEAL;
         }
      } else {
         return o2.Type_u.LOADING;
      }
   }

   public static double nAsDouble(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double CAsDouble(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   public static enum Type_u {
      STARTUP,
      LOADING,
      BRAND_FADE,
      BACKGROUND_REVEAL,
      MENU_ACTIVE;

      private Type_u() {
      }
   }
}
