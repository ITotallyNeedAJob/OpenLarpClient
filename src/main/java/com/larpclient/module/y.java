package com.larpclient.module;

import java.util.Optional;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class y {
   private y() {
   }

   public static double sAsDouble(ClientPlayerEntity var0, Box var1) {
      Vec3d var2 = var0.getEyePos();
      Vec3d var3 = new Vec3d(
         MathHelper.clamp(var2.x, var1.minX, var1.maxX), MathHelper.clamp(var2.y, var1.minY, var1.maxY), MathHelper.clamp(var2.z, var1.minZ, var1.maxZ)
      );
      return var3.subtract(var2).length();
   }

   public static boolean vAsBoolean(ClientPlayerEntity var0) {
      return var0 != null && var0.getEntityWorld() != null
         ? !var0.hasStatusEffect(StatusEffects.LEVITATION)
            && !var0.hasStatusEffect(StatusEffects.BLINDNESS)
            && !var0.isTouchingWater()
            && !var0.isInLava()
            && !var0.getAbilities().flying
            && !var0.isGliding()
            && !var0.isClimbing()
            && !var0.hasVehicle()
         : false;
   }

   public static boolean iAsBoolean(ClientPlayerEntity var0) {
      if (!vAsBoolean(var0)) {
         return false;
      } else {
         return lAsBoolean() ? !var0.isOnGround() : var0.fallDistance > 0.0 && !var0.isOnGround();
      }
   }

   public static boolean BAsBoolean(ClientPlayerEntity var0, Box var1, float var2, float var3, double var4, boolean var6) {
      Vec3d var7 = var0.getEyePos();
      Vec3d var8 = lk.MAsVec3d(var2, var3);
      Vec3d var9 = var7.add(var8.multiply(var4 + 1.0));
      double var10 = var4;
      if (!var6) {
         BlockHitResult var12 = var0.getEntityWorld().raycast(new RaycastContext(var7, var9, ShapeType.COLLIDER, FluidHandling.NONE, var0));
         if (var12.getType() != Type.MISS) {
            var10 = var7.distanceTo(var12.getPos());
         }
      }

      Vec3d var13 = var7.add(var8.multiply(var10));
      return var1.raycast(var7, var13).isPresent() || var1.contains(var7);
   }

   public static EntityHitResult hAsEntityHitResult(ClientPlayerEntity var0, float var1, float var2, double var3, boolean var5) {
      if (var0 != null && var0.getEntityWorld() != null && !(var3 <= 0.0)) {
         Vec3d var6 = var0.getEyePos();
         Vec3d var7 = lk.MAsVec3d(var1, var2);
         Vec3d var8 = var6.add(var7.multiply(var3));
         double var9 = var3 * var3;
         if (!var5) {
            BlockHitResult var11 = var0.getEntityWorld().raycast(new RaycastContext(var6, var8, ShapeType.COLLIDER, FluidHandling.NONE, var0));
            if (var11.getType() != Type.MISS) {
               var9 = var6.squaredDistanceTo(var11.getPos());
            }
         }

         Box var12 = var0.getBoundingBox().stretch(var7.multiply(var3)).expand(1.0);
         return ProjectileUtil.raycast(var0, var6, var8, var12, var1x -> var1x != var0 && var1x.canHit() && var1x.isAlive() && !var1x.isSpectator(), var9);
      } else {
         return null;
      }
   }

   public static boolean XAsBoolean(Vec3d var0, double var1, Box var3) {
      ClientPlayerEntity var4 = MinecraftClient.getInstance().player;
      if (var4 == null) {
         return false;
      } else {
         Vec3d var5 = var4.getEyePos();
         if (var3.contains(var5)) {
            return true;
         } else {
            Optional var6 = var3.raycast(var5, var5.add(var0.multiply(var1)));
            return var6.isPresent();
         }
      }
   }

   public static boolean WAsBoolean() {
      ClientPlayerEntity var0 = MinecraftClient.getInstance().player;
      if (var0 != null && var0.isUsingItem()) {
         ItemStack var1 = var0.getActiveItem();
         if (var1.isEmpty()) {
            return false;
         } else if (var1.contains(DataComponentTypes.FOOD)) {
            return true;
         } else {
            UseAction var2 = var1.getUseAction();
            return var2 == UseAction.EAT || var2 == UseAction.DRINK;
         }
      } else {
         return false;
      }
   }

   public static boolean lAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (var0.player != null && var0.world != null) {
         BlockPos var1 = var0.player.getBlockPos();
         if (!var0.world.getBlockState(var1).isOf(Blocks.COBWEB)
            && !var0.world.getBlockState(var1.up()).isOf(Blocks.COBWEB)
            && !var0.world.getBlockState(BlockPos.ofFloored(var0.player.getX(), var0.player.getY() + 0.5, var0.player.getZ())).isOf(Blocks.COBWEB)
            && !var0.world.getBlockState(BlockPos.ofFloored(var0.player.getX(), var0.player.getY() + 1.2F, var0.player.getZ())).isOf(Blocks.COBWEB)) {
            Box var2 = var0.player.getBoundingBox();

            for (int var3 = var1.getX() - 2; var3 <= var1.getX() + 2; var3++) {
               for (int var4 = var1.getY() - 1; var4 <= var1.getY() + 4; var4++) {
                  for (int var5 = var1.getZ() - 2; var5 <= var1.getZ() + 2; var5++) {
                     BlockPos var6 = new BlockPos(var3, var4, var5);
                     if (var2.intersects(new Box(var6)) && var0.world.getBlockState(var6).isOf(Blocks.COBWEB)) {
                        return true;
                     }
                  }
               }
            }

            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public static Vec3d nAsVec3d(Entity var0) {
      Vec3d var1 = MinecraftClient.getInstance().player.getEyePos();
      Box var2 = var0.getBoundingBox();
      return new Vec3d(
         MathHelper.clamp(var1.x, var2.minX, var2.maxX), MathHelper.clamp(var1.y, var2.minY, var2.maxY), MathHelper.clamp(var1.z, var2.minZ, var2.maxZ)
      );
   }
}
