package com.larpclient;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public final class RefractionGeometry {
   private RefractionGeometry() {
   }

   public static void iAsVoid(BipedEntityModel<?> var0, float[] var1, float var2) {
      QAsVoid(var0.head, var1, EmotecraftJsonReader.Type_r.HEAD, var2);
      QAsVoid(var0.body, var1, EmotecraftJsonReader.Type_r.TORSO, var2);
      QAsVoid(var0.rightArm, var1, EmotecraftJsonReader.Type_r.RIGHT_ARM, var2);
      QAsVoid(var0.leftArm, var1, EmotecraftJsonReader.Type_r.LEFT_ARM, var2);
      QAsVoid(var0.rightLeg, var1, EmotecraftJsonReader.Type_r.RIGHT_LEG, var2);
      QAsVoid(var0.leftLeg, var1, EmotecraftJsonReader.Type_r.LEFT_LEG, var2);
   }

   public static float[] AAsFloatArray(float[] var0, float var1) {
      EmotecraftJsonReader.Type_r[] var2 = new EmotecraftJsonReader.Type_r[]{
         EmotecraftJsonReader.Type_r.RIGHT_ARM,
         EmotecraftJsonReader.Type_r.LEFT_ARM,
         EmotecraftJsonReader.Type_r.RIGHT_LEG,
         EmotecraftJsonReader.Type_r.LEFT_LEG
      };
      float[] var3 = null;

      for (int var4 = 0; var4 < var2.length; var4++) {
         float var5 = var0[var2[var4].ordinal() * 10 + 6];
         if (!Float.isNaN(var5)) {
            if (var3 == null) {
               var3 = new float[4];
            }

            var3[var4] = var5 * var1;
         }
      }

      return var3;
   }

   public static void IAsVoid(MatrixStack var0, float[] var1, float var2) {
      int var3 = EmotecraftJsonReader.Type_r.BODY.ordinal() * 10;
      var0.translate(pAsFloat(var1[var3], var2), pAsFloat(var1[var3 + 1], var2) + 0.7F, pAsFloat(var1[var3 + 2], var2));
      var0.multiply(RotationAxis.POSITIVE_Z.rotation(pAsFloat(var1[var3 + 5], var2)));
      var0.multiply(RotationAxis.POSITIVE_Y.rotation(pAsFloat(var1[var3 + 4], var2)));
      var0.multiply(RotationAxis.POSITIVE_X.rotation(pAsFloat(var1[var3 + 3], var2)));
      var0.translate(0.0F, -0.7F, 0.0F);
   }

   private static void QAsVoid(ModelPart var0, float[] var1, EmotecraftJsonReader.Type_r var2, float var3) {
      int var4 = var2.ordinal() * 10;
      var0.originX = LAsFloat(var0.originX, var1[var4], var3);
      var0.originY = LAsFloat(var0.originY, var1[var4 + 1], var3);
      var0.originZ = LAsFloat(var0.originZ, var1[var4 + 2], var3);
      var0.pitch = LAsFloat(var0.pitch, var1[var4 + 3], var3);
      var0.yaw = LAsFloat(var0.yaw, var1[var4 + 4], var3);
      var0.roll = LAsFloat(var0.roll, var1[var4 + 5], var3);
   }

   static float LAsFloat(float var0, float var1, float var2) {
      return Float.isNaN(var1) ? var0 : var0 + (var1 - var0) * var2;
   }

   private static float pAsFloat(float var0, float var1) {
      return Float.isNaN(var0) ? 0.0F : var0 * var1;
   }
}
