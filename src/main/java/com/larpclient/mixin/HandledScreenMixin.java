package com.larpclient.mixin;

import com.larpclient.module.GUIAnimation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HandledScreen.class})
public abstract class HandledScreenMixin {
   @Shadow
   protected Slot field_2787;
   @Unique
   private boolean I;

   public HandledScreenMixin() {
   }

   @Inject(
      method = {"method_2385(Lnet/minecraft/class_332;Lnet/minecraft/class_1735;II)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectIIV(DrawContext var1, Slot var2, int var3, int var4, CallbackInfo var5) {
      GUIAnimation var6 = GUIAnimation.kAsGUIAnimation();
      this.I = var6 != null && var6.WAsBoolean();
      if (this.I) {
         float var7 = var6.MAsFloat(var2, var2 == this.field_2787 && var2.isEnabled());
         var1.getMatrices().pushMatrix();
         var1.getMatrices().translate((float)var2.x + 8.0F, (float)var2.y + 8.0F);
         var1.getMatrices().scale(var7, var7);
         var1.getMatrices().translate((float)(-var2.x) - 8.0F, (float)(-var2.y) - 8.0F);
      }
   }

   @Inject(
      method = {"method_2385(Lnet/minecraft/class_332;Lnet/minecraft/class_1735;II)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectIIV2(DrawContext var1, Slot var2, int var3, int var4, CallbackInfo var5) {
      if (this.I) {
         var1.getMatrices().popMatrix();
         this.I = false;
      }
   }
}
