package com.larpclient;

import java.util.Arrays;
import net.minecraft.util.math.Vec3d;

public final class CometOrbit {
   public static final int b = 3;
   public static final int d = 256;
   private static final double g = 0.008333333333333333;
   private final Vec3d[][] m = new Vec3d[3][256];
   private final double[] k = new double[256];
   private final Vec3d[] Z = new Vec3d[3];
   private int I;
   private int U;
   private double N = Double.NaN;
   private double O;
   private double w;
   private double J;
   private double P;

   public CometOrbit() {
   }

   public void aAsVoid() {
      this.I = this.U = 0;
      this.N = Double.NaN;
      this.w = 0.0;
      Arrays.fill(this.Z, null);

      for (Vec3d[] var4 : this.m) {
         Arrays.fill(var4, null);
      }
   }

   public void VAsVoid(double var1, double var3, double var5, double var7) {
      if (!Double.isNaN(this.N) && (var1 < this.N || var1 - this.N > 1.0)) {
         this.aAsVoid();
      }

      if (Double.isNaN(this.N)) {
         this.N = this.O = var1;
         this.P = var1;
         this.J = var5;
         this.pAsVoid(var1, this.w, var5);
      }

      double var9 = var1 - this.N;
      double var11 = this.w + var9 * var3 * Math.PI * 2.0;

      while (this.O + 0.008333333333333333 <= var1) {
         this.O += 0.008333333333333333;
         double var13 = var9 > 0.0 ? Math.clamp((this.O - this.N) / var9, 0.0, 1.0) : 1.0;
         this.pAsVoid(this.O, this.w + (var11 - this.w) * var13, this.J + (var5 - this.J) * var13);
      }

      this.w = var11;

      for (int var15 = 0; var15 < 3; var15++) {
         this.Z[var15] = KAsVec3d(this.w, var5, var15);
      }

      this.N = var1;

      for (this.J = var5; this.U > 0 && var1 - this.k[this.I] > var7; this.U--) {
         this.I = (this.I + 1) % 256;
      }
   }

   private void pAsVoid(double var1, double var3, double var5) {
      if (this.U == 256) {
         this.I = (this.I + 1) % 256;
         this.U--;
      }

      int var7 = (this.I + this.U++) % 256;
      this.k[var7] = var1;

      for (int var8 = 0; var8 < 3; var8++) {
         this.m[var8][var7] = KAsVec3d(var3, var5, var8);
      }
   }

   private static Vec3d KAsVec3d(double var0, double var2, int var4) {
      double var5 = (double)var4 * Math.PI * 2.0 / 3.0;
      double var7 = var0 + var5;
      return new Vec3d(Math.cos(var7) * var2, 0.5 + 0.5 * Math.sin(var0 * 0.65 + var5), Math.sin(var7) * var2);
   }

   public int rAsInt() {
      return this.U;
   }

   public double yAsDouble(double var1) {
      return Double.isNaN(this.N) ? 0.0 : FadeCurve.RAsDouble((var1 - this.P) / 0.15);
   }

   public Vec3d gAsVec3d(int var1) {
      return this.Z[var1];
   }

   public Vec3d bAsVec3d(int var1, int var2) {
      return this.m[var1][(this.I + var2) % 256];
   }

   public double wAsDouble(int var1) {
      return this.k[(this.I + var1) % 256];
   }

   public static double BAsDouble(double var0, double var2) {
      return Math.min(var0, var2 / 8.0);
   }

   public static Vec3d dAsVec3d(Vec3d var0, Vec3d var1, double var2, double var4) {
      double var6 = var4 * 3.2;
      return var1.add(var0.x, var6 + var0.y * Math.max(0.0, var2 - var6 * 2.0), var0.z);
   }
}
