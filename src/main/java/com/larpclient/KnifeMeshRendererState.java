package com.larpclient;

import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class KnifeMeshRendererState {
   public static final double P = 0.08;
   private KnifeMesh Y;
   private int F;
   private int W;
   private float Q;
   private double p = Double.NaN;
   private double U = Double.NEGATIVE_INFINITY;
   private Matrix4f[] V;
   private Matrix4f[] j;

   public KnifeMeshRendererState() {
   }

   public void aAsVoid() {
      this.Y = null;
      this.V = this.j = null;
      this.p = Double.NaN;
      this.U = Double.NEGATIVE_INFINITY;
   }

   public Matrix4f[] yAsMatrix4fArray(KnifeMesh var1, KnifeType var2, KnifeState.Type_i var3, int var4, float var5, double var6) {
      int var8 = this.Y == var1
            && this.F == var4
            && this.Q == var5
            && Double.isFinite(var6)
            && Double.isFinite(this.p)
            && !(var6 < this.p)
            && !(var6 - this.p > 0.5)
         ? 0
         : 1;
      if (var8 != 0) {
         this.aAsVoid();
         this.Y = var1;
         this.F = var4;
         this.Q = var5;
         this.V = xAsMatrix4fArray(var1.parts().size() + 2);
         this.j = xAsMatrix4fArray(this.V.length);
      } else if (var3.q || var6 - this.U < 0.08 && var3.J != this.W) {
         for (int var9 = 0; var9 < this.V.length; var9++) {
            this.j[var9].set(this.V[var9]);
         }

         this.U = var6;
      }

      WalkAnimation var15 = WalkAnimation.sample(var1, var2, var3.J, var3.L, var4, var5);
      this.V[0].set(var15.weapon());
      this.V[1].set(var15.arm());
      MatrixStack var10 = new MatrixStack();

      for (int var11 = 0; var11 < var1.parts().size(); var11++) {
         var10.push();
         var1.parts().get(var11).transform(var10, var3.J, var3.L, var4);
         this.V[var11 + 2].set(var10.peek().getPositionMatrix());
         var10.pop();
      }

      float var16 = (float)Math.clamp((var6 - this.U) / 0.08, 0.0, 1.0);
      if (var16 < 1.0F) {
         var16 = 1.0F - (1.0F - var16) * (1.0F - var16);
         Matrix4f var12 = new Matrix4f(this.V[0]).invert().mul(this.V[1]);
         Matrix4f var13 = new Matrix4f(this.j[0]).invert().mul(this.j[1]);
         OAsVoid(var13, var12, var16);
         OAsVoid(this.j[0], this.V[0], var16);
         this.V[1].set(this.V[0]).mul(var12);

         for (int var14 = 2; var14 < this.V.length; var14++) {
            OAsVoid(this.j[var14], this.V[var14], var16);
         }
      }

      this.p = var6;
      this.W = var3.J;
      return this.V;
   }

   private static Matrix4f[] xAsMatrix4fArray(int var0) {
      Matrix4f[] var1 = new Matrix4f[var0];

      for (int var2 = 0; var2 < var0; var2++) {
         var1[var2] = new Matrix4f();
      }

      return var1;
   }

   private static void OAsVoid(Matrix4f var0, Matrix4f var1, float var2) {
      Vector3f var3 = var0.getTranslation(new Vector3f()).lerp(var1.getTranslation(new Vector3f()), var2);
      Quaternionf var4 = var0.getUnnormalizedRotation(new Quaternionf()).normalize().slerp(var1.getUnnormalizedRotation(new Quaternionf()).normalize(), var2);
      Vector3f var5 = var0.getScale(new Vector3f()).lerp(var1.getScale(new Vector3f()), var2);
      var1.translationRotateScale(var3, var4, var5);
   }
}
