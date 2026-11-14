package com.larpclient.module;

import com.larpclient.CelestialEffectEngine;
import com.larpclient.ClientLog;
import com.larpclient.EffectGeometryRenderer;
import com.larpclient.UiStyle;
import com.larpclient.LandQuery;
import com.larpclient.QueueNode;
import com.larpclient.WebRenderer;
import com.larpclient.BloomQualityHelper;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType.Skybox;

public final class CelestialWeather extends Module implements qn {
   private static CelestialWeather A;
   public final ModeSetting style = this.addSetting(
      new ModeSetting("Style", "Local celestial atmosphere", "Meteor Shower", "Meteor Shower", "Stardust", "Astral Rain")
   );
   public final NumberSetting D = this.LAsNumberSetting("Density", "Emission strength within the weather budget", 1.0, 0.1, 4.0, 0.1);
   public final NumberSetting z = this.LAsNumberSetting("Speed", "Flight and floating motion", 1.0, 0.0, 3.0, 0.1);
   public final NumberSetting p = this.LAsNumberSetting("Size", "Meteor cores, dust and droplet width", 1.0, 0.4, 2.5, 0.1);
   public final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use the current theme palette", true));
   public final ColorSetting color = this.addSetting(new ColorSetting("Color", "Celestial cores and dust", -2561281));
   public final ColorSetting secondaryColor = this.addSetting(new ColorSetting("Secondary Color", "Celestial tails and contact glow", -6059009));
   public final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Use shared world glow", true));
   public final NumberSetting R = this.LAsNumberSetting("Radius", "World-space emission region in blocks", 28.0, 8.0, 48.0, 1.0);
   public final NumberSetting s = this.LAsNumberSetting("Wind Strength", "Coherent horizontal drift", 0.8, 0.0, 6.0, 0.1);
   public final BooleanSetting surfaceImpacts = this.addSetting(
      new BooleanSetting("Surface Impacts", "Selected nearby flights contact actual block surfaces", true)
   );
   public final NumberSetting f = this.LAsNumberSetting("Render Distance", "Fade celestial weather beyond this distance", 80.0, 24.0, 128.0, 4.0);
   public final ModeSetting dimensionFilter = this.addSetting(
      new ModeSetting("Dimension Filter", "Choose where local weather appears", "Overworld", "Overworld", "All")
   );
   public final ModeSetting indoors = this.addSetting(new ModeSetting("Indoors", "Fade weather when the camera is sheltered", "Fade", "Fade", "Allow"));
   private final CelestialEffectEngine b = new CelestialEffectEngine();
   private Object u;
   private Object T;
   private Vec3d E;
   private int a;
   private boolean C;
   private boolean x;

   public CelestialWeather() {
      super("Celestial Weather", "Meteor showers, floating stardust and luminous rain", Category.VISUALS, -1);
      A = this;
      this.color.visibleWhen(() -> !this.themeColors.getValue());
      this.secondaryColor.visibleWhen(() -> !this.themeColors.getValue());
      this.surfaceImpacts.visibleWhen(() -> !this.style.pAsBoolean("Stardust"));
      this.style.onChanged(var1 -> this.clear());
      this.dimensionFilter.onChanged(var1 -> this.clear());
   }

   private NumberSetting LAsNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public List<Setting<?>> XAsList() {
      return List.of(this.style, this.D, this.z, this.p);
   }

   public List<Setting<?>> BAsList() {
      return List.of(this.themeColors, this.color, this.secondaryColor, this.bloom);
   }

   public List<Setting<?>> NAsList() {
      return List.of(this.R, this.s, this.surfaceImpacts, this.f, this.dimensionFilter, this.indoors);
   }

   public static CelestialWeather IAsCelestialWeather() {
      return A;
   }

   public CelestialEffectEngine MAsQd() {
      return this.b;
   }

   public CelestialEffectEngine.Type_i tAsQd_Type_i() {
      CelestialEffectEngine.Type_z var1 = this.style.pAsBoolean("Stardust")
         ? CelestialEffectEngine.Type_z.STARDUST
         : (this.style.pAsBoolean("Astral Rain") ? CelestialEffectEngine.Type_z.ASTRAL_RAIN : CelestialEffectEngine.Type_z.METEOR_SHOWER);
      return new CelestialEffectEngine.Type_i(
         var1,
         this.D.getValue(),
         this.R.getValue(),
         this.z.getValue(),
         this.p.getValue(),
         this.s.getValue(),
         this.f.getValue(),
         this.themeColors.getValue() ? bd.w : this.color.getValue(),
         this.themeColors.getValue() ? bd.Q : this.secondaryColor.getValue(),
         this.bloom.getValue(),
         this.surfaceImpacts.getValue(),
         x9.jAsInt(256)
      );
   }

   @Override
   public String effectName() {
      return "Celestial Weather";
   }

   @Override
   public void tick(MinecraftClient var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1.world != null && var1.player != null && var1.player.isAlive()) {
            this.x = var1.isPaused();
            if (!this.x) {
               Vec3d var2 = var1.gameRenderer.getCamera().getCameraPos();
               if (this.u != var1.world || this.T != var1.player || this.E != null && this.E.squaredDistanceTo(var2) > 4096.0) {
                  this.clear();
                  this.u = var1.world;
                  this.T = var1.player;
                  ClientLog.XAsVoid(
                     this.effectName(),
                     "World field initialized; style={}; max flights={}; max sprites={}; max terrain queries/tick={}",
                     this.style.getValue(),
                     256,
                     1024,
                     4
                  );
               }

               this.E = var2;
               if (this.dimensionFilter.pAsBoolean("Overworld") && var1.world.getDimension().skybox() != Skybox.OVERWORLD) {
                  this.b.ZAsVoid();
               } else {
                  if (this.a++ % 5 == 0) {
                     BlockPos var3 = BlockPos.ofFloored(var2);
                     int var4 = !this.indoors.pAsBoolean("Allow") && (!var1.world.isChunkLoaded(var3) || !var1.world.isSkyVisible(var3)) ? 0 : 1;
                     if (var4 != (this.C ? 1 : 0)) {
                        ClientLog.XAsVoid(this.effectName(), "Exposure changed: {}; doorway fade active", var4 != 0 ? "outdoors" : "sheltered");
                     }

                     this.C = var4 != 0;
                  }

                  this.b.mAsVoid(var2, this.tAsQd_Type_i(), this.C, (var1xx, var2x) -> lq.AAsLq_Type_l(var1.world, var1xx, var2x));
               }
            }
         } else {
            this.clear();
         }
      }
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (!this.isRuntimeActive()) {
            this.b.ZAsVoid();
         } else {
            this.b.aAsVoid(var2.pos, this.x ? 1.0F : var1, w3.BAsFrustum());
         }
      }
   }

   @Override
   public void render(EffectGeometryRenderer var1, boolean var2) {
      if (this.isEnabled()) {
         this.b.EAsVoid(var1, var2);
      }
   }

   @Override
   public boolean glowing() {
      return this.isEnabled() ? this.b.lAsBoolean() : false;
   }

   @Override
   public boolean soft() {
      return this.b.aAsBoolean();
   }

   @Override
   public void clear() {
      this.b.ZAsVoid();
      this.u = this.T = null;
      this.E = null;
      this.a = 0;
      this.C = this.x = false;
   }

   @Override
   public void onEnable() {
      this.clear();
      ClientLog.GAsVoid(this.effectName(), "Enabled {}; isolated weather pool; terrain queries capped at {}/tick", this.style.getValue(), 4);
   }

   @Override
   public void onDisable() {
      this.clear();
   }
}
