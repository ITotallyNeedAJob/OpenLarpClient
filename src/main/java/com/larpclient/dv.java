package com.larpclient;

public final class dv {
   public dv.Type_l ChinaHatCodec = dv.Type_l.RECYCLE;
   public String V = "";
   public double M;
   public double A;
   public double N;
   public double E;
   public double j;
   public double Q;
   public double h;
   public double Y;
   public double L;
   public double D;
   public double q;
   public double l;
   public double m;
   public double F = 1.0;
   public double e;
   public double I;
   public double B;
   public double f;
   public double U;
   public double x;
   public int o;
   public boolean T;

   public dv() {
   }

   public void CAsVoid() {
      if (this.i == dv.Type_l.SPAWNING || this.i == dv.Type_l.ACTIVE) {
         this.e = this.m;
         this.l = 0.0;
         this.i = dv.Type_l.DESPAWNING;
      }
   }

   public static enum Type_l {
      SPAWNING,
      ACTIVE,
      DESPAWNING,
      RECYCLE;

      private Type_l() {
      }
   }
}
