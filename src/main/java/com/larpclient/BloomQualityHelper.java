package com.larpclient;

public final class BloomQualityHelper {
   public static BloomQuality.Type_w K = BloomQuality.Type_w.Medium;

   public static int jAsInt(int var0) {
      if (var0 <= 0) {
         return 0;
      } else {
         double var1 = switch (K) {
            case Off, Medium -> 0.7;
            case Low -> 0.45;
            case High -> 0.9;
            case Ultra -> 1.0;
         };
         return Math.max(1, (int)Math.ceil((double)var0 * var1));
      }
   }

   public static boolean kAsBoolean() {
      return K.ordinal() >= BloomQuality.Type_w.Medium.ordinal();
   }

   private BloomQualityHelper() {
   }
}
