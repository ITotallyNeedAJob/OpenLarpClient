package com.larpclient;

public final class QueueSort {
   private Object N;
   private Object q;
   private boolean A;

   public QueueSort() {
   }

   public boolean qAsBoolean(Object var1, Object var2, boolean var3) {
      int var4 = this.N == var1 && this.q == var2 && this.A == var3 ? 0 : 1;
      this.N = var1;
      this.q = var2;
      this.A = var3;
      return var4 != 0;
   }
}
