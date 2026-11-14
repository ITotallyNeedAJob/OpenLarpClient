package com.larpclient.mixin.feature.entity;

import com.larpclient.ArmReach;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerEntityRenderer.class})
public class PlayerEntityRendererMixin {
   public PlayerEntityRendererMixin() {
   }

   @Inject(
      method = {"method_23205(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_2960;Lnet/minecraft/class_630;Z)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectZV(
      MatrixStack var1, OrderedRenderCommandQueue var2, int var3, Identifier var4, ModelPart var5, boolean var6, CallbackInfo var7
   ) {
      ((b2)((PlayerEntityRenderer)(Object)this).getModel()).larpRestoreWalkRig();
   }
}
