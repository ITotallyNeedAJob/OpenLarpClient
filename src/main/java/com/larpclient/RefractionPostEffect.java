package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.OptionalInt;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.lwjgl.system.MemoryStack;

public final class RefractionPostEffect {
   private static final BloomFramebuffers O = new BloomFramebuffers(BloomFramebuffers.Type_d.RGBA16_FLOAT);
   private static final Set<String> F = new HashSet<>();
   private static final RenderPipeline B = RenderPipeline.builder(new Snippet[0])
      .withLocation(oAsIdentifier("localized_refraction"))
      .withVertexShader(oAsIdentifier("post/fullscreen"))
      .withFragmentShader(oAsIdentifier("post/refraction"))
      .withSampler("Source")
      .withSampler("Mask")
      .withSampler("SceneDepth")
      .withUniform("Refraction", UniformType.UNIFORM_BUFFER)
      .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   private static GpuBuffer M;
   private static int V = -1;
   private static boolean j;
   private static boolean N;
   private static int d;

   public static void HAsVoid() {
      if (V != BloomQuality.J) {
         V = BloomQuality.J;
         F.clear();
         N = false;

         try {
            EAsVoid();
         } catch (RuntimeException var1) {
            N = true;
            ClientLog.lAsVoid("Refraction", "Generation cleanup failed; base materials retained until resource reload", var1);
         }
      }

      j = false;
      d = 0;
   }

   public static boolean HAsBoolean(String var0, Runnable var1, double var2) {
      if (!N && d < 2 && x9.kAsBoolean() && !(var2 <= 0.0) && !F.contains(var0)) {
         GpuTextureView var4 = RenderSystem.outputColorTextureOverride;
         GpuTextureView var5 = RenderSystem.outputDepthTextureOverride;
         long var6 = op.RAsLong();

         try {
            GpuTextureView var8 = SceneRenderResources.gAsGpuTextureView();
            GpuTextureView var9 = SceneRenderResources.yAsGpuTextureView();
            if (var8 == null || var9 == null) {
               return false;
            }

            if (M == null) {
               WorldLabelLayout.DAsVoid(B);
               M = RenderSystem.getDevice().createBuffer(() -> "Larp refraction parameters", 136, 16L);
               ClientLog.GAsVoid("Refraction", "Localized depth-aware pipeline ready; shared scene samples and mask");
            }

            Framebuffer var10 = MinecraftClient.getInstance().getFramebuffer();
            O.FAsVoid(var10.textureWidth, var10.textureHeight, 1, BloomFramebuffers.Type_b.LOCAL_MASK);
            j = true;
            CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();
            var11.clearColorTexture(O.u.getColorAttachment(), 0);
            RenderSystem.outputColorTextureOverride = O.u.getColorAttachmentView();
            RenderSystem.outputDepthTextureOverride = var9;
            var1.run();
            RenderSystem.outputColorTextureOverride = var4;
            RenderSystem.outputDepthTextureOverride = var5;
            MemoryStack var12 = MemoryStack.stackPush();

            try {
               ByteBuffer var13 = var12.malloc(16);
               var13.putFloat((float)Math.clamp(var2, 0.0, 24.0) / (float)var10.textureWidth)
                  .putFloat((float)Math.clamp(var2, 0.0, 24.0) / (float)var10.textureHeight)
                  .putFloat(0.0F)
                  .putFloat(0.0F)
                  .flip();
               var11.writeToBuffer(M.slice(), var13);
            } catch (Throwable var261) {
               if (var12 != null) {
                  try {
                     var12.close();
                  } catch (Throwable var241) {
                     var261.addSuppressed(var241);
                  }
               }

               throw var261;
            }

            if (var12 != null) {
               var12.close();
            }

            RenderPass var28 = var11.createRenderPass(() -> "Larp localized refraction " + var0, var10.getColorAttachmentView(), OptionalInt.empty());

            try {
               var28.setPipeline(B);
               var28.setUniform("Refraction", M.slice());
               GpuSampler var29 = RenderSystem.getSamplerCache().get(FilterMode.LINEAR);
               var28.bindTexture("Source", var8, var29);
               var28.bindTexture("Mask", O.u.getColorAttachmentView(), var29);
               var28.bindTexture("SceneDepth", var9, RenderSystem.getSamplerCache().get(FilterMode.NEAREST));
               var28.draw(0, 3);
            } catch (Throwable var25) {
               if (var28 != null) {
                  try {
                     var28.close();
                  } catch (Throwable var23) {
                     var25.addSuppressed(var23);
                  }
               }

               throw var25;
            }

            if (var28 != null) {
               var28.close();
            }

            d++;
            SceneRenderResources.cAsVoid();
            op.FAsVoid("Localized refraction composites", 1L);
         } catch (RuntimeException var27) {
            F.add(var0);
            ClientLog.lAsVoid("Refraction", "Owner=" + var0 + "; mask/composite failed, base material retained until resource reload", var27);
            return false;
         } finally {
            RenderSystem.outputColorTextureOverride = var4;
            RenderSystem.outputDepthTextureOverride = var5;
            op.aAsVoid(var0 + " refraction CPU", var6);
         }

         return true;
      } else {
         return false;
      }
   }

   public static void aAsVoid() {
      if (!j) {
         EAsVoid();
      }

      op.YAsVoid("Refraction mask bytes (estimate)", O.hAsLong());
      op.YAsVoid("Refraction quarantined owners", (long)F.size());
   }

   public static void EAsVoid() {
      try {
         O.close();
      } finally {
         GpuBuffer var2 = M;
         M = null;
         if (var2 != null) {
            var2.close();
         }
      }
   }

   private static Identifier oAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }

   private RefractionPostEffect() {
   }
}
