package com.larpclient;

import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class AnimatedButton extends ButtonWidget {
   private final AnimatedButton.Type_u f;
   private final w2 Z = new w2(0.0);
   private nc.Type_q q = nc.Type_q.REST;
   private double r;
   public Consumer<DrawContext> C;

   // Fully qualified: inherits ButtonWidget.Text, which shadows the imported
   // net.minecraft.text.Text inside this class. Bytecode descriptor takes the latter.
   public AnimatedButton(net.minecraft.text.Text var1, AnimatedButton.Type_u var2, Runnable var3, MenuLayout.Type_c var4) {
      super(var4.x(), var4.y(), var4.width(), var4.height(), var1, var1x -> var3.run(), DEFAULT_NARRATION_SUPPLIER);
      this.f = var2;
   }

   public static int zAsInt(AnimatedButton.Type_u var0, double var1) {
      int var3 = switch (var0) {
         case PRIMARY -> -2171170;
         case SECONDARY -> -5592406;
         case QUIET -> -6513508;
         case SESSION -> -3618616;
      };
      return bg.xAsInt(var3, -657931, var1);
   }

   public static int lAsInt(double var0, boolean var2) {
      return var2 ? -657931 : bg.XAsInt(-3092272, var0 * 0.65);
   }

   public static void LAsVoid(DrawContext var0, float var1, int var2, float var3, int var4) {
      if (!(var3 <= 0.0F) && var4 >>> 24 != 0) {
         var0.getMatrices().pushMatrix();

         try {
            var0.getMatrices().translate(var1, (float)var2);
            var0.getMatrices().scale(var3 / 2.0F, 1.0F);
            var0.fill(-1, 0, 1, 1, var4);
         } finally {
            var0.getMatrices().popMatrix();
         }
      }
   }

   public void rAsVoid(nc.Type_q var1) {
      this.q = var1;
   }

   public double eAsDouble() {
      return this.q.alpha();
   }

   public double HAsDouble() {
      return this.r;
   }

   protected void drawIcon(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      if (!(this.q.alpha() <= 0.0)) {
         context.getMatrices().pushMatrix();

         try {
            float var5 = (float)this.getX() + (float)this.getWidth() / 2.0F;
            float var6 = (float)this.getY() + (float)this.getHeight() / 2.0F;
            context.getMatrices().translate((float)((double)var5 + this.q.x()), (float)((double)var6 + this.q.y()));
            context.getMatrices().rotate((float)this.q.rotation());
            context.getMatrices().scale((float)this.q.scaleX(), (float)this.q.scaleY());
            context.getMatrices().translate(-var5, -var6);
            this.wAsVoid(context);
         } finally {
            context.getMatrices().popMatrix();
         }
      }
   }

   private void wAsVoid(DrawContext var1) {
      boolean var2 = LarpClient.CAsLarpClient().fAsBo().yAsN7().Z;
      boolean var3 = MinecraftClient.getInstance().getNavigationType().isKeyboard();
      int var4 = this.active && this.isFocused() && var3 ? 1 : 0;
      double var5 = this.active && this.isHovered() && !var3 ? 1.0 : 0.0;
      double var7 = var2 ? this.Z.bAsDouble(var5, 140) : var5;
      if (var4 != 0) {
         var7 = 1.0;
      }

      this.r = var7;
      if (this.C != null) {
         this.C.accept(var1);
      } else {
         int var9 = this.getX();
         int var10 = this.getY();
         int var11 = this.getWidth();
         int var12 = this.getHeight();
         String var13 = TextDrawing.sAsString(this.getMessage().getString(), var11 - 40, TextDrawing.Type_x.MEDIUM);
         int var14 = TextDrawing.qAsInt(var13, TextDrawing.Type_x.MEDIUM);
         int var15 = var10 + (var12 - 9) / 2;
         TextDrawing.oAsVoid(var1, var13, var9 + (var11 - var14) / 2, var15, bg.pAsInt(zAsInt(this.f, var7), this.q.alpha()), TextDrawing.Type_x.MEDIUM);
         if (var7 > 0.0) {
            LAsVoid(
               var1,
               (float)var9 + (float)var11 / 2.0F,
               Math.min(var15 + 13, var10 + var12 - 1),
               (float)((double)var14 * var7),
               bg.pAsInt(lAsInt(var7, var4 != 0), this.q.alpha())
            );
         }
      }
   }

   public static enum Type_u {
      PRIMARY,
      SECONDARY,
      QUIET,
      SESSION;

      private Type_u() {
      }
   }
}
