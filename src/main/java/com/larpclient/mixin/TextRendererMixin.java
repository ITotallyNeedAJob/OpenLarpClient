package com.larpclient.mixin;

import com.larpclient.DisplayAliasManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.StringVisitable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({TextRenderer.class})
public class TextRendererMixin {
   public TextRendererMixin() {
   }

   @ModifyVariable(
      method = {"method_27525(Lnet/minecraft/class_5348;)I", "method_1728(Lnet/minecraft/class_5348;I)Ljava/util/List;"},
      at = {@At("HEAD")},
      argsOnly = true
   )
   private StringVisitable larpclient$modifyVariableI(StringVisitable var1) {
      return DisplayAliasManager.BAsStringVisitable(var1);
   }
}
