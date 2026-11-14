package com.larpclient;

import com.larpclient.module.SelfVisuals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public final class CometRenderer {
   private static final CometOrbit v = new CometOrbit();
   private static final RibbonCapacity.Type_v[] E = new RibbonCapacity.Type_v[3];
   private static final Vec3d[] o = new Vec3d[3];
   private static Object T;
   private static Object N;
   private static Vec3d c;
   private static Vec3d m;
   private static double j;
   private static double H;
   private static double l;
   private static double C;
   private static double z;
   private static double a;
   private static int R;
   private static int q;
   private static boolean P;
   private static boolean F;

   public static void uAsVoid() {
      v.aAsVoid();
      N = null;
      T = null;
      m = null;
      c = null;
      P = false;
   }

   public static void AAsVoid(float var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      SelfVisuals var2 = SelfVisuals.zAsSelfVisuals();
      ClientPlayerEntity var3 = var1.player;
      if (var2 != null && var2.tAsBoolean() && var2.style.pAsBoolean("Comets") && var3 != null && var3.isAlive() && !var3.isInvisible() && !var3.isSpectator()) {
         Vec3d var4 = var3.getEntityPos();
         int var5 = T != var3 || N != var1.world || c != null && c.squaredDistanceTo(var4) > 64.0 ? 1 : 0;
         if (var5 != 0) {
            uAsVoid();
         }

         T = var3;
         N = var1.world;
         c = var4;
         m = var3.getLerpedPos(var0).squaredDistanceTo(var4) > 64.0 ? var4 : var3.getLerpedPos(var0);
         H = (double)var3.getHeight();
         l = CometOrbit.BAsDouble(var2.cometSize.getValue(), H);
         C = var2.trailLength.getValue();
         z = var2.glowIntensity.getValue();
         F = var2.cometBloom.getValue();
         R = var2.themeColors.getValue() ? UiStyle.w : var2.cometColor.getValue();
         q = var2.themeColors.getValue() ? UiStyle.Q : var2.tailColor.getValue();
         j = MediaSession.RAsDouble();
         double var6 = Math.max(var2.orbitRadius.getValue(), (double)var3.getWidth() * 0.71 + l);
         v.VAsVoid(j, var2.orbitSpeed.getValue(), var6, C);
         a = v.yAsDouble(j);
         Vec3d var8 = var1.gameRenderer.getCamera().getCameraPos();

         for (int var9 = 0; var9 < 3; var9++) {
            RibbonCapacity.Type_v var10 = E[var9];
            var10.rAsVoid();
            Vec3d var11 = o[var9] = CometOrbit.dAsVec3d(v.gAsVec3d(var9), m, H, l);

            for (int var12 = 0; var12 <= v.rAsInt(); var12++) {
               Vec3d var13 = var12 == v.rAsInt() ? var11 : CometOrbit.dAsVec3d(v.bAsVec3d(var9, var12), m, H, l);
               double var14 = var12 == v.rAsInt() ? 1.0 : Math.clamp(1.0 - (j - v.wAsDouble(var12)) / C, 0.0, 1.0);
               var10.bAsVoid(var13, l * 1.4 * var14 * var14, ColorUtil.pAsInt(ColorUtil.xAsInt(q, R, var14), var14 * var14 * z * a));
            }

            var10.zAsVoid();
            var10.aAsVoid(var8);
         }

         P = true;
      } else {
         uAsVoid();
      }
   }

   public static boolean zAsBoolean() {
      return P && F;
   }

   public static void iAsVoid(EffectGeometryRenderer var0, boolean var1) {
      if (P && (!var1 || F)) {
         var0.blend(EffectRenderParams.Type_g.ADDITIVE);

         for (int var2 = 0; var2 < 3; var2++) {
            Vec3d var3 = o[var2];
            var0.hAsVoid(E[var2], true);
            var0.cAsVoid(var3.x, var3.y, var3.z, l * 2.2, 0.0, ColorUtil.pAsInt(R, 0.35 * z * a), ParticleEmitterEngine.Type_a.Glow, 0.0, 0.0, 0.0);
            var0.cAsVoid(var3.x, var3.y, var3.z, l, 0.0, ColorUtil.pAsInt(R, 0.9 * a), ParticleEmitterEngine.Type_a.Circle, 0.0, 0.0, 0.0);
            var0.cAsVoid(
               var3.x,
               var3.y,
               var3.z,
               l * 0.55,
               0.0,
               ColorUtil.pAsInt(ColorUtil.xAsInt(R, R & 0xFF000000 | 16777215, 0.8), a),
               ParticleEmitterEngine.Type_a.Glow,
               0.0,
               0.0,
               0.0
            );
         }

         var0.blend(EffectRenderParams.Type_g.ALPHA);
      }
   }

   private CometRenderer() {
   }

   static {
      for (int var0 = 0; var0 < E.length; var0++) {
         E[var0] = new RibbonCapacity.Type_v(257);
      }
   }
}
