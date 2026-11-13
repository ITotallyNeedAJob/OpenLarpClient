package com.larpclient.mixin;

import com.larpclient.module.CustomSky;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WorldRenderer.class})
public class WorldRendererMixin {
   public WorldRendererMixin() {
   }

   @Inject(
      method = {"method_62204(Lnet/minecraft/class_9909;Lnet/minecraft/class_4063;Lnet/minecraft/class_243;JFIF)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectJFIFV(FrameGraphBuilder var1, CloudRenderMode var2, Vec3d var3, long var4, float var6, int var7, float var8, CallbackInfo var9) {
      if (CustomSky.yAsBoolean() && !CustomSky.NAsCustomSky().clouds()) {
         var9.cancel();
      }
   }
}
