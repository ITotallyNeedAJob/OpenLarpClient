package com.larpclient.mixin;

import com.larpclient.module.BoatFly;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AbstractBoatEntity.class})
public abstract class AbstractBoatEntityMixin {
   @Shadow
   private float field_54456;

   public AbstractBoatEntityMixin() {
   }

   @Inject(
      method = {"method_64482()V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectUpdatePaddles(CallbackInfo var1) {
      if (BoatFly.SAsBoolean((AbstractBoatEntity)(Object)this)) {
         this.field_54456 = 0.0F;
         var1.cancel();
      }
   }
}
