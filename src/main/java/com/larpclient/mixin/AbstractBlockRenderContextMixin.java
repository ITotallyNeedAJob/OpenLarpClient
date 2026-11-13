package com.larpclient.mixin;

import com.larpclient.module.XRay;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(
   targets = {"net/caffeinemc/mods/sodium/client/render/model/AbstractBlockRenderContext"},
   remap = false
)
public abstract class AbstractBlockRenderContextMixin {
   public AbstractBlockRenderContextMixin() {
   }

   @WrapOperation(
      method = {"shouldDrawSide"},
      require = 0,
      remap = false,
      at = {@At(
         value = "INVOKE",
         ordinal = 0,
         target = "Lnet/minecraft/class_2680;method_26173(Lnet/minecraft/class_2350;)Lnet/minecraft/class_265;",
         remap = true
      )}
   )
   private VoxelShape larpclient$wrapOperationShouldDrawSide(BlockState var1, Direction var2, Operation<VoxelShape> var3) {
      return XRay.aAsBoolean() && !XRay.ZAsBoolean(var1.getBlock()) ? VoxelShapes.empty() : (VoxelShape)var3.call(new Object[]{var1, var2});
   }
}
