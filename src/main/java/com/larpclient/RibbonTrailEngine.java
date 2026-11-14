package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.util.math.Vec3d;

public final class RibbonTrailEngine {
   private static final int I = 128;
   private static final int y = 32;
   private final Map<Integer, RibbonTrailEngine.Type_y> D = new LinkedHashMap<>();
   private long M;
   public RibbonTrailEngine.Type_b N = RibbonTrailEngine.Type_b.TAPER_TAIL;
   public FadeCurve.Type_r u = FadeCurve.Type_r.LINEAR;
   public EffectRenderParams.Type_g d = EffectRenderParams.Type_g.ALPHA;
   public double i = 1.2;
   public double c = 0.12;
   public boolean A = true;
   public boolean h = true;
   public boolean x;
   private boolean G;
   public int g = UiStyle.w;
   public int z = UiStyle.Q;

   public RibbonTrailEngine() {
      ClientLog.GAsVoid("Trails", "Ribbon engine initialized: 32 sources, 128 points per source");
   }

   public boolean CAsBoolean() {
      for (RibbonTrailEngine.Type_y var2 : this.D.values()) {
         if (var2.w == null ? this.A : var2.w.glow()) {
            return true;
         }
      }

      return false;
   }

   public boolean lAsBoolean() {
      return this.G;
   }

   public boolean uAsBoolean() {
      return this.D.isEmpty();
   }

   public int KAsInt() {
      return this.D.size();
   }

   public int rAsInt() {
      return this.D.values().stream().mapToInt(var0 -> var0.V).sum();
   }

   public void FAsVoid() {
      this.D.clear();
      this.M = 0L;
      this.G = false;
   }

   public void tAsVoid(int var1) {
      this.D.remove(var1);
   }

   public void DAsVoid() {
      this.M++;
      this.D.values().removeIf(var1 -> {
         while (var1.V > 0 && (double)(this.M - var1.v[var1.E]) > (var1.w == null ? this.i : var1.w.lifetime()) * 20.0) {
            var1.E = (var1.E + 1) % 128;
            var1.V--;
         }

         return var1.V == 0;
      });
   }

   public void gAsVoid(int var1, Vec3d var2) {
      RibbonTrailEngine.Type_y var3 = this.D.get(var1);
      if (var3 == null) {
         if (this.D.size() >= 32) {
            return;
         }

         var3 = new RibbonTrailEngine.Type_y();
         this.D.put(var1, var3);
      }

      if (var3.V > 0) {
         Vec3d var4 = var3.F[(var3.E + var3.V - 1) % 128];
         double var5 = var4.squaredDistanceTo(var2);
         if (var5 > 64.0) {
            var3.E = var3.V = 0;
         } else if (var5 < 4.0E-4) {
            return;
         }
      }

      if (var3.V == 128) {
         var3.E = (var3.E + 1) % 128;
         var3.V--;
      }

      int var7 = (var3.E + var3.V++) % 128;
      var3.F[var7] = var2;
      var3.v[var7] = this.M;
   }

   public void CAsVoid(int var1, Vec3d var2, RibbonTrailEngine.Type_f var3) {
      this.gAsVoid(var1, var2);
      RibbonTrailEngine.Type_y var4 = this.D.get(var1);
      if (var4 != null) {
         var4.w = var3;
      }
   }

   public void TAsVoid(Vec3d var1, float var2) {
      this.G = false;
      double var3 = MediaSession.RAsDouble();

      for (RibbonTrailEngine.Type_y var6 : this.D.values()) {
         var6.P.rAsVoid();
         var6.P.i = 4096.0;
         RibbonTrailEngine.Type_f var7 = var6.w;
         double var8 = var7 == null ? this.i : var7.lifetime();
         double var10 = var7 == null ? this.c : var7.width();
         int var12 = var7 == null ? this.g : var7.from();
         int var13 = var7 == null ? this.z : var7.to();
         boolean var14 = var7 == null ? this.h : var7.smoothing();
         RibbonTrailEngine.Type_b var15 = var7 == null ? this.N : var7.widthCurve();
         FadeCurve.Type_r var16 = var7 == null ? this.u : var7.alphaCurve();
         var6.o = var7 == null ? this.d : var7.blend();
         var6.f = var7 == null ? this.A : var7.glow();
         var6.h = this.x;

         for (int var17 = 1; var17 < var6.V; var17++) {
            int var18 = (var6.E + var17 - 1) % 128;
            int var19 = (var6.E + var17) % 128;
            Vec3d var20 = var6.F[var18];
            Vec3d var21 = var6.F[var19];
            if (var17 == var6.V - 1 && var6.v[var19] == this.M) {
               var21 = var20.lerp(var21, (double)var2);
            }

            double var22 = Math.clamp(1.0 - (double)((float)this.M + var2 - (float)var6.v[var18]) / (var8 * 20.0), 0.0, 1.0);
            double var24 = Math.clamp(1.0 - (double)((float)this.M + var2 - (float)var6.v[var19]) / (var8 * 20.0), 0.0, 1.0);
            if (var17 == 1) {
               this.gAsVoid(var6.P, var20, var22, var12, var13, var16, var15, var10, var3);
            }

            int var26 = var14 ? 4 : 1;
            if (var26 > 1) {
               Vec3d var27 = var17 > 1 ? var6.F[(var6.E + var17 - 2) % 128] : var20.add(var20.subtract(var21));
               Vec3d var28 = var17 < var6.V - 1 ? var6.F[(var6.E + var17 + 1) % 128] : var21.add(var21.subtract(var20));
               var6.I.BAsVoid(var27, var20, var21, var28);
            }

            for (int var31 = 1; var31 <= var26; var31++) {
               double var32 = (double)var31 / (double)var26;
               Vec3d var30 = var26 == 1 ? var21 : var6.I.hAsVec3d(var32);
               this.gAsVoid(var6.P, var30, var22 + (var24 - var22) * var32, var12, var13, var16, var15, var10, var3);
            }
         }

         if (var15 != RibbonTrailEngine.Type_b.CONSTANT) {
            var6.P.zAsVoid();
         }

         var6.P.aAsVoid(var1);
         var6.x = kAsBoolean(var6.P, var1);
         if (var6.f && var6.x) {
            this.G = true;
         }
      }
   }

   private static boolean kAsBoolean(RibbonCapacity.Type_v var0, Vec3d var1) {
      for (int var2 = 1; var2 < var0.B; var2++) {
         if (var0.W[var2 - 1].squaredDistanceTo(var1) <= 4096.0
            && var0.W[var2 - 1].squaredDistanceTo(var0.W[var2]) >= 1.0E-10
            && (var0.D[var2 - 1] > 0.0 || var0.D[var2] > 0.0)
            && (var0.O[var2 - 1] >>> 24 > 0 || var0.O[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   private void gAsVoid(
      RibbonCapacity.Type_v var1, Vec3d var2, double var3, int var5, int var6, FadeCurve.Type_r var7, RibbonTrailEngine.Type_b var8, double var9, double var11
   ) {
      int var13 = ColorUtil.pAsInt(ColorUtil.xAsInt(var6, var5, var3), var7.fAsDouble(1.0 - var3));
      double var14 = var9 * this.cAsDouble(var3, var8);
      if (this.x) {
         var14 *= (1.0 + 0.075 * Math.sin(var2.x * 7.0 + var2.y * 3.0 + var2.z * 5.0 - var11 * 1.7)) * (1.0 + 0.3 * var3);
         var13 = ColorUtil.pAsInt(var13, var3 * var3);
      }

      var1.bAsVoid(var2, var14, var13);
   }

   public void RAsVoid(EffectGeometryRenderer var1, float var2, boolean var3) {
      long var4 = OpenScreen.RAsLong();

      for (RibbonTrailEngine.Type_y var7 : this.D.values()) {
         if (var7.x && (!var3 || var7.f)) {
            var1.blend(var7.o);
            var1.hAsVoid(var7.P, var7.h);
         }
      }

      var1.blend(EffectRenderParams.Type_g.ALPHA);
      OpenScreen.aAsVoid("Trails", var4);
   }

   private double cAsDouble(double var1, RibbonTrailEngine.Type_b var3) {
      return switch (var3) {
         case CONSTANT -> 1.0;
         case TAPER_TAIL -> var1 * var1;
         case TAPER_BOTH -> Math.sin(Math.PI * var1);
         case HEAD_HEAVY -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   public static enum Type_b {
      CONSTANT,
      TAPER_TAIL,
      TAPER_BOTH,
      HEAD_HEAVY;

      private Type_b() {
      }
   }

   public static record Type_f(
      double lifetime,
      double width,
      int from,
      int to,
      boolean glow,
      boolean smoothing,
      EffectRenderParams.Type_g blend,
      RibbonTrailEngine.Type_b widthCurve,
      FadeCurve.Type_r alphaCurve
   ) {
      public Type_f(
         double lifetime,
         double width,
         int from,
         int to,
         boolean glow,
         boolean smoothing,
         EffectRenderParams.Type_g blend,
         RibbonTrailEngine.Type_b widthCurve,
         FadeCurve.Type_r alphaCurve
      ) {
         if (Double.isFinite(lifetime) && !(lifetime <= 0.0) && Double.isFinite(width) && !(width <= 0.0)) {
            Objects.requireNonNull(blend);
            Objects.requireNonNull(widthCurve);
            Objects.requireNonNull(alphaCurve);
            this.lifetime = lifetime;
            this.width = width;
            this.from = from;
            this.to = to;
            this.glow = glow;
            this.smoothing = smoothing;
            this.blend = blend;
            this.widthCurve = widthCurve;
            this.alphaCurve = alphaCurve;
         } else {
            throw new IllegalArgumentException("Trail lifetime and width must be finite and positive");
         }
      }
   }

   private static final class Type_y {
      final Vec3d[] F = new Vec3d[128];
      final long[] v = new long[128];
      int E;
      int V;
      RibbonTrailEngine.Type_f w;
      final RibbonCapacity.Type_v P = new RibbonCapacity.Type_v(512);
      final RibbonCapacity.Type_f I = new RibbonCapacity.Type_f();
      EffectRenderParams.Type_g o;
      boolean f;
      boolean h;
      boolean x;

      private Type_y() {
      }
   }
}
