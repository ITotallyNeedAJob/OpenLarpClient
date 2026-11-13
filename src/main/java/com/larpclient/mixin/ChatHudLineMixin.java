package com.larpclient.mixin;

import com.larpclient.DisplayAliasManager;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.StringVisitable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({ChatHudLine.class})
public class ChatHudLineMixin {
   public ChatHudLineMixin() {
   }

   @ModifyArg(
      method = {"method_75757(Lnet/minecraft/class_327;I)Ljava/util/List;"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_341;method_1850(Lnet/minecraft/class_5348;ILnet/minecraft/class_327;)Ljava/util/List;"
      )},
      index = 0
   )
   private StringVisitable larpclient$modifyArgHandler(StringVisitable var1) {
      return DisplayAliasManager.BAsStringVisitable(var1);
   }
}
