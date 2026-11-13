package com.larpclient;

import java.util.IdentityHashMap;
import java.util.Map.Entry;
import java.util.function.Predicate;

public final class wm {
   public static final int O = 64;
   public static final double D = 3.0;
   private final IdentityHashMap<Object, Double> Z = new IdentityHashMap<>();
   private final IdentityHashMap<Object, Double> y = new IdentityHashMap<>();

   public wm() {
   }

   public void SAsVoid(Object var1, boolean var2, double var3) {
      this.JAsVoid(var3, var0 -> true);
      if (!var2) {
         this.Z.remove(var1);
      } else {
         if (this.Z.size() >= 64 && !this.Z.containsKey(var1)) {
            sAsVoid(this.Z);
         }

         this.Z.put(var1, var3);
      }
   }

   public boolean VAsBoolean(Object var1, double var2) {
      this.JAsVoid(var2, var0 -> true);
      Double var4 = this.Z.remove(var1);
      if (this.y.containsKey(var1)) {
         return false;
      } else {
         if (this.y.size() >= 64) {
            sAsVoid(this.y);
         }

         this.y.put(var1, var2);
         return var4 != null && var2 >= var4 && var2 - var4 <= 3.0;
      }
   }

   public void UAsVoid(Object var1) {
      this.Z.remove(var1);
   }

   public void JAsVoid(double var1, Predicate<Object> var3) {
      this.Z.entrySet().removeIf(var3xx -> var1 < var3xx.getValue() || var1 - var3xx.getValue() > 3.0 || !var3.test(var3xx.getKey()));
      this.y.entrySet().removeIf(var3xx -> var1 < var3xx.getValue() || var1 - var3xx.getValue() > 12.0 || !var3.test(var3xx.getKey()));
   }

   public int UAsInt() {
      return this.Z.size();
   }

   public int uAsInt() {
      return this.y.size();
   }

   public void vAsVoid() {
      this.Z.clear();
      this.y.clear();
   }

   private static void sAsVoid(IdentityHashMap<Object, Double> var0) {
      Object var1 = null;
      double var2 = Double.POSITIVE_INFINITY;

      for (Entry var5 : var0.entrySet()) {
         if ((Double)var5.getValue() < var2) {
            var1 = var5.getKey();
            var2 = (Double)var5.getValue();
         }
      }

      var0.remove(var1);
   }
}
