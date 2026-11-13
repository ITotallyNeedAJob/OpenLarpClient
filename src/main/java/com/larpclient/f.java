package com.larpclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public interface f {
   void larpOpenMenuPopup();

   wz larpMenuPopup();

   boolean larpDrawingMenuPopup();

   static wz pAsWz(Screen var0) {
      return var0 instanceof f var1 ? var1.larpMenuPopup() : null;
   }

   static aq.Type_x bAsAq_Type_x(Screen var0) {
      if (MinecraftClient.getInstance().world != null) {
         return aq.Type_x.REST;
      } else {
         wz var1 = pAsWz(var0);
         return var1 == null ? aq.Type_x.REST : var1.AAsAq_Type_x();
      }
   }

   static boolean CAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      ClientPreferences var1 = LarpClient.CAsLarpClient().fAsBo().yAsN7();
      return var1.Z && var1.L != WorldTimePreset.Type_b.STATIC && (Double)var0.options.getPanoramaSpeed().getValue() > 0.0;
   }
}
