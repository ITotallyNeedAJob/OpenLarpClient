package com.larpclient.mixin.feature.render;

import com.larpclient.SnowSubdivision;
import com.larpclient.TerrainMaterialClassifier;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.SectionBuilder;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({SectionBuilder.class})
public abstract class SectionBuilderMixin {
   public SectionBuilderMixin() {
   }

   @WrapOperation(
      method = {"method_60904(Lnet/minecraft/class_4076;Lnet/minecraft/class_853;Lnet/minecraft/class_8251;Lnet/minecraft/class_750;)Lnet/minecraft/class_9810$class_9811;"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_776;method_3355(Lnet/minecraft/class_2680;Lnet/minecraft/class_2338;Lnet/minecraft/class_1920;Lnet/minecraft/class_4587;Lnet/minecraft/class_4588;ZLjava/util/List;)V"
      )}
   )
   private void larpclient$wrapOperationHandler(
      BlockRenderManager var1,
      BlockState var2,
      BlockPos var3,
      BlockRenderView var4,
      MatrixStack var5,
      VertexConsumer var6,
      boolean var7,
      List<BlockModelPart> var8,
      Operation<Void> var9
   ) {
      TerrainMaterialClassifier.QAsVoid(var2, (float)(var3.getX() & 15), (float)(var3.getY() & 15), (float)(var3.getZ() & 15), var4, var3);

      try {
         var9.call(new Object[]{var1, var2, var3, var4, var5, SnowSubdivision.zAsVertexConsumer(var6), var7, var8});
      } finally {
         TerrainMaterialClassifier.zAsVoid();
      }
   }
}
