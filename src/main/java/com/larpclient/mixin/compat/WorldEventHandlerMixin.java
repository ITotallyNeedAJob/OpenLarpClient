package com.larpclient.mixin.compat;

import com.larpclient.FakeSpawnerController;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.WorldEventHandler;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({WorldEventHandler.class})
public abstract class WorldEventHandlerMixin {
   public WorldEventHandlerMixin() {
   }

   @WrapOperation(
      method = {"method_62194(ILnet/minecraft/class_2338;I)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_2248;method_9531(I)Lnet/minecraft/class_2680;",
         ordinal = 0
      )}
   )
   private BlockState larpclient$wrapOperationIV(int var1, Operation<BlockState> var2, int var3, BlockPos var4, int var5) {
      return FakeSpawnerController.ZAsMa().AAsBlockState(var4, (BlockState)var2.call(new Object[]{var1}));
   }
}
