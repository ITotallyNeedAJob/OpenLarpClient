package com.larpclient;

import net.minecraft.client.gui.DrawContext;

public abstract class GuiWidget {
   public int k;
   public int x;
   public int V;
   public int F = 24;
   public boolean Y = true;
   public boolean B = true;
   public boolean y;
   public boolean Q;

   public GuiWidget() {
   }

   public void bounds(int var1, int var2, int var3) {
      this.k = var1;
      this.x = var2;
      this.V = var3;
   }

   public boolean contains(double var1, double var3) {
      return this.Y && this.B && GuiTextureDrawing.ZAsBoolean(var1, var3, this.k, this.x, this.V, this.F);
   }

   public abstract void render(DrawContext var1, int var2, int var3);

   public boolean click(double var1, double var3, int var5) {
      return false;
   }

   public void drag(double var1, double var3) {
   }

   public void release() {
   }

   public boolean key(int var1) {
      return false;
   }

   public boolean character(int var1) {
      return false;
   }

   public boolean pAsBoolean(int var1) {
      return false;
   }

   public void hAsVoid() {
      this.y = false;
      this.Q = false;
      this.release();
   }
}
