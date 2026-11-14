package com.larpclient.mixin;

import com.larpclient.ParticleKind;
import com.larpclient.module.AutoDrain;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientPlayerInteractionManager.class})
public abstract class ClientPlayerInteractionManagerInteractItemMixin {
   public ClientPlayerInteractionManagerInteractItemMixin() {
   }

   @Inject(
      method = {"method_2919(Lnet/minecraft/class_1657;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectHandler(PlayerEntity var1, Hand var2, CallbackInfoReturnable<ActionResult> var3) {
      AutoDrain.QAsVoid(var1, var2);
   }

   @Inject(
      method = {"method_2919(Lnet/minecraft/class_1657;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectHandler2(PlayerEntity var1, Hand var2, CallbackInfoReturnable<ActionResult> var3) {
      pk.OAsVoid(var1);
   }

   @Inject(
      method = {"method_2896(Lnet/minecraft/class_746;Lnet/minecraft/class_1268;Lnet/minecraft/class_3965;)Lnet/minecraft/class_1269;"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectHandler(ClientPlayerEntity var1, Hand var2, BlockHitResult var3, CallbackInfoReturnable<ActionResult> var4) {
      pk.OAsVoid(var1);
   }

   @Inject(
      method = {"method_2917(Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;Lnet/minecraft/class_3966;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectHandler(PlayerEntity var1, Entity var2, EntityHitResult var3, Hand var4, CallbackInfoReturnable<ActionResult> var5) {
      pk.OAsVoid(var1);
   }

   @Inject(
      method = {"method_2905(Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectHandler(PlayerEntity var1, Entity var2, Hand var3, CallbackInfoReturnable<ActionResult> var4) {
      pk.OAsVoid(var1);
   }

   @Inject(
      method = {"method_2897(Lnet/minecraft/class_1657;)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectV(PlayerEntity var1, CallbackInfo var2) {
      pk.OAsVoid(var1);
   }
}
