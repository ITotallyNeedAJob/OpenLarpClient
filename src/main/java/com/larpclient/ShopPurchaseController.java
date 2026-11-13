package com.larpclient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public final class ShopPurchaseController {
   private static final Map<Integer, ItemStack> h = new LinkedHashMap<>();
   private static final Map<Item, Integer> D = new LinkedHashMap<>();
   private static final Map<Item, Integer> a = new LinkedHashMap<>();
   private static Object W;
   private static Object T;
   private static BlockPos O;
   private static ScreenHandler E;
   private static boolean l;
   private static boolean u;
   private static boolean v;
   private static boolean V;
   private static boolean w;
   private static boolean z;
   private static int g;
   private static int y;
   private static int B;
   private static int K;
   private static int m;
   private static int s;
   private static int R = -1;
   private static int I;
   private static Item X;
   private static boolean e;

   public ShopPurchaseController() {
   }

   public static boolean UAsBoolean(int var0) {
      return h.containsKey(var0);
   }

   public static int kAsInt(Item var0) {
      int var1 = 0;

      for (ItemStack var3 : h.values()) {
         if (var3.isOf(var0)) {
            var1 += var3.getCount();
         }
      }

      return var1;
   }

   public static ItemStack tAsItemStack(int var0, ItemStack var1) {
      if (!var1.isEmpty()) {
         return var1;
      } else {
         ItemStack var2 = h.get(var0);
         if (var2 == null) {
            return var1;
         } else {
            int var3 = a.getOrDefault(var2.getItem(), 0);

            for (Entry var5 : h.entrySet()) {
               if (((ItemStack)var5.getValue()).isOf(var2.getItem())) {
                  if ((Integer)var5.getKey() == var0) {
                     int var6 = var2.getCount() - Math.min(var3, var2.getCount());
                     return var6 <= 0 ? var1 : Sha256Digest.fAsItemStack(new ItemStack(var2.getItem(), var6));
                  }

                  var3 = Math.max(0, var3 - ((ItemStack)var5.getValue()).getCount());
               }
            }

            return var1;
         }
      }
   }

   public static int GAsInt(Item var0, int var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (!l
         && var1 > 0
         && SpawnerEntityCatalog.oAsBoolean(var0)
         && var2.player != null
         && var2.player.currentScreenHandler == var2.player.playerScreenHandler
         && var2.player.playerScreenHandler.getCursorStack().isEmpty()) {
         if (W != var2.player || T != var2.world) {
            aAsVoid();
         }

         W = var2.player;
         T = var2.world;

         for (int var3 = 0; var3 < 36; var3++) {
            int var4 = VisualInventory.rAsInt(var3);
            if (var4 >= 0 && !FakeSpawnerController.ZAsMa().vAsBoolean(var4) && var2.player.getInventory().getStack(var4).isEmpty()) {
               ItemStack var5 = h.get(var4);
               if (var5 == null || var5.isOf(var0) && var5.getCount() < 64) {
                  int var6 = Math.min(var1, 64 - (var5 == null ? 0 : var5.getCount()));
                  h.put(var4, new ItemStack(var0, var6 + (var5 == null ? 0 : var5.getCount())));
                  VisualInventory.YAsVoid(var3, var4);
                  return var6;
               }
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public static void gAsVoid() {
      wAsVoid((BlockPos)null);
   }

   public static void wAsVoid(BlockPos var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (!h.isEmpty() && !l && var1.player != null) {
         if (var1.player.currentScreenHandler == var1.player.playerScreenHandler && var1.player.playerScreenHandler.getCursorStack().isEmpty()) {
            D.clear();
            O = var0 == null ? null : var0.toImmutable();

            for (ItemStack var3 : h.values()) {
               D.merge(var3.getItem(), var3.getCount(), Integer::sum);
            }

            D.replaceAll((var0x, var1x) -> com.larpclient.m.mAsInt(var1x));
            u = true;
            l = true;
            B = g + 120;
            y = g + 100;
            var1.player.networkHandler.sendChatCommand("shop");
         } else {
            wAsVoid("Empty your cursor before withdrawing loot.");
         }
      }
   }

   public static boolean TAsBoolean(OpenScreenS2CPacket var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (!var1.isOnThread() || !u || var1.player == null) {
         return false;
      } else if (var1.currentScreen == null && var1.player == W && var1.world == T && g <= y) {
         ScreenHandler var2 = var0.getScreenHandlerType().create(var0.getSyncId(), var1.player.getInventory());
         if (!(var2 instanceof GenericContainerScreenHandler)) {
            wAsVoid("Unsupported shop menu.");
            return false;
         } else {
            E = var2;
            var1.player.currentScreenHandler = var2;
            u = false;
            y = g + 100;
            return true;
         }
      } else {
         wAsVoid("Shop purchase stopped: another screen opened or the request expired.");
         return false;
      }
   }

   public static void YAsVoid() {
      if (MinecraftClient.getInstance().isOnThread()) {
         K++;
         if (l && v && !z) {
            z = true;
            al.UAsVoid(0L, () -> {
               z = false;
               if (l && v) {
                  rAsVoid(MinecraftClient.getInstance());
               }
            });
         }
      }
   }

   public static boolean DAsBoolean() {
      return MinecraftClient.getInstance().isOnThread() && g < B;
   }

   public static boolean pAsBoolean(Text var0, boolean var1) {
      if (!DAsBoolean()) {
         return false;
      } else {
         String var2 = var0.getString().toLowerCase(Locale.ROOT);
         int var3 = !var2.contains("not enough")
               && !var2.contains("insufficient")
               && !var2.contains("inventory full")
               && !var2.contains("inventory is full")
               && !var2.contains("cannot afford")
               && !var2.contains("can't afford")
            ? 0
            : 1;
         int var4 = var3 == 0
               && !var2.contains("bought")
               && !var2.contains("purchas")
               && !var2.contains("paid")
               && !var2.contains("shop")
               && !var2.contains("received")
               && !var2.contains("successfully")
               && !var2.contains("deducted")
            ? 0
            : 1;
         if (var3 != 0 && l) {
            wAsVoid("Shop purchase failed; remaining withdrawals cancelled.");
         }

         return var1 || var4 != 0;
      }
   }

   public static void AAsVoid(MinecraftClient var0) {
      g++;
      rAsVoid(var0);
   }

   private static void rAsVoid(MinecraftClient var0) {
      if (W == null || W == var0.player && T == var0.world && FakeSpawnerController.ZAsMa().XAsBoolean()) {
         if (!l) {
            if (!h.isEmpty() && !(var0.currentScreen instanceof SpawnerLootScreen)) {
               aAsVoid();
            }
         } else if (var0.player != null && var0.interactionManager != null) {
            if (var0.currentScreen instanceof InventoryScreen) {
               w = true;
               var0.setScreen(null);
            }

            if (var0.currentScreen != null || var0.player.isDead()) {
               wAsVoid("Shop purchase cancelled.");
            } else if (g > y) {
               wAsVoid(e ? "Shop purchase stopped: cannot read the server price (details in latest.log)." : "Shop purchase timed out; no retry was sent.");
            } else if (!u) {
               if (V) {
                  JAsVoid(var0);
               } else if (E != null && var0.player.currentScreenHandler == E) {
                  if (v) {
                     if (K <= m || OAsInt(X) < s + 64 || !E.getCursorStack().isEmpty()) {
                        return;
                     }

                     D.computeIfPresent(X, (var0x, var1) -> var1 - 1);
                     a.merge(X, 64, Integer::sum);
                     B = g + 20;
                     v = false;
                     y = g + 100;
                  }

                  X = D.entrySet().stream().filter(var0x -> var0x.getValue() > 0).map(Entry::getKey).findFirst().orElse(null);
                  if (X == null) {
                     var0.player.closeHandledScreen();
                     E = null;
                     V = true;
                     y = g + 100;
                  } else {
                     for (Slot var2 : E.slots) {
                        if (var2.inventory != var0.player.getInventory() && var2.getStack().isOf(X)) {
                           LoreComponent var3 = (LoreComponent)var2.getStack().get(DataComponentTypes.LORE);
                           List var4 = var3 == null ? List.of() : var3.lines().stream().map(Text::getString).toList();
                           m.Type_i var5 = com.larpclient.m.CAsM_Type_i(var4);
                           if (var5 == com.larpclient.m.Type_i.SAFE) {
                              if (E.getCursorStack().isEmpty() && MAsInt(X) >= 64) {
                                 e = false;
                                 s = OAsInt(X);
                                 m = K;
                                 v = true;
                                 B = g + 120;
                                 y = g + 100;
                                 var0.interactionManager.clickSlot(E.syncId, var2.id, 0, SlotActionType.PICKUP, var0.player);
                                 return;
                              }

                              wAsVoid("Shop purchase stopped: inventory or cursor is full.");
                              return;
                           }

                           if (var5 == com.larpclient.m.Type_i.UNREADABLE && !e) {
                              ClientLog.oAsVoid("FakeSpawner", "Waiting for shop price for {}. Server lore: {}", Registries.ITEM.getId(X), var4);
                           }

                           if (var5 != com.larpclient.m.Type_i.UNREADABLE) {
                              ClientLog.oAsVoid("FakeSpawner", "Purchase blocked for {}: {}. Server lore: {}", Registries.ITEM.getId(X), var5, var4);
                              wAsVoid("Shop purchase blocked: server listing is above $3M.");
                              return;
                           }

                           e = true;
                        }
                     }
                  }
               } else {
                  wAsVoid("Shop closed before purchases completed.");
               }
            }
         } else {
            aAsVoid();
         }
      } else {
         wAsVoid((String)null);
         B = 0;
      }
   }

   private static int OAsInt(Item var0) {
      PlayerInventory var1 = MinecraftClient.getInstance().player.getInventory();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (var1.getStack(var3).isOf(var0)) {
            var2 += var1.getStack(var3).getCount();
         }
      }

      return var2;
   }

   private static int MAsInt(Item var0) {
      PlayerInventory var1 = MinecraftClient.getInstance().player.getInventory();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (!FakeSpawnerController.ZAsMa().vAsBoolean(var3)) {
            ItemStack var4 = var1.getStack(var3);
            if (var4.isEmpty()) {
               var2 += 64;
            } else if (var4.isOf(var0)) {
               var2 += Math.max(0, var4.getMaxCount() - var4.getCount());
            }
         }
      }

      return var2;
   }

   private static int rAsInt(int var0) {
      return var0 < 9 ? var0 + 36 : var0;
   }

   private static void JAsVoid(MinecraftClient var0) {
      if (var0.player.currentScreenHandler != var0.player.playerScreenHandler) {
         wAsVoid("Inventory changed; purchased loot kept.");
      } else if (!FakeSpawnerTransaction.aAsBoolean()) {
         if (R >= 0) {
            if (var0.player.getInventory().getStack(R).getCount() < I || !var0.player.playerScreenHandler.getCursorStack().isEmpty()) {
               return;
            }

            R = -1;
         }

         if (var0.player.playerScreenHandler.getCursorStack().isEmpty()) {
            for (Entry var2 : h.entrySet()) {
               int var3 = (Integer)var2.getKey();
               ItemStack var4 = (ItemStack)var2.getValue();
               ItemStack var5 = var0.player.getInventory().getStack(var3);
               if (var5.isEmpty() || var5.isOf(var4.getItem())) {
                  int var6 = var4.getCount() - var5.getCount();
                  if (var6 > 0) {
                     for (int var7 = 0; var7 < 36; var7++) {
                        if (var7 != var3) {
                           ItemStack var8 = var0.player.getInventory().getStack(var7);
                           if (var8.isOf(var4.getItem())) {
                              int var9 = h.containsKey(var7) && h.get(var7).isOf(var8.getItem()) ? h.get(var7).getCount() : 0;
                              int var10 = Math.min(var6, var8.getCount() - var9);
                              if (var10 > 0) {
                                 R = var3;
                                 I = var5.getCount() + var10;
                                 m = K;
                                 y = g + 100;
                                 int var11 = var8.getCount();
                                 FakeSpawnerTransaction.nAsBoolean(
                                    "arrange purchased loot",
                                    FakeSpawnerTransaction.VAsList(var7, var3, var10, var11),
                                    VisualInventory::vAsVoid,
                                    () -> wAsVoid("Inventory corrected; purchased loot kept in its real slots.")
                                 );
                                 return;
                              }
                           }
                        }
                     }
                  }
               }
            }

            wAsVoid((String)null);
         }
      }
   }

   private static void wAsVoid(String var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      int var2 = w && var1.player == W ? 1 : 0;
      if (O != null && var1.player == W && var1.world == T) {
         LinkedHashMap<Item, Integer> var3 = new LinkedHashMap<>();

         for (ItemStack var5 : h.values()) {
            var3.merge(var5.getItem(), Integer.valueOf(var5.getCount()), Integer::sum);
         }

         var3.forEach((var0x, var1x) -> FakeSpawnerController.ZAsMa().oAsVoid(O, var0x, Math.min(var1x, a.getOrDefault(var0x, 0))));
      }

      if (var1.player == W && E != null && var1.player.currentScreenHandler == E) {
         var1.player.closeHandledScreen();
      }

      aAsVoid();
      B = g + 20;
      if (var0 != null && var1.player != null) {
         ChatMessages.RAsVoid(Text.literal(var0), false);
      }

      if (var2 != 0 && var1.player != null) {
         var1.setScreen(new InventoryScreen(var1.player));
      }
   }

   public static void WAsVoid() {
      wAsVoid((String)null);
      B = 0;
   }

   private static void aAsVoid() {
      h.clear();
      D.clear();
      a.clear();
      E = null;
      O = null;
      T = null;
      W = null;
      z = false;
      w = false;
      e = false;
      V = false;
      v = false;
      u = false;
      l = false;
      R = -1;
      VisualInventory.vAsVoid();
   }
}
