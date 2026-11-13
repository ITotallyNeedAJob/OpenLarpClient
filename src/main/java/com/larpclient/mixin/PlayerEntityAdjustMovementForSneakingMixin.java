package com.larpclient.mixin;

import com.larpclient.module.Scaffold;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({PlayerEntity.class})
public abstract class PlayerEntityAdjustMovementForSneakingMixin {
   public PlayerEntityAdjustMovementForSneakingMixin() {
   }

   @Shadow
   protected abstract boolean method_21825();

   @Redirect(
      method = {"method_18796(Lnet/minecraft/class_243;Lnet/minecraft/class_1313;)Lnet/minecraft/class_243;"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1657;method_21825()Z"
      )}
   )
   private boolean larpclient$redirectHandler(PlayerEntity var1) {
      return this.method_21825() || Scaffold.yAsBoolean(var1);
   }
}
