package com.larpclient.mixin;

import com.larpclient.module.NoSlowdown;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientPlayerEntity.class})
public class ClientPlayerEntityGetActiveItemSpeedMultiplierMixin {
   public ClientPlayerEntityGetActiveItemSpeedMultiplierMixin() {
   }

   @Inject(
      method = {"method_75410()F"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectGetActiveItemSpeedMultiplier(CallbackInfoReturnable<Float> var1) {
      NoSlowdown var2 = NoSlowdown.yAsNoSlowdown();
      if (var2 != null && var2.MAsBoolean()) {
         var1.setReturnValue(1.0F);
      }
   }
}
