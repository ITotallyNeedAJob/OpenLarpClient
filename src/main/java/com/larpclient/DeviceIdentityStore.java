package com.larpclient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HexFormat;

public final class DeviceIdentityStore {
   private final KeyPair H;
   private static DeviceIdentityStore K;

   DeviceIdentityStore(KeyPair var1) {
      this.H = var1;
   }

   public static synchronized DeviceIdentityStore FAsW1() throws IOException {
      if (K == null) {
         K = cAsW1(l5.d);
      }

      return K;
   }

   static DeviceIdentityStore cAsW1(Path var0) throws IOException {
      try {
         Path var1 = var0.resolve("identity.json");

         try {
            return VAsW1(var1);
         } catch (NoSuchFileException var27) {
            Files.createDirectories(var0);
            uAsVoid(var0, true);

            DeviceIdentityStore var20;
            try (
               FileChannel var2 = FileChannel.open(var0.resolve("identity.lock"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
               FileLock var3 = var2.lock();
            ) {
               try {
                  return VAsW1(var1);
               } catch (NoSuchFileException var24) {
                  DeviceIdentityStore var4 = new DeviceIdentityStore(KeyPairGenerator.getInstance("Ed25519").generateKeyPair());
                  JsonObject var5 = new JsonObject();
                  var5.addProperty("publicKey", var4.WAsString());
                  var5.addProperty("privateKey", bAsString(var4.H.getPrivate().getEncoded()));
                  Path var6 = Files.createTempFile(var0, "identity-", ".tmp");

                  try {
                     uAsVoid(var6, false);
                     Files.writeString(var6, var5.toString());

                     try {
                        Files.move(var6, var1, StandardCopyOption.ATOMIC_MOVE);
                     } catch (AtomicMoveNotSupportedException var22) {
                        Files.move(var6, var1);
                     }
                  } finally {
                     Files.deleteIfExists(var6);
                  }

                  var20 = var4;
               }
            }

            return var20;
         }
      } catch (RuntimeException | GeneralSecurityException var28) {
         throw new IOException("Unable to read install identity; restore identity.json or reset the license", var28);
      }
   }

   private static DeviceIdentityStore VAsW1(Path var0) throws IOException, GeneralSecurityException {
      if (Files.size(var0) > 4096L) {
         throw new IOException("Invalid install identity size");
      } else {
         uAsVoid(var0, false);
         JsonObject var1 = JsonParser.parseString(Files.readString(var0)).getAsJsonObject();
         KeyFactory var2 = KeyFactory.getInstance("Ed25519");
         PrivateKey var3 = var2.generatePrivate(new PKCS8EncodedKeySpec(BAsByteArray(var1.get("privateKey").getAsString())));
         PublicKey var4 = var2.generatePublic(new X509EncodedKeySpec(BAsByteArray(var1.get("publicKey").getAsString())));
         DeviceIdentityStore var5 = new DeviceIdentityStore(new KeyPair(var4, var3));
         Signature var6 = Signature.getInstance("Ed25519");
         var6.initVerify(var4);
         var6.update("install-key-check".getBytes(StandardCharsets.UTF_8));
         if (!var6.verify(BAsByteArray(var5.zAsString("install-key-check")))) {
            throw new IOException("Install identity keys do not match");
         } else {
            return var5;
         }
      }
   }

   static void uAsVoid(Path var0, boolean var1) throws IOException {
      try {
         Files.setPosixFilePermissions(var0, PosixFilePermissions.fromString(var1 ? "rwx------" : "rw-------"));
      } catch (UnsupportedOperationException var2) {
      }
   }

   public String WAsString() {
      return bAsString(this.H.getPublic().getEncoded());
   }

   public String ZAsString() {
      return pAsString(this.H.getPublic().getEncoded());
   }

   public String zAsString(String var1) throws IOException {
      try {
         Signature var2 = Signature.getInstance("Ed25519");
         var2.initSign(this.H.getPrivate());
         var2.update(var1.getBytes(StandardCharsets.UTF_8));
         return bAsString(var2.sign());
      } catch (GeneralSecurityException var31) {
         throw new IOException("Install signature failed", var31);
      }
   }

   public static String yAsString(String var0) {
      return pAsString(var0.getBytes(StandardCharsets.UTF_8));
   }

   static String pAsString(byte[] var0) {
      try {
         return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var0));
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static String fAsString() {
      byte[] var0 = new byte[32];
      new SecureRandom().nextBytes(var0);
      return HexFormat.of().formatHex(var0);
   }

   static String bAsString(byte[] var0) {
      return Base64.getUrlEncoder().withoutPadding().encodeToString(var0);
   }

   static byte[] BAsByteArray(String var0) {
      return Base64.getUrlDecoder().decode(var0);
   }
}
