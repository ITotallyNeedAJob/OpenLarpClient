package com.larpclient;

import com.larpclient.module.FakePay;
import com.larpclient.module.FakeSpawner;
import com.larpclient.module.Module;
import com.larpclient.module.Performance;
import com.larpclient.module.StorageESP;
import com.larpclient.module.sf;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public enum GuiCategory {
   FAVORITES("Favorites", "star"),
   COMBAT("Combat", "swords"),
   MOVEMENT("Movement", "footprints"),
   PLAYER("Player", "user"),
   VISUALS("Visuals", "eye"),
   COSMETICS("Cosmetics", "paintbrush"),
   BASE_FINDING("Base Finding", "search"),
   MISC("Misc", "list");

   public final String J;
   public final String n;
   private static final Set<String> v = Set.of("AutoArmor", "AutoGap", "Fake Lag");
   private static final Set<String> X = Set.of("IgnoreEntity");
   private static final Set<String> CropHelper = Set.of("Freecam", "SmoothCamera", "TargetHUD", "CustomCrosshair", "Damage Markers", "Stretched Res");
   private static final Set<String> V = Set.of("Light Debug", "Motion Scanner", "Prime Chunk Finder", "Holes ESP");
   private static final Set<String> S = Set.of(
      "Animations",
      "ViewModel",
      "Sword Models",
      "Player Trail",
      "HitFX",
      "Hit Sounds",
      "Click Pulse",
      "JumpFX",
      "TargetAura",
      "SelfVisuals",
      "CustomSky",
      "Sky Shader",
      "China Hat",
      "Wings",
      "Headless Korblox",
      "Ambient Particles",
      "Item Physic",
      "EchoHand",
      "Hand Chams",
      "Flame Hands",
      "Player Model",
      "Skin Changer",
      "Capes",
      "Crystal Chams",
      "Walk Style",
      "Emotes",
      "Pets",
      "Crypto Pets",
      "Astral Weapon Trails",
      "Celestial Weather",
      "WorldLyrics",
      "KillFX",
      "GUI Animation",
      "3D UI",
      "World Menu",
      "Line Glyphs",
      "Pearl Particles",
      "No Block Rotation",
      "TimeChanger",
      "WorldColor",
      "Motion Blur",
      "Realistic Water",
      "Waving Foliage",
      "Wet Ground",
      "Snow Ground"
   );

   private GuiCategory(String var3, String var4) {
      this.J = var3;
      this.n = var4;
   }

   public boolean LAsBoolean(GuiCategory var1, Runnable var2) {
      if (this == var1) {
         return false;
      } else {
         var2.run();
         return true;
      }
   }

   public static GuiCategory SAsAk(Module var0) {
      if (var0.getName().equals("LARP") || var0.getName().equals("Name Changer") || var0 instanceof Performance) {
         return MISC;
      } else if (v.contains(var0.getName())) {
         return COMBAT;
      } else if (X.contains(var0.getName())) {
         return PLAYER;
      } else if (CropHelper.contains(var0.getName())) {
         return VISUALS;
      } else if (S.contains(var0.getName())) {
         return COSMETICS;
      } else if (V.contains(var0.getName()) || var0 instanceof sf || var0 instanceof StorageESP) {
         return BASE_FINDING;
      } else if (!(var0 instanceof FakeSpawner) && !(var0 instanceof FakePay)) {
         return switch (var0.getCategory()) {
            case COMBAT -> COMBAT;
            case MOVEMENT -> MOVEMENT;
            case PLAYER -> PLAYER;
            case RENDER, VISUALS -> VISUALS;
         };
      } else {
         return MISC;
      }
   }

   public boolean GAsBoolean(Module var1, String var2, ClientPreferences var3) {
      if (!var2.isBlank()) {
         return SkyShaderSettings.GAsInt(var1, var2) != Integer.MAX_VALUE;
      } else {
         return this == FAVORITES ? var3.yAsBoolean(var1.getName()) : SAsAk(var1) == this;
      }
   }

   public List<Module> nAsList(List<Module> var1, String var2, ClientPreferences var3) {
      return nk.sAsList(var1, var3)
         .stream()
         .filter(var3xx -> this.GAsBoolean(var3xx, var2, var3))
         .sorted(
            Comparator.<Module, Boolean>comparing(var1x -> !var3.yAsBoolean(var1x.getName()))
               .thenComparing(Module::getDisplayName, String.CASE_INSENSITIVE_ORDER)
               .thenComparing(Module::getName)
         )
         .toList();
   }

   public static GuiCategory hAsAk(String var0) {
      if (var0 == null) {
         return VISUALS;
      } else if (!var0.equals("RENDER") && !var0.equals("WORLD")) {
         try {
            return valueOf(var0);
         } catch (IllegalArgumentException var2) {
            return VISUALS;
         }
      } else {
         return VISUALS;
      }
   }
}
