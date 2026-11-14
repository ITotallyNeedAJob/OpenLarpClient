package com.larpclient;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextVisitFactory;

public final class DisplayAliasManager {
   private static Map<String, String> P = Map.of();
   private static Object B;
   private static long V;
   private static final ReferenceQueue<StringVisitable> MenuPopupProvider = new ReferenceQueue<>();
   private static final Set<DisplayAliasManager.Type_t> DropletTrail = new HashSet<>();

   public DisplayAliasManager() {
   }

   public static long CAsLong() {
      return V;
   }

   public static void kAsVoid() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      HashMap var1 = new HashMap();
      if (var0.world != null && ml.jAsBoolean()) {
         String var2 = ml.QAsString(var0.getSession().getUuidOrNull());
         CAsVoid(var1, var0.getSession().getUsername(), var2);
         if (var0.player != null) {
            CAsVoid(var1, var0.player.getGameProfile().name(), var2);
         }
      }

      tAsVoid(var0.world, var1);

      for (Reference var3 = MenuPopupProvider.poll(); var3 != null; var3 = MenuPopupProvider.poll()) {
         DropletTrail.remove(var3);
      }
   }

   static void tAsVoid(Object var0, Map<String, String> var1) {
      if (B != var0 || !var1.equals(P)) {
         B = var0;
         P = Map.copyOf(var1);
         V++;
         DropletTrail.clear();
         ClientLog.XAsVoid("Appearance", "Display aliases refreshed: {} participants; chat will reflow", P.size());
      }
   }

   private static void CAsVoid(Map<String, String> var0, String var1, String var2) {
      if (!var2.isEmpty() && !var1.equals(var2)) {
         var0.put(var1.toLowerCase(Locale.ROOT), var2);
      }
   }

   public static Text eAsText(Text var0) {
      return (Text)BAsStringVisitable(var0);
   }

   public static StringVisitable BAsStringVisitable(StringVisitable var0) {
      return !P.isEmpty() && ml.jAsBoolean() && MinecraftClient.getInstance().world != null ? dAsStringVisitable(var0) : var0;
   }

   static StringVisitable dAsStringVisitable(StringVisitable var0) {
      if (DropletTrail.contains(new DisplayAliasManager.Type_t(var0, null))) {
         return var0;
      } else {
         Text var1 = cAsText(var0, P);
         if (var1 != var0) {
            DropletTrail.add(new DisplayAliasManager.Type_t(var1, f));
         }

         return var1;
      }
   }

   public static Text pAsText(UUID var0, String var1, Text var2) {
      String var3 = ml.QAsString(var0);
      if (var3.isEmpty()) {
         return var2;
      } else {
         Text var4 = cAsText(var2, Map.of(var1.toLowerCase(Locale.ROOT), var3));
         if (var4 != var2) {
            DropletTrail.add(new DisplayAliasManager.Type_t(var4, f));
         }

         return var4;
      }
   }

   public static Text cAsText(StringVisitable var0, Map<String, String> var1) {
      if (var1.isEmpty()) {
         return QAsText(var0);
      } else {
         String var2 = var0.getString();
         List var3 = null;
         if (var2.indexOf(167) >= 0) {
            var3 = ZAsList(var0);
            StringBuilder var4 = new StringBuilder();

            for (DisplayAliasManager.Type_u var6 : (Iterable<DisplayAliasManager.Type_u>) (var3)) {
               var4.append(var6.text());
            }

            var2 = var4.toString();
         }

         ArrayList var12 = new ArrayList();
         int var13 = 0;

         while (var13 < var2.length()) {
            if (!FAsBoolean(var2.charAt(var13))) {
               var13++;
            } else {
               int var15 = var13;

               while (var13 < var2.length() && FAsBoolean(var2.charAt(var13))) {
                  var13++;
               }

               String var7 = var1.get(var2.substring(var15, var13).toLowerCase(Locale.ROOT));
               if (var7 != null && !var7.equals(var2.substring(var15, var13))) {
                  var12.add(new DisplayAliasManager.Type_x(var15, var13, var7));
               }
            }
         }

         if (var12.isEmpty()) {
            return QAsText(var0);
         } else {
            if (var3 == null) {
               var3 = ZAsList(var0);
            }

            MutableText var14 = Text.empty();
            int var16 = 0;

            for (DisplayAliasManager.Type_x var8 : (Iterable<DisplayAliasManager.Type_x>) (var12)) {
               hAsVoid(var14, var3, var16, var8.start());
               Style var9 = Style.EMPTY;

               for (DisplayAliasManager.Type_u var11 : (Iterable<DisplayAliasManager.Type_u>) (var3)) {
                  if (var11.start() <= var8.start() && var11.end() > var8.start()) {
                     var9 = var11.style();
                     break;
                  }
               }

               var14.append(Text.literal(var8.name()).setStyle(var9));
               var16 = var8.end();
            }

            hAsVoid(var14, var3, var16, var2.length());
            return var14;
         }
      }
   }

   private static List<DisplayAliasManager.Type_u> ZAsList(StringVisitable var0) {
      ArrayList var1 = new ArrayList();
      int[] var2 = new int[]{0};
      var0.visit((var2xx, var3) -> {
         if (var3.indexOf(167) < 0) {
            var1.add(new DisplayAliasManager.Type_u(var2[0], var2[0] + var3.length(), var2xx, var3));
            var2[0] += var3.length();
         } else {
            StringBuilder var4 = new StringBuilder();
            Style[] var5 = new Style[]{var2xx};
            TextVisitFactory.visitFormatted(var3, var2xx, (var4xx, var5xx, var6) -> {
               if (!var5xx.equals(var5[0])) {
                  KAsVoid(var1, var2, var5[0], var4);
                  var5[0] = var5xx;
               }

               var4.appendCodePoint(var6);
               return true;
            });
            KAsVoid(var1, var2, var5[0], var4);
         }

         return Optional.empty();
      }, Style.EMPTY);
      return var1;
   }

   private static void KAsVoid(List<DisplayAliasManager.Type_u> var0, int[] var1, Style var2, StringBuilder var3) {
      if (!var3.isEmpty()) {
         String var4 = var3.toString();
         var0.add(new DisplayAliasManager.Type_u(var1[0], var1[0] + var4.length(), var2, var4));
         var1[0] += var4.length();
         var3.setLength(0);
      }
   }

   private static Text QAsText(StringVisitable var0) {
      if (var0 instanceof Text) {
         return (Text)var0;
      } else {
         MutableText var1 = Text.empty();
         var0.visit((var1xx, var2x) -> {
            var1.append(Text.literal(var2x).setStyle(var1xx));
            return Optional.empty();
         }, Style.EMPTY);
         return var1;
      }
   }

   private static void hAsVoid(MutableText var0, List<DisplayAliasManager.Type_u> var1, int var2, int var3) {
      for (DisplayAliasManager.Type_u var5 : var1) {
         int var6 = Math.max(var2, var5.start());
         int var7 = Math.min(var3, var5.end());
         if (var6 < var7) {
            var0.append(Text.literal(var5.text().substring(var6 - var5.start(), var7 - var5.start())).setStyle(var5.style()));
         }
      }
   }

   private static boolean FAsBoolean(char var0) {
      return var0 >= 'a' && var0 <= 'z' || var0 >= 'A' && var0 <= 'Z' || var0 >= '0' && var0 <= '9' || var0 == '_';
   }

   private static final class Type_t extends WeakReference<StringVisitable> {
      private final int a;

      Type_t(StringVisitable var1, ReferenceQueue<StringVisitable> var2) {
         super(var1, var2);
         this.a = System.identityHashCode(var1);
      }

      @Override
      public int hashCode() {
         return this.a;
      }

      @Override
      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else {
            if (var1 instanceof DisplayAliasManager.Type_t var2 && this.get() != null && this.get() == var2.get()) {
               return true;
            }

            return false;
         }
      }
   }

   private static record Type_u(int start, int end, Style style, String text) {
   }

   private static record Type_x(int start, int end, String name) {
   }
}
