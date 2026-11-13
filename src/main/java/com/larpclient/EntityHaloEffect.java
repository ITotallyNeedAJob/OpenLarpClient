package com.larpclient;

import com.larpclient.module.SelfVisuals;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
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
import java.util.OptionalInt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.system.MemoryStack;

public final class EntityHaloEffect {
   private static final BloomFramebuffers F = new BloomFramebuffers();
   private static final RenderPipeline B = RenderPipeline.builder(new Snippet[0])
      .withLocation(YAsIdentifier("entity_halo"))
      .withVertexShader(YAsIdentifier("post/fullscreen"))
      .withFragmentShader(YAsIdentifier("post/entity_halo"))
      .withSampler("Source")
      .withSampler("Mask")
      .withUniform("Halo", UniformType.UNIFORM_BUFFER)
      .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withBlend(BlendFunction.TRANSLUCENT)
      .withCull(false)
      .build();
   private static EffectDrawBuffers J;
   private static GpuBuffer P;
   private static int U = -1;
   private static boolean z;

   public EntityHaloEffect() {
   }

   public static void tAsVoid(MatrixStack var0, EntityRenderState var1) {
      SelfVisuals var2 = SelfVisuals.zAsSelfVisuals();
      int var3 = var2 != null && var2.tAsBoolean() && var2.MAsBoolean() && var2.outline.getValue() && var1 != null ? 1 : 0;
      if (var3 != 0) {
         if (U != BloomQuality.J) {
            aAsVoid();
            z = false;
            U = BloomQuality.J;
         }

         if (!z) {
            GpuTextureView var4 = RenderSystem.outputColorTextureOverride;
            GpuTextureView var5 = RenderSystem.outputDepthTextureOverride;

            try {
               if (J == null) {
                  J = new EffectDrawBuffers();
               }

               if (P == null) {
                  WorldLabelLayout.DAsVoid(B);
                  P = RenderSystem.getDevice().createBuffer(() -> "Larp entity halo parameters", 136, 32L);
                  ClientLog.GAsVoid("SelfVisuals", "Outline pass initialized; full-resolution silhouette edges");
               }

               MinecraftClient var6 = MinecraftClient.getInstance();
               Framebuffer var7 = var6.getFramebuffer();
               F.FAsVoid(
                  var7.textureWidth,
                  var7.textureHeight,
                  2,
                  var2.outlineBloom.getValue() ? BloomFramebuffers.Type_b.MASK_AND_BLUR : BloomFramebuffers.Type_b.MASK
               );
               if (var3 != 0) {
                  long var8 = op.RAsLong();
                  WAsVoid();
                  CosmeticSubmissionHandler.AAsVoid(var1, var0, J, CosmeticSubmissionHandler.Type_n.MASK, -1, 1.0, true);
                  J.CAsVoid();
                  fAsVoid(var4, var5);
                  op.aAsVoid("SelfVisuals outline mask", var8);
                  var8 = op.RAsLong();
                  pAsVoid(
                     var2.outlineColor.getValue(),
                     var2.outlineThickness.getValue().floatValue(),
                     var2.outlineOpacity.getValue().floatValue(),
                     var2.outlineBloom.getValue() ? 0.2F : 0.0F
                  );
                  op.aAsVoid("SelfVisuals outline composite", var8);
               }
            } catch (RuntimeException var131) {
               z = true;
               ClientLog.lAsVoid(
                  "SelfVisuals", "EntityMaskRenderer stage=mask/composite framebuffer=entity mask shader=post/entity_halo; disabled until reload", var131
               );
               aAsVoid();
            } finally {
               fAsVoid(var4, var5);
               if (J != null) {
                  J.hAsVoid();
               }
            }
         }
      }
   }

   private static void fAsVoid(GpuTextureView var0, GpuTextureView var1) {
      RenderSystem.outputColorTextureOverride = var0;
      RenderSystem.outputDepthTextureOverride = var1;
   }

   private static void WAsVoid() {
      CommandEncoder var0 = RenderSystem.getDevice().createCommandEncoder();
      var0.clearColorTexture(F.u.getColorAttachment(), 0);
      F.u.copyDepthFrom(MinecraftClient.getInstance().getFramebuffer());
      RenderSystem.outputColorTextureOverride = F.u.getColorAttachmentView();
      RenderSystem.outputDepthTextureOverride = F.u.getDepthAttachmentView();
   }

   private static void pAsVoid(int var0, float var1, float var2, float var3) {
      if (var3 > 0.0F) {
         BloomQuality.aAsVoid(WorldLabelLayout.c, F.u, F.f);
         BloomQuality.aAsVoid(WorldLabelLayout.B, F.f, F.y);
      }

      Framebuffer var4 = MinecraftClient.getInstance().getFramebuffer();
      CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var6 = MemoryStack.stackPush();

      try {
         ByteBuffer var7 = var6.malloc(32);
         var7.putFloat((float)(var0 >> 16 & 0xFF) / 255.0F).putFloat((float)(var0 >> 8 & 0xFF) / 255.0F).putFloat((float)(var0 & 0xFF) / 255.0F).putFloat(var2);
         var7.putFloat(var1).putFloat(var3).putFloat(1.0F / (float)var4.textureWidth).putFloat(1.0F / (float)var4.textureHeight);
         var7.flip();
         var5.writeToBuffer(P.slice(), var7);
      } catch (Throwable var131) {
         if (var6 != null) {
            try {
               var6.close();
            } catch (Throwable var111) {
               var131.addSuppressed(var111);
            }
         }

         throw var131;
      }

      if (var6 != null) {
         var6.close();
      }

      RenderPass var13 = var5.createRenderPass(() -> "Larp entity outline and halo", var4.getColorAttachmentView(), OptionalInt.empty());

      try {
         var13.setPipeline(B);
         var13.setUniform("Halo", P.slice());
         GpuSampler var14 = RenderSystem.getSamplerCache().get(FilterMode.LINEAR);
         var13.bindTexture("Source", (var3 > 0.0F ? F.y : F.u).getColorAttachmentView(), var14);
         var13.bindTexture("Mask", F.u.getColorAttachmentView(), var14);
         var13.draw(0, 3);
      } catch (Throwable var12) {
         if (var13 != null) {
            try {
               var13.close();
            } catch (Throwable var10) {
               var12.addSuppressed(var10);
            }
         }

         throw var12;
      }

      if (var13 != null) {
         var13.close();
      }
   }

   public static void aAsVoid() {
      F.close();
      if (P != null) {
         P.close();
         P = null;
      }

      if (J != null) {
         J.close();
         J = null;
      }
   }

   private static Identifier YAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }
}
