package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ControlCategory;
import com.larpclient.EffectGeometryRenderer;
import com.larpclient.ParticleEmitterEngine;
import com.larpclient.RibbonCapacity;
import com.larpclient.SharedRenderEffects;
import com.larpclient.FadeCurve;
import com.larpclient.UiStyle;
import com.larpclient.ColorUtil;
import com.larpclient.MediaSession;
import com.larpclient.OpenScreen;
import com.larpclient.PanelQuality;
import com.larpclient.EffectRenderParams;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class TargetAura extends Module {
   private static TargetAura A;
   private final ModeSetting style = this.addSetting(
      new ModeSetting("Style", "Composed target effect", "Orbit", "Orbit", "Helix", "Energy", "Halo", "Atom", "Vortex", "Scanner", "Star of David")
   );
   private final ModeSetting source = this.addSetting(new ModeSetting("Source", "Read-only target source", "Auto", "Auto", "Aura", "Combat", "Crosshair") {
      @Override
      public void setValue(String var1) {
         super.setValue(TargetAura.RAsString(var1));
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(TargetAura.RAsString(var1));
      }
   });
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Cycles per second", 0.65, 0.1, 3.0, 0.05));
   private final NumberSetting width = this.addSetting(new NumberSetting("Width", "Trail width", 0.045, 0.01, 0.15, 0.005));
   private final NumberSetting particlesPerSecond = this.addSetting(new NumberSetting("Particles Per Second", "Persistent aura emission", 18.0, 0.0, 60.0, 1.0));
   private final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Explicit glow pass", true));
   private final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Follow current theme", true));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Primary color", UiStyle.w));
   private final ColorSetting gradient = this.addSetting(new ColorSetting("Gradient", "Secondary color", UiStyle.Q));
   private final EffectRenderParams g = new EffectRenderParams();
   private double H;
   private int q;
   private final PanelQuality K = new PanelQuality();
   private final RibbonCapacity.Type_v[] e = new RibbonCapacity.Type_v[18];
   private double y;
   private final ArrayList<TargetAura.Type_t> F;
   private final ArrayList<TargetAura.Type_z> j;
   private boolean C;
   private boolean T;
   private Object i;
   private int f;
   private Vec3d p;

   private static String RAsString(String var0) {
      return "KillAura".equalsIgnoreCase(var0) ? "Aura" : var0;
   }

   public TargetAura() {
      super("TargetAura", "Show glowing effects around your target", Category.VISUALS, -1);

      for (int var1 = 0; var1 < this.e.length; var1++) {
         this.e[var1] = new RibbonCapacity.Type_v(65);
      }

      this.y = Double.NaN;
      this.F = new ArrayList<>(10);
      this.j = new ArrayList<>(8);
      this.f = -1;
      A = this;
      this.color.visibleWhen(() -> !this.themeColors.getValue());
      this.gradient.visibleWhen(() -> !this.themeColors.getValue());
      this.style.onChanged(var0 -> ClientLog.GAsVoid("TargetAura", "Style changed: {}", var0));
   }

   public static TargetAura mAsTargetAura() {
      return A;
   }

   public List<Setting<?>> fAsList() {
      return List.of(this.style, this.source, this.speed, this.width, this.particlesPerSecond, this.bloom);
   }

   public List<Setting<?>> aAsList() {
      return List.of(this.themeColors, this.color, this.gradient);
   }

   public boolean VAsBoolean() {
      return this.isEnabled() ? this.C && this.T : false;
   }

   @Override
   public void onEnable() {
      ClientLog.GAsVoid(
         "TargetAura",
         "Expanded body-height aura: style={}, width={}, particle rate={}/s; readable base with bloom off",
         this.style.getValue(),
         this.width.getValue(),
         this.particlesPerSecond.getValue()
      );
   }

   @Override
   public void onTick() {
      LivingEntity var1 = ControlCategory.CAsLivingEntity(this.source.getValue());
      if (var1 == null) {
         this.H = 0.0;
      } else {
         this.MAsVoid(var1, var1.getEntityPos());
         this.H = this.H + this.particlesPerSecond.getValue() * 0.05;
         int var2 = (int)this.H;
         this.H -= (double)var2;
         if (var2 != 0) {
            EffectRenderParams var3 = this.dAsX4(var2, (double)var1.getWidth(), (double)var1.getHeight());
            SharedRenderEffects.B.pAsVoid(var1.getEntityPos().add(0.0, this.kAsDouble((double)var1.getHeight()), 0.0), var3);
         }
      }
   }

   private double kAsDouble(double var1) {
      if (this.style.pAsBoolean("Halo")) {
         return var1 + 0.22;
      } else {
         return this.style.pAsBoolean("Star of David") && Double.isFinite(this.y) ? this.y : var1 * 0.65;
      }
   }

   public EffectRenderParams dAsX4(int var1, double var2, double var4) {
      this.g.K = 2;
      this.g.M = var1;
      this.g.B = EffectRenderParams.Type_o.CYLINDER;
      boolean var6 = this.style.pAsBoolean("Star of David");
      this.g.z = this.g.c = (var2 * 0.75 + 0.35) * (var6 ? 2.6 : 2.0);
      this.g.s = var4 * (this.style.pAsBoolean("Halo") ? 0.08 : (var6 ? 0.1 : 0.8));
      this.g.I = this.themeColors.getValue() ? UiStyle.w : this.color.getValue();
      this.g.A = this.themeColors.getValue() ? UiStyle.Q : this.gradient.getValue();
      this.g.x = 0.85;
      this.g.v = 1.4;
      this.g.g = 0.055;
      this.g.R = 0.13;
      this.g.O = 128;
      this.g.a = -0.05;
      this.g.w = 0.1;
      this.g.b = 0.12;
      this.g.L = 0.3;
      this.g.J = 1.5;
      this.g.i = this.bloom.getValue();
      this.g.q = ParticleEmitterEngine.Type_a.Glow;
      this.g.j = EffectRenderParams.Type_g.ADDITIVE;
      this.g.Q = 1.0;
      if (var6) {
         this.g.J = 0.6;
         this.g.L = 0.18;
      }

      if (!this.style.pAsBoolean("Halo")) {
         this.g.I = ColorUtil.pAsInt(this.g.I, 0.85);
         this.g.A = ColorUtil.pAsInt(this.g.A, 0.65);
         this.g.R = 0.11;
         if (++this.q % 9 == 0) {
            this.g.q = ParticleEmitterEngine.Type_a.Spark;
            this.g.g = 0.025;
            this.g.R = 0.045;
            this.g.x = 0.18;
            this.g.v = 0.35;
            this.g.w = 0.75;
            this.g.Q = 0.35;
            this.g.L = 0.08;
            this.g.I = ColorUtil.xAsInt(this.g.I, this.g.I & 0xFF000000 | 16777215, 0.35);
         }
      }

      return this.g;
   }

   public void LAsVoid(float var1) {
      if (this.isEnabled()) {
         this.C = false;
         this.y = Double.NaN;
         this.F.clear();
         this.j.clear();

         for (RibbonCapacity.Type_v var5 : this.e) {
            var5.rAsVoid();
         }

         if (this.isRuntimeActive()) {
            LivingEntity var24 = ControlCategory.CAsLivingEntity(this.source.getValue());
            double var25 = this.K.uAsDouble(MediaSession.RAsDouble(), this.speed.getValue());
            if (var24 == null) {
               this.p = null;
               this.f = -1;
            } else {
               Vec3d var26 = var24.getLerpedPos(var1);
               this.MAsVoid(var24, var26);
               this.C = true;
               this.T = this.bloom.getValue();
               double var6 = (double)var24.getWidth() * 0.75 + 0.35;
               double var8 = (double)var24.getHeight();
               int var10 = this.themeColors.getValue() ? UiStyle.w : this.color.getValue();
               int var11 = this.themeColors.getValue() ? UiStyle.Q : this.gradient.getValue();
               if (!this.style.pAsBoolean("Orbit") && !this.style.pAsBoolean("Helix")) {
                  if (this.style.pAsBoolean("Energy")) {
                     this.uAsVoid(this.e[0], var26, var6, var8 * 0.48, var8, var25, var10, var11);
                     this.uAsVoid(this.e[1], var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, var11, var10);
                     this.DAsVoid(aAsVec3d(var26, var6, var8 * 0.48, var8, var25, MediaSession.RAsDouble(), 1.0), this.width.getValue() * 2.1, var10);
                     this.DAsVoid(aAsVec3d(var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, MediaSession.RAsDouble(), 1.0), this.width.getValue() * 2.1, var11);
                     this.MAsVoid(var26.add(0.0, 0.055, 0.0), var6 * 1.1, this.width.getValue() * 2.5, ColorUtil.pAsInt(var10, 0.6));
                  } else if (this.style.pAsBoolean("Atom")) {
                     this.OAsVoid(var26, var6, var8, var25, var10, var11);
                  } else if (this.style.pAsBoolean("Vortex")) {
                     this.PAsVoid(var26, var6, var8, var25, var10, var11);
                  } else if (this.style.pAsBoolean("Scanner")) {
                     this.GAsVoid(var26, var6, var8, var25, var10, var11);
                  } else if (this.style.pAsBoolean("Star of David")) {
                     this.bAsVoid(var26, var6, var8, var25, var10, var11);
                  } else {
                     Vec3d var12 = var26.add(0.0, var8 + 0.22 + Math.sin(var25 * 0.6) * 0.045, 0.0);
                     this.MAsVoid(var12, var6 * 1.05, this.width.getValue() * 3.0, ColorUtil.pAsInt(var10, 0.95));
                     this.MAsVoid(var12, var6 * 1.05, this.width.getValue() * 6.0, ColorUtil.pAsInt(var11, 0.25));
                     this.uAsVoid(this.e[0], var26, var6 * 1.05, var12.y - var26.y, var8 * 0.08, var25 * 0.65, var10, var11);
                     this.DAsVoid(
                        aAsVec3d(var26, var6 * 1.05, var12.y - var26.y, var8 * 0.08, var25 * 0.65, MediaSession.RAsDouble(), 1.0), this.width.getValue() * 1.8, var10
                     );
                     this.MAsVoid(var26.add(0.0, 0.055, 0.0), var6 * 1.05, this.width.getValue() * 2.0, ColorUtil.pAsInt(var11, 0.55));
                  }
               } else {
                  boolean var27 = this.style.pAsBoolean("Helix");

                  for (int var13 = 0; var13 < 2; var13++) {
                     RibbonCapacity.Type_v var14 = this.e[var13];

                     for (int var15 = 0; var15 <= 64; var15++) {
                        double var16 = (double)var15 / 64.0;
                        Vec3d var18 = mAsVec3d(var26, var6, var8, var25, var13, var16, var27);
                        double var19 = var27 ? Math.sin(Math.PI * var16) : bAsDouble(var16);
                        double var21 = this.width.getValue() * var19 * 3.4;
                        int var23 = ColorUtil.pAsInt(ColorUtil.xAsInt(var11, var10, var16), 0.92 * (var27 ? Math.sin(Math.PI * var16) : Math.pow(var16, 0.65)));
                        var14.bAsVoid(var18, var21, var23);
                     }

                     double var31 = var27 ? 0.5 + 0.44 * Math.sin(var25 * 0.33 + (double)var13 * Math.PI) : 1.0;
                     this.DAsVoid(
                        mAsVec3d(var26, var6, var8, var25, var13, var31, var27), this.width.getValue() * (var27 ? 1.6 : 2.1), ColorUtil.xAsInt(var11, var10, var31)
                     );
                  }

                  this.MAsVoid(var26.add(0.0, 0.055, 0.0), var6 * 1.05, this.width.getValue() * 2.0, ColorUtil.pAsInt(var11, 0.55));
               }

               Vec3d var28 = MinecraftClient.getInstance().gameRenderer.getCamera().getCameraPos();

               for (RibbonCapacity.Type_v var33 : this.e) {
                  var33.aAsVoid(var28);
               }
            }
         }
      }
   }

   private void MAsVoid(LivingEntity var1, Vec3d var2) {
      World var3 = var1.getEntityWorld();
      if (this.i != var3 || this.f != var1.getId() || this.p != null && this.p.squaredDistanceTo(var2) > 64.0) {
         this.H = 0.0;
         SharedRenderEffects.B.zAsVoid(2);
      }

      this.i = var3;
      this.f = var1.getId();
      this.p = var2;
   }

   private void MAsVoid(Vec3d var1, double var2, double var4, int var6) {
      this.F.add(new TargetAura.Type_t(var1, var2, var4, var6));
   }

   private void DAsVoid(Vec3d var1, double var2, int var4) {
      this.j.add(new TargetAura.Type_z(var1, var2, ColorUtil.xAsInt(var4, var4 & 0xFF000000 | 16777215, 0.55)));
   }

   public static Vec3d mAsVec3d(Vec3d var0, double var1, double var3, double var5, int var7, double var8, boolean var10) {
      double var11 = var5 + (double)var7 * Math.PI + (var10 ? var8 * Math.PI * 4.0 : -(1.0 - var8) * Math.PI * 1.85);
      double var13 = Math.sin(var11 * 2.3 + var5 * 0.23 + (double)var7);
      double var15 = var1 * (1.0 + 0.045 * var13);
      double var17 = var10 ? var3 * (0.06 + 0.97 * var8) : 0.54 * var3 + Math.sin(var11 * 1.2) * var3 * 0.34;
      var17 += Math.sin(var11 * 1.7 - var5 * 0.31) * var3 * 0.025;
      return var0.add(Math.cos(var11) * var15, var17, Math.sin(var11) * var15);
   }

   private void OAsVoid(Vec3d var1, double var2, double var4, double var6, int var8, int var9) {
      Vec3d var10 = var1.add(0.0, var4 * 0.55, 0.0);
      double var11 = var2 * 1.15;
      double var13 = this.width.getValue();

      for (int var15 = 0; var15 < 3; var15++) {
         double var16 = var6 * 0.18 + (double)var15 * Math.PI * 2.0 / 3.0;
         double var18 = var6 * (1.0 + (double)var15 * 0.17) + (double)var15 * 2.1;
         int var20 = ColorUtil.xAsInt(var8, var9, (double)var15 / 2.0);

         for (int var21 = 0; var21 <= 48; var21++) {
            this.e[var15].bAsVoid(YAsVec3d(var10, var11, var4, var16, (double)var21 / 48.0 * Math.PI * 2.0), var13 * 1.3, ColorUtil.pAsInt(var20, 0.24));
         }

         for (int var24 = 0; var24 <= 32; var24++) {
            double var22 = (double)var24 / 32.0;
            this.e[var15 + 3]
               .bAsVoid(
                  YAsVec3d(var10, var11, var4, var16, var18 - (1.0 - var22) * Math.PI * 1.1),
                  var13 * bAsDouble(var22) * 3.2,
                  ColorUtil.pAsInt(ColorUtil.xAsInt(var9, var8, var22), 0.95 * Math.pow(var22, 0.65))
               );
         }

         this.DAsVoid(YAsVec3d(var10, var11, var4, var16, var18), var13 * 2.0, var20);
      }

      this.MAsVoid(var1.add(0.0, 0.055, 0.0), var2 * 1.05, var13 * 2.0, ColorUtil.pAsInt(var9, 0.5));
   }

   public static Vec3d YAsVec3d(Vec3d var0, double var1, double var3, double var5, double var7) {
      double var9 = Math.toRadians(56.0);
      double var11 = Math.min(var1 * Math.sin(var9), var3 * 0.42);
      double var13 = Math.cos(var7);
      double var15 = Math.sin(var7);
      double var17 = var1 * Math.cos(var9) * var15;
      return var0.add(Math.cos(var5) * var1 * var13 - Math.sin(var5) * var17, var11 * var15, Math.sin(var5) * var1 * var13 + Math.cos(var5) * var17);
   }

   private void PAsVoid(Vec3d var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.width.getValue();

      for (int var12 = 0; var12 < 3; var12++) {
         double var13 = floor(var6 * 0.12 + (double)var12 / 3.0);

         for (int var15 = 0; var15 <= 64; var15++) {
            double var16 = (double)var15 / 64.0;
            double var18 = tAsDouble(var16);
            double var20 = Math.exp(-vAsDouble((var16 - var13) / 0.09));
            this.e[var12]
               .bAsVoid(
                  lAsVec3d(var1, var2, var4, var6, var12, var16),
                  var10 * (1.6 + 1.6 * var16) * (1.0 + 0.7 * var20) * var18,
                  ColorUtil.pAsInt(ColorUtil.xAsInt(var8, var9, var16), (0.38 + 0.57 * var20) * var18)
               );
         }

         this.DAsVoid(lAsVec3d(var1, var2, var4, var6, var12, var13), var10 * 1.9 * tAsDouble(var13), ColorUtil.xAsInt(var8, var9, var13));
      }

      this.MAsVoid(var1.add(0.0, 0.055, 0.0), var2 * 0.8, var10 * 2.0, ColorUtil.pAsInt(var8, 0.55));
      this.MAsVoid(var1.add(0.0, var4 * 1.1, 0.0), var2 * 1.36, var10 * 1.6, ColorUtil.pAsInt(var9, 0.32));
   }

   public static Vec3d lAsVec3d(Vec3d var0, double var1, double var3, double var5, int var7, double var8) {
      double var10 = var5 * 0.9 + (double)var7 * Math.PI * 2.0 / 3.0 - var8 * Math.PI * 2.3;
      double var12 = var1 * (0.72 + 0.62 * Math.pow(var8, 1.3));
      return var0.add(Math.cos(var10) * var12, var3 * (0.02 + 1.08 * var8), Math.sin(var10) * var12);
   }

   private static double tAsDouble(double var0) {
      return Math.pow(Math.sin(Math.PI * Math.clamp(var0, 0.0, 1.0)), 0.6);
   }

   private void GAsVoid(Vec3d var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.width.getValue();
      double var12 = var6 * 0.45;
      double var14 = fAsDouble(var4, var12);
      double var16 = Math.sin(var12);
      double var18 = Math.abs(var16) * var4 * 0.32;
      double var20 = Math.min(1.0, var18 * 8.0);
      this.MAsVoid(var1.add(0.0, var14, 0.0), var2 * 1.02, var10 * 2.6, ColorUtil.pAsInt(var8, 0.95));
      this.MAsVoid(var1.add(0.0, var14, 0.0), var2 * 1.02, var10 * 7.0, ColorUtil.pAsInt(var9, 0.28));

      for (int var22 = 1; var22 <= 6 && var20 > 0.02; var22++) {
         double var23 = (double)var22 / 6.0;
         this.MAsVoid(
            var1.add(0.0, var14 - Math.signum(var16) * var18 * var23, 0.0),
            var2 * (1.02 - 0.04 * var23),
            var10 * 1.6,
            ColorUtil.pAsInt(ColorUtil.xAsInt(var8, var9, var23), 0.55 * Math.pow(1.0 - var23, 1.4) * var20)
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         double var26 = var6 * 1.1 + (double)var25 * Math.PI;
         this.DAsVoid(var1.add(Math.cos(var26) * var2 * 1.02, var14, Math.sin(var26) * var2 * 1.02), var10 * 1.8, var25 == 0 ? var8 : var9);
      }
   }

   public static double fAsDouble(double var0, double var2) {
      return 0.04 + (0.5 - 0.5 * Math.cos(var2)) * (var0 + 0.06);
   }

   private void bAsVoid(Vec3d var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.width.getValue();
      double var12 = var6 * 0.45;
      double var14 = Math.sin(var12);
      double var16 = var2 * 1.3 * (1.0 + 0.025 * Math.sin(var6 * 0.5));
      double var18 = var6 * 0.1;
      double var20 = fAsDouble(var4, var12);
      double var22 = Math.abs(var14) * var4 * 0.32;
      double var24 = Math.min(1.0, var22 * 8.0);
      this.y = var20;
      Vec3d var26 = var1.add(0.0, var20, 0.0);
      this.pAsVoid(var26, var16, var18, var6, var8, var9, 1.0, 1.0, 0, true);

      for (int var27 = 1; var27 <= 2 && var24 > 0.02; var27++) {
         double var28 = (double)var27 / 2.0;
         this.pAsVoid(
            var1.add(0.0, var20 - Math.signum(var14) * var22 * var28, 0.0),
            var16 * (1.0 - 0.04 * var28),
            var18,
            var6,
            var8,
            var9,
            (var27 == 1 ? 0.42 : 0.16) * var24,
            0.7,
            var27 * 6,
            false
         );
      }

      this.MAsVoid(var26, var16 * 1.12, var10 * 2.4, ColorUtil.pAsInt(var8, 0.85));
      this.MAsVoid(var26, var16 * 1.24, var10 * 1.2, ColorUtil.pAsInt(var9, 0.45));

      for (int var30 = 1; var30 <= 4 && var24 > 0.02; var30++) {
         double var31 = (double)var30 / 4.0;
         this.MAsVoid(
            var1.add(0.0, var20 - Math.signum(var14) * var22 * var31, 0.0),
            var16 * 1.12 * (1.0 - 0.04 * var31),
            var10 * 1.4,
            ColorUtil.pAsInt(ColorUtil.xAsInt(var8, var9, var31), 0.4 * Math.pow(1.0 - var31, 1.4) * var24)
         );
      }
   }

   private void pAsVoid(Vec3d var1, double var2, double var4, double var6, int var8, int var9, double var10, double var12, int var14, boolean var15) {
      double var16 = this.width.getValue();

      for (int var18 = 0; var18 < 2; var18++) {
         int var19 = var18 == 0 ? var8 : var9;
         double var20 = floor(var6 * 0.09 * (double)(var18 == 0 ? 1 : -1) + (double)var18 * 0.5);

         for (int var22 = 0; var22 < 3; var22++) {
            Vec3d var23 = pAsVec3d(var1, var2, var4, var18 + var22 * 2);
            Vec3d var24 = pAsVec3d(var1, var2, var4, var18 + var22 * 2 + 2);

            for (int var25 = 0; var25 <= 16; var25++) {
               double var26 = (double)var25 / 16.0;
               double var28 = ((double)var22 + var26) / 3.0 - var20;
               double var30 = Math.exp(-vAsDouble(var28 - Math.rint(var28)) / 0.006);
               this.e[var14 + var18 * 3 + var22]
                  .bAsVoid(var23.lerp(var24, var26), var16 * (2.6 + 1.6 * var30) * var12, ColorUtil.pAsInt(var19, (0.72 + 0.28 * var30) * var10));
            }
         }

         if (var15) {
            int var33 = (int)(var20 * 3.0);
            this.DAsVoid(
               pAsVec3d(var1, var2, var4, var18 + var33 * 2).lerp(pAsVec3d(var1, var2, var4, var18 + var33 * 2 + 2), var20 * 3.0 - (double)var33),
               var16 * 2.3,
               var19
            );
         }
      }

      if (var15) {
         for (int var32 = 0; var32 < 6; var32++) {
            this.DAsVoid(pAsVec3d(var1, var2, var4, var32), var16 * 1.5, var32 % 2 == 0 ? var8 : var9);
         }
      }
   }

   public static Vec3d pAsVec3d(Vec3d var0, double var1, double var3, int var5) {
      double var6 = var3 + (double)Math.floorMod(var5, 6) * Math.PI / 3.0;
      return var0.add(Math.cos(var6) * var1, 0.0, Math.sin(var6) * var1);
   }

   private static double floor(double var0) {
      return var0 - Math.floor(var0);
   }

   private static double vAsDouble(double var0) {
      return var0 * var0;
   }

   private static double bAsDouble(double var0) {
      return (0.12 + 0.88 * Math.pow(var0, 0.8)) * FadeCurve.RAsDouble(var0 / 0.08);
   }

   private static Vec3d aAsVec3d(Vec3d var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = var7 - (1.0 - var11) * 2.4;
      double var15 = Math.sin(var13 * 2.1 + var9 * 0.7);
      double var17 = var1 * (1.0 + var15 * 0.045);
      return var0.add(Math.cos(var13) * var17, var3 + var5 * 0.045 * Math.sin(var13 * 1.6), Math.sin(var13) * var17);
   }

   public void XAsVoid(EffectGeometryRenderer var1, float var2, boolean var3) {
      if (this.isEnabled() && this.C && (!var3 || this.T)) {
         long var4 = OpenScreen.RAsLong();
         var1.blend(EffectRenderParams.Type_g.ADDITIVE);

         for (RibbonCapacity.Type_v var9 : this.e) {
            var1.hAsVoid(var9, true);
         }

         for (TargetAura.Type_t var12 : this.F) {
            var1.oAsVoid(var12.center(), var12.radius(), var12.width(), var12.color());
         }

         for (TargetAura.Type_z var13 : this.j) {
            Vec3d var14 = var13.center();
            var1.BAsVoid(var14.x, var14.y, var14.z, var13.size() * (var3 ? 1.8 : 2.6), 0.0, ColorUtil.pAsInt(var13.color(), var3 ? 0.35 : 0.18));
            if (!var3) {
               var1.BAsVoid(var14.x, var14.y, var14.z, var13.size(), 0.0, var13.color());
            }
         }

         var1.blend(EffectRenderParams.Type_g.ALPHA);
         OpenScreen.aAsVoid("TargetAura", var4);
      }
   }

   private void uAsVoid(RibbonCapacity.Type_v var1, Vec3d var2, double var3, double var5, double var7, double var9, int var11, int var12) {
      for (int var13 = 0; var13 <= 32; var13++) {
         double var14 = (double)var13 / 32.0;
         Vec3d var16 = aAsVec3d(var2, var3, var5, var7, var9, MediaSession.RAsDouble(), var14);
         double var17 = this.width.getValue() * bAsDouble(var14) * 3.4;
         int var19 = ColorUtil.pAsInt(ColorUtil.xAsInt(var12, var11, var14), Math.pow(var14, 0.65));
         var1.bAsVoid(var16, var17, var19);
      }
   }

   @Override
   public void onDisable() {
      this.H = 0.0;
      this.q = 0;
      this.C = false;
      this.y = Double.NaN;
      this.p = null;
      this.i = null;
      this.f = -1;
      this.F.clear();
      this.j.clear();

      for (RibbonCapacity.Type_v var4 : this.e) {
         var4.rAsVoid();
      }

      this.K.nAsVoid();
      SharedRenderEffects.B.zAsVoid(2);
   }

   private static record Type_t(Vec3d center, double radius, double width, int color) {
   }

   private static record Type_z(Vec3d center, double size, int color) {
   }
}
