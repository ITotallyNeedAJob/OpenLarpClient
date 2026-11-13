package com.larpclient.mixin;

import com.larpclient.module.Removals;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class})
public class EntityIsGlowingMixin {
   public EntityIsGlowingMixin() {
   }

   @Inject(
      method = {"method_5851()Z"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectIsGlowing(CallbackInfoReturnable<Boolean> var1) {
      if (((Entity)(Object)this).getEntityWorld().isClient() && Removals.xAsBoolean("glowing")) {
         var1.setReturnValue(false);
      }
   }
}
