package com.larpclient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.util.Identifier;

public final class EmoteCatalog {
   public static final String s = "Empty";
   private static final String I = "/assets/larp-client/emotes/";
   public static final String X = "cash_fans";
   public static final String y = "ak47";
   public static final List<EmoteCatalog.Type_a> b = List.of(
      new EmoteCatalog.Type_a("Take the L", "take_the_l.json", null),
      new EmoteCatalog.Type_a("Floss", "floss.json", null),
      new EmoteCatalog.Type_a("Maybach Bounce", "maybach_bounce.json", null, "maybach"),
      new EmoteCatalog.Type_a("Money Spread", "money_spread.json", null, "cash_fans"),
      new EmoteCatalog.Type_a("AK Spray", "ak_spray.json", null, "ak47")
   );
   private static final Map<String, EmotecraftJsonReader> m = new ConcurrentHashMap<>();

   private EmoteCatalog() {
   }

   public static String[] wAsStringArray() {
      String[] var0 = new String[b.size() + 1];
      var0[0] = "Empty";

      for (int var1 = 0; var1 < b.size(); var1++) {
         var0[var1 + 1] = b.get(var1).label();
      }

      return var0;
   }

   public static EmoteCatalog.Type_a LAsA2_Type_a(String var0) {
      for (EmoteCatalog.Type_a var2 : b) {
         if (var2.label().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static EmotecraftJsonReader pAsNh(EmoteCatalog.Type_a var0) {
      EmotecraftJsonReader var1 = m.get(var0.label());
      if (var1 != null) {
         return var1;
      } else {
         try {
            EmotecraftJsonReader var4;
            try (InputStream var2 = EmoteCatalog.class.getResourceAsStream("/assets/larp-client/emotes/" + var0.file())) {
               if (var2 == null) {
                  throw new IllegalStateException("missing /assets/larp-client/emotes/" + var0.file());
               }

               EmotecraftJsonReader var3 = EmotecraftJsonReader.nAsNh(new InputStreamReader(var2, StandardCharsets.UTF_8), var0.loop());
               ClientLog.XAsVoid(
                  "Emotes", "Loaded {} by {}: ticks {}..{}, loop={} from {}, ignored={}", var0.label(), var3.Q, var3.M, var3.y, var3.e, var3.i, var3.EAsSet()
               );
               m.put(var0.label(), var3);
               var4 = var3;
            }

            return var4;
         } catch (Exception var71) {
            ClientLog.lAsVoid("Emotes", "Could not load bundled emote " + var0.label() + "; slot stays unavailable", var71);
            return null;
         }
      }
   }

   public static record Type_a(String label, String file, EmotecraftJsonReader.Type_a loop, String prop) {
      public Type_a(String var1, String var2, EmotecraftJsonReader.Type_a var3) {
         this(var1, var2, var3, null);
      }

      public boolean handProp() {
         return "cash_fans".equals(this.prop) || "ak47".equals(this.prop);
      }

      public Identifier icon() {
         return Identifier.of("larp-client", "textures/gui/emotes/" + this.file.replace(".json", ".png"));
      }

      public Identifier music() {
         return Identifier.of("larp-client", "emote." + this.file.replace(".json", ""));
      }
   }
}
