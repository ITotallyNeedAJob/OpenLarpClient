package com.larpclient;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongPredicate;

public final class wl {
   private final Map<Long, wl.Type_v> q = new HashMap<>();
   private final Map<Long, wl.Type_v> DropletTrail = new HashMap<>();
   private final Map<Long, Long> SmoothInput = new HashMap<>();
   private final LinkedHashMap<Long, Long> C = new LinkedHashMap<>();
   public static final int w = 1024;
   private static final long S = 1200L;
   private static final long H = 6000L;

   public wl() {
   }

   public Map<Long, wl.Type_v> EAsMap() {
      return Map.copyOf(this.q);
   }

   public void GAsVoid(long var1) {
      this.t.remove(var1);
   }

   public Map<Long, wl.Type_v> CAsMap() {
      return Map.copyOf(this.l);
   }

   public int lAsInt() {
      return this.l.size();
   }

   public int wAsInt() {
      return this.t.size();
   }

   public int MAsInt() {
      return this.C.size();
   }

   public boolean iAsBoolean(long var1, long var3, long var5) {
      Long var7 = this.t.get(var1);
      return var7 == null || var3 - var7 >= var5;
   }

   public boolean PAsBoolean(long var1, wl.Type_v var3, boolean var4, long var5) {
      if (this.q.size() >= 1024 && !this.q.containsKey(var1)) {
         return false;
      } else {
         this.t.put(var1, var5);
         this.q.put(var1, var3);
         return this.jAsBoolean(var1, var3, var4, var5);
      }
   }

   public boolean jAsBoolean(long var1, wl.Type_v var3, boolean var4, long var5) {
      if (!this.q.containsKey(var1)) {
         return false;
      } else if (!var4) {
         this.l.remove(var1);
         return false;
      } else {
         int var7 = this.l.put(var1, var3) == null ? 1 : 0;
         Long var8 = this.C.get(var1);
         if (var7 != 0 && (var8 == null || var5 - var8 >= 1200L)) {
            this.C.remove(Long.valueOf(var1));
            this.C.put(Long.valueOf(var1), Long.valueOf(var5));

            while (this.C.size() > 1024) {
               this.C.remove(this.C.keySet().iterator().next());
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public void rAsVoid(long var1) {
      this.l.remove(var1);
      this.q.remove(var1);
      this.t.remove(var1);
   }

   public void JAsVoid(LongPredicate var1, long var2) {
      this.q.keySet().removeIf(var1xx -> !var1.test(var1xx));
      this.l.keySet().removeIf(var1xx -> !var1.test(var1xx));
      this.t.keySet().removeIf(var1xx -> !var1.test(var1xx));
      this.C.values().removeIf(var2xx -> var2 - var2xx >= 6000L);
   }

   public void EAsVoid() {
      this.q.clear();
      this.l.clear();
      this.t.clear();
      this.C.clear();
   }

   public static record Type_g(int x, int y, int z, boolean trial, String name) {
   }

   public static record Type_v(int mature, int buds, int spawners, int sampleY, ae base, GrowthClueSummary sus, List<wl.Type_g> hits) {
      public Type_v(int mature, int buds, int spawners, int sampleY, ae base, GrowthClueSummary sus, List<wl.Type_g> hits) {
         hits = List.copyOf(hits);
         this.mature = mature;
         this.buds = buds;
         this.spawners = spawners;
         this.sampleY = sampleY;
         this.base = base;
         this.sus = sus;
         this.hits = hits;
      }

      public Type_v(int var1, int var2, int var3, int var4, ae var5) {
         this(var1, var2, var3, var4, var5, GrowthClueSummary.EMPTY, List.of());
      }

      public Type_v(int var1, int var2, int var3, int var4) {
         this(var1, var2, var3, var4, ae.EMPTY);
      }

      public boolean matureAmethyst(int var1, int var2) {
         long var3 = (long)this.mature + (long)this.buds;
         return this.mature >= var1 && var3 > 0L && (long)this.mature * 100L >= var3 * (long)var2;
      }

      public int maturePercent() {
         long var1 = (long)this.mature + (long)this.buds;
         return var1 == 0L ? 0 : (int)((long)this.mature * 100L / var1);
      }
   }
}
