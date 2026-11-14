package com.larpclient;

import com.larpclient.module.Module;
import net.minecraft.client.gui.DrawContext;

final class ModuleToggleWidget extends GuiWidget {
   private final String D;
   private final Module w;
   private final WeatherRenderer b;

   ModuleToggleWidget(String var1, Module var2) {
      this.D = var1;
      this.w = var2;
      this.F = 26;
      this.b = new WeatherRenderer(var2.isEnabled() ? 1.0 : 0.0);
   }

   @Override
   public void render(DrawContext var1, int var2, int var3) {
      String var4 = this.D.isEmpty() ? (this.w.isEnabled() ? "Enabled" : "Disabled") : this.D;
      double var5 = this.b.bAsDouble(this.w.isEnabled() ? 1.0 : 0.0, 180);
      int var7 = ColorUtil.xAsInt(UiStyle.W, MouseInput.readableAccent(UiStyle.tAsInt(this.k), UiStyle.m), var5);
      TextDrawing.Type_s var8 = TextDrawing.Type_s.HELP;
      TextDrawing.NAsVoid(var1, TextDrawing.zAsString(var4, this.V - 38, var8), this.k + 4, this.x + (this.F - var8.zAsInt()) / 2, var7, var8);
      GuiDrawing.uAsVoid(var1, this.k + this.V - 28, this.x + (this.F - 13) / 2, var5);
      if (this.y && this.Q) {
         GuiDrawing.dAsVoid(var1, this.k + this.V - 28, this.x + (this.F - 13) / 2, 24, 13, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.RAsVoid();
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
         this.RAsVoid();
         return true;
      }
   }

   private void RAsVoid() {
      this.w.toggle();
      GuiSounds.rAsVoid(this.w.isEnabled() ? GuiSounds.Type_o.ON : GuiSounds.Type_o.OFF);
   }
}
