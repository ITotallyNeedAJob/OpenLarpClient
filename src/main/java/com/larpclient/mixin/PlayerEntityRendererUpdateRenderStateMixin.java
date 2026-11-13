package com.larpclient.mixin;

import com.larpclient.module.PlayerModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerEntityRenderer.class})
public class PlayerEntityRendererUpdateRenderStateMixin {
   public PlayerEntityRendererUpdateRenderStateMixin() {
   }

   @Inject(
      method = {"method_62604(Lnet/minecraft/class_11890;Lnet/minecraft/class_10055;F)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectFV(PlayerLikeEntity var1, PlayerEntityRenderState var2, float var3, CallbackInfo var4) {
      PlayerModel var5 = PlayerModel.hAsPlayerModel();
      if (var5 != null && var1 == MinecraftClient.getInstance().player) {
         var5.LAsVoid(null);
         if (var5.zAsBoolean()) {
            var2.invisible = true;
            var2.invisibleToPlayer = true;
            var2.equippedHeadStack = var2.equippedLegsStack = var2.equippedFeetStack = ItemStack.EMPTY;
            if (!var2.equippedChestStack.contains(DataComponentTypes.GLIDER)) {
               var2.equippedChestStack = ItemStack.EMPTY;
            }

            var2.leftShoulderParrotVariant = var2.rightShoulderParrotVariant = null;
            var5.LAsVoid(var2);
         }
      }
   }
}
