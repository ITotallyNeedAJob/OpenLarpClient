package com.larpclient;

import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class DiscordRPC {
   public static final double LineSegment = 3.6;
   public static final double L = 1.56;
   public static final double MenuPopupProvider = 1.28;
   public static final double z = 1.12;
   public static final Vec3d w = new Vec3d(-0.031, 0.08, -0.067);
   public static final Vec3d I = new Vec3d(-0.02649, -0.064244, -0.046604);
   public static final Vec3d F = new Vec3d(-0.088521, -0.283297, 0.098837);
   private static final double U = -0.105;
   private static final double P = 1.65;
   private final SahurMeshReader.Type_u Q;
   private final float[] R;
   private final float[] n;
   private final float[] l;
   private final double S;
   private final double d;
   private final Vec3d T;
   private final Matrix4f B = new Matrix4f();
   private final Matrix4f q = new Matrix4f();
   private final Vector3f C = new Vector3f();

   public DiscordRPC(SahurMeshReader.Type_u var1) {
      SahurArticulation.Type_o var2 = SahurArticulation.rAsAu_Type_o(var1);
      this.Q = var2.mesh();
      this.R = (float[])this.Q.vertices().clone();
      this.n = new float[this.Q.colors().length];
      this.l = new float[this.n.length];
      this.S = 3.6 / (double)var1.height();

      for (int var3 = 0; var3 < this.n.length; var3++) {
         double var4 = (double)this.R[var3 * 5 + 1];
         this.n[var3] = var2.arm()[var3] ? 1.0F : 0.0F;
         this.l[var3] = (float)Math.max((double)this.n[var3], az.mAsDouble((var4 + 0.17) / 0.1));
      }

      double var10 = -1.5;
      double var5 = 0.0;

      for (int var7 = 0; var7 < 40; var7++) {
         double var8 = (var10 + var5) * 0.5;
         if (this.hAsDouble(var8) > 1.65) {
            var10 = var8;
         } else {
            var5 = var8;
         }
      }

      this.d = (var10 + var5) * 0.5;
      Vec3d var11 = this.rAsVec3d(F, 1.56);
      this.T = new Vec3d(-var11.x * this.S, 0.0, -var11.z * this.S);
   }

   private double hAsDouble(double var1) {
      this.SAsVoid(var1, xAsDouble(1.56), 0.0);
      this.C.set((float)F.x, (float)F.y, (float)F.z);
      this.B.transformPosition(this.C);
      this.q.transformPosition(this.C);
      return (double)(this.C.y - this.Q.minimumY()) * this.S;
   }

   private void SAsVoid(double var1, double var3, double var5) {
      this.B
         .identity()
         .translate((float)w.x, (float)w.y, (float)w.z)
         .rotateY((float)var5)
         .rotateX((float)var1)
         .translate((float)(-w.x), (float)(-w.y), (float)(-w.z));
      this.q.identity().translate(0.0F, -0.105F, 0.0F).rotateX((float)var3).translate(0.0F, 0.105F, 0.0F);
   }

   public double HAsDouble(double var1) {
      if (var1 < 1.28) {
         return -2.65 * az.mAsDouble((var1 - 0.36) / 0.7600000000000001);
      } else if (var1 < 1.56) {
         double var3 = Math.clamp((var1 - 1.28) / 0.28, 0.0, 1.0);
         return -2.65 + (this.d + 2.65) * var3 * var3;
      } else if (var1 < 1.6800000000000002) {
         return this.d + 0.13 * az.mAsDouble((var1 - 1.56) / 0.12);
      } else {
         return var1 < 2.12
            ? this.d + 0.13 - 0.27 * az.mAsDouble((var1 - 1.56 - 0.12) / 0.44000000000000006)
            : (this.d - 0.14) * (1.0 - az.mAsDouble((var1 - 2.35) / 0.8));
      }
   }

   private static double LAsDouble(double var0) {
      return 0.7 * Math.sin(Math.PI * az.mAsDouble((var0 - 0.36) / 0.7600000000000001));
   }

   public static double xAsDouble(double var0) {
      double var2 = -0.055 * az.mAsDouble((var0 - 0.36) / 0.7600000000000001);
      if (var0 < 1.28) {
         return var2;
      } else if (var0 < 1.56) {
         return -0.055 + 0.235 * az.mAsDouble((var0 - 1.28) / 0.28);
      } else {
         return var0 < 2.12 ? 0.18 - 0.12 * az.mAsDouble((var0 - 1.56) / 0.56) : 0.06 * (1.0 - az.mAsDouble((var0 - 2.35) / 0.8));
      }
   }

   private Vec3d rAsVec3d(Vec3d var1, double var2) {
      this.SAsVoid(this.HAsDouble(var2), xAsDouble(var2), LAsDouble(var2));
      this.C.set((float)var1.x, (float)var1.y, (float)var1.z);
      this.B.transformPosition(this.C);
      this.q.transformPosition(this.C);
      return new Vec3d((double)this.C.x, (double)this.C.y, (double)this.C.z);
   }

   private Vec3d gAsVec3d(az var1, Vec3d var2) {
      Vec3d var3 = var2.add(0.0, (double)(-this.Q.minimumY()), 0.0).multiply(this.S).add(this.T);
      double var4 = Math.cos(var1.M);
      double var6 = Math.sin(var1.M);
      return var1.X.add(var3.x * var4 - var3.z * var6, var3.y, var3.x * var6 + var3.z * var4);
   }

   public Vec3d OAsVec3d(az var1, double var2) {
      return this.gAsVec3d(var1, this.rAsVec3d(F, var2));
   }

   public Vec3d bAsVec3d(az var1, double var2) {
      return this.gAsVec3d(var1, this.rAsVec3d(I, var2));
   }

   public DiscordRPC.Type_z oAsDj_Type_z(az var1, double var2, Vec3d var4) {
      this.SAsVoid(this.HAsDouble(var2), xAsDouble(var2), LAsDouble(var2));
      float[] var5 = this.Q.vertices();

      for (int var6 = 0; var6 < this.n.length; var6++) {
         int var7 = var6 * 5;
         float var8 = var5[var7];
         float var9 = var5[var7 + 1];
         float var10 = var5[var7 + 2];
         this.C.set(var8, var9, var10);
         this.B.transformPosition(this.C);
         float var11 = this.n[var6];
         float var12 = this.l[var6];
         float var13 = var8 + (this.C.x - var8) * var11;
         float var14 = var9 + (this.C.y - var9) * var11;
         float var15 = var10 + (this.C.z - var10) * var11;
         this.C.set(var13, var14, var15);
         this.q.transformPosition(this.C);
         this.R[var7] = var13 + (this.C.x - var13) * var12;
         this.R[var7 + 1] = var14 + (this.C.y - var14) * var12;
         this.R[var7 + 2] = var15 + (this.C.z - var15) * var12;
      }

      double var16 = Math.cos(var1.M);
      double var17 = Math.sin(var1.M);
      Vec3d var18 = var1.X.add(this.T.x * var16 - this.T.z * var17, 0.0, this.T.x * var17 + this.T.z * var16);
      Matrix4f var19 = new Matrix4f()
         .translation((float)(var18.x - var4.x), (float)(var18.y - var4.y), (float)(var18.z - var4.z))
         .rotateY((float)(-var1.M))
         .scale((float)this.S)
         .translate(0.0F, -this.Q.minimumY(), 0.0F);
      Vec3d[] var20 = new Vec3d[14];

      for (int var21 = 0; var21 < var20.length; var21++) {
         var20[var21] = this.OAsVec3d(var1, Math.max(1.28, var2 - 0.13 + (double)var21 * 0.13 / (double)(var20.length - 1)));
      }

      double var22 = az.mAsDouble(var2 / 0.42) * az.mAsDouble((4.6 - var2) / 1.15);
      return new DiscordRPC.Type_z(this.R, this.Q.colors(), var19, var22, var18, this.bAsVec3d(var1, var2), this.OAsVec3d(var1, var2), var20);
   }

   public static az.Type_x AAsAz_Type_x(az var0, int var1, double var2) {
      double var4 = az.mAsDouble((var2 - 1.56) / 0.28);
      double var6 = var4 * Math.PI / 2.0;
      double var8 = az.mAsDouble(var1, var0.k) / 16.0 * 0.9375;
      double var10 = (24.0 - az.A[var1][1]) / 16.0 * 0.9375;
      double var12 = var10 * Math.sin(var6);
      double var14 = Math.cos(var0.M);
      double var16 = Math.sin(var0.M);
      Vec3d var18 = var0.X.add(var8 * var14 - var12 * var16, var10 * Math.cos(var6) + 0.27 * var4, var8 * var16 + var12 * var14);
      double var19 = az.mAsDouble((3.65 - var2) / 0.9);
      double var21 = az.mAsDouble((var2 - 2.6) / 1.05);
      return new az.Type_x(var18, -var6, 0.0, 0.0, 1.0, var19, var21, var4 >= 1.0);
   }

   public static record Type_z(float[] vertices, int[] colors, Matrix4f transform, double opacity, Vec3d feet, Vec3d grip, Vec3d tip, Vec3d[] trail) {
   }
}
