package com.larpclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.PendingWriteQueue;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.ScheduledFuture;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public final class ProxyChannelHandler extends ChannelDuplexHandler {
   public static final String Y = "larp_proxy";
   static final int MenuPopupProvider = 10;
   static final int A = 8192;
   private final ProxyValidation.Type_y v;
   private InetSocketAddress g;
   private ChannelPromise r;
   private PendingWriteQueue q;
   private ByteBuf TextLayout = Unpooled.EMPTY_BUFFER;
   private ScheduledFuture<?> L;
   private boolean z;
   private boolean x;
   private boolean E;
   private int m;

   public ProxyChannelHandler(ProxyValidation.Type_y var1) {
      this.v = var1;
   }

   public void handlerAdded(ChannelHandlerContext var1) {
      this.q = new PendingWriteQueue(var1);
   }

   public void connect(ChannelHandlerContext var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) {
      if (this.g != null) {
         var4.setFailure(new ProxyChannelHandler.Type_r("Proxy connection was already started"));
      } else if (var2 instanceof InetSocketAddress var5) {
         this.g = var5;
         this.r = var4;
         var4.addListener(var1xx -> {
            if (var1xx.isCancelled()) {
               var1.close();
            }
         });
         ChannelPromise var6 = var1.newPromise();
         var6.addListener(var2x -> {
            if (!var2x.isSuccess() && !var2x.isCancelled()) {
               this.KAsVoid(var1, new ProxyChannelHandler.Type_r(this.MAsString() + "could not reach the proxy", var2x.cause()));
            }
         });
         var1.connect(this.v.address(), var3, var6);
      } else {
         var4.setFailure(new ProxyChannelHandler.Type_r("Proxy destinations must be network addresses"));
      }
   }

   public void channelActive(ChannelHandlerContext var1) {
      this.L = var1.executor()
         .schedule(() -> this.KAsVoid(var1, new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy did not answer in time")), 10L, TimeUnit.SECONDS);

      try {
         this.mAsVoid(var1);
      } catch (ProxyChannelHandler.Type_r var3) {
         this.KAsVoid(var1, var3);
      }
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) {
      if (this.z) {
         var1.fireChannelRead(var2);
      } else if (var2 instanceof ByteBuf var3) {
         if (this.x) {
            var3.release();
         } else {
            if (this.j.isReadable()) {
               this.j = Unpooled.wrappedBuffer(new ByteBuf[]{this.j, var3});
            } else {
               this.j.release();
               this.j = var3;
            }

            try {
               if (this.j.readableBytes() > 8192) {
                  throw new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy sent an oversized reply");
               }

               while (!this.z && !this.x && this.mAsBoolean(var1)) {
               }
            } catch (ProxyChannelHandler.Type_r var5) {
               this.KAsVoid(var1, var5);
            }
         }
      } else {
         ReferenceCountUtil.release(var2);
      }
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      if (this.z) {
         super.channelInactive(var1);
      } else {
         this.KAsVoid(var1, new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy closed the connection"));
      }
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) {
      if (this.z) {
         var1.fireExceptionCaught(var2);
      } else {
         this.KAsVoid(
            var1,
            var2 instanceof ProxyChannelHandler.Type_r var3 ? var3 : new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy connection failed", var2)
         );
      }
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) {
      if (this.z) {
         var1.write(var2, var3);
      } else if (this.x) {
         ReferenceCountUtil.release(var2);
         var3.setFailure(new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy connection failed"));
      } else {
         this.q.add(var2, var3);
      }
   }

   public void flush(ChannelHandlerContext var1) {
      if (this.z) {
         var1.flush();
      } else {
         this.E = true;
      }
   }

   public void handlerRemoved(ChannelHandlerContext var1) {
      if (this.L != null) {
         this.L.cancel(false);
      }

      if (!this.z) {
         if (!this.q.isEmpty()) {
            this.q.removeAndFailAll(new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy connection closed"));
         }

         if (this.j.refCnt() > 0 && this.j != Unpooled.EMPTY_BUFFER) {
            this.j.release();
         }

         this.j = Unpooled.EMPTY_BUFFER;
      }
   }

   private void mAsVoid(ChannelHandlerContext var1) throws ProxyChannelHandler.Type_r {
      ProxyValidation var2 = this.v.settings();
      ByteBuf var3 = var1.alloc().buffer();
      switch (var2.type()) {
         case SOCKS5:
            if (var2.authenticated()) {
               var3.writeBytes(new byte[]{5, 2, 0, 2});
            } else {
               var3.writeBytes(new byte[]{5, 1, 0});
            }
            break;
         case SOCKS4:
            var3.writeByte(4).writeByte(1).writeShort(this.g.getPort());
            byte[] var7 = var2.username().getBytes(StandardCharsets.UTF_8);
            if (this.g.isUnresolved()) {
               byte[] var6 = OAsByteArray(this.g.getHostString());
               var3.writeBytes(new byte[]{0, 0, 0, 1}).writeBytes(var7).writeByte(0).writeBytes(var6).writeByte(0);
            } else {
               if (!(this.g.getAddress() instanceof Inet4Address var8)) {
                  var3.release();
                  throw new ProxyChannelHandler.Type_r(this.MAsString() + "SOCKS4 proxies cannot reach IPv6 servers");
               }

               var3.writeBytes(var8.getAddress()).writeBytes(var7).writeByte(0);
            }
            break;
         case HTTP:
            String var4 = WAsString(this.g);
            StringBuilder var5 = new StringBuilder("CONNECT ").append(var4).append(" HTTP/1.1\r\nHost: ").append(var4).append("\r\n");
            if (var2.authenticated()) {
               var5.append("Proxy-Authorization: Basic ")
                  .append(Base64.getEncoder().encodeToString((var2.username() + ":" + var2.password()).getBytes(StandardCharsets.UTF_8)))
                  .append("\r\n");
            }

            var3.writeBytes(var5.append("\r\n").toString().getBytes(StandardCharsets.ISO_8859_1));
      }

      var1.writeAndFlush(var3).addListener(var2x -> {
         if (!var2x.isSuccess()) {
            this.KAsVoid(var1, new ProxyChannelHandler.Type_r(this.MAsString() + "could not talk to the proxy", var2x.cause()));
         }
      });
   }

   private boolean mAsBoolean(ChannelHandlerContext var1) throws ProxyChannelHandler.Type_r {
      ProxyValidation var2 = this.v.settings();
      switch (var2.type()) {
         case SOCKS5:
            return this.SAsBoolean(var1, var2);
         case SOCKS4:
            if (this.j.readableBytes() < 8) {
               return false;
            } else {
               short var10 = this.j.readUnsignedByte();
               short var11 = this.j.readUnsignedByte();
               this.j.skipBytes(6);
               if (var10 != 0) {
                  throw new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy does not speak SOCKS4");
               } else {
                  if (var11 == 90) {
                     this.tAsVoid(var1);
                     return true;
                  }

                  throw new ProxyChannelHandler.Type_r(
                     this.MAsString() + (var11 != 92 && var11 != 93 ? "the proxy refused the connection" : "the proxy rejected the user ID")
                  );
               }
            }
         case HTTP:
            int var3 = this.PAsInt();
            if (var3 < 0) {
               return false;
            } else {
               String var4 = this.j.readCharSequence(var3 + 4 - this.j.readerIndex(), StandardCharsets.ISO_8859_1).toString();
               String var5 = var4.substring(0, Math.max(0, var4.indexOf("\r\n")));
               String[] var6 = var5.split(" ", 3);
               if (var6.length >= 2 && var6[0].startsWith("HTTP/1.")) {
                  int var7;
                  try {
                     var7 = Integer.parseInt(var6[1]);
                  } catch (NumberFormatException var9) {
                     throw new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy sent an invalid reply");
                  }

                  if (var7 == 407) {
                     throw new ProxyChannelHandler.Type_r(
                        this.MAsString() + (var2.authenticated() ? "the proxy rejected the username or password" : "the proxy needs PetEffectRenderer username and password")
                     );
                  }

                  if (var7 / 100 != 2) {
                     throw new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy refused the connection (HTTP " + var7 + ")");
                  }

                  this.tAsVoid(var1);
                  return true;
               }

               throw new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy does not speak HTTP CONNECT");
            }
         default:
            return false;
      }
   }

   private boolean SAsBoolean(ChannelHandlerContext var1, ProxyValidation var2) throws ProxyChannelHandler.Type_r {
      if (this.m == 0) {
         if (this.j.readableBytes() < 2) {
            return false;
         } else {
            short var7 = this.j.readUnsignedByte();
            short var8 = this.j.readUnsignedByte();
            if (var7 != 5) {
               throw new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy does not speak SOCKS5");
            } else if (var8 == 2 && var2.authenticated()) {
               byte[] var9 = var2.username().getBytes(StandardCharsets.UTF_8);
               byte[] var10 = var2.password().getBytes(StandardCharsets.UTF_8);
               var1.writeAndFlush(var1.alloc().buffer().writeByte(1).writeByte(var9.length).writeBytes(var9).writeByte(var10.length).writeBytes(var10));
               this.m = 1;
               return true;
            } else if (var8 != 0) {
               throw new ProxyChannelHandler.Type_r(
                  this.MAsString()
                     + (
                        var8 == 255
                           ? (var2.authenticated() ? "the proxy refused these login methods" : "the proxy needs PetEffectRenderer username and password")
                           : "the proxy chose an unsupported login method"
                     )
               );
            } else {
               this.gAsVoid(var1);
               this.m = 2;
               return true;
            }
         }
      } else if (this.m == 1) {
         if (this.j.readableBytes() < 2) {
            return false;
         } else {
            this.j.readUnsignedByte();
            if (this.j.readUnsignedByte() != 0) {
               throw new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy rejected the username or password");
            } else {
               this.gAsVoid(var1);
               this.m = 2;
               return true;
            }
         }
      } else if (this.j.readableBytes() < 5) {
         return false;
      } else {
         int var3 = this.j.readerIndex();
         short var4 = this.j.getUnsignedByte(var3 + 3);

         int var5 = switch (var4) {
            case 1 -> 10;
            default -> -1;
            case 3 -> 7 + this.j.getUnsignedByte(var3 + 4);
            case 4 -> 22;
         };
         if (this.j.getUnsignedByte(var3) != 5) {
            throw new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy does not speak SOCKS5");
         } else {
            short var6 = this.j.getUnsignedByte(var3 + 1);
            if (var6 != 0) {
               throw new ProxyChannelHandler.Type_r(this.MAsString() + aAsString(var6));
            } else if (var5 < 0) {
               throw new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy sent an invalid reply");
            } else if (this.j.readableBytes() < var5) {
               return false;
            } else {
               this.j.skipBytes(var5);
               this.tAsVoid(var1);
               return true;
            }
         }
      }
   }

   private void gAsVoid(ChannelHandlerContext var1) throws ProxyChannelHandler.Type_r {
      ByteBuf var2 = var1.alloc().buffer().writeBytes(new byte[]{5, 1, 0});
      if (this.g.isUnresolved()) {
         byte[] var4 = OAsByteArray(this.g.getHostString());
         if (var4.length > 255) {
            var2.release();
            throw new ProxyChannelHandler.Type_r(this.MAsString() + "the server name is too long for SOCKS5");
         }

         var2.writeByte(3).writeByte(var4.length).writeBytes(var4);
      } else if (this.g.getAddress() instanceof Inet6Address var3) {
         var2.writeByte(4).writeBytes(var3.getAddress());
      } else {
         var2.writeByte(1).writeBytes(this.g.getAddress().getAddress());
      }

      var2.writeShort(this.g.getPort());
      var1.writeAndFlush(var2);
   }

   private void tAsVoid(ChannelHandlerContext var1) {
      if (this.L != null) {
         this.L.cancel(false);
      }

      this.z = true;
      ByteBuf var2 = this.j;
      this.j = Unpooled.EMPTY_BUFFER;
      if (!this.r.trySuccess()) {
         var2.release();
         var1.close();
      } else {
         var1.fireChannelActive();
         this.q.removeAndWriteAll();
         if (this.E) {
            var1.flush();
         }

         if (var2.isReadable()) {
            var1.fireChannelRead(var2);
         } else {
            var2.release();
         }

         var1.pipeline().remove(this);
      }
   }

   private void KAsVoid(ChannelHandlerContext var1, Throwable var2) {
      if (!this.z && !this.x) {
         this.x = true;
         if (this.L != null) {
            this.L.cancel(false);
         }

         ProxyChannelHandler.Type_r var3 = var2 instanceof ProxyChannelHandler.Type_r var4
            ? var4
            : new ProxyChannelHandler.Type_r(this.MAsString() + "the proxy connection failed", var2);
         if (this.r != null) {
            this.r.tryFailure(var3);
         }

         if (!this.q.isEmpty()) {
            this.q.removeAndFailAll(var3);
         }

         var1.close();
      }
   }

   private int PAsInt() {
      int var1 = this.j.readerIndex();
      int var2 = this.j.writerIndex() - 3;

      for (int var3 = var1; var3 < var2; var3++) {
         if (this.j.getByte(var3) == 13 && this.j.getByte(var3 + 1) == 10 && this.j.getByte(var3 + 2) == 13 && this.j.getByte(var3 + 3) == 10) {
            return var3;
         }
      }

      return -1;
   }

   private String MAsString() {
      return "Proxy " + this.v.settings().label() + ": ";
   }

   private static byte[] OAsByteArray(String var0) throws ProxyChannelHandler.Type_r {
      if (!var0.matches("[A-Za-z0-9.-]{1,253}")) {
         throw new ProxyChannelHandler.Type_r("Proxy destinations must be ASCII host names");
      } else {
         return var0.getBytes(StandardCharsets.US_ASCII);
      }
   }

   static String WAsString(InetSocketAddress var0) {
      String var1 = var0.isUnresolved() ? var0.getHostString() : var0.getAddress().getHostAddress();
      return (var1.indexOf(58) >= 0 ? "[" + var1 + "]" : var1) + ":" + var0.getPort();
   }

   static String aAsString(int var0) {
      return switch (var0) {
         case 1 -> "the proxy failed";
         case 2 -> "the proxy does not allow this connection";
         case 3 -> "the proxy cannot reach that network";
         case 4 -> "the proxy cannot reach that server";
         case 5 -> "the server refused the connection through the proxy";
         case 6 -> "the proxy connection expired";
         case 7 -> "the proxy does not support connect requests";
         case 8 -> "the proxy does not support this address type";
         default -> "the proxy refused the connection (SOCKS5 reply " + var0 + ")";
      };
   }

   public static final class Type_r extends IOException {
      public Type_r(String var1) {
         super(var1);
      }

      public Type_r(String var1, Throwable var2) {
         super(var1, var2);
      }
   }
}
