package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.Category;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.Module;
import com.larpclient.module.NumberSetting;

public final class WetGround extends Module {
   private static WetGround F;
   public final ModeSetting style = this.addSetting(
      new ModeSetting("Style", "Patches of puddles or a reflective sheet across exposed ground", "Puddles", "Puddles", "Flooded")
   );
   public final NumberSetting puddles = this.addSetting(new NumberSetting("Puddles", "Coverage of exposed ground, percent", 45.0, 0.0, 100.0, 5.0));
   public final NumberSetting wetness = this.addSetting(
      new NumberSetting("Wetness", "Soaked look: darker soil, wider puddles and mirror-like reflections, percent", 50.0, 0.0, 100.0, 5.0)
   );
   public final NumberSetting shine = this.addSetting(new NumberSetting("Shine", "Strength of sky, world and sun reflections, percent", 70.0, 0.0, 100.0, 5.0));
   public final BooleanSetting worldReflections = this.addSetting(
      new BooleanSetting("World reflections", "Reflect visible trees, buildings and entities; costs GPU time", true)
   );
   public final BooleanSetting raindrops = this.addSetting(new BooleanSetting("Raindrops", "Expanding rings disturb puddle reflections", true));
   public final BooleanSetting wetterInRain = this.addSetting(new BooleanSetting("Wetter in rain", "Rain increases puddle coverage and sheen", true));

   public WetGround() {
      super("Wet Ground", "Damp soil, reflective puddles and rain rings on exposed terrain", Category.RENDER, -1);
      F = this;
      this.puddles.visibleWhen(() -> this.style.pAsBoolean("Puddles"));
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && WetReflectionEffect.zAsBoolean() ? "Material fallback" : "";
   }

   public static WetGround KAsWetGround() {
      return F;
   }

   @Override
   public void onEnable() {
      WetReflectionEffect.gAsVoid();
   }

   @Override
   public void onDisable() {
      WetReflectionEffect.OAsVoid();
   }
}
