package com.larpclient.mixin.compat;

import com.larpclient.RifleJoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"com/misterpemodder/shulkerboxtooltip/impl/renderer/BasePreviewRenderer"}
)
public abstract class BasePreviewRendererMixin {
   public BasePreviewRendererMixin() {
   }

   @Inject(
      method = {"drawInnerTooltip"},
      at = {@At("HEAD")},
      require = 0
   )
   private void larpclient$injectDrawInnerTooltip(CallbackInfo var1) {
      rj.NAsVoid(true);
   }

   @Inject(
      method = {"drawInnerTooltip"},
      at = {@At("RETURN")},
      require = 0
   )
   private void larpclient$injectDrawInnerTooltip2(CallbackInfo var1) {
      rj.NAsVoid(false);
   }
}
