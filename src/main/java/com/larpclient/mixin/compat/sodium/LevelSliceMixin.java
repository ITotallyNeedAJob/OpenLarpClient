package com.larpclient.mixin.compat.sodium;

import com.larpclient.FakeSpawnerController;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(
   targets = {"net/caffeinemc/mods/sodium/client/world/LevelSlice"},
   remap = false
)
public abstract class LevelSliceMixin {
   public LevelSliceMixin() {
   }

   @Inject(
      target = {@Desc(
         args = {int.class, int.class, int.class},
         ret = BlockState.class,
         value = "getBlockState"
      )},
      at = {@At("RETURN")},
      cancellable = true,
      remap = false
   )
   private void larpclient$injectHandler(int var1, int var2, int var3, CallbackInfoReturnable<BlockState> var4) {
      FakeSpawnerController var5 = FakeSpawnerController.ZAsMa();
      long var6 = BlockPos.asLong(var1, var2, var3);
      var4.setReturnValue(var5.YAsBlockState(var6, var5.cAsBlockState(var6, (BlockState)var4.getReturnValue())));
   }
}
