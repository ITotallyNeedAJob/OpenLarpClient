package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.nio.ByteBuffer;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public final class DropletSurfaceRenderer {
   private static final int u = 512;
   private static final int k = 8240;
   private static final RenderPipeline L = RenderPipeline.builder(new Snippet[0])
      .withLocation(SAsIdentifier("pipeline/temple/droplet_surface"))
      .withVertexShader(SAsIdentifier("temple/droplet_surface"))
      .withFragmentShader(SAsIdentifier("temple/droplet_surface"))
      .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER)
      .withUniform("DropletBatch", UniformType.UNIFORM_BUFFER)
      .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
      .withDepthWrite(true)
      .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
      .withCull(false)
      .build();
   private static final RenderPipeline h = RenderPipeline.builder(new Snippet[0])
      .withLocation(SAsIdentifier("pipeline/temple/droplet_composite"))
      .withVertexShader(SAsIdentifier("post/fullscreen"))
      .withFragmentShader(SAsIdentifier("temple/droplet_composite"))
      .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER)
      .withSampler("SceneColor")
      .withSampler("SceneDepth")
      .withSampler("DropletMask")
      .withSampler("DropletDepth")
      .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   private static SimpleFramebuffer T;
   private static GpuBuffer p;

   public static void tAsVoid() {
      if (VolumetricWaterEngine.jAsInt() != 0 && WetReflectionEffect.gAsBoolean()) {
         WorldRenderState var0 = WetReflectionEffect.iAsWorldRenderState();
         if (var0 != null) {
            Framebuffer var1 = MinecraftClient.getInstance().getFramebuffer();
            if (T == null || T.textureWidth != var1.textureWidth || T.textureHeight != var1.textureHeight) {
               EAsVoid();
               T = new SimpleFramebuffer("Larp volumetric water surface", var1.textureWidth, var1.textureHeight, true);
            }

            if (p == null) {
               WorldLabelLayout.DAsVoid(L);
               WorldLabelLayout.DAsVoid(h);
               p = RenderSystem.getDevice().createBuffer(() -> "Larp volumetric droplet batch", 136, 8240L);
               ClientLog.GAsVoid("TerrainMaterials", "Volumetric surface ready; {} droplets maximum, {} per draw", 4096, 512);
            }

            WetReflectionEffect.FAsVoid();
            GpuDevice var2 = RenderSystem.getDevice();
            CommandEncoder var3 = var2.createCommandEncoder();
            var3.clearColorAndDepthTextures(T.getColorAttachment(), 0, T.getDepthAttachment(), 1.0);
            var3.copyTextureToTexture(var1.getDepthAttachment(), T.getDepthAttachment(), 0, 0, 0, 0, 0, var1.textureWidth, var1.textureHeight);
            CameraRenderState var4 = var0.cameraRenderState;
            Matrix4f var5 = new Matrix4f(WebRenderer.VAsMatrix4fc()).invert();
            Vector3f var6 = var5.transformDirection(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var7 = var5.transformDirection(new Vector3f(0.0F, 1.0F, 0.0F));
            float var8 = MediaSession.LAsFloat();

            for (short var9 = 0; var9 < VolumetricWaterEngine.jAsInt(); var9 = (short)(var9 + 512)) {
               int var10 = Math.min(512, VolumetricWaterEngine.jAsInt() - var9);
               MemoryStack var11 = MemoryStack.stackPush();

               try {
                  ByteBuffer var12 = var11.calloc(8240);

                  for (int var13 = 0; var13 < var10; var13++) {
                     int var14 = var9 + var13;
                     var12.putFloat((float)(VolumetricWaterEngine.HAsDouble(var14, var8) - var4.pos.x))
                        .putFloat((float)(VolumetricWaterEngine.RAsDouble(var14, var8) - var4.pos.y))
                        .putFloat((float)(VolumetricWaterEngine.fAsDouble(var14, var8) - var4.pos.z))
                        .putFloat((float)VolumetricWaterEngine.fAsDouble());
                  }

                  var12.position(8192);
                  var12.putFloat(var6.x).putFloat(var6.y).putFloat(var6.z).putFloat(0.0F);
                  var12.putFloat(var7.x).putFloat(var7.y).putFloat(var7.z).putFloat(0.0F);
                  var12.putFloat((float)var1.textureWidth).putFloat((float)var1.textureHeight).putFloat(0.0F).putFloat(0.0F);
                  var12.flip();
                  var3.writeToBuffer(p.slice(), var12);
               } catch (Throwable var20) {
                  if (var11 != null) {
                     try {
                        var11.close();
                     } catch (Throwable var17) {
                        var20.addSuppressed(var17);
                     }
                  }

                  throw var20;
               }

               if (var11 != null) {
                  var11.close();
               }

               RenderPass var23 = var3.createRenderPass(
                  () -> "Larp volumetric surface", T.getColorAttachmentView(), OptionalInt.empty(), T.getDepthAttachmentView(), OptionalDouble.empty()
               );

               try {
                  var23.setPipeline(L);
                  var23.setUniform("LarpMaterial", WetReflectionEffect.MAsGpuBuffer().slice());
                  var23.setUniform("DropletBatch", p.slice());
                  var23.draw(0, 6 * var10);
               } catch (Throwable var191) {
                  if (var23 != null) {
                     try {
                        var23.close();
                     } catch (Throwable var161) {
                        var191.addSuppressed(var161);
                     }
                  }

                  throw var191;
               }

               if (var23 != null) {
                  var23.close();
               }
            }

            RenderPass var21 = var3.createRenderPass(() -> "Larp volumetric water composite", var1.getColorAttachmentView(), OptionalInt.empty());

            try {
               var21.setPipeline(h);
               var21.setUniform("LarpMaterial", WetReflectionEffect.MAsGpuBuffer().slice());
               GpuSampler var22 = RenderSystem.getSamplerCache().get(FilterMode.LINEAR);
               GpuSampler var24 = RenderSystem.getSamplerCache().get(FilterMode.NEAREST);
               var21.bindTexture("SceneColor", WetReflectionEffect.SAsGpuTextureView(), var22);
               var21.bindTexture("SceneDepth", WetReflectionEffect.RAsGpuTextureView(), var24);
               var21.bindTexture("DropletMask", T.getColorAttachmentView(), var24);
               var21.bindTexture("DropletDepth", T.getDepthAttachmentView(), var24);
               var21.draw(0, 3);
            } catch (Throwable var181) {
               if (var21 != null) {
                  try {
                     var21.close();
                  } catch (Throwable var151) {
                     var181.addSuppressed(var151);
                  }
               }

               throw var181;
            }

            if (var21 != null) {
               var21.close();
            }
         }
      }
   }

   public static void EAsVoid() {
      if (T != null) {
         T.delete();
         T = null;
      }

      if (p != null) {
         p.close();
         p = null;
      }
   }

   private static Identifier SAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }

   private DropletSurfaceRenderer() {
   }
}
