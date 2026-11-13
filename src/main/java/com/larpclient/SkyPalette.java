package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.nio.ByteBuffer;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class SkyPalette implements AutoCloseable {
   public static final int N = 160;
   private GpuBuffer R;
   private GpuBuffer T;
   private GpuBuffer m;

   public SkyPalette() {
   }

   public static RenderPipeline QAsRenderPipeline(String var0, String... var1) {
      Builder var2 = RenderPipeline.builder(new Snippet[0]).withUniform("SkyPalette", UniformType.UNIFORM_BUFFER);

      for (String var6 : var1) {
         var2.withSampler(var6);
      }

      Builder var7 = var2.withLocation(Identifier.of("larp-client", "sky/" + var0))
         .withVertexShader(Identifier.of("larp-client", var0.startsWith("larp/") ? "larp/fullscreen" : "sky/dome"))
         .withFragmentShader(Identifier.of("larp-client", var0.startsWith("larp/") ? var0 : "sky/" + var0))
         .withVertexFormat(VertexFormats.POSITION, DrawMode.TRIANGLES)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend();
      if (var0.startsWith("larp/")) {
         var7.withBlend(BlendFunction.TRANSLUCENT);
      }

      if (!var0.startsWith("larp/")) {
         var7.withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER).withUniform("Projection", UniformType.UNIFORM_BUFFER);
      }

      return var7.build();
   }

   private void yAsVoid(boolean var1) {
      if (!var1 && this.R == null) {
         ClientLog.GAsVoid("Sky", "Sky renderer initialized: camera-centered cube, no depth writes");
         BufferAllocator var2 = BufferAllocator.fixedSized(36 * VertexFormats.POSITION.getVertexSize());

         try {
            BufferBuilder var3 = new BufferBuilder(var2, DrawMode.TRIANGLES, VertexFormats.POSITION);
            float[][] var4 = new float[][]{
               {-1.0F, -1.0F, -1.0F},
               {1.0F, -1.0F, -1.0F},
               {1.0F, 1.0F, -1.0F},
               {-1.0F, 1.0F, -1.0F},
               {-1.0F, -1.0F, 1.0F},
               {1.0F, -1.0F, 1.0F},
               {1.0F, 1.0F, 1.0F},
               {-1.0F, 1.0F, 1.0F}
            };
            int[][] var5 = new int[][]{{0, 1, 2, 3}, {5, 4, 7, 6}, {4, 0, 3, 7}, {1, 5, 6, 2}, {3, 2, 6, 7}, {4, 5, 1, 0}};
            int[] var6 = new int[]{0, 1, 2, 0, 2, 3};

            for (int[] var10 : var5) {
               for (int var14 : var6) {
                  float[] var15 = var4[var10[var14]];
                  var3.vertex(var15[0], var15[1], var15[2]);
               }
            }

            BuiltBuffer var20 = var3.end();

            try {
               this.R = RenderSystem.getDevice().createBuffer(() -> "Larp sky cube", 32, var20.getBuffer());
            } catch (Throwable var18) {
               if (var20 != null) {
                  try {
                     var20.close();
                  } catch (Throwable var17) {
                     var18.addSuppressed(var17);
                  }
               }

               throw var18;
            }

            if (var20 != null) {
               var20.close();
            }
         } catch (Throwable var191) {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Throwable var161) {
                  var191.addSuppressed(var161);
               }
            }

            throw var191;
         }

         if (var2 != null) {
            var2.close();
         }
      }

      if (this.m == null) {
         this.m = RenderSystem.getDevice().createBuffer(() -> "Larp sky uniforms", 136, 160L);
      }
   }

   public void dAsVoid(bl var1, SkyShaderEffect var2, ByteBuffer var3) {
      boolean var4 = var2.LAsRenderPipeline().getFragmentShader().getPath().startsWith("larp/");
      this.yAsVoid(var4);
      if (var4 && this.T == null) {
         this.KAsVoid();
      }

      CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
      var5.writeToBuffer(this.m.slice(), var3);
      GpuBufferSlice var6 = var4
         ? null
         : RenderSystem.getDynamicUniforms().write(RenderSystem.getModelViewMatrix(), new Vector4f(1.0F), new Vector3f(), new Matrix4f());
      RenderPass var7 = var5.createRenderPass(
         () -> "Larp world sky", var1.L.getColorAttachmentView(), OptionalInt.empty(), var1.L.getDepthAttachmentView(), OptionalDouble.empty()
      );

      try {
         var7.setPipeline(var2.LAsRenderPipeline());
         if (!var4) {
            RenderSystem.bindDefaultUniforms(var7);
            var7.setUniform("DynamicTransforms", var6);
         }

         var7.setUniform("SkyPalette", this.m.slice());
         var2.bindTextures(var7);
         var7.setVertexBuffer(0, var4 ? this.T : this.R);
         var7.draw(0, var4 ? 3 : 36);
      } catch (Throwable var11) {
         if (var7 != null) {
            try {
               var7.close();
            } catch (Throwable var10) {
               var11.addSuppressed(var10);
            }
         }

         throw var11;
      }

      if (var7 != null) {
         var7.close();
      }
   }

   private void KAsVoid() {
      BufferAllocator var1 = BufferAllocator.fixedSized(3 * VertexFormats.POSITION.getVertexSize());

      try {
         BufferBuilder var2 = new BufferBuilder(var1, DrawMode.TRIANGLES, VertexFormats.POSITION);
         var2.vertex(-1.0F, -1.0F, 0.0F);
         var2.vertex(3.0F, -1.0F, 0.0F);
         var2.vertex(-1.0F, 3.0F, 0.0F);
         BuiltBuffer var3 = var2.end();

         try {
            this.T = RenderSystem.getDevice().createBuffer(() -> "Larp full-screen sky", 32, var3.getBuffer());
         } catch (Throwable var8) {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (var3 != null) {
            var3.close();
         }
      } catch (Throwable var91) {
         if (var1 != null) {
            try {
               var1.close();
            } catch (Throwable var61) {
               var91.addSuppressed(var61);
            }
         }

         throw var91;
      }

      if (var1 != null) {
         var1.close();
      }

      ClientLog.GAsVoid("SkyShader", "Full-screen sky triangle initialized; one fragment per pixel, camera rays independent of native sky transforms");
   }

   @Override
   public void close() {
      if (this.R != null) {
         this.R.close();
         this.R = null;
      }

      if (this.T != null) {
         this.T.close();
         this.T = null;
      }

      if (this.m != null) {
         this.m.close();
         this.m = null;
      }
   }
}
