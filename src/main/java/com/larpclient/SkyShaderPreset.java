package com.larpclient;

public final class SkyShaderPreset {
   private SkyShaderPreset() {
   }

   public static int gAsInt(String var0, int var1, float var2) {
      double[] var3 = new double[]{(double)(var1 >> 16 & 0xFF) / 255.0, (double)(var1 >> 8 & 0xFF) / 255.0, (double)(var1 & 0xFF) / 255.0};
      double var4 = (double)var2 * 100.0;

      double[] var6 = switch (var0) {
         case "Water" -> EAsDoubleArray2(var3, 0.2);
         case "Caustic" -> EAsDoubleArray2(var3, 0.25);
         case "Nebula" -> EAsDoubleArray2(var3, 0.5 * var4 + 0.1);
         case "Aurora" -> HAsDoubleArray2(hAsDoubleArray2(0.0186, 0.028, 0.059), var3, 0.044 * var4);
         case "Starfall" -> mAsDoubleArray2(HAsDoubleArray2(hAsDoubleArray2(0.0109, 0.0163, 0.0388), var3, 0.0157 * var4));
         case "BlackHole" -> hAsDoubleArray2(0.006, 0.006, 0.01);
         case "Plasma" -> EAsDoubleArray2(var3, 0.65 * var4);
         case "Voronoi" -> EAsDoubleArray2(var3, 0.3 * var4);
         case "Core Caustic" -> YAsDoubleArray2(aAsDoubleArray2(var3));
         case "Core Nebula" -> LAsDoubleArray2(aAsDoubleArray2(var3));
         case "Sunset" -> QAsDoubleArray2(aAsDoubleArray2(var3));
         case "Thunderstorm" -> UAsDoubleArray2(aAsDoubleArray2(var3));
         default -> EAsDoubleArray2(var3, 0.2);
      };
      return 0xFF000000 | zAsInt(var6[0]) << 16 | zAsInt(var6[1]) << 8 | zAsInt(var6[2]);
   }

   private static double[] YAsDoubleArray2(double[] var0) {
      double[] var1 = ZAsDoubleArray2(var0, hAsDoubleArray2(1.0, 1.0, 1.0), 0.55);
      return dAsDoubleArray2(HAsDoubleArray2(EAsDoubleArray2(var0, 0.46275), var1, 0.018), 0.55);
   }

   private static double[] LAsDoubleArray2(double[] var0) {
      double[] var1 = HAsDoubleArray2(EAsDoubleArray2(var0, 0.16), hAsDoubleArray2(0.004, 0.003, 0.014), 1.0);
      double[] var2 = ZAsDoubleArray2(var1, EAsDoubleArray2(var0, 0.55), 0.55);
      double[] var3 = ZAsDoubleArray2(var0, hAsDoubleArray2(1.0, 0.54, 0.2), 0.42);
      double[] var4 = ZAsDoubleArray2(var0, hAsDoubleArray2(0.16, 0.52, 1.0), 0.45);
      var2 = HAsDoubleArray2(var2, ZAsDoubleArray2(var4, var3, 0.5), 0.42);
      var2 = HAsDoubleArray2(var2, ZAsDoubleArray2(EAsDoubleArray2(var1, 2.0), var3, 0.55), 0.35);
      return dAsDoubleArray2(var2, 0.45);
   }

   private static double[] UAsDoubleArray2(double[] var0) {
      double[] var1 = hAsDoubleArray2(0.064, 0.071, 0.088);
      var1 = ZAsDoubleArray2(var1, LAsDoubleArray2(var1, HAsDoubleArray2(hAsDoubleArray2(0.55, 0.55, 0.55), var0, 0.9)), 0.34);
      return hAsDoubleArray2(1.0 - Math.exp(-var1[0] * 1.15), 1.0 - Math.exp(-var1[1] * 1.15), 1.0 - Math.exp(-var1[2] * 1.15));
   }

   private static double[] QAsDoubleArray2(double[] var0) {
      double[] var1 = LAsDoubleArray2(hAsDoubleArray2(1.0, 0.62, 0.25), HAsDoubleArray2(hAsDoubleArray2(0.835, 0.835, 0.835), var0, 0.264));
      var1 = HAsDoubleArray2(var1, hAsDoubleArray2(1.0, 0.65, 0.25), 0.075);
      var1 = HAsDoubleArray2(var1, hAsDoubleArray2(1.0, 0.85, 0.4), 0.068);
      var1 = HAsDoubleArray2(var1, hAsDoubleArray2(1.0, 0.9, 0.7), 0.0175);
      var1 = HAsDoubleArray2(var1, hAsDoubleArray2(1.0, 0.65, 0.3), 0.014);
      var1 = HAsDoubleArray2(var1, hAsDoubleArray2(1.0, 0.72, 0.38), 0.077);
      double[] var2 = ZAsDoubleArray2(hAsDoubleArray2(0.45, 0.05, 0.1), hAsDoubleArray2(1.0, 0.62, 0.25), 0.6);
      var1 = ZAsDoubleArray2(var1, HAsDoubleArray2(EAsDoubleArray2(var1, 0.85), var2, 0.25), 0.55);
      return dAsDoubleArray2(var1, 0.45);
   }

   private static double[] dAsDoubleArray2(double[] var0, double var1) {
      double[] var3 = new double[3];

      for (int var4 = 0; var4 < 3; var4++) {
         var3[var4] = Math.pow(Math.max(0.0, var0[var4] / (1.0 + var0[var4] * var1)), 0.92);
      }

      return var3;
   }

   private static double[] aAsDoubleArray2(double[] var0) {
      return hAsDoubleArray2(Math.max(var0[0], 0.05), Math.max(var0[1], 0.05), Math.max(var0[2], 0.05));
   }

   private static double[] mAsDoubleArray2(double[] var0) {
      return hAsDoubleArray2(Math.tanh(var0[0]), Math.tanh(var0[1]), Math.tanh(var0[2]));
   }

   private static double[] hAsDoubleArray2(double var0, double var2, double var4) {
      return new double[]{var0, var2, var4};
   }

   private static double[] EAsDoubleArray2(double[] var0, double var1) {
      return hAsDoubleArray2(var0[0] * var1, var0[1] * var1, var0[2] * var1);
   }

   private static double[] LAsDoubleArray2(double[] var0, double[] var1) {
      return hAsDoubleArray2(var0[0] * var1[0], var0[1] * var1[1], var0[2] * var1[2]);
   }

   private static double[] HAsDoubleArray2(double[] var0, double[] var1, double var2) {
      return hAsDoubleArray2(var0[0] + var1[0] * var2, var0[1] + var1[1] * var2, var0[2] + var1[2] * var2);
   }

   private static double[] ZAsDoubleArray2(double[] var0, double[] var1, double var2) {
      return hAsDoubleArray2(var0[0] + (var1[0] - var0[0]) * var2, var0[1] + (var1[1] - var0[1]) * var2, var0[2] + (var1[2] - var0[2]) * var2);
   }

   private static int zAsInt(double var0) {
      return (int)Math.round(Math.clamp(var0, 0.0, 1.0) * 255.0);
   }
}
