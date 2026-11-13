package com.larpclient;

public final class bd {
   public static final int c = -1381654;
   public static final int H = -6250336;
   public static int w = -1381654;
   public static int Q = -6250336;
   public static int d = -434562791;
   public static int M = -920509918;
   public static int h = -534173399;
   public static int u = -1381654;
   public static int e = -6250336;
   public static int A = -13882324;
   public static int X = -1341058799;
   public static int W = -8947849;
   public static int _Z = -921036262;
   public static int V = -98426334;
   public static int m = -15132391;
   public static final int o = -1381654;
   public static final int B = -7873104;
   public static final int r = -996986;
   public static final int N = -24915;
   public static int R = 2;
   public static int T = 3;
   public static int p = 3;
   public static int l = 5;
   public static int q = 7;
   public static int E = 5;
   public static int J = 5;
   public static int j = 0;
   public static int k = 0;
   public static final int i = 100;
   public static final int b = 160;
   public static final int s = 180;
   public static final int C = 150;
   public static final int G = 160;
   public static final int _X = 160;
   public static final int F = 200;
   public static final int x = 280;
   public static final int f = 270;
   public static final int _n = 4;
   public static final int z = 26;
   public static final int O = 38;
   public static int y = -533976020;
   public static int __ = -233038818;
   public static int _C = 1075978786;
   public static int Z = -8947844;
   public static int I = 569043690;
   public static int a = 166390506;
   public static final int L = 8;
   public static final int g = 12;
   public static final int v = 8;
   public static final int t = 11;
   public static final int n = 22;
   public static final int _P = 22;
   public static final int _K = 20;
   public static final int D = 44;
   public static final int Y = 44;
   private static boolean K;
   private static boolean _1;
   private static long P;
   private static double S = 1.0;
   private static bd.Type_p U = bd.Type_p.DEFAULT;

   public bd() {
   }

   public static bd.Type_e rAsBd_Type_e(boolean var0, double var1) {
      return VAsBd_Type_e(var0, var1, false);
   }

   public static int MAsInt() {
      return bg.pAsInt(m, S);
   }

   public static boolean hAsBoolean() {
      return _1;
   }

   public static int WAsInt(int var0, int var1) {
      return mi.readableAccent(tAsInt(var0), var1 | 0xFF000000);
   }

   public static int tAsInt(int var0) {
      return K ? NAsInt(0.5 + 0.5 * Math.sin((double)P / 800.0 + (double)var0 * 0.008)) : w;
   }

   public static bd.Type_p yAsBd_Type_p() {
      return U;
   }

   public static bd.Type_e VAsBd_Type_e(boolean var0, double var1, boolean var3) {
      return jAsBd_Type_e(var0, var1, var3, bd.Type_p.DEFAULT);
   }

   public static bd.Type_e jAsBd_Type_e(boolean var0, double var1, boolean var3, bd.Type_p var4) {
      int[] var5 = PAsIntArray();
      boolean var6 = K;
      boolean var7 = _1;
      long var8 = P;
      double var10 = S;
      bd.Type_p var12 = U;
      U = var4;
      K = var3;
      _1 = true;
      P = System.currentTimeMillis();
      S = var1;
      if (var0) {
         d = -592136;
         M = -1;
         h = -921100;
         u = -15263974;
         e = -10592664;
         A = -2302750;
         X = -1447443;
         W = -7697772;
         _Z = -1;
         V = -1;
         m = -1;
         y = -1052685;
         __ = -1184272;
         _C = -855307;
         Z = -6710880;
         I = 385875968;
         a = 520093696;
         j = -1250065;
         k = 706485284;
      } else {
         int var13 = vAsInt(-16119283, 0.09);
         int var14 = vAsInt(-15263973, 0.11);
         int var15 = vAsInt(-14079698, 0.2);
         u = -1184272;
         e = -6645085;
         W = -9934735;
         d = var14;
         A = var15;
         M = bg.xAsInt(var14, bg.xAsInt(var13, u, 0.035), 0.62);
         h = bg.xAsInt(var14, bg.xAsInt(var13, u, 0.06), 0.62);
         X = var13;
         _Z = bg.xAsInt(var13, u, 0.04);
         V = bg.xAsInt(var14, u, 0.025);
         m = var14;
         y = bg.xAsInt(var14, u, 0.05);
         __ = var13;
         _C = bg.XAsInt(var14, 0.25);
         Z = -6908257;
         I = bg.XAsInt(bg.xAsInt(var15, u, 0.12), 0.75);
         a = bg.XAsInt(var15, 0.6);
         j = bg.XAsInt(var13, 0.45);
         k = -1946157056;
      }

      j = bg.pAsInt(j, 0.5 + 0.3 * Math.clamp(var1, 0.0, 1.0));
      d = bg.pAsInt(d, var1);
      __ = bg.pAsInt(__, var1);
      double var16 = Math.clamp(var4.rounding(), 0.0, 2.0);
      R = round(4, var16);
      T = round(6, var16);
      p = round(8, var16);
      l = round(7, var16);
      q = round(16, var16);
      E = round(10, var16);
      J = round(12, var16);
      return new bd.Type_e(var5, var6, var7, var8, var10, var12);
   }

   public static double ZAsDouble() {
      int var0 = w >>> 16 & 0xFF;
      int var1 = w >>> 8 & 0xFF;
      int var2 = w & 0xFF;
      int var3 = Math.max(var0, Math.max(var1, var2));
      int var4 = Math.min(var0, Math.min(var1, var2));
      return 0.4 + 0.6 * (var3 == 0 ? 0.0 : (double)(var3 - var4) / (double)var3);
   }

   static int vAsInt(int var0, double var1) {
      int var3 = w >>> 16 & 0xFF;
      int var4 = w >>> 8 & 0xFF;
      int var5 = w & 0xFF;
      double var6 = 0.2126 * (double)var3 + 0.7152 * (double)var4 + 0.0722 * (double)var5;
      int[] var8 = new int[]{var0 >>> 16 & 0xFF, var0 >>> 8 & 0xFF, var0 & 0xFF};
      int[] var9 = new int[]{var3, var4, var5};
      int var10 = var0 & 0xFF000000;

      for (int var11 = 0; var11 < 3; var11++) {
         var10 |= Math.clamp(Math.round((double)var8[var11] + ((double)var9[var11] - var6) * var1), 0, 255) << 16 - 8 * var11;
      }

      return var10;
   }

   private static int[] PAsIntArray() {
      return new int[]{d, M, h, u, e, A, X, W, _Z, V, m, y, __, _C, Z, I, a, j, k, R, T, p, l, q, E, J};
   }

   private static int round(int var0, double var1) {
      return (int)Math.round((double)var0 * var1);
   }

   public static double IAsDouble() {
      return S;
   }

   public static int RAsInt(int var0, double var1) {
      return bg.XAsInt(tAsInt(var0), Math.clamp(var1, 0.0, 1.0) * 0.2);
   }

   public static int NAsInt(double var0) {
      return bg.xAsInt(w, Q, var0);
   }

   public static record Type_e(int[] previous, boolean animatedAccent, boolean style, long millis, double opacity, bd.Type_p look) implements AutoCloseable {
      @Override
      public void close() {
         bd.d = this.previous[0];
         bd.M = this.previous[1];
         bd.h = this.previous[2];
         bd.u = this.previous[3];
         bd.e = this.previous[4];
         bd.A = this.previous[5];
         bd.X = this.previous[6];
         bd.W = this.previous[7];
         bd._Z = this.previous[8];
         bd.V = this.previous[9];
         bd.m = this.previous[10];
         bd.y = this.previous[11];
         bd.__ = this.previous[12];
         bd._C = this.previous[13];
         bd.Z = this.previous[14];
         bd.I = this.previous[15];
         bd.a = this.previous[16];
         bd.j = this.previous[17];
         bd.k = this.previous[18];
         bd.R = this.previous[19];
         bd.T = this.previous[20];
         bd.p = this.previous[21];
         bd.l = this.previous[22];
         bd.q = this.previous[23];
         bd.E = this.previous[24];
         bd.J = this.previous[25];
         bd.K = this.animatedAccent;
         bd._1 = this.style;
         bd.P = this.millis;
         bd.S = this.opacity;
         bd.U = this.look;
      }
   }

   public static record Type_p(
      String card, String enabled, String switches, String slider, double rounding, boolean accentGlow, boolean ripple, boolean hoverLift
   ) {
      public static final bd.Type_p DEFAULT = new bd.Type_p("Filled", "Glow", "Pill", "Pill", 1.0, true, true, true);
   }
}
