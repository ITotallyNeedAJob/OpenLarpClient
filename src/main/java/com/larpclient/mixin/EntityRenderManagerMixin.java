package com.larpclient.mixin;

import com.larpclient.module.KillFX;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityRenderManager.class})
public final class EntityRenderManagerMixin {
   public EntityRenderManagerMixin() {
   }

   @Inject(
      method = {"method_72977(Lnet/minecraft/class_1297;F)Lnet/minecraft/class_10017;"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectHandler(Entity var1, float var2, CallbackInfoReturnable<EntityRenderState> var3) {
      KillFX var4 = KillFX.aAsKillFX();
      if (var4 != null) {
         var4.LAsVoid(var1, (EntityRenderState)var3.getReturnValue());
      }
   }

   @Inject(
      method = {"method_72976(Lnet/minecraft/class_10017;Lnet/minecraft/class_12075;DDDLnet/minecraft/class_4587;Lnet/minecraft/class_11659;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(
      EntityRenderState var1,
      CameraRenderState var2,
      double var3,
      double var5,
      double var7,
      MatrixStack var9,
      OrderedRenderCommandQueue var10,
      CallbackInfo var11
   ) {
      KillFX var12 = KillFX.aAsKillFX();
      if (var12 != null && var12.uAsBoolean(var1)) {
         var11.cancel();
      }
   }
}
