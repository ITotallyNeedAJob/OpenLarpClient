package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.NumberSetting;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class AutoFarm extends NodeRenderer {
   private final BooleanSetting Q = this.QAsBooleanSetting("Legit", "Look at reachable crops and harvest through the real crosshair", false);
   private final NumberSetting k = this.HAsNumberSetting("Range", "Harvest radius measured from your eyes", 4.0, 1.0, 6.0, 1.0);

   public AutoFarm() {
      super("Auto Farm", "Harvest mature crops around you while walking through a farm");
   }

   private boolean sAsBoolean(BlockPos var1) {
      if (!mc.world.isChunkLoaded(var1)) {
         return false;
      } else {
         BlockState var2 = mc.world.getBlockState(var1);
         if (var2.getBlock() instanceof CropBlock var3 && var3.isMature(var2)) {
            return true;
         }

         return false;
      }
   }

   @Override
   public void onTick() {
      if (!this.fAsBoolean()) {
         MiningProgress.cAsVoid(this);
      } else {
         double var1 = Math.min(this.k.getValue(), mc.player.getBlockInteractionRange());
         int var3 = (int)Math.ceil(var1);
         BlockPos var4 = mc.player.getBlockPos();
         BlockPos var5 = null;
         double var6 = var1 * var1;

         for (BlockPos var9 : BlockPos.iterate(var4.add(-var3, -var3, -var3), var4.add(var3, var3, var3))) {
            if (this.sAsBoolean(var9)) {
               double var10 = mc.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(var9));
               if (!(var10 >= var6) && (!this.Q.getValue() || MiningProgress.PAsBlockHitResult(var9, var1) != null)) {
                  var5 = var9.toImmutable();
                  var6 = var10;
               }
            }
         }

         if (var5 == null) {
            MiningProgress.cAsVoid(this);
            this.SAsVoid("No mature crops in reach");
         } else {
            BlockPos var12 = var5;
            MiningProgress.UAsBoolean(this, var12, var1, this.Q.getValue(), 35.0F, false, () -> this.sAsBoolean(var12));
         }
      }
   }
}
