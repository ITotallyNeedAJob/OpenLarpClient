package com.larpclient;

import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class MiningProgress {
   private static NodeRenderer y;
   private static BlockPos W;
   private static Object S;
   private static Object Y;
   private static int e = -1;
   private static int U = -1;
   private static int o;
   private static ItemStack b = ItemStack.EMPTY;
   private static boolean z;

   public static boolean VAsBoolean(BlockPos var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1.world != null
         && var1.player != null
         && var1.world.isChunkLoaded(var0)
         && !var1.world.isOutOfHeightLimit(var0)
         && var1.world.getWorldBorder().contains(var0)) {
         BlockState var2 = var1.world.getBlockState(var0);
         return !var2.isAir() && var2.getFluidState().isEmpty() && var2.getHardness(var1.world, var0) >= 0.0F;
      } else {
         return false;
      }
   }

   public static BlockHitResult PAsBlockHitResult(BlockPos var0, double var1) {
      return BAsBlockHitResult(null, var0, var1);
   }

   static BlockHitResult BAsBlockHitResult(NodeRenderer var0, BlockPos var1, double var2) {
      MinecraftClient var4 = MinecraftClient.getInstance();
      if (!ModuleImportBroker.tAsBoolean(var0)) {
         return null;
      } else {
         Vec3d var5 = var4.player.getEyePos();
         double var6 = Math.min(var2, var4.player.getBlockInteractionRange());
         VoxelShape var8 = var4.world.getBlockState(var1).getOutlineShape(var4.world, var1);
         ArrayList var9 = new ArrayList();
         if (!var8.isEmpty()) {
            var9.add(var8.getBoundingBox().getCenter().add((double)var1.getX(), (double)var1.getY(), (double)var1.getZ()));
         }

         for (Direction var13 : Direction.values()) {
            var9.addAll(BlockPlacementController.HAsList(var1, var13));
         }

         for (Vec3d var15 : (Iterable<Vec3d>) (var9)) {
            Vec3d var16 = var15.subtract(var5);
            if (!(var16.lengthSquared() > var6 * var6)) {
               BlockHitResult var17 = var4.world
                  .raycast(new RaycastContext(var5, var5.add(var16.normalize().multiply(var6)), ShapeType.OUTLINE, FluidHandling.NONE, var4.player));
               if (var17.getType() == Type.BLOCK && var17.getBlockPos().equals(var1)) {
                  return var17;
               }
            }
         }

         return null;
      }
   }

   static boolean NAsBoolean(BlockPos var0, Vec3d var1, double var2) {
      MinecraftClient var4 = MinecraftClient.getInstance();
      Vec3d var5 = var4.player.getEyePos();
      Vec3d var6 = var1.subtract(var5);
      double var7 = Math.min(var2, var4.player.getBlockInteractionRange());
      if (!(var6.lengthSquared() > var7 * var7) && !(var6.lengthSquared() < 1.0E-8)) {
         BlockHitResult var9 = var4.world
            .raycast(new RaycastContext(var5, var5.add(var6.normalize().multiply(var7)), ShapeType.OUTLINE, FluidHandling.NONE, var4.player));
         return var9.getType() == Type.BLOCK && var9.getBlockPos().equals(var0);
      } else {
         return false;
      }
   }

   static BlockHitResult NAsBlockHitResult(BlockPos var0, double var1) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      Vec3d var4 = var3.player.getEyePos();
      BlockHitResult var5 = var3.world
         .raycast(
            new RaycastContext(
               var4,
               var4.add(var3.player.getRotationVector().multiply(Math.min(var1, var3.player.getBlockInteractionRange()))),
               ShapeType.OUTLINE,
               FluidHandling.NONE,
               var3.player
            )
         );
      return var5.getType() == Type.BLOCK && var5.getBlockPos().equals(var0) ? var5 : null;
   }

   static boolean UAsBoolean(NodeRenderer var0, BlockPos var1, double var2, boolean var4, float var5, boolean var6, BooleanSupplier var7) {
      if (var0.fAsBoolean() && VAsBoolean(var1)) {
         MinecraftClient var8 = MinecraftClient.getInstance();
         BlockPos var9 = var1.toImmutable();
         ClientWorld var10 = var8.world;
         ClientPlayerEntity var11 = var8.player;
         int var12 = var8.player.age;
         BlockState var13 = var8.world.getBlockState(var9);
         BooleanSupplier var14 = () -> var0.fAsBoolean()
               && var7.getAsBoolean()
               && BlockPlacementController.rAsBoolean(var10, var8.world, var11, var8.player, var12, var8.player == null ? -10 : var8.player.age)
               && VAsBoolean(var9)
               && var8.world.getBlockState(var9).equals(var13)
               && var8.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(var9)) <= var2 * var2;
         BlockHitResult var15 = var4 ? BAsBlockHitResult(var0, var9, var2) : null;
         if (var4 && var15 == null) {
            cAsVoid(var0);
            return false;
         } else {
            if (var4 && ModuleImportBroker.nAsBoolean(var0)) {
               var0.aimAt(var15.getPos(), var5);
            }

            ModuleImportBroker.UAsVoid(var0, () -> {
               if (var14.getAsBoolean()) {
                  BlockHitResult var9x = var4 ? NAsBlockHitResult(var9, var2) : null;
                  if (!var4 || var9x != null) {
                     if (y != null && y != var0) {
                        cAsVoid(y);
                     }

                     if (y != null && var8.player.getInventory().getSelectedSlot() != U) {
                        cAsVoid(var0);
                     } else {
                        if (y == null) {
                           y = var0;
                           S = var8.world;
                           Y = var8.player;
                           e = var8.player.getInventory().getSelectedSlot();
                        }

                        if (!var9.equals(W)) {
                           if (W != null) {
                              ClientLog.XAsVoid(var0.getName(), "Next block {}, {}, {}", var9.getX(), var9.getY(), var9.getZ());
                           }

                           W = var9;
                           U = var6 ? DAsInt(var13) : var8.player.getInventory().getSelectedSlot();
                           b = var8.player.getInventory().getStack(U);
                        }

                        if (var6) {
                           var8.player.getInventory().setSelectedSlot(U);
                        }

                        o = var8.player.age;
                        Direction var10x = var9x == null ? Direction.UP : var9x.getSide();
                        if (var8.interactionManager.updateBlockBreakingProgress(var9, var10x)) {
                           var8.world.spawnBlockBreakingParticle(var9, var10x);
                           var8.player.swingHand(Hand.MAIN_HAND);
                        }

                        var0.SAsVoid("Mining " + var9.getX() + ", " + var9.getY() + ", " + var9.getZ());
                     }
                  }
               }
            }, () -> var14.getAsBoolean() && (!var4 || NAsBlockHitResult(var9, var2) != null));
            return true;
         }
      } else {
         cAsVoid(var0);
         return false;
      }
   }

   private static int DAsInt(BlockState var0) {
      PlayerInventory var1 = MinecraftClient.getInstance().player.getInventory();
      int var2 = var1.getSelectedSlot();
      float var3 = var1.getStack(var2).getMiningSpeedMultiplier(var0) + (float)(var1.getStack(var2).isSuitableFor(var0) ? 1000 : 0);

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var1.getStack(var4);
         float var6 = var5.getMiningSpeedMultiplier(var0);
         if (var5.isSuitableFor(var0)) {
            var6 += 1000.0F;
         }

         if (var6 > var3) {
            var3 = var6;
            var2 = var4;
         }
      }

      return var2;
   }

   public static boolean ZAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      return !z
         && y != null
         && y.fAsBoolean()
         && var0.world == S
         && var0.player == Y
         && var0.player.age - o <= 1
         && VAsBoolean(W)
         && var0.player.getInventory().getSelectedSlot() == U;
   }

   public static void cAsVoid(Module var0) {
      if (y == var0) {
         MinecraftClient var1 = MinecraftClient.getInstance();

         try {
            z = true;
            if (var1.world == S && var1.player == Y && var1.interactionManager != null) {
               if (!var1.options.attackKey.isPressed()) {
                  var1.interactionManager.cancelBlockBreaking();
               }

               if (!var1.options.attackKey.isPressed()
                  && U != e
                  && var1.player.getInventory().getSelectedSlot() == U
                  && var1.player.getInventory().getStack(U) == b) {
                  var1.player.getInventory().setSelectedSlot(e);
               }
            }
         } finally {
            z = false;
            y = null;
            W = null;
            S = null;
            Y = null;
            e = -1;
            U = -1;
            b = ItemStack.EMPTY;
         }
      }
   }

   private MiningProgress() {
   }
}
