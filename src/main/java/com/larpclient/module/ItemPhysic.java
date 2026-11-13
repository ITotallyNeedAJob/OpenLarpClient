package com.larpclient.module;

import com.larpclient.ClientLog;

public final class ItemPhysic extends Module {
   private static ItemPhysic S;
   private final ModeSetting physics = this.addSetting(new ModeSetting("Physics", "Dropped-item presentation", "Normal", "Normal", "2D"));

   public ItemPhysic() {
      super("Item Physic", "Rest dropped items on the ground, float them in water and tumble them in the air", Category.VISUALS, -1);
      S = this;
   }

   public static ItemPhysic lAsItemPhysic() {
      return S;
   }

   public boolean wAsBoolean() {
      return this.physics.pAsBoolean("2D");
   }

   @Override
   public void onEnable() {
      ClientLog.GAsVoid("ItemPhysic", "Dropped-item render choreography enabled; native physics retained");
   }
}
