package com.larpclient.mixin.compat;

import com.larpclient.FakeSpawnerController;
import net.minecraft.client.render.entity.state.ItemStackEntityRenderState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({ItemStackEntityRenderState.class})
public abstract class ItemStackEntityRenderStateMixin {
   public ItemStackEntityRenderStateMixin() {
   }

   @ModifyVariable(
      method = {"method_65581(Lnet/minecraft/class_1297;Lnet/minecraft/class_1799;Lnet/minecraft/class_10442;)V"},
      at = {@At("HEAD")},
      argsOnly = true,
      ordinal = 0
   )
   private ItemStack larpclient$modifyVariableV(ItemStack var1) {
      return FakeSpawnerController.ZAsMa().XAsItemStack(var1);
   }
}
