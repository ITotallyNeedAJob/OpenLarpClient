package com.larpclient.mixin;

import com.larpclient.module.Performance;
import net.minecraft.client.option.InactivityFpsLimiter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({InactivityFpsLimiter.class})
public abstract class InactivityFpsLimiterMixin {
   public InactivityFpsLimiterMixin() {
   }

   @Inject(
      method = {"method_61937()I"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectUpdate(CallbackInfoReturnable<Integer> var1) {
      var1.setReturnValue(Performance.QAsInt(var1.getReturnValueI()));
   }
}
