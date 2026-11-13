package com.larpclient;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class ParticleEmitterEngine {
   private static final int Z = 2048;
   private final ParticleEmitterEngine.Type_y[] v = new ParticleEmitterEngine.Type_y[2048];
   private final Random K = new Random();
   private Int2IntOpenHashMap E;
   private int N;
   private int l;
   private int w;
   private int q;
   private final ParticleEmitterEngine.Type_m[] Q = new ParticleEmitterEngine.Type_m[2048];
   private final ParticleEmitterEngine.Type_m[] X = new ParticleEmitterEngine.Type_m[2048];
   private boolean T;
   private static final Comparator<ParticleEmitterEngine.Type_m> S = Comparator.<ParticleEmitterEngine.Type_m>comparingDouble(var0 -> var0.A).reversed();
   public int B = 2048;
   public double c = 64.0;

   public ParticleEmitterEngine() {
      for (int var1 = 0; var1 < 2048; var1++) {
         this.v[var1] = new ParticleEmitterEngine.Type_y();
         this.Q[var1] = new ParticleEmitterEngine.Type_m();
      }

      ClientLog.GAsVoid("Particles", "Emitter engine initialized; capacity={}", 2048);
   }

   public int UAsInt() {
      return this.N;
   }

   public void JAsVoid() {
      this.N = 0;
      if (this.E != null) {
         this.E.clear();
      }

      this.l = 0;
      this.w = this.q = 0;
      this.T = false;
   }

   public void zAsVoid(int var1) {
      for (int var2 = this.N - 1; var2 >= 0; var2--) {
         if (this.v[var2].z == var1) {
            this.iAsVoid(var2);
         }
      }
   }

   private void iAsVoid(int var1) {
      ParticleEmitterEngine.Type_y var2 = this.v[var1];
      if (this.E != null) {
         int var3 = this.E.get(var2.z) - 1;
         if (var3 == 0) {
            this.E.remove(var2.z);
         } else {
            this.E.put(var2.z, var3);
         }
      }

      if (var2.J) {
         this.l--;
      }

      this.v[var1] = this.v[--this.N];
      this.v[this.N] = var2;
   }

   public boolean xAsBoolean() {
      return this.l > 0;
   }

   private double CAsDouble(double var1, double var3) {
      return var1 + this.K.nextDouble() * (var3 - var1);
   }

   public void pAsVoid(Vec3d var1, x4 var2) {
      int var3 = Math.clamp((long)this.B, 0, 2048);
      int var4 = var2.O < 2048 ? this.iAsInt(var2.K) : 0;
      int var5 = Math.clamp((long)var2.O, 0, 2048);
      int var6 = var2.G ? this.iAsInt(var2.K) : 0;
      double var7 = Math.cos(var2.J * 0.05);
      double var9 = Math.sin(var2.J * 0.05);

      for (int var11 = 0; var11 < Math.clamp((long)var2.M, 0, 2048) && this.N < var3 && var4++ < var5; var11++) {
         ParticleEmitterEngine.Type_y var12 = this.v[this.N++];
         var12.O = var6++;
         double var13 = this.CAsDouble(0.0, Math.PI * 2);
         double var15 = Math.sqrt(this.K.nextDouble());
         double var17 = this.CAsDouble(-0.5, 0.5);
         double var19 = 0.0;
         double var21 = 0.0;
         switch (var2.B) {
            case BOX:
               var19 = this.CAsDouble(-0.5, 0.5);
               var21 = this.CAsDouble(-0.5, 0.5);
               break;
            case SPHERE:
               double var23 = this.CAsDouble(-1.0, 1.0);
               double var25 = Math.cbrt(this.K.nextDouble()) * 0.5;
               double var27 = Math.sqrt(1.0 - var23 * var23) * var25;
               var19 = Math.cos(var13) * var27;
               var21 = Math.sin(var13) * var27;
               var17 = var23 * var25;
               break;
            case CYLINDER:
               var19 = Math.cos(var13) * var15 * 0.5;
               var21 = Math.sin(var13) * var15 * 0.5;
               break;
            case RING:
               var19 = Math.cos(var13) * 0.5;
               var21 = Math.sin(var13) * 0.5;
               var17 = 0.0;
               break;
            case POINT:
               var17 = 0.0;
         }

         var12.Z = var12.Q = var1.x + var19 * var2.z;
         var12.w = var12.r = var1.y + var17 * var2.s;
         var12.x = var12.W = var1.z + var21 * var2.c;
         var12.Y = this.CAsDouble(-1.0, 1.0) * var2.w * var2.Q + var2.f + var2.Z;
         var12.A = this.CAsDouble(-1.0, 1.0) * var2.w * var2.Q + var2.P;
         var12.q = this.CAsDouble(var2.G ? -1.0 : -0.4, 1.0) * var2.w + var2.b + var2.e;
         var12.u = -Math.max(0.0, var2.N);
         var12.T = Math.max(0.05, this.CAsDouble(var2.x, var2.v));
         var12.j = Math.max(0.001, this.CAsDouble(var2.g, var2.R));
         if (var2.E != x4.Type_s.NONE && Double.isFinite(var2.h)) {
            var12.w = var12.r = Math.max(var12.w, var2.h + var12.j * 1.2 + 0.002);
         }

         var12.p = var13;
         var12.B = this.CAsDouble(Double.isFinite(var2.T) ? var2.T : -var2.V, var2.V);
         var12.C = var2.G;
         var12.K = var2.Y;
         var12.N = var2.l;
         var12.Pr = this.CAsDouble(0.0, Math.PI * 2);
         var12.h = var12.Pr;
         var12.a = var2.C;
         var12.M = var2.r;
         var12.e = (Math.PI * 2) / Math.max(0.05, var2.S);
         var12.V = var12.c = var12.E = 0.0;
         if (var12.a != 0.0 || var12.M != 0.0) {
            var12.V = Math.sin(var12.h * 0.83) * var12.M;
            var12.c = Math.sin(var12.h) * var12.a;
            var12.E = Math.cos(var12.h * 0.67) * var12.M;
            var12.Z = var12.Q = var12.Q + var12.V;
            var12.w = var12.r = var12.r + var12.c;
            var12.x = var12.W = var12.W + var12.E;
         }

         var12.S = var2.a;
         var12.X = Math.exp(-Math.max(0.0, var2.D) * 0.05);
         var12.t = var2.L;
         var12.o = var2.J;
         var12.G = var7;
         var12.s = var9;
         var12.H = var2.t;
         var12.m = var2.o;
         var12.y = var2.j;
         var12.b = var2.q == ParticleEmitterEngine.Type_a.Cash ? var2.I : bg.xAsInt(var2.I, var2.A, this.K.nextDouble() * 0.35);
         var12.d = var2.A;
         var12.J = var2.i;
         if (var12.J) {
            this.l++;
         }

         var12.l = var2.q;
         var12.P = var2.E;
         var12.n = false;
         var12.i = var2.y.x;
         var12.U = var2.y.y;
         var12.R = var2.y.z;
         var12.I = var12.f = var12.L = var12.F = 0.0;
         if (var12.l == ParticleEmitterEngine.Type_a.Cash || var12.l == ParticleEmitterEngine.Type_a.Shard) {
            var12.I = this.CAsDouble(0.0, Math.PI * 2);
            var12.f = this.CAsDouble(0.0, Math.PI * 2);
            var12.L = this.CAsDouble(-2.4, 2.4);
            var12.F = this.CAsDouble(-1.8, 1.8);
         }

         var12.g = var2.H;
         var12.v = var2.p;
         var12.k = var2.n;
         var12.z = var2.K;
         if (this.E != null) {
            this.E.addTo(var12.z, 1);
         }

         var12.D = Double.isFinite(var2.d) ? Math.max(0.0, var2.d) : Math.max(0.0, this.c);
      }
   }

   public void tAsVoid(Vec3d var1, ParticleEmitterEngine.Type_a var2, int var3, double var4, double var6, int var8, int var9, boolean var10, double var11) {
      x4 var13 = new x4();
      var13.q = var2;
      var13.M = var3;
      var13.g = var13.R = var4;
      var13.x = var13.v = var6;
      var13.I = var8;
      var13.A = var9;
      var13.i = var10;
      var13.p = var4 * var11;
      this.pAsVoid(var1, var13);
   }

   public void NAsVoid() {
      this.IAsVoid(1.0);
   }

   public void IAsVoid(double var1) {
      this.eAsVoid(var1, null);
   }

   public void eAsVoid(double var1, lq.Type_a var3) {
      if (Double.isFinite(var1) && !(var1 < 0.0)) {
         int var4 = 0;

         while (var4 < this.N) {
            ParticleEmitterEngine.Type_y var5 = this.v[var4];
            var5.u += 0.05;
            if (var5.u >= var5.T) {
               this.iAsVoid(var4);
            } else {
               var5.Q = var5.Z;
               var5.r = var5.w;
               var5.W = var5.x;
               if (var5.u >= 0.0 && var5.l != ParticleEmitterEngine.Type_a.Ring && !var5.n) {
                  if (var5.C) {
                     var5.Z = var5.Z + var5.Y * 0.05 * var1;
                     var5.w = var5.w + var5.q * 0.05 * var1;
                     var5.x = var5.x + var5.A * 0.05 * var1;
                     var5.Y = var5.Y * var5.X;
                     var5.A = var5.A * var5.X;
                     var5.q = (var5.q - var5.S * 0.05) * var5.X;
                     var4++;
                     continue;
                  }

                  double var6 = var5.Pr + var5.u * 3.0;
                  double var8 = var5.Y;
                  double var10 = var5.A;
                  var5.Y = (var8 * var5.G - var10 * var5.s) * var5.X;
                  var5.A = (var8 * var5.s + var10 * var5.G) * var5.X;
                  var5.q = (var5.q - var5.S * 0.05) * var5.X;
                  if (var5.t != 0.0 || var5.Y != 0.0 || var5.q != 0.0 || var5.A != 0.0) {
                     var5.Z = var5.Z + (var5.Y + Math.sin(var6) * var5.t) * 0.05 * var1;
                     var5.w = var5.w + (var5.q + Math.cos(var6 * 1.3) * var5.t * 0.4) * 0.05 * var1;
                     var5.x = var5.x + (var5.A + Math.cos(var6 * 0.83) * var5.t) * 0.05 * var1;
                  }

                  if (var5.a != 0.0 || var5.M != 0.0) {
                     WAsVoid(var5, var1);
                  }

                  if (var3 != null && var5.P != x4.Type_s.NONE) {
                     tAsVoid(var5, var3);
                  }
               }

               var4++;
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid motion speed");
      }
   }

   private static void tAsVoid(ParticleEmitterEngine.Type_y var0, lq.Type_a var1) {
      Vec3d var2 = new Vec3d(var0.Q, var0.r, var0.W);
      Vec3d var3 = new Vec3d(var0.Z, var0.w, var0.x);
      if (!(var2.squaredDistanceTo(var3) < 1.0E-12)) {
         double var4 = var0.l == ParticleEmitterEngine.Type_a.Shard ? var0.j * 1.2 : 0.015;
         op.FAsVoid("Particle surface queries", 1L);
         lq.Type_l var6 = var1.HAsLq_Type_l(var2.add(0.0, -var4, 0.0), var3.add(0.0, -var4, 0.0));
         if (var6 != null) {
            Vec3d var7 = var6.normal();
            var0.Z = var6.point().x + var7.x * 0.002;
            var0.w = var6.point().y + var4 + var7.y * 0.002;
            var0.x = var6.point().z + var7.z * 0.002;
            double var8 = var0.Y * var7.x + var0.q * var7.y + var0.A * var7.z;
            if (!(var8 >= 0.0)) {
               int var10 = var7.y > 0.5 && (var0.P == x4.Type_s.SETTLE || -var8 < 0.55) ? 1 : 0;
               if (var10 != 0) {
                  var0.n = true;
                  var0.Y = var0.q = var0.A = 0.0;
                  var0.p = var0.p + var0.u * var0.B;
                  var0.I = var0.I + var0.u * var0.L;
                  var0.f = var0.f + var0.u * var0.F;
                  var0.B = var0.L = var0.F = 0.0;
                  if (var0.l == ParticleEmitterEngine.Type_a.Shard) {
                     var0.w = var6.point().y + bz.TAsDouble(var0.j, var0.I, var0.f) + 0.002;
                  }
               } else {
                  double var11 = var0.P == x4.Type_s.BOUNCE ? 0.38 : 0.08;
                  var0.Y = (var0.Y - (1.0 + var11) * var8 * var7.x) * 0.72;
                  var0.q = (var0.q - (1.0 + var11) * var8 * var7.y) * 0.72;
                  var0.A = (var0.A - (1.0 + var11) * var8 * var7.z) * 0.72;
               }
            }
         }
      }
   }

   private static void WAsVoid(ParticleEmitterEngine.Type_y var0, double var1) {
      var0.h = var0.h + 0.05 * var1 * var0.e;
      double var3 = Math.sin(var0.h * 0.83) * var0.M;
      double var5 = Math.sin(var0.h) * var0.a;
      double var7 = Math.cos(var0.h * 0.67) * var0.M;
      var0.Z = var0.Z + (var3 - var0.V);
      var0.w = var0.w + (var5 - var0.c);
      var0.x = var0.x + (var7 - var0.E);
      var0.V = var3;
      var0.c = var5;
      var0.E = var7;
   }

   public int iAsInt(int var1) {
      if (this.E == null) {
         this.qAsVoid();
      }

      return this.E.get(var1);
   }

   private void qAsVoid() {
      this.E = new Int2IntOpenHashMap();

      for (int var1 = 0; var1 < this.N; var1++) {
         this.E.addTo(this.v[var1].z, 1);
      }
   }

   public boolean yAsBoolean() {
      return this.q > 0;
   }

   public boolean oAsBoolean() {
      return this.T;
   }

   public int ZAsInt() {
      return this.w;
   }

   public static int cAsInt(double var0, int var2) {
      long var3 = ((long)(var0 * 1000.0) / 12L + (long)var2 * 14L) % 360L;
      return bg.HSBtoRGB((float)var3 / 360.0F, 0.7F, 1.0F);
   }

   public void fAsVoid(Vec3d var1, float var2, Frustum var3) {
      this.nAsVoid(var1, var2, var3, 1.0, 1.0);
   }

   public void nAsVoid(Vec3d var1, float var2, Frustum var3, double var4, double var6) {
      long var8 = op.RAsLong();
      this.w = this.q = 0;
      this.T = false;
      if (var6 <= 0.0) {
         op.aAsVoid("Particles prepare", var8);
      } else {
         byte var10 = 0;

         for (int var11 = 0; var11 < this.N; var11++) {
            ParticleEmitterEngine.Type_y var12 = this.v[var11];
            double var13 = var12.u + (double)var2 * 0.05;
            if (!(var13 < 0.0)) {
               double var15 = var12.Q + (var12.Z - var12.Q) * (double)var2;
               double var17 = var12.r + (var12.w - var12.r) * (double)var2;
               double var19 = var12.W + (var12.x - var12.W) * (double)var2;
               double var21 = var15 - var1.x;
               double var23 = var17 - var1.y;
               double var25 = var19 - var1.z;
               if (!(var21 * var21 + var23 * var23 + var25 * var25 > var12.D * var12.D)) {
                  double var27 = Math.clamp(var13 / var12.T, 0.0, 1.0);
                  double var29 = var12.j * var12.m.vAsDouble(var27);
                  int var31 = var12.K ? cAsInt(mb.RAsDouble(), var12.O) : bg.xAsInt(var12.b, var12.d, var27);
                  double var32 = var12.N == 1.0 ? var12.H.fAsDouble(var27) : Math.pow(var12.H.fAsDouble(var27), var12.N);
                  if (var6 < 1.0) {
                     var32 *= var6;
                  }

                  if (var4 < 1.0) {
                     double var34 = var12.D * var12.D;
                     var32 *= ay.RAsDouble((var34 - var21 * var21 - var23 * var23 - var25 * var25) / Math.max(0.001, var34 * (1.0 - var4 * var4)));
                  }

                  int var40 = var12.C ? var31 & 16777215 | Math.clamp(Math.round((double)(var31 >>> 24) * var32), 0, 255) << 24 : bg.pAsInt(var31, var32);
                  if (var40 >>> 24 != 0) {
                     double var35 = var12.l == ParticleEmitterEngine.Type_a.Ring ? var12.v * (1.0 - Math.pow(1.0 - var27, 3.0)) : var29;
                     double var37 = var12.l == ParticleEmitterEngine.Type_a.Ring ? var35 + Math.abs(var12.k) : Math.abs(var29) * 5.0;
                     if (var3 == null || var3.isVisible(new Box(var15 - var37, var17 - var37, var19 - var37, var15 + var37, var17 + var37, var19 + var37))) {
                        ParticleEmitterEngine.Type_m var39 = this.Q[this.w];
                        var39.D = var15;
                        var39.Z = var17;
                        var39.c = var19;
                        var39.X = var29;
                        var39.S = var35;
                        var39.F = var12.p + var13 * var12.B;
                        var39.h = var12.I + var13 * var12.L;
                        var39.E = var12.f + var13 * var12.F;
                        var39.a = var12.Pr + var13 * 4.0;
                        var39.n = var12.Y;
                        var39.m = var12.q;
                        var39.z = var12.A;
                        var39.s = var12.k;
                        var39.r = var12.i;
                        var39.B = var12.U;
                        var39.g = var12.R;
                        var39.d = var40;
                        var39.b = var12.J;
                        var39.e = var12.l;
                        var39.w = var12.y;
                        var39.o = var12.g;
                        var39.Y = var12.l == ParticleEmitterEngine.Type_a.Smoke || var12.l == ParticleEmitterEngine.Type_a.Glow;
                        var39.A = var21 * var21 + var23 * var23 + var25 * var25;
                        var10 = (byte)(var10 | (var39.w != x4.Type_g.ADDITIVE ? 1 : 0));
                        this.X[this.w++] = var39;
                        if (var39.b) {
                           this.q++;
                        }

                        this.T = this.T | var39.Y;
                     }
                  }
               }
            }
         }

         if (var10 != 0) {
            Arrays.sort(this.X, 0, this.w, S);
         }

         op.FAsVoid("Particles prepared", (long)this.w);
         op.aAsVoid("Particles prepare", var8);
      }
   }

   public void MAsVoid(EffectGeometryRenderer var1, Vec3d var2, float var3, boolean var4) {
      long var5 = op.RAsLong();
      ParticleEmitterEngine.Type_m var7 = null;

      for (int var8 = 0; var8 < this.w; var8++) {
         ParticleEmitterEngine.Type_m var9 = this.X[var8];
         if (!var4 || var9.b) {
            if (var7 != null && (var9.w != x4.Type_g.ADDITIVE || var7.w != x4.Type_g.ADDITIVE) && !jAsBoolean(var7, var9)) {
               var1.CAsVoid();
            }

            var1.blend(var9.w);
            var1.QAsVoid(var9.Y);
            if (var9.e == ParticleEmitterEngine.Type_a.Ring) {
               var1.SAsVoid(var9.D, var9.Z, var9.c, var9.S, var9.s, var9.d, var9.r, var9.B, var9.g);
            } else if (var9.e == ParticleEmitterEngine.Type_a.Cash) {
               var1.gAsVoid(var9.D, var9.Z, var9.c, var9.X, var9.F, var9.h, var9.E, var9.a, var9.d);
            } else if (var9.e == ParticleEmitterEngine.Type_a.Shard) {
               var1.IAsVoid(var9.D, var9.Z, var9.c, var9.X, var9.F, var9.h, var9.E, var9.d);
            } else {
               var1.hAsVoid(var9.D, var9.Z, var9.c, var9.X, var9.F, var9.d, var9.e, var9.n, var9.m, var9.z, var9.o);
            }

            var7 = var9;
         }
      }

      var1.QAsVoid(false);
      var1.blend(x4.Type_g.ALPHA);
      op.aAsVoid("Particles", var5);
   }

   private static boolean jAsBoolean(ParticleEmitterEngine.Type_m var0, ParticleEmitterEngine.Type_m var1) {
      return var0.w == var1.w
         && var0.Y == var1.Y
         && var0.o == var1.o
         && var0.e == ParticleEmitterEngine.Type_a.Cash == (var1.e == ParticleEmitterEngine.Type_a.Cash)
         && var0.e == ParticleEmitterEngine.Type_a.Shard == (var1.e == ParticleEmitterEngine.Type_a.Shard)
         && var0.e == ParticleEmitterEngine.Type_a.Ring == (var1.e == ParticleEmitterEngine.Type_a.Ring);
   }

   public static enum Type_a {
      Spark,
      Glow,
      Circle,
      Star,
      Triangle,
      Streak,
      Textured,
      Ring,
      Diamond,
      Shard,
      Petal,
      Smoke,
      Cash;

      private Type_a() {
      }
   }

   private static final class Type_m {
      double D;
      double Z;
      double c;
      double X;
      double S;
      double s;
      double F;
      double h;
      double E;
      double a;
      double n;
      double m;
      double z;
      double A;
      double r;
      double B;
      double g;
      int d;
      boolean b;
      boolean Y;
      ParticleEmitterEngine.Type_a e;
      x4.Type_i o;
      x4.Type_g w;

      private Type_m() {
      }
   }

   private static final class Type_y {
      double Z;
      double w;
      double x;
      double Q;
      double r;
      double W;
      double Y;
      double q;
      double A;
      double u;
      double T;
      double j;
      double p;
      double B;
      double Pr;
      double I;
      double f;
      double L;
      double F;
      double S;
      double X;
      double t;
      double o;
      double G;
      double s;
      double v;
      double k;
      double D;
      double a;
      double M;
      double h;
      double e;
      double V;
      double c;
      double E;
      double i;
      double U;
      double R;
      x4.Type_s P;
      boolean n;
      int b;
      int d;
      int z;
      int O;
      boolean J;
      boolean C;
      boolean K;
      double N;
      ParticleEmitterEngine.Type_a l;
      x4.Type_i g;
      ay.Type_r H;
      ay.Type_z m;
      x4.Type_g y;

      private Type_y() {
      }
   }
}
