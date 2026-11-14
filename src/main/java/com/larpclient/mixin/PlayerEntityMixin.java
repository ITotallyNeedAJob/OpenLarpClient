package com.larpclient.mixin;

import com.larpclient.ParticleKind;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerEntity.class})
public abstract class PlayerEntityMixin {
   public PlayerEntityMixin() {
   }

   @Inject(
      method = {"method_75124()V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectBeforePlayerAttack(CallbackInfo var1) {
      pk.OAsVoid((PlayerEntity)(Object)this);
   }
}
