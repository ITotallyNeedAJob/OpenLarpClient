package com.larpclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundInstance.AttenuationType;
import net.minecraft.client.sound.SoundSystem.PlayResult;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public final class NotificationSound {
   private static final Identifier N = Identifier.of("larp-client", "notification");
   private static boolean A;

   private NotificationSound() {
   }

   public static PositionedSoundInstance WAsPositionedSoundInstance() {
      return new PositionedSoundInstance(N, SoundCategory.MASTER, 1.0F, 1.0F, SoundInstance.createRandom(), false, 0, AttenuationType.NONE, 0.0, 0.0, 0.0, true);
   }

   public static void XAsVoid() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (var0 != null) {
         if (!var0.isOnThread()) {
            var0.execute(NotificationSound::XAsVoid);
         } else if (!(var0.options.getSoundVolume(SoundCategory.MASTER) <= 0.0F)) {
            try {
               PlayResult var1 = var0.getSoundManager().play(WAsPositionedSoundInstance());
               if (!A) {
                  A = true;
                  ClientLog.GAsVoid("ESP", "Notification sound uses Minecraft Master volume; playback={}", var1);
               }
            } catch (RuntimeException var21) {
               ClientLog.hAsVoid("ESP", "Notification playback failed; alerts remain available, reload resources to retry", var21);
            }
         }
      }
   }
}
