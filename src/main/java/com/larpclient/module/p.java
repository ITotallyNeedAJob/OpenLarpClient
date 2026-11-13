package com.larpclient.module;

import java.util.SplittableRandom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class p implements h {
   private final SplittableRandom l = new SplittableRandom();
   private final a b = new a(com.larpclient.module.a.A);
   private final z w = new z();
   private boolean i;
   private float Q;
   private float q;
   private float s;
   private float M;
   private float m;
   private float W;
   private float a;
   private float R;
   private int v;

   public p() {
   }

   @Override
   public RotationAngles rotate(RotationAngles var1, RotationAngles var2, LivingEntity var3, boolean var4, boolean var5) {
      ClientPlayerEntity var6 = MinecraftClient.getInstance().player;
      if (var1 != null && var2 != null && var3 != null && var6 != null) {
         int var7 = !var4 && var5 ? 0 : 1;
         this.b.qAsVoid();
         double var8 = var3.getX() - var6.getX();
         double var10 = var3.getZ() - var6.getZ();
         double var12 = var8 * var8 + var10 * var10;
         float var14 = lk.aAsFloat(var1.n, this.CAsFloat(var1, var2, var12, var7 != 0));
         float var15 = MathHelper.clamp(var2.m, -90.0F, 90.0F);
         float var16 = var14 - var1.n;
         float var17 = var15 - var1.m;
         float var18 = (float)Math.hypot((double)var16, (double)var17);
         if (var7 != 0) {
            this.R = 0.0F;
            this.v = 0;
         } else if (var18 > 25.0F && this.v <= 0 && this.l.nextDouble() < 0.18) {
            this.R = (float)(this.l.nextDouble() * 2.5 - 1.25);
            this.v = this.l.nextInt(4, 10);
         } else if (this.v > 0) {
            this.v--;
            this.R *= 0.65F;
         } else {
            this.R = 0.0F;
         }

         float var19 = var7 != 0 ? 0.0F : this.DAsFloat(var18, var12, var4, var5);
         this.rAsVoid(var19);
         this.LAsVoid(var3, var14 + this.R, var15, var19);
         float var20 = this.b.AAsFloat(var18, var7 != 0);
         float var21 = this.b.OAsFloat(com.larpclient.module.a.Type_b.YAW) * 0.07F;
         float var22 = this.b.OAsFloat(com.larpclient.module.a.Type_b.PITCH) * 0.055F;
         float var23 = this.b.pAsFloat(com.larpclient.module.a.Type_b.YAW, var7 != 0);
         float var24 = this.b.pAsFloat(com.larpclient.module.a.Type_b.PITCH, var7 != 0);
         float var25 = this.KAsFloat(this.W - var1.n, 20.0F, 54.0F, 42.0F, var20, var21, var23, var7 != 0, var5);
         float var26 = this.KAsFloat(this.a - var1.m, 14.0F, 42.0F, 28.0F, var20, var22, var24, var7 != 0, var5);
         if (var7 == 0) {
            var26 += this.b.dAsFloat(var25);
         }

         return new RotationAngles(var1.n + var25, MathHelper.clamp(var1.m + var26, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   public RotationAngles vAsL6(RotationAngles var1, RotationAngles var2) {
      return this.w.MAsL6(var1, var2);
   }

   private float CAsFloat(RotationAngles var1, RotationAngles var2, double var3, boolean var5) {
      if (!var5 && !(var3 >= 0.16F)) {
         return this.i ? this.Q : var1.n;
      } else {
         this.Q = var2.n;
         this.i = true;
         return this.Q;
      }
   }

   private float DAsFloat(float var1, double var2, boolean var4, boolean var5) {
      if (!(var2 < 0.16F) && (var4 || var5)) {
         float var6 = 1.0F - MathHelper.clamp(var1 / 14.0F, 0.0F, 1.0F);
         return var6 * var6 * (var4 ? 0.35F : 0.65F);
      } else {
         return 0.0F;
      }
   }

   private void rAsVoid(float var1) {
      if (var1 <= 1.0E-5F) {
         this.q *= 0.5F;
         this.s *= 0.5F;
         this.M = 0.0F;
         this.m = 0.0F;
      } else {
         float var2 = this.wAsFloat() * (0.22F + var1 * 0.22F);
         float var3 = this.wAsFloat() * (0.15F + var1 * 0.15F);
         this.M = MathHelper.clamp(this.M * 0.68F + var2, -0.65F, 0.65F);
         this.m = MathHelper.clamp(this.m * 0.68F + var3, -0.45F, 0.45F);
         this.q = MathHelper.clamp(this.q + this.M, -4.1F, 4.1F);
         this.s = MathHelper.clamp(this.s + this.m, -2.85F, 2.85F);
      }
   }

   private void LAsVoid(LivingEntity var1, float var2, float var3, float var4) {
      if (var4 <= 1.0E-5F) {
         this.W = var2;
         this.a = var3;
      } else {
         float var5 = var2 + this.q * var4;
         float var6 = MathHelper.clamp(var3 + this.s * var4, -90.0F, 90.0F);
         if (jAsBoolean(var1, var5, var6)) {
            this.W = var5;
            this.a = var6;
         } else {
            this.q *= 0.5F;
            this.s *= 0.5F;
            var5 = var2 + this.q * var4;
            var6 = MathHelper.clamp(var3 + this.s * var4, -90.0F, 90.0F);
            if (jAsBoolean(var1, var5, var6)) {
               this.W = var5;
               this.a = var6;
            } else {
               this.q = this.s = this.M = this.m = 0.0F;
               this.W = var2;
               this.a = var3;
            }
         }
      }
   }

   private float KAsFloat(float var1, float var2, float var3, float var4, float var5, float var6, float var7, boolean var8, boolean var9) {
      float var10 = Math.abs(var1);
      if (var10 <= 1.0E-5F) {
         return 0.0F;
      } else {
         float var11 = MathHelper.clamp(var10 / var4, 0.0F, 1.0F);
         float var12 = var11 * var11 * (3.0F - 2.0F * var11);
         float var13 = Math.min(var10, var2 + (var3 - var2) * var12);
         float var14;
         if (!var8 && var9) {
            float var15 = 0.78F + (float)this.l.nextDouble() * 0.22000003F;
            var14 = MathHelper.clamp(var15 * var5 * var7 + var6, 0.65F, 1.08F);
         } else {
            var14 = MathHelper.clamp(var7 + var6 * 0.18F, 0.92F, 1.15F);
         }

         float var16;
         if (!var8 && var9) {
            var16 = Math.min(var10, Math.min(var3, var13 * var14));
         } else {
            var16 = Math.min(var10, Math.min(var3, Math.max(var13 * var14, var10 * 0.93F)));
         }

         return Math.copySign(var16, var1);
      }
   }

   private float wAsFloat() {
      return (float)(this.l.nextDouble() * 2.0 - 1.0);
   }

   private static boolean jAsBoolean(LivingEntity var0, float var1, float var2) {
      ClientPlayerEntity var3 = MinecraftClient.getInstance().player;
      Box var4 = var0.getBoundingBox();
      double var5 = Math.min(0.0125, Math.max(0.0, var4.getLengthX() * 0.25));
      double var7 = Math.min(0.0125, Math.max(0.0, var4.getLengthY() * 0.25));
      double var9 = Math.min(0.0125, Math.max(0.0, var4.getLengthZ() * 0.25));
      Box var11 = new Box(var4.minX + var5, var4.minY + var7, var4.minZ + var9, var4.maxX - var5, var4.maxY - var7, var4.maxZ - var9);
      Vec3d var12 = var3.getEyePos();
      if (var11.contains(var12)) {
         return true;
      } else {
         Vec3d var13 = lk.MAsVec3d(var1, var2);
         double var14 = var12.distanceTo(var11.getCenter()) + Math.max(var11.getLengthX(), Math.max(var11.getLengthY(), var11.getLengthZ())) + 1.0;
         return var11.raycast(var12, var12.add(var13.multiply(var14))).isPresent();
      }
   }

   @Override
   public float yawSpeed() {
      return 22.0F;
   }

   @Override
   public float pitchSpeed() {
      return 14.0F;
   }

   @Override
   public void reset() {
      this.i = false;
      this.Q = 0.0F;
      this.q = this.s = this.M = this.m = 0.0F;
      this.W = this.a = 0.0F;
      this.R = 0.0F;
      this.v = 0;
      this.b.VAsVoid();
      this.w.vAsVoid();
   }

   @Override
   public void targetChanged(LivingEntity var1) {
      this.reset();
   }
}
