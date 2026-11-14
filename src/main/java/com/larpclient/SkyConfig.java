package com.larpclient;

public final class SkyConfig {
   public int X = 32;
   public double V = 4.0;
   public double ChinaHatCodec = 14.0;
   public double SahurDanceEngine = -2.0;
   public double r = 7.0;
   public double D = 1.8;
   public double C = 1.0;
   public double P = 0.3;
   public double G = 5.0;
   public double CropHelper = 0.1;
   public double B = 1.0;
   public double TextLayout = 3.0;
   public double DropletTrail = 1.2;
   public double J = 1.2;
   public double GroundSample = 24.0;
   public double I = 18.0;
   public double BloomUtility = 28.0;
   public double Y = 0.9;
   public double A = 0.35;
   public boolean N = true;
   public boolean h = false;
   public boolean H = true;
   public int AuraModule = -2109441;
   public int O = -7873035;
   public SkyConfig.Type_n z = SkyConfig.Type_n.WORLD;
   public SkyConfig.Type_m E = SkyConfig.Type_m.BILLBOARD;
   public SkyConfig.Type_k BoxUtils = SkyConfig.Type_k.UNIFORM;
   public SkyConfig.Type_q HexEdgeloop = SkyConfig.Type_q.CALM;
   public SkyConfig.Type_f PetEffectRenderer = SkyConfig.Type_f.NORMAL;
   public SkyConfig.Type_l M = SkyConfig.Type_l.GRADIENT;

   public SkyConfig() {
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
