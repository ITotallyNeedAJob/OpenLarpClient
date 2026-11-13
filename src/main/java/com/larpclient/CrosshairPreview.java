package com.larpclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.MipmapStrategy;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ReloadableTexture;
import net.minecraft.client.texture.TextureContents;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public final class CrosshairPreview {
   private static final CrosshairPreview.Type_z A = new CrosshairPreview.Type_z("live");
   private static final CrosshairPreview.Type_z V = new CrosshairPreview.Type_z("preview");

   private CrosshairPreview() {
   }

   private static CrosshairPreview.Type_y sAsMw_Type_y(CrosshairPreview.Type_z var0, CrosshairShape.Type_b var1) {
      TextureManager var2 = MinecraftClient.getInstance().getTextureManager();
      if (var0.n != var2) {
         var0.o = null;
         var0.y = null;
         var0.n = var2;
      }

      if (var1.equals(var0.y)) {
         return null;
      } else {
         if (var0.o == null || !var0.o.y.equals(var1)) {
            CrosshairPreview.Type_y var3 = null;

            try {
               var3 = new CrosshairPreview.Type_y(var0.u, var1);
               var2.registerTexture(var0.u, var3);
            } catch (RuntimeException var5) {
               if (var3 != null) {
                  var3.close();
               }

               var0.y = var1;
               ClientLog.hAsVoid("Crosshair", "Mask upload failed; using simple cross until settings change", var5);
               return null;
            }

            var0.o = var3;
            var0.y = null;
            ClientLog.XAsVoid("Crosshair", "Prepared {} mask at {} pixels per GUI unit", var1.style(), var1.scale());
         }

         return var0.o;
      }
   }

   public static void EAsVoid(
      DrawContext var0, int var1, int var2, String var3, int var4, int var5, CrosshairDrawingCodec var6, int var7, boolean var8, double var9, boolean var11
   ) {
      if (var7 >>> 24 != 0) {
         CrosshairPreview.Type_z var12 = var11 ? V : A;
         double var13 = (double)MinecraftClient.getInstance().getWindow().getScaleFactor();
         double var15 = CrosshairShape.uAsDouble(Math.hypot((double)var0.getMatrices().m00(), (double)var0.getMatrices().m01()) * var13);
         CrosshairShape.Type_b var17 = new CrosshairShape.Type_b(var3, var4, var5, var8, var15, var6);
         CrosshairPreview.Type_y var18 = sAsMw_Type_y(var12, var17);
         if (var18 == null) {
            int var28 = Math.min(var4, var5 + 1);
            GuiTextureDrawing.CAsVoid(var0, var1 - var28, var2, 2 * var28 + 1, 1, var7);
            GuiTextureDrawing.CAsVoid(var0, var1, var2 - var28, 1, 2 * var28 + 1, var7);
         } else {
            var0.getMatrices().pushMatrix();

            try {
               var0.getMatrices().translate((float)var1, (float)var2);
               double var19 = var18.Z % 2 == 0 ? 0.0 : 0.5;
               double var21 = (double)var0.getMatrices().m20() * var13;
               double var23 = (double)var0.getMatrices().m21() * var13;
               var0.getMatrices()
                  .translateLocal(
                     (float)(((double)Math.round(var21 - var19) + var19 - var21) / var13), (float)(((double)Math.round(var23 - var19) + var19 - var23) / var13)
                  );
               if (var9 != 0.0) {
                  var0.getMatrices().rotate((float)var9);
               }

               var0.getMatrices().scale((float)(1.0 / var15));
               var0.getMatrices().translate((float)(-var18.Z) / 2.0F, (float)(-var18.n) / 2.0F);
               TAsVoid(var0, var12.u, var18, var7, var8);
            } finally {
               var0.getMatrices().popMatrix();
            }
         }
      }
   }

   public static void PAsVoid(DrawContext var0, int var1, int var2, CrosshairSettings.Type_i var3, int var4, int var5, double var6) {
      if (var5 >>> 24 != 0) {
         double var8 = 2.0 * (double)var4 / (double)Math.max(var3.width(), var3.height());
         float var10 = (float)((double)var3.width() * var8);
         float var11 = (float)((double)var3.height() * var8);
         var0.getMatrices().pushMatrix();

         try {
            var0.getMatrices().translate((float)var1, (float)var2);
            if (var6 != 0.0) {
               var0.getMatrices().rotate((float)var6);
            }

            var0.getMatrices().scale(var10 / 64.0F, var11 / 64.0F);
            var0.drawTexture(
               RenderPipelines.GUI_TEXTURED,
               var3.id(),
               -32,
               -32,
               0.0F,
               0.0F,
               64,
               64,
               var3.width(),
               var3.height(),
               var3.width(),
               var3.height(),
               qg.iAsInt(var5 | 16777215)
            );
         } finally {
            var0.getMatrices().popMatrix();
         }
      }
   }

   public static int hAsInt(CrosshairSettings.Type_i var0, int var1, boolean var2) {
      double var3 = 2.0 * (double)var1 / (double)Math.max(var0.width(), var0.height());
      double var5 = var2 ? Math.hypot((double)var0.width(), (double)var0.height()) * var3 / 2.0 : (double)var1;
      return (int)Math.ceil(var5 + 1.0);
   }

   private static void TAsVoid(DrawContext var0, Identifier var1, CrosshairPreview.Type_y var2, int var3, boolean var4) {
      if (var4) {
         var0.drawTexture(
            RenderPipelines.GUI_TEXTURED, var1, 0, 0, 0.0F, (float)var2.n, var2.Z, var2.n, var2.Z, var2.n, var2.Z, var2.n * 2, qg.iAsInt(var3 & 0xFF000000)
         );
      }

      var0.drawTexture(RenderPipelines.GUI_TEXTURED, var1, 0, 0, 0.0F, 0.0F, var2.Z, var2.n, var2.Z, var2.n, var2.Z, var2.n * 2, qg.iAsInt(var3));
   }

   private static final class Type_y extends ReloadableTexture {
      final CrosshairShape.Type_b y;
      final int Z;
      final int n;
      final CrosshairShape.Type_x M;

      Type_y(Identifier var1, CrosshairShape.Type_b var2) {
         super(var1);
         this.y = var2;
         this.M = new CrosshairShape(var2).jAsOr_Type_x();
         this.Z = this.M.width();
         this.n = this.M.height();
      }

      public TextureContents loadContents(ResourceManager resourceManager) {
         NativeImage var2 = new NativeImage(this.Z, this.n * 2, false);

         try {
            for (int var3 = 0; var3 < this.n * 2; var3++) {
               for (int var4 = 0; var4 < this.Z; var4++) {
                  var2.setColorArgb(var4, var3, this.M.pixels()[var3 * this.Z + var4]);
               }
            }

            return new TextureContents(var2, new TextureResourceMetadata(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var51) {
            var2.close();
            throw var51;
         }
      }
   }

   private static final class Type_z {
      final Identifier u;
      CrosshairPreview.Type_y o;
      TextureManager n;
      CrosshairShape.Type_b y;

      Type_z(String var1) {
         this.u = Identifier.of("larp-client", "crosshair/" + var1);
      }
   }
}
