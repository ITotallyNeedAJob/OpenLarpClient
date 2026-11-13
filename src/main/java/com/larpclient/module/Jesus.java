package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.r6;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.CollisionView;

public final class Jesus extends Module {
   private static Jesus u;
   private static final VoxelShape[] I = iAsClass_265Array();
   private long b = Long.MIN_VALUE;
   private boolean G;

   public Jesus() {
      super("Jesus", "Walk on water surfaces; hold sneak to swim normally", Category.MOVEMENT, -1);
      u = this;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   @Override
   public void onEnable() {
      this.b = Long.MIN_VALUE;
      this.G = false;
      ClientLog.XAsVoid("Jesus", "Enabled: water surfaces only; sneak and long falls retain normal swimming");
   }

   @Override
   public void onDisable() {
      this.b = Long.MIN_VALUE;
      this.G = false;
      ClientLog.XAsVoid("Jesus", "Released water support; native collision restored");
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      } else if (!this.CAsBoolean()) {
         return "Paused; native water movement";
      } else {
         return this.b != Long.MIN_VALUE && mc.world.getTime() - this.b <= 1L ? "Walking on water" : "Approach the water surface from above";
      }
   }

   private boolean CAsBoolean() {
      if (this.isInGame() && r6.VAsBoolean()) {
         ClientPlayerEntity var1 = mc.player;
         if (!var1.isSpectator()
            && !var1.hasVehicle()
            && !var1.isGliding()
            && !var1.getAbilities().flying
            && !var1.noClip
            && !var1.isSleeping()
            && !var1.hasBlindnessEffect()
            && !var1.isClimbing()
            && !var1.isInLava()
            && !var1.isSneaking()
            && !mc.options.sneakKey.isPressed()
            && !(var1.fallDistance > 3.0)) {
            Fly var2 = Fly.bAsFly();
            AirStuck var3 = AirStuck.zAsAirStuck();
            ElytraMotion var4 = ElytraMotion.vAsElytraMotion();
            return (var2 == null || !var2.vAsBoolean()) && (var3 == null || !var3.wAsBoolean()) && (var4 == null || !var4.ZAsBoolean());
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static VoxelShape hAsVoxelShape(BlockState var0, CollisionView var1, BlockPos var2, EntityShapeContext var3, VoxelShape var4) {
      Jesus var5 = u;
      if (var5 != null && var5.isRuntimeActive()) {
         try {
            if (var1 == getMc().world
               && var3.getEntity() == mc.player
               && var0.getBlock() instanceof FluidBlock
               && var4.isEmpty()
               && !var3.isDescending()
               && var5.CAsBoolean()) {
               FluidState var6 = var0.getFluidState();
               if (var6.isIn(FluidTags.WATER) && !var1.getFluidState(var2.up()).isIn(FluidTags.WATER)) {
                  float var7 = var6.getHeight(var1, var2);
                  if (Float.isFinite(var7) && !(var7 <= 0.0F) && !(var7 > 1.0F)) {
                     int var8 = Math.round(var7 * 9.0F);
                     VoxelShape var9 = (double)Math.abs(var7 - (float)var8 / 9.0F) < 1.0E-6
                        ? I[var8]
                        : VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, (double)var7, 1.0);
                     if (!var3.isAbove(var9, var2, false)) {
                        return var4;
                     } else {
                        var5.b = mc.world.getTime();
                        if (!var5.G) {
                           var5.G = true;
                           ClientLog.XAsVoid("Jesus", "Water support active at native fluid height; no forced motion or extra packets");
                        }

                        return var9;
                     }
                  } else {
                     return var4;
                  }
               } else {
                  return var4;
               }
            } else {
               return var4;
            }
         } catch (RuntimeException var101) {
            ClientLog.lAsVoid("Jesus", "Water collision query failed; disabling and returning native collision", var101);
            var5.disable();
            return var4;
         }
      } else {
         return var4;
      }
   }

   private static VoxelShape[] iAsClass_265Array() {
      VoxelShape[] var0 = new VoxelShape[10];
      var0[0] = VoxelShapes.empty();

      for (int var1 = 1; var1 <= 9; var1++) {
         var0[var1] = VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, (double)((float)var1 / 9.0F), 1.0);
      }

      return var0;
   }
}
