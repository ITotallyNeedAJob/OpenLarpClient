package com.larpclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.LongPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public final class we {
   public static final int i = 625;
   public static final int Q = 4096;
   private static final byte[] t = new byte[2048];
   private final LinkedHashMap<Long, byte[][]> c = new LinkedHashMap<>();
   private final LinkedHashMap<Long, we.Type_i> w = new LinkedHashMap<>();

   public we() {
   }

   public void aAsVoid() {
      this.c.clear();
      this.w.clear();
   }

   public int TAsInt() {
      return this.c.size();
   }

   public int RAsInt() {
      return this.w.size();
   }

   public boolean hAsBoolean(long var1) {
      return this.c.containsKey(Long.valueOf(var1));
   }

   public void SAsVoid(long var1) {
      this.c.remove(Long.valueOf(var1));
      this.w.values().removeIf(var2 -> ChunkPos.toLong(var2.pos().getX() >> 4, var2.pos().getZ() >> 4) == var1);
   }

   public void PAsVoid(LongPredicate var1) {
      this.c.keySet().removeIf(var1xx -> !var1.test(var1xx));
      this.w.values().removeIf(var1xx -> !var1.test(ChunkPos.toLong(var1xx.pos().getX() >> 4, var1xx.pos().getZ() >> 4)));
   }

   public void MAsVoid(int var1, int var2, int var3, BitSet var4, BitSet var5, List<byte[]> var6, boolean var7) {
      long var8 = ChunkPos.toLong(var1, var2);
      if (var7) {
         this.SAsVoid(var8);
      }

      byte[][] var10 = this.c.computeIfAbsent(Long.valueOf(var8), var0 -> new byte[4][]);

      while (this.c.size() > 625) {
         this.SAsVoid(this.c.keySet().iterator().next());
      }

      int var11 = 0;

      for (int var12 = var4.nextSetBit(0); var12 >= 0; var12 = var4.nextSetBit(var12 + 1)) {
         byte[] var13 = var11 < var6.size() ? var6.get(var11) : null;
         var11++;
         int var14 = var3 + var12;
         if (var14 >= -4 && var14 < 0 && var13 != null && var13.length == 2048) {
            this.qAsVoid(var10, var1, var2, var14, var13, !var7);
         }
      }

      for (int var15 = var5.nextSetBit(0); var15 >= 0; var15 = var5.nextSetBit(var15 + 1)) {
         int var16 = var3 + var15;
         if (var16 >= -4 && var16 < 0) {
            this.qAsVoid(var10, var1, var2, var16, t, !var7);
         }
      }
   }

   private void qAsVoid(byte[][] var1, int var2, int var3, int var4, byte[] var5, boolean var6) {
      byte[] var7 = var1[var4 + 4];
      if (var6 && var7 != null && !Arrays.equals(var7, var5)) {
         for (int var8 = 0; var8 < 4096; var8++) {
            int var9 = (var8 & 1) * 4;
            int var10 = var7[var8 >> 1] >> var9 & 15;
            int var11 = var5[var8 >> 1] >> var9 & 15;
            if (var10 != var11) {
               BlockPos var12 = new BlockPos((var2 << 4) + (var8 & 15), (var4 << 4) + (var8 >> 8), (var3 << 4) + (var8 >> 4 & 15));
               this.w.put(Long.valueOf(var12.asLong()), new we.Type_i(var12, var10, var11));
               if (this.w.size() > 4096) {
                  this.w.remove(this.w.keySet().iterator().next());
               }
            }
         }
      }

      var1[var4 + 4] = var5 == t ? t : (byte[])var5.clone();
   }

   public List<we.Type_i> cAsList(int var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.w.values().iterator();

      while (var2.size() < var1 && var3.hasNext()) {
         var2.add((we.Type_i)var3.next());
         var3.remove();
      }

      return List.copyOf(var2);
   }

   public static record Type_i(BlockPos pos, int before, int after) {
   }
}
