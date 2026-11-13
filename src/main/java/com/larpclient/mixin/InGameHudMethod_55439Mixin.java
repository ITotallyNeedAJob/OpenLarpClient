package com.larpclient.mixin;

import com.larpclient.module.FakePay;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.text.MutableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InGameHud.class})
public class InGameHudMethod_55439Mixin {
   public InGameHudMethod_55439Mixin() {
   }

   @ModifyExpressionValue(
      method = {"method_55439(Lnet/minecraft/class_269;Lnet/minecraft/class_9022;Lnet/minecraft/class_9011;)Lnet/minecraft/class_329$class_9016;"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_268;method_1142(Lnet/minecraft/class_270;Lnet/minecraft/class_2561;)Lnet/minecraft/class_5250;"
      )}
   )
   private MutableText larpclient$modifyExpressionValueHandler(MutableText var1, Scoreboard var2, NumberFormat var3, ScoreboardEntry var4) {
      Team var5 = var2.getScoreHolderTeam(var4.owner());
      return FakePay.VAsMutableText(var1, null, var4.owner(), var5 == null ? "" : var5.getName());
   }

   @ModifyExpressionValue(
      method = {"method_55439(Lnet/minecraft/class_269;Lnet/minecraft/class_9022;Lnet/minecraft/class_9011;)Lnet/minecraft/class_329$class_9016;"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_9011;method_55386(Lnet/minecraft/class_9022;)Lnet/minecraft/class_5250;"
      )}
   )
   private MutableText larpclient$modifyExpressionValueHandler2(MutableText var1, Scoreboard var2, NumberFormat var3, ScoreboardEntry var4) {
      Team var5 = var2.getScoreHolderTeam(var4.owner());
      return FakePay.VAsMutableText(var1, Team.decorateName(var5, var4.name()), var4.owner(), var5 == null ? "" : var5.getName());
   }

   @Inject(
      method = {"method_1757(Lnet/minecraft/class_332;Lnet/minecraft/class_266;)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectV(DrawContext var1, ScoreboardObjective var2, CallbackInfo var3) {
      FakePay.aAsVoid(var2);
   }
}
