package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Stream;

public final class ClientPreferences {
   public b4 Jf = b4.NEBULA;
   public boolean Z = true;
   public boolean S = true;
   public String J = "";
   public String c = "";
   public boolean u = true;
   public WorldTimePreset.Type_d f = WorldTimePreset.Type_d.SUMMER;
   public WorldTimePreset.Type_b L = WorldTimePreset.Type_b.HIGH;
   public double t = 0.33;
   public boolean N = true;
   public boolean l = true;
   public boolean M;
   public boolean b;
   public String A = "Fade";
   public static final List<String> Y = List.of("Reverse", "Shatter", "Crumble", "Slice", "Implode", "CRT");
   public String V = "Reverse";
   public int n = 280;
   public double G = 0.67;
   public static final double q = 0.5;
   public static final double m = 2.0;
   public double JD = 1.0;
   public static final List<String> Q = List.of("Aurora", "Nebula", "Waves", "Gradient", "Dim", "None");
   public String D = "None";
   public static final double W = 0.9;
   public double JE = 0.42;
   public static final int x = 4;
   public int o = 2;
   public static final List<String> P = List.of("Window", "Everywhere");
   public String s = "Window";
   public static final List<String> K = List.of("Filled", "Outline", "Flat");
   public static final List<String> h = List.of("Glow", "Tint", "Text", "None");
   public static final List<String> C = List.of("Pill", "Square", "Minimal");
   public static final List<String> F = List.of("Pill", "Minimal", "Classic");
   public static final double w = 2.0;
   public String r = "Filled";
   public String T = "Glow";
   public String d = "Pill";
   public String R = "Pill";
   public double X = 1.0;
   public boolean B = true;
   public boolean z = true;
   public boolean I = true;
   private static final int U = 2;
   public boolean O = true;
   public BloomQuality.Type_w a = BloomQuality.Type_w.Medium;
   public String i = "World";
   public AppearanceRestoreJournal v = new AppearanceRestoreJournal();
   public ActiveProfileJournal E = new ActiveProfileJournal();
   public SettingShortcuts k = new SettingShortcuts();
   public PanelLayoutCodec y = new PanelLayoutCodec();
   private final Set<String> e = new LinkedHashSet<>();
   private final Set<String> p = new LinkedHashSet<>();
   private final Set<String> JL = new LinkedHashSet<>();
   public boolean H;
   private final Map<String, Long> g = new LinkedHashMap<>();
   public static int j;
   private static final String[] ab = new String[]{
      "clickGuiAnimation",
      "larpPreset",
      "updateSeenReceipt",
      "skySource",
      "legacyClickGui",
      "background",
      "effectsQuality",
      "clickGuiSliderStyle",
      "clickGuiOpacity",
      "israelMode",
      "moduleFavoritesOpen",
      "cinematicMenu",
      "menuQuality",
      "recent",
      "menuSeason",
      "settingBinds",
      "clickGuiLight",
      "clickGuiDuration",
      "clickGuiEnabledStyle",
      "clickGuiHoverLift",
      "moduleFavorites",
      "clickGuiCloseAnimation",
      "clickGuiRounding",
      "clickGuiBlurArea",
      "softParticles",
      "clickGuiBackdrop",
      "updateAutoCheck",
      "motion",
      "clickGuiCardStyle",
      "clickGuiGradient",
      "customMenu",
      "hudLocks",
      "clickGuiSwitchStyle",
      "clickGuiBlur",
      "menuTime",
      "clickGuiDim",
      "clickGuiScale",
      "clickGuiBackdropRevision",
      "clickGuiRipple",
      "discordPresence",
      "clickGuiAccentGlow",
      "updateSkippedRelease",
      "favorites"
   };

   public ClientPreferences() {
   }

   public bd.Type_p pAsBd_Type_p() {
      return new bd.Type_p(this.r, this.T, this.d, this.R, this.X, this.B, this.z, this.I);
   }

   public boolean VAsBoolean(String var1) {
      return this.JL.contains(var1);
   }

   public void dAsVoid(String var1, boolean var2) {
      if (var1 != null && var1.matches("[a-z][a-z0-9_-]{0,63}")) {
         if (var2 && this.JL.size() < 128) {
            this.JL.add(var1);
         } else if (!var2) {
            this.JL.remove(var1);
         }
      } else {
         throw new IllegalArgumentException("Invalid HUD identifier");
      }
   }

   public boolean yAsBoolean(String var1) {
      return this.p.contains(var1) || "CustomSky".equals(var1) && this.p.contains("Sky Shader");
   }

   public void sAsVoid(String var1, boolean var2) {
      if (var1 != null && var1.matches("[A-Za-z0-9 _-]{1,128}") && !var1.isBlank()) {
         if (var2) {
            if (this.p.size() >= 1000 && !this.p.contains(var1)) {
               return;
            }

            this.p.add(var1);
         } else {
            this.p.remove(var1);
            if (var1.equals("CustomSky")) {
               this.p.remove("Sky Shader");
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid module identifier");
      }
   }

   public boolean cAsBoolean(String var1) {
      return this.e.contains(var1);
   }

   public void kAsVoid(String var1, boolean var2) {
      ProfileCodec.IAsString(var1);
      if (var2) {
         this.e.add(var1);
      } else {
         this.e.remove(var1);
      }
   }

   public long iAsLong(String var1) {
      return this.g.getOrDefault(var1, 0L);
   }

   public void oAsVoid(String var1, long var2) {
      ProfileCodec.IAsString(var1);
      if (var2 > 0L) {
         this.g.put(var1, var2);
      }
   }

   public void oAsVoid(String var1) {
      this.e.remove(var1);
      this.g.remove(var1);
   }

   public void BAsVoid(String var1, String var2) {
      ProfileCodec.IAsString(var2);
      this.v.HAsVoid(var1, var2);
      this.E.JAsVoid(var1, var2);
      if (this.e.remove(var1)) {
         this.e.add(var2);
      }

      Long var3 = this.g.remove(var1);
      if (var3 != null) {
         this.g.put(var2, var3);
      }
   }

   public List<String> EAsList(List<String> var1, String var2, String var3) {
      Stream<String> var4 = var1.stream().filter(var1x -> var1x.toLowerCase(Locale.ROOT).contains(var2.toLowerCase(Locale.ROOT)));
      if (var3.equals("Favorites")) {
         var4 = var4.filter(this::cAsBoolean);
      }

      if (var3.equals("Recent")) {
         var4 = var4.filter(var1x -> this.iAsLong(var1x) > 0L)
            .sorted(Comparator.comparingLong(this::iAsLong).reversed().thenComparing(Comparator.naturalOrder()));
      }

      return var4.toList();
   }

   public JsonObject tAsJsonObject() {
      JsonObject var3 = new JsonObject();
      String[] var8 = ab;
      var3.addProperty(var8[5], this.Jf.x);
      var3.addProperty(var8[27], this.Z);
      var3.addProperty(var8[26], this.S);
      var3.addProperty(var8[41], this.J);
      var3.addProperty(var8[2], this.c);
      var3.addProperty(var8[14], this.f.name());
      var3.addProperty(var8[12], this.L.name());
      var3.addProperty(var8[34], this.t);
      var3.addProperty(var8[11], this.N);
      var3.addProperty(var8[30], this.l);
      var3.addProperty(var8[39], this.u);
      var3.addProperty(var8[24], this.O);
      var3.addProperty(var8[6], this.a.name());
      var3.addProperty(var8[3], this.i);
      var3.addProperty(var8[16], this.M);
      var3.addProperty(var8[29], this.b);
      var3.addProperty(var8[0], this.A);
      var3.addProperty(var8[21], this.V);
      int var10000 = j;
      var3.addProperty(var8[17], this.n);
      var3.addProperty(var8[8], this.G);
      var3.addProperty(var8[36], this.JD);
      var3.addProperty(var8[25], this.D);
      var3.addProperty(var8[37], 2);
      var3.addProperty(var8[35], this.JE);
      var3.addProperty(var8[33], this.o);
      var3.addProperty(var8[23], this.s);
      var3.addProperty(var8[28], this.r);
      var3.addProperty(var8[18], this.T);
      var3.addProperty(var8[32], this.d);
      var3.addProperty(var8[7], this.R);
      var3.addProperty(var8[22], this.X);
      var3.addProperty(var8[40], this.B);
      var3.addProperty(var8[38], this.z);
      var3.addProperty(var8[19], this.I);
      JsonArray var4 = new JsonArray();
      this.e.forEach(var4::add);
      var3.add(var8[42], var4);
      JsonObject var5 = new JsonObject();
      this.g.forEach(var5::addProperty);
      var3.add(var8[13], var5);
      JsonArray var6 = new JsonArray();
      this.p.forEach(var6::add);
      var3.add(var8[20], var6);
      var3.addProperty(var8[10], this.H);
      JsonArray var7 = new JsonArray();
      this.JL.forEach(var7::add);
      var3.add(var8[31], var7);
      var3.add(var8[9], this.v.kAsJsonObject());
      var3.add(var8[1], this.E.XAsJsonObject());
      var3.add(var8[15], this.k.uAsJsonObject());
      var3.add(var8[4], this.y.UAsJsonObject());
      if (var10000 != 0) {
         int var2 = ProfileManager.sm;
         ProfileManager.sm = ++var2;
      }

      return var3;
   }

   public static ClientPreferences LAsN7(JsonObject var0) {
      ClientPreferences var1 = new ClientPreferences();

      try {
         if (var0.has("updateAutoCheck")) {
            var1.S = ProfileCodec.SAsBoolean(var0.get("updateAutoCheck"));
         }
      } catch (RuntimeException var44) {
      }

      try {
         String var2 = ProfileCodec.MAsString(var0, "updateSkippedRelease", 128);
         if (var2.isEmpty() || var2.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.J = var2;
         }
      } catch (RuntimeException var51) {
      }

      try {
         String var53 = ProfileCodec.MAsString(var0, "updateSeenReceipt", 257);
         if (var53.isEmpty() || var53.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}:[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.c = var53;
         }
      } catch (RuntimeException var50) {
      }

      try {
         if (var0.has("discordPresence")) {
            var1.u = ProfileCodec.SAsBoolean(var0.get("discordPresence"));
         }
      } catch (RuntimeException var43) {
      }

      try {
         String var54 = ProfileCodec.MAsString(var0, "skySource", 16);
         if (Set.of("World", "Shader").contains(var54)) {
            var1.i = var54;
         }
      } catch (RuntimeException var42) {
      }

      try {
         var1.f = WorldTimePreset.Type_d.valueOf(ProfileCodec.MAsString(var0, "menuSeason", 16));
      } catch (RuntimeException var41) {
      }

      try {
         var1.L = WorldTimePreset.Type_b.valueOf(ProfileCodec.MAsString(var0, "menuQuality", 16));
      } catch (RuntimeException var40) {
      }

      try {
         ProfileCodec.YAsVoid(var0.get("menuTime"), 0.0, 1.0, false);
         var1.t = var0.get("menuTime").getAsDouble();
      } catch (RuntimeException var39) {
      }

      try {
         if (var0.has("cinematicMenu")) {
            var1.N = ProfileCodec.SAsBoolean(var0.get("cinematicMenu"));
         }
      } catch (RuntimeException var38) {
      }

      try {
         if (var0.has("customMenu")) {
            var1.l = ProfileCodec.SAsBoolean(var0.get("customMenu"));
         }
      } catch (RuntimeException var37) {
      }

      try {
         if (var0.has("clickGuiGradient")) {
            var1.b = ProfileCodec.SAsBoolean(var0.get("clickGuiGradient"));
         }
      } catch (RuntimeException var36) {
      }

      try {
         if (var0.has("clickGuiLight")) {
            var1.M = ProfileCodec.SAsBoolean(var0.get("clickGuiLight"));
         }
      } catch (RuntimeException var35) {
      }

      try {
         String var55 = ProfileCodec.MAsString(var0, "clickGuiAnimation", 16);
         if (xb.L.contains(var55)) {
            var1.A = var55;
         }
      } catch (RuntimeException var34) {
      }

      try {
         String var56 = ProfileCodec.MAsString(var0, "clickGuiCloseAnimation", 16);
         if (Y.contains(var56)) {
            var1.V = var56;
         }
      } catch (RuntimeException var33) {
      }

      try {
         ProfileCodec.YAsVoid(var0.get("clickGuiDuration"), 100.0, 1000.0, true);
         var1.n = var0.get("clickGuiDuration").getAsInt();
      } catch (RuntimeException var32) {
      }

      try {
         ProfileCodec.YAsVoid(var0.get("clickGuiOpacity"), 0.0, 1.0, false);
         var1.G = var0.get("clickGuiOpacity").getAsDouble();
      } catch (RuntimeException var31) {
      }

      try {
         if (var0.has("clickGuiScale")) {
            ProfileCodec.YAsVoid(var0.get("clickGuiScale"), 0.5, 2.0, false);
            var1.JD = var0.get("clickGuiScale").getAsDouble();
         }
      } catch (RuntimeException var30) {
      }

      try {
         String var57 = ProfileCodec.MAsString(var0, "clickGuiBackdrop", 16);
         byte var3 = 0;

         try {
            var3 = (byte)(var0.get("clickGuiBackdropRevision").getAsInt() >= 2 ? 1 : 0);
         } catch (RuntimeException var29) {
         }

         if (var57.equals("Aurora") && var3 == 0) {
            ClientLog.XAsVoid("GUI", "Moved the former default Aurora backdrop to None");
         } else if (Q.contains(var57)) {
            var1.D = var57;
         }
      } catch (RuntimeException var49) {
      }

      try {
         if (var0.has("clickGuiDim")) {
            ProfileCodec.YAsVoid(var0.get("clickGuiDim"), 0.0, 0.9, false);
            var1.JE = var0.get("clickGuiDim").getAsDouble();
         }
      } catch (RuntimeException var28) {
      }

      try {
         if (var0.has("clickGuiBlur")) {
            ProfileCodec.YAsVoid(var0.get("clickGuiBlur"), 0.0, 4.0, true);
            var1.o = var0.get("clickGuiBlur").getAsInt();
         }
      } catch (RuntimeException var27) {
      }

      try {
         String var58 = ProfileCodec.MAsString(var0, "clickGuiBlurArea", 16);
         if (P.contains(var58)) {
            var1.s = var58;
         }
      } catch (RuntimeException var26) {
      }

      try {
         String var59 = ProfileCodec.MAsString(var0, "clickGuiCardStyle", 16);
         if (K.contains(var59)) {
            var1.r = var59;
         }
      } catch (RuntimeException var25) {
      }

      try {
         String var60 = ProfileCodec.MAsString(var0, "clickGuiEnabledStyle", 16);
         if (h.contains(var60)) {
            var1.T = var60;
         }
      } catch (RuntimeException var24) {
      }

      try {
         String var61 = ProfileCodec.MAsString(var0, "clickGuiSwitchStyle", 16);
         if (C.contains(var61)) {
            var1.d = var61;
         }
      } catch (RuntimeException var23) {
      }

      try {
         String var62 = ProfileCodec.MAsString(var0, "clickGuiSliderStyle", 16);
         if (F.contains(var62)) {
            var1.R = var62;
         }
      } catch (RuntimeException var22) {
      }

      try {
         if (var0.has("clickGuiRounding")) {
            ProfileCodec.YAsVoid(var0.get("clickGuiRounding"), 0.0, 2.0, false);
            var1.X = var0.get("clickGuiRounding").getAsDouble();
         }
      } catch (RuntimeException var21) {
      }

      try {
         if (var0.has("clickGuiAccentGlow")) {
            var1.B = ProfileCodec.SAsBoolean(var0.get("clickGuiAccentGlow"));
         }
      } catch (RuntimeException var20) {
      }

      try {
         if (var0.has("clickGuiRipple")) {
            var1.z = ProfileCodec.SAsBoolean(var0.get("clickGuiRipple"));
         }
      } catch (RuntimeException var19) {
      }

      try {
         if (var0.has("clickGuiHoverLift")) {
            var1.I = ProfileCodec.SAsBoolean(var0.get("clickGuiHoverLift"));
         }
      } catch (RuntimeException var18) {
      }

      try {
         var1.Jf = b4.AAsB4(ProfileCodec.MAsString(var0, "background", 32));
      } catch (RuntimeException var17) {
      }

      try {
         if (var0.has("softParticles")) {
            var1.O = ProfileCodec.SAsBoolean(var0.get("softParticles"));
         }
      } catch (RuntimeException var16) {
      }

      try {
         var1.a = BloomQuality.Type_w.valueOf(ProfileCodec.MAsString(var0, "effectsQuality", 16));
      } catch (RuntimeException var15) {
      }

      try {
         var1.Z = ProfileCodec.SAsBoolean(var0.get("motion"));
      } catch (RuntimeException var14) {
      }

      try {
         JsonArray var63 = var0.getAsJsonArray("favorites");
         if (var63.size() <= 1000) {
            for (JsonElement var4 : var63) {
               try {
                  var1.kAsVoid(ProfileCodec.IAsString(var4.getAsString()), true);
               } catch (RuntimeException var13) {
               }
            }
         }
      } catch (RuntimeException var48) {
      }

      try {
         JsonObject var64 = var0.getAsJsonObject("recent");
         if (var64.size() <= 1000) {
            for (Entry var72 : var64.entrySet()) {
               try {
                  ProfileCodec.YAsVoid((JsonElement)var72.getValue(), 1.0, 9.223372E18F, true);
                  var1.oAsVoid((String)var72.getKey(), ((JsonElement)var72.getValue()).getAsLong());
               } catch (RuntimeException var12) {
               }
            }
         }
      } catch (RuntimeException var47) {
      }

      try {
         var1.H = ProfileCodec.SAsBoolean(var0.get("moduleFavoritesOpen"));
      } catch (RuntimeException var11) {
      }

      try {
         JsonArray var65 = var0.getAsJsonArray("hudLocks");
         if (var65.size() <= 128) {
            for (JsonElement var73 : var65) {
               try {
                  if (var73.isJsonPrimitive() && var73.getAsJsonPrimitive().isString()) {
                     var1.dAsVoid(var73.getAsString(), true);
                  }
               } catch (RuntimeException var10) {
               }
            }
         }
      } catch (RuntimeException var46) {
      }

      try {
         JsonArray var66 = var0.getAsJsonArray("moduleFavorites");
         if (var66.size() <= 1000) {
            for (JsonElement var74 : var66) {
               try {
                  if (var74.isJsonPrimitive() && var74.getAsJsonPrimitive().isString()) {
                     var1.sAsVoid(var74.getAsString(), true);
                  }
               } catch (RuntimeException var9) {
               }
            }
         }
      } catch (RuntimeException var45) {
      }

      try {
         if (var0.has("settingBinds")) {
            var1.k = SettingShortcuts.aAsXn(var0.get("settingBinds"));
         }
      } catch (RuntimeException var8) {
         ClientLog.oAsVoid("Input", "Invalid setting shortcuts ignored: {}", var8.toString());
      }

      try {
         if (var0.has("legacyClickGui")) {
            var1.y = PanelLayoutCodec.OAsD2(var0.getAsJsonObject("legacyClickGui"));
         }
      } catch (RuntimeException var7) {
         ClientLog.oAsVoid("GUI", "Invalid legacy ClickGUI preference ignored: {}", var7.toString());
      }

      try {
         if (var0.has("israelMode")) {
            var1.v = AppearanceRestoreJournal.sAsX5(var0.getAsJsonObject("israelMode"));
         }
      } catch (RuntimeException var61) {
         ClientLog.oAsVoid("Israel mode", "Invalid local preference ignored: {}", var61.toString());
      }

      String var67 = var0.has("larpPreset") ? "larpPreset" : "moonMode";

      try {
         if (var0.has(var67)) {
            var1.E = ActiveProfileJournal.FAsAs(var0.getAsJsonObject(var67));
            if (!var0.has("larpPreset")) {
               ClientLog.XAsVoid("Larp Preset", "Loaded legacy cosmetic preset journal; next preference save uses larpPreset");
            }
         }
      } catch (RuntimeException var5) {
         ClientLog.oAsVoid("Larp Preset", "Invalid local preference ignored: {}", var5.toString());
      }

      return var1;
   }
}
