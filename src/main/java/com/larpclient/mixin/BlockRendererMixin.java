package com.larpclient.mixin;

import com.larpclient.module.XRay;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(
   targets = {"net/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer"},
   remap = false
)
public abstract class BlockRendererMixin {
   @Unique
   private boolean m;

   public BlockRendererMixin() {
   }

   @Inject(
      method = {"renderModel"},
      at = {@At("HEAD")},
      require = 0,
      remap = false
   )
   private void larpclient$injectRenderModel(BlockStateModel var1, BlockState var2, BlockPos var3, BlockPos var4, CallbackInfo var5) {
      this.m = XRay.aAsBoolean() && XRay.ZAsBoolean(var2.getBlock());
   }

   @ModifyArg(
      method = {"renderModel"},
      require = 0,
      remap = false,
      at = {@At(
         value = "INVOKE",
         target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;prepareAoInfo(Z)V"
      )}
   )
   private boolean larpclient$modifyArgRenderModel(boolean var1) {
      return var1 && !this.m;
   }

   @ModifyArg(
      method = {"processQuad"},
      index = 2,
      require = 0,
      remap = false,
      at = {@At(
         value = "INVOKE",
         target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;shadeQuad(Lnet/caffeinemc/mods/sodium/client/render/model/MutableQuadViewImpl;Lnet/caffeinemc/mods/sodium/client/model/light/LightMode;ZLnet/caffeinemc/mods/sodium/client/render/model/SodiumShadeMode;)V"
      )}
   )
   private boolean larpclient$modifyArgProcessQuad(boolean var1) {
      return var1 || this.m;
   }
}
