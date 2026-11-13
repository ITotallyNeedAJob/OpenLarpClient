package com.larpclient;

import com.larpclient.module.Aura;
import com.larpclient.module.RotationAngles;
import com.larpclient.module.z;
import java.util.SplittableRandom;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public final class x {
   static final double r = 0.546;
   static final double J = 0.4;
   static final double j = 1.5;
   private static final float k = 6.0F;
   private static final int O = Integer.MIN_VALUE;
   private final com.larpclient.module.a w = new com.larpclient.module.a(com.larpclient.module.a.A);
   private final z I = new z();
   private final SplittableRandom T = new SplittableRandom();
   private float K;
   private float a;
   private float n;
   private boolean F;
   private boolean z;
   private int t = -1;
   private int C = Integer.MIN_VALUE;

   public x() {
      this.eAsVoid();
   }

   public void eAsVoid() {
      this.K = this.a = 0.0F;
      this.F = this.z = false;
      this.t = -1;
      this.C = Integer.MIN_VALUE;
      this.n = this.HAsFloat();
      this.w.VAsVoid();
      this.I.vAsVoid();
   }

   public float[] eAsFloatArray(float var1, float var2, float var3, float var4, boolean var5) {
      float var6 = MathHelper.wrapDegrees(var3 - var1);
      float var7 = MathHelper.clamp(var4, -90.0F, 90.0F) - var2;
      float var8 = (float)Math.hypot((double)var6, (double)var7);
      if (!var5 || !(var8 <= this.n) && (!this.F || !(var8 <= 3.5F))) {
         this.F = false;
         int var9 = var8 < 6.0F ? 1 : 0;
         this.w.qAsVoid();
         float var10 = this.w.AAsFloat(var8, var9 != 0);
         this.K = bAsFloat(
            var6,
            this.K,
            12.0F * this.w.pAsFloat(com.larpclient.module.a.Type_b.YAW, var9 != 0) * var10,
            3.0F,
            this.w.OAsFloat(com.larpclient.module.a.Type_b.YAW) * 0.08F
         );
         this.a = bAsFloat(
            var7,
            this.a,
            9.0F * this.w.pAsFloat(com.larpclient.module.a.Type_b.PITCH, var9 != 0) * var10,
            2.25F,
            this.w.OAsFloat(com.larpclient.module.a.Type_b.PITCH) * 0.06F
         );
         this.a = eAsFloat(this.a + this.w.dAsFloat(this.K), var7);
         RotationAngles var11 = this.I.MAsL6(new RotationAngles(var1, var2), new RotationAngles(var1 + this.K, var2 + this.a));
         return new float[]{var11.n, var11.m};
      } else {
         if (!this.F) {
            this.n = this.HAsFloat();
         }

         this.F = true;
         this.K = this.a = 0.0F;
         return new float[]{var1, var2};
      }
   }

   private static float bAsFloat(float var0, float var1, float var2, float var3, float var4) {
      float var5 = MathHelper.clamp(var0 * 0.32F * (1.0F + var4), -var2, var2);
      return eAsFloat(var1 + MathHelper.clamp(var5 - var1, -var3, var3), var0);
   }

   private static float eAsFloat(float var0, float var1) {
      return Math.signum(var0) != Math.signum(var1) ? 0.0F : Math.copySign(Math.min(Math.abs(var0), Math.abs(var1)), var1);
   }

   private float HAsFloat() {
      return 0.8F + (float)this.T.nextDouble() * 1.2F;
   }

   public boolean fAsBoolean(double var1, double var3, boolean var5) {
      if (var5 && !(var1 <= 0.0)) {
         if (this.z) {
            if (var1 <= jAsDouble(var3) + 0.04) {
               this.z = false;
            }
         } else if (var1 > 0.4) {
            if (this.t < 0) {
               this.t = this.T.nextInt(1, 5);
            } else if (--this.t <= 0) {
               this.z = true;
               this.t = -1;
            }
         } else {
            this.t = -1;
         }

         return this.z;
      } else {
         this.z = false;
         this.t = -1;
         return false;
      }
   }

   public boolean UAsBoolean() {
      return this.z;
   }

   public static double jAsDouble(double var0) {
      return Math.max(0.0, var0) * 0.546 / 0.45399999999999996;
   }

   public PlayerInput jAsPlayerInput(float var1, Vec3d var2) {
      double var3 = Math.toDegrees(Math.atan2(var2.z, var2.x)) - 90.0;
      double var5 = MathHelper.wrapDegrees(var3 - (double)var1);
      int var7 = (int)Math.round(var5 / 45.0);
      if (var7 == -4) {
         var7 = 4;
      }

      if (this.C != Integer.MIN_VALUE && Math.abs(MathHelper.wrapDegrees(var5 - (double)this.C * 45.0)) < 28.5) {
         var7 = this.C;
      }

      this.C = var7;
      float[] var8 = Aura.sAsFloatArray((double)var1 + (double)var7 * 45.0, var1);
      return new PlayerInput(var8[0] > 0.0F, var8[0] < 0.0F, var8[1] > 0.0F, var8[1] < 0.0F, false, false, false);
   }

   public static Vec2f sAsVec2f(PlayerInput var0) {
      float var1 = var0.forward() == var0.backward() ? 0.0F : (var0.forward() ? 1.0F : -1.0F);
      float var2 = var0.left() == var0.right() ? 0.0F : (var0.left() ? 1.0F : -1.0F);
      return new Vec2f(var2, var1).normalize();
   }

   public static double vAsDouble(Vec3d var0, Vec3d var1, Direction var2) {
      return (var1.x - var0.x) * (double)var2.getOffsetX() + (var1.z - var0.z) * (double)var2.getOffsetZ();
   }

   public static double VAsDouble(Vec3d var0, Vec3d var1, Direction var2) {
      Direction var3 = var2.rotateYClockwise();
      return (var1.x - var0.x) * (double)var3.getOffsetX() + (var1.z - var0.z) * (double)var3.getOffsetZ();
   }

   public static Vec3d WAsVec3d(Vec3d var0, Vec3d var1, Direction var2) {
      Direction var3 = var2.rotateYClockwise();
      double var4 = VAsDouble(var0, var1, var2);
      return new Vec3d(
         (double)var2.getOffsetX() * 1.5 + (double)var3.getOffsetX() * var4, 0.0, (double)var2.getOffsetZ() * 1.5 + (double)var3.getOffsetZ() * var4
      );
   }
}
