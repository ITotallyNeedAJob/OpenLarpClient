package com.larpclient.mixin.compat;

import com.larpclient.FakeSpawnerController;
import com.larpclient.FakeSpawnerTransaction;
import com.larpclient.VisualInventory;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({MinecraftClient.class})
public abstract class MinecraftClientMixin {
   public MinecraftClientMixin() {
   }

   @WrapOperation(
      method = {"method_1508()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_304;method_1436()Z"
      )}
   )
   private boolean larpclient$wrapOperationHandleInputEvents(KeyBinding var1, Operation<Boolean> var2) {
      boolean var3 = (Boolean)var2.call(new Object[]{var1});
      MinecraftClient var4 = MinecraftClient.getInstance();
      if (var3 && VisualInventory.jAsBoolean()) {
         FakeSpawnerController var5 = FakeSpawnerController.ZAsMa();
         boolean var6 = var5.vAsBoolean(var4.player.getInventory().getSelectedSlot());
         if (var1 == var4.options.dropKey && var6) {
            return false;
         } else {
            return var1 == var4.options.swapHandsKey && (var6 || var5.vAsBoolean(40) || FakeSpawnerTransaction.aAsBoolean()) ? false : var3;
         }
      } else {
         return var3;
      }
   }

   @WrapOperation(
      method = {"method_1508()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1661;method_61496(I)V"
      )}
   )
   private void larpclient$wrapOperationHandleInputEvents(PlayerInventory var1, int var2, Operation<Void> var3) {
      if (VisualInventory.jAsBoolean()) {
         VisualInventory.aAsVoid(var2);
      } else {
         var3.call(new Object[]{var1, var2});
      }
   }
}
