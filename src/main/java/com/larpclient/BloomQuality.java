package com.larpclient;

import com.larpclient.module.Chams;
import com.larpclient.module.KillFX;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import org.lwjgl.system.MemoryStack;

public final class BloomQuality {
   public static BloomQuality.Type_w U = BloomQuality.Type_w.Medium;
   public static BloomQuality.Type_w v = BloomQuality.Type_w.Medium;
   private static final BloomFramebuffers L = new BloomFramebuffers();
   private static final BloomFramebuffers z = new BloomFramebuffers();
   private static final BloomFramebuffers DropletTrail = new BloomFramebuffers();
   private static final BloomFramebuffers SmoothInput = new BloomFramebuffers();
   private static boolean N;
   private static boolean m;
   private static long s;
   private static long M = -1L;
   public static int J;
   private static BloomQuality.Type_w i;
   private static BloomQuality.Type_w u;
   public static float h = 0.55F;
   public static float X = 1.0F;
   private static GpuBuffer S;
   private static boolean b;
   private static final List<int[]> ChatPayParser = new ArrayList<>();
   private static float H = 1.0F;
   private static int q;
   private static int BoxUtils = 2;

   public BloomQuality() {
   }

   public static int bAsInt(BloomQuality.Type_w var0) {
      if (var0 == BloomQuality.Type_w.Off) {
         return 0;
      } else {
         int var1 = var0.m * 2 + 1;
         if (var0.ordinal() >= BloomQuality.Type_w.Medium.ordinal()) {
            var1 += BloomQuality.Type_w.Medium.m * 2 + 1;
         }

         if (var0.ordinal() >= BloomQuality.Type_w.High.ordinal()) {
            var1 += var0.m * 2 + 1;
         }

         return var1;
      }
   }

   static int aAsInt(BloomQuality.Type_w var0) {
      return var0 == BloomQuality.Type_w.Off ? 0 : (var0.ordinal() >= BloomQuality.Type_w.High.ordinal() ? 3 : (var0 == BloomQuality.Type_w.Medium ? 2 : 1));
   }

   static float IAsFloat(float var0, int var1) {
      if (var1 >= 1 && Float.isFinite(var0) && !(var0 < 0.0F)) {
         return var0 / (float)var1;
      } else {
         throw new IllegalArgumentException("Invalid blur footprint");
      }
   }

   public static void AAsVoid() {
      BloomUtility = false;
      ChatPayParser.clear();
      iAsVoid(2);
   }

   public static void iAsVoid(int var0) {
      BoxUtils = Math.clamp((long)var0, 0, 4);

      H = switch (c) {
         case 1 -> 0.6F;
         default -> 1.0F;
         case 3 -> 1.4F;
         case 4 -> 1.8F;
      };
      q = BoxUtils >= 3 ? 1 : 0;
   }

   public static boolean cAsBoolean() {
      return v != BloomQuality.Type_w.Off && BoxUtils > 0;
   }

   public static void nAsVoid() {
      if (BoxUtils > 0) {
         BloomUtility = true;
      }
   }

   public static void XAsVoid(int var0, int var1, int var2, int var3) {
      if (BoxUtils > 0 && ChatPayParser.size() < 32) {
         ChatPayParser.add(new int[]{var0, var1, var2, var3});
      }
   }

   public static void iAsVoid() {
      RoundedShapePipeline.EAsVoid();
      HudSdfAtlas.FAsVoid();
      if (b) {
         OAsVoid();
      } else {
         for (int[] var1 : g) {
            PAsVoid(var1[0], var1[1], var1[2], var1[3]);
         }
      }
   }

   public static void FAsVoid() {
      s++;
      if (ChinaHatCodec != U) {
         L.close();
         DropletTrail.close();
         SmoothInput.close();
         ClientLog.GAsVoid("Bloom", "Quality changed: {}", U);
         ChinaHatCodec = U;
      }

      if (CropHelper != v) {
         if (v == BloomQuality.Type_w.Off) {
            z.close();
         }

         ClientLog.XAsVoid("Blur", "Quality: {}", v);
         CropHelper = v;
      }
   }

   private static void sAsVoid() {
      if (!m) {
         for (RenderPipeline var3 : new RenderPipeline[]{WorldLabelLayout.c, WorldLabelLayout.B, WorldLabelLayout.u, WorldLabelLayout.N}) {
            WorldLabelLayout.DAsVoid(var3);
         }

         PriceDetector = true;
         ClientLog.GAsVoid("Shader", "Post pipelines initialized");
      }
   }

   public static void aAsVoid(RenderPipeline var0, Framebuffer var1, Framebuffer var2) {
      BAsVoid(var0, var1, var2, 1.0F);
   }

   private static void BAsVoid(RenderPipeline var0, Framebuffer var1, Framebuffer var2, float var3) {
      int var4 = var0 != WorldLabelLayout.u ? 1 : 0;
      if (var4 != 0) {
         if (S == null) {
            S = RenderSystem.getDevice().createBuffer(() -> "Larp post parameters", 136, 16L);
         }

         MemoryStack var5 = MemoryStack.stackPush();

         try {
            ByteBuffer var6 = var5.malloc(16);
            int var7 = Math.max(1, aAsInt(U));
            var6.putFloat(h / (float)var7).putFloat(var3).putFloat(IAsFloat(var3, var2.textureWidth)).putFloat(IAsFloat(var3, var2.textureHeight)).flip();
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(S.slice(), var6);
         } catch (Throwable var10) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (var5 != null) {
            var5.close();
         }
      }

      RenderPass var12 = RenderSystem.getDevice()
         .createCommandEncoder()
         .createRenderPass(() -> "Larp post " + var0.getLocation(), var2.getColorAttachmentView(), OptionalInt.empty());

      try {
         var12.setPipeline(var0);
         if (var4 != 0) {
            var12.setUniform("PostParameters", S.slice());
         }

         var12.bindTexture("Source", var1.getColorAttachmentView(), RenderSystem.getSamplerCache().get(FilterMode.LINEAR));
         var12.draw(0, 3);
         op.FAsVoid("Post fullscreen draws", 1L);
      } catch (Throwable var111) {
         if (var12 != null) {
            try {
               var12.close();
            } catch (Throwable var81) {
               var111.addSuppressed(var81);
            }
         }

         throw var111;
      }

      if (var12 != null) {
         var12.close();
      }
   }

   public static void ZAsVoid(Framebuffer var0) {
      sAsVoid();
      aAsVoid(WorldLabelLayout.u, MinecraftClient.getInstance().getFramebuffer(), var0);
   }

   private static void gAsVoid(Framebuffer var0, BloomFramebuffers var1, BloomQuality.Type_w var2) {
      dAsVoid(var0, var1, var2, 1.0F);
   }

   private static void dAsVoid(Framebuffer var0, BloomFramebuffers var1, BloomQuality.Type_w var2, float var3) {
      EAsVoid(var0, var1, var2, var3, 0);
   }

   private static void EAsVoid(Framebuffer var0, BloomFramebuffers var1, BloomQuality.Type_w var2, float var3, int var4) {
      BAsVoid(WorldLabelLayout.c, var0, var1.f, var3);
      BAsVoid(WorldLabelLayout.B, var1.f, var1.y, var3);

      for (int var5 = 1; var5 < var2.m + var4; var5++) {
         BAsVoid(WorldLabelLayout.c, var1.y, var1.f, var3);
         BAsVoid(WorldLabelLayout.B, var1.f, var1.y, var3);
      }
   }

   public static Framebuffer rAsFramebuffer() {
      if (!N && U != BloomQuality.Type_w.Off) {
         try {
            sAsVoid();
            Framebuffer var0 = MinecraftClient.getInstance().getFramebuffer();
            L.PAsVoid(var0.textureWidth, var0.textureHeight, U.F, true);
            L.u.copyDepthFrom(var0);
            RenderSystem.getDevice().createCommandEncoder().clearColorTexture(L.u.getColorAttachment(), 0);
            return L.u;
         } catch (RuntimeException var11) {
            IAsVoid("Bloom", var11);
            return null;
         }
      } else {
         return null;
      }
   }

   public static void QAsVoid() {
      if (!N && U != BloomQuality.Type_w.Off && L.u != null) {
         long var0 = op.RAsLong();

         try {
            dAsVoid(L.u, L, U, X);
            Framebuffer var2 = MinecraftClient.getInstance().getFramebuffer();
            aAsVoid(WorldLabelLayout.N, L.y, var2);
            if (U.ordinal() >= BloomQuality.Type_w.Medium.ordinal()) {
               DropletTrail.PAsVoid(var2.textureWidth, var2.textureHeight, Math.max(4, U.F * 2), false);
               dAsVoid(L.y, l, BloomQuality.Type_w.Medium, X);
               aAsVoid(WorldLabelLayout.N, DropletTrail.y, var2);
            }

            if (U.ordinal() >= BloomQuality.Type_w.High.ordinal()) {
               SmoothInput.PAsVoid(var2.textureWidth, var2.textureHeight, Math.max(8, U.F * 4), false);
               dAsVoid(DropletTrail.y, t, U, X);
               aAsVoid(WorldLabelLayout.N, SmoothInput.y, var2);
            }
         } catch (RuntimeException var61) {
            IAsVoid("Bloom", var61);
         } finally {
            op.aAsVoid("Bloom", var0);
         }
      }
   }

   private static boolean EAsBoolean() {
      if (N || v == BloomQuality.Type_w.Off) {
         return false;
      } else if (M == s) {
         return true;
      } else {
         long var0 = op.RAsLong();

         try {
            sAsVoid();
            Framebuffer var2 = MinecraftClient.getInstance().getFramebuffer();
            z.PAsVoid(var2.textureWidth, var2.textureHeight, v.F, false);
            EAsVoid(var2, z, v, H, q);
            M = s;
            return true;
         } catch (RuntimeException var71) {
            IAsVoid("Blur", var71);
         } finally {
            op.aAsVoid("Blur", var0);
         }

         return false;
      }
   }

   private static void OAsVoid() {
      if (EAsBoolean()) {
         try {
            aAsVoid(WorldLabelLayout.u, z.y, MinecraftClient.getInstance().getFramebuffer());
         } catch (RuntimeException var1) {
            IAsVoid("Blur", var1);
         }
      }
   }

   private static void PAsVoid(int var0, int var1, int var2, int var3) {
      if (EAsBoolean()) {
         MinecraftClient var4 = MinecraftClient.getInstance();
         Framebuffer var5 = var4.getFramebuffer();
         int var6 = var4.getWindow().getScaleFactor();

         try {
            RenderPass var7 = RenderSystem.getDevice()
               .createCommandEncoder()
               .createRenderPass(() -> "Larp HUD blur region", var5.getColorAttachmentView(), OptionalInt.empty());

            try {
               var7.setPipeline(WorldLabelLayout.u);
               var7.bindTexture("Source", z.y.getColorAttachmentView(), RenderSystem.getSamplerCache().get(FilterMode.LINEAR));
               var7.enableScissor(
                  Math.max(0, var0 * var6),
                  Math.max(0, var5.textureHeight - (var1 + var3) * var6),
                  Math.max(0, Math.min(var2 * var6, var5.textureWidth - var0 * var6)),
                  Math.max(0, Math.min(var3 * var6, var5.textureHeight - var1 * var6))
               );
               var7.draw(0, 3);
               op.FAsVoid("Post fullscreen draws", 1L);
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
         } catch (RuntimeException var121) {
            IAsVoid("Blur", var121);
         }
      }
   }

   private static void IAsVoid(String var0, RuntimeException var1) {
      N = true;
      ClientLog.lAsVoid(var0, "Post-processing disabled for this session; see shader/compiler diagnostics", var1);
      rAsVoid();
   }

   public static void rAsVoid() {
      L.close();
      DropletTrail.close();
      SmoothInput.close();
      z.close();
      WorldColorPostEffect.YAsVoid();
      MotionBlurEffect.QAsVoid();
      EntityHaloEffect.aAsVoid();
      b8.lAsVoid();
      Chams var0 = Chams.mAsChams();
      if (var0 != null) {
         var0.jAsVoid();
      }

      CosmeticSubmissionHandler.XAsVoid();
      if (S != null) {
         S.close();
         S = null;
      }

      PriceDetector = false;
      M = -1L;
   }

   public static void yAsVoid() {
      J++;
      RenderPerformanceStats.nAsVoid();
      SharedRenderEffects.EAsVoid("Scene resource cleanup", SceneRenderResources::BAsVoid);
      SharedRenderEffects.EAsVoid("Refraction cleanup", RefractionPostEffect::EAsVoid);
      RoundedShapePipeline.qAsVoid();
      HudSdfAtlas.hAsVoid();
      SoftParticlePipeline.yAsVoid();
      SharedRenderEffects.H.rAsVoid();
      KillFX var0 = KillFX.aAsKillFX();
      if (var0 != null) {
         var0.lAsVoid();
      }

      rAsVoid();
      N = false;
      ClientLog.GAsVoid("Shader", "Resources reloaded; post pipelines will be checked on next use");
   }

   public static enum Type_w {
      Off(0, 0),
      Low(4, 1),
      Medium(2, 1),
      High(2, 2),
      Ultra(1, 2);

      final int F;
      final int m;

      private Type_w(int var3, int var4) {
         this.F = var3;
         this.m = var4;
      }
   }
}
