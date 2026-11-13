package com.larpclient.mixin;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTextureDownloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({PlayerSkinTextureDownloader.class})
public interface PlayerSkinTextureDownloaderInvoker {
   @Invoker("method_65863")
   static NativeImage larpProcessSkin(NativeImage var0, String var1) {
      throw new AssertionError("Mixin invoker");
   }
}
