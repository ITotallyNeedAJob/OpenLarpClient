package com.larpclient.module;

import com.larpclient.ClientLog;
import java.util.BitSet;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class ChestStealer extends Module {
   private final NumberSetting delay = this.addSetting(new NumberSetting("Delay", "Ticks between items", 2.0, 0.0, 10.0, 1.0));
   private int x;
   private GenericContainerScreenHandler z;
   private ItemStack[] a;
   private final BitSet q = new BitSet();
   private boolean N;

   public ChestStealer() {
      super("ChestStealer", "Automatically loot chests", Category.PLAYER, -1);
   }

   @Override
   public void onDisable() {
      this.LAsVoid();
   }

   private void LAsVoid() {
      this.x = 0;
      this.z = null;
      this.a = null;
      this.q.clear();
      this.N = false;
   }

   private void BAsVoid(GenericContainerScreenHandler var1) {
      if (var1 != this.z) {
         this.LAsVoid();
         this.z = var1;
      }

      int var2 = this.a != null && this.a.length == var1.slots.size() ? 0 : 1;
      if (var2 == 0) {
         for (int var3 = 0; var3 < this.a.length; var3++) {
            if (!ItemStack.areEqual(this.a[var3], var1.getSlot(var3).getStack())) {
               var2 = 1;
               break;
            }
         }
      }

      if (var2 != 0) {
         this.a = new ItemStack[var1.slots.size()];

         for (int var4 = 0; var4 < this.a.length; var4++) {
            this.a[var4] = var1.getSlot(var4).getStack().copy();
         }

         this.q.clear();
         this.N = false;
      }
   }

   private boolean AAsBoolean(GenericContainerScreenHandler var1, ItemStack var2, int var3) {
      for (int var4 = var3; var4 < var1.slots.size(); var4++) {
         Slot var5 = var1.getSlot(var4);
         ItemStack var6 = var5.getStack();
         if (var6.isEmpty()) {
            if (var5.canInsert(var2) && var5.getMaxItemCount(var2) > 0) {
               return true;
            }
         } else if (var2.isStackable() && ItemStack.areItemsAndComponentsEqual(var2, var6) && var6.getCount() < var5.getMaxItemCount(var6)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void onTick() {
      if (!this.isInGame() || !(mc.player.currentScreenHandler instanceof GenericContainerScreenHandler var1)) {
         this.LAsVoid();
      } else if (AutoSell.vAsBoolean(var1)) {
         this.LAsVoid();
      } else {
         this.BAsVoid(var1);
         if (this.x > 0) {
            this.x--;
         } else if (mc.interactionManager != null) {
            int var7 = var1.getRows() * 9;
            byte var3 = 0;

            for (int var4 = 0; var4 < var7; var4++) {
               Slot var5 = var1.getSlot(var4);
               ItemStack var6 = var5.getStack();
               if (!var6.isEmpty()) {
                  var3 = 1;
                  if (!this.q.get(var4) && var5.canTakeItems(mc.player) && this.AAsBoolean(var1, var6, var7)) {
                     this.q.set(var4);
                     mc.interactionManager.clickSlot(var1.syncId, var4, 0, SlotActionType.QUICK_MOVE, mc.player);
                     this.x = this.delay.getValue().intValue();
                     return;
                  }
               }
            }

            if (var3 == 0) {
               mc.player.closeHandledScreen();
               this.LAsVoid();
            } else if (!this.N) {
               this.N = true;
               ClientLog.XAsVoid("ChestStealer", "Waiting for inventory capacity or container update");
            }
         }
      }
   }
}
