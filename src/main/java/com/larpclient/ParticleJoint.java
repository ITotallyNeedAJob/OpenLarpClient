package com.larpclient;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.function.LongConsumer;
import net.minecraft.server.network.ChunkFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;

public final class ParticleJoint {
   public static void MAsVoid(LongSet var0, BlockPos var1, int var2, int var3, int var4, LongConsumer var5) {
      int var6 = ChunkSectionPos.getSectionCoord(var1.getX());
      int var7 = ChunkSectionPos.getSectionCoord(var1.getZ());
      int var8 = Math.clamp((long)ChunkSectionPos.getSectionCoord(var1.getY()), var3, var4);
      LongIterator var9 = var0.iterator();

      while (var9.hasNext()) {
         long var10 = var9.nextLong();
         int var12 = ChunkPos.getPackedX(var10);
         int var13 = ChunkPos.getPackedZ(var10);
         if (ChunkFilter.isWithinDistanceExcludingEdge(var6, var7, var2, var12, var13)) {
            var5.accept(ChunkSectionPos.asLong(var12, var8, var13));
         }
      }
   }

   private ParticleJoint() {
   }
}
