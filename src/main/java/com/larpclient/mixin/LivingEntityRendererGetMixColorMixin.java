package com.larpclient.mixin;

import com.larpclient.module.HitFX;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntityRenderer.class})
public class LivingEntityRendererGetMixColorMixin {
   public LivingEntityRendererGetMixColorMixin() {
   }

   @Inject(
      method = {"method_62484(Lnet/minecraft/class_10042;)I"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectI(LivingEntityRenderState var1, CallbackInfoReturnable<Integer> var2) {
      HitFX var3 = HitFX.tAsHitFX();
      if (var1.hurt && var3 != null && var3.vAsBoolean()) {
         var2.setReturnValue(var3.lAsInt());
      }
   }
}
