package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public final class HornsMeshRenderer {
   static final int a = 827216456;
   static final int G = 65535;
   static final int d = 8192;
   public static final int c = -61184;
   public static final int i = -42752;
   public static final int q = -2304;
   private static HornsMeshRenderer.Type_s U;
   private static boolean X;
   private static final Identifier v = Identifier.of("larp-client", "dynamic/china_hat_horns");
   private static NativeImageBackedTexture I;
   private static int[] n;
   private static int[] j;
   private static long w = -1L;
   private static long p = -1L;
   private static int L;
   private static int h;
   private static int A;

   private HornsMeshRenderer() {
   }

   public static HornsMeshRenderer.Type_s RAsM5_Type_s() {
      if (U == null && !X) {
         try (InputStream var0 = HornsMeshRenderer.class.getResourceAsStream("/assets/larp-client/models/hats/horns.bin")) {
            if (var0 == null) {
               throw new IOException("missing resource");
            }

            U = yAsM5_Type_s(var0.readAllBytes());
            ClientLog.GAsVoid(
               "ChinaHat",
               "Horns mesh loaded; {} triangles ({} glowing), {} vertices, {} blocks tall",
               U.triangles(),
               U.glowTriangles().length,
               U.vertices(),
               String.format(Locale.ROOT, "%.3f", U.rise())
            );
         } catch (RuntimeException | IOException var51) {
            X = true;
            ClientLog.lAsVoid("ChinaHat", "Horns mesh unavailable; horns hidden until restart", var51);
         }

         return U;
      } else {
         return U;
      }
   }

   public static HornsMeshRenderer.Type_s yAsM5_Type_s(byte[] var0) {
      ByteBuffer var1 = ByteBuffer.wrap(var0).order(ByteOrder.LITTLE_ENDIAN);
      if (var0.length >= 12 && var1.getInt() == 827216456) {
         int var2 = var1.getInt();
         int var3 = var1.getInt();
         if (var2 > 0 && var2 <= 65535 && var3 > 0 && var3 <= 8192 && (long)var0.length == 12L + (long)var2 * 22L + (long)var3 * 6L) {
            float[] var4 = new float[var2 * 3];
            float[] var5 = new float[var2 * 2];
            int[] var6 = new int[var2];
            int[] var7 = new int[var2];
            boolean[] var8 = new boolean[var2];
            float var9 = 0.0F;
            float var10 = 0.0F;

            for (int var11 = 0; var11 < var2; var11++) {
               for (int var12 = 0; var12 < 5; var12++) {
                  float var13 = var1.getFloat();
                  if (!Float.isFinite(var13) || Math.abs(var13) > 2.0F) {
                     throw new IllegalArgumentException("Invalid HRN1 vertex");
                  }

                  if (var12 < 3) {
                     var4[var11 * 3 + var12] = var13;
                  } else {
                     var5[var11 * 2 + var12 - 3] = var13;
                  }
               }

               float var18 = (float)(var1.get() & 255) / 255.0F;
               float var20 = (float)(var1.get() & 255) / 255.0F;
               var6[var11] = rAsInt(var18 + (1.0F - var18) * var20);
               var7[var11] = rAsInt((float)Math.pow((double)var20, 1.2) * 0.9F);
               var8[var11] = var20 > 0.0F;
               var9 = Math.max(var9, var4[var11 * 3 + 1]);
               var10 += var4[var11 * 3 + 2];
            }

            int[] var17 = new int[var3 * 3];
            int var19 = 0;

            for (int var21 = 0; var21 < var3; var21++) {
               byte var14 = 0;

               for (int var15 = 0; var15 < 3; var15++) {
                  int var16 = var1.getShort() & '\uffff';
                  if (var16 >= var2) {
                     throw new IllegalArgumentException("Invalid HRN1 index");
                  }

                  var17[var21 * 3 + var15] = var16;
                  var14 |= (byte)(var8[var16] ? 1 : 0);
               }

               if (var14 != 0) {
                  var19++;
               }
            }

            if (var19 == 0) {
               throw new IllegalArgumentException("HRN1 mesh has no glowing tips");
            } else {
               int[] var22 = new int[var19];
               int var23 = 0;

               for (int var24 = 0; var23 < var3; var23++) {
                  if (var8[var17[var23 * 3]] || var8[var17[var23 * 3 + 1]] || var8[var17[var23 * 3 + 2]]) {
                     var22[var24++] = var23;
                  }
               }

               return new HornsMeshRenderer.Type_s(var4, var5, var6, var7, var17, var22, var9, var10 / (float)var2);
            }
         } else {
            throw new IllegalArgumentException("Invalid HRN1 counts " + var2 + "/" + var3);
         }
      } else {
         throw new IllegalArgumentException("Not an HRN1 mesh");
      }
   }

   public static double XAsDouble(double var0) {
      HornsMeshRenderer.Type_s var2 = RAsM5_Type_s();
      return var2 == null ? 0.0 : (double)var2.rise() * var0;
   }

   static float tAsFloat(float var0, float var1, float var2) {
      float var3 = Math.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (3.0F - 2.0F * var3);
   }

   public static int lAsInt(int var0) {
      return ColorUtil.xAsInt(var0, -1, 0.45);
   }

   public static int ZAsInt(int var0, int var1, int var2, int var3) {
      float var4 = (float)(var0 >> 16 & 0xFF) / 255.0F;
      float var5 = (float)(var0 >> 8 & 0xFF) / 255.0F;
      float var6 = (float)(var0 & 0xFF) / 255.0F;
      float var7 = Math.max(var4, Math.max(var5, var6));
      float var8 = Math.min(var4, Math.min(var5, var6));
      if (var7 <= 0.0F) {
         return var0;
      } else {
         float var9 = (var7 - var8) / var7;
         float var10 = tAsFloat(0.34F, 0.55F, var9);
         if (var10 <= 0.0F) {
            return var0;
         } else {
            float var11 = Math.max(var7 - var8, 1.0E-6F);
            float var12 = var7 == var4
               ? ((var5 - var6) / var11 % 6.0F + 6.0F) % 6.0F
               : (var7 == var5 ? (var6 - var4) / var11 + 2.0F : (var4 - var5) / var11 + 4.0F);
            var12 *= 60.0F;
            if (var12 > 300.0F) {
               var12 -= 360.0F;
            }

            float var13 = tAsFloat(9.0F, 20.0F, var12);
            float var14 = tAsFloat(18.0F, 60.0F, var12);
            int var15 = var0 & 0xFF000000;

            for (int var16 = 16; var16 >= 0; var16 -= 8) {
               float var17 = (float)(var0 >> var16 & 0xFF) / 255.0F;
               float var18 = (float)(var1 >> var16 & 0xFF) / 255.0F;
               float var19 = ((float)(var2 >> var16 & 0xFF) + (float)((var3 >> var16 & 0xFF) - (var2 >> var16 & 0xFF)) * var14) / 255.0F;
               float var20 = var18 + (var19 - var18) * var13;
               float var21 = var7 * (1.0F - var9 + var9 * var20);
               int var22 = Math.round(Math.clamp(var17 + (var21 - var17) * var10, 0.0F, 1.0F) * 255.0F);
               var15 |= var22 << var16;
            }

            return var15;
         }
      }
   }

   public static void WAsVoid(int[] var0, int[] var1, int var2, int var3, int var4) {
      for (int var5 = 0; var5 < var0.length; var5++) {
         var1[var5] = ZAsInt(var0[var5], var2, var3, var4);
      }
   }

   public static boolean XAsBoolean(int var0, int var1, int var2) {
      if (p == (long)BloomQuality.J) {
         return false;
      } else {
         try {
            if (RAsM5_Type_s() == null) {
               return false;
            } else {
               if (I == null || w != (long)BloomQuality.J) {
                  BAsVoid();
                  L = ~var0;
               }

               if (var0 != L || var1 != h || var2 != A) {
                  WAsVoid(n, j, var0, var1, var2);
                  NativeImage var3 = I.getImage();
                  int var4 = var3.getWidth();

                  for (int var5 = 0; var5 < j.length; var5++) {
                     var3.setColorArgb(var5 % var4, var5 / var4, j[var5]);
                  }

                  I.upload();
                  L = var0;
                  h = var1;
                  A = var2;
                  ClientLog.XAsVoid(
                     "ChinaHat",
                     "Horns recolored: horn #{}, fire #{}, core #{}",
                     Integer.toHexString(var0),
                     Integer.toHexString(var1),
                     Integer.toHexString(var2)
                  );
               }

               return true;
            }
         } catch (RuntimeException | IOException var61) {
            p = (long)BloomQuality.J;
            I = null;
            ClientLog.lAsVoid("ChinaHat", "Horns texture unavailable; horns hidden until resource reload", var61);
            return false;
         }
      }
   }

   private static void BAsVoid() throws IOException {
      try (InputStream var0 = HornsMeshRenderer.class.getResourceAsStream("/assets/larp-client/models/hats/horns.png")) {
         if (var0 == null) {
            throw new IOException("missing horns.png");
         }

         NativeImage var1 = NativeImage.read(var0);

         NativeImageBackedTexture var2;
         try {
            n = var1.copyPixelsArgb();
            j = new int[n.length];
            var2 = new NativeImageBackedTexture(() -> "LARP China Hat horns", var1);
         } catch (RuntimeException var6) {
            var1.close();
            throw var6;
         }

         try {
            MinecraftClient.getInstance().getTextureManager().registerTexture(v, var2);
         } catch (RuntimeException var5) {
            var2.close();
            throw var5;
         }

         WorldLabelLayout.DAsVoid(HornsMeshRenderer.Type_i.C);
         I = var2;
         w = (long)BloomQuality.J;
      }
   }

   public static RenderLayer oAsRenderLayer() {
      return HornsMeshRenderer.Type_i.J;
   }

   public static void sAsVoid(VertexConsumer var0, Matrix4f var1, HornsMeshRenderer.Type_s var2, double var3) {
      float var5 = (float)var3;
      float[] var6 = var2.points;
      float[] var7 = var2.uvs;

      for (int var11 : var2.indices) {
         var0.vertex(var1, var6[var11 * 3] * var5, -0.5F - var6[var11 * 3 + 1] * var5, var6[var11 * 3 + 2] * var5)
            .texture(var7[var11 * 2], var7[var11 * 2 + 1])
            .color(var2.colors[var11]);
      }

      OpenScreen.FAsVoid("China Hat horns triangles", (long)var2.triangles());
   }

   public static void vAsVoid(VertexConsumer var0, Matrix4f var1, HornsMeshRenderer.Type_s var2, double var3) {
      float var5 = (float)var3;
      float[] var6 = var2.points;
      float[] var7 = var2.uvs;

      for (int var11 : var2.glowTriangles) {
         for (int var12 = 0; var12 < 3; var12++) {
            int var13 = var2.indices[var11 * 3 + var12];
            var0.vertex(var1, var6[var13 * 3] * var5, -0.5F - var6[var13 * 3 + 1] * var5, var6[var13 * 3 + 2] * var5)
               .texture(var7[var13 * 2], var7[var13 * 2 + 1])
               .color(var2.glow[var13]);
         }
      }
   }

   static int rAsInt(float var0) {
      int var1 = Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F);
      return 0xFF000000 | var1 << 16 | var1 << 8 | var1;
   }

   private static final class Type_i {
      static final RenderPipeline C = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_TEX_COLOR_SNIPPET})
         .withLocation(Identifier.of("larp-client", "china_hat_horns"))
         .withFragmentShader(Identifier.of("larp-client", "particle/sprite"))
         .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, DrawMode.TRIANGLES)
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(true)
         .withoutBlend()
         .withCull(true)
         .build();
      static final RenderLayer J = RenderLayer.of("larp_china_hat_horns", RenderSetup.builder(C).texture("Sampler0", HornsMeshRenderer.v).build());

      private Type_i() {
      }
   }

   public static record Type_s(float[] points, float[] uvs, int[] colors, int[] glow, int[] indices, int[] glowTriangles, float rise, float centerZ) {
      public int vertices() {
         return this.colors.length;
      }

      public int triangles() {
         return this.indices.length / 3;
      }
   }
}
