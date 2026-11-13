package com.larpclient.mixin;

import com.larpclient.module.GUIAnimation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerListHud.class})
public class PlayerListHudRenderMixin {
   @Unique
   private boolean w;

   public PlayerListHudRenderMixin() {
   }

   @Inject(
      method = {"method_1919(Lnet/minecraft/class_332;ILnet/minecraft/class_269;Lnet/minecraft/class_266;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV(DrawContext var1, int var2, Scoreboard var3, ScoreboardObjective var4, CallbackInfo var5) {
      GUIAnimation var6 = GUIAnimation.kAsGUIAnimation();
      this.w = var6 != null && var6.pAsBoolean();
      if (this.w) {
         var1.getMatrices().pushMatrix();
         var1.getMatrices().translate(0.0F, (float)(-var1.getScaledWindowHeight()) * (1.0F - var6.aAsFloat()));
      }
   }

   @Inject(
      method = {"method_1919(Lnet/minecraft/class_332;ILnet/minecraft/class_269;Lnet/minecraft/class_266;)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectV2(DrawContext var1, int var2, Scoreboard var3, ScoreboardObjective var4, CallbackInfo var5) {
      if (this.w) {
         var1.getMatrices().popMatrix();
         this.w = false;
      }
   }
}
