package com.larpclient.mixin;

import com.larpclient.ProxyRoute;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import net.minecraft.client.network.MultiplayerServerListPinger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({MultiplayerServerListPinger.class})
public abstract class MultiplayerServerListPingerMixin {
   public MultiplayerServerListPingerMixin() {
   }

   @Redirect(
      method = {"method_3001(Ljava/net/InetSocketAddress;Lnet/minecraft/class_639;Lnet/minecraft/class_642;Lnet/minecraft/class_12239;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lio/netty/bootstrap/Bootstrap;connect(Ljava/net/InetAddress;I)Lio/netty/channel/ChannelFuture;"
      )}
   )
   private ChannelFuture larpclient$redirectV(Bootstrap var1, InetAddress var2, int var3) {
      return ProxyRoute.sAsChannelFuture(var1, var2, var3);
   }
}
