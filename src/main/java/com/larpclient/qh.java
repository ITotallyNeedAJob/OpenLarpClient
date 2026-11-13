package com.larpclient;

import java.util.function.Consumer;
import net.minecraft.client.gui.DrawContext;

public final class qh extends GuiWidget {
   public final String i;
   public final String u;
   private final Runnable C;
   private final w2 v = new w2(0.0);
   private final w2 Z = new w2(0.0);
   private final w2 m = new w2(0.0);
   private final w2 I = new w2(0.0);
   private aq.Type_x a = new aq.Type_x(1.0, 0.0);
   public boolean z;
   public double e;
   public Consumer<DrawContext> b;
   public boolean P;
   public boolean J;
   public double c = 1.0;
   private long t;

   public qh(String var1, String var2, Runnable var3) {
      this.i = var1;
      this.u = var2;
      this.C = var3;
   }

   @Override
   public void render(DrawContext var1, int var2, int var3) {
      if (this.Y) {
         boolean var4 = LarpClient.CAsLarpClient().fAsBo().yAsN7().Z;
         double var5 = var4
            ? this.v.bAsDouble(this.contains((double)var2, (double)var3) || this.y && this.Q ? 1.0 : 0.0, 140)
            : (this.contains((double)var2, (double)var3) || this.y && this.Q ? 1.0 : 0.0);
         double var7 = var4 ? this.Z.bAsDouble(System.nanoTime() < this.t ? 1.0 : 0.0, 90) : 0.0;
         double var9 = var4 ? this.m.bAsDouble(this.P ? 1.0 : 0.0, 220) : (this.P ? 1.0 : 0.0);
         this.a = aq.vAsAq_Type_x(var5, var7, this.e, var4, this.z);
         int var11 = this.J ? -24915 : bd.u;
         var1.getMatrices().pushMatrix();

         try {
            var1.getMatrices().translate((float)this.k + (float)this.V / 2.0F, (float)((double)this.x + (double)this.F / 2.0 + this.a.offsetY()));
            var1.getMatrices().scale((float)this.a.scale(), (float)this.a.scale());
            var1.getMatrices().translate((float)(-this.k) - (float)this.V / 2.0F, (float)(-this.x) - (float)this.F / 2.0F);
            GuiDrawing.CAsVoid(
               var1,
               this.k,
               this.x,
               this.V,
               this.F,
               5,
               bg.pAsInt(bg.xAsInt(-987684056, bg.XAsInt(this.J ? -24915 : bd.w, 0.45), Math.max(var9 * 0.55, var5 * 0.6 + var7 * 0.2)), this.c)
            );
            if (this.y && this.Q) {
               GuiDrawing.vAsVoid(var1, this.k, this.x, this.V, this.F, bg.pAsInt(bd.w, this.c));
            }

            if (this.b != null) {
               this.b.accept(var1);
            } else if (!this.u.isEmpty()) {
               GuiIcons.aAsVoid(
                  var1, GuiIcons.PAsIdentifier(this.u), this.k + (this.V - 14) / 2, this.x + (this.F - 14) / 2, 14, bg.pAsInt(this.B ? var11 : bd.W, this.c)
               );
            } else {
               TextDrawing.uAsVoid(
                  var1, TextDrawing.LAsString(this.i, this.V - 12), this.k + this.V / 2, this.x + (this.F - 9) / 2, bg.pAsInt(this.B ? var11 : bd.W, this.c)
               );
            }
         } finally {
            var1.getMatrices().popMatrix();
         }
      }
   }

   @Override
   public boolean contains(double var1, double var3) {
      return super.contains(this.a.localX(var1, (double)this.k + (double)this.V / 2.0), this.a.localY(var3, (double)this.x + (double)this.F / 2.0));
   }

   public void BAsVoid(DrawContext var1, int var2, int var3, int var4, int var5) {
      if (this.Y) {
         boolean var6 = LarpClient.CAsLarpClient().fAsBo().yAsN7().Z;
         int var7 = this.contains((double)var2, (double)var3) || this.y && this.Q ? 1 : 0;
         double var8 = var6 ? this.I.bAsDouble(var7 != 0 ? 1.0 : 0.0, var7 != 0 ? 160 : 100) : (var7 != 0 ? 1.0 : 0.0);
         if (!(var8 < 0.01)) {
            String var10 = TextDrawing.LAsString(this.i, var4 - 20);
            int var11 = TextDrawing.nAsInt(var10) + 14;
            int var12 = Math.clamp((long)(this.k + this.V / 2 - var11 / 2), 4, Math.max(4, var4 - var11 - 4));
            int var13 = Math.clamp((long)(this.x - 23 + (int)Math.round(this.a.offsetY() + (1.0 - var8) * 4.0)), 4, Math.max(4, var5 - 24));
            GuiDrawing.CAsVoid(var1, var12, var13, var11, 19, 4, bg.pAsInt(-283567328, var8));
            TextDrawing.CAsVoid(var1, var10, var12 + 7, var13 + 5, bg.pAsInt(bd.u, var8));
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.t = System.nanoTime() + 90000000L;
         this.C.run();
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
         this.t = System.nanoTime() + 90000000L;
         this.C.run();
         return true;
      }
   }
}
