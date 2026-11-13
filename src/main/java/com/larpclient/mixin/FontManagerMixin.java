package com.larpclient.mixin;

import com.larpclient.TextDrawing;
import net.minecraft.client.font.FontManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({FontManager.class})
public class FontManagerMixin {
   public FontManagerMixin() {
   }

   @Inject(
      method = {"method_51614(Lnet/minecraft/class_378$class_8536;Lnet/minecraft/class_3695;)V", "method_57024(Lnet/minecraft/class_315;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV(CallbackInfo var1) {
      TextDrawing.XAsVoid();
   }
}
