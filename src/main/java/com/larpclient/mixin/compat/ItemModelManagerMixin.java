package com.larpclient.mixin.compat;

import com.larpclient.AuctionMenuText;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ItemModelManager.class})
public abstract class ItemModelManagerMixin {
   public ItemModelManagerMixin() {
   }

   @Redirect(
      method = {"method_65596(Lnet/minecraft/class_10444;Lnet/minecraft/class_1799;Lnet/minecraft/class_811;Lnet/minecraft/class_1937;Lnet/minecraft/class_11566;I)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1799;method_58694(Lnet/minecraft/class_9331;)Ljava/lang/Object;"
      )}
   )
   private Object larpclient$redirectIV(ItemStack var1, ComponentType<?> var2) {
      return var2 != DataComponentTypes.ITEM_MODEL ? var1.get(var2) : AuctionMenuText.NAsItemStack(var1).get(DataComponentTypes.ITEM_MODEL);
   }

   @ModifyVariable(
      method = {"method_65598(Lnet/minecraft/class_10444;Lnet/minecraft/class_1799;Lnet/minecraft/class_811;Lnet/minecraft/class_1937;Lnet/minecraft/class_11566;I)V"},
      at = {@At("HEAD")},
      argsOnly = true,
      ordinal = 0
   )
   private ItemStack larpclient$modifyVariableIV(ItemStack var1) {
      return AuctionMenuText.NAsItemStack(var1);
   }
}
