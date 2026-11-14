package com.larpclient;

public final class ModuleKeybinds {
   public static ModuleKeybinds.Type_g UAsMk_Type_g(int var0, boolean var1, boolean var2) {
      if (var1 && var0 >= 0 && var0 <= 20) {
         return var2 ? ModuleKeybinds.Type_g.READY : ModuleKeybinds.Type_g.WAIT;
      } else {
         return ModuleKeybinds.Type_g.CANCEL;
      }
   }

   private ModuleKeybinds() {
   }

   public static enum Type_g {
      CANCEL,
      WAIT,
      READY;

      private Type_g() {
      }
   }
}
