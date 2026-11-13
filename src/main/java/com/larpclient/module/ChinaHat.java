package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.ChinaHatFireRenderer;
import com.larpclient.ClientLog;
import com.larpclient.CosmeticSubmissionHandler;
import com.larpclient.EffectDrawBuffers;
import com.larpclient.EffectGeometryRenderer;
import com.larpclient.HatMeshLoader;
import com.larpclient.HornsMeshRenderer;
import com.larpclient.bd;
import com.larpclient.bg;
import com.larpclient.mb;
import com.larpclient.x4;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public final class ChinaHat extends Module {
   private static ChinaHat v;
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "Hat style", "Cone", "Cone", "Kippah", "Crown", "Halo", "Horns"));
   private final ColorSetting kippahColor = this.addSetting(new ColorSetting("Kippah Color", "Skullcap fabric color", -15258800));
   private final ColorSetting starColor = this.addSetting(new ColorSetting("Star Color", "Star of David embroidery", -7517));
   private final NumberSetting kippahSize = this.addSetting(bAsNumberSetting("Kippah Size", "Skullcap radius in blocks", 0.22, 0.15, 0.3, 0.01));
   private final NumberSetting kippahHeight = this.addSetting(bAsNumberSetting("Kippah Height", "Rounded dome height in blocks", 0.08, 0.03, 0.14, 0.01));
   private final NumberSetting kippahOffset = this.addSetting(
      new NumberSetting("Kippah Offset", "Clearance above the head in blocks", 0.005, 0.0, 0.1, 0.005) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.3f", this.getValue());
         }
      }
   );
   private final NumberSetting accessorySize = this.addSetting(bAsNumberSetting("Accessory Size", "Crown, halo or horns scale", 1.0, 0.6, 1.4, 0.05));
   private final NumberSetting accessoryOpacity = this.addSetting(bAsNumberSetting("Accessory Opacity", "Crown or halo transparency", 1.0, 0.1, 1.0, 0.05));
   private final BooleanSetting self = this.addSetting(new BooleanSetting("Self", "Show your hat in third person", true));
   private final BooleanSetting otherPlayers = this.addSetting(
      new BooleanSetting("Other Players", "Local preview on nearby players; never advertises their hats", false)
   );
   private final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use the current theme gradient", true));
   private final ColorSetting topColor = this.addSetting(new ColorSetting("Top Color", "Cone tip color", -3429889));
   private final ColorSetting rimColor = this.addSetting(new ColorSetting("Rim Color", "Brim color", -8890948));
   private final NumberSetting radius = this.addSetting(new NumberSetting("Radius", "Brim radius in blocks", 0.6, 0.25, 1.0, 0.05));
   private final NumberSetting height = this.addSetting(new NumberSetting("Height", "Cone height in blocks", 0.3, 0.1, 0.7, 0.05));
   private final NumberSetting offset = this.addSetting(new NumberSetting("Offset", "Height above the head", 0.04, 0.0, 0.5, 0.01));
   private final NumberSetting opacity = this.addSetting(new NumberSetting("Opacity", "Hat transparency", 0.65, 0.1, 1.0, 0.05));
   private final BooleanSetting outline = this.addSetting(new BooleanSetting("Outline", "Fine brim outline", true));
   private final ColorSetting hornColor = this.addSetting(new ColorSetting("Horn Color", "Horn and thorn color", -61184));
   private final BooleanSetting hornTheme = this.addSetting(new BooleanSetting("Horn Theme", "Horns use the theme's second color", false));
   private final ColorSetting fireColor = this.addSetting(new ColorSetting("Fire Color", "Outer flames and the horn tips", -42752));
   private final ColorSetting fireCoreColor = this.addSetting(new ColorSetting("Fire Core Color", "Bright inner flames and the hottest tip points", -2304));
   private final BooleanSetting fireTheme = this.addSetting(new BooleanSetting("Fire Theme", "Fire uses the theme accent", false));
   private final NumberSetting fireSize = this.addSetting(bAsNumberSetting("Fire Size", "Width and height of the fire between the horns", 1.0, 0.5, 2.0, 0.05));
   private final NumberSetting fireHeat = this.addSetting(bAsNumberSetting("Fire Heat", "How fast and high the flames rise", 1.0, 0.5, 2.0, 0.05));
   private List<ChinaHat.Type_k> f = List.of();
   private ClientWorld y;
   private final Map<Integer, ChinaHatFireRenderer> K = new HashMap<>();
   private long A;
   private long c = -1L;

   public ChinaHat() {
      super("China Hat", "Wear a cone hat, kippah, crown, halo or fiery horns", Category.VISUALS, -1);
      v = this;

      for (Setting var4 : new Setting[]{this.kippahColor, this.starColor, this.kippahSize, this.kippahHeight, this.kippahOffset}) {
         var4.visibleWhen(() -> this.mode.pAsBoolean("Kippah"));
      }

      for (Setting var17 : new Setting[]{this.themeColors, this.radius, this.height, this.offset}) {
         var17.visibleWhen(() -> this.mode.pAsBoolean("Cone"));
      }

      this.accessorySize.visibleWhen(() -> this.OAsNu_Type_l() != null || this.gAsBoolean());
      this.accessoryOpacity.visibleWhen(() -> this.OAsNu_Type_l() != null);

      for (Setting var18 : new Setting[]{this.opacity, this.outline}) {
         var18.visibleWhen(() -> this.OAsNu_Type_l() == null && !this.gAsBoolean());
      }

      for (Setting var19 : new Setting[]{this.hornTheme, this.fireTheme, this.fireSize, this.fireHeat}) {
         var19.visibleWhen(this::gAsBoolean);
      }

      this.hornColor.visibleWhen(() -> this.gAsBoolean() && !this.hornTheme.getValue());

      for (Setting var20 : new Setting[]{this.fireColor, this.fireCoreColor}) {
         var20.visibleWhen(() -> this.gAsBoolean() && !this.fireTheme.getValue());
      }

      this.topColor.visibleWhen(() -> this.mode.pAsBoolean("Cone") && !this.themeColors.getValue());
      this.rimColor.visibleWhen(() -> this.mode.pAsBoolean("Cone") && !this.themeColors.getValue());
   }

   private static NumberSetting bAsNumberSetting(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new NumberSetting(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      };
   }

   private HatMeshLoader.Type_l OAsNu_Type_l() {
      return this.mode.pAsBoolean("Crown") ? HatMeshLoader.Type_l.CROWN : (this.mode.pAsBoolean("Halo") ? HatMeshLoader.Type_l.HALO : null);
   }

   private boolean gAsBoolean() {
      return this.mode.pAsBoolean("Horns");
   }

   private ChinaHat.Type_y FAsChinaHat_Type_y() {
      int var1 = this.fireTheme.getValue() ? bd.w : this.fireColor.getValue();
      return new ChinaHat.Type_y(
         this.hornTheme.getValue() ? bd.Q : this.hornColor.getValue(),
         var1,
         this.fireTheme.getValue() ? HornsMeshRenderer.lAsInt(bd.w) : this.fireCoreColor.getValue(),
         this.fireSize.getValue(),
         this.fireHeat.getValue()
      );
   }

   public Setting<?> XAsSetting() {
      return this.mode;
   }

   public Setting<?> WAsSetting() {
      return this.self;
   }

   public static ChinaHat uAsChinaHat() {
      return v;
   }

   @Override
   public void onEnable() {
      ClientLog.GAsVoid(
         "ChinaHat", "Cosmetic enabled; mode={}, cached geometry, depth tested; Kippah, Crown, Halo and Horns follow vanilla head pose", this.mode.getValue()
      );
   }

   private JsonObject lAsJsonObject() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("version", 1);
      var1.addProperty("enabled", this.isOperational() && this.mode.pAsBoolean("Cone"));
      var1.addProperty("top", this.themeColors.getValue() ? bd.w : this.topColor.getValue());
      var1.addProperty("rim", this.themeColors.getValue() ? bd.Q : this.rimColor.getValue());
      var1.addProperty("radius", this.radius.getValue());
      var1.addProperty("height", this.height.getValue());
      var1.addProperty("offset", this.offset.getValue());
      var1.addProperty("opacity", this.opacity.getValue());
      var1.addProperty("outline", this.outline.getValue());
      return var1;
   }

   public ChinaHat.Type_a VAsChinaHat_Type_a() {
      if (this.isEnabled()) {
         if (!this.isOperational() || !this.self.getValue()) {
            return null;
         } else if (this.gAsBoolean()) {
            double var5 = this.accessorySize.getValue();
            double var3 = (0.24F + ChinaHatFireRenderer.NAsDouble(1.0, this.fireHeat.getValue()) * this.fireSize.getValue()) * var5;
            return new ChinaHat.Type_a(false, var5, Math.max(HornsMeshRenderer.XAsDouble(var5), var3), 0.0, -1, 0, false, null, this.FAsChinaHat_Type_y());
         } else {
            HatMeshLoader.Type_l var1 = this.OAsNu_Type_l();
            if (var1 != null) {
               return new ChinaHat.Type_a(
                  false,
                  this.accessorySize.getValue(),
                  HatMeshLoader.LAsDouble(var1, this.accessorySize.getValue()),
                  0.0,
                  bg.XAsInt(16777215, this.accessoryOpacity.getValue()),
                  0,
                  false,
                  var1
               );
            } else {
               double var2 = this.opacity.getValue();
               return this.mode.pAsBoolean("Kippah")
                  ? new ChinaHat.Type_a(
                     true,
                     this.kippahSize.getValue(),
                     this.kippahHeight.getValue(),
                     this.kippahOffset.getValue(),
                     bg.XAsInt(this.kippahColor.getValue(), var2),
                     bg.XAsInt(this.starColor.getValue(), var2),
                     this.outline.getValue()
                  )
                  : new ChinaHat.Type_a(
                     false,
                     this.radius.getValue(),
                     this.height.getValue(),
                     this.offset.getValue(),
                     bg.XAsInt(this.themeColors.getValue() ? bd.w : this.topColor.getValue(), var2),
                     bg.XAsInt(this.themeColors.getValue() ? bd.Q : this.rimColor.getValue(), var2),
                     this.outline.getValue()
                  );
            }
         }
      } else {
         return null;
      }
   }

   public List<Setting<?>> aAsList() {
      return List.of(this.self, this.otherPlayers);
   }

   public List<Setting<?>> FAsList() {
      return List.of(
         this.mode,
         this.themeColors,
         this.topColor,
         this.rimColor,
         this.kippahColor,
         this.starColor,
         this.hornTheme,
         this.hornColor,
         this.fireTheme,
         this.fireColor,
         this.fireCoreColor,
         this.opacity,
         this.accessoryOpacity,
         this.outline,
         this.fireSize,
         this.fireHeat
      );
   }

   public List<Setting<?>> QAsList() {
      return List.of(this.radius, this.height, this.offset, this.kippahSize, this.kippahHeight, this.kippahOffset, this.accessorySize);
   }

   @Override
   public void onDisable() {
      this.f = List.of();
      this.y = null;
      this.K.clear();
      this.c = -1L;
   }

   public void OAsVoid(float var1) {
      if (this.isEnabled()) {
         if (this.y != null && this.y != mc.world) {
            this.K.clear();
         }

         this.f = List.of();
         this.y = null;
         this.c = -1L;
         if (!this.isInGame()) {
            this.K.clear();
         } else {
            ArrayList<ChinaHat.Type_k> var2 = new ArrayList<>();
            JsonObject var3 = this.lAsJsonObject();
            var3.addProperty("enabled", this.isEnabled());
            boolean var4 = this.mode.pAsBoolean("Kippah");
            HatMeshLoader.Type_l var5 = this.OAsNu_Type_l();
            HornsMeshRenderer.Type_s var6 = this.gAsBoolean() ? HornsMeshRenderer.RAsM5_Type_s() : null;
            ChinaHat.Type_y var7 = var6 == null ? null : this.FAsChinaHat_Type_y();
            int var8 = 0;

            for (AbstractClientPlayerEntity var10 : mc.world.getPlayers()) {
               if ((
                     var10 == mc.player
                        ? this.isOperational() && this.self.getValue() && !mc.options.getPerspective().isFirstPerson()
                        : this.isOperational() && this.otherPlayers.getValue()
                  )
                  && var3.get("enabled").getAsBoolean()) {
                  int var12 = bg.XAsInt(var3.get("top").getAsInt(), var3.get("opacity").getAsDouble());
                  int var13 = bg.XAsInt(var3.get("rim").getAsInt(), var3.get("opacity").getAsDouble());
                  if (var10.isAlive()
                     && !var10.isInvisible()
                     && !var10.isSpectator()
                     && !var10.isSwimming()
                     && !var10.isGliding()
                     && !var10.isCrawling()
                     && !var10.isSleeping()
                     && !(var10.squaredDistanceTo(mc.player) > 2304.0)) {
                     if (this.gAsBoolean()) {
                        if (var6 != null && (var10 == mc.player || var8++ < 16)) {
                           ChinaHatFireRenderer var17 = this.K.computeIfAbsent(var10.getId(), var0 -> new ChinaHatFireRenderer((long)var0.intValue()));
                           var2.add(
                              new ChinaHat.Type_k(
                                 false,
                                 null,
                                 CosmeticSubmissionHandler.oAsEntityRenderState(var10, var1),
                                 this.accessorySize.getValue(),
                                 0.0,
                                 0.0,
                                 -1,
                                 0,
                                 false,
                                 null,
                                 var7,
                                 var17
                              )
                           );
                        }
                     } else if (var5 != null) {
                        if (var10 == mc.player || var8++ < 16) {
                           var2.add(
                              new ChinaHat.Type_k(
                                 false,
                                 null,
                                 CosmeticSubmissionHandler.oAsEntityRenderState(var10, var1),
                                 this.accessorySize.getValue(),
                                 0.0,
                                 0.0,
                                 bg.XAsInt(16777215, this.accessoryOpacity.getValue()),
                                 0,
                                 false,
                                 var5,
                                 null,
                                 null
                              )
                           );
                        }
                     } else if (var4) {
                        if (var10 == mc.player || var8++ < 16) {
                           double var16 = this.opacity.getValue();
                           var2.add(
                              new ChinaHat.Type_k(
                                 true,
                                 null,
                                 CosmeticSubmissionHandler.oAsEntityRenderState(var10, var1),
                                 this.kippahSize.getValue(),
                                 this.kippahHeight.getValue(),
                                 this.kippahOffset.getValue(),
                                 bg.XAsInt(this.kippahColor.getValue(), var16),
                                 bg.XAsInt(this.starColor.getValue(), var16),
                                 this.outline.getValue()
                              )
                           );
                        }
                     } else {
                        Vec3d var14 = var10.getLerpedPos(var1).add(0.0, (double)var10.getHeight() + var3.get("offset").getAsDouble(), 0.0);
                        var2.add(
                           new ChinaHat.Type_k(
                              false,
                              var14,
                              CosmeticSubmissionHandler.oAsEntityRenderState(var10, var1),
                              var3.get("radius").getAsDouble(),
                              var3.get("height").getAsDouble(),
                              var3.get("offset").getAsDouble(),
                              var12,
                              var13,
                              var3.get("outline").getAsBoolean()
                           )
                        );
                     }
                  }
               }
            }

            this.y = mc.world;
            this.f = List.copyOf(var2);
            this.K.values().removeIf(var1x -> var2.stream().noneMatch(var1xxx -> var1xxx.fire == var1x));
         }
      }
   }

   public void LAsVoid(EffectGeometryRenderer var1, float var2) {
      if (this.isEnabled()) {
         this.A++;
         if (this.y == mc.world) {
            var1.blend(x4.Type_g.ALPHA);
            double var3 = mb.RAsDouble();
            byte var5 = 0;

            for (ChinaHat.Type_k var7 : this.f) {
               if (var7.horns != null) {
                  if (var5 != 0 || (var5 = (byte)(HornsMeshRenderer.XAsBoolean(var7.horns.horn(), var7.horns.fire(), var7.horns.core()) ? 1 : 0)) != 0) {
                     Matrix4f var8 = var7.model == null ? null : var1.zAsMatrix4f(var7.model, var7.radius);
                     if (var8 != null) {
                        var7.fire.M.set(var8);
                        var7.fire.q = this.c = this.A;
                     }
                  }
               } else if (var7.accessory != null) {
                  if (var7.model != null) {
                     var1.QAsVoid(var7.model, var7.accessory, var7.radius, (double)(var7.top >>> 24) / 255.0);
                  }
               } else if (var7.kippah && var7.model != null) {
                  var1.dAsVoid(var7.model, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline);
               } else if (!var7.kippah) {
                  var1.UAsVoid(var7.model, var7.base, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline);
               }
            }

            for (ChinaHat.Type_k var10 : this.f) {
               if (var10.fire != null && var10.fire.q == this.A) {
                  var1.zAsVoid(var10.fire, var10.fire.M, var10.radius, var10.horns.size(), var10.horns.heat(), var10.horns.fire(), var10.horns.core(), var3);
               }
            }
         }
      }
   }

   public boolean jAsBoolean() {
      return this.isEnabled() ? this.isOperational() && this.y == mc.world && this.c == this.A : false;
   }

   public void QAsVoid(EffectGeometryRenderer var1, EffectDrawBuffers var2) {
      if (this.isEnabled() && this.jAsBoolean()) {
         for (ChinaHat.Type_k var4 : this.f) {
            if (var4.fire != null && var4.fire.q == this.A) {
               var1.ZAsVoid(var4.fire.M, var4.radius);
            }
         }

         for (ChinaHat.Type_k var6 : this.f) {
            if (var6.fire != null && var6.fire.q == this.A) {
               var1.pAsVoid(var6.fire, var6.horns.fire(), var6.horns.core(), 0.6);
            }
         }

         var2.CAsVoid();
      }
   }

   public static record Type_a(
      boolean kippah, double radius, double height, double offset, int top, int rim, boolean outline, HatMeshLoader.Type_l accessory, ChinaHat.Type_y horns
   ) {
      public Type_a(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
         this(var1, var2, var4, var6, var8, var9, var10, null, null);
      }

      public Type_a(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10, HatMeshLoader.Type_l var11) {
         this(var1, var2, var4, var6, var8, var9, var10, var11, null);
      }

      public double rise() {
         return this.offset + this.height;
      }

      public String label() {
         return this.horns != null ? "Horns" : (this.accessory != null ? this.accessory.m : (this.kippah ? "Kippah" : "Cone"));
      }
   }

   private static record Type_k(
      boolean kippah,
      Vec3d base,
      EntityRenderState model,
      double radius,
      double height,
      double offset,
      int top,
      int rim,
      boolean outline,
      HatMeshLoader.Type_l accessory,
      ChinaHat.Type_y horns,
      ChinaHatFireRenderer fire
   ) {
      Type_k(boolean var1, Vec3d var2, EntityRenderState var3, double var4, double var6, double var8, int var10, int var11, boolean var12) {
         this(var1, var2, var3, var4, var6, var8, var10, var11, var12, null, null, null);
      }
   }

   public static record Type_y(int horn, int fire, int core, double size, double heat) {
   }
}
