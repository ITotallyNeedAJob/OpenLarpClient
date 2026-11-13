package com.larpclient.mixin.feature.render;

import com.larpclient.RealisticWater;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InGameOverlayRenderer.class})
public abstract class InGameOverlayRendererMixin {
   public InGameOverlayRendererMixin() {
   }

   @Inject(
      method = {"method_23069(Lnet/minecraft/class_310;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void larpclient$injectV(CallbackInfo var0) {
      if (RealisticWater.wAsBoolean()) {
         var0.cancel();
      }
   }
}
