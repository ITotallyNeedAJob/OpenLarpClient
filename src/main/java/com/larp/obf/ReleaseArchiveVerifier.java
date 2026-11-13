package com.larp.obf;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.CodeSource;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ReleaseArchiveVerifier {
   public static final String f0 = "larp/integrity.bin";
   private static final int f1 = 2097152;
   private static final long f2 = 268435456L;
   private static Path f3;
   private static String f4;
   private static Map<String, ArchiveEntryDigest> f5;

   private ReleaseArchiveVerifier() {
   }

   static Path m0(Class<?> var0) throws IOException {
      try {
         CodeSource var1 = var0.getProtectionDomain().getCodeSource();
         if (var1 != null && var1.getLocation() != null) {
            URI var2 = var1.getLocation().toURI();
            if ("jar".equals(var2.getScheme())) {
               String var3 = var2.getRawSchemeSpecificPart();
               int var4 = var3.indexOf("!/");
               if (var4 < 0 || !(var0.getName().replace('.', '/') + ".class").equals(URI.create(var3.substring(var4 + 2)).getPath())) {
                  throw new IOException("Release archive origin names a different entry");
               }

               var2 = URI.create(var3.substring(0, var4));
            }

            if (!"file".equals(var2.getScheme())) {
               throw new IOException("Release archive origin is unavailable");
            } else {
               Path var6 = Path.of(var2).toRealPath();
               if (!Files.isRegularFile(var6)) {
                  throw new IOException("Protected release requires its packaged archive");
               } else {
                  return var6;
               }
            }
         } else {
            throw new IOException("Release archive origin is unavailable");
         }
      } catch (IllegalArgumentException | SecurityException | URISyntaxException var51) {
         throw new IOException("Release archive origin is invalid", var51);
      }
   }

   public static synchronized void m1(Class<?> var0, String var1) throws IOException {
      if (var0.getClassLoader() == ReleaseArchiveVerifier.class.getClassLoader()
         && Set.of("com.larp.obf.ConstantBootstrap", "com.larp.obf.ReleaseMixinPlugin").contains(var0.getName())) {
         Path var2 = m0(var0);
         if (f3 == null) {
            try (JarFile var3 = new JarFile(var2.toFile())) {
               JarEntry var4 = var3.getJarEntry("larp/integrity.bin");
               if (var4 == null || var4.getSize() < 72L || var4.getSize() > 2097152L) {
                  throw new IOException("Release inventory is missing or oversized");
               }

               byte[] var5;
               try (InputStream var6 = var3.getInputStream(var4)) {
                  var5 = var6.readNBytes(2097153);
               }

               if (var5.length < 72 || var5.length > 2097152) {
                  throw new IOException("Release inventory has invalid bounds");
               }

               Signature var28 = Signature.getInstance("Ed25519");
               var28.initVerify(KeyFactory.getInstance("Ed25519").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(var1))));
               var28.update(var5, 0, var5.length - 64);
               if (!var28.verify(Arrays.copyOfRange(var5, var5.length - 64, var5.length))) {
                  throw new IOException("Release inventory signature mismatch");
               }

               HashMap var7 = new HashMap();

               try (DataInputStream var8 = new DataInputStream(new ByteArrayInputStream(var5, 0, var5.length - 64))) {
                  label227: {
                     if (var8.readInt() != 1280461105) {
                        throw new IOException("Release inventory format mismatch");
                     }

                     int var9 = var8.readInt();
                     if (var9 >= 1 && var9 <= 16384) {
                        long var10 = 0L;

                        for (int var12 = 0; var12 < var9; var12++) {
                           String var13 = var8.readUTF();
                           long var14 = var8.readLong();
                           byte[] var16 = var8.readNBytes(32);
                           if (var13.isEmpty()
                              || var13.length() > 4096
                              || var13.startsWith("/")
                              || var13.contains("\\")
                              || var13.contains(":")
                              || var13.indexOf(0) >= 0
                              || var13.endsWith("/")
                              || var13.equals("larp/integrity.bin")
                              || Arrays.stream(var13.split("/", -1)).anyMatch(var0x -> var0x.isEmpty() || var0x.equals(".") || var0x.equals(".."))
                              || var14 < 0L
                              || var14 > 268435456L
                              || var16.length != 32
                              || var7.put(var13, new ArchiveEntryDigest(var14, var16)) != null) {
                              throw new IOException("Release inventory entry is invalid: " + var13);
                           }

                           var10 += var14;
                           if (var10 > 268435456L) {
                              throw new IOException("Release contents exceed startup bounds");
                           }
                        }

                        if (var8.read() == -1) {
                           break label227;
                        }

                        throw new IOException("Release inventory has trailing data");
                     }

                     throw new IOException("Release inventory count is invalid");
                  }
               }

               HashSet var291 = new HashSet();
               int var301 = 0;
               byte[] var311 = new byte[65536];
               MessageDigest var11 = MessageDigest.getInstance("SHA-256");
               Enumeration var321 = var3.entries();

               while (var321.hasMoreElements()) {
                  JarEntry var33 = (JarEntry)var321.nextElement();
                  String var34 = var33.getName();
                  if (!var291.add(var34)) {
                     throw new IOException("Duplicate release entry: " + var34);
                  }

                  if (!var33.isDirectory() && !var34.equals("larp/integrity.bin")) {
                     ArchiveEntryDigest var15 = (ArchiveEntryDigest)var7.get(var34);
                     if (var15 == null) {
                        throw new IOException("Unexpected release entry: " + var34);
                     }

                     if (var33.getSize() != var15.size()) {
                        throw new IOException("Release entry size mismatch: " + var34);
                     }

                     var11.reset();

                     try (InputStream var35 = var3.getInputStream(var33)) {
                        m4(var35, null, var15.size(), var11, var311);
                     }

                     if (!MessageDigest.isEqual(var11.digest(), var15.digest())) {
                        throw new IOException("Release entry digest mismatch: " + var34);
                     }

                     var301++;
                  }
               }

               if (var301 != var7.size()) {
                  throw new IOException("Release entries are missing");
               }

               f3 = var2;
               f4 = var1;
               f5 = Map.copyOf(var7);
            } catch (IllegalArgumentException | GeneralSecurityException var271) {
               throw new IOException("Release inventory verification failed", var271);
            }
         } else if (!var2.equals(f3) || !var1.equals(f4)) {
            throw new IOException("Release bootstrap origin or key changed");
         }
      } else {
         throw new IOException("Release verification anchor mismatch");
      }
   }

   public static synchronized void m2(Set<String> var0) {
      if (f5 == null) {
         throw new IllegalStateException("Release inventory was not verified");
      } else {
         for (String var2 : var0) {
            if (f5.containsKey(var2.replace('.', '/') + ".class")) {
               throw new IllegalStateException(
                  "External Mixin targets protected Larp class: " + var2 + "; remove the modifying mod and restart with an intact release"
               );
            }
         }
      }
   }

   public static synchronized void m3(Class<?> var0, String var1, String var2, Path var3) throws IOException {
      m1(var0, var1);
      boolean var4 = var2.equals("larp/native/macos-arm64.dylib") || var2.equals("larp/native/macos-x64.dylib");
      if (!var4 && !var2.equals("larp/native/windows-x64.dll") && !var2.equals("larp/native/linux-x64.so")) {
         throw new IOException("Invalid release native path");
      } else {
         ArchiveEntryDigest var5 = f5.get(var2);
         if (var5 == null) {
            throw new IOException(
               var4
                  ? "This Larp JAR is for Windows and Linux; install the macOS release JAR"
                  : "This Larp JAR is the macOS release; install the Windows and Linux release JAR"
            );
         } else {
            try {
               try (JarFile var6 = new JarFile(f3.toFile())) {
                  JarEntry var7 = var6.getJarEntry(var2);
                  if (var7 == null || var7.getSize() != var5.size()) {
                     throw new IOException("Native library size changed after verification");
                  }

                  MessageDigest var8 = MessageDigest.getInstance("SHA-256");

                  try (
                     InputStream var9 = var6.getInputStream(var7);
                     OutputStream var10 = Files.newOutputStream(var3, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
                  ) {
                     m4(var9, var10, var5.size(), var8, new byte[65536]);
                  }

                  if (!MessageDigest.isEqual(var8.digest(), var5.digest())) {
                     throw new IOException("Native library digest changed after verification");
                  }
               }
            } catch (GeneralSecurityException var191) {
               throw new IOException("Native library verification failed", var191);
            }
         }
      }
   }

   private static void m4(InputStream var0, OutputStream var1, long var2, MessageDigest var4, byte[] var5) throws IOException {
      long var6 = 0L;

      int var8;
      while ((var8 = var0.read(var5)) != -1) {
         var6 += (long)var8;
         if (var6 > var2) {
            throw new IOException("Release entry exceeds its signed size");
         }

         var4.update(var5, 0, var8);
         if (var1 != null) {
            var1.write(var5, 0, var8);
         }
      }

      if (var6 != var2) {
         throw new IOException("Release entry is truncated");
      }
   }
}
