package com.larpclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class AuctionNotifier {
   private static final long K = 5000L;
   private static final long S = 2000L;
   private static final long G = 1500L;
   private static long l;
   private static long x;
   private static long g;
   private static long r;
   private static long y;
   private static Text Q;

   private AuctionNotifier() {
   }

   public static void qAsVoid(long var0) {
      Q = Text.literal("$ ").formatted(Formatting.GREEN).append(Text.literal(" " + AuctionMenuText.mAsString(var0)).formatted(Formatting.WHITE));
      long var2 = System.currentTimeMillis();
      long var4 = al.DAsLong();
      AuraModule = var2 + 2000L;
      DropletTrail = var2 + 5000L + var4;
      r = var2 + var4;
      ScreenScale = 0L;
      al.UAsVoid(var4, () -> {
         if (Q != null && System.currentTimeMillis() < l) {
            TAsVoid();
            ClientPlayerEntity var0x = MinecraftClient.getInstance().player;
            if (var0x != null) {
               ChatMessages.RAsVoid(Q, false);
            }
         }
      });
   }

   public static void IAsVoid() {
      Q = null;
      ChatPayParser = 0L;
      AuraModule = 0L;
      DropletTrail = 0L;
   }

   public static void eAsVoid() {
      if (Q == null || System.currentTimeMillis() >= l) {
         Q = null;
      } else if (System.currentTimeMillis() >= r && System.currentTimeMillis() - ScreenScale >= 1500L) {
         TAsVoid();
      }
   }

   public static void jAsVoid(long var0) {
      ChatPayParser = System.currentTimeMillis() + var0;
   }

   public static boolean KAsBoolean(Text var0, boolean var1) {
      return Q != null && System.currentTimeMillis() < AuraModule || System.currentTimeMillis() < g;
   }

   private static void TAsVoid() {
      ClientPlayerEntity var0 = MinecraftClient.getInstance().player;
      if (var0 != null && Q != null) {
         ChatMessages.RAsVoid(Q, true);
         ScreenScale = System.currentTimeMillis();
      }
   }
}
