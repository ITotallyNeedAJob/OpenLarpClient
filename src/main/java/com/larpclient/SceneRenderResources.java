package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.MinecraftClient;

public final class SceneRenderResources {
   private static final w6 SkyChunkCache = new w6();
   private static GpuTexture M;
   private static GpuTexture j;
   private static GpuTextureView D;
   private static GpuTextureView U;
   private static GpuTexture J;
   private static GpuTexture f;
   private static GpuTexture c;
   private static GpuTextureView u;
   private static boolean d;
   private static boolean B;
   private static boolean y;
   private static boolean g;
   private static boolean W;
   private static boolean V;
   private static int R = -1;

   public static void gAsVoid() {
      if (R != BloomQuality.J) {
         R = BloomQuality.J;
         V = false;
         ChatPayParser = false;
         ScreenScale = false;

         try {
            BAsVoid();
         } catch (RuntimeException var1) {
            V = true;
            ChatPayParser = true;
            ScreenScale = true;
            ClientLog.lAsVoid("Scene resources", "Generation cleanup failed; geometric materials retained until resource reload", var1);
         }
      }

      SkyChunkCache.GAsVoid();
      W = false;
      B = false;
      SahurDanceEngine = false;
   }

   public static void xAsVoid() {
      if (!V) {
         try {
            GpuTexture var0 = MinecraftClient.getInstance().getFramebuffer().getDepthAttachment();
            if (var0 == null) {
               return;
            }

            if (!jAsBoolean(c, var0)) {
               XAsVoid();
               BoxUtils = PAsGpuTexture("Larp depth before translucent terrain", var0);
               CropHelper = RenderSystem.getDevice().createTextureView(c);
            }

            xAsVoid(var0, c);
            W = true;
            op.FAsVoid("Scene depth copies", 1L);
         } catch (RuntimeException var31) {
            V = true;

            try {
               XAsVoid();
            } catch (RuntimeException var2) {
               var31.addSuppressed(var2);
            }

            ClientLog.lAsVoid("Scene resources", "Depth before translucent terrain failed; local glow stays hidden behind glass until resource reload", var31);
         }
      }
   }

   public static GpuTextureView oAsGpuTextureView() {
      return W ? CropHelper : null;
   }

   public static void cAsVoid() {
      SkyChunkCache.OAsVoid();
   }

   public static void YAsVoid() {
      SkyChunkCache.lAsVoid();
   }

   public static GpuTextureView gAsGpuTextureView() {
      SahurDanceEngine = true;
      if (y) {
         return null;
      } else {
         try {
            GpuTexture var0 = MinecraftClient.getInstance().getFramebuffer().getColorAttachment();
            if (var0 == null) {
               return null;
            } else {
               if (!jAsBoolean(M, var0)) {
                  HAsVoid();
                  M = PAsGpuTexture("Larp shared scene color", var0);
                  D = RenderSystem.getDevice().createTextureView(M);
                  SkyChunkCache.OAsVoid();
               }

               if (var0 != J) {
                  J = var0;
                  SkyChunkCache.OAsVoid();
               }

               if (!SkyChunkCache.vAsBoolean()) {
                  xAsVoid(var0, M);
                  SkyChunkCache.BAsVoid();
                  op.FAsVoid("Scene color copies", 1L);
               }

               return D;
            }
         } catch (RuntimeException var31) {
            ScreenScale = true;

            try {
               HAsVoid();
            } catch (RuntimeException var2) {
               var31.addSuppressed(var2);
            }

            ClientLog.lAsVoid("Scene resources", "Color capture failed; geometric materials retained until resource reload", var31);
            return null;
         }
      }
   }

   public static GpuTextureView yAsGpuTextureView() {
      B = true;
      if (g) {
         return null;
      } else {
         try {
            GpuTexture var0 = MinecraftClient.getInstance().getFramebuffer().getDepthAttachment();
            if (var0 == null) {
               return null;
            } else {
               if (!jAsBoolean(j, var0)) {
                  AAsVoid();
                  TextLayout = PAsGpuTexture("Larp shared scene depth", var0);
                  U = RenderSystem.getDevice().createTextureView(j);
                  SkyChunkCache.lAsVoid();
               }

               if (var0 != f) {
                  MenuPopupProvider = var0;
                  SkyChunkCache.lAsVoid();
               }

               if (!SkyChunkCache.BAsBoolean()) {
                  xAsVoid(var0, j);
                  SkyChunkCache.RAsVoid();
                  op.FAsVoid("Scene depth copies", 1L);
               }

               return U;
            }
         } catch (RuntimeException var31) {
            ChatPayParser = true;

            try {
               AAsVoid();
            } catch (RuntimeException var2) {
               var31.addSuppressed(var2);
            }

            ClientLog.lAsVoid("Scene resources", "Depth capture failed; hard-depth geometry retained until resource reload", var31);
            return null;
         }
      }
   }

   private static boolean jAsBoolean(GpuTexture var0, GpuTexture var1) {
      return var0 != null && var0.getWidth(0) == var1.getWidth(0) && var0.getHeight(0) == var1.getHeight(0) && var0.getFormat() == var1.getFormat();
   }

   private static GpuTexture PAsGpuTexture(String var0, GpuTexture var1) {
      GpuTexture var2 = RenderSystem.getDevice()
         .createTexture(var0, 5 | (var1.getFormat().hasDepthAspect() ? 8 : 0), var1.getFormat(), var1.getWidth(0), var1.getHeight(0), 1, 1);
      ClientLog.XAsVoid("Scene resources", "Allocated {} {}x{}, {} bytes before alignment", var0, var1.getWidth(0), var1.getHeight(0), SAsLong(var2));
      return var2;
   }

   private static void xAsVoid(GpuTexture var0, GpuTexture var1) {
      if (var0 == var1) {
         throw new IllegalStateException("Scene capture aliases output");
      } else {
         RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(var0, var1, 0, 0, 0, 0, 0, var0.getWidth(0), var0.getHeight(0));
      }
   }

   private static long SAsLong(GpuTexture var0) {
      return var0 == null ? 0L : (long)var0.getWidth(0) * (long)var0.getHeight(0) * (long)var0.getFormat().pixelSize();
   }

   public static long xAsLong() {
      return SAsLong(M) + SAsLong(j) + SAsLong(c);
   }

   public static void UAsVoid() {
      try {
         if (!d) {
            HAsVoid();
         }
      } finally {
         try {
            if (!B) {
               AAsVoid();
            }
         } finally {
            if (!W) {
               XAsVoid();
            }
         }
      }

      op.YAsVoid("Scene attachment bytes (estimate)", xAsLong());
   }

   private static void HAsVoid() {
      GpuTextureView var0 = D;
      GpuTexture var1 = M;
      D = null;
      M = null;
      J = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      SkyChunkCache.OAsVoid();
   }

   private static void AAsVoid() {
      GpuTextureView var0 = U;
      GpuTexture var1 = j;
      U = null;
      TextLayout = null;
      MenuPopupProvider = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      SkyChunkCache.lAsVoid();
   }

   private static void XAsVoid() {
      GpuTextureView var0 = u;
      GpuTexture var1 = c;
      CropHelper = null;
      BoxUtils = null;
      W = false;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }
   }

   public static void BAsVoid() {
      try {
         HAsVoid();
      } finally {
         try {
            AAsVoid();
         } finally {
            XAsVoid();
            SkyChunkCache.zAsVoid();
         }
      }
   }

   private SceneRenderResources() {
   }
}
