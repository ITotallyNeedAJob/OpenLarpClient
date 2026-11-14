package com.larpclient.mixin;

import com.larpclient.BloomQuality;
import com.larpclient.MotionBlurEffect;
import com.larpclient.SkyEffectManager;
import com.larpclient.WorldColorPostEffect;
import com.larpclient.MediaSession;
import com.larpclient.OpenScreen;
import com.larpclient.WebRenderer;
import com.larpclient.module.SkyShader;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GameRenderer.class})
public class GameRendererRenderMixin {
   public GameRendererRenderMixin() {
   }

   @Inject(
      method = {"method_3192(Lnet/minecraft/class_9779;Z)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectZV(RenderTickCounter var1, boolean var2, CallbackInfo var3) {
      MediaSession.qAsVoid(var1.getTickProgress(false));
      BloomQuality.AAsVoid();
      OpenScreen.sAsVoid();
      BloomQuality.FAsVoid();
      SkyEffectManager.vAsVoid();
      SkyShader var4 = SkyShader.EAsSkyShader();
      if (var4 != null) {
         var4.tAsVoid();
      }
   }

   @Inject(
      method = {"method_3192(Lnet/minecraft/class_9779;Z)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_11228;method_70890(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"
      )}
   )
   private void larpclient$injectZV2(RenderTickCounter var1, boolean var2, CallbackInfo var3) {
      MotionBlurEffect.vAsVoid();
      WorldColorPostEffect.AAsVoid();
      BloomQuality.iAsVoid();
   }

   @ModifyExpressionValue(
      method = {"method_3188(Lnet/minecraft/class_9779;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_757;method_3196(Lnet/minecraft/class_4184;FZ)F",
         ordinal = 0
      )}
   )
   private float larpclient$modifyExpressionValueV(float var1) {
      WebRenderer.XAsVoid(var1);
      return var1;
   }

   @ModifyExpressionValue(
      method = {"method_3188(Lnet/minecraft/class_9779;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_757;method_3196(Lnet/minecraft/class_4184;FZ)F",
         ordinal = 1
      )}
   )
   private float larpclient$modifyExpressionValueV2(float var1) {
      WebRenderer.HAsVoid(var1);
      return var1;
   }
}
