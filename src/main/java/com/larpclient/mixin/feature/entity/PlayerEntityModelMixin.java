package com.larpclient.mixin.feature.entity;

import com.larpclient.PlayerJointTransforms;
import com.larpclient.WalkStyle;
import com.larpclient.ArmReach;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerEntityModel.class})
public abstract class PlayerEntityModelMixin implements b2 {
   @Unique
   private PlayerJointTransforms l;

   public PlayerEntityModelMixin() {
   }

   @Unique
   @Override
   public void larpRestoreWalkRig() {
      if (this.l != null) {
         this.l.SAsVoid();
      }
   }

   @Unique
   @Override
   public void larpApplyJoints(float[] var1, PlayerEntityRenderState var2) {
      if (this.l == null) {
         this.l = new PlayerJointTransforms((PlayerEntityModel)(Object)this);
      }

      this.l.fAsVoid(var1, var2);
   }

   @Inject(
      method = {"method_62110(Lnet/minecraft/class_10055;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV(PlayerEntityRenderState var1, CallbackInfo var2) {
      if (this.l != null) {
         this.l.SAsVoid();
      }
   }

   @Inject(
      method = {"method_72859(Lnet/minecraft/class_10055;Lnet/minecraft/class_1306;Lnet/minecraft/class_4587;)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectV(PlayerEntityRenderState var1, Arm var2, MatrixStack var3, CallbackInfo var4) {
      if (this.l != null) {
         this.l.dAsVoid(var2, var3);
      }
   }

   @Inject(
      method = {"method_62110(Lnet/minecraft/class_10055;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV2(PlayerEntityRenderState var1, CallbackInfo var2) {
      WalkStyle var3 = WalkStyle.xAsWalkStyle();
      if (var3 != null) {
         PlayerEntityModel var4 = (PlayerEntityModel)(Object)this;
         float[] var5 = var3.yAsFloatArray(var4, var1);
         if (var5 != null) {
            if (this.l == null) {
               this.l = new PlayerJointTransforms(var4);
            }

            this.l.fAsVoid(var5, var1);
         }
      }
   }
}
