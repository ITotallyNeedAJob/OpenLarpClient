package com.larpclient.module;

import net.minecraft.text.Text;

public class AutoDisconnect extends Module {
   private final NumberSetting health = this.addSetting(new NumberSetting("Health", "Disconnect below health", 4.0, 1.0, 20.0, 0.5));

   public AutoDisconnect() {
      super("AutoDisconnect", "Disconnect at low health", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      double var1;
      float var2;
      if (this.isInGame()
         && ((var1 = (double)mc.player.getHealth() - this.health.getValue()) == 0.0 ? 0 : (var1 < 0.0 ? -1 : 1)) <= 0
         && ((var2 = mc.player.getHealth() - 0.0F) == 0.0F ? 0 : (var2 < 0.0F ? -1 : 1)) > 0) {
         mc.player.networkHandler.getConnection().disconnect(Text.literal("LarpClient AutoDisconnect - Health too low!"));
         this.disable();
      }
   }
}
