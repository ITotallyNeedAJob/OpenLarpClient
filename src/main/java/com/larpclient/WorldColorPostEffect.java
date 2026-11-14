package com.larpclient;

import com.larpclient.module.WorldColor;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.nio.ByteBuffer;
import java.util.OptionalInt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.lwjgl.system.MemoryStack;

public final class WorldColorPostEffect {
   private static final RenderPipeline k = RenderPipeline.builder(new Snippet[0])
      .withLocation(ZAsIdentifier("world_color"))
      .withVertexShader(ZAsIdentifier("post/fullscreen"))
      .withFragmentShader(ZAsIdentifier("post/world_color"))
      .withSampler("Source")
      .withUniform("Grade", UniformType.UNIFORM_BUFFER)
      .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   private static final BloomFramebuffers v = new BloomFramebuffers();
   private static GpuBuffer d;
   private static double I = -100.0;
   private static int q;
   private static int u = -1;
   private static boolean K;

   public WorldColorPostEffect() {
   }

   public static void CAsVoid(int var0) {
      I = MediaSession.RAsDouble();
      q = var0;
   }

   public static void AAsVoid() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      WorldColor var1 = WorldColor.FAsWorldColor();
      if (var0.world != null) {
         double var2 = Math.max(0.0, 1.0 - (MediaSession.RAsDouble() - I) / 0.24);
         var2 *= var2;
         int var4 = var1 != null && var1.isRuntimeActive() ? 1 : 0;
         if (var4 != 0 || var2 != 0.0) {
            if (u != BloomQuality.J) {
               YAsVoid();
               K = false;
               u = BloomQuality.J;
            }

            if (!K) {
               long var5 = OpenScreen.RAsLong();

               try {
                  if (d == null) {
                     WorldLabelLayout.DAsVoid(k);
                     d = RenderSystem.getDevice().createBuffer(() -> "Larp WorldColor parameters", 136, 96L);
                     ClientLog.GAsVoid("Shader", "WorldColor shader loaded");
                  }

                  Framebuffer var7 = var0.getFramebuffer();
                  v.FAsVoid(var7.textureWidth, var7.textureHeight, 1, BloomFramebuffers.Type_b.COLOR);
                  BloomQuality.aAsVoid(WorldLabelLayout.u, var7, v.f);
                  CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
                  MemoryStack var9 = MemoryStack.stackPush();

                  try {
                     ByteBuffer var10 = var9.malloc(96);
                     var10.putFloat(var4 != 0 ? var1.saturation.getValue().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.contrast.getValue().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.exposure.getValue().floatValue() : 0.0F)
                        .putFloat(var4 != 0 ? var1.gamma.getValue().floatValue() : 1.0F);
                     NAsVoid(var10, var4 != 0 ? var1.tint.getValue() : -1, var4 != 0 ? var1.vignette.getValue().floatValue() : 0.0F);
                     NAsVoid(var10, var4 != 0 ? var1.shadowTint.getValue() : -1, 0.0F);
                     NAsVoid(var10, var4 != 0 ? var1.highlightTint.getValue() : -1, 0.0F);
                     NAsVoid(var10, q, (float)var2);
                     var10.putFloat(1.0F / (float)var7.textureWidth).putFloat(1.0F / (float)var7.textureHeight).putFloat(0.0F).putFloat(0.0F);
                     var10.flip();
                     var8.writeToBuffer(d.slice(), var10);
                  } catch (Throwable var231) {
                     if (var9 != null) {
                        try {
                           var9.close();
                        } catch (Throwable var21) {
                           var231.addSuppressed(var21);
                        }
                     }

                     throw var231;
                  }

                  if (var9 != null) {
                     var9.close();
                  }

                  RenderPass var26 = var8.createRenderPass(() -> "Larp WorldColor grade + impact", var7.getColorAttachmentView(), OptionalInt.empty());

                  try {
                     var26.setPipeline(k);
                     var26.setUniform("Grade", d.slice());
                     var26.bindTexture("Source", v.f.getColorAttachmentView(), RenderSystem.getSamplerCache().get(FilterMode.LINEAR));
                     var26.draw(0, 3);
                  } catch (Throwable var22) {
                     if (var26 != null) {
                        try {
                           var26.close();
                        } catch (Throwable var20) {
                           var22.addSuppressed(var20);
                        }
                     }

                     throw var22;
                  }

                  if (var26 != null) {
                     var26.close();
                  }
               } catch (RuntimeException var24) {
                  K = true;
                  YAsVoid();
                  ClientLog.lAsVoid("WorldColor", "WorldPostRenderer stage=grade framebuffer=scene shader=post/world_color; disabled until reload", var24);
               } finally {
                  OpenScreen.aAsVoid("WorldColor", var5);
               }
            }
         }
      }
   }

   private static void NAsVoid(ByteBuffer var0, int var1, float var2) {
      var0.putFloat((float)(var1 >> 16 & 0xFF) / 255.0F).putFloat((float)(var1 >> 8 & 0xFF) / 255.0F).putFloat((float)(var1 & 0xFF) / 255.0F).putFloat(var2);
   }

   public static void YAsVoid() {
      v.close();
      if (d != null) {
         d.close();
         d = null;
      }
   }

   public static void fAsVoid() {
      I = -100.0;
      YAsVoid();
   }

   private static Identifier ZAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }
}
