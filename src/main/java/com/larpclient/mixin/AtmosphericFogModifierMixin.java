package com.larpclient.mixin;

import com.larpclient.DynamicLoader;
import com.larpclient.module.CustomSky;
import com.larpclient.module.SkyShader;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.AtmosphericFogModifier;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AtmosphericFogModifier.class})
public class AtmosphericFogModifierMixin {
   public AtmosphericFogModifierMixin() {
   }

   @Inject(
      method = {"method_42591(Lnet/minecraft/class_7285;Lnet/minecraft/class_4184;Lnet/minecraft/class_638;FLnet/minecraft/class_9779;)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectV(FogData var1, Camera var2, ClientWorld var3, float var4, RenderTickCounter var5, CallbackInfo var6) {
      CustomSky var7 = CustomSky.NAsCustomSky();
      if (CustomSky.yAsBoolean()) {
         float var8 = var7.fogDistance();
         var1.environmentalStart *= var8;
         var1.environmentalEnd *= var8;
         var1.renderDistanceStart *= var8;
         var1.renderDistanceEnd *= var8;
         var1.skyEnd *= var8;
         var1.cloudEnd *= var8;
      }
   }

   @Inject(
      method = {"method_71654(Lnet/minecraft/class_638;Lnet/minecraft/class_4184;IF)I"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectIFI(ClientWorld var1, Camera var2, int var3, float var4, CallbackInfoReturnable<Integer> var5) {
      DynamicLoader.EAsVoid((Integer)var5.getReturnValue());
      CustomSky var6 = CustomSky.NAsCustomSky();
      SkyShader var7 = SkyShader.EAsSkyShader();
      if (CustomSky.yAsBoolean()) {
         if (!var6.matchFog()) {
            return;
         }

         var5.setReturnValue(var6.YAsNv().horizon());
      } else {
         if (var7 == null || !var7.CAsBoolean()) {
            return;
         }

         var5.setReturnValue(var7.FAsInt());
      }

      DynamicLoader.EAsVoid((Integer)var5.getReturnValue());
   }
}
