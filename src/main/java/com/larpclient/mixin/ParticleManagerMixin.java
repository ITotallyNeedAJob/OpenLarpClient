package com.larpclient.mixin;

import com.larpclient.module.HitFX;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ParticleManager.class})
public class ParticleManagerMixin {
   public ParticleManagerMixin() {
   }

   @Inject(
      method = {"method_3056(Lnet/minecraft/class_2394;DDDDDD)Lnet/minecraft/class_703;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler(
      ParticleEffect var1, double var2, double var4, double var6, double var8, double var10, double var12, CallbackInfoReturnable<Particle> var14
   ) {
      ParticleType var15 = var1.getType();
      HitFX var16 = HitFX.tAsHitFX();
      if (var16 != null
         && var16.isRuntimeActive()
         && (
            var15 == ParticleTypes.SMOKE
               || var15 == ParticleTypes.LARGE_SMOKE
               || var15 == ParticleTypes.WHITE_SMOKE
               || var15 == ParticleTypes.CAMPFIRE_COSY_SMOKE
               || var15 == ParticleTypes.CAMPFIRE_SIGNAL_SMOKE
               || var15 == ParticleTypes.CRIT
               || var15 == ParticleTypes.ENCHANTED_HIT
               || var15 == ParticleTypes.DAMAGE_INDICATOR
               || var15 == ParticleTypes.SWEEP_ATTACK
         )) {
         var14.setReturnValue(null);
      }
   }
}
