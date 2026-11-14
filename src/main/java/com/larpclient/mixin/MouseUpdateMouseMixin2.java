package com.larpclient.mixin;

import com.larpclient.WaveEffect;
import com.larpclient.module.AimAssist;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({Mouse.class})
public abstract class MouseUpdateMouseMixin2 {
   public MouseUpdateMouseMixin2() {
   }

   @ModifyArgs(
      method = {"method_1606(D)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_746;method_5872(DD)V"
      )},
      order = 1100,
      require = 1,
      allow = 1
   )
   private void larpclient$modifyArgsUpdateMouse(Args var1, double var2) {
      w7.Type_h var4 = AimAssist.OAsW7_Type_h(MinecraftClient.getInstance().player, (Double)var1.get(0), (Double)var1.get(1), var2);
      var1.set(0, (Double)var1.get(0) + var4.yaw());
      var1.set(1, (Double)var1.get(1) + var4.pitch());
   }
}
