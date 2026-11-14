package com.larpclient.mixin;

import com.larpclient.ParticleConfig;
import com.larpclient.XOverlay;
import com.larpclient.module.ItemPhysic;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.state.ItemEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemEntityRenderer.class})
public abstract class ItemEntityRendererMixin {
   public ItemEntityRendererMixin() {
   }

   @Inject(
      method = {"method_62470(Lnet/minecraft/class_1542;Lnet/minecraft/class_10039;F)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectFV(ItemEntity var1, ItemEntityRenderState var2, float var3, CallbackInfo var4) {
      ((p1)var2).larp$setGrounded(var1.isOnGround());
      ((p1)var2).larp$setFloating(!var1.isOnGround() && var1.isTouchingWater());
   }

   @Inject(
      method = {"method_3996(Lnet/minecraft/class_10039;Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;Lnet/minecraft/class_12075;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_916;method_72986(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_10428;Lnet/minecraft/class_5819;Lnet/minecraft/class_238;)V"
      )}
   )
   private void larpclient$injectV(ItemEntityRenderState var1, MatrixStack var2, OrderedRenderCommandQueue var3, CameraRenderState var4, CallbackInfo var5) {
      ItemPhysic var6 = ItemPhysic.lAsItemPhysic();
      if (var6 != null && var6.isRuntimeActive()) {
         Box var7 = var1.itemRenderState.getModelBoundingBox();
         float var8 = (float)Math.sin((double)(var1.age / 10.0F + var1.uniqueOffset)) * 0.1F + 0.1F;
         float var9 = -((float)var7.minY) + 0.0625F;
         float var10 = ItemEntity.getRotation(var1.age, var1.uniqueOffset);
         var2.multiply(RotationAxis.POSITIVE_Y.rotation(-var10));
         var2.translate(0.0F, -var8 - var9, 0.0F);
         p1 var11 = (p1)var1;
         xo.Type_r var12 = xo.tAsXo_Type_r(
            var11.larp$isGrounded(), var11.larp$isFloating(), var6.wAsBoolean(), (double)var1.age, (double)var1.uniqueOffset, var7.minY, var7.minZ, var7.maxZ
         );
         var2.translate(0.0, var12.height(), 0.0);
         if (var12.billboard()) {
            var2.multiply(var4.orientation);
            var2.scale(1.1F, 1.1F, 1.0F);
         } else {
            var2.multiply(RotationAxis.POSITIVE_Y.rotation((float)var12.yaw()));
            var2.multiply(RotationAxis.POSITIVE_X.rotation((float)var12.pitch()));
            var2.multiply(RotationAxis.POSITIVE_Z.rotation((float)var12.roll()));
         }
      }
   }

   @Mixin({ItemEntityRenderState.class})
   public abstract static class ItemEntityRenderStateMixin implements p1 {
      @Unique
      private boolean b;
      @Unique
      private boolean l;

      public ItemEntityRenderStateMixin() {
      }

      @Override
      public void larp$setGrounded(boolean var1) {
         this.b = var1;
      }

      @Override
      public boolean larp$isGrounded() {
         return this.b;
      }

      @Override
      public void larp$setFloating(boolean var1) {
         this.l = var1;
      }

      @Override
      public boolean larp$isFloating() {
         return this.l;
      }
   }
}
