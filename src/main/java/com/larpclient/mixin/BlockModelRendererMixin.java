package com.larpclient.mixin;

import com.larpclient.module.XRay;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockModelRenderer.LightmapCache;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BlockModelRenderer.class})
public abstract class BlockModelRendererMixin {
   private static final String T = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer;renderModelFaceFlat(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;IIZLcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Ljava/util/List;Lnet/minecraft/client/renderer/block/ModelBlockRenderer$CommonRenderStorage;)V";

   public BlockModelRendererMixin() {
   }

   @Inject(
      method = {"method_3361(Lnet/minecraft/class_1920;Ljava/util/List;Lnet/minecraft/class_2680;Lnet/minecraft/class_2338;Lnet/minecraft/class_4587;Lnet/minecraft/class_4588;ZI)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectZIV(
      BlockRenderView var1,
      List<BlockModelPart> var2,
      BlockState var3,
      BlockPos var4,
      MatrixStack var5,
      VertexConsumer var6,
      boolean var7,
      int var8,
      CallbackInfo var9
   ) {
      if (XRay.aAsBoolean() && XRay.ZAsBoolean(var3.getBlock())) {
         ((BlockModelRenderer)(Object)this).renderFlat(var1, var2, var3, var4, var5, var6, var7, var8);
         var9.cancel();
      }
   }

   @WrapOperation(
      method = {"method_3373(Lnet/minecraft/class_1920;Ljava/util/List;Lnet/minecraft/class_2680;Lnet/minecraft/class_2338;Lnet/minecraft/class_4587;Lnet/minecraft/class_4588;ZI)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_778;method_3370(Lnet/minecraft/class_1920;Lnet/minecraft/class_2680;Lnet/minecraft/class_2338;IIZLnet/minecraft/class_4587;Lnet/minecraft/class_4588;Ljava/util/List;Lnet/minecraft/class_778$class_10931;)V"
      )}
   )
   private void larpclient$wrapOperationZIV(
      BlockModelRenderer var1,
      BlockRenderView var2,
      BlockState var3,
      BlockPos var4,
      int var5,
      int var6,
      boolean var7,
      MatrixStack var8,
      VertexConsumer var9,
      List<BakedQuad> var10,
      LightmapCache var11,
      Operation<Void> var12
   ) {
      boolean var13 = XRay.aAsBoolean() && XRay.ZAsBoolean(var3.getBlock());
      var12.call(new Object[]{var1, var2, var3, var4, var13 ? 15728880 : var5, var6, var7 && !var13, var8, var9, var10, var11});
   }
}
