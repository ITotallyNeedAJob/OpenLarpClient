package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.larpclient.module.BooleanSetting;
import com.larpclient.module.KeybindSetting;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import com.larpclient.module.TextSetting;

public final class SettingValueCodec {
   public SettingValueCodec() {
   }

   public static Object jAsObject(Setting<?> var0, JsonElement var1, boolean var2) {
      if (var1 != null && var1.isJsonPrimitive()) {
         JsonPrimitive var3 = var1.getAsJsonPrimitive();
         if (var0 instanceof BooleanSetting) {
            if (!var3.isBoolean()) {
               throw new IllegalArgumentException("Expected boolean");
            } else {
               return var3.getAsBoolean();
            }
         } else if (var0 instanceof TextSetting) {
            if (!var3.isString()) {
               throw new IllegalArgumentException("Expected text");
            } else {
               return TextSetting.oAsString(var3.getAsString());
            }
         } else if (var0 instanceof ModeSetting var7) {
            if (var3.isString() && var7.acceptsValue(var3.getAsString())) {
               return var3.getAsString();
            } else {
               throw new IllegalArgumentException("Unsupported mode");
            }
         } else if (var2 && !var3.isNumber()) {
            throw new IllegalArgumentException("Expected number");
         } else {
            double var4 = var3.getAsDouble();
            if (!Double.isFinite(var4)) {
               throw new IllegalArgumentException("Nonfinite setting");
            } else if (var0 instanceof NumberSetting var8) {
               if (var2 && (var4 < var8.getMin() || var4 > var8.getMax())) {
                  throw new IllegalArgumentException("Setting outside range");
               } else {
                  return var4;
               }
            } else if (!var2 || var4 == Math.rint(var4) && !(var4 < -2.1474836E9F) && !(var4 > 2.147483647E9)) {
               int var6 = var3.getAsInt();
               if (var0 instanceof KeybindSetting && (var6 < -1 || var6 > 348)) {
                  throw new IllegalArgumentException("Invalid keybind");
               } else {
                  return var6;
               }
            } else {
               throw new IllegalArgumentException("Expected integer");
            }
         }
      } else {
         throw new IllegalArgumentException("Expected setting primitive");
      }
   }

   public static void lAsVoid(Setting var0, JsonElement var1) {
      var0.setValue(jAsObject(var0, var1, false));
   }
}
