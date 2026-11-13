package com.larpclient.mixin.feature.render;

import com.larpclient.SodiumTerrainMaterialBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(
   targets = {"net/caffeinemc/mods/sodium/client/render/chunk/ShaderChunkRenderer"},
   remap = false
)
public abstract class ShaderChunkRendererMixin {
   public ShaderChunkRendererMixin() {
   }

   @Inject(
      method = {"compileProgram"},
      at = {@At("HEAD")},
      cancellable = true,
      require = 0,
      remap = false
   )
   private void larpclient$injectCompileProgram(@Coerce Object var1, CallbackInfoReturnable<Object> var2) {
      Object var3 = SodiumTerrainMaterialBridge.JAsObject(this, var1);
      if (var3 != null) {
         var2.setReturnValue(var3);
      }
   }

   @Inject(
      method = {"delete"},
      at = {@At("TAIL")},
      require = 0,
      remap = false
   )
   private void larpclient$injectDelete(CallbackInfo var1) {
      SodiumTerrainMaterialBridge.fAsVoid(this);
   }
}
