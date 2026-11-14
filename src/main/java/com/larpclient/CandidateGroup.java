package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.LongPredicate;
import net.minecraft.util.math.ChunkPos;

public final class CandidateGroup {
   public static final int M = 8192;
   private final Map<Long, CandidateGroup.Type_n[]> z = new LinkedHashMap<>();
   private final Set<Long> P = new HashSet<>();
   private final Set<Long> I = new HashSet<>();
   private final Set<Long> PetEffectRenderer = new HashSet<>();
   private int x;
   private int S;

   public CandidateGroup() {
   }

   public void VAsVoid() {
      this.z.clear();
      this.P.clear();
      this.I.clear();
      this.a.clear();
      this.x = 0;
   }

   public void CAsVoid(long var1) {
      this.z.remove(var1);
      this.P.remove(var1);
      this.I.remove(var1);
      this.a.remove(var1);
   }

   public static boolean wAsBoolean(CandidateGroup.Type_n[] var0, int var1, int var2) {
      for (int var3 = 1; var3 <= Math.min(var1, var0.length - 1); var3++) {
         int var4 = (var2 + var3 - 1) * 16;
         int var5 = var4 + 15;
         if (var5 >= -64 && var4 <= 184 && var5 < 62 && var0[var3] == CandidateGroup.Type_n.ZEROED_PRESENT) {
            return true;
         }
      }

      return false;
   }

   public boolean oAsBoolean(int var1, int var2, int var3, int var4, BitSet var5, BitSet var6, List<CandidateGroup.Type_n> var7) {
      if (var3 > 0 && var3 <= 256) {
         if (var3 != this.x || var4 != this.S) {
            this.VAsVoid();
            this.x = var3;
            this.S = var4;
         }

         long var8 = ChunkPos.toLong(var1, var2);
         int var10 = !this.z.containsKey(var8) && this.z.size() >= 8192 ? 1 : 0;
         if (var10 != 0) {
            this.CAsVoid(this.z.keySet().iterator().next());
         }

         CandidateGroup.Type_n[] var11 = this.z.computeIfAbsent(var8, var1x -> {
            CandidateGroup.Type_n[] var2x = new CandidateGroup.Type_n[this.x + 2];
            Arrays.fill(var2x, CandidateGroup.Type_n.UNKNOWN);
            return var2x;
         });
         int var12 = 0;

         for (int var13 = 0; var13 < var11.length; var13++) {
            if (var5 != null && var5.get(var13)) {
               var11[var13] = var12 < var7.size() ? var7.get(var12) : CandidateGroup.Type_n.UNKNOWN;
               var12++;
            } else if (var6 != null && var6.get(var13)) {
               var11[var13] = CandidateGroup.Type_n.EMPTY_MASK;
            }
         }

         int var14 = wAsBoolean(var11, this.x, this.S) && this.P.add(var8) ? 1 : 0;
         return var14 != 0 || var10 != 0;
      } else {
         return false;
      }
   }

   public Set<Long> xAsSet() {
      return Set.copyOf(this.P);
   }

   public Set<Long> jAsSet() {
      return Set.copyOf(this.a);
   }

   public int mAsInt() {
      return this.z.size();
   }

   public static CandidateGroup.Type_n eAsX0_Type_n(byte[] var0) {
      if (var0 != null && var0.length == 2048) {
         byte var1 = 1;
         byte var2 = 1;

         for (byte var6 : var0) {
            var1 = (byte)(var1 & (var6 == 0 ? 1 : 0));
            var2 = (byte)(var2 & (var6 == -1 ? 1 : 0));
            if (var1 == 0 && var2 == 0) {
               return CandidateGroup.Type_n.NORMAL;
            }
         }

         return var1 != 0 ? CandidateGroup.Type_n.ZEROED_PRESENT : CandidateGroup.Type_n.UNIFORM_MAX;
      } else {
         return CandidateGroup.Type_n.UNKNOWN;
      }
   }

   public static List<List<Long>> bAsList(Set<Long> var0) {
      TreeSet var1 = new TreeSet<>(var0);
      ArrayList var2 = new ArrayList();

      while (!var1.isEmpty()) {
         long var3 = (Long)var1.iterator().next();
         var1.remove(var3);
         ArrayDeque var5 = new ArrayDeque();
         var5.add(var3);
         ArrayList var6 = new ArrayList();

         while (!var5.isEmpty()) {
            long var7 = (Long)var5.removeFirst();
            var6.add(var7);
            ChunkPos var9 = new ChunkPos(var7);

            for (int var10 = -2; var10 <= 2; var10++) {
               for (int var11 = -2; var11 <= 2; var11++) {
                  if (var10 != 0 || var11 != 0) {
                     long var12 = ChunkPos.toLong(var9.x + var10, var9.z + var11);
                     if (var1.remove(var12)) {
                        var5.addLast(var12);
                     }
                  }
               }
            }
         }

         var2.add(List.copyOf(var6));
      }

      return List.copyOf(var2);
   }

   public static long JAsLong(List<Long> var0, int var1, int var2) {
      if (var0.isEmpty()) {
         throw new IllegalArgumentException("Empty candidate group");
      } else {
         return var0.stream().min(Comparator.<Long>comparingDouble(var2xx -> {
            ChunkPos var3 = new ChunkPos(var2xx);
            double var4 = (double)var3.x - (double)var1;
            double var6 = (double)var3.z - (double)var2;
            return var4 * var4 + var6 * var6;
         }).thenComparingLong(Long::longValue)).orElseThrow();
      }
   }

   public List<Long> RAsList(int var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();

      for (List var6 : bAsList(this.P)) {
         if (var6.size() >= var1) {
            boolean var7 = var6.stream().anyMatch(this.I::contains);
            this.I.addAll(var6);
            if (!var7) {
               long var8 = JAsLong(var6, var2, var3);
               if (this.a.add(var8)) {
                  var4.add(var8);
               }
            }
         }
      }

      return List.copyOf(var4);
   }

   public boolean TAsBoolean(LongPredicate var1) {
      byte var2 = 0;

      for (long var4 : List.copyOf(this.z.keySet())) {
         if (!var1.test(var4)) {
            this.CAsVoid(var4);
            var2 = 1;
         }
      }

      return var2 != 0;
   }

   public static enum Type_n {
      UNKNOWN,
      EMPTY_MASK,
      ZEROED_PRESENT,
      UNIFORM_MAX,
      NORMAL;

      private Type_n() {
      }
   }
}
