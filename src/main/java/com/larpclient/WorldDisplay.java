package com.larpclient;

import java.nio.ByteBuffer;
import java.util.Arrays;
import net.minecraft.util.math.Vec3d;

public final class WorldDisplay {
   public static final int t = 48;
   public static final int W = 800;
   private final Vec3d[] m = new Vec3d[47];
   private final double[] o = new double[47];
   private int i;
   private int X;
   private int L = -1;
   private Vec3d H;
   private Vec3d g;
   private Vec3d w;
   private double l = Double.NaN;

   public WorldDisplay() {
   }

   public int dAsInt() {
      return this.i;
   }

   public void TAsVoid() {
      Arrays.fill(this.m, null);
      this.i = this.X = 0;
      this.H = this.g = this.w = null;
      this.L = -1;
      this.l = Double.NaN;
   }

   public void vAsVoid(Vec3d var1, boolean var2, double var3, double var5) {
      if (Double.isFinite(var3) && Double.isFinite(var1.x + var1.y + var1.z)) {
         if (Double.isFinite(this.l) && (var3 < this.l || this.H != null && this.H.squaredDistanceTo(var1) > 9.0)) {
            this.TAsVoid();
         }

         this.l = var3;
         this.H = var1;

         for (int var7 = 0; var7 < this.m.length; var7++) {
            if (this.m[var7] != null && var3 - this.o[var7] >= var5) {
               this.m[var7] = null;
               this.i--;
            }
         }

         this.w = var2 ? var1 : null;
         if (!var2) {
            this.g = null;
         } else if (this.g != null && this.g.squaredDistanceTo(var1) < 0.2025 && this.m[this.L] != null) {
            this.o[this.L] = var3;
         } else {
            if (this.m[this.X] == null) {
               this.i++;
            }

            this.m[this.X] = var1;
            this.o[this.X] = var3;
            this.L = this.X;
            this.X = (this.X + 1) % this.m.length;
            this.g = var1;
         }
      } else {
         this.TAsVoid();
      }
   }

   public void DAsVoid(ByteBuffer var1, Vec3d var2, Vec3d var3, double var4, double var6, boolean var8) {
      int var9 = 0;
      float var10 = Float.POSITIVE_INFINITY;
      float var11 = Float.POSITIVE_INFINITY;
      float var12 = Float.NEGATIVE_INFINITY;
      float var13 = Float.NEGATIVE_INFINITY;
      int var14 = var1.position();
      var1.position(var14 + 32);

      for (int var15 = -1; var15 < this.m.length; var15++) {
         Vec3d var16 = var15 < 0 ? (this.w == null ? null : var3) : this.m[var15];
         float var17 = var15 < 0 ? 1.0F : (float)Math.clamp(1.0 - (var4 - this.o[var15]) / Math.max(0.1, var6), 0.0, 1.0);
         var17 = var17 >= 0.35F ? 1.0F : var17 / 0.35F;
         if (var8 && var16 != null && !(var17 <= 0.0F) && !(var16.squaredDistanceTo(var2) > 9216.0)) {
            float var18 = (float)(var16.x - var2.x);
            float var19 = (float)(var16.y - var2.y);
            float var20 = (float)(var16.z - var2.z);
            var1.putFloat(var18).putFloat(var19).putFloat(var20).putFloat(var17);
            var10 = Math.min(var10, var18);
            var12 = Math.max(var12, var18);
            var11 = Math.min(var11, var20);
            var13 = Math.max(var13, var20);
            var9++;
         }
      }

      for (int var21 = var9; var21 < 48; var21++) {
         var1.putLong(0L).putLong(0L);
      }

      int var22 = var1.position();
      var1.position(var14);
      var1.putFloat((float)var9).putFloat(0.0F).putFloat(0.0F).putFloat(0.0F);
      var1.putFloat(var9 == 0 ? 0.0F : var10 - 1.8F)
         .putFloat(var9 == 0 ? 0.0F : var11 - 1.8F)
         .putFloat(var9 == 0 ? 0.0F : var12 + 1.8F)
         .putFloat(var9 == 0 ? 0.0F : var13 + 1.8F);
      var1.position(var22);
   }
}
