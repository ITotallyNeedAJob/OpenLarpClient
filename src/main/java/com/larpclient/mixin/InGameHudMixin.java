package com.larpclient.mixin;

import com.larpclient.p5;
import com.larpclient.module.CustomCrosshair;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InGameHud.class})
public class InGameHudMixin {
   public InGameHudMixin() {
   }

   @ModifyExpressionValue(
      method = {"method_1736(Lnet/minecraft/class_332;Lnet/minecraft/class_9779;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_5498;method_31034()Z"
      )}
   )
   private boolean larpclient$modifyExpressionValueV(boolean var1) {
      return p5.oAsBoolean() || var1;
   }

   @Inject(
      method = {"method_1736(Lnet/minecraft/class_332;Lnet/minecraft/class_9779;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(DrawContext var1, RenderTickCounter var2, CallbackInfo var3) {
      if (CustomCrosshair.qAsBoolean()) {
         var3.cancel();
      }
   }
}
