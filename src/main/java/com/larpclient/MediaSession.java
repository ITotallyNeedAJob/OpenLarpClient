package com.larpclient;

public final class MediaSession {
   private static long F;
   private static double u;
   private static double Y;
   private static float a;

   public static void JAsVoid() {
      F++;
   }

   public static void qAsVoid(float var0) {
      PetEffectRenderer = Math.clamp(var0, 0.0F, 1.0F);
      double var1 = (double)((float)F + a) * 0.05;
      Y = Math.clamp(var1 - u, 0.0, 0.1);
      CropHelper = var1;
   }

   public static double RAsDouble() {
      return u;
   }

   public static double YAsDouble() {
      return Y;
   }

   public static float LAsFloat() {
      return a;
   }

   public static double dAsDouble() {
      return (double)F * 0.05;
   }

   private MediaSession() {
   }
}
