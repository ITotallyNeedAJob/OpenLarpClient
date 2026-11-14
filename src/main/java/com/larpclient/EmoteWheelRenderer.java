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

final class EmoteWheelRenderer {
   static final int SahurDanceEngine = 96;
   static final int SkyChunkCache = 38;
   static final int PetEffectRenderer = 4;
   private static final int T = -((int)Math.ceil(96.0 * Math.sqrt(0.5))) - 1;
   private static final int AuctionNotifier = -97;
   private static final int K = -T * 2;
   private static final int SmoothInput = 97 - (int)Math.floor(38.0 * Math.sqrt(0.5) - 2.0);
   private static final Identifier U = Identifier.of("larp-client", "emote/wheel_wedge");
   private static EmoteWheelRenderer.Type_o A;
   private static TextureManager y;
   private static int B;

   private EmoteWheelRenderer() {
   }

   static double fAsDouble(double var0, double var2) {
      double var4 = Math.hypot(var0, var2);
      double var6 = -var2;
      double var8 = Math.min(var4 - 38.0, 96.0 - var4);
      double var10 = Math.min((var6 - var0) / Math.sqrt(2.0), (var6 + var0) / Math.sqrt(2.0)) - 2.0;
      return Math.min(var8, var10);
   }

   private static EmoteWheelRenderer.Type_o lAsPz_Type_o() {
      TextureManager var0 = MinecraftClient.getInstance().getTextureManager();
      if (var0 != y) {
         A = null;
         B = 0;
         ScreenScale = var0;
      }

      int var1 = (int)Math.clamp(Math.ceil((double)MinecraftClient.getInstance().getWindow().getScaleFactor()), 1.0, 6.0);
      if (var1 == B) {
         return null;
      } else {
         if (A == null || A.C != var1) {
            EmoteWheelRenderer.Type_o var2 = null;

            try {
               var2 = new EmoteWheelRenderer.Type_o(var1);
               var0.registerTexture(U, var2);
            } catch (RuntimeException var4) {
               if (var2 != null) {
                  var2.close();
               }

               B = var1;
               ClientLog.hAsVoid("Emotes", "Wheel wedge upload failed; drawing plain slot markers", var4);
               return null;
            }

            A = var2;
            ClientLog.XAsVoid("Emotes", "Prepared wheel wedge at {} pixels per GUI unit", var1);
         }

         return A;
      }
   }

   static void IAsVoid(DrawContext var0, int var1, double var2, int var4, int var5) {
      EmoteWheelRenderer.Type_o var6 = lAsPz_Type_o();
      if (var6 == null) {
         double var7 = (double)(var1 - 1) * Math.PI / 2.0;
         GuiTextureDrawing.RAsVoid(var0, (int)Math.round(Math.cos(var7) * 67.0), (int)Math.round(Math.sin(var7) * 67.0), 22, var4);
      } else {
         var0.getMatrices().pushMatrix();

         try {
            var0.getMatrices().rotate((float)((double)var1 * Math.PI / 2.0));
            var0.getMatrices().translate(0.0F, (float)(-var2));
            var0.drawTexture(RenderPipelines.GUI_TEXTURED, U, T, -97, 0.0F, 0.0F, K, t, var6.p, var6.j, var6.p, var6.j * 2, qg.iAsInt(var4));
            if (var5 >>> 24 != 0) {
               var0.drawTexture(RenderPipelines.GUI_TEXTURED, U, T, -97, 0.0F, (float)var6.j, K, t, var6.p, var6.j, var6.p, var6.j * 2, qg.iAsInt(var5));
            }
         } finally {
            var0.getMatrices().popMatrix();
         }
      }
   }

   private static final class Type_o extends ReloadableTexture {
      final int C;
      final int p;
      final int j;

      Type_o(int var1) {
         super(EmoteWheelRenderer.U);
         this.C = var1;
         this.p = EmoteWheelRenderer.K * var1;
         this.j = EmoteWheelRenderer.t * var1;
      }

      public TextureContents loadContents(ResourceManager resourceManager) {
         NativeImage var2 = new NativeImage(this.p, this.j * 2, false);

         try {
            for (int var3 = 0; var3 < this.j; var3++) {
               for (int var4 = 0; var4 < this.p; var4++) {
                  double var5 = (double)EmoteWheelRenderer.T + ((double)var4 + 0.5) / (double)this.C;
                  double var7 = -97.0 + ((double)var3 + 0.5) / (double)this.C;
                  double var9 = EmoteWheelRenderer.fAsDouble(var5, var7);
                  double var11 = this.zAsDouble(var9);
                  double var13 = this.zAsDouble(var9) - this.zAsDouble(var9 - 1.1);
                  var2.setColorArgb(var4, var3, (int)Math.round(var11 * 255.0) << 24 | 16777215);
                  var2.setColorArgb(var4, var3 + this.j, (int)Math.round(Math.max(0.0, var13) * 255.0) << 24 | 16777215);
               }
            }

            return new TextureContents(var2, new TextureResourceMetadata(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var151) {
            var2.close();
            throw var151;
         }
      }

      private double zAsDouble(double var1) {
         return Math.clamp(var1 * (double)this.C + 0.5, 0.0, 1.0);
      }
   }
}
