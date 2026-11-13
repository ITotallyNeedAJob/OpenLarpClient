package com.larpclient.mixin;

import com.larpclient.BloomQuality;
import com.larpclient.FlameParticles;
import com.larpclient.HandEffectRenderer;
import com.larpclient.KnifeAssets;
import net.minecraft.client.gl.ShaderLoader;
import net.minecraft.client.gl.ShaderLoader.Definitions;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ShaderLoader.class})
public class ShaderLoaderMixin {
   public ShaderLoaderMixin() {
   }

   @Inject(
      method = {"method_62945(Lnet/minecraft/class_10151$class_10153;Lnet/minecraft/class_3300;Lnet/minecraft/class_3695;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV(Definitions var1, ResourceManager var2, Profiler var3, CallbackInfo var4) {
      BloomQuality.yAsVoid();
      FlameParticles.UAsVoid();
      HandEffectRenderer.KAsVoid();
      KnifeAssets.LAsVoid();
   }
}
