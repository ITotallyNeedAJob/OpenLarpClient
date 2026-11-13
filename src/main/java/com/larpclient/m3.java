package com.larpclient;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class m3 {
   public static m3.Type_d kAsM3_Type_d(Vec3d var0, float var1, float var2) {
      double var3 = Math.toRadians((double)var1);
      Vec3d var5 = new Vec3d(-Math.sin(var3), 0.0, Math.cos(var3));
      Vec3d var6 = new Vec3d(var5.x, Math.clamp(-Math.tan(Math.toRadians((double)var2)), -5.0, 5.0), var5.z);
      return new m3.Type_d(var0.add(var5.multiply(0.3)), var6.multiply(0.6 / var6.length() + 0.5));
   }

   public static double rAsDouble(Vec3d var0, Vec3d var1, Box var2) {
      return var2.contains(var0) ? 0.0 : var2.raycast(var0, var1).<Double>map(var0::squaredDistanceTo).orElse(Double.POSITIVE_INFINITY);
   }

   public static double XAsDouble(int var0) {
      return Math.clamp((double)(var0 - 1) / 20.0, 0.0, 0.3);
   }

   public static Vec3d EAsVec3d(Vec3d var0, int var1) {
      double var2 = var0.length();
      return var0.multiply((var2 > 0.6 ? 0.6 / var2 : 1.0) * (double)Math.min(var1, 6));
   }

   public static boolean kAsBoolean(double var0, double var2, double var4) {
      return Double.isFinite(var0) && var0 > var2 + 0.75 && var0 <= var4;
   }

   public static boolean EAsBoolean(Vec3d var0, Vec3d var1) {
      return var0.multiply(1.0, 0.0, 1.0).normalize().dotProduct(var1.multiply(1.0, 0.0, 1.0)) < -0.02;
   }

   private m3() {
   }

   public static record Type_d(Vec3d position, Vec3d velocity) {
      public Vec3d collisionEnd() {
         return this.position.add(this.velocity);
      }

      public m3.Type_d next() {
         Vec3d var1 = this.velocity.add(0.0, -0.03, 0.0);
         return new m3.Type_d(this.position.add(var1), var1.multiply(0.92));
      }
   }
}
