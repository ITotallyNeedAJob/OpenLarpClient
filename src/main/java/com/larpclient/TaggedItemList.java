package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;

public final class TaggedItemList {
   public static final int v = 118;
   private final List<TaggedItemList.Type_v> G = new ArrayList<>();

   public TaggedItemList() {
   }

   public List<TaggedItemList.Type_v> tAsList() {
      return Collections.unmodifiableList(this.G);
   }

   public void eAsVoid() {
      this.G.clear();
   }

   public void TAsVoid(double var1, RandomGenerator var3) {
      double var4 = Math.clamp(var1 / 6.0, 0.0, 1.0);
      if (!(var1 <= 0.05)) {
         while (this.G.size() > 80) {
            this.G.removeFirst();
         }

         int var6 = 24 + (int)Math.round(var4 * 14.0);

         for (int var7 = 0; var7 < var6; var7++) {
            double var8 = var3.nextDouble(Math.PI * 2);
            double var10 = 2.0 * (var3.nextDouble(62.0, 118.0) + var4 * 48.0);
            double var12 = var3.nextDouble(0.0, 8.0);
            this.G
               .add(
                  new TaggedItemList.Type_v(
                     Math.cos(var8) * var12,
                     Math.sin(var8) * var12,
                     Math.cos(var8) * var10,
                     Math.sin(var8) * var10,
                     var3.nextDouble(8.4, 14.8),
                     var3.nextDouble(0.62, 0.92),
                     var3.nextBoolean()
                  )
               );
         }
      }
   }

   public void sAsVoid(double var1) {
      double var3 = Math.clamp(var1, 0.0, 0.25);

      while (var3 > 0.0) {
         double var5 = Math.min(0.008333333333333333, var3);

         for (TaggedItemList.Type_v var8 : this.G) {
            var8.T += var5;
            var8.U = var8.U + var8.t * var5;
            var8.R = var8.R + var8.v * var5;
            var8.t = var8.t * Math.max(0.0, 1.0 - 3.2 * var5);
            var8.v = var8.v * Math.max(0.0, 1.0 - 3.2 * var5) + 16.0 * var5;
         }

         var3 -= var5;
      }

      this.G.removeIf(var0 -> var0.T >= var0.a);
   }

   public static final class Type_v {
      public double U;
      public double R;
      public double t;
      public double v;
      public double T;
      public final double z;
      public final double a;
      public final boolean p;

      private Type_v(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13) {
         this.U = var1;
         this.R = var3;
         this.t = var5;
         this.v = var7;
         this.z = var9;
         this.a = var11;
         this.p = var13;
      }
   }
}
