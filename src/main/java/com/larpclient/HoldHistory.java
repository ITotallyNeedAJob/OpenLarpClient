package com.larpclient;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class HoldHistory {
   private final ArrayDeque<Runnable> S = new ArrayDeque<>();
   private final int E;
   private long Q;
   private boolean I;
   private boolean p;

   public HoldHistory(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      } else {
         this.E = var1;
      }
   }

   public void JAsVoid(long var1, long var3) {
      if (!this.I && !this.p && this.S.isEmpty() && var3 >= 0L) {
         this.Q = var1 + var3;
         this.I = true;
      } else {
         throw new IllegalStateException("Invalid hold start");
      }
   }

   public boolean aAsBoolean(Runnable var1) {
      if (this.I && !this.p && this.S.size() < this.E) {
         this.S.addLast(Objects.requireNonNull(var1));
         return true;
      } else {
         return false;
      }
   }

   public boolean eAsBoolean(long var1) {
      return this.I && var1 - this.Q >= 0L;
   }

   public boolean UAsBoolean() {
      return this.I;
   }

   public boolean VAsBoolean() {
      return this.p;
   }

   public int NAsInt() {
      return this.S.size();
   }

   public void IAsVoid(BooleanSupplier var1) {
      if (!this.p) {
         this.I = false;
         this.p = true;

         try {
            while (!this.S.isEmpty() && var1.getAsBoolean()) {
               this.S.removeFirst().run();
            }
         } finally {
            this.S.clear();
            this.p = false;
            this.Q = 0L;
         }
      }
   }

   public void xAsVoid() {
      this.S.clear();
      this.I = false;
      this.Q = 0L;
   }
}
