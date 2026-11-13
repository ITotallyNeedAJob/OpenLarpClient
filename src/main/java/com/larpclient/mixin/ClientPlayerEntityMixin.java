package com.larpclient.mixin;

import com.larpclient.module.AutoTotem;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayerEntity.class})
public class ClientPlayerEntityMixin {
   public ClientPlayerEntityMixin() {
   }

   @Inject(
      method = {"method_6007()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_310;method_1577()Lnet/minecraft/class_1156;"
      )}
   )
   private void larpclient$injectTickMovement(CallbackInfo var1) {
      ClientPlayerEntity var2 = (ClientPlayerEntity)(Object)this;
      if (AutoTotem.TAsBoolean(var2)) {
         var2.input.playerInput = PlayerInput.DEFAULT;
         var2.input.movementVector = Vec2f.ZERO;
      }
   }
}
