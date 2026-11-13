package com.larpclient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.ChinaHat;
import com.larpclient.module.CustomSky;
import com.larpclient.module.HitFX;
import com.larpclient.module.JumpFX;
import com.larpclient.module.Module;
import com.larpclient.module.Setting;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class AppearanceRestoreJournal {
   private static final Gson n = new Gson();
   private boolean d;
   private boolean K;
   private String S = "";
   private JsonObject A;

   public AppearanceRestoreJournal() {
   }

   public boolean uAsBoolean() {
      return this.d && this.K;
   }

   public void HAsVoid(String var1, String var2) {
      if (this.d && this.S.equals(var1)) {
         this.S = ProfileCodec.IAsString(var2);
      }
   }

   public JsonObject kAsJsonObject() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("active", this.d);
      if (this.d && this.A != null) {
         var1.addProperty("profile", this.S);
         var1.add("previous", this.A.deepCopy());
      }

      return var1;
   }

   public static AppearanceRestoreJournal sAsX5(JsonObject var0) {
      AppearanceRestoreJournal var1 = new AppearanceRestoreJournal();

      try {
         if (!ProfileCodec.SAsBoolean(var0.get("active"))) {
            return var1;
         }

         String var2 = ProfileCodec.IAsString(ProfileCodec.MAsString(var0, "profile", 128));
         JsonObject var3 = var0.getAsJsonObject("previous");
         if (var3 == null || var3.size() != 4) {
            throw new IllegalArgumentException("Incomplete restore journal");
         }

         var1.d = true;
         var1.S = var2;
         var1.A = var3.deepCopy();
      } catch (RuntimeException var41) {
         ClientLog.oAsVoid("Israel mode", "Invalid local restore journal ignored; profile values retained: {}", var41.toString());
      }

      return var1;
   }

   private static <T extends Module> T vAsModule(List<Module> var0, Class<T> var1) {
      return var0.stream()
         .filter(var1::isInstance)
         .map(var1::cast)
         .findFirst()
         .orElseThrow(() -> new IllegalStateException("Missing cosmetic: " + var1.getSimpleName()));
   }

   private static List<AppearanceRestoreJournal.Type_q> nAsList(List<Module> var0) {
      CustomSky var1 = vAsModule(var0, CustomSky.class);
      ChinaHat var2 = vAsModule(var0, ChinaHat.class);
      JumpFX var3 = vAsModule(var0, JumpFX.class);
      HitFX var4 = vAsModule(var0, HitFX.class);
      return List.of(
         new AppearanceRestoreJournal.Type_q(var1, Map.of(var1.skyStyle(), "Israel Flag")),
         new AppearanceRestoreJournal.Type_q(var2, Map.of(var2.XAsSetting(), "Kippah", var2.WAsSetting(), true)),
         new AppearanceRestoreJournal.Type_q(var3, Map.of(var3.FAsSetting(), "Star of David")),
         new AppearanceRestoreJournal.Type_q(var4, Map.of(var4.rAsSetting(), "Cash", var4.tAsSetting(), true))
      );
   }

   private static JsonObject KAsJsonObject(List<AppearanceRestoreJournal.Type_q> var0) {
      JsonObject var1 = new JsonObject();

      for (AppearanceRestoreJournal.Type_q var3 : var0) {
         JsonObject var4 = new JsonObject();
         var4.addProperty("enabled", var3.module.isEnabled());
         JsonObject var5 = new JsonObject();
         var3.values.keySet().forEach(var1x -> var5.add(var1x.getName(), n.toJsonTree(var1x.getValue())));
         var4.add("settings", var5);
         var1.add(var3.module.getName(), var4);
      }

      return var1;
   }

   private static List<AppearanceRestoreJournal.Type_q> EAsList(List<AppearanceRestoreJournal.Type_q> var0, JsonObject var1) {
      if (var1 != null && var1.size() == var0.size()) {
         ArrayList var2 = new ArrayList();

         for (AppearanceRestoreJournal.Type_q var4 : var0) {
            JsonObject var5 = var1.getAsJsonObject(var4.module.getName());
            ProfileCodec.SAsBoolean(var5.get("enabled"));
            JsonObject var6 = var5.getAsJsonObject("settings");
            if (var6.size() != var4.values.size()) {
               throw new IllegalArgumentException("Unexpected restore settings");
            }

            LinkedHashMap var7 = new LinkedHashMap();

            for (Setting var9 : var4.values.keySet()) {
               var7.put(var9, SettingValueCodec.jAsObject(var9, var6.get(var9.getName()), true));
            }

            var2.add(new AppearanceRestoreJournal.Type_q(var4.module, var7));
         }

         return var2;
      } else {
         throw new IllegalArgumentException("Invalid restore journal");
      }
   }

   private static void LAsVoid(List<AppearanceRestoreJournal.Type_q> var0, JsonObject var1) {
      for (AppearanceRestoreJournal.Type_q var3 : var0) {
         var3.values.forEach((var0x, var1x) -> {
            if (!Objects.equals(var0x.getValue(), var1x)) {
               ((Setting<? super Object>)var0x).setValue(var1x);
            }
         });
         int var4 = var1 != null && !ProfileCodec.SAsBoolean(var1.getAsJsonObject(var3.module.getName()).get("enabled")) ? 0 : 1;
         var3.module.setEnabled(var4 != 0);
         if ((var3.module.isEnabled() ? 1 : 0) != var4) {
            throw new IllegalStateException("Cosmetic activation failed: " + var3.module.getName());
         }
      }
   }

   public boolean lAsBoolean(boolean var1, List<Module> var2, String var3, BooleanSupplier var4) {
      if (var1 == this.uAsBoolean()) {
         return true;
      } else {
         try {
            List var5 = nAsList(var2);
            if (var1) {
               this.A = KAsJsonObject(var5);
               this.S = ProfileCodec.IAsString(var3);
               this.d = true;
               if (!var4.getAsBoolean()) {
                  this.d = false;
                  this.A = null;
                  return false;
               }

               this.K = true;

               try {
                  LAsVoid(var5, null);
               } catch (RuntimeException var12) {
                  try {
                     LAsVoid(EAsList(var5, this.A), this.A);
                     this.d = this.K = false;
                  } catch (RuntimeException var10) {
                     var12.addSuppressed(var10);
                  }

                  var4.getAsBoolean();
                  throw var12;
               }
            } else {
               List var6 = EAsList(var5, this.A);
               this.d = false;
               if (!var4.getAsBoolean()) {
                  this.d = true;
                  return false;
               }

               try {
                  LAsVoid(var6, this.A);
                  this.K = false;
                  this.A = null;
                  this.S = "";
               } catch (RuntimeException var11) {
                  this.d = true;

                  try {
                     LAsVoid(var5, null);
                  } catch (RuntimeException var9) {
                     var11.addSuppressed(var9);
                  }

                  var4.getAsBoolean();
                  throw var11;
               }
            }

            ClientLog.GAsVoid("Israel mode", var1 ? "Enabled four cosmetics; previous choices saved locally" : "Disabled; previous cosmetic choices restored");
            return true;
         } catch (RuntimeException var131) {
            ClientLog.lAsVoid("Israel mode", "Could not change cosmetic preset; restore journal retained where needed", var131);
            return false;
         }
      }
   }

   public boolean yAsBoolean(List<Module> var1, String var2, BooleanSupplier var3) {
      if (this.d && !this.K) {
         try {
            if (!this.S.equals(var2)) {
               throw new IllegalArgumentException("Active profile changed outside the client");
            } else {
               List var4 = nAsList(var1);
               List var5 = EAsList(var4, this.A);

               try {
                  LAsVoid(var4, null);
                  this.K = true;
               } catch (RuntimeException var9) {
                  try {
                     LAsVoid(var5, this.A);
                  } catch (RuntimeException var8) {
                     var9.addSuppressed(var8);
                     this.K = true;
                     ClientLog.lAsVoid("Israel mode", "Preset recovery failed; local restore journal retained for Off retry", var9);
                     return false;
                  }

                  throw var9;
               }

               ClientLog.GAsVoid("Israel mode", "Resumed local cosmetic preset for {}", this.S);
               return true;
            }
         } catch (RuntimeException var101) {
            this.d = this.K = false;
            this.A = null;
            this.S = "";
            var3.getAsBoolean();
            ClientLog.lAsVoid("Israel mode", "Could not resume preset; loaded profile retained", var101);
            return false;
         }
      } else {
         return true;
      }
   }

   public void yAsVoid(List<Module> var1, BooleanSupplier var2) {
      if (this.uAsBoolean() && !this.lAsBoolean(false, var1, this.S, var2)) {
         throw new IllegalStateException("Israel mode restore/save failed; configuration load cancelled");
      }
   }

   public void HAsVoid(Module var1, JsonObject var2) {
      if (this.uAsBoolean() && this.A != null && this.A.has(var1.getName())) {
         JsonObject var3 = this.A.getAsJsonObject(var1.getName());
         var2.add("enabled", var3.get("enabled").deepCopy());
         JsonObject var4 = var2.getAsJsonObject("settings");
         var3.getAsJsonObject("settings").entrySet().forEach(var1x -> var4.add((String)var1x.getKey(), ((JsonElement)var1x.getValue()).deepCopy()));
      }
   }

   private static record Type_q(Module module, Map<Setting<?>, Object> values) {
   }
}
