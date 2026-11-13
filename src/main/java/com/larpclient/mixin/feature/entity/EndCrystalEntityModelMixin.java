package com.larpclient.mixin.feature.entity;

import com.larpclient.CrystalChams;
import net.minecraft.client.render.entity.model.EndCrystalEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({EndCrystalEntityModel.class})
public class EndCrystalEntityModelMixin {
   public EndCrystalEntityModelMixin() {
   }

   @ModifyArg(
      method = {"method_62083(Lnet/minecraft/class_10014;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_7833;rotationDegrees(F)Lorg/joml/Quaternionf;"
      )}
   )
   private float larpclient$modifyArgV(float var1) {
      CrystalChams var2 = CrystalChams.hAsCrystalChams();
      return var1 * (var2 == null ? 1.0F : var2.xAsFloat());
   }

   @ModifyArg(
      method = {"method_62083(Lnet/minecraft/class_10014;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lorg/joml/Quaternionf;rotateY(F)Lorg/joml/Quaternionf;"
      )}
   )
   private float larpclient$modifyArgV2(float var1) {
      CrystalChams var2 = CrystalChams.hAsCrystalChams();
      return var1 * (var2 == null ? 1.0F : var2.xAsFloat());
   }
}
