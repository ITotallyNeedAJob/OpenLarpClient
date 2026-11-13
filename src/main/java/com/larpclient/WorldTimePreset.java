package com.larpclient;

import java.util.Locale;

public final class WorldTimePreset {
   private WorldTimePreset() {
   }

   public static String vAsString(Enum<?> var0) {
      String var1 = var0.name().toLowerCase(Locale.ROOT);
      return Character.toUpperCase(var1.charAt(0)) + var1.substring(1);
   }

   public static String yAsString(double var0) {
      return var0 < 0.25 ? "Morning" : (var0 < 0.5 ? "Midday" : (var0 < 0.75 ? "Sunset" : "Night"));
   }

   public static enum Type_b {
      HIGH(160),
      MEDIUM(100),
      LOW(48),
      STATIC(0);

      public final int V;

      private Type_b(int var3) {
         this.V = var3;
      }
   }

   public static enum Type_d {
      SPRING,
      SUMMER,
      AUTUMN,
      WINTER;

      private Type_d() {
      }
   }
}
