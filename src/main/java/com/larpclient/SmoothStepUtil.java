package com.larpclient;

public final class SmoothStepUtil {
   public static double OAsDouble(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   public static void rAsVoid(dv var0, d1 var1, double var2) {
      var0.D += var2;
      var0.l += var2;
      switch (var0.i) {
         case SPAWNING:
            var0.m = OAsDouble(var0.l / var1.l);
            if (var0.l >= var1.l) {
               var0.i = dv.Type_l.ACTIVE;
               var0.l = 0.0;
            }
            break;
         case ACTIVE:
            var0.m = 1.0;
            if (var0.D >= var0.q) {
               var0.CAsVoid();
            }
            break;
         case DESPAWNING:
            var0.m = var0.e * (1.0 - OAsDouble(var0.l / var1.J));
            if (var0.l >= var1.J) {
               var0.m = 0.0;
               var0.i = dv.Type_l.RECYCLE;
               var0.T = false;
            }
            break;
         case RECYCLE:
            var0.m = 0.0;
      }
      double var4 = switch (var1.p) {
         case STATIC -> 0.0;
         case CALM -> 0.45;
         case FLOATING -> 1.0;
         case DREAMY -> 0.8;
         case CHAOTIC -> 2.0;
      };
      double var6 = var0.D * var0.Q * var1.B * (var1.p == d1.Type_q.DREAMY ? 0.5 : 1.0) + var0.j;
      var0.f = Math.sin(var6) * var1.u * var4;
      var0.B = Math.sin(var6 * 0.63) * var1.u * var4 * 0.35;
      var0.U = Math.cos(var6 * 0.77) * var1.u * var4 * 0.25;
      var0.x = Math.sin(var6 * 0.7) * Math.toRadians(var1.j) * var4;
      double var8 = var1.p == d1.Type_q.DREAMY ? 0.045 : 0.012;
      var0.F = (0.7 + 0.3 * var0.m) * (1.0 + Math.sin(var6 * 0.65) * var8 * var4);
      if (var1.p != d1.Type_q.STATIC) {
         var0.f = var0.f - (1.0 - var0.m) * 0.12;
      }
   }

   private SmoothStepUtil() {
   }
}
