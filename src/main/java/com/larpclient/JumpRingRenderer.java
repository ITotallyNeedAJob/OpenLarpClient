package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.ScissorState;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.lwjgl.system.MemoryStack;

public final class JumpRingRenderer {
   private static final RenderLayer[][] E = new RenderLayer[7][2];
   private static GpuBuffer x;
   private static int LineSegment = -1;
   private static int i;
   private static float V;

   public static void TAsVoid(int var0) {
      ChinaHatCodec = var0;
      V = (float)((double)System.nanoTime() / 1.0E9 % 3600.0);
   }

   public static RenderLayer YAsRenderLayer(int var0, boolean var1) {
      if (LineSegment != BloomQuality.J) {
         HAsVoid();
         LineSegment = BloomQuality.J;

         for (RenderLayer[] var5 : E) {
            Arrays.fill(var5, null);
         }
      }

      int var6 = var1 ? 1 : 0;
      if (E[var0][var6] == null) {
         Identifier var7 = Identifier.of("larp-client", "jump/jumpring");
         Builder var8 = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_COLOR_SNIPPET})
            .withLocation(Identifier.of("larp-client", "jump_" + var0 + "_" + var6))
            .withVertexShader(var7)
            .withFragmentShader(var7)
            .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, DrawMode.QUADS)
            .withShaderDefine("CLARITY_JUMP")
            .withShaderDefine("JUMP_STYLE", var0)
            .withUniform("JumpParameters", UniformType.UNIFORM_BUFFER)
            .withBlend(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA)
            .withCull(false);
         if (var1) {
            var8.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withDepthWrite(false);
         } else {
            var8.withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST).withDepthWrite(false);
         }

         RenderPipeline var9 = var8.build();
         WorldLabelLayout.DAsVoid(var9);
         E[var0][var6] = RenderLayer.of("larp_jump_" + var0 + "_" + var6, RenderSetup.builder(var9).translucent().build());
      }

      return E[var0][var6];
   }

   public static boolean QAsBoolean(ImmediateMaterialState var0, bk.Type_c var1) {
      if (!var0.pipeline().getShaderDefines().flags().contains("CLARITY_JUMP")) {
         return false;
      } else {
         CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
         if (AuraModule == null) {
            AuraModule = RenderSystem.getDevice().createBuffer(() -> "Larp jump parameters", 136, 16L);
         }

         MemoryStack var3 = MemoryStack.stackPush();

         try {
            ByteBuffer var4 = var3.malloc(16);
            var4.putFloat((float)(ChinaHatCodec >> 16 & 0xFF) / 255.0F).putFloat((float)(ChinaHatCodec >> 8 & 0xFF) / 255.0F).putFloat((float)(ChinaHatCodec & 0xFF) / 255.0F).putFloat(V).flip();
            var2.writeToBuffer(AuraModule.slice(), var4);
         } catch (Throwable var121) {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (Throwable var11) {
                  var121.addSuppressed(var11);
               }
            }

            throw var121;
         }

         if (var3 != null) {
            var3.close();
         }

         Framebuffer var13 = var0.outputTarget().getFramebuffer();
         GpuTextureView var14 = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : var13.getColorAttachmentView();
         GpuTextureView var5 = RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : var13.getDepthAttachmentView();
         RenderPass var6 = var2.createRenderPass(() -> "Larp jump circles", var14, OptionalInt.empty(), var5, OptionalDouble.empty());

         try {
            var6.setPipeline(var0.pipeline());
            ScissorState var7 = var0.scissorState();
            if (var7.isEnabled()) {
               var6.enableScissor(var7.getX(), var7.getY(), var7.getWidth(), var7.getHeight());
            }

            RenderSystem.bindDefaultUniforms(var6);
            var6.setUniform("DynamicTransforms", var0.dynamicTransforms());
            var6.setUniform("JumpParameters", AuraModule.slice());
            var6.setVertexBuffer(0, var1.vertexBuffer());
            var6.setIndexBuffer(var1.indexBuffer(), var1.indexType());
            var6.drawIndexed(var1.baseVertex(), var1.firstIndex(), var1.indexCount(), 1);
         } catch (Throwable var13x) {
            if (var6 != null) {
               try {
                  var6.close();
               } catch (Throwable var10) {
                  var13x.addSuppressed(var10);
               }
            }

            throw var13x;
         }

         if (var6 != null) {
            var6.close();
         }

         return true;
      }
   }

   public static void HAsVoid() {
      if (AuraModule != null) {
         AuraModule.close();
         AuraModule = null;
      }
   }

   private JumpRingRenderer() {
   }
}
