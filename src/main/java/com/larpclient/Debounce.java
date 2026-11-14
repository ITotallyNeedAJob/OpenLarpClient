package com.larpclient;

public final class Debounce {
   private Object X;
   private long t;
   private long J;
   private long T = Long.MIN_VALUE;

   public Debounce() {
   }

   public boolean bAsBoolean(Object var1, long var2, int var4) {
      if (var1 == null) {
         this.KAsVoid();
         return false;
      } else {
         if (var1 != this.X || var2 < this.J) {
            this.X = var1;
            this.t = var2;
            this.T = Long.MIN_VALUE;
         }

         this.J = var2;
         return var2 != this.T && var2 - this.t >= (long)Math.max(0, var4);
      }
   }

   public void JAsVoid(Object var1, long var2) {
      if (var1 == this.X) {
         this.t = this.J = this.T = var2;
      }
   }

   public void KAsVoid() {
      this.X = null;
      this.t = this.J = 0L;
      this.T = Long.MIN_VALUE;
   }
}
