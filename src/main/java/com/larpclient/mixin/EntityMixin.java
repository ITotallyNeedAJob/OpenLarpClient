package com.larpclient.mixin;

import com.larpclient.CometRenderer;
import com.larpclient.module.AimAssist;
import com.larpclient.module.GrimSpeed;
import com.larpclient.module.Speed;
import com.larpclient.module.TargetStrafe;
import com.larpclient.module.WTap;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Entity.class})
public class EntityMixin {
   public EntityMixin() {
   }

   @Inject(
      method = {"method_30634(DDD)V", "method_5808(DDDFF)V", "method_74862(Lnet/minecraft/class_10182;Lnet/minecraft/class_10182;Ljava/util/Set;)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectUpdatePosition(CallbackInfo var1) {
      if ((Object)this instanceof ClientPlayerEntity var2) {
         Speed.dAsVoid(var2);
         TargetStrafe.LAsVoid(var2);
         GrimSpeed.PAsVoid(var2);
         AimAssist.fAsVoid(var2);
         WTap.RAsVoid(var2);
         CometRenderer.uAsVoid();
      }
   }
}
