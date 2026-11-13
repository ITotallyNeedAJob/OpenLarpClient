package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ScissorState;
import net.minecraft.client.gl.UniformType;
import org.lwjgl.system.MemoryStack;

public final class SoftParticlePipeline {
   private static GpuBuffer a;
   private static int r = -1;
   public static boolean S = true;
   private static boolean n;
   private static boolean Q;
   private static boolean G;

   public static void WAsVoid(Builder var0) {
      var0.withShaderDefine("SOFT_PARTICLE").withSampler("SceneDepth").withUniform("ParticleDepth", UniformType.UNIFORM_BUFFER);
   }

   public static boolean UAsBoolean() {
      return n;
   }

   public static void TAsVoid(boolean var0) {
      n = false;

      try {
         if (r != BloomQuality.J) {
            yAsVoid();
            G = false;
            Q = false;
            r = BloomQuality.J;
         }

         if (!S) {
            yAsVoid();
            return;
         }

         if (!var0) {
            yAsVoid();
            return;
         }

         if (Q) {
            return;
         }

         if (!G) {
            EffectGeometryRenderer.aAsVoid();
            G = true;
         }

         if (SceneRenderResources.yAsGpuTextureView() == null) {
            return;
         }

         if (a == null) {
            a = RenderSystem.getDevice().createBuffer(() -> "Larp particle depth parameters", 136, 16L);
         }

         CommandEncoder var1 = RenderSystem.getDevice().createCommandEncoder();
         MemoryStack var3 = MemoryStack.stackPush();

         try {
            ByteBuffer var4 = var3.malloc(16);
            var4.putFloat(2.0F).putFloat(-1.0F).putFloat(0.16F).putFloat(0.0F).flip();
            var1.writeToBuffer(a.slice(), var4);
         } catch (Throwable var81) {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (Throwable var7) {
                  var81.addSuppressed(var7);
               }
            }

            throw var81;
         }

         if (var3 != null) {
            var3.close();
         }

         n = true;
         op.YAsVoid("Soft particle depth consumers", 1L);
      } catch (RuntimeException var91) {
         Q = true;

         try {
            yAsVoid();
         } catch (RuntimeException var6) {
            var91.addSuppressed(var6);
         }

         ClientLog.lAsVoid("Particles", "Soft depth unavailable; ordinary sprites retained until resource reload", var91);
      }
   }

   static boolean FAsBoolean(ImmediateMaterialState var0, bk.Type_c var1) {
      if (!var0.pipeline().getShaderDefines().flags().contains("SOFT_PARTICLE")) {
         return false;
      } else if (!n) {
         EffectGeometryRenderer.aAsPw(var0).drawFromBuffer(var1);
         return true;
      } else {
         GpuTextureView var2 = SceneRenderResources.yAsGpuTextureView();
         if (var2 == null) {
            EffectGeometryRenderer.aAsPw(var0).drawFromBuffer(var1);
            return true;
         } else {
            Framebuffer var3 = var0.outputTarget().getFramebuffer();
            GpuTextureView var4 = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : var3.getColorAttachmentView();
            GpuTextureView var5 = RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : var3.getDepthAttachmentView();
            if (var2 == var5) {
               EffectGeometryRenderer.aAsPw(var0).drawFromBuffer(var1);
               return true;
            } else {
               try {
                  RenderPass var6 = RenderSystem.getDevice()
                     .createCommandEncoder()
                     .createRenderPass(() -> "Larp soft particles", var4, OptionalInt.empty(), var5, OptionalDouble.empty());

                  try {
                     var6.setPipeline(var0.pipeline());
                     ScissorState var7 = var0.scissorState();
                     if (var7.isEnabled()) {
                        var6.enableScissor(var7.getX(), var7.getY(), var7.getWidth(), var7.getHeight());
                     }

                     RenderSystem.bindDefaultUniforms(var6);
                     var6.setUniform("DynamicTransforms", var0.dynamicTransforms());
                     var6.setUniform("ParticleDepth", a.slice());
                     var6.bindTexture("SceneDepth", var2, RenderSystem.getSamplerCache().get(FilterMode.NEAREST));

                     for (ImmediateMaterialState.Type_l var9 : var0.textures()) {
                        var6.bindTexture(var9.name(), var9.textureView(), var9.sampler());
                     }

                     var6.setVertexBuffer(0, var1.vertexBuffer());
                     var6.setIndexBuffer(var1.indexBuffer(), var1.indexType());
                     var6.drawIndexed(var1.baseVertex(), var1.firstIndex(), var1.indexCount(), 1);
                  } catch (Throwable var12) {
                     if (var6 != null) {
                        try {
                           var6.close();
                        } catch (Throwable var11) {
                           var12.addSuppressed(var11);
                        }
                     }

                     throw var12;
                  }

                  if (var6 != null) {
                     var6.close();
                  }
               } catch (RuntimeException var131) {
                  Q = true;

                  try {
                     yAsVoid();
                  } catch (RuntimeException var101) {
                     var131.addSuppressed(var101);
                  }

                  ClientLog.lAsVoid("Particles", "Soft particle draw failed; ordinary sprites retained until resource reload", var131);
               }

               return true;
            }
         }
      }
   }

   public static void yAsVoid() {
      n = false;
      if (a != null) {
         AutoCloseable[] var0 = new AutoCloseable[]{a};
         a = null;
         RuntimeException var1 = null;

         for (AutoCloseable var5 : var0) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Exception var9) {
                  RuntimeException var7 = (RuntimeException)(var9 instanceof RuntimeException var8 ? var8 : new IllegalStateException(var9));
                  if (var1 == null) {
                     var1 = var7;
                  } else {
                     var1.addSuppressed(var7);
                  }
               }
            }
         }

         if (var1 != null) {
            throw var1;
         }
      }
   }

   private SoftParticlePipeline() {
   }
}
