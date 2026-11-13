package com.larpclient;

import net.minecraft.block.ShapeContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class lq {
   public static lq.Type_l AAsLq_Type_l(ClientWorld var0, Vec3d var1, Vec3d var2) {
      if (var0 != null
         && !(var1.squaredDistanceTo(var2) > 64.0)
         && var0.isChunkLoaded(BlockPos.ofFloored(var1))
         && var0.isChunkLoaded(BlockPos.ofFloored(var2))) {
         BlockHitResult var3 = var0.raycast(new RaycastContext(var1, var2, ShapeType.COLLIDER, FluidHandling.NONE, ShapeContext.absent()));
         if (var3.getType() == Type.BLOCK && !var3.isInsideBlock()) {
            Direction var4 = var3.getSide();
            return new lq.Type_l(var3.getPos(), new Vec3d((double)var4.getOffsetX(), (double)var4.getOffsetY(), (double)var4.getOffsetZ()));
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private lq() {
   }

   @FunctionalInterface
   public interface Type_a {
      lq.Type_l HAsLq_Type_l(Vec3d var1, Vec3d var2);
   }

   public static record Type_l(Vec3d point, Vec3d normal) {
   }
}
