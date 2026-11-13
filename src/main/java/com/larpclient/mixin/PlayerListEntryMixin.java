package com.larpclient.mixin;

import com.larpclient.ml;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerListEntry.class})
public class PlayerListEntryMixin {
   public PlayerListEntryMixin() {
   }

   @Inject(
      method = {"method_52810()Lnet/minecraft/class_8685;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectHandler(CallbackInfoReturnable<SkinTextures> var1) {
      var1.setReturnValue(ml.HAsSkinTextures(((PlayerListEntry)(Object)this).getProfile().id(), (SkinTextures)var1.getReturnValue()));
   }
}
