package com.larpclient.mixin;

import com.larpclient.CosmeticSubmissionHandler;
import com.larpclient.module.SelfVisuals;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({CapeFeatureRenderer.class})
public class CapeFeatureRendererMixin {
   public CapeFeatureRendererMixin() {
   }

   @Inject(
      method = {"method_4177(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_10055;FF)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectFFV(
      MatrixStack var1, OrderedRenderCommandQueue var2, int var3, PlayerEntityRenderState var4, float var5, float var6, CallbackInfo var7
   ) {
      if (!CosmeticSubmissionHandler.bAsBoolean() && SelfVisuals.HAsBoolean(var4) && SelfVisuals.zAsSelfVisuals().KAsBoolean()) {
         var7.cancel();
      }
   }
}
