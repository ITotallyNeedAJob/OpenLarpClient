package com.larpclient.mixin;

import com.larpclient.ParticleEmitter;
import com.larpclient.module.Freecam;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({GameRenderer.class})
public abstract class GameRendererRenderHandMixin {
   public GameRendererRenderHandMixin() {
   }

   @ModifyExpressionValue(
      method = {"method_3172(FZLorg/joml/Matrix4f;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_5498;method_31034()Z"
      )}
   )
   private boolean larpclient$modifyExpressionValueV(boolean var1) {
      return p5.oAsBoolean() ? Freecam.oAsBoolean() : var1;
   }
}
