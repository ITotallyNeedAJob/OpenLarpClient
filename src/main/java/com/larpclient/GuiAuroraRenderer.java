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
import java.util.OptionalInt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.render.state.TexturedQuadGuiElementRenderState;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class GuiAuroraRenderer {
   private static final RenderPipeline i = RenderPipeline.builder(new Snippet[0])
      .withLocation(Identifier.of("larp-client", "gui_aurora"))
      .withVertexShader(Identifier.of("larp-client", "post/fullscreen"))
      .withFragmentShader(Identifier.of("larp-client", "gui/aurora"))
      .withUniform("AuroraParameters", UniformType.UNIFORM_BUFFER)
      .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   public static final int E = 112;
   private static final long j = System.nanoTime();
   private static SimpleFramebuffer R;
   private static GpuBuffer l;
   private static int C = -1;
   private static boolean y;
   private static long q;

   private GuiAuroraRenderer() {
   }

   public static boolean AAsBoolean(
      DrawContext var0, int var1, int var2, double var3, int var5, int var6, boolean var7, boolean var8, GuiAuroraRenderer.Type_d var9, double var10
   ) {
      if (var1 > 0 && var2 > 0 && !(var3 <= 0.0)) {
         if (C != BloomQuality.J) {
            gAsVoid();
            C = BloomQuality.J;
            y = false;
         }

         if (y) {
            return false;
         } else {
            try {
               Window var12 = MinecraftClient.getInstance().getWindow();
               double var13 = Math.min(1.0, 640.0 / (double)Math.max(1, var12.getFramebufferWidth()));
               int var15 = Math.max(1, (int)Math.round((double)var12.getFramebufferWidth() * var13));
               int var16 = Math.max(1, (int)Math.round((double)var12.getFramebufferHeight() * var13));
               if (R == null || R.textureWidth != var15 || R.textureHeight != var16) {
                  gAsVoid();
                  WorldLabelLayout.DAsVoid(i);
                  R = new SimpleFramebuffer("Larp ClickGUI aurora", var15, var16, false);
                  l = RenderSystem.getDevice().createBuffer(() -> "Larp aurora parameters", 136, 112L);
                  ClientLog.XAsVoid("GUI", "Aurora backdrop target {}x{}", var15, var16);
               }

               long var17 = System.nanoTime();
               if (q == 0L || var17 - q >= 33000000L) {
                  float var19 = var8 ? (float)((double)(var17 - j) / 1.0E9 % 20000.0) : 0.0F;
                  CommandEncoder var20 = RenderSystem.getDevice().createCommandEncoder();
                  MemoryStack var21 = MemoryStack.stackPush();

                  try {
                     Std140Builder var22 = Std140Builder.onStack(var21, 112);

                     for (Vector4f var26 : tAsVector4fArray(var15, var16, var19, var5, var6, var7, var9, var10)) {
                        var22.putVec4(var26);
                     }

                     var20.writeToBuffer(l.slice(), var22.get());
                  } catch (Throwable var30) {
                     if (var21 != null) {
                        try {
                           var21.close();
                        } catch (Throwable var28) {
                           var30.addSuppressed(var28);
                        }
                     }

                     throw var30;
                  }

                  if (var21 != null) {
                     var21.close();
                  }

                  RenderPass var32 = var20.createRenderPass(() -> "Larp ClickGUI aurora", R.getColorAttachmentView(), OptionalInt.empty());

                  try {
                     var32.setPipeline(i);
                     var32.setUniform("AuroraParameters", l.slice());
                     var32.draw(0, 3);
                  } catch (Throwable var29) {
                     if (var32 != null) {
                        try {
                           var32.close();
                        } catch (Throwable var27) {
                           var29.addSuppressed(var27);
                        }
                     }

                     throw var29;
                  }

                  if (var32 != null) {
                     var32.close();
                  }

                  q = var17;
               }

               var0.state
                  .addSimpleElement(
                     new TexturedQuadGuiElementRenderState(
                        RenderPipelines.GUI_TEXTURED,
                        TextureSetup.of(R.getColorAttachmentView(), RenderSystem.getSamplerCache().get(FilterMode.LINEAR)),
                        new Matrix3x2f(var0.getMatrices()),
                        0,
                        0,
                        var1,
                        var2,
                        0.0F,
                        1.0F,
                        1.0F,
                        0.0F,
                        ColorUtil.XAsInt(-1, var3),
                        var0.scissorStack.peekLast()
                     )
                  );
               return true;
            } catch (RuntimeException var311) {
               y = true;
               gAsVoid();
               ClientLog.lAsVoid("GUI", var9 + " backdrop unavailable; using the plain dim until resource reload", var311);
               return false;
            }
         }
      } else {
         return true;
      }
   }

   public static Vector4f[] QAsVector4fArray(int var0, int var1, float var2, int var3, int var4, boolean var5) {
      return tAsVector4fArray(var0, var1, var2, var3, var4, var5, GuiAuroraRenderer.Type_d.AURORA, 0.55);
   }

   public static Vector4f[] tAsVector4fArray(int var0, int var1, float var2, int var3, int var4, boolean var5, GuiAuroraRenderer.Type_d var6, double var7) {
      float var9 = (float)Math.clamp(var7, 0.0, 1.0) * (var5 ? 0.73F : 1.0F);
      int var10 = ColorUtil.xAsInt(var3, var4, 0.5);
      int var11 = ColorUtil.xAsInt(var3, -1, 0.35);
      float var12 = (float)Math.clamp(Math.max(kAsDouble(var3), kAsDouble(var4)) / 0.5, 0.0, 1.0);
      float var13 = var5 ? 0.12F + 0.14F * var12 : 0.14F + 0.18F * var12;
      return new Vector4f[]{
         new Vector4f((float)var0, (float)var1, var2, 0.0F),
         xAsVector4f(var3, 0.95F),
         xAsVector4f(var4, 0.85F),
         xAsVector4f(var10, 0.7F),
         xAsVector4f(var11, 0.55F),
         var5 ? new Vector4f(0.086F, 0.086F, 0.1F, var9) : new Vector4f(0.03F, 0.03F, 0.04F, var9),
         new Vector4f(var13, 0.6F, (float)var6.ordinal(), 0.0F)
      };
   }

   static double kAsDouble(int var0) {
      int var1 = var0 >> 16 & 0xFF;
      int var2 = var0 >> 8 & 0xFF;
      int var3 = var0 & 0xFF;
      return (double)(Math.max(var1, Math.max(var2, var3)) - Math.min(var1, Math.min(var2, var3))) / 255.0;
   }

   private static Vector4f xAsVector4f(int var0, float var1) {
      return new Vector4f((float)(var0 >> 16 & 0xFF) / 255.0F, (float)(var0 >> 8 & 0xFF) / 255.0F, (float)(var0 & 0xFF) / 255.0F, var1);
   }

   public static void gAsVoid() {
      if (R != null) {
         R.delete();
         R = null;
      }

      if (l != null) {
         l.close();
         l = null;
      }

      q = 0L;
   }

   public static enum Type_d {
      AURORA,
      NEBULA,
      WAVES;

      private Type_d() {
      }
   }
}
