package com.larpclient.mixin;

import com.larpclient.pk;
import com.larpclient.module.Mace;
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
public abstract class ClientPlayerInteractionManagerAttackEntityMixin2 {
   public ClientPlayerInteractionManagerAttackEntityMixin2() {
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
      Mace var4 = Mace.yAsMace();
      if (var1 == MinecraftClient.getInstance().player
         && var1 instanceof ClientPlayerEntity var5
         && var4 != null
         && var4.isRuntimeActive()
         && !pk.BAsBoolean(var5)) {
         var4.LAsVoid(var5, var2);
      }
   }

   @Inject(
      method = {"method_2918(Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;)V"},
      at = {@At("TAIL")},
      require = 1,
      allow = 1
   )
   private void larpclient$injectV2(PlayerEntity var1, Entity var2, CallbackInfo var3) {
      Mace var4 = Mace.yAsMace();
      if (var1 == MinecraftClient.getInstance().player && var1 instanceof ClientPlayerEntity var5 && var4 != null && var4.isRuntimeActive()) {
         var4.wAsVoid(var5, var2);
      }
   }
}
