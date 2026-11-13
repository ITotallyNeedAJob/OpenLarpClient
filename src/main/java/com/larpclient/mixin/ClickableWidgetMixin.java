package com.larpclient.mixin;

import com.larpclient.aq;
import com.larpclient.f;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({ClickableWidget.class})
public abstract class ClickableWidgetMixin {
   public ClickableWidgetMixin() {
   }

   @ModifyArgs(
      method = {"method_25394(Lnet/minecraft/class_332;IIF)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_332;method_58135(II)Z"
      )}
   )
   private void larpclient$modifyArgsIIFV(Args var1) {
      Screen var2 = MinecraftClient.getInstance().currentScreen;
      if (var2 instanceof f var3 && var3.larpDrawingMenuPopup()) {
         aq.Type_x var4 = f.bAsAq_Type_x(var2);
         var1.set(0, (int)Math.round(var4.screenX((double)((Integer)var1.get(0)).intValue(), (double)var2.width / 2.0)));
         var1.set(1, (int)Math.round(var4.screenY((double)((Integer)var1.get(1)).intValue(), (double)var2.height / 2.0)));
      }
   }
}
