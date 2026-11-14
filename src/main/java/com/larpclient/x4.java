package com.larpclient;

import net.minecraft.util.math.Vec3d;

public final class x4 {
   public x4.Type_s E = x4.Type_s.NONE;
   public double h = Double.NaN;
   public Vec3d ScreenScale = new Vec3d(0.0, 1.0, 0.0);
   public x4.Type_i H = x4.Type_i.DEFAULT;
   public x4.Type_o B = x4.Type_o.POINT;
   public ParticleEmitterEngine.Type_a q = ParticleEmitterEngine.Type_a.Glow;
   public x4.Type_g TextLayout = x4.Type_g.ADDITIVE;
   public ay.Type_r SmoothInput = ay.Type_r.SMOOTH;
   public ay.Type_z AuctionNotifier = ay.Type_z.POP;
   public int M = 8;
   public int I = -1;
   public int A = -1;
   public int K;
   public double AuraModule = 0.3;
   public double v = 0.65;
   public double ChatPayParser = 0.04;
   public double R = 0.1;
   public double z = 1.0;
   public double SkyChunkCache = 1.0;
   public double BoxUtils = 1.0;
   public double w = 0.7;
   public double Q = 1.0;
   public double BloomUtility = 0.3;
   public double PetEffectRenderer = 0.4;
   public double D = 1.2;
   public double f;
   public double L = 0.2;
   public double J;
   public double V = 3.0;
   public double Z;
   public double e;
   public double P;
   public double HexEdgeloop = 1.0;
   public double n = 0.035;
   public double N;
   public double C;
   public double r;
   public double S = 5.0;
   public int O = 2048;
   public double SahurDanceEngine = Double.NaN;
   public boolean G;
   public boolean Y;
   public double DropletTrail = 1.0;
   public double T = Double.NaN;
   public boolean ChinaHatCodec = true;

   public x4() {
   }

   public static enum Type_g {
      ALPHA,
      ADDITIVE,
      PREMULTIPLIED_ALPHA;

      private Type_g() {
      }
   }

   public static enum Type_i {
      DEFAULT("effects.png"),
      HITFX("hitfx.png"),
      BLOOM("bloom.png");

      public final String k;

      private Type_i(String var3) {
         this.k = var3;
      }
   }

   public static enum Type_o {
      POINT,
      BOX,
      SPHERE,
      CYLINDER,
      RING;

      private Type_o() {
      }
   }

   public static enum Type_s {
      NONE,
      BOUNCE,
      SETTLE;

      private Type_s() {
      }
   }
}
