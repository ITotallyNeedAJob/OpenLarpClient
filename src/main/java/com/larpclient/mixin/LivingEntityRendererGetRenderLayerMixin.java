package com.larpclient.mixin;

import com.larpclient.CosmeticSubmissionHandler;
import com.larpclient.module.SelfVisuals;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntityRenderer.class})
public class LivingEntityRendererGetRenderLayerMixin {
   public LivingEntityRendererGetRenderLayerMixin() {
   }

   @Inject(
      method = {"method_24302(Lnet/minecraft/class_10042;ZZZ)Lnet/minecraft/class_1921;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler(LivingEntityRenderState var1, boolean var2, boolean var3, boolean var4, CallbackInfoReturnable<RenderLayer> var5) {
      if (!CosmeticSubmissionHandler.bAsBoolean() && SelfVisuals.HAsBoolean(var1)) {
         var5.setReturnValue(null);
      }
   }
}
