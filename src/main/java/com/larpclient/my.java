package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class my {
   public static final double M = 5.0;

   public static float CAsFloat(LivingEntity var0, BlockPos var1, BlockPos var2) {
      if (var0 instanceof PlayerEntity var3 && var3.getAbilities().invulnerable) {
         return 0.0F;
      }

      return pAsFloat(var0, var1, LAsFloat(var0, var1, var2));
   }

   public static float pAsFloat(LivingEntity var0, BlockPos var1, float var2) {
      if (var0 instanceof PlayerEntity var3 && var3.getAbilities().invulnerable) {
         return 0.0F;
      }

      double var5 = var0.getEntityPos().distanceTo(Vec3d.ofCenter(var1));
      return var5 > 10.0 ? 0.0F : nf.DAsFloat(var0, nf.sAsFloat(var5, (double)var2, 5.0));
   }

   public static float LAsFloat(LivingEntity var0, BlockPos var1, BlockPos var2) {
      Vec3d var3 = Vec3d.ofCenter(var1);
      if (var0.getEntityPos().distanceTo(var3) > 10.0) {
         return 0.0F;
      } else {
         World var4 = var0.getEntityWorld();
         return FAsFloat(
            var3,
            var0.getBoundingBox(),
            var1,
            var2,
            (var2x, var3x) -> var4.raycast(new RaycastContext(var2x, var3x, ShapeType.COLLIDER, FluidHandling.NONE, var0))
         );
      }
   }

   public static List<BlockPos> fAsList(BlockPos var0, Vec3d var1) {
      Vec3d var2 = var1.subtract(Vec3d.ofCenter(var0));
      ArrayList var3 = new ArrayList(26);

      for (int var4 = -1; var4 <= 1; var4++) {
         for (int var5 = -1; var5 <= 1; var5++) {
            for (int var6 = -1; var6 <= 1; var6++) {
               if ((var4 != 0 || var5 != 0 || var6 != 0) && (double)var4 * var2.x + (double)var5 * var2.y + (double)var6 * var2.z > 0.0) {
                  var3.add(var0.add(var4, var5, var6));
               }
            }
         }
      }

      return var3;
   }

   public static float FAsFloat(Vec3d var0, Box var1, BlockPos var2, BlockPos var3, BiFunction<Vec3d, Vec3d, BlockHitResult> var4) {
      double var5 = 1.0 / ((var1.maxX - var1.minX) * 2.0 + 1.0);
      double var7 = 1.0 / ((var1.maxY - var1.minY) * 2.0 + 1.0);
      double var9 = 1.0 / ((var1.maxZ - var1.minZ) * 2.0 + 1.0);
      if (!(var5 < 0.0) && !(var7 < 0.0) && !(var9 < 0.0)) {
         double var11 = (1.0 - Math.floor(1.0 / var5) * var5) / 2.0;
         double var13 = (1.0 - Math.floor(1.0 / var9) * var9) / 2.0;
         Box var15 = var3 == null ? null : new Box(var3);
         int var16 = 0;
         int var17 = 0;

         for (double var18 = 0.0; var18 <= 1.0; var18 += var5) {
            for (double var20 = 0.0; var20 <= 1.0; var20 += var7) {
               for (double var22 = 0.0; var22 <= 1.0; var22 += var9) {
                  Vec3d var24 = new Vec3d(
                     MathHelper.lerp(var18, var1.minX, var1.maxX) + var11,
                     MathHelper.lerp(var20, var1.minY, var1.maxY),
                     MathHelper.lerp(var22, var1.minZ, var1.maxZ) + var13
                  );
                  BlockHitResult var25 = var4.apply(var24, var0);
                  int var26 = var25.getType() == Type.MISS ? 1 : 0;
                  if ((var26 != 0 || var25.getBlockPos().equals(var2))
                     && (var15 == null || !var15.contains(var24) && var15.raycast(var24, var26 != 0 ? var0 : var25.getPos()).isEmpty())) {
                     var16++;
                  }

                  var17++;
               }
            }
         }

         return (float)var16 / (float)var17;
      } else {
         return 0.0F;
      }
   }

   private my() {
   }
}
