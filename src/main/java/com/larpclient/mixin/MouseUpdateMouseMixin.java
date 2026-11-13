package com.larpclient.mixin;

import com.larpclient.module.Freecam;
import com.larpclient.module.WorldMenu;
import com.larpclient.module.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({Mouse.class})
public abstract class MouseUpdateMouseMixin {
   public MouseUpdateMouseMixin() {
   }

   @ModifyArgs(
      method = {"method_1606(D)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_746;method_5872(DD)V"
      )}
   )
   private void larpclient$modifyArgsUpdateMouse(Args var1) {
      double var2 = Zoom.EAsDouble();
      var1.set(0, (Double)var1.get(0) * var2);
      var1.set(1, (Double)var1.get(1) * var2);
   }

   @Inject(
      method = {"method_1598(JDD)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectOnMouseScroll(long var1, double var3, double var5, CallbackInfo var7) {
      MinecraftClient var8 = MinecraftClient.getInstance();
      if (var1 == var8.getWindow().getHandle() && var5 != 0.0) {
         double var9 = (var8.options.getDiscreteMouseScroll().getValue() ? Math.signum(var5) : var5)
            * (Double)var8.options.getMouseWheelSensitivity().getValue();
         if (var8.currentScreen == null && var8.getOverlay() == null && (WorldMenu.lAsBoolean(var9) || Zoom.hAsBoolean(var9) || Freecam.QAsBoolean(var9))) {
            var7.cancel();
         }
      }
   }
}
