package com.larpclient;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class ProjectionUtil {
   public static ProjectionUtil.Type_e vAsD5_Type_e(Vec3d var0, Vec3d var1, Matrix4f var2, Vector3f var3, int var4, int var5, boolean var6) {
      Vector4f var7 = var2.transform(new Vector4f((float)(var0.x - var1.x), (float)(var0.y - var1.y), (float)(var0.z - var1.z), 1.0F));
      if (!(var7.w <= 1.0E-4F) && !(var7.z > var7.w) && !(var7.z < (var6 ? 0.0F : -var7.w)) && !(Math.abs(var7.x) > var7.w) && !(Math.abs(var7.y) > var7.w)) {
         Vector4f var8 = var2.transform(new Vector4f(var3.x, var3.y, var3.z, 0.0F));
         float var9 = (var7.x / var7.w + 1.0F) * (float)var4 * 0.5F;
         float var10 = (1.0F - var7.y / var7.w) * (float)var5 * 0.5F;
         float var11 = var7.w + var8.w;
         if (var11 <= 1.0E-4F) {
            return null;
         } else {
            float var12 = ((var7.x + var8.x) / var11 - var7.x / var7.w) * (float)var4 * 0.5F;
            float var13 = ((var7.y + var8.y) / var11 - var7.y / var7.w) * (float)var5 * 0.5F;
            float var14 = (float)Math.hypot((double)var12, (double)var13);
            return Float.isFinite(var9) && Float.isFinite(var10) && Float.isFinite(var14) && var14 > 0.0F ? new ProjectionUtil.Type_e(var9, var10, var14) : null;
         }
      } else {
         return null;
      }
   }

   public static ProjectionUtil.Type_s dAsD5_Type_s(Box var0, Vec3d var1, Matrix4f var2, int var3, int var4, boolean var5) {
      if (var0.contains(var1)) {
         return null;
      } else {
         Vector4f[] var6 = new Vector4f[8];

         for (int var7 = 0; var7 < 8; var7++) {
            var6[var7] = var2.transform(
               new Vector4f(
                  (float)(((var7 & 1) == 0 ? var0.minX : var0.maxX) - var1.x),
                  (float)(((var7 & 2) == 0 ? var0.minY : var0.maxY) - var1.y),
                  (float)(((var7 & 4) == 0 ? var0.minZ : var0.maxZ) - var1.z),
                  1.0F
               )
            );
         }

         float var21 = Float.POSITIVE_INFINITY;
         float var8 = Float.POSITIVE_INFINITY;
         float var9 = Float.NEGATIVE_INFINITY;
         float var10 = Float.NEGATIVE_INFINITY;

         for (int var11 = 0; var11 < 8; var11++) {
            for (int var12 = 1; var12 <= 4; var12 <<= 1) {
               if ((var11 & var12) == 0) {
                  Vector4f var13 = new Vector4f(var6[var11]);
                  Vector4f var14 = new Vector4f(var6[var11 | var12]);
                  if (cAsBoolean(var13, var14, var5)) {
                     for (Vector4f var18 : new Vector4f[]{var13, var14}) {
                        float var19 = (var18.x / var18.w + 1.0F) * (float)var3 * 0.5F;
                        float var20 = (1.0F - var18.y / var18.w) * (float)var4 * 0.5F;
                        var21 = Math.min(var21, var19);
                        var9 = Math.max(var9, var19);
                        var8 = Math.min(var8, var20);
                        var10 = Math.max(var10, var20);
                     }
                  }
               }
            }
         }

         if (Float.isFinite(var21) && !(var9 < 0.0F) && !(var10 < 0.0F) && !(var21 > (float)var3) && !(var8 > (float)var4)) {
            int var22 = (int)Math.floor((double)Math.clamp(var21, 0.0F, (float)var3));
            int var23 = (int)Math.ceil((double)Math.clamp(var9, 0.0F, (float)var3));
            int var24 = (int)Math.floor((double)Math.clamp(var8, 0.0F, (float)var4));
            int var25 = (int)Math.ceil((double)Math.clamp(var10, 0.0F, (float)var4));
            return var23 - var22 >= 2 && var25 - var24 >= 3 ? new ProjectionUtil.Type_s(var22, var24, var23, var25) : null;
         } else {
            return null;
         }
      }
   }

   public static ProjectionUtil.Type_w CAsD5_Type_w(Vec3d var0, Vec3d var1, Vec3d var2, Matrix4f var3, int var4, int var5, boolean var6) {
      Vector4f var7 = var3.transform(new Vector4f((float)(var0.x - var2.x), (float)(var0.y - var2.y), (float)(var0.z - var2.z), 1.0F));
      Vector4f var8 = var3.transform(new Vector4f((float)(var1.x - var2.x), (float)(var1.y - var2.y), (float)(var1.z - var2.z), 1.0F));
      if (!cAsBoolean(var7, var8, var6)) {
         return null;
      } else {
         float var9 = (var7.x / var7.w + 1.0F) * (float)var4 * 0.5F;
         float var10 = (1.0F - var7.y / var7.w) * (float)var5 * 0.5F;
         float var11 = (var8.x / var8.w + 1.0F) * (float)var4 * 0.5F;
         float var12 = (1.0F - var8.y / var8.w) * (float)var5 * 0.5F;
         return Float.isFinite(var9)
               && Float.isFinite(var10)
               && Float.isFinite(var11)
               && Float.isFinite(var12)
               && !(Math.max(var9, var11) < 0.0F)
               && !(Math.max(var10, var12) < 0.0F)
               && !(Math.min(var9, var11) > (float)var4)
               && !(Math.min(var10, var12) > (float)var5)
            ? new ProjectionUtil.Type_w(var9, var10, var11, var12)
            : null;
      }
   }

   private static boolean cAsBoolean(Vector4f var0, Vector4f var1, boolean var2) {
      for (int var3 = 0; var3 < 3; var3++) {
         float var4 = var3 == 0 ? var0.w - 1.0E-4F : (var3 == 1 ? (var2 ? var0.z : var0.w + var0.z) : var0.w - var0.z);
         float var5 = var3 == 0 ? var1.w - 1.0E-4F : (var3 == 1 ? (var2 ? var1.z : var1.w + var1.z) : var1.w - var1.z);
         if (var4 < 0.0F && var5 < 0.0F) {
            return false;
         }

         if (var4 < 0.0F) {
            var0.lerp(var1, var4 / (var4 - var5));
         } else if (var5 < 0.0F) {
            var1.lerp(var0, var5 / (var5 - var4));
         }
      }

      return true;
   }

   private ProjectionUtil() {
   }

   public static record Type_e(float x, float y, float scale) {
   }

   public static record Type_s(int left, int top, int right, int bottom) {
   }

   public static record Type_w(float x1, float y1, float x2, float y2) {
   }
}
