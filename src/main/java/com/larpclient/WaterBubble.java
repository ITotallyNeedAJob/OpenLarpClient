package com.larpclient;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class WaterBubble {
   private static final Vector3f[] X = new Vector3f[]{new Vector3f(-0.031F, 0.08F, -0.067F), new Vector3f(0.088F, 0.08F, -0.067F)};
   private static final Vector3f[] l = new Vector3f[]{new Vector3f(-0.038F, 0.002F, -0.068F), new Vector3f(0.096F, 0.002F, -0.068F)};
   private static final Vector3f[] i = new Vector3f[]{new Vector3f(0.001F, -0.105F, -0.062F), new Vector3f(0.055F, -0.105F, -0.062F)};
   private static final Vector3f[] G = new Vector3f[]{new Vector3f(-0.001F, -0.194F, -0.067F), new Vector3f(0.057F, -0.194F, -0.067F)};
   private static final float g = 0.002F;
   private static final float Z = -0.194F;
   private static final float O = 0.9F;
   private static final float H = 0.7F;
   private final SahurMeshReader.Type_u m;
   private final SahurMeshReader.Type_u D;
   private final int[] r;
   private final float[] n;
   private final Matrix4f[] j = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Matrix4f[] b = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Vector3f h = new Vector3f();
   private final Vector3f o = new Vector3f();
   private float V;

   public WaterBubble(SahurMeshReader.Type_u var1) {
      SahurArticulation.Type_b var2 = SahurArticulation.eAsAu_Type_b(var1);
      this.m = var1;
      this.D = var2.mesh();
      this.r = var2.joints();
      this.n = (float[])this.D.vertices().clone();
   }

   public SahurMeshReader.Type_u pAsAo_Type_u() {
      return this.m;
   }

   public SahurMeshReader.Type_u aAsAo_Type_u() {
      return this.D;
   }

   public float[] vAsFloatArray() {
      return this.n;
   }

   private static void kAsVoid(Matrix4f var0, Matrix4f var1, Vector3f var2, float var3) {
      var0.set(var1).translate(var2).rotateX(var3).translate(-var2.x, -var2.y, -var2.z);
   }

   private static float xAsFloat(float var0) {
      var0 = Math.clamp(var0, 0.0F, 1.0F);
      return var0 * var0 * (3.0F - 2.0F * var0);
   }

   public float[] xAsFloatArray(float var1, float var2) {
      float var3 = Math.clamp(var2, 0.0F, 1.0F);
      float var4 = var1 * 0.6662F;
      float var5 = (float)Math.cos((double)var4) * var3;
      float var6 = (float)Math.sin((double)var4) * var3;
      float[] var7 = new float[]{var5 * 0.9F, -var5 * 0.9F};
      float[] var8 = new float[]{-var5 * 0.7F, var5 * 0.7F};
      float[] var9 = new float[]{Math.max(0.0F, var6) * 0.9F, Math.max(0.0F, -var6) * 0.9F};
      Matrix4f var10 = new Matrix4f();

      for (int var11 = 0; var11 < 2; var11++) {
         float var12 = -0.3F * var3 - (var11 == 0 ? Math.max(0.0F, var8[var11]) : 0.0F);
         kAsVoid(this.j[var11], var10, X[var11], var8[var11]);
         kAsVoid(this.b[var11], this.j[var11], l[var11], var12);
         kAsVoid(this.j[var11 + 2], var10, i[var11], var7[var11]);
         kAsVoid(this.b[var11 + 2], this.j[var11 + 2], G[var11], var9[var11]);
      }

      float[] var21 = this.D.vertices();
      float var22 = Float.POSITIVE_INFINITY;

      for (int var13 = 0; var13 < this.r.length; var13++) {
         int var14 = var13 * 5;
         int var15 = this.r[var13];
         float var16 = var21[var14];
         float var17 = var21[var14 + 1];
         float var18 = var21[var14 + 2];
         this.h.set(var16, var17, var18);
         if (var15 > 0) {
            int var19 = var15 >= 3 ? 1 : 0;
            int var20 = (var15 - 1) % 2 + (var19 != 0 ? 2 : 0);
            this.o.set(this.h);
            this.j[var20].transformPosition(this.h);
            this.b[var20].transformPosition(this.o);
            this.h.lerp(this.o, xAsFloat(((var19 != 0 ? -0.194F : 0.002F) + 0.013F - var17) / 0.026F));
            if (var19 != 0) {
               var22 = Math.min(var22, this.h.y);
            }
         }

         this.n[var14] = this.h.x;
         this.n[var14 + 1] = this.h.y;
         this.n[var14 + 2] = this.h.z;
      }

      this.V = var22 - this.D.minimumY();

      for (int var23 = 1; var23 < this.n.length; var23 += 5) {
         this.n[var23] = this.n[var23] - this.V;
      }

      return this.n;
   }

   public Vector3f NAsVector3f(boolean var1, Vector3f var2) {
      return this.b[var1 ? 0 : 1].transformPosition(var2).sub(0.0F, this.V, 0.0F);
   }
}
