package com.larpclient.mixin;

import com.larpclient.module.FakeLag;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayerEntity.class})
public class ClientPlayerEntitySendMovementPacketsMixin {
   @Shadow
   private int field_3923;
   @Shadow
   private float field_3925;
   @Shadow
   private float field_3941;

   public ClientPlayerEntitySendMovementPacketsMixin() {
   }

   @Inject(
      method = {"method_3136()V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectSendMovementPackets(CallbackInfo var1) {
      if (FakeLag.KAsBoolean((ClientPlayerEntity)(Object)this)) {
         this.field_3923 = 20;
         this.field_3925 = Float.NaN;
         this.field_3941 = Float.NaN;
      }
   }
}
