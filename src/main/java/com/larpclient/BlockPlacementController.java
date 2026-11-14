package com.larpclient;

import com.larpclient.module.AntiTrap;
import com.larpclient.module.AutoAnchor;
import com.larpclient.module.AutoTrap;
import com.larpclient.module.AutoWeb;
import com.larpclient.module.BooleanSetting;
import com.larpclient.module.Category;
import com.larpclient.module.IgnoreEntity;
import com.larpclient.module.MCFriend;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.Module;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Scaffold;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class BlockPlacementController {
   private static final Map<Module, BlockPlacementController.Type_e> b = new LinkedHashMap<>();
   private static final Map<Module, String> O = new HashMap<>();
   private static final Map<Module, Long> T = new HashMap<>();
   private static final Direction[] R = new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN};

   public static boolean rAsBoolean(Object var0, Object var1, Object var2, Object var3, int var4, int var5) {
      int var6 = var5 - var4;
      return var0 != null && var0 == var1 && var2 != null && var2 == var3 && var6 >= 0 && var6 <= 1;
   }

   public static String XAsString(Module var0) {
      return O.getOrDefault(var0, "No supported face within native reach");
   }

   private static void YAsVoid(Module var0, String var1) {
      O.put(var0, var1);
   }

   private static void uAsVoid(Module var0, String var1) {
      YAsVoid(var0, var1);
      long var2 = System.nanoTime();
      long var4 = T.getOrDefault(var0, Long.MIN_VALUE);
      if (var4 == Long.MIN_VALUE || var2 - var4 >= 2000000000L) {
         T.put(var0, var2);
         ClientLog.GAsVoid(var0.getName(), "Placement paused: {}", var1);
      }
   }

   public static void WAsVoid(Module var0) {
      b.remove(var0);
      ModuleImportBroker.KAsVoid(var0);
   }

   public static void dAsVoid() {
      b.clear();
      O.clear();
      T.clear();
   }

   public static boolean tAsBoolean(Module var0) {
      BlockPlacementController.Type_e var1 = b.get(var0);
      MinecraftClient var2 = MinecraftClient.getInstance();
      return var1 != null && var2.player != null && rAsBoolean(var1.world, var2.world, var1.player, var2.player, var1.tick, var2.player.age);
   }

   public static int qAsInt(Item var0, boolean var1) {
      ClientPlayerEntity var2 = MinecraftClient.getInstance().player;
      if (var2 == null) {
         return -1;
      } else {
         int var3 = var2.getInventory().getSelectedSlot();
         if (BAsBoolean(var2.getMainHandStack(), var0)) {
            return var3;
         } else if (BAsBoolean(var2.getOffHandStack(), var0)) {
            return -2;
         } else {
            if (var1) {
               for (int var4 = 0; var4 < 9; var4++) {
                  if (BAsBoolean(var2.getInventory().getStack(var4), var0)) {
                     return var4;
                  }
               }
            }

            return -1;
         }
      }
   }

   private static boolean BAsBoolean(ItemStack var0, Item var1) {
      return var1 == Items.AIR ? var0.isEmpty() : var0.isOf(var1);
   }

   public static boolean jAsBoolean(BlockPos var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      return var1.world != null
         && var1.world.isChunkLoaded(var0)
         && !var1.world.isOutOfHeightLimit(var0)
         && var1.world.getWorldBorder().contains(var0)
         && var1.world.getBlockState(var0).isReplaceable();
   }

   private static BlockHitResult hAsBlockHitResult(BlockPos var0, Vec3d var1, double var2, boolean var4) {
      if (!ModuleImportBroker.jAsBoolean()) {
         return null;
      } else {
         MinecraftClient var5 = MinecraftClient.getInstance();
         Vec3d var6 = var5.player.getEyePos();
         double var7 = Math.min(var2, var5.player.getBlockInteractionRange());
         Vec3d var9 = var6.add(var1.normalize().multiply(var7));
         BlockHitResult var10 = var5.world.raycast(new RaycastContext(var6, var9, ShapeType.OUTLINE, FluidHandling.NONE, var5.player));
         if (var10.getType() == Type.BLOCK && var10.getBlockPos().equals(var0) && !AntiTrap.YAsBoolean(var5.world.getBlockState(var0))) {
            if (var4) {
               return var10;
            } else {
               for (Entity var12 : var5.world.getOtherEntities(var5.player, new Box(var6, var10.getPos()).expand(1.0))) {
                  if (var12.canHit()
                     && !var12.isSpectator()
                     && !AntiTrap.TAsBoolean(var12)
                     && !IgnoreEntity.yAsBoolean(var12)
                     && OptifineBridge.EAsBoolean(var6, var10.getPos(), var12.getBoundingBox())) {
                     return null;
                  }
               }

               return var10;
            }
         } else {
            return null;
         }
      }
   }

   public static BlockHitResult MAsBlockHitResult(BlockPos var0, Vec3d var1, Vec3d var2, double var3, boolean var5, BlockPlacementController.Type_w var6) {
      if (!var6.replaceable(var0)) {
         return null;
      } else {
         for (Direction var10 : Direction.values()) {
            BlockPos var11 = var0.offset(var10);
            if (var6.usableSupport(var11)) {
               Direction var12 = var10.getOpposite();

               for (Vec3d var14 : HAsList(var11, var12)) {
                  BlockHitResult var15 = var6.ray(var11, var5 ? var14.subtract(var1) : var2, var3);
                  if (var15 != null && var15.getSide() == var12 && var6.contextMatches(var15, var0)) {
                     return var15;
                  }

                  if (!var5) {
                     break;
                  }
               }
            }
         }

         return null;
      }
   }

   public static List<Vec3d> HAsList(BlockPos var0, Direction var1) {
      Vec3d var2 = Vec3d.ofCenter(var0).add(new Vec3d((double)var1.getOffsetX(), (double)var1.getOffsetY(), (double)var1.getOffsetZ()).multiply(0.499));
      ArrayList var3 = new ArrayList(9);

      for (double var7 : new double[]{0.0, -0.42, 0.42}) {
         for (double var12 : new double[]{0.0, -0.42, 0.42}) {
            var3.add(switch (var1.getAxis()) {
               case X -> var2.add(0.0, var7, var12);
               case Y -> var2.add(var7, 0.0, var12);
               case Z -> var2.add(var7, var12, 0.0);
               default -> throw new MatchException(null, null);
            });
         }
      }

      return var3;
   }

   public static boolean CAsBoolean(BlockPos var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (!var1.world.isChunkLoaded(var0)) {
         return false;
      } else {
         BlockState var2 = var1.world.getBlockState(var0);
         return !var2.isReplaceable()
            && (
               var1.player.isSneaking()
                  || !var2.hasBlockEntity() && !(var2.getBlock() instanceof RespawnAnchorBlock) && !(var2.getBlock() instanceof CraftingTableBlock)
            );
      }
   }

   public static BlockHitResult uAsBlockHitResult(BlockPos var0, Item var1, double var2, boolean var4) {
      return HAsBlockHitResult(var0, var1, var2, var4, true);
   }

   public static BlockHitResult HAsBlockHitResult(BlockPos var0, Item var1, double var2, boolean var4, boolean var5) {
      if (var1 instanceof BlockItem && ModuleImportBroker.jAsBoolean()) {
         MinecraftClient var6 = MinecraftClient.getInstance();
         return MAsBlockHitResult(
            var0,
            var6.player.getEyePos(),
            var6.player.getRotationVector(),
            Math.min(var2, var6.player.getBlockInteractionRange()),
            var4,
            new BlockPlacementController.Type_w() {
               @Override
               public boolean replaceable(BlockPos var1x) {
                  return BlockPlacementController.jAsBoolean(var1x);
               }

               @Override
               public boolean usableSupport(BlockPos var1x) {
                  return BlockPlacementController.CAsBoolean(var1x);
               }

               @Override
               public BlockHitResult ray(BlockPos var1x, Vec3d var2x, double var3) {
                  return BlockPlacementController.hAsBlockHitResult(var1x, var2x, var3, true);
               }

               @Override
               public boolean contextMatches(BlockHitResult var1x, BlockPos var2x) {
                  return BlockPlacementController.hAsBoolean(var1x, var2x, var1, var5);
               }
            }
         );
      } else {
         return null;
      }
   }

   private static boolean hAsBoolean(BlockHitResult var0, BlockPos var1, Item var2, boolean var3) {
      return kAsRe_Type_i(var0, var2, var3, var1) != null;
   }

   public static BlockPlacementController.Type_i vAsRe_Type_i(BlockHitResult var0, Item var1, boolean var2) {
      return kAsRe_Type_i(var0, var1, var2, null);
   }

   private static BlockPlacementController.Type_i kAsRe_Type_i(BlockHitResult var0, Item var1, boolean var2, BlockPos var3) {
      MinecraftClient var4 = MinecraftClient.getInstance();
      int var5 = qAsInt(var1, var2);
      if (var5 != -1 && var1 instanceof BlockItem var6) {
         ItemStack var7 = var5 == -2 ? var4.player.getOffHandStack() : var4.player.getInventory().getStack(var5);
         Hand var8 = var5 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND;
         ItemPlacementContext var9 = new ItemPlacementContext(var4.player, var8, var7, var0);
         if (!var9.canPlace()) {
            return null;
         } else {
            BlockPos var10 = var9.getBlockPos();
            if (var3 != null && !var10.equals(var3)) {
               return null;
            } else {
               BlockState var11 = var6.getBlock().getPlacementState(var9);
               return var11 != null && var11.canPlaceAt(var4.world, var10) && var4.world.canPlace(var11, var10, ShapeContext.ofPlacement(var4.player))
                  ? new BlockPlacementController.Type_i(var10, var11)
                  : null;
            }
         }
      } else {
         return null;
      }
   }

   public static BlockHitResult BAsBlockHitResult(BlockPos var0, Vec3d var1, double var2) {
      return hAsBlockHitResult(var0, var1, var2, true);
   }

   public static boolean sAsBoolean(
      Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, Consumer<Boolean> var8
   ) {
      return JAsBoolean(var0, var1, var2, var3, var5, var6, var7, () -> true, var8);
   }

   public static boolean JAsBoolean(
      Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, BooleanSupplier var8, Consumer<Boolean> var9
   ) {
      if (qAsInt(var2, var6) == -1) {
         YAsVoid(var0, "Required item missing from held hands or hotbar");
         return false;
      } else {
         BlockHitResult var10 = HAsBlockHitResult(var1, var2, var3, var5, var6);
         if (var10 == null) {
            YAsVoid(var0, "No valid supported face, collision-free position or aim within native reach");
            return false;
         } else {
            return nAsBoolean(var0, var1, var10, var2, var3, true, var5, var6, var7, var8, var9);
         }
      }
   }

   public static boolean xAsBoolean(
      Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, Consumer<Boolean> var8
   ) {
      if (ModuleImportBroker.jAsBoolean() && qAsInt(var2, var6) != -1) {
         MinecraftClient var9 = MinecraftClient.getInstance();
         Vec3d var10 = var5 ? Vec3d.ofCenter(var1).subtract(var9.player.getEyePos()) : var9.player.getRotationVector();
         BlockHitResult var11 = hAsBlockHitResult(var1, var10, var3, false);
         if (var11 == null && var5) {
            var11 = hAsBlockHitResult(var1, var9.player.getEyePos(), var3);
         }

         return var11 == null ? false : nAsBoolean(var0, var1, var11, var2, var3, false, var5, var6, var7, () -> true, var8);
      } else {
         return false;
      }
   }

   private static BlockHitResult hAsBlockHitResult(BlockPos var0, Vec3d var1, double var2) {
      Vec3d var4 = Vec3d.ofCenter(var0);

      for (Direction var8 : R) {
         Vec3d var9 = new Vec3d((double)var8.getOffsetX(), (double)var8.getOffsetY(), (double)var8.getOffsetZ());
         if (!(var1.subtract(var4.add(var9.multiply(0.5))).dotProduct(var9) <= 0.0)) {
            for (Vec3d var11 : HAsList(var0, var8)) {
               BlockHitResult var12 = hAsBlockHitResult(var0, var11.subtract(var1), var2, false);
               if (var12 != null && var12.getSide() == var8) {
                  return var12;
               }
            }
         }
      }

      return null;
   }

   private static boolean nAsBoolean(
      Module var0,
      BlockPos var1,
      BlockHitResult var2,
      Item var3,
      double var4,
      boolean var6,
      boolean var7,
      boolean var8,
      BooleanSupplier var9,
      BooleanSupplier var10,
      Consumer<Boolean> var11
   ) {
      MinecraftClient var12 = MinecraftClient.getInstance();
      BlockPlacementController.Type_e var13 = new BlockPlacementController.Type_e(
         var0, var1.toImmutable(), var2, var3, var4, var6, var7, var9, var10, var11, var12.world, var12.player, var12.player.age
      );
      b.put(var0, var13);
      ModuleImportBroker.UAsVoid(
         var0,
         () -> tAsVoid(var13, var8),
         () -> JAsBoolean(var13) && var13.actionReady.getAsBoolean() && qAsInt(var3, var8) != -1 && CAsBlockHitResult(var13, var8) != null
      );
      return true;
   }

   private static boolean JAsBoolean(BlockPlacementController.Type_e var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      return b.get(var0.source) == var0
         && rAsBoolean(var0.world, var1.world, var0.player, var1.player, var0.tick, var1.player == null ? 0 : var1.player.age)
         && var0.source.isRuntimeActive()
         && ModuleImportBroker.jAsBoolean()
         && var1.isWindowFocused()
         && var0.valid.getAsBoolean();
   }

   private static BlockHitResult CAsBlockHitResult(BlockPlacementController.Type_e var0, boolean var1) {
      if (!JAsBoolean(var0)) {
         return null;
      } else {
         MinecraftClient var2 = MinecraftClient.getInstance();
         BlockHitResult var3 = hAsBlockHitResult(var0.hit.getBlockPos(), var2.player.getRotationVector(), var0.range, var0.place);
         if (var3 != null && var3.getSide() == var0.hit.getSide()) {
            if (var0.place && !var2.player.isSneaking()) {
               BlockState var4 = var2.world.getBlockState(var3.getBlockPos());
               if (var4.hasBlockEntity() || var4.getBlock() instanceof RespawnAnchorBlock || var4.getBlock() instanceof CraftingTableBlock) {
                  return null;
               }
            }

            if (var0.place && (!jAsBoolean(var0.target) || !hAsBoolean(var3, var0.target, var0.item, var1))) {
               uAsVoid(var0.source, "Native placement context rejects the current block position, support or entity collision");
               return null;
            } else {
               return var3;
            }
         } else {
            uAsVoid(var0.source, "Current look does not reach the planned support face; check competing rotations or Crosshair aim");
            return null;
         }
      }
   }

   public static void wAsVoid() {
      b.values().removeIf(var0x -> {
         if (JAsBoolean(var0x)) {
            return false;
         } else {
            uAsVoid(var0x.source, "Intent expired or its target, player, world, focus or pause condition changed");
            return true;
         }
      });
      Optional var0 = b.values()
         .stream()
         .filter(var0x -> var0x.visible && JAsBoolean(var0x) && ModuleImportBroker.nAsBoolean(var0x.source))
         .max(Comparator.comparingInt(var0x -> ModuleImportBroker.bAsInt(var0x.source.getName())));
      if (var0.isEmpty() || !(((BlockPlacementController.Type_e)var0.get()).source instanceof AutoTrap)) {
         ParticleEmitter.GAsVoid(ParticleEmitter.Type_u.AUTOTRAP);
      }

      if (var0.isEmpty() || !(((BlockPlacementController.Type_e)var0.get()).source instanceof AutoWeb)) {
         ParticleEmitter.GAsVoid(ParticleEmitter.Type_u.AUTOWEB);
      }

      if (var0.isEmpty() || !(((BlockPlacementController.Type_e)var0.get()).source instanceof AutoAnchor)) {
         ParticleEmitter.GAsVoid(ParticleEmitter.Type_u.AUTOANCHOR);
      }

      if (var0.isPresent() && !(((BlockPlacementController.Type_e)var0.get()).source instanceof Scaffold)) {
         ParticleEmitter.GAsVoid(ParticleEmitter.Type_u.SCAFFOLD);
      }

      if (!var0.isEmpty()) {
         BlockPlacementController.Type_e var1 = (BlockPlacementController.Type_e)var0.get();
         MinecraftClient var2 = MinecraftClient.getInstance();
         float[] var3 = ModuleDetector.QAsFloatArray(var2.player.getEyePos(), var1.hit.getPos());
         if (var1.source instanceof BlockPlacementController.Type_n var4) {
            var4.aimPlacement(var3[0], var3[1]);
         } else {
            ModuleDetector.yAsVoid(ModuleDetector.Type_s.PLACEMENT, var3[0], var3[1], 1.0);
         }

         ModuleDetector.aAsVoid();
      }
   }

   private static void tAsVoid(BlockPlacementController.Type_e var0, boolean var1) {
      if (var0.actionReady.getAsBoolean()) {
         BlockHitResult var2 = CAsBlockHitResult(var0, var1);
         int var3 = qAsInt(var0.item, var1);
         if (var2 != null && var3 != -1) {
            MinecraftClient var4 = MinecraftClient.getInstance();
            int var5 = var4.player.getInventory().getSelectedSlot();

            try {
               if (var3 >= 0) {
                  var4.player.getInventory().setSelectedSlot(var3);
               }

               Hand var6 = var3 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND;
               boolean var7 = var4.interactionManager.interactBlock(var4.player, var6, var2).isAccepted();
               if (var7) {
                  var4.player.swingHand(var6);
               }

               var0.completed.accept(var7);
            } finally {
               var4.player.getInventory().setSelectedSlot(var5);
               b.remove(var0.source, var0);
            }
         } else {
            uAsVoid(var0.source, "Execution lost its valid support ray or inventory item");
         }
      }
   }

   private BlockPlacementController() {
   }

   private static record Type_e(
      Module source,
      BlockPos target,
      BlockHitResult hit,
      Item item,
      double range,
      boolean place,
      boolean visible,
      BooleanSupplier valid,
      BooleanSupplier actionReady,
      Consumer<Boolean> completed,
      Object world,
      Object player,
      int tick
   ) {
   }

   public static record Type_i(BlockPos target, BlockState state) {
   }

   public abstract static class Type_k extends Module implements BlockPlacementController.Type_n {
      protected final NumberSetting h = this.KAsNumberSetting("Range", "Placement reach capped by native block reach", 4.1, 1.0, 6.0, 0.1);
      protected final NumberSetting p = this.KAsNumberSetting("Target Range", "Search distance for survival opponents", 6.0, 1.0, 12.0, 0.5);
      protected final NumberSetting d;
      protected final ModeSetting rotation;
      protected final BooleanSetting J;
      protected final BooleanSetting Q;
      protected final BooleanSetting j;
      private long k;
      private String B = "Waiting for a target";

      protected Type_k(String var1, String var2) {
         this(var1, var2, 0);
      }

      protected Type_k(String var1, String var2, int var3) {
         super(var1, var2, Category.COMBAT, -1);
         this.d = this.KAsNumberSetting("Delay Per Action", "Ticks between native placement attempts", (double)var3, 0.0, 20.0, 1.0);
         this.rotation = this.addSetting(
            new ModeSetting("Rotation", "Visible turns the player; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
         );
         this.J = this.UAsBooleanSetting("Auto Switch", "Use hotbar blocks and restore the selected slot", true);
         this.Q = this.UAsBooleanSetting("Pause Using Item", "Pause while eating or using an item", true);
         this.j = this.UAsBooleanSetting("Pause Mining", "Pause while the attack key is held", true);
      }

      protected void aim(float var1, float var2) {
         if (this.isEnabled()) {
            ModuleDetector.yAsVoid(ModuleDetector.Type_s.PLACEMENT, var1, var2, 1.0);
         }
      }

      @Override
      public final void aimPlacement(float var1, float var2) {
         if (this.isEnabled()) {
            this.aim(var1, var2);
         }
      }

      protected final void WAsVoid(boolean var1) {
         this.k = mc.world.getTime() + (long)Math.max(1, this.d.getValue().intValue());
         this.KAsVoid(var1 ? "Waiting for world update" : "Placement rejected; retrying");
      }

      protected final NumberSetting KAsNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
         return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
      }

      protected final BooleanSetting UAsBooleanSetting(String var1, String var2, boolean var3) {
         return this.addSetting(new BooleanSetting(var1, var2, var3));
      }

      @Override
      public Module.Type_s getTickPhase() {
         return Module.Type_s.INTENT;
      }

      @Override
      public Set<com.larpclient.module.k> getNetworkImpacts() {
         return Set.of(com.larpclient.module.k.SERVER_STATE_MODIFYING);
      }

      protected final void KAsVoid(String var1) {
         if (!var1.equals(this.B) && !var1.startsWith("Preparing") && !var1.equals("Waiting for world update")) {
            BlockPlacementController.uAsVoid(this, var1);
         }

         this.B = var1;
      }

      @Override
      public String getStatus() {
         return this.B;
      }

      protected final boolean rAsBoolean() {
         return this.isEnabled() ? this.cAsBoolean() && mc.world.getTime() >= this.k : false;
      }

      protected final boolean cAsBoolean() {
         return this.isEnabled()
            ? ModuleImportBroker.jAsBoolean()
               && mc.isWindowFocused()
               && (!this.Q.getValue() || !mc.player.isUsingItem())
               && (!this.j.getValue() || !mc.options.attackKey.isPressed())
            : false;
      }

      protected final boolean PAsBoolean(BlockPos var1, Item var2) {
         return this.isEnabled()
            ? BlockPlacementController.qAsInt(var2, this.J.getValue()) != -1
               && BlockPlacementController.HAsBlockHitResult(var1, var2, this.h.getValue(), this.rotation.pAsBoolean("Visible"), this.J.getValue()) != null
            : false;
      }

      protected final boolean oAsBoolean(PlayerEntity var1) {
         if (var1 != null
            && var1 != mc.player
            && var1.getEntityWorld() == mc.world
            && var1.isAlive()
            && !var1.isSpectator()
            && !var1.isCreative()
            && !var1.isRemoved()
            && !(mc.player.squaredDistanceTo(var1) > this.p.getValue() * this.p.getValue())) {
            MCFriend var2 = LarpClient.CAsLarpClient().mAsF().EAsModule(MCFriend.class);
            return var2 == null || !var2.SAsBoolean(var1.getName().getString());
         } else {
            return false;
         }
      }

      protected final PlayerEntity qAsPlayerEntity() {
         return mc.world.getPlayers().stream().filter(this::oAsBoolean).min(Comparator.comparingDouble(mc.player::squaredDistanceTo)).orElse(null);
      }

      protected final boolean LAsBoolean(BlockPos var1, Item var2, PlayerEntity var3) {
         return this.isEnabled()
            ? BlockPlacementController.sAsBoolean(
               this,
               var1,
               var2,
               this.h.getValue(),
               this.rotation.pAsBoolean("Visible"),
               this.J.getValue(),
               () -> this.rAsBoolean() && this.oAsBoolean(var3),
               this::WAsVoid
            )
            : false;
      }

      @Override
      public void onEnable() {
         this.k = 0L;
         BlockPlacementController.T.remove(this);
         BlockPlacementController.O.remove(this);
         ClientLog.GAsVoid(
            this.getName(), "Native placement enabled; rotation={}, range={}, autoSwitch={}", this.rotation.getValue(), this.h.getValue(), this.J.getValue()
         );
      }

      @Override
      public void onDisable() {
         BlockPlacementController.WAsVoid(this);
         this.k = 0L;
         this.B = "Waiting for a target";
         ClientLog.XAsVoid(this.getName(), "Placement intents cleared");
      }
   }

   public interface Type_n {
      void aimPlacement(float var1, float var2);
   }

   public interface Type_w {
      boolean replaceable(BlockPos var1);

      boolean usableSupport(BlockPos var1);

      BlockHitResult ray(BlockPos var1, Vec3d var2, double var3);

      boolean contextMatches(BlockHitResult var1, BlockPos var2);
   }
}
