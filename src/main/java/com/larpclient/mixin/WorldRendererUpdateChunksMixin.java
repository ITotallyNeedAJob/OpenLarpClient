package com.larpclient.mixin;

import com.larpclient.module.Performance;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.render.ChunkBuilderMode;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({WorldRenderer.class})
public abstract class WorldRendererUpdateChunksMixin {
   public WorldRendererUpdateChunksMixin() {
   }

   @WrapOperation(
      method = {"method_3269(Lnet/minecraft/class_4184;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_7172;method_41753()Ljava/lang/Object;"
      )}
   )
   private Object larpclient$wrapOperationV(SimpleOption<?> var1, Operation<Object> var2) {
      Object var3 = var2.call(new Object[]{var1});
      return var1 == MinecraftClient.getInstance().options.getChunkBuilderMode() ? Performance.tAsChunkBuilderMode((ChunkBuilderMode)var3) : var3;
   }
}
