package com.larpclient.module;

import com.larpclient.KeybindNames;

public final class KeybindSetting extends Setting<Integer> {
   public KeybindSetting(String var1, String var2, int var3) {
      super(var1, var2, var3);
   }

   public void lAsVoid(Integer var1) {
      if (var1 >= -1 && var1 <= 348) {
         super.setValue(var1);
      } else {
         throw new IllegalArgumentException("Invalid key code");
      }
   }

   @Override
   public String getValueAsString() {
      if (KeybindNames.KAsBoolean(this.getValue())) {
         return KeybindNames.pAsString(this.getValue());
      } else {
         return this.getValue() < 0 ? "Unbound" : "Key " + this.getValue();
      }
   }
}
