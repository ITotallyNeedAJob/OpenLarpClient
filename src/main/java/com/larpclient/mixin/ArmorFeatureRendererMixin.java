package com.larpclient.mixin;

import com.larpclient.CosmeticSubmissionHandler;
import com.larpclient.module.SelfVisuals;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ArmorFeatureRenderer.class})
public class ArmorFeatureRendererMixin {
   public ArmorFeatureRendererMixin() {
   }

   @Inject(
      method = {"method_17157(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_10034;FF)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectFFV(
      MatrixStack var1, OrderedRenderCommandQueue var2, int var3, BipedEntityRenderState var4, float var5, float var6, CallbackInfo var7
   ) {
      if (!CosmeticSubmissionHandler.bAsBoolean() && SelfVisuals.HAsBoolean(var4) && SelfVisuals.zAsSelfVisuals().affectArmor.getValue()) {
         var7.cancel();
      }
   }
}
