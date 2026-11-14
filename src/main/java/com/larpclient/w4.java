package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.BlockPos;

public final class w4 {
   public static final int F = 4;
   public static final double N = 0.06;
   public static final double AuctionNotifier = 0.06;
   public static final double W = 0.18;
   private final List<w4.Type_b> q = new ArrayList<>(4);
   private double BoxUtils = Double.NaN;

   public w4() {
   }

   public void gAsVoid() {
      this.q.clear();
      this.c = Double.NaN;
   }

   public List<w4.Type_s> CAsList(BlockPos var1, double var2) {
      return this.ZAsList(var1, var2, 0.06, 0.18);
   }

   public List<w4.Type_s> ZAsList(BlockPos var1, double var2, double var4, double var6) {
      var4 = Math.clamp(var4, 0.0, 3.0);
      var6 = Math.clamp(var6, 0.01, 2.0);
      double var8 = var4 + var6;
      if (var2 < this.c || var2 - this.c > 1.0) {
         this.gAsVoid();
      }

      this.c = var2;
      this.q.removeIf(var4x -> var2 - var4x.w >= var8);
      if (var1 != null) {
         w4.Type_b var10 = this.q.stream().filter(var1xx -> var1xx.H.equals(var1)).findFirst().orElse(null);
         if (var10 == null) {
            if (this.q.size() == 4) {
               this.q.removeFirst();
            }

            this.q.add(new w4.Type_b(var1, var2));
         } else {
            if (var2 - var10.w > var4) {
               var10.l = xAsDouble(var10, var2, var4, var6);
               var10.O = var2;
            }

            var10.w = var2;
            this.q.remove(var10);
            this.q.add(var10);
         }
      }

      ArrayList var17 = new ArrayList(this.q.size());

      for (w4.Type_b var12 : this.q) {
         double var13 = xAsDouble(var12, var2, var4, var6);
         if (var13 > 0.001) {
            var17.add(new w4.Type_s(var12.H, var13));
         }
      }

      return List.copyOf(var17);
   }

   private static double xAsDouble(w4.Type_b var0, double var1, double var3, double var5) {
      return (var0.l + (1.0 - var0.l) * xAsDouble((var1 - var0.O) / 0.06)) * (1.0 - xAsDouble((var1 - var0.w - var3) / var5));
   }

   private static double xAsDouble(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static final class Type_b {
      final BlockPos H;
      double O;
      double l;
      double w;

      Type_b(BlockPos var1, double var2) {
         this.H = var1.toImmutable();
         this.O = this.w = var2;
      }
   }

   public static record Type_s(BlockPos pos, double alpha) {
   }
}
