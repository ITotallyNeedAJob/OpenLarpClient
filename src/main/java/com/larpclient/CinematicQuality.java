package com.larpclient;

public enum CinematicQuality {
   SUBTLE(5.0, 48, 12.0, 0.6, 0.38, 12, 0.32),
   BALANCED(8.0, 96, 16.0, 0.82, 0.55, 18, 0.44),
   CINEMATIC(10.0, 128, 18.0, 0.94, 0.65, 24, 0.5);

   public final double S;
   public final double T;
   public final double n;
   public final double s;
   public final double l;
   public final int t;
   public final int j;

   private CinematicQuality(double var3, int var5, double var6, double var8, double var10, int var12, double var13) {
      this.S = var3;
      this.t = var5;
      this.T = var6;
      this.n = var8;
      this.s = var10;
      this.j = var12;
      this.l = var13;
   }

   public int HAsInt() {
      return Math.min(this.t, BloomQualityHelper.jAsInt(this.t));
   }

   public int EAsInt() {
      return Math.max(8, BloomQualityHelper.jAsInt(this.j));
   }

   public boolean dAsBoolean() {
      return BloomQualityHelper.K != BloomQuality.Type_w.Off;
   }

   public static CinematicQuality xAsM7(String var0) {
      return switch (var0) {
         case "Subtle" -> SUBTLE;
         case "Cinematic" -> CINEMATIC;
         default -> BALANCED;
      };
   }
}
