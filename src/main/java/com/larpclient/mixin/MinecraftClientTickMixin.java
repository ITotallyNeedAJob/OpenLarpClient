package com.larpclient.mixin;

import com.larpclient.ClientLog;
import com.larpclient.LarpClient;
import com.larpclient.module.Mace;
import com.larpclient.module.Module;
import com.larpclient.module.RodSwap;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MinecraftClient.class})
public abstract class MinecraftClientTickMixin {
   public MinecraftClientTickMixin() {
   }

   @Inject(
      method = {"method_1574()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_329;method_39191(Z)V"
      )}
   )
   private void larpclient$injectTick(CallbackInfo var1) {
      LarpClient var2 = LarpClient.CAsLarpClient();
      if (var2 != null && var2.mAsF() != null) {
         var2.mAsF().OAsVoid();
         RodSwap var3 = var2.mAsF().EAsModule(RodSwap.class);
         if (var3 != null && var3.isRuntimeActive()) {
            C(var3, var3::CAsVoid);
         }

         Mace var4 = Mace.yAsMace();
         if (var4 != null && var4.isRuntimeActive()) {
            C(var4, Mace::eAsVoid);
         }
      }
   }

   @Unique
   private static void C(Module var0, Runnable var1) {
      try {
         var1.run();
      } catch (RuntimeException var3) {
         ClientLog.lAsVoid("Combat", "Pre-input tick failed; releasing " + var0.getName(), var3);
         var0.disable();
      }
   }
}
