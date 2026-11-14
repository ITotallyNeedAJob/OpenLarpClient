package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.CosmeticSubmissionHandler;
import com.larpclient.EntityHaloEffect;
import com.larpclient.EntityMaterialRenderer;
import com.larpclient.SharedRenderEffects;
import com.larpclient.CometRenderer;
import com.larpclient.UiStyle;
import java.util.List;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;

public final class SelfVisuals extends Module {
   private static SelfVisuals j;
   public final ModeSetting style = this.addSetting(
      new ModeSetting(
         "Style",
         "Player material",
         "Rainbow Outline",
         "Rainbow Outline",
         "Hologram",
         "Comets",
         "Outline",
         "Liquid Glass",
         "Solid",
         "Glow",
         "Metallic",
         "Camo",
         "Marble",
         "Glitter",
         "Ghost"
      )
   );
   public final NumberSetting rainbowSpeed = this.addSetting(new NumberSetting("Rainbow Speed", "Cycles per second", 0.18, 0.0, 2.0, 0.01));
   public final ModeSetting direction = this.addSetting(new ModeSetting("Direction", "Gradient flow", "Downward", "Downward", "Upward", "Horizontal", "Static"));
   public final NumberSetting gradientScale = this.addSetting(new NumberSetting("Gradient Scale", "Rainbow cycles per block", 0.5, 0.1, 2.0, 0.05));
   public final NumberSetting saturation = this.addSetting(new NumberSetting("Saturation", "Rainbow saturation", 0.62, 0.0, 1.0, 0.02));
   public final NumberSetting brightness = this.addSetting(new NumberSetting("Brightness", "Rainbow brightness", 0.95, 0.1, 1.0, 0.025));
   public final NumberSetting modelOpacity = this.addSetting(new NumberSetting("Model Opacity", "Body opacity", 1.0, 0.05, 1.0, 0.05));
   public final BooleanSetting outline = this.addSetting(new BooleanSetting("Outline", "Screen-space silhouette outline", true));
   public final ColorSetting outlineColor = this.addSetting(new ColorSetting("Outline Color", "Silhouette border", -1));
   public final NumberSetting outlineThickness = this.addSetting(new NumberSetting("Outline Thickness", "Screen pixels", 1.5, 0.5, 5.0, 0.5));
   public final NumberSetting outlineOpacity = this.addSetting(new NumberSetting("Outline Opacity", "Border opacity", 0.7, 0.0, 1.0, 0.05));
   public final BooleanSetting outlineBloom = this.addSetting(new BooleanSetting("Outline Bloom", "Soft halo outside border", false));
   public final BooleanSetting modelBloom = this.addSetting(new BooleanSetting("Model Bloom", "Explicit body glow", false));
   public final BooleanSetting affectCape = this.addSetting(new BooleanSetting("Affect Cape", "Apply Rainbow Outline to your Minecraft cape", false));
   public final NumberSetting cometSize = this.addSetting(new NumberSetting("Comet Size", "Core radius in blocks", 0.055, 0.025, 0.12, 0.005));
   public final NumberSetting orbitSpeed = this.addSetting(new NumberSetting("Orbit Speed", "Orbits per second", 0.32, 0.05, 1.2, 0.01));
   public final NumberSetting orbitRadius = this.addSetting(new NumberSetting("Orbit Radius", "Blocks; kept outside the current body", 0.52, 0.3, 1.0, 0.01));
   public final NumberSetting trailLength = this.addSetting(new NumberSetting("Trail Length", "Seconds of previous orbit positions", 0.24, 0.08, 0.6, 0.01));
   public final NumberSetting glowIntensity = this.addSetting(new NumberSetting("Glow Intensity", "Halo and trail brightness", 1.0, 0.2, 2.0, 0.05));
   public final BooleanSetting cometBloom = this.addSetting(new BooleanSetting("Comet Bloom", "Submit comets to the shared bloom pass", true));
   public final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use current theme accents for comets", true));
   public final ColorSetting cometColor = this.addSetting(new ColorSetting("Comet Color", "Core and trail head", UiStyle.w));
   public final ColorSetting tailColor = this.addSetting(new ColorSetting("Tail Color", "Faded trail end", UiStyle.Q));
   public final NumberSetting glassOpacity = this.addSetting(new NumberSetting("Glass Opacity", "Liquid surface opacity", 0.62, 0.1, 1.0, 0.02));
   public final NumberSetting refractionStrength = this.addSetting(
      new NumberSetting("Refraction Strength", "Screen displacement in pixels", 3.5, 0.0, 12.0, 0.25)
   );
   public final NumberSetting surfaceFlow = this.addSetting(new NumberSetting("Surface Flow", "Fluid surface motion speed", 0.65, 0.0, 2.0, 0.05));
   public final NumberSetting gloss = this.addSetting(new NumberSetting("Gloss", "Broad soft reflections to tight highlights", 0.78, 0.0, 1.0, 0.02));
   public final NumberSetting edgeHighlight = this.addSetting(new NumberSetting("Edge Highlight", "Viewing-angle surface highlights", 0.8, 0.0, 1.0, 0.02));
   public final NumberSetting iridescence = this.addSetting(new NumberSetting("Iridescence", "Mild viewing-angle interference", 0.18, 0.0, 1.0, 0.02));
   public final BooleanSetting enhancedRefraction = this.addSetting(
      new BooleanSetting("Enhanced Refraction", "Local scene refraction where effects quality permits", true)
   );
   public final BooleanSetting themePalette = this.addSetting(new BooleanSetting("Theme Palette", "Use current theme accents for chams colors", false));
   public final ColorSetting primaryColor = this.addSetting(new ColorSetting("Primary Color", "Main chams color", -15814));
   public final ColorSetting secondaryColor = this.addSetting(new ColorSetting("Secondary Color", "Pattern, vein and shadow color", -14018044));
   public final NumberSetting shading = this.addSetting(new NumberSetting("Shading", "Flat color to fully lit faces", 0.6, 0.0, 1.0, 0.05));
   public final NumberSetting patternScale = this.addSetting(new NumberSetting("Pattern Scale", "Pattern cells per skin area", 1.0, 0.25, 4.0, 0.05));
   public final NumberSetting patternSpeed = this.addSetting(
      new NumberSetting("Pattern Speed", "Pattern drift and twinkle cycles per second", 0.25, 0.0, 2.0, 0.05)
   );
   private static final List<String> y = List.of("Solid", "Glow", "Metallic", "Camo", "Marble", "Glitter", "Ghost");
   public final BooleanSetting affectArmor = this.addSetting(new BooleanSetting("Affect Armor", "Apply material to armor", false));
   public final BooleanSetting affectHeldItems = this.addSetting(new BooleanSetting("Affect Held Items", "Apply material to equipped items", false));
   public final BooleanSetting firstPersonArms = this.addSetting(
      new BooleanSetting("First Person Arms", "Apply material to arms independently of held items", false)
   );
   public final BooleanSetting thirdPerson = this.addSetting(new BooleanSetting("Third Person", "Style the local player in third person", true));

   public boolean YAsBoolean() {
      return this.style.pAsBoolean("Liquid Glass");
   }

   public boolean DAsBoolean() {
      return y.contains(this.style.getValue());
   }

   private boolean TAsBoolean() {
      return this.style.pAsBoolean("Camo") || this.style.pAsBoolean("Marble") || this.style.pAsBoolean("Glitter");
   }

   public int pAsInt() {
      return this.themePalette.getValue() ? UiStyle.w : this.primaryColor.getValue();
   }

   public int gAsInt() {
      return this.themePalette.getValue() ? UiStyle.Q : this.secondaryColor.getValue();
   }

   public CosmeticSubmissionHandler.Type_n rAsO0_Type_n() {
      if (this.isEnabled()) {
         String var1 = this.style.getValue();

         return switch (var1) {
            case "Rainbow Outline" -> CosmeticSubmissionHandler.Type_n.RAINBOW;
            case "Hologram" -> CosmeticSubmissionHandler.Type_n.HOLOGRAM;
            case "Liquid Glass" -> CosmeticSubmissionHandler.Type_n.GLASS;
            case "Solid" -> CosmeticSubmissionHandler.Type_n.CHAMS_SOLID;
            case "Glow" -> CosmeticSubmissionHandler.Type_n.CHAMS_GLOW;
            case "Metallic" -> CosmeticSubmissionHandler.Type_n.CHAMS_METALLIC;
            case "Camo" -> CosmeticSubmissionHandler.Type_n.CHAMS_CAMO;
            case "Marble" -> CosmeticSubmissionHandler.Type_n.CHAMS_MARBLE;
            case "Glitter" -> CosmeticSubmissionHandler.Type_n.CHAMS_GLITTER;
            case "Ghost" -> CosmeticSubmissionHandler.Type_n.CHAMS_GHOST;
            default -> null;
         };
      } else {
         return null;
      }
   }

   public double hAsDouble() {
      return this.YAsBoolean() ? this.glassOpacity.getValue() : this.modelOpacity.getValue();
   }

   public boolean VAsBoolean() {
      return !this.style.pAsBoolean("Comets") && !this.style.pAsBoolean("Outline");
   }

   public boolean MAsBoolean() {
      return this.VAsBoolean() || this.style.pAsBoolean("Outline");
   }

   public boolean KAsBoolean() {
      return this.isEnabled() ? this.style.pAsBoolean("Rainbow Outline") && this.affectCape.getValue() : false;
   }

   public SelfVisuals() {
      super("SelfVisuals", "Style your player with colors and comets", Category.VISUALS, -1);
      j = this;

      for (Setting var4 : new Setting[]{this.rainbowSpeed, this.direction, this.gradientScale, this.saturation, this.brightness}) {
         var4.visibleWhen(() -> this.style.pAsBoolean("Rainbow Outline"));
      }

      this.rainbowSpeed.visibleWhen(() -> this.style.pAsBoolean("Rainbow Outline") && !this.direction.pAsBoolean("Static"));

      for (Setting var17 : new Setting[]{this.outlineColor, this.outlineThickness, this.outlineOpacity, this.outlineBloom}) {
         var17.visibleWhen(() -> this.MAsBoolean() && this.outline.getValue());
      }

      this.affectCape.visibleWhen(() -> this.style.pAsBoolean("Rainbow Outline"));
      this.themePalette.visibleWhen(this::DAsBoolean);
      this.primaryColor.visibleWhen(() -> this.DAsBoolean() && !this.themePalette.getValue());
      this.secondaryColor.visibleWhen(() -> (this.TAsBoolean() || this.style.pAsBoolean("Metallic")) && !this.themePalette.getValue());
      this.shading.visibleWhen(() -> this.DAsBoolean() && !this.style.pAsBoolean("Glow") && !this.style.pAsBoolean("Ghost"));
      this.patternScale.visibleWhen(this::TAsBoolean);
      this.patternSpeed.visibleWhen(this::TAsBoolean);

      for (Setting var18 : new Setting[]{this.modelOpacity, this.outline, this.modelBloom, this.affectArmor, this.affectHeldItems, this.firstPersonArms}) {
         var18.visibleWhen(this::VAsBoolean);
      }

      this.modelOpacity.visibleWhen(() -> this.VAsBoolean() && !this.YAsBoolean());
      this.affectHeldItems.visibleWhen(() -> this.VAsBoolean() && !this.YAsBoolean());

      for (Setting var19 : new Setting[]{this.glassOpacity, this.surfaceFlow, this.gloss, this.edgeHighlight, this.iridescence, this.enhancedRefraction}) {
         var19.visibleWhen(this::YAsBoolean);
      }

      this.refractionStrength.visibleWhen(() -> this.YAsBoolean() && this.enhancedRefraction.getValue());
      this.outline.visibleWhen(this::MAsBoolean);

      for (Setting var20 : new Setting[]{
         this.cometSize, this.orbitSpeed, this.orbitRadius, this.trailLength, this.glowIntensity, this.cometBloom, this.themeColors
      }) {
         var20.visibleWhen(() -> this.style.pAsBoolean("Comets"));
      }

      this.cometColor.visibleWhen(() -> this.style.pAsBoolean("Comets") && !this.themeColors.getValue());
      this.tailColor.visibleWhen(() -> this.style.pAsBoolean("Comets") && !this.themeColors.getValue());
      this.style.onChanged(var0 -> {
         CometRenderer.uAsVoid();
         ClientLog.GAsVoid("SelfVisuals", "Style: {}", var0);
      });
   }

   public List<Setting<?>> CAsList() {
      return List.of(this.style, this.thirdPerson, this.outline, this.affectArmor, this.affectHeldItems, this.firstPersonArms, this.affectCape);
   }

   public List<Setting<?>> LAsList() {
      return List.of(
         this.gradientScale,
         this.saturation,
         this.brightness,
         this.themePalette,
         this.primaryColor,
         this.secondaryColor,
         this.shading,
         this.patternScale,
         this.modelOpacity,
         this.outlineColor,
         this.outlineThickness,
         this.outlineOpacity,
         this.outlineBloom,
         this.modelBloom,
         this.cometSize,
         this.glowIntensity,
         this.cometBloom,
         this.themeColors,
         this.cometColor,
         this.tailColor,
         this.glassOpacity,
         this.gloss,
         this.edgeHighlight,
         this.iridescence
      );
   }

   public List<Setting<?>> ZAsList() {
      return List.of(this.direction, this.rainbowSpeed, this.patternSpeed, this.orbitSpeed, this.orbitRadius, this.trailLength, this.surfaceFlow);
   }

   public List<Setting<?>> aAsList() {
      return List.of(this.enhancedRefraction, this.refractionStrength);
   }

   public static SelfVisuals zAsSelfVisuals() {
      return j;
   }

   public boolean tAsBoolean() {
      return this.isEnabled()
         ? SharedRenderEffects.RAsBoolean()
            && SharedRenderEffects.ZAsBoolean("SelfVisuals")
            && this.isOperational()
            && (!this.YAsBoolean() || PlayerModel.hAsPlayerModel() == null || !PlayerModel.hAsPlayerModel().zAsBoolean())
            && this.thirdPerson.getValue()
            && this.isInGame()
            && !mc.options.getPerspective().isFirstPerson()
         : false;
   }

   public static boolean HAsBoolean(LivingEntityRenderState var0) {
      if (j != null && j.isEnabled()) {
         SelfVisuals var1 = j;
         if (var1 != null
            && var1.tAsBoolean()
            && var1.VAsBoolean()
            && SharedRenderEffects.BAsBoolean(var0)
            && (!var1.YAsBoolean() || EntityMaterialRenderer.YAsBoolean())
            && var0 instanceof PlayerEntityRenderState var2
            && var2.id == getMc().player.getId()) {
            return true;
         }

         return false;
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      CometRenderer.uAsVoid();
      ClientLog.GAsVoid(
         "SelfVisuals",
         "Style: {}; third-person: {}; first-person arms: {}",
         this.style.getValue(),
         this.thirdPerson.getValue(),
         this.firstPersonArms.getValue()
      );
   }

   @Override
   public void onDisable() {
      CometRenderer.uAsVoid();
      CosmeticSubmissionHandler.XAsVoid();
      EntityHaloEffect.aAsVoid();
   }
}
