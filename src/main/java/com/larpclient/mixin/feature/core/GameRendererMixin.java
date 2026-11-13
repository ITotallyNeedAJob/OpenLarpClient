package com.larpclient.mixin.feature.core;

import com.larpclient.AutoTunnel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({GameRenderer.class})
public abstract class GameRendererMixin {
   public GameRendererMixin() {
   }

   @WrapOperation(
      method = {"method_3192(Lnet/minecraft/class_9779;Z)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_310;method_20539(Z)V"
      )}
   )
   private void larpclient$wrapOperationZV(MinecraftClient var1, boolean var2, Operation<Void> var3) {
      if (!AutoTunnel.IAsBoolean()) {
         var3.call(new Object[]{var1, var2});
      }
   }
}
