package com.larpclient.mixin;

import com.larpclient.module.NoSlowdown;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class})
public abstract class EntitySlowMovementMixin {
   public EntitySlowMovementMixin() {
   }

   @Inject(
      method = {"method_5844(Lnet/minecraft/class_2680;Lnet/minecraft/class_243;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(BlockState var1, Vec3d var2, CallbackInfo var3) {
      NoSlowdown var4 = NoSlowdown.yAsNoSlowdown();
      if ((Object)this == MinecraftClient.getInstance().player && var4 != null && var4.YAsBoolean() && var1.isOf(Blocks.COBWEB)) {
         ((Entity)(Object)this).onLanding();
         var3.cancel();
      }
   }

   @Inject(
      method = {"method_23326()F"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectGetVelocityMultiplier(CallbackInfoReturnable<Float> var1) {
      NoSlowdown var2 = NoSlowdown.yAsNoSlowdown();
      Entity var3 = (Entity)(Object)this;
      if (var3 == MinecraftClient.getInstance().player && var2 != null && var2.OAsBoolean()) {
         BlockState var4 = var3.getEntityWorld().getBlockState(var3.getBlockPos());
         BlockState var5 = var3.getEntityWorld().getBlockState(var3.getVelocityAffectingPos());
         if (var4.isOf(Blocks.SOUL_SAND) || var4.getBlock().getVelocityMultiplier() == 1.0F && var5.isOf(Blocks.SOUL_SAND)) {
            var1.setReturnValue(1.0F);
         }
      }
   }
}
