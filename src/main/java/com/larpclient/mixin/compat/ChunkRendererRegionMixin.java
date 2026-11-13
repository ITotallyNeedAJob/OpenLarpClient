package com.larpclient.mixin.compat;

import com.larpclient.FakeSpawnerController;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ChunkRendererRegion.class})
public abstract class ChunkRendererRegionMixin {
   @Shadow
   @Final
   private World field_4490;

   public ChunkRendererRegionMixin() {
   }

   @Inject(
      method = {"method_8320(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2680;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectHandler(BlockPos var1, CallbackInfoReturnable<BlockState> var2) {
      var2.setReturnValue(
         FakeSpawnerController.ZAsMa().mAsBlockState(var1, FakeSpawnerController.ZAsMa().AAsBlockState(var1, (BlockState)var2.getReturnValue()))
      );
   }

   @Inject(
      method = {"method_8321(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2586;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler2(BlockPos var1, CallbackInfoReturnable<BlockEntity> var2) {
      MobSpawnerBlockEntity var3 = FakeSpawnerController.ZAsMa().KAsMobSpawnerBlockEntity(this.field_4490, var1);
      if (var3 != null) {
         var2.setReturnValue(var3);
      }
   }
}
