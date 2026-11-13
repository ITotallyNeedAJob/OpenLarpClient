package com.larpclient.mixin.feature.core;

import com.larpclient.AutoTunnel;
import com.larpclient.x;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.PlayerInput;
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
      PlayerInput var2 = AutoTunnel.LAsPlayerInput();
      if (var2 != null) {
         this.playerInput = var2;
         this.movementVector = x.sAsVec2f(var2);
      }
   }
}
