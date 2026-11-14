package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public final class RodAttempt {
   public static final float n = 0.12F;
   public static final int S = 73;
   private static final int[] Z = new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
   private static final float[] ChatPayParser = new float[]{0.27F, 0.2F, 0.2F, 0.1F};
   public static final List<RodAttempt.Type_s> L = YAsList();
   public static final List<RodAttempt.Type_t> T;
   public static final List<RodAttempt.Type_d> PriceDetector = List.of(
      new RodAttempt.Type_d(0, 1, 0.028F, true),
      new RodAttempt.Type_d(1, 2, 0.024F, true),
      new RodAttempt.Type_d(2, 3, 0.019F, true),
      new RodAttempt.Type_d(3, 4, 0.012F, false),
      new RodAttempt.Type_d(2, 19, 0.013F, false),
      new RodAttempt.Type_d(19, 7, 0.008F, false),
      new RodAttempt.Type_d(2, 22, 0.012F, false),
      new RodAttempt.Type_d(22, 10, 0.007F, false),
      new RodAttempt.Type_d(2, 25, 0.01F, false),
      new RodAttempt.Type_d(25, 13, 0.005F, false)
   );
   public static final float h = 1.38F;
   public static final float PetEffectRenderer = 0.16F;
   public static final float E = 2.0F;

   private static List<RodAttempt.Type_s> YAsList() {
      ArrayList var0 = new ArrayList<>(
         List.of(
            new RodAttempt.Type_s(0.0F, 0.0F, 0.0F),
            new RodAttempt.Type_s(0.21F, 0.29F, -0.015F),
            new RodAttempt.Type_s(0.48F, 0.47F, 0.045F),
            new RodAttempt.Type_s(1.14F, 0.49F, 0.05F),
            new RodAttempt.Type_s(1.52F, 0.12F, 0.08F),
            new RodAttempt.Type_s(0.0F, 0.0F, 0.0F),
            new RodAttempt.Type_s(0.0F, 0.0F, 0.0F),
            new RodAttempt.Type_s(1.18F, -0.45F, 0.06F),
            new RodAttempt.Type_s(0.0F, 0.0F, 0.0F),
            new RodAttempt.Type_s(0.0F, 0.0F, 0.0F),
            new RodAttempt.Type_s(0.68F, -0.55F, 0.03F),
            new RodAttempt.Type_s(0.0F, 0.0F, 0.0F),
            new RodAttempt.Type_s(0.0F, 0.0F, 0.0F),
            new RodAttempt.Type_s(0.1F, -0.7F, 0.005F),
            new RodAttempt.Type_s(0.0F, 0.0F, 0.0F),
            new RodAttempt.Type_s(0.0F, 0.0F, 0.0F)
         )
      );

      for (int var1 = 0; var1 < 4; var1++) {
         RodAttempt.Type_s var2 = (RodAttempt.Type_s)var0.get(Z[var1 * 3]);
         RodAttempt.Type_s var3 = (RodAttempt.Type_s)var0.get(Z[var1 * 3 + 3]);
         RodAttempt.Type_s var4 = nAsRo_Type_s(nAsRo_Type_s(var2, var3, 0.5F), (RodAttempt.Type_s)var0.get(2), g[var1] * 2.0F);

         for (int var5 = 1; var5 < 3; var5++) {
            float var6 = (float)var5 / 3.0F;
            var0.set(Z[var1 * 3 + var5], nAsRo_Type_s(nAsRo_Type_s(var2, var4, var6), nAsRo_Type_s(var4, var3, var6), var6));
         }
      }

      return List.copyOf(var0);
   }

   public static RodAttempt.Type_h wAsRo_Type_h(boolean var0, boolean var1, float var2, float var3) {
      return rAsRo_Type_h(var0 ? 1.0F : 0.0F, var1 ? 1.0F : 0.0F, var2, var3);
   }

   public static RodAttempt.Type_h rAsRo_Type_h(float var0, float var1, float var2, float var3) {
      float var4 = Math.clamp(var0, 0.0F, 1.0F);
      float var5 = Math.clamp(var1, 0.0F, 1.0F);
      float var6 = Math.clamp(var2 * var2 / 100.0F, 0.0F, 1.0F) * (-90.0F - var3);
      return new RodAttempt.Type_h(
         0.34F * var5,
         0.46F * var5,
         (1.38F - 0.42F * var4) * (1.0F - var5),
         0.16F * (1.0F - var5),
         18.0F * var4 * (1.0F - var5) + var6 * var5,
         1.0F - 0.24F * var5,
         1.0F - 0.08F * var5,
         7.0F * (1.0F - var5) + 2.0F * var5,
         11.0F * (1.0F - var5) + 1.5F * var5,
         0.09F,
         0.025F,
         -6.0F - 3.0F * var4,
         -3.0F,
         0.12F
      );
   }

   public static Matrix4f lAsMatrix4f(RodAttempt.Type_h var0, float var1, float var2, float var3, float var4, int var5) {
      return MAsMatrix4f(
         new Matrix4f()
            .rotateY(vAsFloat(180.0F - var1))
            .translate(0.0F, var0.preY, var0.preZ)
            .rotateX(vAsFloat(var0.pitch))
            .translate(0.0F, var0.anchorY, var0.anchorZ),
         var0,
         var2,
         var3,
         var4,
         var5
      );
   }

   public static Matrix4f RAsMatrix4f(RodAttempt.Type_h var0, float var1, float var2, float var3, int var4, boolean var5) {
      return MAsMatrix4f(
         new Matrix4f()
            .translate(0.0F, var5 ? 0.125F : 0.0F, 0.0F)
            .translate(0.0F, 1.501F, 0.0F)
            .scale(1.0666667F)
            .scale(-1.0F, -1.0F, 1.0F)
            .translate(0.0F, 1.38F, 0.16F),
         var0,
         var1,
         var2,
         var3,
         var4
      );
   }

   private static Matrix4f MAsMatrix4f(Matrix4f var0, RodAttempt.Type_h var1, float var2, float var3, float var4, int var5) {
      float var6 = var2 * var1.frequency;
      float var7 = (float)(Math.sin((double)var6) + 0.16 * Math.sin((double)(var6 * 2.0F))) * var1.amplitude;
      float var8 = (18.0F + var7 + Math.clamp(var3, 0.0F, 1.0F) * var1.walkSpread) * var1.openScale;
      return var0.scale(var4 * var1.scale)
         .translate((float)var5 * var1.hingeX, 0.0F, var1.hingeZ)
         .rotateY(vAsFloat((float)(-var5) * var8))
         .rotateZ(vAsFloat((float)var5 * (var1.splay + 3.5F * (float)Math.cos((double)var6))))
         .rotateX(vAsFloat(var1.wingPitch));
   }

   public static RodAttempt.Type_v vAsRo_Type_v(float var0, float var1, float var2, float var3) {
      ArrayList var4 = new ArrayList<>(L);
      RodAttempt.Type_s var5 = (RodAttempt.Type_s)var4.get(2);
      float var6 = var0 * 0.12F;
      float var7 = 0.075F + 0.018F * (float)Math.sin((double)(var6 - 0.7F));

      for (int var8 = 0; var8 < 13; var8++) {
         RodAttempt.Type_s var9 = nAsRo_Type_s(var5, (RodAttempt.Type_s)var4.get(Z[var8]), 0.5F);
         var4.add(new RodAttempt.Type_s(var9.x, var9.y, var9.z + (var8 % 3 != 0 ? var7 : 0.008F)));
      }

      float var21 = vAsFloat(8.0F + 7.0F * (float)Math.sin((double)(var6 - 0.65F)) + 5.0F * Math.clamp(var1, 0.0F, 1.0F) + 7.0F * var2);
      float var22 = vAsFloat(-4.0F * var2 + 2.0F * (float)Math.sin((double)(var6 - 0.95F)));
      float var10 = 1.0F - 0.7F * Math.clamp(var3, 0.0F, 1.0F);

      for (int var11 = 0; var11 < var4.size(); var11++) {
         if (var11 != 0 && var11 != 1 && var11 != 2) {
            RodAttempt.Type_s var12 = (RodAttempt.Type_s)var4.get(var11);
            float var13 = Math.clamp(var12.x / 0.48F, 0.0F, 1.0F);
            float var14 = var21 * var13 * var10;
            float var15 = var22 * var13 * var10;
            float var16 = var12.x - var5.x;
            float var17 = var12.y - var5.y;
            float var18 = var12.z - var5.z;
            float var19 = var16 * (float)Math.cos((double)var14) + var18 * (float)Math.sin((double)var14);
            float var20 = -var16 * (float)Math.sin((double)var14) + var18 * (float)Math.cos((double)var14);
            var4.set(
               var11,
               new RodAttempt.Type_s(
                  var5.x + var19 * (float)Math.cos((double)var15) - var17 * (float)Math.sin((double)var15),
                  var5.y + var19 * (float)Math.sin((double)var15) + var17 * (float)Math.cos((double)var15),
                  var5.z + var20
               )
            );
         }
      }

      return new RodAttempt.Type_v(List.copyOf(var4), 0.88F + 0.12F * (float)Math.sin((double)(var6 - 0.25F)));
   }

   public static Vec3d vAsVec3d(Vec3d var0, Matrix4f var1, int var2, RodAttempt.Type_s var3) {
      float var4 = (float)var2 * var3.x;
      float var5 = var3.y;
      float var6 = var3.z;
      return var0.add(
         (double)(var1.m00() * var4 + var1.m10() * var5 + var1.m20() * var6 + var1.m30()),
         (double)(var1.m01() * var4 + var1.m11() * var5 + var1.m21() * var6 + var1.m31()),
         (double)(var1.m02() * var4 + var1.m12() * var5 + var1.m22() * var6 + var1.m32())
      );
   }

   public static RodAttempt.Type_r gAsRo_Type_r(Vec3d var0, Matrix4f var1, int var2, RodAttempt.Type_v var3, int var4, int var5, float var6) {
      return new RodAttempt.Type_r(
         var3.points.stream().map(var3x -> vAsVec3d(var0, var1, var2, var3x)).toList(),
         new Vec3d((double)var1.m20(), (double)var1.m21(), (double)var1.m22()).normalize(),
         var4,
         var5,
         var6,
         var3.pulse
      );
   }

   public static int ZAsInt(RodAttempt.Type_r var0, int var1) {
      float var2 = var1 < L.size() ? L.get(var1).x / 1.52F : 0.4F;
      int var3 = bg.xAsInt(var0.primary, var0.secondary, (double)Math.clamp(var2, 0.0F, 1.0F));
      int var4 = var1 >= L.size() && (var1 - L.size()) % 3 != 0 ? 1 : 0;
      double var5 = var4 != 0 ? 0.9 : (var1 >= L.size() ? 0.56 : (var1 < 3 ? 0.3 : 0.62));
      int var7 = var1 >= L.size() ? 212 : (var1 < 3 ? 225 : 145);
      return VAsInt(bg.xAsInt(-15526620, var3, var5), Math.round((float)(var7 * (var3 >>> 24)) / 255.0F));
   }

   public static int iAsInt(RodAttempt.Type_r var0, int var1, boolean var2) {
      double var3 = var1 >= L.size() ? 0.4 : (double)Math.clamp(L.get(var1).x / 1.52F, 0.0F, 1.0F);
      int var5 = bg.xAsInt(var0.primary, var0.secondary, var3);
      return VAsInt(bg.xAsInt(var5, -1, var2 ? 0.22 : 0.38), Math.round((float)(var2 ? 88 : 215) * var0.pulse * (float)(var5 >>> 24) / 255.0F));
   }

   public static boolean UAsBoolean(RodAttempt.Type_r var0) {
      return var0.primary >>> 24 > 0 || var0.secondary >>> 24 > 0;
   }

   public static void vAsVoid(o_ var0, RodAttempt.Type_r var1, RodAttempt.Type_x var2) {
      List var3 = var1.points;
      if (var2 != RodAttempt.Type_x.SURFACE) {
         var0.blend(x4.Type_g.ADDITIVE);
         double var9 = (var2 == RodAttempt.Type_x.HALO ? 0.047 : 0.008) * (double)var1.size;

         for (int var11 = 0; var11 < L.size(); var11++) {
            var0.ribbon(
               (Vec3d)var3.get(var11),
               (Vec3d)var3.get((var11 + 1) % L.size()),
               var9,
               iAsInt(var1, var11, var2 == RodAttempt.Type_x.HALO),
               iAsInt(var1, (var11 + 1) % L.size(), var2 == RodAttempt.Type_x.HALO)
            );
         }
      } else {
         var0.blend(x4.Type_g.ALPHA);

         for (RodAttempt.Type_t var5 : T) {
            LAsVoid(var0, (Vec3d)var3.get(var5.a), ZAsInt(var1, var5.a));
            LAsVoid(var0, (Vec3d)var3.get(var5.b), ZAsInt(var1, var5.b));
            LAsVoid(var0, (Vec3d)var3.get(var5.c), ZAsInt(var1, var5.c));
            LAsVoid(var0, (Vec3d)var3.get(var5.d), ZAsInt(var1, var5.d));
         }

         for (RodAttempt.Type_d var10 : m) {
            int var6 = iAsInt(var1, var10.a, false);
            int var7 = iAsInt(var1, var10.b, false);
            var0.ribbon((Vec3d)var3.get(var10.a), (Vec3d)var3.get(var10.b), (double)(var10.width * var1.size), var6, var7);
            if (var10.ridge) {
               bAsVoid(var0, (Vec3d)var3.get(var10.a), (Vec3d)var3.get(var10.b), var1.normal, (double)(var10.width * var1.size), var6, var7);
            }
         }

         rAsVoid(var0, (Vec3d)var3.get(1), (Vec3d)var3.get(0), (Vec3d)var3.get(2), var1.normal, 0.045 * (double)var1.size, iAsInt(var1, 1, false));
         rAsVoid(var0, (Vec3d)var3.get(2), (Vec3d)var3.get(1), (Vec3d)var3.get(3), var1.normal, 0.036 * (double)var1.size, iAsInt(var1, 2, false));
      }
   }

   public static void MAsVoid(EffectGeometryRenderer var0, RodAttempt.Type_r var1) {
      if (UAsBoolean(var1)) {
         var0.blend(x4.Type_g.ADDITIVE);
         List var2 = var1.points;

         for (int var3 = 0; var3 < L.size(); var3++) {
            var0.ribbon(
               (Vec3d)var2.get(var3),
               (Vec3d)var2.get((var3 + 1) % L.size()),
               0.016 * (double)var1.size,
               iAsInt(var1, var3, false),
               iAsInt(var1, (var3 + 1) % L.size(), false)
            );
         }

         for (RodAttempt.Type_d var4 : m) {
            if (var4.a == 2) {
               var0.ribbon((Vec3d)var2.get(var4.a), (Vec3d)var2.get(var4.b), 0.01 * (double)var1.size, iAsInt(var1, var4.a, false), iAsInt(var1, var4.b, false));
            }
         }
      }
   }

   private static void bAsVoid(o_ var0, Vec3d var1, Vec3d var2, Vec3d var3, double var4, int var6, int var7) {
      Vec3d var8 = var3.multiply(var4 * 0.4);
      LAsVoid(var0, var1.add(var8), bg.pAsInt(var6, 0.72));
      LAsVoid(var0, var2.add(var8), bg.pAsInt(var7, 0.72));
      LAsVoid(var0, var2.subtract(var8), bg.pAsInt(var7, 0.45));
      LAsVoid(var0, var1.subtract(var8), bg.pAsInt(var6, 0.45));
   }

   private static void rAsVoid(o_ var0, Vec3d var1, Vec3d var2, Vec3d var3, Vec3d var4, double var5, int var7) {
      Vec3d var8 = var3.subtract(var2).normalize().multiply(var5);
      Vec3d var9 = var8.crossProduct(var4).normalize().multiply(var5);
      LAsVoid(var0, var1.add(var8), var7);
      LAsVoid(var0, var1.add(var9), bg.pAsInt(var7, 0.75));
      LAsVoid(var0, var1.subtract(var8), bg.pAsInt(var7, 0.55));
      LAsVoid(var0, var1.subtract(var9), var7);
   }

   private static RodAttempt.Type_s nAsRo_Type_s(RodAttempt.Type_s var0, RodAttempt.Type_s var1, float var2) {
      return new RodAttempt.Type_s(var0.x + (var1.x - var0.x) * var2, var0.y + (var1.y - var0.y) * var2, var0.z + (var1.z - var0.z) * var2);
   }

   private static void LAsVoid(o_ var0, Vec3d var1, int var2) {
      var0.vertex(var1.x, var1.y, var1.z, var2);
   }

   private static int VAsInt(int var0, int var1) {
      return var1 << 24 | var0 & 16777215;
   }

   private static float vAsFloat(float var0) {
      return var0 * (float) (Math.PI / 180.0);
   }

   private RodAttempt() {
   }

   static {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 12; var1++) {
         var0.add(new RodAttempt.Type_t(2, 16 + var1, 17 + var1, 2));
         var0.add(new RodAttempt.Type_t(16 + var1, Z[var1], Z[var1 + 1], 17 + var1));
      }

      var0.add(new RodAttempt.Type_t(0, 1, 2, 28));
      var0.add(new RodAttempt.Type_t(2, 3, 4, 16));
      T = List.copyOf(var0);
   }

   public static record Type_d(int a, int b, float width, boolean ridge) {
   }

   public static record Type_h(
      float preY,
      float preZ,
      float anchorY,
      float anchorZ,
      float pitch,
      float openScale,
      float scale,
      float walkSpread,
      float amplitude,
      float hingeX,
      float hingeZ,
      float splay,
      float wingPitch,
      float frequency
   ) {
   }

   public static final class Type_j {
      private boolean o;
      private float p;
      private float E;
      private float Z;
      private float c;
      private float h;
      private float u;
      private float A;
      private Vec3d a;

      public Type_j() {
      }

      public void GAsVoid(float var1, boolean var2, float var3, boolean var4, Vec3d var5) {
         this.cAsVoid(var1, var2, var3, var4, false, var5);
      }

      public void cAsVoid(float var1, boolean var2, float var3, boolean var4, boolean var5, Vec3d var6) {
         float var7 = Math.clamp(var3, 0.0F, 1.0F);
         int var8 = !this.o || var1 < this.p || var1 - this.p > 10.0F || this.a != null && var6.squaredDistanceTo(this.a) > 64.0 ? 1 : 0;
         if (var8 != 0) {
            this.E = var2 ? 1.0F : 0.0F;
            this.Z = var7;
            this.c = var4 ? 1.0F : 0.0F;
            this.h = var5 ? 1.0F : 0.0F;
            this.u = 0.0F;
         } else {
            float var9 = Math.clamp((var1 - this.p) * 0.05F, 0.0F, 0.1F);
            float var10 = 1.0F - (float)Math.exp((double)(-var9 / 0.13F));
            float var11 = 1.0F - (float)Math.exp((double)(-var9 / 0.19F));
            this.E = this.E + ((float)(var2 ? 1 : 0) - this.E) * var10;
            this.c = this.c + ((float)(var4 ? 1 : 0) - this.c) * var10;
            this.h = this.h + ((float)(var5 ? 1 : 0) - this.h) * var10;
            this.Z = this.Z + (var7 - this.Z) * var11;
            this.u = var9 * 20.0F;
         }

         this.p = var1;
         this.a = var6;
         this.o = true;
      }

      public void OAsVoid(float var1) {
         this.A = (float)((double)(this.A + this.u * var1) % (Math.PI * 2));
      }

      public float eAsFloat() {
         return this.E;
      }

      public float aAsFloat() {
         return this.Z;
      }

      public float YAsFloat() {
         return this.c;
      }

      public float vAsFloat() {
         return this.h;
      }

      public float ZAsFloat() {
         return this.A;
      }
   }

   public static record Type_r(List<Vec3d> points, Vec3d normal, int primary, int secondary, float size, float pulse) {
   }

   public static record Type_s(float x, float y, float z) {
   }

   public static record Type_t(int a, int b, int c, int d) {
   }

   public static record Type_v(List<RodAttempt.Type_s> points, float pulse) {
   }

   public static enum Type_x {
      HALO,
      SURFACE,
      DETAIL;

      private Type_x() {
      }
   }
}
