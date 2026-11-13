package com.larpclient;

import java.util.Random;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.util.math.Vec3d;

public final class HalloweenCinders {
   private final ParticleEmitterEngine C = new ParticleEmitterEngine();
   private final x4 g = new x4();
   private final Random o = new Random(184360998L);
   private double a;
   private double y;
   private double h;

   public HalloweenCinders() {
      this.g.O = 2048;
      this.g.M = 1;
   }

   public void YAsVoid(Vec3d var1, CinematicQuality var2, boolean var3) {
      this.h = this.y;
      this.y = Math.clamp(this.y + (var3 ? 0.1 : -0.1), 0.0, 1.0);
      this.C.B = var2.HAsInt();
      if (this.C.UAsInt() > this.C.B) {
         this.C.JAsVoid();
      }

      this.C.IAsVoid(1.0);
      if (!var3) {
         this.a = 0.0;
      } else {
         this.a = this.a + var2.S * 0.05;
         int var4 = (int)this.a;
         this.a -= (double)var4;

         for (int var5 = 0; var5 < var4 && this.C.UAsInt() < this.C.B; var5++) {
            int var6 = this.o.nextInt(7) == 0 ? 1 : 0;
            this.g.q = var6 != 0 ? ParticleEmitterEngine.Type_a.Glow : ParticleEmitterEngine.Type_a.Shard;
            this.g.j = x4.Type_g.ALPHA;
            this.g.I = bg.XAsInt(var6 != 0 ? -3046331 : -6511188, (var6 != 0 ? 0.65 : 0.3) * var2.n);
            this.g.A = bg.XAsInt(var6 != 0 ? -6540233 : -15260879, 0.08);
            this.g.i = var6 != 0 && var2.dAsBoolean();
            this.g.x = 9.0;
            this.g.v = 12.0;
            this.g.g = var6 != 0 ? 0.013 : 0.014;
            this.g.R = var6 != 0 ? 0.029 : 0.04;
            this.g.t = ay.Type_r.FADE_IN_OUT;
            this.g.o = ay.Type_z.PULSE;
            this.g.a = 0.0;
            this.g.D = 0.15;
            this.g.w = 0.045;
            this.g.Q = 0.5;
            this.g.b = -0.12;
            this.g.Z = 0.14;
            this.g.P = 0.06;
            this.g.L = 0.0;
            this.g.V = 0.7;
            this.g.C = 0.13;
            this.g.r = 0.28;
            this.g.S = 6.5;
            this.g.d = var2.T * 1.45;
            this.C.pAsVoid(b9.iAsVec3d(var1.add(0.0, 2.0, 0.0), var2.T, 8.0, this.o), this.g);
         }

         op.YAsVoid("Halloween cinders", (long)this.C.UAsInt());
      }
   }

   public void pAsVoid(float var1, CameraRenderState var2) {
      this.C.nAsVoid(var2.pos, var1, w3.BAsFrustum(), 0.65, ay.RAsDouble(this.h + (this.y - this.h) * (double)var1));
   }

   public void cAsVoid(EffectGeometryRenderer var1, boolean var2) {
      this.C.MAsVoid(var1, Vec3d.ZERO, 0.0F, var2);
   }

   public boolean eAsBoolean() {
      return this.C.yAsBoolean();
   }

   public boolean LAsBoolean() {
      return this.C.oAsBoolean();
   }

   public int BAsInt() {
      return this.C.UAsInt();
   }

   public void jAsVoid() {
      this.C.JAsVoid();
      this.a = this.y = this.h = 0.0;
   }
}
