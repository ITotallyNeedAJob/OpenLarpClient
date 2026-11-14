package com.larpclient;

import com.mojang.blaze3d.textures.GpuTexture;
import java.util.Objects;
import net.minecraft.client.gl.SimpleFramebuffer;

public final class BloomFramebuffers implements AutoCloseable {
   public SimpleFramebuffer u;
   public SimpleFramebuffer f;
   public SimpleFramebuffer y;
   private final BloomFramebuffers.Type_n k;
   private final BloomFramebuffers.Type_d A;
   private BloomFramebuffers.Type_k q;

   private static SimpleFramebuffer FAsSimpleFramebuffer(String var0, int var1, int var2, boolean var3, BloomFramebuffers.Type_d var4) {
      return (SimpleFramebuffer)(var4 == BloomFramebuffers.Type_d.RGBA16_FLOAT ? new CrosshairCodec(var0, var1, var2, var3) : new SimpleFramebuffer(var0, var1, var2, var3));
   }

   public BloomFramebuffers() {
      this(BloomFramebuffers::FAsSimpleFramebuffer);
   }

   public BloomFramebuffers(BloomFramebuffers.Type_d var1) {
      this(BloomFramebuffers::FAsSimpleFramebuffer, var1);
   }

   BloomFramebuffers(BloomFramebuffers.Type_n var1) {
      this(var1, BloomFramebuffers.Type_d.RGBA8);
   }

   private BloomFramebuffers(BloomFramebuffers.Type_n var1, BloomFramebuffers.Type_d var2) {
      this.k = Objects.requireNonNull(var1);
      this.A = Objects.requireNonNull(var2);
   }

   public void PAsVoid(int var1, int var2, int var3, boolean var4) {
      this.FAsVoid(var1, var2, var3, var4 ? BloomFramebuffers.Type_b.MASK_AND_BLUR : BloomFramebuffers.Type_b.BLUR);
   }

   public void FAsVoid(int var1, int var2, int var3, BloomFramebuffers.Type_b var4) {
      BloomFramebuffers.Type_k var5 = new BloomFramebuffers.Type_k(var1, var2, var3, var4);
      if (!var5.equals(this.q)) {
         this.close();

         try {
            if (var4.V) {
               this.u = this.k.jAsSimpleFramebuffer("Larp glow mask", var1, var2, var4 != BloomFramebuffers.Type_b.LOCAL_MASK, this.A);
            }

            if (var4.j) {
               this.f = this.k.jAsSimpleFramebuffer("Larp blur ping", var5.scaledWidth(), var5.scaledHeight(), false, this.A);
            }

            if (var4.h) {
               this.y = this.k.jAsSimpleFramebuffer("Larp blur pong", var5.scaledWidth(), var5.scaledHeight(), false, this.A);
            }

            this.q = var5;
         } catch (RuntimeException var9) {
            try {
               this.close();
            } catch (RuntimeException var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         ClientLog.XAsVoid("Render", "Framebuffers recreated {}x{}, scale=1/{}, mode={}", var1, var2, var3, var4);
      }
   }

   public long hAsLong() {
      long var1 = 0L;

      for (SimpleFramebuffer var6 : new SimpleFramebuffer[]{this.u, this.f, this.y}) {
         if (var6 != null) {
            for (GpuTexture var10 : new GpuTexture[]{var6.getColorAttachment(), var6.getDepthAttachment()}) {
               if (var10 != null) {
                  var1 += (long)var10.getWidth(0)
                     * (long)var10.getHeight(0)
                     * (long)(var6 instanceof CrosshairCodec && var10 == var6.getColorAttachment() ? 8 : var10.getFormat().pixelSize());
               }
            }
         }
      }

      return var1;
   }

   @Override
   public void close() {
      SimpleFramebuffer var1 = this.u;
      SimpleFramebuffer var2 = this.f;
      SimpleFramebuffer var3 = this.y;
      this.u = this.f = this.y = null;
      this.q = null;
      RuntimeException var4 = null;

      for (SimpleFramebuffer var8 : new SimpleFramebuffer[]{var1, var2, var3}) {
         if (var8 != null) {
            try {
               var8.delete();
            } catch (RuntimeException var10) {
               if (var4 == null) {
                  var4 = var10;
               } else {
                  var4.addSuppressed(var10);
               }
            }
         }
      }

      if (var4 != null) {
         throw var4;
      }
   }

   public static enum Type_b {
      COLOR(false, true, false),
      MASK(true, false, false),
      LOCAL_MASK(true, false, false),
      BLUR(false, true, true),
      MASK_AND_BLUR(true, true, true);

      final boolean V;
      final boolean j;
      final boolean h;

      private Type_b(boolean var3, boolean var4, boolean var5) {
         this.V = var3;
         this.j = var4;
         this.h = var5;
      }
   }

   public static enum Type_d {
      RGBA8,
      RGBA16_FLOAT;

      private Type_d() {
      }
   }

   public static record Type_k(int width, int height, int divisor, BloomFramebuffers.Type_b mode) {
      public Type_k(int width, int height, int divisor, BloomFramebuffers.Type_b mode) {
         if (width >= 1 && height >= 1 && divisor >= 1 && mode != null) {
            this.width = width;
            this.height = height;
            this.divisor = divisor;
            this.mode = mode;
         } else {
            throw new IllegalArgumentException("Invalid framebuffer layout");
         }
      }

      public int scaledWidth() {
         return Math.max(1, this.width / this.divisor);
      }

      public int scaledHeight() {
         return Math.max(1, this.height / this.divisor);
      }

      public long colorBytes() {
         return (this.mode.V ? (long)this.width * (long)this.height * 4L : 0L)
            + ((this.mode.j ? 1L : 0L) + (this.mode.h ? 1L : 0L)) * (long)this.scaledWidth() * (long)this.scaledHeight() * 4L;
      }
   }

   @FunctionalInterface
   interface Type_n {
      SimpleFramebuffer jAsSimpleFramebuffer(String var1, int var2, int var3, boolean var4, BloomFramebuffers.Type_d var5);
   }
}
