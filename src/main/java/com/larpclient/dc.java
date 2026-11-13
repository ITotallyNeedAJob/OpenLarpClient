package com.larpclient;

import java.util.List;
import net.minecraft.util.math.Vec3d;

public final class dc {
   public static dc.Type_g iAsDc_Type_g(List<lb.Type_l> var0, double var1, dc.Type_c var3) {
      if (var0.size() >= 2 && !(var1 - var0.getLast().time() >= var3.duration)) {
         int var4 = var3.smoothing ? Math.clamp((long)var3.subdivisions, 1, 4) : 1;
         dc.Type_l var5 = new dc.Type_l(var0.size() * var4 * 12);
         dc.Type_l var6 = new dc.Type_l(var0.size() * var4 * 20 + var0.size() * 4);

         for (int var7 = 1; var7 < var0.size(); var7++) {
            lb.Type_l var8 = var0.get(var7 - 1);
            lb.Type_l var9 = var0.get(var7);
            if (var8.stroke() == var9.stroke() && !(var8.tip().squaredDistanceTo(var9.tip()) < 1.0E-9)) {
               lb.Type_l var10 = var7 > 1 && var0.get(var7 - 2).stroke() == var8.stroke() ? var0.get(var7 - 2) : var8;
               lb.Type_l var11 = var7 + 1 < var0.size() && var0.get(var7 + 1).stroke() == var8.stroke() ? var0.get(var7 + 1) : var9;
               int var12 = var7 - 1;

               while (var12 > 0 && var0.get(var12 - 1).stroke() == var8.stroke()) {
                  var12--;
               }

               int var13 = var7;

               while (var13 + 1 < var0.size() && var0.get(var13 + 1).stroke() == var8.stroke()) {
                  var13++;
               }

               dc.Type_o var14 = gAsDc_Type_o(var10, var8, var9, var11, 0.0, (double)(var7 - 1 - var12), var13 - var12, var1, var3);

               for (int var15 = 1; var15 <= var4; var15++) {
                  double var16 = (double)var15 / (double)var4;
                  dc.Type_o var18 = gAsDc_Type_o(var10, var8, var9, var11, var16, (double)(var7 - 1 - var12) + var16, var13 - var12, var1, var3);
                  WAsVoid(var5, var6, var14, var18, var3, (double)var8.serial() + (double)(var9.serial() - var8.serial()) * var16);
                  var14 = var18;
               }

               if (var3.sparks && var9.serial() % 7L == 0L && var7 > 1) {
                  JAsVoid(var6, var8, var9, var1, var3);
               }
            }
         }

         return new dc.Type_g(var5, var6);
      } else {
         return dc.Type_g.EMPTY;
      }
   }

   private static dc.Type_o gAsDc_Type_o(
      lb.Type_l var0, lb.Type_l var1, lb.Type_l var2, lb.Type_l var3, double var4, double var6, int var8, double var9, dc.Type_c var11
   ) {
      Vec3d var12 = var11.smoothing ? RibbonCapacity.dAsVec3d(var0.tip(), var1.tip(), var2.tip(), var3.tip(), var4) : var1.tip().lerp(var2.tip(), var4);
      Vec3d var13 = var11.smoothing ? RibbonCapacity.dAsVec3d(var0.root(), var1.root(), var2.root(), var3.root(), var4) : var1.root().lerp(var2.root(), var4);
      Vec3d var14 = var13.subtract(var12);
      double var15 = var14.length();
      if (var15 < 1.0E-8) {
         var14 = new Vec3d(0.0, -1.0, 0.0);
      } else {
         var14 = var14.multiply(1.0 / var15);
      }

      double var17 = Math.clamp((var9 - (var1.time() + (var2.time() - var1.time()) * var4)) / var11.duration, 0.0, 1.0);
      double var19 = Math.clamp(var6 / Math.max(1.0, (double)var8 * 0.2), 0.0, 1.0);
      var19 = var19 * var19 * (3.0 - 2.0 * var19);
      double var21 = Math.pow(1.0 - var17, 1.65) * var19;
      double var23 = var15 * var11.width * (0.78 + 0.22 * Math.cos(var17 * 3.0)) * Math.sqrt(var21);
      if (var11.style.equals("Void")) {
         var23 *= 0.7 + 0.3 * (1.0 - var17);
      }

      int var25 = bg.xAsInt(var11.secondary, var11.primary, 1.0 - var17 * 0.8);
      if (var11.style.equals("Prismatic")) {
         int var26 = bg.HSBtoRGB((float)((0.53 + var17 * 0.35 + var9 * 0.08) % 1.0), 0.5F, 1.0F);
         var25 = bg.xAsInt(var25, var26, 0.45);
      }

      return new dc.Type_o(var12, var14, var23, var25, var21 * var11.intensity);
   }

   private static void WAsVoid(dc.Type_l var0, dc.Type_l var1, dc.Type_o var2, dc.Type_o var3, dc.Type_c var4, double var5) {
      if (!(var2.width < 1.0E-7) || !(var3.width < 1.0E-7)) {
         boolean var7 = var4.style.equals("Void");
         int var8 = var7 ? bg.xAsInt(var2.color, -16317420, 0.95) : var2.color;
         int var9 = var7 ? bg.xAsInt(var3.color, -16317420, 0.95) : var3.color;
         DAsVoid(var0, var2, var3, 1.0, 0.8, var8, var9, 0.0, var7 ? 0.68 : 0.09);
         DAsVoid(var0, var2, var3, 0.8, 0.12, var8, var9, var7 ? 0.68 : 0.09, var7 ? 0.85 : 0.32);
         DAsVoid(var0, var2, var3, 0.12, 0.0, var8, var9, var7 ? 0.85 : 0.32, 0.05);
         double var10 = var4.style.equals("Prismatic") ? 0.009 : 0.014;
         CAsVoid(var1, var2, var3, 0.0, var10, bg.xAsInt(var2.color, -1, 0.72), bg.xAsInt(var3.color, -1, 0.72), 0.92);
         CAsVoid(var1, var2, var3, 0.56, var10 * 0.46, var2.color, var3.color, 0.34);
         if (var7) {
            CAsVoid(var1, var2, var3, 0.95, var10 * 0.65, var2.color, var3.color, 0.7);
         }

         if (var4.style.equals("Prismatic")) {
            double var12 = Math.sin(var5 * 1.3);
            double var14 = Math.max(0.0, var12) * Math.max(0.0, var12);
            CAsVoid(var1, var2, var3, 0.84, var10 * 0.5, bg.xAsInt(var2.color, var4.secondary, 0.7), bg.xAsInt(var3.color, var4.secondary, 0.7), 0.48 * var14);
         }

         if (var4.halo) {
            CAsVoid(var1, var2, var3, 0.0, var10 * 4.5, var2.color, var3.color, 0.085);
            CAsVoid(var1, var2, var3, 0.0, var10 * 2.5, var2.color, var3.color, 0.13);
         }
      }
   }

   private static void DAsVoid(dc.Type_l var0, dc.Type_o var1, dc.Type_o var2, double var3, double var5, int var7, int var8, double var9, double var11) {
      AAsVoid(
         var0,
         var1.atAsVec3d(var3),
         var2.atAsVec3d(var3),
         var2.atAsVec3d(var5),
         var1.atAsVec3d(var5),
         bg.pAsInt(var7, var1.opacity * var9),
         bg.pAsInt(var8, var2.opacity * var9),
         bg.pAsInt(var8, var2.opacity * var11),
         bg.pAsInt(var7, var1.opacity * var11)
      );
   }

   private static void CAsVoid(dc.Type_l var0, dc.Type_o var1, dc.Type_o var2, double var3, double var5, int var7, int var8, double var9) {
      Vec3d var11 = var1.atAsVec3d(var3);
      Vec3d var12 = var2.atAsVec3d(var3);
      Vec3d var13 = var1.axis.multiply(var5 * Math.sqrt(Math.max(0.0, var1.opacity)) * 0.5);
      Vec3d var14 = var2.axis.multiply(var5 * Math.sqrt(Math.max(0.0, var2.opacity)) * 0.5);
      int var15 = bg.pAsInt(var7, var1.opacity * var9);
      int var16 = bg.pAsInt(var8, var2.opacity * var9);
      AAsVoid(var0, var11.add(var13), var12.add(var14), var12.subtract(var14), var11.subtract(var13), var15, var16, var16, var15);
   }

   private static void JAsVoid(dc.Type_l var0, lb.Type_l var1, lb.Type_l var2, double var3, dc.Type_c var5) {
      double var6 = var3 - var2.time();
      double var8 = Math.min(0.3, var5.duration * 1.2);
      if (!(var6 < 0.0) && !(var6 >= var8)) {
         Vec3d var10 = var2.tip().subtract(var1.tip());
         if (!(var10.lengthSquared() < 1.0E-4)) {
            Vec3d var11 = var2.root().subtract(var2.tip()).normalize();
            double var12 = (var2.serial() & 1L) == 0L ? -1.0 : 1.0;
            Vec3d var14 = var10.normalize().multiply(0.3).add(var11.crossProduct(new Vec3d(0.0, 0.0, 1.0)).multiply(var12 * 0.25));
            Vec3d var15 = var2.tip().add(var14.multiply(var6)).add(0.0, -0.18 * var6 * var6, 0.0);
            Vec3d var16 = var15.subtract(var14.multiply(0.035));
            Vec3d var17 = new Vec3d(-var14.y, var14.x, 0.0).normalize().multiply(0.0025 * (1.0 - var6 / var8));
            int var18 = bg.pAsInt(bg.xAsInt(var5.primary, -1, 0.55), (1.0 - var6 / var8) * var5.intensity * 0.8);
            AAsVoid(var0, var15.add(var17), var16.add(var17), var16.subtract(var17), var15.subtract(var17), var18, 0, 0, var18);
         }
      }
   }

   private static void AAsVoid(dc.Type_l var0, Vec3d var1, Vec3d var2, Vec3d var3, Vec3d var4, int var5, int var6, int var7, int var8) {
      var0.QAsVoid(var1, var5);
      var0.QAsVoid(var2, var6);
      var0.QAsVoid(var3, var7);
      var0.QAsVoid(var4, var8);
   }

   private dc() {
   }

   public static record Type_c(
      String style,
      double duration,
      double width,
      double intensity,
      int primary,
      int secondary,
      boolean halo,
      boolean sparks,
      boolean smoothing,
      int subdivisions
   ) {
   }

   public static record Type_g(dc.Type_l body, dc.Type_l light) {
      public static final dc.Type_g EMPTY = new dc.Type_g(new dc.Type_l(0), new dc.Type_l(0));

      public int vertices() {
         return this.body.DAsInt() + this.light.DAsInt();
      }
   }

   public static final class Type_l {
      private final float[] R;
      private final int[] h;
      private int C;

      private Type_l(int var1) {
         this.R = new float[var1 * 3];
         this.h = new int[var1];
      }

      public int DAsInt() {
         return this.C;
      }

      public boolean AAsBoolean() {
         return this.C == 0;
      }

      public float NAsFloat(int var1) {
         return this.R[var1 * 3];
      }

      public float ZAsFloat(int var1) {
         return this.R[var1 * 3 + 1];
      }

      public float JAsFloat(int var1) {
         return this.R[var1 * 3 + 2];
      }

      public int BAsInt(int var1) {
         return this.h[var1];
      }

      private void QAsVoid(Vec3d var1, int var2) {
         this.R[this.C * 3] = (float)var1.x;
         this.R[this.C * 3 + 1] = (float)var1.y;
         this.R[this.C * 3 + 2] = (float)var1.z;
         this.h[this.C++] = var2;
      }
   }

   private static record Type_o(Vec3d tip, Vec3d axis, double width, int color, double opacity) {
      Vec3d atAsVec3d(double var1) {
         return this.tip.add(this.axis.multiply(this.width * var1));
      }
   }
}
