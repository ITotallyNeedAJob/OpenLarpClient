package com.larpclient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

final class dr<T> {
   private final Executor n;
   private final BooleanSupplier N;
   private CompletableFuture<T> X;

   dr(Executor var1, BooleanSupplier var2) {
      this.n = var1;
      this.N = var2;
   }

   boolean BAsBoolean(CompletableFuture<T> var1, BiConsumer<T, Throwable> var2) {
      if (this.X != null) {
         var1.cancel(true);
         return false;
      } else {
         this.X = var1;
         var1.whenComplete((var3, var4) -> this.n.execute(() -> {
               if (this.X == var1) {
                  this.X = null;
                  if (this.N.getAsBoolean()) {
                     var2.accept((T)var3, var4);
                  }
               }
            }));
         return true;
      }
   }

   void dAsVoid() {
      CompletableFuture var1 = this.X;
      this.X = null;
      if (var1 != null) {
         var1.cancel(true);
      }
   }
}
