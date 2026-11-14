package com.larpclient;

import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionfc;

public final class WalkPose {
   public static final int MenuPopupProvider = 6;
   public static final double TextLayout = 1.25;
   public static final double w = 4.6;
   public static final double P = 0.9375;
   public static final double[][] A = new double[][]{{0.0, -4.0, 0.0}, {0.0, 6.0, 0.0}, {-6.0, 6.0, 0.0}, {6.0, 6.0, 0.0}, {-1.9, 18.0, 0.0}, {1.9, 18.0, 0.0}};
   private final Vec3d[] SmoothInput = new Vec3d[6];
   private final Vec3d[] Z = new Vec3d[6];
   private final Vec3d[] h = new Vec3d[6];
   private final double[] BoxUtils = new double[6];
   private final double[] AuraModule = new double[6];
   private final boolean[] PriceDetector = new boolean[6];
   public final WalkPose.Type_f C;
   public final Vec3d X;
   public final double M;
   public final double U;
   public final long B;
   public final boolean k;
   private double N;
   private boolean e;

   public WalkPose(WalkPose.Type_f var1, Vec3d var2, double var3, double var5, long var7) {
      this(var1, var2, var3, var5, var7, false);
   }

   public WalkPose(WalkPose.Type_f var1, Vec3d var2, double var3, double var5, long var7, boolean var9) {
      this.C = var1;
      this.X = var2;
      this.M = var3;
      this.U = var5;
      this.B = var7;
      this.k = var9;
      this.N = var5;
   }

   public static double mAsDouble(double var0) {
      return ay.RAsDouble(var0);
   }

   public static double GAsDouble(double var0, double var2, double var4) {
      return !(var0 < var2) && !(var0 >= var4) ? mAsDouble((var0 - var2) / 0.09) * mAsDouble((var4 - var0) / 0.22) : 0.0;
   }

   public Vec3d JAsVec3d(int var1) {
      double var2 = mAsDouble(var1, this.k) / 16.0 * 0.9375;
      return new Vec3d(var2 * Math.cos(this.M), (24.0 - A[var1][1]) / 16.0 * 0.9375, var2 * Math.sin(this.M));
   }

   public static double mAsDouble(int var0, boolean var1) {
      return var1 && (var0 == 2 || var0 == 3) ? A[var0][0] + (var0 == 2 ? 0.5 : -0.5) : A[var0][0];
   }

   public static double jAsDouble(int var0) {
      return var0 == 0 ? 0.46 : (var0 == 1 ? 0.47 : 0.42);
   }

   public void bAsVoid(double var1, lq.Type_a var3) {
      double var4 = this.N - this.U;
      double var6 = var1 - this.U;
      this.N = var1;
      if (this.C == WalkPose.Type_f.EXECUTION && !(var6 < 1.25) && !(var1 < this.U)) {
         if (!this.e) {
            this.e = true;

            for (int var8 = 0; var8 < 6; var8++) {
               this.t[var8] = this.Z[var8] = this.X.add(this.JAsVec3d(var8)).add(0.0, 1.7, 0.0);
               double var9 = (double)var8 * Math.PI * 2.0 / 6.0 + this.rAsDouble(var8) * 0.6;
               this.h[var8] = new Vec3d(
                  Math.cos(var9) * (1.1 + this.rAsDouble(var8 + 8)), 0.65 + this.rAsDouble(var8 + 15), Math.sin(var9) * (1.1 + this.rAsDouble(var8 + 8))
               );
            }

            var4 = 1.25;
         }

         double var23 = Math.clamp(var6 - Math.max(var4, 1.25), 0.0, 0.05);

         for (int var10 = 0; var10 < 6; var10++) {
            this.Z[var10] = this.t[var10];
            this.x[var10] = this.c[var10];
            if (!this.m[var10] && var23 != 0.0) {
               this.h[var10] = this.h[var10].add(0.0, -8.8 * var23, 0.0);
               Vec3d var11 = this.t[var10].add(this.h[var10].multiply(var23));
               double var12 = jAsDouble(var10);
               Vec3d var14 = new Vec3d(0.0, -var12, 0.0);
               lq.Type_l var15 = var3 == null ? null : var3.HAsLq_Type_l(this.t[var10].add(var14), var11.add(var14));
               Vec3d var16 = var11.subtract(this.t[var10]);

               for (int var17 = 0; var17 < 2 && var3 != null; var17++) {
                  double var18 = var17 == 0 ? var16.x : var16.z;
                  if (!(Math.abs(var18) < 1.0E-8)) {
                     Vec3d var20 = var17 == 0 ? new Vec3d(Math.copySign(var12, var18), 0.0, 0.0) : new Vec3d(0.0, 0.0, Math.copySign(var12, var18));
                     Vec3d var21 = this.t[var10].add(var20);
                     lq.Type_l var22 = var3.HAsLq_Type_l(var21, var11.add(var20));
                     if (var22 != null && (var15 == null || var21.squaredDistanceTo(var22.point()) < this.t[var10].add(var14).squaredDistanceTo(var15.point()))
                        )
                      {
                        var15 = var22;
                        var14 = var20;
                     }
                  }
               }

               if (var15 != null) {
                  this.t[var10] = var15.point().subtract(var14).add(var15.normal().multiply(var12 + 0.025 + var14.dotProduct(var15.normal())));
                  if (var15.normal().y > 0.5) {
                     this.m[var10] = true;
                     this.h[var10] = Vec3d.ZERO;
                     this.t[var10] = new Vec3d(this.t[var10].x, var15.point().y + (var10 == 0 ? 0.27 : 0.15), this.t[var10].z);
                  } else {
                     this.h[var10] = new Vec3d(0.0, this.h[var10].y * 0.85, 0.0);
                  }
               } else {
                  this.t[var10] = var11;
               }

               if (!this.m[var10]) {
                  this.c[var10] = this.c[var10] + var23 * (2.8 + this.rAsDouble(var10 + 22) * 2.0);
               }
            }
         }
      }
   }

   public WalkPose.Type_x QAsAz_Type_x(int var1, double var2, double var4) {
      return this.JAsAz_Type_x(var1, var2, var4, aa.a);
   }

   public WalkPose.Type_x JAsAz_Type_x(int var1, double var2, double var4, aa.Type_z var6) {
      if (this.C == WalkPose.Type_f.STAR_OF_DAVID) {
         return aa.zAsAz_Type_x(this, var1, var2, var6);
      } else if (this.C == WalkPose.Type_f.TUNG_TUNG) {
         return dj.AAsAz_Type_x(this, var1, var2);
      } else if (this.C == WalkPose.Type_f.TUNG_TUNG_FLOSS) {
         return SahurDanceEngine.lAsAz_Type_x(this, var1, var2);
      } else {
         Vec3d var7 = this.JAsVec3d(var1);
         double var8 = mAsDouble((4.6 - var2) / 0.75);
         if (this.C == WalkPose.Type_f.EXECUTION) {
            if (!(var2 < 1.25) && this.e) {
               double var22 = this.x[var1] + (this.c[var1] - this.x[var1]) * Math.clamp(var4, 0.0, 1.0);
               return new WalkPose.Type_x(
                  this.Z[var1].lerp(this.t[var1], Math.clamp(var4, 0.0, 1.0)),
                  this.m[var1] ? Math.PI / 2 : var22 * 0.75 + (var1 < 2 ? -0.07 : 0.0),
                  this.m[var1] ? 0.0 : var22 * 0.45,
                  this.m[var1] ? var22 * 0.45 : var22 + (var1 == 2 ? -0.32 : (var1 == 3 ? 0.32 : 0.0)),
                  1.0,
                  var8,
                  0.0,
                  this.m[var1]
               );
            } else {
               double var21 = 1.7 * mAsDouble(var2 / 0.85);
               double var23 = mAsDouble(var2 / 0.8);
               return new WalkPose.Type_x(
                  this.X.add(var7).add(0.0, var21, 0.0),
                  var1 < 2 ? -0.07 * var23 : 0.0,
                  0.0,
                  var1 == 2 ? -0.32 * var23 : (var1 == 3 ? 0.32 * var23 : 0.0),
                  1.0,
                  var8,
                  0.0,
                  false
               );
            }
         } else if (this.C == WalkPose.Type_f.SINGULARITY) {
            double var20 = mAsDouble((var2 - 0.45) / 1.45);
            double var12 = var20 * 8.5;
            Vec3d var14 = var7.subtract(0.0, 1.05, 0.0).multiply(1.0 - var20);
            double var15 = Math.cos(var12);
            double var17 = Math.sin(var12);
            Vec3d var19 = new Vec3d(var14.x * var15 - var14.z * var17, var14.y, var14.x * var17 + var14.z * var15);
            return new WalkPose.Type_x(
               this.X.add(0.0, 1.05 + 0.35 * Math.sin(var20 * Math.PI), 0.0).add(var19),
               var20 * 2.0,
               var12,
               var20 * 1.2,
               Math.max(0.0, 1.0 - var20),
               1.0 - var20,
               0.0,
               false
            );
         } else {
            double var10 = mAsDouble((var2 - 0.35 - (1.8 - var7.y) * 0.3) / 1.2);
            return new WalkPose.Type_x(
               this.X.add(var7).add(Math.sin(var2 * 2.0 + (double)var1) * var10 * 0.04, var10 * 0.38, 0.0),
               var10 * 0.1,
               0.0,
               0.0,
               1.0,
               1.0 - var10 * 0.2,
               var10,
               false
            );
         }
      }
   }

   public double TAsDouble() {
      return this.C == WalkPose.Type_f.TUNG_TUNG_FLOSS ? 7.5 : 4.6;
   }

   public boolean uAsBoolean(double var1) {
      return var1 - this.U >= this.TAsDouble();
   }

   public boolean dAsBoolean(int var1) {
      return this.m[var1];
   }

   public double rAsDouble(int var1) {
      long var2 = this.B + (long)var1 * -7046029254386353131L;
      var2 = (var2 ^ var2 >>> 30) * -4658895280553007687L;
      var2 = (var2 ^ var2 >>> 27) * -7723592293110705685L;
      return (double)((float)((var2 ^ var2 >>> 31) >>> 11) * 1.110223E-16F);
   }

   public static enum Type_f {
      EXECUTION,
      SINGULARITY,
      SOUL_PYRE,
      STAR_OF_DAVID,
      TUNG_TUNG,
      TUNG_TUNG_FLOSS;

      private Type_f() {
      }
   }

   public static record Type_x(
      Vec3d position, double rx, double ry, double rz, double scale, double alpha, double dissolve, boolean landed, Quaternionfc orientation, Vec3d stretch
   ) {
      public Type_x(Vec3d var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14) {
         this(var1, var2, var4, var6, var8, var10, var12, var14, null, null);
      }
   }
}
