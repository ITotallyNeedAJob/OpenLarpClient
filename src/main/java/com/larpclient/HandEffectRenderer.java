package com.larpclient;

import com.larpclient.module.EchoHand;
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
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.nio.ByteBuffer;
import java.util.OptionalInt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.system.MemoryStack;

public final class HandEffectRenderer {
   private static final RenderPipeline MenuPopupProvider = mAsRenderPipeline("echo_hand", false);
   private static final RenderPipeline S = mAsRenderPipeline("echo_hand", true);
   private static final RenderPipeline v = mAsRenderPipeline("hand_flame", false);
   private static final RenderPipeline G = mAsRenderPipeline("hand_flame", true);
   private static SimpleFramebuffer I;
   private static SimpleFramebuffer d;
   private static SimpleFramebuffer t;
   private static SimpleFramebuffer F;
   private static GpuBuffer w;
   private static int AuctionNotifier = -1;
   private static boolean V;
   private static boolean m;
   private static Object i;
   private static Object z;
   private static String b;
   private static double u;
   private static double W;
   private static double p;
   private static final HandEffectRenderer.Type_a E = new HandEffectRenderer.Type_a();

   public HandEffectRenderer() {
   }

   private static RenderPipeline mAsRenderPipeline(String var0, boolean var1) {
      Builder var2 = RenderPipeline.builder(new Snippet[0]);

      for (int var3 = 0; var3 < 5; var3++) {
         var2.withSampler("Sampler" + var3);
      }

      var2.withUniform("EchoParameters", UniformType.UNIFORM_BUFFER);
      if (var1) {
         var2.withBlend(BlendFunction.LIGHTNING);
      }

      return var2.withLocation(eAsIdentifier("core/" + var0 + (var1 ? "_composite" : "_history")))
         .withVertexShader(eAsIdentifier("core/echo_hand"))
         .withFragmentShader(eAsIdentifier("core/" + var0))
         .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .build();
   }

   public static boolean TAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      EchoHand var1 = EchoHand.OAsEchoHand();
      return var1 != null
         && var1.isRuntimeActive()
         && var0.world != null
         && var0.player != null
         && var0.options.getPerspective().isFirstPerson()
         && !var0.options.hudHidden
         && !var0.player.isSleeping()
         && !var0.player.isSpectator()
         && (var1.oAsBoolean() || var1.echoTrail.getValue())
         && (!var1.oAsBoolean() || !var1.onlyItems.getValue() || !var0.player.getMainHandStack().isEmpty() || !var0.player.getOffHandStack().isEmpty());
   }

   public static void hAsVoid() {
      if (!TAsBoolean()) {
         KAsVoid();
      } else if (ChinaHatCodec != null && (ChinaHatCodec != MinecraftClient.getInstance().world || z != MinecraftClient.getInstance().player)) {
         KAsVoid();
      }
   }

   public static boolean fAsBoolean() {
      if (AuctionNotifier != BloomQuality.J) {
         KAsVoid();
         AuctionNotifier = BloomQuality.J;
         V = false;
      }

      if (!TAsBoolean()) {
         KAsVoid();
         return false;
      } else if (V) {
         return false;
      } else {
         try {
            MinecraftClient var0 = MinecraftClient.getInstance();
            EchoHand var1 = EchoHand.OAsEchoHand();
            Framebuffer var2 = var0.getFramebuffer();
            if (RenderSystem.outputColorTextureOverride == null && RenderSystem.outputDepthTextureOverride == null) {
               int var3 = I != null
                     && I.textureWidth == var2.textureWidth
                     && I.textureHeight == var2.textureHeight
                     && ChinaHatCodec == var0.world
                     && z == var0.player
                     && var1.mode.getValue().equals(b)
                     && !(FAsDouble(var0.player.getX(), var0.player.getY(), var0.player.getZ(), u, W, p) > 256.0)
                  ? 0
                  : 1;
               if (var3 != 0) {
                  KAsVoid();
                  mAsVoid(var2.textureWidth, var2.textureHeight);
                  ChinaHatCodec = var0.world;
                  z = var0.player;
                  BloomUtility = var1.mode.getValue();
               }

               CropHelper = var0.player.getX();
               W = var0.player.getY();
               HexEdgeloop = var0.player.getZ();
               if (!var0.isPaused()) {
                  E.MAsVoid(var0.player.getYaw(), var0.player.getPitch(), var1.oAsBoolean() ? 1.0F : var1.R.getValue().floatValue());
               }

               BloomQuality.aAsVoid(WorldLabelLayout.u, var2, I);
               I.copyDepthFrom(var2);
               PriceDetector = true;
               return true;
            } else {
               KAsVoid();
               return false;
            }
         } catch (RuntimeException var41) {
            YAsVoid("capture", var41);
            return false;
         }
      }
   }

   public static void cAsVoid() {
      if (m) {
         PriceDetector = false;

         try {
            EchoHand var0 = EchoHand.OAsEchoHand();
            Framebuffer var1 = MinecraftClient.getInstance().getFramebuffer();
            BloomQuality.aAsVoid(WorldLabelLayout.u, var1, d);
            SahurDanceEngine.copyDepthFrom(var1);
            if (!MinecraftClient.getInstance().isPaused()) {
               GAsVoid(var0.oAsBoolean() ? v : f, F, var0, 0.0F);
               SimpleFramebuffer var2 = t;
               SmoothInput = F;
               F = var2;
            }

            GAsVoid(var0.oAsBoolean() ? G : S, var1, var0, 1.0F);
         } catch (RuntimeException var31) {
            YAsVoid("history/composite", var31);
         }
      }
   }

   public static void lAsVoid() {
      PriceDetector = false;
      KAsVoid();
   }

   private static void mAsVoid(int var0, int var1) {
      try {
         WorldLabelLayout.DAsVoid(f);
         WorldLabelLayout.DAsVoid(S);
         WorldLabelLayout.DAsVoid(v);
         WorldLabelLayout.DAsVoid(G);
         I = new SimpleFramebuffer("Larp EchoHand before", var0, var1, true);
         SahurDanceEngine = new SimpleFramebuffer("Larp EchoHand after", var0, var1, true);
         SmoothInput = new SimpleFramebuffer("Larp EchoHand history", var0, var1, false);
         F = new SimpleFramebuffer("Larp EchoHand next", var0, var1, false);
         w = RenderSystem.getDevice().createBuffer(() -> "Larp EchoHand material", 136, 96L);
         CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
         var2.clearColorTexture(SmoothInput.getColorAttachment(), 0);
         var2.clearColorTexture(F.getColorAttachment(), 0);
         ClientLog.GAsVoid("EchoHand", "History initialized {}x{}; four targets, both hands/items", var0, var1);
      } catch (RuntimeException var31) {
         KAsVoid();
         throw var31;
      }
   }

   private static void GAsVoid(RenderPipeline var0, Framebuffer var1, EchoHand var2, float var3) {
      CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var5 = MemoryStack.stackPush();

      try {
         ByteBuffer var6 = var5.malloc(96);
         var6.putFloat((float)I.textureWidth)
            .putFloat((float)I.textureHeight)
            .putFloat((var2.oAsBoolean() ? var2.k : var2.a).getValue().floatValue())
            .putFloat(var2.K.getValue().floatValue());
         var6.putFloat(var2.E.getValue().floatValue()).putFloat(var2.R.getValue().floatValue()).putFloat(var3).putFloat((float)mb.RAsDouble());
         var6.putFloat(E.z).putFloat(E.h).putFloat(var2.outsideOnly.getValue() ? 1.0F : 0.0F).putFloat(0.0F);
         int var7 = var2.LAsInt();
         var6.putFloat((float)(var7 >> 16 & 0xFF) / 255.0F)
            .putFloat((float)(var7 >> 8 & 0xFF) / 255.0F)
            .putFloat((float)(var7 & 0xFF) / 255.0F)
            .putFloat((float)(var7 >>> 24) / 255.0F);
         var6.putFloat(var2.b.getValue().floatValue())
            .putFloat(var2.B.getValue().floatValue())
            .putFloat(var2.c.getValue().floatValue())
            .putFloat(var2.r.getValue().floatValue());
         var6.putFloat(var2.o.getValue().floatValue()).putFloat(var2.y.getValue().floatValue()).putFloat(var2.S.getValue().floatValue()).putFloat(0.0F).flip();
         var4.writeToBuffer(w.slice(), var6);
      } catch (Throwable var121) {
         if (var5 != null) {
            try {
               var5.close();
            } catch (Throwable var101) {
               var121.addSuppressed(var101);
            }
         }

         throw var121;
      }

      if (var5 != null) {
         var5.close();
      }

      RenderPass var12 = var4.createRenderPass(() -> "Larp EchoHand history/composite", var1.getColorAttachmentView(), OptionalInt.empty());

      try {
         var12.setPipeline(var0);
         var12.setUniform("EchoParameters", w.slice());
         GpuSampler var13 = RenderSystem.getSamplerCache().get(FilterMode.LINEAR);
         var12.bindTexture("Sampler0", SahurDanceEngine.getColorAttachmentView(), var13);
         var12.bindTexture("Sampler1", I.getColorAttachmentView(), var13);
         var12.bindTexture("Sampler2", SmoothInput.getColorAttachmentView(), var13);
         GpuSampler var14 = RenderSystem.getSamplerCache().get(FilterMode.NEAREST);
         var12.bindTexture("Sampler3", SahurDanceEngine.getDepthAttachmentView(), var14);
         var12.bindTexture("Sampler4", I.getDepthAttachmentView(), var14);
         var12.draw(0, 3);
      } catch (Throwable var11) {
         if (var12 != null) {
            try {
               var12.close();
            } catch (Throwable var9) {
               var11.addSuppressed(var9);
            }
         }

         throw var11;
      }

      if (var12 != null) {
         var12.close();
      }
   }

   private static void YAsVoid(String var0, RuntimeException var1) {
      V = true;
      KAsVoid();
      ClientLog.lAsVoid("EchoHand", "stage=" + var0 + "; history disabled until resource reload", var1);
   }

   public static void KAsVoid() {
      SimpleFramebuffer[] var0 = new SimpleFramebuffer[]{I, d, t, F};
      F = null;
      SmoothInput = null;
      SahurDanceEngine = null;
      I = null;
      GpuBuffer var1 = w;
      w = null;
      PriceDetector = false;
      z = null;
      ChinaHatCodec = null;
      BloomUtility = null;
      E.DAsVoid();
      RuntimeException var2 = null;

      for (SimpleFramebuffer var6 : var0) {
         if (var6 != null) {
            try {
               var6.delete();
            } catch (RuntimeException var9) {
               if (var2 == null) {
                  var2 = var9;
               } else {
                  var2.addSuppressed(var9);
               }
            }
         }
      }

      if (var1 != null) {
         try {
            var1.close();
         } catch (RuntimeException var8) {
            if (var2 == null) {
               var2 = var8;
            } else {
               var2.addSuppressed(var8);
            }
         }
      }

      if (var2 != null) {
         ClientLog.lAsVoid("EchoHand", "stage=cleanup; attachments released", var2);
      }
   }

   public static double FAsDouble(double var0, double var2, double var4, double var6, double var8, double var10) {
      double var12 = var0 - var6;
      double var14 = var2 - var8;
      double var16 = var4 - var10;
      return var12 * var12 + var14 * var14 + var16 * var16;
   }

   private static Identifier eAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }

   public static final class Type_a {
      public float z;
      public float h;
      private float N;
      private float U;
      private float R;
      private float H;
      private boolean q;

      public Type_a() {
      }

      public void MAsVoid(float var1, float var2, float var3) {
         if (!this.q) {
            this.N = var1;
            this.U = var2;
            this.q = true;
         } else {
            float var4 = MathHelper.wrapDegrees(var1 - this.N);
            float var5 = var2 - this.U;
            this.N = var1;
            this.U = var2;
            float var6 = MathHelper.clamp(var4 * 0.002F * var3, -0.065F, 0.065F);
            float var7 = MathHelper.clamp(-var5 * 0.002F * var3, -0.065F, 0.065F);
            this.R = (this.R + (var6 - this.z) * 0.18F) * 0.74F;
            this.H = (this.H + (var7 - this.h) * 0.18F) * 0.74F;
            this.z = MathHelper.clamp((this.z + this.R) * 0.92F, -0.075F, 0.075F);
            this.h = MathHelper.clamp((this.h + this.H) * 0.92F, -0.075F, 0.075F);
         }
      }

      public void DAsVoid() {
         this.q = false;
         this.z = this.h = this.N = this.U = this.R = this.H = 0.0F;
      }
   }
}
