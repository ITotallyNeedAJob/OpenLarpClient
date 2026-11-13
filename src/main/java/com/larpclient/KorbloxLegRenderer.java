package com.larpclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.command.RenderCommandQueue;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.util.Identifier;

public final class KorbloxLegRenderer {
   private static final Identifier v = Identifier.of("larp-client", "dynamic/korblox_leg");
   private static final int r = 15728880;
   private static final int S = 64;
   private static int m = -1;
   private static int P = -1;

   private KorbloxLegRenderer() {
   }

   public static boolean DAsBoolean() {
      int var0 = BloomQuality.J;
      if (m == var0) {
         return true;
      } else if (P == var0) {
         return false;
      } else {
         try {
            NativeImageBackedTexture var1 = new NativeImageBackedTexture(() -> "LARP Korblox leg", 64, 64, false);

            try {
               int[] var2 = ms.sAsIntArray();
               NativeImage var3 = var1.getImage();

               for (int var4 = 0; var4 < var2.length; var4++) {
                  var3.setColorArgb(var4 % 64, var4 / 64, var2[var4]);
               }

               var1.upload();
               MinecraftClient.getInstance().getTextureManager().registerTexture(v, var1);
            } catch (RuntimeException var51) {
               var1.close();
               throw var51;
            }

            m = var0;
            ClientLog.GAsVoid("Korblox", "Leg ready: {} faces, generated {}x{} texture, resource generation {}", ms.ZAsMs_Type_w().faces(), 64, 64, var0);
            return true;
         } catch (RuntimeException var61) {
            P = var0;
            ClientLog.lAsVoid("Korblox", "Leg texture unavailable; native legs stay until resource reload", var61);
            return false;
         }
      }
   }

   public static void JAsVoid(MatrixStack var0, RenderCommandQueue var1, ModelPart var2, la var3, int var4, int var5, boolean var6) {
      var0.push();

      try {
         var2.applyTransform(var0);
         var1.submitCustom(var0, KorbloxLegRenderer.Type_c.g, (var4xx, var5xx) -> aAsVoid(var4xx, var5xx, var3, var4, var5, var6, false));
         if (var3.glow() > 0.0F) {
            var1.submitCustom(
               var0, KorbloxLegRenderer.Type_c.p, (var2x, var3xx) -> aAsVoid(var2x, var3xx, var3, 15728880, OverlayTexture.DEFAULT_UV, var6, true)
            );
         }
      } finally {
         var0.pop();
      }
   }

   public static void xAsVoid(MatrixStack var0, Immediate var1, ModelPart var2, la var3, int var4, int var5, boolean var6) {
      var0.push();

      try {
         var2.applyTransform(var0);
         aAsVoid(var0.peek(), var1.getBuffer(KorbloxLegRenderer.Type_c.g), var3, var4, var5, var6, false);
         if (var3.glow() > 0.0F) {
            aAsVoid(var0.peek(), var1.getBuffer(KorbloxLegRenderer.Type_c.p), var3, 15728880, OverlayTexture.DEFAULT_UV, var6, true);
         }

         var1.draw();
      } finally {
         var0.pop();
      }
   }

   static void aAsVoid(Entry var0, VertexConsumer var1, la var2, int var3, int var4, boolean var5, boolean var6) {
      long var7 = op.RAsLong();
      ms.Type_w var9 = ms.ZAsMs_Type_w();
      float[] var10 = var9.uvs();
      float[] var11 = var9.gradient();
      float[] var12 = new float[6];
      int[] var13 = var9.quads();
      float var14 = var6 ? 0.5F : 0.0F;

      for (int var15 = 0; var15 < var9.faces(); var15++) {
         for (int var16 = 0; var16 < 4; var16++) {
            int var17 = var13[var15 * 4 + (var5 ? (4 - var16) % 4 : var16)];
            ms.dAsVoid(var9, var17, (double)var2.length(), (double)var2.thickness(), var5, var12);
            int var18 = var6 ? ms.xAsInt(var2.tip(), (double)var2.glow(), var11[var17]) : ms.EAsInt(var2.hip(), var2.tip(), var11[var17]);
            var1.vertex(var0, var12[0] / 16.0F, var12[1] / 16.0F, var12[2] / 16.0F)
               .color(var18)
               .texture(var10[var17 * 2] + var14, var10[var17 * 2 + 1])
               .overlay(var4)
               .light(var3)
               .normal(var0, var12[3], var12[4], var12[5]);
         }
      }

      op.aAsVoid("Korblox", var7);
   }

   private static final class Type_c {
      static final RenderLayer g = RenderLayers.entityCutout(KorbloxLegRenderer.v);
      static final RenderLayer p = RenderLayers.eyes(KorbloxLegRenderer.v);

      private Type_c() {
      }
   }
}
