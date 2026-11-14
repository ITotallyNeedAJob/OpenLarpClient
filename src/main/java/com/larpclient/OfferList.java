package com.larpclient;

import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public final class OfferList {
   private Module[] h = new Module[0];
   private boolean[] H = new boolean[0];
   private Object X;
   private int T;
   private int e;
   private List<OfferList.Type_x> O = List.of();

   public OfferList() {
   }

   public List<OfferList.Type_x> KAsList(List<Module> var1, Object var2, int var3, int var4, ToIntFunction<String> var5) {
      int var6 = this.X == var2 && this.T == var3 && this.e == var4 && this.h.length == var1.size() ? 0 : 1;
      if (var6 == 0) {
         for (int var7 = 0; var7 < this.h.length; var7++) {
            Module var8 = var1.get(var7);
            if (this.h[var7] != var8 || this.H[var7] != var8.isEnabled()) {
               var6 = 1;
               break;
            }
         }
      }

      if (var6 == 0) {
         return this.O;
      } else {
         Module[] var12 = var1.toArray(Module[]::new);
         boolean[] var13 = new boolean[var12.length];
         ArrayList var9 = new ArrayList();

         for (int var10 = 0; var10 < var12.length; var10++) {
            Module var11 = var12[var10];
            var13[var10] = var11.isEnabled();
            if (var13[var10]) {
               var9.add(new OfferList.Type_x(var11.getDisplayName(), var5.applyAsInt(var11.getDisplayName())));
            }
         }

         var9.sort(Comparator.comparingInt(OfferList.Type_x::width).reversed().thenComparing(OfferList.Type_x::name));
         this.O = List.copyOf(var9);
         this.h = var12;
         this.H = var13;
         this.X = var2;
         this.T = var3;
         this.e = var4;
         return this.O;
      }
   }

   public static record Type_x(String name, int width) {
   }
}
