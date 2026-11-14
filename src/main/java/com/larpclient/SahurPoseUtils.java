package com.larpclient;

import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class SahurPoseUtils {
   public static final double w = 1.7;
   public static final double F = 0.85;
   public static final double f = 1.85;
   public static final double X = 1.02;
   public static final SahurPoseUtils.Type_z a = new SahurPoseUtils.Type_z(new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, 0.0), new Vec3d(0.0, 0.0, 1.0));

   public static double AAsDouble(double var0) {
      return WalkPose.mAsDouble((var0 - 0.85) / 1.0);
   }

   public static Vec3d IAsVec3d(WalkPose var0, double var1) {
      double var3 = Math.sin(Math.max(0.0, var1 - 1.85) * 2.4) * 0.045;
      return var0.X.add(0.0, 2.75 + var3, 0.0);
   }

   public static SahurPoseUtils.Type_z iAsAa_Type_z(Vec3d var0, Vec3d var1, Vec3d var2, Vec3d var3) {
      Vec3d var4 = var1.subtract(var0);
      if (var4.lengthSquared() < 1.0E-10) {
         var4 = var2.crossProduct(var3);
      }

      if (var4.lengthSquared() < 1.0E-10) {
         var4 = a.normal;
      }

      var4 = var4.normalize();
      Vec3d var5 = var2.subtract(var4.multiply(var2.dotProduct(var4)));
      if (var5.lengthSquared() < 1.0E-10) {
         var5 = var3.crossProduct(var4);
      }

      if (var5.lengthSquared() < 1.0E-10) {
         Vec3d var6 = Math.abs(var4.y) < 0.9 ? new Vec3d(0.0, 1.0, 0.0) : new Vec3d(0.0, 0.0, 1.0);
         var5 = var6.crossProduct(var4);
      }

      var5 = var5.normalize();
      return new SahurPoseUtils.Type_z(var5, var4.crossProduct(var5).normalize(), var4);
   }

   public static SahurPoseUtils.Type_j nAsAa_Type_j(WalkPose var0, double var1, SahurPoseUtils.Type_z var3, int var4) {
      LightmapAtlas.Type_w var5 = HexEdgeloop.d.get(var4);
      Vec3d var6 = IAsVec3d(var0, var1).add(var3.normal.multiply(var4 < 3 ? 0.035 : -0.035));
      return new SahurPoseUtils.Type_j(wAsVec3d(var6, var3, var5.from()), wAsVec3d(var6, var3, var5.to()));
   }

   private static Vec3d wAsVec3d(Vec3d var0, SahurPoseUtils.Type_z var1, Vec3d var2) {
      return var0.add(var1.right.multiply(var2.x * 1.02)).add(var1.up.multiply(-var2.z * 1.02));
   }

   private static Quaternionf UAsQuaternionf(SahurPoseUtils.Type_z var0) {
      Vec3d var1 = var0.right.multiply(-1.0);
      Vec3d var2 = var0.up;
      Vec3d var3 = var0.normal.multiply(-1.0);
      return new Quaternionf()
         .setFromNormalized(
            new Matrix3f()
               .setColumn(0, new Vector3f((float)var1.x, (float)var1.y, (float)var1.z))
               .setColumn(1, new Vector3f((float)var2.x, (float)var2.y, (float)var2.z))
               .setColumn(2, new Vector3f((float)var3.x, (float)var3.y, (float)var3.z))
         );
   }

   public static WalkPose.Type_x zAsAz_Type_x(WalkPose var0, int var1, double var2, SahurPoseUtils.Type_z var4) {
      double var5 = AAsDouble(var2);
      double var7 = 1.7 * WalkPose.mAsDouble(var2 / 0.85);
      double var9 = WalkPose.mAsDouble(var1, var0.k) / 16.0 * 0.9375;
      double var11 = (24.0 - WalkPose.A[var1][1]) / 16.0 * 0.9375;
      Vec3d var13 = var0.X.add(0.0, 1.05 + var7, 0.0).add(var4.right.multiply(var9)).add(var4.up.multiply(var11 - 1.05));
      SahurPoseUtils.Type_j var14 = nAsAa_Type_j(var0, var2, var4, var1);
      double var15 = var1 < 2 ? 0.5 : (var0.k && (var1 == 2 || var1 == 3) ? 0.1875 : 0.25);
      double var17 = var1 == 0 ? 0.5 : 0.75;
      double var19 = var1 == 0 ? 0.5 : 0.25;
      double var21 = var1 == 1 ? 0.22 : 0.18;
      Vec3d var23 = new Vec3d(var21 / (var15 * 0.9375), var14.length() / (var17 * 0.9375), 0.14 / (var19 * 0.9375));
      LightmapAtlas.Type_w var24 = HexEdgeloop.d.get(var1);
      Vec3d var25 = var24.to().subtract(var24.from()).normalize();
      double var26 = Math.atan2(-var25.x, var25.z);
      Quaternionf var28 = UAsQuaternionf(var4).rotateZ((float)(var26 * var5));
      Vec3d var29 = var13.lerp(var14.center(), var5).add(var4.normal.multiply(Math.sin(var5 * Math.PI) * 0.18 * (double)(var1 < 3 ? 1 : -1)));
      double var30 = WalkPose.mAsDouble((4.6 - var2) / 0.75);
      return new WalkPose.Type_x(var29, 0.0, 0.0, 0.0, 1.0, var30, 0.0, false, var28, new Vec3d(1.0, 1.0, 1.0).lerp(var23, var5));
   }

   private SahurPoseUtils() {
   }

   public static record Type_j(Vec3d from, Vec3d to) {
      public Vec3d center() {
         return this.from.lerp(this.to, 0.5);
      }

      public Vec3d tangent() {
         return this.to.subtract(this.from).normalize();
      }

      public double length() {
         return this.from.distanceTo(this.to);
      }
   }

   public static record Type_z(Vec3d right, Vec3d up, Vec3d normal) {
   }
}
