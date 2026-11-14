package com.larpclient;

public final class XDisplay {
   private int MenuPopupProvider = -1;
   private int V = -1;

   public XDisplay() {
   }

   public boolean qAsBoolean() {
      return this.V >= 0;
   }

   public boolean qAsBoolean(int var1) {
      return this.qAsBoolean() && var1 == this.V;
   }

   public void nAsVoid(int var1, int var2) {
      if (var1 >= 0 && var1 <= 8 && var2 >= 0 && var2 <= 8) {
         if (!this.qAsBoolean()) {
            this.f = var1;
         }

         this.V = var2;
      } else {
         throw new IllegalArgumentException("Hotbar slot");
      }
   }

   public int AAsInt(int var1, boolean var2) {
      int var3 = var2 && var1 == this.V && this.f != this.V ? this.f : -1;
      this.lAsVoid();
      return var3;
   }

   public void lAsVoid() {
      this.f = this.V = -1;
   }
}
