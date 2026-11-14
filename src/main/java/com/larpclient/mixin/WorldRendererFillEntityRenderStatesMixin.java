package com.larpclient.mixin;

import com.larpclient.ParticleEmitter;
import com.larpclient.module.Freecam;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({WorldRenderer.class})
public abstract class WorldRendererFillEntityRenderStatesMixin {
   @Unique
   private boolean H;

   public WorldRendererFillEntityRenderStatesMixin() {
   }

   @WrapOperation(
      method = {"method_72917(Lnet/minecraft/class_4184;Lnet/minecraft/class_4604;Lnet/minecraft/class_9779;Lnet/minecraft/class_11658;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_898;method_3950(Lnet/minecraft/class_1297;Lnet/minecraft/class_4604;DDD)Z"
      )}
   )
   private boolean larpclient$wrapOperationV(
      EntityRenderManager var1, Entity var2, Frustum var3, double var4, double var6, double var8, Operation<Boolean> var10
   ) {
      return (!ParticleEmitter.oAsBoolean() || Freecam.bAsBoolean() || var2 != MinecraftClient.getInstance().player)
         && (Boolean)var10.call(new Object[]{var1, var2, var3, var4, var6, var8});
   }

   @Redirect(
      method = {"method_72917(Lnet/minecraft/class_4184;Lnet/minecraft/class_4604;Lnet/minecraft/class_9779;Lnet/minecraft/class_11658;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_761;method_40050(Lnet/minecraft/class_2338;)Z"
      )}
   )
   private boolean larpclient$redirectV(WorldRenderer var1, BlockPos var2) {
      return ParticleEmitter.oAsBoolean() || var1.isRenderingReady(var2);
   }

   @WrapOperation(
      method = {"method_22710(Lnet/minecraft/class_9922;Lnet/minecraft/class_9779;ZLnet/minecraft/class_4184;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_761;method_74752(Lnet/minecraft/class_4184;Lnet/minecraft/class_4604;Z)V"
      )}
   )
   private void larpclient$wrapOperationZV(WorldRenderer var1, Camera var2, Frustum var3, boolean var4, Operation<Void> var5) {
      boolean var6 = ParticleEmitter.oAsBoolean();
      if (var6 != this.H) {
         var1.scheduleTerrainUpdate();
      }

      this.H = var6;
      if (!var6) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else {
         MinecraftClient var7 = MinecraftClient.getInstance();
         boolean var8 = var7.chunkCullingEnabled;

         try {
            var7.chunkCullingEnabled = false;
            var5.call(new Object[]{var1, var2, var3, var4});
         } finally {
            var7.chunkCullingEnabled = var8;
         }
      }
   }
}
