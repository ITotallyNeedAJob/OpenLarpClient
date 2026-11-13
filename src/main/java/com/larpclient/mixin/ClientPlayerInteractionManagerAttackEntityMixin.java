package com.larpclient.mixin;

import com.larpclient.module.TotemHit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayerInteractionManager.class})
public abstract class ClientPlayerInteractionManagerAttackEntityMixin {
   public ClientPlayerInteractionManagerAttackEntityMixin() {
   }

   @Inject(
      method = {"method_2918(Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_636;method_2911()V"
      )},
      require = 1,
      allow = 1
   )
   private void larpclient$injectV(PlayerEntity var1, Entity var2, CallbackInfo var3) {
      if (var1 == MinecraftClient.getInstance().player && var1 instanceof ClientPlayerEntity var4) {
         TotemHit.fAsVoid(var4, var2);
      }
   }
}
