package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public final class FontCache {
   private final Map<FontCache.Type_c, Integer> L = lAsMap();
   private final Map<FontCache.Type_c, String> c = lAsMap();
   private Object O;
   private int q;

   public FontCache() {
   }

   private static <V> Map<FontCache.Type_c, V> lAsMap() {
      return new LinkedHashMap<FontCache.Type_c, V>(64, 0.75F, true) {
         @Override
         protected boolean removeEldestEntry(Entry<FontCache.Type_c, V> var1) {
            return this.size() > 512;
         }
      };
   }

   public void DAsVoid(Object var1, int var2) {
      if (this.O != var1 || this.q != var2) {
         this.L.clear();
         this.c.clear();
         this.O = var1;
         this.q = var2;
      }
   }

   public int aAsInt(String var1, int var2, IntSupplier var3) {
      return var1.length() > 256 ? var3.getAsInt() : this.L.computeIfAbsent(new FontCache.Type_c(var1, var2, 0), var1x -> var3.getAsInt());
   }

   public String nAsString(String var1, int var2, int var3, Supplier<String> var4) {
      return var1.length() > 256 ? var4.get() : this.c.computeIfAbsent(new FontCache.Type_c(var1, var2, var3), var1x -> var4.get());
   }

   int lAsInt() {
      return this.L.size();
   }

   int OAsInt() {
      return this.c.size();
   }

   private static record Type_c(String text, int style, int width) {
   }
}
