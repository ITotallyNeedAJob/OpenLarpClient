package com.larpclient.mixin;

import com.larpclient.module.Animations;
import com.larpclient.module.SwordModels;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public class LivingEntityGetHandSwingDurationMixin {
   public LivingEntityGetHandSwingDurationMixin() {
   }

   @Inject(
      method = {"method_6028()I"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectGetHandSwingDuration(CallbackInfoReturnable<Integer> var1) {
      if ((Object)this instanceof ClientPlayerEntity var2 && (Object)this == MinecraftClient.getInstance().player) {
         SwordModels var6 = SwordModels.vAsSwordModels();
         Hand var4 = var2.preferredHand == null ? Hand.MAIN_HAND : var2.preferredHand;
         if (var6 == null || !var6.rAsBoolean(var2.getStackInHand(var4))) {
            Animations var5 = Animations.oAsAnimations();
            if (var5 != null && var5.isRuntimeActive() && var5.CAsBoolean()) {
               var1.setReturnValue(var5.vAsInt((Integer)var1.getReturnValue()));
            }
         }
      }
   }
}
