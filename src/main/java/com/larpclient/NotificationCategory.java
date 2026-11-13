package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public final class NotificationCategory {
   private static final ArrayDeque<String> A = new ArrayDeque<>();
   private static String o = "";
   private static final Set<String> N = Set.of("Auth", "Config", "Render", "API", "Module", "Error", "Releases", "Reports", "Status");
   private static final Pattern k = Pattern.compile(
      "(?i)LARP-[A-F0-9]{4}(?:-[A-F0-9]{4}){3}|LARP-[A-F0-9]{16}|\\b[a-f0-9]{64}\\b|\\bBearer\\s+\\S+|\\bmfa\\.[\\w-]+|\\b[\\w-]{20,}\\.[\\w-]{6}\\.[\\w-]{20,}\\b|(?:license[_ -]?key|access[_ -]?token|refresh[_ -]?token|install[_ -]?token|session[_ -]?token|token|password|client[_ -]?secret|authorization|auth\\.json|hwid)\\s*[\"']?\\s*[:=][^\\r\\n]*|-----BEGIN .*PRIVATE KEY-----[\\s\\S]*?-----END .*PRIVATE KEY-----"
   );

   public NotificationCategory() {
   }

   public static String BAsString(String var0) {
      return var0 == null ? "" : k.matcher(var0).replaceAll("[redacted]").replaceAll("(?:[A-Za-z]:\\\\|/home/|/Users/)[^\\s\"']+", "[path]").replace('§', ' ');
   }

   public static synchronized void CAsVoid(String var0, NotificationCategory.Type_o var1) {
      mAsVoid(var0, var1.name());
   }

   public static synchronized void KAsVoid(String var0, String var1, Throwable var2) {
      if (N.contains(var0)) {
         mAsVoid(var0, var1 + " event");
         if (var2 != null) {
            o = "[Larp/" + var0 + "] " + var2.getClass().getSimpleName();
         }
      }
   }

   private static void mAsVoid(String var0, String var1) {
      if (N.contains(var0)) {
         if (A.size() == 80) {
            A.removeFirst();
         }

         A.addLast("[Larp/" + var0 + "] " + var1);
      }
   }

   public static synchronized String NAsString() {
      return String.join("\n", A);
   }

   public static synchronized String WAsString() {
      return o;
   }

   public static JsonObject JAsJsonObject(String var0, String var1, String var2, String var3, List<String> var4) {
      var0 = BAsString(var0).trim();
      var1 = BAsString(var1).trim();
      if (!var0.isEmpty() && var0.length() <= 120 && var1.length() >= 10 && var1.length() <= 4000) {
         JsonObject var5 = new JsonObject();
         var5.addProperty("title", var0);
         var5.addProperty("description", var1);
         var5.addProperty("minecraftVersion", BuildMetadata.e);
         var5.addProperty("larpVersion", BuildMetadata.I);
         var5.addProperty("fabricVersion", BAsString(var2));
         var5.addProperty(
            "operatingSystem", var3.contains("Windows") ? "Windows" : (var3.contains("Mac") ? "macOS" : (var3.contains("Linux") ? "Linux" : "Other"))
         );
         JsonArray var6 = new JsonArray();
         var4.stream().sorted().limit(200L).filter(var0x -> var0x.matches("[A-Za-z0-9][A-Za-z0-9 _.-]{0,63}")).forEach(var6::add);
         var5.add("enabledModules", var6);
         var5.addProperty("logs", NAsString());
         var5.addProperty("exception", WAsString());
         if (var5.toString().getBytes(StandardCharsets.UTF_8).length > 32768) {
            throw new AuthException(413, "PAYLOAD_TOO_LARGE");
         } else {
            return var5;
         }
      } else {
         throw new IllegalArgumentException("Enter a title (1–120 characters) and description (10–4000 characters).");
      }
   }

   public static enum Type_o {
      REQUEST_STARTED,
      REQUEST_SUCCEEDED,
      REQUEST_FAILED,
      IMPORT_VALIDATED,
      IMPORT_REJECTED,
      REPORT_ACCEPTED;

      private Type_o() {
      }
   }
}
