package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.b9;
import com.larpclient.bd;

public final class AmbientParticles extends Module {
   private static AmbientParticles Z;
   public final ModeSetting style = this.addSetting(new ModeSetting("Style", "Flying world ambience", "Both", "Both", "Sparkles", "Comets"));
   public final NumberSetting H = this.DAsNumberSetting("Radius", "Spawn region in blocks", 12.0, 4.0, 32.0, 1.0);
   public final NumberSetting x = this.DAsNumberSetting("Height", "Spawn region height in blocks", 10.0, 2.0, 24.0, 1.0);
   public final NumberSetting W = this.DAsNumberSetting("Density", "Sparkles per second; fills the region on enable", 160.0, 0.0, 512.0, 1.0);
   public final NumberSetting v = this.DAsNumberSetting("Lifetime", "Sparkle lifetime in seconds", 5.0, 1.0, 8.0, 0.25);
   public final NumberSetting G = this.DAsNumberSetting("Speed", "Motion multiplier; zero freezes flight", 1.5, 0.0, 6.0, 0.1);
   public final NumberSetting E = this.DAsNumberSetting("Vertical Travel", "Peak-to-peak rise and fall in blocks", 3.0, 0.0, 8.0, 0.25);
   public final NumberSetting N = this.DAsNumberSetting("Sideways Travel", "Peak-to-peak sideways motion in blocks", 2.5, 0.0, 8.0, 0.25);
   public final NumberSetting z = this.DAsNumberSetting("Motion Period", "Seconds per rise and fall at Speed 1", 5.0, 1.0, 12.0, 0.25);
   public final NumberSetting C = this.DAsNumberSetting("Maximum Particles", "Hard ambient sprite limit, independent of combat", 1536.0, 128.0, 2048.0, 64.0);
   public final NumberSetting h = this.DAsNumberSetting("Size", "Sparkle radius in blocks", 0.05, 0.015, 0.15, 0.005);
   public final NumberSetting Q = this.DAsNumberSetting("Comet Count", "Maximum simultaneous flying comets", 12.0, 0.0, 32.0, 1.0);
   public final NumberSetting O = this.DAsNumberSetting("Trail Length", "Comet history in seconds", 0.8, 0.2, 2.0, 0.1);
   public final NumberSetting T = this.DAsNumberSetting("Trail Width", "Comet ribbon width in blocks", 0.025, 0.01, 0.08, 0.005);
   public final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use the current theme palette", true));
   public final ColorSetting color = this.addSetting(new ColorSetting("Color", "Sparkles and comet heads", -1517057));
   public final ColorSetting tailColor = this.addSetting(new ColorSetting("Tail Color", "Custom comet tails and sparkle fade", -6794006));
   public final NumberSetting J = this.DAsNumberSetting("Opacity", "Ambient effect strength", 0.7, 0.1, 1.0, 0.05);
   public final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Shared world glow", true));
   private final b9 V = new b9();

   private NumberSetting DAsNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public AmbientParticles() {
      super("Ambient Particles", "Add sparkles and comets around you", Category.VISUALS, -1);
      Z = this;
      this.W.visibleWhen(() -> !this.style.pAsBoolean("Comets"));
      this.v.visibleWhen(() -> !this.style.pAsBoolean("Comets"));

      for (Setting var4 : new Setting[]{this.Q, this.O, this.T}) {
         var4.visibleWhen(() -> !this.style.pAsBoolean("Sparkles"));
      }

      this.z.visibleWhen(() -> this.E.getValue() > 0.0 || this.N.getValue() > 0.0);
      this.color.visibleWhen(() -> !this.themeColors.getValue());
      this.tailColor.visibleWhen(() -> !this.themeColors.getValue());
      this.themeColors.onChanged(var0 -> ClientLog.GAsVoid("Ambient Particles", "Theme colors: {}", var0));
      this.style.onChanged(var1 -> this.pAsVoid());
   }

   public b9.Type_d qAsB9_Type_d() {
      return new b9.Type_d(
         !this.style.pAsBoolean("Comets"),
         !this.style.pAsBoolean("Sparkles"),
         this.H.getValue(),
         this.x.getValue(),
         this.W.getValue(),
         this.v.getValue(),
         this.G.getValue(),
         this.h.getValue(),
         this.Q.getValue().intValue(),
         this.O.getValue(),
         this.T.getValue(),
         this.themeColors.getValue() ? bd.w : this.color.getValue(),
         this.themeColors.getValue() ? bd.Q : this.tailColor.getValue(),
         this.J.getValue(),
         this.bloom.getValue(),
         this.E.getValue(),
         this.N.getValue(),
         this.z.getValue(),
         this.C.getValue().intValue()
      );
   }

   public static AmbientParticles CAsAmbientParticles() {
      return Z;
   }

   public b9 OAsB9() {
      return this.V;
   }

   public void pAsVoid() {
      this.V.eAsVoid();
   }

   @Override
   public void onTick() {
      if (this.isInGame() && !mc.isPaused()) {
         this.V.GAsVoid(mc.gameRenderer.getCamera().getCameraPos(), this.qAsB9_Type_d());
      }
   }

   @Override
   public void onEnable() {
      this.pAsVoid();
      ClientLog.GAsVoid(
         "Ambient Particles",
         "Enabled {}; density={}/s; sprite limit={}; comets={}; vertical travel={} blocks",
         this.style.getValue(),
         this.W.getValue(),
         this.C.getValue(),
         this.Q.getValue(),
         this.E.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.pAsVoid();
   }
}
