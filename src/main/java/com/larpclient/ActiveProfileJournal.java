package com.larpclient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.AmbientParticles;
import com.larpclient.module.CustomSky;
import com.larpclient.module.HitFX;
import com.larpclient.module.JumpFX;
import com.larpclient.module.Module;
import com.larpclient.module.PlayerTrail;
import com.larpclient.module.SelfVisuals;
import com.larpclient.module.Setting;
import com.larpclient.module.TargetAura;
import com.larpclient.module.WorldColor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public final class ActiveProfileJournal {
   private static final Gson N = new Gson();
   private boolean o;
   private boolean J;
   private String A = "";
   private JsonObject n;
   private int SmoothInput = 3;

   public ActiveProfileJournal() {
   }

   public boolean RAsBoolean() {
      return this.o && this.J;
   }

   public void JAsVoid(String var1, String var2) {
      if (this.o && this.A.equals(var1)) {
         this.A = ProfileCodec.IAsString(var2);
      }
   }

   public JsonObject XAsJsonObject() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("active", this.o);
      var1.addProperty("version", this.t);
      if (this.o && this.n != null) {
         var1.addProperty("profile", this.A);
         var1.add("previous", this.n.deepCopy());
      }

      return var1;
   }

   public static ActiveProfileJournal FAsAs(JsonObject var0) {
      ActiveProfileJournal var1 = new ActiveProfileJournal();

      try {
         if (!ProfileCodec.SAsBoolean(var0.get("active"))) {
            return var1;
         }

         String var2 = ProfileCodec.IAsString(ProfileCodec.MAsString(var0, "profile", 128));
         JsonObject var3 = var0.getAsJsonObject("previous");
         if (var0.has("version")) {
            ProfileCodec.YAsVoid(var0.get("version"), 1.0, 3.0, true);
         }

         int var4 = var0.has("version") ? var0.get("version").getAsInt() : 1;
         if (var3 == null || var3.size() != (var4 == 1 ? 6 : 8)) {
            throw new IllegalArgumentException("Incomplete restore journal");
         }

         var1.t = var4;
         var1.o = true;
         var1.A = var2;
         var1.n = var3.deepCopy();
      } catch (RuntimeException var51) {
         ClientLog.oAsVoid("Larp Preset", "Invalid local restore journal ignored; profile values retained: {}", var51.toString());
      }

      return var1;
   }

   private static <T extends Module> T sAsModule(List<Module> var0, Class<T> var1) {
      return var0.stream()
         .filter(var1::isInstance)
         .map(var1::cast)
         .findFirst()
         .orElseThrow(() -> new IllegalStateException("Missing cosmetic: " + var1.getSimpleName()));
   }

   private static ActiveProfileJournal.Type_p eAsAs_Type_p(List<Module> var0, Class<? extends Module> var1, Object... var2) {
      Module var3 = sAsModule(var0, var1);
      LinkedHashMap var4 = new LinkedHashMap();

      for (int var5 = 0; var5 < var2.length; var5 += 2) {
         String var6 = (String)var2[var5];
         Setting var7 = var3.getSettings()
            .stream()
            .filter(var1x -> var1x.getName().equals(var6))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Missing preset field: " + var3.getName() + "/" + var6));
         Object var8 = SettingValueCodec.jAsObject(var7, N.toJsonTree(var2[var5 + 1]), true);
         var4.put(var7, var8);
      }

      return new ActiveProfileJournal.Type_p(var3, var4);
   }

   private static List<ActiveProfileJournal.Type_p> iAsList(List<Module> var0) {
      return List.of(
         eAsAs_Type_p(
            var0,
            CustomSky.class,
            "Sky Style",
            "Nebula",
            "Nebula Brightness",
            0.75,
            "Nebula Saturation",
            0.85,
            "Nebula Intensity",
            0.65,
            "Nebula Motion",
            "Flow",
            "Nebula Animation Speed",
            1.0,
            "Nebula Flow Strength",
            0.35,
            "Nebula Scale",
            1.0,
            "Nebula Color",
            -1,
            "Nebula Rotation Speed",
            0.02,
            "Match Fog",
            true,
            "Clouds",
            false
         ),
         eAsAs_Type_p(
            var0,
            SelfVisuals.class,
            "Style",
            "Outline",
            "Model Opacity",
            0.85,
            "Outline",
            true,
            "Outline Color",
            -988929,
            "Outline Thickness",
            1.5,
            "Outline Opacity",
            0.8,
            "Outline Bloom",
            true,
            "Model Bloom",
            false,
            "Third Person",
            true
         ),
         eAsAs_Type_p(
            var0,
            PlayerTrail.class,
            "Style",
            "Glow",
            "Player",
            true,
            "Width",
            0.09,
            "Lifetime",
            0.8,
            "Bloom",
            true,
            "Smoothing",
            true,
            "Color",
            -2043905,
            "Gradient",
            -7909428,
            "Trail Particles",
            true
         ),
         eAsAs_Type_p(
            var0,
            TargetAura.class,
            "Style",
            "Halo",
            "Speed",
            0.45,
            "Width",
            0.035,
            "Particles Per Second",
            12.0,
            "Bloom",
            true,
            "Theme Colors",
            false,
            "Color",
            -1780481,
            "Gradient",
            -7054365
         ),
         eAsAs_Type_p(
            var0,
            HitFX.class,
            "Preset",
            "Energy",
            "Particles",
            true,
            "Hit Rings",
            true,
            "Amount",
            14.0,
            "Size",
            0.07,
            "Lifetime",
            0.6,
            "Bloom",
            true,
            "Color",
            -2571009,
            "Gradient",
            -7977523,
            "Ring Size",
            0.7,
            "Ring Fade",
            0.55
         ),
         eAsAs_Type_p(
            var0,
            JumpFX.class,
            "Mode",
            "Terrain",
            "Color Theme",
            "Custom",
            "Color",
            -3758859,
            "Maximum Radius",
            3.0,
            "Pulse Duration",
            0.9,
            "Opacity",
            0.7,
            "Glow Intensity",
            0.55
         ),
         eAsAs_Type_p(
            var0,
            WorldColor.class,
            "Ambient Color",
            -5342721,
            "Ambient Strength",
            0.65,
            "Tint",
            -1517057,
            "Shadow Tint",
            -3363084,
            "Highlight Tint",
            -923393,
            "Saturation",
            0.85,
            "Contrast",
            1.0,
            "Exposure",
            0.05,
            "Gamma",
            1.0,
            "Vignette",
            0.08
         ),
         eAsAs_Type_p(
            var0,
            AmbientParticles.class,
            "Style",
            "Both",
            "Radius",
            12.0,
            "Height",
            10.0,
            "Density",
            160.0,
            "Lifetime",
            5.0,
            "Speed",
            1.5,
            "Size",
            0.05,
            "Comet Count",
            12.0,
            "Vertical Travel",
            3.0,
            "Sideways Travel",
            2.5,
            "Motion Period",
            5.0,
            "Maximum Particles",
            1536.0,
            "Trail Length",
            0.8,
            "Trail Width",
            0.025,
            "Color",
            -1517057,
            "Tail Color",
            -6794006,
            "Opacity",
            0.7,
            "Bloom",
            true
         )
      );
   }

   private static JsonObject jAsJsonObject(List<ActiveProfileJournal.Type_p> var0) {
      JsonObject var1 = new JsonObject();

      for (ActiveProfileJournal.Type_p var3 : var0) {
         JsonObject var4 = new JsonObject();
         var4.addProperty("enabled", var3.module.isEnabled());
         JsonObject var5 = new JsonObject();
         var3.values.keySet().forEach(var1x -> var5.add(var1x.getName(), N.toJsonTree(var1x.getValue())));
         var4.add("settings", var5);
         var1.add(var3.module.getName(), var4);
      }

      return var1;
   }

   private static List<ActiveProfileJournal.Type_p> qAsList(List<ActiveProfileJournal.Type_p> var0, JsonObject var1) {
      if (var1 != null && var1.size() == var0.size()) {
         ArrayList var2 = new ArrayList();

         for (ActiveProfileJournal.Type_p var4 : var0) {
            JsonObject var5 = var1.getAsJsonObject(var4.module.getName());
            ProfileCodec.SAsBoolean(var5.get("enabled"));
            JsonObject var6 = var5.getAsJsonObject("settings");
            if (var6.size() != var4.values.size()) {
               throw new IllegalArgumentException("Unexpected restore settings");
            }

            LinkedHashMap var7 = new LinkedHashMap();

            for (Setting var9 : var4.values.keySet()) {
               var7.put(var9, SettingValueCodec.jAsObject(var9, var6.get(var9.getName()), true));
            }

            var2.add(new ActiveProfileJournal.Type_p(var4.module, var7));
         }

         return var2;
      } else {
         throw new IllegalArgumentException("Invalid restore journal");
      }
   }

   private static void hAsVoid(List<ActiveProfileJournal.Type_p> var0, JsonObject var1) {
      for (ActiveProfileJournal.Type_p var3 : var0) {
         var3.values.forEach((var0x, var1x) -> {
            if (!Objects.equals(var0x.getValue(), var1x)) {
               ((Setting<? super Object>)var0x).setValue(var1x);
            }
         });
         int var4 = var1 != null && !ProfileCodec.SAsBoolean(var1.getAsJsonObject(var3.module.getName()).get("enabled")) ? 0 : 1;
         var3.module.setEnabled(var4 != 0);
         if ((var3.module.isEnabled() ? 1 : 0) != var4) {
            throw new IllegalStateException("Cosmetic activation failed: " + var3.module.getName());
         }
      }
   }

   private static void uAsVoid(BooleanSupplier var0, RuntimeException var1) {
      try {
         if (!var0.getAsBoolean()) {
            ClientLog.oAsVoid("Larp Preset", "Recovery preference save failed; in-memory state retained for retry");
         }
      } catch (RuntimeException var3) {
         var1.addSuppressed(var3);
      }
   }

   public boolean WAsBoolean(boolean var1, List<Module> var2, String var3, BooleanSupplier var4) {
      if (var1 == this.RAsBoolean()) {
         return true;
      } else {
         try {
            List var5 = iAsList(var2);
            if (var1) {
               this.t = 3;
               this.n = jAsJsonObject(var5);
               this.A = ProfileCodec.IAsString(var3);
               this.o = true;

               try {
                  if (!var4.getAsBoolean()) {
                     this.o = false;
                     this.n = null;
                     this.A = "";
                     return false;
                  }
               } catch (RuntimeException var13) {
                  this.o = false;
                  this.n = null;
                  this.A = "";
                  throw var13;
               }

               this.J = true;

               try {
                  hAsVoid(var5, null);
               } catch (RuntimeException var12) {
                  try {
                     hAsVoid(qAsList(var5, this.n), this.n);
                     this.o = this.J = false;
                  } catch (RuntimeException var10) {
                     var12.addSuppressed(var10);
                  }

                  uAsVoid(var4, var12);
                  throw var12;
               }
            } else {
               List var6 = qAsList(var5, this.n);
               this.o = false;

               try {
                  if (!var4.getAsBoolean()) {
                     this.o = true;
                     return false;
                  }
               } catch (RuntimeException var14) {
                  this.o = true;
                  throw var14;
               }

               try {
                  hAsVoid(var6, this.n);
                  this.J = false;
                  this.n = null;
                  this.A = "";
               } catch (RuntimeException var11) {
                  this.o = true;

                  try {
                     hAsVoid(var5, null);
                  } catch (RuntimeException var9) {
                     var11.addSuppressed(var9);
                  }

                  uAsVoid(var4, var11);
                  throw var11;
               }
            }

            ClientLog.GAsVoid("Larp Preset", var1 ? "Enabled eight cosmetics; previous choices saved locally" : "Disabled; previous cosmetic choices restored");
            return true;
         } catch (RuntimeException var151) {
            ClientLog.lAsVoid("Larp Preset", "Could not change cosmetic preset; restore journal retained where needed", var151);
            return false;
         }
      }
   }

   public boolean DAsBoolean(List<Module> var1, String var2, BooleanSupplier var3) {
      if (this.o && !this.J) {
         try {
            if (!this.A.equals(var2)) {
               throw new IllegalArgumentException("Active profile changed outside the client");
            } else {
               List var4 = iAsList(var1);
               if (this.t < 3 && !this.RAsBoolean(var4, var3)) {
                  return false;
               } else {
                  List var5 = qAsList(var4, this.n);

                  try {
                     hAsVoid(var4, null);
                     this.J = true;
                  } catch (RuntimeException var9) {
                     try {
                        hAsVoid(var5, this.n);
                     } catch (RuntimeException var8) {
                        var9.addSuppressed(var8);
                        this.J = true;
                        ClientLog.lAsVoid("Larp Preset", "Preset recovery failed; local restore journal retained for Off retry", var9);
                        return false;
                     }

                     throw var9;
                  }

                  ClientLog.GAsVoid("Larp Preset", "Resumed local cosmetic preset for {}", this.A);
                  return true;
               }
            }
         } catch (RuntimeException var101) {
            this.o = this.J = false;
            this.n = null;
            this.A = "";
            uAsVoid(var3, var101);
            ClientLog.lAsVoid("Larp Preset", "Could not resume preset; loaded profile retained", var101);
            return false;
         }
      } else {
         return true;
      }
   }

   private boolean RAsBoolean(List<ActiveProfileJournal.Type_p> var1, BooleanSupplier var2) {
      JsonObject var3 = jAsJsonObject(var1);
      Set var4 = Set.of("Sky Style", "Nebula Brightness", "Nebula Saturation", "Nebula Intensity");
      Set var5 = Set.of("Vertical Travel", "Sideways Travel", "Motion Period", "Maximum Particles");
      int var6 = 0;

      for (ActiveProfileJournal.Type_p var8 : var1) {
         if (this.t != 1 || !(var8.module instanceof WorldColor) && !(var8.module instanceof AmbientParticles)) {
            JsonObject var9 = this.n.getAsJsonObject(var8.module.getName());
            ProfileCodec.SAsBoolean(var9.get("enabled"));
            JsonObject var10 = var9.getAsJsonObject("settings");
            Set var11 = this.t == 1 && var8.module instanceof CustomSky
               ? var4
               : var8.values
                  .keySet()
                  .stream()
                  .map(Setting::getName)
                  .filter(var2x -> !(var8.module instanceof AmbientParticles) || !var5.contains(var2x))
                  .collect(Collectors.toSet());
            if (!var10.keySet().equals(var11)) {
               throw new IllegalArgumentException("Invalid legacy restore fields");
            }

            JsonObject var12 = var3.getAsJsonObject(var8.module.getName());
            var12.add("enabled", var9.get("enabled").deepCopy());

            for (Setting var14 : var8.values.keySet()) {
               if (var10.has(var14.getName())) {
                  SettingValueCodec.jAsObject(var14, var10.get(var14.getName()), true);
                  var12.getAsJsonObject("settings").add(var14.getName(), var10.get(var14.getName()).deepCopy());
               }
            }

            var6++;
         }
      }

      if (var6 != this.n.size()) {
         throw new IllegalArgumentException("Invalid legacy restore modules");
      } else {
         JsonObject var16 = this.n;
         int var17 = this.t;
         this.n = var3;
         this.t = 3;

         try {
            if (var2.getAsBoolean()) {
               return true;
            }
         } catch (RuntimeException var151) {
            ClientLog.oAsVoid("Larp Preset", "Preset journal upgrade save failed; loaded profile retained: {}", var151.toString());
         }

         this.n = var16;
         this.t = var17;
         return false;
      }
   }

   public void uAsVoid(List<Module> var1, BooleanSupplier var2) {
      if (this.RAsBoolean() && !this.WAsBoolean(false, var1, this.A, var2)) {
         throw new IllegalStateException("Larp Preset restore/save failed; configuration load cancelled");
      }
   }

   public void aAsVoid(Module var1, JsonObject var2) {
      if (this.RAsBoolean() && this.n != null && this.n.has(var1.getName())) {
         JsonObject var3 = this.n.getAsJsonObject(var1.getName());
         var2.add("enabled", var3.get("enabled").deepCopy());
         JsonObject var4 = var2.getAsJsonObject("settings");
         var3.getAsJsonObject("settings").entrySet().forEach(var1x -> var4.add((String)var1x.getKey(), ((JsonElement)var1x.getValue()).deepCopy()));
      }
   }

   private static record Type_p(Module module, Map<Setting<?>, Object> values) {
   }
}
