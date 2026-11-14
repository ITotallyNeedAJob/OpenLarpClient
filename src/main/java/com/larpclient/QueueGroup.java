package com.larpclient;

public final class QueueGroup implements AutoCloseable {
   private static final ThreadLocal<Double> SmoothInput = ThreadLocal.withInitial(() -> 1.0);
   private final double U = SmoothInput.get();
   private boolean s;

   private QueueGroup(double var1) {
      SmoothInput.set(this.U * Math.clamp(var1, 0.0, 1.0));
   }

   public static qg nAsQg(double var0) {
      return new QueueGroup(var0);
   }

   public static int iAsInt(int var0) {
      return bg.pAsInt(var0, SmoothInput.get());
   }

   @Override
   public void close() {
      if (!this.s) {
         SmoothInput.set(this.U);
         this.s = true;
      }
   }
}
