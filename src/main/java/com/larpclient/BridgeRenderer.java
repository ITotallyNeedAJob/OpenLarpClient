package com.larpclient;

import com.larpclient.module.ColorSetting;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import java.util.List;
import java.util.function.Consumer;

public final class BridgeRenderer {
   public final ColorSetting color1 = new ColorSetting("Color 1", "First gradient color", -16738561);
   public final ColorSetting color2 = new ColorSetting("Color 2", "Second gradient color", -16111496);
   public final NumberSetting Z = bAsNumberSetting("Line Width", "Outline radius in screen pixels", 2.0, 1.0, 8.0, 0.5);
   public final NumberSetting J = bAsNumberSetting("Outline Opacity", "Silhouette border opacity", 1.0, 0.0, 1.0, 0.05);
   public final NumberSetting P = bAsNumberSetting("Fill Opacity", "Gradient inside the silhouette", 0.25, 0.0, 1.0, 0.05);
   public final NumberSetting SahurDanceEngine = bAsNumberSetting("Glow Radius", "Gaussian halo radius in pixels", 12.0, 0.0, 32.0, 1.0);
   public final NumberSetting ScreenScale = bAsNumberSetting("Glow Intensity", "Soft halo strength", 0.75, 0.0, 1.0, 0.05);
   public final NumberSetting h = bAsNumberSetting("Speed", "Gradient cycles per second", 0.5, 0.0, 3.0, 0.1);
   public final NumberSetting D = bAsNumberSetting("Gradient Scale", "Gradient repetitions across the screen", 1.0, 0.25, 4.0, 0.25);
   private final BridgeRenderer.Type_f ChatPayParser = new BridgeRenderer.Type_f();

   public BridgeRenderer(Consumer<Setting<?>> var1) {
      for (Setting var3 : List.of(this.color1, this.color2, this.Z, this.J, this.P, this.d, this.y, this.h, this.D)) {
         var1.accept(var3);
      }

      this.d.visibleWhen(() -> this.y.getValue() > 0.0);
   }

   private static NumberSetting bAsNumberSetting(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new NumberSetting(var0, var1, var2, var4, var6, var8);
   }

   public List<Setting<?>> PAsList() {
      return List.of(this.color1, this.color2);
   }

   public List<Setting<?>> xAsList() {
      return List.of(this.Z, this.J, this.P);
   }

   public List<Setting<?>> sAsList() {
      return List.of(this.d, this.y);
   }

   public List<Setting<?>> bAsList() {
      return List.of(this.h, this.D);
   }

   public BridgeRenderer.Type_e NAsBp_Type_e(boolean var1) {
      return new BridgeRenderer.Type_e(
         this.color1.getValue(),
         this.color2.getValue(),
         this.Z.getValue().floatValue(),
         this.D.getValue().floatValue(),
         this.P.getValue().floatValue(),
         this.J.getValue().floatValue(),
         this.d.getValue().floatValue(),
         this.y.getValue().floatValue(),
         this.g.UAsFloat(mb.RAsDouble(), this.h.getValue()),
         var1
      );
   }

   public void EAsVoid() {
      this.g.zAsVoid();
   }

   public static record Type_e(
      int color1, int color2, float lineWidth, float scale, float fill, float outline, float radius, float intensity, float phase, boolean entityColor
   ) {
      public boolean glowing() {
         return this.radius > 0.0F && this.intensity > 0.0F;
      }
   }

   public static final class Type_f {
      private double M = Double.NaN;
      private double l;

      public Type_f() {
      }

      public float UAsFloat(double var1, double var3) {
         if (Double.isFinite(var1) && Double.isFinite(var3)) {
            if (Double.isFinite(this.M) && var1 >= this.M) {
               this.l = (this.l + (var1 - this.M) * Math.max(0.0, var3)) % 1.0;
            }

            this.M = var1;
            return (float)this.l;
         } else {
            return (float)this.l;
         }
      }

      public void zAsVoid() {
         this.M = Double.NaN;
         this.l = 0.0;
      }
   }
}
