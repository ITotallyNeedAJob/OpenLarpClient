package com.larpclient.mixin.feature.render;

import com.larpclient.SodiumTerrainMaterialBridge;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(
   targets = {"net/caffeinemc/mods/sodium/client/gl/shader/ShaderLoader"},
   remap = false
)
public abstract class ShaderLoaderMixin {
   public ShaderLoaderMixin() {
   }

   @Inject(
      method = {"getShaderSource"},
      at = {@At("RETURN")},
      cancellable = true,
      require = 0,
      remap = false
   )
   private static void larpclient$injectGetShaderSource(Identifier var0, CallbackInfoReturnable<String> var1) {
      String var2 = (String)var1.getReturnValue();
      String var3 = SodiumTerrainMaterialBridge.mAsString(var0, var2);
      if (var3 != var2) {
         var1.setReturnValue(var3);
      }
   }
}
