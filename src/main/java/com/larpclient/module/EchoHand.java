package com.larpclient.module;

import com.larpclient.HandEffectRenderer;
import com.larpclient.UiStyle;
import com.larpclient.MediaSession;
import java.awt.Color;
import java.util.List;
import java.util.Locale;

public final class EchoHand extends Module {
   private static EchoHand Q;
   public final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "History material", "Echo Trail", "Echo Trail", "Flame"));
   public final BooleanSetting echoTrail = this.addSetting(new BooleanSetting("Echo Trail", "Enable the echo trail material", true));
   public final NumberSetting a = this.qAsNumberSetting("Strength", 0.75, 0.05, 1.5, 0.05, false);
   public final NumberSetting K = this.qAsNumberSetting("Radius", 11.0, 2.0, 28.0, 0.5, false);
   public final NumberSetting E = this.qAsNumberSetting("Trail", 0.84, 0.55, 0.96, 0.01, false);
   public final NumberSetting R = this.qAsNumberSetting("Lag", 1.15, 0.0, 3.0, 0.05, false);
   public final ColorSetting color = this.addSetting(new ColorSetting("Color", "Echo color and opacity", -924394030));
   public final BooleanSetting outsideOnly = this.addSetting(new BooleanSetting("Outside Only", "Keep echoes outside the current hand silhouette", true));
   public final ModeSetting flameColor = this.addSetting(new ModeSetting("Flame Color", "Flame color source", "Custom", "Custom", "Client", "Rainbow"));
   public final ColorSetting flameColorValue = this.addSetting(new ColorSetting("Flame Color Value", "Flame color and opacity", -419430401));
   public final NumberSetting k = this.qAsNumberSetting("Flame Strength", 0.85, 0.0, 2.0, 0.05, true);
   public final NumberSetting b = this.qAsNumberSetting("Rise Speed", 0.0, 0.0, 2.0, 0.05, true);
   public final NumberSetting B = this.qAsNumberSetting("Wobble", 0.65, 0.0, 2.0, 0.05, true);
   public final NumberSetting c = this.qAsNumberSetting("Length", 0.95, 0.1, 2.5, 0.05, true);
   public final NumberSetting r = this.qAsNumberSetting("Flame Brightness", 0.9, 0.0, 2.0, 0.05, true);
   public final NumberSetting o = this.qAsNumberSetting("Width", 1.0, 0.1, 3.0, 0.05, true);
   public final NumberSetting y = this.qAsNumberSetting("Fire", 1.0, 0.0, 3.0, 0.05, true);
   public final NumberSetting S = this.qAsNumberSetting("Turbulence", 1.0, 0.0, 3.0, 0.05, true);
   public final BooleanSetting onlyItems = this.addSetting(new BooleanSetting("Only Items", "Require an item in either hand for the flame material", false));

   public EchoHand() {
      super("EchoHand", "Colored motion history around first-person hands and items", Category.RENDER, -1);
      Q = this;
      this.echoTrail.visibleWhen(() -> !this.oAsBoolean());

      for (Setting var2 : List.of(this.a, this.K, this.E, this.R, this.color, this.outsideOnly)) {
         var2.visibleWhen(() -> !this.oAsBoolean() && this.echoTrail.getValue());
      }

      this.flameColor.visibleWhen(this::oAsBoolean);
      this.flameColorValue.visibleWhen(() -> this.oAsBoolean() && this.flameColor.getValue().equals("Custom"));
      this.onlyItems.visibleWhen(this::oAsBoolean);
      this.mode.onChanged(var0 -> HandEffectRenderer.KAsVoid());
      this.echoTrail.onChanged(var0 -> HandEffectRenderer.KAsVoid());
   }

   private NumberSetting qAsNumberSetting(String var1, double var2, double var4, double var6, double var8, boolean var10) {
      NumberSetting var11 = this.addSetting(new NumberSetting(var1, var1 + " of the history material", var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 0.5 ? "%.1f" : "%.2f", this.getValue());
         }
      });
      var11.visibleWhen(() -> this.oAsBoolean() == var10);
      return var11;
   }

   public boolean oAsBoolean() {
      return this.mode.getValue().equals("Flame");
   }

   public List<Setting<?>> QAsList() {
      return List.of(this.mode, this.echoTrail, this.onlyItems);
   }

   public List<Setting<?>> pAsList() {
      return List.of(this.a, this.color, this.outsideOnly, this.flameColor, this.flameColorValue, this.k, this.r, this.c, this.o);
   }

   public List<Setting<?>> RAsList() {
      return List.of(this.K, this.E, this.R, this.b, this.B, this.y, this.S);
   }

   public static EchoHand OAsEchoHand() {
      return Q;
   }

   public int LAsInt() {
      if (!this.oAsBoolean()) {
         return this.color.getValue();
      } else {
         String var1 = this.flameColor.getValue();

         return switch (var1) {
            case "Client" -> -436207616 | bd.w & 16777215;
            case "Rainbow" -> -436207616 | Color.HSBtoRGB((float)(mb.RAsDouble() / 4.0 % 1.0), 0.72F, 1.0F) & 16777215;
            default -> this.flameColorValue.getValue();
         };
      }
   }

   @Override
   public void onDisable() {
      HandEffectRenderer.KAsVoid();
   }

   @Override
   public void onTick() {
      HandEffectRenderer.hAsVoid();
   }
}
