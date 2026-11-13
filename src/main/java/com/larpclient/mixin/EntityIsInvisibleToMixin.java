package com.larpclient.mixin;

import com.larpclient.p5;
import com.larpclient.module.Freecam;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class})
public abstract class EntityIsInvisibleToMixin {
   public EntityIsInvisibleToMixin() {
   }

   @Inject(
      method = {"method_5756(Lnet/minecraft/class_1657;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectZ(PlayerEntity var1, CallbackInfoReturnable<Boolean> var2) {
      if (p5.oAsBoolean() && Freecam.bAsBoolean() && (Object)this == MinecraftClient.getInstance().player && var1 == (Object)this) {
         var2.setReturnValue(false);
      }
   }
}
