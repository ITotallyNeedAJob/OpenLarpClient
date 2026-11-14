package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.BooleanSetting;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.Module;
import com.larpclient.module.Setting;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class SettingShortcuts {
   private static final int F = 256;
   private final Map<String, Map<String, Integer>> TextLayout = new LinkedHashMap<>();
   private List<SettingShortcuts.Type_x> q;
   private List<Module> n;

   public SettingShortcuts() {
   }

   public static boolean lAsBoolean(Setting<?> var0) {
      if (var0 instanceof BooleanSetting) {
         return true;
      } else {
         if (var0 instanceof ModeSetting var1 && !var1.isSearchable()) {
            return true;
         }

         return false;
      }
   }

   public static Module aAsModule(List<Module> var0, Setting<?> var1) {
      for (Module var3 : var0) {
         for (Setting var5 : var3.getSettings()) {
            if (var5 == var1) {
               return var3;
            }
         }
      }

      return null;
   }

   public int KAsInt(Module var1, Setting<?> var2) {
      Map<String, Integer> var3 = this.j.get(var1.getName());
      return var3 == null ? -1 : var3.getOrDefault(var2.getName(), -1);
   }

   public void bAsVoid(Module var1, Setting<?> var2, int var3) {
      if (!lAsBoolean(var2)) {
         throw new IllegalArgumentException("Only toggle and choice settings take PetEffectRenderer shortcut");
      } else if (var3 != -1 && !KeybindNames.hAsBoolean(var3)) {
         throw new IllegalArgumentException("Invalid bind code");
      } else {
         String var4 = EAsString(var1.getName());
         String var5 = hAsString(var2.getName());
         if (var3 == -1) {
            Map var6 = this.j.get(var4);
            if (var6 != null && var6.remove(var5) != null && var6.isEmpty()) {
               this.j.remove(var4);
            }
         } else {
            if (this.KAsInt(var1, var2) == -1 && this.GAsInt() >= 256) {
               throw new IllegalArgumentException("Too many setting shortcuts");
            }

            this.j.computeIfAbsent(var4, var0 -> new LinkedHashMap<>()).put(var5, var3);
         }

         this.q = null;
      }
   }

   public int GAsInt() {
      return this.j.values().stream().mapToInt(Map::size).sum();
   }

   public List<SettingShortcuts.Type_x> oAsList(List<Module> var1) {
      if (this.q != null && this.n == var1) {
         return this.q;
      } else {
         ArrayList var2 = new ArrayList();

         for (Module var4 : var1) {
            Map var5 = this.j.get(var4.getName());
            if (var5 != null) {
               for (Setting var7 : var4.getSettings()) {
                  Integer var8 = (Integer)var5.get(var7.getName());
                  if (var8 != null && lAsBoolean(var7)) {
                     var2.add(new SettingShortcuts.Type_x(var4, var7, var8));
                  }
               }
            }
         }

         this.n = var1;
         return this.q = List.copyOf(var2);
      }
   }

   public SettingShortcuts VAsXn() {
      return aAsXn(this.uAsJsonObject());
   }

   public JsonObject uAsJsonObject() {
      JsonObject var1 = new JsonObject();
      this.j.forEach((var1xx, var2) -> {
         JsonObject var3 = new JsonObject();
         var2.forEach(var3::addProperty);
         var1.add(var1xx, var3);
      });
      return var1;
   }

   public static SettingShortcuts aAsXn(JsonElement var0) {
      SettingShortcuts var1 = new SettingShortcuts();
      if (var0 != null && var0.isJsonObject()) {
         for (Entry var3 : var0.getAsJsonObject().entrySet()) {
            if (((JsonElement)var3.getValue()).isJsonObject()) {
               for (Entry var5 : ((JsonElement)var3.getValue()).getAsJsonObject().entrySet()) {
                  try {
                     if (var1.GAsInt() >= 256) {
                        return var1;
                     }

                     JsonElement var6 = (JsonElement)var5.getValue();
                     if (var6.isJsonPrimitive() && var6.getAsJsonPrimitive().isNumber()) {
                        double var7 = var6.getAsDouble();
                        if (var7 == Math.rint(var7) && KeybindNames.hAsBoolean((int)var7)) {
                           var1.j
                              .computeIfAbsent(EAsString((String)var3.getKey()), var0x -> new LinkedHashMap<>())
                              .put(hAsString((String)var5.getKey()), (int)var7);
                        }
                     }
                  } catch (RuntimeException var9) {
                  }
               }
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   private static String EAsString(String var0) {
      if (var0 != null && var0.matches("[A-Za-z0-9 _-]{1,128}") && !var0.isBlank()) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid module identifier");
      }
   }

   private static String hAsString(String var0) {
      if (var0 != null && !var0.isEmpty() && var0.length() <= 128 && !var0.chars().anyMatch(Character::isISOControl)) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid setting identifier");
      }
   }

   public static record Type_x(Module module, Setting<?> setting, int code) {
   }
}
