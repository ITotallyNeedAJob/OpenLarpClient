package com.larpclient.mixin;

import com.larpclient.module.PlayerModel;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({HeldItemFeatureRenderer.class})
public class HeldItemFeatureRendererMixin {
   public HeldItemFeatureRendererMixin() {
   }

   @Redirect(
      method = {"method_4192(Lnet/minecraft/class_10426;Lnet/minecraft/class_10444;Lnet/minecraft/class_1799;Lnet/minecraft/class_1306;Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;I)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_3881;method_2803(Lnet/minecraft/class_10017;Lnet/minecraft/class_1306;Lnet/minecraft/class_4587;)V"
      )}
   )
   private void larpclient$redirectIV(ModelWithArms<EntityRenderState> var1, EntityRenderState var2, Arm var3, MatrixStack var4) {
      PlayerModel var5 = PlayerModel.hAsPlayerModel();
      if (var5 == null || !var5.vAsBoolean(var2, var3, var4)) {
         var1.setArmAngle(var2, var3, var4);
      }
   }
}
