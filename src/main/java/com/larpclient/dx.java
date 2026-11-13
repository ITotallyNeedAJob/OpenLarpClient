package com.larpclient;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public final class dx {
   public static final String[] N = new String[]{
      "Mode 1",
      "Mode 2",
      "Mode 3",
      "Mode 4",
      "Mode 5",
      "Module 1",
      "Module 2",
      "Module 3",
      "Module 4",
      "Module 5",
      "Module 6",
      "Module 7",
      "Module 8",
      "360",
      "Slant",
      "Triangle",
      "Shift",
      "Break",
      "Lunge",
      "Spear",
      "Down",
      "Jelly",
      "Shake"
   };

   private dx() {
   }

   public static float oAsFloat(float var0) {
      return Float.isFinite(var0) ? Math.clamp(var0, 0.0F, 1.0F) : 0.0F;
   }

   public static int yAsInt(int var0, double var1) {
      double var3 = Double.isFinite(var1) ? Math.clamp(var1, 0.25, 3.0) : 1.0;
      return Math.max(1, (int)Math.round((double)var0 * var3));
   }

   public static float RAsFloat(float var0, boolean var1, float var2, float var3) {
      float var4 = oAsFloat(var0);
      if (Math.abs(var3 - 1.0F) > 0.001F) {
         var4 = (float)Math.pow((double)var4, (double)var3);
      }

      if (!var1) {
         return var4;
      } else {
         float var5 = var4 < 0.5F ? 4.0F * var4 * var4 * var4 : 1.0F - (float)Math.pow((double)(-2.0F * var4 + 2.0F), 3.0) / 2.0F;
         return var4 + (var5 - var4) * Math.clamp(var2, 0.0F, 1.0F);
      }
   }

   private static void gAsVoid(MatrixStack var0, float var1) {
      var0.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var1));
   }

   private static void CAsVoid(MatrixStack var0, float var1) {
      var0.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var1));
   }

   private static void kAsVoid(MatrixStack var0, float var1) {
      var0.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(var1));
   }

   private static void wAsVoid(MatrixStack var0, float var1) {
      var0.translate(var1 * 0.56F, -0.52F, -0.72F);
   }

   private static void vAsVoid(MatrixStack var0, float var1, float var2) {
      var0.translate(
         var1 * -0.4F * (float)Math.sin(Math.sqrt((double)var2) * Math.PI),
         0.2F * (float)Math.sin(Math.sqrt((double)var2) * 2.0 * Math.PI),
         -0.2F * (float)Math.sin((double)var2 * Math.PI)
      );
   }

   private static void qAsVoid(MatrixStack var0, float var1, float var2) {
      float var3 = (float)Math.sin(Math.sqrt((double)var2) * Math.PI);
      CAsVoid(var0, var1 * (45.0F - 20.0F * (float)Math.sin((double)(var2 * var2) * Math.PI)));
      kAsVoid(var0, var1 * -20.0F * var3);
      gAsVoid(var0, -80.0F * var3);
      CAsVoid(var0, var1 * -45.0F);
   }

   public static void kAsVoid(MatrixStack var0, String var1, boolean var2, float var3, dx.Type_j var4, dx.Type_r var5) {
      float var6 = RAsFloat(var3, var4.smooth, var4.smoothAmount, var4.curve);
      float var7 = (float)Math.sin(Math.sqrt((double)var6) * Math.PI);
      float var8 = (float)Math.sin((double)var6 * Math.PI);
      float var9 = var2 ? -1.0F : 1.0F;
      float var10 = var4.strength * 10.0F;
      if (var1.equals("Module 8")) {
         vAsVoid(var0, var9, var6);
      }

      wAsVoid(var0, var9);
      if (!var1.equals("Mode 1") && !var1.equals("Module 8")) {
         switch (var1) {
            case "Mode 2":
               gAsVoid(var0, 50.0F);
               CAsVoid(var0, var9 * -60.0F);
               kAsVoid(var0, var9 * (110.0F + var4.legacyStrength * var7));
               return;
            case "Mode 3":
               gAsVoid(var0, 50.0F);
               CAsVoid(var0, var9 * (-30.0F * (1.0F - var7) - 30.0F + (var4.legacyStrength - 20.0F) * var7));
               kAsVoid(var0, var9 * 110.0F);
               return;
            case "Mode 4":
               CAsVoid(var0, var9 * 90.0F);
               kAsVoid(var0, var9 * -30.0F);
               gAsVoid(var0, -80.0F - var4.legacyStrength * var8);
               return;
            case "Mode 5":
               gAsVoid(var0, -360.0F * var6);
               return;
            default:
               if (var4.down > 0.0F) {
                  var0.translate(0.0F, -var4.down * 0.18F * var8, 0.0F);
               }

               switch (var1) {
                  case "Module 1":
                     CAsVoid(var0, var9 * (-45.0F - 20.0F * var7));
                     kAsVoid(var0, var9 * -20.0F * var7);
                     gAsVoid(var0, -80.0F * var7);
                     CAsVoid(var0, var9 * 45.0F);
                     gAsVoid(var0, -var10 * var8);
                     break;
                  case "Module 2":
                     var0.translate(0.0F, 0.15F, -0.3F - var4.distance);
                     CAsVoid(var0, var9 * 90.0F);
                     kAsVoid(var0, var9 * -60.0F);
                     gAsVoid(var0, -var4.angle - var10 * var8);
                     break;
                  case "Module 3":
                     gAsVoid(var0, var6 * (float) Math.PI - var10 * var8);
                     break;
                  case "Module 4":
                     var0.translate(0.0F, 0.15F, -0.3F - var4.distance);
                     CAsVoid(var0, var9 * 70.0F);
                     kAsVoid(var0, var9 * -30.0F);
                     var0.scale(0.9F, 0.9F, 0.9F);
                     gAsVoid(var0, -var4.angle - var10 * var8);
                     break;
                  case "Module 5":
                     CAsVoid(var0, var9 * 45.0F);
                     gAsVoid(var0, -20.0F * var8);
                     kAsVoid(var0, var9 * -20.0F * var8);
                     gAsVoid(var0, -80.0F * var8);
                     CAsVoid(var0, var9 * -45.0F);
                     break;
                  case "Module 6":
                     CAsVoid(var0, var9 * (45.0F - 20.0F * var8));
                     kAsVoid(var0, var9 * -20.0F * var8);
                     gAsVoid(var0, -80.0F * var8);
                     CAsVoid(var0, var9 * -45.0F);
                     gAsVoid(var0, -var10 * var8);
                     break;
                  case "Module 7":
                     var0.translate(0.0F, 0.0F, -0.1F * var4.strength * var8);
                     CAsVoid(var0, var9 * 75.0F);
                     kAsVoid(var0, var9 * (-37.5F * var8 - 60.0F));
                     gAsVoid(var0, -75.0F);
                     break;
                  case "360":
                     gAsVoid(var0, -360.0F * var6);
                     break;
                  case "Slant":
                     var0.translate(0.0F, 0.0F, -(0.3F + var4.distance) * var8);
                     gAsVoid(var0, -35.0F * var8);
                     kAsVoid(var0, var9 * 35.0F * var8);
                     break;
                  case "Triangle":
                     float var16 = var5.aAsFloat(oAsFloat(var3), var6);
                     var0.translate(0.0F, 0.02F * var8, -var4.distance * 0.35F * var8);
                     CAsVoid(var0, var9 * 45.0F);
                     gAsVoid(var0, -var16);
                     kAsVoid(var0, var9 * 10.0F * var8);
                     CAsVoid(var0, var9 * -45.0F);
                     break;
                  case "Shift":
                  case "Break":
                     var0.translate(var9 * 0.18F, 0.17F, -0.35F - var4.distance);
                     CAsVoid(var0, var9 * 90.0F);
                     kAsVoid(var0, var9 * (float)(var1.equals("Shift") ? -60 : -30));
                     gAsVoid(var0, -90.0F - var10 * var8 + var4.turn);
                     break;
                  case "Lunge":
                     var0.translate(var9 * (0.22F + 0.2F * var8), -0.04F + 0.1F * var8, -0.35F - var4.distance - 0.28F * var8);
                     CAsVoid(var0, var9 * (45.0F - var8 * var10 * 0.35F));
                     kAsVoid(var0, var9 * var8 * -var10 * 0.3F);
                     gAsVoid(var0, -var8 * var10);
                     CAsVoid(var0, var9 * 30.0F);
                     break;
                  case "Spear":
                     var0.translate(0.0F, 0.07F, -0.18F - var4.distance - 0.22F * var8);
                     var0.scale(1.0F, 1.0F, 1.0F + var8 * var4.strength * 0.03F);
                     gAsVoid(var0, -90.0F);
                     CAsVoid(var0, var9 * 20.0F);
                     break;
                  case "Down":
                     var0.translate(var9 * -0.18F * var8, 0.05F + 0.18F * var8, -0.22F - var4.distance);
                     CAsVoid(var0, var9 * 65.0F);
                     kAsVoid(var0, var9 * 5.0F);
                     gAsVoid(var0, -110.0F - 80.0F * var8);
                     break;
                  case "Jelly":
                     var0.translate(0.0F, 0.04F, -0.2F - var4.distance - 0.2F * var8);
                     CAsVoid(var0, var9 * 35.0F);
                     var0.scale(1.0F, 1.0F, 1.0F + var8 * var6 * var4.strength * 0.06F);
                     gAsVoid(var0, -90.0F);
                     break;
                  case "Shake":
                     float var13 = (float)Math.sin((double)var6 * Math.PI * 8.0) * var4.strength;
                     var0.translate(0.0F, 0.04F * var8, -0.2F - var4.distance * 0.5F);
                     CAsVoid(var0, var9 * (110.0F + var13));
                     gAsVoid(var0, -110.0F);
                     CAsVoid(var0, var9 * 75.0F);
                     gAsVoid(var0, 35.0F + var13);
                     break;
                  default:
                     throw new IllegalArgumentException("Unknown swing mode: " + var1);
               }
         }
      } else {
         qAsVoid(var0, var9, var6);
      }
   }

   public static record Type_j(
      float legacyStrength, float strength, float angle, float turn, float down, float distance, boolean smooth, float smoothAmount, float curve
   ) {
   }

   public static final class Type_r {
      private boolean t = true;
      private int h;
      private int F;

      public Type_r() {
      }

      public float aAsFloat(float var1, float var2) {
         if (var1 < 0.03F) {
            this.t = true;
            return (float)this.h * 120.0F;
         } else {
            if (this.t && var1 > 0.08F) {
               this.F = this.h;
               this.h = (this.h + 1) % 3;
               this.t = false;
            }

            return (float)this.F * 120.0F + var2 * 120.0F;
         }
      }

      public void EAsVoid() {
         this.t = true;
         this.h = this.F = 0;
      }
   }
}
