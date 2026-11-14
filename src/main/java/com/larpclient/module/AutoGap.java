package com.larpclient.module;

import com.larpclient.SkyModeState;
import com.larpclient.OutlineEffect;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class AutoGap extends Module {
   private final NumberSetting health = this.addSetting(new NumberSetting("Health", "Eat below this HP", 14.0, 2.0, 20.0, 1.0));
   private final BooleanSetting goldenApple = this.addSetting(new BooleanSetting("Golden Apple", "Use golden apples", true));
   private final BooleanSetting enchanted = this.addSetting(new BooleanSetting("Enchanted", "Use enchanted golden apples", true));

   public AutoGap() {
      super("AutoGap", "Automatically eat golden apples", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      double var2;
      if (this.isInGame() && ((var2 = (double)mc.player.getHealth() - this.health.getValue()) == 0.0 ? 0 : (var2 < 0.0 ? -1 : 1)) <= 0) {
         int var1 = this.enchanted.getValue() ? this.tAsInt(Items.ENCHANTED_GOLDEN_APPLE) : -1;
         if (var1 == -1 && this.goldenApple.getValue()) {
            var1 = this.tAsInt(Items.GOLDEN_APPLE);
         }

         OutlineEffect.cAsVoid(
            SkyModeState.Type_q.GOLDEN_APPLE,
            var1,
            var1x -> this.goldenApple.getValue() && var1x.isOf(Items.GOLDEN_APPLE) || this.enchanted.getValue() && var1x.isOf(Items.ENCHANTED_GOLDEN_APPLE)
         );
      } else {
         this.onDisable();
      }
   }

   @Override
   public void onDisable() {
      OutlineEffect.cAsVoid(SkyModeState.Type_q.GOLDEN_APPLE);
   }

   private int tAsInt(Item var1) {
      for (int var2 = 0; var2 < 9; var2++) {
         if (mc.player.getInventory().getStack(var2).isOf(var1)) {
            return var2;
         }
      }

      return -1;
   }
}
