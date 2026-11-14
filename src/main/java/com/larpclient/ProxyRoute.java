package com.larpclient;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.util.AttributeKey;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.UUID;

public final class ProxyRoute {
   public static final AttributeKey<ProxyValidation.Type_y> Z = AttributeKey.valueOf("larp_proxy_route");
   private static volatile ProxyRoute.Type_h O = new ProxyRoute.Type_h(null, null);

   private ProxyRoute() {
   }

   public static ProxyValidation.Type_y fAsPy_Type_y() {
      return O.active();
   }

   public static String jAsString() {
      return O.blocked();
   }

   public static void EAsVoid(ProxyValidation.Type_y var0) {
      O = new ProxyRoute.Type_h(Objects.requireNonNull(var0), null);
      ClientLog.GAsVoid("Proxy", "Server traffic now uses {} ({})", var0.settings().label(), var0.settings().authenticated() ? "with login" : "no login");
   }

   public static void IAsVoid() {
      if (O.active() != null || O.blocked() != null) {
         ClientLog.GAsVoid("Proxy", "Proxy off; server traffic connects directly");
      }

      O = new ProxyRoute.Type_h(null, null);
   }

   public static void iAsVoid(String var0) {
      O = new ProxyRoute.Type_h(null, Objects.requireNonNull(var0));
      ClientLog.oAsVoid("Proxy", "Server connections paused: {}", var0);
   }

   public static ChannelFuture sAsChannelFuture(Bootstrap var0, InetAddress var1, int var2) {
      ProxyRoute.Type_h var3 = O;
      if (!aAsBoolean(var1)) {
         if (var3.blocked() != null) {
            throw new UncheckedIOException(new ProxyChannelHandler.Type_r(var3.blocked()));
         } else if (var3.active() == null) {
            return var0.connect(var1, var2);
         } else {
            var0.handler(new ProxyRoute.Type_b(var3.active(), var0.config().handler()));
            ClientLog.XAsVoid("Proxy", "Opening PetEffectRenderer server connection through {}", var3.active().settings().label());
            return var0.connect(var1, var2);
         }
      } else {
         if (var3.active() != null || var3.blocked() != null) {
            ClientLog.XAsVoid("Proxy", "Local server address; connecting directly");
         }

         return var0.connect(var1, var2);
      }
   }

   public static void mAsVoid(MinecraftSessionService var0, Channel var1, UUID var2, String var3, String var4) throws AuthenticationException {
      ProxyValidation.Type_y var5 = var1 == null ? null : (ProxyValidation.Type_y)var1.attr(Z).get();
      if (var5 != null) {
         ProxySessionJoin.HAsVoid(var5, var2, var3, var4);
      } else {
         int var6 = var1 != null && var1.remoteAddress() instanceof InetSocketAddress var7 && var7.getAddress() != null && aAsBoolean(var7.getAddress())
            ? 1
            : 0;
         if (var6 == 0 && (O.active() != null || O.blocked() != null)) {
            ClientLog.oAsVoid("Proxy", "A direct server connection is logging in while the proxy is on; it was opened before the proxy changed");
         }

         var0.joinServer(var2, var3, var4);
      }
   }

   public static boolean aAsBoolean(InetAddress var0) {
      return var0.isLoopbackAddress()
         || var0.isAnyLocalAddress()
         || var0.isLinkLocalAddress()
         || var0.isSiteLocalAddress()
         || var0 instanceof Inet6Address && (var0.getAddress()[0] & 254) == 252;
   }

   public static long jAsLong(ProxyValidation.Type_y var0) throws IOException, InterruptedException {
      return ProxySessionJoin.GAsLong(var0);
   }

   static final class Type_b extends ChannelInitializer<Channel> {
      private final ProxyValidation.Type_y J;
      private final ChannelHandler r;

      Type_b(ProxyValidation.Type_y var1, ChannelHandler var2) {
         this.J = var1;
         this.r = Objects.requireNonNull(var2);
      }

      protected void initChannel(Channel var1) {
         var1.attr(ProxyRoute.Z).set(this.J);
         var1.pipeline().addLast(new ChannelHandler[]{this.r}).addFirst("larp_proxy", new ProxyChannelHandler(this.J));
      }
   }

   private static record Type_h(ProxyValidation.Type_y active, String blocked) {
   }
}
