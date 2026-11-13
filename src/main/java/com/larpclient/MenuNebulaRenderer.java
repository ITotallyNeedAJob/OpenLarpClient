package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Random;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.render.state.TexturedQuadGuiElementRenderState;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.client.util.Window;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class MenuNebulaRenderer {
   private static final RenderPipeline X = RenderPipeline.builder(new Snippet[0])
      .withLocation(Identifier.of("larp-client", "menu_nebula"))
      .withVertexShader(Identifier.of("larp-client", "post/fullscreen"))
      .withFragmentShader(Identifier.of("larp-client", "menu/nebula"))
      .withUniform("MenuParameters", UniformType.UNIFORM_BUFFER)
      .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   private static final long t = System.nanoTime();
   private static final double[][] S = new double[140][4];
   private static SimpleFramebuffer l;
   private static GpuBuffer w;
   private static int g = -1;
   private static boolean E;
   private static long G;
   private static float i;
   private static float n;
   private static final Map<b4, Boolean> u;
   private static int P;
   private static b4 J;
   private static b4 z;
   private static final w2 V;
   private static final rp r;

   private MenuNebulaRenderer() {
   }

   public static void MAsVoid(DrawContext var0, int var1, int var2, boolean var3) {
      iAsVoid(var0, var1, var2, var3 ? 0.8 : 1.0, true);
   }

   private static void iAsVoid(DrawContext var0, int var1, int var2, double var3, boolean var5) {
      if (var1 > 0 && var2 > 0) {
         MinecraftClient var6 = MinecraftClient.getInstance();
         double var7 = var5 ? (double)(System.nanoTime() - t) / 1.0E9 : 0.0;
         if (g != BloomQuality.J) {
            yAsVoid();
            g = BloomQuality.J;
            E = false;
         }

         byte var9 = 0;
         if (!E) {
            try {
               Window var10 = var6.getWindow();
               int var11 = var10.getFramebufferWidth();
               int var12 = var10.getFramebufferHeight();
               double var13 = Math.min(1.0, 900.0 / (double)Math.max(1, var11));
               int var15 = Math.max(1, (int)Math.round((double)var11 * var13));
               int var16 = Math.max(1, (int)Math.round((double)var12 * var13));
               if (l == null || l.textureWidth != var15 || l.textureHeight != var16) {
                  yAsVoid();
                  WorldLabelLayout.DAsVoid(X);
                  l = new SimpleFramebuffer("Larp website nebula", var15, var16, false);
                  w = RenderSystem.getDevice().createBuffer(() -> "Larp menu parameters", 136, 32L);
               }

               long var17 = System.nanoTime();
               if (G == 0L || var17 - G >= 33000000L) {
                  float var19 = var5 ? (float)(var6.mouse.getX() / (double)Math.max(1, var10.getWidth()) - 0.5) : 0.0F;
                  float var20 = var5 ? (float)(0.5 - var6.mouse.getY() / (double)Math.max(1, var10.getHeight())) : 0.0F;
                  if (var5) {
                     i = i + (var19 - i) * 0.025F;
                     n = n + (var20 - n) * 0.025F;
                  } else {
                     n = 0.0F;
                     i = 0.0F;
                  }

                  CommandEncoder var21 = RenderSystem.getDevice().createCommandEncoder();
                  MemoryStack var22 = MemoryStack.stackPush();

                  try {
                     ByteBuffer var23 = Std140Builder.onStack(var22, 32)
                        .putVec4(new Vector4f((float)var15, (float)var16, (float)var7, 0.0F))
                        .putVec4(new Vector4f(i, n, 0.0F, 0.0F))
                        .get();
                     var21.writeToBuffer(w.slice(), var23);
                  } catch (Throwable var29) {
                     if (var22 != null) {
                        try {
                           var22.close();
                        } catch (Throwable var27) {
                           var29.addSuppressed(var27);
                        }
                     }

                     throw var29;
                  }

                  if (var22 != null) {
                     var22.close();
                  }

                  RenderPass var35 = var21.createRenderPass(() -> "Larp menu nebula", l.getColorAttachmentView(), OptionalInt.empty());

                  try {
                     var35.setPipeline(X);
                     var35.setUniform("MenuParameters", w.slice());
                     var35.draw(0, 3);
                  } catch (Throwable var28) {
                     if (var35 != null) {
                        try {
                           var35.close();
                        } catch (Throwable var26) {
                           var28.addSuppressed(var26);
                        }
                     }

                     throw var28;
                  }

                  if (var35 != null) {
                     var35.close();
                  }

                  G = var17;
               }

               var0.state
                  .addSimpleElement(
                     new TexturedQuadGuiElementRenderState(
                        RenderPipelines.GUI_TEXTURED,
                        TextureSetup.of(l.getColorAttachmentView(), RenderSystem.getSamplerCache().get(FilterMode.LINEAR)),
                        new Matrix3x2f(var0.getMatrices()),
                        0,
                        0,
                        var1,
                        var2,
                        0.0F,
                        1.0F,
                        1.0F,
                        0.0F,
                        bg.XAsInt(-1, var3),
                        null
                     )
                  );
               var9 = 1;
            } catch (RuntimeException var301) {
               E = true;
               yAsVoid();
               ClientLog.lAsVoid("Menu", "Nebula unavailable; using dark blue fallback until resource reload", var301);
            }
         }

         if (var9 == 0) {
            GuiTextureDrawing.mAsVoid(var0, 0, 0, var1, var2, bg.XAsInt(-16116958, var3), bg.XAsInt(-16118252, var3));
         }

         double var30 = (double)Math.max(1, var6.getWindow().getScaleFactor());
         int var31 = Math.min(140, Math.max(1, (int)Math.round((double)(var1 * var2) * var30 * var30 / 10000.0)));

         for (int var32 = 0; var32 < var31; var32++) {
            double[] var14 = S[var32];
            double var33 = (0.2 + (1.0 + Math.sin(var7 * 0.6 + var14[3])) * 0.18) * 0.26;
            int var34 = Math.max(1, (int)Math.round(var14[2] * 2.0 / var30));
            GuiTextureDrawing.CAsVoid(var0, (int)(var14[0] * (double)var1), (int)(var14[1] * (double)var2), var34, var34, bg.XAsInt(-4201985, var33 * var3));
         }
      }
   }

   public static double KAsDouble() {
      return r.VAsDouble();
   }

   public static double yAsDouble() {
      return r.mAsDouble();
   }

   public static void eAsVoid(DrawContext var0, int var1, int var2, b4 var3, boolean var4, boolean var5) {
      if (!(MinecraftClient.getInstance().currentScreen instanceof MenuAtmospherePanel)) {
         MenuLogoAssets.gAsVoid(var0, var1, var2);
         if (var4) {
            GuiTextureDrawing.CAsVoid(var0, 0, 0, var1, var2, -2012607472);
         }
      } else {
         MinecraftClient var6 = MinecraftClient.getInstance();
         Window var7 = var6.getWindow();
         r.SAsVoid(
            var6.mouse.getX() / (double)Math.max(1, var7.getWidth()) * 2.0 - 1.0,
            var6.mouse.getY() / (double)Math.max(1, var7.getHeight()) * 2.0 - 1.0,
            System.nanoTime(),
            var5
         );
         if (J != var3) {
            z = J;
            J = var3;
            V.aAsVoid(z != null && var5 ? 0.0 : 1.0);
         }

         double var8 = var5 ? V.bAsDouble(1.0, 260) : 1.0;
         if (var8 < 0.999 && z != null) {
            MAsVoid(var0, z, 0, 0, var1, var2, 1.0, var5);
         }

         MAsVoid(var0, var3, 0, 0, var1, var2, var8, var5);
         if (var8 >= 0.999) {
            z = null;
         }

         if (var4) {
            GuiTextureDrawing.CAsVoid(var0, 0, 0, var1, var2, -2012607472);
         }
      }
   }

   public static void MAsVoid(DrawContext var0, b4 var1, int var2, int var3, int var4, int var5, double var6, boolean var8) {
      if (var4 > 0 && var5 > 0 && !(var6 <= 0.0)) {
         if (var1 == b4.NEBULA) {
            var0.getMatrices().pushMatrix();

            try {
               var0.getMatrices().translate((float)var2, (float)var3);
               if (var8) {
                  var0.getMatrices().translate((float)((double)var4 * 0.0225 * (r.VAsDouble() - 1.0)), (float)((double)var5 * 0.0225 * (r.mAsDouble() - 1.0)));
                  var0.getMatrices().scale(1.045F, 1.045F);
               }

               iAsVoid(var0, var4, var5, var6, LarpClient.CAsLarpClient().fAsBo().yAsN7().Z);
            } finally {
               var0.getMatrices().popMatrix();
            }
         } else {
            if (P != BloomQuality.J) {
               P = BloomQuality.J;
               u.clear();
            }

            Identifier var9 = Identifier.of("larp-client", "textures/menu/" + var1.x + ".png");
            boolean var10 = u.computeIfAbsent(var1, var2x -> {
               try {
                  Resource var3x = (Resource)MinecraftClient.getInstance().getResourceManager().getResource(var9).orElseThrow();

                  try (InputStream var4x = var3x.getInputStream()) {
                     NativeImage var5x = NativeImage.read(var4x);

                     try {
                        if (var5x.getWidth() != 1672 || var5x.getHeight() != 941) {
                           throw new IOException("Wallpaper dimensions changed");
                        }
                     } catch (Throwable var10x) {
                        if (var5x != null) {
                           try {
                              var5x.close();
                           } catch (Throwable var9x) {
                              var10x.addSuppressed(var9x);
                           }
                        }

                        throw var10x;
                     }

                     if (var5x != null) {
                        var5x.close();
                     }
                  }

                  return true;
               } catch (RuntimeException | IOException var121) {
                  ClientLog.oAsVoid("Menu", "Missing/invalid {} artwork; using nebula fallback: {}", var1.O, var121.toString());
                  return false;
               }
            });
            if (!var10) {
               MAsVoid(var0, b4.NEBULA, var2, var3, var4, var5, var6, var8);
               return;
            } else {
               try {
                  ArtworkDimensions var11 = ArtworkDimensions.cover(var4, var5, var8 ? 1.045 : 1.0, var8 ? r.VAsDouble() : 0.0, var8 ? r.mAsDouble() : 0.0);
                  var0.drawTexture(
                     RenderPipelines.GUI_TEXTURED,
                     var9,
                     var2,
                     var3,
                     var11.u(),
                     var11.v(),
                     var4,
                     var5,
                     var11.width(),
                     var11.height(),
                     1672,
                     941,
                     bg.XAsInt(-1, var6)
                  );
               } catch (RuntimeException var141) {
                  u.put(var1, false);
                  ClientLog.lAsVoid("Menu", "Wallpaper unavailable; using nebula fallback until reload", var141);
                  MAsVoid(var0, b4.NEBULA, var2, var3, var4, var5, var6, var8);
               }

               return;
            }
         }
      }
   }

   public static void yAsVoid() {
      MenuLogoAssets.ZAsVoid();
      if (l != null) {
         l.delete();
         l = null;
      }

      if (w != null) {
         w.close();
         w = null;
      }

      G = 0L;
   }

   static {
      Random var0 = new Random(10842L);

      for (double[] var4 : S) {
         var4[0] = var0.nextDouble();
         var4[1] = var0.nextDouble();
         var4[2] = 0.3 + var0.nextDouble() * 0.8;
         var4[3] = var0.nextDouble() * Math.PI * 2.0;
      }

      u = new EnumMap<>(b4.class);
      P = -1;
      V = new w2(1.0);
      r = new rp();
   }
}
