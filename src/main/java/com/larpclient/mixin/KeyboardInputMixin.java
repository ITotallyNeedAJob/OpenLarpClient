package com.larpclient.mixin;

import com.larpclient.OuterEffect;
import com.larpclient.ParticleEmitter;
import com.larpclient.module.Freecam;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({KeyboardInput.class})
public abstract class KeyboardInputMixin extends Input {
   public KeyboardInputMixin() {
   }

   @Inject(
      method = {"method_3129()V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectTick(CallbackInfo var1) {
      if (p5.oAsBoolean()) {
         this.playerInput = Freecam.yAsPlayerInput(this.playerInput);
         this.movementVector = ot.UAsVec2f(this.playerInput);
      }
   }
}
