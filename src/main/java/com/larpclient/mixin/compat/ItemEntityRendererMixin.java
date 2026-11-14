package com.larpclient.mixin.compat;

import com.larpclient.FakeSpawnerController;
import com.larpclient.ItemBlockCache;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.state.ItemEntityRenderState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemEntityRenderer.class})
public abstract class ItemEntityRendererMixin {
   public ItemEntityRendererMixin() {
   }

   @Redirect(
      method = {"method_62470(Lnet/minecraft/class_1542;Lnet/minecraft/class_10039;F)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1542;method_6983()Lnet/minecraft/class_1799;"
      )}
   )
   private ItemStack larpclient$redirectFV(ItemEntity var1) {
      FakeSpawnerController var2 = FakeSpawnerController.ZAsMa();
      r2.tAsVoid(var1);
      if (!var2.XAsBoolean()) {
         return var1.getStack();
      } else {
         return var2.FAsBoolean(var1) ? ItemStack.EMPTY : var2.XAsItemStack(var1.getStack());
      }
   }

   @Inject(
      method = {"method_62470(Lnet/minecraft/class_1542;Lnet/minecraft/class_10039;F)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectFV(ItemEntity var1, ItemEntityRenderState var2, float var3, CallbackInfo var4) {
      FakeSpawnerController var5 = FakeSpawnerController.ZAsMa();
      if (var5.XAsBoolean() && var5.hAsBoolean(var1)) {
         var2.invisible = true;
         var2.itemRenderState.clear();
         var2.renderedAmount = 0;
      }
   }
}
