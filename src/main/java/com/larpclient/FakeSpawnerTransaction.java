package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public final class FakeSpawnerTransaction {
   private static ScreenHandler p;
   private static Object r;
   private static Object Z;
   private static List<FakeSpawnerTransaction.Type_z> ScreenScale = List.of();
   private static List<ItemStack> GroundSample = List.of();
   private static ItemStack X = ItemStack.EMPTY;
   private static ItemStack DropletTrail = ItemStack.EMPTY;
   private static Runnable x;
   private static Runnable E;
   private static String e;
   private static int C;
   private static int G;
   private static boolean n;

   private FakeSpawnerTransaction() {
   }

   public static boolean aAsBoolean() {
      return HexEdgeloop != null;
   }

   public static boolean SAsBoolean() {
      return n;
   }

   public static void QAsVoid(Runnable var0) {
      if (!aAsBoolean() || n) {
         boolean var1 = n;
         n = true;

         try {
            var0.run();
         } finally {
            n = var1;
         }
      }
   }

   public static int xAsInt(int var0) {
      return var0 == 40 ? 45 : (var0 < 9 ? var0 + 36 : var0);
   }

   public static List<FakeSpawnerTransaction.Type_z> VAsList(int var0, int var1, int var2, int var3) {
      if (var0 != var1 && var2 > 0 && var2 <= var3 && var3 <= 64) {
         ArrayList var4 = new ArrayList();
         var4.add(new FakeSpawnerTransaction.Type_z(xAsInt(var0), 0, SlotActionType.PICKUP));
         if (var2 == var3) {
            var4.add(new FakeSpawnerTransaction.Type_z(xAsInt(var1), 0, SlotActionType.PICKUP));
         } else {
            for (int var5 = 0; var5 < var2; var5++) {
               var4.add(new FakeSpawnerTransaction.Type_z(xAsInt(var1), 1, SlotActionType.PICKUP));
            }

            var4.add(new FakeSpawnerTransaction.Type_z(xAsInt(var0), 0, SlotActionType.PICKUP));
         }

         return var4;
      } else {
         throw new IllegalArgumentException("Invalid backing transfer");
      }
   }

   public static boolean nAsBoolean(String var0, List<FakeSpawnerTransaction.Type_z> var1, Runnable var2, Runnable var3) {
      MinecraftClient var4 = MinecraftClient.getInstance();
      return var4.player != null && RAsBoolean(var4.player.playerScreenHandler, var0, var1, var2, var3);
   }

   public static boolean RAsBoolean(ScreenHandler var0, String var1, List<FakeSpawnerTransaction.Type_z> var2, Runnable var3, Runnable var4) {
      return QAsBoolean(var0, var1, var2, ItemStack.EMPTY, var3, var4);
   }

   public static boolean QAsBoolean(ScreenHandler var0, String var1, List<FakeSpawnerTransaction.Type_z> var2, ItemStack var3, Runnable var4, Runnable var5) {
      MinecraftClient var6 = MinecraftClient.getInstance();
      if (!aAsBoolean()
         && !var2.isEmpty()
         && var2.size() <= 132
         && var6.player != null
         && var6.interactionManager != null
         && var6.player.currentScreenHandler == var0
         && var0.getCursorStack().isEmpty()) {
         HexEdgeloop = var0;
         r = var6.player;
         Z = var6.world;
         ScreenScale = List.copyOf(var2);
         G = 0;
         C = 0;
         AuraModule = var4;
         E = var5;
         LineSegment = var1;
         DropletTrail = var3.copy();
         VAsVoid();
         ClientLog.XAsVoid("FakeSpawner", "Transaction started: {} clicks={} container={}", var1, var2.size(), HexEdgeloop.syncId);
         return true;
      } else {
         return false;
      }
   }

   private static void VAsVoid() {
      GroundSample = HexEdgeloop.slots.stream().map(var0 -> var0.getStack().copy()).toList();
      X = HexEdgeloop.getCursorStack().copy();
   }

   static boolean mAsBoolean(ScreenHandler var0, List<ItemStack> var1, ItemStack var2) {
      if (var0.slots.size() == var1.size() && ItemStack.areEqual(var0.getCursorStack(), var2)) {
         for (int var3 = 0; var3 < var1.size(); var3++) {
            if (!ItemStack.areEqual(var0.getSlot(var3).getStack(), var1.get(var3))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static void cAsVoid() {
      if (aAsBoolean() && !mAsBoolean(p, k, X)) {
         uAsVoid("server inventory correction");
      }

      VisualInventory.vAsVoid();
   }

   public static void LAsVoid() {
      if (aAsBoolean()) {
         MinecraftClient var0 = MinecraftClient.getInstance();
         if (var0.player == r
            && var0.world == Z
            && var0.player.currentScreenHandler == HexEdgeloop
            && var0.player.isAlive()
            && FakeSpawnerController.ZAsMa().XAsBoolean()) {
            if (++G <= 200 && mAsBoolean(p, k, X)) {
               if (C == ScreenScale.size()) {
                  if (!ItemStack.areEqual(HexEdgeloop.getCursorStack(), l)) {
                     uAsVoid("unexpected cursor at completion");
                  } else {
                     Runnable var6 = x;
                     ClientLog.XAsVoid("FakeSpawner", "Transaction predicted complete: {} clicks={}", e, C);
                     jAsVoid();
                     if (var6 != null) {
                        var6.run();
                     }

                     VisualInventory.vAsVoid();
                  }
               } else {
                  for (int var1 = 0; var1 < 4 && C < ScreenScale.size(); var1++) {
                     FakeSpawnerTransaction.Type_z var2 = ScreenScale.get(C++);
                     n = true;

                     try {
                        var0.interactionManager.clickSlot(HexEdgeloop.syncId, var2.slot, var2.button, var2.type, var0.player);
                     } finally {
                        n = false;
                     }

                     VAsVoid();
                     VisualInventory.vAsVoid();
                  }
               }
            } else {
               uAsVoid(G > 200 ? "timeout" : "inventory changed");
            }
         } else {
            uAsVoid("owner or container changed");
         }
      }
   }

   public static void uAsVoid(String var0) {
      if (aAsBoolean()) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         Runnable var2 = E;
         int var3 = var1.player == r && var1.world == Z && var1.player.currentScreenHandler == HexEdgeloop ? 1 : 0;
         ClientLog.XAsVoid("FakeSpawner", "Transaction cancelled: {} step={}/{} reason={}; native cursor recovery={}", e, C, ScreenScale.size(), var0, var3 != 0);
         jAsVoid();
         if (var3 != 0) {
            var1.player.closeHandledScreen();
         }

         if (var2 != null) {
            var2.run();
         }

         VisualInventory.vAsVoid();
      }
   }

   private static void jAsVoid() {
      HexEdgeloop = null;
      Z = null;
      r = null;
      ScreenScale = List.of();
      GroundSample = List.of();
      X = DropletTrail = ItemStack.EMPTY;
      E = null;
      AuraModule = null;
      LineSegment = null;
      G = 0;
      C = 0;
   }

   public static record Type_z(int slot, int button, SlotActionType type) {
   }
}
