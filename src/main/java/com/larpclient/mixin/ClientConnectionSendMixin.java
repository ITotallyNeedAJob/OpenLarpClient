package com.larpclient.mixin;

import com.larpclient.FallDamage;
import com.larpclient.module.Backtrack;
import com.larpclient.module.FakeLag;
import com.larpclient.module.FakePay;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientConnection.class})
public class ClientConnectionSendMixin {
   public ClientConnectionSendMixin() {
   }

   @Inject(
      method = {"method_52906(Lnet/minecraft/class_2596;Lio/netty/channel/ChannelFutureListener;Z)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectZV(Packet<?> var1, ChannelFutureListener var2, boolean var3, CallbackInfo var4) {
      if (!Backtrack.zAsBoolean()) {
         if (FakePay.tAsBoolean(var1)) {
            var4.cancel();
         } else if (Backtrack.pAsBoolean() && Backtrack.zAsBoolean((ClientConnection)(Object)this, var1, var2, var3)) {
            var4.cancel();
         } else if (FakeLag.JAsBoolean() && FakeLag.FAsBoolean((ClientConnection)(Object)this, var1, var2, var3)) {
            var4.cancel();
         } else {
            FallDamage.uAsVoid(var1);
         }
      }
   }

   @Inject(
      method = {"method_10759(Lnet/minecraft/class_2596;Lnet/minecraft/class_2547;)V"},
      at = {@At("HEAD")}
   )
   private static void larpclient$injectV(Packet<?> var0, PacketListener var1, CallbackInfo var2) {
      Backtrack.KAsVoid(var0, var1);
   }
}
