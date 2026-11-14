package com.larpclient.module;

import com.larpclient.EffectGeometryRenderer;
import com.larpclient.ParticleEmitterEngine;
import com.larpclient.UiStyle;
import com.larpclient.ColorUtil;
import com.larpclient.MediaSession;
import com.larpclient.NebulaGenerator;
import com.larpclient.EffectRenderParams;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.VisibilityConfigurable;

public final class AttackRaycast {
   private static Object A;
   private static int D;
   private static long m = Long.MIN_VALUE;
   private static Box d;
   private static double R;
   private static double J;
   private static boolean O;
   private static ng.Type_e G;
   private static int v;
   private static Vec3d F;

   public static void kAsVoid() {
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register(AttackRaycast::JAsVoid);
   }

   private static void JAsVoid(WorldRenderContext var0) {
      Aura var1 = Aura.fAsAura();
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var1 != null && var1.isOperational() && var2.player != null && var2.currentScreen == null && !var2.options.hudHidden) {
         float var3 = var2.getRenderTickCounter().getTickProgress(false);
         if (var1.XAsBoolean()) {
            Vec3d var4 = var2.player.getLerpedPos(var3).add(0.0, 0.12, 0.0);
            double var5 = (double)var2.player.getYaw() - var1.fOV() / 2.0;
            int var7 = Math.max(8, (int)Math.ceil(var1.fOV() / 6.0));

            for (int var8 = 0; var8 < var7; var8++) {
               pAsVoid(
                  var1,
                  GAsVec3d(var4, var5 + var1.fOV() * (double)var8 / (double)var7, var1.uAsDouble()),
                  GAsVec3d(var4, var5 + var1.fOV() * (double)(var8 + 1) / (double)var7, var1.uAsDouble()),
                  bg.xAsInt(bd.w, -1, 0.3),
                  var1.indicatorWidth()
               );
            }

            if (var1.fOV() < 360.0) {
               pAsVoid(var1, var4, GAsVec3d(var4, var5, var1.uAsDouble()), bg.xAsInt(bd.w, -1, 0.3), var1.indicatorWidth());
               pAsVoid(var1, var4, GAsVec3d(var4, var5 + var1.fOV(), var1.uAsDouble()), bg.xAsInt(bd.w, -1, 0.3), var1.indicatorWidth());
            }
         }
      }
   }

   public static void ZAsVoid() {
      Aura var0 = Aura.fAsAura();
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var0 != null && var0.qAsBoolean() && var1.world != null && var1.player != null && var1.currentScreen == null && !var1.options.hudHidden) {
         xAsVoid(var0, var1);
         String var2 = var0.SAsString();
         int var3 = var2.equals("Valid attack ray")
            ? bg.xAsInt(bd.w, -8327246, 0.35)
            : (var2.equals("Outside reach") ? bg.xAsInt(bd.w, -1277295, 0.55) : bg.xAsInt(bd.w, -12672, 0.5));
         v = v == 0 ? var3 : bg.xAsInt(v, var3, 1.0 - Math.exp(-mb.YAsDouble() * 9.0));
      } else {
         G = null;
         A = null;
         v = 0;
      }
   }

   private static void xAsVoid(Aura var0, MinecraftClient var1) {
      LivingEntity var2 = var0.hAsLivingEntity();
      Box var3 = var2.getBoundingBox();
      double var4 = var0.uAsDouble();
      double var6 = (double)var1.player.getStandingEyeHeight();
      long var8 = var1.world.getTime();
      if (G == null || A != var1.world || D != var2.getId() || m != var8 || !var3.equals(d) || R != var4 || J != var6 || O != var0.zoneFill()) {
         HashMap<Long, List<Box>> var10 = new HashMap<>();
         double var11 = var2.getY();
         ShapeContext var13 = ShapeContext.of(var1.player);
         ng.Type_u var14 = (var5, var7) -> {
            int var9 = (int)Math.floor(var5);
            int var10x = (int)Math.floor(var7);
            long var11x = BlockPos.asLong(var9, 0, var10x);
            List var13x = var10.computeIfAbsent(var11x, var6x -> {
               ArrayList var7x = new ArrayList();

               for (int var8x = (int)Math.floor(var11 - 2.5); (double)var8x <= Math.ceil(var11 + 3.1); var8x++) {
                  BlockPos var9x = new BlockPos(var9, var8x, var10x);
                  if (var1.world.isChunkLoaded(var9x)) {
                     for (Box var11xx : var1.world.getBlockState(var9x).getCollisionShape(var1.world, var9x, var13).getBoundingBoxes()) {
                        var7x.add(var11xx.offset((double)var9, (double)var8x, (double)var10x));
                     }
                  }
               }

               return var7x;
            });
            return ng.XAsNg_Type_n(var13x, var5, var7, var11);
         };
         G = ng.HAsNg_Type_e(var3, var4, var6, var14, var0.zoneFill());
         if (A != var1.world || D != var2.getId()) {
            v = 0;
         }

         A = var1.world;
         D = var2.getId();
         m = var8;
         d = var3;
         R = var4;
         J = var6;
         O = var0.zoneFill();
      }

      F = var3.getCenter();
   }

   public static boolean YAsBoolean() {
      Aura var0 = Aura.fAsAura();
      return G != null && !G.outline().isEmpty() && var0 != null && var0.zoneBloom();
   }

   public static void yAsVoid(EffectGeometryRenderer var0, boolean var1) {
      Aura var2 = Aura.fAsAura();
      if (G != null && var2 != null && (!var1 || var2.zoneBloom())) {
         double var3 = var2.zoneOpacity() * (var1 ? 0.65 : 1.0);
         double var5 = (double)var2.zoneWidth() * 0.035;
         double var7 = var2.zoneMotion() ? mb.RAsDouble() : 0.0;
         var0.blend(x4.Type_g.ADDITIVE);

         for (ng.Type_p var10 : G.outline()) {
            Vec3d var11 = var10.from();
            Vec3d var12 = var10.to();
            int var13 = bAsInt(var11, var7, var3, var2.zoneMotion());
            int var14 = bAsInt(var12, var7, var3, var2.zoneMotion());
            var0.zAsVoid(var11.x, var11.y, var11.z, var12.x, var12.y, var12.z, var5, var5, var13, var14);
         }

         if (var2.zoneMotion()) {
            for (int var42 = 0; var42 < 3; var42++) {
               double var44 = var7 * 0.55 + (double)var42 * Math.PI * 2.0 / 3.0;
               double var47 = Math.cos(var44);
               double var48 = Math.sin(var44);
               double var16 = Double.POSITIVE_INFINITY;
               Vec3d var18 = null;

               for (ng.Type_p var20 : G.outline()) {
                  Vec3d var21 = var20.from();
                  Vec3d var22 = var20.to();
                  double var23 = var21.x - F.x;
                  double var25 = var21.z - F.z;
                  double var27 = var22.x - var21.x;
                  double var29 = var22.z - var21.z;
                  double var31 = var27 * var48 - var29 * var47;
                  double var33 = Math.abs(var31) < 1.0E-8 ? 0.5 : Math.clamp((var25 * var47 - var23 * var48) / var31, 0.0, 1.0);
                  Vec3d var35 = var21.lerp(var22, var33);
                  double var36 = var35.x - F.x;
                  double var38 = var35.z - F.z;
                  if (!(var36 * var47 + var38 * var48 <= 0.0)) {
                     double var40 = Math.abs(var36 * var48 - var38 * var47);
                     if (var40 < var16) {
                        var16 = var40;
                        var18 = var35;
                     }
                  }
               }

               if (var18 != null && var16 < 0.08) {
                  int var49 = bg.pAsInt(bg.xAsInt(v, -1, 0.65), var3);
                  var0.cAsVoid(
                     var18.x, var18.y + 0.018, var18.z, var5 * 1.6, var7 * 0.6, bg.pAsInt(var49, 0.45), ParticleEmitterEngine.Type_a.Glow, 0.0, 0.0, 0.0
                  );
                  var0.BAsVoid(var18.x, var18.y + 0.018, var18.z, var5 * 0.6, var7 * 0.6, var49);
               }
            }
         }

         var0.blend(x4.Type_g.ALPHA);
         if (!var1) {
            int var43 = bg.pAsInt(v, var3 * 0.065);

            for (ng.Type_d var46 : G.fill()) {
               cAsVoid(var0, var46.a(), var43);
               cAsVoid(var0, var46.b(), var43);
               cAsVoid(var0, var46.c(), var43);
               cAsVoid(var0, var46.c(), var43);
            }
         }
      }
   }

   private static void cAsVoid(EffectGeometryRenderer var0, Vec3d var1, int var2) {
      var0.vertex(var1.x, var1.y, var1.z, var2);
   }

   private static int bAsInt(Vec3d var0, double var1, double var3, boolean var5) {
      double var6 = Math.atan2(var0.z - F.z, var0.x - F.x);
      double var8 = var5 ? Math.pow(0.5 + 0.5 * Math.cos(var6 * 3.0 - var1 * 1.65), 12.0) : 0.0;
      return bg.pAsInt(bg.xAsInt(v, -1, 0.15 + var8 * 0.55), var3 * (0.65 + var8 * 0.35));
   }

   private static void pAsVoid(Aura var0, Vec3d var1, Vec3d var2, int var3, float var4) {
      VisibilityConfigurable var5 = GizmoDrawing.line(var1, var2, var3 | 0xFF000000, var4);
      if (var0.visibleLines()) {
         var5.ignoreOcclusion();
      }
   }

   private static Vec3d GAsVec3d(Vec3d var0, double var1, double var3) {
      double var5 = Math.toRadians(var1);
      return var0.add(-Math.sin(var5) * var3, 0.0, Math.cos(var5) * var3);
   }

   private AttackRaycast() {
   }
}
