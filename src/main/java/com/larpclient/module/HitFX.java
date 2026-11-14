package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ParticleEmitterEngine;
import com.larpclient.SharedRenderEffects;
import com.larpclient.WorldColorPostEffect;
import com.larpclient.FadeCurve;
import com.larpclient.UiStyle;
import com.larpclient.ColorUtil;
import com.larpclient.LandQuery;
import com.larpclient.EffectRenderParams;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public final class HitFX extends Module {
   private static HitFX g;
   private final ModeSetting preset = this.addSetting(
      new ModeSetting(
         "Preset", "Choose a complete impact style; its behavior is explained below", "Clean", "Clean", "Energy", "Spark", "Petal", "Heavy", "Cash", "Bloom"
      )
   );
   private final BooleanSetting screenImpact = this.addSetting(new BooleanSetting("Screen Impact", "Brief subtle exposure and edge pulse", false));
   private final NumberSetting ringThickness = this.addSetting(new NumberSetting("Ring Thickness", "Shockwave width", 0.045, 0.01, 0.2, 0.005));
   private Vec3d o = Vec3d.ZERO;
   private final EffectRenderParams X = new EffectRenderParams();
   private final ModeSetting particleType = this.addSetting(
      new ModeSetting(
         "Particle Type",
         "Accent shape for Clean; the other presets choose their own shapes",
         "Star",
         "Spark",
         "Glow",
         "Circle",
         "Star",
         "Triangle",
         "Streak",
         "Textured"
      )
   );
   private final NumberSetting amount = this.addSetting(
      new NumberSetting("Amount", "Burst density: higher values emit more particles in every layer", 18.0, 1.0, 100.0, 1.0)
   );
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "World-space particle size", 0.08, 0.02, 0.3, 0.01));
   private final NumberSetting lifetime = this.addSetting(new NumberSetting("Lifetime", "Seconds", 0.8, 0.2, 4.0, 0.1));
   private final NumberSetting maximum = this.addSetting(
      new NumberSetting("Maximum", "HitFX particle limit; raise only if repeated bursts reach the limit", 1000.0, 100.0, 2000.0, 100.0)
   );
   private final NumberSetting distance = this.addSetting(new NumberSetting("Distance", "Render distance", 64.0, 8.0, 128.0, 8.0));
   private final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use the current theme palette", true));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Start color", UiStyle.w));
   private final ColorSetting gradient = this.addSetting(new ColorSetting("Gradient", "End color", UiStyle.Q));
   private final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Submit glow mask", true));
   private final BooleanSetting particles = this.addSetting(new BooleanSetting("Particles", "Emit particles", true));
   private final BooleanSetting hitRings = this.addSetting(new BooleanSetting("Hit Rings", "Expanding surface or airborne impact rings", true));
   private final NumberSetting ringSize = this.addSetting(new NumberSetting("Ring Size", "Final radius", 0.8, 0.2, 2.0, 0.1));
   private final NumberSetting ringSpeed = this.addSetting(new NumberSetting("Ring Speed", "Expansion multiplier", 1.0, 0.5, 3.0, 0.1));
   private final BooleanSetting entityHitColor = this.addSetting(new BooleanSetting("Entity Hit Color", "Tint hurt entity models", false));
   private final BooleanSetting hitTrails = this.addSetting(new BooleanSetting("Hit Trails", "Additional streaks on impact", true));
   private final NumberSetting ringFade = this.addSetting(new NumberSetting("Ring Fade", "Ring lifetime in seconds", 0.7, 0.2, 3.0, 0.1));
   private final NumberSetting bloomCount = this.addSetting(fAsNumberSetting("Bloom Count", "Soft sprites per impact", 15.0, 3.0, 30.0, 1.0));
   private final NumberSetting y = this.addSetting(fAsNumberSetting("Bloom Size", "Full sprite width in blocks", 0.25, 0.05, 0.5, 0.01));
   private final NumberSetting z = this.addSetting(fAsNumberSetting("Bloom Lifetime", "Seconds before the sprites expire", 1.5, 0.5, 3.0, 0.1));
   private final NumberSetting x = this.addSetting(fAsNumberSetting("Bloom Spread", "Initial travel in blocks per tick", 0.08, 0.02, 0.15, 0.01));
   private final NumberSetting bloomGravity = this.addSetting(fAsNumberSetting("Bloom Gravity", "Downward acceleration per tick", 0.015, 0.005, 0.03, 0.001));
   private final NumberSetting bloomFadeSpeed = this.addSetting(fAsNumberSetting("Bloom Fade Speed", "Higher values fade faster", 1.0, 0.3, 2.0, 0.1));
   private final BooleanSetting bloomColorAnimation = this.addSetting(
      new BooleanSetting("Bloom Color Animation", "Cycle each sprite through rainbow colors", false)
   );
   private Object e;
   private Object v;
   private int l = -1;

   public boolean vAsBoolean() {
      return this.isOperational() && this.entityHitColor.getValue();
   }

   public int lAsInt() {
      return this.themeColors.getValue() ? UiStyle.w : this.color.getValue();
   }

   private int uAsInt() {
      return this.themeColors.getValue() ? UiStyle.Q : this.gradient.getValue();
   }

   private static NumberSetting fAsNumberSetting(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new NumberSetting(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 1.0 ? "%.0f" : (var8 >= 0.1 ? "%.1f" : (var8 >= 0.01 ? "%.2f" : "%.3f")), this.getValue());
         }
      };
   }

   public HitFX() {
      super("HitFX", "Add particles and rings to hits", Category.VISUALS, -1);
      g = this;

      for (Setting var4 : new Setting[]{this.ringSize, this.ringSpeed, this.ringFade, this.ringThickness}) {
         var4.visibleWhen(() -> this.hitRings.getValue());
      }

      this.particleType.visibleWhen(() -> this.particles.getValue() && this.preset.pAsBoolean("Clean"));

      for (Setting var11 : new Setting[]{this.amount, this.size, this.lifetime, this.hitTrails}) {
         var11.visibleWhen(() -> this.particles.getValue() && !this.preset.pAsBoolean("Bloom"));
      }

      for (Setting var12 : new Setting[]{this.bloomCount, this.y, this.z, this.x, this.bloomGravity, this.bloomFadeSpeed, this.bloomColorAnimation}) {
         var12.visibleWhen(() -> this.particles.getValue() && this.preset.pAsBoolean("Bloom"));
      }

      this.color.visibleWhen(() -> !this.themeColors.getValue());
      this.gradient
         .visibleWhen(() -> !this.themeColors.getValue() && (this.hitRings.getValue() || this.particles.getValue() && !this.preset.pAsBoolean("Bloom")));
      this.bloomColorAnimation.visibleWhen(() -> !this.themeColors.getValue() && this.particles.getValue() && this.preset.pAsBoolean("Bloom"));
      this.themeColors.onChanged(var0 -> ClientLog.GAsVoid("HitFX", "Theme colors: {}", var0));
      this.preset.onChanged(var0 -> ClientLog.GAsVoid("HitFX", "Preset changed: {}", var0));
   }

   public Setting<?> tAsSetting() {
      return this.particles;
   }

   public static HitFX tAsHitFX() {
      return g;
   }

   public Setting<?> rAsSetting() {
      return this.preset;
   }

   public List<Setting<?>> AAsList() {
      return List.of(this.preset, this.particles, this.particleType, this.amount, this.size, this.lifetime, this.hitTrails, this.bloomCount, this.y, this.z);
   }

   public List<Setting<?>> RAsList() {
      return List.of(this.hitRings, this.ringSize, this.ringSpeed);
   }

   public List<Setting<?>> tAsList() {
      return List.of(this.themeColors, this.color, this.gradient, this.bloom, this.bloomColorAnimation);
   }

   public List<Setting<?>> eAsList() {
      return List.of(
         this.ringThickness, this.ringFade, this.x, this.bloomGravity, this.bloomFadeSpeed, this.screenImpact, this.entityHitColor, this.maximum, this.distance
      );
   }

   public String xAsString() {
      String var1 = this.preset.getValue();

      return switch (var1) {
         case "Bloom" -> "Bloom: soft glowing sprites with tumbling drift, gravity and a smooth fade. Choose a fixed color or animated rainbow.";
         case "Energy" -> "Energy: bright energy flares, drifting light and a delayed second ring.";
         case "Spark" -> "Spark: a dense spray of narrow sparks and streaks that slow and fade around the hit.";
         case "Petal" -> "Petal: a cloud of gently turning petals with light sparks and a soft glow.";
         case "Cash" -> "Cash: tumbling $100 bills with printed fronts and backs, curved paper and a gentle falling flutter.";
         case "Heavy" -> "Heavy: chunky tumbling shards, stronger outward motion and a delayed second ring.";
         default -> "Clean: a full, balanced burst of colorful accents with light sparks. Particle Type changes its accent shape.";
      };
   }

   @Override
   public void onEnable() {
      this.e = this.v = null;
      this.l = -1;
      ClientLog.GAsVoid(
         "HitFX", "Impact style={}; contact-local bursts, surface/airborne rings and tumbling Heavy shards; particle counts retained", this.preset.getValue()
      );
   }

   @Override
   public void onTick() {
      if (this.isInGame() && this.preset.pAsBoolean("Bloom")) {
         ClientPlayerEntity var1 = this.getPlayer();
         if (this.e != var1 || this.v != getMc().world) {
            this.e = var1;
            this.v = getMc().world;
            this.l = -1;
         }

         if (var1.hurtTime == 10 && var1.age != this.l) {
            this.l = var1.age;
            this.dAsVoid(var1);
         }
      } else {
         this.e = this.v = null;
         this.l = -1;
      }
   }

   public void dAsVoid(LivingEntity var1) {
      if (this.isEnabled() && !Halloween.bAsBoolean()) {
         ClientPlayerEntity var2 = this.getPlayer();
         Vec3d var3 = var2 != null && var2 != var1 ? var2.getEyePos() : var1.getBoundingBox().getCenter();
         Vec3d var4 = var2 != null && var2 != var1 ? var2.getRotationVec(1.0F) : new Vec3d(0.0, 0.0, 1.0);
         com.larpclient.GroundSample var5 = com.larpclient.GroundSample.sample(var1.getBoundingBox(), var3, var4, (var0, var1x) -> LandQuery.AAsLq_Type_l(getMc().world, var0, var1x));
         this.KAsVoid(SharedRenderEffects.B, var1.getEntityPos(), (double)var1.getWidth(), (double)var1.getHeight(), var5);
      }
   }

   public void HAsVoid(ParticleEmitterEngine var1, Vec3d var2, double var3, double var5, Vec3d var7) {
      if (this.isEnabled()) {
         this.KAsVoid(var1, var2, var3, var5, new com.larpclient.GroundSample(var2.add(0.0, var5 * 0.5, 0.0), var7, var2.add(0.0, 0.035, 0.0), new Vec3d(0.0, 1.0, 0.0)));
      }
   }

   public void KAsVoid(ParticleEmitterEngine var1, Vec3d var2, double var3, double var5, com.larpclient.GroundSample var7) {
      if (this.isEnabled()) {
         this.o = var7.normal();
         this.X.O = this.maximum.getValue().intValue();
         this.X.d = this.distance.getValue();
         Vec3d var8 = var7.point();
         this.X.K = 1;
         this.X.B = EffectRenderParams.Type_o.BOX;
         this.X.z = this.X.c = Math.max(0.08, var3 * 0.45);
         this.X.s = Math.min(0.4, var5 * 0.22);
         this.X.I = this.lAsInt();
         this.X.A = this.uAsInt();
         this.X.i = this.bloom.getValue();
         this.X.N = 0.0;
         this.X.E = EffectRenderParams.Type_s.NONE;
         this.X.h = var7.groundY();
         this.X.y = var7.ringNormal();
         this.X.G = this.X.Y = false;
         this.X.l = 1.0;
         this.X.T = Double.NaN;
         double var9 = this.amount.getValue() / 18.0;
         double var11 = this.size.getValue() / 0.08;
         double var13 = this.lifetime.getValue() / 0.8;
         boolean var15 = this.preset.pAsBoolean("Heavy");
         boolean var16 = this.preset.pAsBoolean("Petal");
         boolean var17 = this.preset.pAsBoolean("Energy");
         boolean var18 = this.preset.pAsBoolean("Spark");
         this.X.H = var17 ? EffectRenderParams.Type_i.HITFX : EffectRenderParams.Type_i.DEFAULT;
         if (this.particles.getValue() && this.preset.pAsBoolean("Bloom")) {
            this.X.H = EffectRenderParams.Type_i.BLOOM;
            this.X.B = EffectRenderParams.Type_o.POINT;
            this.X.q = ParticleEmitterEngine.Type_a.Glow;
            this.X.M = this.bloomCount.getValue().intValue();
            this.X.g = this.X.R = this.y.getValue() * 0.5;
            this.X.x = this.X.v = this.z.getValue();
            this.X.I = this.X.A = this.lAsInt();
            this.X.i = this.bloom.getValue();
            this.X.j = EffectRenderParams.Type_g.ADDITIVE;
            this.X.w = this.x.getValue() * 20.0;
            this.X.Q = 1.0;
            this.X.b = 0.0;
            this.X.a = this.bloomGravity.getValue() * 400.0;
            this.X.D = -Math.log(0.98) * 20.0;
            this.X.Z = this.X.e = this.X.P = 0.0;
            this.X.L = this.X.f = this.X.J = 0.0;
            this.X.G = true;
            this.X.Y = !this.themeColors.getValue() && this.bloomColorAnimation.getValue();
            this.X.t = FadeCurve.Type_r.LINEAR;
            this.X.l = this.bloomFadeSpeed.getValue();
            this.X.o = FadeCurve.Type_z.CONSTANT;
            this.X.T = Math.toRadians(20.0);
            this.X.V = Math.toRadians(80.0);
            var1.pAsVoid(var2.add(0.0, var5 * 0.5 + 0.1, 0.0), this.X);
         } else if (this.particles.getValue() && this.preset.pAsBoolean("Cash")) {
            this.tAsVoid(
               var1,
               var8,
               ParticleEmitterEngine.Type_a.Cash,
               18.0 * var9,
               0.17 * var11,
               0.25 * var11,
               0.9 * var13,
               1.5 * var13,
               1.25,
               1.15,
               0.22,
               EffectRenderParams.Type_g.ALPHA
            );
         } else if (this.particles.getValue()) {
            this.tAsVoid(
               var1,
               var8,
               ParticleEmitterEngine.Type_a.Glow,
               (double)(var17 ? 10 : (!var18 && !var16 ? 6 : 4)) * var9,
               0.065 * var11,
               0.13 * var11,
               0.3 * var13,
               0.6 * var13,
               0.35,
               0.05,
               0.35,
               EffectRenderParams.Type_g.ADDITIVE
            );
            this.tAsVoid(
               var1,
               var8,
               ParticleEmitterEngine.Type_a.Spark,
               (double)(var16 ? 6 : (var18 ? 36 : (var15 ? 18 : (var17 ? 12 : 8)))) * var9,
               0.018 * var11,
               0.04 * var11,
               0.28 * var13,
               0.55 * var13,
               var15 ? 4.2 : 3.2,
               0.8,
               0.08,
               EffectRenderParams.Type_g.ADDITIVE
            );
            this.tAsVoid(
               var1,
               var8,
               var16
                  ? ParticleEmitterEngine.Type_a.Petal
                  : (
                     var15
                        ? ParticleEmitterEngine.Type_a.Shard
                        : (
                           var17
                              ? ParticleEmitterEngine.Type_a.Diamond
                              : (var18 ? ParticleEmitterEngine.Type_a.Streak : ParticleEmitterEngine.Type_a.valueOf(this.particleType.getValue()))
                        )
                  ),
               (double)(var16 ? 52 : (var15 ? 38 : (var17 ? 34 : (var18 ? 14 : 36)))) * var9,
               (var18 ? 0.025 : (var15 ? 0.07 : 0.045)) * var11,
               (var18 ? 0.065 : (var15 ? 0.14 : 0.09)) * var11,
               (var18 ? 0.35 : 0.65) * var13,
               (var18 ? 0.7 : 1.2) * var13,
               var16 ? 0.65 : (var15 ? 1.8 : 1.1),
               var16 ? 0.15 : (var15 ? 1.5 : 0.45),
               var16 ? 0.35 : (var17 ? 0.4 : 0.1),
               EffectRenderParams.Type_g.ALPHA
            );
         }

         if (this.particles.getValue() && this.hitTrails.getValue() && !this.preset.pAsBoolean("Bloom")) {
            this.tAsVoid(
               var1,
               var8,
               ParticleEmitterEngine.Type_a.Streak,
               6.0 * var9,
               0.025 * var11,
               0.055 * var11,
               0.25 * var13,
               0.5 * var13,
               3.0,
               0.1,
               0.0,
               EffectRenderParams.Type_g.ADDITIVE
            );
         }

         if (this.hitRings.getValue()) {
            this.X.G = this.X.Y = false;
            this.X.E = EffectRenderParams.Type_s.NONE;
            this.X.N = 0.0;
            this.X.l = 1.0;
            this.X.T = Double.NaN;
            this.X.H = var17 ? EffectRenderParams.Type_i.HITFX : EffectRenderParams.Type_i.DEFAULT;
            this.X.I = this.lAsInt();
            this.X.A = this.uAsInt();
            this.X.i = this.bloom.getValue();
            this.X.B = EffectRenderParams.Type_o.POINT;
            this.X.q = ParticleEmitterEngine.Type_a.Ring;
            this.X.M = 1;
            this.X.x = this.X.v = this.ringFade.getValue();
            this.X.p = this.ringSize.getValue() * this.ringSpeed.getValue();
            this.X.n = this.ringThickness.getValue();
            this.X.j = EffectRenderParams.Type_g.ADDITIVE;
            this.X.t = FadeCurve.Type_r.QUICK_FADE;
            var1.pAsVoid(var7.ring(), this.X);
            if (var15 || var17) {
               this.X.N = 0.075;
               this.X.p *= 1.25;
               var1.pAsVoid(var7.ring().add(var7.ringNormal().multiply(0.005)), this.X);
            }
         }

         if (this.screenImpact.getValue()) {
            WorldColorPostEffect.CAsVoid(this.lAsInt());
         }
      }
   }

   private void tAsVoid(
      ParticleEmitterEngine var1,
      Vec3d var2,
      ParticleEmitterEngine.Type_a var3,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      double var18,
      EffectRenderParams.Type_g var20
   ) {
      int var21 = var3 != ParticleEmitterEngine.Type_a.Spark && var3 != ParticleEmitterEngine.Type_a.Streak ? 0 : 1;
      int var22 = var3 == ParticleEmitterEngine.Type_a.Glow ? 1 : 0;
      this.X.E = var3 == ParticleEmitterEngine.Type_a.Shard ? EffectRenderParams.Type_s.BOUNCE : EffectRenderParams.Type_s.NONE;
      this.X.N = var22 == 0 && var21 == 0 ? 0.025 : 0.0;
      this.X.B = var22 != 0 ? EffectRenderParams.Type_o.BOX : EffectRenderParams.Type_o.SPHERE;
      this.X.b = var3 == ParticleEmitterEngine.Type_a.Cash ? 0.65 : (var3 == ParticleEmitterEngine.Type_a.Petal ? 0.35 : 0.15);
      this.X.I = var21 != 0 ? ColorUtil.xAsInt(this.lAsInt(), -1, 0.65) : ColorUtil.XAsInt(this.lAsInt(), var22 != 0 ? 0.52 : 0.92);
      this.X.A = ColorUtil.XAsInt(this.uAsInt(), var22 != 0 ? 0.16 : 0.65);
      if (var3 == ParticleEmitterEngine.Type_a.Cash) {
         this.X.I = this.X.A = -1;
      }

      this.X.i = this.bloom.getValue() && var20 == EffectRenderParams.Type_g.ADDITIVE;
      this.X.Q = var21 != 0 ? 0.65 : 1.0;
      double var23 = var3 != ParticleEmitterEngine.Type_a.Spark && var3 != ParticleEmitterEngine.Type_a.Streak ? 0.0 : var14 * 0.95;
      this.X.Z = this.o.x * var23;
      this.X.e = this.o.y * var23;
      this.X.P = this.o.z * var23;
      this.X.q = var3;
      this.X.M = (int)Math.round(var4);
      this.X.g = var6;
      this.X.R = var8;
      this.X.x = var10;
      this.X.v = var12;
      this.X.w = var21 != 0 ? var14 * 0.3 : var14;
      this.X.a = var16;
      this.X.L = var18;
      this.X.j = var20;
      this.X.D = var21 != 0 ? 2.4 : (var3 == ParticleEmitterEngine.Type_a.Petal ? 0.8 : 1.4);
      this.X.V = var3 == ParticleEmitterEngine.Type_a.Cash
         ? 3.2
         : (var3 == ParticleEmitterEngine.Type_a.Petal ? 2.1 : (var3 == ParticleEmitterEngine.Type_a.Shard ? 5.0 : 3.0));
      this.X.t = var22 != 0 ? FadeCurve.Type_r.QUICK_FADE : FadeCurve.Type_r.HOLD_FADE;
      this.X.o = var22 != 0 ? FadeCurve.Type_z.POP : (var21 != 0 ? FadeCurve.Type_z.SHRINK : FadeCurve.Type_z.CONSTANT);
      var1.pAsVoid(var2, this.X);
   }

   @Override
   public void onDisable() {
      this.e = this.v = null;
      this.l = -1;
      SharedRenderEffects.B.zAsVoid(1);
   }
}
