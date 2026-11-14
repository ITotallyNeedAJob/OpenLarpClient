package com.larpclient.module;

import com.larpclient.BlockPlacementController;
import com.larpclient.ClientLog;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ModuleDetector;
import com.larpclient.ParticleEmitter;
import com.larpclient.ParticleGeometry;
import com.larpclient.mixin.ClientPlayerInteractionManagerAccessor;
import com.larpclient.mixin.MinecraftClientAccessor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class AutoDrain extends Module {
   public static final long w = 120000L;
   public static final int V = 2;
   public static final long z = 60000L;
   private static final double W = 0.4;
   private static final int q = 512;
   private static final Map<BlockPos, Long> U = new HashMap<>();
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Furthest water to scoop, capped by your reach", 4.5, 2.0, 6.0, 0.5));
   private final NumberSetting delay = this.addSetting(new NumberSetting("Delay", "Ticks to wait between scoops", 2.0, 0.0, 20.0, 1.0));
   private final BooleanSetting waterlogged = this.addSetting(
      new BooleanSetting("Waterlogged", "Also drain water inside stairs, slabs and other waterlogged blocks", false)
   );
   private final BooleanSetting ignoreMyWater = this.addSetting(
      new BooleanSetting("Ignore My Water", "Leave water you emptied yourself in the last two minutes", true)
   );
   private final ParticleGeometry f = new ParticleGeometry(this);
   private final Map<BlockPos, int[]> Z = new HashMap<>();
   private final Map<BlockPos, Long> r = new HashMap<>();
   private ClientPlayerEntity l;
   private ClientWorld D;
   private BlockPos Y;
   private Vec3d I;
   private boolean t;
   private float L;
   private float k;
   private int F;
   private int S;
   private String b = "Ready";

   public AutoDrain() {
      super("Auto Drain", "Scoop up nearby still water with empty buckets", Category.PLAYER, -1);
   }

   public static int GAsInt(double var0, int var2, double var3, int var5) {
      int var6 = Double.compare(var3, var0);
      return var6 != 0 ? var6 : Integer.compare(var5, var2);
   }

   public static boolean CAsBoolean(int var0, boolean var1) {
      return var0 == 1 || var1;
   }

   public static boolean aAsBoolean(int var0) {
      return var0 >= 2;
   }

   public static void QAsVoid(PlayerEntity var0, Hand var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var0 == var2.player && var2.world != null && var0.getStackInHand(var1).isOf(Items.WATER_BUCKET)) {
         Vec3d var3 = var0.getEyePos();
         Vec3d var4 = var3.add(var0.getRotationVector().multiply(var0.getBlockInteractionRange()));
         BlockHitResult var5 = var2.world.raycast(new RaycastContext(var3, var4, ShapeType.OUTLINE, FluidHandling.NONE, var0));
         if (var5.getType() == Type.BLOCK) {
            long var6 = System.currentTimeMillis();
            if (U.size() >= 512) {
               U.clear();
            }

            U.put(var5.getBlockPos().toImmutable(), var6);
            U.put(var5.getBlockPos().offset(var5.getSide()).toImmutable(), var6);
         }
      }
   }

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.INTENT;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(com.larpclient.module.k.VANILLA_INPUT, com.larpclient.module.k.SERVER_STATE_MODIFYING);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.b : "";
   }

   @Override
   public void onEnable() {
      this.mAsVoid();
      this.l = mc.player;
      this.D = mc.world;
      this.b = "Ready";
      ClientLog.GAsVoid(
         "AutoDrain",
         "Enabled; range {}, delay {} ticks, waterlogged={}, ignore own={}",
         this.range.getValue(),
         this.delay.getValue().intValue(),
         this.waterlogged.getValue(),
         this.ignoreMyWater.getValue()
      );
   }

   @Override
   public void onDisable() {
      if (this.t && this.l != null && this.l == getMc().player) {
         this.f.vAsVoid(mc, true);
         this.l.setYaw(this.L);
         this.l.setPitch(this.k);
      }

      this.mAsVoid();
      this.l = null;
      this.D = null;
   }

   private void mAsVoid() {
      ModuleImportBroker.KAsVoid(this);
      this.t = false;
      this.Y = null;
      this.I = null;
      this.F = 0;
      this.S = 0;
      this.Z.clear();
      this.r.clear();
   }

   private boolean uAsBoolean() {
      ParticleEmitter.Type_u var1 = ParticleEmitter.NAsP5_Type_u();
      return !ModuleImportBroker.jAsBoolean() || mc.currentScreen != null || var1 != ParticleEmitter.Type_u.NONE;
   }

   @Override
   public void onTick() {
      if (this.l != mc.player || this.D != mc.world) {
         this.f.vAsVoid(getMc(), false);
         this.mAsVoid();
         U.clear();
         this.l = mc.player;
         this.D = mc.world;
      }

      if (this.l != null && this.D != null) {
         if (this.uAsBoolean()) {
            if (this.t) {
               this.b = "Paused";
            }
         } else {
            this.BAsVoid(System.currentTimeMillis());
            if (this.F > 0) {
               this.F--;
            } else {
               int var1 = BlockPlacementController.qAsInt(Items.BUCKET, true);
               if (var1 == -1) {
                  this.DAsVoid("No empty buckets");
               } else {
                  ItemStack var2 = var1 == -2 ? this.l.getOffHandStack() : this.l.getInventory().getStack(var1);
                  if (!CAsBoolean(var2.getCount(), this.l.getInventory().getEmptySlot() != -1)) {
                     this.DAsVoid("Inventory full");
                  } else if (!this.bAsBoolean()) {
                     this.DAsVoid(this.S > 0 ? "Drained " + this.S : "No water in reach");
                  } else {
                     if (!this.t) {
                        this.t = true;
                        this.S = 0;
                        this.L = this.l.getYaw();
                        this.k = this.l.getPitch();
                        ClientLog.XAsVoid("AutoDrain", "Run started at {}", this.Y);
                     }

                     this.b = "Draining";
                     float[] var3 = ModuleDetector.QAsFloatArray(this.l.getEyePos(), this.I);
                     ModuleDetector.RAsVoid(this, ModuleDetector.Type_s.AUTODRAIN, var3[0], var3[1], 1.0);
                     ModuleImportBroker.UAsVoid(this, this::bAsVoid, this::WAsBoolean);
                  }
               }
            }
         }
      }
   }

   private boolean bAsBoolean() {
      double var1 = Math.min(this.range.getValue(), this.l.getBlockInteractionRange());
      Vec3d var3 = this.l.getEyePos();
      int var4 = (int)Math.ceil(var1);
      BlockPos var5 = BlockPos.ofFloored(var3);
      long var6 = System.currentTimeMillis();
      BlockPos var8 = null;
      Vec3d var9 = null;
      double var10 = 0.0;

      for (BlockPos var13 : BlockPos.iterate(var5.add(-var4, -var4, -var4), var5.add(var4, var4, var4))) {
         if (this.jAsBoolean(var13, var6)) {
            Vec3d var14 = Vec3d.ofBottomCenter(var13).add(0.0, 0.4, 0.0);
            double var15 = var3.distanceTo(var14);
            if (!(var15 > var1)
               && (var8 == null || GAsInt(var15, var13.getY(), var10, var8.getY()) < 0)
               && this.GAsBoolean(var13, var3, var14.subtract(var3), var1)) {
               var8 = var13.toImmutable();
               var9 = var14;
               var10 = var15;
            }
         }
      }

      this.Y = var8;
      this.I = var9;
      return var8 != null;
   }

   private boolean jAsBoolean(BlockPos var1, long var2) {
      if (!this.D.getFluidState(var1).isEqualAndStill(Fluids.WATER)) {
         return false;
      } else {
         BlockState var4 = this.D.getBlockState(var1);
         if (!(var4.getBlock() instanceof FluidDrainable)) {
            return false;
         } else if (!(var4.getBlock() instanceof FluidBlock) && !this.waterlogged.getValue()) {
            return false;
         } else if (this.l.getBoundingBox().intersects(new Box(var1))) {
            return false;
         } else {
            if (this.ignoreMyWater.getValue()) {
               Long var5 = U.get(var1);
               if (var5 != null && var2 - var5 < 120000L) {
                  return false;
               }
            }

            int[] var6 = this.Z.get(var1);
            return var6 == null || !aAsBoolean(var6[0]);
         }
      }
   }

   private boolean GAsBoolean(BlockPos var1, Vec3d var2, Vec3d var3, double var4) {
      Vec3d var6 = var2.add(var3.normalize().multiply(var4));
      BlockHitResult var7 = this.D.raycast(new RaycastContext(var2, var6, ShapeType.OUTLINE, FluidHandling.SOURCE_ONLY, this.l));
      return var7.getType() == Type.BLOCK && var7.getBlockPos().equals(var1);
   }

   private boolean WAsBoolean() {
      if (this.t && this.Y != null && !this.l.isUsingItem() && this.RAsBoolean()) {
         if (!this.D.getFluidState(this.Y).isEqualAndStill(Fluids.WATER)) {
            return false;
         } else {
            double var1 = Math.min(this.range.getValue(), this.l.getBlockInteractionRange());
            return this.GAsBoolean(this.Y, this.l.getEyePos(), this.l.getRotationVector(), var1);
         }
      } else {
         return false;
      }
   }

   private boolean RAsBoolean() {
      int var1 = BlockPlacementController.qAsInt(Items.BUCKET, true);
      return var1 == -2 || var1 >= 0 && (var1 == this.l.getInventory().getSelectedSlot() || ParticleGeometry.yAsBoolean(this));
   }

   private Hand oAsHand() {
      int var1 = BlockPlacementController.qAsInt(Items.BUCKET, true);
      if (var1 == -2) {
         return Hand.OFF_HAND;
      } else if (var1 < 0) {
         return null;
      } else {
         return var1 != this.l.getInventory().getSelectedSlot() && !this.f.uAsBoolean(this.l, var1) ? null : Hand.MAIN_HAND;
      }
   }

   private void bAsVoid() {
      if (this.WAsBoolean()) {
         Hand var1 = this.oAsHand();
         if (var1 != null) {
            BlockPos var2 = this.Y;
            boolean var3 = mc.interactionManager.interactItem(this.l, var1).isAccepted();
            this.F = this.delay.getValue().intValue();
            if (!var3) {
               this.b = "Scoop rejected";
               ClientLog.XAsVoid("AutoDrain", "Scoop at {} rejected locally", var2);
            } else {
               this.l.swingHand(var1);
               ((MinecraftClientAccessor)mc).larpUseDelay(4);
               long var4 = System.currentTimeMillis();
               int[] var6 = this.Z.computeIfAbsent(var2, var0 -> new int[1]);
               var6[0]++;
               this.r.put(var2, var4);
               this.S++;
               if (aAsBoolean(var6[0])) {
                  ClientLog.XAsVoid("AutoDrain", "{} refills; skipped for {} s", var2, 60L);
               }

               ClientLog.XAsVoid("AutoDrain", "Scooped {} ({} this run)", var2, this.S);
            }
         }
      }
   }

   private void DAsVoid(String var1) {
      this.b = var1;
      if (this.t) {
         int var2 = this.l.getInventory().getSelectedSlot();
         this.f.vAsVoid(mc, true);
         if (mc.interactionManager != null && var2 != this.l.getInventory().getSelectedSlot()) {
            ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).larpSyncCarriedItem();
         }

         ModuleDetector.RAsVoid(this, ModuleDetector.Type_s.AUTODRAIN, this.L, this.k, 1.0);
         ClientLog.XAsVoid("AutoDrain", "Run ended after {} scoops: {}", this.S, var1);
         this.t = false;
         this.Y = null;
         this.I = null;
      }
   }

   private void BAsVoid(long var1) {
      U.values().removeIf(var2 -> var1 - var2 >= 120000L);
      this.r.entrySet().removeIf(var3 -> {
         int var4 = var1 - var3.getValue() >= 60000L ? 1 : 0;
         if (var4 != 0) {
            this.Z.remove(var3.getKey());
         }

         return var4 != 0;
      });
      if (this.Z.size() > 512) {
         this.Z.clear();
         this.r.clear();
      }
   }
}
