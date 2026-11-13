package com.larpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest.Builder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;

public final class CloudRequestContext {
   private final String h;
   private final String N;
   private final String H;

   public CloudRequestContext(String var1, String var2, String var3) {
      if (var1 != null
         && var1.matches("[a-fA-F0-9]{64}")
         && var2 != null
         && var2.matches("[A-Za-z0-9_-]{8,128}")
         && var3 != null
         && var3.matches("[A-Za-z0-9_]{3,16}")) {
         this.h = var1;
         this.N = var2;
         this.H = var3;
      } else {
         throw new AuthException(401, "UNAUTHENTICATED");
      }
   }

   void gAsVoid(Builder var1) {
      var1.header("Authorization", "Bearer " + this.h).header("x-larp-hwid", this.N).header("x-minecraft-username", this.H);
   }

   void yAsVoid(Builder var1, String var2, URI var3, byte[] var4) {
      this.gAsVoid(var1);

      try {
         String var5 = Long.toString(System.currentTimeMillis() / 1000L);
         String var6 = DeviceIdentityStore.fAsString();
         String var7 = String.join(
            "\n",
            "larp-api-v1",
            var2.toUpperCase(Locale.ROOT),
            var3.getRawPath() + (var3.getRawQuery() == null ? "" : "?" + var3.getRawQuery()),
            DeviceIdentityStore.yAsString(this.h),
            this.N,
            this.H.toLowerCase(Locale.ROOT),
            HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var4)),
            var5,
            var6
         );
         var1.header("x-larp-timestamp", var5).header("x-larp-nonce", var6).header("x-larp-signature", DeviceIdentityStore.FAsW1().zAsString(var7));
      } catch (GeneralSecurityException | IOException var81) {
         throw new AuthException(401, "UNAUTHENTICATED");
      }
   }

   public String CAsString() {
      return QAsString(this.h, this.H);
   }

   public static String QAsString(String var0, String var1) {
      try {
         return HexFormat.of()
            .formatHex(MessageDigest.getInstance("SHA-256").digest((var0 + ":" + var1.toLowerCase(Locale.ROOT)).getBytes(StandardCharsets.UTF_8)));
      } catch (NoSuchAlgorithmException var3) {
         throw new IllegalStateException(var3);
      }
   }

   @Override
   public String toString() {
      return "PlatformAuth[redacted]";
   }
}
