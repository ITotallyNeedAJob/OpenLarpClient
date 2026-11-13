package com.larpclient;

public record ThemePreset(int zenith, int horizon) {
   public static ThemePreset ofAsNv(String var0, int var1, float var2) {
      return switch (var0) {
         case "Theme" -> new ThemePreset(bg.xAsInt(-16315882, bd.w, 0.16), bg.xAsInt(-15263703, bd.Q, 0.48));
         case "Sunset" -> new ThemePreset(-14609097, -3245717);
         case "Ocean" -> new ThemePreset(-16378327, -12349780);
         case "Purple" -> new ThemePreset(-15725786, -8955221);
         case "Red" -> new ThemePreset(-14678256, -5486497);
         case "Black" -> new ThemePreset(-16645368, -15460316);
         case "Rainbow" -> new ThemePreset(bg.HSBtoRGB(var2, 0.65F, 0.13F), bg.HSBtoRGB(var2, 0.42F, 0.58F));
         case "Custom" -> new ThemePreset(bg.xAsInt(-16579055, var1 | 0xFF000000, 0.2), var1 | 0xFF000000);
         default -> new ThemePreset(-16446431, -12758401);
      };
   }
}
