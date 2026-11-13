package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.util.math.ChunkPos;

public final class FindingZoneBounds {
   public static List<FindingZoneBounds.Type_c> HAsList(Set<Long> var0, int var1) {
      if (var1 >= 0 && var1 <= 4 && var0.size() <= 1024) {
         TreeSet var2 = new TreeSet<>(var0);
         ArrayList var3 = new ArrayList();

         while (!var2.isEmpty()) {
            HashSet var4 = new HashSet();
            ArrayDeque var5 = new ArrayDeque();
            long var6 = (Long)var2.first();
            var2.remove(var6);
            var5.add(var6);
            double var8 = 0.0;
            double var10 = 0.0;

            while (!var5.isEmpty()) {
               long var12 = (Long)var5.removeFirst();
               var4.add(var12);
               ChunkPos var14 = new ChunkPos(var12);
               var8 += (double)var14.getStartX() + 8.0;
               var10 += (double)var14.getStartZ() + 8.0;

               for (int var15 = -var1; var15 <= var1; var15++) {
                  for (int var16 = -var1; var16 <= var1; var16++) {
                     long var17 = ChunkPos.toLong(var14.x + var15, var14.z + var16);
                     if (var2.remove(var17)) {
                        var5.addLast(var17);
                     }
                  }
               }
            }

            var3.add(new FindingZoneBounds.Type_c(Set.copyOf(var4), var8 / (double)var4.size(), var10 / (double)var4.size()));
         }

         return List.copyOf(var3);
      } else {
         throw new IllegalArgumentException("Invalid finding zone bounds");
      }
   }

   private FindingZoneBounds() {
   }

   public static record Type_c(Set<Long> members, double x, double z) {
   }
}
