package com.larpclient.mixin;

import com.larpclient.module.Module3DUI;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InGameHud.class})
public abstract class InGameHudRenderMixin {
   public InGameHudRenderMixin() {
   }

   @Inject(
      method = {"method_1753(Lnet/minecraft/class_332;Lnet/minecraft/class_9779;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(CallbackInfo var1) {
      if (Module3DUI.WAsBoolean()) {
         var1.cancel();
      }
   }
}
