package com.larpclient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.PlayerSkinTextureDownloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.AssetInfo.TextureAsset;

public final class CapeTextureCache {
   private static final long G = 30000000000L;
   private static final Map<String, CompletableFuture<TextureAsset>> U = new HashMap<>();
   private static final Map<String, Long> P = new HashMap<>();
   private static PlayerSkinTextureDownloader b;

   public static CompletableFuture<TextureAsset> xAsCompletableFuture(CapeCatalog.Type_r var0) {
      CompletableFuture var1 = U.get(var0.hash());
      if (var1 == null || var1.isCompletedExceptionally() && MAsBoolean(var0)) {
         MinecraftClient var2 = MinecraftClient.getInstance();
         if (b == null) {
            b = new PlayerSkinTextureDownloader(var2.getNetworkProxy(), var2.getTextureManager(), var2);
         }

         Identifier var3 = Identifier.of("larp-client", "capes/" + var0.hash());
         Path var4 = var2.runDirectory.toPath().resolve("config/larp-client/capes").resolve(var0.hash() + ".png");
         ClientLog.XAsVoid("Capes", "Loading {} from cache or {}", var0.label(), var0.url());
         CompletableFuture var5 = b.downloadAndRegisterTexture(var3, var4, var0.url(), false).whenCompleteAsync((var2x, var3x) -> {
            if (var3x != null) {
               iAsVoid(var0, var4, var3x);
            }
         }, var2);
         U.put(var0.hash(), var5);
         P.remove(var0.hash());
         return var5;
      } else {
         return var1;
      }
   }

   public static TextureAsset KAsAssetInfo_TextureAsset(CapeCatalog.Type_r var0) {
      CompletableFuture var1 = xAsCompletableFuture(var0);
      return var1.isDone() && !var1.isCompletedExceptionally() ? (TextureAsset)var1.join() : null;
   }

   private static boolean MAsBoolean(CapeCatalog.Type_r var0) {
      Long var1 = P.get(var0.hash());
      return var1 == null || System.nanoTime() - var1 >= 30000000000L;
   }

   private static void iAsVoid(CapeCatalog.Type_r var0, Path var1, Throwable var2) {
      P.put(var0.hash(), System.nanoTime());
      CompletableFuture.runAsync(() -> {
         try {
            Files.deleteIfExists(var1);
         } catch (Exception var1x) {
         }
      }, Util.getDownloadWorkerExecutor().named("larpCapeCache"));
      ClientLog.hAsVoid("Capes", "Download or decode failed for " + var0.label() + "; retrying", var2);
   }

   private CapeTextureCache() {
   }
}
