package com.larpclient.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.ClientLog;
import com.larpclient.HttpsJsonClient;
import com.larpclient.SkinImageValidation;
import com.larpclient.mixin.PlayerSkinTextureDownloaderInvoker;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.AssetInfo.TextureAsset;
import net.minecraft.util.AssetInfo.TextureAssetInfo;

public final class SkinChanger extends Module {
   private static SkinChanger y;
   private static final ExecutorService C = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-SkinChanger");
      var1.setDaemon(true);
      return var1;
   });
   private final ModeSetting skin = this.addSetting(new ModeSetting("Skin", "Choose a PNG from the skins folder", "None", "None", "Default") {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(var1) || SkinChanger.eAsBoolean(var1);
      }

      @Override
      public void setValue(String var1) {
         if (!this.getModes().contains(var1) && SkinChanger.eAsBoolean(var1)) {
            ArrayList var2 = new ArrayList<>(this.getModes());
            var2.add(var1);
            this.oAsVoid(var2);
         }

         super.setValue(var1);
      }
   });
   private final ModeSetting model = this.addSetting(new ModeSetting("Model", "Auto uses the copied player's arm type", "Auto", "Auto", "Wide", "Slim"));
   private final TextSetting copyPlayer = this.addSetting(new TextSetting("Copy Player", "Minecraft username; leave empty to use the selected skin", ""));
   private final AtomicLong T = new AtomicLong();
   private Identifier a;
   private boolean l;
   private volatile boolean O;
   private volatile String F = "Select a skin or enter a username";
   private String i = "";
   private String f = "";
   private long g;
   private long h;
   private String Q = "";
   private boolean L;
   private boolean s;

   public SkinChanger() {
      super("Skin Changer", "Use a custom or copied skin locally", Category.VISUALS, -1);
      y = this;
      this.skin.visibleWhen(() -> this.copyPlayer.getValue().isBlank());
      this.model.visibleWhen(() -> !this.skin.pAsBoolean("None") || !this.copyPlayer.getValue().isBlank());
      this.skin.onChanged(var1 -> {
         this.h = 0L;
         if (this.OAsBoolean()) {
            this.KAsVoid();
         }
      });
      this.copyPlayer.onChanged(var1 -> {
         this.T.incrementAndGet();
         this.L = true;
         this.h = 0L;
         this.i = var1.strip();
         this.g = System.nanoTime();
      });
   }

   public static SkinChanger pAsSkinChanger() {
      return y;
   }

   private Path vAsPath() {
      return mc.runDirectory.toPath().resolve("config/larp-client/skins");
   }

   @Override
   public String getStatus() {
      return this.F;
   }

   public void YAsVoid() {
      Path var1 = this.vAsPath();
      C.execute(() -> {
         try {
            Files.createDirectories(var1);
            mc.execute(() -> Util.getOperatingSystem().open(var1));
         } catch (Exception var3) {
            this.F = "Could not open skins folder";
            ClientLog.lAsVoid("SkinChanger", "Folder stage failed", var3);
         }
      });
   }

   public void fAsVoid() {
      Path var1 = this.vAsPath();
      C.execute(
         () -> {
            try {
               Files.createDirectories(var1);

               List var2;
               try (Stream<Path> var3 = Files.list(var1)) {
                  var2 = var3.filter(var0 -> Files.isRegularFile(var0) && var0.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".png"))
                     .map(var0 -> var0.getFileName().toString())
                     .filter(var0 -> var0.length() <= 128)
                     .sorted(String.CASE_INSENSITIVE_ORDER)
                     .limit(256L)
                     .toList();
               }

               ArrayList var9 = new ArrayList<>(List.of("None", "Default"));
               var9.addAll(var2);
               mc.execute(() -> this.skin.oAsVoid(var9));
            } catch (Exception var81) {
               this.F = "Could not read skins folder";
               ClientLog.lAsVoid("SkinChanger", "Library stage failed", var81);
            }
         }
      );
   }

   @Override
   public void onEnable() {
      this.f = "";
      this.h = 0L;
      this.fAsVoid();
      this.KAsVoid();
   }

   @Override
   public void onDisable() {
      this.T.incrementAndGet();
      this.tAsVoid();
      this.f = this.Q = "";
      this.F = "Original skin restored";
      ClientLog.GAsVoid("SkinChanger", "Disabled; local texture released");
   }

   @Override
   public void onTick() {
      String var1 = this.copyPlayer.getValue().strip();
      long var2 = System.nanoTime();
      if (!var1.equals(this.i)) {
         this.i = var1;
         this.g = var2;
      }

      if (!this.O && (this.L || !var1.equals(this.f) || !this.skin.getValue().equals(this.Q)) && var2 - this.g >= 600000000L && var2 >= this.h) {
         this.KAsVoid();
      }
   }

   public void KAsVoid() {
      if (this.isEnabled() && this.OAsBoolean()) {
         if (this.O) {
            this.T.incrementAndGet();
            this.L = true;
         } else {
            this.L = false;
            String var1 = this.copyPlayer.getValue().strip();
            String var2 = this.skin.getValue();
            if (!var1.isEmpty() && !lAsBoolean(var1)) {
               this.F = "Enter a Minecraft username (1 to 16 characters)";
            } else if (!var1.isEmpty() || !var2.equals("None") && !var2.equals("Default")) {
               long var3 = this.T.incrementAndGet();
               this.O = true;
               this.F = var1.isEmpty() ? "Loading " + var2 : "Looking up " + var1 + "...";
               Path var5 = this.vAsPath();
               C.execute(
                  () -> {
                     NativeImage var6 = null;
                     boolean var21 = false /* VF: Semaphore variable */;

                     label177: {
                        try {
                           var21 = true;
                           byte var23 = 0;
                           byte[] var7x;
                           if (var1.isEmpty()) {
                              Path var25 = var5.resolve(var2).normalize();
                              if (!var25.getParent().equals(var5.normalize()) || !var2.toLowerCase(Locale.ROOT).endsWith(".png")) {
                                 throw new IllegalArgumentException("Choose a PNG from the skins folder");
                              }

                              try (InputStream var27 = Files.newInputStream(var25)) {
                                 var7x = var27.readNBytes(131073);
                              }
                           } else {
                              JsonObject var9 = HttpsJsonClient.rAsJsonObject("https://api.mojang.com/users/profiles/minecraft/" + var1, 8192);
                              String var10 = var9.get("id").getAsString();
                              if (!var10.matches("[a-fA-F0-9]{32}")) {
                                 throw new IllegalArgumentException("Invalid player profile");
                              }

                              JsonObject var11 = HttpsJsonClient.rAsJsonObject("https://sessionserver.mojang.com/session/minecraft/profile/" + var10, 32768);
                              JsonObject var12 = null;

                              for (JsonElement var14 : var11.getAsJsonArray("properties")) {
                                 JsonObject var15 = var14.getAsJsonObject();
                                 if (var15.get("name").getAsString().equals("textures")) {
                                    var12 = JsonParser.parseString(
                                          new String(Base64.getDecoder().decode(var15.get("value").getAsString()), StandardCharsets.UTF_8)
                                       )
                                       .getAsJsonObject()
                                       .getAsJsonObject("textures")
                                       .getAsJsonObject("SKIN");
                                 }
                              }

                              if (var12 == null) {
                                 throw new IllegalArgumentException("Player has no public skin");
                              }

                              String var29 = var12.get("url").getAsString().replace("http://", "https://");
                              if (!var29.matches("https://textures\\.minecraft\\.net/texture/[a-fA-F0-9]+")) {
                                 throw new IllegalArgumentException("Unexpected texture URL");
                              }

                              var23 = (byte)(var12.has("metadata") && "slim".equals(var12.getAsJsonObject("metadata").get("model").getAsString()) ? 1 : 0);
                              var7x = HttpsJsonClient.LAsByteArray(var29, 131072);
                           }

                           SAsVoid(var7x);
                           var6 = NativeImage.read(new ByteArrayInputStream(var7x));
                           var6 = PlayerSkinTextureDownloaderInvoker.larpProcessSkin(var6, "Skin Changer");
                           NativeImage var26 = var6;
                           var6 = null;
                           byte var28 = var23;
                           mc.execute(() -> {
                              if (var3 == this.T.get() && this.OAsBoolean()) {
                                 try {
                                    Identifier var7x_r2 = SkinImageValidation.JAsIdentifier("skin-changer-" + var3, var26);
                                    this.tAsVoid();
                                    this.a = var7x_r2;
                                    this.l = var28 != 0;
                                    this.f = var1;
                                    this.Q = var2;
                                    this.F = var1.isEmpty() ? var2 : "Copied " + var1;
                                    ClientLog.GAsVoid("SkinChanger", "Skin applied: {}, model={}", this.F, this.model.getValue());
                                 } catch (RuntimeException var8x) {
                                    var26.close();
                                    this.F = "Texture registration failed";
                                    ClientLog.lAsVoid("SkinChanger", "GPU registration failed; previous skin retained", var8x);
                                 }
                              } else {
                                 var26.close();
                              }
                           });
                           var21 = false;
                           break label177;
                        } catch (Exception var24) {
                           mc.execute(() -> {
                              if (var3 == this.T.get() && this.OAsBoolean()) {
                                 this.F = "Skin failed: " + var24.getMessage();
                                 this.h = System.nanoTime() + 30000000000L;
                                 ClientLog.lAsVoid("SkinChanger", "Resolve/decode failed; original or previous skin retained", var24);
                              }
                           });
                           var21 = false;
                        } finally {
                           if (var21) {
                              if (var6 != null) {
                                 var6.close();
                              }

                              mc.execute(() -> this.O = false);
                           }
                        }

                        if (var6 != null) {
                           var6.close();
                        }

                        mc.execute(() -> this.O = false);
                        return;
                     }

                     mc.execute(() -> this.O = false);
                  }
               );
            } else {
               this.T.incrementAndGet();
               this.tAsVoid();
               this.f = var1;
               this.Q = var2;
               this.F = var2.equals("Default") ? "Default Minecraft skin" : "Original skin";
            }
         }
      }
   }

   public static boolean eAsBoolean(String var0) {
      return var0 != null
         && var0.length() <= 128
         && var0.length() > 4
         && !var0.contains("/")
         && !var0.contains("\\")
         && var0.codePoints().noneMatch(Character::isISOControl)
         && var0.toLowerCase(Locale.ROOT).endsWith(".png");
   }

   public static boolean lAsBoolean(String var0) {
      return var0 != null && var0.matches("[A-Za-z0-9_]{1,16}");
   }

   public static void SAsVoid(byte[] var0) {
      if (var0.length >= 33 && var0.length <= 131072) {
         ByteBuffer var1 = ByteBuffer.wrap(var0);
         if (var1.getLong() == -8552249625308161526L && var1.getInt() == 13 && var1.getInt() == 1229472850) {
            int var2 = var1.getInt();
            int var3 = var1.getInt();
            if (var2 != 64 || var3 != 32 && var3 != 64) {
               throw new IllegalArgumentException("Skin must be 64x64 or legacy 64x32");
            }
         } else {
            throw new IllegalArgumentException("Expected PNG");
         }
      } else {
         throw new IllegalArgumentException("Skin must be a PNG of at most 128 KiB");
      }
   }

   private void tAsVoid() {
      SkinImageValidation.XAsVoid(this.a);
      this.a = null;
   }

   private boolean OAsBoolean() {
      return this.isRuntimeActive() || this.s && this.isOperational();
   }

   public SkinTextures zAsSkinTextures(SkinTextures var1) {
      if (var1 == null || this.isRuntimeActive()) {
         return this.ZAsSkinTextures(var1);
      } else if (this.isOperational()) {
         if (!this.s) {
            this.s = true;
            this.f = this.Q = "";
            this.h = 0L;
            ClientLog.XAsVoid("SkinChanger", "Menu preview loading the saved selection without a world");
         }

         this.onTick();
         return this.ZAsSkinTextures(var1);
      } else {
         if (this.s || this.a != null) {
            this.s = false;
            this.T.incrementAndGet();
            this.tAsVoid();
            this.f = this.Q = "";
            ClientLog.XAsVoid("SkinChanger", "Menu preview ended; local texture released");
         }

         return var1;
      }
   }

   public SkinTextures aAsSkinTextures(UUID var1, SkinTextures var2) {
      if (!this.isEnabled()) {
         return var2;
      } else {
         return this.isRuntimeActive() && var2 != null && mc.player != null && var1.equals(mc.player.getUuid()) ? this.ZAsSkinTextures(var2) : var2;
      }
   }

   private SkinTextures ZAsSkinTextures(SkinTextures var1) {
      if (var1 == null) {
         return null;
      } else if (this.skin.pAsBoolean("None") && this.copyPlayer.getValue().isBlank() && this.a == null) {
         return var1;
      } else {
         Object var2 = this.a == null
            ? (this.skin.pAsBoolean("Default") ? DefaultSkinHelper.getSteve().body() : var1.body())
            : new TextureAssetInfo(this.a, this.a);
         PlayerSkinType var3 = this.a == null
            ? (this.skin.pAsBoolean("Default") ? DefaultSkinHelper.getSteve().model() : var1.model())
            : (this.l ? PlayerSkinType.SLIM : PlayerSkinType.WIDE);
         PlayerSkinType var4 = this.model.pAsBoolean("Auto") ? var3 : (this.model.pAsBoolean("Slim") ? PlayerSkinType.SLIM : PlayerSkinType.WIDE);
         return new SkinTextures((TextureAsset)var2, var1.cape(), var1.elytra(), var4, var1.secure());
      }
   }
}
