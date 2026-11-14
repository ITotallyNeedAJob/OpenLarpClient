package com.larpclient;

public final class FadeCurve {
   public static double RAsDouble(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private FadeCurve() {
   }

   public static enum Type_r {
      LINEAR,
      SMOOTH,
      FADE_IN_OUT,
      HOLD_FADE,
      QUICK_FADE;

      private Type_r() {
      }

      public double fAsDouble(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case LINEAR -> 1.0 - var1;
            case SMOOTH -> FadeCurve.RAsDouble(Math.min(var1 / 0.08, 1.0)) * FadeCurve.RAsDouble(1.0 - var1);
            case FADE_IN_OUT -> FadeCurve.RAsDouble(Math.min(var1 / 0.15, 1.0)) * FadeCurve.RAsDouble(Math.min((1.0 - var1) / 0.55, 1.0));
            case HOLD_FADE -> FadeCurve.RAsDouble(Math.min(var1 / 0.06, 1.0)) * FadeCurve.RAsDouble(Math.min((1.0 - var1) / 0.3, 1.0));
            case QUICK_FADE -> FadeCurve.RAsDouble(Math.min(var1 / 0.04, 1.0)) * Math.pow(1.0 - var1, 3.0);
         };
      }
   }

   public static enum Type_z {
      CONSTANT,
      SHRINK,
      GROW,
      POP,
      PULSE;

      private Type_z() {
      }

      public double vAsDouble(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case CONSTANT -> 1.0;
            case SHRINK -> 1.0 - var1;
            case GROW -> 0.15 + 0.85 * FadeCurve.RAsDouble(var1);
            case POP -> (var1 < 0.16 ? 1.18 * FadeCurve.RAsDouble(var1 / 0.16) : 1.0 + 0.18 * Math.exp(-(var1 - 0.16) * 15.0)) * Math.min(1.0, (1.0 - var1) / 0.25);
            case PULSE -> (0.8 + 0.2 * Math.sin(var1 * Math.PI * 6.0)) * Math.min(1.0, var1 / 0.08);
         };
      }
   }
}
