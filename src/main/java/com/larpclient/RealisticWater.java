package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.NumberSetting;

public final class RealisticWater extends Module {
   private static RealisticWater k;
   public final NumberSetting intensity = this.addSetting(new NumberSetting("Intensity", "Strength of the water material, percent", 100.0, 0.0, 100.0, 5.0));
   public final NumberSetting waves = this.addSetting(new NumberSetting("Waves", "Height of rolling surface waves, percent", 60.0, 0.0, 100.0, 5.0));
   public final BooleanSetting worldReflections = this.addSetting(new BooleanSetting("World reflections", "Reflect visible surroundings; costs GPU time", true));
   public final BooleanSetting foam = this.addSetting(new BooleanSetting("Foam", "Shoreline foam and waterfall streaks", true));
   public final BooleanSetting caustics = this.addSetting(new BooleanSetting("Caustics", "Sunlight patterns on the bed of shallow water", true));
   public final BooleanSetting underwater = this.addSetting(
      new BooleanSetting("Underwater", "Clear depth color, light shafts and caustics beneath water", true)
   );
   public final BooleanSetting volumetric = this.addSetting(
      new BooleanSetting("Volumetric", "Poured or newly released water becomes persistent droplets; real fluid still controls swimming and drowning", false)
   );

   public RealisticWater() {
      super("Realistic Water", "Rolling waves, reflections, refraction, depth color and shoreline foam", Category.RENDER, -1);
      k = this;
      this.volumetric.onChanged(var0 -> {
         VolumetricWaterEngine.YAsVoid();
         WetReflectionEffect.TAsVoid();
      });
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && WetReflectionEffect.NAsBoolean() ? "Material fallback" : "";
   }

   public static RealisticWater tAsRealisticWater() {
      return k;
   }

   @Override
   public void onEnable() {
      WetReflectionEffect.TAsVoid();
   }

   @Override
   public void onDisable() {
      VolumetricWaterEngine.YAsVoid();
      WetReflectionEffect.TAsVoid();
      WetReflectionEffect.OAsVoid();
   }

   public static boolean wAsBoolean() {
      if (k != null && k.isEnabled()) {
         RealisticWater var0 = k;
         return var0 != null && var0.isRuntimeActive() && var0.underwater.getValue() && WetReflectionEffect.gAsBoolean();
      } else {
         return false;
      }
   }
}
