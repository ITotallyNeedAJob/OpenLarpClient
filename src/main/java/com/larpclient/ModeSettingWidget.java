package com.larpclient;

import com.larpclient.module.ModeSetting;
import net.minecraft.client.gui.DrawContext;

public final class ModeSettingWidget extends GuiWidget {
   private final ModeSetting j;

   public ModeSettingWidget(ModeSetting var1) {
      this.j = var1;
      this.F = 20;
   }

   @Override
   public void render(DrawContext var1, int var2, int var3) {
      int var4 = Math.max(1, this.V / this.j.getModes().size());

      for (int var5 = 0; var5 < this.j.getModes().size(); var5++) {
         String var6 = this.j.getModes().get(var5);
         boolean var7 = this.j.pAsBoolean(var6);
         int var8 = this.k + var5 * var4;
         int var9 = Math.max(1, var4 - 3);
         int var10 = var7 ? mi.readableAccent(bd.tAsInt(var8), bd.M | 0xFF000000) : bd._Z | 0xFF000000;
         int var11 = bd.hAsBoolean() ? 7 : 5;
         if (var7) {
            GuiDrawing.HAsVoid(var1, (float)var8, (float)this.x, (float)var9, (float)this.F, (float)var11, 5.0F, bg.XAsInt(var10, 0.22));
         }

         GuiDrawing.CAsVoid(var1, var8, this.x, var9, this.F, var11, var10);
         GuiDrawing.wAsVoid(var1, var8, this.x, var9, this.F, var11, var7 ? var10 : bd.A);
         TextDrawing.NAsVoid(
            var1, TextDrawing.zAsString(var6, var4 - 9, TextDrawing.Type_s.HELP), var8 + 4, this.x + 6, mi.controlInk(var10), TextDrawing.Type_s.HELP
         );
      }

      if (this.y && this.Q) {
         GuiDrawing.dAsVoid(var1, this.k, this.x, this.V, this.F, bd.hAsBoolean() ? 7 : 5);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         int var6 = Math.max(1, this.V / this.j.getModes().size());
         this.j.setValue(this.j.getModes().get(Math.clamp((long)((int)(var1 - (double)this.k) / var6), 0, this.j.getModes().size() - 1)));
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
         int var2 = this.j.getModes().indexOf(this.j.getValue());
         this.j.setValue(this.j.getModes().get(Math.floorMod(var2 + (var1 == 263 ? -1 : 1), this.j.getModes().size())));
         return true;
      }
   }
}
