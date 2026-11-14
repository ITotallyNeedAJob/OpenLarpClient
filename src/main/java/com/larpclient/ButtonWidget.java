package com.larpclient;

import com.larpclient.module.HandChams;
import net.minecraft.client.MinecraftClient;

public final class ButtonWidget {
   private static final ChamsOutlineEffect MenuPopupProvider = new ChamsOutlineEffect("HandChams");
   private static bp.Type_e Z;
   private static Object o;
   private static Object w;

   public static boolean rAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      HandChams var1 = HandChams.eAsHandChams();
      return var1 != null
         && var1.isRuntimeActive()
         && var0.world != null
         && var0.player != null
         && var0.options.getPerspective().isFirstPerson()
         && !var0.options.hudHidden
         && !var0.player.isSleeping()
         && !var0.player.isSpectator();
   }

   public static boolean xAsBoolean() {
      if (!rAsBoolean()) {
         lAsVoid();
         return false;
      } else {
         MinecraftClient var0 = MinecraftClient.getInstance();
         if (AuctionNotifier != var0.world || w != var0.player) {
            lAsVoid();
         }

         AuctionNotifier = var0.world;
         w = var0.player;
         Z = HandChams.eAsHandChams().t.NAsBp_Type_e(false);
         if (MenuPopupProvider.HAsBoolean()) {
            return true;
         } else {
            Z = null;
            return false;
         }
      }
   }

   public static void kAsVoid() {
      if (Z != null) {
         if (rAsBoolean() && AuctionNotifier == MinecraftClient.getInstance().world && w == MinecraftClient.getInstance().player) {
            bp.Type_e var0 = Z;
            Z = null;
            MenuPopupProvider.oAsVoid(var0);
         } else {
            lAsVoid();
         }
      }
   }

   public static void yAsVoid() {
      if (!rAsBoolean() || AuctionNotifier != null && (AuctionNotifier != MinecraftClient.getInstance().world || w != MinecraftClient.getInstance().player)) {
         lAsVoid();
      }
   }

   public static void lAsVoid() {
      Z = null;
      w = null;
      AuctionNotifier = null;
      MenuPopupProvider.close();
   }

   private ButtonWidget() {
   }
}
