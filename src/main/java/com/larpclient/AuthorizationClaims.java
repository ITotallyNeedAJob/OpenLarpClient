package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larp.obf.RuntimeAdmission;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public final class AuthorizationClaims {
   public static final long BloomUtility = 600L;

   private AuthorizationClaims() {
   }

   public static AuthorizationClaims.Type_y FAsOc_Type_y(boolean var0, String var1, String var2, String var3, DeviceIdentityStore var4, long var5) throws IOException {
      return wAsOc_Type_y(var0, var1, var2, var3, var4, var5, RuntimeAdmission.m2());
   }

   static AuthorizationClaims.Type_y wAsOc_Type_y(boolean var0, String var1, String var2, String var3, DeviceIdentityStore var4, long var5, String var7) throws IOException {
      if (!var7.isEmpty() && !var7.matches("[a-f0-9]{32}")) {
         throw new IOException("Invalid protected release identifier");
      } else {
         String var8 = var0 ? "activate" : "session";
         String var9 = DeviceIdentityStore.fAsString();
         JsonObject var10 = AuthHttpExchange.SAsJsonObject(var0 ? "key" : "token", var1, var2, var3);
         var10.addProperty("protectionVersion", var7.isEmpty() ? 1 : 2);
         if (!var7.isEmpty()) {
            var10.addProperty("release", var7);
         }

         var10.addProperty("challenge", var9);
         var10.addProperty("deviceKey", var4.WAsString());
         var10.addProperty("timestamp", var5);
         String var11 = QAsString(var8, var1, var2, var3, var9, var4.WAsString(), var5, var7);
         var10.addProperty("deviceSignature", var4.zAsString(var11));
         return new AuthorizationClaims.Type_y(var10, var9, var4.ZAsString(), var8, var2, var3, var7);
      }
   }

   static String fAsString(String var0, String var1, String var2, String var3, String var4, String var5, long var6) {
      return QAsString(var0, var1, var2, var3, var4, var5, var6, "");
   }

   static String QAsString(String var0, String var1, String var2, String var3, String var4, String var5, long var6, String var8) {
      String var9 = String.join(
         "\n",
         var8.isEmpty() ? "larp-auth-v1" : "larp-auth-v2",
         var0,
         DeviceIdentityStore.yAsString(var1),
         var2,
         var3.toLowerCase(Locale.ROOT),
         var4,
         var5,
         Long.toString(var6)
      );
      return var8.isEmpty() ? var9 : var9 + "\n" + var8;
   }

   public static AuthorizationClaims.Type_l KAsOc_Type_l(AuthHttpExchange.Type_h var0, AuthorizationClaims.Type_y var1, String var2, long var3, long var5) throws IOException {
      return QAsOc_Type_l(var0.grant(), var1, var2, var3, var5, nAsMap());
   }

   static AuthorizationClaims.Type_l QAsOc_Type_l(String var0, AuthorizationClaims.Type_y var1, String var2, long var3, long var5, Map<String, PublicKey> var7) throws IOException {
      try {
         if (var0.length() > 4096) {
            throw new GeneralSecurityException();
         } else {
            String[] var8 = var0.split("\\.", -1);
            if (var8.length == 3 && var8[0].matches("[A-Za-z0-9_-]{1,32}")) {
               PublicKey var9 = var7.get(var8[0]);
               if (var9 == null) {
                  throw new GeneralSecurityException();
               } else {
                  Signature var10 = Signature.getInstance("Ed25519");
                  var10.initVerify(var9);
                  var10.update((var8[0] + "." + var8[1]).getBytes(StandardCharsets.UTF_8));
                  if (!var10.verify(DeviceIdentityStore.BAsByteArray(var8[2]))) {
                     throw new GeneralSecurityException();
                  } else {
                     JsonObject var11 = JsonParser.parseString(new String(DeviceIdentityStore.BAsByteArray(var8[1]), StandardCharsets.UTF_8)).getAsJsonObject();
                     long var12 = XAsLong(var11, "iat");
                     long var14 = XAsLong(var11, "exp");
                     if (var12 <= var3 + 30L
                        && var12 >= var3 - 60L
                        && var14 > var3
                        && var14 > var12
                        && var14 - var12 <= 600L
                        && MAsString(var11, "iss").equals("https://larpclient.com")
                        && MAsString(var11, "aud").equals("larp-client")
                        && !MAsString(var11, "sub").isBlank()
                        && MAsString(var11, "operation").equals(var1.operation())
                        && MAsString(var11, "challenge").equals(var1.challenge())
                        && MAsString(var11, "device").equals(var1.device())
                        && MAsString(var11, "hwid").equals(var1.hwid())
                        && MAsString(var11, "player").equalsIgnoreCase(var1.player())
                        && var2.matches("[a-f0-9]{64}")
                        && MAsString(var11, "tokenHash").equals(DeviceIdentityStore.yAsString(var2))) {
                        if (!var1.release().isEmpty()
                           && (!MAsString(var11, "release").equals(var1.release()) || !MAsString(var11, "codeKey").matches("[a-f0-9]{32}"))) {
                           throw new GeneralSecurityException();
                        } else {
                           long var16 = Math.min(var14 - var3, 600L);
                           return new AuthorizationClaims.Type_l(var1.player(), DeviceIdentityStore.yAsString(var2), var14, var5 + var16 * 1000000000L);
                        }
                     } else {
                        throw new GeneralSecurityException();
                     }
                  }
               }
            } else {
               throw new GeneralSecurityException();
            }
         }
      } catch (GeneralSecurityException | RuntimeException var181) {
         throw new IOException("Invalid signed authorization; update the client or retry activation", var181);
      }
   }

   private static String MAsString(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      if (var2 != null && var2.isJsonPrimitive() && var2.getAsJsonPrimitive().isString()) {
         return var2.getAsString();
      } else {
         throw new IllegalArgumentException();
      }
   }

   private static long XAsLong(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      if (var2 != null && var2.isJsonPrimitive() && var2.getAsJsonPrimitive().isNumber() && var2.getAsString().matches("[0-9]{1,12}")) {
         return var2.getAsLong();
      } else {
         throw new IllegalArgumentException();
      }
   }

   private static Map<String, PublicKey> nAsMap() {
      return AuthorizationClaims.Type_x.V;
   }

   public static record Type_l(String player, String tokenHash, long expiresAt, long deadlineNanos) {
      public boolean active(long var1, long var3, String var5) {
         return var1 < this.expiresAt && var3 - this.deadlineNanos < 0L && this.player.equalsIgnoreCase(var5);
      }
   }

   private static final class Type_x {
      static final Map<String, PublicKey> V = EAsMap();

      private Type_x() {
      }

      static Map<String, PublicKey> EAsMap() {
         try {
            Map var9;
            try (InputStream var0 = AuthorizationClaims.class.getResourceAsStream("/assets/larp-client/auth/trust.json")) {
               if (var0 == null) {
                  throw new IOException("Missing authorization trust keys");
               }

               JsonObject var1 = JsonParser.parseString(new String(var0.readNBytes(8192), StandardCharsets.UTF_8)).getAsJsonObject();
               HashMap var2 = new HashMap();
               KeyFactory var3 = KeyFactory.getInstance("Ed25519");

               for (Entry var5 : var1.entrySet()) {
                  var2.put(
                     (String)var5.getKey(),
                     var3.generatePublic(new X509EncodedKeySpec(DeviceIdentityStore.BAsByteArray(((JsonElement)var5.getValue()).getAsString())))
                  );
               }

               var9 = Map.copyOf(var2);
            }

            return var9;
         } catch (GeneralSecurityException | RuntimeException | IOException var81) {
            throw new IllegalStateException("Authorization trust unavailable", var81);
         }
      }
   }

   public static record Type_y(JsonObject body, String challenge, String device, String operation, String hwid, String player, String release) {
      public Type_y(JsonObject var1, String var2, String var3, String var4, String var5, String var6) {
         this(var1, var2, var3, var4, var5, var6, "");
      }
   }
}
