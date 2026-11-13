package com.larpclient;

import org.joml.Matrix3x2f;

public final class aq {
   private aq() {
   }

   public static double ZAsDouble(double var0, double var2, double var4, boolean var6) {
      return var6 ? w2.Type_e.CUBIC_OUT.zAsDouble((var0 - var2) / var4) : 1.0;
   }

   public static aq.Type_x vAsAq_Type_x(double var0, double var2, double var4, boolean var6, boolean var7) {
      if (!var6) {
         return new aq.Type_x(1.0, 0.0);
      } else {
         double var8 = Math.clamp(var0, 0.0, 1.0);
         double var10 = Math.clamp(var2, 0.0, 1.0);
         return new aq.Type_x(var7 ? 1.0 + 0.035 * var8 - 0.025 * var10 : 1.0 - 0.015 * var10, var4 - (var7 ? 2.5 * var8 : 0.0) + var10);
      }
   }

   public static record Type_x(double scale, double offsetY) {
      public static final aq.Type_x REST = new aq.Type_x(1.0, 0.0);

      public double localX(double var1, double var3) {
         return var3 + (var1 - var3) / this.scale;
      }

      public double localY(double var1, double var3) {
         return var3 + (var1 - var3 - this.offsetY) / this.scale;
      }

      public double screenX(double var1, double var3) {
         return var3 + (var1 - var3) * this.scale;
      }

      public double screenY(double var1, double var3) {
         return var3 + (var1 - var3) * this.scale + this.offsetY;
      }

      public Matrix3x2f matrix(double var1, double var3) {
         return new Matrix3x2f().translate((float)var1, (float)(var3 + this.offsetY)).scale((float)this.scale).translate((float)(-var1), (float)(-var3));
      }
   }
}
