package com.larpclient.module;

import com.larpclient.BlockPlacementController;
import com.larpclient.ClientLog;
import com.larpclient.PlacementPreview;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ModuleDetector;
import com.larpclient.DropletSurface;
import com.larpclient.RotationAccumulator;
import com.larpclient.ParticleEmitter;
import com.larpclient.ModuleKeybindChecker;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.TntBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

public final class Scaffold extends Module implements BlockPlacementController.Type_n, PlacementPreview.Type_e {
   private static Scaffold f;
   private final BooleanSetting autoSwitch = this.addSetting(
      new BooleanSetting("Auto Switch", "Use suitable hotbar blocks and restore your selected slot", true)
   );
   private final BooleanSetting safeWalk = this.addSetting(
      new BooleanSetting("Safe Walk", "Use vanilla ledge protection while grounded; jumping and sneak remain manual", true)
   );
   private final BooleanSetting keepHeight = this.addSetting(
      new BooleanSetting("Keep Height", "Build at the height captured when enabled or when this setting changes", false)
   );
   private final ModeSetting rotation = this.addSetting(
      new ModeSetting("Rotation", "Turn the actual player while keeping mouse look free", "Instant", "Instant", "Smooth")
   );
   private final NumberSetting turnSpeed = this.addSetting(
      new NumberSetting("Turn Speed", "Maximum smooth aim speed in degrees per second", 360.0, 30.0, 360.0, 15.0)
   );
   private final NumberSetting lookAhead = this.addSetting(
      new NumberSetting("Look Ahead", "Extra footprint prediction in your movement direction, in blocks", 0.35, 0.0, 0.6, 0.05)
   );
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Placement reach capped by native block reach", 4.1, 1.0, 6.0, 0.1));
   private final NumberSetting delay = this.addSetting(
      new NumberSetting("Delay", "Extra ticks between placement attempts; zero allows one per tick", 0.0, 0.0, 10.0, 1.0)
   );
   private final BooleanSetting pauseUsingItem = this.addSetting(new BooleanSetting("Pause Using Item", "Pause while eating or using an item", true));
   private final BooleanSetting pauseMining = this.addSetting(new BooleanSetting("Pause Mining", "Pause while the attack key is held", true));
   private final PlacementPreview G = new PlacementPreview(this, var1 -> this.addSetting(var1));
   private final RotationAccumulator U = new RotationAccumulator();
   private Object J;
   private Object M;
   private BlockPos o;
   private int X;
   private int d;
   private int P;
   private boolean R;
   private String E = "Inactive";
   private String r = "";
   private long u;

   public Scaffold() {
      super("Scaffold", "Bridge beneath your feet with visible player aim and free mouse look", Category.MOVEMENT, -1);
      f = this;
      this.turnSpeed.visibleWhen(() -> this.rotation.pAsBoolean("Smooth"));
      this.keepHeight.onChanged(var1 -> {
         this.TAsVoid();
         if (this.isInGame()) {
            this.X = DropletSurface.vAsInt(mc.player.getY());
         }
      });
      this.rotation.onChanged(var1 -> this.TAsVoid());
      this.autoSwitch.onChanged(var1 -> this.TAsVoid());
   }

   public static Scaffold FAsScaffold() {
      return f;
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
   public PlacementPreview placementHighlight() {
      return this.G;
   }

   @Override
   public String getStatus() {
      return this.E;
   }

   public List<Setting<?>> sAsList() {
      return List.of(this.autoSwitch, this.safeWalk, this.keepHeight, this.rotation);
   }

   public List<Setting<?>> pAsList() {
      return List.of(this.turnSpeed, this.lookAhead, this.range, this.delay, this.pauseUsingItem, this.pauseMining);
   }

   @Override
   public void onEnable() {
      getMc();
      this.TAsVoid();
      this.d = 0;
      this.J = mc.world;
      this.M = mc.player;
      this.X = DropletSurface.vAsInt(mc.player.getY());
      this.E = "Ready";
      this.r = "";
      this.u = 0L;
      this.G.SAsVoid();
      ClientLog.GAsVoid(
         this.getName(),
         "Enabled: real {} player aim, free camera, native placement, safe walk={}, keep height={}; selected slot is restored",
         this.rotation.getValue(),
         this.safeWalk.getValue(),
         this.keepHeight.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.TAsVoid();
      this.J = this.M = null;
      this.d = 0;
      this.E = "Inactive";
      ClientLog.XAsVoid(this.getName(), "Released placement, independent camera and ledge protection");
   }

   private void TAsVoid() {
      this.o = null;
      this.R = false;
      this.U.aAsVoid();
      this.G.rAsVoid();
      BlockPlacementController.WAsVoid(this);
      ParticleEmitter.GAsVoid(ParticleEmitter.Type_u.SCAFFOLD);
   }

   private boolean pAsBoolean() {
      if (this.isRuntimeActive()
         && mc != null
         && mc.player != null
         && mc.world != null
         && this.J == mc.world
         && this.M == mc.player
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.currentScreen == null
         && mc.isWindowFocused()) {
         ClientPlayerEntity var1 = mc.player;
         return !var1.hasVehicle()
            && !var1.isGliding()
            && !var1.getAbilities().flying
            && !var1.noClip
            && !var1.isSleeping()
            && !var1.hasBlindnessEffect()
            && !var1.isClimbing()
            && !var1.isTouchingWater()
            && !var1.isInLava()
            && var1.fallDistance <= 3.0
            && (!this.pauseUsingItem.getValue() || !var1.isUsingItem())
            && (!this.pauseMining.getValue() || !mc.options.attackKey.isPressed());
      } else {
         return false;
      }
   }

   private boolean EAsBoolean() {
      if (!this.pAsBoolean() || !ModuleImportBroker.jAsBoolean() || ModuleImportBroker.MAsBoolean()) {
         return false;
      } else if (!ParticleEmitter.fAsBoolean(ParticleEmitter.Type_u.SCAFFOLD)) {
         return false;
      } else {
         Fly var1 = Fly.bAsFly();
         AirStuck var2 = AirStuck.zAsAirStuck();
         ElytraMotion var3 = ElytraMotion.vAsElytraMotion();
         return (var1 == null || !var1.vAsBoolean())
            && (var2 == null || !var2.wAsBoolean())
            && (var3 == null || !var3.ZAsBoolean())
            && !TargetStrafe.yAsBoolean();
      }
   }

   public boolean bAsBoolean() {
      return this.isEnabled() ? this.pAsBoolean() && (this.o != null || this.R && mc.player.age - this.P >= 0 && mc.player.age - this.P <= 3) : false;
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled() && this.EAsBoolean() && ModuleImportBroker.nAsBoolean(this)) {
         float[] var3 = this.rotation.pAsBoolean("Smooth")
            ? this.U.DAsFloatArray(mc.player.age, mc.player.getYaw(), mc.player.getPitch(), var1, var2, this.turnSpeed.getValue())
            : new float[]{var1, var2};
         ModuleDetector.RAsVoid(this, ModuleDetector.Type_s.PLACEMENT, var3[0], var3[1], 1.0);
         if (!ModuleDetector.CAsBoolean(ModuleDetector.Type_s.PLACEMENT)) {
            this.R = false;
            ParticleEmitter.GAsVoid(ParticleEmitter.Type_u.SCAFFOLD);
         } else {
            ParticleEmitter.jAsVoid(ParticleEmitter.Type_u.SCAFFOLD);
            this.R = true;
            this.P = mc.player.age;
         }
      }
   }

   public float UAsFloat(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && ParticleEmitter.NAsP5_Type_u() == ParticleEmitter.Type_u.SCAFFOLD) {
            mc.player.setHeadYaw(mc.player.getYaw());
            return mc.player.getYaw();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static boolean yAsBoolean(PlayerEntity var0) {
      if (f != null && f.isEnabled()) {
         Scaffold var1 = f;
         return var1 != null
            && var0 == getMc().player
            && var1.safeWalk.getValue()
            && var1.EAsBoolean()
            && var0.isOnGround()
            && !mc.options.sneakKey.isPressed();
      } else {
         return false;
      }
   }

   public static boolean nAsBoolean(ItemStack var0, BlockView var1, BlockPos var2) {
      if (!var0.isEmpty() && var0.getItem() instanceof BlockItem var3) {
         Block var6 = var3.getBlock();
         BlockState var5 = var6.getDefaultState();
         return !(var6 instanceof FallingBlock)
            && !(var6 instanceof TntBlock)
            && var6 != Blocks.MAGMA_BLOCK
            && var6 != Blocks.RESPAWN_ANCHOR
            && var6 != Blocks.CRAFTING_TABLE
            && var6 != Blocks.SMITHING_TABLE
            && var6 != Blocks.FLETCHING_TABLE
            && var6 != Blocks.NOTE_BLOCK
            && !var5.hasBlockEntity()
            && var5.isFullCube(var1, var2);
      } else {
         return false;
      }
   }

   private List<Item> AAsList() {
      LinkedHashSet var1 = new LinkedHashSet();
      PlayerInventory var2 = mc.player.getInventory();
      ItemStack var3 = mc.player.getMainHandStack();
      ItemStack var4 = mc.player.getOffHandStack();
      if (nAsBoolean(var3, mc.world, mc.player.getBlockPos())) {
         var1.add(var3.getItem());
      }

      if (nAsBoolean(var4, mc.world, mc.player.getBlockPos())) {
         var1.add(var4.getItem());
      }

      if (this.autoSwitch.getValue()) {
         ArrayList var5 = new ArrayList();

         for (int var6 = 0; var6 < 9; var6++) {
            ItemStack var7 = var2.getStack(var6);
            if (nAsBoolean(var7, mc.world, mc.player.getBlockPos())) {
               var5.add(var7);
            }
         }

         var5.sort(Comparator.comparingInt(ItemStack::getCount).reversed());
         var5.forEach(var1xx -> var1.add(((ItemStack)var1xx).getItem()));
      }

      return List.copyOf(var1);
   }

   private List<BlockPos> qAsList() {
      Vec3d var1 = ModuleKeybindChecker.JAsVec3d().multiply(this.lookAhead.getValue());
      Vec3d var2 = mc.player.getVelocity().multiply(1.0, 0.0, 1.0);
      return DropletSurface.oAsList(mc.player.getEntityPos(), mc.player.getBoundingBox(), var1.add(var2), this.keepHeight.getValue() ? this.X : DropletSurface.vAsInt(mc.player.getY()));
   }

   private void cAsVoid(String var1) {
      this.E = var1;
      long var2 = System.nanoTime();
      if (!var1.equals(this.r) && var2 - this.u >= 2000000000L) {
         this.r = var1;
         this.u = var2;
         ClientLog.XAsVoid(this.getName(), "{}; will retry when the current placement conditions permit", var1);
      }
   }

   @Override
   public void onTick() {
      BlockPlacementController.WAsVoid(this);
      this.o = null;
      if (!this.EAsBoolean()) {
         this.TAsVoid();
         this.cAsVoid("Paused: screen, movement, item use or another camera owns control");
      } else {
         if (!this.bAsBoolean()) {
            ParticleEmitter.GAsVoid(ParticleEmitter.Type_u.SCAFFOLD);
         }

         if (mc.player.age < this.d) {
            this.G.mAsVoid(null);
            this.E = "Placement delay";
         } else {
            List var1 = this.AAsList();
            if (var1.isEmpty()) {
               this.TAsVoid();
               this.cAsVoid("No suitable blocks in held hands or hotbar");
            } else {
               for (BlockPos var3 : this.qAsList()) {
                  if (BlockPlacementController.jAsBoolean(var3)) {
                     for (Item var5 : (Iterable<Item>) (var1)) {
                        if (BlockPlacementController.HAsBlockHitResult(var3, var5, this.range.getValue(), true, this.autoSwitch.getValue()) != null) {
                           this.o = var3;
                           boolean var6 = BlockPlacementController.sAsBoolean(
                              this,
                              var3,
                              var5,
                              this.range.getValue(),
                              true,
                              this.autoSwitch.getValue(),
                              () -> this.EAsBoolean() && var3.equals(this.o) && this.qAsList().contains(var3),
                              var1x -> {
                                 this.d = mc.player.age + this.delay.getValue().intValue() + 1;
                                 this.E = var1x ? "Bridging" : "Placement rejected; retrying";
                              }
                           );
                           if (var6) {
                              this.G.mAsVoid(var3);
                              this.E = "Aiming at bridge block";
                              return;
                           }

                           this.o = null;
                        }
                     }
                  }
               }

               this.G.mAsVoid(null);
               this.cAsVoid("Waiting for a reachable support face beneath your path");
            }
         }
      }
   }
}
