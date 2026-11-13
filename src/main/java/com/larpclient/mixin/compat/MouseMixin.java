package com.larpclient.mixin.compat;

import com.larpclient.VisualInventory;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({Mouse.class})
public abstract class MouseMixin {
   public MouseMixin() {
   }

   @WrapOperation(
      method = {"method_1598(JDD)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1661;method_61496(I)V"
      )}
   )
   private void larpclient$wrapOperationOnMouseScroll(PlayerInventory var1, int var2, Operation<Void> var3) {
      if (VisualInventory.jAsBoolean()) {
         VisualInventory.aAsVoid(var2);
      } else {
         var3.call(new Object[]{var1, var2});
      }
   }

   @WrapOperation(
      method = {"method_1598(JDD)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1661;method_67532()I"
      )}
   )
   private int larpclient$wrapOperationOnMouseScroll(PlayerInventory var1, Operation<Integer> var2) {
      return VisualInventory.nAsInt((Integer)var2.call(new Object[]{var1}));
   }
}
