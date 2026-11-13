package com.larpclient.mixin;

import com.larpclient.module.XRay;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Block.class})
public class BlockMixin {
   public BlockMixin() {
   }

   @Inject(
      method = {"method_9607(Lnet/minecraft/class_2680;Lnet/minecraft/class_2680;Lnet/minecraft/class_2350;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void larpclient$injectZ(BlockState var0, BlockState var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      if (XRay.aAsBoolean() && XRay.ZAsBoolean(var0.getBlock()) && !XRay.ZAsBoolean(var1.getBlock())) {
         var3.setReturnValue(true);
      }
   }
}
