package com.larpclient.mixin;

import com.larpclient.module.RodSwap;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Mouse.class})
public abstract class MouseOnMouseScrollMixin {
   public MouseOnMouseScrollMixin() {
   }

   @Inject(
      method = {"method_1598(JDD)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1661;method_61496(I)V"
      )}
   )
   private void larpclient$injectOnMouseScroll(long var1, double var3, double var5, CallbackInfo var7) {
      RodSwap.bAsVoid();
   }
}
