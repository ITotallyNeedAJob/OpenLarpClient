package com.larpclient.mixin;

import com.larpclient.module.GUIAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
   targets = {"net/minecraft/class_846$class_851"}
)
public class BuiltChunkMixin {
   @Shadow
   private long field_64231;

   public BuiltChunkMixin() {
   }

   @Inject(
      method = {"method_76298"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectMethod_76298(long var1, CallbackInfoReturnable<Float> var3) {
      GUIAnimation var4 = GUIAnimation.kAsGUIAnimation();
      if (var4 != null && var4.ZAsBoolean()) {
         var3.setReturnValue((float)var4.yAsDouble((double)(var1 - this.field_64231) / (double)var4.LAsLong()));
      }
   }
}
