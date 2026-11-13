package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.MotionBlurEffect;

public final class MotionBlur extends Module {
   private static MotionBlur U;
   public final ModeSetting mode = this.addSetting(
      new ModeSetting("Mode", "Smooth follows camera turns; Classic blends recent frames", "Smooth", "Smooth", "Classic")
   );
   public final NumberSetting strength = this.addSetting(new NumberSetting("Strength", "Shutter length; higher leaves longer streaks", 0.5, 0.1, 1.0, 0.05));
   public final ModeSetting quality = this.addSetting(new ModeSetting("Quality", "Samples along each streak", "Medium", "Low", "Medium", "High"));
   public final NumberSetting maxLength = this.addSetting(new NumberSetting("Max Length", "Longest streak as a percent of the screen", 12.0, 2.0, 25.0, 1.0));

   public MotionBlur() {
      super("Motion Blur", "Smooth camera motion blur", Category.RENDER, -1);
      U = this;
      this.quality.visibleWhen(() -> this.mode.getValue().equals("Smooth"));
      this.maxLength.visibleWhen(() -> this.mode.getValue().equals("Smooth"));
   }

   public static MotionBlur XAsMotionBlur() {
      return U;
   }

   @Override
   public void onEnable() {
      ClientLog.GAsVoid("MotionBlur", "Enabled; mode={}; strength={}; quality={}", this.mode.getValue(), this.strength.getValue(), this.quality.getValue());
   }

   @Override
   public void onDisable() {
      MotionBlurEffect.QAsVoid();
   }
}
