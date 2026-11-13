package com.larpclient;

import com.larpclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class dq {
   private static dq.Type_s f;
   private static Module y;
   private static float t;
   private static float F;

   public static void bAsVoid() {
      f = null;
      y = null;
   }

   public static boolean CAsBoolean(dq.Type_s var0) {
      return f == var0;
   }

   public static boolean YAsBoolean() {
      return f != null;
   }

   public static boolean vAsBoolean(dq.Type_s var0, dq.Type_s var1) {
      return var1 == null || var0.ordinal() > var1.ordinal();
   }

   public static void yAsVoid(dq.Type_s var0, float var1, float var2, double var3) {
      ClientPlayerEntity var5 = MinecraftClient.getInstance().player;
      if (var5 != null) {
         gAsVoid(var0, var1, var2, var3, var5.getYaw(), var5.getPitch());
      }
   }

   public static void gAsVoid(dq.Type_s var0, float var1, float var2, double var3, float var5, float var6) {
      if (var0 == f || vAsBoolean(var0, f)) {
         f = var0;
         y = null;
         float[] var7 = kAsFloatArray(var5, var6, var1, var2, var3);
         t = var7[0];
         F = var7[1];
      }
   }

   public static void RAsVoid(Module var0, dq.Type_s var1, float var2, float var3, double var4) {
      yAsVoid(var1, var2, var3, var4);
      if (f == var1) {
         y = var0;
      }
   }

   public static float[] kAsFloatArray(float var0, float var1, float var2, float var3, double var4) {
      double var6 = var4 <= 0.0 ? 1.0 : Math.clamp(var4, 0.0, 1.0);
      return new float[]{var0 + MathHelper.wrapDegrees(var2 - var0) * (float)var6, MathHelper.clamp(var1 + (var3 - var1) * (float)var6, -90.0F, 90.0F)};
   }

   public static void aAsVoid() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (f != null && b_.tAsBoolean(y) && (y == null || y.isRuntimeActive())) {
         var0.player.setYaw(t);
         var0.player.setPitch(F);
      }
   }

   public static float[] QAsFloatArray(Vec3d var0, Vec3d var1) {
      Vec3d var2 = var1.subtract(var0);
      return new float[]{(float)Math.toDegrees(Math.atan2(var2.z, var2.x)) - 90.0F, (float)(-Math.toDegrees(Math.atan2(var2.y, Math.hypot(var2.x, var2.z))))};
   }

   private dq() {
   }

   public static enum Type_s {
      AUTODRAIN,
      TUNNEL,
      PLACEMENT,
      AURA,
      MACE,
      AUTOCART,
      AUTOCRYSTAL,
      AUTOMLG,
      WINDHOP,
      PEARLCATCH;

      private Type_s() {
      }
   }
}
