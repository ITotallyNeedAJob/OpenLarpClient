package com.larpclient.mixin;

import com.larpclient.module.StretchedRes;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({GameRenderer.class})
public class GameRendererGetBasicProjectionMatrixMixin {
   public GameRendererGetBasicProjectionMatrixMixin() {
   }

   @ModifyArg(
      method = {"method_22973(F)Lorg/joml/Matrix4f;"},
      at = {@At(
         value = "INVOKE",
         target = "Lorg/joml/Matrix4f;perspective(FFFF)Lorg/joml/Matrix4f;"
      )},
      index = 1
   )
   private float larpclient$modifyArgHandler(float var1) {
      return var1 / StretchedRes.hAsFloat();
   }
}
