package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class CustomSkySettings {
   public static void wAsVoid(JsonObject var0) {
      if (var0.get("CustomSky") instanceof JsonObject var1 && var1.get("settings") instanceof JsonObject var2) {
         JsonElement var11 = var2.remove("Sun and Moon");
         if (var11 != null) {
            if (!var2.has("Show Vanilla Sun")) {
               var2.add("Show Vanilla Sun", var11.deepCopy());
            }

            if (!var2.has("Show Vanilla Moon")) {
               var2.add("Show Vanilla Moon", var11.deepCopy());
            }
         }
      }

      if (var0.get("SelfVisuals") instanceof JsonObject var6
         && var6.get("settings") instanceof JsonObject var8
         && var8.get("Style") != null
         && var8.get("Style").isJsonPrimitive()
         && var8.get("Style").getAsString().equals("Theme Gradient")) {
         var8.addProperty("Style", "Rainbow Outline");
      }

      for (String var4 : new String[]{"Trails", "BreadCrumbs"}) {
         JsonElement var5 = var0.remove(var4);
         if (var5 != null && !var0.has("Player Trail")) {
            var0.add("Player Trail", var5);
         }
      }
   }

   private CustomSkySettings() {
   }
}
