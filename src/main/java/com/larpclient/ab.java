package com.larpclient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextVisitFactory;

public final class ab {
   public static final BigDecimal TextLayout = new BigDecimal("10000000000000");
   private static final Pattern J = Pattern.compile(
      "(?iu)^[^\\p{L}\\p{N}$]*?(?:money\\s*[:»|]?\\s*[$＄]?\\s*|[$＄]\\s*)([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\s*$"
   );
   private static final Pattern E = Pattern.compile("(?iu)^\\s*(?:money|balance|cash|[$＄])\\s*[:»|]?\\s*[$＄]?\\s*$");
   private static final Pattern K = Pattern.compile("(?iu)^\\s*([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\s*$");
   private static final Pattern P = Pattern.compile("(?i)(?:^|[^a-z])(money|balance|cash)(?:$|[^a-z])");
   private static final Pattern MenuPopupProvider = Pattern.compile("^[\\p{Co} ]+([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kKmMbBtT]?) *$");
   private static final Pattern ChinaHatCodec = Pattern.compile("^[\\p{Co} ]+$");
   private static final Pattern G = Pattern.compile("(?imu)^\\h*[$＄]\\h*([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\h*(?=$|[·•▪|⋅●])");
   private static final Pattern n = Pattern.compile("(?iu)^\\s*You\\h+have\\h+[$＄]\\h*([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\s*$");
   private BigDecimal SmoothInput = BigDecimal.ZERO;

   public ab() {
   }

   public void SAsVoid(BigDecimal var1) {
      if (var1.signum() < 0) {
         throw new IllegalArgumentException("Negative fake balance");
      } else {
         this.t = var1;
      }
   }

   public BigDecimal gAsBigDecimal() {
      return this.t;
   }

   public boolean MAsBoolean(BigDecimal var1) {
      if (var1.signum() <= 0) {
         throw new IllegalArgumentException("Non-positive payment");
      } else if (var1.compareTo(this.t) > 0) {
         return false;
      } else {
         this.t = this.t.subtract(var1);
         return true;
      }
   }

   public static boolean MAsBoolean(Text var0) {
      return E.matcher(IAsAb_Type_p(var0).text()).matches();
   }

   public static boolean sAsBoolean(Text var0, String var1, String var2) {
      String var3 = IAsAb_Type_p(var0).text();
      return MAsBoolean(var0) || ChinaHatCodec.matcher(var3).matches() && (RAsBoolean(var1, var2) || vAsBoolean(var0));
   }

   private static boolean RAsBoolean(String var0, String var1) {
      return P.matcher(var0 + " " + var1).find();
   }

   public static BigDecimal jAsBigDecimal(String var0) {
      String var1 = var0 == null ? "" : var0.strip();
      if (var1.matches("\\$?0+(?:\\.0+)?[kKmMbBtT]?")) {
         return BigDecimal.ZERO;
      } else {
         ChatPayParser.Type_t var2 = ChatPayParser.qAsG_Type_t("pay Local " + var1);
         if (!var2.valid()) {
            throw new IllegalArgumentException("Enter an amount from 0 to 10T, LineSegment.g. 10T, 1B or 61.4.");
         } else if (var2.amount().compareTo(j) > 0) {
            throw new IllegalArgumentException("Fake balance must be between 0 and 10T.");
         } else {
            return var2.amount();
         }
      }
   }

   private static String dAsString(Text var0) {
      StringBuilder var1 = new StringBuilder();
      var0.visit((var1xx, var2) -> {
         TextVisitFactory.visitFormatted(var2, var1xx, (var1xxxx, var2x, var3) -> {
            var1.appendCodePoint(var3);
            return true;
         });
         return Optional.empty();
      }, Style.EMPTY);
      return var1.toString();
   }

   public static Text QAsText(Text var0, BigDecimal var1) {
      if (var0 == null) {
         return null;
      } else {
         ab.Type_p var2 = IAsAb_Type_p(var0);
         Matcher var3 = G.matcher(var2.text());
         return (Text)(!var3.find()
            ? var0
            : AAsMutableText(var0, var2.positions().get(var3.start(1)), var2.positions().get(var3.end(1) - 1) + 1, ChatPayParser.gAsString(var1)));
      }
   }

   public static Text JAsText(Text var0, BigDecimal var1) {
      if (var0 == null) {
         return null;
      } else {
         ab.Type_p var2 = IAsAb_Type_p(var0);
         Matcher var3 = n.matcher(var2.text());
         return (Text)(!var3.matches()
            ? var0
            : AAsMutableText(var0, var2.positions().get(var3.start(1)), var2.positions().get(var3.end(1) - 1) + 1, ChatPayParser.gAsString(var1)));
      }
   }

   public static MutableText gAsMutableText(MutableText var0, BigDecimal var1) {
      ab.Type_p var2 = IAsAb_Type_p(var0);
      Matcher var3 = J.matcher(var2.text());
      return !var3.matches() ? var0 : AAsMutableText(var0, var2.positions().get(var3.start(1)), var2.positions().get(var3.end(1) - 1) + 1, ChatPayParser.gAsString(var1));
   }

   public static MutableText vAsMutableText(MutableText var0, BigDecimal var1, String var2, String var3) {
      MutableText var4 = gAsMutableText(var0, var1);
      if (var4 != var0) {
         return var4;
      } else {
         ab.Type_p var5 = IAsAb_Type_p(var0);
         boolean var6 = RAsBoolean(var2, var3);
         if (var6 && K.matcher(var5.text()).matches()) {
            return KAsMutableText(var0, var1);
         } else {
            Matcher var7 = MenuPopupProvider.matcher(var5.text());
            if (!var7.matches()) {
               return var0;
            } else {
               return !var6 && !vAsBoolean(var0)
                  ? var0
                  : AAsMutableText(var0, var5.positions().get(var7.start(1)), var5.positions().get(var7.end(1) - 1) + 1, ChatPayParser.gAsString(var1));
            }
         }
      }
   }

   private static boolean vAsBoolean(Text var0) {
      boolean[] var1 = new boolean[]{false};
      var0.visit((var1xx, var2) -> {
         TextVisitFactory.visitFormatted(var2, var1xx, (var1xxxx, var2x, var3) -> {
            if (Character.getType(var3) == 18 && var2x.getColor() != null) {
               int var4 = var2x.getColor().getRgb();
               int var5 = var4 >> 16 & 0xFF;
               int var6 = var4 >> 8 & 0xFF;
               int var7 = var4 & 0xFF;
               if (var6 >= 120 && (double)var6 > (double)var5 * 1.3 && (double)var6 > (double)var7 * 1.3) {
                  var1[0] = true;
               }
            }

            return true;
         });
         return Optional.empty();
      }, Style.EMPTY);
      return var1[0];
   }

   private static ab.Type_p IAsAb_Type_p(Text var0) {
      String var1 = dAsString(var0);
      StringBuilder var2 = new StringBuilder();
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < var1.length(); var4++) {
         char var5 = var1.charAt(var4);
         if (Character.getType(var5) != 16) {
            if (Character.isSpaceChar(var5)) {
               var5 = ' ';
            }
            var5 = switch (var5) {
               case 'ɴ' -> 'n';
               case 'ʏ' -> 'y';
               case 'ᴇ' -> 'e';
               case 'ᴍ' -> 'm';
               case 'ᴏ' -> 'o';
               default -> var5;
            };
            var2.append(var5);
            var3.add(var4);
         }
      }

      return new ab.Type_p(var2.toString(), var3);
   }

   public static MutableText KAsMutableText(MutableText var0, BigDecimal var1) {
      if (dAsString(var0).isBlank()) {
         return var0;
      } else {
         MutableText var2 = gAsMutableText(var0, var1);
         return var2 != var0 ? var2 : AAsMutableText(var0, 0, dAsString(var0).length(), ChatPayParser.gAsString(var1));
      }
   }

   private static MutableText AAsMutableText(Text var0, int var1, int var2, String var3) {
      ArrayList var4 = new ArrayList();
      int[] var5 = new int[]{0};
      var0.visit((var2x, var3x) -> {
         StringBuilder var4x = new StringBuilder();
         Style[] var5x = new Style[]{var2x};
         TextVisitFactory.visitFormatted(var3x, var2x, (var4xxxx, var5xxxx, var6x) -> {
            if (!var5x[0].equals(var5xxxx)) {
               NAsVoid(var4, var5, var4x, var5x[0]);
               var5x[0] = var5xxxx;
            }

            var4x.appendCodePoint(var6x);
            return true;
         });
         NAsVoid(var4, var5, var4x, var5x[0]);
         return Optional.empty();
      }, Style.EMPTY);
      MutableText var6 = Text.empty();
      byte var7 = 0;

      for (ab.Type_l var9 : (Iterable<ab.Type_l>) (var4)) {
         if (var9.start() < var1) {
            sAsVoid(var6, var9, var9.start(), Math.min(var1, var9.end()));
         }

         if (var7 == 0 && var9.end() > var1) {
            var6.append(Text.literal(var3).setStyle(var9.style()));
            var7 = 1;
         }

         if (var9.end() > var2) {
            sAsVoid(var6, var9, Math.max(var2, var9.start()), var9.end());
         }
      }

      return var6;
   }

   private static void sAsVoid(MutableText var0, ab.Type_l var1, int var2, int var3) {
      if (var3 > var2) {
         var0.append(Text.literal(var1.text().substring(var2 - var1.start(), var3 - var1.start())).setStyle(var1.style()));
      }
   }

   private static void NAsVoid(List<ab.Type_l> var0, int[] var1, StringBuilder var2, Style var3) {
      if (!var2.isEmpty()) {
         var0.add(new ab.Type_l(var1[0], var1[0] + var2.length(), var2.toString(), var3));
         var1[0] += var2.length();
         var2.setLength(0);
      }
   }

   private static record Type_l(int start, int end, String text, Style style) {
   }

   private static record Type_p(String text, List<Integer> positions) {
   }
}
