package com.larpclient;

public final class ClickState {
   private static final long PetEffectRenderer = 511L;
   private static final long C = 68719476735L;

   public static boolean oAsBoolean(boolean var0, boolean var1, boolean var2, double var3, double var5, boolean var7, double var8) {
      return !var0 && (var2 || var1 || var3 + (var7 ? var5 : 0.0) <= var8);
   }

   public static ClickState.Type_e rAsAt_Type_e(long var0, int var2, boolean var3, boolean var4, boolean var5, int var6) {
      var0 &= 68719476735L;
      long var7 = var0 & 511L;
      long var9 = var0 & -512L;
      var2 &= 511;
      if (var3 && var4 && var7 != 0L) {
         return new ClickState.Type_e(Long.numberOfTrailingZeros(var7), 40, true);
      } else if (var3 && var0 != 0L) {
         int var11 = Long.numberOfTrailingZeros(var9 != 0L ? var9 : var0);
         return var4 && var2 != 0 ? new ClickState.Type_e(var11, Integer.numberOfTrailingZeros(var2), false) : new ClickState.Type_e(var11, 40, true);
      } else {
         return !var3 && var5 && Long.bitCount(var7) < Math.clamp((long)var6, 1, 9) && var9 != 0L && var2 != 0
            ? new ClickState.Type_e(Long.numberOfTrailingZeros(var9), Integer.numberOfTrailingZeros(var2), false)
            : null;
      }
   }

   public static boolean cAsBoolean(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      return mx.uAsBoolean(var0, var1, var2, var3, var4, var5, var6);
   }

   public static int yAsInt(int var0, int var1, double var2) {
      int var4 = Math.max(0, Math.min(var0, var1));
      int var5 = Math.max(0, Math.max(var0, var1));
      return var4 + (int)Math.floor(Math.clamp(var2, 0.0, 0.999999) * (double)(var5 - var4 + 1));
   }

   private ClickState() {
   }

   public static final class Type_a {
      public static final long h = 1000L;
      public static final long SahurDanceEngine = 4000L;
      public static final int S = 6;
      private ClickState.Type_a.Type_u ChinaHatCodec = ClickState.Type_a.Type_u.IDLE;
      private long B;
      private long R;
      private int l;
      private boolean n;

      public Type_a() {
      }

      public void GAsVoid(long var1, boolean var3) {
         this.i = ClickState.Type_a.Type_u.STOPPING;
         this.B = this.R = var1;
         this.l = 0;
         this.n = var3;
      }

      public ClickState.Type_a.Type_u iAsAt_Type_a_Type_u() {
         return this.i;
      }

      public boolean pAsBoolean() {
         return this.i != ClickState.Type_a.Type_u.IDLE;
      }

      public int PAsInt() {
         return this.l;
      }

      public boolean nAsBoolean() {
         return this.n;
      }

      public boolean PAsBoolean(long var1, boolean var3, int var4) {
         if (this.i == ClickState.Type_a.Type_u.STOPPING && var3) {
            this.i = ClickState.Type_a.Type_u.CLICKING;
            this.R = var1 + (long)Math.max(0, var4);
            return true;
         } else {
            return false;
         }
      }

      public boolean mAsBoolean(long var1, boolean var3) {
         return this.i == ClickState.Type_a.Type_u.CLICKING && var3 && var1 >= this.R && this.l < 6;
      }

      public void xAsVoid(long var1, int var3) {
         this.l++;
         this.R = var1 + (long)Math.max(0, var3);
      }

      public boolean dAsBoolean(long var1, int var3) {
         if (this.l > 0 && this.n) {
            this.i = ClickState.Type_a.Type_u.CLOSING;
            this.R = var1 + (long)Math.max(0, var3);
            return true;
         } else {
            this.i = ClickState.Type_a.Type_u.IDLE;
            return false;
         }
      }

      public boolean gAsBoolean(long var1, boolean var3) {
         return this.i == ClickState.Type_a.Type_u.CLOSING && var3 && var1 >= this.R;
      }

      public boolean LAsBoolean(long var1) {
         return this.pAsBoolean() && var1 - this.B > (this.i == ClickState.Type_a.Type_u.STOPPING ? 1000L : 4000L);
      }

      public void XAsVoid() {
         this.i = ClickState.Type_a.Type_u.IDLE;
         this.l = 0;
         this.n = false;
      }

      public static enum Type_u {
         IDLE,
         STOPPING,
         CLICKING,
         CLOSING;

         private Type_u() {
         }
      }
   }

   public static record Type_e(int source, int button, boolean equips) {
      public int menuSlot() {
         return this.source < 9 ? this.source + 36 : this.source;
      }
   }

   public static final class Type_r {
      public static final long P = 2000L;
      private ClickState.Type_r.Type_v I = ClickState.Type_r.Type_v.IDLE;
      private int J = -1;
      private int q = -1;
      private long n;
      private long z;

      public Type_r() {
      }

      public void QAsVoid(long var1, int var3, int var4, int var5) {
         this.J = var3;
         this.q = var4;
         this.I = var3 == var4 ? ClickState.Type_r.Type_v.SWAP : ClickState.Type_r.Type_v.SELECT;
         this.n = var1;
         this.z = var1 + (long)Math.max(0, var5);
      }

      public ClickState.Type_r.Type_v aAsAt_Type_r_Type_v() {
         return this.I;
      }

      public boolean iAsBoolean() {
         return this.I != ClickState.Type_r.Type_v.IDLE;
      }

      public int sAsInt() {
         return this.J;
      }

      public int PAsInt() {
         return this.q;
      }

      public boolean fAsBoolean(long var1) {
         return this.iAsBoolean() && var1 >= this.z;
      }

      public void mAsVoid(long var1, int var3) {
         this.I = switch (this.I) {
            case SELECT -> ClickState.Type_r.Type_v.SWAP;
            case SWAP -> this.J == this.q ? ClickState.Type_r.Type_v.IDLE : ClickState.Type_r.Type_v.RESTORE;
            default -> ClickState.Type_r.Type_v.IDLE;
         };
         this.z = var1 + (long)Math.max(0, var3);
      }

      public boolean GAsBoolean(int var1) {
         return this.J != this.q && var1 == this.q;
      }

      public boolean kAsBoolean(long var1) {
         return this.iAsBoolean() && var1 - this.n > 2000L;
      }

      public void oAsVoid() {
         this.I = ClickState.Type_r.Type_v.IDLE;
         this.J = this.q = -1;
      }

      public static enum Type_v {
         IDLE,
         SELECT,
         SWAP,
         RESTORE;

         private Type_v() {
         }
      }
   }

   public static final class Type_s {
      private long GroundSample = -1L;
      private long X;

      public Type_s() {
      }

      public boolean lAsBoolean() {
         return this.k >= 0L;
      }

      public boolean eAsBoolean(long var1, int var3) {
         if (this.k < 0L) {
            this.k = var1 + (long)Math.max(0, var3);
         }

         return var1 >= this.k && var1 >= this.X;
      }

      public boolean nAsBoolean(long var1) {
         return var1 >= this.X;
      }

      public void cAsVoid(long var1, boolean var3) {
         this.X = var1 + 100L;
         if (var3) {
            this.k = -1L;
         }
      }

      public void SAsVoid() {
         this.k = -1L;
      }

      public void MAsVoid() {
         this.k = -1L;
         this.X = 0L;
      }
   }
}
