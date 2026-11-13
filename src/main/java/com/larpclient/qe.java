package com.larpclient;

import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

public final class qe {
   public static qe.Type_b mAsQe_Type_b(ItemStack var0, BlockState var1, boolean var2) {
      double var3 = (double)var0.getMiningSpeedMultiplier(var1);
      if (var3 > 1.0) {
         for (RegistryEntry var6 : var0.getEnchantments().getEnchantments()) {
            if (var6.matchesKey(Enchantments.EFFICIENCY)) {
               int var7 = var0.getEnchantments().getLevel(var6);
               var3 += (double)var7 * (double)var7 + 1.0;
            }
         }
      }

      int var8 = var2 && var0.isDamageable() && var0.getMaxDamage() - var0.getDamage() <= 1 ? 0 : 1;
      return new qe.Type_b(var3, !var1.isToolRequired() || var0.isSuitableFor(var1), var8 != 0);
   }

   public static int lAsInt(List<qe.Type_b> var0, int var1) {
      int var2 = var1;

      for (int var3 = 0; var3 < var0.size(); var3++) {
         qe.Type_b var4 = var0.get(var3);
         qe.Type_b var5 = var0.get(var2);
         if (var4.usable() && (!var5.usable() || var4.harvest() && !var5.harvest() || var4.harvest() == var5.harvest() && var4.speed() > var5.speed())) {
            var2 = var3;
         }
      }

      return var2;
   }

   private qe() {
   }

   public static record Type_b(double speed, boolean harvest, boolean usable) {
   }

   public static final class Type_q {
      private int K = -1;
      private int C = -1;
      private boolean R;

      public Type_q() {
      }

      public void PAsVoid() {
         this.K = this.C = -1;
         this.R = true;
      }

      public int kAsInt(int var1, int var2) {
         if (this.C >= 0 && var1 != this.C) {
            this.PAsVoid();
         }

         if (!this.R && var1 != var2) {
            if (this.K < 0) {
               this.K = var1;
            }

            this.C = var2;
            return var2;
         } else {
            return var1;
         }
      }

      public int qAsInt(int var1, boolean var2) {
         int var3 = var2 && var1 == this.C && this.K >= 0 ? this.K : var1;
         this.K = this.C = -1;
         this.R = false;
         return var3;
      }
   }
}
