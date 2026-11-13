package com.larpclient.mixin;

import com.larpclient.FlameParticles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HeldItemRenderer.class})
public final class HeldItemRendererMixin {
   public HeldItemRendererMixin() {
   }

   @Inject(
      method = {"method_3233(Lnet/minecraft/class_1309;Lnet/minecraft/class_1799;Lnet/minecraft/class_811;Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;I)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectIV(
      LivingEntity var1, ItemStack var2, ItemDisplayContext var3, MatrixStack var4, OrderedRenderCommandQueue var5, int var6, CallbackInfo var7
   ) {
      if (var1 == MinecraftClient.getInstance().player && var3.isFirstPerson() && !var2.isEmpty()) {
         FlameParticles.PAsVoid(var3 == ItemDisplayContext.FIRST_PERSON_LEFT_HAND ? Arm.LEFT : Arm.RIGHT, var4, var5, true);
      }
   }

   @Inject(
      method = {"method_3219(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;IFFLnet/minecraft/class_1306;)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectV(MatrixStack var1, OrderedRenderCommandQueue var2, int var3, float var4, float var5, Arm var6, CallbackInfo var7) {
      MinecraftClient var8 = MinecraftClient.getInstance();
      if (var8.player != null) {
         ItemStack var9 = var6 == var8.player.getMainArm() ? var8.player.getMainHandStack() : var8.player.getOffHandStack();
         FlameParticles.PAsVoid(var6, var1, var2, !var9.isEmpty());
      }
   }

   @Inject(
      method = {"method_3216(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_1306;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_4587;method_22909()V",
         shift = Shift.BEFORE
      )},
      require = 1,
      allow = 1
   )
   private void larpclient$injectV(MatrixStack var1, OrderedRenderCommandQueue var2, int var3, Arm var4, CallbackInfo var5) {
      FlameParticles.PAsVoid(var4, var1, var2, true);
   }
}
