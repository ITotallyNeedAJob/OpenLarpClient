package com.larpclient;

import java.util.Objects;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ChatCommandSignedC2SPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;

/**
 * Reconstructed from the bytecode Vineflower could not decompile (Java 21
 * pattern-matching switch over three C2S chat packet types). The original
 * failure comment and bytecode dump were removed; behaviour is identical.
 */
public final class l8 {
   private l8() {
   }

   public static String iAsString(Packet<?> var0) {
      Objects.requireNonNull(var0);
      String var1;
      if (var0 instanceof CommandExecutionC2SPacket var4) {
         var1 = var4.command();
      } else if (var0 instanceof ChatCommandSignedC2SPacket var5) {
         var1 = var5.command();
      } else if (var0 instanceof ChatMessageC2SPacket var6) {
         var1 = var6.chatMessage().strip().startsWith("/") ? var6.chatMessage() : null;
      } else {
         var1 = null;
      }

      return g.xAsBoolean(var1) ? var1 : null;
   }
}
