package com.larpclient;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

public final class NetClient {
   public static final double E = 1420.0;
   private static final double D = 150.0;
   private static final double Q = 610.0;
   private static final double j = 720.0;
   private static final double l = 600.0;
   private static final double F = 0.28;
   public static final int C = 12;
   private static final double z = 1.4;
   private final NetClient.Type_c c;
   private NetClient.Type_j H;
   private long R = -1L;
   private double A;
   private double v;
   private boolean O;
   private final Set<Integer> i = new HashSet<>();

   private NetClient(NetClient.Type_c var1, boolean var2) {
      this.c = var1;
      this.H = var2 ? NetClient.Type_j.WAITING : NetClient.Type_j.ACTIVE;
   }

   public NetClient.Type_j aAsNc_Type_j() {
      return this.H;
   }

   public boolean LAsBoolean() {
      return this.H == NetClient.Type_j.WAITING;
   }

   public boolean wAsBoolean() {
      return this.H == NetClient.Type_j.ACTIVE;
   }

   public double yAsDouble() {
      return this.v;
   }

   public void xAsVoid(long var1, boolean var3, boolean var4) {
      double var5 = this.R >= 0L && var1 >= this.R && var1 - this.R <= 500000000L ? (double)(var1 - this.R) / 1000000.0 : 0.0;
      this.R = var1;
      if (var3 && this.O) {
         if (this.LAsBoolean()) {
            this.A += var5;
         } else if (this.H == NetClient.Type_j.REVEAL) {
            this.v += var5;
         } else if (this.H == NetClient.Type_j.RETURN) {
            this.v -= var5 * 1.4;
         }
      }

      this.O = var3;
      if (this.H == NetClient.Type_j.REVEAL && (!var4 || this.v >= 1420.0)) {
         this.HAsVoid();
      }

      if (this.H == NetClient.Type_j.RETURN && (!var4 || this.v <= 0.0)) {
         this.HAsVoid();
      }
   }

   public boolean RAsBoolean(boolean var1) {
      if (this.O && this.LAsBoolean()) {
         this.c.Z = true;
         this.H = var1 ? NetClient.Type_j.REVEAL : NetClient.Type_j.ACTIVE;
         this.v = 0.0;
         return true;
      } else {
         return false;
      }
   }

   public boolean BAsBoolean(boolean var1) {
      if (this.O && this.wAsBoolean()) {
         this.H = var1 ? NetClient.Type_j.RETURN : NetClient.Type_j.WAITING;
         this.v = var1 ? 1420.0 : 0.0;
         return true;
      } else {
         return false;
      }
   }

   public void HAsVoid() {
      if (this.H == NetClient.Type_j.RETURN) {
         this.H = NetClient.Type_j.WAITING;
         this.v = 0.0;
      } else if (!this.LAsBoolean()) {
         this.H = NetClient.Type_j.ACTIVE;
         this.v = 1420.0;
      }
   }

   public void pAsVoid() {
      this.c.Z = true;
      this.H = NetClient.Type_j.ACTIVE;
      this.v = 1420.0;
   }

   public boolean LAsBoolean(int var1) {
      return this.i.add(var1);
   }

   public boolean ZAsBoolean(int var1) {
      return this.i.contains(var1);
   }

   public boolean WAsBoolean(int var1) {
      return this.i.remove(var1);
   }

   public void oAsVoid(IntPredicate var1) {
      this.i.removeIf(var1xx -> !var1.test(var1xx));
   }

   public static MenuLayout.Type_c FAsD8_Type_c(int var0, int var1) {
      int var2 = Math.min(188, var0 - 32);
      return new MenuLayout.Type_c((var0 - var2) / 2, (int)((double)var1 * 0.48) + NAsInt(var1) / 2 + 42, var2, 28);
   }

   private static int NAsInt(int var0) {
      return (int)Math.clamp((double)var0 * 0.16, 30.0, 86.0);
   }

   public NetClient.Type_a TAsNc_Type_a(RootMenuLayout var1, boolean var2) {
      MenuLayout.Type_c var3 = var1.logo();
      double var4 = (double)var1.width() / 2.0;
      double var6 = (double)var1.height() * 0.48;
      double var8 = (double)var3.x() + (double)var3.width() / 2.0;
      double var10 = (double)var3.y() + (double)var3.height() / 2.0;
      double var12 = (double)NAsInt(var1.height());
      if (this.wAsBoolean()) {
         return new NetClient.Type_a(var8, var10, (double)var3.width(), 1.0, 0.0);
      } else {
         double var14 = var2 ? Math.sin(this.A / 1800.0) * 0.025 : 0.0;
         if (this.LAsBoolean()) {
            return new NetClient.Type_a(var4, var6, var12, 1.0, var14);
         } else {
            double var16 = Math.pow(Math.sin(Math.PI * clamp(this.v / 150.0)), 2.0);
            if (this.v < 150.0) {
               return new NetClient.Type_a(var4, var6 + 5.0 * var16, var12 * (1.0 + 0.06 * var16), 1.0 - 0.15 * var16, var14 - 0.055 * var16);
            } else {
               double var18 = clamp((this.v - 150.0) / 610.0);
               double var20 = CAsDouble(var18);
               double var22 = Math.sin(Math.PI * var20);
               double var24 = Math.sin((Math.PI * 2) * var18) * Math.pow(1.0 - var18, 2.0);
               return new NetClient.Type_a(
                  var4 + (var8 - var4) * var20 + Math.min(42.0, (double)var1.width() * 0.06) * var22,
                  var6 + (var10 - var6) * var20 - 24.0 * var22,
                  var12 + ((double)var3.width() - var12) * var20,
                  1.0 + 0.13 * var24,
                  var14 * (1.0 - var20) - 0.28 * var22 + 0.1 * var24
               );
            }
         }
      }
   }

   public NetClient.Type_h WAsNc_Type_h(RootMenuLayout var1, boolean var2) {
      MenuLayout.Type_c var3 = var1.logo();
      MenuLayout.Type_c var4 = var1.title();
      NetClient.Type_a var5 = this.TAsNc_Type_a(var1, var2);
      double var6 = var5.x() + var5.size() * 0.28;
      if (this.wAsBoolean()) {
         return new NetClient.Type_h((double)var4.x(), 0.0, var6, 1.0);
      } else {
         double var8 = this.LAsBoolean() ? 0.0 : clamp((this.v - 720.0) / 600.0);
         double var10 = 1.0 - Math.pow(1.0 - var8, 3.0);
         double var12 = (double)var3.width() * 0.28 - (double)var4.width() - 2.0;
         double var14 = (double)var4.x() - ((double)var3.x() + (double)var3.width() / 2.0);
         double var16 = var5.y() - ((double)var3.y() + (double)var3.height() / 2.0);
         return new NetClient.Type_h(var5.x() + var12 + (var14 - var12) * var10, var16, var6, iAsDouble(var8 / 0.25));
      }
   }

   public NetClient.Type_q BAsNc_Type_q(boolean var1) {
      double var2 = this.LAsBoolean() ? 1.0 : 1.0 - iAsDouble(this.v / 150.0);
      return new NetClient.Type_q(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public NetClient.Type_q RAsNc_Type_q(boolean var1) {
      double var2 = this.LAsBoolean() ? (var1 ? 0.78 + 0.12 * Math.sin(this.A / 900.0) : 1.0) : 1.0 - iAsDouble(this.v / 110.0);
      return new NetClient.Type_q(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public NetClient.Type_q mAsNc_Type_q(int var1) {
      return this.OAsNc_Type_q(
         var1 == 5 ? 960.0 : (double)(700 + var1 * 70),
         440.0,
         var1 < 2 ? 27.0 : 16.0,
         var1 == 5 ? -10.0 : (double)(var1 % 2 == 0 ? -7 : 7),
         var1 < 2 ? 0.78 : 0.9
      );
   }

   private NetClient.Type_q OAsNc_Type_q(double var1, double var3, double var5, double var7, double var9) {
      if (this.wAsBoolean()) {
         return NetClient.Type_q.REST;
      } else {
         double var11 = this.LAsBoolean() ? 0.0 : clamp((this.v - var1) / var3);
         double var13 = iAsDouble(var11 / 0.42);
         double var15 = jAsDouble(var11);
         double var17 = Math.sin(Math.PI * var11) * (1.0 - var11);
         return new NetClient.Type_q(var7 * var17, var5 * (1.0 - var15), var9 + (1.0 - var9) * var15, var9 + (1.0 - var9) * var15, 0.022 * var7 / 10.0 * var17, var13);
      }
   }

   public double BAsDouble() {
      return this.wAsBoolean() ? 1.0 : (this.LAsBoolean() ? 0.12 : 0.12 + 0.88 * CAsDouble((this.v - 110.0) / 820.0));
   }

   public NetClient.Type_l oAsNc_Type_l(int var1, RootMenuLayout var2) {
      double var3 = !this.LAsBoolean() && !this.wAsBoolean() ? clamp((this.v - 180.0 - (double)(var1 % 3 * 20)) / 660.0) : 0.0;
      double var5 = var1 % 2 == 0 ? -1.0 : 1.0;
      double var7 = -0.9 + (double)var1 / 2.0 * 0.29;
      double var9 = Math.min((double)var2.width() * 0.43, 270.0) * (1.0 - Math.pow(1.0 - var3, 3.0));
      double var11 = (double)var2.width() / 2.0 + var5 * (20.0 + var9 * Math.cos(var7));
      double var13 = (double)var2.height() * 0.48 + var9 * 0.35 * Math.sin(var7) - 44.0 * Math.sin(Math.PI * var3) + 35.0 * var3 * var3;
      return new NetClient.Type_l(
         var11,
         var13,
         (double)(3 + var1 % 3 * 2) * (1.0 - 0.6 * var3),
         var5 * var3 * 2.3,
         0.32 * iAsDouble(var3 / 0.15) * (1.0 - iAsDouble((var3 - 0.35) / 0.65))
      );
   }

   public static double clamp(double var0) {
      return Math.clamp(var0, 0.0, 1.0);
   }

   private static double iAsDouble(double var0) {
      var0 = clamp(var0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double CAsDouble(double var0) {
      var0 = clamp(var0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   private static double jAsDouble(double var0) {
      var0 = clamp(var0);
      double var2 = var0 - 1.0;
      return 1.0 + 2.4 * var2 * var2 * var2 + 1.4 * var2 * var2;
   }

   public static record Type_a(double x, double y, double size, double stretch, double rotation) {
   }

   public static final class Type_c {
      private boolean Z;

      public Type_c() {
      }

      public NetClient cAsNc() {
         return new NetClient(this, !this.Z);
      }
   }

   public static record Type_h(double x, double lift, double clip, double alpha) {
   }

   public static enum Type_j {
      WAITING,
      REVEAL,
      RETURN,
      ACTIVE;

      private Type_j() {
      }
   }

   public static record Type_l(double x, double y, double size, double rotation, double alpha) {
   }

   public static record Type_q(double x, double y, double scaleX, double scaleY, double rotation, double alpha) {
      public static final NetClient.Type_q REST = new NetClient.Type_q(0.0, 0.0, 1.0, 1.0, 0.0, 1.0);
   }
}
