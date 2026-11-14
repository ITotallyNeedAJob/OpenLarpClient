package com.larpclient;

import java.awt.Color;

public final class ColorUtil {
   public ColorUtil() {
   }

   public static int XAsInt(int var0, double var1) {
      return var0 & 16777215 | (int)(Math.clamp(var1, 0.0, 1.0) * 255.0) << 24;
   }

   public static int pAsInt(int var0, double var1) {
      return XAsInt(var0, (double)(var0 >>> 24) / 255.0 * var1);
   }

   public static int xAsInt(int var0, int var1, double var2) {
      var2 = Math.clamp(var2, 0.0, 1.0);
      int var4 = 0;

      for (int var5 = 0; var5 <= 24; var5 += 8) {
         var4 |= (int)((double)(var0 >>> var5 & 0xFF) * (1.0 - var2) + (double)(var1 >>> var5 & 0xFF) * var2) << var5;
      }

      return var4;
   }

   public static int HSBtoRGB(float var0, float var1, float var2) {
      return Color.HSBtoRGB(var0, var1, var2);
   }
}
