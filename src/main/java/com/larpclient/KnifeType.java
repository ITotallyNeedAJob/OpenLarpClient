package com.larpclient;

public enum KnifeType {
   BAYONET("Bayonet", "bayonet", 1.0, 6.2, 0.85, 0.0F, 0.0F, 0.0F),
   STILETTO("Stiletto", "stiletto", 1.0, 3.933333333333333, 1.0, 0.0F, 0.0F, 0.0F),
   BUTTERFLY("Butterfly", "butterfly", 0.7666666666666667, 4.1, 1.0, -0.021F, 0.0F, 0.0F),
   WATCH("Luxury Watch", "dress_watch", 0.3, 5.2, 1.0, 0.0F, 0.0F, 0.0F),
   LEDGER("Ledger Nano S", "ledger_nano_s", 1.0, 1.0, 1.0, 0.0F, 0.0F, 0.0F);

   public final String X;
   public final String q;
   public final double F;
   public final double I;
   public final double r;
   public final float Q;
   public final float a;
   public final float d;

   private KnifeType(String var3, String var4, double var5, double var7, double var9, float var11, float var12, float var13) {
      this.X = var3;
      this.q = var4;
      this.F = var5;
      this.I = var7;
      this.r = var9;
      this.Q = var11;
      this.a = var12;
      this.d = var13;
   }

   public static KnifeType bAsBc(String var0) {
      for (KnifeType var4 : values()) {
         if (var4.X.equals(var0)) {
            return var4;
         }
      }

      return BAYONET;
   }

   public boolean XAsBoolean() {
      return this == WATCH || this == LEDGER;
   }

   public boolean VAsBoolean() {
      return this != LEDGER;
   }
}
