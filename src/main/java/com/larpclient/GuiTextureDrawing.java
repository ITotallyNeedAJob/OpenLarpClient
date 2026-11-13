package com.larpclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2fStack;

public final class GuiTextureDrawing {
   public GuiTextureDrawing() {
   }

   public static void CAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5) {
      if (var3 > 0 && var4 > 0) {
         var0.fill(var1, var2, var1 + var3, var2 + var4, qg.iAsInt(var5));
      }
   }

   public static void dAsVoid(DrawContext var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      float var7 = var3 - var1;
      float var8 = var4 - var2;
      float var9 = (float)Math.hypot((double)var7, (double)var8);
      if (!(var5 <= 0.0F) && var6 >>> 24 != 0 && Float.isFinite(var9)) {
         var0.getMatrices().pushMatrix();

         try {
            var0.getMatrices().translate(var1, var2);
            var0.getMatrices().rotate((float)Math.atan2((double)var8, (double)var7));
            var0.getMatrices().translate(-var5 / 2.0F, -var5 / 2.0F);
            var0.getMatrices().scale(var9 + var5, var5);
            var0.fill(0, 0, 1, 1, qg.iAsInt(var6));
         } finally {
            var0.getMatrices().popMatrix();
         }
      }
   }

   public static void oAsVoid(DrawContext var0, int var1, int var2, int var3, boolean var4, int var5) {
      if (var3 > 0 && var5 >>> 24 != 0) {
         Matrix3x2fStack var6 = var0.getMatrices();
         float var7 = var4 ? var6.m00() : var6.m11();
         float var8 = var7 * (float)MinecraftClient.getInstance().getWindow().getScaleFactor();
         if (!(var8 <= 1.001F) && var6.m01() == 0.0F && var6.m10() == 0.0F) {
            float var9 = var4 ? var6.m20() : var6.m21();
            float var10 = (float)Math.floor((double)((var7 * (float)(var4 ? var1 : var2) + var9) * var8 / var7 + 0.001F));
            float var11 = (var10 * var7 / var8 - var9) / var7;
            var6.pushMatrix();

            try {
               if (var4) {
                  var6.translate(var11, (float)var2).scale(1.0F / var8, (float)var3);
               } else {
                  var6.translate((float)var1, var11).scale((float)var3, 1.0F / var8);
               }

               var0.fill(0, 0, 1, 1, qg.iAsInt(var5));
            } finally {
               var6.popMatrix();
            }
         } else {
            CAsVoid(var0, var1, var2, var4 ? 1 : var3, var4 ? var3 : 1, var5);
         }
      }
   }

   public static void AAsVoid(DrawContext var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      RoundedShapePipeline.nAsVoid(var0, var1, var2, var3, var4, var5, var6);
   }

   public static void pAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5) {
      CAsVoid(var0, var1, var2, var3, 1, var5);
      CAsVoid(var0, var1, var2 + var4 - 1, var3, 1, var5);
      CAsVoid(var0, var1, var2, 1, var4, var5);
      CAsVoid(var0, var1 + var3 - 1, var2, 1, var4, var5);
   }

   public static void mAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      var0.fillGradient(var1, var2, var1 + var3, var2 + var4, qg.iAsInt(var5), qg.iAsInt(var6));
   }

   public static void RAsVoid(DrawContext var0, int var1, int var2, int var3, int var4) {
      if (var3 >= 0) {
         RoundedShapePipeline.nAsVoid(var0, (float)(var1 - var3), (float)(var2 - var3), (float)(var3 * 2 + 1), (float)(var3 * 2 + 1), (float)var3 + 0.5F, var4);
      }
   }

   public static void yAsVoid(DrawContext var0, int var1, int var2, int var3, double var4, double var6, int var8) {
      int var9 = Math.max(4, Math.min(96, var3 * 4));

      for (int var10 = 0; var10 <= var9; var10++) {
         double var11 = var4 + (var6 - var4) * (double)var10 / (double)var9;
         CAsVoid(var0, var1 + (int)(Math.cos(var11) * (double)var3), var2 + (int)(Math.sin(var11) * (double)var3), 1, 1, var8);
      }
   }

   public static void AAsVoid(DrawContext var0, Identifier var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      var0.drawTexture(RenderPipelines.GUI_TEXTURED, var1, var2, var3, 0.0F, 0.0F, var4, var5, var6, var7, var6, var7);
   }

   public static void OAsVoid(DrawContext var0, int var1, int var2, int var3, int var4) {
      GuiDrawing.bAsVoid(var0, var1, var2, var3, var4);
      GuiDrawing.OAsVoid(var0, var1, var2, var3, var4, bd.l, bd.M, 0.8);
   }

   public static boolean ZAsBoolean(double var0, double var2, int var4, int var5, int var6, int var7) {
      return var0 >= (double)var4 && var0 < (double)(var4 + var6) && var2 >= (double)var5 && var2 < (double)(var5 + var7);
   }
}
