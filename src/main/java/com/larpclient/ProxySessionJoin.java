package com.larpclient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.MinecraftClientHttpException;
import com.mojang.authlib.yggdrasil.response.ErrorResponse;
import com.mojang.util.UndashedUuid;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslHandler;
import io.netty.resolver.NoopAddressResolverGroup;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import net.minecraft.network.NetworkingBackend;

final class ProxySessionJoin {
   static final URI BoxUtils = URI.create("https://sessionserver.mojang.com/session/minecraft/join");
   static final int SahurDanceEngine = 10000;
   static final int W = 15;
   static final int D = 65536;

   private ProxySessionJoin() {
   }

   static void HAsVoid(ProxyValidation.Type_y var0, UUID var1, String var2, String var3) throws AuthenticationException {
      JsonObject var4 = new JsonObject();
      var4.addProperty("accessToken", var2);
      var4.addProperty("selectedProfile", UndashedUuid.toString(var1));
      var4.addProperty("serverId", var3);

      ProxySessionJoin.Type_x var5;
      try {
         var5 = xAsOf_Type_x(var0, c, var4.toString());
      } catch (IOException var7) {
         ClientLog.oAsVoid(
            "Proxy",
            "Mojang join check through {} failed ({}); the server login stops instead of going direct",
            var0.settings().label(),
            var7.getClass().getSimpleName()
         );
         throw new AuthenticationException(var7.getMessage());
      } catch (InterruptedException var8) {
         Thread.currentThread().interrupt();
         throw new AuthenticationException("Proxy " + var0.settings().label() + ": the join check was interrupted");
      }

      if (var5.status() / 100 == 2) {
         ClientLog.GAsVoid("Proxy", "Mojang join check passed through {}", var0.settings().label());
      } else {
         ClientLog.oAsVoid("Proxy", "Mojang join check through {} returned HTTP {}", var0.settings().label(), var5.status());
         throw NAsAuthenticationException(var5.status(), var5.body());
      }
   }

   static AuthenticationException NAsAuthenticationException(int var0, String var1) {
      ErrorResponse var2 = xAsErrorResponse(var1);
      return (var2 == null ? new MinecraftClientHttpException(var0) : new MinecraftClientHttpException(var0, var2)).toAuthenticationException();
   }

   static long GAsLong(ProxyValidation.Type_y var0) throws IOException, InterruptedException {
      long var1 = System.nanoTime();
      CompletableFuture var3 = new CompletableFuture();
      ChannelFuture var4 = JAsBootstrap(var0, new ProxySessionJoin.Type_k(var0, var3)).connect(IAsInetSocketAddress(var0, BoxUtils.getHost()));
      var4.addListener(var1x -> {
         if (!var1x.isSuccess()) {
            var3.completeExceptionally(var1x.cause());
         }
      });

      long var5;
      try {
         uAsObject(var0, var3);
         var5 = (System.nanoTime() - var1) / 1000000L;
      } finally {
         var4.channel().close();
      }

      return var5;
   }

   static ProxySessionJoin.Type_x xAsOf_Type_x(ProxyValidation.Type_y var0, URI var1, String var2) throws IOException, InterruptedException {
      if ("https".equals(var1.getScheme()) && var1.getPort() == -1) {
         byte[] var3 = var2.getBytes(StandardCharsets.UTF_8);
         DefaultFullHttpRequest var4 = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, var1.getRawPath(), Unpooled.wrappedBuffer(var3));
         var4.headers()
            .set(HttpHeaderNames.HOST, var1.getHost())
            .set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8")
            .set(HttpHeaderNames.CONTENT_LENGTH, var3.length)
            .set(HttpHeaderNames.ACCEPT, "application/json")
            .set(HttpHeaderNames.USER_AGENT, "Java/" + System.getProperty("java.version"))
            .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
         CompletableFuture var5 = new CompletableFuture();

         ChannelFuture var6;
         try {
            var6 = JAsBootstrap(var0, new ProxySessionJoin.Type_w(var0, var1.getHost(), var4, var5)).connect(IAsInetSocketAddress(var0, var1.getHost()));
         } catch (RuntimeException var121) {
            var4.release();
            throw var121;
         }

         var6.addListener(var1x -> {
            if (!var1x.isSuccess()) {
               var5.completeExceptionally(var1x.cause());
            }
         });

         ProxySessionJoin.Type_x var12;
         try {
            var12 = (ProxySessionJoin.Type_x)uAsObject(var0, var5);
         } finally {
            var6.channel().close();
         }

         return var12;
      } else {
         throw new IllegalArgumentException("Proxy requests use default HTTPS endpoints");
      }
   }

   private static Bootstrap JAsBootstrap(ProxyValidation.Type_y var0, ChannelHandler var1) {
      NetworkingBackend var2 = NetworkingBackend.remote(false);
      return (Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group(var2.getEventLoopGroup())).channel(var2.getChannelClass()))
            .resolver(NoopAddressResolverGroup.INSTANCE)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000))
         .handler(var1);
   }

   private static InetSocketAddress IAsInetSocketAddress(ProxyValidation.Type_y var0, String var1) throws IOException {
      return var0.settings().type() == ProxyValidation.Type_j.SOCKS4
         ? new InetSocketAddress(InetAddress.getByName(var1), 443)
         : InetSocketAddress.createUnresolved(var1, 443);
   }

   private static <T> T uAsObject(ProxyValidation.Type_y var0, CompletableFuture<T> var1) throws IOException, InterruptedException {
      try {
         return var1.get(15L, TimeUnit.SECONDS);
      } catch (TimeoutException var5) {
         var1.cancel(false);
         throw new ProxyChannelHandler.Type_r("Proxy " + var0.settings().label() + ": the request did not finish in time");
      } catch (ExecutionException var6) {
         Throwable var3 = var6.getCause();
         if (var3 instanceof ProxyChannelHandler.Type_r var4) {
            throw var4;
         } else if (var3 instanceof SSLException) {
            throw new ProxyChannelHandler.Type_r("Proxy " + var0.settings().label() + ": the secure connection through the proxy failed", var3);
         } else {
            throw new ProxyChannelHandler.Type_r("Proxy " + var0.settings().label() + ": the request failed", var3);
         }
      }
   }

   private static ErrorResponse xAsErrorResponse(String var0) {
      try {
         JsonObject var1 = JsonParser.parseString(var0).getAsJsonObject();
         return new ErrorResponse(mAsString(var1, "path"), mAsString(var1, "error"), mAsString(var1, "errorMessage"), null);
      } catch (RuntimeException var21) {
         return null;
      }
   }

   private static String mAsString(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonPrimitive() ? var0.get(var1).getAsString() : null;
   }

   private static SslHandler OAsSslHandler(String var0) throws NoSuchAlgorithmException {
      SSLEngine var1 = SSLContext.getDefault().createSSLEngine(var0, 443);
      var1.setUseClientMode(true);
      SSLParameters var2 = var1.getSSLParameters();
      var2.setEndpointIdentificationAlgorithm("HTTPS");
      var2.setServerNames(List.of(new SNIHostName(var0)));
      var1.setSSLParameters(var2);
      return new SslHandler(var1);
   }

   static final class Type_c extends SimpleChannelInboundHandler<FullHttpResponse> {
      private final FullHttpRequest l;
      private final CompletableFuture<ProxySessionJoin.Type_x> d;

      Type_c(FullHttpRequest var1, CompletableFuture<ProxySessionJoin.Type_x> var2) {
         this.l = var1;
         this.d = var2;
      }

      public void channelActive(ChannelHandlerContext var1) {
         var1.writeAndFlush(this.l.retain());
      }

      protected void channelRead0(ChannelHandlerContext var1, FullHttpResponse var2) {
         this.d.complete(new ProxySessionJoin.Type_x(var2.status().code(), var2.content().toString(StandardCharsets.UTF_8)));
         var1.close();
      }

      public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) {
         this.d.completeExceptionally(var2);
         var1.close();
      }

      public void channelInactive(ChannelHandlerContext var1) {
         this.d.completeExceptionally(new IOException("The connection closed before PetEffectRenderer response"));
      }

      public void handlerRemoved(ChannelHandlerContext var1) {
         if (this.l.refCnt() > 0) {
            this.l.release();
         }
      }
   }

   static final class Type_k extends ChannelInitializer<Channel> {
      private final ProxyValidation.Type_y a;
      private final CompletableFuture<Void> D;

      Type_k(ProxyValidation.Type_y var1, CompletableFuture<Void> var2) {
         this.a = var1;
         this.D = var2;
      }

      protected void initChannel(Channel var1) {
         var1.pipeline().addLast("larp_proxy", new ProxyChannelHandler(this.a)).addLast("larp_probe", new ProxySessionJoin.Type_u(this.D));
      }
   }

   static final class Type_u extends ChannelInboundHandlerAdapter {
      private final CompletableFuture<Void> L;

      Type_u(CompletableFuture<Void> var1) {
         this.L = var1;
      }

      public void channelActive(ChannelHandlerContext var1) {
         this.L.complete(null);
         var1.close();
      }
   }

   static final class Type_w extends ChannelInitializer<Channel> {
      private final ProxyValidation.Type_y M;
      private final String i;
      private final FullHttpRequest H;
      private final CompletableFuture<ProxySessionJoin.Type_x> L;

      Type_w(ProxyValidation.Type_y var1, String var2, FullHttpRequest var3, CompletableFuture<ProxySessionJoin.Type_x> var4) {
         this.M = var1;
         this.i = var2;
         this.H = var3;
         this.L = var4;
      }

      protected void initChannel(Channel var1) throws Exception {
         var1.pipeline()
            .addLast("larp_proxy", new ProxyChannelHandler(this.M))
            .addLast("tls", ProxySessionJoin.OAsSslHandler(this.i))
            .addLast("http", new HttpClientCodec())
            .addLast("aggregate", new HttpObjectAggregator(65536))
            .addLast("larp_response", new ProxySessionJoin.Type_c(this.H, this.L));
      }
   }

   static record Type_x(int status, String body) {
   }
}
