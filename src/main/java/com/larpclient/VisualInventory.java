package com.larpclient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen.CreativeScreenHandler;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen.CreativeSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;

public final class VisualInventory {
   private static InventorySlotSnapshot X = new InventorySlotSnapshot();
   private static Object z;
   private static int[] B;
   private static int P;
   private static int t = -1;
   private static boolean F;
   private static ScreenHandler c;
   private static ScreenHandler G;
   private static final Map<Slot, VisualInventory.Type_m> V = new IdentityHashMap<>();
   private static final Map<Slot, CreativeSlot> x = new IdentityHashMap<>();
   private static final SimpleInventory E = new SimpleInventory(1);
   private static boolean p = true;
   private static Object A;
   private static long h;
   private static int b = -1;

   public VisualInventory() {
   }

   public static boolean DAsBoolean() {
      return FakeSpawnerTransaction.aAsBoolean();
   }

   public static void vAsVoid() {
      p = true;
   }

   public static long jAsLong() {
      return h;
   }

   public static void XAsVoid() {
      A = null;
      z = null;
      B = null;
      G = null;
      c = null;
      V.clear();
      x.clear();
      b = -1;
      t = -1;
      F = false;
      X = new InventorySlotSnapshot();
      p = true;
   }

   public static boolean jAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      return var0.player != null && FakeSpawnerController.ZAsMa().XAsBoolean();
   }

   public static void GAsVoid() {
      p = true;
      wAsVoid();
   }

   private static void wAsVoid() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (!jAsBoolean()) {
         z = null;
         B = null;
      } else {
         if (var0.player.currentScreenHandler.getCursorStack().isEmpty()) {
            b = -1;
         }

         if (z != var0.player) {
            z = var0.player;
            X = new InventorySlotSnapshot();
            B = null;
            t = var0.player.getInventory().getSelectedSlot();
            P = t;
            F = false;
            p = true;
            ClientLog.XAsVoid("FakeSpawner", "Visual inventory initialized; ordinary slot positions retained");
         }

         if (A != var0.player.currentScreenHandler) {
            A = var0.player.currentScreenHandler;
            p = true;
         }

         if ((p || B == null) && (B == null || !DAsBoolean())) {
            long var1 = 0L;
            long var3 = 0L;

            for (int var5 = 0; var5 < 36; var5++) {
               if (!var0.player.getInventory().getStack(var5).isEmpty()
                  || ShopPurchaseController.UAsBoolean(var5)
                  || FakeSpawnerController.ZAsMa().qAsBoolean(var5)) {
                  var1 |= 1L << var5;
               }

               if (FakeSpawnerController.ZAsMa().vAsBoolean(var5) && FakeSpawnerController.ZAsMa().hAsInt(var5, var0.player.getInventory().getStack(var5)) == 0
                  )
                {
                  var3 |= 1L << var5;
               }
            }

            p = false;
            int[] var6 = X.zAsIntArray(var1, var3);
            if (var6 != B) {
               B = var6;
               h++;
               ClientLog.XAsVoid("FakeSpawner", "Mapping snapshot {} occupancy={} reserved={}", h, Long.toHexString(var1), Long.toHexString(var3));
            }
         }
      }
   }

   public static int rAsInt(int var0) {
      wAsVoid();
      return B != null && var0 >= 0 && var0 < 36 ? B[var0] : var0;
   }

   public static ItemStack QAsItemStack(int var0) {
      int var1 = rAsInt(var0);
      return var1 < 0 ? ItemStack.EMPTY : pAsItemStack(var1, MinecraftClient.getInstance().player.getInventory().getStack(var1));
   }

   public static ItemStack pAsItemStack(int var0, ItemStack var1) {
      return FakeSpawnerController.ZAsMa().vAsBoolean(var0)
         ? FakeSpawnerController.ZAsMa().sAsItemStack(var0, var1)
         : FakeSpawnerController.ZAsMa().MAsItemStack(var0, ShopPurchaseController.tAsItemStack(var0, var1));
   }

   public static int nAsInt(int var0) {
      wAsVoid();
      if (!jAsBoolean()) {
         return var0;
      } else {
         if (var0 != t) {
            P = var0;
            F = false;
         }

         t = var0;
         if (F && B != null && P >= 0 && P < 9 && B[P] < 0) {
            return P;
         } else {
            F = false;
            if (B != null) {
               for (int var1 = 0; var1 < 9; var1++) {
                  if (B[var1] == var0) {
                     P = var1;
                     break;
                  }
               }
            }

            return P;
         }
      }
   }

   public static int JAsInt() {
      wAsVoid();

      for (int var0 = 0; var0 < 36; var0++) {
         int var1 = B == null ? var0 : B[var0];
         if (var1 < 0
            || MinecraftClient.getInstance().player.getInventory().getStack(var1).isEmpty()
               && !FakeSpawnerController.ZAsMa().qAsBoolean(var1)
               && !ShopPurchaseController.UAsBoolean(var1)) {
            return var0;
         }
      }

      return -1;
   }

   public static void AAsVoid(int var0, ItemStack var1, int var2) {
      if (var2 >= 0) {
         X.KAsVoid(var2, var0);
      }

      GAsVoid();
   }

   public static void oAsVoid(int var0, ItemStack var1) {
      AAsVoid(var0, var1, JAsInt());
   }

   public static Slot eAsSlot(ScreenHandler var0, Slot var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var0 instanceof CreativeScreenHandler) {
         if (jAsBoolean() && var1 instanceof CreativeSlot var7 && !(var7.slot instanceof VisualInventory.Type_m)) {
            dAsVoid(var0);
            return qAsSlot(var1, eAsSlot(var2.player.playerScreenHandler, var7.slot));
         }

         return var1;
      } else if (jAsBoolean()
         && !(var1 instanceof VisualInventory.Type_m)
         && var1.inventory == var2.player.getInventory()
         && var1.getIndex() >= 0
         && var1.getIndex() < 36) {
         int var3 = rAsInt(var1.getIndex());
         Slot var4 = null;
         if (var3 >= 0) {
            for (Slot var6 : var0.slots) {
               if (var6.inventory == var1.inventory && var6.getIndex() == var3) {
                  var4 = var6;
                  break;
               }
            }
         }

         if (c != var0) {
            V.clear();
            x.clear();
            c = var0;
         }

         VisualInventory.Type_m var8 = V.get(var1);
         if (var8 == null || var8.l != var4) {
            var8 = new VisualInventory.Type_m(var1, var4);
            V.put(var1, var8);
         }

         return var8;
      } else {
         return var1;
      }
   }

   private static Slot qAsSlot(Slot var0, Slot var1) {
      CreativeSlot var2 = x.get(var0);
      if (var2 == null || var2.slot != var1) {
         var2 = new CreativeSlot(var1, var0.getIndex(), var0.x, var0.y);
         var2.id = var0.id;
         x.put(var0, var2);
      }

      return var2;
   }

   private static void dAsVoid(ScreenHandler var0) {
      if (G != var0) {
         x.clear();
         G = var0;
      }
   }

   public static Slot UAsSlot(Slot var0) {
      return var0 instanceof CreativeSlot var1 ? var1.slot : var0;
   }

   public static Slot tAsSlot(ScreenHandler var0, Slot var1, Set<Slot> var2) {
      if (jAsBoolean() && var0 instanceof CreativeScreenHandler && var1 instanceof CreativeSlot var16) {
         dAsVoid(var0);
         PlayerScreenHandler var17 = MinecraftClient.getInstance().player.playerScreenHandler;
         HashSet var18 = new HashSet();

         for (Slot var21 : var2) {
            var18.add(UAsSlot(var21));
         }

         Slot var20 = tAsSlot(var17, var16.slot, var18);
         if (!var2.isEmpty()) {
            var2.clear();

            for (Slot var23 : (Iterable<Slot>) (var18)) {
               for (Slot var25 : var0.slots) {
                  Slot var26 = eAsSlot(var0, var25);
                  if (UAsSlot(var26) == var23) {
                     var2.add(var26);
                     break;
                  }
               }
            }
         }

         return qAsSlot(var1, var20);
      } else {
         Slot var3 = eAsSlot(var0, var1);
         if (var3 instanceof VisualInventory.Type_m var4
            && var4.l == null
            && !DAsBoolean()
            && var0.syncId >= 0
            && !var0.getCursorStack().isEmpty()
            && !Sha256Digest.oAsBoolean(var0.getCursorStack())) {
            PlayerInventory var5 = MinecraftClient.getInstance().player.getInventory();

            for (int var6 = 0; var6 < 2; var6++) {
               for (Slot var8 : var0.slots) {
                  int var9 = var8.getIndex();
                  if (var8.inventory == var5
                     && var9 >= 0
                     && var9 < 36
                     && !var8.hasStack()
                     && (var6 != 0 || var9 != b)
                     && (var6 != 1 || var9 == b)
                     && !qAsBoolean(var9, var8.getStack())
                     && var8.canInsert(var0.getCursorStack())
                     && var8.isEnabled()
                     && var8.getMaxItemCount(var0.getCursorStack()) >= 1
                     && !var2.stream().anyMatch(var2x -> var2x.inventory == var5 && var2x.getIndex() == var9)) {
                     X.KAsVoid(var4.D, var9);
                     vAsVoid();
                     wAsVoid();
                     if (!var2.isEmpty()) {
                        ArrayList var10 = new ArrayList(var2.size());

                        for (Slot var12 : var2) {
                           Slot var13 = var12;
                           if (var12.inventory == var5) {
                              for (Slot var15 : var0.slots) {
                                 if (var15.inventory == var5 && var15.getIndex() >= 0 && var15.getIndex() < 36 && rAsInt(var15.getIndex()) == var12.getIndex()) {
                                    var13 = eAsSlot(var0, var15);
                                    break;
                                 }
                              }
                           }

                           var10.add(var13);
                        }

                        var2.clear();
                        var2.addAll(var10);
                     }

                     ClientLog.XAsVoid("FakeSpawner", "Visual gap bound logical={} physical={} menu={}", var4.D, var9, var0.syncId);
                     return eAsSlot(var0, var1);
                  }
               }
            }

            return var3;
         }

         return var3;
      }
   }

   public static Slot PAsSlot(ScreenHandler var0, Slot var1) {
      if (var1 instanceof VisualInventory.Type_m var2 && var2.l != null) {
         for (Slot var4 : var0.slots) {
            if (var4.inventory == var1.inventory && var4.getIndex() == var1.getIndex()) {
               return new VisualInventory.Type_m(new Slot(var1.inventory, var2.D, var1.x, var1.y), var4);
            }
         }

         return var1;
      }

      return var1;
   }

   public static void ZAsVoid(Slot var0) {
      if (!DAsBoolean() && var0 instanceof VisualInventory.Type_m var1 && var1.l != null) {
         if (MinecraftClient.getInstance().player.currentScreenHandler.getCursorStack().isEmpty() && var1.hasStack()) {
            b = var1.l.getIndex();
         }

         X.KAsVoid(var1.D, var1.l.getIndex());
         vAsVoid();
      }
   }

   private static boolean qAsBoolean(int var0, ItemStack var1) {
      return var0 < 0
         || FakeSpawnerController.ZAsMa().vAsBoolean(var0)
         || ShopPurchaseController.UAsBoolean(var0)
         || FakeSpawnerController.ZAsMa().qAsBoolean(var0);
   }

   public static boolean YAsBoolean(Slot var0) {
      if (var0 instanceof VisualInventory.Type_m var1 && (var1.l == null || qAsBoolean(var1.l.getIndex(), var1.l.getStack()))) {
         return true;
      }

      return false;
   }

   public static void YAsVoid(int var0, int var1) {
      X.KAsVoid(var0, var1);
      GAsVoid();
   }

   public static void OAsVoid(ScreenHandler var0, Slot var1, int var2) {
      if (!DAsBoolean()) {
         MinecraftClient var3 = MinecraftClient.getInstance();
         if (var1 != null && !YAsBoolean(var1) && var3.interactionManager != null && var0.getCursorStack().isEmpty()) {
            int var4 = rAsInt(var2);
            Slot var5 = null;

            for (Slot var7 : var0.slots) {
               if (var7.inventory == var3.player.getInventory() && var7.getIndex() == var4) {
                  var5 = var7;
                  break;
               }
            }

            if (var5 != null
               && var5.id != var1.id
               && !qAsBoolean(var4, var5.getStack())
               && var1.canTakeItems(var3.player)
               && var5.canTakeItems(var3.player)
               && (var5.getStack().isEmpty() || var1.canInsert(var5.getStack()) && var5.getStack().getCount() <= var1.getMaxItemCount(var5.getStack()))
               && (var1.getStack().isEmpty() || var5.canInsert(var1.getStack()))) {
               if (var4 < 9) {
                  if (!FakeSpawnerController.ZAsMa().XAsBoolean(var0, List.of(new FakeSpawnerTransaction.Type_z(var1.id, var4, SlotActionType.SWAP)))) {
                     var3.interactionManager.clickSlot(var0.syncId, var1.id, var4, SlotActionType.SWAP, var3.player);
                  }
               } else {
                  int var10 = 0;

                  while (var1.inventory == var3.player.getInventory() && var1.getIndex() == var10) {
                     var10++;
                  }

                  if (FakeSpawnerController.ZAsMa().vAsBoolean(var10)) {
                     return;
                  }

                  List var9 = List.of(
                     new FakeSpawnerTransaction.Type_z(var1.id, var10, SlotActionType.SWAP),
                     new FakeSpawnerTransaction.Type_z(var5.id, var10, SlotActionType.SWAP),
                     new FakeSpawnerTransaction.Type_z(var1.id, var10, SlotActionType.SWAP)
                  );
                  if (!FakeSpawnerController.ZAsMa().XAsBoolean(var0, var9)) {
                     FakeSpawnerTransaction.RAsBoolean(var0, "visual number-key swap", var9, VisualInventory::vAsVoid, VisualInventory::vAsVoid);
                  }
               }

               X.KAsVoid(var2, var4);
               if (var1 instanceof VisualInventory.Type_m) {
                  ZAsVoid(var1);
               } else if (var1.inventory == var3.player.getInventory()) {
                  X.KAsVoid(var1.getIndex(), var1.getIndex());
               }

               GAsVoid();
            }
         }
      }
   }

   public static void aAsVoid(int var0) {
      if (!DAsBoolean()) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         if (!jAsBoolean()) {
            var1.player.getInventory().setSelectedSlot(var0);
         } else {
            int var2 = rAsInt(var0);
            if (var2 >= 0) {
               F = false;
               if (var2 < 9) {
                  var1.player.getInventory().setSelectedSlot(var2);
                  t = var2;
                  P = var0;
               } else if (var1.interactionManager != null
                  && var1.player.currentScreenHandler == var1.player.playerScreenHandler
                  && var1.player.playerScreenHandler.getCursorStack().isEmpty()) {
                  for (int var6 = 0; var6 < 9; var6++) {
                     if (!FakeSpawnerController.ZAsMa().vAsBoolean(var6)) {
                        FakeSpawnerController var8 = FakeSpawnerController.ZAsMa();
                        if (var1.player.getInventory().getStack(var2).isOf(Items.BONE)) {
                           var8.nAsVoid(var6);
                        }

                        if (var1.player.getInventory().getStack(var6).isOf(Items.BONE)) {
                           var8.nAsVoid(var2);
                        }

                        var1.interactionManager.clickSlot(0, var2, var6, SlotActionType.SWAP, var1.player);
                        X.NAsVoid(var2, var6);
                        vAsVoid();
                        var1.player.getInventory().setSelectedSlot(var6);
                        t = var6;
                        P = var0;
                        return;
                     }
                  }

                  if (FakeSpawnerController.ZAsMa().WAsBoolean(var2, var0)) {
                     X.NAsVoid(var2, var0);
                     X.KAsVoid(var0, var0);
                     var1.player.getInventory().setSelectedSlot(var0);
                     t = var0;
                     P = var0;
                     GAsVoid();
                  }
               }
            } else {
               int var3 = 0;

               int var4;
               while (true) {
                  if (var3 >= 36) {
                     var3 = var0;

                     for (int var7 = 0; var7 < 9; var7++) {
                        if (FakeSpawnerController.ZAsMa().vAsBoolean(var7)) {
                           var3 = var7;
                           break;
                        }
                     }

                     var1.player.getInventory().setSelectedSlot(var3);
                     t = var3;
                     P = var0;
                     F = true;
                     return;
                  }

                  if (!qAsBoolean(var3, var1.player.getInventory().getStack(var3)) && var1.player.getInventory().getStack(var3).isEmpty()) {
                     var4 = var3;
                     if (var3 < 9) {
                        break;
                     }

                     var4 = var0;
                     if (FakeSpawnerController.ZAsMa().WAsBoolean(var3, var0)) {
                        X.NAsVoid(var3, var0);
                        X.KAsVoid(var0, var0);
                        vAsVoid();
                        break;
                     }
                  }

                  var3++;
               }

               var1.player.getInventory().setSelectedSlot(var4);
               t = var4;
               P = var0;
               F = true;
            }
         }
      }
   }

   public static final class Type_m extends Slot {
      private final Slot l;
      private final int D;

      Type_m(Slot var1, Slot var2) {
         super((Inventory)(var2 == null ? VisualInventory.E : var2.inventory), var2 == null ? 0 : var2.getIndex(), var1.x, var1.y);
         this.l = var2;
         this.D = var1.getIndex();
         this.id = var2 == null ? -1 : var2.id;
      }

      public ItemStack getStack() {
         return this.l == null ? ItemStack.EMPTY : VisualInventory.pAsItemStack(this.l.getIndex(), this.l.getStack());
      }

      public boolean canInsert(ItemStack stack) {
         return this.l != null && !VisualInventory.YAsBoolean(this) && this.l.canInsert(stack);
      }

      public boolean canTakeItems(PlayerEntity playerEntity) {
         return this.l != null
            && (!VisualInventory.YAsBoolean(this) || FakeSpawnerController.ZAsMa().hAsInt(this.l.getIndex(), this.l.getStack()) > 0)
            && this.l.canTakeItems(playerEntity);
      }

      public int getMaxItemCount() {
         return this.l == null ? 0 : this.l.getMaxItemCount();
      }

      public int getMaxItemCount(ItemStack stack) {
         return this.l == null ? 0 : this.l.getMaxItemCount(stack);
      }

      public boolean isEnabled() {
         return this.l != null && this.l.isEnabled();
      }

      public Identifier getBackgroundSprite() {
         return this.l == null ? null : this.l.getBackgroundSprite();
      }

      public boolean disablesDynamicDisplay() {
         return this.l != null && this.l.disablesDynamicDisplay();
      }
   }
}
