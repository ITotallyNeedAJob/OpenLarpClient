package com.larpclient.mixin;

import com.larpclient.module.TimeChanger;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({World.class})
public class WorldMixin {
   public WorldMixin() {
   }

   @Inject(
      method = {"method_8532()J"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectGetTimeOfDay(CallbackInfoReturnable<Long> var1) {
      TimeChanger var2 = TimeChanger.QAsTimeChanger();
      if ((Object)this instanceof ClientWorld && var2 != null && var2.isRuntimeActive()) {
         var1.setReturnValue(TimeChanger.xAsLong((Long)var1.getReturnValue(), var2.time()));
      }
   }
}
