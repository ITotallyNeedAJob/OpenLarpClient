package com.larpclient.mixin.compat;

import com.larpclient.FakeSpawnerController;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HeldItemRenderer.class})
public abstract class HeldItemRendererMixin {
   @Shadow
   private ItemStack field_4047;
   @Shadow
   private ItemStack field_4048;

   public HeldItemRendererMixin() {
   }

   @Inject(
      method = {"method_3215(Lnet/minecraft/class_1268;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(Hand var1, CallbackInfo var2) {
      if (FakeSpawnerController.ZAsMa().GAsBoolean(var1)) {
         ClientPlayerEntity var3 = MinecraftClient.getInstance().player;
         if (var1 == Hand.MAIN_HAND) {
            this.field_4047 = var3.getMainHandStack();
         } else {
            this.field_4048 = var3.getOffHandStack();
         }

         var2.cancel();
      }
   }

   @Inject(
      method = {"method_3220()V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectUpdateHeldItems(CallbackInfo var1) {
      FakeSpawnerController var2 = FakeSpawnerController.ZAsMa();
      ClientPlayerEntity var3 = MinecraftClient.getInstance().player;
      if (var2.GAsBoolean(Hand.MAIN_HAND)) {
         this.field_4047 = var3.getMainHandStack();
      }

      if (var2.GAsBoolean(Hand.OFF_HAND)) {
         this.field_4048 = var3.getOffHandStack();
      }
   }
}
