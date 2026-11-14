package com.larpclient;

import java.util.Arrays;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class RifleMeshConstraints {
   public static final String V = "ak47";
   public static final double Q = -1.0;
   public static final double W = 8.0;
   public static final double K = -1.5;
   public static final double SahurDanceEngine = -2.0;
   public static final double ChatPayParser = 1.2;
   public static final double AuraModule = 3.3;
   public static final double M = 0.64;
   public static final float q = -0.11886F;
   public static final float F = -0.77511F;
   public static final float I = -0.04688F;
   public static final float R = -0.11886F;
   public static final float Y = -0.19011F;
   public static final int z = 32;
   public static final int B = 2;
   public static final int CropHelper = 12;
   public static final int C = 8;
   static final float X = 1.0F;
   static final float MenuPopupProvider = 4.5F;
   static final float v = 1.6F;
   static final float DropletTrail = 14.0F;
   static final int BloomUtility = 4;
   static final float PriceDetector = 7.0F;
   static final float SkyChunkCache = 0.035F;
   private static final float PetEffectRenderer = 0.03125F;
   private static final float LineSegment = 0.0625F;
   private static final int U = 2;
   private static final int D = 4;

   private RifleMeshConstraints() {
   }

   public static float[] FAsFloatArray(PropMeshReader.Type_a var0) {
      if (var0.parts().size() != 1) {
         throw new IllegalArgumentException("The rifle mesh has one part");
      } else {
         float[] var1 = var0.parts().getFirst().vertices();
         int var2 = var1.length / 15;
         float[] var3 = new float[var2 * 4 * 8];
         int var4 = 0;

         for (int var5 = 0; var5 < var2; var5++) {
            int var6 = var5 * 15;
            int[] var7 = null;
            if (var5 + 1 < var2) {
               int var8 = var6 + 15;
               if (vAsBoolean(var1, var6, var8) && vAsBoolean(var1, var6 + 10, var8 + 5)) {
                  var7 = new int[]{var6, var6 + 5, var6 + 10, var8 + 10};
               } else if (vAsBoolean(var1, var6, var8) && vAsBoolean(var1, var6 + 5, var8 + 10)) {
                  var7 = new int[]{var8, var8 + 5, var8 + 10, var6 + 10};
               }
            }

            if (var7 == null) {
               var7 = new int[]{var6, var6 + 5, var6 + 10, var6 + 10};
            } else {
               var5++;
            }

            float[] var14 = qAsFloatArray(var1, var7[0], var7[1], var7[2]);

            for (int var12 : var7) {
               int var13 = var4++ * 8;
               System.arraycopy(var1, var12, var3, var13, 5);
               System.arraycopy(var14, 0, var3, var13 + 5, 3);
            }
         }

         return Arrays.copyOf(var3, var4 * 8);
      }
   }

   private static boolean vAsBoolean(float[] var0, int var1, int var2) {
      return var0[var1] == var0[var2] && var0[var1 + 1] == var0[var2 + 1] && var0[var1 + 2] == var0[var2 + 2];
   }

   private static float[] qAsFloatArray(float[] var0, int var1, int var2, int var3) {
      Vector3f var4 = new Vector3f(var0[var2] - var0[var1], var0[var2 + 1] - var0[var1 + 1], var0[var2 + 2] - var0[var1 + 2])
         .cross(var0[var3] - var0[var1], var0[var3 + 1] - var0[var1 + 1], var0[var3 + 2] - var0[var1 + 2]);
      if (var4.lengthSquared() < 1.0E-20F) {
         return new float[]{0.0F, -1.0F, 0.0F};
      } else {
         var4.normalize();
         return new float[]{var4.x, var4.y, var4.z};
      }
   }

   public static void uAsVoid(MatrixStack var0, boolean var1, double var2, double var4, double var6, double var8) {
      if (var1) {
         var0.translate(-0.0625, 0.5, -0.09375);
         var0.multiply(new Quaternionf().rotationZYX((float)var6, (float)var4, (float)var2));
      } else {
         var0.translate(-0.125, 0.075, 0.20625);
         var0.multiply(PAsQuaternionf());
      }

      var0.scale((float)var8, (float)var8, (float)var8);
   }

   public static Quaternionf PAsQuaternionf() {
      float var0 = (float)Math.sin(0.64);
      float var1 = (float)Math.cos(0.64);
      return new Quaternionf().setFromNormalized(new Matrix3f(0.0F, 0.0F, 1.0F, -var1, var0, 0.0F, -var0, -var1, 0.0F));
   }

   public static int mAsInt(float var0, int var1) {
      if (!(var0 >= 0.0F)) {
         return -1;
      } else {
         int var2 = (int)Math.floor((double)(var0 * 32.0F / 2.0F)) - var1;
         return var2 >= 0 && var2 < 12 ? var2 : -1;
      }
   }

   public static float KAsFloat(float var0, int var1) {
      return var0 * 32.0F - (float)(var1 * 2);
   }

   public static float[] pAsFloatArray(float var0) {
      RifleMeshConstraints.Type_q var1 = new RifleMeshConstraints.Type_q();
      int var2 = mAsInt(var0, 0);
      if (var2 >= 0) {
         float var3 = KAsFloat(var0, var2);
         if (var3 < 1.0F) {
            float var4 = (float)Math.sqrt((double)(1.0F - var3 / 1.0F));
            float var5 = 0.3F * var4;
            float var6 = 0.085F * var4;
            float var10 = -0.78511F - var5;
            var1.xAsVoid(
               new float[]{-var6, -0.11886F, -0.78511F, 0.5F, 0.0F},
               new float[]{var6, -0.11886F, -0.78511F, 0.5F, 1.0F},
               new float[]{var6, -0.11886F, var10, 1.0F, 1.0F},
               new float[]{-var6, -0.11886F, var10, 1.0F, 0.0F}
            );
            var1.xAsVoid(
               new float[]{0.0F, -0.11886F - var6, -0.78511F, 0.5F, 0.0F},
               new float[]{0.0F, -0.11886F + var6, -0.78511F, 0.5F, 1.0F},
               new float[]{0.0F, -0.11886F + var6, var10, 1.0F, 1.0F},
               new float[]{0.0F, -0.11886F - var6, var10, 1.0F, 0.0F}
            );
            double var11 = (double)var2 * 1.1;
            float var13 = var6 * 1.5F;
            float var14 = (float)Math.cos(var11) * var13;
            float var15 = (float)Math.sin(var11) * var13;
            var1.xAsVoid(
               new float[]{-var14 + var15, -0.11886F - var15 - var14, -0.80511F, 0.5F, 0.0F},
               new float[]{var14 + var15, -0.11886F + var15 - var14, -0.80511F, 1.0F, 0.0F},
               new float[]{var14 - var15, -0.11886F + var15 + var14, -0.80511F, 1.0F, 1.0F},
               new float[]{-var14 - var15, -0.11886F - var15 + var14, -0.80511F, 0.5F, 1.0F}
            );
         }
      }

      for (int var17 = 0; var17 < 2; var17++) {
         int var18 = mAsInt(var0, var17);
         if (var18 >= 0) {
            float var19 = KAsFloat(var0, var18) * 4.5F;
            float var20 = Math.max(0.0F, var19 - 1.6F);
            if (!(var20 >= 14.0F)) {
               float var21 = (float)Math.sin((double)var18 * 2.39) * 0.022F;
               float var22 = (float)Math.sin((double)var18 * 1.71 + 1.0) * 0.018F;
               Vector3f var23 = new Vector3f(var21, var22, -1.0F).normalize();
               float[] var27 = dAsFloatArray(var23, var20);
               float[] var28 = dAsFloatArray(var23, Math.min(var19, 14.0F));
               var1.xAsVoid(
                  new float[]{var27[0] - 0.018F, var27[1], var27[2], 0.015625F, 0.15625F},
                  new float[]{var27[0] + 0.018F, var27[1], var27[2], 0.015625F, 0.15625F},
                  new float[]{var28[0] + 0.018F, var28[1], var28[2], 0.484375F, 0.15625F},
                  new float[]{var28[0] - 0.018F, var28[1], var28[2], 0.484375F, 0.15625F}
               );
               var1.xAsVoid(
                  new float[]{var27[0], var27[1] - 0.018F, var27[2], 0.015625F, 0.15625F},
                  new float[]{var27[0], var27[1] + 0.018F, var27[2], 0.015625F, 0.15625F},
                  new float[]{var28[0], var28[1] + 0.018F, var28[2], 0.484375F, 0.15625F},
                  new float[]{var28[0], var28[1] - 0.018F, var28[2], 0.484375F, 0.15625F}
               );
            }
         }
      }

      return var1.xAsFloatArray();
   }

   private static float[] dAsFloatArray(Vector3f var0, float var1) {
      return new float[]{var0.x * var1, -0.11886F + var0.y * var1, -0.77511F + var0.z * var1};
   }

   public static float[] rAsFloatArray(float var0, Vector3f var1) {
      RifleMeshConstraints.Type_q var2 = new RifleMeshConstraints.Type_q();

      for (int var3 = 0; var3 < 4; var3++) {
         int var4 = mAsInt(var0, var3);
         if (var4 >= 0) {
            float var5 = KAsFloat(var0, var4);
            if (!(var5 >= 7.0F)) {
               float var6 = (float)Math.sin((double)var4 * 3.7) * 0.008F;
               Vector3f var7 = new Vector3f(-0.04688F, -0.11886F, -0.19011F)
                  .add(new Vector3f(-0.055F + var6, -0.045F, 0.012F).mul(var5))
                  .add(new Vector3f(var1).mul(0.0175F * var5 * var5));
               Quaternionf var8 = new Quaternionf().rotationXYZ(var5 * 0.9F + (float)var4, var5 * 1.3F, (float)var4 * 0.7F);
               jAsVoid(var2, var7, var8, 0.011F, 0.011F, 0.028F, 0.28125F);
            }
         }
      }

      return var2.xAsFloatArray();
   }

   private static void jAsVoid(RifleMeshConstraints.Type_q var0, Vector3f var1, Quaternionf var2, float var3, float var4, float var5, float var6) {
      float[][] var7 = new float[][]{{1.0F, 0.0F, 0.0F}, {-1.0F, 0.0F, 0.0F}, {0.0F, 1.0F, 0.0F}, {0.0F, -1.0F, 0.0F}, {0.0F, 0.0F, 1.0F}, {0.0F, 0.0F, -1.0F}};
      float[] var8 = new float[]{var3, var4, var5};

      for (float[] var12 : var7) {
         int var13 = var12[0] != 0.0F ? 0 : (var12[1] != 0.0F ? 1 : 2);
         int var14 = (var13 + 1) % 3;
         int var15 = (var13 + 2) % 3;
         float var16 = var12[var13];
         float var17 = (var13 == 2 ? 1.5F : 0.5F) * 0.03125F;
         float[][] var18 = new float[4][];
         float[][] var19 = new float[][]{{-1.0F, -1.0F}, {1.0F, -1.0F}, {1.0F, 1.0F}, {-1.0F, 1.0F}};

         for (int var20 = 0; var20 < 4; var20++) {
            float[] var21 = var19[var16 > 0.0F ? var20 : 3 - var20];
            Vector3f var22 = new Vector3f();
            var22.setComponent(var13, var16 * var8[var13]);
            var22.setComponent(var14, var21[0] * var8[var14]);
            var22.setComponent(var15, var21[1] * var8[var15]);
            var2.transform(var22).add(var1);
            var18[var20] = new float[]{var22.x, var22.y, var22.z, var17, var6};
         }

         Vector3f var23 = var2.transform(new Vector3f(var12[0], var12[1], var12[2]));
         var0.tAsVoid(var18, var23);
      }
   }

   private static final class Type_q {
      private float[] w = new float[512];
      private int p;

      private Type_q() {
      }

      void xAsVoid(float[] var1, float[] var2, float[] var3, float[] var4) {
         this.tAsVoid(new float[][]{var1, var2, var3, var4}, null);
         this.tAsVoid(new float[][]{var4, var3, var2, var1}, null);
      }

      void tAsVoid(float[][] var1, Vector3f var2) {
         if (var2 == null) {
            Vector3f var3 = new Vector3f(var1[1][0] - var1[0][0], var1[1][1] - var1[0][1], var1[1][2] - var1[0][2])
               .cross(var1[2][0] - var1[0][0], var1[2][1] - var1[0][1], var1[2][2] - var1[0][2]);
            var2 = var3.lengthSquared() < 1.0E-20F ? new Vector3f(0.0F, -1.0F, 0.0F) : var3.normalize();
         }

         if (this.p + 32 > this.w.length) {
            this.w = Arrays.copyOf(this.w, this.w.length * 2);
         }

         for (float[] var6 : var1) {
            System.arraycopy(var6, 0, this.w, this.p, 5);
            this.w[this.p + 5] = var2.x;
            this.w[this.p + 6] = var2.y;
            this.w[this.p + 7] = var2.z;
            this.p += 8;
         }
      }

      float[] xAsFloatArray() {
         return Arrays.copyOf(this.w, this.p);
      }
   }
}
