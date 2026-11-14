package com.larpclient.module;

import com.larpclient.BlockPlacementController;
import com.larpclient.PlacementPreview;
import com.larpclient.ModuleDetector;
import com.larpclient.ParticleEmitter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Direction.Type;

public final class AutoWeb extends BlockPlacementController.Type_k implements PlacementPreview.Type_e {
   private final PlacementPreview F = new PlacementPreview(this, var1 -> this.addSetting(var1));
   private static AutoWeb P;
   private Object s;
   private Object z;
   private int a;
   private final BooleanSetting N = this.UAsBooleanSetting("Legs", "Place webs at the opponent's feet", true);
   private final BooleanSetting C = this.UAsBooleanSetting("Head", "Place webs at the opponent's head", true);
   private final BooleanSetting i = this.UAsBooleanSetting("Surround", "Web the lower perimeter", false);
   private final BooleanSetting G = this.UAsBooleanSetting("Upper Surround", "Web the upper perimeter", false);
   private final BooleanSetting b = this.UAsBooleanSetting("Web Predict", "Predict movement unless the target is already in webs", false);
   private final NumberSetting D = this.KAsNumberSetting("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final BooleanSetting x = this.UAsBooleanSetting("Integrate With Trap", "Choose the footprint used by the active AutoTrap", false);
   private final BooleanSetting g = this.UAsBooleanSetting("Auto Sign", "Use ordinary hotbar signs at head positions instead of head webs", false);

   @Override
   public PlacementPreview placementHighlight() {
      return this.F;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.F.SAsVoid();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.F.rAsVoid();
      this.fAsVoid();
   }

   public AutoWeb() {
      super("AutoWeb", "Place cobwebs around opponents");
      P = this;
      this.D.visibleWhen(this.b::getValue);
      this.rotation.onChanged(var1 -> {
         BlockPlacementController.WAsVoid(this);
         this.fAsVoid();
      });
   }

   public static AutoWeb PAsAutoWeb() {
      return P;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled() && ParticleEmitter.fAsBoolean(ParticleEmitter.Type_u.AUTOWEB)) {
         super.aim(var1, var2);
         if (ModuleDetector.CAsBoolean(ModuleDetector.Type_s.PLACEMENT)) {
            this.s = mc.player;
            this.z = mc.world;
            this.a = mc.player.age;
            ParticleEmitter.jAsVoid(ParticleEmitter.Type_u.AUTOWEB);
         } else {
            this.fAsVoid();
         }
      }
   }

   private void fAsVoid() {
      this.s = this.z = null;
      ParticleEmitter.GAsVoid(ParticleEmitter.Type_u.AUTOWEB);
   }

   public boolean kAsBoolean() {
      return this.isEnabled()
         ? this.isRuntimeActive()
            && this.rotation.pAsBoolean("Visible")
            && mc.player != null
            && this.s == mc.player
            && this.z == mc.world
            && mc.player.age >= this.a
            && mc.player.age - this.a <= 1
            && (!this.Q.getValue() || !mc.player.isUsingItem())
            && (!this.j.getValue() || !mc.options.attackKey.isPressed())
         : false;
   }

   public float zAsFloat(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && ParticleEmitter.NAsP5_Type_u() == ParticleEmitter.Type_u.AUTOWEB && ModuleDetector.CAsBoolean(ModuleDetector.Type_s.PLACEMENT)) {
            mc.player.setHeadYaw(mc.player.getYaw());
            return mc.player.getYaw();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private boolean HAsBoolean(PlayerEntity var1) {
      for (BlockPos var3 : AutoTrap.rAsList(var1, var1.getEntityPos())) {
         if (mc.world.getBlockState(var3).isOf(Blocks.COBWEB)) {
            return true;
         }
      }

      return false;
   }

   private Item FAsItem() {
      for (int var1 = 0; var1 < 9; var1++) {
         Item var2 = mc.player.getInventory().getStack(var1).getItem();
         if (var2 instanceof BlockItem var3 && var3.getBlock() instanceof AbstractSignBlock) {
            return var2;
         }
      }

      return null;
   }

   @Override
   public void onTick() {
      this.F.mAsVoid(null);
      BlockPlacementController.WAsVoid(this);
      boolean var1 = this.rAsBoolean();
      if (var1 || this.F.highlight() && this.cAsBoolean()) {
         PlayerEntity var2 = this.qAsPlayerEntity();
         if (var2 == null) {
            this.KAsVoid("Waiting for a target");
         } else {
            Vec3d var3 = AutoTrap.cAsVec3d(var2, this.b.getValue() && !this.HAsBoolean(var2) ? this.D.getValue() : 0.0);
            List var4 = AutoTrap.rAsList(var2, var3);
            AutoTrap var5 = AutoTrap.oAsAutoTrap();
            if (this.x.getValue() && var5 != null && var5.isRuntimeActive()) {
               LinkedHashSet var6 = new LinkedHashSet();

               for (BlockPos var8 : var5.eAsList(var2)) {
                  if (var8.getY() == ((BlockPos)var4.getFirst()).getY() - 1) {
                     var6.add(var8.up());
                  }
               }

               if (!var6.isEmpty()) {
                  var4 = new ArrayList(var6);
               }
            }

            Item var14 = this.g.getValue() ? this.FAsItem() : null;
            LinkedHashSet var15 = new LinkedHashSet();
            LinkedHashSet var16 = new LinkedHashSet();

            for (BlockPos var10 : (Iterable<BlockPos>) (var4)) {
               if (this.N.getValue()) {
                  var15.add(var10);
               }

               if (var14 != null) {
                  var16.add(var10.up());
               } else if (this.C.getValue()) {
                  var15.add(var10.up());
               }

               for (Direction var12 : Type.HORIZONTAL) {
                  BlockPos var13 = var10.offset(var12);
                  if (!var4.contains(var13)) {
                     if (this.i.getValue()) {
                        var15.add(var13);
                     }

                     if (this.G.getValue()) {
                        var15.add(var13.up());
                     }
                  }
               }
            }

            if (BlockPlacementController.qAsInt(Items.COBWEB, this.J.getValue()) != -1) {
               for (BlockPos var19 : (Iterable<BlockPos>) (var15)) {
                  if (var1 ? this.LAsBoolean(var19, Items.COBWEB, var2) : this.PAsBoolean(var19, Items.COBWEB)) {
                     this.F.mAsVoid(var19);
                     if (var1) {
                        this.KAsVoid("Preparing web placement");
                     }

                     return;
                  }
               }
            }

            if (var14 != null) {
               for (BlockPos var20 : (Iterable<BlockPos>) (var16)) {
                  if (var1 ? this.LAsBoolean(var20, var14, var2) : this.PAsBoolean(var20, var14)) {
                     this.F.mAsVoid(var20);
                     if (var1) {
                        this.KAsVoid("Preparing sign placement");
                     }

                     return;
                  }
               }
            }

            this.KAsVoid(
               BlockPlacementController.qAsInt(Items.COBWEB, this.J.getValue()) == -1
                  ? "Cobwebs missing from hotbar"
                  : "No reachable empty web position: " + BlockPlacementController.XAsString(this)
            );
         }
      }
   }
}
