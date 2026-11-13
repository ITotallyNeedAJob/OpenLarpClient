package com.larpclient.mixin;

import com.larpclient.module.NoFall;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({ClientPlayerEntity.class})
public class ClientPlayerEntitySendMovementPacketsMixin2 {
   public ClientPlayerEntitySendMovementPacketsMixin2() {
   }

   @ModifyArg(
      method = {"method_3136()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_2828$class_2830;<init>(Lnet/minecraft/class_243;FFZZ)V"
      )},
      index = 3,
      require = 1
   )
   private boolean larpclient$modifyArgSendMovementPackets(boolean var1) {
      return NoFall.UAsBoolean(var1);
   }

   @ModifyArg(
      method = {"method_3136()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_2828$class_2829;<init>(Lnet/minecraft/class_243;ZZ)V"
      )},
      index = 1,
      require = 1
   )
   private boolean larpclient$modifyArgSendMovementPackets2(boolean var1) {
      return NoFall.UAsBoolean(var1);
   }

   @ModifyArg(
      method = {"method_3136()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_2828$class_2831;<init>(FFZZ)V"
      )},
      index = 2,
      require = 1
   )
   private boolean larpclient$modifyArgSendMovementPackets3(boolean var1) {
      return NoFall.UAsBoolean(var1);
   }

   @ModifyArg(
      method = {"method_3136()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_2828$class_5911;<init>(ZZ)V"
      )},
      index = 0,
      require = 1
   )
   private boolean larpclient$modifyArgSendMovementPackets4(boolean var1) {
      return NoFall.UAsBoolean(var1);
   }
}
