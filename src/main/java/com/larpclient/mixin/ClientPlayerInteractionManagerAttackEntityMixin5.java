package com.larpclient.mixin;

import com.larpclient.ModuleImportBroker;
import com.larpclient.ParticleEmitter;
import com.larpclient.module.Aura;
import com.larpclient.module.AutoTool;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientPlayerInteractionManager.class})
public class ClientPlayerInteractionManagerAttackEntityMixin5 {
   public ClientPlayerInteractionManagerAttackEntityMixin5() {
   }

   @Shadow
   private void method_2911() {
      throw new AssertionError();
   }

   @Inject(
      method = {"method_2918(Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(PlayerEntity var1, Entity var2, CallbackInfo var3) {
      if (!ModuleImportBroker.YAsBoolean(var2)) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"method_2896(Lnet/minecraft/class_746;Lnet/minecraft/class_1268;Lnet/minecraft/class_3965;)Lnet/minecraft/class_1269;", "method_2919(Lnet/minecraft/class_1657;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;", "method_2905(Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;", "method_2917(Lnet/minecraft/class_1657;Lnet/minecraft/class_1297;Lnet/minecraft/class_3966;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler(CallbackInfoReturnable<ActionResult> var1) {
      if (Aura.WAsBoolean() || !ModuleImportBroker.JAsBoolean()) {
         var1.setReturnValue(ActionResult.PASS);
      }
   }

   @Inject(
      method = {"method_75407(Lnet/minecraft/class_12125;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(CallbackInfo var1) {
      if (!ModuleImportBroker.JAsBoolean()) {
         var1.cancel();
      }
   }

   @Inject(
      method = {"method_2910(Lnet/minecraft/class_2338;Lnet/minecraft/class_2350;)Z", "method_2902(Lnet/minecraft/class_2338;Lnet/minecraft/class_2350;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectZ(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      if (ParticleEmitter.oAsBoolean()) {
         var3.setReturnValue(false);
      } else if (AutoTool.UAsBoolean(var1)) {
         this.method_2911();
      }
   }
}
