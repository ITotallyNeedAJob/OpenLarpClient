package com.larpclient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.function.Consumer;

public final class g {
   private static final BigDecimal h = new BigDecimal("9000000000000000");
   private static final String D = "Usage: /pay <player> <amount>, e.g. /pay Alex 1.5m";
   public static boolean T;

   private g() {
   }

   public static boolean xAsBoolean(String var0) {
      if (var0 == null) {
         return false;
      } else {
         String var1 = var0.strip();
         if (var1.startsWith("/")) {
            var1 = var1.substring(1);
         }

         int var2 = 0;

         while (var2 < var1.length() && !Character.isWhitespace(var1.charAt(var2))) {
            var2++;
         }

         String var3 = var1.substring(0, var2).toLowerCase(Locale.ROOT);
         return var3.equals("pay") || var3.matches("[a-z0-9_.-]+:pay");
      }
   }

   public static g.Type_t qAsG_Type_t(String var0) {
      if (var0 == null) {
         return new g.Type_t(null, null, "Usage: /pay <player> <amount>, e.g. /pay Alex 1.5m");
      } else {
         String var1 = var0.strip();
         if (var1.startsWith("/")) {
            var1 = var1.substring(1);
         }

         String[] var2 = var1.split("(?U)\\s+");
         if (var2.length == 3 && xAsBoolean(var1)) {
            if (!var2[1].matches("[A-Za-z0-9_]{1,16}")) {
               return new g.Type_t(null, null, "Use a Minecraft player name (1 to 16 letters, digits or underscores).");
            } else {
               String var3 = var2[2];
               if (var3.length() <= 40 && var3.matches("\\$?(?:[0-9]+|[0-9]{1,3}(?:,[0-9]{3})+)(?:\\.[0-9]{1,6})?[kKmMbBtT]?")) {
                  String var4 = var3.replace("$", "").replace(",", "").toLowerCase(Locale.ROOT);

                  byte var5 = switch (var4.charAt(var4.length() + -1)) {
                     case 'b' -> 9;
                     case 'k' -> 3;
                     case 'm' -> 6;
                     case 't' -> 12;
                     default -> 0;
                  };
                  if (var5 != 0) {
                     var4 = var4.substring(0, var4.length() + -1);
                  }

                  BigDecimal var6 = new BigDecimal(var4).scaleByPowerOfTen(var5).stripTrailingZeros();
                  return var6.signum() > 0 && var6.compareTo(h) <= 0 && var6.scale() <= 2
                     ? new g.Type_t(var2[1], var6, null)
                     : new g.Type_t(null, null, "Amount must be positive, at most 9 quadrillion, with at most two decimal places after expansion.");
               } else {
                  return new g.Type_t(null, null, "Use a positive amount with optional commas and k, m, b or t.");
               }
            }
         } else {
            return new g.Type_t(null, null, "Usage: /pay <player> <amount>, e.g. /pay Alex 1.5m");
         }
      }
   }

   public static boolean ZAsBoolean(boolean var0, String var1, Consumer<g.Type_t> var2, Consumer<RuntimeException> var3) {
      if (var0 && xAsBoolean(var1)) {
         try {
            var2.accept(qAsG_Type_t(var1));
         } catch (RuntimeException var7) {
            RuntimeException var4 = var7;

            try {
               var3.accept(var4);
            } catch (RuntimeException var6) {
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static String gAsString(BigDecimal var0) {
      String var3 = "";
      boolean var1 = T;
      byte var4 = 0;
      if (var0.compareTo(BigDecimal.valueOf(1000000000000L)) >= 0) {
         var4 = 12;
         var3 = "T";
         if (!var1) {
            return var0.scaleByPowerOfTen(-12).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + "T";
         }

         int var2 = ProfileManager.sm;
         ProfileManager.sm = ++var2;
      }

      if (var0.compareTo(BigDecimal.valueOf(1000000000L)) >= 0) {
         var4 = 9;
         var3 = "B";
         if (!var1) {
            return var0.scaleByPowerOfTen(-9).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + "B";
         }
      }

      if (var0.compareTo(BigDecimal.valueOf(1000000L)) >= 0) {
         var4 = 6;
         var3 = "M";
         if (!var1) {
            return var0.scaleByPowerOfTen(-6).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + "M";
         }
      }

      if (var0.compareTo(BigDecimal.valueOf(1000L)) >= 0) {
         var4 = 3;
         var3 = "k";
      }

      return var0.scaleByPowerOfTen(-var4).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + var3;
   }

   public static record Type_t(String player, BigDecimal amount, String error) {
      public boolean valid() {
         return this.error == null;
      }
   }
}
