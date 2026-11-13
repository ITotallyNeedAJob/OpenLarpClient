package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public final class xh {
   private static final int t = 32;
   private static final int m = 6;
   private static final int v = 12;
   public static final List<xh.Type_y> U = dAsList();
   public static final int g = 16;

   private static xh.Type_h AAsXh_Type_h(double var0, double var2, double var4) {
      return new xh.Type_h(var0, Math.sqrt(Math.max(0.0, 1.0 - var0 * var0 - var2 * var2)) + var4, var2);
   }

   private static xh.Type_h wAsXh_Type_h(double var0, int var2, double var3) {
      double var5 = (double)var2 * Math.PI * 2.0 / 32.0;
      return AAsXh_Type_h(var0 * Math.cos(var5), var0 * Math.sin(var5), var3);
   }

   private static List<xh.Type_y> dAsList() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 6; var1++) {
         for (int var2 = 0; var2 < 32; var2++) {
            double var3 = (double)var1 / 6.0;
            double var5 = (double)(var1 + 1) / 6.0;
            var0.add(
               new xh.Type_y(
                  wAsXh_Type_h(var3, var2, 0.0),
                  wAsXh_Type_h(var5, var2, 0.0),
                  wAsXh_Type_h(var5, var2 + 1, 0.0),
                  wAsXh_Type_h(var3, var2 + 1, 0.0),
                  false,
                  false
               )
            );
         }
      }

      for (int var34 = 0; var34 < 2; var34++) {
         for (int var36 = 0; var36 < 3; var36++) {
            double var37 = (double)(var34 + var36 * 2) * Math.PI / 3.0 - (Math.PI / 2);
            double var38 = var37 + (Math.PI * 2.0 / 3.0);
            double var7 = 0.57 * Math.cos(var37);
            double var9 = 0.57 * Math.sin(var37);
            double var11 = 0.57 * Math.cos(var38);
            double var13 = 0.57 * Math.sin(var38);
            double var15 = Math.hypot(var11 - var7, var13 - var9);
            double var17 = -(var13 - var9) / var15 * 0.018;
            double var19 = (var11 - var7) / var15 * 0.018;

            for (int var21 = 0; var21 < 12; var21++) {
               double var22 = (double)var21 / 12.0;
               double var24 = (double)(var21 + 1) / 12.0;
               double var26 = var7 + (var11 - var7) * var22;
               double var28 = var9 + (var13 - var9) * var22;
               double var30 = var7 + (var11 - var7) * var24;
               double var32 = var9 + (var13 - var9) * var24;
               var0.add(
                  new xh.Type_y(
                     AAsXh_Type_h(var26 + var17, var28 + var19, 0.025),
                     AAsXh_Type_h(var30 + var17, var32 + var19, 0.025),
                     AAsXh_Type_h(var30 - var17, var32 - var19, 0.025),
                     AAsXh_Type_h(var26 - var17, var28 - var19, 0.025),
                     true,
                     false
                  )
               );
            }
         }
      }

      for (int var35 = 0; var35 < 32; var35++) {
         var0.add(
            new xh.Type_y(
               wAsXh_Type_h(0.982, var35, 0.015),
               wAsXh_Type_h(1.0, var35, 0.015),
               wAsXh_Type_h(1.0, var35 + 1, 0.015),
               wAsXh_Type_h(0.982, var35 + 1, 0.015),
               false,
               true
            )
         );
      }

      return List.copyOf(var0);
   }

   public static void YAsVoid(o_ var0, Matrix4f var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
      for (xh.Type_y var12 : U) {
         if (!var12.hem || var10) {
            int var13 = var12.symbol ? var9 : (var12.hem ? bg.xAsInt(var8, var9, 0.25) : var8);
            vAsVoid(var0, var1, var12.a, var2, var4, var6, var13);
            vAsVoid(var0, var1, var12.b, var2, var4, var6, var13);
            vAsVoid(var0, var1, var12.c, var2, var4, var6, var13);
            vAsVoid(var0, var1, var12.d, var2, var4, var6, var13);
         }
      }
   }

   private static void vAsVoid(o_ var0, Matrix4f var1, xh.Type_h var2, double var3, double var5, double var7, int var9) {
      var0.modelVertex(var1, (float)(var2.x * var3), (float)(-0.5 - var7 - var2.rise * var5), (float)(var2.z * var3 + 0.035), var9);
   }

   private xh() {
   }

   public static record Type_h(double x, double rise, double z) {
   }

   public static record Type_y(xh.Type_h a, xh.Type_h b, xh.Type_h c, xh.Type_h d, boolean symbol, boolean hem) {
   }
}
