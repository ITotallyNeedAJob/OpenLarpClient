package com.larpclient;

import java.util.function.IntConsumer;
import net.minecraft.client.gui.DrawContext;

public final class qj extends GuiWidget {
   private static final int[] f = new int[]{-15198181, -1096636, -429290, -1395960, -14498466, -16337196, -12877066, -5745161, -1292135};
   private final IntConsumer U;
   private int a;

   public qj(IntConsumer var1) {
      this.U = var1;
      this.F = 18;
   }

   @Override
   public void render(DrawContext var1, int var2, int var3) {
      int var4 = Math.max(1, this.V / f.length);
      int var5 = Math.min(12, var4 - 1);

      for (int var6 = 0; var6 < f.length; var6++) {
         int var7 = this.k + var6 * var4;
         GuiDrawing.CAsVoid(var1, var7, this.x + 2, var5, var5, 4, f[var6]);
         RoundedShapePipeline.SAsVoid(var1, (float)var7, (float)(this.x + 2), (float)var5, (float)var5, 4.0F, bg.pAsInt(bd.I, 1.6), bg.pAsInt(bd.a, 2.0));
         if (bd.w == f[var6]) {
            RoundedShapePipeline.cAsVoid(var1, (float)(var7 - 2), (float)this.x, (float)(var5 + 4), (float)(var5 + 4), 6.0F, bg.XAsInt(bd.u, 0.8));
         }

         if (this.y && this.Q && this.a == var6) {
            GuiDrawing.dAsVoid(var1, var7 - 2, this.x, var5 + 4, var5 + 4, 6);
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.a = Math.clamp((long)((int)(var1 - (double)this.k) / Math.max(1, this.V / f.length)), 0, f.length - 1);
         this.U.accept(f[this.a]);
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
         this.a = Math.floorMod(this.a + (var1 == 263 ? -1 : (var1 == 262 ? 1 : 0)), f.length);
         this.U.accept(f[this.a]);
         return true;
      }
   }
}
