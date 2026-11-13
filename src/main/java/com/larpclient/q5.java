package com.larpclient;

import net.minecraft.client.gui.DrawContext;

public final class q5 extends GuiWidget {
   private final String R;
   private final String z;

   public q5(String var1, String var2) {
      this.R = var1;
      this.z = var2;
      this.B = false;
   }

   @Override
   public void render(DrawContext var1, int var2, int var3) {
      if (bd.hAsBoolean()) {
         GuiDrawing.PAsVoid(var1, this.k, this.x, this.V, this.F, bd.E, bd.M, 0.6);
      } else {
         GuiDrawing.OAsVoid(var1, this.k, this.x, this.V, this.F, 6, bd.M, 0.3);
      }

      TextDrawing.NAsVoid(var1, TextDrawing.zAsString(this.R, this.V - 20, TextDrawing.Type_s.MODULE), this.k + 10, this.x + 8, bd.u, TextDrawing.Type_s.MODULE);
      TextDrawing.NAsVoid(var1, TextDrawing.zAsString(this.z, this.V - 20, TextDrawing.Type_s.HELP), this.k + 10, this.x + 21, bd.e, TextDrawing.Type_s.HELP);
   }
}
