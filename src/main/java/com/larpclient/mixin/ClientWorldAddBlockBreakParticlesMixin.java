package com.larpclient.mixin;

import com.larpclient.module.Removals;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientWorld.class})
public class ClientWorldAddBlockBreakParticlesMixin {
   public ClientWorldAddBlockBreakParticlesMixin() {
   }

   @Inject(
      method = {"method_31595(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(BlockPos var1, BlockState var2, CallbackInfo var3) {
      if (Removals.xAsBoolean("breakParticles")) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"method_74254(Lnet/minecraft/class_2338;Lnet/minecraft/class_2350;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(BlockPos var1, Direction var2, CallbackInfo var3) {
      if (Removals.xAsBoolean("breakParticles")) {
         var3.cancel();
      }
   }
}
