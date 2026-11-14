package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.EffectGeometryRenderer;
import com.larpclient.JumpRingRenderer;
import com.larpclient.LarpClient;
import com.larpclient.ParticleEffectPreset;
import com.larpclient.UiStyle;
import com.larpclient.ColorUtil;
import com.larpclient.LightmapAtlas;
import com.larpclient.MediaSession;
import com.larpclient.MediaDecoder;
import com.larpclient.EffectRenderParams;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class JumpFX extends Module {
   private static JumpFX Y;
   private final ModeSetting mode = this.addSetting(
      new ModeSetting(
         "Mode",
         "Terrain outlines, stars and Clarity jump effects",
         "Star of David",
         "Terrain",
         "Star of David",
         "Ripple",
         "Shockwave",
         "Rune",
         "Bloom",
         "Corona",
         "Vortex",
         "Spectrum",
         "Cloud",
         "End Rod",
         "Crit",
         "Flame",
         "Soul Flame",
         "Happy Villager"
      )
   );
   private final ModeSetting trigger = this.addSetting(new ModeSetting("Trigger", "Movement transition", "Land", "Jump", "Land", "Both"));
   private final NumberSetting maximumRadius = this.addSetting(new NumberSetting("Maximum Radius", "Blocks", 1.0, 1.0, 8.0, 0.25));
   private final NumberSetting pulseDuration = this.addSetting(new NumberSetting("Pulse Duration", "Seconds", 1.0, 0.3, 3.0, 0.05));
   private final NumberSetting waveWidth = this.addSetting(new NumberSetting("Wave Width", "Soft wave band in blocks", 0.9, 0.5, 3.0, 0.05));
   private final NumberSetting outlineThickness = this.addSetting(
      new NumberSetting("Outline Thickness", "World-space stroke width", 0.04, 0.005, 0.08, 0.005) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.3f", this.getValue());
         }
      }
   );
   private final ModeSetting colorTheme = this.addSetting(
      new ModeSetting("Color Theme", "Theme gradient, white or custom color", "Custom", "Theme", "White", "Custom")
   );
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Custom outline color", -15204097));
   private final NumberSetting opacity = this.addSetting(new NumberSetting("Opacity", "Outline opacity", 0.35, 0.05, 1.0, 0.05));
   private final NumberSetting glowIntensity = this.addSetting(
      new NumberSetting("Glow Intensity", "Soft strokes and quality-controlled bloom", 2.0, 0.0, 2.0, 0.05)
   );
   private final ColorSetting circleColour = this.addSetting(new ColorSetting("Circle Colour", "Primary circle color", -9452545));
   private final ColorSetting circleColour2 = this.addSetting(new ColorSetting("Circle Colour 2", "Secondary circle color", -3703297));
   private final NumberSetting circleSize = this.addSetting(new NumberSetting("Circle Size", "Circle scale in blocks", 1.6, 0.5, 6.0, 0.1));
   private final NumberSetting circleDuration = this.addSetting(new NumberSetting("Circle Duration", "Seconds", 0.9, 0.2, 3.0, 0.1));
   private final BooleanSetting circleThroughWalls = this.addSetting(new BooleanSetting("Circle Through Walls", "Show circles through terrain", false));
   private final NumberSetting radius = this.addSetting(new NumberSetting("Radius", "Particle ring radius in blocks", 0.6, 0.1, 3.0, 0.1));
   private final NumberSetting points = this.addSetting(new NumberSetting("Points", "Particles per jump or landing", 16.0, 4.0, 64.0, 1.0));
   private final BooleanSetting onlySprinting = this.addSetting(new BooleanSetting("Only Sprinting", "Emit Clarity effects only while sprinting", false));
   private final ParticleEffectPreset.Type_o K = new ParticleEffectPreset.Type_o();
   private final ArrayList<Particle> c = new ArrayList<>();
   private JumpFX.Type_j H;
   private final MediaDecoder d = new MediaDecoder();
   private final com.larpclient.DropletTrail B = new com.larpclient.DropletTrail();
   private Object W;
   private Object N;

   public JumpFX() {
      super("JumpFX", "Show glowing effects on jumps and landings", Category.VISUALS, -1);
      Y = this;
      this.color.visibleWhen(() -> this.AAsBoolean() && this.colorTheme.pAsBoolean("Custom"));

      for (Setting var2 : List.of(this.maximumRadius, this.pulseDuration, this.outlineThickness, this.colorTheme, this.opacity)) {
         var2.visibleWhen(this::AAsBoolean);
      }

      for (Setting var5 : List.of(this.circleColour, this.circleColour2, this.circleSize, this.circleDuration, this.circleThroughWalls)) {
         var5.visibleWhen(() -> ParticleEffectPreset.JAsInt(this.mode.getValue()) >= 0);
      }

      for (NumberSetting var6 : List.of(this.radius, this.points)) {
         var6.visibleWhen(() -> ParticleEffectPreset.hAsBoolean(this.mode.getValue()));
      }

      this.onlySprinting.visibleWhen(() -> ParticleEffectPreset.MAsBoolean(this.mode.getValue()));
      this.glowIntensity.visibleWhen(() -> this.AAsBoolean() || ParticleEffectPreset.JAsInt(this.mode.getValue()) >= 0);
      this.waveWidth.visibleWhen(() -> this.mode.pAsBoolean("Terrain"));
      this.mode.onChanged(var1 -> {
         this.oAsVoid();
         ClientLog.GAsVoid("JumpFX", "Mode changed: {}; active pulses cleared", var1);
      });
   }

   public Setting<?> FAsSetting() {
      return this.mode;
   }

   public List<Setting<?>> gAsList() {
      return List.of(this.mode, this.trigger, this.onlySprinting);
   }

   public List<Setting<?>> RAsList() {
      return List.of(
         this.maximumRadius,
         this.pulseDuration,
         this.waveWidth,
         this.outlineThickness,
         this.circleSize,
         this.circleDuration,
         this.circleThroughWalls,
         this.radius,
         this.points
      );
   }

   public List<Setting<?>> zAsList() {
      return List.of(this.colorTheme, this.color, this.opacity, this.glowIntensity, this.circleColour, this.circleColour2);
   }

   public static JumpFX JAsJumpFX() {
      return Y;
   }

   @Override
   public void onEnable() {
      this.SAsVoid();
      ClientLog.GAsVoid("JumpFX", "Enabled; mode={}, trigger={}, bounded world effects and native particles", this.mode.getValue(), this.trigger.getValue());
   }

   @Override
   public void onDisable() {
      this.SAsVoid();
   }

   private boolean AAsBoolean() {
      return this.mode.pAsBoolean("Terrain") || this.mode.pAsBoolean("Star of David");
   }

   private void oAsVoid() {
      this.B.bAsVoid();
      this.K.pAsVoid();
      this.H = null;

      for (Particle var2 : this.c) {
         var2.markDead();
      }

      this.c.clear();
      JumpRingRenderer.HAsVoid();
   }

   public void SAsVoid() {
      this.d.xAsVoid();
      this.oAsVoid();
      this.W = this.N = null;
   }

   public void QAsVoid() {
      if (this.isEnabled()) {
         this.H = null;
         int var1 = ParticleEffectPreset.JAsInt(this.mode.getValue());
         if (this.isRuntimeActive() && var1 >= 0 && !this.K.lAsList().isEmpty()) {
            this.H = new JumpFX.Type_j(
               var1,
               this.circleColour.getValue(),
               this.circleSize.getValue(),
               this.circleDuration.getValue(),
               this.circleThroughWalls.getValue(),
               this.glowIntensity.getValue().floatValue(),
               MediaSession.RAsDouble()
            );
            JumpRingRenderer.TAsVoid(this.circleColour2.getValue());
         }
      }
   }

   private boolean MAsBoolean() {
      ClientPlayerEntity var1 = this.getPlayer();
      Module var2 = LarpClient.CAsLarpClient().mAsF().TAsModule("Fly");
      Module var3 = LarpClient.CAsLarpClient().mAsF().TAsModule("Freecam");
      return var1 != null
         && var1.isAlive()
         && !var1.isSpectator()
         && !var1.isTouchingWater()
         && !var1.isInLava()
         && !var1.isClimbing()
         && !var1.hasVehicle()
         && !var1.isGliding()
         && !var1.getAbilities().flying
         && (var2 == null || !var2.isOperational())
         && (var3 == null || !var3.isOperational());
   }

   private MediaDecoder.Type_a qAsMd_Type_a() {
      ClientPlayerEntity var1 = this.getPlayer();
      if (this.W != getMc().world || this.N != var1) {
         this.SAsVoid();
         this.W = getMc().world;
         this.N = var1;
      }

      return new MediaDecoder.Type_a(this.W, var1, var1.getEntityPos(), var1.isOnGround(), this.MAsBoolean());
   }

   public void OAsVoid() {
      if (this.isEnabled() && this.isRuntimeActive() && this.isInGame()) {
         this.qAsVoid(this.d.IAsMd_Type_u(this.qAsMd_Type_a()));
      }
   }

   public void FAsVoid() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame()) {
            this.B.gAsVoid(MediaSession.dAsDouble());
            this.K.nAsVoid(MediaSession.dAsDouble(), this.circleDuration.getValue());
            this.c.removeIf(var0 -> !var0.isAlive());
            this.qAsVoid(this.d.MAsMd_Type_u(this.qAsMd_Type_a()));
         } else {
            this.SAsVoid();
         }
      }
   }

   private void qAsVoid(MediaDecoder.Type_u var1) {
      if (MediaDecoder.vAsBoolean(this.trigger.getValue(), var1.event())) {
         if (!this.AAsBoolean()) {
            if (!this.onlySprinting.getValue() || this.getPlayer().isSprinting()) {
               int var7 = ParticleEffectPreset.JAsInt(this.mode.getValue());
               if (var7 >= 0) {
                  this.K.AAsVoid(new ParticleEffectPreset.Type_z(var1.source(), MediaSession.dAsDouble(), ThreadLocalRandom.current().nextFloat()));
               } else {
                  ParticleEffect var3 = ParticleEffectPreset.iAsParticleEffect(this.mode.getValue());
                  if (var3 == null) {
                     return;
                  }

                  for (Vec3d var5 : ParticleEffectPreset.RAsList(var1.source(), this.radius.getValue(), this.points.getValue().intValue())) {
                     if (this.c.size() == 384) {
                        this.c.removeFirst().markDead();
                     }

                     Particle var6 = getMc().particleManager.addParticle(var3, var5.x, var5.y, var5.z, 0.0, 0.02, 0.0);
                     if (var6 != null) {
                        this.c.add(var6);
                     }
                  }
               }
            }
         } else {
            com.larpclient.DropletTrail.Type_t var2 = this.XAsL_Type_t(
               var1.source(), MediaSession.dAsDouble(), this.mode.pAsBoolean("Terrain") ? this.uAsLc_Type_x(var1.source().y) : null
            );
            if (var2 != null) {
               this.B.NAsVoid(var2);
            }
         }
      }
   }

   public com.larpclient.DropletTrail.Type_t XAsL_Type_t(Vec3d var1, double var2, LightmapAtlas.Type_x var4) {
      if (this.isEnabled()) {
         if (!this.AAsBoolean()) {
            return null;
         } else {
            boolean var5 = this.mode.pAsBoolean("Star of David");
            List var6 = var5 ? com.larpclient.HexEdgeloop.d : LightmapAtlas.GAsList(var1, this.maximumRadius.getValue(), var4);
            if (var6.isEmpty()) {
               return null;
            } else {
               int var7 = this.colorTheme.pAsBoolean("Theme") ? UiStyle.w : (this.colorTheme.pAsBoolean("White") ? -1 : this.color.getValue());
               int var8 = this.colorTheme.pAsBoolean("Theme") ? UiStyle.Q : var7;
               return new com.larpclient.DropletTrail.Type_t(
                  var1,
                  var2,
                  this.maximumRadius.getValue(),
                  this.pulseDuration.getValue(),
                  this.waveWidth.getValue(),
                  this.outlineThickness.getValue(),
                  var7,
                  var8,
                  this.opacity.getValue(),
                  this.glowIntensity.getValue(),
                  var6,
                  var5 ? com.larpclient.DropletTrail.Type_g.STAR_OF_DAVID : com.larpclient.DropletTrail.Type_g.TERRAIN
               );
            }
         }
      } else {
         return null;
      }
   }

   private LightmapAtlas.Type_x uAsLc_Type_x(double var1) {
      ClientWorld var3 = getMc().world;
      ShapeContext var4 = ShapeContext.of(this.getPlayer());
      return (var4xx, var5) -> {
         ArrayList var6 = new ArrayList();
         int var7 = (int)Math.floor(var1);

         for (int var8 = var7 - 3; var8 <= var7 + 3; var8++) {
            BlockPos var9 = new BlockPos(var4xx, var8, var5);
            if (var3.isChunkLoaded(var9)) {
               BlockState var10 = var3.getBlockState(var9);
               if (var10.getFluidState().isEmpty()) {
                  for (Box var12 : var10.getCollisionShape(var3, var9, var4).getBoundingBoxes()) {
                     var6.add(var12.offset((double)var4xx, (double)var8, (double)var5));
                     if (var6.size() > 32) {
                        return var6;
                     }
                  }
               }
            }
         }

         return var6;
      };
   }

   public boolean NAsBoolean() {
      return this.isEnabled()
         ? this.isRuntimeActive() && (this.H != null && this.H.glow() > 0.0F || this.B.zAsList().stream().anyMatch(var0 -> var0.glow() > 0.0))
         : false;
   }

   public void EAsVoid(EffectGeometryRenderer var1, boolean var2) {
      if (this.isEnabled() && this.isRuntimeActive()) {
         if (this.H != null) {
            JumpFX.Type_j var3 = this.H;
            float var4 = var2 ? Math.clamp(var3.glow() * 0.2F, 0.0F, 1.0F) : 1.0F;
            if (var4 > 0.0F) {
               for (ParticleEffectPreset.Type_z var6 : this.K.lAsList()) {
                  float var7 = (float)((var3.now() - var6.born()) / var3.duration());
                  if (var7 >= 0.0F && var7 < 1.0F) {
                     var1.OAsVoid(var6.source(), var3.size(), var7, var6.seed(), var3.primary(), var3.style(), var3.walls(), var4);
                  }
               }
            }
         }

         var1.blend(EffectRenderParams.Type_g.ADDITIVE);
         double var31 = MediaSession.RAsDouble();

         for (com.larpclient.DropletTrail.Type_t var33 : this.B.zAsList()) {
            if (!var2 || !(var33.glow() <= 0.0)) {
               double var34 = Math.clamp((var31 - var33.born()) / var33.duration(), 0.0, 1.0);
               int var9 = ColorUtil.xAsInt(var33.from(), var33.to(), var34);
               int var10 = var33.style() == com.larpclient.DropletTrail.Type_g.STAR_OF_DAVID ? 1 : 0;
               double var11 = var10 != 0 ? com.larpclient.DropletTrail.zAsDouble(var33, var31) : 1.0;

               for (LightmapAtlas.Type_w var14 : var33.edges()) {
                  double var15 = (var10 != 0 ? com.larpclient.DropletTrail.lAsDouble(var33, var31) : com.larpclient.DropletTrail.AAsDouble(var33, var14.distance(), var31))
                     * var33.opacity();
                  if (!(var15 < 0.003)) {
                     Vec3d var17 = var14.from();
                     Vec3d var18 = var14.to();
                     double var19 = var17.x;
                     double var21 = var17.y;
                     double var23 = var17.z;
                     double var25 = var18.x;
                     double var27 = var18.y;
                     double var29 = var18.z;
                     if (var10 != 0) {
                        var19 = com.larpclient.HexEdgeloop.kAsDouble(var33.source().x, var19, var11);
                        var23 = com.larpclient.HexEdgeloop.kAsDouble(var33.source().z, var23, var11);
                        var25 = com.larpclient.HexEdgeloop.kAsDouble(var33.source().x, var25, var11);
                        var29 = com.larpclient.HexEdgeloop.kAsDouble(var33.source().z, var29, var11);
                        var21 = var27 = var33.source().y + 0.012;
                     }

                     fAsVoid(var1, var33, var2, var9, var15, var19, var21, var23, var25, var27, var29);
                  }
               }
            }
         }

         var1.blend(EffectRenderParams.Type_g.ALPHA);
      }
   }

   private static void fAsVoid(
      EffectGeometryRenderer var0,
      com.larpclient.DropletTrail.Type_t var1,
      boolean var2,
      int var3,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16
   ) {
      if (var2) {
         int var18 = ColorUtil.pAsInt(var3, var4 * var1.glow() * 0.2);
         var0.ZAsVoid(var6, var8, var10, var12, var14, var16, var1.thickness() * 2.0, var1.thickness() * 2.0, var18, var18);
      } else {
         for (int var22 = 0; var22 < 3 && var1.glow() > 0.0; var22++) {
            double var19 = var1.thickness() * (2.0 + (double)var22 * 1.5);
            int var21 = ColorUtil.pAsInt(var3, var4 * var1.glow() * 0.065 / (double)(var22 + 1));
            var0.ZAsVoid(var6, var8, var10, var12, var14, var16, var19, var19, var21, var21);
         }

         int var23 = ColorUtil.pAsInt(ColorUtil.xAsInt(var3, var3 & 0xFF000000 | 16777215, 0.4), var4);
         var0.ZAsVoid(var6, var8, var10, var12, var14, var16, var1.thickness(), var1.thickness(), var23, var23);
      }
   }

   private static record Type_j(int style, int primary, double size, double duration, boolean walls, float glow, double now) {
   }
}
