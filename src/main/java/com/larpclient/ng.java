package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class ng {
   public static final double P = 0.25;
   public static final double N = 0.012;

   public static ng.Type_e HAsNg_Type_e(Box var0, double var1, double var3, ng.Type_u var5, boolean var6) {
      ArrayList var7 = new ArrayList();
      ArrayList var8 = new ArrayList();
      if (var1 > 0.0 && Double.isFinite(var1)) {
         double var9 = Math.min(8.0, var1 + Math.max(var0.getLengthX(), var0.getLengthZ()) / 2.0 + 0.25);
         Vec3d var11 = var0.getCenter();
         int var12 = (int)Math.floor((var11.x - var9) / 0.25);
         int var13 = (int)Math.ceil((var11.x + var9) / 0.25);
         int var14 = (int)Math.floor((var11.z - var9) / 0.25);
         int var15 = (int)Math.ceil((var11.z + var9) / 0.25);

         for (int var16 = var12; var16 < var13; var16++) {
            for (int var17 = var14; var17 < var15; var17++) {
               double var18 = (double)var16 * 0.25;
               double var20 = (double)var17 * 0.25;
               ng.Type_n var22 = var5.wAsNg_Type_n(var18 + 0.125, var20 + 0.125);
               if (var22 != null) {
                  double var23 = Math.max(var18, var22.minX);
                  double var25 = Math.min(var18 + 0.25, var22.maxX);
                  double var27 = Math.max(var20, var22.minZ);
                  double var29 = Math.min(var20 + 0.25, var22.maxZ);
                  if (!(var23 >= var25)
                     && !(var27 >= var29)
                     && iAsBoolean(var5, var23 + 1.0E-5, var27 + 1.0E-5, var22.y)
                     && iAsBoolean(var5, var25 - 1.0E-5, var27 + 1.0E-5, var22.y)
                     && iAsBoolean(var5, var25 - 1.0E-5, var29 - 1.0E-5, var22.y)
                     && iAsBoolean(var5, var23 + 1.0E-5, var29 - 1.0E-5, var22.y)) {
                     Vec3d var31 = new Vec3d(var23, var22.y + 0.012, var27);
                     Vec3d var32 = new Vec3d(var25, var22.y + 0.012, var27);
                     Vec3d var33 = new Vec3d(var25, var22.y + 0.012, var29);
                     Vec3d var34 = new Vec3d(var23, var22.y + 0.012, var29);
                     lAsVoid(var0, var1, var3, var31, var32, var33, var7, var6 ? var8 : null);
                     lAsVoid(var0, var1, var3, var31, var33, var34, var7, var6 ? var8 : null);
                     zAsVoid(var0, var1, var3, var5, var31, var32, 0.0, -0.003, var22.y, var7);
                     zAsVoid(var0, var1, var3, var5, var32, var33, 0.003, 0.0, var22.y, var7);
                     zAsVoid(var0, var1, var3, var5, var33, var34, 0.0, 0.003, var22.y, var7);
                     zAsVoid(var0, var1, var3, var5, var34, var31, -0.003, 0.0, var22.y, var7);
                  }
               }
            }
         }

         return new ng.Type_e(List.copyOf(var7), List.copyOf(var8));
      } else {
         return new ng.Type_e(List.of(), List.of());
      }
   }

   private static boolean iAsBoolean(ng.Type_u var0, double var1, double var3, double var5) {
      ng.Type_n var7 = var0.wAsNg_Type_n(var1, var3);
      return var7 != null && Math.abs(var7.y - var5) < 0.001;
   }

   private static void zAsVoid(
      Box var0, double var1, double var3, ng.Type_u var5, Vec3d var6, Vec3d var7, double var8, double var10, double var12, List<ng.Type_p> var14
   ) {
      ng.Type_n var15 = var5.wAsNg_Type_n((var6.x + var7.x) / 2.0 + var8, (var6.z + var7.z) / 2.0 + var10);
      if (var15 != null && !(var12 - var15.y < 0.001) && !(var12 - var15.y > 1.05)) {
         Vec3d var16 = var6.add(var8, 0.0, var10);
         Vec3d var17 = var7.add(var8, 0.0, var10);
         Vec3d var18 = new Vec3d(var17.x, var15.y + 0.012, var17.z);
         Vec3d var19 = new Vec3d(var16.x, var15.y + 0.012, var16.z);
         lAsVoid(var0, var1, var3, var16, var17, var18, var14, null);
         lAsVoid(var0, var1, var3, var16, var18, var19, var14, null);
      }
   }

   public static double CAsDouble(Box var0, Vec3d var1, double var2, double var4) {
      return o5.lAsDouble(var1.add(0.0, var2 - 0.012, 0.0), var0) - var4;
   }

   private static void lAsVoid(Box var0, double var1, double var3, Vec3d var5, Vec3d var6, Vec3d var7, List<ng.Type_p> var8, List<ng.Type_d> var9) {
      Vec3d[] var10 = new Vec3d[]{var5, var6, var7};
      double[] var11 = new double[]{CAsDouble(var0, var5, var3, var1), CAsDouble(var0, var6, var3, var1), CAsDouble(var0, var7, var3, var1)};
      if ((!(var11[0] > 0.0) || !(var11[1] > 0.0) || !(var11[2] > 0.0)) && (var9 != null || !(var11[0] <= 0.0) || !(var11[1] <= 0.0) || !(var11[2] <= 0.0))) {
         ArrayList var12 = new ArrayList(4);
         ArrayList var13 = new ArrayList(2);

         for (int var14 = 0; var14 < 3; var14++) {
            int var15 = (var14 + 1) % 3;
            if (var11[var14] <= 0.0) {
               var12.add(var10[var14]);
            }

            if (var11[var14] <= 0.0 != var11[var15] <= 0.0) {
               Vec3d var16 = VAsVec3d(var0, var1, var3, var10[var14], var10[var15], var11[var14] <= 0.0);
               var12.add(var16);
               var13.add(var16);
            }
         }

         if (var13.size() == 2 && ((Vec3d)var13.get(0)).squaredDistanceTo((Vec3d)var13.get(1)) > 1.0E-12) {
            var8.add(new ng.Type_p((Vec3d)var13.get(0), (Vec3d)var13.get(1)));
         }

         if (var9 != null) {
            for (int var17 = 1; var17 + 1 < var12.size(); var17++) {
               var9.add(new ng.Type_d((Vec3d)var12.get(0), (Vec3d)var12.get(var17), (Vec3d)var12.get(var17 + 1)));
            }
         }
      }
   }

   private static Vec3d VAsVec3d(Box var0, double var1, double var3, Vec3d var5, Vec3d var6, boolean var7) {
      double var8 = 0.0;
      double var10 = 1.0;

      for (int var12 = 0; var12 < 12; var12++) {
         double var13 = (var8 + var10) / 2.0;
         if (CAsDouble(var0, var5.lerp(var6, var13), var3, var1) <= 0.0 == var7) {
            var8 = var13;
         } else {
            var10 = var13;
         }
      }

      return var5.lerp(var6, (var8 + var10) / 2.0);
   }

   public static ng.Type_n XAsNg_Type_n(List<Box> var0, double var1, double var3, double var5) {
      Box var7 = null;

      for (Box var9 : var0) {
         if (!(var1 < var9.minX)
            && !(var1 >= var9.maxX)
            && !(var3 < var9.minZ)
            && !(var3 >= var9.maxZ)
            && !(var9.maxY < var5 - 1.5)
            && !(var9.maxY > var5 + 1.05)) {
            byte var10 = 0;

            for (Box var12 : var0) {
               if (var1 >= var12.minX
                  && var1 < var12.maxX
                  && var3 >= var12.minZ
                  && var3 < var12.maxZ
                  && var12.minY <= var9.maxY + 0.001
                  && var12.maxY > var9.maxY + 0.001) {
                  var10 = 1;
                  break;
               }
            }

            if (var10 == 0
               && (
                  var7 == null
                     || Math.abs(var9.maxY - var5) < Math.abs(var7.maxY - var5)
                     || Math.abs(var9.maxY - var5) == Math.abs(var7.maxY - var5) && var9.maxY > var7.maxY
               )) {
               var7 = var9;
            }
         }
      }

      return var7 == null ? null : new ng.Type_n(var7.minX, var7.minZ, var7.maxX, var7.maxZ, var7.maxY);
   }

   private ng() {
   }

   public static record Type_d(Vec3d a, Vec3d b, Vec3d c) {
   }

   public static record Type_e(List<ng.Type_p> outline, List<ng.Type_d> fill) {
   }

   public static record Type_n(double minX, double minZ, double maxX, double maxZ, double y) {
   }

   public static record Type_p(Vec3d from, Vec3d to) {
   }

   @FunctionalInterface
   public interface Type_u {
      ng.Type_n wAsNg_Type_n(double var1, double var3);
   }
}
