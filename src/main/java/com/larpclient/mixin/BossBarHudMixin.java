package com.larpclient.mixin;

import com.larpclient.module.Removals;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BossBarHud.class})
public class BossBarHudMixin {
   public BossBarHudMixin() {
   }

   @Inject(
      method = {"method_1796(Lnet/minecraft/class_332;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(DrawContext var1, CallbackInfo var2) {
      if (Removals.xAsBoolean("bossBar")) {
         var2.cancel();
      }
   }
}
