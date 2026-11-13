package com.larpclient.mixin;

import com.larpclient.module.Removals;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InGameHud.class})
public class InGameHudRenderScoreboardSidebarMixin {
   public InGameHudRenderScoreboardSidebarMixin() {
   }

   @Inject(
      method = {"method_55803(Lnet/minecraft/class_332;Lnet/minecraft/class_9779;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(DrawContext var1, RenderTickCounter var2, CallbackInfo var3) {
      if (Removals.xAsBoolean("scoreboard")) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"method_1746(Lnet/minecraft/class_332;F)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectFV(DrawContext var1, float var2, CallbackInfo var3) {
      if (Removals.xAsBoolean("portal")) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"method_61980(Lnet/minecraft/class_332;F)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectFV2(DrawContext var1, float var2, CallbackInfo var3) {
      if (Removals.xAsBoolean("nausea")) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"method_31977(Lnet/minecraft/class_332;Lnet/minecraft/class_2960;F)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectFV(DrawContext var1, Identifier var2, float var3, CallbackInfo var4) {
      if (Removals.xAsBoolean("pumpkin") && var2.equals(Identifier.ofVanilla("textures/misc/pumpkinblur.png"))) {
         var4.cancel();
      }
   }
}
