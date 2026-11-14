package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.Category;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public final class GuiWorkspace {
   public static long m;
   public static String I = "RENDER";
   public static final GuiWorkspace.Type_e n = new GuiWorkspace.Type_e("Modules", "", "", 0, 0);
   public static GuiWorkspace.Type_e y = n;
   public static GuiWorkspace.Type_a c = new GuiWorkspace.Type_a(new GuiWorkspace.Type_g(1.0, 0.05), false, true, false);
   public static final Map<String, GuiWorkspace.Type_z> v = new LinkedHashMap<>();
   public static final Map<String, GuiWorkspace.Type_g> B = new LinkedHashMap<>();

   public GuiWorkspace() {
   }

   public static String PAsString(String var0) {
      String var1 = switch (var0) {
         case "Modules", "Configs", "Settings" -> var0;
         case "Appearance", "Account", "HUD Editor" -> "Settings";
         case "Local", "Cloud", "Import", "Marketplace", "Experimental" -> "Configs";
         default -> "Modules";
      };
      if (!var1.equals(var0)) {
         ClientLog.GAsVoid("GUI", "Restored obsolete destination {} as {}", var0, var1);
      }

      return var1;
   }

   public static JsonObject bAsJsonObject() {
      JsonObject var0 = new JsonObject();
      var0.addProperty("category", I);
      var0.add("clickGui", y.save());
      var0.addProperty("accent", UiStyle.w);
      var0.addProperty("secondary", UiStyle.Q);
      var0.addProperty("bloom", BloomQuality.U.name());
      var0.addProperty("blur", BloomQuality.v.name());
      var0.addProperty("bloomIntensity", BloomQuality.h);
      var0.addProperty("bloomRadius", BloomQuality.X);
      JsonObject var1 = new JsonObject();
      B.forEach((var1xx, var2x) -> {
         JsonArray var3x = new JsonArray();
         var3x.add(var2x.x());
         var3x.add(var2x.y());
         var1.add(var1xx, var3x);
      });
      var0.add("hud", var1);
      JsonObject var2 = new JsonObject();
      v.forEach((var1x, var2xx) -> {
         JsonObject var3x = new JsonObject();
         var3x.addProperty("scale", var2xx.scale());
         var3x.addProperty("hidden", var2xx.hidden());
         var2.add(var1x, var3x);
      });
      var0.add("hudStyles", var2);
      JsonObject var3 = new JsonObject();
      var3.addProperty("x", c.panel().x());
      var3.addProperty("y", c.panel().y());
      var3.addProperty("collapsed", c.collapsed());
      var3.addProperty("snapping", c.snapping());
      var3.addProperty("grid", c.grid());
      var0.add("hudEditor", var3);
      return var0;
   }

   private static float CAsFloat(JsonElement var0, float var1, float var2) {
      float var3 = var0.getAsFloat();
      if (!Float.isFinite(var3)) {
         throw new IllegalArgumentException("Nonfinite post setting");
      } else {
         return Math.clamp(var3, var1, var2);
      }
   }

   public static void TAsVoid(JsonObject var0) {
      m++;
      v.clear();
      B.clear();
      c = new GuiWorkspace.Type_a(new GuiWorkspace.Type_g(1.0, 0.05), false, true, false);
      y = n;

      for (Entry var2 : var0.entrySet()) {
         try {
            JsonElement var3 = (JsonElement)var2.getValue();
            String var4 = (String)var2.getKey();
            switch (var4) {
               case "category":
                  I = var3.getAsString().equals("COSMETICS")
                     ? "COSMETICS"
                     : (
                        var3.getAsString().equals("BASE_FINDING")
                           ? "BASE_FINDING"
                           : (
                              var3.getAsString().equals("MISC")
                                 ? "MISC"
                                 : (var3.getAsString().equals("WORLD") ? "RENDER" : Category.valueOf(var3.getAsString()).name())
                           )
                     );
                  break;
               case "clickGui":
                  y = GuiWorkspace.Type_e.load(var3.getAsJsonObject());
                  break;
               case "accent":
                  UiStyle.w = var3.getAsInt();
                  break;
               case "secondary":
                  UiStyle.Q = var3.getAsInt();
                  break;
               case "bloom":
                  BloomQuality.U = BloomQuality.Type_w.valueOf(var3.getAsString());
                  break;
               case "bloomIntensity":
                  BloomQuality.h = CAsFloat(var3, 0.0F, 2.0F);
                  break;
               case "bloomRadius":
                  BloomQuality.X = CAsFloat(var3, 0.5F, 3.0F);
                  break;
               case "blur":
                  BloomQuality.v = BloomQuality.Type_w.valueOf(var3.getAsString());
                  break;
               case "hudEditor":
                  JsonObject var13 = var3.getAsJsonObject();
                  c = new GuiWorkspace.Type_a(
                     new GuiWorkspace.Type_g(var13.has("x") ? var13.get("x").getAsDouble() : 1.0, var13.has("y") ? var13.get("y").getAsDouble() : 0.05),
                     var13.has("collapsed") && var13.get("collapsed").getAsBoolean(),
                     !var13.has("snapping") || var13.get("snapping").getAsBoolean(),
                     var13.has("grid") && var13.get("grid").getAsBoolean()
                  );
                  break;
               case "hudStyles":
                  for (Entry var14 : var3.getAsJsonObject().entrySet()) {
                     try {
                        JsonObject var15 = ((JsonElement)var14.getValue()).getAsJsonObject();
                        v.put(
                           (String)var14.getKey(),
                           new GuiWorkspace.Type_z(
                              var15.has("scale") ? var15.get("scale").getAsDouble() : 1.0, var15.has("hidden") && var15.get("hidden").getAsBoolean()
                           )
                        );
                     } catch (RuntimeException var111) {
                        ClientLog.oAsVoid("Config", "Invalid HUD style {} ignored", var14.getKey());
                     }
                  }
                  break;
               case "hud":
                  for (Entry var7 : var3.getAsJsonObject().entrySet()) {
                     try {
                        JsonArray var8 = ((JsonElement)var7.getValue()).getAsJsonArray();
                        B.put((String)var7.getKey(), new GuiWorkspace.Type_g(var8.get(0).getAsDouble(), var8.get(1).getAsDouble()));
                     } catch (RuntimeException var10) {
                        ClientLog.oAsVoid("Config", "Invalid HUD position {} ignored: {}", var7.getKey(), var10.toString());
                     }
                  }
                  break;
               default:
                  ClientLog.oAsVoid("Config", "Unknown UI setting {} ignored", var2.getKey());
            }
         } catch (RuntimeException var12) {
            ClientLog.oAsVoid("Config", "Invalid UI setting {} ignored: {}", var2.getKey(), var12.toString());
         }
      }
   }

   public static record Type_a(GuiWorkspace.Type_g panel, boolean collapsed, boolean snapping, boolean grid) {
   }

   public static record Type_e(String page, String module, String query, int moduleScroll, int settingScroll) {
      public Type_e(String page, String module, String query, int moduleScroll, int settingScroll) {
         if (!Set.of("Modules", "Configs", "Settings").contains(page)) {
            throw new IllegalArgumentException("Unknown ClickGUI page");
         } else if (module != null && query != null && module.length() <= 128 && query.length() <= 256) {
            if (module.equals("Trails") || module.equals("BreadCrumbs")) {
               module = "Player Trail";
            }

            moduleScroll = Math.clamp((long)moduleScroll, 0, 100000);
            settingScroll = Math.clamp((long)settingScroll, 0, 100000);
            this.page = page;
            this.module = module;
            this.query = query;
            this.moduleScroll = moduleScroll;
            this.settingScroll = settingScroll;
         } else {
            throw new IllegalArgumentException("Invalid ClickGUI selection/search");
         }
      }

      public JsonObject save() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("page", this.page);
         var1.addProperty("module", this.module);
         var1.addProperty("query", this.query);
         var1.addProperty("moduleScroll", this.moduleScroll);
         var1.addProperty("settingScroll", this.settingScroll);
         return var1;
      }

      public static GuiWorkspace.Type_e load(JsonObject var0) {
         return new GuiWorkspace.Type_e(
            GuiWorkspace.PAsString(var0.has("page") ? var0.get("page").getAsString() : "Modules"),
            var0.has("module") ? var0.get("module").getAsString() : "",
            var0.has("query") ? var0.get("query").getAsString() : "",
            var0.has("moduleScroll") ? var0.get("moduleScroll").getAsInt() : 0,
            var0.has("settingScroll") ? var0.get("settingScroll").getAsInt() : 0
         );
      }
   }

   public static record Type_g(double x, double y) {
      public Type_g(double x, double y) {
         if (Double.isFinite(x) && Double.isFinite(y)) {
            x = Math.clamp(x, 0.0, 1.0);
            y = Math.clamp(y, 0.0, 1.0);
            this.x = x;
            this.y = y;
         } else {
            throw new IllegalArgumentException("Nonfinite HUD position");
         }
      }
   }

   public static record Type_z(double scale, boolean hidden) {
      public Type_z(double scale, boolean hidden) {
         if (!Double.isFinite(scale)) {
            throw new IllegalArgumentException("Nonfinite HUD scale");
         } else {
            scale = Math.clamp(scale, 0.5, 2.5);
            this.scale = scale;
            this.hidden = hidden;
         }
      }
   }
}
