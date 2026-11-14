package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.ClientLog;
import com.larpclient.CrosshairDrawingCodec;
import com.larpclient.CrosshairPreview;
import com.larpclient.CrosshairSettings;
import com.larpclient.CrosshairShape;
import com.larpclient.HudEditorScreen;
import com.larpclient.HudRenderer;
import com.larpclient.UiStyle;
import com.larpclient.WidgetLayoutUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.Identifier;

public final class CustomCrosshair extends Module {
   public static final String R = "Tung Tung";
   private static final Map<String, String> y = Map.of("6-7", "67", "Custom Image", "Tung Tung");
   private static CustomCrosshair m;
   private final ModeSetting style = this.addSetting(
      new ModeSetting("Style", "Crosshair geometry", "Star of David", Stream.concat(CrosshairShape.t.stream(), Stream.of("Tung Tung")).toArray(String[]::new)) {
         @Override
         public boolean acceptsValue(String var1) {
            return CustomCrosshair.y.containsKey(var1) || super.acceptsValue(var1);
         }

         @Override
         public void setValue(String var1) {
            super.setValue(CustomCrosshair.y.getOrDefault(var1, var1));
         }
      }
   );
   private final TextSetting drawing = this.addSetting(new TextSetting("Drawing", "Cells of the drawn crosshair", CrosshairDrawingCodec.O.eAsString()));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Shape radius; half the image width", 7.0, 2.0, 24.0, 1.0));
   private final NumberSetting thickness = this.addSetting(new NumberSetting("Thickness", "Line width", 1.0, 1.0, 5.0, 1.0));
   private final BooleanSetting themeColor = this.addSetting(new BooleanSetting("Theme Color", "Follow the interface accent color", true));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Crosshair color and opacity", -16737281));
   private final BooleanSetting outline = this.addSetting(new BooleanSetting("Outline", "Thin dark contour for bright and dark scenery", false));
   private final BooleanSetting spin = this.addSetting(new BooleanSetting("Spin", "Continuously rotate the crosshair", true));
   private final NumberSetting spinSpeed = this.addSetting(new NumberSetting("Spin Speed", "Degrees per second", 180.0, 30.0, 720.0, 15.0));
   private final BooleanSetting clickSpin = this.addSetting(new BooleanSetting("Click Spin", "Turn a quarter on each left click", false));
   private final NumberSetting clickSpinTime = this.addSetting(
      new NumberSetting("Click Spin Time", "Milliseconds per quarter turn", 350.0, 100.0, 1000.0, 25.0)
   );
   private double s;
   private long F;
   private double P;
   private double u;
   private double I;
   private long r;
   private CrosshairSettings.Type_i S;
   private String Y;
   private CrosshairDrawingCodec T = CrosshairDrawingCodec.O;

   public CustomCrosshair() {
      super("CustomCrosshair", "Customize your crosshair", Category.RENDER, -1);
      m = this;
      this.spinSpeed.visibleWhen(this.spin::getValue);
      this.color.visibleWhen(() -> !this.themeColor.getValue());
      this.clickSpinTime.visibleWhen(this.clickSpin::getValue);
      this.thickness.visibleWhen(() -> CrosshairShape.AAsBoolean(this.style.getValue()));
      this.outline.visibleWhen(() -> !this.style.pAsBoolean("Tung Tung"));
      this.drawing.visibleWhen(() -> false);
      this.style.onChanged(var0 -> ClientLog.GAsVoid("Crosshair", "Style selected: {}", var0));
      this.spin.onChanged(var0 -> ClientLog.GAsVoid("Crosshair", "Spin {}", var0 ? "enabled" : "disabled"));
      this.clickSpin.onChanged(var1 -> {
         this.gAsVoid();
         ClientLog.GAsVoid("Crosshair", "Click Spin {}", var1 ? "enabled" : "disabled");
      });
      HudElementRegistry.addLast(
         Identifier.of("larp-client", "customcrosshair"),
         (var1, var2) -> {
            MinecraftClient var3 = getMc();
            HudRenderer.wAsVoid(var1);
            if (this.isOperational()
               && this.isInGame()
               && !var3.options.hudHidden
               && var3.options.getPerspective().isFirstPerson()
               && (
                  var3.currentScreen == null
                     || var3.currentScreen.getClass() == ChatScreen.class
                     || var3.currentScreen instanceof HudEditorScreen var4 && var4.pAsBoolean()
               )) {
               HudRenderer.Type_m var9 = HudRenderer.QAsLp_Type_m("crosshair");
               if (!var9.EAsBoolean()) {
                  var9.PAsVoid(var3.getWindow().getScaledWidth(), var3.getWindow().getScaledHeight());
                  var9.uAsVoid(var1, var9.PAsInt(var3.getWindow().getScaledWidth()), var9.ZAsInt(var3.getWindow().getScaledHeight()));

                  try {
                     this.WAsVoid(
                        var1, var9.PAsInt(var3.getWindow().getScaledWidth()) + var9.v / 2, var9.ZAsInt(var3.getWindow().getScaledHeight()) + var9.y / 2
                     );
                  } finally {
                     var9.IAsVoid(var1);
                  }
               }
            }
         }
      );
   }

   public static void yAsVoid(DrawContext var0, int var1, int var2) {
      if (m != null) {
         m.KAsVoid(var0, var1, var2, true);
      }
   }

   public static int JAsInt() {
      return m == null ? 10 : m.qAsInt(m.style.pAsBoolean("Tung Tung") ? m.S : null);
   }

   private int qAsInt(CrosshairSettings.Type_i var1) {
      int var2 = this.size.getValue().intValue();
      return var1 != null
         ? CrosshairPreview.hAsInt(var1, var2, this.spin.getValue() || this.clickSpin.getValue())
         : CrosshairShape.nAsInt(this.style.getValue(), var2, this.thickness.getValue().intValue(), this.outline.getValue());
   }

   public List<Setting<?>> yAsList() {
      return List.of(this.style, this.drawing, this.size, this.thickness);
   }

   public List<Setting<?>> CAsList() {
      return List.of(this.themeColor, this.color, this.outline, this.spin, this.spinSpeed, this.clickSpin, this.clickSpinTime);
   }

   public boolean TAsBoolean() {
      return this.style.pAsBoolean("Tung Tung");
   }

   public boolean YAsBoolean() {
      return this.style.pAsBoolean("Draw Your Own");
   }

   public CrosshairDrawingCodec mAsXp() {
      String var1 = this.drawing.getValue();
      if (!var1.equals(this.Y)) {
         this.Y = var1;

         try {
            this.T = CrosshairDrawingCodec.dAsXp(var1);
         } catch (IllegalArgumentException var3) {
            this.T = CrosshairDrawingCodec.O;
            ClientLog.oAsVoid("Crosshair", "Saved drawing is unreadable ({}); showing the default until it is edited", var3.getMessage());
         }
      }

      return this.T;
   }

   public void bAsVoid(int var1, int var2, boolean var3) {
      CrosshairDrawingCodec var4 = this.mAsXp().qAsXp(var1, var2, var3);
      if (var4 != this.T) {
         this.drawing.dAsVoid(var4.eAsString());
      }
   }

   public void TAsVoid() {
      this.drawing.dAsVoid(CrosshairDrawingCodec.g.eAsString());
      ClientLog.GAsVoid("Crosshair", "Drawing cleared");
   }

   public static void aAsVoid(JsonObject var0) {
      if (!var0.has("Theme Color") && var0.has("Color")) {
         try {
            int var1 = (int)((long)var0.get("Color").getAsDouble());
            var0.addProperty("Theme Color", var1 == -1381654 || var1 == UiStyle.w);
         } catch (RuntimeException var2) {
         }
      }
   }

   private int gAsInt() {
      return this.themeColor.getValue() ? UiStyle.w | 0xFF000000 : this.color.getValue();
   }

   public int yAsInt() {
      return this.gAsInt() | 0xFF000000;
   }

   @Override
   public void onEnable() {
      this.s = 0.0;
      this.gAsVoid();
   }

   @Override
   public void onDisable() {
      this.s = 0.0;
      this.gAsVoid();
      this.S = null;
      CrosshairSettings.yAsVoid();
   }

   public static void MAsVoid(int var0) {
      if (m != null && m.isEnabled()) {
         CustomCrosshair var1 = m;
         if (var0 == 0 && var1 != null && var1.isOperational() && var1.clickSpin.getValue() && getMc().player != null && getMc().currentScreen == null) {
            var1.qAsVoid(System.nanoTime());
         }
      }
   }

   private void gAsVoid() {
      this.P = this.u = this.I = 0.0;
      this.r = 0L;
   }

   private void qAsVoid(long var1) {
      double var3 = this.clickSpinTime.getValue() / 1000.0;
      double var5 = this.r == 0L ? 1.0 : Math.clamp((double)(var1 - this.r) / 1.0E9 / var3, 0.0, 1.0);
      double var7 = this.TAsDouble(var5);
      double var9 = this.WAsDouble(var5);
      double var11 = Math.floor(var7 / (Math.PI * 2)) * 2.0 * Math.PI;
      this.P = var7 - var11;
      this.u = this.u - var11 + (Math.PI / 2);
      this.I = var9;
      this.r = var1;
   }

   private double TAsDouble(double var1) {
      double var3 = this.clickSpinTime.getValue() / 1000.0;
      double var5 = var1 * var1;
      double var7 = var5 * var1;
      return (2.0 * var7 - 3.0 * var5 + 1.0) * this.P + (var7 - 2.0 * var5 + var1) * this.I * var3 + (3.0 * var5 - 2.0 * var7) * this.u;
   }

   private double WAsDouble(double var1) {
      double var3 = this.clickSpinTime.getValue() / 1000.0;
      double var5 = var1 * var1;
      return ((6.0 * var5 - 6.0 * var1) * this.P + (3.0 * var5 - 4.0 * var1 + 1.0) * this.I * var3 + (6.0 * var1 - 6.0 * var5) * this.u) / var3;
   }

   private double sAsDouble() {
      if (this.clickSpin.getValue() && this.r != 0L) {
         double var1 = Math.clamp((double)(System.nanoTime() - this.r) / 1.0E9 / (this.clickSpinTime.getValue() / 1000.0), 0.0, 1.0);
         return this.TAsDouble(var1);
      } else {
         return this.u;
      }
   }

   private double OAsDouble() {
      long var1 = System.nanoTime();
      if (!this.spin.getValue()) {
         this.s = 0.0;
         this.F = var1;
         return 0.0;
      } else {
         double var3 = Math.clamp((double)(var1 - this.F) / 1.0E9, 0.0, 0.1);
         this.F = var1;
         this.s = (this.s + var3 * Math.toRadians(this.spinSpeed.getValue())) % (Math.PI * 2);
         return this.s;
      }
   }

   private void WAsVoid(DrawContext var1, int var2, int var3) {
      this.KAsVoid(var1, var2, var3, false);
   }

   private void KAsVoid(DrawContext var1, int var2, int var3, boolean var4) {
      MinecraftClient var5 = getMc();
      int var6 = this.size.getValue().intValue();
      int var7 = this.thickness.getValue().intValue();
      int var8 = this.gAsInt();
      CrosshairSettings.Type_i var9 = null;
      if (this.style.pAsBoolean("Tung Tung")) {
         double var10 = Math.hypot((double)var1.getMatrices().m00(), (double)var1.getMatrices().m01()) * (double)var5.getWindow().getScaleFactor();
         var9 = CrosshairSettings.dAsA0_Type_i((int)Math.ceil((double)(2 * var6) * var10), var4);
      }

      if (!var4) {
         this.S = var9;
      }

      if (!var4 && var8 >>> 24 != 0) {
         HudRenderer.Type_m var12 = HudRenderer.QAsLp_Type_m("crosshair");
         int var11 = this.qAsInt(var9);
         HudRenderer.xAsVoid(
            var12,
            new WidgetLayoutUtil.Type_p(
               var12.PAsInt(var5.getWindow().getScaledWidth()) + (int)Math.floor(((double)var12.v / 2.0 - (double)var11) * var12.vAsDouble()),
               var12.ZAsInt(var5.getWindow().getScaledHeight()) + (int)Math.floor(((double)var12.y / 2.0 - (double)var11) * var12.vAsDouble()),
               (int)Math.ceil((double)(2 * var11) * var12.vAsDouble()),
               (int)Math.ceil((double)(2 * var11) * var12.vAsDouble())
            )
         );
      }

      double var13 = this.OAsDouble() + this.sAsDouble();
      if (var9 != null) {
         CrosshairPreview.PAsVoid(var1, var2, var3, var9, var6, var8, var13);
      } else {
         CrosshairPreview.EAsVoid(
            var1,
            var2,
            var3,
            this.style.pAsBoolean("Tung Tung") ? "Star of David" : this.style.getValue(),
            var6,
            var7,
            this.mAsXp(),
            var8,
            this.outline.getValue(),
            var13,
            var4
         );
      }
   }

   public static boolean qAsBoolean() {
      return m != null && m.isEnabled() ? m != null && m.isOperational() : false;
   }
}
