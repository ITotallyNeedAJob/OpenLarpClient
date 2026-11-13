package com.larpclient.module;

import com.larpclient.d4;
import com.larpclient.o9;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.ItemStack;

public class AutoEat extends Module {
   private final NumberSetting hunger = this.addSetting(new NumberSetting("Hunger", "Eat below this hunger", 14.0, 1.0, 20.0, 1.0));

   public AutoEat() {
      super("AutoEat", "Automatically eat food", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      double var6;
      if (this.isInGame() && ((var6 = (double)mc.player.getHungerManager().getFoodLevel() - this.hunger.getValue()) == 0.0 ? 0 : (var6 < 0.0 ? -1 : 1)) < 0) {
         int var1 = -1;
         int var2 = 0;

         for (int var3 = 0; var3 < 9; var3++) {
            ItemStack var4 = mc.player.getInventory().getStack(var3);
            FoodComponent var5 = (FoodComponent)var4.get(DataComponentTypes.FOOD);
            if (!var4.isEmpty() && var5 != null && var5.nutrition() > var2) {
               var2 = var5.nutrition();
               var1 = var3;
            }
         }

         o9.cAsVoid(d4.Type_q.FOOD, var1, var0 -> var0.get(DataComponentTypes.FOOD) != null);
      } else {
         this.onDisable();
      }
   }

   @Override
   public void onDisable() {
      o9.cAsVoid(d4.Type_q.FOOD);
   }
}
