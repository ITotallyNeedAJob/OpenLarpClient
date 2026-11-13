package com.larpclient.mixin;

import com.larpclient.module.BaseFinder;
import com.larpclient.module.HolesESP;
import com.larpclient.module.PrimeChunkFinder;
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
      method = {"method_41928(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;I)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectIV(BlockPos var1, BlockState var2, int var3, CallbackInfo var4) {
      BaseFinder.AAsVoid((ClientWorld)(Object)this, var1, var2);
   }

   @Inject(
      method = {"method_41928(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;I)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectIV2(BlockPos var1, BlockState var2, int var3, CallbackInfo var4) {
      PrimeChunkFinder.eAsVoid((ClientWorld)(Object)this, var1, var2);
   }

   @Inject(
      method = {"method_41928(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;I)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectIV3(BlockPos var1, BlockState var2, int var3, CallbackInfo var4) {
      HolesESP.NAsVoid((ClientWorld)(Object)this, var1);
   }
}
