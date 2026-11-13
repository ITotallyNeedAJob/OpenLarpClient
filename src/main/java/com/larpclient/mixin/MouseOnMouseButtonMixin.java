package com.larpclient.mixin;

import com.larpclient.LarpClient;
import com.larpclient.module.WorldMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Mouse.class})
public abstract class MouseOnMouseButtonMixin {
   public MouseOnMouseButtonMixin() {
   }

   @Inject(
      method = {"method_1601(JLnet/minecraft/class_11910;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectIV(long var1, MouseInput var3, int var4, CallbackInfo var5) {
      if (var1 == MinecraftClient.getInstance().getWindow().getHandle()) {
         LarpClient var6 = LarpClient.CAsLarpClient();
         if (var6 != null && var4 == 1) {
            var6.vAsVoid(var3.button());
         }

         if (WorldMenu.zAsBoolean(var3, var4)) {
            var5.cancel();
         }
      }
   }
}
