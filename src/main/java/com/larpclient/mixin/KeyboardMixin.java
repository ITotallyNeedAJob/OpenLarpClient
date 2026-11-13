package com.larpclient.mixin;

import com.larpclient.LarpClient;
import com.larpclient.module.WorldMenu;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Keyboard.class})
public abstract class KeyboardMixin {
   public KeyboardMixin() {
   }

   @Inject(
      method = {"method_1466(JILnet/minecraft/class_11908;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(long var1, int var3, KeyInput var4, CallbackInfo var5) {
      if (var1 == MinecraftClient.getInstance().getWindow().getHandle()) {
         LarpClient var6 = LarpClient.CAsLarpClient();
         if (var6 != null && var3 == 1) {
            var6.OAsVoid(var4.key());
         }

         if (WorldMenu.JAsBoolean(var3, var4)) {
            var5.cancel();
         }
      }
   }

   @Inject(
      method = {"method_1457(JLnet/minecraft/class_11905;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(long var1, CharInput var3, CallbackInfo var4) {
      if (var1 == MinecraftClient.getInstance().getWindow().getHandle() && WorldMenu.AAsBoolean(var3)) {
         var4.cancel();
      }
   }
}
