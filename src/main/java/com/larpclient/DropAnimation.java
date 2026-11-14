package com.larpclient;

import net.minecraft.util.math.Vec3d;

public final class DropAnimation {
   public static final double K = -0.03125;
   public static final int U = 80;
   public static final int X = 40;
   public static final int AuctionNotifier = 2;
   public static final double BoxUtils = 0.04;
   public static final double P = 0.02;
   private Vec3d SkyChunkCache = Vec3d.ZERO;
   private int v;
   private int Y;
   private int w;
   private double y;
   private boolean p;

   public DropAnimation() {
   }

   public void kAsVoid(double var1, boolean var3) {
      this.v = !var3 && !(var1 < -0.03125) ? this.v + 1 : 0;
   }

   public Vec3d TAsVec3d(Vec3d var1, double var2, int var4, int var5, double var6, double var8, boolean var10) {
      Vec3d var11 = new Vec3d(var1.x * var2, 0.0, var1.z * var2);
      if (var4 <= 0) {
         this.s = var11;
      } else {
         Vec3d var12 = var11.subtract(this.s);
         double var13 = Math.max(var2, this.s.length()) / (double)var4;
         double var15 = var12.length();
         this.s = var15 > var13 ? this.s.add(var12.multiply(var13 / var15)) : var11;
      }

      double var17 = var5 != 0 ? (double)var5 * var6 : -var8;
      if (var5 != 0) {
         this.y = 0.0;
      }

      if (!var10) {
         this.Y = 0;
         this.y = 0.0;
      } else if (this.Y == 0 && this.v >= 40) {
         this.Y = 2;
         this.w++;
      }

      this.p = this.Y > 0;
      if (this.p) {
         this.Y--;
         if (var17 > -0.04) {
            if (var5 == 0) {
               this.y += var17 + 0.04;
            }

            var17 = -0.04;
         }
      } else if (var5 == 0 && this.y > 0.0) {
         double var14 = Math.min(this.y, 0.02);
         this.y -= var14;
         var17 += var14;
      }

      return new Vec3d(this.s.x, var17, this.s.z);
   }

   public void aAsVoid() {
      this.s = Vec3d.ZERO;
   }

   public Vec3d eAsVec3d() {
      return this.s;
   }

   public int tAsInt() {
      return this.v;
   }

   public boolean uAsBoolean() {
      return this.p;
   }

   public int JAsInt() {
      return this.w;
   }
}
