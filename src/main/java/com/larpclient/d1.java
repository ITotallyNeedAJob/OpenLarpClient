package com.larpclient;

public final class d1 {
   public int X = 32;
   public double V = 4.0;
   public double i = 14.0;
   public double d = -2.0;
   public double r = 7.0;
   public double D = 1.8;
   public double C = 1.0;
   public double P = 0.3;
   public double G = 5.0;
   public double u = 0.1;
   public double B = 1.0;
   public double j = 3.0;
   public double l = 1.2;
   public double J = 1.2;
   public double k = 24.0;
   public double I = 18.0;
   public double b = 28.0;
   public double Y = 0.9;
   public double A = 0.35;
   public boolean N = true;
   public boolean h = false;
   public boolean H = true;
   public int x = -2109441;
   public int O = -7873035;
   public d1.Type_n z = d1.Type_n.WORLD;
   public d1.Type_m E = d1.Type_m.BILLBOARD;
   public d1.Type_k c = d1.Type_k.UNIFORM;
   public d1.Type_q p = d1.Type_q.CALM;
   public d1.Type_f a = d1.Type_f.NORMAL;
   public d1.Type_l M = d1.Type_l.GRADIENT;

   public d1() {
   }

   public double PAsDouble() {
      return Math.min(this.V, this.i);
   }

   public double aAsDouble() {
      return Math.max(this.V, this.i);
   }

   public double CAsDouble() {
      return Math.min(this.d, this.r);
   }

   public double IAsDouble() {
      return Math.max(this.d, this.r);
   }

   public static enum Type_f {
      NORMAL,
      THROUGH_WALLS,
      SOFT_OCCLUSION;

      private Type_f() {
      }
   }

   public static enum Type_k {
      UNIFORM,
      MOVEMENT_DIRECTION,
      LOOK_DIRECTION;

      private Type_k() {
      }
   }

   public static enum Type_l {
      STATIC,
      GRADIENT,
      RAINBOW;

      private Type_l() {
      }
   }

   public static enum Type_m {
      BILLBOARD,
      FIXED,
      Y_AXIS_BILLBOARD;

      private Type_m() {
      }
   }

   public static enum Type_n {
      WORLD,
      PLAYER;

      private Type_n() {
      }
   }

   public static enum Type_q {
      CALM,
      FLOATING,
      DREAMY,
      CHAOTIC,
      STATIC;

      private Type_q() {
      }
   }
}
