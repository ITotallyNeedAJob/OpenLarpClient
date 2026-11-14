package com.larpclient;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class m_ {
   private static final Vector3f AuraModule = new Vector3f(0.0F, -0.375F, 0.21F);

   public static void yAsVoid(MatrixStack var0, int var1, float var2, float var3) {
      float var4 = Float.isFinite(var2) ? Math.clamp(var2, 0.0F, 1.0F) : 0.0F;
      float var5 = Float.isFinite(var3) ? Math.clamp(var3, 0.0F, 1.0F) : 0.0F;
      var0.translate((float)var1 * 0.56F, -0.52F - var5 * 0.6F, -0.72F);
      float var6 = MathHelper.sqrt(var4);
      var0.translate(
         (float)var1 * -0.4F * MathHelper.sin((double)(var6 * (float) Math.PI)),
         0.2F * MathHelper.sin((double)(var6 * (float) (Math.PI * 2))),
         -0.2F * MathHelper.sin((double)(var4 * (float) Math.PI))
      );
      float var7 = MathHelper.sin((double)(var4 * var4 * (float) Math.PI));
      float var8 = MathHelper.sin((double)(var6 * (float) Math.PI));
      var0.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var1 * (45.0F + var7 * -20.0F)));
      var0.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)var1 * var8 * -20.0F));
      var0.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var8 * -80.0F));
      var0.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)(var1 * -45)));
   }

   public static wa BAsWa(int var0, float var1, float var2) {
      MatrixStack var3 = new MatrixStack();
      yAsVoid(var3, var0, var1, var2);
      var3.translate((float)var0 * -0.25F, 0.35F, -0.18F);
      Quaternionf var4 = new Quaternionf().rotationXYZ((float)Math.toRadians(-50.0), 0.0F, (float)Math.toRadians(25.0));
      var3.multiply(new Quaternionf(var4.x, (float)var0 * var4.y, (float)var0 * var4.z, var4.w));
      MatrixStack var5 = new MatrixStack();
      var5.multiplyPositionMatrix(var3.peek().getPositionMatrix());
      var5.scale(0.72F, 0.72F, 0.72F);
      var5.multiply(RotationAxis.POSITIVE_Z.rotation((float)var0 * 0.1F));
      return new wa(new Matrix4f(var5.peek().getPositionMatrix()), new Matrix4f(var3.peek().getPositionMatrix()));
   }

   public static wa lAsWa(int var0, float var1, float var2, float var3) {
      Matrix4f var4 = BAsWa(var0, var1, var2).arm();
      MatrixStack var5 = new MatrixStack();
      yAsVoid(var5, var0, var1, var2);
      var5.translate((float)var0 * -0.25F, 0.35F, -0.18F);
      Quaternionf var6 = new Quaternionf().rotationXYZ((float)Math.toRadians(15.0), (float)Math.toRadians(-20.0), (float)Math.toRadians(15.0));
      var5.multiply(new Quaternionf(var6.x, (float)var0 * var6.y, (float)var0 * var6.z, var6.w));
      var5.scale(0.72F * var3, 0.72F * var3, 0.72F * var3);
      return new wa(new Matrix4f(var5.peek().getPositionMatrix()), new Matrix4f(var4));
   }

   public static float eAsFloat(float var0) {
      float var1 = Float.isFinite(var0) ? Math.clamp(var0, 0.0F, 1.0F) : 0.0F;
      float var2 = cAsFloat(Math.clamp(var1 / 0.22F, 0.0F, 1.0F));
      float var3 = cAsFloat(Math.clamp((1.0F - var1) / 0.22F, 0.0F, 1.0F));
      return var2 * var3;
   }

   public static wa YAsWa(wa var0, int var1, float var2, float var3) {
      float var4 = Float.isFinite(var3) ? Math.clamp(var3, 0.0F, 1.0F) : 0.0F;
      if (var4 <= 0.0F) {
         return var0;
      } else {
         float var5 = Float.isFinite(var2) ? Math.clamp(var2, 0.0F, 1.0F) : 0.0F;
         Matrix4f var6 = var0.weapon();
         Vector3f var7 = var6.transformPosition(new Vector3f(x));
         Matrix3f var8 = HAsMatrix3f(var6.transformDirection(new Vector3f(0.0F, 0.0F, 1.0F)), var6.transformDirection(new Vector3f(0.0F, -1.0F, 0.0F)));
         Vector3f var9 = new Vector3f((float)var1 * 0.09F, -0.18F, -0.5F);
         Vector3f var10 = new Vector3f((float)var1 * 0.72F, -0.5F, 0.48F);
         Matrix3f var11 = HAsMatrix3f(new Vector3f(var9).negate().add(0.0F, 0.1F, 0.0F), var10);
         Quaternionf var12 = new Quaternionf().setFromNormalized(var11.mul(var8.transpose(new Matrix3f()), new Matrix3f()));
         Quaternionf var13 = new Quaternionf().slerp(var12, var4);
         float var14 = Math.clamp((var5 - 0.22F) / 0.56F, 0.0F, 1.0F);
         float var15 = (float)Math.toRadians(11.0) * (float)Math.sin((double)var14 * Math.PI * 2.0);
         Vector3f var16 = var13.transform(var6.transformDirection(new Vector3f(0.0F, -1.0F, 0.0F)).normalize());
         var13.premul(new Quaternionf().rotationAxis((float)var1 * var15 * var4, var16));
         float var17 = 0.012F * (float)Math.sin((double)var14 * Math.PI) * var4;
         Vector3f var18 = new Vector3f(var7).lerp(var9, var4).add(0.0F, var17, 0.0F);
         Matrix4f var19 = new Matrix4f().translation(var18).rotate(var13).translate(-var7.x, -var7.y, -var7.z);
         return new wa(new Matrix4f(var19).mul(var0.weapon()), new Matrix4f(var19).mul(var0.arm()));
      }
   }

   private static Matrix3f HAsMatrix3f(Vector3f var0, Vector3f var1) {
      Vector3f var2 = new Vector3f(var0).normalize();
      Vector3f var3 = new Vector3f(var1).sub(new Vector3f(var2).mul(var2.dot(var1))).normalize();
      return new Matrix3f(var2, var3, new Vector3f(var2).cross(var3));
   }

   private static float cAsFloat(float var0) {
      return var0 * var0 * var0 * (var0 * (var0 * 6.0F - 15.0F) + 10.0F);
   }

   public static void jAsVoid(MatrixStack var0, float var1) {
      var0.translate(0.0F, -0.375F, 0.165F);
      var0.scale(var1, var1, var1);
      var0.translate(0.0F, 0.375F, -0.165F);
   }

   private m_() {
   }
}
