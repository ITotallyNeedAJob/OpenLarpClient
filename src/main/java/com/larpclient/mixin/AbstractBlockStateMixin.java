package com.larpclient.mixin;

import com.larpclient.module.XRay;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractBlockState.class})
public class AbstractBlockStateMixin {
   public AbstractBlockStateMixin() {
   }

   @Inject(
      method = {"method_26216()Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectIsOpaqueFullCube(CallbackInfoReturnable<Boolean> var1) {
      if (XRay.aAsBoolean()) {
         AbstractBlockState var2 = (AbstractBlockState)(Object)this;
         if (!XRay.ZAsBoolean(var2.getBlock())) {
            var1.setReturnValue(false);
         }
      }
   }

   @Inject(
      method = {"method_26225()Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectIsOpaque(CallbackInfoReturnable<Boolean> var1) {
      if (XRay.aAsBoolean()) {
         AbstractBlockState var2 = (AbstractBlockState)(Object)this;
         if (!XRay.ZAsBoolean(var2.getBlock())) {
            var1.setReturnValue(false);
         }
      }
   }

   @Inject(
      method = {"method_26217()Lnet/minecraft/class_2464;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler(CallbackInfoReturnable<BlockRenderType> var1) {
      if (XRay.aAsBoolean()) {
         AbstractBlockState var2 = (AbstractBlockState)(Object)this;
         if (!XRay.ZAsBoolean(var2.getBlock())) {
            var1.setReturnValue(BlockRenderType.INVISIBLE);
         }
      }
   }
}
