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
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.OptionalInt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.system.MemoryStack;

public final class ChamsOutlineEffect implements AutoCloseable {
   private static final int SahurDanceEngine = 0;
   private static final double F = 1.0;
   private static final RenderPipeline AuctionNotifier = mAsRenderPipeline("chams_edge", false);
   private static final RenderPipeline GroundSample = qAsRenderPipeline();
   private static final RenderPipeline N = mAsRenderPipeline("chams_blur", false);
   private static final RenderPipeline C = mAsRenderPipeline("chams_composite", true);
   private static final RenderPipeline ChatPayParser = RenderPipeline.builder(new Snippet[0])
      .withLocation(rAsIdentifier("chams_hand_mask"))
      .withVertexShader(rAsIdentifier("post/fullscreen"))
      .withFragmentShader(rAsIdentifier("post/chams_hand_mask"))
      .withSampler("Before")
      .withSampler("After")
      .withSampler("HandDepth")
      .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   private final String i;
   private final BloomFramebuffers r = new BloomFramebuffers();
   private SimpleFramebuffer X;
   private GpuBuffer H;
   private EffectDrawBuffers S;
   private int PetEffectRenderer = -1;
   private boolean p;
   private boolean Z;
   private boolean W;
   private boolean q;
   private boolean T;
   private boolean V;

   public ChamsOutlineEffect(String var1) {
      this.i = var1;
   }

   private static RenderPipeline qAsRenderPipeline() {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(rAsIdentifier("chams_entity_mask"))
         .withVertexShader(rAsIdentifier("post/fullscreen"))
         .withFragmentShader(rAsIdentifier("post/chams_entity_mask"))
         .withSampler("Source")
         .withSampler("ModelDepth")
         .withSampler("SceneDepth")
         .withUniform("ChamsParameters", UniformType.UNIFORM_BUFFER)
         .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withoutBlend()
         .withCull(false)
         .build();
   }

   private static RenderPipeline mAsRenderPipeline(String var0, boolean var1) {
      Builder var2 = RenderPipeline.builder(new Snippet[0]).withSampler("Source").withUniform("ChamsParameters", UniformType.UNIFORM_BUFFER);
      if (var1) {
         var2.withSampler("Glow");
      }

      if (var1) {
         var2.withBlend(BlendFunction.TRANSLUCENT);
      } else {
         var2.withoutBlend();
      }

      return var2.withLocation(rAsIdentifier(var0))
         .withVertexShader(rAsIdentifier("post/fullscreen"))
         .withFragmentShader(rAsIdentifier("post/" + var0))
         .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .build();
   }

   private boolean jAsBoolean() {
      int var1 = RenderSystem.outputColorTextureOverride == null && RenderSystem.outputDepthTextureOverride == null ? 1 : 0;
      if (var1 == 0 && !this.Z) {
         this.Z = true;
         ClientLog.oAsVoid(this.i, "Gradient capture skipped while another renderer owns the output attachments");
      }

      return var1 != 0;
   }

   private boolean wAsBoolean() {
      if (this.a != BloomQuality.J) {
         this.close();
         this.a = BloomQuality.J;
         this.p = false;
      }

      if (!this.p && this.jAsBoolean()) {
         try {
            Framebuffer var1 = MinecraftClient.getInstance().getFramebuffer();
            if (this.X != null && (this.X.textureWidth != var1.textureWidth || this.X.textureHeight != var1.textureHeight)) {
               this.close();
            }

            if (this.X == null) {
               WorldLabelLayout.DAsVoid(o);
               WorldLabelLayout.DAsVoid(k);
               WorldLabelLayout.DAsVoid(N);
               WorldLabelLayout.DAsVoid(C);
               this.r.FAsVoid(var1.textureWidth, var1.textureHeight, 1, BloomFramebuffers.Type_b.MASK_AND_BLUR);
               this.X = new SimpleFramebuffer("Larp " + this.i + " gradient", var1.textureWidth, var1.textureHeight, false);
               this.H = RenderSystem.getDevice().createBuffer(() -> "Larp " + this.i + " gradient parameters", 136, 80L);
               ClientLog.GAsVoid(this.i, "Gradient pass initialized {}x{}; four full-resolution targets", var1.textureWidth, var1.textureHeight);
            }

            return true;
         } catch (RuntimeException var21) {
            this.fAsVoid("allocate/check", var21);
            return false;
         }
      } else {
         return false;
      }
   }

   public void GAsVoid(List<ChamsOutlineEffect.Type_a> var1, MatrixStack var2, bp.Type_e var3, boolean var4) {
      if (var1.isEmpty()) {
         this.close();
      } else if (this.wAsBoolean()) {
         GpuTextureView var5 = RenderSystem.outputColorTextureOverride;
         GpuTextureView var6 = RenderSystem.outputDepthTextureOverride;

         try {
            this.T = true;
            this.V = var4;
            if (SceneRenderResources.yAsGpuTextureView() == null) {
               throw new IllegalStateException("scene-depth capture unavailable");
            }

            if (this.S == null) {
               this.S = new EffectDrawBuffers();
            }

            CommandEncoder var7 = RenderSystem.getDevice().createCommandEncoder();
            var7.clearColorTexture(this.r.u.getColorAttachment(), 0);
            var7.clearDepthTexture(this.r.u.getDepthAttachment(), 1.0);
            RenderSystem.outputColorTextureOverride = this.r.u.getColorAttachmentView();
            RenderSystem.outputDepthTextureOverride = this.r.u.getDepthAttachmentView();

            try {
               for (ChamsOutlineEffect.Type_a var9 : var1) {
                  CosmeticSubmissionHandler.xAsVoid(var9.state, var2, this.S, CosmeticSubmissionHandler.Type_n.CHAMS_SURFACE, var9.color, 1.0, false, false);
               }

               this.S.CAsVoid();
            } finally {
               RenderSystem.outputColorTextureOverride = var5;
               RenderSystem.outputDepthTextureOverride = var6;
            }

            this.wAsVoid(var3);
         } catch (RuntimeException var191) {
            this.fAsVoid("entity mask/composite", var191);
         } finally {
            RenderSystem.outputColorTextureOverride = var5;
            RenderSystem.outputDepthTextureOverride = var6;
            if (this.S != null) {
               this.S.hAsVoid();
            }

            this.T = false;
         }
      }
   }

   public boolean HAsBoolean() {
      this.W = false;
      if (!this.wAsBoolean()) {
         return false;
      } else {
         try {
            if (!this.q) {
               WorldLabelLayout.DAsVoid(g);
               this.q = true;
            }

            BloomQuality.aAsVoid(WorldLabelLayout.u, MinecraftClient.getInstance().getFramebuffer(), this.X);
            this.W = true;
            return true;
         } catch (RuntimeException var2) {
            this.fAsVoid("hand scene capture", var2);
            return false;
         }
      }
   }

   public void oAsVoid(bp.Type_e var1) {
      Framebuffer var2 = this.kAsFramebuffer();
      if (var2 != null) {
         try {
            this.wAsVoid(var1);
         } catch (RuntimeException var4) {
            this.fAsVoid("hand mask/composite", var4);
         }
      }
   }

   public void tAsVoid(List<ChamsOutlineEffect.Type_k> var1) {
      Framebuffer var2 = this.kAsFramebuffer();
      if (var2 != null) {
         long var3 = op.RAsLong();

         try {
            for (ChamsOutlineEffect.Type_k var6 : var1) {
               this.DAsVoid(o, this.r.u, this.X, var6.frame(), 0.0F, 0.0F, var6);
               this.DAsVoid(C, this.X, var2, var6.frame(), 0.0F, 0.0F, var6);
            }

            SceneRenderResources.cAsVoid();
         } catch (RuntimeException var10) {
            this.fAsVoid("hand region composite", var10);
         } finally {
            op.aAsVoid(this.i + " gradient post", var3);
         }
      }
   }

   private Framebuffer kAsFramebuffer() {
      if (!this.W) {
         return null;
      } else {
         this.W = false;
         if (!this.jAsBoolean()) {
            this.close();
            return null;
         } else {
            try {
               Framebuffer var1 = MinecraftClient.getInstance().getFramebuffer();
               if (var1.textureWidth == this.X.textureWidth && var1.textureHeight == this.X.textureHeight) {
                  CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
                  RenderPass var3 = var2.createRenderPass(() -> "Larp " + this.i + " silhouette", this.r.u.getColorAttachmentView(), OptionalInt.empty());

                  try {
                     var3.setPipeline(g);
                     GpuSampler var4 = RenderSystem.getSamplerCache().get(FilterMode.LINEAR);
                     var3.bindTexture("Before", this.X.getColorAttachmentView(), var4);
                     var3.bindTexture("After", var1.getColorAttachmentView(), var4);
                     var3.bindTexture("HandDepth", var1.getDepthAttachmentView(), RenderSystem.getSamplerCache().get(FilterMode.NEAREST));
                     var3.draw(0, 3);
                  } catch (Throwable var71) {
                     if (var3 != null) {
                        try {
                           var3.close();
                        } catch (Throwable var6) {
                           var71.addSuppressed(var6);
                        }
                     }

                     throw var71;
                  }

                  if (var3 != null) {
                     var3.close();
                  }

                  return var1;
               } else {
                  this.close();
                  return null;
               }
            } catch (RuntimeException var81) {
               this.fAsVoid("hand mask", var81);
               return null;
            }
         }
      }
   }

   private void wAsVoid(bp.Type_e var1) {
      long var2 = op.RAsLong();

      try {
         if (this.T) {
            this.oAsVoid(k, this.r.u, this.r.f, var1, 0.0F, 0.0F);
         }

         this.oAsVoid(o, this.T ? this.r.f : this.r.u, this.X, var1, 0.0F, 0.0F);
         if (var1.glowing()) {
            this.oAsVoid(N, this.X, this.r.f, var1, 1.0F, 0.0F);
            this.oAsVoid(N, this.r.f, this.r.y, var1, 0.0F, 1.0F);
         }

         this.oAsVoid(C, this.X, MinecraftClient.getInstance().getFramebuffer(), var1, 0.0F, 0.0F);
         SceneRenderResources.cAsVoid();
      } finally {
         op.aAsVoid(this.i + " gradient post", var2);
      }
   }

   private void oAsVoid(RenderPipeline var1, Framebuffer var2, Framebuffer var3, bp.Type_e var4, float var5, float var6) {
      this.DAsVoid(var1, var2, var3, var4, var5, var6, null);
   }

   private void DAsVoid(RenderPipeline var1, Framebuffer var2, Framebuffer var3, bp.Type_e var4, float var5, float var6, ChamsOutlineEffect.Type_k var7) {
      CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var9 = MemoryStack.stackPush();

      try {
         ByteBuffer var10 = var9.malloc(80);
         var10.putFloat(var4.lineWidth()).putFloat(var4.scale()).putFloat(var4.fill()).putFloat(var4.outline());
         var10.putFloat(var4.entityColor() ? 1.0F : 0.0F).putFloat(var4.phase()).putFloat(var4.radius()).putFloat(var4.glowing() ? var4.intensity() : 0.0F);
         wAsVoid(var10, var4.color1());
         wAsVoid(var10, var4.color2());
         var10.putFloat(var5).putFloat(var6).putFloat(this.V ? 1.0F : 0.0F).putFloat(0.0F).flip();
         var8.writeToBuffer(this.H.slice(), var10);
      } catch (Throwable var171) {
         if (var9 != null) {
            try {
               var9.close();
            } catch (Throwable var16) {
               var171.addSuppressed(var16);
            }
         }

         throw var171;
      }

      if (var9 != null) {
         var9.close();
      }

      RenderPass var18 = var8.createRenderPass(() -> "Larp " + this.i + " " + var1.getLocation(), var3.getColorAttachmentView(), OptionalInt.empty());

      try {
         var18.setPipeline(var1);
         if (var7 != null) {
            int var19 = Math.clamp((long)var7.x(), 0, var3.textureWidth);
            int var11 = Math.clamp((long)var7.y(), 0, var3.textureHeight);
            int var12 = Math.clamp((long)(var7.x() + var7.width()), var19, var3.textureWidth);
            int var13 = Math.clamp((long)(var7.y() + var7.height()), var11, var3.textureHeight);
            var18.enableScissor(var19, var3.textureHeight - var13, var12 - var19, var13 - var11);
         }

         var18.setUniform("ChamsParameters", this.H.slice());
         GpuSampler var20 = RenderSystem.getSamplerCache().get(FilterMode.LINEAR);
         var18.bindTexture("Source", var2.getColorAttachmentView(), var20);
         if (var1 == k) {
            GpuSampler var21 = RenderSystem.getSamplerCache().get(FilterMode.NEAREST);
            var18.bindTexture("Source", var2.getColorAttachmentView(), var21);
            var18.bindTexture("ModelDepth", this.r.u.getDepthAttachmentView(), var21);
            var18.bindTexture("SceneDepth", SceneRenderResources.yAsGpuTextureView(), var21);
         }

         if (var1 == C) {
            var18.bindTexture("Glow", (var4.glowing() ? this.r.y : this.X).getColorAttachmentView(), var20);
         }

         var18.draw(0, 3);
         op.FAsVoid(this.i + " fullscreen draws", 1L);
      } catch (Throwable var18x) {
         if (var18 != null) {
            try {
               var18.close();
            } catch (Throwable var15) {
               var18x.addSuppressed(var15);
            }
         }

         throw var18x;
      }

      if (var18 != null) {
         var18.close();
      }
   }

   private static void wAsVoid(ByteBuffer var0, int var1) {
      var0.putFloat((float)(var1 >> 16 & 0xFF) / 255.0F)
         .putFloat((float)(var1 >> 8 & 0xFF) / 255.0F)
         .putFloat((float)(var1 & 0xFF) / 255.0F)
         .putFloat((float)(var1 >>> 24) / 255.0F);
   }

   private void fAsVoid(String var1, RuntimeException var2) {
      this.p = true;
      this.close();
      ClientLog.lAsVoid(this.i, "stage=" + var1 + "; gradient pass disabled until resource reload; ordinary models retained", var2);
   }

   @Override
   public void close() {
      this.W = false;
      this.q = false;
      SimpleFramebuffer var1 = this.X;
      this.X = null;
      GpuBuffer var2 = this.H;
      this.H = null;
      EffectDrawBuffers var3 = this.S;
      this.S = null;
      RuntimeException var4 = null;

      for (Runnable var8 : new Runnable[]{this.r::close, () -> {
         if (var1 != null) {
            var1.delete();
         }
      }, () -> {
         if (var2 != null) {
            var2.close();
         }
      }, () -> {
         if (var3 != null) {
            var3.close();
         }
      }}) {
         try {
            var8.run();
         } catch (RuntimeException var10) {
            if (var4 == null) {
               var4 = var10;
            } else {
               var4.addSuppressed(var10);
            }
         }
      }

      if (var4 != null) {
         ClientLog.lAsVoid(this.i, "stage=cleanup; all gradient resource releases attempted", var4);
      }
   }

   private static Identifier rAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }

   public static record Type_a(EntityRenderState state, int color) {
   }

   public static record Type_k(int x, int y, int width, int height, bp.Type_e frame) {
   }
}
