package com.larpclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public final class ChatMessages {
   private ChatMessages() {
   }

   public static void RAsVoid(Text var0, boolean var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2.player != null) {
         if (var1) {
            var2.inGameHud.setOverlayMessage(var0, false);
         } else {
            var2.getMessageHandler().onGameMessage(var0, false);
         }
      }
   }
}
