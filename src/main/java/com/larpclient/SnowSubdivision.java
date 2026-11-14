package com.larpclient;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.client.util.math.MatrixStack.Entry;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class SnowSubdivision {
   public static final float I = 0.55F;
   public static final int f = 9;

   public static VertexConsumer zAsVertexConsumer(VertexConsumer var0) {
      return (VertexConsumer)(TerrainMaterialClassifier.aAsBoolean() ? new SnowSubdivision.Type_i(var0) : var0);
   }

   public static void JAsVoid(
      VertexConsumer var0, Entry var1, BakedQuad var2, float[] var3, float var4, float var5, float var6, float var7, int[] var8, int var9, int var10
   ) {
      if (var10 >= 1 && var10 <= 3) {
         Vector3fc var11 = var2.position0();
         Vector3fc var12 = var2.position1();
         Vector3fc var13 = var2.position3();
         int var14 = gAsBoolean(var11, var12) ? var10 : 1;
         int var15 = gAsBoolean(var11, var13) ? var10 : 1;

         for (int var16 = 0; var16 < var15; var16++) {
            for (int var17 = 0; var17 < var14; var17++) {
               float var18 = (float)var17 / (float)var14;
               float var19 = (float)(var17 + 1) / (float)var14;
               float var20 = (float)var16 / (float)var15;
               float var21 = (float)(var16 + 1) / (float)var15;
               float[] var22 = new float[]{var18, var19, var19, var18};
               float[] var23 = new float[]{var20, var20, var21, var21};
               Vector3f[] var24 = new Vector3f[4];
               long[] var25 = new long[4];
               float[] var26 = new float[4];
               int[] var27 = new int[4];

               for (int var28 = 0; var28 < 4; var28++) {
                  float var29 = var22[var28];
                  float var30 = var23[var28];
                  var24[var28] = new Vector3f(
                     LAsFloat(var11.x(), var12.x(), var2.position2().x(), var13.x(), var29, var30),
                     LAsFloat(var11.y(), var12.y(), var2.position2().y(), var13.y(), var29, var30),
                     LAsFloat(var11.z(), var12.z(), var2.position2().z(), var13.z(), var29, var30)
                  );
                  var25[var28] = Vector2f.toLong(PAsFloat(var2, var29, var30, true), PAsFloat(var2, var29, var30, false));
                  var26[var28] = LAsFloat(var3[0], var3[1], var3[2], var3[3], var29, var30);
                  var27[var28] = kAsInt(var8, var29, var30);
               }

               BakedQuad var31 = new BakedQuad(
                  var24[0],
                  var24[1],
                  var24[2],
                  var24[3],
                  var25[0],
                  var25[1],
                  var25[2],
                  var25[3],
                  var2.tintIndex(),
                  var2.face(),
                  var2.sprite(),
                  var2.shade(),
                  var2.lightEmission()
               );
               var0.quad(var1, var31, var26, var4, var5, var6, var7, var27, var9);
            }
         }
      } else {
         throw new IllegalArgumentException("Unbounded snow subdivision");
      }
   }

   private static boolean gAsBoolean(Vector3fc var0, Vector3fc var1) {
      return Math.abs(var0.x() - var1.x()) + Math.abs(var0.z() - var1.z()) > 1.0E-5F;
   }

   private static float LAsFloat(float var0, float var1, float var2, float var3, float var4, float var5) {
      return var4 >= var5 ? var0 + (var1 - var0) * (var4 - var5) + (var2 - var0) * var5 : var0 + (var2 - var0) * var4 + (var3 - var0) * (var5 - var4);
   }

   private static float PAsFloat(BakedQuad var0, float var1, float var2, boolean var3) {
      return var3
         ? LAsFloat(
            Vector2f.getX(var0.packedUV0()), Vector2f.getX(var0.packedUV1()), Vector2f.getX(var0.packedUV2()), Vector2f.getX(var0.packedUV3()), var1, var2
         )
         : LAsFloat(
            Vector2f.getY(var0.packedUV0()), Vector2f.getY(var0.packedUV1()), Vector2f.getY(var0.packedUV2()), Vector2f.getY(var0.packedUV3()), var1, var2
         );
   }

   private static int kAsInt(int[] var0, float var1, float var2) {
      int var3 = 0;

      for (int var7 : new int[]{0, 16}) {
         var3 |= Math.clamp(
               (long)Math.round(
                  LAsFloat(
                     (float)(var0[0] >>> var7 & 65535),
                     (float)(var0[1] >>> var7 & 65535),
                     (float)(var0[2] >>> var7 & 65535),
                     (float)(var0[3] >>> var7 & 65535),
                     var1,
                     var2
                  )
               ),
               0,
               240
            )
            << var7;
      }

      return var3;
   }

   private SnowSubdivision() {
   }

   private static record Type_i(VertexConsumer output) implements VertexConsumer {
      public void quad(Entry matrixEntry, BakedQuad quad, float[] brightnesses, float red, float green, float blue, float alpha, int[] lights, int overlay) {
         int var10 = TerrainMaterialClassifier.MAsInt(quad, lights);
         if (var10 == 0) {
            this.output.quad(matrixEntry, quad, brightnesses, red, green, blue, alpha, lights, overlay);
         } else {
            SnowSubdivision.JAsVoid(this.output, matrixEntry, quad, brightnesses, red, green, blue, alpha, lights, overlay, var10);
         }
      }

      public VertexConsumer vertex(float x, float y, float z) {
         this.output.vertex(x, y, z);
         return this;
      }

      public VertexConsumer color(int red, int green, int blue, int alpha) {
         this.output.color(red, green, blue, alpha);
         return this;
      }

      public VertexConsumer color(int argb) {
         this.output.color(argb);
         return this;
      }

      public VertexConsumer texture(float u, float v) {
         this.output.texture(u, v);
         return this;
      }

      public VertexConsumer overlay(int u, int v) {
         this.output.overlay(u, v);
         return this;
      }

      public VertexConsumer light(int u, int v) {
         this.output.light(u, v);
         return this;
      }

      public VertexConsumer normal(float x, float y, float z) {
         this.output.normal(x, y, z);
         return this;
      }

      public VertexConsumer lineWidth(float width) {
         this.output.lineWidth(width);
         return this;
      }
   }
}
