package com.larpclient.mixin.feature.render;

import com.larpclient.SodiumTerrainMaterialBridge;
import com.larpclient.TerrainMaterialClassifier;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(
   targets = {"net/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer"},
   remap = false
)
public abstract class BlockRendererMixin {
   @Unique
   private BlockView h;

   public BlockRendererMixin() {
   }

   @Inject(
      method = {"prepare"},
      at = {@At("HEAD")},
      require = 0,
      remap = false
   )
   private void larpclient$injectPrepare(@Coerce Object var1, @Coerce Object var2, @Coerce Object var3, CallbackInfo var4) {
      this.h = var2 instanceof BlockView var5 ? var5 : null;
   }

   @Inject(
      method = {"release"},
      at = {@At("HEAD")},
      require = 0,
      remap = false
   )
   private void larpclient$injectRelease(CallbackInfo var1) {
      this.h = null;
      TerrainMaterialClassifier.aAsVoid();
   }

   @Inject(
      method = {"renderModel"},
      at = {@At("HEAD")},
      require = 0,
      remap = false
   )
   private void larpclient$injectRenderModel(@Coerce Object var1, BlockState var2, BlockPos var3, BlockPos var4, CallbackInfo var5) {
      TerrainMaterialClassifier.NAsVoid(var2, var4, this.h, var3);
   }

   @Inject(
      method = {"renderModel"},
      at = {@At("RETURN")},
      require = 0,
      remap = false
   )
   private void larpclient$injectRenderModel2(@Coerce Object var1, BlockState var2, BlockPos var3, BlockPos var4, CallbackInfo var5) {
      TerrainMaterialClassifier.aAsVoid();
   }

   @Inject(
      method = {"bufferQuad"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/builder/ChunkMeshBufferBuilder;push([Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkVertexEncoder$Vertex;I)V"
      )},
      require = 0,
      remap = false
   )
   private void larpclient$injectBufferQuad(@Coerce Object var1, float[] var2, @Coerce Object var3, CallbackInfo var4) {
      SodiumTerrainMaterialBridge.uAsVoid(this);
   }
}
