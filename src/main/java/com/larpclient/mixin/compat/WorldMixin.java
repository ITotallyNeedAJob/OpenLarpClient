package com.larpclient.mixin.compat;

import com.larpclient.FakeSpawnerController;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({World.class})
public abstract class WorldMixin {
   public WorldMixin() {
   }

   @Inject(
      method = {"method_8321(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2586;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler(BlockPos var1, CallbackInfoReturnable<BlockEntity> var2) {
      if ((Object)this instanceof ClientWorld var3) {
         MobSpawnerBlockEntity var5 = FakeSpawnerController.ZAsMa().KAsMobSpawnerBlockEntity(var3, var1);
         if (var5 != null) {
            var2.setReturnValue(var5);
         }
      }
   }
}
