package com.larpclient.module;

import com.larpclient.BloomQuality;
import com.larpclient.ClientLog;
import com.larpclient.EffectGeometryRenderer;
import com.larpclient.WeaponTrailRenderer;
import com.larpclient.UiStyle;
import com.larpclient.RibbonRenderer;
import com.larpclient.MediaSession;
import com.larpclient.QueueNode;
import com.larpclient.BloomQualityHelper;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public final class AstralWeaponTrails extends Module implements QueueNode {
   private static AstralWeaponTrails O;
   private final ModeSetting style = this.addSetting(new ModeSetting("Style", "Energy ribbon appearance", "Astral", "Astral", "Prismatic", "Void"));
   private final NumberSetting trailDuration = this.addSetting(new NumberSetting("Trail Duration", "Seconds of recent blade motion", 0.28, 0.1, 0.7, 0.02));
   private final NumberSetting width = this.addSetting(new NumberSetting("Width", "Ribbon span along the actual blade", 0.65, 0.15, 1.0, 0.05));
   private final NumberSetting intensity = this.addSetting(new NumberSetting("Intensity", "Body and leading edge brightness", 1.0, 0.2, 2.0, 0.1));
   private final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use the current theme palette", true));
   private final ColorSetting primaryColor = this.addSetting(new ColorSetting("Primary Color", "Leading energy color", -8790017));
   private final ColorSetting secondaryColor = this.addSetting(new ColorSetting("Secondary Color", "Fading wake color", -9142785));
   private final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Soft emissive halo on the native hand ribbon", true));
   private final BooleanSetting sparks = this.addSetting(new BooleanSetting("Sparks", "Small streaks separating from the swing", true));
   private final BooleanSetting firstPerson = this.addSetting(new BooleanSetting("First Person", "Show trails on the local rendered weapon", true));
   private final BooleanSetting motionSmoothing = this.addSetting(new BooleanSetting("Motion Smoothing", "Curve between captured blade poses", true));
   private Object f;
   private Object x;
   private Vec3d I;
   private int q = -1;
   private int G = -1;
   private boolean s;

   public AstralWeaponTrails() {
      super("Astral Weapon Trails", "Luminous ribbons following the rendered sword or knife", Category.VISUALS, -1);
      O = this;
      this.primaryColor.visibleWhen(() -> !this.themeColors.getValue());
      this.secondaryColor.visibleWhen(() -> !this.themeColors.getValue());
      this.style.onChanged(var0 -> {
         WeaponTrailRenderer.LAsVoid();
         ClientLog.XAsVoid("WeaponTrails", "Style changed to {}; history reset", var0);
      });
      this.firstPerson.onChanged(var0 -> WeaponTrailRenderer.LAsVoid());
   }

   public static AstralWeaponTrails JAsAstralWeaponTrails() {
      return O;
   }

   public List<Setting<?>> rAsList() {
      return List.of(this.style, this.trailDuration, this.width, this.intensity);
   }

   public List<Setting<?>> JAsList() {
      return List.of(this.themeColors, this.primaryColor, this.secondaryColor, this.bloom);
   }

   public List<Setting<?>> CAsList() {
      return List.of(this.sparks, this.firstPerson, this.motionSmoothing);
   }

   public int lAsInt() {
      return Math.max(16, BloomQualityHelper.jAsInt(64));
   }

   public RibbonRenderer.Type_c vAsDc_Type_c() {
      return new RibbonRenderer.Type_c(
         this.style.getValue(),
         this.trailDuration.getValue(),
         this.width.getValue(),
         this.intensity.getValue(),
         this.themeColors.getValue() ? UiStyle.w : this.primaryColor.getValue(),
         this.themeColors.getValue() ? UiStyle.Q : this.secondaryColor.getValue(),
         this.bloom.getValue() && BloomQualityHelper.K != BloomQuality.Type_w.Off,
         this.sparks.getValue(),
         this.motionSmoothing.getValue(),
         BloomQualityHelper.kAsBoolean() ? 3 : 2
      );
   }

   public boolean JAsBoolean(ItemStack var1) {
      if (!this.isEnabled()) {
         return false;
      } else {
         MinecraftClient var2 = getMc();
         return this.isRuntimeActive()
            && this.firstPerson.getValue()
            && SwordModels.PAsBoolean(var1)
            && var2.player != null
            && var2.world != null
            && var2.player.isAlive()
            && !var2.player.isUsingSpyglass()
            && var2.options.getPerspective().isFirstPerson();
      }
   }

   @Override
   public String effectName() {
      return "WeaponTrails";
   }

   @Override
   public void onEnable() {
      this.clear();
      ClientLog.GAsVoid(
         "WeaponTrails", "Enabled {}; native sword layer and articulated knife blade capture, two hands, at most 128 retained samples", this.style.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.clear();
   }

   @Override
   public void tick(MinecraftClient var1) {
      if (this.isEnabled() && !Halloween.bAsBoolean()) {
         if (!this.isRuntimeActive()) {
            this.clear();
         } else {
            boolean var2 = var1.options.getPerspective().isFirstPerson();
            if (this.f != var1.world || this.x != var1.player || this.s != var2 || this.q != BloomQuality.J) {
               this.clear();
               this.f = var1.world;
               this.x = var1.player;
               this.s = var2;
               this.q = BloomQuality.J;
            }

            if (var1.player != null && var1.world != null && var1.player.isAlive() && var2) {
               Vec3d var3 = var1.player.getEntityPos();
               if (this.I != null && this.I.squaredDistanceTo(var3) > 64.0) {
                  WeaponTrailRenderer.LAsVoid();
                  ClientLog.XAsVoid("WeaponTrails", "Cleared blade history after player relocation");
               }

               this.I = var3;
               int var4 = var1.player.getInventory().getSelectedSlot();
               if (this.G != var4) {
                  WeaponTrailRenderer.TAsVoid(Hand.MAIN_HAND);
               }

               this.G = var4;
               if (!SwordModels.PAsBoolean(var1.player.getMainHandStack())) {
                  WeaponTrailRenderer.TAsVoid(Hand.MAIN_HAND);
               }

               if (!SwordModels.PAsBoolean(var1.player.getOffHandStack())) {
                  WeaponTrailRenderer.TAsVoid(Hand.OFF_HAND);
               }

               WeaponTrailRenderer.PAsVoid(MediaSession.dAsDouble(), this.trailDuration.getValue());
            } else {
               WeaponTrailRenderer.LAsVoid();
            }
         }
      }
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
      }
   }

   @Override
   public void render(EffectGeometryRenderer var1, boolean var2) {
      if (this.isEnabled()) {
      }
   }

   @Override
   public boolean glowing() {
      return this.isEnabled() ? false : false;
   }

   @Override
   public void clear() {
      WeaponTrailRenderer.LAsVoid();
      this.f = this.x = null;
      this.I = null;
      this.G = -1;
   }
}
