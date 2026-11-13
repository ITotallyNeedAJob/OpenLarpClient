package com.larpclient;

public final class RodAttemptState {
   private RodAttemptState.Type_o J = RodAttemptState.Type_o.IDLE;
   private boolean A;
   private boolean S;
   private boolean e;
   private long j;
   private long V;
   private long z;
   private int H;

   public RodAttemptState() {
   }

   public RodAttemptState.Type_o QAsAn_Type_o() {
      return this.J;
   }

   public boolean yAsBoolean() {
      return this.J != RodAttemptState.Type_o.IDLE;
   }

   public void YAsVoid(long var1) {
      this.BAsVoid();
      this.e = true;
      this.z = var1 + 250000000L;
   }

   public boolean fAsBoolean(long var1) {
      if (this.e && var1 - this.z >= 0L) {
         this.e = false;
      }

      return !this.e;
   }

   public void kAsVoid(long var1) {
      if (!this.yAsBoolean()) {
         this.A = true;
         this.j = var1 + 250000000L;
      }
   }

   public boolean vAsBoolean(long var1) {
      if (this.A && var1 - this.j >= 0L) {
         this.A = false;
      }

      return this.A;
   }

   public void lAsVoid(long var1, long var3, boolean var5) {
      if (this.yAsBoolean()) {
         throw new IllegalStateException("Rod attempt already active");
      } else {
         this.A = false;
         this.S = var5;
         this.V = var1 + var3;
         this.H = 0;
         this.J = RodAttemptState.Type_o.SPAWN;
      }
   }

   public RodAttemptState.Type_a bAsAn_Type_a(long var1, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      if (!this.yAsBoolean()) {
         return RodAttemptState.Type_a.WAIT;
      } else if (var1 - this.V < 0L && ++this.H < 60) {
         if (this.J == RodAttemptState.Type_o.SPAWN) {
            if (!var3) {
               return RodAttemptState.Type_a.WAIT;
            }

            this.J = RodAttemptState.Type_o.FLIGHT;
         }

         if (!var3 || var5) {
            return RodAttemptState.Type_a.RESTORE;
         } else if (this.J == RodAttemptState.Type_o.REMOVE) {
            return RodAttemptState.Type_a.WAIT;
         } else if (!var4) {
            return !var6 && !var7 ? RodAttemptState.Type_a.WAIT : RodAttemptState.Type_a.RESTORE;
         } else if (!this.S) {
            return RodAttemptState.Type_a.RESTORE;
         } else {
            this.J = RodAttemptState.Type_o.REEL;
            return RodAttemptState.Type_a.REEL;
         }
      } else {
         return RodAttemptState.Type_a.RESTORE;
      }
   }

   public void aAsVoid() {
      if (this.J != RodAttemptState.Type_o.REEL) {
         throw new IllegalStateException("No confirmed rod hook");
      } else {
         this.J = RodAttemptState.Type_o.REMOVE;
      }
   }

   public void BAsVoid() {
      this.J = RodAttemptState.Type_o.IDLE;
      this.A = false;
      this.e = false;
      this.H = 0;
   }

   public static enum Type_a {
      WAIT,
      RESTORE,
      REEL;

      private Type_a() {
      }
   }

   public static final class Type_g {
      private int l = -1;
      private int p;

      public Type_g() {
      }

      public void YAsVoid(int var1) {
         if (var1 == this.l) {
            this.p = 0;
         } else if (++this.p >= 10) {
            this.l = -1;
            this.p = 0;
         }
      }

      public boolean gAsBoolean(int var1) {
         return var1 >= 0 && var1 != this.l;
      }

      public void tAsVoid(int var1) {
         this.l = var1;
         this.p = 0;
      }

      public void oAsVoid() {
         this.l = -1;
         this.p = 0;
      }
   }

   public static enum Type_o {
      IDLE,
      SPAWN,
      FLIGHT,
      REEL,
      REMOVE;

      private Type_o() {
      }
   }
}
