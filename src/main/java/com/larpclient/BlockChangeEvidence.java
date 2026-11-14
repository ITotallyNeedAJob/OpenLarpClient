package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public final class BlockChangeEvidence {
   public static final int I = 2048;
   public static final int o = 2048;
   public static final int N = 64;
   public static final long B = 600L;
   private final LinkedHashMap<Long, BlockChangeEvidence.Type_t> f = new LinkedHashMap<>();
   private final ArrayDeque<BlockChangeEvidence.Type_i> J = new ArrayDeque<>();
   private long g = Long.MIN_VALUE;
   private int W;
   private long G;
   private long R;
   private long E;
   private long C;
   private long U;
   private long y;
   private long A;
   private long j;
   private long x;

   public BlockChangeEvidence() {
   }

   public void EAsVoid() {
      this.f.clear();
      this.J.clear();
      this.g = Long.MIN_VALUE;
      this.W = 0;
      this.G = this.R = this.E = this.C = this.U = this.y = this.A = this.j = this.x = 0L;
   }

   public void TAsVoid() {
      this.G++;
   }

   public boolean jAsBoolean(long var1) {
      if (this.g != var1) {
         this.g = var1;
         this.W = 0;
      }

      return this.W < 2048;
   }

   public void qAsVoid(BlockChangeEvidence.Type_k var1, long var2) {
      this.y = this.y + Math.max(0L, var2);
      if (var1 == BlockChangeEvidence.Type_k.UPDATE) {
         this.R = this.R + Math.max(0L, var2);
      }
   }

   private void cAsVoid(BlockChangeEvidence.Type_i var1) {
      if (this.J.size() == 64) {
         this.J.removeFirst();
         this.x++;
      }

      this.J.addLast(var1);
   }

   public void ZAsVoid(long var1, long var3, String var5, boolean var6, boolean var7, BlockChangeEvidence.Type_k var8, long var9) {
      if (var8 == BlockChangeEvidence.Type_k.UPDATE) {
         this.R++;
      }

      if (!this.jAsBoolean(var9)) {
         this.y++;
      } else {
         this.W++;
         BlockChangeEvidence.Type_t var11 = this.f.get(var1);
         if (var11 != null && var9 - var11.tick() >= 600L) {
            this.f.remove(Long.valueOf(var1));
            this.j++;
            var11 = null;
         }

         if (var11 != null || var6) {
            if (var6) {
               this.E++;
            }

            if (var11 == null && var6) {
               this.cAsVoid(new BlockChangeEvidence.Type_i(var1, var3, "<not previously observed>", var5, "identifying state received", var8, var9));
            }

            if (var11 != null && !var11.identity().equals(var5)) {
               String var12;
               if (var11.clue() && !var6) {
                  if (var7) {
                     this.C++;
                     var12 = "clue to terrain, cause unknown";
                  } else {
                     this.U++;
                     var12 = "clue replaced, cause unknown";
                  }
               } else if (var6) {
                  var12 = var11.clue() ? "identifying state changed" : "identifying state received again";
               } else {
                  var12 = null;
               }

               if (var12 != null) {
                  this.cAsVoid(new BlockChangeEvidence.Type_i(var1, var3, var11.identity(), var5, var12, var8, var9));
               }
            }

            if (var11 == null && this.f.size() == 2048) {
               this.f.remove(this.f.keySet().iterator().next());
               this.A++;
            }

            this.f.put(Long.valueOf(var1), new BlockChangeEvidence.Type_t(var3, var5, var6, var9));
         }
      }
   }

   public void kAsVoid(long var1) {
      this.f.values().removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         } else {
            this.j++;
            return true;
         }
      });
      this.J.removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         } else {
            this.x++;
            return true;
         }
      });
   }

   public void NAsVoid(long var1) {
      this.f.values().removeIf(var2 -> var2.chunk() == var1);
      this.J.removeIf(var2 -> var2.chunk() == var1);
   }

   public List<Long> gAsList(long var1) {
      return this.f.entrySet().stream().filter(var2 -> var2.getValue().chunk() == var1).map(Entry::getKey).toList();
   }

   public List<BlockChangeEvidence.Type_i> jAsList(int var1) {
      ArrayList var2 = new ArrayList();

      for (int var3 = 0; var3 < Math.max(0, var1) && !this.J.isEmpty(); var3++) {
         var2.add(this.J.removeFirst());
      }

      return List.copyOf(var2);
   }

   public BlockChangeEvidence.Type_y hAsXj_Type_y() {
      return new BlockChangeEvidence.Type_y(this.G, this.R, this.E, this.C, this.U, this.y, this.A, this.j, this.x, this.f.size());
   }

   public static record Type_i(long position, long chunk, String before, String after, String transition, BlockChangeEvidence.Type_k source, long tick) {
   }

   public static enum Type_k {
      CHUNK,
      UPDATE;

      private Type_k() {
      }
   }

   private static record Type_t(long chunk, String identity, boolean clue, long tick) {
   }

   public static record Type_y(
      long chunks,
      long updates,
      long clues,
      long terrainReplacements,
      long otherReplacements,
      long budgetDrops,
      long evictions,
      long expired,
      long omittedDetails,
      int tracked
   ) {
   }
}
