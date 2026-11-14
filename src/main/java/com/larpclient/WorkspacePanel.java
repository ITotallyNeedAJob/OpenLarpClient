package com.larpclient;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;

public abstract class WorkspacePanel extends GuiPanel implements xs {
   protected final Screen e;
   protected int S;
   protected int L;
   protected int r;
   protected int R;
   private final w2 J = new w2(0.0);
   private boolean j;
   private boolean F;
   private long K;
   private double ChatPayParser = 1.0;
   private double W;
   private double SkyChunkCache = 1.0;

   protected WorkspacePanel(String var1, Screen var2) {
      super(var1);
      this.e = var2;
   }

   @Override
   public boolean dimAtmosphere() {
      return true;
   }

   protected qh AAsQh(String var1, Runnable var2, int var3, int var4, int var5) {
      qh var6 = new qh(var1, "", var2);
      var6.bounds(var3, var4, var5);
      var6.F = 24;
      this.Q.add(var6);
      return var6;
   }

   protected void CAsVoid(int var1, int var2) {
      this.r = Math.min(var1, this.width - 24);
      this.R = Math.min(var2, this.height - 24);
      this.S = (this.width - this.r) / 2;
      this.L = (this.height - this.R) / 2;
   }

   protected void fAsVoid(DrawContext var1) {
      if (!this.j) {
         this.j = true;
         this.J.aAsVoid(0.0);
      }

      double var2 = LarpClient.CAsLarpClient().fAsBo().yAsN7().Z ? this.J.bAsDouble(1.0, 260) : 1.0;
      if (this.F) {
         var2 = this.s * (1.0 - w2.Type_e.CUBIC_IN.zAsDouble((double)(System.nanoTime() - this.K) / 1.6E8));
      }

      this.g = var2;
      this.W = LarpClient.CAsLarpClient().fAsBo().yAsN7().Z ? (1.0 - var2) * 12.0 : 0.0;
      var1.getMatrices().pushMatrix();
      var1.getMatrices().translate(0.0F, (float)this.W);
      GuiDrawing.bAsVoid(var1, this.S, this.L, this.r, this.R);
      GuiDrawing.CAsVoid(var1, this.S, this.L, this.r, this.R, 7, bg.pAsInt(-451076058, var2));
   }

   protected void drawControls(DrawContext var1, int var2, int var3) {
      for (GuiWidget var5 : this.Q) {
         if (var5.Y) {
            if (var5 instanceof qh var6) {
               var6.c = this.g;
            }

            if (this.F) {
               var5.B = false;
            }

            var5.render(var1, var2, var3);
         }
      }
   }

   public void close() {
      if (!this.F) {
         if (!LarpClient.CAsLarpClient().fAsBo().yAsN7().Z) {
            this.client.setScreen(this.e);
         } else {
            this.F = true;
            this.s = this.g;
            this.K = System.nanoTime();

            for (GuiWidget var2 : this.Q) {
               var2.B = false;
            }
         }
      }
   }

   public void tick() {
      if (this.F && System.nanoTime() - this.K >= 160000000L) {
         this.client.setScreen(this.e);
      }
   }

   @Override
   public boolean keyPressed(KeyInput input) {
      return !this.j || this.g <= 0.15 || this.F || super.keyPressed(input);
   }

   @Override
   public boolean charTyped(CharInput input) {
      return !this.j || this.g <= 0.15 || this.F || super.charTyped(input);
   }

   private Click PAsClick(Click var1) {
      return new Click(var1.x(), var1.y() - this.W, var1.buttonInfo());
   }

   @Override
   public boolean mouseClicked(Click click, boolean doubled) {
      return !this.j || this.g <= 0.15 || this.F || super.mouseClicked(this.PAsClick(click), doubled);
   }

   @Override
   public boolean mouseDragged(Click click, double offsetX, double offsetY) {
      return this.F || super.mouseDragged(this.PAsClick(click), offsetX, offsetY);
   }

   @Override
   public boolean mouseReleased(Click click) {
      return super.mouseReleased(this.PAsClick(click));
   }

   protected int qAsInt(int var1) {
      return (int)Math.round((double)var1 - this.W);
   }

   protected double IAsDouble(double var1) {
      return var1 - this.W;
   }

   protected void QAsVoid(DrawContext var1) {
      var1.getMatrices().popMatrix();
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      return this.F;
   }

   protected boolean LAsBoolean() {
      return this.F;
   }

   protected double ZAsDouble() {
      return this.g;
   }
}
