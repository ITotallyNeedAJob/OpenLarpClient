package com.larpclient;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.MipmapStrategy;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ReloadableTexture;
import net.minecraft.client.texture.TextureContents;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public final class CrosshairSettings {
   public static final Identifier x = Identifier.of("larp-client", "crosshair/crosshair.png");
   static final int E = 4096;
   static final int h = 512;
   static final int m = 16777216;
   private static final CrosshairSettings.Type_p live = new CrosshairSettings.Type_p("live");
   private static final CrosshairSettings.Type_p preview = new CrosshairSettings.Type_p("preview");
   private static CrosshairSettings.Type_q X;
   private static boolean w;
   private static TextureManager n;

   private CrosshairSettings() {
   }

   public static CrosshairSettings.Type_i dAsA0_Type_i(int var0, boolean var1) {
      if (!w) {
         w = true;
         X = AAsA0_Type_q();
      }

      if (X == null) {
         return null;
      } else {
         TextureManager var2 = MinecraftClient.getInstance().getTextureManager();
         if (var2 != n) {
            live.R = preview.R = null;
            live.L = preview.L = -1;
            n = var2;
         }

         CrosshairSettings.Type_p var3 = var1 ? preview : live;
         int var4 = Math.clamp((long)var0, 1, 512);
         int var5 = HAsInt(X, var4);
         if (var3.R == null || var5 != var3.L) {
            CrosshairSettings.Type_h var6 = new CrosshairSettings.Type_h(var3.B, VAsA0_Type_q(X, var4));

            try {
               n.registerTexture(var3.B, var6);
            } catch (RuntimeException var8) {
               var6.close();
               X = null;
               live.NAsVoid(n);
               preview.NAsVoid(n);
               ClientLog.lAsVoid("Crosshair", "Image upload failed; using Star of David until the module is re-enabled", var8);
               return null;
            }

            var3.R = var6;
            var3.L = var5;
            ClientLog.XAsVoid("Crosshair", "Prepared image at {}x{} for {} pixels", var6.g.width(), var6.g.height(), var4);
         }

         return new CrosshairSettings.Type_i(var3.B, var3.R.g.width(), var3.R.g.height());
      }
   }

   public static void yAsVoid() {
      w = false;
      X = null;
      if (live.R != null || preview.R != null) {
         TextureManager var0 = MinecraftClient.getInstance().getTextureManager();
         live.NAsVoid(var0);
         preview.NAsVoid(var0);
      }
   }

   private static CrosshairSettings.Type_q AAsA0_Type_q() {
      Optional var0 = MinecraftClient.getInstance().getResourceManager().getResource(x);
      if (var0.isEmpty()) {
         ClientLog.oAsVoid("Crosshair", "{} is not in the mod; using Star of David", x);
         return null;
      } else {
         try {
            CrosshairSettings.Type_q var3;
            try (InputStream var1 = ((Resource)var0.get()).getInputStream()) {
               CrosshairSettings.Type_c var2 = KAsA0_Type_c(var1);
               ClientLog.GAsVoid("Crosshair", "Loaded {} ({}), kept at {}x{}", x, var2.original(), var2.picture().width(), var2.picture().height());
               var3 = var2.picture();
            }

            return var3;
         } catch (RuntimeException | IOException var61) {
            ClientLog.oAsVoid("Crosshair", "Could not read {}; using Star of David: {}", x, var61.toString());
            return null;
         }
      }
   }

   public static CrosshairSettings.Type_c KAsA0_Type_c(InputStream var0) throws IOException {
      byte[] var1 = var0.readNBytes(16777217);
      if (var1.length > 16777216) {
         throw new IOException("file is larger than 16 MB");
      } else {
         BufferedImage var2;
         try (ImageInputStream var3 = ImageIO.createImageInputStream(new ByteArrayInputStream(var1))) {
            Iterator var4 = ImageIO.getImageReaders(var3);
            if (!var4.hasNext()) {
               throw new IOException("not a PNG image");
            }

            ImageReader var5 = (ImageReader)var4.next();

            try {
               var5.setInput(var3);
               if (!var5.getFormatName().equalsIgnoreCase("png")) {
                  throw new IOException("expected PNG, found " + var5.getFormatName());
               }

               int var6 = var5.getWidth(0);
               int var7 = var5.getHeight(0);
               if (var6 > 4096 || var7 > 4096) {
                  throw new IOException(var6 + "x" + var7 + " is too large; use at most 4096 pixels per side");
               }

               var2 = var5.read(0);
            } finally {
               var5.dispose();
            }
         }

         int var15 = var2.getWidth();
         int var16 = var2.getHeight();
         CrosshairSettings.Type_q var17 = new CrosshairSettings.Type_q(var15, var16, var2.getRGB(0, 0, var15, var16, null, 0, var15));

         while (Math.max(var17.width(), var17.height()) > 512) {
            var17 = bAsA0_Type_q(var17);
         }

         return new CrosshairSettings.Type_c(rAsA0_Type_q(var17), var15 + "x" + var16);
      }
   }

   public static int HAsInt(CrosshairSettings.Type_q var0, int var1) {
      int var2 = 0;
      int var3 = Math.max(var0.width(), var0.height());

      for (int var4 = Math.min(var0.width(), var0.height()); var3 >= var1 * 2 && var4 > 1; var4 = Math.max(1, var4 / 2)) {
         var2++;
         var3 = Math.max(1, var3 / 2);
      }

      return var2;
   }

   public static CrosshairSettings.Type_q VAsA0_Type_q(CrosshairSettings.Type_q var0, int var1) {
      CrosshairSettings.Type_q var2 = var0;

      for (int var3 = HAsInt(var0, var1); var3 > 0; var3--) {
         var2 = bAsA0_Type_q(var2);
      }

      return var2 == var0 ? var0 : rAsA0_Type_q(var2);
   }

   private static CrosshairSettings.Type_q bAsA0_Type_q(CrosshairSettings.Type_q var0) {
      int var1 = Math.max(1, var0.width() / 2);
      int var2 = Math.max(1, var0.height() / 2);
      int[] var3 = new int[var1 * var2];

      for (int var4 = 0; var4 < var2; var4++) {
         for (int var5 = 0; var5 < var1; var5++) {
            long var6 = 0L;
            long var8 = 0L;
            long var10 = 0L;
            long var12 = 0L;

            for (int var14 = 0; var14 < 2; var14++) {
               for (int var15 = 0; var15 < 2; var15++) {
                  int var16 = var0.pixels()[Math.min(var4 * 2 + var14, var0.height() - 1) * var0.width() + Math.min(var5 * 2 + var15, var0.width() - 1)];
                  int var17 = var16 >>> 24;
                  var6 += (long)var17;
                  var8 += (long)((var16 >> 16 & 0xFF) * var17);
                  var10 += (long)((var16 >> 8 & 0xFF) * var17);
                  var12 += (long)((var16 & 0xFF) * var17);
               }
            }

            var3[var4 * var1 + var5] = var6 == 0L
               ? 0
               : (int)Math.round((double)var6 / 4.0) << 24 | (int)(var8 / var6) << 16 | (int)(var10 / var6) << 8 | (int)(var12 / var6);
         }
      }

      return new CrosshairSettings.Type_q(var1, var2, var3);
   }

   private static CrosshairSettings.Type_q rAsA0_Type_q(CrosshairSettings.Type_q var0) {
      int var1 = var0.width();
      int var2 = var0.height();
      int[] var3 = (int[])var0.pixels().clone();

      for (int var4 = 0; var4 < var2; var4++) {
         for (int var5 = 0; var5 < var1; var5++) {
            if (var0.pixels()[var4 * var1 + var5] >>> 24 == 0) {
               for (int var6 = 0; var6 < 4; var6++) {
                  int var7 = var5 + (var6 == 0 ? -1 : (var6 == 1 ? 1 : 0));
                  int var8 = var4 + (var6 == 2 ? -1 : (var6 == 3 ? 1 : 0));
                  if (var7 >= 0 && var8 >= 0 && var7 < var1 && var8 < var2) {
                     int var9 = var0.pixels()[var8 * var1 + var7];
                     if (var9 >>> 24 != 0) {
                        var3[var4 * var1 + var5] = var9 & 16777215;
                        break;
                     }
                  }
               }
            }
         }
      }

      return new CrosshairSettings.Type_q(var1, var2, var3);
   }

   public static record Type_c(CrosshairSettings.Type_q picture, String original) {
   }

   private static final class Type_h extends ReloadableTexture {
      final CrosshairSettings.Type_q g;

      Type_h(Identifier var1, CrosshairSettings.Type_q var2) {
         super(var1);
         this.g = var2;
      }

      public TextureContents loadContents(ResourceManager resourceManager) {
         NativeImage var2 = new NativeImage(this.g.width(), this.g.height(), false);

         try {
            for (int var3 = 0; var3 < this.g.height(); var3++) {
               for (int var4 = 0; var4 < this.g.width(); var4++) {
                  var2.setColorArgb(var4, var3, this.g.pixels()[var3 * this.g.width() + var4]);
               }
            }

            return new TextureContents(var2, new TextureResourceMetadata(true, true, MipmapStrategy.MEAN, 0.0F));
         } catch (RuntimeException var51) {
            var2.close();
            throw var51;
         }
      }
   }

   public static record Type_i(Identifier id, int width, int height) {
   }

   private static final class Type_p {
      final Identifier B;
      CrosshairSettings.Type_h R;
      int L = -1;

      Type_p(String var1) {
         this.B = Identifier.of("larp-client", "crosshair/image-" + var1);
      }

      void NAsVoid(TextureManager var1) {
         if (this.R != null) {
            var1.destroyTexture(this.B);
            this.R = null;
         }

         this.L = -1;
      }
   }

   public static record Type_q(int width, int height, int[] pixels) {
   }
}
