package com.larpclient;

import com.larpclient.module.PlayerModel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.RenderSetup.OutlineMode;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

public final class CashFanRenderer {
   private static final Identifier S = mAsIdentifier("textures/particles/cash.png");
   private static final Identifier BloomUtility = mAsIdentifier("dynamic/cash_fan");
   public static final int Z = 4;
   private static NativeImageBackedTexture o;
   private static long H = -1L;
   private static long G = -1L;

   private CashFanRenderer() {
   }

   private static Identifier mAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }

   public static boolean WAsBoolean(PlayerEntityModel var0, PlayerEntityRenderState var1, MatrixStack var2, OrderedRenderCommandQueue var3, int var4) {
      o6 var5 = (o6)var1;
      float var6 = var5.larp$emoteWeight();
      if (!(var6 <= 0.0F) && "cash_fans".equals(var5.larp$emoteProp())) {
         PlayerModel var7 = PlayerModel.hAsPlayerModel();
         if (var7 != null && var7.FAsBoolean(var1)) {
            return false;
         } else {
            float[] var8 = var5.larp$emoteSample();
            int var9 = EmotecraftJsonReader.Type_r.PROP.ordinal() * 10;
            double var10 = dAsDouble(var8[var9 + 7], 1.0) * (double)var6;
            if (!fAsBoolean()) {
               return false;
            } else if (var10 < 0.001) {
               return true;
            } else {
               double var12 = dAsDouble(var8[var9 + 3], 0.0);
               double var14 = dAsDouble(var8[var9 + 4], 0.0);
               double var16 = dAsDouble(var8[var9 + 5], 0.0);
               double var18 = dAsDouble(var8[var9 + 8], 0.0);

               for (Arm var23 : Arm.values()) {
                  float[] var24 = pf.mAsFloatArray(var23 == Arm.RIGHT, var12, var14, var16, var18, var10, (double)var5.larp$emoteCycle());
                  var2.push();

                  try {
                     var0.setArmAngle(var1, var23, var2);
                     var3.submitCustom(var2, CashFanRenderer.Type_m.A, (var2x, var3x) -> BAsVoid(var24, var2x, var3x, var4));
                  } finally {
                     var2.pop();
                  }
               }

               op.YAsVoid("Cash fan bills", 14L);
               return true;
            }
         }
      } else {
         return false;
      }
   }

   private static double dAsDouble(float var0, double var1) {
      return Float.isNaN(var0) ? var1 : (double)var0;
   }

   private static void BAsVoid(float[] var0, Entry var1, VertexConsumer var2, int var3) {
      for (int var4 = 0; var4 < var0.length; var4 += 9) {
         int var5 = Math.round(Math.clamp(var0[var4 + 8], 0.0F, 1.0F) * 255.0F);
         var2.vertex(var1, var0[var4], var0[var4 + 1], var0[var4 + 2])
            .color(0xFF000000 | var5 * 65793)
            .texture(var0[var4 + 3], var0[var4 + 4])
            .overlay(OverlayTexture.DEFAULT_UV)
            .light(var3)
            .normal(var1, var0[var4 + 5], var0[var4 + 6], var0[var4 + 7]);
      }
   }

   private static boolean fAsBoolean() {
      if (G == (long)BloomQuality.J) {
         return false;
      } else if (AuctionNotifier != null && H == (long)BloomQuality.J) {
         return true;
      } else {
         try {
            KAsVoid();
            return true;
         } catch (RuntimeException | IOException var1) {
            G = (long)BloomQuality.J;
            AuctionNotifier = null;
            ClientLog.lAsVoid("Emotes", "Cash fan atlas unavailable; Money Spread shows held items until resource reload", var1);
            return false;
         }
      }
   }

   private static void KAsVoid() throws IOException {
      ResourceManager var0 = MinecraftClient.getInstance().getResourceManager();

      int[] var1;
      int var2;
      int var3;
      try (InputStream var4 = var0.getResourceOrThrow(S).getInputStream()) {
         NativeImage var5 = NativeImage.read(var4);

         try {
            var2 = var5.getWidth() / 4;
            var3 = var5.getHeight() / 4;
            if (var2 * 4 != var5.getWidth() || var3 * 4 != var5.getHeight()) {
               throw new IOException("Cash atlas size " + var5.getWidth() + "x" + var5.getHeight() + " is not PetEffectRenderer multiple of 4");
            }

            var1 = jAsIntArray(var5.copyPixelsArgb(), var5.getWidth(), var5.getHeight(), 4);
         } catch (Throwable var12) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Throwable var9) {
                  var12.addSuppressed(var9);
               }
            }

            throw var12;
         }

         if (var5 != null) {
            var5.close();
         }
      }

      NativeImage var14 = new NativeImage(var2, var3, false);

      NativeImageBackedTexture var15;
      try {
         for (int var6 = 0; var6 < var1.length; var6++) {
            var14.setColorArgb(var6 % var2, var6 / var2, var1[var6]);
         }

         var15 = new NativeImageBackedTexture(() -> "LARP cash fan", var14);
      } catch (RuntimeException var11) {
         var14.close();
         throw var11;
      }

      try {
         MinecraftClient.getInstance().getTextureManager().registerTexture(b, var15);
      } catch (RuntimeException var101) {
         var15.close();
         throw var101;
      }

      AuctionNotifier = var15;
      H = (long)BloomQuality.J;
      ClientLog.GAsVoid("Emotes", "Cash fan atlas ready: {}x{}, resource generation {}", var2, var3, H);
   }

   public static int[] jAsIntArray(int[] var0, int var1, int var2, int var3) {
      int var4 = var1 / var3;
      int var5 = var2 / var3;
      int[] var6 = new int[var4 * var5];

      for (int var7 = 0; var7 < var5; var7++) {
         for (int var8 = 0; var8 < var4; var8++) {
            long var9 = 0L;
            long var11 = 0L;
            long var13 = 0L;
            long var15 = 0L;

            for (int var17 = 0; var17 < var3; var17++) {
               for (int var18 = 0; var18 < var3; var18++) {
                  int var19 = var0[(var7 * var3 + var17) * var1 + var8 * var3 + var18];
                  int var20 = var19 >>> 24;
                  var9 += (long)var20;
                  var11 += (long)((var19 >> 16 & 0xFF) * var20);
                  var13 += (long)((var19 >> 8 & 0xFF) * var20);
                  var15 += (long)((var19 & 0xFF) * var20);
               }
            }

            if (var9 != 0L) {
               int var29 = (int)Math.round((double)var9 / (double)(var3 * var3));
               var6[var7 * var4 + var8] = var29 << 24
                  | (int)Math.round((double)var11 / (double)var9) << 16
                  | (int)Math.round((double)var13 / (double)var9) << 8
                  | (int)Math.round((double)var15 / (double)var9);
            }
         }
      }

      for (int var23 = 0; var23 < 2; var23++) {
         int[] var24 = (int[])var6.clone();

         for (int var25 = 0; var25 < var5; var25++) {
            for (int var10 = 0; var10 < var4; var10++) {
               if (var6[var25 * var4 + var10] >>> 24 == 0) {
                  long var26 = 0L;
                  long var27 = 0L;
                  long var28 = 0L;
                  int var30 = 0;

                  for (int var31 = -1; var31 <= 1; var31++) {
                     for (int var32 = -1; var32 <= 1; var32++) {
                        int var33 = var10 + var32;
                        int var21 = var25 + var31;
                        if (var33 >= 0 && var21 >= 0 && var33 < var4 && var21 < var5) {
                           int var22 = var6[var21 * var4 + var33];
                           if (var22 >>> 24 >= 128) {
                              var26 += (long)(var22 >> 16 & 0xFF);
                              var27 += (long)(var22 >> 8 & 0xFF);
                              var28 += (long)(var22 & 0xFF);
                              var30++;
                           }
                        }
                     }
                  }

                  if (var30 > 0) {
                     var24[var25 * var4 + var10] = 0xFF000000 | (int)(var26 / (long)var30) << 16 | (int)(var27 / (long)var30) << 8 | (int)(var28 / (long)var30);
                  }
               }
            }
         }

         var6 = var24;
      }

      return var6;
   }

   private static final class Type_m {
      static final RenderLayer A = RenderLayer.of(
         "larp_cash_fan",
         RenderSetup.builder(RenderPipelines.ENTITY_CUTOUT)
            .texture("Sampler0", CashFanRenderer.b, () -> RenderSystem.getSamplerCache().get(FilterMode.LINEAR))
            .useLightmap()
            .useOverlay()
            .crumbling()
            .outlineMode(OutlineMode.AFFECTS_OUTLINE)
            .build()
      );

      private Type_m() {
      }
   }
}
