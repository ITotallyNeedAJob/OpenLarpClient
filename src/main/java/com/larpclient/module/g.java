package com.larpclient.module;

import com.larpclient.ActionOwnership;
import com.larpclient.bd;
import com.larpclient.bg;
import java.util.Optional;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

public final class g {
   private static final int d = 64;
   private static final float o = 3.5F;
   private static final double h = 0.06;
   private static final double e = 0.07;
   private static Object Q;
   private static long s = Long.MIN_VALUE;
   private static double z;
   private static double X = Double.NaN;

   public static void hAsVoid() {
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register(g::QAsVoid);
   }

   private static void QAsVoid(WorldRenderContext var0) {
      Mace var1 = Mace.yAsMace();
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var1 != null && var1.isRuntimeActive() && var2.player != null && var2.world != null && var2.currentScreen == null && !var2.options.hudHidden) {
         float var3 = var2.getRenderTickCounter().getTickProgress(false);
         ZAsVoid(var1, var2, var3);
         PAsVoid(var1, var2.player, var3);
      }
   }

   private static void ZAsVoid(Mace var0, MinecraftClient var1, float var2) {
      ClientPlayerEntity var3 = var1.player;
      long var4 = var1.world.getTime();
      if (Q != var1.world || s != var4) {
         Q = var1.world;
         s = var4;
         z = var0.YAsDouble(var3);
         X = z > 0.0 ? qAsDouble(var1, var3, z) : Double.NaN;
      }

      WAsVoid(var3, z, X, var2);
   }

   private static void WAsVoid(ClientPlayerEntity var0, double var1, double var3, float var5) {
      if (!(var1 <= 0.0) && !Double.isNaN(var3)) {
         Vec3d var6 = var0.getLerpedPos(var5);
         double var7 = ActionOwnership.UAsDouble(var1, var6.y + (double)var0.getStandingEyeHeight(), var3);
         if (!(var7 <= 0.0)) {
            Vec3d var9 = new Vec3d(var6.x, var3 + 0.06, var6.z);
            int var10 = bg.xAsInt(bd.w, -1, 0.3) | 0xFF000000;

            for (int var11 = 0; var11 < 64; var11++) {
               GizmoDrawing.line(JAsVec3d(var9, var11, var7), JAsVec3d(var9, var11 + 1, var7), var10, 3.5F);
            }
         }
      }
   }

   private static double qAsDouble(MinecraftClient var0, ClientPlayerEntity var1, double var2) {
      Vec3d var4 = var1.getEntityPos().add(0.0, 0.05, 0.0);
      Vec3d var5 = var1.getEyePos().subtract(0.0, 1.8 + var2, 0.0);
      BlockHitResult var6 = var0.world.raycast(new RaycastContext(var4, var5, ShapeType.COLLIDER, FluidHandling.NONE, var1));
      return var6.getType() == Type.BLOCK ? var6.getPos().y : Double.NaN;
   }

   private static void PAsVoid(Mace var0, ClientPlayerEntity var1, float var2) {
      LivingEntity var3 = var0.JAsLivingEntity();
      if (var3 != null) {
         OAsVoid(var1, var3, var2);
      }
   }

   private static void OAsVoid(ClientPlayerEntity var0, LivingEntity var1, float var2) {
      Box var3 = var1.getBoundingBox().offset(var1.getLerpedPos(var2).subtract(var1.getEntityPos()));
      Vec3d var4 = var0.getCameraPosVec(var2);
      Vec3d var5 = var0.getRotationVec(var2);
      Vec3d var6 = var4.add(var5.multiply(var4.distanceTo(var3.getCenter()) + var3.getAverageSideLength()));
      Optional<Vec3d> var7 = var3.expand((double)var1.getTargetingMargin()).raycast(var4, var6);
      int var8 = (var7.isPresent() ? bg.xAsInt(bd.w, -8327246, 0.35) : bg.xAsInt(bd.w, -12672, 0.5)) | 0xFF000000;
      GizmoDrawing.box(var3, DrawStyle.stroked(var8, 2.0F)).ignoreOcclusion();
      Vec3d var9 = var7.orElseGet(() -> dAsVec3d(var4, var5, var3.getCenter()));
      GizmoDrawing.box(new Box(var9, var9).expand(0.07), DrawStyle.filled(var8)).ignoreOcclusion();
   }

   private static Vec3d dAsVec3d(Vec3d var0, Vec3d var1, Vec3d var2) {
      return var0.add(var1.multiply(Math.max(0.0, var2.subtract(var0).dotProduct(var1))));
   }

   private static Vec3d JAsVec3d(Vec3d var0, int var1, double var2) {
      double var4 = (Math.PI * 2) * (double)var1 / 64.0;
      return var0.add(Math.cos(var4) * var2, 0.0, Math.sin(var4) * var2);
   }

   private g() {
   }
}
