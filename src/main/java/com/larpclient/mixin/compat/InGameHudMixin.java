package com.larpclient.mixin.compat;

import com.larpclient.VisualInventory;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({InGameHud.class})
public abstract class InGameHudMixin {
   public InGameHudMixin() {
   }

   @WrapOperation(
      method = {"method_1759(Lnet/minecraft/class_332;Lnet/minecraft/class_9779;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1661;method_5438(I)Lnet/minecraft/class_1799;"
      )}
   )
   private ItemStack larpclient$wrapOperationV(PlayerInventory var1, int var2, Operation<ItemStack> var3) {
      int var4 = VisualInventory.jAsBoolean() ? VisualInventory.rAsInt(var2) : var2;
      return var4 < 0 ? ItemStack.EMPTY : VisualInventory.pAsItemStack(var4, (ItemStack)var3.call(new Object[]{var1, var4}));
   }

   @WrapOperation(
      method = {"method_1759(Lnet/minecraft/class_332;Lnet/minecraft/class_9779;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1661;method_67532()I"
      )}
   )
   private int larpclient$wrapOperationV(PlayerInventory var1, Operation<Integer> var2) {
      return VisualInventory.nAsInt((Integer)var2.call(new Object[]{var1}));
   }
}
