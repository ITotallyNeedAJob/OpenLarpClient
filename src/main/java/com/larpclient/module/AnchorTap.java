package com.larpclient.module;

import com.larpclient.BlockPlacementController;
import com.larpclient.ClientLog;
import com.larpclient.ModuleImportBroker;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.attribute.EnvironmentAttributes;

public final class AnchorTap extends Module {
   private static AnchorTap N;
   private final NumberSetting delay = this.addSetting(new NumberSetting("Delay", "Ticks between executed anchor steps", 0.0, 0.0, 20.0, 1.0));
   private final BooleanSetting safeAnchor = this.addSetting(
      new BooleanSetting("Safe Anchor", "Place an obsidian shield between you and the anchor before detonation", false)
   );
   private final BooleanSetting airReplace = this.addSetting(new BooleanSetting("Air Replace", "Replace the removed anchor once after detonation", false));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Reach capped by native block interaction distance", 4.1, 1.0, 6.0, 0.1));
   private final ModeSetting rotation = this.addSetting(
      new ModeSetting("Rotation", "Visible turns the real view; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final BooleanSetting autoSwitch = this.addSetting(
      new BooleanSetting("Auto Switch", "Use hotbar items and restore the selected slot after each step", true)
   );
   private AnchorTap.Type_z D = AnchorTap.Type_z.IDLE;
   private BlockPos u;
   private boolean e;
   private long J;
   private long T;
   private String c = "Aim at an anchor and press Use";

   public AnchorTap() {
      super("AnchorTap", "Charge and explode an anchor with the use key", Category.COMBAT, -1);
      N = this;
   }

   public static boolean YAsBoolean() {
      if (N != null && N.isEnabled()) {
         AnchorTap var0 = N;
         if (var0 != null && var0.isRuntimeActive() && ModuleImportBroker.jAsBoolean()) {
            boolean var10000;
            if (var0.D != AnchorTap.Type_z.IDLE) {
               var10000 = true;
            } else {
               if (mc.crosshairTarget instanceof BlockHitResult var1
                  && mc.options.useKey.isPressed()
                  && mc.world.getBlockState(var1.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR)) {
                  return true;
               }

               var10000 = false;
            }

            return var10000;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      return this.c;
   }

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.INTENT;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   @Override
   public void onTick() {
      BlockPlacementController.WAsVoid(this);
      if (ModuleImportBroker.jAsBoolean() && mc.isWindowFocused()) {
         boolean var1 = mc.options.useKey.isPressed();
         if (var1
            && !this.e
            && this.D == AnchorTap.Type_z.IDLE
            && mc.crosshairTarget instanceof BlockHitResult var2
            && mc.world.getBlockState(var2.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR)) {
            this.u = var2.getBlockPos().toImmutable();
            this.D = AnchorTap.Type_z.CHARGE;
            this.J = mc.world.getTime();
            this.T = 0L;
            ClientLog.XAsVoid("AnchorTap", "Anchor cycle started at {}", this.u);
         }

         this.e = var1;
         if (this.D != AnchorTap.Type_z.IDLE && this.u != null) {
            long var8 = mc.world.getTime();
            if (mc.world.isChunkLoaded(this.u) && !mc.player.isUsingItem()) {
               if (var8 - this.J > 40L) {
                  this.eAsVoid("Anchor response timed out");
               } else if (var8 >= this.T) {
                  BlockState var4 = mc.world.getBlockState(this.u);
                  boolean var5 = var4.isOf(Blocks.RESPAWN_ANCHOR);
                  switch (this.D) {
                     case CHARGE:
                        if (!var5) {
                           this.eAsVoid("Anchor no longer exists");
                           return;
                        }

                        if ((Integer)var4.get(RespawnAnchorBlock.CHARGES) > 0) {
                           this.aAsVoid(AnchorTap.Type_z.SHIELD);
                           return;
                        }

                        this.ZAsVoid(
                           Items.GLOWSTONE,
                           AnchorTap.Type_z.WAIT_CHARGE,
                           () -> mc.world.getBlockState(this.u).isOf(Blocks.RESPAWN_ANCHOR)
                                 && (Integer)mc.world.getBlockState(this.u).get(RespawnAnchorBlock.CHARGES) == 0
                        );
                        break;
                     case WAIT_CHARGE:
                        if (!var5) {
                           this.eAsVoid("Anchor removed before charging");
                        } else if ((Integer)var4.get(RespawnAnchorBlock.CHARGES) > 0) {
                           this.aAsVoid(AnchorTap.Type_z.SHIELD);
                        } else if (var8 - this.J > 8L) {
                           this.eAsVoid("Anchor charge not confirmed");
                        }
                        break;
                     case SHIELD:
                        if (!this.safeAnchor.getValue()) {
                           this.aAsVoid(AnchorTap.Type_z.EXPLODE);
                           return;
                        }

                        BlockPos var10 = this.tAsBlockPos();
                        if (!BlockPlacementController.jAsBoolean(var10)) {
                           BlockState var11 = mc.world.getBlockState(var10);
                           if (!var11.isOf(Blocks.OBSIDIAN) && !var11.isOf(Blocks.CRYING_OBSIDIAN) && !var11.isOf(Blocks.BEDROCK)) {
                              this.eAsVoid("Safe Anchor shield position occupied by another block");
                           } else {
                              this.aAsVoid(AnchorTap.Type_z.EXPLODE);
                           }

                           return;
                        }

                        if (mc.player.getBoundingBox().intersects(new Box(var10))) {
                           this.eAsVoid("No safe shield position outside your body");
                           return;
                        }

                        if (BlockPlacementController.qAsInt(Items.OBSIDIAN, this.autoSwitch.getValue()) == -1) {
                           this.eAsVoid("Safe Anchor requires hotbar obsidian");
                           return;
                        }

                        boolean var7 = BlockPlacementController.sAsBoolean(
                           this,
                           var10,
                           Items.OBSIDIAN,
                           this.range.getValue(),
                           this.rotation.pAsBoolean("Visible"),
                           this.autoSwitch.getValue(),
                           () -> this.D == AnchorTap.Type_z.SHIELD && this.cAsBoolean() && !mc.player.getBoundingBox().intersects(new Box(var10)),
                           var1x -> {
                              if (var1x) {
                                 this.T = mc.world.getTime() + (long)Math.max(1, this.delay.getValue().intValue());
                                 this.c = "Waiting for obsidian shield";
                              }
                           }
                        );
                        if (!var7) {
                           this.c = "Aim at a reachable shield support";
                        }
                        break;
                     case EXPLODE:
                        if (!this.cAsBoolean()) {
                           this.eAsVoid("Charged anchor no longer exists");
                           return;
                        }

                        if ((Boolean)mc.world.getEnvironmentAttributes().getAttributeValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS_GAMEPLAY, this.u)) {
                           this.eAsVoid("Anchors set spawn here; detonation skipped");
                           return;
                        }

                        if (mc.player.getOffHandStack().isOf(Items.GLOWSTONE) && (Integer)var4.get(RespawnAnchorBlock.CHARGES) < 4) {
                           this.c = "Move offhand glowstone before detonation";
                           return;
                        }

                        Item var9 = this.iAsItem();
                        if (var9 == null) {
                           this.eAsVoid("A non-glowstone hotbar item is required");
                           return;
                        }

                        this.ZAsVoid(
                           var9,
                           AnchorTap.Type_z.WAIT_REMOVE,
                           () -> this.cAsBoolean()
                                 && (
                                    !mc.player.getOffHandStack().isOf(Items.GLOWSTONE)
                                       || (Integer)mc.world.getBlockState(this.u).get(RespawnAnchorBlock.CHARGES) == 4
                                 )
                                 && !(Boolean)mc.world
                                    .getEnvironmentAttributes()
                                    .getAttributeValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS_GAMEPLAY, this.u)
                        );
                        break;
                     case WAIT_REMOVE:
                        if (!var5) {
                           if (this.airReplace.getValue() && BlockPlacementController.jAsBoolean(this.u)) {
                              this.aAsVoid(AnchorTap.Type_z.REPLACE);
                           } else {
                              this.eAsVoid("Anchor cycle complete");
                           }
                        } else if (var8 - this.J > 8L) {
                           this.eAsVoid("Anchor removal not confirmed");
                        }
                        break;
                     case REPLACE:
                        if (!BlockPlacementController.jAsBoolean(this.u)) {
                           this.eAsVoid("Replacement position occupied");
                           return;
                        }

                        if (BlockPlacementController.qAsInt(Items.RESPAWN_ANCHOR, this.autoSwitch.getValue()) == -1) {
                           this.eAsVoid("Replacement anchor missing from hotbar");
                           return;
                        }

                        boolean var6 = BlockPlacementController.sAsBoolean(
                           this,
                           this.u,
                           Items.RESPAWN_ANCHOR,
                           this.range.getValue(),
                           this.rotation.pAsBoolean("Visible"),
                           this.autoSwitch.getValue(),
                           () -> this.D == AnchorTap.Type_z.REPLACE && BlockPlacementController.jAsBoolean(this.u),
                           var1x -> {
                              if (var1x) {
                                 this.aAsVoid(AnchorTap.Type_z.WAIT_REPLACE);
                              }
                           }
                        );
                        if (!var6) {
                           this.c = "Aim at a reachable replacement support";
                        }
                        break;
                     case WAIT_REPLACE:
                        if (var5) {
                           this.eAsVoid("Anchor replacement complete");
                        } else if (var8 - this.J > 8L) {
                           this.eAsVoid("Anchor replacement not confirmed");
                        }
                  }
               }
            } else {
               this.eAsVoid("Anchor cycle cancelled");
            }
         }
      } else {
         this.eAsVoid("Anchor interaction suspended");
      }
   }

   private BlockPos tAsBlockPos() {
      Vec3d var1 = mc.player.getEntityPos().subtract(Vec3d.ofCenter(this.u));
      Direction var2 = Math.abs(var1.x) > Math.abs(var1.z)
         ? (var1.x >= 0.0 ? Direction.EAST : Direction.WEST)
         : (var1.z >= 0.0 ? Direction.SOUTH : Direction.NORTH);
      return this.u.offset(var2);
   }

   private boolean cAsBoolean() {
      return this.u != null
         && mc.world.getBlockState(this.u).isOf(Blocks.RESPAWN_ANCHOR)
         && (Integer)mc.world.getBlockState(this.u).get(RespawnAnchorBlock.CHARGES) > 0;
   }

   private Item iAsItem() {
      ItemStack var1 = mc.player.getMainHandStack();
      if (!var1.isOf(Items.GLOWSTONE)) {
         return var1.getItem();
      } else {
         if (this.autoSwitch.getValue()) {
            for (int var2 = 0; var2 < 9; var2++) {
               ItemStack var3 = mc.player.getInventory().getStack(var2);
               if (!var3.isOf(Items.GLOWSTONE)) {
                  return var3.getItem();
               }
            }
         }

         return null;
      }
   }

   private void ZAsVoid(Item var1, AnchorTap.Type_z var2, BooleanSupplier var3) {
      if (BlockPlacementController.qAsInt(var1, this.autoSwitch.getValue()) == -1) {
         this.eAsVoid("Required anchor item missing from hotbar");
      } else {
         AnchorTap.Type_z var4 = this.D;
         boolean var5 = BlockPlacementController.xAsBoolean(
            this,
            this.u,
            var1,
            this.range.getValue(),
            this.rotation.pAsBoolean("Visible"),
            this.autoSwitch.getValue(),
            () -> this.D == var4 && var3.getAsBoolean(),
            var2xx -> {
               if (var2xx) {
                  this.aAsVoid(var2);
                  this.T = mc.world.getTime() + (long)Math.max(1, this.delay.getValue().intValue());
               } else {
                  this.c = "Anchor interaction rejected; retrying";
               }
            }
         );
         if (!var5) {
            this.c = "Aim at the anchor within native reach";
         }
      }
   }

   private void aAsVoid(AnchorTap.Type_z var1) {
      this.D = var1;
      this.J = mc.world.getTime();
      this.c = var1.name().toLowerCase().replace('_', ' ');
   }

   private void eAsVoid(String var1) {
      BlockPlacementController.WAsVoid(this);
      if (this.D != AnchorTap.Type_z.IDLE) {
         ClientLog.XAsVoid("AnchorTap", "{}", var1);
      }

      this.D = AnchorTap.Type_z.IDLE;
      this.u = null;
      this.J = 0L;
      this.T = 0L;
      this.c = var1;
   }

   @Override
   public void onEnable() {
      this.e = mc.player != null && mc.options.useKey.isPressed();
      this.eAsVoid("Aim at an anchor and press Use");
      ClientLog.XAsVoid("AnchorTap", "Anchor use handler active");
   }

   @Override
   public void onDisable() {
      this.eAsVoid("Anchor intents cleared");
      this.e = false;
   }

   private static enum Type_z {
      IDLE,
      CHARGE,
      WAIT_CHARGE,
      SHIELD,
      EXPLODE,
      WAIT_REMOVE,
      REPLACE,
      WAIT_REPLACE;

      private Type_z() {
      }
   }
}
