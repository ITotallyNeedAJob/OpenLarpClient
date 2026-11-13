package com.larpclient;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class CelestialEffectEngine {
   public static final int f = 256;
   public static final int K = 32;
   public static final int V = 12;
   public static final int D = 1024;
   public static final int v = 4;
   private static final double p = 0.05;
   private final Random U;
   private final ParticleEmitterEngine M = new ParticleEmitterEngine();
   private final x4 o = new x4();
   private final CelestialEffectEngine.Type_r[] X = new CelestialEffectEngine.Type_r[256];
   private final CelestialEffectEngine.Type_c[] r = new CelestialEffectEngine.Type_c[256];
   private int L;
   private int b;
   private int G;
   private int F;
   private int I;
   private int g;
   private double Q;
   private double x;
   private double T;
   private Vec3d N;
   private Vec3d O = Vec3d.ZERO;
   private CelestialEffectEngine.Type_i C;
   private boolean l;
   private float B;

   public CelestialEffectEngine() {
      this(new Random());
   }

   public CelestialEffectEngine(Random var1) {
      this.U = var1;
      this.M.B = 1024;
      this.o.O = 2048;
      this.o.K = 7;

      for (int var2 = 0; var2 < this.X.length; var2++) {
         this.X[var2] = new CelestialEffectEngine.Type_r();
         this.r[var2] = new CelestialEffectEngine.Type_c();
      }
   }

   public void ZAsVoid() {
      for (CelestialEffectEngine.Type_r var4 : this.X) {
         Arrays.fill(var4.o, null);
         var4.m = var4.S = var4.E = null;
         var4.D = null;
      }

      for (CelestialEffectEngine.Type_c var8 : this.r) {
         Arrays.fill(var8.u, null);
         var8.i = 0;
      }

      this.M.JAsVoid();
      this.L = this.b = this.G = this.F = this.I = this.g = 0;
      this.Q = this.x = this.T = 0.0;
      this.N = null;
      this.C = null;
      this.l = false;
   }

   public void mAsVoid(Vec3d var1, CelestialEffectEngine.Type_i var2, boolean var3, lq.Type_a var4) {
      if (this.C != null && (this.C.style != var2.style || this.N.squaredDistanceTo(var1) > 4096.0)) {
         this.ZAsVoid();
      }

      if (this.C != null && (this.C.speed != var2.speed || this.C.impacts != var2.impacts)) {
         for (int var5 = 0; var5 < this.L; var5++) {
            this.X[var5].D = null;
            this.X[var5].d = 0.0;
         }
      }

      this.N = var1;
      this.C = var2;
      this.F = 0;
      this.T = this.x;
      this.x = this.x + Math.clamp((double)(var3 ? 1 : 0) - this.x, -0.075, 0.075);
      this.M.IAsVoid(var2.speed);
      if (var2.impacts && var4 != null && var2.speed > 0.0) {
         int var13 = 0;

         while (var13++ < this.L && this.F < 4 && this.L > 0) {
            if (this.G >= this.L) {
               this.G = 0;
            }

            CelestialEffectEngine.Type_r var6 = this.X[this.G++];
            if (var6.k && var6.D == null && !(var6.d > var6.H + 0.15)) {
               double var7 = var6.E.length() * var2.speed;
               double var9 = Math.min(var6.A, var6.H + 7.5 / Math.max(0.01, var7));
               Vec3d var11 = var6.m;
               Vec3d var12 = var6.m.add(var6.E.multiply((var9 - var6.H) * var2.speed));
               this.F++;
               var6.D = var4.HAsLq_Type_l(var11, var12);
               var6.d = var9;
               if (var6.D != null) {
                  var6.t = var6.H + var11.distanceTo(var6.D.point()) / var7;
               }
            }
         }
      }

      int var14 = 0;

      while (var14 < this.L) {
         CelestialEffectEngine.Type_r var16 = this.X[var14];
         var16.S = var16.m;
         var16.H += 0.05;
         var16.m = var16.m.add(var16.E.multiply(0.05 * var2.speed));
         if (var16.D != null && var16.H >= var16.t) {
            this.mAsVoid(var16.D, var16.r);
            this.sAsVoid(var14);
         } else {
            if (var2.style == CelestialEffectEngine.Type_z.METEOR_SHOWER && !var16.I && var16.H >= var16.A * 0.62) {
               var16.I = true;
               if (var16.e > 0.65) {
                  this.TAsVoid(var16);
                  var16.A = Math.min(var16.A, var16.H + 0.28);
               }
            }

            if (!(var16.H >= var16.A) && !(var16.m.squaredDistanceTo(this.N) > Math.pow(var2.distance * 1.3, 2.0))) {
               if (var2.style == CelestialEffectEngine.Type_z.METEOR_SHOWER) {
                  System.arraycopy(var16.o, 0, var16.o, 1, 11);
                  var16.o[0] = var16.S;
                  var16.P = Math.min(12, var16.P + 1);
               }

               var14++;
            } else {
               this.sAsVoid(var14);
            }
         }
      }

      var14 = var2.style == CelestialEffectEngine.Type_z.METEOR_SHOWER ? Math.min(32, Math.max(1, var2.maximum * 32 / 256)) : Math.min(var2.maximum, 256);
      double var17 = var2.density
         * (var2.style == CelestialEffectEngine.Type_z.METEOR_SHOWER ? 2.5 : (var2.style == CelestialEffectEngine.Type_z.STARDUST ? 42.0 : 62.0));
      this.Q = Math.min(8.0, this.Q + var17 * 0.05 * this.x);
      int var8 = (int)this.Q;
      this.Q -= (double)var8;
      if (var2.style == CelestialEffectEngine.Type_z.STARDUST) {
         int var18 = Math.min(928, Math.max(32, var2.maximum * 3));

         for (int var10 = 0; var10 < var8 && this.M.UAsInt() < var18; var10++) {
            this.FAsVoid();
         }
      } else {
         for (int var19 = 0; var19 < var8 && this.L < var14; var19++) {
            this.UAsVoid();
         }
      }

      op.FAsVoid("Celestial active flights", (long)this.L);
      op.FAsVoid("Celestial sprites", (long)this.M.UAsInt());
      op.FAsVoid("Celestial terrain queries", (long)this.F);
   }

   private void sAsVoid(int var1) {
      CelestialEffectEngine.Type_r var2 = this.X[var1];
      this.X[var1] = this.X[--this.L];
      this.X[this.L] = var2;
      var2.D = null;
      Arrays.fill(var2.o, null);
      var2.P = 0;
   }

   private void UAsVoid() {
      CelestialEffectEngine.Type_r var1 = this.X[this.L++];
      int var2 = this.C.style == CelestialEffectEngine.Type_z.METEOR_SHOWER ? 1 : 0;
      var1.k = this.C.impacts && this.U.nextDouble() < (var2 != 0 ? 0.23 : 0.16);
      double var3 = 0.65 + (this.U.nextDouble() - 0.5) * (var2 != 0 ? 0.17 : 0.08);
      double var5 = this.C.wind;
      var1.E = var2 != 0
         ? new Vec3d(Math.cos(var3) * (18.0 + this.U.nextDouble() * 7.0), -8.0 - this.U.nextDouble() * 4.0, Math.sin(var3) * (18.0 + this.U.nextDouble() * 7.0))
            .add(var5, 0.0, var5 * 0.4)
         : new Vec3d(var5 * 0.75, -10.0 - this.U.nextDouble() * 5.0, var5 * 0.3);
      Vec3d var7;
      double var8;
      if (var2 != 0) {
         var7 = this.N.add(-var1.E.x * 0.65, var1.k ? 8.0 : Math.min(32.0, this.C.radius * 0.7), -var1.E.z * 0.65);
         var8 = var1.k ? 8.0 : 18.0;
      } else {
         var7 = this.N.add(0.0, 9.0, 0.0);
         var8 = 16.0;
      }

      var1.m = var1.S = b9.iAsVec3d(var7, var1.k ? Math.min(12.0, this.C.radius) : this.C.radius, var8, this.U);
      var1.H = 0.0;
      var1.A = var2 != 0 ? 1.6 + this.U.nextDouble() * 1.8 : 2.3 + this.U.nextDouble() * 1.3;
      var1.r = this.C.size * (var2 != 0 ? 0.055 : 0.014) * (0.65 + this.U.nextDouble() * 0.7);
      var1.e = this.U.nextDouble();
      var1.I = false;
      var1.D = null;
      var1.d = 0.0;
      var1.t = Double.POSITIVE_INFINITY;
      var1.P = 0;
   }

   private void uAsVoid() {
      this.o.M = 1;
      this.o.I = this.C.primary;
      this.o.A = bg.pAsInt(this.C.secondary, 0.55);
      this.o.i = this.C.bloom;
      this.o.d = this.C.distance;
      this.o.Z = this.o.e = this.o.P = this.o.w = this.o.b = 0.0;
      this.o.a = this.o.D = this.o.L = this.o.J = this.o.V = 0.0;
      this.o.C = this.o.r = 0.0;
      this.o.t = ay.Type_r.FADE_IN_OUT;
      this.o.o = ay.Type_z.PULSE;
      this.o.q = ParticleEmitterEngine.Type_a.Glow;
   }

   private void FAsVoid() {
      this.uAsVoid();
      this.o.q = this.U.nextDouble() < 0.2 ? ParticleEmitterEngine.Type_a.Star : ParticleEmitterEngine.Type_a.Glow;
      this.o.x = 3.0;
      this.o.v = 6.0;
      this.o.g = this.C.size * 0.022;
      this.o.R = this.C.size * 0.07;
      this.o.Z = this.C.wind * 0.12;
      this.o.P = this.C.wind * 0.05;
      this.o.C = 0.35 + this.U.nextDouble() * 0.5;
      this.o.r = 0.4 + this.U.nextDouble();
      this.o.S = 4.0 + this.U.nextDouble() * 4.0;
      this.o.I = bg.pAsInt(bg.xAsInt(this.C.primary, this.C.secondary, this.U.nextDouble()), 0.4 + this.U.nextDouble() * 0.5);
      int var1 = this.U.nextDouble() < 0.22 ? 1 : 0;
      this.M
         .pAsVoid(
            b9.iAsVec3d(this.N, var1 != 0 ? Math.min(3.0, this.C.radius * 0.16) : this.C.radius, var1 != 0 ? 4.0 : Math.min(20.0, this.C.radius), this.U),
            this.o
         );
   }

   private void TAsVoid(CelestialEffectEngine.Type_r var1) {
      this.uAsVoid();
      this.o.q = ParticleEmitterEngine.Type_a.Spark;
      this.o.M = 3;
      this.o.x = 0.25;
      this.o.v = 0.55;
      this.o.g = var1.r * 0.65;
      this.o.R = var1.r;
      this.o.w = 1.4;
      this.o.a = 2.0;
      this.o.D = 0.4;
      this.o.Z = var1.E.x * 0.65;
      this.o.e = var1.E.y * 0.65;
      this.o.P = var1.E.z * 0.65;
      this.o.t = ay.Type_r.LINEAR;
      this.o.o = ay.Type_z.SHRINK;
      this.M.pAsVoid(var1.m, this.o);
      this.g++;
   }

   private void mAsVoid(lq.Type_l var1, double var2) {
      this.uAsVoid();
      this.o.q = ParticleEmitterEngine.Type_a.Ring;
      this.o.y = var1.normal();
      this.o.x = this.o.v = 0.42;
      this.o.p = Math.clamp(var2 * 7.0, 0.12, 0.45);
      this.o.n = 0.015;
      this.o.t = ay.Type_r.HOLD_FADE;
      this.o.o = ay.Type_z.CONSTANT;
      this.o.I = bg.pAsInt(this.C.primary, 0.5);
      this.o.A = bg.pAsInt(this.C.secondary, 0.1);
      Vec3d var4 = var1.point().add(var1.normal().multiply(0.012));
      this.M.pAsVoid(var4, this.o);
      this.o.q = ParticleEmitterEngine.Type_a.Glow;
      this.o.M = 2;
      this.o.x = 0.18;
      this.o.v = 0.32;
      this.o.g = 0.025;
      this.o.R = 0.045;
      this.o.p = 1.0;
      this.o.e = 0.2;
      this.o.w = 0.35;
      this.o.o = ay.Type_z.SHRINK;
      this.M.pAsVoid(var4, this.o);
      this.I++;
   }

   public void aAsVoid(Vec3d var1, float var2, Frustum var3) {
      long var4 = op.RAsLong();
      this.O = var1;
      this.B = Math.clamp(var2, 0.0F, 1.0F);
      this.b = 0;
      this.l = false;
      this.M.fAsVoid(var1, this.B, var3);
      if (this.C != null) {
         double var6 = this.T + (this.x - this.T) * (double)this.B;

         for (int var8 = 0; var8 < this.L; var8++) {
            CelestialEffectEngine.Type_r var9 = this.X[var8];
            Vec3d var10 = var9.S.lerp(var9.m, (double)this.B);
            double var11 = Math.max(0.0, var9.H - 0.05 + (double)this.B * 0.05);
            double var13 = var10.distanceTo(var1);
            double var15 = nAsDouble(var11, var9.A) * ay.RAsDouble((this.C.distance - var13) / Math.max(2.0, this.C.distance * 0.18)) * var6;
            if (!(var15 < 0.005)) {
               int var17 = this.C.style == CelestialEffectEngine.Type_z.METEOR_SHOWER ? 1 : 0;
               Vec3d var18 = var17 != 0 && var9.P > 0 ? var9.o[var9.P - 1] : var10.subtract(var9.E.multiply(0.045 + var9.e * 0.025));
               double var19 = Math.max(0.1, var9.r * 3.0);
               if (var3 == null || var3.isVisible(new Box(var10, var18).expand(var19))) {
                  CelestialEffectEngine.Type_c var21 = this.r[this.b++];
                  var21.y = var9.r;
                  var21.Q = var17 != 0;
                  var21.G = bg.pAsInt(
                     bg.xAsInt(this.C.primary, this.C.primary & 0xFF000000 | 16777215, var17 != 0 ? 0.55 : 0.18), var15 * (var17 != 0 ? 0.95 : 0.6)
                  );
                  var21.J = bg.pAsInt(this.C.secondary, var15 * (var17 != 0 ? 0.6 : 0.3));
                  if (var21.G >>> 24 == 0 && var21.J >>> 24 == 0) {
                     this.b--;
                  } else {
                     var21.u[0] = var10;
                     var21.i = 1;
                     if (var17 != 0) {
                        for (int var22 = 0; var22 < var9.P; var22++) {
                           var21.u[var21.i++] = var9.o[var22];
                        }
                     } else {
                        var21.u[var21.i++] = var18;
                     }
                  }
               }
            }
         }

         this.l = this.C.bloom && (this.b > 0 || this.M.yAsBoolean());
         op.FAsVoid("Celestial prepared flights", (long)this.b);
         op.FAsVoid("Celestial ribbon segments", (long)this.KAsInt());
         op.FAsVoid("Celestial culled flights", (long)(this.L - this.b));
         op.aAsVoid("Celestial prepare", var4);
      }
   }

   public static double nAsDouble(double var0, double var2) {
      return ay.RAsDouble(var0 / 0.22) * ay.RAsDouble((var2 - var0) / 0.45);
   }

   public boolean lAsBoolean() {
      return this.l;
   }

   public boolean aAsBoolean() {
      return this.M.oAsBoolean();
   }

   public void EAsVoid(EffectGeometryRenderer var1, boolean var2) {
      if (!var2 || this.l) {
         this.M.MAsVoid(var1, this.O, this.B, var2);
         var1.blend(x4.Type_g.ADDITIVE);

         for (int var3 = 0; var3 < this.b; var3++) {
            CelestialEffectEngine.Type_c var4 = this.r[var3];

            for (int var5 = 1; var5 < var4.i; var5++) {
               Vec3d var6 = var4.u[var5 - 1];
               Vec3d var7 = var4.u[var5];
               double var8 = 1.0 - ((double)var5 - 1.0) / (double)(var4.i - 1);
               double var10 = 1.0 - (double)var5 / (double)(var4.i - 1);
               double var12 = var4.y * (var4.Q ? 0.08 + 0.92 * var8 : 1.0);
               double var14 = var4.y * (var4.Q ? 0.08 + 0.92 * var10 : 0.2);
               int var16 = bg.pAsInt(bg.xAsInt(var4.J, var4.G, var8), var8);
               int var17 = bg.pAsInt(bg.xAsInt(var4.J, var4.G, var10), var10);
               var1.ZAsVoid(var6.x, var6.y, var6.z, var7.x, var7.y, var7.z, var12 * 3.0, var14 * 3.0, bg.pAsInt(var16, 0.15), bg.pAsInt(var17, 0.15));
               var1.ZAsVoid(var6.x, var6.y, var6.z, var7.x, var7.y, var7.z, var12, var14, var16, var17);
               if (var4.Q) {
                  var1.ZAsVoid(
                     var6.x,
                     var6.y,
                     var6.z,
                     var7.x,
                     var7.y,
                     var7.z,
                     var12 * 0.26,
                     var14 * 0.26,
                     bg.xAsInt(var16, var16 & 0xFF000000 | 16777215, 0.75),
                     bg.xAsInt(var17, var17 & 0xFF000000 | 16777215, 0.75)
                  );
               }
            }

            if (var4.Q) {
               Vec3d var18 = var4.u[0];
               var1.cAsVoid(var18.x, var18.y, var18.z, var4.y * 1.5, 0.0, var4.G, ParticleEmitterEngine.Type_a.Glow, 0.0, 0.0, 0.0);
            }
         }

         var1.blend(x4.Type_g.ALPHA);
      }
   }

   public int LAsInt() {
      return this.L;
   }

   public int ZAsInt() {
      return this.M.UAsInt();
   }

   public int VAsInt() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.L; var2++) {
         var1 += this.X[var2].P;
      }

      return var1;
   }

   public int oAsInt() {
      return this.b;
   }

   public int KAsInt() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.b; var2++) {
         var1 += Math.max(0, this.r[var2].i - 1);
      }

      return var1;
   }

   public int kAsInt() {
      return this.F;
   }

   public int rAsInt() {
      return this.I;
   }

   public int OAsInt() {
      return this.g;
   }

   public Vec3d uAsVec3d(int var1) {
      return this.X[var1].m;
   }

   public Vec3d wAsVec3d(int var1) {
      return this.X[var1].E;
   }

   private static final class Type_c {
      final Vec3d[] u = new Vec3d[13];
      int i;
      int G;
      int J;
      double y;
      boolean Q;

      private Type_c() {
      }
   }

   public static record Type_i(
      CelestialEffectEngine.Type_z style,
      double density,
      double radius,
      double speed,
      double size,
      double wind,
      double distance,
      int primary,
      int secondary,
      boolean bloom,
      boolean impacts,
      int maximum
   ) {
   }

   private static final class Type_r {
      Vec3d m;
      Vec3d S;
      Vec3d E;
      final Vec3d[] o = new Vec3d[12];
      int P;
      double H;
      double A;
      double r;
      double e;
      double d;
      double t;
      boolean k;
      boolean I;
      lq.Type_l D;

      private Type_r() {
      }
   }

   public static enum Type_z {
      METEOR_SHOWER,
      STARDUST,
      ASTRAL_RAIN;

      private Type_z() {
      }
   }
}
