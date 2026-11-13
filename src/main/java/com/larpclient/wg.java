package com.larpclient;

import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.util.math.Vec3d;

public final class wg {
   public static final int V = 128;
   private final ParticleEmitterEngine k = new ParticleEmitterEngine();
   private final x4 W = new x4();

   public wg() {
      this.k.B = 128;
      this.W.O = 2048;
   }

   public void VAsVoid(Vec3d var1, Vec3d var2, CinematicQuality var3) {
      this.k.B = Math.min(128, x9.jAsInt(128));
      this.W.M = var3 == CinematicQuality.SUBTLE ? 3 : 5;
      this.W.q = ParticleEmitterEngine.Type_a.Streak;
      this.W.j = x4.Type_g.ALPHA;
      this.W.t = ay.Type_r.SMOOTH;
      this.W.o = ay.Type_z.SHRINK;
      this.W.I = bg.XAsInt(-7026494, 0.75 * var3.n);
      this.W.A = bg.XAsInt(-15260879, 0.05);
      this.W.x = var3.l * 0.7;
      this.W.v = var3.l;
      this.W.g = 0.025;
      this.W.R = 0.055;
      this.W.w = 0.45;
      this.W.Q = 0.6;
      this.W.b = 0.3;
      this.W.a = 0.1;
      this.W.D = 1.5;
      this.W.L = 0.08;
      this.W.Z = var2.x * 0.25;
      this.W.e = var2.y * 0.15;
      this.W.P = var2.z * 0.25;
      this.W.C = 0.0;
      this.W.r = 0.04;
      this.W.S = 1.0;
      this.W.d = 40.0;
      this.W.i = var3.dAsBoolean();
      this.k.pAsVoid(var1, this.W);
      this.W.M = 2;
      this.W.q = ParticleEmitterEngine.Type_a.Spark;
      this.W.I = bg.XAsInt(-3046331, 0.7 * var3.n);
      this.W.A = bg.XAsInt(-6540233, 0.1);
      this.W.g = 0.018;
      this.W.R = 0.03;
      this.W.x = 0.14;
      this.W.v = 0.24;
      this.W.w = 0.65;
      this.k.pAsVoid(var1, this.W);
      this.W.M = 1;
      this.W.q = ParticleEmitterEngine.Type_a.Ring;
      this.W.I = bg.XAsInt(-7026494, 0.2 * var3.n);
      this.W.A = bg.XAsInt(-7026494, 0.0);
      this.W.x = this.W.v = var3.l * 0.8;
      this.W.g = this.W.R = 0.08;
      this.W.p = 0.32;
      this.W.n = 0.012;
      this.W.y = var2.lengthSquared() < 1.0E-8 ? new Vec3d(0.0, 1.0, 0.0) : var2;
      this.W.w = this.W.b = this.W.a = this.W.Z = this.W.e = this.W.P = 0.0;
      this.W.L = this.W.r = 0.0;
      this.W.i = false;
      this.k.pAsVoid(var1, this.W);
   }

   public void EAsVoid() {
      int var1 = x9.jAsInt(128);
      if (this.k.UAsInt() > var1) {
         this.k.JAsVoid();
      }

      this.k.B = var1;
      this.k.IAsVoid(1.0);
   }

   public void RAsVoid(float var1, CameraRenderState var2) {
      this.k.fAsVoid(var2.pos, var1, w3.BAsFrustum());
   }

   public void oAsVoid(EffectGeometryRenderer var1, boolean var2) {
      this.k.MAsVoid(var1, Vec3d.ZERO, 0.0F, var2);
   }

   public boolean SAsBoolean() {
      return this.k.yAsBoolean();
   }

   public boolean gAsBoolean() {
      return this.k.oAsBoolean();
   }

   public int HAsInt() {
      return this.k.UAsInt();
   }

   public void yAsVoid() {
      this.k.JAsVoid();
   }
}
