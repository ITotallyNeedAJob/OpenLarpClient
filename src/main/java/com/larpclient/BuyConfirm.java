package com.larpclient;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class BuyConfirm {
   private final ParticleEmitterEngine e = new ParticleEmitterEngine();
   private final RibbonTrailEngine m = new RibbonTrailEngine();
   private final Random k = new Random();
   private final Vec3d[] C = new Vec3d[32];
   private final Vec3d[] r = new Vec3d[32];
   private final Vec3d[] X = new Vec3d[32];
   private final Vec3d[] J = new Vec3d[32];
   private final double[] M = new double[32];
   private final double[] p = new double[32];
   private final double[] i = new double[32];
   private final int[] n = new int[32];
   private final BuyConfirm.Type_u[] O = new BuyConfirm.Type_u[32];
   private int L;
   private boolean T;
   private final EffectRenderParams K = new EffectRenderParams();
   private Vec3d B;
   private BuyConfirm.Type_d u;
   private double P;
   private int a;

   public BuyConfirm() {
      this.e.B = 1536;
      this.m.x = true;
      this.K.O = 1536;
      this.K.K = 4;
      this.K.a = this.K.D = this.K.b = 0.0;
      this.K.t = FadeCurve.Type_r.FADE_IN_OUT;
      this.K.o = FadeCurve.Type_z.PULSE;
   }

   public void eAsVoid() {
      this.e.JAsVoid();
      this.m.FAsVoid();
      Arrays.fill(this.C, null);
      Arrays.fill(this.r, null);
      Arrays.fill(this.X, null);
      Arrays.fill(this.J, null);
      Arrays.fill(this.M, 0.0);
      Arrays.fill(this.p, 0.0);
      Arrays.fill(this.i, 0.0);
      Arrays.fill(this.n, 0);
      Arrays.fill(this.O, null);
      this.L = 0;
      this.T = false;
      this.P = 0.0;
      this.a = 0;
      this.B = null;
      this.u = null;
   }

   private Vec3d PAsVec3d(Vec3d var1, BuyConfirm.Type_d var2) {
      return iAsVec3d(var1, var2.radius, var2.height, this.k);
   }

   public static Vec3d iAsVec3d(Vec3d var0, double var1, double var3, Random var5) {
      double var6 = var5.nextDouble() * Math.PI * 2.0;
      double var8 = Math.sqrt(0.04 + var5.nextDouble() * 0.96) * var1;
      return var0.add(Math.cos(var6) * var8, (var5.nextDouble() - 0.5) * var3, Math.sin(var6) * var8);
   }

   public void GAsVoid(Vec3d var1, BuyConfirm.Type_d var2) {
      if (this.B != null
         && (
            var1.squaredDistanceTo(this.B) > 4096.0
               || this.u.sparkles != var2.sparkles
               || this.u.comets != var2.comets
               || this.u.radius != var2.radius
               || this.u.height != var2.height
               || this.u.cometCount != var2.cometCount
               || this.u.maximum != var2.maximum
               || this.u.verticalTravel != var2.verticalTravel
               || this.u.sidewaysTravel != var2.sidewaysTravel
               || this.u.motionPeriod != var2.motionPeriod
         )) {
         this.eAsVoid();
      }

      int var3 = this.B == null ? 1 : 0;
      this.B = var1;
      this.u = var2;
      this.a++;
      int var4 = var2.comets ? Math.clamp((long)var2.cometCount, 0, this.C.length) : 0;
      this.e.B = Math.clamp((long)var2.maximum, 128, 2048) - var4;
      this.e.IAsVoid(var2.speed);
      this.m.DAsVoid();
      this.K.I = ColorUtil.XAsInt(var2.color, var2.opacity);
      this.K.A = ColorUtil.XAsInt(var2.tailColor, var2.opacity * 0.35);
      this.K.i = var2.bloom;
      this.K.d = var2.radius * 2.0 + var2.height + 8.0;
      if (var2.sparkles) {
         int var5 = this.e.B - var4 * 3;
         this.K.O = 2048;
         this.P = this.P + var2.rate * 0.05;
         int var6 = (int)this.P + (var3 != 0 ? (int)Math.min((double)var5, var2.rate * var2.lifetime * 0.825) : 0);
         this.P = this.P - (double)((int)this.P);
         this.K.x = var2.lifetime * 0.65;
         this.K.v = var2.lifetime;
         this.K.g = var2.size * 0.6;
         this.K.R = var2.size;
         this.K.t = FadeCurve.Type_r.FADE_IN_OUT;
         this.K.o = FadeCurve.Type_z.PULSE;
         this.K.w = this.K.L = 0.0;
         this.K.C = var2.verticalTravel * 0.5;
         this.K.r = var2.sidewaysTravel * 0.5;
         this.K.S = var2.motionPeriod;
         this.K.M = 1;
         int var7 = Math.max(0, var5 - this.e.UAsInt());

         for (int var8 = 0; var8 < Math.min(var7, var6); var8++) {
            this.K.q = (this.a + var8) % 3 == 0 ? ParticleEmitterEngine.Type_a.Star : ParticleEmitterEngine.Type_a.Glow;
            this.e.pAsVoid(this.PAsVec3d(var1, var2), this.K);
         }
      }

      for (int var11 = 0; var11 < this.C.length; var11++) {
         if (var11 >= var4) {
            this.C[var11] = this.X[var11] = this.J[var11] = null;
            this.m.tAsVoid(var11);
         } else {
            if (this.C[var11] != null
               && !(Math.hypot(this.X[var11].x - var1.x, this.X[var11].z - var1.z) > var2.radius * 1.4)
               && !(Math.abs(this.X[var11].y - var1.y) > var2.height * 0.7)
               && !(this.M[var11] > 12.0)) {
               this.J[var11] = this.C[var11];
            } else {
               this.m.tAsVoid(var11);
               this.X[var11] = this.PAsVec3d(var1, var2);
               double var12 = this.k.nextDouble() * Math.PI * 2.0;
               this.r[var11] = new Vec3d(Math.cos(var12), (this.k.nextDouble() - 0.5) * 0.4, Math.sin(var12));
               this.M[var11] = 0.0;
               this.n[var11] = 0;
               this.J[var11] = null;
               this.p[var11] = this.k.nextDouble() * Math.PI * 2.0;
            }

            this.n[var11]++;
            if (var2.speed > 0.0) {
               this.M[var11] = this.M[var11] + 0.05;
            }

            this.p[var11] = this.p[var11] + 0.05 * var2.speed * Math.PI * 2.0 / var2.motionPeriod;
            this.X[var11] = this.X[var11].add(this.r[var11].multiply(var2.speed * 0.01));
            double var13 = this.p[var11];
            this.C[var11] = this.X[var11]
               .add(
                  Math.sin(var13 * 0.83) * var2.sidewaysTravel * 0.5,
                  Math.sin(var13) * var2.verticalTravel * 0.5,
                  Math.cos(var13 * 0.67) * var2.sidewaysTravel * 0.5
               );
            double var14 = this.i[var11] = UAsDouble((double)this.n[var11] * 0.05, this.M[var11], this.X[var11], var1, var2);
            RibbonTrailEngine.Type_f var10 = new RibbonTrailEngine.Type_f(
               var2.trailLength,
               var2.trailWidth,
               ColorUtil.XAsInt(var2.color, var2.opacity * var14),
               ColorUtil.XAsInt(var2.tailColor, var2.opacity * 0.3 * var14),
               var2.bloom,
               true,
               EffectRenderParams.Type_g.ADDITIVE,
               RibbonTrailEngine.Type_b.HEAD_HEAVY,
               FadeCurve.Type_r.LINEAR
            );
            this.m.CAsVoid(var11, this.C[var11], var10);
            this.K.q = ParticleEmitterEngine.Type_a.Glow;
            this.K.O = 2048;
            this.K.M = 1;
            this.K.x = this.K.v = 0.15;
            this.K.g = this.K.R = var2.size * 1.6;
            this.K.t = FadeCurve.Type_r.LINEAR;
            this.K.o = FadeCurve.Type_z.SHRINK;
            this.K.I = ColorUtil.XAsInt(var2.color, var2.opacity * var14);
            this.K.A = ColorUtil.XAsInt(var2.tailColor, var2.opacity * 0.35 * var14);
            this.K.w = this.K.L = 0.0;
            this.K.C = this.K.r = 0.0;
            this.e.pAsVoid(this.C[var11], this.K);
         }
      }
   }

   public void BAsVoid(Vec3d var1, float var2, Frustum var3) {
      this.e.fAsVoid(var1, var2, var3);
      this.m.TAsVoid(var1, var2);
      this.L = 0;
      this.T = false;
      if (this.u != null) {
         double var4 = this.u.radius * 2.0 + this.u.height + 8.0;

         for (int var6 = 0; var6 < this.C.length; var6++) {
            if (this.C[var6] != null) {
               Vec3d var7 = this.J[var6] == null ? this.C[var6] : this.J[var6].lerp(this.C[var6], (double)var2);
               if (!(var7.squaredDistanceTo(var1) > var4 * var4)) {
                  double var8 = this.u.size * 0.65;
                  if (var3 == null || var3.isVisible(new Box(var7.x - var8, var7.y - var8, var7.z - var8, var7.x + var8, var7.y + var8, var7.z + var8))) {
                     int var10 = ColorUtil.XAsInt(this.u.color, this.u.opacity * this.i[var6]);
                     var10 = ColorUtil.xAsInt(var10, var10 & 0xFF000000 | 16777215, 0.7);
                     if (var10 >>> 24 != 0) {
                        this.O[this.L++] = new BuyConfirm.Type_u(var7, var8, var10);
                     }
                  }
               }
            }
         }

         this.T = this.L > 0 && this.u.bloom;
      }
   }

   public boolean qAsBoolean() {
      return this.e.yAsBoolean() || this.m.lAsBoolean() || this.T;
   }

   public boolean vAsBoolean() {
      return this.e.oAsBoolean();
   }

   public int gAsInt() {
      return this.e.UAsInt() + this.cAsInt();
   }

   public int cAsInt() {
      int var1 = 0;

      for (Vec3d var5 : this.C) {
         if (var5 != null) {
            var1++;
         }
      }

      return var1;
   }

   public int aAsInt() {
      return this.m.rAsInt();
   }

   public Vec3d ZAsVec3d(int var1) {
      return this.C[var1];
   }

   public Vec3d lAsVec3d(int var1) {
      return this.O[var1].position;
   }

   public int vAsInt() {
      return this.L;
   }

   public double qAsDouble(int var1) {
      return this.i[var1];
   }

   public void AAsVoid(EffectGeometryRenderer var1, Vec3d var2, float var3, boolean var4) {
      this.e.MAsVoid(var1, var2, var3, var4);
      this.m.RAsVoid(var1, var3, var4);
      if (!var4 || this.T) {
         var1.blend(EffectRenderParams.Type_g.ADDITIVE);

         for (int var5 = 0; var5 < this.L; var5++) {
            BuyConfirm.Type_u var6 = this.O[var5];
            var1.cAsVoid(var6.position.x, var6.position.y, var6.position.z, var6.size, 0.0, var6.color, ParticleEmitterEngine.Type_a.Circle, 0.0, 0.0, 0.0);
         }

         var1.blend(EffectRenderParams.Type_g.ALPHA);
      }
   }

   private static double UAsDouble(double var0, double var2, Vec3d var4, Vec3d var5, BuyConfirm.Type_d var6) {
      double var7 = Math.hypot(var4.x - var5.x, var4.z - var5.z);
      double var9 = Math.abs(var4.y - var5.y);
      return FadeCurve.RAsDouble(var0 / 0.15)
         * FadeCurve.RAsDouble((12.0 - var2) / 0.35)
         * FadeCurve.RAsDouble((var6.radius * 1.4 - var7) / (var6.radius * 0.15))
         * FadeCurve.RAsDouble((var6.height * 0.7 - var9) / (var6.height * 0.1));
   }

   public static record Type_d(
      boolean sparkles,
      boolean comets,
      double radius,
      double height,
      double rate,
      double lifetime,
      double speed,
      double size,
      int cometCount,
      double trailLength,
      double trailWidth,
      int color,
      int tailColor,
      double opacity,
      boolean bloom,
      double verticalTravel,
      double sidewaysTravel,
      double motionPeriod,
      int maximum
   ) {
   }

   private static record Type_u(Vec3d position, double size, int color) {
   }
}
