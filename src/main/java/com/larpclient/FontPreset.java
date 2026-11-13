package com.larpclient;

import com.larpclient.module.HUD;

public final class FontPreset {
   public static TextDrawing.Type_x NAsV_Type_x(String var0) {
      return switch (var0) {
         case "SF Pro" -> TextDrawing.Type_x.HUD_SF_PRO;
         case "Minecraft" -> TextDrawing.Type_x.MINECRAFT;
         default -> TextDrawing.Type_x.MEDIUM;
      };
   }

   public static TextDrawing.Type_x wAsV_Type_x() {
      HUD var0 = HUD.EAsHUD();
      return NAsV_Type_x(var0 == null ? "Inter" : var0.hUDFont.getValue());
   }

   public static double rAsDouble() {
      return wAsV_Type_x() == TextDrawing.Type_x.MINECRAFT ? 9.0 : 11.0;
   }

   public static int cAsInt(String var0, double var1) {
      return (int)Math.ceil((double)TextDrawing.qAsInt(var0, wAsV_Type_x()) * var1 / rAsDouble());
   }

   public static String NAsString(String var0, double var1, double var3) {
      return TextDrawing.sAsString(var0, Math.max(0, (int)Math.floor(var1 * rAsDouble() / var3)), wAsV_Type_x());
   }

   public static double OAsDouble(String var0) {
      return switch (var0) {
         case "SF Pro" -> 5.25;
         case "Minecraft" -> 4.5;
         default -> 5.125;
      };
   }

   public static double vAsDouble(double var0, double var2, double var4) {
      HUD var6 = HUD.EAsHUD();
      String var7 = var6 == null ? "Inter" : var6.hUDFont.getValue();
      return var0 + var2 / 2.0 - OAsDouble(var7) * var4 / rAsDouble();
   }

   private FontPreset() {
   }
}
