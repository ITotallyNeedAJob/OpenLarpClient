package com.larpclient.mixin;

import com.larpclient.ProxyRoute;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ClientConnection.class})
public abstract class ClientConnectionMixin {
   public ClientConnectionMixin() {
   }

   @Redirect(
      method = {"method_52271(Ljava/net/InetSocketAddress;Lnet/minecraft/class_12239;Lnet/minecraft/class_2535;)Lio/netty/channel/ChannelFuture;"},
      at = {@At(
         value = "INVOKE",
         target = "Lio/netty/bootstrap/Bootstrap;connect(Ljava/net/InetAddress;I)Lio/netty/channel/ChannelFuture;"
      )}
   )
   private static ChannelFuture larpclient$redirectHandler(Bootstrap var0, InetAddress var1, int var2) {
      return ProxyRoute.sAsChannelFuture(var0, var1, var2);
   }
}
