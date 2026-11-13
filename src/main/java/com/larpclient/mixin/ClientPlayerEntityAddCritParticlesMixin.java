package com.larpclient.mixin;

import com.larpclient.module.HitFX;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayerEntity.class})
public class ClientPlayerEntityAddCritParticlesMixin {
   public ClientPlayerEntityAddCritParticlesMixin() {
   }

   @Inject(
      method = {"method_7277(Lnet/minecraft/class_1297;)V", "method_7304(Lnet/minecraft/class_1297;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(Entity var1, CallbackInfo var2) {
      HitFX var3 = HitFX.tAsHitFX();
      if (var3 != null && var3.isOperational()) {
         var2.cancel();
      }
   }
}
