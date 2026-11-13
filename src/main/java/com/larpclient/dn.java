package com.larpclient;

import java.util.Arrays;

public final class dn {
   private String N = "";
   private long[] Q = new long[0];
   private long g;

   public dn() {
   }

   public void zAsVoid(String var1, long var2, boolean var4) {
      this.g = var2;
      if (!var4) {
         if (!var1.equals(this.N)) {
            this.Q = new long[var1.length()];
         }

         this.N = var1;
         Arrays.fill(this.Q, Long.MIN_VALUE);
      } else if (!var1.equals(this.N)) {
         int var5 = 0;

         while (var5 < this.N.length() && var5 < var1.length() && this.N.codePointAt(var5) == var1.codePointAt(var5)) {
            var5 += Character.charCount(var1.codePointAt(var5));
         }

         int var6 = this.N.length();

         int var7;
         for (var7 = var1.length();
            var6 > var5 && var7 > var5 && this.N.codePointBefore(var6) == var1.codePointBefore(var7);
            var7 -= Character.charCount(var1.codePointBefore(var7))
         ) {
            var6 -= Character.charCount(this.N.codePointBefore(var6));
         }

         long[] var8 = new long[var1.length()];
         System.arraycopy(this.Q, 0, var8, 0, var5);
         Arrays.fill(var8, var5, var7, var2);
         System.arraycopy(this.Q, var6, var8, var7, this.N.length() - var6);
         this.N = var1;
         this.Q = var8;
      }
   }

   public double mAsDouble(int var1) {
      return var1 >= 0 && var1 < this.Q.length && this.Q[var1] != Long.MIN_VALUE ? w2.Type_e.CUBIC_OUT.zAsDouble((double)(this.g - this.Q[var1]) / 1.8E8) : 1.0;
   }

   public double WAsDouble(int var1) {
      return 3.0 * (1.0 - this.mAsDouble(var1));
   }

   public int gAsInt(int var1, int var2) {
      int var3 = var1 >>> 24;
      if (var3 == 0) {
         var3 = 255;
      }

      return var1 & 16777215 | (int)Math.round((double)var3 * (0.4 + 0.6 * this.mAsDouble(var2))) << 24;
   }

   public boolean BAsBoolean() {
      for (int var1 = 0; var1 < this.Q.length; var1++) {
         if (this.mAsDouble(var1) < 1.0) {
            return true;
         }
      }

      return false;
   }

   public int aAsInt() {
      return this.Q.length;
   }

   public static final class Type_x {
      private int h;

      public Type_x(int var1) {
         this.h = var1;
      }

      public int sAsInt(int var1) {
         int var2 = this.h;
         this.h = this.h + Character.charCount(var1);
         return var2;
      }
   }
}
