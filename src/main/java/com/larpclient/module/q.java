package com.larpclient.module;

import java.util.Arrays;
import net.minecraft.util.math.MathHelper;

public final class q {
   private static final float[] T = new float[20];
   private static int k;
   private static long c = -1L;

   private q() {
   }

   public static void MAsVoid() {
      long var0 = System.nanoTime();
      if (c != -1L) {
         float var2 = (float)(var0 - c) / 1.0E9F;
         T[k % T.length] = MathHelper.clamp(20.0F / var2, 0.0F, 20.0F);
         k++;
      }

      c = var0;
   }

   public static void EAsVoid() {
      Arrays.fill(T, 0.0F);
      k = 0;
      c = -1L;
   }

   public static float HAsFloat() {
      float var0 = 0.0F;
      float var1 = 0.0F;

      for (float var5 : T) {
         if (var5 > 0.0F) {
            var0 += var5;
            var1++;
         }
      }

      return MathHelper.clamp(var0 / var1, 0.0F, 20.0F);
   }

   public static float aAsFloat() {
      float var0 = HAsFloat();
      return Float.isFinite(var0) && var0 > 0.0F ? var0 : 20.0F;
   }
}
