package com.larpclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.MipmapStrategy;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ReloadableTexture;
import net.minecraft.client.texture.TextureContents;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public final class DiscordAvatarRenderer {
   private static final int C = 128;
   private static final int n = 262144;
   private static final int W = 256;
   private static final int X = 512;
   private static final long P = TimeUnit.MINUTES.toNanos(1L);
   private static final AtomicInteger L = new AtomicInteger();
   private final Identifier g = Identifier.of("larp-client", "discord/avatar-" + L.getAndIncrement());
   private volatile String H = "";
   private volatile CrosshairSettings.Type_q G;
   private volatile boolean p;
   private volatile long t;
   private DiscordAvatarRenderer.Type_d Q;
   private TextureManager q;
   private int y;

   public DiscordAvatarRenderer() {
   }

   public boolean CAsBoolean(DrawContext var1, int var2, int var3, int var4) {
      DiscordAvatarClient var5 = DiscordPresenceStatus.current().account();
      if (var5 != null && var4 > 0) {
         String var6 = var5.id() + "/" + var5.avatar();
         if (!var6.equals(this.H)) {
            this.CAsVoid();
            this.H = var6;
         }

         CrosshairSettings.Type_q var7 = this.G;
         if (var7 == null) {
            if (!this.p && System.nanoTime() >= this.t) {
               this.gAsVoid(var5, var6);
            }

            return false;
         } else {
            int var8 = (int)Math.clamp(Math.ceil((double)MinecraftClient.getInstance().getWindow().getScaleFactor()), 1.0, 6.0);
            int var9 = Math.min(256, var4 * var8);
            if (!this.HAsBoolean(var7, var9)) {
               return false;
            } else {
               var1.drawTexture(RenderPipelines.GUI_TEXTURED, this.g, var2, var3, 0.0F, 0.0F, var4, var4, this.y, this.y, this.y, this.y, qg.iAsInt(-1));
               return true;
            }
         }
      } else {
         this.CAsVoid();
         return false;
      }
   }

   public void CAsVoid() {
      if (this.Q != null || !this.H.isEmpty()) {
         if (this.Q != null) {
            MinecraftClient.getInstance().getTextureManager().destroyTexture(this.g);
            this.Q = null;
         }

         this.H = "";
         this.G = null;
         this.y = 0;
         this.t = 0L;
      }
   }

   private boolean HAsBoolean(CrosshairSettings.Type_q var1, int var2) {
      TextureManager var3 = MinecraftClient.getInstance().getTextureManager();
      if (var3 != this.q) {
         this.Q = null;
         this.y = 0;
         this.q = var3;
      }

      if (this.Q != null && this.y == var2) {
         return true;
      } else {
         DiscordAvatarRenderer.Type_d var4 = new DiscordAvatarRenderer.Type_d(this.g, var1, var2);

         try {
            var3.registerTexture(this.g, var4);
         } catch (RuntimeException var6) {
            var4.close();
            this.G = null;
            this.Q = null;
            this.y = 0;
            this.t = System.nanoTime() + P;
            ClientLog.hAsVoid("Discord", "Avatar upload failed; the account card keeps the game head", var6);
            return false;
         }

         this.Q = var4;
         this.y = var2;
         ClientLog.XAsVoid("Discord", "Prepared the linked avatar at {} pixels", var2);
         return true;
      }
   }

   private void gAsVoid(DiscordAvatarClient var1, String var2) {
      this.p = true;
      String var3 = var1.avatarUrl(128);
      Thread.ofPlatform().daemon().name("Larp-Discord-Avatar").start(() -> {
         try {
            byte[] var3x = HttpsJsonClient.gAsByteArray(var3, 262144, "image/png");
            dAsVoid(var3x);
            CrosshairSettings.Type_c var4 = CrosshairSettings.KAsA0_Type_c(new ByteArrayInputStream(var3x));
            if (var2.equals(this.H)) {
               this.G = var4.picture();
            }
         } catch (Exception var81) {
            this.t = System.nanoTime() + P;
            ClientLog.hAsVoid("Discord", "Avatar download failed; the account card keeps the game head", var81);
         } finally {
            this.p = false;
         }
      });
   }

   private static void dAsVoid(byte[] var0) throws IOException {
      if (var0.length < 24) {
         throw new IOException("Avatar response is not a PNG");
      } else {
         ByteBuffer var1 = ByteBuffer.wrap(var0);
         if (var1.getLong() == -8552249625308161526L && var1.getInt() == 13 && var1.getInt() == 1229472850) {
            int var2 = var1.getInt();
            int var3 = var1.getInt();
            if (var2 < 1 || var3 < 1 || var2 > 512 || var3 > 512) {
               throw new IOException("Avatar is " + var2 + "x" + var3 + "; at most 512 per side");
            }
         } else {
            throw new IOException("Avatar response is not a PNG");
         }
      }
   }

   private static int[] nAsIntArray(CrosshairSettings.Type_q var0, int var1) {
      int var2 = var0.width();
      int var3 = var0.height();
      int[] var4 = new int[var1 * var1];

      for (int var5 = 0; var5 < var1; var5++) {
         int var6 = var5 * var3 / var1;
         int var7 = Math.max(var6 + 1, (var5 + 1) * var3 / var1);

         for (int var8 = 0; var8 < var1; var8++) {
            int var9 = var8 * var2 / var1;
            int var10 = Math.max(var9 + 1, (var8 + 1) * var2 / var1);
            long var11 = 0L;
            long var13 = 0L;
            long var15 = 0L;
            long var17 = 0L;
            int var19 = 0;

            for (int var20 = var6; var20 < var7; var20++) {
               for (int var21 = var9; var21 < var10; var21++) {
                  int var22 = var0.pixels()[var20 * var2 + var21];
                  int var23 = var22 >>> 24;
                  var11 += (long)var23;
                  var13 += (long)((var22 >> 16 & 0xFF) * var23);
                  var15 += (long)((var22 >> 8 & 0xFF) * var23);
                  var17 += (long)((var22 & 0xFF) * var23);
                  var19++;
               }
            }

            var4[var5 * var1 + var8] = var11 == 0L
               ? 0
               : (int)(var11 / (long)var19) << 24 | (int)(var13 / var11) << 16 | (int)(var15 / var11) << 8 | (int)(var17 / var11);
         }
      }

      return var4;
   }

   private static final class Type_d extends ReloadableTexture {
      private final CrosshairSettings.Type_q t;
      private final int I;

      Type_d(Identifier var1, CrosshairSettings.Type_q var2, int var3) {
         super(var1);
         this.t = var2;
         this.I = var3;
      }

      public TextureContents loadContents(ResourceManager resourceManager) {
         int[] var2 = DiscordAvatarRenderer.nAsIntArray(this.t, this.I);
         NativeImage var3 = new NativeImage(this.I, this.I, false);

         try {
            double var4 = (double)this.I / 2.0;

            for (int var6 = 0; var6 < this.I; var6++) {
               for (int var7 = 0; var7 < this.I; var7++) {
                  double var8 = var4 - Math.hypot((double)var7 + 0.5 - var4, (double)var6 + 0.5 - var4);
                  int var10 = var2[var6 * this.I + var7];
                  int var11 = (int)Math.round((double)(var10 >>> 24) * Math.clamp(var8 + 0.5, 0.0, 1.0));
                  var3.setColorArgb(var7, var6, var11 << 24 | var10 & 16777215);
               }
            }

            return new TextureContents(var3, new TextureResourceMetadata(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var121) {
            var3.close();
            throw var121;
         }
      }
   }
}
