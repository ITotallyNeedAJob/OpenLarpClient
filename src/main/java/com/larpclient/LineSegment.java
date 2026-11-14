package com.larpclient;

import java.util.List;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

public final class LineSegment {
   private static Vec3d kAsVec3d(Matrix4fc var0, Vec3d var1, int var2) {
      Vector3f var3 = var0.transformPosition(new Vector3f((float)((double)var2 * var1.x), (float)var1.y, (float)var1.z));
      return new Vec3d((double)var3.x, (double)var3.y, (double)var3.z);
   }

   public static LineSegment.Type_c dAsE_Type_c(float[] var0) {
      double var1 = Double.POSITIVE_INFINITY;
      double var3 = Double.NEGATIVE_INFINITY;

      for (int var5 = 0; var5 < var0.length; var5 += 8) {
         var1 = Math.min(var1, (double)var0[var5 + 1]);
         var3 = Math.max(var3, (double)var0[var5 + 1]);
      }

      if (Double.isFinite(var1 + var3) && !(var3 - var1 < 0.02)) {
         Vec3d var7 = zAsVec3d(var0, var1, var1 + (var3 - var1) * 0.055);
         Vec3d var6 = zAsVec3d(var0, var3 - (var3 - var1) * 0.025, var3);
         return new LineSegment.Type_c(var7, var6);
      } else {
         return null;
      }
   }

   private static Vec3d zAsVec3d(float[] var0, double var1, double var3) {
      double var5 = 0.0;
      double var7 = 0.0;
      double var9 = 0.0;
      int var11 = 0;

      for (int var12 = 0; var12 < var0.length; var12 += 8) {
         if ((double)var0[var12 + 1] >= var1 && (double)var0[var12 + 1] <= var3) {
            var5 += (double)var0[var12];
            var7 += (double)var0[var12 + 1];
            var9 += (double)var0[var12 + 2];
            var11++;
         }
      }

      return new Vec3d(var5 / (double)var11, var7 / (double)var11, var9 / (double)var11);
   }

   public static LineSegment.Type_c LAsE_Type_c(List<Vec3d> var0) {
      if (var0.size() < 4) {
         return null;
      } else {
         Vec3d var1 = Vec3d.ZERO;

         for (Vec3d var3 : var0) {
            var1 = var1.add(var3);
         }

         var1 = var1.multiply(1.0 / (double)var0.size());
         double var24 = 0.0;
         double var4 = 0.0;
         double var6 = 0.0;
         double var8 = 0.0;
         double var10 = 0.0;
         double var12 = 0.0;

         for (Vec3d var15 : var0) {
            Vec3d var16 = var15.subtract(var1);
            var24 += var16.x * var16.x;
            var4 += var16.x * var16.y;
            var6 += var16.x * var16.z;
            var8 += var16.y * var16.y;
            var10 += var16.y * var16.z;
            var12 += var16.z * var16.z;
         }

         Vec3d var25 = new Vec3d(0.6, 1.0, 0.05).normalize();

         for (int var26 = 0; var26 < 12; var26++) {
            Vec3d var28 = new Vec3d(
               var24 * var25.x + var4 * var25.y + var6 * var25.z,
               var4 * var25.x + var8 * var25.y + var10 * var25.z,
               var6 * var25.x + var10 * var25.y + var12 * var25.z
            );
            if (var28.lengthSquared() < 1.0E-12) {
               return null;
            }

            var25 = var28.normalize();
         }

         if (var25.y < 0.0) {
            var25 = var25.multiply(-1.0);
         }

         double var27 = Double.POSITIVE_INFINITY;
         double var17 = Double.NEGATIVE_INFINITY;

         for (Vec3d var20 : var0) {
            double var21 = var20.subtract(var1).dotProduct(var25);
            var27 = Math.min(var27, var21);
            var17 = Math.max(var17, var21);
         }

         if (var17 - var27 < 0.1) {
            return null;
         } else {
            Vec3d var29 = Vec3d.ZERO;
            int var31 = 0;

            for (Vec3d var22 : var0) {
               if (var22.subtract(var1).dotProduct(var25) >= var17 - (var17 - var27) * 0.025) {
                  var29 = var29.add(var22);
                  var31++;
               }
            }

            var29 = var29.multiply(1.0 / (double)var31);
            Vec3d var33 = var1.add(var25.multiply(var27 + (var17 - var27) * 0.48));
            return new LineSegment.Type_c(var33, var29);
         }
      }
   }

   private LineSegment() {
   }

   public static record Type_c(Vec3d root, Vec3d tip) {
      public LineSegment.Type_c transform(Matrix4fc var1, int var2) {
         return new LineSegment.Type_c(LineSegment.kAsVec3d(var1, this.root, var2), LineSegment.kAsVec3d(var1, this.tip, var2));
      }

      public double length() {
         return this.root.distanceTo(this.tip);
      }
   }
}
