package com.larpclient.mixin;

import com.larpclient.HandEffectRenderer;
import com.larpclient.ButtonWidget;
import com.larpclient.WindowState;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({GameRenderer.class})
public abstract class GameRendererMixin {
   public GameRendererMixin() {
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @WrapOperation(
      method = {"method_3188(Lnet/minecraft/class_9779;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_757;method_3172(FZLorg/joml/Matrix4f;)V"
      )}
   )
   private void larpclient$wrapOperationV(GameRenderer var1, float var2, boolean var3, Matrix4f var4, Operation<Void> var5) {
      if (var1.isRenderingPanorama()) {
         HandEffectRenderer.KAsVoid();
         ButtonWidget.lAsVoid();
         WindowState.eAsVoid();
      }

      boolean var6 = !var1.isRenderingPanorama() && ButtonWidget.xAsBoolean();
      boolean var7 = !var1.isRenderingPanorama() && HandEffectRenderer.fAsBoolean();
      boolean var8 = !var1.isRenderingPanorama() && WindowState.FAsBoolean();
      boolean var9 = false;
      boolean var12 = false /* VF: Semaphore variable */;

      try {
         var12 = true;
         var5.call(new Object[]{var1, var2, var3, var4});
         var9 = true;
         var12 = false;
      } finally {
         if (var12) {
            if (var6) {
               if (var9) {
                  ButtonWidget.kAsVoid();
               } else {
                  ButtonWidget.lAsVoid();
               }
            }

            if (var7) {
               if (var9) {
                  HandEffectRenderer.cAsVoid();
               } else {
                  HandEffectRenderer.lAsVoid();
               }
            }

            if (var8) {
               if (var9) {
                  WindowState.qAsVoid();
               } else {
                  WindowState.eAsVoid();
               }
            }
         }
      }

      if (var6) {
         ButtonWidget.kAsVoid();
      }

      if (var7) {
         HandEffectRenderer.cAsVoid();
      }

      if (var8) {
         WindowState.qAsVoid();
      }
   }
}
