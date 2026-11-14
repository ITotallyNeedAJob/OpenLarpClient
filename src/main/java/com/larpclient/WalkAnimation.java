package com.larpclient;

import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record WalkAnimation(Matrix4f weapon, Matrix4f arm) {
   public static WalkAnimation sample(KnifeMesh var0, KnifeType var1, int var2, float var3, int var4, float var5) {
      MatrixStack var6 = new MatrixStack();
      var0.root().transform(var6, var2, var3, var4);
      float var7 = var0.scale() * var5;
      var6.translate((float)var4 * var1.Q * var0.scale(), var1.a * var0.scale(), var1.d * var0.scale());
      var6.scale(var7, var7, var7);
      var6.translate((float)(-var4) * var1.Q, -var1.a, -var1.d);
      MatrixStack var8 = new MatrixStack();
      var0.arm().transform(var8, var2, var3, var4);
      if (var0.armFit() != null) {
         var0.armFit().translate(var8, var2, var3, var4);
      }

      Quaternionf var9 = BuildHelper.LAsQuaternionf(var0);
      Vector3f var10 = BuildHelper.EAsVector3f(var0, var1, var9, var5);
      var8.multiply(new Quaternionf(var9.x, (float)var4 * var9.y, (float)var4 * var9.z, var9.w));
      var8.translate((float)var4 * var10.x, var10.y, var10.z);
      return new WalkAnimation(new Matrix4f(var6.peek().getPositionMatrix()), new Matrix4f(var8.peek().getPositionMatrix()));
   }
}
