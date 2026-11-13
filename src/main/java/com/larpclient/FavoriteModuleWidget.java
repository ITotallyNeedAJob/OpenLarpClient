package com.larpclient;

import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.DrawContext;

public final class FavoriteModuleWidget extends GuiWidget {
   private final BooleanSupplier L;
   private final Runnable H;
   private final w2 q;
   private final w2 T = new w2(0.0);
   private final w2 R = new w2(0.0);
   private long h;
   private ActionButton t;

   public FavoriteModuleWidget(BooleanSupplier var1, Runnable var2) {
      this.L = var1;
      this.H = var2;
      this.q = new w2(var1.getAsBoolean() ? 1.0 : 0.0);
   }

   public void cAsVoid(ActionButton var1) {
      this.t = var1;
      this.bounds(var1.k + var1.V - 62, var1.x + (var1.F - 22) / 2, 22);
      this.F = 22;
      this.Y = var1.Y;
      this.B = var1.B;
   }

   public String OAsString() {
      return this.L.getAsBoolean() ? "Remove from favorites" : "Add to favorites";
   }

   @Override
   public void render(DrawContext var1, int var2, int var3) {
      if (this.Y) {
         double var4 = this.q.bAsDouble(this.L.getAsBoolean() ? 1.0 : 0.0, 160);
         double var6 = this.T.bAsDouble(this.contains((double)var2, (double)var3) || this.y && this.Q ? 1.0 : 0.0, 160);
         double var8 = this.R.bAsDouble(System.nanoTime() < this.h ? 1.0 : 0.0, 100);
         mi var10 = this.t == null ? mi.atAsMi(0.0, 0.0, bd.w) : this.t.IAsMi();
         float var11 = this.t == null ? 0.0F : this.t.JAsFloat();
         var1.getMatrices().pushMatrix();

         try {
            var1.getMatrices().translate(0.0F, -var11);
            if (var6 > 0.001 || var8 > 0.001) {
               GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, bd.T, bg.XAsInt(var10.title(), var6 * 0.07 + var8 * 0.07));
            }

            int var12 = this.k + (this.V - 11) / 2;
            int var13 = this.x + (this.F - 11) / 2;
            int var14 = bg.xAsInt(var10.subtitle(), var10.title(), var6 * 0.5);
            GuiIcons.aAsVoid(var1, GuiIcons.PAsIdentifier("star"), var12, var13, 11, var14);
            if (var4 > 0.001 && mi.contrast(bd.w, var10.background()) < 3.0) {
               GuiIcons.aAsVoid(var1, GuiIcons.PAsIdentifier("star"), var12 - 1, var13 - 1, 13, var14);
            }

            if (var4 > 0.001) {
               GuiIcons.aAsVoid(var1, GuiIcons.PAsIdentifier("star-filled"), var12, var13, 11, bg.pAsInt(bd.w | 0xFF000000, var4));
            }

            if (this.y && this.Q) {
               GuiDrawing.dAsVoid(var1, this.k, this.x, this.V, this.F, 4);
            }
         } finally {
            var1.getMatrices().popMatrix();
         }
      }
   }

   public void cAsVoid(DrawContext var1, int var2, int var3) {
      String var4 = TextDrawing.HAsString(this.OAsString(), var2 - 22);
      int var5 = TextDrawing.UAsInt(var4) + 14;
      int var6 = Math.clamp((long)(this.k + this.V / 2 - var5 / 2), 4, Math.max(4, var2 - var5 - 4));
      int var7 = Math.clamp((long)(this.x - 23), 4, Math.max(4, var3 - 23));
      GuiDrawing.CAsVoid(var1, var6, var7, var5, 19, 4, bd.m);
      TextDrawing.UAsVoid(var1, var4, var6 + 7, var7 + 5, bd.u);
   }

   private void cAsVoid() {
      this.h = System.nanoTime() + 110000000L;
      this.H.run();
      GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.cAsVoid();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.Y || !this.B || !this.y || var1 != 257 && var1 != 32) {
         return false;
      } else {
         this.cAsVoid();
         return true;
      }
   }
}
