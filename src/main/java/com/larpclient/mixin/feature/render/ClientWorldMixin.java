package com.larpclient.mixin.feature.render;

import com.larpclient.VolumetricWaterEngine;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientWorld.class})
public abstract class ClientWorldMixin {
   public ClientWorldMixin() {
   }

   @Inject(
      method = {"method_8413(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;Lnet/minecraft/class_2680;I)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectIV(BlockPos var1, BlockState var2, BlockState var3, int var4, CallbackInfo var5) {
      if (var2 != var3) {
         VolumetricWaterEngine.jAsVoid(var1, var2, var3);
      }
   }
}
