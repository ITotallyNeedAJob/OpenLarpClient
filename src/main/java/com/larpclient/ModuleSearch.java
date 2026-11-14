package com.larpclient;

import java.util.Arrays;

public final class ModuleSearch {
   public static final float S = 0.9F;
   public static final float H = 4.8F;
   public static final float g = 12.0F;
   public static final int t = 64;
   private static final float[][] p = new float[][]{
      {0.9F, 0.64F}, {1.4F, 0.57F}, {2.06F, 0.51F}, {2.73F, 0.46F}, {3.39F, 0.39F}, {3.97F, 0.3F}, {4.39F, 0.18F}
   };
   private static final float[] q = new float[]{1.0F, 0.86F, 0.95F, 1.08F, 0.9F, 0.97F};
   private static final float O = 0.88F;
   private static final float F = 0.24F;
   private static final float V = 0.08F;
   private static final float T = 0.4F;
   private static ModuleSearch.Type_w w;

   private ModuleSearch() {
   }

   public static synchronized ModuleSearch.Type_w ZAsMs_Type_w() {
      if (w == null) {
         w = nAsMs_Type_w();
      }

      return w;
   }

   static ModuleSearch.Type_w nAsMs_Type_w() {
      ModuleSearch.Type_n var0 = new ModuleSearch.Type_n();
      var0.dAsVoid(0.3F, -0.05F, 0.05F, 1.0F, 0.9F, 0.95F, 8, 5, 11);
      var0.dAsVoid(-0.45F, 0.4F, -0.1F, 0.6F, 0.55F, 0.58F, 7, 4, 23);
      int var1 = q.length;
      int[] var2 = null;

      for (int var3 = 0; var3 < p.length; var3++) {
         float var4 = p[var3][0];
         float var5 = p[var3][1];
         float var6 = xAsFloat(var4);
         float var7 = cAsFloat(var6, true);
         float var8 = cAsFloat(var6, false);
         float var9 = p[Math.max(0, var3 - 1)][1];
         float var10 = var3 + 1 < p.length ? p[var3 + 1][1] : 0.0F;
         float var11 = (var3 + 1 < p.length ? p[var3 + 1][0] : 4.8F) - p[Math.max(0, var3 - 1)][0];
         float var12 = (var9 - var10) / var11;
         int[] var13 = new int[var1 + 1];

         for (int var14 = 0; var14 <= var1; var14++) {
            double var15 = (Math.PI * 2) * (double)var14 / (double)var1 + (double)(0.4F * var6);
            float var17 = var5 * q[var14 % var1];
            float var18 = var7 + (float)Math.cos(var15) * var17;
            float var19 = var8 + (float)Math.sin(var15) * var17 * 0.88F;
            var13[var14] = var0.CAsInt(
               var18,
               var4,
               var19,
               (float)Math.cos(var15),
               var12,
               (float)Math.sin(var15) / 0.88F,
               WAsFloat((float)var14 / (float)var1),
               UAsFloat(0.25F + 0.75F * var6),
               0.1F + 0.9F * var6,
               1.0F
            );
         }

         if (var2 != null) {
            for (int var30 = 0; var30 < var1; var30++) {
               var0.FAsVoid(var2[var30], var13[var30], var13[var30 + 1], var2[var30 + 1]);
            }
         } else {
            int var29 = var0.CAsInt(var7, var4, var8, 0.0F, -1.0F, 0.0F, WAsFloat(0.5F), UAsFloat(0.25F), 0.1F, 1.0F);
            float[] var31 = new float[]{0.0F, -1.0F, 0.0F};
            int[] var16 = new int[var1 + 1];

            for (int var32 = 0; var32 <= var1; var32++) {
               var16[var32] = var0.UAsInt(var13[var32], var31);
            }

            for (int var33 = 0; var33 < var1; var33++) {
               var0.FAsVoid(var29, var16[var33], var16[var33 + 1], var16[var33 + 1]);
            }
         }

         var2 = var13;
      }

      float var20 = cAsFloat(1.0F, true);
      float var21 = cAsFloat(1.0F, false);

      for (int var22 = 0; var22 < var1; var22++) {
         int var23 = var2[var22];
         int var24 = var2[var22 + 1];
         float[] var25 = var0.SAsFloatArray(var23, var24, var20, 4.8F, var21);
         int var26 = var0.UAsInt(var23, var25);
         int var27 = var0.UAsInt(var24, var25);
         int var28 = var0.CAsInt(var20, 4.8F, var21, var25[0], var25[1], var25[2], WAsFloat(((float)var22 + 0.5F) / (float)var1), UAsFloat(1.0F), 1.0F, 1.0F);
         var0.FAsVoid(var26, var28, var28, var27);
      }

      return var0.TAsMs_Type_w();
   }

   static float WAsFloat(float var0) {
      return (0.5F + 31.0F * var0) / 64.0F;
   }

   static float UAsFloat(float var0) {
      return (0.5F + 63.0F * var0) / 64.0F;
   }

   static float xAsFloat(float var0) {
      return Math.clamp((var0 - 0.9F) / 3.9F, 0.0F, 1.0F);
   }

   private static float cAsFloat(float var0, boolean var1) {
      return var1 ? 0.08F * var0 : -0.24F * (float)Math.sin(Math.PI * (double)var0);
   }

   public static float qAsFloat(float var0, float var1, double var2) {
      return var0 + (var0 - 0.9F) * (float)(var2 - 1.0) * var1;
   }

   public static double fAsDouble() {
      return 2.846154F;
   }

   public static void dAsVoid(ModuleSearch.Type_w var0, int var1, double var2, double var4, boolean var6, float[] var7) {
      float var8 = var0.positions[var1 * 3];
      float var9 = var0.positions[var1 * 3 + 1];
      float var10 = var0.positions[var1 * 3 + 2];
      float var11 = var0.stretch[var1];
      float var12 = (float)var4;
      float var13 = var6 ? -1.0F : 1.0F;
      int var14 = var11 > 0.0F ? 1 : 0;
      float var15 = var14 != 0 ? 1.0F + (float)(var2 - 1.0) * var11 : var12;
      var7[0] = var8 * var12 * var13;
      var7[1] = var14 != 0 ? qAsFloat(var9, var11, var2) : var9 * var12;
      var7[2] = var10 * var12;
      float var16 = var0.normals[var1 * 3] / var12 * var13;
      float var17 = var0.normals[var1 * 3 + 1] / var15;
      float var18 = var0.normals[var1 * 3 + 2] / var12;
      float var19 = (float)Math.sqrt((double)(var16 * var16 + var17 * var17 + var18 * var18));
      var7[3] = var16 / var19;
      var7[4] = var17 / var19;
      var7[5] = var18 / var19;
   }

   public static int EAsInt(int var0, int var1, float var2) {
      float var3 = Math.clamp((var2 - 0.1F) / 0.9F, 0.0F, 1.0F);
      int var4 = -16777216;

      for (int var5 = 16; var5 >= 0; var5 -= 8) {
         int var6 = var0 >> var5 & 0xFF;
         int var7 = var1 >> var5 & 0xFF;
         var4 |= Math.round((float)var6 + (float)(var7 - var6) * var3) << var5;
      }

      return var4;
   }

   public static float lAsFloat(float var0) {
      float var1 = Math.clamp((var0 - 0.45F) / 0.55F, 0.0F, 1.0F);
      return var1 * var1 * (3.0F - 2.0F * var1);
   }

   public static int xAsInt(int var0, double var1, float var3) {
      float var4 = (float)(Math.clamp(var1, 0.0, 1.0) * (double)lAsFloat(var3) * 2.2);
      int var5 = -16777216;

      for (int var6 = 16; var6 >= 0; var6 -= 8) {
         var5 |= Math.min(255, Math.round((float)(var0 >> var6 & 0xFF) * var4)) << var6;
      }

      return var5;
   }

   public static int[] sAsIntArray() {
      int[] var3 = new int[4096];

      for (int var4 = 0; var4 < 64; var4++) {
         for (int var5 = 0; var5 < 32; var5++) {
            float var6;
            float var7;
            if (var4 < 16) {
               var6 = 0.8F + 0.07F * DAsFloat(var5, var4, 5);
               var7 = 0.0F;
            } else {
               float var8 = ((float)(var4 - 16) + 0.5F) / 48.0F;
               float var9 = ((float)var5 + 0.5F) / 32.0F;
               float var10 = 0.0F;

               for (int var11 = 0; var11 < 3; var11++) {
                  float var12 = (float)((double)var11 * 0.33 + 0.12 + 0.05 * Math.sin((double)(var8 * 9.0F) + (double)var11 * 2.1));
                  float var13 = Math.abs(var9 - (var12 - (float)Math.floor((double)var12)));
                  var13 = Math.min(var13, 1.0F - var13) * 32.0F;
                  float var14 = (float)(0.55 + 0.45 * Math.sin((double)(var8 * 13.0F) + (double)var11 * 1.7));
                  var10 += (float)Math.exp((double)(-var13 * var13) / 1.2) * var14;
               }

               var10 = Math.min(1.0F, var10);
               var6 = Math.min(1.0F, 0.86F + 0.05F * DAsFloat(var5, var4, 9) + 0.2F * var10);
               var7 = (float)Math.pow((double)Math.clamp((var8 - 0.45F) / 0.55F, 0.0F, 1.0F), 1.5) * (0.35F + 0.65F * var10);
            }

            var3[var4 * 64 + var5] = MAsInt(var6);
            var3[var4 * 64 + var5 + 32] = MAsInt(var7);
         }
      }

      return var3;
   }

   static float DAsFloat(int var0, int var1, int var2) {
      int var3 = var0 * 374761393 + var1 * 668265263 + var2 * Integer.MAX_VALUE;
      var3 = (var3 ^ var3 >>> 13) * 1274126177;
      var3 ^= var3 >>> 16;
      return (float)(var3 & 65535) / 32767.5F - 1.0F;
   }

   private static int MAsInt(float var0) {
      int var1 = Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F);
      return 0xFF000000 | var1 << 16 | var1 << 8 | var1;
   }

   private static final class Type_n {
      private float[] M = new float[384];
      private float[] c = new float[384];
      private float[] a = new float[256];
      private float[] k = new float[128];
      private float[] z = new float[128];
      private int[] H = new int[512];
      private int f;
      private int x;

      private Type_n() {
      }

      int CAsInt(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
         if (this.f == this.k.length) {
            this.M = Arrays.copyOf(this.M, this.f * 6);
            this.c = Arrays.copyOf(this.c, this.f * 6);
            this.a = Arrays.copyOf(this.a, this.f * 4);
            this.k = Arrays.copyOf(this.k, this.f * 2);
            this.z = Arrays.copyOf(this.z, this.f * 2);
         }

         float var11 = (float)Math.sqrt((double)(var4 * var4 + var5 * var5 + var6 * var6));
         if (var11 < 1.0E-6F) {
            var4 = 0.0F;
            var5 = 1.0F;
            var6 = 0.0F;
            var11 = 1.0F;
         }

         this.M[this.f * 3] = var1;
         this.M[this.f * 3 + 1] = var2;
         this.M[this.f * 3 + 2] = var3;
         this.c[this.f * 3] = var4 / var11;
         this.c[this.f * 3 + 1] = var5 / var11;
         this.c[this.f * 3 + 2] = var6 / var11;
         this.a[this.f * 2] = var7;
         this.a[this.f * 2 + 1] = var8;
         this.k[this.f] = var9;
         this.z[this.f] = var10;
         return this.f++;
      }

      int UAsInt(int var1, float[] var2) {
         return this.CAsInt(
            this.M[var1 * 3],
            this.M[var1 * 3 + 1],
            this.M[var1 * 3 + 2],
            var2[0],
            var2[1],
            var2[2],
            this.a[var1 * 2],
            this.a[var1 * 2 + 1],
            this.k[var1],
            this.z[var1]
         );
      }

      float[] SAsFloatArray(int var1, int var2, float var3, float var4, float var5) {
         float var6 = this.M[var1 * 3];
         float var7 = this.M[var1 * 3 + 1];
         float var8 = this.M[var1 * 3 + 2];
         float var9 = var3 - var6;
         float var10 = var4 - var7;
         float var11 = var5 - var8;
         float var12 = this.M[var2 * 3] - var6;
         float var13 = this.M[var2 * 3 + 1] - var7;
         float var14 = this.M[var2 * 3 + 2] - var8;
         return new float[]{var10 * var14 - var11 * var13, var11 * var12 - var9 * var14, var9 * var13 - var10 * var12};
      }

      void FAsVoid(int var1, int var2, int var3, int var4) {
         if (this.x * 4 == this.H.length) {
            this.H = Arrays.copyOf(this.H, this.H.length * 2);
         }

         this.H[this.x * 4] = var1;
         this.H[this.x * 4 + 1] = var2;
         this.H[this.x * 4 + 2] = var3;
         this.H[this.x * 4 + 3] = var4;
         this.x++;
      }

      void dAsVoid(float var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, int var9) {
         int[][] var10 = new int[var8 + 1][var7 + 1];

         for (int var11 = 0; var11 <= var8; var11++) {
            double var12 = Math.PI * (double)var11 / (double)var8;

            for (int var14 = 0; var14 <= var7; var14++) {
               double var15 = (Math.PI * 2) * (double)var14 / (double)var7;
               float var17 = var11 != 0 && var11 != var8 ? 1.0F + 0.07F * ModuleSearch.DAsFloat(var11, var14 % var7, var9) : 1.0F;
               float var18 = (float)(Math.sin(var12) * Math.cos(var15));
               float var19 = (float)(-Math.cos(var12));
               float var20 = (float)(Math.sin(var12) * Math.sin(var15));
               var10[var11][var14] = this.CAsInt(
                  var1 + var18 * var4 * var17,
                  var2 + var19 * var5 * var17,
                  var3 + var20 * var6 * var17,
                  var18 / var4,
                  var19 / var5,
                  var20 / var6,
                  ModuleSearch.WAsFloat((float)var14 / (float)var7),
                  ModuleSearch.UAsFloat(0.25F * (float)var11 / (float)var8),
                  0.1F * (float)var11 / (float)var8,
                  0.0F
               );
            }
         }

         for (int var21 = 0; var21 < var8; var21++) {
            for (int var22 = 0; var22 < var7; var22++) {
               this.FAsVoid(var10[var21][var22], var10[var21 + 1][var22], var10[var21 + 1][var22 + 1], var10[var21][var22 + 1]);
            }
         }
      }

      ModuleSearch.Type_w TAsMs_Type_w() {
         return new ModuleSearch.Type_w(
            Arrays.copyOf(this.M, this.f * 3),
            Arrays.copyOf(this.c, this.f * 3),
            Arrays.copyOf(this.a, this.f * 2),
            Arrays.copyOf(this.k, this.f),
            Arrays.copyOf(this.z, this.f),
            Arrays.copyOf(this.H, this.x * 4)
         );
      }
   }

   public static record Type_w(float[] positions, float[] normals, float[] uvs, float[] gradient, float[] stretch, int[] quads) {
      public int vertices() {
         return this.gradient.length;
      }

      public int faces() {
         return this.quads.length / 4;
      }
   }
}
