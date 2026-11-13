package com.larpclient;

import com.larpclient.module.WorldMenu;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundInstance.AttenuationType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public final class GuiSounds {
   private static long U;
   private static boolean A;

   private GuiSounds() {
   }

   public static PositionedSoundInstance JAsPositionedSoundInstance(GuiSounds.Type_o var0) {
      return new PositionedSoundInstance(
         var0.OAsIdentifier(), SoundCategory.MASTER, var0.x, 1.0F, SoundInstance.createRandom(), false, 0, AttenuationType.NONE, 0.0, 0.0, 0.0, true
      );
   }

   public static void rAsVoid(GuiSounds.Type_o var0) {
      if (var0 != null) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         if (var1 != null
            && var1.inGameHud != null
            && (ModuleSearchPanel.lAsBoolean(var1.currentScreen) || var1.currentScreen instanceof ChatScreen || WorldMenu.dAsBoolean())) {
            if (var0 == GuiSounds.Type_o.SLIDER) {
               long var2 = System.nanoTime();
               if (var2 - U < 40000000L) {
                  return;
               }

               U = var2;
            }

            try {
               var1.getSoundManager().play(JAsPositionedSoundInstance(var0));
            } catch (RuntimeException var41) {
               if (!A) {
                  A = true;
                  ClientLog.lAsVoid("GUI", "GUI sample playback failed; controls remain available", var41);
               }
            }
         }
      }
   }

   public static enum Type_o {
      OPEN("open-gui", 0.8F),
      CLOSE("close-gui", 0.8F),
      CLICK("buttonclick", 0.8F),
      SLIDER("slider", 0.8F),
      TYPING("searchtyping", 0.7F),
      CATEGORY("switchcategory", 0.8F),
      OFF("switch-left", 0.8F),
      ON("switch-right", 0.8F),
      SETTINGS_OPEN("settings-open", 0.8F),
      SETTINGS_CLOSE("settings-close", 0.8F);

      public final String u;
      public final float x;

      private Type_o(String var3, float var4) {
         this.u = var3;
         this.x = var4;
      }

      public Identifier OAsIdentifier() {
         return Identifier.of("larp-client", "gui." + this.name().toLowerCase(Locale.ROOT));
      }
   }
}
