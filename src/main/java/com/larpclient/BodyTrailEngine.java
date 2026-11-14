package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.util.math.Vec3d;

public final class BodyTrailEngine {
   static final int PriceDetector = 128;
   static final int PetEffectRenderer = 16;
   static final int ScreenScale = 8;
   static final int n = 3;
   static final int CropHelper = 382;
   private final Map<Integer, BodyTrailEngine.Type_l> Z = new LinkedHashMap<>();
   private long S;
   private boolean J;
   private x4.Type_g SkyChunkCache = x4.Type_g.ADDITIVE;
   public BodyTrailEngine.Type_i DropletTrail = BodyTrailEngine.Type_i.AURA;
   public RibbonTrailEngine.Type_b R = RibbonTrailEngine.Type_b.TAPER_TAIL;
   public ay.Type_r BloomUtility = ay.Type_r.LINEAR;
   public double U = 1.2;
   public double AuraModule = 0.12;
   public boolean v = true;
   public boolean O = true;
   public int w;
   public int c;

   public BodyTrailEngine() {
      this.w = bd.w;
      this.c = bd.Q;
      ClientLog.GAsVoid("Trails", "Body trail engine initialized: {} sources, {} points per source", 16, 128);
   }

   public boolean gAsBoolean() {
      return this.Z.isEmpty();
   }

   public int VAsInt() {
      return this.Z.size();
   }

   public int mAsInt() {
      return this.Z.values().stream().mapToInt(var0 -> var0.O).sum();
   }

   public boolean QAsBoolean() {
      return this.J;
   }

   public void WAsVoid() {
      this.Z.clear();
      this.S = 0L;
      this.J = false;
   }

   public void bAsVoid() {
      this.S++;
      this.Z.values().removeIf(var1 -> {
         while (var1.O > 0 && (double)(this.S - var1.s[var1.Q]) > this.U * 20.0) {
            var1.Q = (var1.Q + 1) % 128;
            var1.O--;
         }

         return var1.O == 0;
      });
   }

   public void nAsVoid(int var1, Vec3d var2, double var3, double var5) {
      if (Double.isFinite(var2.x) && Double.isFinite(var2.y) && Double.isFinite(var2.z) && Double.isFinite(var3) && Double.isFinite(var5)) {
         var3 = Math.clamp(var3, 0.1, 6.0);
         var5 = Math.clamp(var5, 0.1, 4.0);
         BodyTrailEngine.Type_l var7 = this.Z.get(var1);
         if (var7 == null) {
            if (this.Z.size() >= 16) {
               return;
            }

            var7 = new BodyTrailEngine.Type_l();
            this.Z.put(var1, var7);
         }

         double var8 = var7.x;
         if (var7.O > 0) {
            int var10 = (var7.Q + var7.O - 1) % 128;
            double var11 = var7.e[var10].squaredDistanceTo(var2);
            if (var11 > 64.0) {
               var7.Q = var7.O = 0;
            } else {
               if (var11 < 4.0E-4 && Math.abs(var7.b[var10] - var3) < 0.02) {
                  return;
               }

               var8 += Math.sqrt(var11);
            }
         }

         if (var7.O == 128) {
            var7.Q = (var7.Q + 1) % 128;
            var7.O--;
         }

         int var15 = (var7.Q + var7.O++) % 128;
         var7.e[var15] = var2;
         var7.b[var15] = var3;
         var7.R[var15] = var5;
         var7.T[var15] = var7.x = var8;
         var7.s[var15] = this.S;
      }
   }

   public void ZAsVoid(Vec3d var1, float var2) {
      this.J = false;
      this.s = this.l == BodyTrailEngine.Type_i.SILK ? x4.Type_g.ALPHA : x4.Type_g.ADDITIVE;
      double var3 = mb.RAsDouble();

      for (BodyTrailEngine.Type_l var6 : this.Z.values()) {
         var6.W = false;
         var6.o = var6.G = 0;
         this.qAsVoid(var6, var2);
         if (var6.p >= 2) {
            switch (this.l) {
               case SILK:
                  this.cAsVoid(var6, var1, var3);
                  break;
               case AURA:
                  this.qAsVoid(var6, var1, var3);
                  break;
               case COMET:
                  this.VAsVoid(var6, var1, var3);
                  break;
               case HELIX:
                  this.GAsVoid(var6, var1, var3);
                  break;
               case STARDUST:
                  this.BAsVoid(var6, var1, var3);
            }

            for (int var7 = 0; var7 < var6.o; var7++) {
               RibbonCapacity.Type_v var8 = var6.g[var7];
               if (this.R != RibbonTrailEngine.Type_b.CONSTANT) {
                  var8.zAsVoid();
               }

               var8.aAsVoid(var1);
               var6.M[var7] = LAsBoolean(var8, var1);
               var6.W = var6.W | var6.M[var7];
            }

            if (this.v && var6.W) {
               this.J = true;
            }
         }
      }
   }

   public void EAsVoid(EffectGeometryRenderer var1, boolean var2) {
      if (!var2 || this.v) {
         long var3 = op.RAsLong();
         var1.blend(this.s);

         for (BodyTrailEngine.Type_l var6 : this.Z.values()) {
            if (var6.W) {
               for (int var7 = 0; var7 < var6.o; var7++) {
                  if (var6.M[var7]) {
                     if (var6.F[var7] >= 0.0) {
                        var1.MAsVoid(var6.g[var7], var6.F[var7]);
                     } else {
                        var1.hAsVoid(var6.g[var7], var6.Y[var7]);
                     }
                  }
               }
            }
         }

         var1.blend(x4.Type_g.ADDITIVE);

         for (BodyTrailEngine.Type_l var13 : this.Z.values()) {
            if (var13.W) {
               for (int var14 = 0; var14 < var13.G; var14++) {
                  Vec3d var8 = var13.I[var14];
                  int var9 = var13.V[var14];
                  double var10 = var13.m[var14];
                  var1.cAsVoid(var8.x, var8.y, var8.z, var10, 0.0, bg.pAsInt(var9, 0.32), ParticleEmitterEngine.Type_a.Glow, 0.0, 0.0, 0.0);
                  var1.cAsVoid(
                     var8.x,
                     var8.y,
                     var8.z,
                     var10 * 0.32,
                     0.0,
                     bg.xAsInt(var9, var9 & 0xFF000000 | 16777215, 0.7),
                     ParticleEmitterEngine.Type_a.Glow,
                     0.0,
                     0.0,
                     0.0
                  );
               }
            }
         }

         var1.blend(x4.Type_g.ALPHA);
         op.aAsVoid("Body Trails", var3);
      }
   }

   private void qAsVoid(BodyTrailEngine.Type_l var1, float var2) {
      var1.p = 0;
      if (var1.O != 0) {
         int var3 = this.O ? 3 : 1;

         for (int var4 = 1; var4 < var1.O; var4++) {
            int var5 = (var1.Q + var4 - 1) % 128;
            int var6 = (var1.Q + var4) % 128;
            Vec3d var7 = var1.e[var5];
            Vec3d var8 = var1.e[var6];
            double var9 = var1.b[var6];
            double var11 = var1.R[var6];
            double var13 = var1.T[var6];
            if (var4 == var1.O - 1 && var1.s[var6] == this.S) {
               var8 = var7.lerp(var8, (double)var2);
               var9 = var1.b[var5] + (var9 - var1.b[var5]) * (double)var2;
               var11 = var1.R[var5] + (var11 - var1.R[var5]) * (double)var2;
               var13 = var1.T[var5] + (var13 - var1.T[var5]) * (double)var2;
            }

            double var15 = this.YAsDouble(var1.s[var5], var2);
            double var17 = this.YAsDouble(var1.s[var6], var2);
            if (var4 == 1) {
               this.MAsVoid(var1, var7, var1.b[var5], var1.R[var5], var15, var1.T[var5]);
            }

            if (var3 > 1) {
               Vec3d var19 = var4 > 1 ? var1.e[(var1.Q + var4 - 2) % 128] : var7.add(var7.subtract(var8));
               Vec3d var20 = var4 < var1.O - 1 ? var1.e[(var1.Q + var4 + 1) % 128] : var8.add(var8.subtract(var7));
               var1.S.BAsVoid(var19, var7, var8, var20);
            }

            for (int var27 = 1; var27 <= var3; var27++) {
               double var28 = (double)var27 / (double)var3;
               this.MAsVoid(
                  var1,
                  var3 == 1 ? var8 : var1.S.hAsVec3d(var28),
                  var1.b[var5] + (var9 - var1.b[var5]) * var28,
                  var1.R[var5] + (var11 - var1.R[var5]) * var28,
                  var15 + (var17 - var15) * var28,
                  var1.T[var5] + (var13 - var1.T[var5]) * var28
               );
            }
         }

         Vec3d var22 = null;

         for (int var23 = 0; var23 < var1.p; var23++) {
            Vec3d var24 = var1.D[Math.min(var23 + 1, var1.p - 1)].subtract(var1.D[Math.max(var23 - 1, 0)]);
            double var25 = Math.sqrt(var24.x * var24.x + var24.z * var24.z);
            Vec3d var26 = var25 < 1.0E-6 ? (var22 != null ? var22 : new Vec3d(1.0, 0.0, 0.0)) : new Vec3d(-var24.z / var25, 0.0, var24.x / var25);
            if (var22 != null && var26.dotProduct(var22) < 0.0) {
               var26 = var26.multiply(-1.0);
            }

            var22 = var26;
            var1.N[var23] = var26;
         }
      }
   }

   private void MAsVoid(BodyTrailEngine.Type_l var1, Vec3d var2, double var3, double var5, double var7, double var9) {
      int var11 = var1.p++;
      var1.D[var11] = var2;
      var1.r[var11] = var3;
      var1.l[var11] = var5;
      var1.J[var11] = var7;
      var1.n[var11] = var9;
   }

   private double YAsDouble(long var1, float var3) {
      return Math.clamp(1.0 - (double)((float)this.S + var3 - (float)var1) / (this.U * 20.0), 0.0, 1.0);
   }

   private void cAsVoid(BodyTrailEngine.Type_l var1, Vec3d var2, double var3) {
      this.ZAsVoid(var1, var2, 4, 0.1, 0.0, 0.0, var3, false);
      double[] var5 = new double[]{0.16, 0.52, 0.88};

      for (int var6 = 0; var6 < var5.length; var6++) {
         RibbonCapacity.Type_v var7 = this.nAsRn_Type_v(var1, 0.55, false);

         for (int var8 = 0; var8 < var1.p; var8++) {
            double var9 = var1.J[var8];
            double var11 = var1.r[var8];
            double var13 = var1.n[var8];
            double var15 = 1.0 - var9;
            double var17 = var11 * var5[var6] + Math.sin(var13 * 2.4 + (double)var6 * 2.1 - var3 * 1.6) * 0.09 * var11 * var15;
            double var19 = Math.cos(var13 * 1.8 + (double)var6 * 1.7 - var3 * 1.2) * 0.16 * var15;
            int var21 = bg.xAsInt(this.aAsInt(var9), bg.xAsInt(this.w, this.c, 0.5), (double)var6 * 0.18);
            this.kAsVoid(var7, var1, var8, var17, var19, this.x * 1.15 * this.SAsDouble(var9), var21, 0.9 * this.b.fAsDouble(1.0 - var9), var2);
         }
      }
   }

   private void qAsVoid(BodyTrailEngine.Type_l var1, Vec3d var2, double var3) {
      this.ZAsVoid(var1, var2, 6, 0.24, 3.2, 5.0, var3, true);

      for (int var5 = 0; var5 < 2; var5++) {
         RibbonCapacity.Type_v var6 = this.nAsRn_Type_v(var1, -1.0, true);

         for (int var7 = 0; var7 < var1.p; var7++) {
            double var8 = var1.J[var7];
            double var10 = var1.r[var7];
            double var12 = Math.min(1.0, this.SAsDouble(var8));
            double var14 = var5 == 0 ? var10 * 0.97 : var10 * 0.04;
            double var16 = var10 * 0.5;
            double var18 = var16 + (var14 - var16) * (0.25 + 0.75 * var12);
            this.kAsVoid(var6, var1, var7, var18, 0.0, this.x * 0.55 * this.SAsDouble(var8), this.aAsInt(var8), this.b.fAsDouble(1.0 - var8), var2);
         }
      }
   }

   private void VAsVoid(BodyTrailEngine.Type_l var1, Vec3d var2, double var3) {
      double[] var5 = new double[]{-1.0, 0.0, 1.0};

      for (double var9 : var5) {
         RibbonCapacity.Type_v var11 = this.nAsRn_Type_v(var1, 0.0, false);

         for (int var12 = 0; var12 < var1.p; var12++) {
            double var13 = var1.J[var12];
            double var15 = var1.r[var12];
            double var17 = var13 * var13;
            this.kAsVoid(
               var11,
               var1,
               var12,
               var15 * (0.55 + var9 * 0.32 * var17),
               0.0,
               var15 * 0.42 * var17,
               this.aAsInt(var13),
               0.13 * this.b.fAsDouble(1.0 - var13),
               var2
            );
         }
      }

      for (int var19 = 0; var19 < 2; var19++) {
         RibbonCapacity.Type_v var21 = this.nAsRn_Type_v(var1, -1.0, true);

         for (int var24 = 0; var24 < var1.p; var24++) {
            double var26 = var1.J[var24];
            double var27 = var1.r[var24];
            double var28 = var1.n[var24];
            double var29 = 1.0 - var26;
            double var30 = var28 * 3.0 + (double)var19 * Math.PI - var3 * 3.0;
            this.kAsVoid(
               var21,
               var1,
               var24,
               var27 * 0.55 + Math.cos(var30) * 0.22 * var27 * var29,
               Math.sin(var30) * 0.3 * var29,
               this.x * 0.4 * this.SAsDouble(var26),
               bg.xAsInt(this.aAsInt(var26), this.c, 0.3),
               0.8 * this.b.fAsDouble(1.0 - var26),
               var2
            );
         }
      }

      RibbonCapacity.Type_v var20 = this.nAsRn_Type_v(var1, -1.0, true);

      for (int var22 = 0; var22 < var1.p; var22++) {
         double var25 = var1.J[var22];
         this.kAsVoid(
            var20,
            var1,
            var22,
            var1.r[var22] * 0.55,
            0.0,
            this.x * 2.2 * this.SAsDouble(var25),
            this.aAsInt(var25),
            (0.4 + 0.6 * var25) * this.b.fAsDouble(1.0 - var25),
            var2
         );
      }

      int var23 = var1.p - 1;
      this.rAsVoid(var1, var1.D[var23].add(0.0, var1.r[var23] * 0.55, 0.0), this.w, var1.r[var23] * 0.3, var2);
   }

   private void GAsVoid(BodyTrailEngine.Type_l var1, Vec3d var2, double var3) {
      RibbonCapacity.Type_v var5 = this.nAsRn_Type_v(var1, 0.0, false);

      for (int var6 = 0; var6 < var1.p; var6++) {
         double var7 = var1.J[var6];
         this.kAsVoid(
            var5,
            var1,
            var6,
            var1.r[var6] * 0.5,
            0.0,
            var1.r[var6] * 0.3 * Math.min(1.0, this.SAsDouble(var7) + 0.2),
            this.aAsInt(var7),
            0.12 * this.b.fAsDouble(1.0 - var7),
            var2
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         RibbonCapacity.Type_v var26 = this.nAsRn_Type_v(var1, -1.0, true);
         Vec3d var8 = null;

         for (int var9 = 0; var9 < var1.p; var9++) {
            double var10 = var1.J[var9];
            double var12 = var1.r[var9];
            double var14 = var1.n[var9];
            double var16 = 0.6 + 0.4 * var10;
            double var18 = var14 * 2.4 + (double)var25 * Math.PI - var3 * 2.4;
            double var20 = 0.46 * var12 * var16;
            double var22 = Math.max(var1.l[var9] * 0.8, 0.45) * var16;
            int var24 = var25 == 0 ? this.aAsInt(var10) : bg.xAsInt(this.w, this.c, var10);
            this.kAsVoid(
               var26,
               var1,
               var9,
               var12 * 0.5 + Math.cos(var18) * var20,
               Math.sin(var18) * var22,
               this.x * 0.75 * this.SAsDouble(var10),
               var24,
               this.b.fAsDouble(1.0 - var10),
               var2
            );
            var8 = var26.W[var26.B - 1];
         }

         if (var8 != null) {
            this.rAsVoid(var1, var8, var25 == 0 ? this.w : this.c, 0.16, var2);
         }
      }
   }

   private void BAsVoid(BodyTrailEngine.Type_l var1, Vec3d var2, double var3) {
      this.ZAsVoid(var1, var2, 4, 0.07, 7.0, 3.0, var3, true);
   }

   private void ZAsVoid(BodyTrailEngine.Type_l var1, Vec3d var2, int var3, double var4, double var6, double var8, double var10, boolean var12) {
      for (int var13 = 0; var13 < var3; var13++) {
         RibbonCapacity.Type_v var14 = this.nAsRn_Type_v(var1, 0.0, false);
         double var15 = ((double)var13 + 0.5) / (double)var3;
         double var17 = Math.abs(var15 * 2.0 - 1.0);
         double var19 = var12 ? 0.55 + 0.45 * var17 * var17 : 1.0;

         for (int var21 = 0; var21 < var1.p; var21++) {
            double var22 = var1.J[var21];
            double var24 = var1.r[var21];
            double var26 = var1.n[var21];
            double var28 = 0.3 + 0.7 * Math.min(1.0, this.SAsDouble(var22));
            double var30 = var24 * 0.5 + (var15 - 0.5) * var24 * var28;
            double var32 = var6 == 0.0 ? 1.0 : 0.7 + 0.3 * Math.sin(var26 * var6 - var10 * var8 + (double)var13 * 0.9);
            this.kAsVoid(
               var14,
               var1,
               var21,
               var30,
               0.0,
               2.0 * var24 / (double)var3 * var28,
               this.aAsInt(var22),
               var4 * var19 * var32 * this.b.fAsDouble(1.0 - var22),
               var2
            );
         }
      }
   }

   private RibbonCapacity.Type_v nAsRn_Type_v(BodyTrailEngine.Type_l var1, double var2, boolean var4) {
      int var5 = var1.o++;
      if (var1.g[var5] == null) {
         var1.g[var5] = new RibbonCapacity.Type_v(382);
      }

      RibbonCapacity.Type_v var6 = var1.g[var5];
      var6.rAsVoid();
      var6.i = 4096.0;
      var1.F[var5] = var2;
      var1.Y[var5] = var4;
      return var6;
   }

   private void kAsVoid(
      RibbonCapacity.Type_v var1, BodyTrailEngine.Type_l var2, int var3, double var4, double var6, double var8, int var10, double var11, Vec3d var13
   ) {
      Vec3d var14 = var2.N[var3];
      Vec3d var15 = var2.D[var3];
      Vec3d var16 = new Vec3d(var15.x + var14.x * var6, var15.y + var4, var15.z + var14.z * var6);
      var1.bAsVoid(var16, Math.max(0.0, var8), bg.pAsInt(var10, Math.clamp(var11, 0.0, 1.0) * sAsDouble(var16, var13)));
   }

   private void rAsVoid(BodyTrailEngine.Type_l var1, Vec3d var2, int var3, double var4, Vec3d var6) {
      double var7 = sAsDouble(var2, var6);
      if (!(var7 <= 0.0) && var1.G < var1.I.length) {
         int var9 = var1.G++;
         var1.I[var9] = var2;
         var1.V[var9] = bg.pAsInt(var3, var7);
         var1.m[var9] = var4;
      }
   }

   static double sAsDouble(Vec3d var0, Vec3d var1) {
      return ay.RAsDouble((var0.distanceTo(var1) - 0.6) / 1.4);
   }

   private int aAsInt(double var1) {
      return bg.xAsInt(this.c, this.w, var1);
   }

   private double SAsDouble(double var1) {
      return switch (this.R) {
         case CONSTANT -> 1.0;
         case TAPER_TAIL -> var1 * var1;
         case TAPER_BOTH -> Math.sin(Math.PI * var1);
         case HEAD_HEAVY -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   private static boolean LAsBoolean(RibbonCapacity.Type_v var0, Vec3d var1) {
      for (int var2 = 1; var2 < var0.B; var2++) {
         if (var0.W[var2 - 1].squaredDistanceTo(var1) <= var0.i
            && var0.W[var2 - 1].squaredDistanceTo(var0.W[var2]) >= 1.0E-10
            && (var0.D[var2 - 1] > 0.0 || var0.D[var2] > 0.0)
            && (var0.O[var2 - 1] >>> 24 > 0 || var0.O[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   RibbonCapacity.Type_v pAsRn_Type_v(int var1, int var2) {
      BodyTrailEngine.Type_l var3 = this.Z.get(var1);
      return var3 != null && var2 < var3.o ? var3.g[var2] : null;
   }

   int rAsInt(int var1) {
      BodyTrailEngine.Type_l var2 = this.Z.get(var1);
      return var2 == null ? 0 : var2.o;
   }

   public static enum Type_i {
      SILK,
      AURA,
      COMET,
      HELIX,
      STARDUST;

      private Type_i() {
      }
   }

   private static final class Type_l {
      final Vec3d[] LineSegment = new Vec3d[128];
      final double[] BloomUtility = new double[128];
      final double[] R = new double[128];
      final double[] T = new double[128];
      final long[] SkyChunkCache = new long[128];
      int Q;
      int O;
      double x;
      final Vec3d[] D = new Vec3d[382];
      final Vec3d[] N = new Vec3d[382];
      final double[] r = new double[382];
      final double[] DropletTrail = new double[382];
      final double[] J = new double[382];
      final double[] n = new double[382];
      int p;
      final RibbonCapacity.Type_f S = new RibbonCapacity.Type_f();
      final RibbonCapacity.Type_v[] ChatPayParser = new RibbonCapacity.Type_v[8];
      final double[] F = new double[8];
      final boolean[] Y = new boolean[8];
      final boolean[] M = new boolean[8];
      int o;
      final Vec3d[] I = new Vec3d[2];
      final int[] V = new int[2];
      final double[] PriceDetector = new double[2];
      int G;
      boolean W;

      private Type_l() {
      }
   }
}
