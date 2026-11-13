package com.larpclient.mixin;

import com.larpclient.module.Removals;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.render.fog.StatusEffectFogModifier;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({FogRenderer.class})
public class FogRendererMixin {
   public FogRendererMixin() {
   }

   @Inject(
      method = {"method_71652(Lnet/minecraft/class_4184;)Lnet/minecraft/class_5636;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectHandler(Camera var1, CallbackInfoReturnable<CameraSubmersionType> var2) {
      if (var2.getReturnValue() == CameraSubmersionType.WATER && Removals.xAsBoolean("water")
         || var2.getReturnValue() == CameraSubmersionType.LAVA && Removals.xAsBoolean("lava")) {
         var2.setReturnValue(CameraSubmersionType.ATMOSPHERIC);
      }
   }

   @Redirect(
      method = {"method_62185(Lnet/minecraft/class_4184;FLnet/minecraft/class_638;IF)Lorg/joml/Vector4f;", "method_3211(Lnet/minecraft/class_4184;ILnet/minecraft/class_9779;FLnet/minecraft/class_638;)Lorg/joml/Vector4f;"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_11400;method_42593(Lnet/minecraft/class_5636;Lnet/minecraft/class_1297;)Z"
      )},
      require = 2,
      allow = 2
   )
   private boolean larpclient$redirectHandler(FogModifier var1, CameraSubmersionType var2, Entity var3) {
      return (!(var1 instanceof StatusEffectFogModifier) || !Removals.fAsBoolean()) && var1.shouldApply(var2, var3);
   }
}
