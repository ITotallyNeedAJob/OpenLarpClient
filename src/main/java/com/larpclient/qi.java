package com.larpclient;

import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class qi implements oi.Type_q {
   private final World Z;
   private final Entity W;

   public qi(World var1, Entity var2) {
      this.Z = var1;
      this.W = var2;
   }

   @Override
   public double clip(Vec3d var1, Vec3d var2) {
      double var3 = var1.distanceTo(var2);
      if (var3 < 1.0E-7) {
         return 1.0;
      } else {
         BlockHitResult var5 = this.Z.raycast(new RaycastContext(var1, var2, ShapeType.COLLIDER, FluidHandling.NONE, this.W));
         double var6 = var5.getType() == Type.MISS ? 1.0 : var1.distanceTo(var5.getPos()) / var3;

         for (Entity var9 : this.Z
            .getOtherEntities(
               this.W, new Box(var1, var2).expand(1.0), var0 -> var0.canBeHitByProjectile() && !(var0 instanceof ProjectileEntity) && !var0.isSpectator()
            )) {
            Optional var10 = var9.getBoundingBox().expand(0.3).raycast(var1, var2);
            if (var10.isPresent()) {
               var6 = Math.min(var6, var1.distanceTo((Vec3d)var10.get()) / var3);
            }
         }

         return var6;
      }
   }

   @Override
   public boolean water(Vec3d var1) {
      return this.Z.getFluidState(BlockPos.ofFloored(var1)).isIn(FluidTags.WATER);
   }
}
