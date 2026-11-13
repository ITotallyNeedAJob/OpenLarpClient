package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.NumberSetting;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public final class AirPlace extends nd {
   private final BooleanSetting n = this.QAsBooleanSetting("Legit", "Repeat normal crosshair placement at normal use speed", false);
   private final NumberSetting l = this.HAsNumberSetting("Range", "Distance along your view to search for a supported position", 4.5, 1.0, 6.0, 0.5);
   private final NumberSetting N = this.HAsNumberSetting("Delay", "Ticks between placement attempts", 4.0, 0.0, 20.0, 1.0);
   private int C;

   public AirPlace() {
      super("Air Place", "Place held blocks along your crosshair using nearby support faces");
   }

   @Override
   public void onTick() {
      if (!this.fAsBoolean() || !mc.options.useKey.isPressed()) {
         BlockPlacementController.WAsVoid(this);
      } else if (mc.player.age >= this.C && mc.player.getMainHandStack().getItem() instanceof BlockItem) {
         if (this.n.getValue()) {
            if (mc.crosshairTarget instanceof BlockHitResult var10 && var10.getType() == Type.BLOCK) {
               BlockPos var14 = var10.getBlockPos().offset(var10.getSide());
               BlockPlacementController.sAsBoolean(
                  this,
                  var14,
                  mc.player.getMainHandStack().getItem(),
                  this.l.getValue(),
                  false,
                  false,
                  () -> this.fAsBoolean() && mc.options.useKey.isPressed(),
                  var1x -> {
                     this.C = mc.player.age + Math.max(4, this.N.getValue().intValue());
                     this.SAsVoid(var1x ? "Placed" : "Placement rejected");
                  }
               );
            }
         } else {
            BlockPos var1 = null;
            if (mc.crosshairTarget instanceof BlockHitResult var2 && var2.getType() == Type.BLOCK) {
               BlockPos var15 = var2.getBlockPos().offset(var2.getSide());
               if (BlockPlacementController.jAsBoolean(var15)) {
                  var1 = var15;
               }
            }

            if (var1 == null) {
               Vec3d var11 = mc.player.getEyePos();
               Vec3d var16 = mc.player.getRotationVector();
               BlockPos var4 = null;
               double var5 = Math.min(this.l.getValue(), mc.player.getBlockInteractionRange());

               for (double var7 = 0.5; var7 <= var5; var7 += 0.25) {
                  BlockPos var9 = BlockPos.ofFloored(var11.add(var16.multiply(var7)));
                  if (!var9.equals(var4)) {
                     var4 = var9;
                     if (this.gAsBlockHitResult(var9) != null) {
                        var1 = var9;
                        break;
                     }
                  }
               }
            }

            if (var1 == null) {
               this.SAsVoid("No supported position along view");
            } else {
               BlockPos var12 = var1.toImmutable();
               Item var17 = mc.player.getMainHandStack().getItem();
               ClientWorld var18 = mc.world;
               ClientPlayerEntity var19 = mc.player;
               int var6 = mc.player.age;
               b_.UAsVoid(
                  this,
                  () -> {
                     BlockHitResult var2x = this.gAsBlockHitResult(var12);
                     if (var2x != null) {
                        boolean var3 = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, var2x).isAccepted();
                        if (var3) {
                           mc.player.swingHand(Hand.MAIN_HAND);
                        }

                        this.C = mc.player.age + Math.max(1, this.N.getValue().intValue());
                        this.SAsVoid(var3 ? "Placed" : "Server or placement context rejected action");
                     }
                  },
                  () -> this.fAsBoolean()
                        && mc.options.useKey.isPressed()
                        && mc.player.getMainHandStack().isOf(var17)
                        && BlockPlacementController.rAsBoolean(var18, mc.world, var19, mc.player, var6, mc.player.age)
                        && var12.equals(this.XAsBlockPos(var12))
                        && this.gAsBlockHitResult(var12) != null
               );
            }
         }
      }
   }

   private BlockPos XAsBlockPos(BlockPos var1) {
      if (mc.crosshairTarget instanceof BlockHitResult var2 && var2.getType() == Type.BLOCK && var2.getBlockPos().offset(var2.getSide()).equals(var1)) {
         return var1;
      }

      Vec3d var5 = mc.player.getEyePos();

      for (double var6 = 0.5; var6 <= Math.min(this.l.getValue(), mc.player.getBlockInteractionRange()); var6 += 0.25) {
         if (BlockPos.ofFloored(var5.add(mc.player.getRotationVector().multiply(var6))).equals(var1)) {
            return var1;
         }
      }

      return null;
   }

   private BlockHitResult gAsBlockHitResult(BlockPos var1) {
      if (BlockPlacementController.jAsBoolean(var1) && mc.player.getMainHandStack().getItem() instanceof BlockItem var2) {
         for (Direction var6 : Direction.values()) {
            BlockPos var7 = var1.offset(var6);
            if (mc.world.isChunkLoaded(var7)) {
               BlockState var8 = mc.world.getBlockState(var7);
               if (!var8.isReplaceable() && !var8.getCollisionShape(mc.world, var7).isEmpty()) {
                  Direction var9 = var6.getOpposite();
                  Vec3d var10 = Vec3d.ofCenter(var7).add((double)var9.getOffsetX() * 0.5, (double)var9.getOffsetY() * 0.5, (double)var9.getOffsetZ() * 0.5);
                  double var11 = Math.min(this.l.getValue(), mc.player.getBlockInteractionRange());
                  if (!(mc.player.getEyePos().squaredDistanceTo(var10) > var11 * var11)) {
                     BlockHitResult var13 = new BlockHitResult(var10, var9, var7, false);
                     ItemPlacementContext var14 = new ItemPlacementContext(mc.player, Hand.MAIN_HAND, mc.player.getMainHandStack(), var13);
                     if (var14.canPlace() && var14.getBlockPos().equals(var1)) {
                        BlockState var15 = var2.getBlock().getPlacementState(var14);
                        if (var15 != null && var15.canPlaceAt(mc.world, var1) && mc.world.canPlace(var15, var1, ShapeContext.ofPlacement(mc.player))) {
                           return var13;
                        }
                     }
                  }
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.C = 0;
   }

   @Override
   public void onDisable() {
      super.onDisable();
      BlockPlacementController.WAsVoid(this);
      this.C = 0;
   }
}
