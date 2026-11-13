package com.larpclient.mixin;

import com.larpclient.SnowGround;
import com.larpclient.module.NoWeather;
import com.larpclient.module.Removals;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WeatherRendering;
import net.minecraft.client.render.state.WeatherRenderState;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WeatherRendering.class})
public class WeatherRenderingMixin {
   public WeatherRenderingMixin() {
   }

   @Inject(
      method = {"method_62320(Lnet/minecraft/class_4597;Lnet/minecraft/class_243;Lnet/minecraft/class_12077;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(VertexConsumerProvider var1, Vec3d var2, WeatherRenderState var3, CallbackInfo var4) {
      if (SnowGround.FAsBoolean() || NoWeather.iAsBoolean() || Removals.xAsBoolean("weather")) {
         var4.cancel();
      }
   }
}
