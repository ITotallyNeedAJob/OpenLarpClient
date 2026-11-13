package com.larpclient.mixin;

import com.larpclient.module.NoJumpDelay;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntity.class})
public class LivingEntityTickMovementMixin {
   @Shadow
   private int field_6228;

   public LivingEntityTickMovementMixin() {
   }

   @Inject(
      method = {"method_6007()V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectTickMovement(CallbackInfo var1) {
      this.field_6228 = Math.min(this.field_6228, NoJumpDelay.vAsInt((LivingEntity)(Object)this, this.field_6228));
   }

   @ModifyConstant(
      method = {"method_6007()V"},
      constant = {@Constant(
         intValue = 10
      )},
      require = 1,
      allow = 1
   )
   private int larpclient$modifyConstantTickMovement(int var1) {
      return NoJumpDelay.vAsInt((LivingEntity)(Object)this, var1);
   }
}
