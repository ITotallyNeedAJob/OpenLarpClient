package com.larpclient.mixin.feature.render;

import com.larpclient.TerrainMaterialClassifier;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({FluidRenderer.class})
public abstract class FluidRendererMixin {
   public FluidRendererMixin() {
   }

   @WrapMethod(
      method = {"method_3347(Lnet/minecraft/class_1920;Lnet/minecraft/class_2338;Lnet/minecraft/class_4588;Lnet/minecraft/class_2680;Lnet/minecraft/class_3610;)V"}
   )
   private void ChinaHatCodec(BlockRenderView var1, BlockPos var2, VertexConsumer var3, BlockState var4, FluidState var5, Operation<Void> var6) {
      TerrainMaterialClassifier.QAsVoid(var1, var2, var5);

      try {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
      } finally {
         TerrainMaterialClassifier.gAsVoid();
      }
   }

   @ModifyArg(
      method = {"method_23072(Lnet/minecraft/class_4588;FFFFFFFFI)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_4588;method_22915(FFFF)Lnet/minecraft/class_4588;"
      )},
      index = 3
   )
   private float larpclient$modifyArgFFFFFFFFIV(float var1) {
      return (float)(TerrainMaterialClassifier.iAsInt(-16777216) >>> 24) / 255.0F;
   }
}
