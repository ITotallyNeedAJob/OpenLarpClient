package com.larpclient;

import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public final class AuctionMenuText {
   private AuctionMenuText() {
   }

   public static ItemStack NAsItemStack(ItemStack var0) {
      FakeSpawnerController var1 = FakeSpawnerController.ZAsMa();
      return var1.YAsBoolean(var0) ? ItemStack.EMPTY : var1.XAsItemStack(var0);
   }

   public static boolean BAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      return var0 != null && var0.currentScreen != null && var0.currentScreen.getTitle().getString().toLowerCase(Locale.ROOT).contains("auction");
   }

   public static boolean nAsBoolean(ItemStack var0) {
      if (Sha256Digest.RAsBoolean(var0)) {
         return true;
      } else if (!BAsBoolean()) {
         return false;
      } else {
         ClientPlayerEntity var1 = MinecraftClient.getInstance().player;
         if (var1 == null) {
            return false;
         } else {
            for (Slot var3 : var1.currentScreenHandler.slots) {
               if (var3.inventory == var1.getInventory()) {
                  break;
               }

               if (var3.getStack() == var0) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public static boolean WAsBoolean(ItemStack var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (FakeSpawnerController.ZAsMa().XAsBoolean()
         && var1.currentScreen != null
         && var1.currentScreen.getTitle().getString().toLowerCase(Locale.ROOT).contains("sell")) {
         LoreComponent var2 = (LoreComponent)var0.get(DataComponentTypes.LORE);
         return var2 != null && var2.lines().stream().anyMatch(var0x -> var0x.getString().toUpperCase(Locale.ROOT).contains("CLICK TO SELL ITEMS"));
      } else {
         return false;
      }
   }

   public static long yAsLong() {
      ClientPlayerEntity var0 = MinecraftClient.getInstance().player;
      return var0 == null
         ? 0L
         : var0.currentScreenHandler
            .slots
            .stream()
            .filter(var1 -> var1.inventory != var0.getInventory())
            .mapToLong(var0x -> FakeSpawnerController.ZAsMa().QAsLong(var0x.getStack()))
            .sum();
   }

   public static String mAsString(long var0) {
      if (var0 >= 1000000000L) {
         return uAsString((double)var0 / 1.0E9) + "B";
      } else if (var0 >= 1000000L) {
         return uAsString((double)var0 / 1000000.0) + "M";
      } else {
         return var0 >= 1000L ? uAsString((double)var0 / 1000.0) + "K" : Long.toString(var0);
      }
   }

   private static String uAsString(double var0) {
      return String.format(Locale.ROOT, "%.1f", var0).replaceAll("\\.0$", "");
   }
}
