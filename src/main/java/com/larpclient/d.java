package com.larpclient;

import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class d {
   public static final double H = 3.6;
   public static final double X = 0.78;
   public static final double A = 1.62;
   public static final double G = 2.16;
   public static final double Y = 1.12;
   public static final double r = 6.640000000000001;
   public static final double m = 7.5;
   public static final double f = 0.42;
   public static final double y = 0.84;
   private static final double o = 0.028;
   private static final double l = -0.062;
   private static final double N = -0.105;
   private static final Vec3d z = new Vec3d(0.028, -0.105, -0.062);
   private static final Vec3d[] I = new Vec3d[]{new Vec3d(-0.031, 0.08, -0.067), new Vec3d(0.088, 0.08, -0.067)};
   private static final Vec3d[] S = new Vec3d[]{new Vec3d(-0.038, 0.002, -0.068), new Vec3d(0.096, 0.002, -0.068)};
   private static final Vec3d[] x = new Vec3d[]{new Vec3d(-0.027, -0.077, -0.05), new Vec3d(0.077, -0.077, -0.05)};
   private static final Vec3d[] i = new Vec3d[]{new Vec3d(0.001, -0.105, -0.062), new Vec3d(0.055, -0.105, -0.062)};
   private static final Vec3d[] k = new Vec3d[]{new Vec3d(-0.001, -0.194, -0.067), new Vec3d(0.057, -0.194, -0.067)};
   private static final Vec3d[] B = new Vec3d[]{new Vec3d(-0.003, -0.28, -0.068), new Vec3d(0.057, -0.28, -0.068)};
   private static final double[] F = new double[]{1.0, 1.0, 1.0, -1.0, -1.0, -1.0, 1.0};
   private static final double[] U = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0};
   private static final Vec3d K = new Vec3d(0.0, 0.0, 1.0);
   private static final Vec3d T = new Vec3d(0.0, 0.0, -1.0);
   private final SahurMeshReader.Type_u E;
   private final int[] v;
   private final float[] Q;
   private final double c;
   private final Matrix4f W = new Matrix4f();
   private final Matrix4f t = new Matrix4f();
   private final d.Type_x[] w = new d.Type_x[]{new d.Type_x(), new d.Type_x()};
   private final d.Type_x[] V = new d.Type_x[]{new d.Type_x(), new d.Type_x()};
   private final Vector3f R = new Vector3f();
   private final Vector3f n = new Vector3f();

   public d(SahurMeshReader.Type_u var1) {
      SahurArticulation.Type_b var2 = SahurArticulation.mAsAu_Type_b(var1);
      this.E = var2.mesh();
      this.v = var2.joints();
      this.Q = (float[])this.E.vertices().clone();
      this.c = 3.6 / (double)var1.height();
   }

   private static double sAsDouble(double var0) {
      return az.mAsDouble(var0);
   }

   private static double xAsDouble(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public static d.Type_v xAsD_Type_v(double var0) {
      double var2 = Math.max(0.0, var0);
      double var4 = Math.clamp((var2 - 0.78) / 0.8400000000000001, 0.0, 1.0);
      double var6 = sAsDouble(var4);
      double var8 = 4.0 * var4 * (1.0 - var4);
      double var10 = var2 < 0.78 ? sAsDouble((var2 - 0.32) / 0.34) : 0.0;
      double var12 = 1.0 - sAsDouble((var2 - 0.78) / 0.14);
      double var14 = sAsDouble((var2 - 1.62) / 0.085) * (1.0 - sAsDouble((var2 - 1.62 - 0.085) / 0.31));
      double var16 = sAsDouble((var2 - 2.16) / 0.22) * (1.0 - sAsDouble((var2 - 6.640000000000001) / 0.3));
      double var18 = Math.max(0.0, var2 - 2.16) / 1.12;
      double var20 = (var18 - Math.floor(var18)) * 6.0;
      int var22 = (int)var20;
      double var23 = sAsDouble(var20 - (double)var22);
      double var25 = xAsDouble(F[var22], F[var22 + 1], var23);
      double var27 = xAsDouble(U[var22], U[var22 + 1], var23);
      double var29 = -var25 * 0.018 * var16;
      double var31 = 0.008
         + 0.043 * var10
         + 0.043 * (var2 >= 0.78 && var2 < 1.62 ? var12 : 0.0)
         + 0.05 * var14
         + 0.007 * var16 * (0.5 + 0.5 * Math.cos(var18 * Math.PI * 4.0));
      double var33 = 0.18 * var10 + (var2 >= 0.78 && var2 < 1.62 ? 0.18 * var12 - 0.09 * var8 : 0.0) + 0.15 * var14;
      double var35 = var2 >= 0.78 && var2 < 1.62 ? 0.05 * var8 : 0.0;
      Vec3d[] var37 = new Vec3d[2];

      for (int var38 = 0; var38 < 2; var38++) {
         double var39 = var38 == 0 ? -1.0 : 1.0;
         double var41 = (1.0 + var39 * var25) * 0.5;
         Vec3d var43 = new Vec3d(0.028 + var25 * 0.102 + var39 * 0.017, -0.02, xAsDouble(0.025, -0.173, var27 * var41));
         double var44 = var2 >= 0.78 && var2 < 1.62 ? var8 : 0.0;
         double var46 = var2 < 0.78 ? var10 : (var2 < 1.62 ? var12 : 0.0);
         Vec3d var48 = x[var38].add(var39 * 0.028 * var44, 0.105 * var44, -0.055 * var46 + 0.095 * var44);
         var37[var38] = var48.lerp(var43, var16);
      }

      return new d.Type_v(
         new Vec3d(0.0, 0.42 * var6 + 1.75 * var8, xAsDouble(-1.48, 0.84, var6)), var31, var29, var33, -var25 * 0.045 * var16, var35, var37[0], var37[1], var16
      );
   }

   private static Vec3d IAsVec3d(Matrix4f var0, Vec3d var1) {
      Vector3f var2 = var0.transformPosition(new Vector3f((float)var1.x, (float)var1.y, (float)var1.z));
      return new Vec3d((double)var2.x, (double)var2.y, (double)var2.z);
   }

   public d.Type_g NAsD_Type_g(az var1, double var2, Vec3d var4) {
      d.Type_v var5 = xAsD_Type_v(var2);
      this.W
         .identity()
         .translate((float)(z.x + var5.hipShift), (float)(z.y - var5.compression), (float)z.z)
         .rotateY((float)(var5.roll * 2.2))
         .rotateX((float)var5.lean)
         .rotateZ((float)var5.roll)
         .translate((float)(-z.x), (float)(-z.y), (float)(-z.z));
      this.t
         .set(this.W)
         .translate(0.028F, 0.135F, -0.062F)
         .rotateY((float)(-var5.roll * 1.5))
         .rotateZ((float)(-var5.roll * 0.7))
         .rotateX((float)(-var5.lean * 0.55))
         .translate(-0.028F, -0.135F, 0.062F);

      for (int var6 = 0; var6 < 2; var6++) {
         this.w[var6].yAsVoid(I[var6], S[var6], x[var6], IAsVec3d(this.W, I[var6]), var6 == 0 ? var5.rightHand : var5.leftHand, T);
         Vec3d var7 = B[var6].add(var6 == 0 ? 0.011 : -0.011, var5.tuck, -0.007 + var5.tuck * 0.42);
         this.V[var6].yAsVoid(i[var6], k[var6], B[var6], IAsVec3d(this.W, i[var6]), var7, K);
      }

      float[] var15 = this.E.vertices();

      for (int var16 = 0; var16 < this.v.length; var16++) {
         int var8 = var16 * 5;
         int var9 = this.v[var16];
         float var10 = var15[var8];
         float var11 = var15[var8 + 1];
         float var12 = var15[var8 + 2];
         this.R.set(var10, var11, var12);
         this.n.set(this.R);
         if (var9 == 0) {
            this.W.transformPosition(this.R);
            this.t.transformPosition(this.n);
            this.R.lerp(this.n, (float)sAsDouble(((double)var11 - 0.105) / 0.07));
         } else {
            int var13 = var9 >= 3 ? 1 : 0;
            d.Type_x var14 = (var13 != 0 ? this.V : this.w)[(var9 - 1) % 2];
            var14.j.transformPosition(this.R);
            var14.T.transformPosition(this.n);
            this.R.lerp(this.n, (float)sAsDouble(((var13 != 0 ? -0.194 : 0.002) + 0.013 - (double)var11) / 0.026));
            if (var13 != 0) {
               this.n.set(var10, var11, var12);
               var14.y.transformPosition(this.n);
               this.R.lerp(this.n, (float)sAsDouble((-0.257 - (double)var11) / 0.018));
            }
         }

         this.Q[var8] = this.R.x;
         this.Q[var8 + 1] = this.R.y;
         this.Q[var8 + 2] = this.R.z;
      }

      double var17 = Math.cos(var1.M);
      double var18 = Math.sin(var1.M);
      Vec3d var19 = var1.X.add(var5.root.x * var17 - var5.root.z * var18, var5.root.y, var5.root.x * var18 + var5.root.z * var17);
      Matrix4f var20 = new Matrix4f()
         .translation((float)(var19.x - var4.x), (float)(var19.y - var4.y), (float)(var19.z - var4.z))
         .rotateY((float)(-var1.M))
         .scale((float)this.c)
         .translate(-0.028F, -this.E.minimumY(), 0.062F);
      double var21 = sAsDouble(var2 / 0.3) * sAsDouble((7.5 - var2) / 0.65);
      return new d.Type_g(
         this.Q,
         this.E.colors(),
         var20,
         var21,
         var19,
         sAsVec3d(var20, this.w[0].o, var4),
         sAsVec3d(var20, this.w[1].o, var4),
         sAsVec3d(var20, this.V[0].o, var4),
         sAsVec3d(var20, this.V[1].o, var4)
      );
   }

   private static Vec3d sAsVec3d(Matrix4f var0, Vec3d var1, Vec3d var2) {
      return IAsVec3d(var0, var1).add(var2);
   }

   public static az.Type_x lAsAz_Type_x(az var0, int var1, double var2) {
      double var4 = sAsDouble((var2 - 0.22) / 0.48);
      double var6 = var4 * Math.PI / 2.0;
      double var8 = az.mAsDouble(var1, var0.k) / 16.0 * 0.9375;
      double var10 = (24.0 - az.A[var1][1]) / 16.0 * 0.9375;
      double var12 = var10 * Math.sin(var6);
      double var14 = Math.cos(var0.M);
      double var16 = Math.sin(var0.M);
      Vec3d var18 = var0.X.add(var8 * var14 - var12 * var16, var10 * Math.cos(var6) + 0.27 * var4, var8 * var16 + var12 * var14);
      double var19 = sAsDouble((var2 - 6.790000000000001) / 0.65);
      double var21 = sAsDouble((7.5 - var2) / 0.65);
      return new az.Type_x(var18, -var6, 0.0, 0.0, 1.0, var21, var19, var4 >= 1.0);
   }

   public static record Type_g(
      float[] vertices, int[] colors, Matrix4f transform, double opacity, Vec3d feet, Vec3d rightHand, Vec3d leftHand, Vec3d rightFoot, Vec3d leftFoot
   ) {
   }

   public static record Type_v(
      Vec3d root, double compression, double hipShift, double lean, double roll, double tuck, Vec3d rightHand, Vec3d leftHand, double dance
   ) {
   }

   private static final class Type_x {
      final Matrix4f j = new Matrix4f();
      final Matrix4f T = new Matrix4f();
      final Matrix4f y = new Matrix4f();
      Vec3d o = Vec3d.ZERO;

      private Type_x() {
      }

      void yAsVoid(Vec3d var1, Vec3d var2, Vec3d var3, Vec3d var4, Vec3d var5, Vec3d var6) {
         double var7 = var1.distanceTo(var2);
         double var9 = var2.distanceTo(var3);
         Vec3d var11 = var5.subtract(var4);
         double var12 = Math.clamp(var11.length(), 0.001, var7 + var9 - 1.0E-5);
         Vec3d var14 = var11.lengthSquared() < 1.0E-12 ? new Vec3d(0.0, -1.0, 0.0) : var11.normalize();
         this.o = var4.add(var14.multiply(var12));
         Vec3d var15 = var6.subtract(var14.multiply(var6.dotProduct(var14)));
         if (var15.lengthSquared() < 1.0E-10) {
            var15 = new Vec3d(1.0, 0.0, 0.0).crossProduct(var14);
         }

         var15 = var15.normalize();
         double var16 = (var7 * var7 - var9 * var9 + var12 * var12) / (2.0 * var12);
         Vec3d var18 = var4.add(var14.multiply(var16)).add(var15.multiply(Math.sqrt(Math.max(0.0, var7 * var7 - var16 * var16))));
         JAsVoid(this.j, var1, var2, var4, var18);
         JAsVoid(this.T, var2, var3, var18, this.o);
         this.y.translation((float)(this.o.x - var3.x), (float)(this.o.y - var3.y), (float)(this.o.z - var3.z));
      }

      private static void JAsVoid(Matrix4f var0, Vec3d var1, Vec3d var2, Vec3d var3, Vec3d var4) {
         Vector3f var5 = new Vector3f((float)(var2.x - var1.x), (float)(var2.y - var1.y), (float)(var2.z - var1.z)).normalize();
         Vector3f var6 = new Vector3f((float)(var4.x - var3.x), (float)(var4.y - var3.y), (float)(var4.z - var3.z)).normalize();
         var0.translation((float)var3.x, (float)var3.y, (float)var3.z)
            .rotate(new Quaternionf().rotationTo(var5, var6))
            .translate((float)(-var1.x), (float)(-var1.y), (float)(-var1.z));
      }
   }
}
