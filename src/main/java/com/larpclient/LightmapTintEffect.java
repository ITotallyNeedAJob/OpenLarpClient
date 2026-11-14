package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import net.minecraft.client.gl.UniformType;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.system.MemoryStack;

public final class LightmapTintEffect {
   private static RenderPipeline k;
   private static RenderPipeline j;
   private static GpuBuffer A;
   private static int O = -1;
   private static int R = -1;

   private LightmapTintEffect() {
   }

   public static RenderPipeline HAsRenderPipeline(RenderPipeline var0, int var1, float var2) {
      if (!(var2 <= 0.0F) && (var1 & 16777215) != 16777215 && R != BloomQuality.J) {
         try {
            if (O != BloomQuality.J || GroundSample == null) {
               jAsVoid();
               RenderPipeline var3 = WorldLabelLayout.GAsRenderPipeline_Builder(var0)
                  .withLocation(Identifier.of("larp-client", "lightmap_tint"))
                  .withFragmentShader(Identifier.of("larp-client", "post/lightmap_tint"))
                  .withUniform("LarpLightTint", UniformType.UNIFORM_BUFFER)
                  .build();
               WorldLabelLayout.DAsVoid(var3);
               A = RenderSystem.getDevice().createBuffer(() -> "Larp complete lightmap tint", 136, 16L);
               GroundSample = var3;
               TextLayout = var0;
               O = BloomQuality.J;
               ClientLog.XAsVoid("WorldColor", "Native lightmap tint ready; sky, block, ambient and night vision");
            }

            Vector3fc var10 = nAsVector3fc(new Vector3f(1.0F), var1, var2);
            MemoryStack var4 = MemoryStack.stackPush();

            try {
               ByteBuffer var5 = var4.malloc(16);
               var5.putFloat(var10.x()).putFloat(var10.y()).putFloat(var10.z()).putFloat(0.0F).flip();
               RenderSystem.getDevice().createCommandEncoder().writeToBuffer(A.slice(), var5);
            } catch (Throwable var81) {
               if (var4 != null) {
                  try {
                     var4.close();
                  } catch (Throwable var7) {
                     var81.addSuppressed(var7);
                  }
               }

               throw var81;
            }

            if (var4 != null) {
               var4.close();
            }

            return k;
         } catch (RuntimeException var91) {
            iAsVoid("prepare", var91);
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static void LAsVoid(RenderPass var0, RenderPipeline var1) {
      if (var1 == GroundSample && A != null) {
         try {
            var0.setUniform("LarpLightTint", A.slice());
         } catch (RuntimeException var4) {
            RenderPipeline var3 = j;
            iAsVoid("uniform binding", var4);
            if (var3 != null) {
               var0.setPipeline(var3);
            }
         }
      }
   }

   private static void iAsVoid(String var0, RuntimeException var1) {
      R = BloomQuality.J;

      try {
         jAsVoid();
      } catch (RuntimeException var3) {
         var1.addSuppressed(var3);
      }

      ClientLog.lAsVoid("WorldColor", "Lightmap stage=" + var0 + "; restored native lighting, retained settings; retry after resource reload", var1);
   }

   public static void jAsVoid() {
      TextLayout = null;
      GroundSample = null;
      GpuBuffer var0 = A;
      A = null;
      if (var0 != null) {
         var0.close();
      }
   }

   public static Vector3fc nAsVector3fc(Vector3fc var0, int var1, float var2) {
      if (!(var2 <= 0.0F) && (var1 & 16777215) != 16777215) {
         float var3 = Math.clamp(var2, 0.0F, 1.0F);
         float var4 = 1.0F + ((float)(var1 >>> 16 & 0xFF) / 255.0F - 1.0F) * var3;
         float var5 = 1.0F + ((float)(var1 >>> 8 & 0xFF) / 255.0F - 1.0F) * var3;
         float var6 = 1.0F + ((float)(var1 & 0xFF) / 255.0F - 1.0F) * var3;
         return PAsVector3fc(var0, var4, var5, var6);
      } else {
         return var0;
      }
   }

   private static Vector3fc PAsVector3fc(Vector3fc var0, float var1, float var2, float var3) {
      return new Vector3f(var0.x() * var1, var0.y() * var2, var0.z() * var3);
   }
}
