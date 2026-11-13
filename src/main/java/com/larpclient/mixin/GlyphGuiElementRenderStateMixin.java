package com.larpclient.mixin;

import com.larpclient.xa;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import net.minecraft.client.font.TextDrawable;
import net.minecraft.client.gui.render.state.GlyphGuiElementRenderState;
import net.minecraft.client.texture.TextureSetup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GlyphGuiElementRenderState.class})
public abstract class GlyphGuiElementRenderStateMixin {
   @Shadow
   @Final
   private TextDrawable comp_4637;

   public GlyphGuiElementRenderStateMixin() {
   }

   @Inject(
      method = {"comp_4056()Lnet/minecraft/class_11231;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler(CallbackInfoReturnable<TextureSetup> var1) {
      GpuTextureView var2 = this.comp_4637.textureView();
      GpuTexture var3 = var2.texture();
      if (var3.getFormat() == TextureFormat.RED8 && xa.XAsBoolean(var3)) {
         var1.setReturnValue(TextureSetup.withLightmap(var2, RenderSystem.getSamplerCache().get(FilterMode.LINEAR)));
      }
   }
}
