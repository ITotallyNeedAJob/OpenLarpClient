package com.larpclient.mixin;

import com.larpclient.module.GUIAnimation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud.Backend;
import net.minecraft.client.gui.hud.ChatHudLine.Visible;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(
   targets = {"net/minecraft/class_338$1"}
)
public class ChatHudTextConsumerMixin {
   public ChatHudTextConsumerMixin() {
   }

   @WrapOperation(
      method = {"accept"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_338$class_12233;method_75807(IFLnet/minecraft/class_5481;)Z"
      )}
   )
   private boolean larpclient$wrapOperationAccept(
      Backend var1, int var2, float var3, OrderedText var4, Operation<Boolean> var5, @Local(argsOnly = true) Visible var6
   ) {
      GUIAnimation var7 = GUIAnimation.kAsGUIAnimation();
      MinecraftClient var8 = MinecraftClient.getInstance();
      if (var7 != null && var7.HAsBoolean() && !(var8.currentScreen instanceof ChatScreen)) {
         double var9 = Math.clamp(
            (double)((float)(var8.inGameHud.getTicks() - var6.addedTime()) + var8.getRenderTickCounter().getTickProgress(false)) / 9.0, 0.0, 1.0
         );
         float var11 = (float)(1.0 - Math.pow(1.0 - var9, 3.0));
         float var12 = -8.0F * (1.0F - var11);
         var1.updatePose(var1x -> var1x.translate(var12, 0.0F));

         boolean var13;
         try {
            var13 = (Boolean)var5.call(new Object[]{var1, var2, var3 * var11, var4});
         } finally {
            var1.updatePose(var1x -> var1x.translate(-var12, 0.0F));
         }

         return var13;
      } else {
         return (Boolean)var5.call(new Object[]{var1, var2, var3, var4});
      }
   }
}
