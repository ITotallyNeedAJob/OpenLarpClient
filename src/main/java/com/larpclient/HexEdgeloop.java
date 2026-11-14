package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.Vec3d;

public final class HexEdgeloop {
   public static final List<lc.Type_w> SahurDanceEngine = dAsList();

   private static List<lc.Type_w> dAsList() {
      Vec3d[] var0 = new Vec3d[6];

      for (int var1 = 0; var1 < 6; var1++) {
         double var2 = (double)var1 * Math.PI / 3.0 - (Math.PI / 2);
         var0[var1] = new Vec3d(Math.cos(var2), 0.0, Math.sin(var2));
      }

      ArrayList var4 = new ArrayList();

      for (int var5 = 0; var5 < 2; var5++) {
         for (int var3 = 0; var3 < 3; var3++) {
            var4.add(new lc.Type_w(var0[var5 + var3 * 2], var0[var5 + (var3 * 2 + 2) % 6], 0.5));
         }
      }

      return List.copyOf(var4);
   }

   public static double kAsDouble(double var0, double var2, double var4) {
      return var0 + var2 * var4;
   }

   private HexEdgeloop() {
   }
}
