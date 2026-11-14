package com.larpclient;

import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

final class BrushStroke {
   private final EffectGeometryRenderer P;
   private final Vec3d x;
   private final Quaternionf O;
   private final double C;
   private final boolean i;
   private String V = "Matte";
   private Vec3d a = new Vec3d(0.0, 0.3, 1.0).normalize();

   BrushStroke wAsBr(String var1, Vec3d var2) {
      this.V = var1;
      this.a = var2;
      return this;
   }

   BrushStroke(EffectGeometryRenderer var1, Vec3d var2, Quaternionf var3, double var4, boolean var6) {
      this.P = var1;
      this.x = var2;
      this.O = var3;
      this.C = var4;
      this.i = var6;
   }

   Vec3d EAsVec3d(double var1, double var3, double var5) {
      Vector3f var7 = this.O.transform(new Vector3f((float)(var1 * this.C), (float)(var3 * this.C), (float)(var5 * this.C)));
      return this.x.add((double)var7.x, (double)var7.y, (double)var7.z);
   }

   private void SAsVoid(double var1, double var3, double var5, int var7) {
      Vec3d var8 = this.EAsVec3d(var1, var3, var5);
      this.P.vertex(var8.x, var8.y, var8.z, var7);
   }

   void MAsVoid(Vec3d var1, Vec3d var2, Vec3d var3, Vec3d var4, int var5) {
      Vec3d var6 = var2.subtract(var1).crossProduct(var3.subtract(var1)).normalize();
      double var7 = this.i ? 1.0 : 0.68 + 0.32 * Math.max(0.0, var6.dotProduct(new Vec3d(-0.35, 0.8, 0.45).normalize()));
      int var9 = ColorUtil.xAsInt(var5, var5 & 0xFF000000, 1.0 - var7);
      Vector3f var10 = this.O.transform(new Vector3f((float)var6.x, (float)var6.y, (float)var6.z));
      Vec3d var11 = new Vec3d((double)var10.x, (double)var10.y, (double)var10.z);
      double var12 = Math.abs(var11.dotProduct(this.a));
      if (!this.i) {
         String var14 = this.V;
         switch (var14) {
            case "Glass":
               var9 = ColorUtil.pAsInt(ColorUtil.xAsInt(var9, -1, 0.18 * Math.pow(1.0 - var12, 3.0)), 0.32 + 0.68 * Math.pow(1.0 - var12, 2.0));
               break;
            case "Crystal":
               var9 = ColorUtil.xAsInt(var9, ColorUtil.XAsInt(-1, (double)(var9 >>> 24) / 255.0), 0.35 * Math.pow(1.0 - var12, 4.0));
               break;
            case "Gold":
               var9 = ColorUtil.xAsInt(
                  var9,
                  ColorUtil.XAsInt(-4683, (double)(var9 >>> 24) / 255.0),
                  0.65 * Math.pow(Math.max(0.0, var11.dotProduct(new Vec3d(-0.35, 0.8, 0.45).normalize())), 16.0)
               );
               break;
            case "Obsidian":
               var9 = ColorUtil.xAsInt(var9, ColorUtil.XAsInt(-7301451, (double)(var9 >>> 24) / 255.0), 0.3 * Math.pow(var12, 28.0));
               break;
            case "Silk":
               var9 = ColorUtil.xAsInt(var9, ColorUtil.XAsInt(-5893, (double)(var9 >>> 24) / 255.0), 0.3 * Math.pow(1.0 - var12, 2.0));
               break;
            case "Energy":
               var9 = ColorUtil.xAsInt(var5, ColorUtil.XAsInt(-1, (double)(var5 >>> 24) / 255.0), 0.16 + 0.2 * Math.pow(1.0 - var12, 2.0));
         }
      }

      this.SAsVoid(var1.x, var1.y, var1.z, var9);
      this.SAsVoid(var2.x, var2.y, var2.z, var9);
      this.SAsVoid(var3.x, var3.y, var3.z, var9);
      this.SAsVoid(var4.x, var4.y, var4.z, var9);
   }

   void iAsVoid(Vec3d var1, Vec3d var2, Vec3d var3, int var4) {
      this.MAsVoid(var1, var2, var3, var3, var4);
   }

   void LAsVoid(double var1, double var3, double var5, double var7, double var9, double var11, int var13) {
      for (int var16 = 0; var16 < 10; var16++) {
         for (int var17 = 0; var17 < 20; var17++) {
            double var18 = Math.PI * (double)var16 / 10.0;
            double var20 = Math.PI * (double)(var16 + 1) / 10.0;
            double var22 = (Math.PI * 2) * (double)var17 / 20.0;
            double var24 = (Math.PI * 2) * (double)(var17 + 1) / 20.0;
            this.MAsVoid(
               rAsVec3d(var1, var3, var5, var7, var9, var11, var18, var22),
               rAsVec3d(var1, var3, var5, var7, var9, var11, var20, var22),
               rAsVec3d(var1, var3, var5, var7, var9, var11, var20, var24),
               rAsVec3d(var1, var3, var5, var7, var9, var11, var18, var24),
               var13
            );
         }
      }
   }

   private static Vec3d rAsVec3d(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
      return new Vec3d(var0 + var6 * Math.sin(var12) * Math.cos(var14), var2 + var8 * Math.cos(var12), var4 + var10 * Math.sin(var12) * Math.sin(var14));
   }

   void SAsVoid(double var1, double var3, double var5, double var7, double var9, double var11, int var13) {
      Vec3d[] var14 = new Vec3d[8];

      for (int var15 = 0; var15 < 8; var15++) {
         var14[var15] = new Vec3d(
            var1 + ((var15 & 1) == 0 ? -var7 : var7), var3 + ((var15 & 2) == 0 ? -var9 : var9), var5 + ((var15 & 4) == 0 ? -var11 : var11)
         );
      }

      int[][] var20 = new int[][]{{0, 2, 3, 1}, {4, 5, 7, 6}, {0, 4, 6, 2}, {1, 3, 7, 5}, {0, 1, 5, 4}, {2, 6, 7, 3}};

      for (int[] var19 : var20) {
         this.MAsVoid(var14[var19[0]], var14[var19[1]], var14[var19[2]], var14[var19[3]], var13);
      }
   }

   void rAsVoid(double var1, double var3, double var5, double var7, int var9) {
      for (int var10 = 0; var10 < 8; var10++) {
         double var11 = var7 + (double)var10 * Math.PI / 4.0;
         double var13 = var7 + (double)(var10 + 1) * Math.PI / 4.0;
         Vec3d var15 = new Vec3d(Math.cos(var11) * var1, 0.0, Math.sin(var11) * var1);
         Vec3d var16 = new Vec3d(Math.cos(var13) * var1, 0.0, Math.sin(var13) * var1);
         this.iAsVoid(new Vec3d(0.0, var3, 0.0), var16, var15, var9);
         this.iAsVoid(new Vec3d(0.0, -var5, 0.0), var15, var16, var9);
      }
   }

   void KAsVoid(double var1, double var3, Quaternionf var5, int var6) {
      for (int var7 = 0; var7 < 48; var7++) {
         double var8 = (Math.PI * 2) * (double)var7 / 48.0;
         double var10 = (Math.PI * 2) * (double)(var7 + 1) / 48.0;
         this.MAsVoid(
            JAsVec3d(var1 - var3, var8, var5), JAsVec3d(var1 + var3, var8, var5), JAsVec3d(var1 + var3, var10, var5), JAsVec3d(var1 - var3, var10, var5), var6
         );
      }
   }

   private static Vec3d JAsVec3d(double var0, double var2, Quaternionf var4) {
      Vector3f var5 = var4.transform(new Vector3f((float)(Math.cos(var2) * var0), 0.0F, (float)(Math.sin(var2) * var0)));
      return new Vec3d((double)var5.x, (double)var5.y, (double)var5.z);
   }

   void CAsVoid(Vec3d var1, Vec3d var2, double var3, double var5, int var7) {
      Vec3d var8 = var2.subtract(var1).normalize();
      Vec3d var9 = var8.crossProduct(Math.abs(var8.y) > 0.9 ? new Vec3d(1.0, 0.0, 0.0) : new Vec3d(0.0, 1.0, 0.0)).normalize();
      Vec3d var10 = var8.crossProduct(var9).normalize();

      for (int var11 = 0; var11 < 8; var11++) {
         double var12 = (double)var11 * Math.PI / 4.0;
         double var14 = (double)(var11 + 1) * Math.PI / 4.0;
         Vec3d var16 = var9.multiply(Math.cos(var12)).add(var10.multiply(Math.sin(var12)));
         Vec3d var17 = var9.multiply(Math.cos(var14)).add(var10.multiply(Math.sin(var14)));
         this.MAsVoid(var1.add(var16.multiply(var3)), var2.add(var16.multiply(var5)), var2.add(var17.multiply(var5)), var1.add(var17.multiply(var3)), var7);
      }
   }
}
