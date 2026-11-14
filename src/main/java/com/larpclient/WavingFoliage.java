package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.NumberSetting;

public final class WavingFoliage extends Module {
   private static WavingFoliage f;
   public final BooleanSetting leaves = this.addSetting(new BooleanSetting("Leaves", "Leaves and vines sway", true));
   public final BooleanSetting trees = this.addSetting(
      new BooleanSetting("Trees", "Bend natural trees from their planted base; log buildings stay still", true)
   );
   public final BooleanSetting plants = this.addSetting(new BooleanSetting("Plants", "Grass, flowers, crops and saplings bend from their base", true));
   public final NumberSetting strength = this.addSetting(new NumberSetting("Strength", "How far foliage sways", 1.0, 0.2, 3.0, 0.1));
   public final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Wind animation speed", 1.0, 0.2, 3.0, 0.1));
   public final NumberSetting gusts = this.addSetting(new NumberSetting("Gusts", "Variation in wind strength, percent", 60.0, 0.0, 100.0, 5.0));

   public WavingFoliage() {
      super("Waving Foliage", "Trees, grass, flowers and crops sway in wind and passing gusts", Category.RENDER, -1);
      f = this;
      this.trees.visibleWhen(() -> this.leaves.getValue());
      this.leaves.onChanged(var1 -> this.xAsVoid());
      this.trees.onChanged(var1 -> this.xAsVoid());
      this.plants.onChanged(var1 -> this.xAsVoid());
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && WetReflectionEffect.NAsBoolean() ? "Material fallback" : "";
   }

   public static WavingFoliage DAsWavingFoliage() {
      return f;
   }

   private void xAsVoid() {
      WetReflectionEffect.TAsVoid();
   }

   @Override
   public void onEnable() {
      this.xAsVoid();
   }

   @Override
   public void onDisable() {
      this.xAsVoid();
      WetReflectionEffect.OAsVoid();
   }
}
