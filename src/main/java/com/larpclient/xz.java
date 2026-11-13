package com.larpclient;

import net.minecraft.util.math.Box;

public final class xz {
   private Box e;
   private Box u;
   private double r;
   private double f;
   private double J;
   private double Z;
   private double n;

   public xz() {
   }

   public void kAsVoid() {
      this.e = this.u = null;
      this.J = this.Z = 0.0;
   }

   public xz.Type_n QAsXz_Type_n(double var1) {
      if (this.u == null) {
         return new xz.Type_n(null, 0.0);
      } else {
         double var3 = MAsDouble(this.f <= 0.0 ? 1.0 : (var1 - this.r) / this.f);
         double var5 = MAsDouble((var1 - this.n) / 0.1);
         return new xz.Type_n(
            new Box(
               XAsDouble(this.e.minX, this.u.minX, var3),
               XAsDouble(this.e.minY, this.u.minY, var3),
               XAsDouble(this.e.minZ, this.u.minZ, var3),
               XAsDouble(this.e.maxX, this.u.maxX, var3),
               XAsDouble(this.e.maxY, this.u.maxY, var3),
               XAsDouble(this.e.maxZ, this.u.maxZ, var3)
            ),
            XAsDouble(this.J, this.Z, var5)
         );
      }
   }

   public xz.Type_n wAsXz_Type_n(Box var1, double var2, double var4) {
      xz.Type_n var6 = this.QAsXz_Type_n(var2);
      if (var1 != null && !var1.equals(this.u)) {
         int var7 = var6.bounds != null && !(var6.alpha < 0.001) && !(var1.getCenter().distanceTo(var6.bounds.getCenter()) > 8.0) ? 0 : 1;
         this.e = var7 != 0 ? var1 : var6.bounds;
         this.u = var1;
         this.r = var2;
         this.f = Math.max(0.0, var4);
         if (var7 != 0) {
            this.J = 0.0;
            this.Z = 1.0;
            this.n = var2;
         }
      }

      double var9 = var1 == null ? 0.0 : 1.0;
      if (var9 != this.Z) {
         this.J = var6.alpha;
         this.Z = var9;
         this.n = var2;
      }

      return this.QAsXz_Type_n(var2);
   }

   private static double MAsDouble(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   private static double XAsDouble(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public static record Type_n(Box bounds, double alpha) {
   }
}
