package com.larpclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.util.math.Vec3d;

public final class BloomSettings {
   public static final int N = 64;
   private final BloomSettings.Type_l[] BoxUtils = new BloomSettings.Type_l[64];
   private int r;
   private int h;
   private int y;
   private long O;
   private boolean W;
   private double CropHelper = Double.NaN;
   private float e;
   private Object H;

   public BloomSettings() {
   }

   public int HAsInt() {
      return this.h;
   }

   public void XAsVoid() {
      Arrays.fill(this.c, null);
      this.r = this.h = this.y = 0;
      this.W = false;
      this.u = Double.NaN;
      this.H = null;
   }

   public void HAsVoid(double var1, double var3) {
      while (this.h > 0 && var1 - this.c[this.r].time() > var3) {
         this.c[this.r] = null;
         this.r = (this.r + 1) % 64;
         this.h--;
      }
   }

   public boolean MAsBoolean(Object var1, Vec3d var2, Vec3d var3, double var4, float var6, boolean var7, double var8, int var10) {
      if (NAsBoolean(var2) && NAsBoolean(var3) && Double.isFinite(var4)) {
         if (this.H != var1 || Double.isFinite(this.u) && (var4 < this.u || var4 - this.u > 0.25)) {
            this.XAsVoid();
            this.H = var1;
         }

         this.u = var4;
         this.HAsVoid(var4, var8);

         while (this.h > Math.clamp((long)var10, 4, 64)) {
            this.c[this.r] = null;
            this.r = (this.r + 1) % 64;
            this.h--;
         }

         if (var7 && !(var6 <= 0.0F) && !(var6 >= 1.0F)) {
            int var11 = this.W && !(var6 + 0.05F < this.e) ? 0 : 1;
            if (var11 != 0) {
               this.y++;
            }

            this.W = true;
            this.e = var6;
            BloomSettings.Type_l var12 = this.h > 0 ? this.TAsLb_Type_l(this.h - 1) : null;
            if (var12 != null && var12.stroke() == this.y) {
               double var13 = Math.max(var2.distanceTo(var12.root()), var3.distanceTo(var12.tip()));
               if (!(var13 > 2.5)) {
                  double var15 = var4 - var12.time();
                  if (!(var15 <= 1.0E-7) && (!(var13 < 0.012) || !(var15 < 0.045))) {
                     if (var13 < 0.001) {
                        return false;
                     }

                     int var17 = Math.clamp((long)((int)Math.ceil(var13 / 0.09)), 1, 6);

                     for (int var18 = 1; var18 <= var17; var18++) {
                        double var19 = (double)var18 / (double)var17;
                        this.WAsVoid(
                           new BloomSettings.Type_l(var12.root().lerp(var2, var19), var12.tip().lerp(var3, var19), var12.time() + var15 * var19, this.y, ++this.O), var10
                        );
                     }

                     return true;
                  }

                  return false;
               }

               this.XAsVoid();
               this.H = var1;
               this.y++;
               this.W = true;
            }

            this.WAsVoid(new BloomSettings.Type_l(var2, var3, var4, this.y, ++this.O), var10);
            return true;
         } else {
            this.W = false;
            return false;
         }
      } else {
         this.XAsVoid();
         return false;
      }
   }

   private void WAsVoid(BloomSettings.Type_l var1, int var2) {
      int var3 = Math.clamp((long)var2, 4, 64);

      while (this.h >= var3) {
         this.c[this.r] = null;
         this.r = (this.r + 1) % 64;
         this.h--;
      }

      this.c[(this.r + this.h++) % 64] = var1;
   }

   public BloomSettings.Type_l TAsLb_Type_l(int var1) {
      return this.c[(this.r + var1) % 64];
   }

   public List<BloomSettings.Type_l> eAsList() {
      ArrayList var1 = new ArrayList(this.h);

      for (int var2 = 0; var2 < this.h; var2++) {
         var1.add(this.TAsLb_Type_l(var2));
      }

      return List.copyOf(var1);
   }

   private static boolean NAsBoolean(Vec3d var0) {
      return var0 != null && Double.isFinite(var0.x + var0.y + var0.z);
   }

   public static record Type_l(Vec3d root, Vec3d tip, double time, int stroke, long serial) {
   }
}
