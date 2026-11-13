package com.larpclient.mixin;

import com.larpclient.module.NoBlockRotation;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractBlockState.class})
public abstract class AbstractBlockStateGetRenderingSeedMixin {
   public AbstractBlockStateGetRenderingSeedMixin() {
   }

   @Inject(
      method = {"method_26190(Lnet/minecraft/class_2338;)J"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectJ(BlockPos var1, CallbackInfoReturnable<Long> var2) {
      if (NoBlockRotation.EAsBoolean()) {
         var2.setReturnValue(42L);
      }
   }
}
