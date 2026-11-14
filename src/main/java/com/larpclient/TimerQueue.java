package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class TimerQueue {
   private static final List<TimerQueue.Type_q> O = new ArrayList<>();

   private TimerQueue() {
   }

   public static long DAsLong() {
      return 250L + ThreadLocalRandom.current().nextLong(500L);
   }

   public static void UAsVoid(long var0, Runnable var2) {
      O.add(new TimerQueue.Type_q(System.currentTimeMillis() + Math.max(0L, var0), var2));
   }

   public static void eAsVoid() {
      O.clear();
   }

   public static void tAsVoid() {
      if (!O.isEmpty()) {
         long var0 = System.currentTimeMillis();
         ArrayList var2 = new ArrayList();
         O.removeIf(var3 -> {
            if (var3.at <= var0) {
               var2.add(var3);
               return true;
            } else {
               return false;
            }
         });

         for (TimerQueue.Type_q var4 : (Iterable<TimerQueue.Type_q>) (var2)) {
            var4.action.run();
         }
      }
   }

   private static record Type_q(long at, Runnable action) {
   }
}
