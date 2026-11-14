package com.larpclient.mixin;

import com.larpclient.LightmapTintEffect;
import com.larpclient.module.WorldColor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LightmapTextureManager.class})
public abstract class LightmapTextureManagerMixin {
   @Shadow
   private boolean field_4135;
   @Unique
   private int PriceDetector = -1;
   @Unique
   private float Y;
   @Unique
   private Object v;

   public LightmapTextureManagerMixin() {
   }

   @Inject(
      method = {"method_3313(F)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectUpdate(float var1, CallbackInfo var2) {
      WorldColor var3 = WorldColor.FAsWorldColor();
      boolean var4 = var3 != null && var3.isRuntimeActive();
      int var5 = var4 ? var3.ambientColor.getValue() : -1;
      float var6 = var4 ? var3.ambientStrength.getValue().floatValue() : 0.0F;
      ClientWorld var7 = MinecraftClient.getInstance().world;
      if (var7 != this.v || var5 != this.m || var6 != this.Y) {
         this.field_4135 = true;
         this.v = var7;
         this.m = var5;
         this.Y = var6;
      }
   }

   @WrapOperation(
      method = {"method_3313(F)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V"
      )}
   )
   private void larpclient$wrapOperationUpdate(RenderPass var1, RenderPipeline var2, Operation<Void> var3) {
      RenderPipeline var4 = LightmapTintEffect.HAsRenderPipeline(var2, this.m, this.Y);
      var3.call(new Object[]{var1, var4});
      LightmapTintEffect.LAsVoid(var1, var4);
   }
}
