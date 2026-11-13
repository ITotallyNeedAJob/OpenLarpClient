package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gl.ScissorState;
import net.minecraft.client.render.OutputTarget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.RenderSetup.Texture;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

public record ImmediateMaterialState(
   RenderPipeline pipeline,
   OutputTarget outputTarget,
   GpuBufferSlice dynamicTransforms,
   ScissorState scissorState,
   List<ImmediateMaterialState.Type_l> textures
) {
   public static ImmediateMaterialState prepare(RenderLayer var0) {
      RenderSetup var1 = var0.renderSetup;
      Matrix4fStack var2 = RenderSystem.getModelViewStack();
      Consumer var3 = var1.layeringTransform.getTransform();
      if (var3 != null) {
         var2.pushMatrix();
         var3.accept(var2);
      }

      ImmediateMaterialState var8;
      try {
         GpuBufferSlice var4 = RenderSystem.getDynamicUniforms()
            .write(RenderSystem.getModelViewMatrix(), new Vector4f(1.0F), new Vector3f(), var1.textureTransform.getTransformSupplier());
         List var5 = var1.resolveTextures()
            .entrySet()
            .stream()
            .map(
               var0x -> new ImmediateMaterialState.Type_l(
                     (String)var0x.getKey(), ((Texture)var0x.getValue()).textureView(), ((Texture)var0x.getValue()).sampler()
                  )
            )
            .toList();
         ScissorState var6 = RenderSystem.getScissorStateForRenderTypeDraws();
         ScissorState var7 = new ScissorState();
         if (var6.isEnabled()) {
            var7.enable(var6.getX(), var6.getY(), var6.getWidth(), var6.getHeight());
         }

         var8 = new ImmediateMaterialState(var0.getRenderPipeline(), var1.outputTarget, var4, var7, var5);
      } finally {
         if (var3 != null) {
            var2.popMatrix();
         }
      }

      return var8;
   }

   public void drawFromBuffer(bk.Type_c var1) {
      Framebuffer var2 = this.outputTarget.getFramebuffer();
      GpuTextureView var3 = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : var2.getColorAttachmentView();
      GpuTextureView var4 = !var2.useDepthAttachment
         ? null
         : (RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : var2.getDepthAttachmentView());
      RenderPass var5 = RenderSystem.getDevice()
         .createCommandEncoder()
         .createRenderPass(() -> "Larp immediate material " + this.pipeline.getLocation(), var3, OptionalInt.empty(), var4, OptionalDouble.empty());

      try {
         var5.setPipeline(this.pipeline);
         if (this.scissorState.isEnabled()) {
            var5.enableScissor(this.scissorState.getX(), this.scissorState.getY(), this.scissorState.getWidth(), this.scissorState.getHeight());
         }

         RenderSystem.bindDefaultUniforms(var5);
         var5.setUniform("DynamicTransforms", this.dynamicTransforms);

         for (ImmediateMaterialState.Type_l var7 : this.textures) {
            var5.bindTexture(var7.name(), var7.textureView(), var7.sampler());
         }

         var5.setVertexBuffer(0, var1.vertexBuffer());
         var5.setIndexBuffer(var1.indexBuffer(), var1.indexType());
         var5.drawIndexed(var1.baseVertex(), var1.firstIndex(), var1.indexCount(), 1);
      } catch (Throwable var9) {
         if (var5 != null) {
            try {
               var5.close();
            } catch (Throwable var81) {
               var9.addSuppressed(var81);
            }
         }

         throw var9;
      }

      if (var5 != null) {
         var5.close();
      }
   }

   public static record Type_l(String name, GpuTextureView textureView, GpuSampler sampler) {
   }
}
