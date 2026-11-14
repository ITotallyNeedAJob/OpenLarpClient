package com.larpclient;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class OpenScreen {
   private static final Map<String, Double> Z = new LinkedHashMap<>();
   private static final Map<String, Long> n = new LinkedHashMap<>();
   private static final Map<String, Long> AuctionNotifier = new LinkedHashMap<>();

   public OpenScreen() {
   }

   public static void YAsVoid(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         n.put(var0, var1);
      }
   }

   public static void FAsVoid(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         n.merge(var0, var1, Long::sum);
      }
   }

   public static void sAsVoid() {
      AuctionNotifier.clear();
      if (Boolean.getBoolean("larp.debug")) {
         AuctionNotifier.putAll(n);
      }

      n.clear();
   }

   public static Map<String, Long> gAsMap() {
      return Collections.unmodifiableMap(AuctionNotifier.isEmpty() ? n : o);
   }

   public static long RAsLong() {
      return Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
   }

   public static void aAsVoid(String var0, long var1) {
      if (var1 != 0L) {
         Z.merge(var0, (double)(System.nanoTime() - var1) / 1000000.0, (var0x, var1x) -> var0x * 0.9 + var1x * 0.1);
      }
   }

   public static Map<String, Double> pAsMap() {
      return Collections.unmodifiableMap(Z);
   }
}
