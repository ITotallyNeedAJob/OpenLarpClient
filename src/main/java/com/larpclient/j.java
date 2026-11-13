package com.larpclient;

import java.util.ArrayList;
import java.util.List;

public final class j {
   public static final int W = 24;
   public static final int v = 6;

   public static j.Type_u LAsJ_Type_u(int var0, List<Integer> var1) {
      int var2 = Math.max(1, var0);
      int var3 = 0;
      int var4 = 0;
      ArrayList var5 = new ArrayList();

      for (int var7 : var1) {
         int var8 = Math.clamp((long)var7, 1, var2);
         if (var3 > 0 && var3 + var8 > var2) {
            var3 = 0;
            var4 += 30;
         }

         var5.add(new j.Type_m(var3, var4, var8));
         var3 += var8 + 6;
      }

      return new j.Type_u(List.copyOf(var5), var5.isEmpty() ? 0 : var4 + 24);
   }

   private j() {
   }

   public static record Type_m(int x, int y, int width) {
   }

   public static record Type_u(List<j.Type_m> cells, int height) {
   }
}
