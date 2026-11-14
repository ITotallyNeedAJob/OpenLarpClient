package com.larpclient;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;

public final class OxygenEffect {
   private OxygenEffect() {
   }

   public static void AAsVoid(DrawContext var0, long var1, boolean var3) {
      OverlayManager var4 = OverlayManager.W;
      int var5 = var0.getScaledWindowWidth();
      int var6 = var0.getScaledWindowHeight();
      var0.fill(0, 0, var5, var6, ColorUtil.XAsInt(-16777216, 1.0 - var4.nAsDouble(var1, var3)));
      double var7 = var4.jAsDouble(var1, var3);
      if (!(var7 <= 0.0)) {
         BlockHighlight.Type_t var9 = BlockHighlight.TAsB3_Type_t(var5, var6);
         MenuLayout.Type_c var10 = var9.glow();
         double var11 = var7 * var4.ZAsDouble(var1, var3);
         var0.createNewRootLayer();
         var0.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            StartupPresentation.m,
            var10.x(),
            var10.y(),
            0.0F,
            (float)BlockHighlight.a,
            var10.width(),
            var10.height(),
            256,
            256,
            512,
            BlockHighlight.m,
            ColorUtil.XAsInt(-1379585, var11 * 0.2)
         );
         MenuLayout.Type_c var13 = var9.logo();
         var0.createNewRootLayer();
         var0.getMatrices().pushMatrix();

         try {
            var0.getMatrices().translate((float)var13.x() + (float)var13.width() / 2.0F, (float)var13.y() + (float)var13.height() / 2.0F);
            var0.getMatrices().rotate((float)var4.PAsDouble(var1, var3));
            float var14 = (float)var4.NAsDouble(var1, var3);
            var0.getMatrices().scale(var14, var14);
            var0.drawTexture(
               RenderPipelines.GUI_TEXTURED,
               StartupPresentation.t,
               -var13.width() / 2,
               -var13.height() / 2,
               0.0F,
               0.0F,
               var13.width(),
               var13.height(),
               64,
               64,
               64,
               64,
               ColorUtil.XAsInt(-1, var11)
            );
         } finally {
            var0.getMatrices().popMatrix();
         }

         MenuLayout.Type_c var181 = var9.title();
         int var15 = var4.DAsInt(var1, var3);
         if (var15 > 0) {
            var0.createNewRootLayer();
            var0.drawTexture(
               RenderPipelines.GUI_TEXTURED,
               StartupPresentation.m,
               var181.x(),
               var181.y(),
               0.0F,
               (float)(var15 * 96),
               var181.width(),
               var181.height(),
               512,
               96,
               512,
               BlockHighlight.m,
               ColorUtil.XAsInt(-723722, var7)
            );
         }
      }
   }
}
