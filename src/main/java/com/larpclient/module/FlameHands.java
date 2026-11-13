package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.FlameParticles;
import java.util.List;
import java.util.Locale;

public final class FlameHands extends Module {
   private static FlameHands x;
   public final ModeSetting hands = this.addSetting(new ModeSetting("Hands", "Hands that emit flames", "Both", "Both", "Main Only", "Off Only"));
   public final ModeSetting renderMode = this.addSetting(new ModeSetting("Render Mode", "Reference particle texture", "Bloom", "Bloom", "Glow"));
   public final ModeSetting colorMode = this.addSetting(new ModeSetting("Color Mode", "Particle color animation", "Static", "Static", "Gradient", "Rainbow"));
   public final ColorSetting primaryColor = this.addSetting(new ColorSetting("Primary Color", "Color at emission", -45036));
   public final ColorSetting secondaryColor = this.addSetting(new ColorSetting("Secondary Color", "Gradient destination", -14336));
   public final NumberSetting size = this.addSetting(UAsNumberSetting("Size", "Particle diameter", 0.25, 0.05, 0.8, 0.01));
   public final NumberSetting z = this.addSetting(UAsNumberSetting("Density", "Particles emitted per second per hand", 100.0, 10.0, 300.0, 5.0));
   public final NumberSetting lifetime = this.addSetting(UAsNumberSetting("Lifetime", "Particle life in seconds", 0.35, 0.1, 2.0, 0.05));
   public final NumberSetting buoyancy = this.addSetting(UAsNumberSetting("Buoyancy", "Upward acceleration", 0.5, 0.0, 2.0, 0.05));
   public final NumberSetting turbulence = this.addSetting(UAsNumberSetting("Turbulence", "Random swirling motion", 0.3, 0.0, 1.0, 0.05));
   public final NumberSetting inertiaLag = this.addSetting(UAsNumberSetting("Inertia Lag", "Response to camera and player movement", 0.5, 0.0, 1.0, 0.05));
   public final BooleanSetting onlyWhenHolding = this.addSetting(new BooleanSetting("Only When Holding", "Emit only from hands holding an item", false));

   private static NumberSetting UAsNumberSetting(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new NumberSetting(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getDisplayName() {
            return var0.equals("Inertia Lag") ? "Inertia/Lag" : var0;
         }

         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 1.0 ? "%.0f" : "%.2f", this.getValue());
         }
      };
   }

   public FlameHands() {
      super("Flame Hands", "Animated flames attached to first-person hands", Category.VISUALS, -1);
      x = this;
      this.primaryColor.visibleWhen(() -> !this.colorMode.pAsBoolean("Rainbow"));
      this.secondaryColor.visibleWhen(() -> this.colorMode.pAsBoolean("Gradient"));
      this.hands.onChanged(var0 -> FlameParticles.UAsVoid());
      this.onlyWhenHolding.onChanged(var0 -> FlameParticles.UAsVoid());
   }

   public static FlameHands pAsFlameHands() {
      return x;
   }

   public List<Setting<?>> MAsList() {
      return List.of(this.hands, this.onlyWhenHolding, this.renderMode, this.size, this.z, this.lifetime);
   }

   public List<Setting<?>> iAsList() {
      return List.of(this.colorMode, this.primaryColor, this.secondaryColor);
   }

   public List<Setting<?>> UAsList() {
      return List.of(this.buoyancy, this.turbulence, this.inertiaLag);
   }

   public boolean hAsBoolean(boolean var1, boolean var2) {
      return this.isEnabled()
         ? (!this.onlyWhenHolding.getValue() || var2) && (!this.hands.pAsBoolean("Main Only") || var1) && (!this.hands.pAsBoolean("Off Only") || !var1)
         : false;
   }

   public FlameParticles.Type_z nAsLh_Type_z() {
      return new FlameParticles.Type_z(
         this.size.getValue().floatValue(),
         this.z.getValue().floatValue(),
         this.lifetime.getValue().floatValue(),
         this.buoyancy.getValue().floatValue(),
         this.turbulence.getValue().floatValue(),
         this.inertiaLag.getValue().floatValue(),
         this.primaryColor.getValue(),
         this.secondaryColor.getValue(),
         this.colorMode.getValue()
      );
   }

   @Override
   public void onEnable() {
      FlameParticles.UAsVoid();
      ClientLog.XAsVoid("FlameHands", "Activated hand-attached reference particles");
   }

   @Override
   public void onDisable() {
      FlameParticles.UAsVoid();
      ClientLog.XAsVoid("FlameHands", "Cleared both hand particle histories");
   }

   @Override
   public void onTick() {
      FlameParticles.bAsVoid();
   }
}
