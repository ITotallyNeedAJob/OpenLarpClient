package com.larpclient.mixin;

import com.larpclient.ParticleEmitter;
import net.minecraft.client.input.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Input.class})
public class InputMixin {
   public InputMixin() {
   }

   @Inject(
      method = {"method_64054()V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectJump(CallbackInfo var1) {
      if (ParticleEmitter.oAsBoolean()) {
         var1.cancel();
      }
   }
}
