package com.larpclient.mixin;

import com.larpclient.module.Module3DUI;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GameRenderer.class})
public abstract class GameRendererUpdateCameraMixin {
   public GameRendererUpdateCameraMixin() {
   }

   @Inject(
      method = {"method_76295(Lnet/minecraft/class_9779;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV(CallbackInfo var1) {
      Module3DUI.xAsVoid();
   }
}
