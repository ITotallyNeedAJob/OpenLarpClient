package com.larpclient.module;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class RotationAngles {
   public float n;
   public float m;

   public RotationAngles(float var1, float var2) {
      this.n = var1;
      this.m = var2;
   }

   public RotationAngles mAsL6() {
      return new RotationAngles(this.n, this.m);
   }

   public float mAsFloat(RotationAngles var1) {
      return Math.abs(MathHelper.wrapDegrees(this.n - var1.n)) + Math.abs(MathHelper.wrapDegrees(this.m - var1.m));
   }

   public Vec3d rAsVec3d() {
      return lk.MAsVec3d(this.n, this.m);
   }

   @Override
   public String toString() {
      return "Rotation(yaw=" + this.n + ", pitch=" + this.m + ")";
   }
}
