package com.larpclient;

public enum b4 {
   NEBULA("nebula", "Larp Nebula"),
   GOLDEN("golden-hour", "Golden Hour"),
   MIDNIGHT("midnight", "Midnight");

   public final String x;
   public final String O;

   private b4(String var3, String var4) {
      this.x = var3;
      this.O = var4;
   }

   public static b4 AAsB4(String var0) {
      for (b4 var4 : values()) {
         if (var4.x.equals(var0)) {
            return var4;
         }
      }

      return NEBULA;
   }
}
