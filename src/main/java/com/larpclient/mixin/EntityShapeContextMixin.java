package com.larpclient.mixin;

import com.larpclient.module.Jesus;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.CollisionView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityShapeContext.class})
public class EntityShapeContextMixin {
   public EntityShapeContextMixin() {
   }

   @Inject(
      method = {"method_62878(Lnet/minecraft/class_2680;Lnet/minecraft/class_1941;Lnet/minecraft/class_2338;)Lnet/minecraft/class_265;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectHandler(BlockState var1, CollisionView var2, BlockPos var3, CallbackInfoReturnable<VoxelShape> var4) {
      var4.setReturnValue(Jesus.hAsVoxelShape(var1, var2, var3, (EntityShapeContext)(Object)this, (VoxelShape)var4.getReturnValue()));
   }
}
