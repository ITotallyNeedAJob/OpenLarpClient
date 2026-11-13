package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.AimAssist;
import com.larpclient.module.Animations;
import com.larpclient.module.AutoClicker;
import com.larpclient.module.AutoSell;
import com.larpclient.module.CustomCrosshair;
import com.larpclient.module.FakePay;
import com.larpclient.module.Jesus;
import com.larpclient.module.Module;
import com.larpclient.module.PlayerModel;
import com.larpclient.module.Setting;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public record ModuleConfigCodec(boolean enabled, int keybind, Map<Setting<?>, Object> settings, JsonObject archive) {
   public static ModuleConfigCodec decode(Module var0, JsonObject var1) {
      if (var0 instanceof AimAssist) {
         var1 = ModuleMigrationNames.RAsJsonObject(var1);
      }

      if (var0 instanceof PlayerModel) {
         var1 = ModuleMigrationNames.ZAsJsonObject(var1);
      }

      if (var0 instanceof Jesus) {
         var1 = ModuleMigrationNames.nAsJsonObject(var1);
      }

      ModuleMigrationNames.RAsVoid(var1);
      boolean var2 = var1.has("enabled") ? ProfileCodec.SAsBoolean(var1.get("enabled")) : var0.isEnabled();
      int var3 = var0.getKeyBind();
      if (var1.has("keybind")) {
         JsonElement var4 = var1.get("keybind");
         if (!var4.isJsonPrimitive()) {
            throw new IllegalArgumentException("Invalid module keybind");
         }

         double var5 = var4.getAsDouble();
         if (!Double.isFinite(var5) || var5 != Math.rint(var5) || var5 < -1.0 || var5 > 348.0) {
            throw new IllegalArgumentException("Invalid module keybind");
         }

         var3 = (int)var5;
      }

      JsonObject var11 = var1.has("settings") ? var1.getAsJsonObject("settings").deepCopy() : new JsonObject();
      if (var0 instanceof AutoClicker) {
         AutoClicker.DAsVoid(var11);
      }

      if (var0 instanceof FakePay) {
         FakePay.hAsVoid(var11);
      }

      if (var0 instanceof CustomCrosshair) {
         CustomCrosshair.aAsVoid(var11);
      }

      if (var0 instanceof Animations) {
         Animations.QAsVoid(var11);
      }

      if (var0 instanceof AutoSell) {
         SelectionSettings.EAsVoid(var11);
      }

      LinkedHashMap var12 = new LinkedHashMap();

      for (Entry var7 : var11.entrySet()) {
         Setting var8 = var0.getSettings().stream().filter(var1x -> var1x.getName().equals(var7.getKey())).findFirst().orElse(null);
         if (var8 == null) {
            ClientLog.oAsVoid("Config", "Unknown setting {}.{} ignored", var0.getName(), var7.getKey());
         } else {
            try {
               var12.put(var8, SettingValueCodec.jAsObject(var8, (JsonElement)var7.getValue(), true));
            } catch (RuntimeException var10) {
               ClientLog.oAsVoid("Config", "Invalid {}.{}; keeping prior/default value: {}", var0.getName(), var8.getName(), var10.toString());
            }
         }
      }

      JsonObject var13 = var1.has("legacySettings") ? var1.getAsJsonObject("legacySettings").deepCopy() : null;
      return new ModuleConfigCodec(var2, var3, Collections.unmodifiableMap(var12), var13);
   }

   public void apply(Module var1) {
      boolean var2 = var1.isEnabled();
      int var3 = var1.getKeyBind();
      LinkedHashMap var4 = new LinkedHashMap();

      for (Setting var6 : var1.getSettings()) {
         var4.put(var6, var6.getValue());
      }

      try {
         if (var2) {
            var1.disable();
         }

         var1.setKeyBind(this.keybind);
         this.settings.forEach((var0, var1x) -> ((com.larpclient.module.Setting)var0).setValue(var1x));
         var1.setEnabled(this.enabled);
      } catch (RuntimeException var111) {
         RuntimeException var11x = var111;
         var1.setKeyBind(var3);

         for (Entry var7 : (Iterable<Entry>) (var4.entrySet())) {
            try {
               ((Setting)var7.getKey()).setValue(var7.getValue());
            } catch (RuntimeException var10) {
               var11x.addSuppressed(var10);
            }
         }

         var1.setEnabled(var2);
         throw var11x;
      }
   }
}
