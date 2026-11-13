package com.larpclient;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents.AfterClientWorldChange;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.LightData;
import net.minecraft.util.math.ChunkPos;

public final class ChunkLightCapture {
   private static final s q = new s();
   private static ClientWorld B;
   private static boolean l;

   public static void CAsVoid() {
      if (!l) {
         l = true;
         ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((AfterClientWorldChange)(var0, var1) -> HAsS(var1));
         ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var0, var1) -> {
            if (var0 == B) {
               q.GAsVoid(var1.getPos().toLong());
            }
         });
         ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var0, var1) -> {
            q.YAsVoid();
            B = null;
         });
      }
   }

   public static s HAsS(ClientWorld var0) {
      if (var0 != B) {
         q.YAsVoid();
         B = var0;
      }

      return q;
   }

   public static void RAsVoid(ClientWorld var0, int var1, int var2, LightData var3) {
      MinecraftClient var4 = MinecraftClient.getInstance();
      if (var0 != null && var4.world == var0 && var4.player != null && var4.isOnThread()) {
         try {
            ChunkPos var5 = var4.player.getChunkPos();
            s var6 = HAsS(var0);
            var6.UAsVoid(
               var2x -> {
                  ChunkPos var4x = new ChunkPos(var2x);
                  return Math.abs((long)var4x.x - (long)var5.x) <= 12L
                     && Math.abs((long)var4x.z - (long)var5.z) <= 12L
                     && var0.getChunkManager().getWorldChunk(var4x.x, var4x.z) != null;
               }
            );
            if (Math.abs((long)var1 - (long)var5.x) > 12L
               || Math.abs((long)var2 - (long)var5.z) > 12L
               || var0.getChunkManager().getWorldChunk(var1, var2) == null) {
               return;
            }

            var6.vAsVoid(var1, var2, (var0.getBottomY() >> 4) - 1, var3.getInitedBlock(), var3.getUninitedBlock(), var3.getBlockNibbles());
         } catch (RuntimeException var71) {
            ClientLog.hAsVoid("BaseFinding", "Received light capture failed; ordinary block scans remain available", var71);
         }
      }
   }

   private ChunkLightCapture() {
   }
}
