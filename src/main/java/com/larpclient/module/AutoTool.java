package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ModuleImportBroker;
import com.larpclient.QueueEntry;
import java.util.ArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;

public final class AutoTool extends Module {
   private static AutoTool n;
   private final BooleanSetting switchBack = this.addSetting(new BooleanSetting("Switch Back", "Restore the previous slot after mining", true));
   private final BooleanSetting protectTools = this.addSetting(new BooleanSetting("Protect Tools", "Avoid tools with only one durability remaining", true));
   private final qe.Type_q L = new qe.Type_q();
   private Object l;
   private Object D;
   private int B = -1;
   private ItemStack Y;

   public AutoTool() {
      super("AutoTool", "Select the best hotbar tool while mining", Category.PLAYER, -1);
      n = this;
   }

   @Override
   public void onEnable() {
      this.L.qAsInt(-1, false);
      this.B = -1;
      this.Y = null;
      this.l = getMc().world;
      this.D = getMc().player;
      ClientLog.GAsVoid("AutoTool", "AutoTool active: hotbar tools, harvestability, Efficiency and durability protection");
   }

   @Override
   public void onDisable() {
      this.ZAsVoid();
   }

   private boolean NAsBoolean() {
      return b_.jAsBoolean()
         && !mc.player.isCreative()
         && !mc.player.isUsingItem()
         && mc.options.attackKey.isPressed()
         && mc.crosshairTarget instanceof BlockHitResult var1
         && var1.getType() == Type.BLOCK;
   }

   @Override
   public void onTick() {
      if (this.l != getMc().world || this.D != getMc().player) {
         this.L.qAsInt(-1, false);
         this.B = -1;
         this.Y = null;
         this.l = mc.world;
         this.D = mc.player;
      }

      if (!this.NAsBoolean()) {
         this.ZAsVoid();
      }
   }

   private void ZAsVoid() {
      if (getMc().world == this.l && mc.player == this.D && mc.player != null) {
         PlayerInventory var1 = mc.player.getInventory();
         var1.setSelectedSlot(this.L.qAsInt(var1.getSelectedSlot(), this.switchBack.getValue() && this.B >= 0 && var1.getStack(this.B) == this.Y));
      } else {
         this.L.qAsInt(-1, false);
      }

      this.B = -1;
      this.Y = null;
   }

   public static boolean UAsBoolean(BlockPos var0) {
      if (n != null && n.isEnabled()) {
         AutoTool var1 = n;
         if (var1 != null && var1.isRuntimeActive() && !b_.EAsBoolean() && var1.NAsBoolean()) {
            if (var1.l == getMc().world && var1.D == getMc().player) {
               if (!(mc.crosshairTarget instanceof BlockHitResult var2) || !var2.getBlockPos().equals(var0) || !mc.world.isChunkLoaded(var0)) {
                  return false;
               }

               if (!mc.player.isBlockBreakingRestricted(mc.world, var0, mc.interactionManager.getCurrentGameMode()) && mc.world.getWorldBorder().contains(var0)
                  )
                {
                  BlockState var8 = mc.world.getBlockState(var0);
                  if (!var8.isAir() && !(var8.getHardness(mc.world, var0) <= 0.0F)) {
                     PlayerInventory var4 = mc.player.getInventory();
                     ArrayList var5 = new ArrayList(9);

                     for (int var6 = 0; var6 < 9; var6++) {
                        var5.add(qe.mAsQe_Type_b(var4.getStack(var6), var8, var1.protectTools.getValue()));
                     }

                     int var9 = var4.getSelectedSlot();
                     if (var1.B == var9 && var4.getStack(var9) != var1.Y) {
                        var1.L.PAsVoid();
                     }

                     int var7 = var1.L.kAsInt(var9, qe.lAsInt(var5, var9));
                     if (var7 != var9) {
                        var4.setSelectedSlot(var7);
                        var1.B = var7;
                        var1.Y = var4.getStack(var7);
                     }

                     return var7 != var9;
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
