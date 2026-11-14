package com.larpclient;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.client.gui.DrawContext;

public final class ThemePreviewWidget extends GuiWidget {
   private final BooleanSupplier L;
   private final Consumer<Boolean> o;

   public ThemePreviewWidget(BooleanSupplier var1, Consumer<Boolean> var2) {
      this.L = var1;
      this.o = var2;
      this.F = 52;
   }

   @Override
   public void render(DrawContext var1, int var2, int var3) {
      int var4 = Math.max(1, (this.V - 6) / 2);

      for (int var5 = 0; var5 < 2; var5++) {
         int var6 = var5 == 0 ? 1 : 0;
         int var7 = this.k + var5 * (var4 + 6);
         int var8 = var6 != 0 ? -131587 : -15724526;
         int var9 = var6 != 0 ? -1776409 : -14408664;
         GuiDrawing.CAsVoid(var1, var7, this.x, var4, 36, 6, var8);
         GuiDrawing.wAsVoid(var1, var7, this.x, var4, 36, 6, UiStyle.A);
         GuiDrawing.CAsVoid(var1, var7 + 5, this.x + 4, 8, 28, 2, var6 != 0 ? -723723 : -15132387);

         for (int var10 = 0; var10 < 3; var10++) {
            GuiDrawing.CAsVoid(var1, var7 + 16, this.x + 7 + var10 * 7, Math.max(1, var4 - 22), 4, 2, var9);
         }

         TextDrawing.NAsVoid(
            var1, var6 != 0 ? "Light" : "Dark", var7 + 4, this.x + 40, var6 == (this.L.getAsBoolean() ? 1 : 0) ? UiStyle.u : UiStyle.e, TextDrawing.Type_s.HELP
         );
      }

      if (this.y && this.Q) {
         GuiDrawing.dAsVoid(var1, this.k, this.x, this.V, this.F, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.o.accept(var1 < (double)this.k + (double)this.V / 2.0);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (var1 != 263 && var1 != 262 && var1 != 32 && var1 != 257) {
         return false;
      } else {
         this.o.accept(var1 == 263 || var1 != 262 && !this.L.getAsBoolean());
         return true;
      }
   }
}
