package com.larpclient.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class lk {
   private lk() {
   }

   public static float TAsFloat() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (var0 != null && var0.options != null && var0.options.getMouseSensitivity() != null) {
         double var1 = (Double)var0.options.getMouseSensitivity().getValue();
         double var3 = var1 * 0.6F + 0.2F;
         return (float)(var3 * var3 * var3 * 1.2F);
      } else {
         return 0.00405F;
      }
   }

   public static float WAsFloat(float var0, float var1) {
      float var2 = var1 - var0;

      while (var2 > 180.0F) {
         var2 -= 360.0F;
      }

      while (var2 < -180.0F) {
         var2 += 360.0F;
      }

      return var2;
   }

   public static float aAsFloat(float var0, float var1) {
      return var0 + WAsFloat(var0, var1);
   }

   public static RotationAngles XAsL6(Vec3d var0) {
      ClientPlayerEntity var1 = MinecraftClient.getInstance().player;
      double var2 = var0.x - var1.getX();
      double var4 = var0.y - (var1.getY() + (double)var1.getEyeHeight(var1.getPose()));
      double var6 = var0.z - var1.getZ();
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      float var10 = (float)Math.toDegrees(Math.atan2(var6, var2)) - 90.0F;
      float var11 = (float)(-Math.toDegrees(Math.atan2(var4, var8)));
      return new RotationAngles(var10, var11);
   }

   public static RotationAngles UAsL6(RotationAngles var0, RotationAngles var1) {
      float var2 = TAsFloat();
      if (!(var2 <= 1.0E-6F) && Float.isFinite(var2)) {
         float var3 = WAsFloat(var0.n, var1.n);
         float var4 = var1.m - var0.m;
         int var5 = Math.round(var3 / var2);
         int var6 = Math.round(var4 / var2);
         if (var5 == 0 && Math.abs(var3) >= var2 * 0.25F && Math.abs(var3) > 0.001F) {
            var5 = (int)Math.signum(var3);
         }

         if (var6 == 0 && Math.abs(var4) >= var2 * 0.25F && Math.abs(var4) > 0.001F) {
            var6 = (int)Math.signum(var4);
         }

         return new RotationAngles(var0.n + (float)var5 * var2, MathHelper.clamp(var0.m + (float)var6 * var2, -90.0F, 90.0F));
      } else {
         return var1;
      }
   }

   public static float tAsFloat(float var0, float var1) {
      float var2 = TAsFloat();
      return !(var2 <= 1.0E-6F) && Float.isFinite(var2) ? var0 + (float)Math.round((var1 - var0) / var2) * var2 : var1;
   }

   public static Vec3d MAsVec3d(float var0, float var1) {
      float var2 = var1 * (float) (Math.PI / 180.0);
      float var3 = -var0 * (float) (Math.PI / 180.0);
      float var4 = MathHelper.cos((double)var3);
      float var5 = MathHelper.sin((double)var3);
      float var6 = MathHelper.cos((double)var2);
      float var7 = MathHelper.sin((double)var2);
      return new Vec3d((double)(var5 * var6), (double)(-var7), (double)(var4 * var6));
   }
}
