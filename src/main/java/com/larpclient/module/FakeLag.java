package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.DurationHistory;
import io.netty.channel.ChannelFutureListener;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientTickEndC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public final class FakeLag extends Module {
   private static FakeLag x;
   public static final int V = 256;
   private static final int T = 10;
   private static final double R = 1.0E-6;
   private static final long E = 150000000L;
   private final NumberSetting delay = this.addSetting(
      new NumberSetting("Delay", "Delay native movement and its input state in milliseconds", 200.0, 0.0, 1000.0, 10.0)
   );
   private final BooleanSetting grimCompatibility = this.addSetting(new BooleanSetting("Grim Compatibility", "Delay pong replies with movement", false));
   private final DurationHistory u = new DurationHistory(256);
   private ClientPlayNetworkHandler h;
   private ClientWorld t;
   private ClientPlayerEntity M;
   private int s;
   private boolean o;
   private long K;
   private static ClientPlayerEntity j;
   private static ClientWorld U;
   private static ClientPlayNetworkHandler P;
   private boolean W;
   private int e;
   private boolean f;
   private boolean g;
   private int Q;
   private int C;
   private long d;

   public FakeLag() {
      super("Fake Lag", "Delay native movement updates", Category.PLAYER, -1);
      x = this;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   @Override
   public String getStatus() {
      return !this.isRuntimeActive()
         ? "Inactive"
         : (
            this.delay.getValue() == 0.0
               ? "No delay"
               : (this.g ? "Recovery: stand still" : (this.e < 2 ? "Settling" : (this.W ? "Resyncing" : this.u.yAsInt() + " held")))
         );
   }

   public static boolean JAsBoolean() {
      return x != null && x.isEnabled() ? x != null && x.isRuntimeActive() : false;
   }

   public static boolean HAsBoolean(Packet<?> var0) {
      return var0 instanceof PlayerMoveC2SPacket || var0 instanceof PlayerInputC2SPacket || var0 instanceof ClientCommandC2SPacket;
   }

   public static boolean AAsBoolean(Packet<?> var0) {
      return !HAsBoolean(var0) && !(var0 instanceof ClientTickEndC2SPacket) && !(var0 instanceof KeepAliveC2SPacket) && !(var0 instanceof CommonPongC2SPacket);
   }

   @Override
   public void onEnable() {
      this.u.gAsVoid();
      this.C = 0;
      this.f = false;
      this.g = false;
      this.Q = 0;
      this.h = mc.getNetworkHandler();
      this.t = mc.world;
      this.M = mc.player;
      this.s = this.delay.getValue().intValue();
      this.o = this.grimCompatibility.getValue();
      this.e = 2;
      this.W = j == this.M && U == this.t && P == this.h;
      this.d = System.nanoTime();
      ClientLog.GAsVoid(
         "FakeLag", "Enabled; outgoing delay={} ms, compatibility={}, capacity={}; native packet values and FIFO order retained", this.s, this.o, 256
      );
   }

   private boolean eAsBoolean() {
      return this.h != null && mc != null && mc.getNetworkHandler() == this.h && this.h.getConnection().isOpen();
   }

   private boolean UAsBoolean() {
      return this.eAsBoolean() && mc.world == this.t && mc.player == this.M && this.M != null && this.M.isAlive();
   }

   @Override
   public void onDisable() {
      try {
         if (this.UAsBoolean()) {
            int var1 = this.u.yAsInt();
            int var2 = this.C;
            this.QAsVoid("Stopped; preserve live trajectory");
            if (var1 > 0) {
               ClientLog.GAsVoid(
                  "FakeLag", "Stopped; released {} queued packets including {} movement reports in FIFO order; no new position resend", var1, var2
               );
            }
         } else {
            this.nAsVoid("Stopped after session change");
         }
      } finally {
         if (!this.UAsBoolean() && j == this.M) {
            hAsVoid();
         }

         this.u.gAsVoid();
         this.C = 0;
         this.h = null;
         this.t = null;
         this.M = null;
         this.W = false;
         this.e = 0;
         this.g = false;
         this.Q = 0;
      }
   }

   @Override
   public void onTick() {
      if (!this.UAsBoolean()) {
         this.QAsVoid("Player or world changed; retain live pong replies");
      } else {
         this.NAsVoid();
         if (this.g) {
            this.d = System.nanoTime();
            Vec3d var1 = this.M.getVelocity();
            double var4;
            if (!this.M.isOnGround() || ((var4 = var1.horizontalLengthSquared() - 1.0E-6) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1)) > 0) {
               this.Q = 0;
               return;
            }

            if (++this.Q < 10) {
               return;
            }

            this.g = false;
            this.e = 0;
            ClientLog.GAsVoid("FakeLag", "Correction recovery complete; resuming {} ms delay after grounded settling", this.s);
         }

         this.e = Math.min(2, this.e + 1);
         long var3 = System.nanoTime();
         this.mAsVoid(var3);
         this.u.zAsVoid(var3, false, this::eAsBoolean);
         if (this.u.yAsInt() == 0) {
            this.C = 0;
         }
      }
   }

   private void NAsVoid() {
      int var1 = this.delay.getValue().intValue();
      boolean var2 = this.grimCompatibility.getValue();
      if (var1 != this.s || var2 != this.o) {
         this.nAsVoid("Settings changed");
         this.s = var1;
         this.o = var2;
      }
   }

   private void QAsVoid(String var1) {
      int var2 = this.u.yAsInt();
      this.u.zAsVoid(System.nanoTime(), true, this::eAsBoolean);
      this.C = 0;
      if (var2 > 0) {
         ClientLog.XAsVoid("FakeLag", "Released or discarded {} held packets: {}", var2, var1);
      }
   }

   private void nAsVoid(String var1) {
      this.K++;
      if (this.C > 0 && this.UAsBoolean()) {
         j = this.M;
         U = this.t;
         P = this.h;
         this.W = true;
      }

      this.QAsVoid(var1);
   }

   private void mAsVoid(long var1) {
      if (var1 - this.d > 150000000L) {
         this.nAsVoid("Client stalled; resume native movement");
      }

      this.d = var1;
   }

   private static void hAsVoid() {
      j = null;
      U = null;
      P = null;
   }

   public static void AAsVoid() {
      hAsVoid();
   }

   public static boolean KAsBoolean(ClientPlayerEntity var0) {
      if (j == null) {
         return false;
      } else if (j == var0 && getMc() != null && getMc().player == var0 && getMc().world == U && getMc().getNetworkHandler() == P) {
         hAsVoid();
         return true;
      } else {
         hAsVoid();
         return false;
      }
   }

   public static boolean FAsBoolean(ClientConnection var0, Packet<?> var1, ChannelFutureListener var2, boolean var3) {
      if (x != null && x.isEnabled()) {
         FakeLag var4 = x;
         MinecraftClient var5 = getMc();
         if (var4 == null || var5 == null || !var5.isOnThread() || !var4.isRuntimeActive() || var4.u.YAsBoolean()) {
            return false;
         } else if (!var4.UAsBoolean()) {
            var4.QAsVoid("Player or world changed; retain live pong replies");
            return false;
         } else if (var0 != var4.h.getConnection()) {
            return false;
         } else {
            var4.NAsVoid();
            long var6 = System.nanoTime();
            var4.mAsVoid(var6);
            if (var4.g && var1 instanceof PlayerMoveC2SPacket var8 && !var8.isOnGround()) {
               var4.Q = 0;
            }

            if (!var4.g && var4.e >= 2 && !var4.W) {
               var4.u.zAsVoid(var6, false, var4::eAsBoolean);
               if (var4.u.yAsInt() == 0) {
                  var4.C = 0;
               }

               int var11 = var1 instanceof ClientTickEndC2SPacket || var4.o && var1 instanceof CommonPongC2SPacket ? 1 : 0;
               if (!HAsBoolean(var1) && var11 == 0) {
                  if (AAsBoolean(var1)) {
                     var4.nAsVoid("Interaction or state change");
                  }

                  return false;
               } else if (var4.s == 0) {
                  return false;
               } else {
                  long var9 = var4.K;
                  if (!var4.u.sAsBoolean(var6, (long)var4.s * 1000000L, () -> {
                     if (var1 instanceof PlayerMoveC2SPacket) {
                        var4.C = Math.max(0, var4.C - 1);
                     }

                     if (var1 instanceof CommonPongC2SPacket || var4.UAsBoolean() && (!(var1 instanceof PlayerMoveC2SPacket) || var9 == var4.K)) {
                        var0.send(var1, var2, var3);
                     }
                  })) {
                     var4.nAsVoid("Capacity reached; resume native movement");
                     return false;
                  } else {
                     if (var1 instanceof PlayerMoveC2SPacket) {
                        var4.C++;
                        if (!var4.f) {
                           var4.f = true;
                           ClientLog.GAsVoid(
                              "FakeLag", "Holding native movement at {} ms; input, sprint changes and airborne reports keep their original order", var4.s
                           );
                        }
                     }

                     return true;
                  }
               }
            } else {
               if (var1 instanceof ClientTickEndC2SPacket && j == null) {
                  var4.W = false;
               }

               return false;
            }
         }
      } else {
         return false;
      }
   }

   public static void QAsVoid(ClientPlayerEntity var0) {
      FakeLag var1 = x;
      if (var1 != null && var1.isRuntimeActive() && var0 == var1.M) {
         int var2 = !var1.g ? 1 : 0;
         var1.g = true;
         var1.Q = 0;
         var1.e = 0;
         var1.nAsVoid("Server position correction; discard rejected movement");
         var1.d = System.nanoTime();
         if (var2 != 0) {
            ClientLog.GAsVoid(
               "FakeLag",
               "Server position correction at {} ms delay; enabled preference retained, native packets pass until {} stationary grounded ticks",
               var1.s,
               10
            );
         }
      }
   }
}
