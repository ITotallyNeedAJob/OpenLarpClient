package com.larpclient.module;

import com.larpclient.BlockPlacementController;
import com.larpclient.ClientLog;
import com.larpclient.LarpClient;
import com.larpclient.PlacementPreview;
import com.larpclient.ModuleImportBroker;
import com.larpclient.FallDamage;
import com.larpclient.ModuleDetector;
import com.larpclient.NebulaSky;
import com.larpclient.ParticleEmitter;
import com.larpclient.WorldUpdater;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class AutoCart extends Module implements PlacementPreview.Type_e {
   private static final Item[] l = new Item[]{Items.RAIL, Items.POWERED_RAIL, Items.DETECTOR_RAIL, Items.ACTIVATOR_RAIL};
   private static final int K = 20;
   private static final int o = 40;
   private static AutoCart M;
   private final ModeSetting rotation = this.addSetting(
      new ModeSetting(
         "Rotation", "Visible picks a rail spot by an opponent and turns the real view; Crosshair uses the block you aim at", "Visible", "Visible", "Crosshair"
      )
   );
   private final NumberSetting u = this.PAsNumberSetting("Turn Speed", "Maximum visible turn in degrees per tick", 60.0, 5.0, 180.0, 5.0);
   private final NumberSetting r = this.PAsNumberSetting("Charge", "Bow draw ticks before release; 20 is full power", 20.0, 3.0, 20.0, 1.0);
   private final NumberSetting e = this.PAsNumberSetting("Range", "Rail and cart reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final BooleanSetting x = this.XAsBooleanSetting("Players", "Target other survival players", true);
   private final BooleanSetting v = this.XAsBooleanSetting("Mobs", "Also target hostile mobs for combat or testing", false);
   private final NumberSetting s = this.PAsNumberSetting("Target Range", "Search distance for opponents", 7.0, 2.0, 12.0, 0.5);
   private final NumberSetting k = this.PAsNumberSetting("Min Damage", "Minimum expected target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final BooleanSetting y = this.XAsBooleanSetting("Anti Self", "Reject shots whose strongest possible blast is excessive or lethal for you", true);
   private final NumberSetting c = this.PAsNumberSetting(
      "Max Self Damage", "Maximum self damage from the strongest possible blast, in HP", 10.0, 0.0, 36.0, 0.5
   );
   private final PlacementPreview C;
   private AutoCart.Type_l N = AutoCart.Type_l.IDLE;
   private AutoCart.Type_t U;
   private AutoCart.Type_t b;
   private AutoCart.Type_k R;
   private double[] X;
   private BlockPos f;
   private Vec3d W;
   private long D;
   private long H = Long.MIN_VALUE;
   private float w = -1.0F;
   private Object G;
   private Object T;
   private String P = "Draw a Flame bow to arm";
   private String S = "";

   public AutoCart() {
      super("AutoCart", "Release a drawn Flame bow into a freshly placed TNT cart", Category.COMBAT, -1);
      M = this;
      this.u.visibleWhen(this::gAsBoolean);

      for (Setting var2 : List.of(this.x, this.v, this.s, this.k)) {
         var2.visibleWhen(this::gAsBoolean);
      }

      this.c.visibleWhen(this.y::getValue);
      this.rotation.onChanged(var1 -> this.LAsVoid("Rotation changed"));
      this.C = new PlacementPreview(this, var1 -> this.addSetting(var1));
   }

   private NumberSetting PAsNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   private BooleanSetting XAsBooleanSetting(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   public List<Setting<?>> QAsList() {
      return List.of(this.rotation, this.u);
   }

   public List<Setting<?>> YAsList() {
      return List.of(this.r, this.e);
   }

   public List<Setting<?>> dAsList() {
      return List.of(this.x, this.v, this.s, this.k);
   }

   public List<Setting<?>> qAsList() {
      return List.of(this.y, this.c);
   }

   @Override
   public PlacementPreview placementHighlight() {
      return this.C;
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
      return this.isEnabled() ? this.P : "";
   }

   public static void KAsVoid(Vec3d var0, float var1) {
      if (M != null && M.isEnabled()) {
         AutoCart var2 = M;
         if (var2 != null && var2.N == AutoCart.Type_l.CONFIRM && var2.W != null && var0.squaredDistanceTo(var2.W) < 1.0) {
            var2.w = var1;
         }
      }
   }

   @Override
   public void onTick() {
      this.C.mAsVoid(null);
      if (!this.isInGame()) {
         this.LAsVoid("Waiting for a world");
      } else {
         if (this.G != mc.player || this.T != mc.world) {
            this.LAsVoid("Draw a Flame bow to arm");
            this.G = mc.player;
            this.T = mc.world;
         }

         long var1 = mc.world.getTime();
         if (this.N != AutoCart.Type_l.CONFIRM || !this.wAsBoolean(var1)) {
            if (ModuleImportBroker.jAsBoolean() && mc.isWindowFocused() && ParticleEmitter.NAsP5_Type_u() == ParticleEmitter.Type_u.NONE) {
               int var3 = this.TAsInt();
               if (var3 < 0) {
                  this.EAsVoid("Draw a Flame bow to arm");
               } else {
                  Item var4 = bAsItem();
                  if (var4 == null || BlockPlacementController.qAsInt(Items.TNT_MINECART, true) == -1) {
                     this.EAsVoid("Rails and TNT carts must be in your hotbar or hands");
                  } else if (Aura.WAsBoolean()) {
                     this.EAsVoid("Paused while Kill Aura lowers your shield");
                  } else {
                     double[] var5 = WorldUpdater.tAsDoubleArray2(WorldUpdater.BAsDouble(Math.max(var3, this.r.getValue().intValue())));
                     this.U = this.gAsBoolean() ? this.WAsAutoCart_Type_t(var4, var5) : this.KAsAutoCart_Type_t(var4, var5);
                     if (this.U == null) {
                        this.N = AutoCart.Type_l.IDLE;
                        this.R = null;
                        this.tAsVoid(this.S);
                     } else {
                        this.N = AutoCart.Type_l.AIM;
                        this.C.mAsVoid(this.U.rail());
                        if (this.gAsBoolean() && ModuleImportBroker.nAsBoolean(this)) {
                           float[] var6 = ModuleDetector.QAsFloatArray(mc.player.getEyePos(), this.U.aim());
                           float[] var7 = com.larpclient.SmoothInput.JAsFloatArray(mc.player.getYaw(), mc.player.getPitch(), var6[0], var6[1], this.u.getValue());
                           ModuleDetector.RAsVoid(this, ModuleDetector.Type_s.AUTOCART, var7[0], var7[1], 1.0);
                        }

                        double var9;
                        if (((var9 = (double)var3 - this.r.getValue()) == 0.0 ? 0 : (var9 < 0.0 ? -1 : 1)) < 0) {
                           this.P = String.format(Locale.ROOT, "Drawing %d/%d; expected %.1f HP", var3, this.r.getValue().intValue(), this.U.damage());
                        } else {
                           this.P = String.format(Locale.ROOT, "Armed; expected %.1f HP, worst self %.1f HP", this.U.damage(), this.U.self());
                           AutoCart.Type_t var8 = this.U;
                           ModuleImportBroker.UAsVoid(this, this::gAsVoid, () -> this.U == var8 && this.N == AutoCart.Type_l.AIM && this.ZAsBoolean());
                        }
                     }
                  }
               }
            } else {
               this.EAsVoid("Paused: screen, focus or another camera owner");
            }
         }
      }
   }

   private AutoCart.Type_t WAsAutoCart_Type_t(Item var1, double[] var2) {
      List var3 = this.MAsList();
      if (var3.isEmpty()) {
         return this.VAsObject(this.v.getValue() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)");
      } else {
         if (this.U != null && this.U.target() != null && var3.contains(this.U.target())) {
            AutoCart.Type_t var4 = this.hAsAutoCart_Type_t(this.U.rail(), this.U.aim(), var1, var2, var3);
            if (var4 != null) {
               return var4;
            }
         }

         AutoCart.Type_t var13 = null;
         HashSet var5 = new HashSet();

         for (LivingEntity var7 : (Iterable<LivingEntity>) (var3)) {
            for (int var8 = -1; var8 <= 0; var8++) {
               for (int var9 = -1; var9 <= 1; var9++) {
                  for (int var10 = -1; var10 <= 1; var10++) {
                     BlockPos var11 = var7.getBlockPos().add(var9, var8, var10);
                     if (var5.add(var11)) {
                        AutoCart.Type_t var12 = this.hAsAutoCart_Type_t(var11, null, var1, var2, var3);
                        if (var12 != null && (var13 == null || jAsDouble(var12) > jAsDouble(var13))) {
                           var13 = var12;
                        }
                     }
                  }
               }
            }
         }

         return var13 != null ? var13 : this.VAsObject("No reachable rail spot meets the damage limits");
      }
   }

   private static double jAsDouble(AutoCart.Type_t var0) {
      return var0.damage() - var0.self() * 0.25;
   }

   private AutoCart.Type_t hAsAutoCart_Type_t(BlockPos var1, Vec3d var2, Item var3, double[] var4, List<LivingEntity> var5) {
      if (!mc.world.isChunkLoaded(var1)) {
         return null;
      } else if (!mc.world.getBlockState(var1).isIn(BlockTags.RAILS) && !BlockPlacementController.jAsBoolean(var1)) {
         return null;
      } else {
         Vec3d var6 = mc.player.getEyePos();
         double var7 = this.rAsDouble();
         if (var6.squaredDistanceTo(Vec3d.ofCenter(var1)) > (var7 + 1.0) * (var7 + 1.0)) {
            return null;
         } else {
            for (Vec3d var10 : var2 != null ? List.of(var2) : gAsList(var1)) {
               Vec3d var11 = var10.subtract(var6);
               AutoCart.Type_k var12 = this.mAsAutoCart_Type_k(var11, var11, var1, var3);
               if (var12 != null) {
                  return this.NAsAutoCart_Type_t(var12, var10, var4, var5, true);
               }
            }

            return null;
         }
      }
   }

   private static List<Vec3d> gAsList(BlockPos var0) {
      ArrayList var1 = new ArrayList(9);

      for (double var5 : new double[]{0.0, -0.42, 0.42}) {
         for (double var10 : new double[]{0.0, -0.42, 0.42}) {
            var1.add(new Vec3d((double)var0.getX() + 0.5 + var5, (double)var0.getY() + 0.0625, (double)var0.getZ() + 0.5 + var10));
         }
      }

      return var1;
   }

   private AutoCart.Type_t KAsAutoCart_Type_t(Item var1, double[] var2) {
      Vec3d var3 = mc.player.getRotationVector();
      AutoCart.Type_k var4 = this.mAsAutoCart_Type_k(var3, var3, null, var1);
      return var4 == null ? null : this.NAsAutoCart_Type_t(var4, var4.center(), var2, this.MAsList(), false);
   }

   private AutoCart.Type_t NAsAutoCart_Type_t(AutoCart.Type_k var1, Vec3d var2, double[] var3, List<LivingEntity> var4, boolean var5) {
      double var6 = (double)NebulaSky.TAsFloat(mc.player, var1.center(), var3[2]);
      if (this.y.getValue() && !NebulaSky.uAsBoolean(var6, this.c.getValue(), (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount()))) {
         return this.VAsObject(String.format(Locale.ROOT, "Worst-case self damage %.1f HP is over the limit", var6));
      } else {
         LivingEntity var8 = null;
         double var9 = 0.0;

         for (LivingEntity var12 : var4) {
            double var13 = WorldUpdater.jAsDouble(NebulaSky.wAsFloatArray(var12, var1.center(), var3));
            if (var13 > var9) {
               var9 = var13;
               var8 = var12;
            }
         }

         return !var5 || var8 != null && !(var9 < this.k.getValue())
            ? new AutoCart.Type_t(var1.rail(), var2, var8, var9, var6)
            : this.VAsObject(String.format(Locale.ROOT, "Expected %.1f HP is below Min Damage", var9));
      }
   }

   private AutoCart.Type_k mAsAutoCart_Type_k(Vec3d var1, Vec3d var2, BlockPos var3, Item var4) {
      Vec3d var5 = mc.player.getEyePos();
      double var6 = this.rAsDouble();
      Vec3d var8 = var5.add(var1.normalize().multiply(var6));
      BlockHitResult var9 = mc.world.raycast(new RaycastContext(var5, var8, ShapeType.OUTLINE, FluidHandling.NONE, mc.player));
      if (var9.getType() != Type.BLOCK) {
         return this.VAsObject("Aim at the top of a block within reach");
      } else {
         BlockHitResult var12 = null;
         BlockState var13 = mc.world.getBlockState(var9.getBlockPos());
         BlockPos var10;
         BlockState var11;
         if (var13.isIn(BlockTags.RAILS)) {
            if (BlockPlacementController.BAsBlockHitResult(var9.getBlockPos(), var1, var6) == null) {
               return this.VAsObject("That rail is outside native reach");
            }

            var10 = var9.getBlockPos();
            var11 = var13;
         } else {
            var12 = BlockPlacementController.BAsBlockHitResult(var9.getBlockPos(), var1, var6);
            BlockPlacementController.Type_i var14 = var12 == null ? null : BlockPlacementController.vAsRe_Type_i(var12, var4, true);
            if (var14 == null) {
               return this.VAsObject("A rail cannot be placed on that support");
            }

            var10 = var14.target();
            var11 = var14.state();
         }

         if (var3 != null && !var10.equals(var3)) {
            return this.VAsObject("Aim does not reach the planned rail");
         } else if (var12 != null && var11.getOutlineShape(mc.world, var10).raycast(var5, var8, var10) == null) {
            return this.VAsObject("Aim at a top face so the cart lands on the new rail");
         } else {
            byte var10000;
            label75: {
               if (var11.getBlock() instanceof AbstractRailBlock var15 && ((RailShape)var11.get(var15.getShapeProperty())).isAscending()) {
                  var10000 = 1;
                  break label75;
               }

               var10000 = 0;
            }

            int var23 = var10000;
            Vec3d var24 = new Vec3d((double)var10.getX() + 0.5, (double)var10.getY() + 0.0625 + (var23 != 0 ? 0.5 : 0.0), (double)var10.getZ() + 0.5);
            Box var25 = EntityType.TNT_MINECART.getDimensions().getBoxAt(var24);
            if (!mc.world.getOtherEntities((Entity)null, var25, var0 -> var0 instanceof AbstractMinecartEntity).isEmpty()) {
               return this.VAsObject("A minecart already occupies that rail");
            } else {
               Vec3d var17 = var5.subtract(0.0, 0.1, 0.0);
               Vec3d var18 = var17.add(var2.normalize().multiply(var17.distanceTo(var24) + 2.0));
               BlockHitResult var19 = mc.world.raycast(new RaycastContext(var17, var18, ShapeType.COLLIDER, FluidHandling.NONE, mc.player));
               if (var19.getType() != Type.MISS) {
                  var18 = var19.getPos();
               }

               ArrayList var20 = new ArrayList();

               for (Entity var22 : mc.world
                  .getOtherEntities(mc.player, new Box(var17, var18).expand(1.0), var0 -> var0.canBeHitByProjectile() && !var0.isSpectator())) {
                  var20.add(var22.getBoundingBox());
               }

               if (!WorldUpdater.IAsBoolean(var17, var18, var25, var20)) {
                  return this.VAsObject("The arrow would hit something before the cart");
               } else {
                  return !this.PAsBoolean(var17) && !this.PAsBoolean(var24)
                     ? new AutoCart.Type_k(var10, var12, var4, var24)
                     : this.VAsObject("Rain or water would put out the Flame arrow");
               }
            }
         }
      }
   }

   private boolean PAsBoolean(Vec3d var1) {
      BlockPos var2 = BlockPos.ofFloored(var1);
      return mc.world.hasRain(var2) || mc.world.getFluidState(var2).isIn(FluidTags.WATER);
   }

   private boolean ZAsBoolean() {
      this.R = null;
      int var1 = this.TAsInt();
      if (this.isRuntimeActive()
         && this.U != null
         && !((double)var1 < this.r.getValue())
         && !Aura.WAsBoolean()
         && (this.U.target() == null || !this.gAsBoolean() || this.HAsBoolean(this.U.target()))) {
         Item var2 = bAsItem();
         if (var2 != null && BlockPlacementController.qAsInt(Items.TNT_MINECART, true) != -1) {
            Vec3d var3 = mc.player.getRotationVector();
            Vec3d var4 = FallDamage.JAsVec3d(var3);
            AutoCart.Type_k var5 = this.mAsAutoCart_Type_k(var3, var4, this.U.rail(), var2);
            if (var5 == null) {
               this.tAsVoid(var4.dotProduct(var3) < Math.cos(Math.toRadians(0.5)) ? "Waiting for the aim to reach the server" : this.S);
               return false;
            } else {
               double[] var6 = WorldUpdater.tAsDoubleArray2(WorldUpdater.BAsDouble(var1));
               AutoCart.Type_t var7 = this.NAsAutoCart_Type_t(
                  var5, this.U.aim(), var6, this.U.target() == null ? List.of() : List.of(this.U.target()), this.gAsBoolean()
               );
               if (var7 == null) {
                  this.tAsVoid(this.S);
                  return false;
               } else {
                  this.R = var5;
                  this.b = var7;
                  this.X = var6;
                  return true;
               }
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void gAsVoid() {
      AutoCart.Type_k var1 = this.R;
      this.R = null;
      if (var1 != null) {
         PlayerInventory var2 = mc.player.getInventory();
         int var3 = var2.getSelectedSlot();
         int var4 = var1.railHit() == null ? var3 : BlockPlacementController.qAsInt(var1.railItem(), true);
         int var5 = BlockPlacementController.qAsInt(Items.TNT_MINECART, true);
         if (var4 != -1 && var5 != -1) {
            int var6 = this.TAsInt();

            try {
               mc.interactionManager.stopUsingItem(mc.player);
               if (var1.railHit() != null && !this.rAsBoolean(var4, var1.railHit())) {
                  this.hAsVoid("Rail placement rejected; the arrow flew without a cart");
                  return;
               }

               BlockHitResult var7 = BlockPlacementController.BAsBlockHitResult(var1.rail(), mc.player.getRotationVector(), this.rAsDouble());
               if (var7 == null || !mc.world.getBlockState(var1.rail()).isIn(BlockTags.RAILS)) {
                  this.hAsVoid("Placed rail is off the look; the arrow flew without a cart");
                  return;
               }

               if (!this.rAsBoolean(var5, var7)) {
                  this.hAsVoid("Cart use rejected; the arrow flew without a cart");
                  return;
               }
            } finally {
               var2.setSelectedSlot(var3);
            }

            this.N = AutoCart.Type_l.CONFIRM;
            this.f = var1.rail();
            this.W = var1.center();
            this.D = mc.world.getTime();
            this.w = -1.0F;
            this.P = "Released; waiting for detonation";
            ClientLog.GAsVoid(
               "AutoCart",
               "Released after {} draw ticks into a cart on {}{}; power {} to {}, expected {} HP{}, worst self {} HP",
               var6,
               var1.rail(),
               var1.railHit() == null ? " (existing rail)" : "",
               tAsString(this.X[0]),
               tAsString(this.X[2]),
               tAsString(this.b.damage()),
               this.b.target() == null ? "" : " to " + this.b.target().getName().getString(),
               tAsString(this.b.self())
            );
            return;
         }
      }
   }

   private boolean rAsBoolean(int var1, BlockHitResult var2) {
      if (var1 >= 0) {
         mc.player.getInventory().setSelectedSlot(var1);
      }

      Hand var3 = var1 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND;
      boolean var4 = mc.interactionManager.interactBlock(mc.player, var3, var2).isAccepted();
      if (var4) {
         mc.player.swingHand(var3);
      }

      return var4;
   }

   private boolean wAsBoolean(long var1) {
      if (this.w >= 0.0F) {
         ClientLog.GAsVoid("AutoCart", "Cart on {} detonated {} ticks after release with server power {}", this.f, var1 - this.D, tAsString((double)this.w));
         this.wAsVoid(String.format(Locale.ROOT, "Detonated with power %.2f", this.w));
         return false;
      } else if (var1 - this.D <= 20L) {
         return true;
      } else {
         BlockPos var3 = this.f;
         int var4 = !mc.world.getOtherEntities((Entity)null, new Box(var3), var0 -> var0 instanceof TntMinecartEntity).isEmpty() ? 1 : 0;
         this.hAsVoid(
            (var4 != 0 ? "No detonation; the cart remains, so the arrow missed or was put out" : "No detonation observed within one second")
               + " at "
               + var3.toShortString()
         );
         return false;
      }
   }

   private boolean HAsBoolean(LivingEntity var1) {
      if (var1 == mc.player
         || !var1.isAlive()
         || var1.isRemoved()
         || var1.isSpectator()
         || var1.getEntityWorld() != mc.world
         || (double)mc.player.distanceTo(var1) > this.s.getValue()
         || mc.player.isTeammate(var1)) {
         return false;
      } else if (var1 instanceof PlayerEntity var2) {
         if (this.x.getValue() && !var2.getAbilities().invulnerable) {
            MCFriend var3 = LarpClient.CAsLarpClient().mAsF().EAsModule(MCFriend.class);
            return var3 == null || !var3.SAsBoolean(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.v.getValue() && var1 instanceof Monster;
      }
   }

   private List<LivingEntity> MAsList() {
      ArrayList var1 = new ArrayList();

      for (Entity var3 : mc.world.getEntities()) {
         if (var3 instanceof LivingEntity) {
            LivingEntity var4 = (LivingEntity)var3;
            if (this.HAsBoolean(var4)) {
               var1.add(var4);
            }
         }
      }

      var1.sort(Comparator.comparingDouble(entity -> mc.player.squaredDistanceTo((net.minecraft.entity.Entity)entity)));
      return var1.size() > 4 ? new ArrayList<>(var1.subList(0, 4)) : var1;
   }

   private int TAsInt() {
      ClientPlayerEntity var1 = mc.player;
      if (var1 != null && var1.isUsingItem()) {
         ItemStack var2 = var1.getActiveItem();
         return var2.isOf(Items.BOW) && NAsBoolean(var2) ? var1.getItemUseTime() : -1;
      } else {
         return -1;
      }
   }

   private static boolean NAsBoolean(ItemStack var0) {
      ItemEnchantmentsComponent var1 = (ItemEnchantmentsComponent)var0.get(DataComponentTypes.ENCHANTMENTS);
      if (var1 == null) {
         return false;
      } else {
         for (Entry var3 : var1.getEnchantmentEntries()) {
            if (((RegistryEntry)var3.getKey()).matchesKey(Enchantments.FLAME)) {
               return true;
            }
         }

         return false;
      }
   }

   private static Item bAsItem() {
      for (Item var3 : l) {
         if (BlockPlacementController.qAsInt(var3, true) != -1) {
            return var3;
         }
      }

      return null;
   }

   private boolean gAsBoolean() {
      return this.rotation.pAsBoolean("Visible");
   }

   private double rAsDouble() {
      return Math.min(this.e.getValue(), mc.player.getBlockInteractionRange());
   }

   private static String tAsString(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private <T> T VAsObject(String var1) {
      this.S = var1;
      return null;
   }

   private void tAsVoid(String var1) {
      long var2 = mc.world == null ? 0L : mc.world.getTime();
      if (!var1.equals(this.P) && (this.H == Long.MIN_VALUE || var2 < this.H || var2 - this.H >= 40L)) {
         ClientLog.XAsVoid("AutoCart", "{}", var1);
         this.H = var2;
      }

      this.P = var1;
   }

   private void EAsVoid(String var1) {
      this.N = AutoCart.Type_l.IDLE;
      this.U = null;
      this.R = null;
      this.tAsVoid(var1);
   }

   private void hAsVoid(String var1) {
      ClientLog.GAsVoid("AutoCart", "{}", var1);
      this.wAsVoid(var1);
   }

   private void wAsVoid(String var1) {
      this.N = AutoCart.Type_l.IDLE;
      this.U = null;
      this.R = null;
      this.f = null;
      this.W = null;
      this.w = -1.0F;
      this.P = var1;
   }

   private void LAsVoid(String var1) {
      ModuleImportBroker.KAsVoid(this);
      this.C.rAsVoid();
      this.wAsVoid(var1);
   }

   @Override
   public void onEnable() {
      this.G = this.T = null;
      this.H = Long.MIN_VALUE;
      this.LAsVoid("Draw a Flame bow to arm");
      this.C.SAsVoid();
      ClientLog.GAsVoid(
         "AutoCart", "Insta-cart armed; rotation={}, charge={} ticks, range={}", this.rotation.getValue(), this.r.getValue().intValue(), this.e.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.LAsVoid("Draw a Flame bow to arm");
      this.G = this.T = null;
      ClientLog.GAsVoid("AutoCart", "Insta-cart disarmed; aim, pending release and detonation wait cleared");
   }

   private static record Type_k(BlockPos rail, BlockHitResult railHit, Item railItem, Vec3d center) {
   }

   private static enum Type_l {
      IDLE,
      AIM,
      CONFIRM;

      private Type_l() {
      }
   }

   private static record Type_t(BlockPos rail, Vec3d aim, LivingEntity target, double damage, double self) {
   }
}
