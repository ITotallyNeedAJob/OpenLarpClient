package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class ProfileShareCodec {
   public ProfileShareCodec() {
   }

   public static String uAsString(JsonObject var0, String var1) {
      String var2 = ProfileCodec.MAsString(var0, var1, 128);
      if (!var2.matches("[A-Za-z0-9_-]{1,128}")) {
         throw AuthException.rAsAh();
      } else {
         return var2;
      }
   }

   public static String lAsString(String var0) {
      var0 = var0.trim().toUpperCase(Locale.ROOT);
      if (!var0.matches("(?:LARP-)?[A-F0-9]{16}")) {
         throw new IllegalArgumentException("Enter PetEffectRenderer LARP share code with 16 hexadecimal characters.");
      } else {
         return var0.startsWith("LARP-") ? var0 : "LARP-" + var0;
      }
   }

   public static String nAsString(JsonObject var0) {
      String var1 = ProfileCodec.MAsString(var0, "shareCode", 21);
      if (!var1.matches("LARP-[A-F0-9]{16}")) {
         throw AuthException.rAsAh();
      } else {
         return var1;
      }
   }

   public static String wAsString(JsonObject var0) {
      String var1 = ProfileCodec.MAsString(var0, "id", 16);
      if (!var1.matches("LRP-[A-F0-9]{12}")) {
         throw AuthException.rAsAh();
      } else {
         return var1;
      }
   }

   public static String KAsString(JsonObject var0) {
      String var1 = ProfileCodec.VAsString(var0.get("marketplaceState"), "private", "submitted", "approved", "rejected", "removed");

      return switch (var1) {
         case "private" -> "Private (unlisted)";
         case "submitted" -> "Pending review";
         case "approved" -> "Approved";
         case "rejected" -> "Rejected";
         default -> "Removed";
      };
   }

   public static boolean XAsBoolean(JsonObject var0) {
      return var0.has("discordSharing") && ProfileCodec.SAsBoolean(var0.get("discordSharing"));
   }

   public static String VAsString(JsonObject var0) {
      if (!XAsBoolean(var0)) {
         return "";
      } else {
         String var1 = ProfileCodec.VAsString(var0.get("marketplaceState"), "private", "submitted", "approved", "rejected", "removed");
         return var1.equals("removed") ? "Taken off Discord by staff" : "Posted in Discord";
      }
   }

   public static List<JsonObject> dAsList(JsonObject var0, boolean var1) {
      if (var0.has("items") && var0.get("items").isJsonArray() && var0.getAsJsonArray("items").size() <= 100) {
         ArrayList var2 = new ArrayList();

         for (JsonElement var4 : var0.getAsJsonArray("items")) {
            JsonObject var5 = ProfileCodec.DAsJsonObject(var4);
            uAsString(var5, "id");
            uAsString(var5, "revision");
            ProfileCodec.IAsString(ProfileCodec.MAsString(var5, "name", 32));
            ProfileCodec.MAsString(var5, "updatedAt", 40);
            if (!var1) {
               nAsString(var5);
               KAsString(var5);
               ProfileCodec.MAsString(var5, "rejectionReason", 500);
               VAsString(var5);
            }

            var2.add(var5.deepCopy());
         }

         return List.copyOf(var2);
      } else {
         throw AuthException.rAsAh();
      }
   }

   public static ProfileShareCodec.Type_g ZAsWv_Type_g(JsonObject var0) {
      try {
         String var1 = ProfileCodec.MAsString(var0, "version", 40);
         if (!var1.matches("[A-Za-z0-9][A-Za-z0-9.+_-]*")) {
            throw AuthException.rAsAh();
         } else if (!var0.has("updateAvailable")) {
            throw AuthException.rAsAh();
         } else {
            Boolean var2 = var0.get("updateAvailable").isJsonNull() ? null : ProfileCodec.SAsBoolean(var0.get("updateAvailable"));
            if (Boolean.TRUE.equals(var2) && var1.matches("\\d+(?:\\.\\d+){0,3}") && BuildMetadata.I.matches("\\d+(?:\\.\\d+){0,3}")) {
               String[] var3 = var1.split("\\.");
               String[] var4 = BuildMetadata.I.split("\\.");
               int var5 = 0;

               for (int var6 = 0; var6 < Math.max(var3.length, var4.length) && var5 == 0; var6++) {
                  var5 = new BigInteger(var6 < var3.length ? var3[var6] : "0").compareTo(new BigInteger(var6 < var4.length ? var4[var6] : "0"));
               }

               if (var5 <= 0) {
                  var2 = false;
               }
            }

            ArrayList var8 = new ArrayList();
            var8.add("Larp " + var1 + " — " + ProfileCodec.MAsString(var0, "title", 120));
            var8.add(
               ProfileCodec.MAsString(var0, "releaseDate", 10)
                  + " / Minecraft "
                  + ProfileCodec.MAsString(var0, "minecraftVersion", 40)
                  + " / "
                  + ProfileCodec.VAsString(var0.get("status"), "stable", "beta", "experimental")
            );
            var8.add(ProfileCodec.MAsString(var0, "summary", 500));
            if (var0.has("notice") && !var0.get("notice").isJsonNull()) {
               var8.add(ProfileCodec.MAsString(var0, "notice", 1000));
            }

            if (var0.has("notes") && var0.get("notes").isJsonArray() && var0.getAsJsonArray("notes").size() <= 100) {
               for (JsonElement var10 : var0.getAsJsonArray("notes")) {
                  JsonObject var11 = ProfileCodec.DAsJsonObject(var10);
                  var8.add(
                     ProfileCodec.VAsString(var11.get("category"), "Added", "Changed", "Fixed", "Visuals", "Performance", "Notes")
                        + ": "
                        + ProfileCodec.MAsString(var11, "text", 2000)
                  );
               }

               return new ProfileShareCodec.Type_g(var1, var2, List.copyOf(var8));
            } else {
               throw AuthException.rAsAh();
            }
         }
      } catch (RuntimeException var71) {
         throw AuthException.rAsAh();
      }
   }

   public static ProfileShareCodec.Type_m AAsWv_Type_m(JsonObject var0) {
      try {
         String var1 = ProfileCodec.VAsString(var0.get("state"), "Operational", "Degraded", "Unavailable");
         if (var0.has("services") && var0.get("services").isJsonArray() && var0.getAsJsonArray("services").size() <= 20) {
            byte var2 = 0;
            ArrayList var3 = new ArrayList();

            for (JsonElement var5 : var0.getAsJsonArray("services")) {
               JsonObject var6 = ProfileCodec.DAsJsonObject(var5);
               String var7 = uAsString(var6, "id");
               String var8 = ProfileCodec.VAsString(var6.get("state"), "Operational", "Degraded", "Unavailable");
               if (Set.of("authentication", "client-session").contains(var7) && !var8.equals("Operational")) {
                  var2 = 1;
               }

               var3.add(ProfileCodec.MAsString(var6, "name", 120) + ": " + var8);
            }

            return new ProfileShareCodec.Type_m(var1, var2 != 0, List.copyOf(var3));
         } else {
            throw AuthException.rAsAh();
         }
      } catch (RuntimeException var91) {
         throw AuthException.rAsAh();
      }
   }

   public static record Type_g(String version, Boolean updateAvailable, List<String> lines) {
   }

   public static record Type_m(String state, boolean authIssue, List<String> lines) {
   }
}
