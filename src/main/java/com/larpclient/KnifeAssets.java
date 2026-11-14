package com.larpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map.Entry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

public final class KnifeAssets {
   private static final String o = "/assets/larp-client/knives/";
   static final String[] Y = new String[]{"galaxy_clouds", "galaxy_glow", "galaxy_stars"};
   private static final EnumMap<KnifeType, KnifeMesh> e = new EnumMap<>(KnifeType.class);
   private static boolean A;
   private static KnifeMesh S;

   public static Identifier nAsIdentifier(KnifeType var0) {
      return Identifier.of("larp-client", "dynamic/knife_" + var0.q);
   }

   public static Identifier kAsIdentifier(int var0) {
      return Identifier.of("larp-client", "dynamic/knife_" + Y[var0]);
   }

   public static KnifeMesh lAsDe(KnifeType var0) {
      return A ? e.get(var0) : null;
   }

   public static KnifeMesh RAsDe(KnifeType var0, boolean var1) {
      return A && var0 == KnifeType.WATCH && var1 ? S : lAsDe(var0);
   }

   public static void LAsVoid() {
      A = false;
      e.clear();
      S = null;

      for (KnifeType var3 : KnifeType.values()) {
         try {
            e.put(var3, KnifeMesh.read(KnifeAssets.class.getResourceAsStream("/assets/larp-client/knives/" + var3.q + ".bin")));
         } catch (IOException var14) {
            ClientLog.lAsVoid("SwordModels", "Cannot load " + var3.q + "; retaining vanilla sword", var14);
         }
      }

      try {
         S = KnifeMesh.read(KnifeAssets.class.getResourceAsStream("/assets/larp-client/knives/dress_watch_slim.bin")).sharing(e.get(KnifeType.WATCH), "case");
      } catch (IOException var13) {
         e.remove(KnifeType.WATCH);
         ClientLog.lAsVoid("SwordModels", "Cannot load slim watch; retaining vanilla sword", var13);
      }

      try {
         for (int var20 = 0; var20 < Y.length; var20++) {
            String var23 = Y[var20];

            try (InputStream var26 = KnifeAssets.class.getResourceAsStream("/assets/larp-client/knives/" + var23 + ".png")) {
               if (var26 == null) {
                  throw new IOException("Missing " + var23 + " texture");
               }

               NativeImage var29 = NativeImage.read(var26);

               NativeImageBackedTexture var4;
               try {
                  var4 = new NativeImageBackedTexture(() -> "LARP knife " + var23, var29);
               } catch (RuntimeException var12) {
                  var29.close();
                  throw var12;
               }

               try {
                  MinecraftClient.getInstance().getTextureManager().registerTexture(kAsIdentifier(var20), var4);
               } catch (RuntimeException var11) {
                  var4.close();
                  throw var11;
               }
            }
         }

         ClientLog.XAsVoid("SwordModels", "Galaxy blade layers loaded: {}", String.join(", ", Y));
      } catch (RuntimeException | IOException var19) {
         e.keySet().removeIf(var0 -> !var0.XAsBoolean());
         ClientLog.lAsVoid("SwordModels", "Cannot load the galaxy blade finish; using vanilla swords for coated knives", var19);
      }

      for (KnifeType var30 : KnifeType.values()) {
         try (InputStream var31 = KnifeAssets.class.getResourceAsStream("/assets/larp-client/knives/" + var30.q + ".png")) {
            if (var31 == null) {
               throw new IOException("Missing knife texture");
            }

            NativeImage var5 = NativeImage.read(var31);

            NativeImageBackedTexture var6;
            try {
               KnifeMesh var7 = e.get(var30);
               if (var7 != null && !var30.XAsBoolean()) {
                  e.put(var30, var7.shaded(var5.getWidth(), var5.getHeight(), var5::getColorArgb));
               }

               var6 = new NativeImageBackedTexture(() -> "LARP " + var30.q, var5);
            } catch (RuntimeException var15) {
               var5.close();
               throw var15;
            }

            try {
               MinecraftClient.getInstance().getTextureManager().registerTexture(nAsIdentifier(var30), var6);
            } catch (RuntimeException var91) {
               var6.close();
               throw var91;
            }
         } catch (RuntimeException | IOException var171) {
            e.remove(var30);
            ClientLog.lAsVoid("SwordModels", "Cannot load " + var30.q + " texture; using vanilla sword", var171);
         }
      }

      A = true;
      if (!e.containsKey(KnifeType.WATCH)) {
         S = null;
      }

      for (Entry var25 : e.entrySet()) {
         if (!((KnifeType)var25.getKey()).XAsBoolean()) {
            int var28 = ((KnifeMesh)var25.getValue()).parts().stream().mapToInt(var0 -> var0.vertices().length / 8).sum();
            ClientLog.XAsVoid(
               "SwordModels",
               "{}: {} vertices, native palm fit Y={} Z={}, cached bevel shading",
               ((KnifeType)var25.getKey()).X,
               var28,
               BuildHelper.mAsFloat((KnifeType)var25.getKey()),
               BuildHelper.SAsFloat((KnifeType)var25.getKey())
            );
         }
      }

      ClientLog.GAsVoid("SwordModels", "Loaded {} sword models and textures", e.size());
   }

   private KnifeAssets() {
   }
}
