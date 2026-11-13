package com.larpclient.mixin;

import com.larpclient.SharedRenderEffects;
import com.larpclient.module.ClickPulse;
import com.larpclient.module.DamageMarkers;
import com.larpclient.module.HitSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayerInteractionManager.class})
public class ClientPlayerInteractionManagerAttackEntityMixin4 {
   public ClientPlayerInteractionManagerAttackEntityMixin4() {
   }

   @Inject(
      method = {"method_2918(Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV(PlayerEntity var1, Entity var2, CallbackInfo var3) {
      DamageMarkers var4 = DamageMarkers.DAsDamageMarkers();
      if (var4 != null && var1 == MinecraftClient.getInstance().player) {
         var4.SAsVoid(var2);
      }

      HitSounds var5 = HitSounds.hAsHitSounds();
      if (var5 != null && var1 == MinecraftClient.getInstance().player) {
         var5.IAsVoid(var2);
      }

      ClickPulse var6 = ClickPulse.zAsClickPulse();
      if (var6 != null && var1 == MinecraftClient.getInstance().player) {
         var6.hAsVoid(var2);
      }
   }

   @Inject(
      method = {"method_2918(Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV2(PlayerEntity var1, Entity var2, CallbackInfo var3) {
      if (var2 instanceof LivingEntity var4) {
         SharedRenderEffects.dAsVoid(var4);
      }
   }
}
