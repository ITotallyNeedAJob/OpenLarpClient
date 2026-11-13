package com.larpclient.module;

import com.larpclient.AppearanceAssets;

public final class LARP extends Module {
   private static LARP Z;
   public final BooleanSetting shareAppearance = this.addSetting(new BooleanSetting("Share Appearance", "Retired sharing preference", false));
   public final BooleanSetting receiveCosmetics = this.addSetting(new BooleanSetting("Receive Cosmetics", "Retired receive preference", true));

   public LARP() {
      super("LARP", "Copy a skin, cape and name locally", Category.PLAYER, -1);
      this.shareAppearance.visibleWhen(() -> false);
      this.receiveCosmetics.visibleWhen(() -> false);
      Z = this;
   }

   public static LARP BAsLARP() {
      return Z;
   }

   @Override
   public void onEnable() {
      AppearanceAssets.GAsVoid();
   }

   @Override
   public void onDisable() {
      AppearanceAssets.hAsVoid();
   }

   @Override
   public String getStatus() {
      return (AppearanceAssets.O.isEmpty() ? "" : AppearanceAssets.O + " · ") + AppearanceAssets.y;
   }
}
