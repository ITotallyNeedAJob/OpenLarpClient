package com.larpclient;

import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.DrawContext;

public final class SectionHeaderWidget extends GuiWidget {
   private final String P;
   private final BooleanSupplier s;
   private final boolean j;

   public SectionHeaderWidget(String var1) {
      this(var1, () -> true);
   }

   public SectionHeaderWidget(String var1, BooleanSupplier var2) {
      this(var1, var2, false);
   }

   public SectionHeaderWidget(String var1, BooleanSupplier var2, boolean var3) {
      this.P = var1;
      this.s = var2;
      this.j = var3;
      this.F = var3 ? 16 : 22;
      this.B = false;
   }

   public boolean nAsBoolean() {
      return this.s.getAsBoolean();
   }

   @Override
   public void render(DrawContext var1, int var2, int var3) {
      if (this.Y) {
         if (this.j) {
            TextDrawing.NAsVoid(var1, TextDrawing.zAsString(this.P, this.V, TextDrawing.Type_s.SECTION), this.k, this.x + 3, bd.u, TextDrawing.Type_s.SECTION);
         } else {
            TextDrawing.Type_s var4 = TextDrawing.Type_s.EYEBROW;
            String var5 = TextDrawing.zAsString(this.P.toUpperCase(Locale.ROOT), this.V - 8, var4);
            int var6 = this.x + this.F - var4.zAsInt() - 2;
            TextDrawing.NAsVoid(var1, var5, this.k + 4, var6, bd.W, var4);
            if (bd.hAsBoolean()) {
               int var7 = this.k + 4 + TextDrawing.jAsInt(var5, var4) + 6;
               GuiTextureDrawing.oAsVoid(var1, var7, var6 + var4.zAsInt() / 2, this.k + this.V - 4 - var7, false, bd.I);
            }
         }
      }
   }
}
