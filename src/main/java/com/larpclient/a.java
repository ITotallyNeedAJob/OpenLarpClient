package com.larpclient;

import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

final class a {
   static void jAsVoid(EffectGeometryRenderer var0, Pets.Type_w var1, boolean var2) {
      if (var1.style().equals("Tung Tung Tung Sahur")) {
         if (!var2) {
            SahurPetRenderer.AAsVoid(var0, var1.center(), var1.rotation(), (float)var1.scale());
         }
      } else {
         int var3 = var2 ? bg.pAsInt(var1.glow(), var1.glowStrength() * 0.6) : var1.body();
         int var4 = bg.pAsInt(var1.glow(), var2 ? var1.glowStrength() : 1.0);
         br var5 = new br(var0, var1.center(), var1.rotation(), var1.scale(), var2).wAsBr(var1.realism() ? "Matte" : var1.finish(), var1.view());
         String var6 = var1.style();
         switch (var6) {
            case "Jellyfish":
               JAsVoid(var5, var1, var3, var4, var2);
               break;
            case "Rune Orb":
               AAsVoid(var5, var1, var3, var4, var2);
               break;
            case "Ghost":
               bAsVoid(var5, var1, var3, var4, var2);
               break;
            case "Bee":
               ZAsVoid(var5, var1, var3, var2);
               break;
            default:
               mAsVoid(var0, var5, var1, var3, var4, var2);
         }
      }
   }

   private static void LAsVoid(br var0, double var1, double var3, double var5, double var7, int var9, boolean var10) {
      for (int var11 = -1; var11 <= 1; var11 += 2) {
         var0.LAsVoid((double)var11 * var1, var3, var5, var7, var10 ? 0.003 : var7 * 0.8, var7 * 0.5, -658456);
         var0.LAsVoid((double)var11 * var1, var3 - 0.001, var5 + var7 * 0.43, var7 * 0.42, var10 ? 0.002 : var7 * 0.55, var7 * 0.18, -15263967);
         if (!var10) {
            var0.LAsVoid((double)var11 * var1 - var7 * 0.12, var3 + var7 * 0.2, var5 + var7 * 0.62, var7 * 0.12, var7 * 0.12, var7 * 0.08, var9);
         }
      }
   }

   private static void JAsVoid(br var0, Pets.Type_w var1, int var2, int var3, boolean var4) {
      double var5 = Math.pow(Math.max(0.0, Math.sin(var1.time() * 2.4)), 3.0);
      double var7 = 0.2 + var5 * 0.03;
      double var9 = 0.15 - var5 * 0.03;
      var0.LAsVoid(0.0, -0.02, 0.0, var7 * 0.45, var9 * 0.5, var7 * 0.45, var3);
      if (!var4) {
         if (var1.finish().equals("Crystal") && !var1.realism()) {
            var0.rAsVoid(var7, var9, var9 * 0.5, var1.time() * 0.3, bg.pAsInt(var2, 0.65));
         } else {
            var0.LAsVoid(0.0, 0.0, 0.0, var7, var9, var7, bg.pAsInt(bg.xAsInt(var2, -1, 0.35), 0.47));
         }

         var0.KAsVoid(var7 * 0.9, 0.018, new Quaternionf(), bg.xAsInt(var2, -1, 0.5));
         LAsVoid(var0, 0.07, 0.01, var7 * 0.92, 0.02, var3, Math.sin(var1.time() * 0.8) > 0.995);
      }

      for (int var11 = 0; var11 < 8; var11++) {
         double var12 = var1.time() * 0.3 + (double)var11 * Math.PI / 4.0;
         Vec3d var14 = null;

         for (int var15 = 0; var15 < 9; var15++) {
            double var16 = (double)var15 / 8.0;
            double var18 = var7 * 0.75 * (1.0 - var16 * 0.35);
            Vec3d var20 = new Vec3d(
               Math.cos(var12) * var18 + Math.sin(var1.time() * 2.2 - var16 * 4.0 + (double)var11) * 0.06 * var16,
               -var9 * 0.5 - var16 * (0.38 + 0.08 * Math.sin((double)var11)),
               Math.sin(var12) * var18 + Math.cos(var1.time() * 1.9 - var16 * 3.5 + (double)var11) * 0.05 * var16
            );
            if (var14 != null) {
               var0.CAsVoid(var14, var20, 0.012 * (1.0 - (var16 - 0.125) * 0.7), 0.012 * (1.0 - var16 * 0.7), bg.pAsInt(var3, 0.9 * (1.0 - var16 * 0.6)));
            }

            var14 = var20;
         }
      }

      if (var1.realism() && !var4) {
         for (int var21 = 0; var21 < 4; var21++) {
            double var22 = var1.time() * 0.3 + (double)var21 * Math.PI / 2.0;
            var0.LAsVoid(
               Math.cos(var22) * var7 * 0.32, var9 * 0.05, Math.sin(var22) * var7 * 0.32, var7 * 0.16, var9 * 0.18, var7 * 0.09, bg.xAsInt(var2, -1, 0.5)
            );
         }
      }
   }

   private static void AAsVoid(br var0, Pets.Type_w var1, int var2, int var3, boolean var4) {
      double var5 = Math.pow(Math.max(0.0, Math.sin(var1.time() * 2.6)), 6.0);
      double var7 = 1.0 + var5 * 0.12;
      var0.rAsVoid(0.13 * var7, 0.042 * var7, 0.042 * var7, var1.time() * 0.9, bg.xAsInt(var3, -1, 0.3 + 0.3 * var5));
      if (!var4) {
         if (var1.finish().equals("Crystal") && !var1.realism()) {
            var0.rAsVoid(0.125, 0.13, 0.13, var1.time() * 0.9, bg.pAsInt(var2, 0.65));
         } else if (var1.finish().equals("Obsidian") && !var1.realism()) {
            var0.SAsVoid(0.0, 0.0, 0.0, 0.11, 0.11, 0.11, bg.pAsInt(var2, 0.9));
         } else {
            var0.LAsVoid(0.0, 0.0, 0.0, 0.125, 0.125, 0.125, bg.pAsInt(var2, 0.5));
         }

         LAsVoid(var0, 0.042, 0.03, 0.112, 0.017, var3, Math.sin(var1.time() * 0.8) > 0.995);
      }

      for (int var9 = 0; var9 < 2; var9++) {
         Quaternionf var10 = new Quaternionf()
            .rotationY((float)(var1.time() * (0.5 + (double)var9 * 0.3) + (double)var9 * 1.7))
            .rotateX((float)(0.5 + (double)var9 * 1.1 + Math.sin(var1.time() * 0.4 + (double)var9) * 0.25));
         var0.KAsVoid(0.165 + (double)var9 * 0.03, 0.006, var10, var3);
      }
   }

   private static void bAsVoid(br var0, Pets.Type_w var1, int var2, int var3, boolean var4) {
      double var7 = Math.min(0.12, var1.speed() * 0.03);
      int var9 = var4 ? bg.pAsInt(var3, 0.32) : bg.pAsInt(bg.xAsInt(var2, -1, 0.35), 0.88);

      for (int var10 = 0; var10 < 18; var10++) {
         for (int var11 = 0; var11 < 32; var11++) {
            double var12 = (double)var11 / 32.0;
            double var14 = (double)var10 / 18.0;
            var0.MAsVoid(
               JAsVec3d(var12, var14, var1.time(), 0.135, var7),
               JAsVec3d(var12, (double)(var10 + 1) / 18.0, var1.time(), 0.135, var7),
               JAsVec3d((double)(var11 + 1) / 32.0, (double)(var10 + 1) / 18.0, var1.time(), 0.135, var7),
               JAsVec3d((double)(var11 + 1) / 32.0, var14, var1.time(), 0.135, var7),
               var9
            );
         }
      }

      if (!var4) {
         int var16 = Math.sin(var1.time() * 0.8) > 0.995 ? 1 : 0;
         LAsVoid(var0, 0.045, 0.07, 0.1296, 0.018, -1, var16 != 0);
         var0.LAsVoid(0.0, 0.015, 0.1323, 0.014, var1.reaction() > 0.0 ? 0.022 : 0.01, 0.008, -14408136);

         for (int var17 = -1; var17 <= 1; var17 += 2) {
            double var18 = Math.sin(var1.time() * 3.2 + (double)var17) * 0.25 + (var1.reaction() > 0.0 ? Math.sin(var1.time() * 14.0) * 0.4 : 0.0);
            Vec3d var19 = new Vec3d((double)var17 * 0.135 * 1.05, -0.02, 0.01);
            Vec3d var15 = var19.add((double)var17 * Math.cos(0.9 + var18) * 0.065, -Math.sin(0.9 + var18) * 0.065, 0.0);
            var0.CAsVoid(var19, var15, 0.028, 0.025, var9);
         }
      }
   }

   static Vec3d JAsVec3d(double var0, double var2, double var4, double var6, double var8) {
      double var10 = var0 * Math.PI * 2.0;
      double var12 = var2 < 0.42 ? var6 * Math.sqrt(Math.max(0.0, 1.0 - Math.pow(1.0 - var2 / 0.42, 2.0))) : var6 * (1.0 + (var2 - 0.42) * 0.45);
      double var14 = Math.max(0.0, (var2 - 0.55) / 0.45);
      double var16 = Math.sin(var4 * 2.0 + var2 * 2.5) * 0.02 * var14;
      return new Vec3d(
         Math.cos(var10) * var12 + var16,
         0.171 - var2 * 0.38 + Math.sin(var10 * 6.0 + var4 * 5.0) * 0.022 * var14 * var14 * 2.5,
         Math.sin(var10) * var12 - var8 * var14 * var14
      );
   }

   private static void mAsVoid(EffectGeometryRenderer var0, br var1, Pets.Type_w var2, int var3, int var4, boolean var5) {
      int var6 = bg.xAsInt(var3, -9830, 0.5);
      int var7 = bg.xAsInt(var3, -728387, 0.65);
      var1.LAsVoid(0.0, -var2.wings() * 0.015, 0.0, 0.085, 0.085, 0.13, var3);
      var1.LAsVoid(0.0, -0.01, -0.1, 0.07, 0.065, 0.08, var3);
      if (!var5) {
         for (int var8 = 0; var8 < 5; var8++) {
            var1.LAsVoid(0.0, -0.06 + (double)var8 * 0.004, 0.08 - (double)var8 * 0.042, 0.05, 0.022, 0.028, var6);
         }
      }

      var1.CAsVoid(new Vec3d(0.0, 0.04, 0.1), new Vec3d(0.0, 0.15, 0.17), 0.05, 0.043, var3);
      Vec3d var25 = var1.EAsVec3d(0.0, 0.15, 0.17);
      br var9 = new br(var0, var25, new Quaternionf(var2.rotation()).rotateY((float)(-var2.gazeYaw())).rotateX((float)var2.gazePitch()), var2.scale(), var5)
         .wAsBr(var2.realism() ? "Matte" : var2.finish(), var2.view());
      var9.LAsVoid(0.0, 0.0, 0.0, 0.075, 0.068, 0.08, var3);
      var9.LAsVoid(0.0, -0.026, 0.06, 0.055, 0.032, 0.085, bg.xAsInt(var3, var6, 0.25));
      if (!var5) {
         LAsVoid(var9, 0.042, 0.018, 0.078, 0.018, var4, Math.sin(var2.time() * 0.8) > 0.995);

         for (int var10 = -1; var10 <= 1; var10 += 2) {
            var9.CAsVoid(new Vec3d((double)var10 * 0.035, 0.045, -0.01), new Vec3d((double)var10 * 0.065, 0.065, -0.11), 0.017, 0.002, var7);
            var9.LAsVoid((double)var10 * 0.018, -0.003, 0.135, 0.006, 0.004, 0.005, -14540244);
            var9.iAsVoid(
               new Vec3d((double)var10 * 0.035, 0.0, -0.005),
               new Vec3d((double)var10 * 0.11, 0.022, -0.07),
               new Vec3d((double)var10 * 0.065, -0.04, -0.04),
               bg.pAsInt(var4, 0.8)
            );
         }

         for (int var26 = -1; var26 <= 1; var26 += 2) {
            var1.LAsVoid((double)var26 * 0.06, -0.06, 0.07, 0.02, 0.032, 0.022, var6);
            var1.LAsVoid((double)var26 * 0.055, -0.07, -0.08, 0.028, 0.036, 0.035, var6);

            for (int var11 = -1; var11 <= 1; var11++) {
               var1.CAsVoid(
                  new Vec3d((double)var26 * 0.055 + (double)var11 * 0.011, -0.1, -0.058),
                  new Vec3d((double)var26 * 0.055 + (double)var11 * 0.011, -0.106, -0.025),
                  0.006,
                  0.001,
                  var7
               );
            }
         }
      }

      for (int var27 = -1; var27 <= 1; var27 += 2) {
         double var29 = 0.35 + var2.wings() * 0.7;
         Vec3d var13 = new Vec3d((double)var27 * 0.06, 0.06, 0.05);
         Vec3d var14 = var13.add((double)var27 * 0.13 * Math.cos(var29), 0.13 * Math.sin(var29), -0.03);
         Vec3d var15 = var13.add((double)var27 * 0.24 * Math.cos(var29 * 0.8), 0.24 * Math.sin(var29 * 0.8) + 0.02, -0.02);
         Vec3d var16 = new Vec3d((double)var27 * 0.07, 0.0, -0.08);
         var1.CAsVoid(var13, var14, 0.016, 0.012, var6);
         var1.CAsVoid(var14, var15, 0.012, 0.01, var6);
         Vec3d[] var17 = new Vec3d[3];
         double[] var18 = new double[]{0.35, -0.15, -0.75};
         double[] var19 = new double[]{0.2, 0.24, 0.2};

         for (int var20 = 0; var20 < 3; var20++) {
            double var21 = var18[var20] + var2.wings() * 0.25;
            double var23 = var19[var20];
            var17[var20] = var15.add(
               (double)var27 * var23 * Math.cos(var21) * Math.cos(var29 * 0.5),
               var23 * Math.sin(var29 * 0.5) * 0.6 - 0.08 * (double)var20,
               var23 * Math.sin(var21) * 0.9
            );
            var1.CAsVoid(var15, var17[var20], 0.008, 0.003, var6);
         }

         int var38 = bg.pAsInt(bg.xAsInt(var3, var4, 0.3), var5 ? 0.25 : 0.78);
         var1.iAsVoid(var13, var15, var17[0], var38);
         var1.iAsVoid(var15, var17[0], var17[1], var38);
         var1.iAsVoid(var15, var17[1], var17[2], var38);
         var1.iAsVoid(var15, var17[2], var16, var38);
         var1.iAsVoid(var13, var15, var16, var38);
      }

      Vec3d var28 = new Vec3d(0.0, -0.01, -0.1);

      for (int var30 = 1; var30 <= 12; var30++) {
         double var12 = (double)var30 / 12.0;
         Vec3d var37 = new Vec3d(Math.sin(var2.time() * 2.4 - var12 * 3.0) * 0.08 * var12, -0.01 - var12 * 0.07 + var12 * var12 * 0.1, -0.15 - var12 * 0.32);
         var1.CAsVoid(var28, var37, 0.045 * (1.0 - (var12 - 0.08333333333333333) * 0.82), 0.045 * (1.0 - var12 * 0.82), var3);
         var28 = var37;
      }

      if (!var5) {
         var1.LAsVoid(var28.x, var28.y, var28.z, 0.04, 0.02, 0.07, var4);

         for (int var31 = 0; var31 < 6; var31++) {
            double var34 = (double)var31 / 5.0;
            var1.iAsVoid(
               new Vec3d(-0.012, 0.08 - var34 * 0.03, 0.08 - var34 * 0.2),
               new Vec3d(0.0, 0.13 - var34 * 0.04, 0.04 - var34 * 0.2),
               new Vec3d(0.012, 0.08 - var34 * 0.03, 0.08 - var34 * 0.2),
               var7
            );
         }

         if (var2.realism() || var2.finish().equals("Scales")) {
            for (int var32 = 0; var32 < 5; var32++) {
               for (int var35 = -1; var35 <= 1; var35 += 2) {
                  double var36 = 0.075 - (double)var32 * 0.04;
                  var1.LAsVoid((double)var35 * 0.07, 0.035, var36, 0.013, 0.008, 0.014, bg.xAsInt(var3, -1, 0.15));
               }
            }
         }
      }

      double var33 = var2.time() % 4.0 / 4.0;
      if (var33 < 0.2 && !var5) {
         var9.LAsVoid(0.0, -0.02, 0.15 + var33 * 0.6, 0.025 + var33 * 0.1, 0.025 + var33 * 0.1, 0.05 + var33 * 0.2, bg.XAsInt(var4, 1.0 - var33 * 5.0));
      }
   }

   private static void ZAsVoid(br var0, Pets.Type_w var1, int var2, boolean var3) {
      var0.LAsVoid(0.0, 0.0, 0.0, 0.1, 0.08, 0.15, var2);
      if (!var3) {
         for (int var4 = 0; var4 < 3; var4++) {
            var0.LAsVoid(0.0, 0.0, -0.075 + (double)var4 * 0.065, 0.101, 0.081, 0.022, -13358815);
         }

         var0.LAsVoid(0.0, 0.015, 0.12, 0.07, 0.065, 0.065, -1721248);
         LAsVoid(var0, 0.032, 0.03, 0.17, 0.015, -1, false);
         double var7 = Math.sin(var1.time() * 35.0) * 0.05;

         for (int var6 = -1; var6 <= 1; var6 += 2) {
            var0.iAsVoid(
               new Vec3d((double)var6 * 0.025, 0.065, 0.0),
               new Vec3d((double)var6 * 0.19, 0.09 + var7, -0.045),
               new Vec3d((double)var6 * 0.1, 0.095 + var7, 0.065),
               -1428296209
            );
            var0.CAsVoid(new Vec3d((double)var6 * 0.035, 0.07, 0.12), new Vec3d((double)var6 * 0.055, 0.12, 0.13), 0.004, 0.003, -13424351);
         }
      }
   }

   private a() {
   }
}
