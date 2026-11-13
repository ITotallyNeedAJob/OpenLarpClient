package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.TextDrawing;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HUD extends Module {
   public final ModeSetting hUDFont = this.addSetting(new ModeSetting("HUD Font", "Font used by every HUD overlay", "Inter", "Inter", "SF Pro", "Minecraft"));
   public final TextSetting staffNames = this.addSetting(
      new TextSetting("Staff Names", "Donut account names, comma separated; blank uses the reference list", "")
   );
   private final BooleanSetting watermark = this.addSetting(new BooleanSetting("Watermark", "Show LARP watermark", true));
   private final BooleanSetting moduleList = this.addSetting(new BooleanSetting("Module List", "Show active modules", true));
   private final BooleanSetting infoPanel = this.addSetting(new BooleanSetting("Info Panel", "Show coords/fps/ping etc", true));
   private final BooleanSetting keybinds = this.addSetting(new BooleanSetting("Keybinds", "Show active module keybinds", true));
   private final BooleanSetting potions = this.addSetting(new BooleanSetting("Potions", "Show active potion effects", false));
   private final BooleanSetting armor = this.addSetting(new BooleanSetting("Armor", "Show armor display", true));
   public final BooleanSetting speed = this.addSetting(new BooleanSetting("Speed", "Show the speed graph", false));
   public final BooleanSetting inventory = this.addSetting(new BooleanSetting("Inventory", "Show the Larp inventory widget", false));
   public final BooleanSetting cooldowns = this.addSetting(new BooleanSetting("Cooldowns", "Show item cooldowns", false));
   public final BooleanSetting radar = this.addSetting(new BooleanSetting("Radar", "Show nearby players", false));
   public final BooleanSetting keystrokes = this.addSetting(new BooleanSetting("Keystrokes", "Show movement keys and mouse buttons as they are pressed", false));
   public final BooleanSetting musicBar = this.addSetting(new BooleanSetting("Music Bar", "Retired music-widget preference", false));
   public final BooleanSetting watermarkUser = this.addSetting(new BooleanSetting("Watermark User", "Show player name", true));
   public final BooleanSetting watermarkIP = this.addSetting(new BooleanSetting("Watermark IP", "Show server address", true));
   public final BooleanSetting watermarkFPS = this.addSetting(new BooleanSetting("Watermark FPS", "Show frame rate", true));
   public final BooleanSetting watermarkTime = this.addSetting(new BooleanSetting("Watermark Time", "Show local time", true));
   public final ModeSetting watermarkLogoSide = this.addSetting(new ModeSetting("Watermark Logo Side", "Logo tile position", "Left", "Left", "Right"));
   public final ModeSetting keybindDisplay = this.addSetting(
      new ModeSetting("Keybind Display", "Show every bound module or only enabled modules", "Always", "Always", "Active Only")
   );
   public final ModeSetting cooldownItems = this.addSetting(new ModeSetting("Cooldown Items", "Which cooldown items to display", "Combat", "Combat", "All"));
   public final BooleanSetting inventoryHeader = this.addSetting(new BooleanSetting("Inventory Header", "Show inventory title", true));
   public final NumberSetting radarRange = this.addSetting(new NumberSetting("Radar Range", "Nearby radar range in blocks", 47.0, 20.0, 80.0, 5.0));
   public final BooleanSetting radarMobs = this.addSetting(new BooleanSetting("Radar Mobs", "Show nearby mobs", false));
   public final BooleanSetting keystrokesMouse = this.addSetting(new BooleanSetting("Keystrokes Mouse", "Show the left and right mouse buttons", true));
   public final BooleanSetting keystrokesSpace = this.addSetting(new BooleanSetting("Keystrokes Space", "Show the jump key", true));
   public final BooleanSetting keystrokesCPS = this.addSetting(new BooleanSetting("Keystrokes CPS", "Clicks per second under each mouse button", true));
   public final BooleanSetting keystrokesArrows = this.addSetting(
      new BooleanSetting("Keystrokes Arrows", "Label the movement keys with arrows instead of WASD", false)
   );
   public final NumberSetting keystrokesPadding = this.addSetting(
      new NumberSetting("Keystrokes Padding", "Space between the keys and the card edge", 5.0, 1.0, 10.0, 1.0)
   );
   public final NumberSetting keystrokesBGAlpha = this.addSetting(
      new NumberSetting("Keystrokes BG Alpha", "Card background opacity, from 0 to 255", 150.0, 0.0, 255.0, 5.0)
   );
   public final BooleanSetting keystrokesShadow = this.addSetting(new BooleanSetting("Keystrokes Shadow", "Soft shadow under the card", true));
   public final ModeSetting musicLayout = this.addSetting(new ModeSetting("Music Layout", "Music card layout", "Bottom", "Bottom", "Compact"));
   public final BooleanSetting musicProgress = this.addSetting(new BooleanSetting("Music Progress", "Show track progress", true));
   public final BooleanSetting musicTime = this.addSetting(new BooleanSetting("Music Time", "Show track position and duration", true));
   public final ModeSetting armorLayout = this.addSetting(new ModeSetting("Armor Layout", "Equipment arrangement", "Horizontal", "Horizontal", "Vertical"));
   public final NumberSetting armorSpacing = this.addSetting(new NumberSetting("Armor Spacing", "Space between equipment slots", 5.0, 0.0, 20.0, 1.0));
   public final ModeSetting armorDurability = this.addSetting(
      new ModeSetting("Armor Durability", "Durability labels", "Percent", "Off", "Percent", "Remaining")
   );
   public final BooleanSetting armorBars = this.addSetting(new BooleanSetting("Armor Bars", "Color-coded durability bars", true));
   public final BooleanSetting armorBackground = this.addSetting(new BooleanSetting("Armor Background", "Draw equipment panel", false));
   public final BooleanSetting armorEmptySlots = this.addSetting(
      new BooleanSetting("Armor Empty Slots", "Reserve spacing for empty slots without drawing placeholders", true)
   );
   public final BooleanSetting armorReverse = this.addSetting(new BooleanSetting("Armor Reverse", "Reverse equipment order", false));
   public final BooleanSetting armorHeldItems = this.addSetting(new BooleanSetting("Armor Held Items", "Include main hand and offhand", false));
   public final ColorSetting speedHeader = this.addSetting(new ColorSetting("Speed Header", "Speed graph header color and opacity", -1291845632));
   public final ColorSetting speedBackground = this.addSetting(new ColorSetting("Speed Background", "Speed graph body color and opacity", -1291845632));
   public final ColorSetting speedLine = this.addSetting(new ColorSetting("Speed Line", "Speed graph curve color", -1841171));
   private final BooleanSetting blurPanels = this.addSetting(new BooleanSetting("Blur Panels", "Reuse one scene blur beneath HUD panels", false));
   private final BooleanSetting lightSweep = this.addSetting(new BooleanSetting("Light Sweep", "Faint light band crossing every HUD panel", true));
   private static HUD t;

   public List<Setting<?>> GAsList() {
      return List.of(this.speedHeader, this.speedBackground, this.speedLine);
   }

   public List<Setting<?>> CAsList() {
      return List.of(this.keystrokesMouse, this.keystrokesSpace, this.keystrokesCPS, this.keystrokesArrows, this.keystrokesPadding, this.keystrokesBGAlpha);
   }

   public List<Setting<?>> FAsList() {
      return List.of(
         this.watermark,
         this.moduleList,
         this.infoPanel,
         this.keybinds,
         this.potions,
         this.armor,
         this.speed,
         this.inventory,
         this.cooldowns,
         this.radar,
         this.keystrokes
      );
   }

   public List<Setting<?>> cAsList() {
      return List.of(this.musicBar, this.musicLayout, this.musicProgress, this.musicTime, this.staffNames, this.keystrokesShadow);
   }

   public List<Setting<?>> qAsList() {
      return List.of(this.hUDFont, this.blurPanels, this.lightSweep);
   }

   public List<Setting<?>> LAsList(String var1) {
      List var2 = switch (var1) {
         case "speed" -> this.GAsList();
         case "keybinds" -> List.of(this.keybindDisplay, this.blurPanels);
         case "inventory" -> List.of(this.inventoryHeader, this.blurPanels);
         case "cooldowns" -> List.of(this.cooldownItems, this.blurPanels);
         case "staffs" -> List.of(this.blurPanels);
         case "keystrokes" -> {
            ArrayList var5 = new ArrayList<>(this.CAsList());
            var5.add(this.blurPanels);
            yield List.copyOf(var5);
         }
         case "radar" -> List.of(this.radarRange, this.radarMobs, this.blurPanels);
         case "armor" -> List.of(
         this.armorLayout,
         this.armorSpacing,
         this.armorDurability,
         this.armorBars,
         this.armorBackground,
         this.armorEmptySlots,
         this.armorReverse,
         this.armorHeldItems,
         this.blurPanels
      );
         case "watermark" -> List.of(this.watermarkUser, this.watermarkIP, this.watermarkFPS, this.watermarkTime, this.watermarkLogoSide, this.blurPanels);
         case "info", "effects", "target" -> List.of(this.blurPanels);
         default -> List.of();
      };
      return Stream.concat(Stream.of(this.hUDFont), var2.stream()).distinct().toList();
   }

   public boolean VAsBoolean() {
      return this.isEnabled() ? this.isOperational() && this.speed.getValue() : false;
   }

   public boolean fAsBoolean(String var1) {
      if (this.isEnabled()) {
         if (this.isOperational()) {
            switch (var1) {
               case "inventory":
                  if (this.inventory.getValue()) {
                     return true;
                  }
                  break;
               case "cooldowns":
                  if (this.cooldowns.getValue()) {
                     return true;
                  }
                  break;
               case "radar":
                  if (this.radar.getValue()) {
                     return true;
                  }
                  break;
               case "keystrokes":
                  if (this.keystrokes.getValue()) {
                     return true;
                  }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean wAsBoolean() {
      return this.isOperational() && this.blurPanels.getValue();
   }

   public boolean lightSweep() {
      return this.lightSweep.getValue();
   }

   public HUD() {
      super("HUD", "Choose on-screen information", Category.RENDER, -1);
      t = this;
      this.hUDFont.onChanged(var0 -> {
         TextDrawing.XAsVoid();
         ClientLog.GAsVoid("HUD", "HUD font changed to {}", var0);
      });

      for (Setting var2 : List.of(this.watermarkUser, this.watermarkIP, this.watermarkFPS, this.watermarkTime, this.watermarkLogoSide)) {
         var2.visibleWhen(this.watermark::getValue);
      }

      this.keybindDisplay.visibleWhen(this.keybinds::getValue);
      this.cooldownItems.visibleWhen(this.cooldowns::getValue);
      this.inventoryHeader.visibleWhen(this.inventory::getValue);
      this.radarRange.visibleWhen(this.radar::getValue);
      this.radarMobs.visibleWhen(this.radar::getValue);

      for (Setting var9 : this.CAsList()) {
         var9.visibleWhen(this.keystrokes::getValue);
      }

      this.keystrokesCPS.visibleWhen(() -> this.keystrokes.getValue() && this.keystrokesMouse.getValue());

      for (Setting var10 : this.cAsList()) {
         var10.visibleWhen(() -> false);
      }

      for (Setting var11 : this.GAsList()) {
         var11.visibleWhen(this.speed::getValue);
      }

      for (Setting var4 : new Setting[]{
         this.armorLayout,
         this.armorSpacing,
         this.armorDurability,
         this.armorBars,
         this.armorBackground,
         this.armorEmptySlots,
         this.armorReverse,
         this.armorHeldItems
      }) {
         var4.visibleWhen(this.armor::getValue);
      }
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static HUD EAsHUD() {
      return t;
   }

   public boolean RAsBoolean() {
      return this.isEnabled() ? this.isOperational() && this.watermark.getValue() : false;
   }

   public boolean UAsBoolean() {
      return this.isEnabled() ? this.isOperational() && this.moduleList.getValue() : false;
   }

   public boolean zAsBoolean() {
      return this.isEnabled() ? this.isOperational() && this.infoPanel.getValue() : false;
   }

   public boolean cAsBoolean() {
      return this.isEnabled() ? this.isOperational() && this.keybinds.getValue() : false;
   }

   public boolean rAsBoolean() {
      return this.isEnabled() ? this.isOperational() && this.potions.getValue() : false;
   }

   public boolean BAsBoolean() {
      return this.isEnabled() ? this.isOperational() && this.armor.getValue() : false;
   }
}
