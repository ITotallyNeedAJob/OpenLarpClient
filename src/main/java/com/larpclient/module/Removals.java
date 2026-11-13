package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.VanillaEffectFilters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class Removals extends Module {
   private static Removals b;
   private final Map<String, BooleanSetting> L = new LinkedHashMap<>();

   public Removals() {
      super("Removals", "Hide screen effects and mute ambient sounds", Category.VISUALS, -1);
      b = this;
      this.VAsVoid("hurtCam", "Hurt Cam", "Hide hurt camera shake", true);
      this.VAsVoid("scoreboard", "Scoreboard", "Hide the sidebar scoreboard", false);
      this.VAsVoid("bossBar", "Boss Bar", "Hide boss health bars", false);
      this.VAsVoid("portal", "Portal", "Hide the portal screen overlay", false);
      this.VAsVoid("fire", "Fire", "Hide the first-person fire overlay", false);
      this.VAsVoid("clip", "Clip", "Hide the in-wall overlay and third-person camera collision", false);
      this.VAsVoid("water", "Water", "Hide the underwater overlay and water fog", false);
      this.VAsVoid("nausea", "Nausea", "Hide nausea distortion and status-effect fog", false);
      this.VAsVoid("blindness", "Blindness", "Hide status-effect fog and nausea distortion", false);
      this.VAsVoid("darkness", "Darkness", "Hide status-effect fog and nausea distortion", false);
      this.VAsVoid("pumpkin", "Pumpkin", "Hide the carved pumpkin overlay", false);
      this.VAsVoid("fov", "FOV", "Hide dynamic field-of-view changes", false);
      this.VAsVoid("breakParticles", "Break Particles", "Hide block breaking and hit particles", false);
      this.VAsVoid("weather", "Weather", "Hide precipitation and rain splash particles", false);
      this.VAsVoid("glowing", "Glowing", "Hide vanilla glowing outlines", false);
      this.VAsVoid("lava", "Lava", "Hide lava fog", false);
      this.VAsVoid("beacon", "Beacon", "Mute beacon sounds", false);
      this.VAsVoid("weatherSound", "Weather Sound", "Mute rain and thunder sounds", false);
      this.VAsVoid("phantoms", "Phantoms", "Mute phantom sounds and parrot phantom imitations", false);
      this.VAsVoid("waterSound", "Water Sound", "Mute ambient underwater sounds", false);
      this.VAsVoid("lavaSound", "Lava Sound", "Mute lava bubbling and popping", false);
   }

   private void VAsVoid(String var1, String var2, String var3, boolean var4) {
      this.L.put(var1, this.addSetting(new BooleanSetting(var1, var3, var4) {
         @Override
         public String getDisplayName() {
            return var2;
         }
      }));
   }

   public List<Setting<?>> QAsList() {
      return (List<Setting<?>>)(List<?>)VanillaEffectFilters.V.stream().map(this.L::get).toList();
   }

   public List<Setting<?>> dAsList() {
      return (List<Setting<?>>)(List<?>)VanillaEffectFilters.T.stream().map(this.L::get).toList();
   }

   public static Removals KAsRemovals() {
      return b;
   }

   public static boolean xAsBoolean(String var0) {
      if (b == null || !b.isEnabled()) {
         return false;
      } else if (b != null && b.isRuntimeActive()) {
         BooleanSetting var1 = b.L.get(var0);
         return var1 != null && var1.getValue();
      } else {
         return false;
      }
   }

   public static boolean fAsBoolean() {
      return b != null && b.isEnabled() ? VanillaEffectFilters.GAsBoolean(Removals::xAsBoolean) : false;
   }

   public static boolean KAsBoolean() {
      return b != null && b.isEnabled() ? VanillaEffectFilters.PAsBoolean(Removals::xAsBoolean) : false;
   }

   public static boolean uAsBoolean(String var0) {
      return b != null && b.isEnabled() ? VanillaEffectFilters.aAsBoolean(var0, Removals::xAsBoolean) : false;
   }

   @Override
   public void onEnable() {
      ClientLog.GAsVoid(
         "Removals",
         "Enabled; selected={}; existing camera and weather preferences retained",
         this.L.entrySet().stream().filter(var0 -> var0.getValue().getValue()).map(Entry::getKey).toList()
      );
   }

   @Override
   public void onDisable() {
      ClientLog.GAsVoid("Removals", "Disabled; native visual and sound decisions restored");
   }
}
