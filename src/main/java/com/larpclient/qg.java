package com.larpclient;

public final class qg implements AutoCloseable {
   private static final ThreadLocal<Double> t = ThreadLocal.withInitial(() -> 1.0);
   private final double U = t.get();
   private boolean s;

   private qg(double var1) {
      t.set(this.U * Math.clamp(var1, 0.0, 1.0));
   }

   public static qg nAsQg(double var0) {
      return new qg(var0);
   }

   public static int iAsInt(int var0) {
      return bg.pAsInt(var0, t.get());
   }

   @Override
   public void close() {
      if (!this.s) {
         t.set(this.U);
         this.s = true;
      }
   }
}
