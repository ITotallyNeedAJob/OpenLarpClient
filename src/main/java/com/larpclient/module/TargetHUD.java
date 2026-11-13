package com.larpclient.module;

import com.larpclient.HudRenderer;

public final class TargetHUD extends Module {
   public final ModeSetting equipmentPos = this.addSetting(
      new ModeSetting("Equipment Pos", "Place the equipment row at the top or bottom of the card", "Above", "Above", "Below")
   );
   public final ModeSetting healthBar = this.addSetting(new ModeSetting("Health Bar", "Place health above or below the target name", "Below", "Below", "Above"));
   public final ModeSetting handsItems = this.addSetting(
      new ModeSetting("Hands items", "Place hand items left or right of the armor", "Right", "Right", "Left")
   );

   public TargetHUD() {
      super("TargetHUD", "Show information about your last target", Category.RENDER, -1);
   }

   @Override
   public void onDisable() {
      HudRenderer.UAsVoid();
   }
}
