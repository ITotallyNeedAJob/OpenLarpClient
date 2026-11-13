package com.larpclient.module;

import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class s {
   private static int e = Integer.MIN_VALUE;
   private static Vec3d F = Vec3d.ZERO;
   private static Vec3d o = Vec3d.ZERO;

   private s() {
   }

   public static void QAsVoid() {
      e = Integer.MIN_VALUE;
      F = Vec3d.ZERO;
      o = Vec3d.ZERO;
   }

   public static Vec3d kAsVec3d(Box var0) {
      ClientPlayerEntity var1 = MinecraftClient.getInstance().player;
      if (var1 == null) {
         return var0.getCenter();
      } else {
         Vec3d var2 = var1.getEyePos();
         Vec3d var3 = null;
         double var4 = Double.MAX_VALUE;

         for (double var6 = var0.minX; var6 <= var0.maxX; var6 += 0.1F) {
            for (double var8 = var0.minY; var8 <= var0.maxY; var8 += 0.1F) {
               for (double var10 = var0.minZ; var10 <= var0.maxZ; var10 += 0.1F) {
                  Vec3d var12 = new Vec3d(var6, var8, var10);
                  double var13 = var2.distanceTo(var12);
                  if (var13 < var4) {
                     var4 = var13;
                     var3 = var12;
                  }
               }
            }
         }

         return var3 != null ? var3 : var0.getCenter();
      }
   }

   public static Vec3d dAsVec3d(Entity var0, Box var1, double var2) {
      if (MinecraftClient.getInstance().player == null) {
         return var1.getCenter();
      } else {
         double var8 = var1.getLengthX();
         double var10 = var1.getLengthY();
         double var12 = var1.getLengthZ();
         if (e != var0.getId()) {
            e = var0.getId();
            F = new Vec3d(0.0, var10 * 0.55F, 0.0);
            o = new Vec3d((double)mAsFloat(-0.02F, 0.02F), (double)mAsFloat(-0.02F, 0.02F), (double)mAsFloat(-0.02F, 0.02F));
         }

         F = F.add(o);
         double var14 = (var8 - 0.1F) / 2.0;
         double var16 = (var12 - 0.1F) / 2.0;
         if (F.x >= var14) {
            o = new Vec3d((double)(-mAsFloat(0.005F, 0.015F)), o.y, o.z);
         } else if (F.x <= -var14) {
            o = new Vec3d((double)mAsFloat(0.005F, 0.015F), o.y, o.z);
         }

         if (F.y >= var10 * 0.75) {
            o = new Vec3d(o.x, (double)(-mAsFloat(0.0015F, 0.015F)), o.z);
         } else if (F.y <= var10 * 0.3F) {
            o = new Vec3d(o.x, (double)mAsFloat(0.0015F, 0.015F), o.z);
         }

         if (F.z >= var16) {
            o = new Vec3d(o.x, o.y, (double)(-mAsFloat(0.005F, 0.015F)));
         } else if (F.z <= -var16) {
            o = new Vec3d(o.x, o.y, (double)mAsFloat(0.005F, 0.015F));
         }

         F = F.add((double)mAsFloat(-0.05F, 0.05F), 0.0, (double)mAsFloat(-0.05F, 0.05F));
         F = new Vec3d(MathHelper.clamp(F.x, -var14, var14), MathHelper.clamp(F.y, var10 * 0.3F, var10 * 0.75), MathHelper.clamp(F.z, -var16, var16));
         Vec3d var18 = new Vec3d(var1.getCenter().x, var1.minY, var1.getCenter().z);
         Vec3d var19 = var18.add(F);
         if (!y.XAsBoolean(lk.XAsL6(var19).rAsVec3d(), var2, var1)) {
            float var20 = (float)(var8 / 2.0) * 0.8F;

            for (float var21 = -var20; var21 <= var20; var21 += 0.1F) {
               for (float var22 = -var20; var22 <= var20; var22 += 0.1F) {
                  for (float var23 = (float)(var10 * 0.9F); (double)var23 >= var10 * 0.3F; var23 -= 0.1F) {
                     Vec3d var24 = var18.add((double)var21, (double)var23, (double)var22);
                     if (y.XAsBoolean(lk.XAsL6(var24).rAsVec3d(), var2, var1)) {
                        F = new Vec3d((double)var21, (double)var23, (double)var22);
                        return var18.add(F);
                     }
                  }
               }
            }
         }

         return var19;
      }
   }

   public static Vec3d aAsVec3d(Box var0, Vec3d var1, double var2) {
      MinecraftClient var4 = MinecraftClient.getInstance();
      if (var1 != null && var4.player != null && var4.world != null) {
         if (EAsBoolean(var0, var1, var2)) {
            return var1;
         } else {
            Vec3d var5 = null;
            double var6 = Double.MAX_VALUE;

            for (double var8 = var0.minX; var8 <= var0.maxX; var8 += 0.12) {
               for (double var10 = var0.minY; var10 <= var0.maxY; var10 += 0.12) {
                  for (double var12 = var0.minZ; var12 <= var0.maxZ; var12 += 0.12) {
                     Vec3d var14 = new Vec3d(var8, var10, var12);
                     if (EAsBoolean(var0, var14, var2)) {
                        double var15 = var14.squaredDistanceTo(var1);
                        if (var15 < var6) {
                           var6 = var15;
                           var5 = var14;
                        }
                     }
                  }
               }
            }

            return var5 != null ? var5 : var1;
         }
      } else {
         return var1;
      }
   }

   private static boolean EAsBoolean(Box var0, Vec3d var1, double var2) {
      MinecraftClient var4 = MinecraftClient.getInstance();
      if (var4.player == null) {
         return false;
      } else {
         Vec3d var5 = var4.player.getEyePos();
         double var6 = var5.distanceTo(var1);
         if (var6 > var2) {
            return false;
         } else {
            Vec3d var8 = var1.subtract(var5).normalize();
            if (!y.XAsBoolean(var8, var6 + 0.2, var0)) {
               return false;
            } else {
               BlockHitResult var9 = var4.world.raycast(new RaycastContext(var5, var1, ShapeType.COLLIDER, FluidHandling.NONE, var4.player));
               return var9 == null || var9.getType() == Type.MISS || var5.squaredDistanceTo(var9.getPos()) >= var5.squaredDistanceTo(var1) - 1.0E-4;
            }
         }
      }
   }

   private static float mAsFloat(float var0, float var1) {
      return (float)(ThreadLocalRandom.current().nextDouble() * (double)(var1 - var0) + (double)var0);
   }
}
