package com.larpclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;

public final class MenuAtmospherePanel extends WorkspacePanel {
   private final DeviceHash D;
   private final WeatherRenderer q;
   private final WeatherRenderer[] u = new WeatherRenderer[]{new WeatherRenderer(0.0), new WeatherRenderer(0.0), new WeatherRenderer(0.0)};
   private final double[] M = new double[3];
   private String Y = "";
   private long x;
   private long v;
   private QueueHandler o;
   private QueueHandler C;
   private QueueHandler O;
   private WidgetInfo B;
   private final QueueHandler[] A = new QueueHandler[3];

   public MenuAtmospherePanel(Screen var1) {
      super("Menu atmosphere", var1);
      this.D = new DeviceHash(LarpClient.CAsLarpClient().fAsBo().yAsN7().Jf);
      this.q = new WeatherRenderer((double)this.D.VAsB4().ordinal());
   }

   @Override
   public BlockErase atmosphere() {
      return this.D.VAsB4();
   }

   protected void init() {
      this.yAsVoid();
      this.B = WidgetInfo.calculate(this.width, this.height);
      this.CAsVoid(this.B.width(), this.B.height());
      this.v = System.nanoTime();

      for (int var1 = 0; var1 < this.A.length; var1++) {
         int var2 = var1;
         BlockErase var3 = BlockErase.values()[var1];
         MenuLayout.Type_c var4 = this.B.card(var1);
         QueueHandler var5 = this.A[var1] = this.AAsQh(var3.O, () -> this.D.lAsVoid(var2), var4.x(), var4.y(), var4.width());
         var5.F = var4.height();
         var5.z = true;
         var5.b = var3xx -> this.WAsVoid(var3xx, var2, var3);
      }

      this.o = this.AAsQh("‹", () -> this.D.fAsVoid(-1), this.S + 16, this.B.statusY() - 3, 24);
      this.C = this.AAsQh("›", () -> this.D.fAsVoid(1), this.S + this.r - 40, this.B.statusY() - 3, 24);
      this.o.F = this.C.F = 20;
      this.O = this.LAsQh("", this::hAsVoid, this.B.motion());
      this.LAsQh("Back", this::close, this.B.back());
      this.LAsQh("Apply", this::XAsVoid, this.B.apply());
      this.O.b = var1x -> TextDrawing.uAsVoid(
            var1x,
            TextDrawing.LAsString("Motion: " + (this.MAsBoolean() ? "On" : "Reduced"), this.O.V - 12),
            this.O.k + this.O.V / 2,
            this.O.x + 8,
            ColorUtil.pAsInt(UiStyle.u, this.O.c)
         );
   }

   private QueueHandler LAsQh(String var1, Runnable var2, MenuLayout.Type_c var3) {
      QueueHandler var4 = this.AAsQh(var1, var2, var3.x(), var3.y(), var3.width());
      var4.z = true;
      return var4;
   }

   private boolean MAsBoolean() {
      return LarpClient.CAsLarpClient().fAsBo().yAsN7().Z;
   }

   private void hAsVoid() {
      ProfileManager var1 = LarpClient.CAsLarpClient().fAsBo();
      ClientPreferences var2 = var1.yAsN7();
      var2.Z = !var2.Z;
      if (!var1.jAsBoolean()) {
         var2.Z = !var2.Z;
         this.Y = "Could not save motion preference.";
      }
   }

   private void WAsVoid(DrawContext var1, int var2, BlockErase var3) {
      QueueHandler var4 = this.A[var2];
      double var5 = this.MAsBoolean() ? this.u[var2].bAsDouble(var4.P ? 1.0 : 0.0, 280) : (var4.P ? 1.0 : 0.0);
      int var7 = var4.F - 28;
      MenuNebulaRenderer.MAsVoid(var1, var3, var4.k + 3, var4.x + 3, var4.V - 6, var7, var4.c, false);
      TextDrawing.uAsVoid(var1, TextDrawing.LAsString(var3.O, var4.V - 12), var4.k + var4.V / 2, var4.x + var4.F - 17, ColorUtil.pAsInt(UiStyle.u, var4.c));
      if (var3 == this.D.wAsB4() && var7 >= 24) {
         GuiDrawing.CAsVoid(var1, var4.k + 7, var4.x + 7, 38, 16, 3, ColorUtil.pAsInt(-535485155, var4.c));
         TextDrawing.CAsVoid(var1, "Saved", var4.k + 12, var4.x + 11, ColorUtil.pAsInt(-7873104, var4.c));
      }

      if (var5 > 0.01) {
         int var8 = Math.max(1, (int)((double)(var4.V - 16) * var5));
         GuiDrawing.CAsVoid(var1, var4.k + (var4.V - var8) / 2, var4.x + var4.F - 3, var8, 2, 1, ColorUtil.pAsInt(UiStyle.w, var5 * var4.c));
      }
   }

   private void XAsVoid() {
      ProfileManager var1 = LarpClient.CAsLarpClient().fAsBo();
      ClientPreferences var2 = var1.yAsN7();
      BlockErase var3 = var2.Jf;
      boolean var4 = var2.N;
      var2.Jf = this.D.tAsB4();
      var2.N = false;
      if (!var1.jAsBoolean()) {
         var2.Jf = var3;
         var2.N = var4;
         this.Y = "Could not save background.";
      } else {
         ClientLog.GAsVoid("Menu", "Applied atmosphere {}", var2.Jf.O);
         this.close();
      }
   }

   public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      this.fAsVoid(context);
      mouseY = this.qAsInt(mouseY);

      try {
         boolean var5 = this.MAsBoolean();
         double var6 = (double)(System.nanoTime() - this.v) / 1000000.0;
         double var8 = this.ZAsDouble();
         TextDrawing.uAsVoid(context, "Menu atmosphere", this.width / 2, this.L + 16, ColorUtil.pAsInt(UiStyle.u, var8));
         if (this.R >= 220) {
            TextDrawing.uAsVoid(context, "Preview a scene, then apply it.", this.width / 2, this.L + 35, ColorUtil.pAsInt(UiStyle.e, var8));
         }

         for (int var10 = 0; var10 < this.A.length; var10++) {
            QueueHandler var11 = this.A[var10];
            this.M[var10] = PopupScale.ZAsDouble(var6, (double)(60 + var10 * 65), 380.0, var5);
            var11.P = var10 == this.D.VAsB4().ordinal();
            var11.e = (1.0 - this.M[var10]) * 7.0;
            var11.B = !this.LAsBoolean() && this.M[var10] > 0.85;
         }

         this.o.B = !this.LAsBoolean() && this.D.VAsB4().ordinal() > 0;
         this.C.B = !this.LAsBoolean() && this.D.VAsB4().ordinal() < 2;
         String var18 = this.Y.isEmpty() ? "Saved: " + this.D.wAsB4().O : this.Y;
         TextDrawing.uAsVoid(
            context, TextDrawing.LAsString(var18, this.r - 104), this.width / 2, this.B.statusY(), ColorUtil.pAsInt(this.Y.isEmpty() ? UiStyle.e : -24915, var8)
         );
         double var19 = var5 ? this.q.bAsDouble((double)this.D.VAsB4().ordinal(), 320) : (double)this.D.VAsB4().ordinal();

         for (int var13 = 0; var13 < 3; var13++) {
            GuiTextureDrawing.RAsVoid(context, this.width / 2 + (var13 - 1) * 10, this.B.statusY() + 16, 1, ColorUtil.pAsInt(UiStyle.W, var8));
         }

         GuiTextureDrawing.RAsVoid(context, this.width / 2 + (int)Math.round((var19 - 1.0) * 10.0), this.B.statusY() + 16, 2, ColorUtil.pAsInt(UiStyle.w, var8));
         this.drawControls(context, mouseX, mouseY);
      } finally {
         this.QAsVoid(context);
      }
   }

   @Override
   protected void drawControls(DrawContext var1, int var2, int var3) {
      for (GuiWidget var5 : this.Q) {
         QueueHandler var6 = (QueueHandler)var5;
         double var7 = 1.0;

         for (int var9 = 0; var9 < this.A.length; var9++) {
            if (var6 == this.A[var9]) {
               var7 = this.M[var9];
            }
         }

         var6.c = this.ZAsDouble() * var7;
         if (this.LAsBoolean()) {
            var6.B = false;
         }

         var6.render(var1, var2, var3);
      }
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (!this.LAsBoolean() && !(this.ZAsDouble() <= 0.15)) {
         long var9 = System.nanoTime();
         if (verticalAmount != 0.0 && var9 - this.x >= 150000000L) {
            this.x = var9;
            this.D.fAsVoid(verticalAmount > 0.0 ? -1 : 1);
         }

         return true;
      } else {
         return true;
      }
   }

   @Override
   public boolean keyPressed(KeyInput input) {
      if (this.LAsBoolean() || this.ZAsDouble() <= 0.15) {
         return true;
      } else if (input.key() != 263 && input.key() != 262) {
         return super.keyPressed(input);
      } else {
         this.D.fAsVoid(input.key() == 263 ? -1 : 1);
         return true;
      }
   }
}
