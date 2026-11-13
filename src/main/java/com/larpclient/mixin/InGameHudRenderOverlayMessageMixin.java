package com.larpclient.mixin;

import com.larpclient.module.FakePay;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({InGameHud.class})
public class InGameHudRenderOverlayMessageMixin {
   public InGameHudRenderOverlayMessageMixin() {
   }

   @ModifyExpressionValue(
      method = {"method_55800(Lnet/minecraft/class_332;Lnet/minecraft/class_9779;)V"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/class_329;field_2018:Lnet/minecraft/class_2561;"
      )}
   )
   private Text larpclient$modifyExpressionValueV(Text var1) {
      return FakePay.hAsText(var1, true);
   }
}
