package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public final class mo {
   private static final int Y = 64;
   private static final int h = 3;
   public static final List<mo.Type_k> AuctionNotifier = yAsList();
   private static final double[] S = new double[65];
   private static final double[] ChinaHatCodec = new double[65];

   private static mo.Type_e bAsMo_Type_e(double var0, int var2, double var3) {
      double var5 = (double)var2 * Math.PI * 2.0 / 64.0;
      double var7 = Math.cos(var5);
      double var9 = Math.sin(var5);
      double var11 = 0.76 + 0.18 * Math.max(0.0, -0.6 * var7 + 0.8 * var9);
      return new mo.Type_e(var7 * var0, var3, var9 * var0, var0, var11);
   }

   private static List<mo.Type_k> yAsList() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 3; var1++) {
         double var2 = (double)var1 / 3.0;
         double var4 = (double)(var1 + 1) / 3.0;

         for (int var6 = 0; var6 < 64; var6++) {
            var0.add(
               new mo.Type_k(
                  bAsMo_Type_e(var2, var6, 1.0 - var2),
                  bAsMo_Type_e(var4, var6, 1.0 - var4),
                  bAsMo_Type_e(var4, var6 + 1, 1.0 - var4),
                  bAsMo_Type_e(var2, var6 + 1, 1.0 - var2),
                  false
               )
            );
         }
      }

      for (int var7 = 0; var7 < 64; var7++) {
         var0.add(
            new mo.Type_k(
               bAsMo_Type_e(0.96, var7, -0.022), bAsMo_Type_e(1.0, var7, 0.0), bAsMo_Type_e(1.0, var7 + 1, 0.0), bAsMo_Type_e(0.96, var7 + 1, -0.022), true
            )
         );
      }

      return List.copyOf(var0);
   }

   public static int wAsInt(int var0, double var1) {
      return var0 & 0xFF000000
         | (int)((double)(var0 >>> 16 & 0xFF) * var1) << 16
         | (int)((double)(var0 >>> 8 & 0xFF) * var1) << 8
         | (int)((double)(var0 & 0xFF) * var1);
   }

   public static void JAsVoid(o_ var0, Matrix4f var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
      var0.blend(x4.Type_g.ALPHA);

      for (mo.Type_k var12 : o) {
         lAsVoid(var0, var1, var12.a, var12.brim, var2, var4, var6, var8, var9);
         lAsVoid(var0, var1, var12.b, var12.brim, var2, var4, var6, var8, var9);
         lAsVoid(var0, var1, var12.c, var12.brim, var2, var4, var6, var8, var9);
         lAsVoid(var0, var1, var12.d, var12.brim, var2, var4, var6, var8, var9);
      }

      if (var10) {
         var0.blend(x4.Type_g.ADDITIVE);
         WAsVoid(var0, var1, var2, var6, 0.014, bg.pAsInt(var9, 0.22));
         WAsVoid(var0, var1, var2, var6, 0.004, bg.xAsInt(var9, var9 & 0xFF000000 | 16777215, 0.52));
         var0.blend(x4.Type_g.ALPHA);
      }
   }

   private static void WAsVoid(o_ var0, Matrix4f var1, double var2, double var4, double var6, int var8) {
      float var9 = (float)(-0.503 - var4);

      for (int var10 = 0; var10 < 64; var10++) {
         var0.modelVertex(var1, (float)(S[var10] * (var2 - var6)), var9, (float)(i[var10] * (var2 - var6)), var8);
         var0.modelVertex(var1, (float)(S[var10] * (var2 + var6)), var9, (float)(i[var10] * (var2 + var6)), var8);
         var0.modelVertex(var1, (float)(S[var10 + 1] * (var2 + var6)), var9, (float)(i[var10 + 1] * (var2 + var6)), var8);
         var0.modelVertex(var1, (float)(S[var10 + 1] * (var2 - var6)), var9, (float)(i[var10 + 1] * (var2 - var6)), var8);
      }
   }

   private static void lAsVoid(o_ var0, Matrix4f var1, mo.Type_e var2, boolean var3, double var4, double var6, double var8, int var10, int var11) {
      int var12 = var3 ? wAsInt(var11, 0.58) : wAsInt(bg.xAsInt(var10, var11, var2.radius), var2.light);
      rAsVoid(var0, var1, var2, var4, var6, var8, var12);
   }

   private static void rAsVoid(o_ var0, Matrix4f var1, mo.Type_e var2, double var3, double var5, double var7, int var9) {
      var0.modelVertex(var1, (float)(var2.x * var3), (float)(-0.5 - var7 - var2.rise * var5), (float)(var2.z * var3), var9);
   }

   private mo() {
   }

   static {
      for (int var0 = 0; var0 <= 64; var0++) {
         S[var0] = Math.cos((double)var0 * Math.PI * 2.0 / 64.0);
         i[var0] = Math.sin((double)var0 * Math.PI * 2.0 / 64.0);
      }
   }

   public static record Type_e(double x, double rise, double z, double radius, double light) {
   }

   public static record Type_k(mo.Type_e a, mo.Type_e b, mo.Type_e c, mo.Type_e d, boolean brim) {
   }
}
