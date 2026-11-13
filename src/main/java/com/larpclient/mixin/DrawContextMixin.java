package com.larpclient.mixin;

import com.larpclient.DisplayAliasManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({DrawContext.class})
public class DrawContextMixin {
   public DrawContextMixin() {
   }

   @ModifyVariable(
      method = {"method_51439(Lnet/minecraft/class_327;Lnet/minecraft/class_2561;IIIZ)V"},
      at = {@At("HEAD")},
      argsOnly = true
   )
   private Text larpclient$modifyVariableIIIZV(Text var1) {
      return DisplayAliasManager.eAsText(var1);
   }
}
