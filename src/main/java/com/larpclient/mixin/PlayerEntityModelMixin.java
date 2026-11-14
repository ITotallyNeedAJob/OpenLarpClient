package com.larpclient.mixin;

import com.larpclient.MediaControls;
import com.larpclient.MotionBlur;
import com.larpclient.XKeybind;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerEntityModel.class})
public abstract class PlayerEntityModelMixin implements XKeybind {
   private static final String E = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V";
   @Unique
   private boolean r;
   @Unique
   private boolean w;

   public PlayerEntityModelMixin() {
   }

   @Override
   public void larp$markSkin() {
      this.r = true;
   }

   @Inject(
      method = {"method_62110(Lnet/minecraft/class_10055;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV(PlayerEntityRenderState var1, CallbackInfo var2) {
      if (this.w) {
         ((PlayerEntityModel)(Object)this).head.visible = true;
         this.w = false;
      }
   }

   @Inject(
      method = {"method_62110(Lnet/minecraft/class_10055;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV2(PlayerEntityRenderState var1, CallbackInfo var2) {
      MediaControls var3 = (MediaControls)var1;
      if (MotionBlur.gAsBoolean((PlayerEntityModel)(Object)this, this.r, var3.larp$headless(), var3.larp$korblox())) {
         this.w = true;
      }
   }
}
