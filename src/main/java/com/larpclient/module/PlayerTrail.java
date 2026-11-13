package com.larpclient.module;

import com.larpclient.BodyTrailEngine;
import com.larpclient.ClientLog;
import com.larpclient.ParticleEmitterEngine;
import com.larpclient.RibbonTrailEngine;
import com.larpclient.SharedRenderEffects;
import com.larpclient.ay;
import com.larpclient.bd;
import com.larpclient.bg;
import com.larpclient.ok;
import com.larpclient.x4;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;

public final class PlayerTrail extends Module {
   private static final int C = 3;
   private final ModeSetting style = this.addSetting(
      new ModeSetting("Style", "Movement effect", "Ribbon", "Ribbon", "Glow", "Comet", "Ghost Echo", "Helix", "Stardust")
   );
   private final ModeSetting alphaCurve = this.addSetting(
      new ModeSetting("Alpha Curve", "Trail lifetime fade", "LINEAR", "LINEAR", "SMOOTH", "FADE_IN_OUT", "HOLD_FADE", "QUICK_FADE")
   );
   private final ModeSetting widthCurve = this.addSetting(
      new ModeSetting("Width Curve", "Strand taper", "TAPER_TAIL", "CONSTANT", "TAPER_TAIL", "TAPER_BOTH", "HEAD_HEAVY")
   );
   private final NumberSetting snapshotInterval = this.addSetting(new NumberSetting("Snapshot Interval", "Seconds between frozen poses", 1.2, 0.1, 4.0, 0.1));
   private final NumberSetting ghostLifetime = this.addSetting(new NumberSetting("Ghost Lifetime", "Seconds until fully faded", 2.4, 0.3, 8.0, 0.1));
   private final NumberSetting maximumGhosts = this.addSetting(new NumberSetting("Maximum Ghosts", "Hard snapshot limit", 6.0, 1.0, 12.0, 1.0));
   private final NumberSetting ghostOpacity = this.addSetting(new NumberSetting("Ghost Opacity", "Starting opacity", 0.45, 0.05, 0.9, 0.05));
   private final ModeSetting ghostStyle = this.addSetting(
      new ModeSetting("Ghost Style", "Snapshot material", "Original", "Original", "Theme Tint", "Custom Tint", "Hologram", "Silhouette")
   );
   private final ModeSetting ghostFade = this.addSetting(new ModeSetting("Ghost Fade", "Lifetime opacity", "Smooth", "Smooth", "Linear"));
   private final BooleanSetting player = this.addSetting(new BooleanSetting("Player", "Trail behind yourself", true));
   private final BooleanSetting entities = this.addSetting(new BooleanSetting("Entities", "Nearby living entities", false));
   private final BooleanSetting projectiles = this.addSetting(new BooleanSetting("Projectiles", "Arrows and projectiles", true));
   private final BooleanSetting fullBody = this.addSetting(new BooleanSetting("Full Body", "Span the whole body instead of a low band at the feet", true));
   private final NumberSetting width = this.addSetting(new NumberSetting("Width", "Strand thickness", 0.12, 0.02, 0.6, 0.01));
   private final NumberSetting lifetime = this.addSetting(new NumberSetting("Lifetime", "Seconds", 1.2, 0.2, 5.0, 0.1));
   private final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Submit glow mask", true));
   private final BooleanSetting smoothing = this.addSetting(new BooleanSetting("Smoothing", "Smooth trail curves", true));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Head color", bd.w));
   private final ColorSetting gradient = this.addSetting(new ColorSetting("Gradient", "Tail color", bd.Q));
   private final BooleanSetting trailParticles = this.addSetting(
      new BooleanSetting("Trail Particles", "Style particles shed from the whole body while moving", true)
   );
   private final NumberSetting particleDensity = this.addSetting(new NumberSetting("Particle Density", "Particles per block travelled", 1.0, 0.25, 3.0, 0.05));
   private final x4 T = new x4();

   public PlayerTrail() {
      super("Player Trail", "Leave fading trails and player copies", Category.VISUALS, -1);

      for (Setting var4 : new Setting[]{this.snapshotInterval, this.ghostLifetime, this.maximumGhosts, this.ghostOpacity, this.ghostStyle, this.ghostFade}) {
         var4.visibleWhen(() -> this.style.pAsBoolean("Ghost Echo"));
      }

      for (Setting var8 : new Setting[]{
         this.alphaCurve, this.player, this.entities, this.projectiles, this.fullBody, this.width, this.lifetime, this.smoothing, this.gradient
      }) {
         var8.visibleWhen(() -> !this.style.pAsBoolean("Ghost Echo"));
      }

      this.particleDensity.visibleWhen(this.trailParticles::getValue);
      this.color
         .visibleWhen(
            () -> !this.style.pAsBoolean("Ghost Echo")
                  || this.ghostStyle.pAsBoolean("Custom Tint")
                  || this.ghostStyle.pAsBoolean("Hologram")
                  || this.ghostStyle.pAsBoolean("Silhouette")
         );
      this.widthCurve.visibleWhen(() -> !this.style.pAsBoolean("Ghost Echo") && !this.style.pAsBoolean("Comet") && !this.style.pAsBoolean("Stardust"));
      this.style.onChanged(var0 -> {
         SharedRenderEffects.B.zAsVoid(3);
         SharedRenderEffects.p.FAsVoid();
         SharedRenderEffects.Z.WAsVoid();
         SharedRenderEffects.H.rAsVoid();
         ClientLog.GAsVoid("Trails", "Style changed: {}", var0);
      });
   }

   public List<Setting<?>> qAsList() {
      return List.of(this.style, this.player, this.entities, this.projectiles, this.fullBody, this.ghostStyle);
   }

   public List<Setting<?>> fAsList() {
      return List.of(this.width, this.color, this.gradient, this.bloom, this.ghostOpacity);
   }

   public List<Setting<?>> jAsList() {
      return List.of(
         this.lifetime,
         this.alphaCurve,
         this.widthCurve,
         this.smoothing,
         this.trailParticles,
         this.particleDensity,
         this.snapshotInterval,
         this.ghostLifetime,
         this.ghostFade
      );
   }

   public List<Setting<?>> hAsList() {
      return List.of(this.maximumGhosts);
   }

   @Override
   public void onEnable() {
      ClientLog.GAsVoid(
         "Trails",
         "Style: {}; full body: {}; particles: {} at density {}",
         this.style.getValue(),
         this.fullBody.getValue(),
         this.trailParticles.getValue(),
         this.particleDensity.getValue()
      );
      ClientLog.GAsVoid(
         "GhostEcho",
         "Snapshot interval: {}s; lifetime: {}s; limit: {}",
         this.snapshotInterval.getValue(),
         this.ghostLifetime.getValue(),
         this.maximumGhosts.getValue()
      );
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.style.pAsBoolean("Ghost Echo")) {
            ok var8 = SharedRenderEffects.H;
            var8.n = this.snapshotInterval.getValue();
            var8.R = this.ghostLifetime.getValue();
            var8.t = this.maximumGhosts.getValue().intValue();
            var8.m = this.ghostOpacity.getValue();
            var8.p = this.ghostStyle.getValue();
            var8.h = this.ghostFade.getValue();
            var8.E = this.bloom.getValue();
            var8.Y = this.color.getValue();
            var8.pAsVoid();
            if (this.trailParticles.getValue()) {
               this.MAsVoid(mc.player, (double)mc.player.getHeight(), 1.0);
            }
         } else {
            RibbonTrailEngine.Type_b var1 = this.style.pAsBoolean("Comet")
               ? RibbonTrailEngine.Type_b.HEAD_HEAVY
               : (this.style.pAsBoolean("Stardust") ? RibbonTrailEngine.Type_b.TAPER_TAIL : RibbonTrailEngine.Type_b.valueOf(this.widthCurve.getValue()));
            BodyTrailEngine var2 = SharedRenderEffects.Z;
            var2.l = this.EAsOu_Type_i();
            var2.b = ay.Type_r.valueOf(this.alphaCurve.getValue());
            var2.R = var1;
            var2.x = this.width.getValue();
            var2.U = this.lifetime.getValue();
            var2.v = this.bloom.getValue();
            var2.O = this.smoothing.getValue();
            var2.w = this.color.getValue();
            var2.c = this.gradient.getValue();
            RibbonTrailEngine var3 = SharedRenderEffects.p;
            var3.u = var2.b;
            var3.x = !this.style.pAsBoolean("Ribbon");
            var3.N = var1;
            var3.d = this.style.pAsBoolean("Ribbon") ? x4.Type_g.ALPHA : x4.Type_g.ADDITIVE;
            var3.c = this.width.getValue();
            var3.i = this.lifetime.getValue();
            var3.A = this.bloom.getValue();
            var3.h = this.smoothing.getValue();
            var3.g = this.color.getValue();
            var3.z = this.gradient.getValue();
            if (this.player.getValue()) {
               this.HAsVoid(mc.player, 1.0);
            }

            int var4 = 0;
            int var5 = 0;

            for (Entity var7 : mc.world.getEntities()) {
               if (var7 != mc.player && !(var7.squaredDistanceTo(mc.player) > 2304.0)) {
                  if (this.entities.getValue() && var7 instanceof LivingEntity && var4 < 15) {
                     this.HAsVoid(var7, 0.35);
                     var4++;
                  } else if (this.projectiles.getValue() && var7 instanceof ProjectileEntity) {
                     var3.gAsVoid(var7.getId(), var7.getEntityPos().add(0.0, 0.15, 0.0));
                     if (++var5 >= 31) {
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   private BodyTrailEngine.Type_i EAsOu_Type_i() {
      String var1 = this.style.getValue();

      return switch (var1) {
         case "Glow" -> BodyTrailEngine.Type_i.AURA;
         case "Comet" -> BodyTrailEngine.Type_i.COMET;
         case "Helix" -> BodyTrailEngine.Type_i.HELIX;
         case "Stardust" -> BodyTrailEngine.Type_i.STARDUST;
         default -> BodyTrailEngine.Type_i.SILK;
      };
   }

   private void HAsVoid(Entity var1, double var2) {
      double var4 = this.fullBody.getValue() ? (double)var1.getHeight() : 0.3;
      SharedRenderEffects.Z.nAsVoid(var1.getId(), var1.getEntityPos(), var4, (double)var1.getWidth());
      if (this.trailParticles.getValue()) {
         this.MAsVoid(var1, var4, var2);
      }
   }

   private void MAsVoid(Entity var1, double var2, double var4) {
      Vec3d var6 = var1.getEntityPos();
      Vec3d var7 = new Vec3d(var1.lastX, var1.lastY, var1.lastZ);
      double var8 = var6.distanceTo(var7);
      if (var8 > 4.0) {
         var7 = var6;
         var8 = 0.0;
      }

      ThreadLocalRandom var10 = ThreadLocalRandom.current();
      String var25 = this.style.getValue();

      double var11 = switch (var25) {
         case "Glow" -> 8.0;
         case "Comet" -> 9.0;
         case "Helix" -> 6.0;
         case "Stardust" -> 12.0;
         default -> 4.0;
      };
      double var25_r2 = this.style.pAsBoolean("Stardust") ? 0.12 : 0.0;
      double var15 = (var11 * var8 + var25_r2) * this.particleDensity.getValue() * var4;
      int var17 = (int)var15 + (var10.nextDouble() < var15 - (double)((int)var15) ? 1 : 0);
      if (var17 != 0) {
         x4 var18 = this.T;
         this.eAsVoid(var18, var2);
         var18.z = var18.c = (double)var1.getWidth() * 1.15;
         double var19 = this.style.pAsBoolean("Comet") ? 7.0 : (this.style.pAsBoolean("Stardust") ? 1.2 : 3.0);
         var18.Z = (var7.x - var6.x) * var19;
         var18.P = (var7.z - var6.z) * var19;
         double var21 = this.style.pAsBoolean("Comet") ? 0.55 : 0.5;

         for (int var23 = 0; var23 < Math.min(var17, 24); var23++) {
            var18.q = this.YAsBm_Type_a(var10.nextDouble());
            Vec3d var24 = var7.lerp(var6, var10.nextDouble()).add(0.0, var2 * var21, 0.0);
            SharedRenderEffects.B.pAsVoid(var24, var18);
         }
      }
   }

   private void eAsVoid(x4 var1, double var2) {
      int var4 = this.color.getValue();
      int var5 = this.gradient.getValue();
      var1.K = 3;
      var1.M = 1;
      var1.B = x4.Type_o.CYLINDER;
      var1.s = var2;
      var1.j = x4.Type_g.ADDITIVE;
      var1.i = this.bloom.getValue();
      var1.O = (int)Math.round(140.0 + 260.0 * this.particleDensity.getValue());
      var1.I = bg.pAsInt(var4, 0.9);
      var1.A = bg.pAsInt(var5, 0.25);
      var1.J = 0.0;
      var1.Q = 1.0;
      var1.e = 0.0;
      var1.f = 0.0;
      var1.V = 3.0;
      String var6 = this.style.getValue();
      switch (var6) {
         case "Glow":
            var1.g = 0.025;
            var1.R = 0.06;
            var1.x = 0.45;
            var1.v = 0.95;
            var1.w = 0.18;
            var1.b = 0.45;
            var1.a = -0.35;
            var1.D = 1.6;
            var1.L = 0.4;
            var1.t = ay.Type_r.FADE_IN_OUT;
            var1.o = ay.Type_z.SHRINK;
            break;
         case "Comet":
            var1.s = var2 * 0.5;
            var1.g = 0.018;
            var1.R = 0.038;
            var1.x = 0.25;
            var1.v = 0.55;
            var1.w = 0.9;
            var1.b = 0.2;
            var1.a = 0.7;
            var1.D = 1.8;
            var1.L = 0.1;
            var1.t = ay.Type_r.QUICK_FADE;
            var1.o = ay.Type_z.SHRINK;
            var1.A = bg.pAsInt(bg.xAsInt(var4, var5, 0.6), 0.3);
            break;
         case "Helix":
            var1.g = 0.02;
            var1.R = 0.045;
            var1.x = 0.5;
            var1.v = 0.9;
            var1.w = 0.45;
            var1.b = 0.05;
            var1.a = -0.05;
            var1.D = 1.4;
            var1.L = 0.15;
            var1.J = 3.2;
            var1.Q = 1.2;
            var1.t = ay.Type_r.FADE_IN_OUT;
            var1.o = ay.Type_z.POP;
            break;
         case "Stardust":
            var1.g = 0.016;
            var1.R = 0.05;
            var1.x = 0.8;
            var1.v = 1.7;
            var1.w = 0.06;
            var1.b = 0.02;
            var1.a = -0.03;
            var1.D = 2.5;
            var1.L = 0.15;
            var1.V = 1.2;
            var1.t = ay.Type_r.HOLD_FADE;
            var1.o = ay.Type_z.PULSE;
            var1.I = bg.pAsInt(bg.xAsInt(var4, -1, 0.35), 0.95);
            var1.A = bg.pAsInt(var5, 0.35);
            break;
         default:
            var1.g = 0.02;
            var1.R = 0.045;
            var1.x = 0.55;
            var1.v = 1.0;
            var1.w = 0.06;
            var1.b = 0.08;
            var1.a = -0.04;
            var1.D = 2.0;
            var1.L = 0.25;
            var1.t = ay.Type_r.FADE_IN_OUT;
            var1.o = ay.Type_z.SHRINK;
      }
   }

   private ParticleEmitterEngine.Type_a YAsBm_Type_a(double var1) {
      String var3 = this.style.getValue();

      return switch (var3) {
         case "Glow" -> var1 < 0.72 ? ParticleEmitterEngine.Type_a.Glow : ParticleEmitterEngine.Type_a.Spark;
         case "Comet" -> var1 < 0.6
         ? ParticleEmitterEngine.Type_a.Streak
         : (var1 < 0.85 ? ParticleEmitterEngine.Type_a.Spark : ParticleEmitterEngine.Type_a.Star);
         case "Helix" -> var1 < 0.5 ? ParticleEmitterEngine.Type_a.Diamond : ParticleEmitterEngine.Type_a.Glow;
         case "Stardust" -> var1 < 0.5
         ? ParticleEmitterEngine.Type_a.Star
         : (var1 < 0.75 ? ParticleEmitterEngine.Type_a.Diamond : ParticleEmitterEngine.Type_a.Glow);
         case "Ghost Echo" -> var1 < 0.6 ? ParticleEmitterEngine.Type_a.Diamond : ParticleEmitterEngine.Type_a.Glow;
         default -> var1 < 0.7 ? ParticleEmitterEngine.Type_a.Circle : ParticleEmitterEngine.Type_a.Glow;
      };
   }

   @Override
   public void onDisable() {
      SharedRenderEffects.B.zAsVoid(3);
      SharedRenderEffects.p.FAsVoid();
      SharedRenderEffects.Z.WAsVoid();
      SharedRenderEffects.H.rAsVoid();
   }
}
