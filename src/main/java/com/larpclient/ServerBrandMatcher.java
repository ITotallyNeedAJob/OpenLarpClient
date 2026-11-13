package com.larpclient;

import java.util.List;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public final class ServerBrandMatcher {
   private static final List<String> k = List.of("paper", "purpur", "folia", "pufferfish", "leaves", "gale", "leaf");

   public static boolean cAsBoolean(String var0) {
      if (var0 == null) {
         return false;
      } else {
         String var1 = var0.toLowerCase(Locale.ROOT);

         for (String var3 : k) {
            if (var1.contains(var3)) {
               return true;
            }
         }

         return false;
      }
   }

   public static String AAsString() {
      ClientPlayNetworkHandler var0 = MinecraftClient.getInstance().getNetworkHandler();
      return var0 == null ? null : var0.getBrand();
   }

   public static boolean YAsBoolean() {
      return cAsBoolean(AAsString());
   }

   private ServerBrandMatcher() {
   }
}
