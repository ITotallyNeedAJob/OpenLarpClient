package com.larpclient.mixin;

import com.larpclient.module.Backtrack;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.crash.CrashException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ClientPlayNetworkHandler.class})
public abstract class ClientPlayNetworkHandlerMixin {
   public ClientPlayNetworkHandlerMixin() {
   }

   @Redirect(
      method = {"method_48294(Lnet/minecraft/class_8042;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_2596;method_65081(Lnet/minecraft/class_2547;)V"
      )}
   )
   private <T extends PacketListener> void larpclient$redirectV(Packet<T> var1, T var2) {
      Runnable var3 = () -> {
         if (var2.accepts(var1)) {
            try {
               var1.apply(var2);
            } catch (Exception var4) {
               if (var4 instanceof CrashException var3x && var3x.getCause() instanceof OutOfMemoryError) {
                  throw NetworkThreadUtils.createCrashException(var4, var1, var2);
               }

               var2.onPacketException(var1, var4);
            }
         }
      };
      if (Backtrack.WAsBoolean() || !Backtrack.MAsBoolean(var1, var2, var3)) {
         var1.apply(var2);
      }
   }
}
