package com.larpclient;

import java.util.Objects;
import java.util.function.LongSupplier;

public final class w2 {
   private final LongSupplier y;
   private double Y;
   private double Z;
   private long a;
   private double ChinaHatCodec = 1.0;
   private w2.Type_e H = w2.Type_e.CUBIC_OUT;

   public w2(double var1) {
      this(var1, System::nanoTime);
   }

   public w2(double var1, LongSupplier var3) {
      this.y = Objects.requireNonNull(var3);
      this.Y = this.Z = var1;
      this.a = var3.getAsLong();
   }

   public void aAsVoid(double var1) {
      this.Y = this.Z = var1;
      this.a = this.y.getAsLong();
   }

   public double yAsDouble() {
      return this.Y + (this.Z - this.Y) * this.H.zAsDouble((double)(this.y.getAsLong() - this.a) / this.i);
   }

   public void PAsVoid(int var1) {
      if (this.yAsDouble() >= 1.0) {
         this.aAsVoid(0.0);
      }

      this.YAsDouble(1.0, var1, w2.Type_e.CUBIC_OUT);
   }

   public double bAsDouble(double var1, int var3) {
      return this.YAsDouble(var1, var3, w2.Type_e.CUBIC_OUT);
   }

   public double YAsDouble(double var1, int var3, w2.Type_e var4) {
      if (this.Z != var1) {
         this.Y = this.yAsDouble();
         this.Z = var1;
         this.a = this.y.getAsLong();
         this.i = (double)Math.max(1, var3) * 1000000.0;
         this.H = var4;
      }

      return this.yAsDouble();
   }

   public static enum Type_e {
      LINEAR,
      QUAD_IN,
      QUAD_OUT,
      QUAD_IN_OUT,
      CUBIC_IN,
      CUBIC_OUT,
      CUBIC_IN_OUT,
      EXPO_OUT,
      SINE_OUT,
      BACK_OUT;

      private Type_e() {
      }

      public double zAsDouble(double var1) {
         double var3 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case LINEAR -> var3;
            case QUAD_IN -> var3 * var3;
            case QUAD_OUT -> 1.0 - (1.0 - var3) * (1.0 - var3);
            case QUAD_IN_OUT -> var3 < 0.5 ? 2.0 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 2.0) / 2.0;
            case CUBIC_IN -> var3 * var3 * var3;
            case CUBIC_OUT -> 1.0 - Math.pow(1.0 - var3, 3.0);
            case CUBIC_IN_OUT -> var3 < 0.5 ? 4.0 * var3 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 3.0) / 2.0;
            case EXPO_OUT -> var3 == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * var3);
            case SINE_OUT -> Math.sin(var3 * Math.PI / 2.0);
            case BACK_OUT -> 1.0 + 2.70158 * Math.pow(var3 - 1.0, 3.0) + 1.70158 * Math.pow(var3 - 1.0, 2.0);
         };
      }
   }
}
