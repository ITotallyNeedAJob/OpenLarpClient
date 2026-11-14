package com.larpclient.module;

import com.larpclient.BloomQuality;
import com.larpclient.ClientLog;
import com.larpclient.SkyShaderPreset;
import com.larpclient.UiStyle;
import com.larpclient.BloomSettings;
import com.larpclient.DropletTrailRenderer;
import com.larpclient.MediaSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.world.dimension.DimensionType.Skybox;

public final class SkyShader extends Module {
   private static SkyShader K;
   private final ModeSetting mode = this.addSetting(
      new ModeSetting(
         "Mode",
         "Larp sky shader",
         "Core Caustic",
         "Water",
         "Caustic",
         "Nebula",
         "Aurora",
         "Starfall",
         "BlackHole",
         "Plasma",
         "Voronoi",
         "Core Caustic",
         "Core Nebula",
         "Sunset",
         "Thunderstorm"
      )
   );
   private final NumberSetting L = this.iAsNumberSetting("Speed", "Animation speed", 1.2, 0.1, 5.0, 0.1);
   private final NumberSetting R = this.iAsNumberSetting("Scale", "Pattern scale", 5.0, 1.0, 20.0, 0.5);
   private final NumberSetting m = this.iAsNumberSetting("Intensity", "Pattern intensity", 0.01, 0.001, 0.05, 0.001);
   private final NumberSetting O = this.iAsNumberSetting("Alpha", "Sky opacity", 1.0, 0.3, 1.0, 0.05);
   private final BooleanSetting themeColor = this.addSetting(new BooleanSetting("Theme Color", "Use the client accent", true));
   private final ColorSetting customColor = this.addSetting(new ColorSetting("Custom Color", "Sky tint", -1));
   private final NumberSetting l = this.iAsNumberSetting("Lightning Rate", "How often lightning strikes", 1.0, 0.25, 3.0, 0.05);
   private final bl I = new bl();
   private final HashMap<String, dt> Y = new HashMap<>();
   private final HashSet<String> w = new HashSet<>();
   private int F = -1;
   private Object M;
   private double a = Double.NaN;
   private String i;
   private boolean q;
   private boolean P;
   private String r;
   private String v;
   private int T;
   private float C;
   private int A;

   public SkyShader() {
      super("Sky Shader", "Show animated sky effects", Category.VISUALS, -1);
      K = this;
      this.customColor.visibleWhen(() -> !this.themeColor.getValue());
      this.R.visibleWhen(() -> !this.rAsBoolean() && !this.mode.pAsBoolean("BlackHole"));
      this.m.visibleWhen(() -> !this.rAsBoolean());
      this.O.visibleWhen(() -> !this.rAsBoolean());
      this.l.visibleWhen(() -> this.mode.pAsBoolean("Thunderstorm"));
   }

   private NumberSetting iAsNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public static SkyShader EAsSkyShader() {
      return K;
   }

   public ModeSetting mode() {
      return this.mode;
   }

   public List<Setting<?>> tAsList() {
      return List.of(this.themeColor, this.customColor, this.m, this.O);
   }

   public List<Setting<?>> lAsList() {
      return List.of(this.L, this.R, this.l);
   }

   private boolean rAsBoolean() {
      return this.mode.pAsBoolean("Core Caustic")
         || this.mode.pAsBoolean("Core Nebula")
         || this.mode.pAsBoolean("Sunset")
         || this.mode.pAsBoolean("Thunderstorm");
   }

   public int ZAsInt() {
      return this.themeColor.getValue() ? bd.w : this.customColor.getValue();
   }

   public float TAsFloat() {
      return this.L.getValue().floatValue();
   }

   public float NAsFloat() {
      return this.R.getValue().floatValue();
   }

   public float FAsFloat() {
      return this.m.getValue().floatValue();
   }

   public float BAsFloat() {
      return this.l.getValue().floatValue();
   }

   public float jAsFloat() {
      return this.rAsBoolean() ? 1.0F : this.O.getValue().floatValue();
   }

   public int FAsInt() {
      String var1 = this.mode.getValue();
      int var2 = this.ZAsInt();
      float var3 = this.FAsFloat();
      if (!var1.equals(this.v) || var2 != this.T || var3 != this.C) {
         this.v = var1;
         this.T = var2;
         this.C = var3;
         this.A = SkyShaderPreset.gAsInt(var1, var2, var3);
      }

      return this.A;
   }

   public boolean CAsBoolean() {
      if (!this.isEnabled()) {
         return false;
      } else {
         CustomSky var1 = CustomSky.NAsCustomSky();
         return this.isRuntimeActive()
            && this.isInGame()
            && (var1 == null || !var1.isOperational())
            && getMc().world.getDimension().skybox() == Skybox.OVERWORLD
            && getMc().gameRenderer.getCamera().getSubmersionType() == CameraSubmersionType.NONE
            && (this.F != BloomQuality.J || !this.w.contains(this.mode.getValue()));
      }
   }

   public void tAsVoid() {
      this.P = false;
   }

   public boolean aAsBoolean() {
      return this.isEnabled() ? this.P && this.CAsBoolean() && this.mode.getValue().equals(this.r) : false;
   }

   @Override
   public void onEnable() {
      this.YAsVoid();
      ClientLog.GAsVoid("SkyShader", "Enabled; mode={}; CustomSky takes precedence when enabled", this.mode.getValue());
   }

   @Override
   public void onDisable() {
      this.YAsVoid();
   }

   @Override
   public void onTick() {
      if (this.M != getMc().world) {
         this.YAsVoid();
         this.M = getMc().world;
      }

      CustomSky var1 = CustomSky.NAsCustomSky();
      int var2 = var1 != null && var1.isOperational() ? 1 : 0;
      if (var2 != 0 && !this.q) {
         ClientLog.GAsVoid("SkyShader", "CustomSky is enabled; Larp sky is suspended without changing either preference");
      }

      this.q = var2 != 0;
      if (var2 != 0) {
         this.eAsVoid();
         this.a = Double.NaN;
      }
   }

   public boolean ZAsBoolean(Framebuffer var1) {
      if (!this.isEnabled()) {
         return false;
      } else {
         this.P = false;
         if (!this.CAsBoolean()) {
            return false;
         } else {
            String var2 = this.mode.getValue();

            try {
               if (this.F != BloomQuality.J) {
                  this.YAsVoid();
                  this.F = BloomQuality.J;
                  this.M = getMc().world;
               }

               if (!var2.equals(this.i)) {
                  this.a = Double.NaN;
                  this.i = var2;
                  ClientLog.GAsVoid(
                     "SkyShader", "Rendering Larp shader {}; full-screen coverage, vanilla celestial/dark-disc/cloud suppression after success", var2
                  );
               }
               String var3 = switch (var2) {
                  case "Core Caustic" -> "core_caustic";
                  case "Core Nebula" -> "core_nebula";
                  default -> var2.toLowerCase(Locale.ROOT);
               };
               dt var13 = this.Y.computeIfAbsent(var2, var2x -> new dt(var3, this));
               double var14 = mb.RAsDouble();
               this.I.a = Double.isNaN(this.a) ? 0.0 : Math.clamp(var14 - this.a, 0.0, 0.1);
               this.a = var14;
               this.I.L = var1;
               var13.render(this.I);
               this.P = true;
               this.r = var2;
               return true;
            } catch (IOException | RuntimeException var111) {
               this.w.add(var2);
               this.eAsVoid();
               ClientLog.lAsVoid("SkyShader", var2 + " failed; vanilla sky until resource reload or disable/enable", var111);
            } finally {
               this.I.L = null;
            }

            return false;
         }
      }
   }

   private void eAsVoid() {
      for (dt var2 : this.Y.values()) {
         var2.close();
      }

      this.I.I.close();
   }

   public void YAsVoid() {
      this.P = false;
      this.r = null;
      this.eAsVoid();
      this.Y.clear();
      this.w.clear();
      this.F = -1;
      this.M = null;
      this.i = null;
      this.a = Double.NaN;
      this.q = false;
   }
}
