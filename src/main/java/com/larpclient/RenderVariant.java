package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;

public final class RenderVariant {
   public static final int M = 32;
   public static final double d = 0.01890625;
   private final ArrayDeque<RenderVariant.Type_r> S = new ArrayDeque<>();
   private final Quaterniond F = new Quaterniond();
   private final Vector3d f = new Vector3d();
   private final Vector3d i = new Vector3d();
   private String v = "";
   private RenderVariant.Type_x s;
   private int t = 16;
   private int z;
   private int g = -1;
   private boolean R;
   private double h;
   private double C;
   private double J = 1.0;
   private double V;
   private double n;
   private double w = 8.0;
   private RenderVariant.Type_x U;
   private double y = 8.0;

   public RenderVariant() {
   }

   public void BAsVoid() {
      this.R = false;
      this.h = this.C = 0.0;
      this.v = "";
      this.g = -1;
      this.z = 0;
      this.s = null;
      this.U = null;
      this.y = 8.0;
      this.S.clear();
   }

   public void nAsVoid(int var1) {
      this.t = Math.clamp((long)var1, 4, 32);
      this.wAsVoid();
   }

   public void XAsVoid(double var1) {
      this.y = Math.clamp(var1, 0.6, 8.0);
   }

   private void wAsVoid() {
      int var1 = 1 + (this.t - 4) * 3 / 28;

      while (this.S.size() > var1) {
         this.S.removeLast();
      }
   }

   public void UAsVoid(List<String> var1) {
      String var2 = var1.isEmpty() ? "" : var1.getFirst();
      this.TAsVoid(var1, this.v.equals(var2) ? this.g : this.g + 1);
   }

   public void TAsVoid(List<String> var1, int var2) {
      String var3 = var1.isEmpty() ? "" : var1.getFirst();
      if (!this.v.equals(var3) || this.g != var2) {
         if (this.s != null && this.s.opacity >= 0.00392156862745098) {
            this.S.addFirst(new RenderVariant.Type_r(this.s));
         }

         this.wAsVoid();
         this.v = var3;
         this.g = var2;
         this.z++;
         this.J = this.z % 2 == 0 ? -1.0 : 1.0;
         this.V = this.J * 0.1;
         this.n = 0.17 + 0.035 * Math.sin((double)this.z * 2.399);
         this.w = Math.min(7.5 + 0.6 * Math.sin((double)this.z * 1.7), this.y);
         this.h = this.C = 0.0;
         this.R = false;
         this.s = null;
      }
   }

   public List<RenderVariant.Type_x> KAsList(
      double var1,
      double var3,
      double var5,
      Quaterniondc var7,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      ToDoubleFunction<String> var18
   ) {
      var16 = Math.clamp(var16, 0.0, 0.1);
      this.f.set(var1, var3, var5);
      ArrayList var19 = new ArrayList(5);
      double var20 = 0.65 + (double)this.t / 32.0 * 0.65;
      Iterator var22 = this.S.iterator();

      while (var22.hasNext()) {
         RenderVariant.Type_r var23 = (RenderVariant.Type_r)var22.next();
         var23.n += var16;
         if (var23.n >= var20) {
            var22.remove();
         } else {
            double var24 = SmoothStepUtil.OAsDouble(var23.n / var20);
            RenderVariant.Type_x var26 = var23.O;
            double var27 = var26.opacity * (1.0 - var24);
            if (var27 >= 0.00392156862745098) {
               var19.add(this.GAsRv_Type_x(var26, var27, var26.age, var23.n / var20));
            }
         }
      }

      if (this.v.isBlank()) {
         return List.copyOf(var19);
      } else {
         if (!this.R) {
            double var33 = Math.clamp(Math.abs(var8), 0.1, 8.0);
            double var35 = Math.clamp(Math.abs(var10), 0.1, 8.0);
            double var36 = 0.7 * Math.clamp(var33 / var35, 0.25, 4.0);
            double var28 = 0.75 + Math.clamp(var14, 0.0, 2.2) * 0.25;
            this.i.set(this.V * var33 * this.w, this.n * var35 * this.w, -this.w).rotate(var7);
            this.F.set(var7).rotateXYZ(0.07 * Math.sin((double)this.z * 1.7), this.J * 0.16 * var28, this.J * 0.1 * var28);
            double var30 = 0.098 * this.w / 0.75625 * var12;
            var30 = Math.min(var30, 1.3 * var36 * this.w / (0.01890625 * Math.max(1.0, var18.applyAsDouble(this.v))));
            var30 = Math.min(var30, 0.154 * this.w / 0.75625);
            this.U = new RenderVariant.Type_x(this.v, this.i.x, this.i.y, this.i.z, var30, 1.0, this.F.x, this.F.y, this.F.z, this.F.w, 0.0, 0.0);
            this.R = true;
         }

         this.h = Math.min(1.0, this.h + var16 / 0.28);
         this.C += var16;
         RenderVariant.Type_x var34 = new RenderVariant.Type_x(
            this.v, this.U.x, this.U.y, this.U.z, this.U.scale, SmoothStepUtil.OAsDouble(this.h), this.U.qx, this.U.qy, this.U.qz, this.U.qw, this.C, 0.0
         );
         this.s = var34;
         if (var34.opacity >= 0.00392156862745098) {
            var19.add(this.GAsRv_Type_x(var34, var34.opacity, this.C, 0.0));
         }

         return List.copyOf(var19);
      }
   }

   private RenderVariant.Type_x GAsRv_Type_x(RenderVariant.Type_x var1, double var2, double var4, double var6) {
      return new RenderVariant.Type_x(var1.text, this.f.x + var1.x, this.f.y + var1.y, this.f.z + var1.z, var1.scale, var2, var1.qx, var1.qy, var1.qz, var1.qw, var4, var6);
   }

   private static final class Type_r {
      final RenderVariant.Type_x O;
      double n;

      Type_r(RenderVariant.Type_x var1) {
         this.O = var1;
      }
   }

   public static record Type_x(
      String text, double x, double y, double z, double scale, double opacity, double qx, double qy, double qz, double qw, double age, double exit
   ) {
   }
}
