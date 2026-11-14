package com.larpclient.module;

import com.larpclient.SkyEffectManager;
import com.larpclient.ThemePreset;
import com.larpclient.MediaSession;
import java.util.List;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.dimension.DimensionType.Skybox;

public class CustomSky extends Module {
   private static CustomSky v;
   private final ModeSetting skyStyle = this.addSetting(new ModeSetting("Sky Style", "World sky effect", "Nebula", "Nebula", "Ocean", "Israel Flag"));
   private final NumberSetting animationSpeed = this.addSetting(
      new NumberSetting("Animation Speed", "Effect motion speed; zero freezes it", 0.5, 0.0, 2.0, 0.1)
   );
   private final BooleanSetting stars = this.addSetting(new BooleanSetting("Stars", "Keep a star field visible in the custom sky", true));
   private final NumberSetting starBrightness = this.addSetting(new NumberSetting("Star Brightness", "Star field brightness", 0.65, 0.0, 1.0, 0.05));
   private final BooleanSetting clouds = this.addSetting(new BooleanSetting("Clouds", "Show vanilla clouds", false));
   private final BooleanSetting matchFog = this.addSetting(new BooleanSetting("Match Fog", "Blend atmospheric fog into the horizon", true));
   private final NumberSetting fogDistance = this.addSetting(
      new NumberSetting("Fog Distance", "Multiplier of vanilla atmospheric distance", 1.0, 0.1, 10.0, 0.1)
   );
   private final NumberSetting nebulaBrightness = this.addSetting(new NumberSetting("Nebula Brightness", "Overall nebula exposure", 1.0, 0.2, 2.0, 0.05));
   private final NumberSetting nebulaSaturation = this.addSetting(new NumberSetting("Nebula Saturation", "Nebula color richness", 0.9, 0.0, 1.5, 0.05));
   private final NumberSetting nebulaIntensity = this.addSetting(new NumberSetting("Nebula Intensity", "Cloud and highlight strength", 0.85, 0.0, 1.5, 0.05));
   private final NumberSetting nebulaAnimationSpeed = this.addSetting(
      new NumberSetting("Nebula Animation Speed", "Cloud motion multiplier; zero freezes cloud motion", 1.0, 0.0, 3.0, 0.1)
   );
   private final ModeSetting nebulaMotion = this.addSetting(
      new ModeSetting("Nebula Motion", "Flowing clouds or the original slow drift", "Flow", "Flow", "Classic")
   );
   private final NumberSetting Y = this.eAsNumberSetting("Nebula Flow Strength", "Cloud deformation", 0.35, 0.0, 1.0, 0.05);
   private final NumberSetting E = this.eAsNumberSetting("Nebula Scale", "Cloud pattern scale", 1.0, 0.5, 2.0, 0.05);
   private final ColorSetting nebulaColor = this.addSetting(new ColorSetting("Nebula Color", "Cloud artwork tint", -1));
   private final NumberSetting starDensity = this.addSetting(new NumberSetting("Star Density", "Procedural star field density", 0.45, 0.0, 1.0, 0.05));
   private final NumberSetting nebulaRotationSpeed = this.addSetting(
      new NumberSetting("Nebula Rotation Speed", "Degrees per second; zero stops rotation", 0.02, 0.0, 0.15, 0.01)
   );
   private final BooleanSetting showVanillaSun = this.addSetting(new BooleanSetting("Show Vanilla Sun", "Show Minecraft's sun", false));
   private final BooleanSetting showVanillaMoon = this.addSetting(new BooleanSetting("Show Vanilla Moon", "Show Minecraft's moon", false));
   private final BooleanSetting horizonBlend = this.addSetting(new BooleanSetting("Horizon Blend", "Subtle fade into the fog palette", true));
   private final NumberSetting C = this.eAsNumberSetting("Exposure", "Sky brightness", 1.0, 0.25, 2.0, 0.05);
   private final NumberSetting a = this.eAsNumberSetting("Rotation Speed", "Degrees per second", 0.02, -0.2, 0.2, 0.01);
   private final ModeSetting skyQuality = this.addSetting(
      new ModeSetting("Sky Quality", "Procedural noise and caustic detail", "High", "Low", "Medium", "High", "Ultra")
   );
   private final NumberSetting g = this.eAsNumberSetting("Caustic Scale", "Size of the fluid light cells", 5.0, 2.0, 10.0, 0.25);
   private final NumberSetting l = this.eAsNumberSetting("Caustic Intensity", "Brightness of converging light", 0.65, 0.0, 1.5, 0.05);
   private final NumberSetting D = this.eAsNumberSetting("Caustic Distortion", "Fluid warping strength", 0.8, 0.0, 2.0, 0.1);
   private final ColorSetting oceanBaseColor = this.addSetting(new ColorSetting("Ocean Base Color", "Deep water color", -16635057));
   private final ColorSetting oceanHighlightColor = this.addSetting(new ColorSetting("Ocean Highlight Color", "Caustic light color", -12133155));
   private ThemePreset y;

   private NumberSetting eAsNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public ModeSetting skyStyle() {
      return this.skyStyle;
   }

   public List<Setting<?>> bAsList() {
      String var1 = this.ZAsString();

      return switch (var1) {
         case "Nebula" -> List.of(
         this.nebulaBrightness,
         this.nebulaSaturation,
         this.nebulaIntensity,
         this.nebulaMotion,
         this.nebulaAnimationSpeed,
         this.Y,
         this.E,
         this.nebulaColor,
         this.nebulaRotationSpeed
      );
         case "Ocean" -> List.of(this.g, this.l, this.D, this.oceanBaseColor, this.oceanHighlightColor);
         default -> List.of();
      };
   }

   public List<Setting<?>> EAsList() {
      return List.of(
         this.nebulaBrightness, this.nebulaSaturation, this.nebulaIntensity, this.nebulaColor, this.l, this.oceanBaseColor, this.oceanHighlightColor, this.C
      );
   }

   public List<Setting<?>> yAsList() {
      return List.of(this.animationSpeed, this.nebulaMotion, this.nebulaAnimationSpeed, this.nebulaRotationSpeed, this.a);
   }

   public List<Setting<?>> TAsList() {
      return List.of(
         this.stars,
         this.starBrightness,
         this.starDensity,
         this.clouds,
         this.showVanillaSun,
         this.showVanillaMoon,
         this.horizonBlend,
         this.matchFog,
         this.fogDistance
      );
   }

   public List<Setting<?>> PAsList() {
      return List.of(this.Y, this.E, this.g, this.D, this.skyQuality);
   }

   public String ZAsString() {
      return this.skyStyle.getValue();
   }

   public float animationSpeed() {
      return this.animationSpeed.getValue().floatValue();
   }

   public float DAsFloat() {
      return this.a.getValue().floatValue();
   }

   public float tAsFloat() {
      return this.C.getValue().floatValue();
   }

   public boolean horizonBlend() {
      return this.horizonBlend.getValue();
   }

   public float AAsFloat() {
      return (float)this.skyQuality.getModes().indexOf(this.skyQuality.getValue());
   }

   public float EAsFloat() {
      return this.g.getValue().floatValue();
   }

   public float zAsFloat() {
      return this.l.getValue().floatValue();
   }

   public float SAsFloat() {
      return this.D.getValue().floatValue();
   }

   public int oceanBaseColor() {
      return this.oceanBaseColor.getValue();
   }

   public int oceanHighlightColor() {
      return this.oceanHighlightColor.getValue();
   }

   public boolean showVanillaSun() {
      return this.showVanillaSun.getValue();
   }

   public boolean showVanillaMoon() {
      return this.showVanillaMoon.getValue();
   }

   public boolean uAsBoolean() {
      return !this.skyStyle.pAsBoolean("Israel Flag");
   }

   public boolean AAsBoolean() {
      return this.skyStyle.pAsBoolean("Nebula");
   }

   public float nebulaBrightness() {
      return this.nebulaBrightness.getValue().floatValue();
   }

   public float nebulaSaturation() {
      return this.nebulaSaturation.getValue().floatValue();
   }

   public float nebulaIntensity() {
      return this.nebulaIntensity.getValue().floatValue();
   }

   public float starDensity() {
      return this.starDensity.getValue().floatValue();
   }

   public float nebulaAnimationSpeed() {
      return this.nebulaAnimationSpeed.getValue().floatValue();
   }

   public float nebulaRotationSpeed() {
      return this.nebulaRotationSpeed.getValue().floatValue();
   }

   public boolean XAsBoolean() {
      return this.nebulaMotion.pAsBoolean("Flow");
   }

   public float yAsFloat() {
      return this.Y.getValue().floatValue();
   }

   public float xAsFloat() {
      return this.E.getValue().floatValue();
   }

   public int nebulaColor() {
      return this.nebulaColor.getValue();
   }

   public CustomSky() {
      super("CustomSky", "Use animated custom skies", Category.VISUALS, -1);
      v = this;

      for (Setting var4 : new Setting[]{
         this.nebulaBrightness,
         this.nebulaSaturation,
         this.nebulaIntensity,
         this.nebulaAnimationSpeed,
         this.nebulaRotationSpeed,
         this.nebulaMotion,
         this.Y,
         this.E,
         this.nebulaColor
      }) {
         var4.visibleWhen(this::AAsBoolean);
      }

      this.Y.visibleWhen(() -> this.AAsBoolean() && this.XAsBoolean());

      for (Setting var8 : new Setting[]{this.g, this.l, this.D, this.oceanBaseColor, this.oceanHighlightColor}) {
         var8.visibleWhen(() -> this.skyStyle.pAsBoolean("Ocean"));
      }

      this.animationSpeed.visibleWhen(() -> this.skyStyle.pAsBoolean("Ocean"));
      this.a.visibleWhen(() -> !this.AAsBoolean());
      this.C.visibleWhen(() -> !this.AAsBoolean());
      this.horizonBlend.visibleWhen(() -> !this.AAsBoolean());
      this.skyQuality.visibleWhen(() -> this.skyStyle.pAsBoolean("Ocean"));
      this.stars.visibleWhen(this::AAsBoolean);
      this.starBrightness.visibleWhen(() -> this.stars.isVisible() && this.stars.getValue());
      this.starDensity.visibleWhen(this.starBrightness::isVisible);
   }

   @Override
   public void onEnable() {
      SkyEffectManager.KAsVoid();
   }

   public static CustomSky NAsCustomSky() {
      return v;
   }

   public static boolean yAsBoolean() {
      if (v != null && v.isEnabled()) {
         MinecraftClient var0 = MinecraftClient.getInstance();
         return v != null
            && v.isOperational()
            && SkyEffectManager.gAsBoolean(v.ZAsString())
            && var0.world != null
            && var0.world.getDimension().skybox() == Skybox.OVERWORLD
            && var0.gameRenderer.getCamera().getSubmersionType() == CameraSubmersionType.NONE;
      } else {
         return false;
      }
   }

   private ThemePreset lAsNv(int var1, int var2) {
      if (this.y == null || this.y.zenith() != var1 || this.y.horizon() != var2) {
         this.y = new ThemePreset(var1, var2);
      }

      return this.y;
   }

   public ThemePreset YAsNv() {
      if (this.skyStyle.pAsBoolean("Ocean")) {
         return this.lAsNv(this.oceanBaseColor.getValue(), this.oceanBaseColor.getValue());
      } else {
         return this.skyStyle.pAsBoolean("Israel Flag") ? this.lAsNv(-1, -1) : this.lAsNv(this.GAsInt(-16251885), this.GAsInt(-15265755));
      }
   }

   private int GAsInt(int var1) {
      float var2 = (float)(var1 >> 16 & 0xFF);
      float var3 = (float)(var1 >> 8 & 0xFF);
      float var4 = (float)(var1 & 0xFF);
      float var5 = var2 * 0.2126F + var3 * 0.7152F + var4 * 0.0722F;
      float var6 = this.nebulaSaturation();
      float var7 = this.nebulaBrightness();
      int var8 = (int)Math.clamp((var5 + (var2 - var5) * var6) * var7, 0.0F, 255.0F);
      int var9 = (int)Math.clamp((var5 + (var3 - var5) * var6) * var7, 0.0F, 255.0F);
      int var10 = (int)Math.clamp((var5 + (var4 - var5) * var6) * var7, 0.0F, 255.0F);
      return 0xFF000000 | var8 << 16 | var9 << 8 | var10;
   }

   public float fAsFloat() {
      return (float)(mb.RAsDouble() * this.animationSpeed.getValue());
   }

   public float KAsFloat() {
      return !this.skyStyle.pAsBoolean("Israel Flag") && this.stars.getValue() ? this.starBrightness.getValue().floatValue() : 0.0F;
   }

   public boolean clouds() {
      return this.clouds.getValue();
   }

   public boolean matchFog() {
      return this.matchFog.getValue();
   }

   public float fogDistance() {
      return this.fogDistance.getValue().floatValue();
   }
}
