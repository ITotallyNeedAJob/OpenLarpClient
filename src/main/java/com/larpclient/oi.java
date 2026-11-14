package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class oi {
   public static final double D = 1.5;
   public static final double B = 0.03;
   public static final double A = 0.99;
   public static final double Z = 0.8;
   public static final double HexEdgeloop = 0.0172275;
   public static final double P = 0.1F;
   public static final int SkyChunkCache = 60;
   static final int G = 8;
   static final double h = 0.15625;
   static final double AuctionNotifier = 0.15F;
   static final double BoxUtils = 0.3125;
   static final double PriceDetector = 0.3F;
   static final double R = 1.8F;
   private static final double PetEffectRenderer = 0.0103365;
   private static final Vec3d[] E = new Vec3d[]{
      Vec3d.ZERO,
      new Vec3d(0.0103365, 0.0, 0.0),
      new Vec3d(-0.0103365, 0.0, 0.0),
      new Vec3d(0.0, 0.0103365, 0.0),
      new Vec3d(0.0, -0.0103365, 0.0),
      new Vec3d(0.0, 0.0, 0.0103365),
      new Vec3d(0.0, 0.0, -0.0103365)
   };
   public static final oi.Type_q Y = new oi.Type_t();

   public static oi.Type_d FAsOi_Type_d(oi.Type_x var0, oi.Type_q var1) {
      oi.Type_d var2 = new oi.Type_d();
      Vec3d var3 = var0.position();
      Vec3d var4 = var0.velocity();

      for (int var5 = 0; var5 < 60; var5++) {
         double var6 = var1.water(var3) ? 0.8 : 0.99;
         var4 = new Vec3d(var4.x, var4.y - 0.03, var4.z).multiply(var6);
         Vec3d var8 = var3.add(var4);
         double var9 = var1.clip(var3, var8);
         var2.S[var5] = var3;
         var2.Z[var5] = var9 < 1.0 ? var3.add(var4.multiply(var9)) : var8;
         var2.t[var5] = NAsDouble(var0.age() + var5 + 1);
         var2.b = var5 + 1;
         if (var9 < 1.0) {
            var2.P = var2.Z[var5];
            break;
         }

         var3 = var8;
      }

      return var2;
   }

   public static List<oi.Type_d> wAsList(oi.Type_x var0, oi.Type_q var1) {
      ArrayList var2 = new ArrayList(3);
      var2.add(FAsOi_Type_d(var0, var1));
      var2.add(FAsOi_Type_d(var0.step(var1), var1));
      if (var0.age() > 0) {
         var2.add(FAsOi_Type_d(var0.back(), var1));
      }

      return var2;
   }

   public static List<oi.Type_d> NAsList(Vec3d var0, Vec3d var1, Vec3d var2, int var3, oi.Type_q var4) {
      List var5 = wAsList(oi.Type_x.thrown(var0, var1, var2).after(var3, var4), var4);

      for (int var6 = 1; var6 < E.length; var6++) {
         var5.add(FAsOi_Type_d(oi.Type_x.thrown(var0, var1.add(E[var6]), var2).after(var3, var4), var4));
      }

      return var5;
   }

   public static List<oi.Type_d> jAsList(Vec3d var0, Vec3d var1, Vec3d var2, oi.Type_q var3) {
      ArrayList var4 = new ArrayList(E.length);

      for (Vec3d var8 : E) {
         var4.add(FAsOi_Type_d(oi.Type_x.thrown(var0, var1.add(var8), var2), var3));
      }

      return var4;
   }

   public static oi.Type_y iAsOi_Type_y(Vec3d var0, float var1, float var2, Vec3d var3, oi.Type_q var4, double var5) {
      float var7 = Math.max(-90.0F, Math.min(var2, 0.0F));
      int var8 = 0;

      while (true) {
         float var9 = Math.max(-90.0F, var7 - (float)var8);
         Vec3d var10 = uAsVec3d(var1, var9);
         if (nAsOi_Type_r(FAsOi_Type_d(oi.Type_x.thrown(var0, var10, var3), var4), var0, var10.multiply(1.5).add(var3), var4) != null) {
            oi.Type_v var11 = uAsOi_Type_v(jAsList(var0, var10, var3, var4), var0, var3, var10, var4);
            if (var11 != null && var11.reliability() >= var5) {
               return new oi.Type_y(var9, var11);
            }
         }

         if (var9 <= -90.0F) {
            return null;
         }

         var8++;
      }
   }

   public static double NAsDouble(int var0) {
      return (double)Math.max(0.0F, Math.min(0.3F, (float)(var0 - 2) / 20.0F));
   }

   static Box KAsBox(Vec3d var0) {
      return new Box(var0.x - 0.15625, var0.y - 0.15F, var0.z - 0.15625, var0.x + 0.15625, var0.y - 0.15F + 0.3125, var0.z + 0.15625);
   }

   static Box qAsBox(Vec3d var0) {
      return new Box(var0.x - 0.3F, var0.y, var0.z - 0.3F, var0.x + 0.3F, var0.y + 1.8F, var0.z + 0.3F);
   }

   public static Vec3d uAsVec3d(float var0, float var1) {
      return new Vec3d(
            (double)(-MathHelper.sin((double)(var0 * (float) (Math.PI / 180.0))) * MathHelper.cos((double)(var1 * (float) (Math.PI / 180.0)))),
            (double)(-MathHelper.sin((double)(var1 * (float) (Math.PI / 180.0)))),
            (double)(MathHelper.cos((double)(var0 * (float) (Math.PI / 180.0))) * MathHelper.cos((double)(var1 * (float) (Math.PI / 180.0))))
         )
         .normalize();
   }

   public static oi.Type_r nAsOi_Type_r(oi.Type_d var0, Vec3d var1, Vec3d var2, oi.Type_q var3) {
      for (int var4 = 0; var4 < var0.b; var4++) {
         Vec3d var5 = var1.add(var2.multiply((double)var4));
         if (!KAsBox(var5).expand(var0.t[var4]).raycast(var0.S[var4], var0.Z[var4]).isEmpty()) {
            if (var4 > 0 && var3.clip(var1, var5) < 1.0) {
               return null;
            }

            Vec3d var6 = var0.S[var4];
            boolean var7 = qAsBox(var6).expand(NAsDouble(var4 + 1)).raycast(var5, var5.add(var2)).isPresent();
            return new oi.Type_r(var4 + 1, var6, var7);
         }
      }

      return null;
   }

   public static oi.Type_v uAsOi_Type_v(List<oi.Type_d> var0, Vec3d var1, Vec3d var2, Vec3d var3, oi.Type_q var4) {
      Vec3d var5 = var3.normalize();
      oi.Type_r var6 = nAsOi_Type_r(var0.get(0), var1, var5.multiply(1.5).add(var2), var4);
      return var6 == null ? null : OAsOi_Type_v(var0, var1, var2, var5, var6);
   }

   private static oi.Type_v OAsOi_Type_v(List<oi.Type_d> var0, Vec3d var1, Vec3d var2, Vec3d var3, oi.Type_r var4) {
      int var5 = 0;
      int var6 = 0;

      for (oi.Type_d var8 : var0) {
         for (Vec3d var12 : E) {
            var6++;
            if (nAsOi_Type_r(var8, var1, var3.add(var12).multiply(1.5).add(var2), Y) != null) {
               var5++;
            }
         }
      }

      return new oi.Type_v(var3, var4, var5, var6);
   }

   public static oi.Type_v SAsOi_Type_v(List<oi.Type_d> var0, Vec3d var1, Vec3d var2, oi.Type_q var3) {
      oi.Type_d var4 = var0.get(0);
      Vec3d var5 = var1.add(0.0, 0.006249994F, 0.0);
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 < var4.b; var7++) {
         Vec3d var8 = var5.add(var2.multiply((double)var7));
         Vec3d var9 = var4.S[var7];
         Vec3d var10 = var4.Z[var7].subtract(var9);
         double var11 = var10.lengthSquared();
         if (!(var11 < 1.0E-9)) {
            Vec3d var13 = var9.subtract(var8);
            double var14 = 1.5 * (double)var7;
            double var16 = 2.0 * var10.dotProduct(var13);
            double var18 = var13.lengthSquared() - var14 * var14;
            double var20 = var16 * var16 - 4.0 * var11 * var18;
            if (!(var20 < 0.0)) {
               double var22 = (0.15625 + var4.t[var7]) / Math.sqrt(var11);
               double var24 = Math.sqrt(var20);

               for (double var29 : new double[]{(-var16 - var24) / (2.0 * var11), (-var16 + var24) / (2.0 * var11)}) {
                  if (var29 >= -var22 && var29 <= 1.0 + var22) {
                     var6.add(var9.add(var10.multiply(var29)).subtract(var8));
                  }
               }
            }
         }
      }

      ArrayList<oi.Type_v> var31 = new ArrayList<>();

      for (Vec3d var34 : (Iterable<Vec3d>) (var6)) {
         if (!(var34.lengthSquared() < 1.0E-12)) {
            Vec3d var36 = var34.normalize();
            oi.Type_r var38 = nAsOi_Type_r(var4, var1, var36.multiply(1.5).add(var2), var3);
            if (var38 != null) {
               var31.add(new oi.Type_v(var36, var38, 0, 0));
            }
         }
      }

      var31.sort(Comparator.comparingInt(var0x -> var0x.nominal().tick()));
      oi.Type_v var33 = null;

      for (int var35 = 0; var35 < Math.min(8, var31.size()); var35++) {
         oi.Type_v var37 = OAsOi_Type_v(var0, var1, var2, var31.get(var35).direction(), var31.get(var35).nominal());
         if (var33 == null || var37.score() > var33.score()) {
            var33 = var37;
         }
      }

      return var33;
   }

   private oi() {
   }

   public static final class Type_d {
      final Vec3d[] S = new Vec3d[60];
      final Vec3d[] Z = new Vec3d[60];
      final double[] SmoothInput = new double[60];
      int b;
      Vec3d P;

      public Type_d() {
      }

      public int IAsInt() {
         return this.b;
      }

      public Vec3d BAsVec3d() {
         return this.P;
      }
   }

   public interface Type_q {
      double clip(Vec3d var1, Vec3d var2);

      boolean water(Vec3d var1);
   }

   public static record Type_r(int tick, Vec3d point, boolean burst) {
   }

   private static final class Type_t implements oi.Type_q {
      private Type_t() {
      }

      @Override
      public double clip(Vec3d var1, Vec3d var2) {
         return 1.0;
      }

      @Override
      public boolean water(Vec3d var1) {
         return false;
      }
   }

   public static record Type_v(Vec3d direction, oi.Type_r nominal, int score, int samples) {
      public double reliability() {
         return this.samples == 0 ? 0.0 : (double)this.score / (double)this.samples;
      }
   }

   public static record Type_x(Vec3d position, Vec3d velocity, int age) {
      public static oi.Type_x thrown(Vec3d var0, Vec3d var1, Vec3d var2) {
         return new oi.Type_x(var0.subtract(0.0, 0.1F, 0.0), var1.multiply(1.5).add(var2), 0);
      }

      public oi.Type_x step(oi.Type_q var1) {
         double var2 = var1.water(this.position) ? 0.8 : 0.99;
         Vec3d var4 = new Vec3d(this.velocity.x, this.velocity.y - 0.03, this.velocity.z).multiply(var2);
         return new oi.Type_x(this.position.add(var4), var4, this.age + 1);
      }

      public oi.Type_x back() {
         return new oi.Type_x(this.position.subtract(this.velocity), this.velocity.multiply(1.0101010101010102).add(0.0, 0.03, 0.0), this.age - 1);
      }

      public oi.Type_x after(int var1, oi.Type_q var2) {
         oi.Type_x var3 = this;

         for (int var4 = 0; var4 < var1; var4++) {
            var3 = var3.step(var2);
         }

         return var3;
      }
   }

   public static record Type_y(float pitch, oi.Type_v plan) {
   }
}
