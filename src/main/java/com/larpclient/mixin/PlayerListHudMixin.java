package com.larpclient.mixin;

import com.larpclient.DisplayAliasManager;
import com.larpclient.module.FakePay;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerListHud.class})
public class PlayerListHudMixin {
   public PlayerListHudMixin() {
   }

   @ModifyExpressionValue(
      method = {"method_1919(Lnet/minecraft/class_332;ILnet/minecraft/class_269;Lnet/minecraft/class_266;)V"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/class_355;field_2154:Lnet/minecraft/class_2561;"
      )}
   )
   private Text larpclient$modifyExpressionValueV(Text var1) {
      return FakePay.wAsText(var1);
   }

   @Inject(
      method = {"method_1918(Lnet/minecraft/class_640;)Lnet/minecraft/class_2561;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectHandler(PlayerListEntry var1, CallbackInfoReturnable<Text> var2) {
      var2.setReturnValue(DisplayAliasManager.pAsText(var1.getProfile().id(), var1.getProfile().name(), (Text)var2.getReturnValue()));
   }
}
