package com.larpclient.mixin;

import com.larpclient.module.NoWeather;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({World.class})
public class WorldGetRainGradientMixin {
   public WorldGetRainGradientMixin() {
   }

   @Inject(
      method = {"method_8430(F)F", "method_8478(F)F"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectGetRainGradient(float var1, CallbackInfoReturnable<Float> var2) {
      if ((Object)this instanceof ClientWorld && NoWeather.iAsBoolean()) {
         var2.setReturnValue(0.0F);
      }
   }
}
