package com.larpclient;

import com.larpclient.module.MotionBlur;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
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
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public final class MotionBlurEffect {
   static final float L = 0.033333335F;
   static final float x = 0.1F;
   static final float Z = 0.03F;
   static final float G = 60.0F;
   private static final RenderPipeline c = ZAsRenderPipeline("motion_blur", "motion_blur", true, true, false);
   private static final RenderPipeline a = ZAsRenderPipeline("motion_accumulate", "motion_accumulate", true, true, true);
   private static final RenderPipeline s = ZAsRenderPipeline("motion_present", "motion_present", false, false, false);
   private static final BloomFramebuffers p = new BloomFramebuffers();
   private static final BloomFramebuffers J = new BloomFramebuffers(BloomFramebuffers.Type_d.RGBA16_FLOAT);
   private static GpuBuffer O;
   private static int X = -1;
   private static int u;
   private static int j;
   private static int U;
   private static boolean o;
   private static boolean Q;
   private static boolean W;
   private static long t;
   private static float R;
   private static float w;
   private static float M;
   private static float k;

   public static void vAsVoid() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      MotionBlur var1 = MotionBlur.XAsMotionBlur();
      if (var0.world != null && var1 != null && var1.isRuntimeActive()) {
         if (X != BloomQuality.J) {
            QAsVoid();
            o = false;
            X = BloomQuality.J;
         }

         if (!o) {
            Camera var2 = var0.gameRenderer.getCamera();
            long var3 = System.nanoTime();
            float var5 = W ? (float)(var3 - t) / 1.0E9F : 0.0F;
            float var6 = var2.getYaw();
            float var7 = var2.getPitch();
            int var8 = System.identityHashCode(var0.world) * 31 + System.identityHashCode(var2.getFocusedEntity());
            float var9 = MathHelper.wrapDegrees(var6 - R);
            float var10 = var7 - w;
            if (W && var8 == U && !(var5 <= 0.0F) && !(var5 > 0.25F) && !(Math.abs(var9) > 60.0F) && !(Math.abs(var10) > 60.0F)) {
               M = PAsFloat(M, var9 / var5, var5);
               k = PAsFloat(k, var10 / var5, var5);
            } else {
               k = 0.0F;
               M = 0.0F;
               Q = false;
            }

            W = true;
            U = var8;
            t = var3;
            R = var6;
            w = var7;
            float var11 = var1.strength.getValue().floatValue();
            boolean var12 = var1.mode.getValue().equals("Classic");
            float var13 = var11 * 0.033333335F;
            if (var12) {
               p.close();
               Q = Q & J.f != null;
            } else {
               J.close();
               Q = false;
               if (Math.max(Math.abs(M), Math.abs(k)) * var13 < 0.02F) {
                  return;
               }
            }

            long var14 = OpenScreen.RAsLong();
            String var16 = "resources";

            try {
               if (O == null) {
                  for (RenderPipeline var20 : new RenderPipeline[]{c, a, s}) {
                     WorldLabelLayout.DAsVoid(var20);
                  }

                  O = RenderSystem.getDevice().createBuffer(() -> "Larp MotionBlur parameters", 136, 96L);
                  ClientLog.GAsVoid("MotionBlur", "Motion blur shaders loaded");
               }

               Framebuffer var49x = var0.getFramebuffer();
               CommandEncoder var50 = RenderSystem.getDevice().createCommandEncoder();
               float var51 = (float)Math.tan(Math.toRadians((double)WebRenderer.pAsFloat()) / 2.0);
               MemoryStack var52 = MemoryStack.stackPush();

               try {
                  ByteBuffer var21 = var52.malloc(96);
                  bAsMatrix4f(var6, var7, var6 - M * var13, var7 - k * var13).get(var21);
                  var21.position(64);
                  var21.putFloat(var51 * (float)var49x.textureWidth / (float)Math.max(1, var49x.textureHeight))
                     .putFloat(var51)
                     .putFloat(var1.maxLength.getValue().floatValue() / 100.0F)
                     .putFloat((float)PAsInt(var1.quality.getValue()));
                  var21.putFloat(1.0F / (float)var49x.textureWidth)
                     .putFloat(1.0F / (float)var49x.textureHeight)
                     .putFloat(Q ? LAsFloat(var5, var11) : 0.0F)
                     .putFloat(Q ? 0.0F : 1.0F);
                  var21.flip();
                  var50.writeToBuffer(O.slice(), var21);
               } catch (Throwable var48) {
                  if (var52 != null) {
                     try {
                        var52.close();
                     } catch (Throwable var44) {
                        var48.addSuppressed(var44);
                     }
                  }

                  throw var48;
               }

               if (var52 != null) {
                  var52.close();
               }

               GpuSampler var53 = RenderSystem.getSamplerCache().get(FilterMode.LINEAR);
               GpuSampler var54 = RenderSystem.getSamplerCache().get(FilterMode.NEAREST);
               if (var12) {
                  var16 = "accumulate";
                  if (var49x.textureWidth != u || var49x.textureHeight != j) {
                     Q = false;
                  }

                  J.FAsVoid(var49x.textureWidth, var49x.textureHeight, 1, BloomFramebuffers.Type_b.BLUR);
                  u = var49x.textureWidth;
                  j = var49x.textureHeight;
                  RenderPass var22 = var50.createRenderPass(() -> "Larp MotionBlur accumulate", J.y.getColorAttachmentView(), OptionalInt.empty());

                  try {
                     var22.setPipeline(a);
                     var22.setUniform("MotionBlur", O.slice());
                     var22.bindTexture("Source", var49x.getColorAttachmentView(), var54);
                     var22.bindTexture("History", J.f.getColorAttachmentView(), var54);
                     var22.bindTexture("Depth", var49x.getDepthAttachmentView(), var54);
                     var22.draw(0, 3);
                  } catch (Throwable var47) {
                     if (var22 != null) {
                        try {
                           var22.close();
                        } catch (Throwable var431) {
                           var47.addSuppressed(var431);
                        }
                     }

                     throw var47;
                  }

                  if (var22 != null) {
                     var22.close();
                  }

                  SimpleFramebuffer var55 = J.y;
                  J.y = J.f;
                  J.f = var55;
                  Q = true;
                  var16 = "present";
                  RenderPass var23 = var50.createRenderPass(() -> "Larp MotionBlur present", var49x.getColorAttachmentView(), OptionalInt.empty());

                  try {
                     var23.setPipeline(s);
                     var23.bindTexture("History", var55.getColorAttachmentView(), var54);
                     var23.bindTexture("Depth", var49x.getDepthAttachmentView(), var54);
                     var23.draw(0, 3);
                  } catch (Throwable var46) {
                     if (var23 != null) {
                        try {
                           var23.close();
                        } catch (Throwable var42) {
                           var46.addSuppressed(var42);
                        }
                     }

                     throw var46;
                  }

                  if (var23 != null) {
                     var23.close();
                  }
               } else {
                  var16 = "streak";
                  p.FAsVoid(var49x.textureWidth, var49x.textureHeight, 1, BloomFramebuffers.Type_b.COLOR);
                  BloomQuality.aAsVoid(WorldLabelLayout.u, var49x, p.f);
                  RenderPass var56 = var50.createRenderPass(() -> "Larp MotionBlur streaks", var49x.getColorAttachmentView(), OptionalInt.empty());

                  try {
                     var56.setPipeline(c);
                     var56.setUniform("MotionBlur", O.slice());
                     var56.bindTexture("Source", p.f.getColorAttachmentView(), var53);
                     var56.bindTexture("Depth", var49x.getDepthAttachmentView(), var54);
                     var56.draw(0, 3);
                  } catch (Throwable var45) {
                     if (var56 != null) {
                        try {
                           var56.close();
                        } catch (Throwable var41) {
                           var45.addSuppressed(var41);
                        }
                     }

                     throw var45;
                  }

                  if (var56 != null) {
                     var56.close();
                  }
               }
            } catch (RuntimeException var49) {
               o = true;

               try {
                  QAsVoid();
               } catch (RuntimeException var40) {
                  var49.addSuppressed(var40);
               }

               ClientLog.lAsVoid(
                  "MotionBlur",
                  "MotionBlurRenderer stage=" + var16 + " mode=" + var1.mode.getValue() + " framebuffer=scene; disabled until resource reload",
                  var49
               );
            } finally {
               OpenScreen.aAsVoid("MotionBlur", var14);
            }
         }
      } else {
         CAsVoid();
      }
   }

   static float PAsFloat(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * (1.0F - (float)Math.exp((double)(-var2 / 0.03F)));
   }

   static float LAsFloat(float var0, float var1) {
      return (float)Math.exp((double)(-var0 / (var1 * 0.1F)));
   }

   static int PAsInt(String var0) {
      return switch (var0) {
         case "Low" -> 8;
         case "High" -> 20;
         default -> 12;
      };
   }

   static Matrix4f bAsMatrix4f(float var0, float var1, float var2, float var3) {
      return mAsMatrix4f(var2, var3).mul(mAsMatrix4f(var0, var1).transpose());
   }

   private static Matrix4f mAsMatrix4f(float var0, float var1) {
      return new Matrix4f().rotateX((float)Math.toRadians((double)var1)).rotateY((float)Math.toRadians((double)var0) + (float) Math.PI);
   }

   private static void CAsVoid() {
      Q = false;
      W = false;
      k = 0.0F;
      M = 0.0F;
   }

   public static void QAsVoid() {
      CAsVoid();

      try {
         p.close();
         J.close();
      } finally {
         if (O != null) {
            O.close();
            O = null;
         }
      }
   }

   private static RenderPipeline ZAsRenderPipeline(String var0, String var1, boolean var2, boolean var3, boolean var4) {
      Builder var5 = RenderPipeline.builder(new Snippet[0])
         .withLocation(LAsIdentifier(var0))
         .withVertexShader(LAsIdentifier("post/fullscreen"))
         .withFragmentShader(LAsIdentifier("post/" + var1))
         .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false);
      if (var2) {
         var5.withSampler("Source");
      }

      if (!var1.equals("motion_blur")) {
         var5.withSampler("History");
      }

      var5.withSampler("Depth");
      if (var3) {
         var5.withUniform("MotionBlur", UniformType.UNIFORM_BUFFER);
      }

      return var5.build();
   }

   private static Identifier LAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }

   private MotionBlurEffect() {
   }
}
