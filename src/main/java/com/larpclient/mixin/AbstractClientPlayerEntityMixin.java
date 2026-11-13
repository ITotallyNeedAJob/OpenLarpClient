package com.larpclient.mixin;

import com.larpclient.ml;
import com.larpclient.module.Capes;
import com.larpclient.module.SkinChanger;
import java.util.UUID;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractClientPlayerEntity.class})
public class AbstractClientPlayerEntityMixin {
   public AbstractClientPlayerEntityMixin() {
   }

   @Inject(
      method = {"method_52814()Lnet/minecraft/class_8685;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectHandler(CallbackInfoReturnable<SkinTextures> var1) {
      UUID var2 = ((AbstractClientPlayerEntity)(Object)this).getUuid();
      SkinTextures var3 = ml.HAsSkinTextures(var2, (SkinTextures)var1.getReturnValue());
      SkinChanger var4 = SkinChanger.pAsSkinChanger();
      if (var4 != null) {
         var3 = var4.aAsSkinTextures(var2, var3);
      }

      Capes var5 = Capes.gAsCapes();
      var1.setReturnValue(var5 == null ? var3 : var5.LAsSkinTextures(var2, var3));
   }
}
