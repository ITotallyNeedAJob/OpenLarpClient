package com.larpclient;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class bu {
   public static final float U = 0.72F;
   public static final float N = (float)Math.toRadians(70.0);
   public static final float C = 0.8F;

   public static float mAsFloat(KnifeType var0) {
      return switch (var0) {
         case BAYONET, STILETTO, BUTTERFLY -> 7.5F;
         default -> 8.0F;
      };
   }

   public static float SAsFloat(KnifeType var0) {
      return 0.0F;
   }

   public static Quaternionf LAsQuaternionf(KnifeMesh var0) {
      float[] var1 = var0.root().idle()[0];
      float[] var2 = var0.arm().idle()[0];
      Quaternionf var3 = new Quaternionf(var2[3], var2[4], var2[5], var2[6]);
      Quaternionf var4 = new Quaternionf().rotateZ(0.1F);
      Vector3f var5 = new Quaternionf(var1[3], var1[4], var1[5], var1[6]).transform(new Vector3f(0.0F, 1.0F, 0.0F));
      Vector3f var6 = var3.transform(var4.transform(new Vector3f(0.0F, 1.0F, 0.0F)));
      Vector3f var7 = var3.transform(var4.transform(new Vector3f(0.0F, 0.0F, -1.0F)));
      Vector3f var8 = new Vector3f(var6).cross(var5);
      Vector3f var9 = new Vector3f(var6);
      if (var8.lengthSquared() > 1.0E-8F) {
         new Quaternionf().fromAxisAngleRad(var8.normalize(), (float)Math.acos((double)Math.clamp(var6.dot(var5), -1.0F, 1.0F)) - N).transform(var9);
      }

      Vector3f var10 = sAsVector3f(var5, new Vector3f(var9).lerp(sAsVector3f(var5, new Vector3f(0.0F, 1.0F, 0.0F)), 0.8F));
      Quaternionf var11 = new Quaternionf().rotationTo(var6, var10);
      var11.transform(var6);
      var11.transform(var7);
      Vector3f var12 = new Vector3f(var5).fma(-var5.dot(var6), var6).normalize();
      Quaternionf var13 = new Quaternionf()
         .fromAxisAngleRad(var6, (float)Math.atan2((double)new Vector3f(var7).cross(var12).dot(var6), (double)var7.dot(var12)));
      return new Quaternionf(var3).conjugate().mul(var13).mul(var11).mul(var3).normalize();
   }

   private static Vector3f sAsVector3f(Vector3f var0, Vector3f var1) {
      Vector3f var2 = new Vector3f(var1).fma(-var1.dot(var0), var0).normalize();
      return var2.mul((float)Math.sin((double)N)).fma((float)Math.cos((double)N), var0);
   }

   public static float rAsFloat(KnifeType var0) {
      return switch (var0) {
         case BAYONET -> -0.03F;
         case STILETTO -> -0.07F;
         case BUTTERFLY -> -0.09F;
         default -> 0.0F;
      };
   }

   public static Vector3f EAsVector3f(KnifeMesh var0, KnifeType var1, Quaternionf var2, float var3) {
      float[] var4 = var0.root().idle()[0];
      float[] var5 = var0.arm().idle()[0];
      Vector3f var6 = new Quaternionf(var4[3], var4[4], var4[5], var4[6]).transform(new Vector3f(0.0F, rAsFloat(var1) * var0.scale() * var3, 0.0F));
      return new Quaternionf(var5[3], var5[4], var5[5], var5[6]).mul(var2).conjugate().transform(var6);
   }

   public static void JAsVoid(MatrixStack var0, KnifeType var1, int var2, boolean var3) {
      double var4 = var3 ? -0.5 : -1.0;
      double var6 = (double)mAsFloat(var1);
      var0.translate(
         (double)var2 * (5.0 - var4 * Math.cos(0.1) + var6 * Math.sin(0.1)) / 16.0,
         -(2.0 + var4 * Math.sin(0.1) + var6 * Math.cos(0.1)) / 16.0,
         (double)(-SAsFloat(var1) / 16.0F)
      );
   }

   public static Matrix4f TAsMatrix4f(Matrix4f var0, KnifeType var1, int var2, boolean var3) {
      MatrixStack var4 = new MatrixStack();
      var4.multiplyPositionMatrix(var0);
      var4.scale(0.72F, 0.72F, 0.72F);
      JAsVoid(var4, var1, var2, var3);
      var4.translate((float)(var2 * -5) / 16.0F, 0.125F, 0.0F);
      var4.multiply(RotationAxis.POSITIVE_Z.rotation((float)var2 * 0.1F));
      return new Matrix4f(var4.peek().getPositionMatrix());
   }

   private bu() {
   }
}
