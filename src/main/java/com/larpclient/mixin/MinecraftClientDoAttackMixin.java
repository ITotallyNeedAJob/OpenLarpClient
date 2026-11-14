package com.larpclient.mixin;

import com.larpclient.ModuleImportBroker;
import com.larpclient.module.AutoClicker;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({MinecraftClient.class})
public abstract class MinecraftClientDoAttackMixin {
   public MinecraftClientDoAttackMixin() {
   }

   @Inject(
      method = {"method_1536()Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectDoAttack(CallbackInfoReturnable<Boolean> var1) {
      AutoClicker var2 = AutoClicker.GAsAutoClicker();
      if (var2 != null && var2.NAsBoolean() && !var2.gAsBoolean() && !ModuleImportBroker.EAsBoolean()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(
      method = {"method_1583()V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectDoItemUse(CallbackInfo var1) {
      AutoClicker var2 = AutoClicker.GAsAutoClicker();
      if (var2 != null && var2.jAsBoolean() && !var2.gAsBoolean() && !ModuleImportBroker.EAsBoolean()) {
         var1.cancel();
      }
   }

   @WrapMethod(
      method = {"method_1590(Z)V"}
   )
   private void W(boolean var1, Operation<Void> var2) {
      AutoClicker var3 = AutoClicker.GAsAutoClicker();
      if (var3 == null) {
         var2.call(new Object[]{var1});
      } else {
         var3.IAsVoid(var1x -> var2.call(new Object[]{var1x}), var1);
      }
   }

   @WrapOperation(
      method = {"method_1508()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_304;method_1434()Z"
      )}
   )
   private boolean larpclient$wrapOperationHandleInputEvents(KeyBinding var1, Operation<Boolean> var2) {
      AutoClicker var3 = AutoClicker.GAsAutoClicker();
      return (Boolean)var2.call(new Object[]{var1}) || var1 == MinecraftClient.getInstance().options.useKey && var3 != null && var3.RAsBoolean();
   }
}
