package com.larpclient;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PriceDetector {
   private static final Pattern R = Pattern.compile(
      "(?i)(?:\\$\\s*|(?:worth|price|cost)\\s*[:~≈]?\\s*\\$?\\s*)([0-9][0-9,]*(?:\\.[0-9]+)?)\\s*+([kmbt]?)(?![a-z0-9.,])"
   );
   private static final Pattern r = Pattern.compile("^[^\\p{L}\\p{N}]*([0-9][0-9,]*(?:\\.[0-9]+)?)\\s*([kmbt])(?:\\s*/\\s*[0-9]+)?\\s*$");

   public PriceDetector() {
   }

   public static int mAsInt(int var0) {
      return var0 <= 0 ? 0 : 1 + (var0 - 1) / 64;
   }

   public static boolean AAsBoolean(Iterable<String> var0) {
      return CAsM_Type_i(var0) == PriceDetector.Type_i.SAFE;
   }

   public static PriceDetector.Type_i CAsM_Type_i(Iterable<String> var0) {
      byte var1 = 0;
      byte var2 = 0;

      for (String var4 : var0) {
         String var5 = Normalizer.normalize(var4, Form.NFKC).replaceAll("§.", "").replaceAll("[\\p{Z}\\u200B\\uFEFF]", " ").toLowerCase(Locale.ROOT);
         Matcher var6 = R.matcher(var5);
         if (!var6.find()) {
            var6 = r.matcher(var5);
         }

         var6.reset();

         while (var6.find()) {
            var1 = 1;
            String var10 = var6.group(2);

            byte var7 = switch (var10) {
               case "k" -> 3;
               case "m" -> 6;
               case "b" -> 9;
               case "t" -> 12;
               default -> 0;
            };
            BigDecimal var10_r2 = new BigDecimal(var6.group(1).replace(",", "")).scaleByPowerOfTen(var7);
            if (var10_r2.compareTo(BigDecimal.valueOf(3000000L)) > 0) {
               return PriceDetector.Type_i.ABOVE_LIMIT;
            }
         }

         if ((var5.contains("worth") || var5.contains("price") || var5.contains("cost") || var5.contains("$"))
            && !R.matcher(var5).find()
            && !r.matcher(var5).matches()) {
            var2 = 1;
         }
      }

      return var1 != 0 && var2 == 0 ? PriceDetector.Type_i.SAFE : PriceDetector.Type_i.UNREADABLE;
   }

   public static enum Type_i {
      SAFE,
      ABOVE_LIMIT,
      UNREADABLE;

      private Type_i() {
      }
   }
}
