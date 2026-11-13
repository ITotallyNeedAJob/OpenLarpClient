package com.larpclient.mixin;

import com.larpclient.module.GUIAnimation;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({InGameHud.class})
public class InGameHudRenderPlayerListMixin {
   public InGameHudRenderPlayerListMixin() {
   }

   @ModifyExpressionValue(
      method = {"method_55804(Lnet/minecraft/class_332;Lnet/minecraft/class_9779;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_304;method_1434()Z"
      )}
   )
   private boolean larpclient$modifyExpressionValueV(boolean var1) {
      GUIAnimation var2 = GUIAnimation.kAsGUIAnimation();
      return var2 != null && var2.pAsBoolean() ? var1 || (double)var2.aAsFloat() > 0.001 : var1;
   }
}
