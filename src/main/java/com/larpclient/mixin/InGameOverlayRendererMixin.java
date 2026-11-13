package com.larpclient.mixin;

import com.larpclient.module.Removals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InGameOverlayRenderer.class})
public class InGameOverlayRendererMixin {
   public InGameOverlayRendererMixin() {
   }

   @Inject(
      method = {"method_23070(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;Lnet/minecraft/class_1058;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void larpclient$injectV(MatrixStack var0, VertexConsumerProvider var1, Sprite var2, CallbackInfo var3) {
      if (Removals.xAsBoolean("fire")) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"method_23069(Lnet/minecraft/class_310;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void larpclient$injectV(MinecraftClient var0, MatrixStack var1, VertexConsumerProvider var2, CallbackInfo var3) {
      if (Removals.xAsBoolean("water")) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"method_23068(Lnet/minecraft/class_1058;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void larpclient$injectV(Sprite var0, MatrixStack var1, VertexConsumerProvider var2, CallbackInfo var3) {
      if (Removals.xAsBoolean("clip")) {
         var3.cancel();
      }
   }
}
