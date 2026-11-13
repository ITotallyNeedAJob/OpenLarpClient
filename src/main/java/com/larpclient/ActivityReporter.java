package com.larpclient;

import com.google.gson.JsonObject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

public final class ActivityReporter {
   private static ActivityReporter V;
   private final ActivityQueueStore w;
   private final CompletableFuture<JsonObject> C;
   private m8 s;
   private long v;
   private long I;
   private long h = -1L;
   private Object J;
   private boolean E;

   private ActivityReporter() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      this.w = new ActivityQueueStore(
         FabricLoader.getInstance().getConfigDir().resolve("larp-client/activity.json"),
         var1xx -> var1.execute(() -> ClientLog.hAsVoid("Activity", var1xx, null))
      );
      this.C = this.w.gAsCompletableFuture();
   }

   public static void mAsVoid() {
      if (V == null) {
         V = new ActivityReporter();
      }
   }

   private static long RAsLong() {
      return System.nanoTime() / 1000000L;
   }

   public static void HAsVoid(MinecraftClient var0) {
      if (V != null) {
         V.WAsVoid(var0);
      }
   }

   private void WAsVoid(MinecraftClient var1) {
      if (!this.E && this.C.isDone()) {
         long var2 = RAsLong();
         if (this.s == null) {
            this.s = new m8(
               (var1x, var2x) -> this.w
                     .<CompletableFuture<JsonObject>>PAsCompletableFuture(
                        () -> {
                           CloudRequestContext var2xx = SessionManager.sAsXe();
                           if (!var2xx.CAsString().equals(var1x)) {
                              throw new AuthException(401, "UNAUTHENTICATED");
                           } else {
                              return PlatformService.x
                                 .o
                                 .xAsCompletableFuture(
                                    var2x == null ? "GET" : "POST", var2x == null ? "/api/activity/preferences" : "/api/activity", var2x, true, null, var2xx
                                 );
                           }
                        }
                     )
                     .thenCompose(var0 -> (CompletionStage<JsonObject>)var0),
               var0 -> ClientLog.XAsVoid("Activity", var0),
               this.C.join()
            );
         }

         if (var2 >= this.v) {
            this.v = var2 + 1000L;
            if (this.J != var1.world) {
               this.J = var1.world;
               this.s.JAsVoid();
            }

            ServerInfo var4 = var1.getCurrentServerEntry();
            String var5 = var1.player != null && var1.world != null && !var1.isInSingleplayer() && var4 != null ? var4.address : null;
            this.s.bAsVoid(SessionManager.MAsString(), var5, var5 != null && !var1.isPaused(), var2, System.currentTimeMillis());
            if (var2 >= this.I || this.h != this.s.eAsLong()) {
               this.h = this.s.eAsLong();
               this.I = var2 + 30000L;
               this.w.oAsVoid(this.s.fAsJsonObject());
            }
         }
      }
   }

   public static void pAsVoid(EntityDamageS2CPacket var0) {
      ActivityReporter var1 = V;
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var1 != null && var1.s != null && var2.world != null && var2.player != null) {
         if (var1.J != var2.world) {
            var1.J = var2.world;
            var1.s.JAsVoid();
         }

         if (var2.world.getEntityById(var0.entityId()) instanceof PlayerEntity var4 && var4 != var2.player) {
            var1.s.DAsVoid(var4.getUuid(), var0.sourceCauseId() == var2.player.getId(), RAsLong());
         }
      }
   }

   public static void qAsVoid(EntityStatusS2CPacket var0) {
      ActivityReporter var1 = V;
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var1 != null && var1.s != null && var2.world != null && var0.getStatus() == 3) {
         if (var1.J != var2.world) {
            var1.J = var2.world;
            var1.s.JAsVoid();
         }

         if (var0.getEntity(var2.world) instanceof PlayerEntity var3 && var3 != var2.player) {
            var1.s.bAsVoid(var3.getUuid(), RAsLong());
         }
      }
   }

   public static void bAsVoid() {
      ActivityReporter var0 = V;
      if (var0 != null && var0.s != null) {
         var0.s.qAsVoid();
         var0.w.oAsVoid(var0.s.fAsJsonObject());
         var0.J = null;
      }
   }

   public static void lAsVoid() {
      ActivityReporter var0 = V;
      if (var0 != null) {
         bAsVoid();
         var0.E = true;
         var0.w.close();
      }
   }
}
