package com.larpclient.mixin;

import com.larpclient.module.AntiTrap;
import com.larpclient.module.IgnoreEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientPlayerInteractionManager.class})
public class ClientPlayerInteractionManagerInteractBlockMixin {
   public ClientPlayerInteractionManagerInteractBlockMixin() {
   }

   @Inject(
      method = {"method_2896(Lnet/minecraft/class_746;Lnet/minecraft/class_1268;Lnet/minecraft/class_3965;)Lnet/minecraft/class_1269;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler(ClientPlayerEntity var1, Hand var2, BlockHitResult var3, CallbackInfoReturnable<ActionResult> var4) {
      ClientWorld var5 = MinecraftClient.getInstance().world;
      if (var5 != null && AntiTrap.YAsBoolean(var5.getBlockState(var3.getBlockPos())) && !var1.isSneaking()) {
         var4.setReturnValue(ActionResult.FAIL);
      }
   }

   @Inject(
      method = {"method_2905(Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler(PlayerEntity var1, Entity var2, Hand var3, CallbackInfoReturnable<ActionResult> var4) {
      if (AntiTrap.TAsBoolean(var2) || IgnoreEntity.yAsBoolean(var2)) {
         var4.setReturnValue(ActionResult.FAIL);
      }
   }

   @Inject(
      method = {"method_2917(Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;Lnet/minecraft/class_3966;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler(PlayerEntity var1, Entity var2, EntityHitResult var3, Hand var4, CallbackInfoReturnable<ActionResult> var5) {
      if (AntiTrap.TAsBoolean(var2) || IgnoreEntity.yAsBoolean(var2)) {
         var5.setReturnValue(ActionResult.FAIL);
      }
   }
}
