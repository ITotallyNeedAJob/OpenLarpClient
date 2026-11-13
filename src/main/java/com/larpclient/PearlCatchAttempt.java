package com.larpclient;

public final class PearlCatchAttempt {
   public static final int q = 30;
   private PearlCatchAttempt.Type_w L = PearlCatchAttempt.Type_w.IDLE;
   private boolean X;
   private long O;
   private long P;
   private long W = -1L;

   public PearlCatchAttempt() {
   }

   public PearlCatchAttempt.Type_w jAsWc_Type_w() {
      return this.L;
   }

   public boolean kAsBoolean() {
      return this.L != PearlCatchAttempt.Type_w.IDLE;
   }

   public boolean xAsBoolean() {
      if (this.kAsBoolean()) {
         return false;
      } else {
         this.X = true;
         return true;
      }
   }

   public boolean HAsBoolean() {
      boolean var1 = this.X;
      this.X = false;
      return var1;
   }

   public void vAsVoid(long var1) {
      if (this.W < 0L) {
         this.W = var1;
      }
   }

   public boolean lAsBoolean() {
      return this.W >= 0L;
   }

   public boolean uAsBoolean(long var1) {
      return this.W >= 0L && var1 - this.W > 30L;
   }

   public void nAsVoid() {
      this.W = -1L;
   }

   public void vAsVoid(long var1, int var3) {
      if (this.kAsBoolean()) {
         throw new IllegalStateException("Pearl catch already active");
      } else {
         this.X = false;
         this.W = -1L;
         this.L = PearlCatchAttempt.Type_w.PEARL;
         this.O = var1 + (long)var3;
      }
   }

   public void jAsVoid(long var1, int var3) {
      if (this.L != PearlCatchAttempt.Type_w.PEARL) {
         throw new IllegalStateException("No pearl throw pending");
      } else {
         this.L = PearlCatchAttempt.Type_w.CHARGE;
         this.P = var1;
         this.O = var1 + (long)var3;
      }
   }

   public void iAsVoid(long var1, int var3) {
      if (this.L != PearlCatchAttempt.Type_w.CHARGE) {
         throw new IllegalStateException("No wind charge pending");
      } else {
         this.O = var1 + (long)var3;
      }
   }

   public void vAsVoid() {
      if (this.L != PearlCatchAttempt.Type_w.CHARGE) {
         throw new IllegalStateException("No wind charge pending");
      } else {
         this.L = PearlCatchAttempt.Type_w.IDLE;
      }
   }

   public long XAsLong(long var1) {
      return this.L == PearlCatchAttempt.Type_w.CHARGE ? var1 - this.P : -1L;
   }

   public boolean CAsBoolean(long var1) {
      return this.kAsBoolean() && var1 - this.O >= 0L;
   }

   public void TAsVoid() {
      this.L = PearlCatchAttempt.Type_w.IDLE;
      this.X = false;
      this.W = -1L;
   }

   public static enum Type_w {
      IDLE,
      PEARL,
      CHARGE;

      private Type_w() {
      }
   }
}
