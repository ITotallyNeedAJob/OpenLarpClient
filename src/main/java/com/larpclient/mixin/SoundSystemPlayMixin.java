package com.larpclient.mixin;

import com.larpclient.module.HitSounds;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.SoundSystem.PlayResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({SoundSystem.class})
public class SoundSystemPlayMixin {
   public SoundSystemPlayMixin() {
   }

   @Inject(
      method = {"method_4854(Lnet/minecraft/class_1113;)Lnet/minecraft/class_1140$class_11518;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler(SoundInstance var1, CallbackInfoReturnable<PlayResult> var2) {
      HitSounds var3 = HitSounds.hAsHitSounds();
      if (var3 != null && var3.bAsBoolean(var1)) {
         var2.setReturnValue(PlayResult.NOT_STARTED);
      }
   }
}
