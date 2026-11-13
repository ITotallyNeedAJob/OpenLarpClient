package com.larpclient;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class DurationHistory {
   private final ArrayDeque<DurationHistory.Type_p> r = new ArrayDeque<>();
   private final int K;
   private boolean s;

   public DurationHistory(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      } else {
         this.K = var1;
      }
   }

   public boolean sAsBoolean(long var1, long var3, Runnable var5) {
      if (var3 < 0L) {
         throw new IllegalArgumentException("duration");
      } else if (!this.s && this.r.size() < this.K) {
         this.r.addLast(new DurationHistory.Type_p(var1 + var3, Objects.requireNonNull(var5)));
         return true;
      } else {
         return false;
      }
   }

   public int yAsInt() {
      return this.r.size();
   }

   public boolean YAsBoolean() {
      return this.s;
   }

   public void gAsVoid() {
      this.r.clear();
   }

   public void zAsVoid(long var1, boolean var3, BooleanSupplier var4) {
      if (!this.s) {
         this.s = true;

         try {
            while (!this.r.isEmpty() && var4.getAsBoolean() && (var3 || var1 - this.r.peekFirst().deadline() >= 0L)) {
               this.r.removeFirst().action().run();
            }

            if (!var4.getAsBoolean()) {
               this.r.clear();
            }
         } catch (Error | RuntimeException var9) {
            this.r.clear();
            throw var9;
         } finally {
            this.s = false;
         }
      }
   }

   private static record Type_p(long deadline, Runnable action) {
   }
}
