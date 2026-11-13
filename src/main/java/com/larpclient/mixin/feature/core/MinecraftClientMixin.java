package com.larpclient.mixin.feature.core;

import com.larpclient.AutoTunnel;
import com.larpclient.MiningProgress;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({MinecraftClient.class})
public abstract class MinecraftClientMixin {
   public MinecraftClientMixin() {
   }

   @WrapOperation(
      method = {"method_1523(Z)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_437;method_25421()Z"
      )}
   )
   private boolean larpclient$wrapOperationRender(Screen var1, Operation<Boolean> var2) {
      return (Boolean)var2.call(new Object[]{var1}) && !AutoTunnel.IAsBoolean();
   }

   @WrapOperation(
      method = {"method_1523(Z)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_4071;method_18640()Z"
      )}
   )
   private boolean larpclient$wrapOperationRender(Overlay var1, Operation<Boolean> var2) {
      return (Boolean)var2.call(new Object[]{var1}) && !AutoTunnel.IAsBoolean();
   }

   @WrapOperation(
      method = {"method_1590(Z)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_636;method_2925()V"
      )}
   )
   private void larpclient$wrapOperationHandleBlockBreaking(ClientPlayerInteractionManager var1, Operation<Void> var2) {
      if (!MiningProgress.ZAsBoolean()) {
         var2.call(new Object[]{var1});
      }
   }
}
