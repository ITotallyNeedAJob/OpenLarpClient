package com.larpclient.mixin;

import com.larpclient.BladeGeometry;
import com.larpclient.CosmeticSubmissionHandler;
import com.larpclient.WeaponTrailRenderer;
import com.larpclient.LayerIndex;
import com.larpclient.module.Animations;
import com.larpclient.module.ViewModel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.SwingAnimationType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HeldItemRenderer.class})
public class HeldItemRendererRenderItemMixin {
   @Unique
   private float I;
   @Unique
   private float J;
   @Unique
   private Hand S = Hand.MAIN_HAND;

   public HeldItemRendererRenderItemMixin() {
   }

   @Inject(
      method = {"method_3233(Lnet/minecraft/class_1309;Lnet/minecraft/class_1799;Lnet/minecraft/class_811;Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;I)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectIV(
      LivingEntity var1, ItemStack var2, ItemDisplayContext var3, MatrixStack var4, OrderedRenderCommandQueue var5, int var6, CallbackInfo var7
   ) {
      var4.push();
      if (var3.isFirstPerson() && var1 == MinecraftClient.getInstance().player) {
         ViewModel var8 = ViewModel.qAsViewModel();
         if (var8 != null && var8.isRuntimeActive()) {
            var8.yAsVoid(var4, this.S, var3 == ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
         }

         Animations var9 = Animations.oAsAnimations();
         if (var9 != null && !var2.isEmpty() && !var2.contains(DataComponentTypes.MAP_ID)) {
            var9.xAsVoid(var4);
         }
      }
   }

   @Inject(
      method = {"method_3233(Lnet/minecraft/class_1309;Lnet/minecraft/class_1799;Lnet/minecraft/class_811;Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;I)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectIV2(
      LivingEntity var1, ItemStack var2, ItemDisplayContext var3, MatrixStack var4, OrderedRenderCommandQueue var5, int var6, CallbackInfo var7
   ) {
      var4.pop();
   }

   @Redirect(
      method = {"method_3233(Lnet/minecraft/class_1309;Lnet/minecraft/class_1799;Lnet/minecraft/class_811;Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;I)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_10444;method_65604(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;III)V"
      )}
   )
   private void larpclient$redirectIV(
      ItemRenderState var1,
      MatrixStack var2,
      OrderedRenderCommandQueue var3,
      int var4,
      int var5,
      int var6,
      LivingEntity var7,
      ItemStack var8,
      ItemDisplayContext var9,
      MatrixStack var10,
      OrderedRenderCommandQueue var11,
      int var12
   ) {
      OrderedRenderCommandQueue var13 = var9.isFirstPerson() && var7 == MinecraftClient.getInstance().player
         ? CosmeticSubmissionHandler.sAsOrderedRenderCommandQueue(var3)
         : var3;
      if (var9.isFirstPerson() && var7 == MinecraftClient.getInstance().player) {
         li.AAsVoid(this.S, var9 == ItemDisplayContext.FIRST_PERSON_LEFT_HAND, var2, var1);
      }

      if (var9.isFirstPerson() && var7 == MinecraftClient.getInstance().player) {
         WeaponTrailRenderer.oAsVoid(var1, var2, var13, var3, var8, var9, this.S, this.I, this.J, var4, var5, var6);
      } else {
         var1.render(var2, var13, var4, var5, var6);
      }
   }

   @Shadow
   private void method_65816(float var1, MatrixStack var2, int var3, Arm var4) {
      throw new AssertionError("Mixin shadow");
   }

   @Shadow
   private void method_3224(MatrixStack var1, Arm var2, float var3) {
      throw new AssertionError("Mixin shadow");
   }

   @Redirect(
      method = {"method_3228(Lnet/minecraft/class_742;FFLnet/minecraft/class_1268;FLnet/minecraft/class_1799;FLnet/minecraft/class_4587;Lnet/minecraft/class_11659;I)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_759;method_65816(FLnet/minecraft/class_4587;ILnet/minecraft/class_1306;)V",
         ordinal = 2
      )},
      require = 1,
      allow = 1
   )
   private void larpclient$redirectIV(HeldItemRenderer var1, float var2, MatrixStack var3, int var4, Arm var5) {
      Animations var6 = Animations.oAsAnimations();
      if (var6 != null && var6.KAsBoolean(var5)) {
         var6.sAsVoid(var3, var5, var2);
      } else {
         this.method_65816(var2, var3, var4, var5);
      }
   }

   @Redirect(
      method = {"method_3228(Lnet/minecraft/class_742;FFLnet/minecraft/class_1268;FLnet/minecraft/class_1799;FLnet/minecraft/class_4587;Lnet/minecraft/class_11659;I)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_759;method_3224(Lnet/minecraft/class_4587;Lnet/minecraft/class_1306;F)V",
         ordinal = 4
      )},
      require = 1,
      allow = 1
   )
   private void larpclient$redirectIV(
      HeldItemRenderer var1,
      MatrixStack var2,
      Arm var3,
      float var4,
      AbstractClientPlayerEntity var5,
      float var6,
      float var7,
      Hand var8,
      float var9,
      ItemStack var10,
      float var11,
      MatrixStack var12,
      OrderedRenderCommandQueue var13,
      int var14
   ) {
      Animations var15 = Animations.oAsAnimations();
      boolean var16 = var15 != null && var15.KAsBoolean(var3) && var10.getSwingAnimation().type() == SwingAnimationType.WHACK;
      if (!var16) {
         this.method_3224(var2, var3, var4);
      }
   }

   @Inject(
      method = {"method_3228(Lnet/minecraft/class_742;FFLnet/minecraft/class_1268;FLnet/minecraft/class_1799;FLnet/minecraft/class_4587;Lnet/minecraft/class_11659;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectIV(
      AbstractClientPlayerEntity var1,
      float var2,
      float var3,
      Hand var4,
      float var5,
      ItemStack var6,
      float var7,
      MatrixStack var8,
      OrderedRenderCommandQueue var9,
      int var10,
      CallbackInfo var11
   ) {
      this.S = var4;
      this.I = var5;
      this.J = var7;
      if (BladeGeometry.KAsBoolean(var1, var4, var6, var5, var7, var2, var8, var9, var10)) {
         var11.cancel();
      } else {
         var8.push();
         ViewModel var12 = ViewModel.qAsViewModel();
         if (var12 != null && var12.isRuntimeActive()) {
            boolean var13 = var4 == Hand.MAIN_HAND ? var1.getMainArm() == Arm.LEFT : var1.getMainArm() != Arm.LEFT;
            var12.GAsVoid(var8, var4, var13);
         }
      }
   }

   @Inject(
      method = {"method_22976(FLnet/minecraft/class_4587;Lnet/minecraft/class_11659;Lnet/minecraft/class_746;I)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectIV(float var1, MatrixStack var2, OrderedRenderCommandQueue var3, ClientPlayerEntity var4, int var5, CallbackInfo var6) {
      li.kAsVoid();
   }

   @WrapOperation(
      method = {"method_3219(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;IFFLnet/minecraft/class_1306;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1007;method_4220(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_2960;Z)V"
      )},
      require = 1,
      allow = 1
   )
   private void larpclient$wrapOperationV(
      PlayerEntityRenderer var1, MatrixStack var2, OrderedRenderCommandQueue var3, int var4, Identifier var5, boolean var6, Operation<Void> var7
   ) {
      Y(var2, false);
      var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      li.WAsVoid(Hand.MAIN_HAND, false, var2, ((PlayerEntityModel)var1.getModel()).rightArm);
   }

   @WrapOperation(
      method = {"method_3219(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;IFFLnet/minecraft/class_1306;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1007;method_4221(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_2960;Z)V"
      )},
      require = 1,
      allow = 1
   )
   private void larpclient$wrapOperationV2(
      PlayerEntityRenderer var1, MatrixStack var2, OrderedRenderCommandQueue var3, int var4, Identifier var5, boolean var6, Operation<Void> var7
   ) {
      Y(var2, true);
      var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      li.WAsVoid(Hand.MAIN_HAND, true, var2, ((PlayerEntityModel)var1.getModel()).leftArm);
   }

   @Unique
   private static void Y(MatrixStack var0, boolean var1) {
      ViewModel var2 = ViewModel.qAsViewModel();
      if (var2 != null && var2.isRuntimeActive()) {
         var2.nAsVoid(var0, Hand.MAIN_HAND, var1);
      }
   }

   @Inject(
      method = {"method_3228(Lnet/minecraft/class_742;FFLnet/minecraft/class_1268;FLnet/minecraft/class_1799;FLnet/minecraft/class_4587;Lnet/minecraft/class_11659;I)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectIV2(
      AbstractClientPlayerEntity var1,
      float var2,
      float var3,
      Hand var4,
      float var5,
      ItemStack var6,
      float var7,
      MatrixStack var8,
      OrderedRenderCommandQueue var9,
      int var10,
      CallbackInfo var11
   ) {
      var8.pop();
   }
}
