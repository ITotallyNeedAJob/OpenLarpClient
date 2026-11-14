package com.larpclient;

import java.util.HashMap;
import java.util.Random;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.Vec3d;

public final class SnowOptions {
   public static final int l = 3200;
   public static final int S = 8192;
   public static final int h = 128;
   private final double[] M = new double[3200];
   private final double[] F = new double[3200];
   private final double[] q = new double[3200];
   private final double[] O = new double[3200];
   private final double[] A = new double[3200];
   private final double[] J = new double[3200];
   private final double[] u = new double[3200];
   private final double[] G = new double[3200];
   private final double[] i = new double[3200];
   private final double[] b = new double[3200];
   private final double[] c = new double[3200];
   private final double[] a = new double[3200];
   private final double[] P = new double[3200];
   private final int[] o = new int[3200];
   private final HashMap<Long, SnowOptions.Type_r> V = new HashMap<>();
   private final Random X = new Random(1397641047L);
   private Vec3d n;
   private SnowOptions.Type_k T;
   private int D;
   private int C;
   private int g;
   private long B;
   private final double[] K = new double[3200];

   public SnowOptions() {
   }

   public void yAsVoid() {
      this.n = null;
      this.T = null;
      this.D = this.C = this.g = 0;
      this.B = 0L;
      this.V.clear();
      this.X.setSeed(1397641047L);
   }

   public int dAsInt() {
      return this.D;
   }

   public int cAsInt() {
      return this.C;
   }

   public int jAsInt() {
      return this.V.size();
   }

   public static long ZAsLong(int var0, int var1) {
      return (long)var0 << 32 ^ (long)var1 & 4294967295L;
   }

   private void KAsVoid(int var1, Vec3d var2, boolean var3) {
      double var4 = this.X.nextDouble() * Math.PI * 2.0;
      double var6 = Math.sqrt(this.X.nextDouble()) * this.T.radius();
      this.M[var1] = var2.x + Math.cos(var4) * var6;
      this.q[var1] = var2.z + Math.sin(var4) * var6;
      this.F[var1] = var2.y + (var3 ? this.X.nextDouble() * 24.0 - 6.0 : 16.0 + this.X.nextDouble() * 2.0);
      this.u[var1] = this.X.nextDouble() * Math.PI * 2.0;
      this.G[var1] = 0.8 + this.X.nextDouble() * 1.6;
      this.i[var1] = 0.018 + Math.pow(this.X.nextDouble(), 3.0) * 0.065;
      this.O[var1] = this.M[var1];
      this.A[var1] = this.F[var1];
      this.J[var1] = this.q[var1];
   }

   public void ZAsVoid(Vec3d var1, SnowOptions.Type_k var2, SnowOptions.Type_n var3) {
      if (this.n == null || var1.squaredDistanceTo(this.n) > var2.radius() * var2.radius() || this.T == null || this.T.radius() != var2.radius()) {
         this.yAsVoid();
      }

      this.T = var2;
      this.n = var1;
      this.B++;
      this.C = 0;
      int var4 = this.T.count();
      int var5 = this.D;
      this.D = var4;

      for (int var6 = var5; var6 < this.D; var6++) {
         this.KAsVoid(var6, var1, true);
      }

      double var16 = (double)this.B * 0.05;
      double var8 = 0.65 + 0.35 * Math.sin(var16 * 0.43);

      for (int var10 = 0; var10 < this.D; var10++) {
         this.O[var10] = this.M[var10];
         this.A[var10] = this.F[var10];
         this.J[var10] = this.q[var10];
         this.M[var10] = this.M[var10] + 0.05 * (this.T.wind() * 2.8 * var8 + Math.sin(var16 * 1.4 + this.u[var10]) * 0.22);
         this.q[var10] = this.q[var10] + 0.05 * (this.T.wind() * 0.9 * var8 + Math.cos(var16 * 0.9 + this.u[var10]) * 0.16);
         this.F[var10] = this.F[var10] - this.G[var10] * 0.05;
         SnowOptions.Type_r var11 = this.V.get(ZAsLong((int)Math.floor(this.M[var10]), (int)Math.floor(this.q[var10])));
         double var12 = this.M[var10] - var1.x;
         double var14 = this.q[var10] - var1.z;
         if (var12 * var12 + var14 * var14 > this.T.radius() * this.T.radius()
            || this.F[var10] < var1.y - 6.0
            || this.F[var10] > var1.y + 20.0
            || var11 != null && this.F[var10] <= var11.roof.height() + 0.16) {
            this.KAsVoid(var10, var1, false);
         }
      }

      if (this.V.size() >= 8192) {
         this.V.entrySet().removeIf(var1x -> this.B - var1x.getValue().tick() > 20L);
      }

      int var17 = 0;
      int var18 = 0;

      while (this.D > 0 && var18++ < this.D && var17 < 128) {
         int var19 = this.g++ % this.D;
         long var13 = ZAsLong((int)Math.floor(this.M[var19]), (int)Math.floor(this.q[var19]));
         SnowOptions.Type_r var15 = this.V.get(var13);
         if ((var15 == null || this.B - var15.tick() > 20L) && this.V.size() < 8192) {
            this.V.put(var13, new SnowOptions.Type_r(var3.pAsQp_Type_t((int)Math.floor(this.M[var19]), (int)Math.floor(this.q[var19])), this.B));
            var17++;
         }
      }

      if (this.B % 20L == 0L) {
         this.V.entrySet().removeIf(var1x -> this.B - var1x.getValue().tick() > 40L);
      }

      if (this.g > 320000) {
         this.g = this.g % Math.max(1, this.D);
      }
   }

   public void WAsVoid(Vec3d var1, float var2) {
      this.C = 0;
      if (this.n != null && this.T != null && !(var1.squaredDistanceTo(this.n) > this.T.radius() * this.T.radius())) {
         double var3 = (double)Math.clamp(var2, 0.0F, 1.0F);
         double var5 = ((double)(this.B - 1L) + var3) * 0.05;

         for (int var7 = 0; var7 < this.D; var7++) {
            double var8 = this.O[var7] + (this.M[var7] - this.O[var7]) * var3;
            double var10 = this.A[var7] + (this.F[var7] - this.A[var7]) * var3;
            double var12 = this.J[var7] + (this.q[var7] - this.J[var7]) * var3;
            SnowOptions.Type_r var14 = this.V.get(ZAsLong((int)Math.floor(var8), (int)Math.floor(var12)));
            if (var14 != null && this.B - var14.tick() <= 20L && !(var10 <= var14.roof.height() + 0.16)) {
               double var15 = Math.sqrt((var8 - var1.x) * (var8 - var1.x) + (var10 - var1.y) * (var10 - var1.y) + (var12 - var1.z) * (var12 - var1.z));
               double var17 = Math.clamp((this.T.radius() - var15) / (this.T.radius() * 0.2), 0.0, 1.0) * Math.clamp((var15 - 0.35) / 0.9, 0.0, 1.0);
               var17 *= Math.clamp((var10 - var14.roof.height() - 0.16) / 0.4, 0.0, 1.0);
               int var19 = (int)Math.round(210.0 * var17);
               if (var19 > 1) {
                  double var20 = 0.22 + 0.78 * Math.clamp(var14.roof.light(), 0.0, 1.0);
                  int var22 = (int)(232.0 * var20);
                  int var23 = (int)(241.0 * var20);
                  int var24 = (int)(255.0 * var20);
                  this.o[this.C] = var19 << 24 | var22 << 16 | var23 << 8 | var24;
                  this.b[this.C] = var8;
                  this.c[this.C] = var10;
                  this.a[this.C] = var12;
                  this.P[this.C] = this.u[var7] + var5 * (0.35 + 0.15 * this.G[var7]);
                  this.K[this.C] = this.i[var7];
                  this.C++;
               }
            }
         }
      }
   }

   public void lAsVoid(EffectGeometryRenderer var1) {
      if (this.C != 0) {
         RenderLayer var2 = SnowflakeRenderer.VAsRenderLayer();

         for (int var3 = 0; var3 < this.C; var3++) {
            var1.NAsVoid(var2, this.b[var3], this.c[var3], this.a[var3], this.K[var3], this.P[var3], this.o[var3]);
         }

         OpenScreen.FAsVoid("Snow flakes", (long)this.C);
      }
   }

   public Vec3d PAsVec3d(int var1) {
      if (var1 >= 0 && var1 < this.C) {
         return new Vec3d(this.b[var1], this.c[var1], this.a[var1]);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public static record Type_k(double radius, double density, double wind, int limit) {
      public Type_k(double radius, double density, double wind, int limit) {
         if (Double.isFinite(radius) && Double.isFinite(density) && Double.isFinite(wind)) {
            radius = Math.clamp(radius, 12.0, 40.0);
            density = Math.clamp(density, 0.0, 1.0);
            wind = Math.clamp(wind, 0.0, 1.0);
            limit = Math.clamp((long)limit, 0, 3200);
            this.radius = radius;
            this.density = density;
            this.wind = wind;
            this.limit = limit;
         } else {
            throw new IllegalArgumentException("Non-finite snow options");
         }
      }

      public int count() {
         return (int)Math.round((double)this.limit * this.density);
      }
   }

   @FunctionalInterface
   public interface Type_n {
      SnowOptions.Type_t pAsQp_Type_t(int var1, int var2);
   }

   private static record Type_r(SnowOptions.Type_t roof, long tick) {
   }

   public static record Type_t(double height, double light) {
      public static final SnowOptions.Type_t UNAVAILABLE = new SnowOptions.Type_t(Double.POSITIVE_INFINITY, 0.0);
   }
}
