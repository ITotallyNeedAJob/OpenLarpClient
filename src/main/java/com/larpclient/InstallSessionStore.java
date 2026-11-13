package com.larpclient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

final class InstallSessionStore {
   private final Path C;
   private final Path e;

   InstallSessionStore(Path var1, Path var2) {
      this.C = var1;
      this.e = var2;
   }

   String RAsString() {
      String var1 = xAsString(this.C);
      return var1.isEmpty() ? xAsString(this.e) : var1;
   }

   String pAsString(String var1) {
      String var2 = xAsString(this.e);
      return var2.equals(var1) ? "" : var2;
   }

   InstallSessionStore.Type_r QAsNi_Type_r(String var1, boolean var2) {
      IOException var3 = null;
      IOException var4 = null;

      try {
         zAsVoid(this.C, var1);
      } catch (IOException var7) {
         var3 = var7;
      }

      if (!var1.isBlank() && (var2 || !Files.exists(this.e))) {
         try {
            zAsVoid(this.e, var1);
         } catch (IOException var6) {
            var4 = var6;
         }
      }

      return new InstallSessionStore.Type_r(var3, var4);
   }

   private static String xAsString(Path var0) {
      try {
         if (Files.size(var0) > 4096L) {
            return "";
         } else {
            JsonObject var1 = JsonParser.parseString(Files.readString(var0)).getAsJsonObject();
            String var2 = var1.get("installToken").getAsString();
            return var2.matches("[a-f0-9]{64}") ? var2 : "";
         }
      } catch (RuntimeException | IOException var31) {
         return "";
      }
   }

   private static void zAsVoid(Path var0, String var1) throws IOException {
      Files.createDirectories(var0.getParent());
      DeviceIdentityStore.uAsVoid(var0.getParent(), true);
      JsonObject var2 = new JsonObject();
      var2.addProperty("installToken", var1);
      Path var3 = Files.createTempFile(var0.getParent(), "auth-", ".tmp");

      try {
         DeviceIdentityStore.uAsVoid(var3, false);
         Files.writeString(var3, var2.toString());

         try {
            Files.move(var3, var0, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (AtomicMoveNotSupportedException var8) {
            Files.move(var3, var0, StandardCopyOption.REPLACE_EXISTING);
         }
      } finally {
         Files.deleteIfExists(var3);
      }
   }

   static record Type_r(IOException localFailure, IOException sharedFailure) {
   }
}
