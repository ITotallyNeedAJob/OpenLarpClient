package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.larpclient.module.Animations;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

public final class ModuleMigrationNames {
   private static final List<String> b = List.of(
      "NameProtect",
      "AntiAFK",
      "Aimbot",
      "HitBoxes",
      "InventoryMove",
      "LongJump",
      "Spider",
      "Velocity",
      "Grim anti-KB",
      "Derp",
      "Spin",
      "Particles",
      "NoHurtCam",
      "FastEat",
      "Render Animations"
   );
   private static final List<String> N = List.of(
      "FOV",
      "Players",
      "Mobs",
      "Animals",
      "Invisibles",
      "FOV Cone",
      "Indicator Width",
      "Visible Lines",
      "Hit Zone",
      "Zone Width",
      "Zone Opacity",
      "Zone Fill",
      "Zone Bloom",
      "Zone Motion"
   );
   private static final List<String> a = List.of("Bitcoin", "Solana", "Ethereum", "Litecoin");
   private static final Map<String, Double> V = Map.ofEntries(
      Map.entry("Item Scale", 0.4),
      Map.entry("Hand X", 0.0),
      Map.entry("Hand Y", 0.2),
      Map.entry("Hand Z", 0.0),
      Map.entry("Pitch", 0.0),
      Map.entry("Yaw", 0.0),
      Map.entry("Roll", 0.0),
      Map.entry("Right X", 0.0),
      Map.entry("Right Y", 0.0),
      Map.entry("Right Z", 0.0),
      Map.entry("Left X", 0.0),
      Map.entry("Left Y", 0.0),
      Map.entry("Left Z", 0.0)
   );
   private static final List<String> J = List.of("Custom Position", "Offsets only with aura");
   private static final List<String> R = List.of(
      "Fast Place",
      "Place Delay",
      "Break Recovery",
      "Work with Crystal",
      "Work with Sword",
      "Work with Totem",
      "Work with Fist",
      "Switch Delay Min",
      "Switch Delay Max",
      "Place Delay Min",
      "Place Delay Max",
      "Break Delay Min",
      "Break Delay Max",
      "Swap Back",
      "Swap Back Delay Min",
      "Swap Back Delay Max",
      "Highlight",
      "Highlight Color Mode",
      "Highlight Color",
      "Highlight Opacity",
      "Highlight Thickness",
      "Highlight Fill",
      "Highlight Fill Opacity",
      "Highlight Glow"
   );
   public static boolean r;
   private static final String[] c = new String[]{
      "Staff Heads", "Staff Heads", "Staff List", "enabled", "Staff List", "Player Heads", "HUD", "keybind", "enabled", "settings", "settings"
   };

   public static JsonObject oAsJsonObject(JsonObject var0) {
      JsonObject var1 = var0.deepCopy();
      JsonObject var2 = var1.has("modules") ? var1.getAsJsonObject("modules") : var1;
      b.forEach(var2::remove);

      for (String var6 : new String[]{"Base Finder", "SusChunkFinder"}) {
         if (var2.has(var6) && var2.get(var6).isJsonObject()) {
            JsonObject var7 = var2.getAsJsonObject(var6);
            JsonObject var8 = lAsJsonObject(var7);
            int var9 = var6.equals("Base Finder") ? 320 : 128;

            for (String var13 : new String[]{"Min Scan Y", "Max Scan Y"}) {
               JsonElement var14 = var8.get(var13);
               if (var14 != null && var14.isJsonPrimitive() && var14.getAsJsonPrimitive().isNumber()) {
                  double var15 = var14.getAsDouble();
                  if (Double.isFinite(var15) && var15 > 60.0 && var15 <= (double)var9) {
                     XAsVoid(var7, var13, var14);
                     var8.addProperty(var13, 60);
                  }
               }
            }
         }
      }

      if (var2.has("AltLook") && var2.get("AltLook").isJsonObject()) {
         JsonObject var17 = var2.remove("AltLook").getAsJsonObject();
         boolean var22 = var2.has("Freelook");
         JsonObject var27 = var22 ? var2.getAsJsonObject("Freelook") : var17.deepCopy();
         JsonObject var30 = lAsJsonObject(var27);
         JsonObject var33 = lAsJsonObject(var17);
         if (var22) {
            if (var17.has("enabled")) {
               XAsVoid(var27, "AltLook Enabled", var17.get("enabled"));
            }

            if (var17.has("keybind")) {
               XAsVoid(var27, "AltLook Keybind", var17.get("keybind"));
            }

            if (var33.has("Distance")) {
               XAsVoid(var27, "AltLook Distance", var33.get("Distance"));
            }
         }

         for (String var39 : new String[]{"Sensitivity", "Smoothing"}) {
            if (var33.has(var39)) {
               XAsVoid(var27, var39, var33.get(var39));
            }

            var30.remove(var39);
         }

         var2.add("Freelook", var27);
      }

      WAsVoid(var2, "Criticals", "Mode", "Packet", "Jump");
      WAsVoid(var2, "Reach", "Mode", "Spoof", "Normal");
      WAsVoid(var2, "Reach", "Mode", "Backtrack", "Normal");
      if (var2.has("Reach") && var2.get("Reach").isJsonObject()) {
         JsonObject var18 = var2.getAsJsonObject("Reach");
         JsonObject var23 = lAsJsonObject(var18);

         for (String var36 : new String[]{"LagComp", "PacketSpoof", "Backtrack"}) {
            if (var23.has(var36)) {
               XAsVoid(var18, var36, var23.remove(var36));
            }
         }
      }

      dAsVoid(var2);
      if (var2.has("Animations") && var2.get("Animations").isJsonObject()) {
         JsonObject var19 = var2.getAsJsonObject("Animations");
         JsonObject var24 = lAsJsonObject(var19);
         if (var24.has("Swing") && var24.get("Swing").isJsonPrimitive() && var24.getAsJsonPrimitive("Swing").isString()) {
            String var29 = var24.get("Swing").getAsString();
            String var32 = Animations.KAsString(var29);
            if (!var29.equals(var32)) {
               XAsVoid(var19, "Mode", var24.get("Swing"));
            }
         }

         Animations.QAsVoid(var24);
      }

      GAsVoid(var2);
      if (var2.has("Aim Assist") && var2.get("Aim Assist").isJsonObject()) {
         var2.add("Aim Assist", RAsJsonObject(var2.getAsJsonObject("Aim Assist")));
      }

      if (var2.get("Player Model") instanceof JsonObject var20) {
         var2.add("Player Model", ZAsJsonObject(var20));
      }

      if (var2.get("Jesus") instanceof JsonObject var21) {
         var2.add("Jesus", nAsJsonObject(var21));
      }

      eAsVoid(var2);
      IAsVoid(var2);
      zAsVoid(var2);
      vAsVoid(var2);
      ScanVisualizationSettings.bAsVoid(var2);
      return var1;
   }

   private static void dAsVoid(JsonObject var0) {
      if (var0.get("KillAura") instanceof JsonObject var1) {
         JsonObject var7 = var1.get("settings") instanceof JsonObject var3 ? var3 : new JsonObject();
         rAsVoid(var0, var1, var7);
         if (!var0.has("Aura")) {
            JsonObject var8 = new JsonObject();
            if (var1.get("enabled") instanceof JsonPrimitive var9 && var9.isBoolean()) {
               var8.add("enabled", var9.deepCopy());
            }

            if (var1.get("keybind") instanceof JsonPrimitive var10 && var10.isNumber()) {
               var8.add("keybind", var10.deepCopy());
            }

            JsonObject var11 = new JsonObject();

            for (String var6 : N) {
               if (var7.has(var6)) {
                  var11.add(var6, var7.get(var6).deepCopy());
               }
            }

            if (var7.has("Range")) {
               var11.add("Attack Range", var7.get("Range").deepCopy());
            }

            var8.add("settings", var11);
            var0.add("Aura", var8);
            ClientLog.GAsVoid("Config", "Replaced KillAura with Aura; shared range, target and indicator values carried over");
         }

         var0.remove("KillAura");
      }
   }

   private static void rAsVoid(JsonObject var0, JsonObject var1, JsonObject var2) {
      if (!var0.has("TargetAura") && var2.has("Indicator")) {
         if (var2.remove("Indicator") instanceof JsonPrimitive var4 && var4.isBoolean()) {
            JsonObject var5 = new JsonObject();
            var5.addProperty("enabled", var4.getAsBoolean() && var1.get("enabled") instanceof JsonPrimitive var6 && var6.isBoolean() && var6.getAsBoolean());
            JsonObject var8 = new JsonObject();
            var8.addProperty("Style", "Orbit");
            var8.addProperty("Source", "Aura");
            var5.add("settings", var8);
            var0.add("TargetAura", var5);
            ClientLog.GAsVoid("Config", "Migrated KillAura.Indicator to TargetAura (explicit TargetAura settings take priority)");
            return;
         }

         ClientLog.oAsVoid("Config", "Invalid legacy target indicator ignored");
      }
   }

   private static void zAsVoid(JsonObject var0) {
      int var10000 = ClientPreferences.j;
      String[] var9 = c;
      if (var0.get(var9[6]) instanceof JsonObject var2 && var2.get(var9[9]) instanceof JsonObject var3 && var10000 == 0) {
         JsonPrimitive var10000x;
         label72: {
            var9 = c;
            if (var3.get(var9[4]) instanceof JsonPrimitive var5 && var5.isBoolean()) {
               var10000x = var5;
               break label72;
            }

            var10000x = null;
         }

         JsonPrimitive var11;
         label67: {
            var11 = var10000x;
            var9 = c;
            if (var3.get(var9[1]) instanceof JsonPrimitive var13 && var13.isBoolean()) {
               var10000x = var13;
               break label67;
            }

            var10000x = null;
         }

         JsonPrimitive var12 = var10000x;
         if (var11 != null || var12 != null) {
            if (var11 != null) {
               var9 = c;
               XAsVoid(var2, var9[2], var3.remove(var9[2]));
            }

            if (var12 != null) {
               var9 = c;
               XAsVoid(var2, var9[0], var3.remove(var9[0]));
            }

            var9 = c;
            if (!var0.has(var9[2])) {
               boolean var29;
               label54: {
                  var9 = c;
                  if (var2.get(var9[3]) instanceof JsonPrimitive var15 && var15.isBoolean() && !var15.getAsBoolean()) {
                     var29 = true;
                     break label54;
                  }

                  var29 = false;
               }

               int var14 = !var29 ? 1 : 0;
               JsonObject var16 = new JsonObject();
               var9 = c;
               var16.addProperty(var9[8], var14 != 0 && var11 != null && var11.getAsBoolean());
               var9 = c;
               var16.addProperty(var9[7], -1);
               JsonObject var17 = new JsonObject();
               if (var12 != null) {
                  var17.add(var9[5], var12.deepCopy());
               }

               var9 = c;
               var16.add(var9[10], var17);
               var0.add(var9[2], var16);
            }
         }
      }
   }

   private static void vAsVoid(JsonObject var0) {
      if (var0.get("Pets") instanceof JsonObject var1 && var1.get("settings") instanceof JsonObject var2) {
         byte var10000;
         label70: {
            if (var2.get("Realism") instanceof JsonPrimitive var4 && var4.isBoolean() && var4.getAsBoolean()) {
               var10000 = 1;
               break label70;
            }

            var10000 = 0;
         }

         int var10 = var10000;
         String var11 = null;

         for (String var6 : List.of("Pet", "Realistic Pet")) {
            JsonElement var17 = var2.get(var6);
            if (var17 instanceof JsonPrimitive) {
               JsonPrimitive var7 = (JsonPrimitive)var17;
               if (var7.isString() && a.contains(var7.getAsString())) {
                  if (var6.equals(var10 != 0 ? "Realistic Pet" : "Pet")) {
                     var11 = var7.getAsString();
                  }

                  XAsVoid(var1, var6, var2.remove(var6));
               }
            }
         }

         if (var11 != null && !var0.has("Crypto Pets")) {
            JsonObject var13 = new JsonObject();
            var13.addProperty("enabled", qAsBoolean(var1));
            var13.addProperty("keybind", -1);
            JsonObject var14 = new JsonObject();

            for (String var17 : a) {
               var14.addProperty(var17, var17.equals(var11));
            }

            var14.addProperty("Count", 1);
            if (var2.get(var10 != 0 ? "Realistic Size" : "Size") instanceof JsonPrimitive var16 && var16.isNumber() && Double.isFinite(var16.getAsDouble())) {
               var14.addProperty("Size", Math.clamp(var16.getAsDouble(), 0.5, 2.5));
            }

            var13.add("settings", var14);
            var0.add("Crypto Pets", var13);
            if (qAsBoolean(var1)) {
               XAsVoid(var1, "Coin enabled", var1.get("enabled"));
               var1.addProperty("enabled", false);
            }
         }
      }
   }

   public static JsonObject RAsJsonObject(JsonObject var0) {
      JsonObject var1 = var0.deepCopy();
      JsonObject var2 = lAsJsonObject(var1);
      JsonElement var3 = var2.remove("Mode");
      if (var3 != null) {
         if (!var3.isJsonPrimitive() || var3.toString().length() > 128 || var3.getAsJsonPrimitive().isNumber() && !Double.isFinite(var3.getAsDouble())) {
            ClientLog.oAsVoid("Config", "Invalid retired Aim Assist.Mode ignored");
         } else {
            XAsVoid(var1, "Mode", var3);
         }
      }

      return var1;
   }

   public static JsonObject nAsJsonObject(JsonObject var0) {
      JsonObject var1 = var0.deepCopy();
      JsonElement var2 = lAsJsonObject(var1).remove("Mode");
      if (var2 == null
         || !var2.isJsonPrimitive()
         || var2.toString().length() > 128
         || var2.getAsJsonPrimitive().isNumber() && !Double.isFinite(var2.getAsDouble())) {
         if (var2 != null) {
            ClientLog.oAsVoid("Config", "Invalid retired Jesus.Mode ignored");
         }
      } else {
         XAsVoid(var1, "Mode", var2);
      }

      return var1;
   }

   public static JsonObject ZAsJsonObject(JsonObject var0) {
      JsonObject var1 = var0.deepCopy();
      JsonObject var2 = lAsJsonObject(var1);

      for (String var4 : List.of("Model", "Through Walls")) {
         JsonElement var5 = var2.remove(var4);
         if (var5 != null) {
            if (!var5.isJsonPrimitive() || var5.toString().length() > 128 || var5.getAsJsonPrimitive().isNumber() && !Double.isFinite(var5.getAsDouble())) {
               ClientLog.oAsVoid("Config", "Invalid retired Player Model.{} ignored", var4);
            } else {
               XAsVoid(var1, var4, var5);
            }
         }
      }

      return var1;
   }

   private static void GAsVoid(JsonObject var0) {
      if (var0.get("Animations") instanceof JsonObject var1) {
         JsonObject var16 = eAsJsonObject(var1);
         int var3 = !J.stream().anyMatch(var16::has) && !V.keySet().stream().anyMatch(var16::has) ? 0 : 1;
         if (var3 != 0 || qAsBoolean(var1)) {
            HashMap var4;
            byte var10000;
            label121: {
               var4 = new HashMap();
               V.forEach((var2, var3x) -> {
                  Double var10002;
                  label14: {
                     if (var16.get(var2) instanceof JsonPrimitive var4x && var4x.isNumber() && Double.isFinite(var4x.getAsDouble())) {
                        var10002 = var4x.getAsDouble();
                        break label14;
                     }

                     var10002 = var3x;
                  }

                  var4.put(var2, var10002);
               });
               label97:
               if (var16.get("Custom Position") instanceof JsonPrimitive var7 && var7.isBoolean() && var7.getAsBoolean()) {
                  if (var16.get("Offsets only with aura") instanceof JsonPrimitive var6 && var6.isBoolean() && var6.getAsBoolean()) {
                     break label97;
                  }

                  var10000 = 1;
                  break label121;
               }

               var10000 = 0;
            }

            int var5 = var10000;

            for (String var19 : Stream.concat(V.keySet().stream(), J.stream()).sorted().toList()) {
               JsonElement var23 = var16.remove(var19);
               if (var23 instanceof JsonPrimitive var9 && var9.toString().length() <= 128 && (!var9.isNumber() || Double.isFinite(var9.getAsDouble()))) {
                  XAsVoid(var1, var19, var9);
                  continue;
               }

               if (var23 != null) {
                  ClientLog.oAsVoid("Config", "Invalid retired Animations.{} ignored", var19);
               }
            }

            if (!var0.has("ViewModel")) {
               JsonObject var18 = new JsonObject();

               for (String var10 : new String[]{"Main", "Off"}) {
                  String var11 = var10.equals("Main") ? "Right" : "Left";
                  var18.addProperty(var10 + " Scale", Math.clamp((Double)var4.get("Item Scale"), 0.1, 2.0));
                  var18.addProperty(var10 + " X", Math.clamp((Double)var4.get("Hand X") + (var5 != 0 ? (Double)var4.get(var11 + " X") : 0.0), -2.5, 2.5));
                  var18.addProperty(var10 + " Y", Math.clamp((Double)var4.get("Hand Y") + (var5 != 0 ? (Double)var4.get(var11 + " Y") : 0.0), -2.0, 2.0));
                  var18.addProperty(var10 + " Z", Math.clamp((Double)var4.get("Hand Z") + (var5 != 0 ? (Double)var4.get(var11 + " Z") : 0.0), -2.0, 1.0));

                  for (String var15 : new String[]{"Pitch", "Yaw", "Roll"}) {
                     var18.addProperty(var10 + " " + var15, Math.clamp((Double)var4.get(var15), -180.0, 180.0));
                  }
               }

               JsonObject var21 = new JsonObject();
               var21.addProperty("enabled", qAsBoolean(var1));
               var21.addProperty("keybind", -1);
               var21.add("settings", var18);
               var0.add("ViewModel", var21);
               ClientLog.GAsVoid("Config", "Moved SwingAnimation hand size and position to ViewModel; old values archived");
            }
         }
      }
   }

   private static void eAsVoid(JsonObject var0) {
      if (var0.get("Crystal Optimizer") instanceof JsonObject var1 && var1.get("settings") instanceof JsonObject var2) {
         for (String var4 : R) {
            if (var2.get(var4) instanceof JsonPrimitive var5) {
               XAsVoid(var1, var4, var5);
               var2.remove(var4);
            }
         }
      }
   }

   private static void IAsVoid(JsonObject var0) {
      JsonObject var1 = var0.get("AutoLunge") instanceof JsonObject var2 ? var2 : null;
      JsonObject var12 = var0.get("Spear Swap") instanceof JsonObject var13 ? var13 : null;
      if (var1 != null || var12 != null) {
         if (!var0.has("Lunge Swap")) {
            int var14 = var1 == null ? -1 : jAsInt(eAsJsonObject(var1).get("Lunge Bind"));
            int var15 = var12 == null ? -1 : iAsInt(eAsJsonObject(var12));
            JsonObject var5 = VAsJsonObject(var1, var14, var12, var15);
            JsonObject var6 = new JsonObject();
            var6.addProperty("enabled", qAsBoolean(var1) || qAsBoolean(var12));
            if (var5.get("keybind") instanceof JsonPrimitive var7 && var7.isNumber()) {
               var6.add("keybind", var7.deepCopy());
            }

            JsonObject var16 = new JsonObject();
            int var17 = var5 == var1 ? var14 : var15;
            if (var17 < 0) {
               var17 = var14 >= 0 ? var14 : var15;
            }

            if (var17 >= 0) {
               var16.addProperty("Activation Key", var17);
            }

            if ((var5 == var1 ? eAsJsonObject(var1).get("Swap Back") : eAsJsonObject(var12).get("Switch Back")) instanceof JsonPrimitive var10
               && var10.isBoolean()) {
               var16.add("Swap Back", var10.deepCopy());
            }

            if (var1 != null && eAsJsonObject(var1).get("Bind Hold Loop") instanceof JsonPrimitive var18 && var18.isBoolean()) {
               var16.add("Hold To Lunge", var18.deepCopy());
            }

            var6.add("settings", var16);
            var0.add("Lunge Swap", var6);
            ClientLog.GAsVoid("Config", "Replaced AutoLunge and Spear Swap with Lunge Swap; toggle, binds and swap back carried over");
         }

         var0.remove("AutoLunge");
         var0.remove("Spear Swap");
      }
   }

   private static JsonObject VAsJsonObject(JsonObject var0, int var1, JsonObject var2, int var3) {
      if (qAsBoolean(var0) && var1 >= 0) {
         return var0;
      } else if (qAsBoolean(var2) && var3 >= 0) {
         return var2;
      } else if (qAsBoolean(var0)) {
         return var0;
      } else if (qAsBoolean(var2)) {
         return var2;
      } else if (var1 >= 0) {
         return var0;
      } else if (var3 >= 0) {
         return var2;
      } else {
         return var0 != null ? var0 : var2;
      }
   }

   private static boolean qAsBoolean(JsonObject var0) {
      return var0 != null && var0.get("enabled") instanceof JsonPrimitive var1 && var1.isBoolean() && var1.getAsBoolean();
   }

   private static JsonObject eAsJsonObject(JsonObject var0) {
      return var0.get("settings") instanceof JsonObject var1 ? var1 : new JsonObject();
   }

   private static int jAsInt(JsonElement var0) {
      if (var0 instanceof JsonPrimitive var1 && var1.isNumber()) {
         double var2 = var1.getAsDouble();
         return var2 == Math.rint(var2) && KeybindNames.hAsBoolean((int)var2) ? (int)var2 : -1;
      }

      return -1;
   }

   private static int iAsInt(JsonObject var0) {
      byte var10000;
      label29: {
         if (var0.get("Lunge Input") instanceof JsonPrimitive var2 && var2.isString() && var2.getAsString().equals("Mouse")) {
            var10000 = 1;
            break label29;
         }

         var10000 = 0;
      }

      int var1 = var10000;
      if (var1 == 0) {
         return jAsInt(var0.get("Lunge Key"));
      } else {
         if (var0.get("Lunge Mouse") instanceof JsonPrimitive var4 && var4.isString()) {
            int var6 = List.of("Unbound", "Left", "Right", "Middle", "Mouse 4", "Mouse 5", "Mouse 6", "Mouse 7", "Mouse 8").indexOf(var4.getAsString());
            return var6 > 0 ? KeybindNames.ZAsInt(var6 - 1) : -1;
         }

         return -1;
      }
   }

   private static JsonObject lAsJsonObject(JsonObject var0) {
      if (!var0.has("settings")) {
         var0.add("settings", new JsonObject());
      }

      return var0.getAsJsonObject("settings");
   }

   private static void WAsVoid(JsonObject var0, String var1, String var2, String var3, String var4) {
      if (var0.has(var1) && var0.get(var1).isJsonObject()) {
         JsonObject var5 = var0.getAsJsonObject(var1);
         JsonObject var6 = lAsJsonObject(var5);
         if (var6.has(var2) && var6.get(var2).isJsonPrimitive() && var6.get(var2).getAsJsonPrimitive().isString() && var6.get(var2).getAsString().equals(var3)) {
            XAsVoid(var5, var2, var6.get(var2));
            var6.addProperty(var2, var4);
         }
      }
   }

   private static void XAsVoid(JsonObject var0, String var1, JsonElement var2) {
      if (!var0.has("legacySettings")) {
         var0.add("legacySettings", new JsonObject());
      }

      var0.getAsJsonObject("legacySettings").add(var1, var2.deepCopy());
   }

   public static void RAsVoid(JsonObject var0) {
      if (var0.has("legacySettings")) {
         JsonObject var1 = ProfileCodec.DAsJsonObject(var0.get("legacySettings"));
         if (var1.size() > 200) {
            throw new IllegalArgumentException("Too many legacy settings");
         }

         ProfileCodec.PAsVoid(var1, false);

         for (Entry var3 : var1.entrySet()) {
            JsonElement var4 = (JsonElement)var3.getValue();
            if (!((String)var3.getKey()).equals("Text List")
               || !var4.isJsonPrimitive()
               || !var4.getAsJsonPrimitive().isString()
               || var4.getAsString().length() > 8192) {
               if (!var4.isJsonPrimitive() || var4.toString().length() > 128) {
                  throw new IllegalArgumentException("Invalid legacy value");
               }

               if (var4.getAsJsonPrimitive().isNumber() && !Double.isFinite(var4.getAsDouble())) {
                  throw new IllegalArgumentException("Invalid legacy number");
               }
            }
         }
      }
   }

   public static void qAsVoid(JsonObject var0, JsonObject var1) {
      JsonObject var2 = var0.deepCopy();
      JsonObject var3 = var2.has("modules") ? var2.getAsJsonObject("modules") : var2;
      ArrayList var4 = new ArrayList();

      for (String var6 : b) {
         if (var3.remove(var6) != null) {
            var4.add(var6);
         }
      }

      if (!var4.isEmpty()) {
         ClientLog.GAsVoid("Config", "Ignored retired modules {}; original profile bytes retained until explicit save", var4);
      }

      if (!var2.equals(var1)) {
         ClientLog.GAsVoid(
            "Config",
            "Migrated gameplay settings; retired values preserved in legacySettings. Unsupported combat variation/range claims retired; original values retained."
         );
      }
   }

   private ModuleMigrationNames() {
   }
}
