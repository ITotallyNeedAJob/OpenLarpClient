package com.larpclient.mixin;

import com.larpclient.ClientLog;
import com.larpclient.xa;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.util.function.Supplier;
import net.minecraft.client.font.GlyphAtlasTexture;
import net.minecraft.client.font.TextRenderLayerSet;
import net.minecraft.client.texture.AbstractTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GlyphAtlasTexture.class})
public abstract class GlyphAtlasTextureMixin extends AbstractTexture {
   public GlyphAtlasTextureMixin() {
   }

   @Inject(
      method = {"<init>"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectInit(Supplier<String> var1, TextRenderLayerSet var2, boolean var3, CallbackInfo var4) {
      String var5 = var1.get();
      if (xa.AAsBoolean(this.glTexture, var5, var3)) {
         this.sampler = RenderSystem.getSamplerCache().get(FilterMode.LINEAR);
         ClientLog.XAsVoid("Font", "Linear UI glyph sampling selected for {}", var5);
      }
   }
}
