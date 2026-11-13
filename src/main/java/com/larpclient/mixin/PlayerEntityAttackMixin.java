package com.larpclient.mixin;

import com.larpclient.module.AimAssist;
import com.larpclient.module.WTap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerEntity.class})
public class PlayerEntityAttackMixin {
   public PlayerEntityAttackMixin() {
   }

   @Inject(
      method = {"method_7324(Lnet/minecraft/class_1297;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV(Entity var1, CallbackInfo var2) {
      WTap.jAsVoid((PlayerEntity)(Object)this, var1);
   }

   @Inject(
      method = {"method_7324(Lnet/minecraft/class_1297;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1657;method_75124()V"
      )},
      require = 1,
      allow = 1
   )
   private void larpclient$injectV2(Entity var1, CallbackInfo var2) {
      WTap.cAsVoid((PlayerEntity)(Object)this, var1);
      AimAssist.dAsVoid((PlayerEntity)(Object)this, var1);
   }

   @Inject(
      method = {"method_7324(Lnet/minecraft/class_1297;)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectV3(Entity var1, CallbackInfo var2) {
      WTap.EAsVoid((PlayerEntity)(Object)this, var1);
   }
}
