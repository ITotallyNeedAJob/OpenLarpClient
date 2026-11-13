package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.MinecraftClient;

public final class SceneRenderResources {
   private static final w6 s = new w6();
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
         g = false;
         y = false;

         try {
            BAsVoid();
         } catch (RuntimeException var1) {
            V = true;
            g = true;
            y = true;
            ClientLog.lAsVoid("Scene resources", "Generation cleanup failed; geometric materials retained until resource reload", var1);
         }
      }

      s.GAsVoid();
      W = false;
      B = false;
      d = false;
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
               c = PAsGpuTexture("Larp depth before translucent terrain", var0);
               u = RenderSystem.getDevice().createTextureView(c);
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
      return W ? u : null;
   }

   public static void cAsVoid() {
      s.OAsVoid();
   }

   public static void YAsVoid() {
      s.lAsVoid();
   }

   public static GpuTextureView gAsGpuTextureView() {
      d = true;
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
                  s.OAsVoid();
               }

               if (var0 != J) {
                  J = var0;
                  s.OAsVoid();
               }

               if (!s.vAsBoolean()) {
                  xAsVoid(var0, M);
                  s.BAsVoid();
                  op.FAsVoid("Scene color copies", 1L);
               }

               return D;
            }
         } catch (RuntimeException var31) {
            y = true;

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
                  j = PAsGpuTexture("Larp shared scene depth", var0);
                  U = RenderSystem.getDevice().createTextureView(j);
                  s.lAsVoid();
               }

               if (var0 != f) {
                  f = var0;
                  s.lAsVoid();
               }

               if (!s.BAsBoolean()) {
                  xAsVoid(var0, j);
                  s.RAsVoid();
                  op.FAsVoid("Scene depth copies", 1L);
               }

               return U;
            }
         } catch (RuntimeException var31) {
            g = true;

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

      s.OAsVoid();
   }

   private static void AAsVoid() {
      GpuTextureView var0 = U;
      GpuTexture var1 = j;
      U = null;
      j = null;
      f = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      s.lAsVoid();
   }

   private static void XAsVoid() {
      GpuTextureView var0 = u;
      GpuTexture var1 = c;
      u = null;
      c = null;
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
            s.zAsVoid();
         }
      }
   }

   private SceneRenderResources() {
   }
}
