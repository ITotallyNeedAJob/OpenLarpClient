package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.module.LARP;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;

public final class AppearanceAssets {
   private static final ExecutorService u = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-Assets");
      var1.setDaemon(true);
      return var1;
   });
   private static final AtomicLong Z = new AtomicLong();
   private static volatile AppearanceCodec i = AppearanceCodec.EMPTY;
   public static volatile String y = "Local only";
   public static volatile String O = "";
   private static boolean r;
   private static final AtomicBoolean w = new AtomicBoolean();
   private static volatile long h;

   public AppearanceAssets() {
   }

   public static boolean lAsBoolean() {
      return w.get();
   }

   private static Path bAsPath() {
      return MinecraftClient.getInstance().runDirectory.toPath().resolve("config/larp-appearance.json");
   }

   public static AppearanceCodec IAsWh() {
      return i;
   }

   public static void GAsVoid() {
      ClientLog.GAsVoid("Appearance", "Local appearance enabled; multiplayer sharing is removed");
      long var0 = Z.incrementAndGet();
      u.execute(() -> {
         try {
            AppearanceCodec var2 = i;
            if (!r) {
               Path var3 = bAsPath();
               if (Files.exists(var3) && Files.size(var3) <= 360000L) {
                  var2 = AppearanceCodec.parse(JsonParser.parseString(Files.readString(var3)).getAsJsonObject());
               }

               r = true;
            }

            lAsVoid(var2, var0, false);
         } catch (Exception var41) {
            O = "Could not load appearance: " + var41.getMessage();
         }
      });
   }

   public static void hAsVoid() {
      Z.incrementAndGet();
      NotificationQueue.DAsVoid();
      ClientLog.XAsVoid("Appearance", "Local appearance disabled; original textures restored");
   }

   public static void oAsVoid(String var0) {
      long var1 = Z.incrementAndGet();
      u.execute(
         () -> {
            AppearanceCodec var3 = i;
            BAsVoid(
               new AppearanceCodec(
                  !var0.equals("skin") && !var0.equals("all") ? var3.skin() : "",
                  !var0.equals("cape") && !var0.equals("all") ? var3.cape() : "",
                  var3.slim(),
                  !var0.equals("cape") && !var0.equals("all") ? var3.hideCape() : false,
                  !var0.equals("name") && !var0.equals("all") ? var3.name() : "",
                  new JsonObject()
               ),
               var1
            );
         }
      );
   }

   public static void rAsVoid(boolean var0) {
      long var1 = Z.incrementAndGet();
      u.execute(() -> {
         AppearanceCodec var3 = i;
         BAsVoid(new AppearanceCodec(var3.skin(), var3.cape(), var0, var3.hideCape(), var3.name(), new JsonObject()), var1);
      });
   }

   public static void NAsVoid(String var0, boolean var1) {
      long var2 = Z.incrementAndGet();
      O = "Loading PNG...";
      u.execute(
         () -> {
            try {
               Path var4 = Path.of(var0);
               if (Files.size(var4) > 131072L) {
                  throw new IllegalArgumentException("PNG exceeds 128 KiB");
               }

               byte[] var5;
               try (InputStream var6 = Files.newInputStream(var4)) {
                  var5 = var6.readNBytes(131073);
               }

               SkinImageValidation.yAsVoid(var5, var1);
               String var12 = Base64.getEncoder().encodeToString(var5);
               AppearanceCodec var7 = i;
               lAsVoid(
                  new AppearanceCodec(
                     var1 ? var12 : var7.skin(), var1 ? var7.cape() : var12, var7.slim(), var1 && var7.hideCape(), var7.name(), new JsonObject()
                  ),
                  var2,
                  true
               );
            } catch (Exception var111) {
               O = "Load failed: " + var111.getMessage();
            }
         }
      );
   }

   public static void eAsVoid(String var0) {
      if (!var0.matches("[A-Za-z0-9_]{1,16}")) {
         O = "Enter a Minecraft username";
      } else if (System.nanoTime() >= h && w.compareAndSet(false, true)) {
         h = System.nanoTime() + TimeUnit.SECONDS.toNanos(5L);
         long var1 = Z.incrementAndGet();
         O = "Looking up " + var0 + "...";
         u.execute(
            () -> {
               try {
                  JsonObject var3 = HttpsJsonClient.rAsJsonObject("https://api.mojang.com/users/profiles/minecraft/" + var0, 8192);
                  String var4 = var3.get("id").getAsString();
                  if (!var4.matches("[0-9a-fA-F]{32}")) {
                     throw new IllegalArgumentException("Invalid profile");
                  }

                  JsonObject var5 = HttpsJsonClient.rAsJsonObject("https://sessionserver.mojang.com/session/minecraft/profile/" + var4, 32768);
                  JsonObject var6 = null;

                  for (JsonElement var8 : var5.getAsJsonArray("properties")) {
                     JsonObject var9 = var8.getAsJsonObject();
                     if (var9.get("name").getAsString().equals("textures")) {
                        var6 = JsonParser.parseString(new String(Base64.getDecoder().decode(var9.get("value").getAsString()), StandardCharsets.UTF_8))
                           .getAsJsonObject()
                           .getAsJsonObject("textures");
                     }
                  }

                  if (var6 == null || !var6.has("SKIN")) {
                     throw new IllegalArgumentException("No public skin");
                  }

                  JsonObject var15 = var6.getAsJsonObject("SKIN");
                  int var16 = var15.has("metadata") && "slim".equals(var15.getAsJsonObject("metadata").get("model").getAsString()) ? 1 : 0;
                  lAsVoid(
                     new AppearanceCodec(
                        FAsString(var15, true),
                        var6.has("CAPE") ? FAsString(var6.getAsJsonObject("CAPE"), false) : "",
                        var16 != 0,
                        !var6.has("CAPE"),
                        var3.get("name").getAsString(),
                        new JsonObject()
                     ),
                     var1,
                     true
                  );
               } catch (Exception var131) {
                  O = "Import failed: " + var131.getMessage();
               } finally {
                  w.set(false);
               }
            }
         );
      } else {
         O = "Please wait before starting another lookup";
      }
   }

   private static String FAsString(JsonObject var0, boolean var1) throws Exception {
      String var2 = var0.get("url").getAsString().replace("http://", "https://");
      if (!var2.matches("https://textures\\.minecraft\\.net/texture/[a-fA-F0-9]+")) {
         throw new IllegalArgumentException("Untrusted texture URL");
      } else {
         byte[] var3 = HttpsJsonClient.LAsByteArray(var2, 131072);
         SkinImageValidation.yAsVoid(var3, var1);
         return Base64.getEncoder().encodeToString(var3);
      }
   }

   private static void BAsVoid(AppearanceCodec var0, long var1) {
      try {
         lAsVoid(var0, var1, true);
      } catch (Exception var4) {
         O = "Appearance failed: " + var4.getMessage();
      }
   }

   private static void lAsVoid(AppearanceCodec var0, long var1, boolean var3) throws Exception {
      NativeImage var4 = SkinImageValidation.FAsNativeImage(var0.skin(), true);

      NativeImage var5;
      try {
         var5 = SkinImageValidation.FAsNativeImage(var0.cape(), false);
      } catch (Exception var8) {
         if (var4 != null) {
            var4.close();
         }

         throw var8;
      }

      if (var1 != Z.get()) {
         if (var4 != null) {
            var4.close();
         }

         if (var5 != null) {
            var5.close();
         }
      } else {
         try {
            if (var3) {
               Path var6 = bAsPath();
               Files.createDirectories(var6.getParent());
               Path var7 = var6.resolveSibling(var6.getFileName() + ".tmp");
               Files.writeString(var7, var0.json().toString());
               Files.move(var7, var6, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            }
         } catch (Exception var9) {
            if (var4 != null) {
               var4.close();
            }

            if (var5 != null) {
               var5.close();
            }

            throw var9;
         }

         i = var0;
         MinecraftClient.getInstance().execute(() -> {
            if (var1 != Z.get()) {
               if (var4 != null) {
                  var4.close();
               }

               if (var5 != null) {
                  var5.close();
               }
            } else {
               NotificationQueue.DAsVoid();
               LARP var5x = LARP.BAsLARP();
               if (var5x != null && var5x.isOperational()) {
                  NotificationQueue.J = NotificationQueue.aAsNq_Type_i("own", var0, var4, var5);
               } else {
                  if (var4 != null) {
                     var4.close();
                  }

                  if (var5 != null) {
                     var5.close();
                  }
               }

               O = "Appearance saved";
            }
         });
      }
   }
}
