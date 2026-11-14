package com.larpclient.mixin;

import com.larpclient.ArmReach;
import com.larpclient.OptionPanel;
import com.larpclient.RefractionGeometry;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BipedEntityModel.class})
public abstract class BipedEntityModelMixin {
   public BipedEntityModelMixin() {
   }

   @Inject(
      method = {"method_17087(Lnet/minecraft/class_10034;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV(BipedEntityRenderState var1, CallbackInfo var2) {
      if (var1 instanceof OptionPanel var3) {
         float var4 = var3.larp$emoteWeight();
         if (!(var4 <= 0.0F)) {
            RefractionGeometry.iAsVoid((BipedEntityModel<?>)(Object)this, var3.larp$emoteSample(), var4);
            if ((Object)this instanceof PlayerEntityModel && var1 instanceof PlayerEntityRenderState var5) {
               float[] var6 = RefractionGeometry.AAsFloatArray(var3.larp$emoteSample(), var4);
               if (var6 != null) {
                  ((ArmReach)(Object)this).larpApplyJoints(var6, var5);
               }
            }
         }
      }
   }
}
