package com.larpclient;

import com.larpclient.mixin.DrawContextAccessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.render.VertexConsumer;

public final class xt {
   public static final ScreenRect SkyChunkCache = new ScreenRect(0, 0, 1, 1);
   private static final double V = -10000.0;
   private static xt.Type_s Q;
   private static Object a;
   private static xt.Type_s j;
   private static boolean E;
   private static int q;
   private static int h = -1;
   private static final double[] n = new double[4];
   private static final double[] ScreenScale = new double[4];
   private static boolean c;
   private static boolean r;
   private static Object e;
   private static double G = 1.0;
   private static double i;
   private static double R;
   private static final xt.Type_m Y = new xt.Type_m();

   private xt() {
   }

   private static int HAsInt(double var0, int var2) {
      return (int)Math.clamp(var0, 0.0, (double)Math.max(1, var2));
   }

   public static void LAsVoid(Object var0, xt.Type_s var1) {
      if (BoxUtils && PetEffectRenderer != null) {
         System.arraycopy(n, 0, y, 0, 4);
         LineSegment = a;
         r = true;
      }

      BoxUtils = false;
      if (var0 != e) {
         r = false;
      }

      PetEffectRenderer = var1 == null ? null : var0;
      Q = var1;
   }

   public static void oAsVoid(DrawContext var0, xt.Type_s var1, boolean var2, Runnable var3) {
      xt.Type_e var4 = (xt.Type_e)((DrawContextAccessor)var0).larpGuiRenderState();
      var4.larpWarpBegin(var1, var2);
      q++;

      try {
         var3.run();
      } finally {
         q--;
         var4.larpWarpEnd();
      }
   }

   public static boolean zAsBoolean() {
      return q > 0;
   }

   public static void cAsVoid(double var0, double var2, double var4) {
      G = Math.max(0.01, var0);
      ChinaHatCodec = var2;
      R = var4;
   }

   public static double[] vAsDoubleArray2(Object var0) {
      return r && var0 == LineSegment ? (double[])ScreenScale.clone() : null;
   }

   public static xt.Type_s JAsXt_Type_s(Object var0) {
      return var0 != null && var0 == PetEffectRenderer ? Q : null;
   }

   public static void ZAsVoid(int var0, xt.Type_s var1, boolean var2) {
      h = var1 == null ? -1 : var0;
      TextLayout = var1;
      E = var2;
   }

   public static xt.Type_s rAsXt_Type_s() {
      return h >= 0 ? TextLayout : null;
   }

   private static void rAsVoid(float var0, float var1) {
      if (E && h >= 0 && h <= 1) {
         double var2 = ChinaHatCodec + ((double)var0 - i) / G;
         double var4 = R + ((double)var1 - R) / G;
         if (!c) {
            n[0] = n[2] = var2;
            n[1] = n[3] = var4;
            BoxUtils = true;
         } else {
            n[0] = Math.min(n[0], var2);
            n[1] = Math.min(n[1], var4);
            n[2] = Math.max(n[2], var2);
            n[3] = Math.max(n[3], var4);
         }
      }
   }

   public static xt.Type_m aAsXt_Type_m(VertexConsumer var0, xt.Type_s var1, boolean var2) {
      return IAsXt_Type_m(var0, var1, var2, null, xt.Type_d.PLAIN);
   }

   public static xt.Type_m IAsXt_Type_m(VertexConsumer var0, xt.Type_s var1, boolean var2, ScreenRect var3, xt.Type_d var4) {
      return Y.XAsXt_Type_m(var0, var1, var2, var1.clipsFlat() && var4 != xt.Type_d.CORNERS ? var3 : null, var4);
   }

   public static enum Type_d {
      PLAIN,
      CORNERS,
      SIZED;

      private Type_d() {
      }
   }

   public interface Type_e {
      void larpWarpBegin(xt.Type_s var1, boolean var2);

      void larpWarpEnd();
   }

   public static final class Type_f implements xt.Type_s {
      public static final double L = 0.02;
      private final double Y;
      private final double r;
      private final double P;
      private final double b;
      private final double Q;
      private final double G;
      private final double y;
      private final double T;
      private final double R;
      private final double M;
      private final double X;
      private final double d;
      private final double u;
      private final double p;
      private final double q;
      private final double F;
      private final double V;
      private final double J;

      public Type_f(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
         this.Y = var1;
         this.r = var3;
         this.P = var5;
         this.b = var7;
         this.Q = var9;
         this.G = var11;
         this.y = var13;
         this.T = var15;
         this.R = var17;
         this.M = var9 * var17 - var11 * var15;
         this.X = var5 * var15 - var3 * var17;
         this.d = var3 * var11 - var5 * var9;
         this.u = var11 * var13 - var7 * var17;
         this.p = var1 * var17 - var5 * var13;
         this.q = var5 * var7 - var1 * var11;
         this.F = var7 * var15 - var9 * var13;
         this.V = var3 * var13 - var1 * var15;
         this.J = var1 * var9 - var3 * var7;
      }

      public double RAsDouble(double var1, double var3) {
         return this.y * var1 + this.T * var3 + this.R;
      }

      @Override
      public boolean front(double var1, double var3) {
         return this.RAsDouble(var1, var3) >= 0.02;
      }

      @Override
      public boolean clipsFlat() {
         return true;
      }

      @Override
      public double[] forward(double var1, double var3) {
         double var5 = Math.max(0.02, this.RAsDouble(var1, var3));
         return new double[]{(this.Y * var1 + this.r * var3 + this.P) / var5, (this.b * var1 + this.Q * var3 + this.G) / var5};
      }

      @Override
      public double[] inverse(double var1, double var3) {
         double var5 = this.M * var1 + this.X * var3 + this.d;
         double var7 = this.u * var1 + this.p * var3 + this.q;
         double var9 = this.F * var1 + this.V * var3 + this.J;
         if (Math.abs(var9) < 1.0E-12) {
            return new double[]{-10000.0, -10000.0};
         } else {
            double var11 = var5 / var9;
            double var13 = var7 / var9;
            return Double.isFinite(var11) && Double.isFinite(var13) && this.front(var11, var13) ? new double[]{var11, var13} : new double[]{-10000.0, -10000.0};
         }
      }

      @Override
      public double[] bounds(double var1, double var3, double var5, double var7) {
         double[][] var9 = new double[][]{{var1, var3}, {var1, var7}, {var5, var7}, {var5, var3}};
         double var10 = Double.POSITIVE_INFINITY;
         double var12 = Double.POSITIVE_INFINITY;
         double var14 = Double.NEGATIVE_INFINITY;
         double var16 = Double.NEGATIVE_INFINITY;
         byte var18 = 0;

         for (int var19 = 0; var19 < 4; var19++) {
            double[] var20 = var9[var19];
            double[] var21 = var9[var19 + 1 & 3];
            double var22 = this.RAsDouble(var20[0], var20[1]) - 0.02;
            double var24 = this.RAsDouble(var21[0], var21[1]) - 0.02;
            if (var22 >= 0.0) {
               double[] var26 = this.forward(var20[0], var20[1]);
               var18 = 1;
               var10 = Math.min(var10, var26[0]);
               var12 = Math.min(var12, var26[1]);
               var14 = Math.max(var14, var26[0]);
               var16 = Math.max(var16, var26[1]);
            }

            if (var22 >= 0.0 != var24 >= 0.0) {
               double var29 = var22 / (var22 - var24);
               double[] var28 = this.forward(var20[0] + (var21[0] - var20[0]) * var29, var20[1] + (var21[1] - var20[1]) * var29);
               var18 = 1;
               var10 = Math.min(var10, var28[0]);
               var12 = Math.min(var12, var28[1]);
               var14 = Math.max(var14, var28[0]);
               var16 = Math.max(var16, var28[1]);
            }
         }

         return var18 != 0 ? new double[]{var10, var12, var14, var16} : new double[]{-10000.0, -10000.0, -10000.0, -10000.0};
      }
   }

   public static final class Type_m implements VertexConsumer {
      private static final float N = 12.0F;
      private static final int AuctionNotifier = 1;
      private static final int ChatPayParser = 2;
      private static final int DropletTrail = 4;
      private static final int BloomUtility = 8;
      private static final int n = 16;
      private static final int SmoothInput = 32;
      private VertexConsumer j;
      private xt.Type_s W;
      private ScreenRect s;
      private boolean x;
      private boolean Y;
      private boolean S;
      private int ChinaHatCodec = -1;
      private int r;
      private int p;
      private int a;
      private final float[] Q = new float[4];
      private final float[] R = new float[4];
      private final float[] J = new float[4];
      private final float[] X = new float[4];
      private final float[] Z = new float[4];
      private final int[] CropHelper = new int[4];
      private final int[] U = new int[4];
      private final int[] GroundSample = new int[4];
      private final int[] w = new int[4];
      private final int[] v = new int[4];
      private float h;
      private float K;
      private float E;
      private float B;

      public Type_m() {
      }

      private xt.Type_m XAsXt_Type_m(VertexConsumer var1, xt.Type_s var2, boolean var3, ScreenRect var4, xt.Type_d var5) {
         this.j = var1;
         this.W = var2;
         this.x = var3;
         this.s = var4;
         this.Y = var5 == xt.Type_d.CORNERS;
         this.S = var5 == xt.Type_d.SIZED;
         this.i = -1;
         this.r = 0;
         return this;
      }

      public void uAsVoid() {
         if (this.i == 3) {
            this.yAsVoid();
         } else {
            for (int var1 = 0; var1 <= this.i; var1++) {
               this.dAsVoid(var1);
            }
         }

         this.i = -1;
         this.r = 0;
         this.j = null;
      }

      public VertexConsumer vertex(float x, float y, float z) {
         xt.rAsVoid(x, y);
         if (!this.x) {
            double[] var4 = this.W.forward((double)x, (double)y);
            this.j.vertex((float)var4[0], (float)var4[1], z);
            return this;
         } else {
            if (this.i == 3) {
               this.yAsVoid();
               this.i = -1;
               this.r = 0;
            }

            this.i++;
            this.Q[this.i] = x;
            this.R[this.i] = y;
            this.J[this.i] = z;
            return this;
         }
      }

      public VertexConsumer color(int red, int green, int blue, int alpha) {
         return this.color((alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF);
      }

      public VertexConsumer color(int argb) {
         if (!this.x) {
            this.j.color(argb);
            return this;
         } else {
            this.u[this.i] = argb;
            this.r |= 1;
            return this;
         }
      }

      public VertexConsumer texture(float u, float v) {
         if (!this.x) {
            this.j.texture(u, v);
            return this;
         } else {
            this.X[this.i] = u;
            this.Z[this.i] = v;
            this.r |= 2;
            return this;
         }
      }

      public VertexConsumer overlay(int u, int v) {
         if (!this.x) {
            this.j.overlay(u, v);
            return this;
         } else {
            this.U[this.i] = u;
            this.k[this.i] = v;
            this.r |= 4;
            return this;
         }
      }

      public VertexConsumer light(int u, int v) {
         if (!this.x) {
            this.j.light(u, v);
            return this;
         } else {
            this.w[this.i] = u;
            this.v[this.i] = v;
            this.r |= 8;
            return this;
         }
      }

      public VertexConsumer normal(float x, float y, float z) {
         if (!this.x) {
            this.j.normal(x, y, z);
            return this;
         } else {
            this.h = x;
            this.K = y;
            this.E = z;
            this.r |= 16;
            return this;
         }
      }

      public VertexConsumer lineWidth(float width) {
         if (!this.x) {
            this.j.lineWidth(width);
            return this;
         } else {
            this.B = width;
            this.r |= 32;
            return this;
         }
      }

      private void dAsVoid(int var1) {
         double[] var2 = this.W.forward((double)this.Q[var1], (double)this.R[var1]);
         this.j.vertex((float)var2[0], (float)var2[1], this.J[var1]);
         if ((this.r & 1) != 0) {
            this.j.color(this.u[var1]);
         }

         if ((this.r & 2) != 0) {
            this.j.texture(this.X[var1], this.Z[var1]);
         }

         if ((this.r & 4) != 0) {
            this.j.overlay(this.U[var1], this.k[var1]);
         }

         if ((this.r & 8) != 0) {
            this.j.light(this.w[var1], this.v[var1]);
         }

         if ((this.r & 16) != 0) {
            this.j.normal(this.h, this.K, this.E);
         }

         if ((this.r & 32) != 0) {
            this.j.lineWidth(this.B);
         }

         if (this.S) {
            this.j.overlay(this.p, this.a);
         }
      }

      private void yAsVoid() {
         if (this.S) {
            this.p = Math.clamp((long)Math.round((Math.abs(this.X[0]) - this.J[0]) * 64.0F), 0, 32767);
            this.a = Math.clamp((long)Math.round((Math.abs(this.Z[0]) - this.J[0]) * 64.0F), 0, 32767);
         }

         if (this.Y) {
            for (int var14 = 0; var14 < 4; var14++) {
               if (!this.W.front((double)this.Q[var14], (double)this.R[var14])) {
                  return;
               }
            }

            for (int var15 = 0; var15 < 4; var15++) {
               this.dAsVoid(var15);
            }
         } else {
            float var1 = 0.0F;
            float var2 = 1.0F;
            float var3 = 0.0F;
            float var4 = 1.0F;
            if (this.s != null) {
               float[] var5;
               float[] var6;
               if (this.Q[0] == this.Q[1] && this.Q[3] == this.Q[2] && this.R[0] == this.R[3] && this.R[1] == this.R[2]) {
                  var5 = MAsFloatArray(this.Q[0], this.Q[3], this.s.getLeft(), this.s.getRight());
                  var6 = MAsFloatArray(this.R[0], this.R[1], this.s.getTop(), this.s.getBottom());
               } else if (this.R[0] == this.R[1] && this.R[3] == this.R[2] && this.Q[0] == this.Q[3] && this.Q[1] == this.Q[2]) {
                  var5 = MAsFloatArray(this.R[0], this.R[3], this.s.getTop(), this.s.getBottom());
                  var6 = MAsFloatArray(this.Q[0], this.Q[1], this.s.getLeft(), this.s.getRight());
               } else {
                  var5 = var6 = new float[]{0.0F, 1.0F};
               }

               if (var5 == null || var6 == null) {
                  return;
               }

               var1 = var5[0];
               var2 = var5[1];
               var3 = var6[0];
               var4 = var6[1];
            }

            int var16 = var1 == 0.0F && var2 == 1.0F && var3 == 0.0F && var4 == 1.0F ? 1 : 0;
            float var17 = 0.0F;

            for (int var7 = 0; var7 < 4; var7++) {
               int var8 = var7 + 1 & 3;
               var17 = Math.max(var17, (float)Math.hypot((double)(this.Q[var8] - this.Q[var7]), (double)(this.R[var8] - this.R[var7])));
            }

            var17 *= Math.max(var2 - var1, var4 - var3);
            int var19 = Math.clamp((long)((int)Math.ceil((double)(var17 / 12.0F))), 1, 24);
            if (var19 == 1 && var16 != 0) {
               for (int var21 = 0; var21 < 4; var21++) {
                  if (!this.W.front((double)this.Q[var21], (double)this.R[var21])) {
                     return;
                  }
               }

               for (int var22 = 0; var22 < 4; var22++) {
                  this.dAsVoid(var22);
               }
            } else {
               for (int var20 = 0; var20 < var19; var20++) {
                  float var9 = var1 + (var2 - var1) * (float)var20 / (float)var19;
                  float var10 = var1 + (var2 - var1) * (float)(var20 + 1) / (float)var19;

                  for (int var11 = 0; var11 < var19; var11++) {
                     float var12 = var3 + (var4 - var3) * (float)var11 / (float)var19;
                     float var13 = var3 + (var4 - var3) * (float)(var11 + 1) / (float)var19;
                     if (this.pAsBoolean(var9, var12) && this.pAsBoolean(var9, var13) && this.pAsBoolean(var10, var13) && this.pAsBoolean(var10, var12)) {
                        this.lAsVoid(var9, var12);
                        this.lAsVoid(var9, var13);
                        this.lAsVoid(var10, var13);
                        this.lAsVoid(var10, var12);
                     }
                  }
               }
            }
         }
      }

      private static float[] MAsFloatArray(float var0, float var1, int var2, int var3) {
         if (var0 != var1) {
            float var4 = ((float)var2 - var0) / (var1 - var0);
            float var5 = ((float)var3 - var0) / (var1 - var0);
            float var6 = Math.max(0.0F, Math.min(var4, var5));
            float var7 = Math.min(1.0F, Math.max(var4, var5));
            return var7 > var6 ? new float[]{var6, var7} : null;
         } else {
            return var0 >= (float)var2 && var0 <= (float)var3 ? new float[]{0.0F, 1.0F} : null;
         }
      }

      private boolean pAsBoolean(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         return this.W.front((double)VAsFloat(this.Q, var3, var4, var5, var6), (double)VAsFloat(this.R, var3, var4, var5, var6));
      }

      private void lAsVoid(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         double[] var7 = this.W.forward((double)VAsFloat(this.Q, var3, var4, var5, var6), (double)VAsFloat(this.R, var3, var4, var5, var6));
         this.j.vertex((float)var7[0], (float)var7[1], VAsFloat(this.J, var3, var4, var5, var6));
         if ((this.r & 1) != 0) {
            this.j.color(this.pAsInt(var3, var4, var5, var6));
         }

         if ((this.r & 2) != 0) {
            this.j.texture(VAsFloat(this.X, var3, var4, var5, var6), VAsFloat(this.Z, var3, var4, var5, var6));
         }

         if ((this.r & 4) != 0) {
            this.j.overlay(Math.round(xAsFloat(this.U, var3, var4, var5, var6)), Math.round(xAsFloat(this.k, var3, var4, var5, var6)));
         }

         if ((this.r & 8) != 0) {
            this.j.light(Math.round(xAsFloat(this.w, var3, var4, var5, var6)), Math.round(xAsFloat(this.v, var3, var4, var5, var6)));
         }

         if ((this.r & 16) != 0) {
            this.j.normal(this.h, this.K, this.E);
         }

         if ((this.r & 32) != 0) {
            this.j.lineWidth(this.B);
         }

         if (this.S) {
            this.j.overlay(this.p, this.a);
         }
      }

      private static float VAsFloat(float[] var0, float var1, float var2, float var3, float var4) {
         return var0[0] * var1 + var0[1] * var2 + var0[2] * var3 + var0[3] * var4;
      }

      private static float xAsFloat(int[] var0, float var1, float var2, float var3, float var4) {
         return (float)var0[0] * var1 + (float)var0[1] * var2 + (float)var0[2] * var3 + (float)var0[3] * var4;
      }

      private int pAsInt(float var1, float var2, float var3, float var4) {
         int var5 = 0;

         for (int var6 = 0; var6 < 32; var6 += 8) {
            float var7 = (float)(this.u[0] >>> var6 & 0xFF) * var1
               + (float)(this.u[1] >>> var6 & 0xFF) * var2
               + (float)(this.u[2] >>> var6 & 0xFF) * var3
               + (float)(this.u[3] >>> var6 & 0xFF) * var4;
            var5 |= Math.clamp((long)Math.round(var7), 0, 255) << var6;
         }

         return var5;
      }
   }

   public static record Type_n(double pivotX, double pivotY, double targetX, double targetY, double scale, double sin, double cos, double focal)
      implements xt.Type_s {
      public static xt.Type_n ofAsXt_Type_n(double var0, double var2, double var4, double var6, double var8, double var10, double var12) {
         return new xt.Type_n(var0, var2, var4, var6, var8, Math.sin(var10), Math.cos(var10), var12);
      }

      @Override
      public double[] forward(double var1, double var3) {
         double var5 = (var1 - this.pivotX) * this.scale;
         double var7 = (var3 - this.pivotY) * this.scale;
         double var9 = this.focal / Math.max(this.focal * 0.2, this.focal + var5 * this.sin);
         return new double[]{this.targetX + var5 * this.cos * var9, this.targetY + var7 * var9};
      }

      @Override
      public double[] inverse(double var1, double var3) {
         double var5 = var1 - this.targetX;
         double var7 = var3 - this.targetY;
         double var9 = this.focal * this.cos - var5 * this.sin;
         if (var9 <= this.focal * 0.001) {
            return new double[]{-10000.0, -10000.0};
         } else {
            double var11 = var5 * this.focal / var9;
            double var13 = this.focal / Math.max(this.focal * 0.2, this.focal + var11 * this.sin);
            return new double[]{this.pivotX + var11 / this.scale, this.pivotY + var7 / var13 / this.scale};
         }
      }

      @Override
      public double[] bounds(double var1, double var3, double var5, double var7) {
         double[] var9 = this.forward(var1, var3);
         double[] var10 = this.forward(var1, var7);
         double[] var11 = this.forward(var5, var7);
         double[] var12 = this.forward(var5, var3);
         return new double[]{
            Math.min(Math.min(var9[0], var10[0]), Math.min(var11[0], var12[0])),
            Math.min(Math.min(var9[1], var10[1]), Math.min(var11[1], var12[1])),
            Math.max(Math.max(var9[0], var10[0]), Math.max(var11[0], var12[0])),
            Math.max(Math.max(var9[1], var10[1]), Math.max(var11[1], var12[1]))
         };
      }
   }

   public interface Type_s {
      double[] forward(double var1, double var3);

      double[] inverse(double var1, double var3);

      double[] bounds(double var1, double var3, double var5, double var7);

      default boolean front(double var1, double var3) {
         return true;
      }

      default boolean clipsFlat() {
         return false;
      }

      default ScreenRect mAsScreenRect(ScreenRect var1, int var2, int var3) {
         if (var1 == null) {
            return null;
         } else {
            double[] var4 = this.bounds((double)var1.getLeft(), (double)var1.getTop(), (double)var1.getRight(), (double)var1.getBottom());
            int var5 = xt.HAsInt(Math.floor(var4[0]), var2);
            int var6 = xt.HAsInt(Math.floor(var4[1]), var3);
            int var7 = xt.HAsInt(Math.ceil(var4[2]), var2);
            int var8 = xt.HAsInt(Math.ceil(var4[3]), var3);
            return var7 > var5 && var8 > var6 ? new ScreenRect(var5, var6, var7 - var5, var8 - var6) : xt.s;
         }
      }
   }
}
