package com.larpclient.mixin;

import com.larpclient.module.AirStuck;
import com.larpclient.module.ElytraMotion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Entity.class})
public class EntityMoveMixin {
   public EntityMoveMixin() {
   }

   @Inject(
      method = {"method_5784(Lnet/minecraft/class_1313;Lnet/minecraft/class_243;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(MovementType var1, Vec3d var2, CallbackInfo var3) {
      if ((Object)this == MinecraftClient.getInstance().player) {
         AirStuck var4 = AirStuck.zAsAirStuck();
         ElytraMotion var5 = ElytraMotion.vAsElytraMotion();
         if (var4 != null && var4.wAsBoolean() || var5 != null && var5.ZAsBoolean()) {
            ((Entity)(Object)this).setVelocity(Vec3d.ZERO);
            var3.cancel();
         }
      }
   }
}
