package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class PanelLayoutCodec {
   public static final int MenuPopupProvider = -12564250;
   static final int ChinaHatCodec = 16384;
   static final int K = 32;
   public boolean R;
   public int BloomUtility = -12564250;
   private final Map<String, PanelLayoutCodec.Type_e> E = new LinkedHashMap<>();

   public PanelLayoutCodec() {
   }

   public PanelLayoutCodec.Type_e TAsD2_Type_e(String var1) {
      return this.E.get(var1);
   }

   public void VAsVoid(String var1, PanelLayoutCodec.Type_e var2) {
      if (var1 != null && var1.matches("[A-Z][A-Z0-9_]{0,31}")) {
         if (Math.abs(var2.x()) <= 16384 && Math.abs(var2.y()) <= 16384) {
            if (this.E.size() < 32 || this.E.containsKey(var1)) {
               this.E.put(var1, var2);
            }
         } else {
            throw new IllegalArgumentException("Panel position out of range");
         }
      } else {
         throw new IllegalArgumentException("Invalid panel identifier");
      }
   }

   public void SAsVoid() {
      this.E.clear();
   }

   public int KAsInt() {
      return this.E.size();
   }

   public JsonObject UAsJsonObject() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("enabled", this.R);
      var1.addProperty("color", this.b);
      JsonObject var2 = new JsonObject();
      this.E.forEach((var1x, var2xx) -> {
         JsonObject var3 = new JsonObject();
         var3.addProperty("x", var2xx.x());
         var3.addProperty("y", var2xx.y());
         var3.addProperty("open", var2xx.open());
         var2.add(var1x, var3);
      });
      var1.add("panels", var2);
      return var1;
   }

   public static PanelLayoutCodec OAsD2(JsonObject var0) {
      PanelLayoutCodec var1 = new PanelLayoutCodec();

      try {
         if (var0.has("enabled")) {
            var1.R = ProfileCodec.SAsBoolean(var0.get("enabled"));
         }
      } catch (RuntimeException var12) {
      }

      try {
         JsonElement var2 = var0.get("color");
         if (var2 != null && var2.isJsonPrimitive() && var2.getAsJsonPrimitive().isNumber()) {
            long var3 = var2.getAsLong();
            if (var3 >= -2147483648L && var3 <= 4294967295L) {
               var1.b = (int)var3;
            }
         }
      } catch (RuntimeException var11) {
      }

      try {
         JsonObject var15 = var0.getAsJsonObject("panels");
         if (var15 != null && var15.size() <= 32) {
            for (Entry var4 : var15.entrySet()) {
               try {
                  JsonObject var5 = ((JsonElement)var4.getValue()).getAsJsonObject();
                  if (var5.get("x").getAsJsonPrimitive().isNumber() && var5.get("y").getAsJsonPrimitive().isNumber()) {
                     double var6 = var5.get("x").getAsDouble();
                     double var8 = var5.get("y").getAsDouble();
                     if (var6 == Math.rint(var6) && var8 == Math.rint(var8)) {
                        int var10 = var5.has("open") && !ProfileCodec.SAsBoolean(var5.get("open")) ? 0 : 1;
                        var1.VAsVoid((String)var4.getKey(), new PanelLayoutCodec.Type_e((int)var6, (int)var8, var10 != 0));
                     }
                  }
               } catch (RuntimeException var13) {
               }
            }
         }
      } catch (RuntimeException var14) {
      }

      return var1;
   }

   public static record Type_e(int x, int y, boolean open) {
   }
}
