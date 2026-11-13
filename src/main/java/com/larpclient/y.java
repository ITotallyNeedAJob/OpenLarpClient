package com.larpclient;

import net.minecraft.client.util.Window;
import org.joml.Matrix3x2f;

record y(int width, int height, float scale) {
   static final int MIN_WIDTH = 640;
   static final int MIN_HEIGHT = 360;
   static final int PAIRED_WIDTH = 766;
   static final double MIN_PAIRED_UNIT = 1.5;

   static y from(Window var0) {
      return from(var0, 1.0);
   }

   static y from(Window var0, double var1) {
      return calculate(var0.getFramebufferWidth(), var0.getFramebufferHeight(), var0.getScaleFactor(), var0.calculateScaleFactor(2, false), var1);
   }

   static y calculate(int var0, int var1, int var2, int var3) {
      return new y(Math.max(1, Math.ceilDiv(var0, var3)), Math.max(1, Math.ceilDiv(var1, var3)), (float)var3 / (float)var2);
   }

   static y calculate(int var0, int var1, int var2, int var3, double var4) {
      if (var4 != 1.0 && Double.isFinite(var4)) {
         double var6 = Math.max((double)var3, Math.min((double)var0 / 640.0, (double)var1 / 360.0));
         double var8 = Math.min((double)var3 * var4, var6);
         return new y(Math.max(1, (int)Math.ceil((double)var0 / var8)), Math.max(1, (int)Math.ceil((double)var1 / var8)), (float)(var8 / (double)var2));
      } else {
         return calculate(var0, var1, var2, var3);
      }
   }

   static y paired(Window var0, double var1) {
      return paired(from(var0, var1), var0.getFramebufferWidth(), var0.getFramebufferHeight(), var0.getScaleFactor());
   }

   static y paired(y var0, int var1, int var2, int var3) {
      if (var0.width() >= 766) {
         return var0;
      } else {
         double var4 = (double)var1 / 766.0;
         return !(var4 < 1.5) && !(var4 >= (double)(var0.scale() * (float)var3))
            ? new y(766, Math.max(1, (int)Math.ceil((double)var2 / var4)), (float)(var4 / (double)var3))
            : var0;
      }
   }

   Matrix3x2f transform() {
      return new Matrix3x2f().scaling(this.scale);
   }
}
