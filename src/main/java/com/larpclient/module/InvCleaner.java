package com.larpclient.module;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class InvCleaner extends Module {
   private final BooleanSetting dirt = this.addSetting(new BooleanSetting("Dirt", "Drop dirt blocks", true));
   private final BooleanSetting cobble = this.addSetting(new BooleanSetting("Cobble", "Drop cobblestone", true));
   private final BooleanSetting gravel = this.addSetting(new BooleanSetting("Gravel", "Drop gravel", true));
   private final BooleanSetting seeds = this.addSetting(new BooleanSetting("Seeds", "Drop seeds", true));
   private final BooleanSetting rottenFlesh = this.addSetting(new BooleanSetting("Rotten Flesh", "Drop rotten flesh", true));
   private int C = 0;

   public InvCleaner() {
      super("InvCleaner", "Automatically drop junk items", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame() && mc.currentScreen == null) {
         if (this.C > 0) {
            this.C--;
         } else {
            for (int var1 = 0; var1 < 36; var1++) {
               ItemStack var2 = mc.player.getInventory().getStack(var1);
               if (!var2.isEmpty() && this.OAsBoolean(var2)) {
                  int var3 = var1 < 9 ? var1 + 36 : var1;
                  mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, var3, 1, SlotActionType.THROW, mc.player);
                  this.C = 3;
                  return;
               }
            }
         }
      }
   }

   private boolean OAsBoolean(ItemStack var1) {
      Item var2 = var1.getItem();
      if (!this.dirt.getValue() || var2 != Items.DIRT && var2 != Items.COARSE_DIRT) {
         if (this.cobble.getValue() && var2 == Items.COBBLESTONE) {
            return true;
         } else if (!this.gravel.getValue() || var2 != Items.GRAVEL && var2 != Items.FLINT) {
            return !this.seeds.getValue()
                  || var2 != Items.WHEAT_SEEDS && var2 != Items.BEETROOT_SEEDS && var2 != Items.MELON_SEEDS && var2 != Items.PUMPKIN_SEEDS
               ? this.rottenFlesh.getValue() && var2 == Items.ROTTEN_FLESH
               : true;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }
}
