package com.larpclient.mixin;

import com.larpclient.SkyEffectManager;
import com.larpclient.module.SkyShader;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.render.CloudRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({CloudRenderer.class})
public class CloudRendererMixin {
   public CloudRendererMixin() {
   }

   @Inject(
      method = {"method_62168(ILnet/minecraft/class_4063;FLnet/minecraft/class_243;JF)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectJFV(int var1, CloudRenderMode var2, float var3, Vec3d var4, long var5, float var7, CallbackInfo var8) {
      SkyShader var9 = SkyShader.EAsSkyShader();
      if (SkyEffectManager.AAsBoolean() || var9 != null && var9.aAsBoolean()) {
         var8.cancel();
      }
   }
}
