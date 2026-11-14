package com.larpclient.module;

import com.larpclient.BlockPlacementController;
import com.larpclient.ClientLog;
import com.larpclient.LarpClient;
import com.larpclient.PlacementPreview;
import com.larpclient.SharedRenderEffects;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ModuleDetector;
import com.larpclient.MusicSync;
import com.larpclient.NebulaSky;
import com.larpclient.ParticleEmitter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.world.attribute.EnvironmentAttributes;

public final class AutoAnchor extends Module implements PlacementPreview.Type_e, BlockPlacementController.Type_n {
   private static final int k = 12;
   private static final int C = 2;
   private static AutoAnchor j;
   private final NumberSetting w = this.IAsNumberSetting("Range", "Anchor reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final NumberSetting S = this.IAsNumberSetting("Delay", "Ticks between confirmed anchor steps", 0.0, 0.0, 20.0, 1.0);
   private final BooleanSetting G = this.NAsBooleanSetting("Auto Switch", "Use hotbar items and restore the selected slot after each step", true);
   private final BooleanSetting P = this.NAsBooleanSetting(
      "Safe Anchor", "Place a block between you and the anchor before detonating, where it leaves you least exposed", true
   );
   private final ModeSetting shieldBlock = this.addSetting(
      new ModeSetting("Shield Block", "Block placed as the shield; the blast hurts players before it breaks blocks", "Glowstone", "Glowstone", "Obsidian")
   );
   private final BooleanSetting c = this.NAsBooleanSetting("Players", "Target other survival players", true);
   private final BooleanSetting X = this.NAsBooleanSetting("Mobs", "Also target hostile mobs for combat or testing", false);
   private final NumberSetting e = this.IAsNumberSetting("Target Range", "Search distance for opponents", 8.0, 2.0, 14.0, 0.5);
   private final NumberSetting i = this.IAsNumberSetting("Min Damage", "Minimum estimated target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final BooleanSetting l = this.NAsBooleanSetting("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final NumberSetting z = this.IAsNumberSetting("Max Self Damage", "Maximum estimated self damage in HP", 8.0, 0.0, 36.0, 0.5);
   private final BooleanSetting r = this.NAsBooleanSetting("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final ModeSetting rotation = this.addSetting(
      new ModeSetting("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final PlacementPreview M;
   private AutoAnchor.Type_u J;
   private String p;
   private String D = "";
   private String F = "";
   private String f = "Waiting for a target";
   private BooleanSupplier L;
   private long Y;
   private long s;
   private Object y;
   private Object E;
   private Object V;
   private Object A;
   private int a;

   public AutoAnchor() {
      super("AutoAnchor", "Automatically place, charge and detonate respawn anchors", Category.COMBAT, -1);
      j = this;
      this.shieldBlock.visibleWhen(this.P::getValue);
      this.z.visibleWhen(this.l::getValue);
      this.rotation.onChanged(var1 -> {
         this.ZAsVoid("Rotation changed");
         this.dAsVoid();
      });
      this.M = new PlacementPreview(this, var1 -> this.addSetting(var1), 0.5, 0.3);
   }

   private NumberSetting IAsNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   private BooleanSetting NAsBooleanSetting(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   public static AutoAnchor SAsAutoAnchor() {
      return j;
   }

   public List<Setting<?>> CAsList() {
      return List.of(this.w, this.S, this.G, this.P, this.shieldBlock);
   }

   public List<Setting<?>> sAsList() {
      return List.of(this.c, this.X, this.e);
   }

   public List<Setting<?>> wAsList() {
      return List.of(this.i, this.l, this.z, this.r);
   }

   public List<Setting<?>> MAsList() {
      return List.of(this.rotation);
   }

   @Override
   public PlacementPreview placementHighlight() {
      return this.M;
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
      return this.isEnabled() ? this.f : "";
   }

   @Override
   public void onEnable() {
      this.y = this.E = null;
      this.ZAsVoid("Waiting for a target");
      this.M.SAsVoid();
      ClientLog.GAsVoid(
         "AutoAnchor",
         "Enabled; rotation={}, range={}, safeAnchor={}, minDamage={}, maxSelf={}",
         this.rotation.getValue(),
         this.w.getValue(),
         this.P.getValue(),
         this.i.getValue(),
         this.l.getValue() ? this.z.getValue() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.ZAsVoid("Waiting for a target");
      this.dAsVoid();
      this.M.rAsVoid();
      this.y = this.E = null;
      ClientLog.GAsVoid("AutoAnchor", "Disabled; anchor intents and server waits cleared");
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled() && ParticleEmitter.fAsBoolean(ParticleEmitter.Type_u.AUTOANCHOR)) {
         ModuleDetector.yAsVoid(ModuleDetector.Type_s.PLACEMENT, var1, var2, 1.0);
         if (ModuleDetector.CAsBoolean(ModuleDetector.Type_s.PLACEMENT)) {
            this.V = mc.player;
            this.A = mc.world;
            this.a = mc.player.age;
            ParticleEmitter.jAsVoid(ParticleEmitter.Type_u.AUTOANCHOR);
         } else {
            this.dAsVoid();
         }
      }
   }

   private void dAsVoid() {
      this.V = this.A = null;
      ParticleEmitter.GAsVoid(ParticleEmitter.Type_u.AUTOANCHOR);
   }

   public boolean LAsBoolean() {
      return this.isEnabled()
         ? this.isRuntimeActive()
            && this.rotation.pAsBoolean("Visible")
            && mc.player != null
            && this.V == mc.player
            && this.A == mc.world
            && mc.player.age >= this.a
            && mc.player.age - this.a <= 1
            && (!this.r.getValue() || !mc.player.isUsingItem())
         : false;
   }

   public float IAsFloat(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && ParticleEmitter.NAsP5_Type_u() == ParticleEmitter.Type_u.AUTOANCHOR && ModuleDetector.CAsBoolean(ModuleDetector.Type_s.PLACEMENT)) {
            mc.player.setHeadYaw(mc.player.getYaw());
            return mc.player.getYaw();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void onTick() {
      this.M.mAsVoid(null);
      BlockPlacementController.WAsVoid(this);
      if (!this.isInGame()) {
         this.ZAsVoid("Waiting for a world");
      } else {
         if (this.y != mc.player || this.E != mc.world) {
            this.ZAsVoid("Waiting for a target");
            this.y = mc.player;
            this.E = mc.world;
         }

         boolean var3 = false;
         boolean var4 = false /* VF: Semaphore variable */;

         try {
            var4 = true;
            var3 = true;
            this.qAsVoid();
            var3 = false;
            var4 = false;
         } finally {
            if (var4) {
               if (var3 && !this.f.equals(this.F)) {
                  this.F = this.f;
                  ClientLog.XAsVoid("AutoAnchor", "Status: {}", this.f);
               }
            }
         }

         if (!this.f.equals(this.F)) {
            this.F = this.f;
            ClientLog.XAsVoid("AutoAnchor", "Status: {}", this.f);
         }
      }
   }

   private void qAsVoid() {
      long var1 = mc.world.getTime();
      if (this.p != null) {
         if (!this.L.getAsBoolean()) {
            if (var1 - this.Y > 12L) {
               ClientLog.GAsVoid("AutoAnchor", "No server update after {} ticks for: {}; replanning", 12, this.p);
               this.ZAsVoid("Server did not confirm the last step");
               return;
            }

            if (this.J != null) {
               this.M.mAsVoid(this.J.anchor());
            }

            this.f = "Waiting for " + this.p;
            return;
         }

         ClientLog.XAsVoid("AutoAnchor", "Server confirmed: {}", this.p);
         if (this.p.equals("detonation")) {
            this.J = null;
         }

         this.p = null;
         this.s = var1 + (long)this.S.getValue().intValue();
      }

      String var3 = this.dAsString();
      if (var3 != null) {
         this.J = null;
         this.f = var3;
      } else if (var1 < this.s) {
         if (this.J != null) {
            this.M.mAsVoid(this.J.anchor());
         }
      } else {
         List var4 = this.EAsList();
         if (var4.isEmpty()) {
            this.J = null;
            this.f = this.X.getValue() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)";
         } else {
            if (this.J != null) {
               this.J = this.uAsAutoAnchor_Type_u(this.J, var4);
            }

            if (this.J == null) {
               this.J = this.vAsAutoAnchor_Type_u(var4);
            }

            if (this.J == null) {
               this.f = this.D;
            } else {
               this.M.mAsVoid(this.J.anchor());
               SharedRenderEffects.UAsVoid(this.J.target());
               this.EAsVoid(this.J);
            }
         }
      }
   }

   private String dAsString() {
      if (!ModuleImportBroker.jAsBoolean() || !mc.isWindowFocused()) {
         return "Paused: screen, focus or detached camera";
      } else if (this.r.getValue() && mc.player.isUsingItem()) {
         return "Paused while using an item";
      } else if (mc.player.shouldCancelInteraction()) {
         return "Paused while sneaking";
      } else if (this.sAsBoolean(mc.player.getBlockPos())) {
         return "Anchors set spawn here, so they cannot explode";
      } else {
         return this.rotation.pAsBoolean("Visible") && !ParticleEmitter.fAsBoolean(ParticleEmitter.Type_u.AUTOANCHOR) ? "Paused for another camera owner" : null;
      }
   }

   private void EAsVoid(AutoAnchor.Type_u var1) {
      BlockState var2 = mc.world.getBlockState(var1.anchor());
      BlockPos var3 = var1.anchor();
      if (!var2.isOf(Blocks.RESPAWN_ANCHOR)) {
         this.QAsVoid(true, var3, Items.RESPAWN_ANCHOR, "anchor placement", () -> mc.world.getBlockState(var3).isOf(Blocks.RESPAWN_ANCHOR));
      } else if ((Integer)var2.get(RespawnAnchorBlock.CHARGES) == 0) {
         this.QAsVoid(false, var3, Items.GLOWSTONE, "anchor charge", () -> this.KAsInt(var3) > 0);
      } else if (var1.shield() != null && BlockPlacementController.jAsBoolean(var1.shield())) {
         BlockPos var5 = var1.shield();
         this.QAsVoid(true, var5, this.MAsItem(), "shield placement", () -> !BlockPlacementController.jAsBoolean(var5));
      } else {
         if (mc.player.getOffHandStack().isOf(Items.GLOWSTONE) && (Integer)var2.get(RespawnAnchorBlock.CHARGES) < 4) {
            this.f = "Move offhand glowstone before detonation";
            return;
         }

         Item var4 = this.JAsItem();
         if (var4 == null) {
            this.f = "A hotbar item other than glowstone is required to detonate";
            return;
         }

         this.QAsVoid(false, var3, var4, "detonation", () -> !mc.world.getBlockState(var3).isOf(Blocks.RESPAWN_ANCHOR));
      }
   }

   private void QAsVoid(boolean var1, BlockPos var2, Item var3, String var4, BooleanSupplier var5) {
      AutoAnchor.Type_u var6 = this.J;
      BooleanSupplier var7 = () -> this.J == var6 && this.p == null && this.dAsString() == null;
      Consumer var8 = var5xx -> {
         if (!(Boolean)var5xx) {
            this.f = "Anchor " + var4 + " rejected; retrying";
         } else {
            this.p = var4;
            this.L = var5;
            this.Y = mc.world.getTime();
            if (var4.equals("detonation")) {
               ClientLog.GAsVoid(
                  "AutoAnchor",
                  "Detonating anchor at {}: {} HP on {}, {} HP to you, {}% exposed{}",
                  var2.toShortString(),
                  String.format(Locale.ROOT, "%.1f", var6.damage()),
                  var6.target().getName().getString(),
                  String.format(Locale.ROOT, "%.1f", var6.self()),
                  Math.round(var6.exposure() * 100.0),
                  var6.shield() == null ? "" : " behind a shield at " + var6.shield().toShortString()
               );
            }
         }
      };
      boolean var9 = this.rotation.pAsBoolean("Visible");
      boolean var10 = var1
         ? BlockPlacementController.sAsBoolean(this, var2, var3, this.w.getValue(), var9, this.G.getValue(), var7, var8)
         : BlockPlacementController.xAsBoolean(this, var2, var3, this.w.getValue(), var9, this.G.getValue(), var7, var8);
      this.f = var10
         ? String.format(
            Locale.ROOT,
            "%s%s; %.1f HP on %s",
            Character.toUpperCase(var4.charAt(0)),
            var4.substring(1),
            this.J.damage(),
            this.J.target().getName().getString()
         )
         : (var9 ? "No reachable face for " + var4 : "Aim at the " + (var1 ? "support" : "anchor") + " for " + var4);
   }

   private boolean sAsBoolean(BlockPos var1) {
      return Boolean.TRUE.equals(mc.world.getEnvironmentAttributes().getAttributeValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS_GAMEPLAY, var1));
   }

   private int KAsInt(BlockPos var1) {
      BlockState var2 = mc.world.getBlockState(var1);
      return var2.isOf(Blocks.RESPAWN_ANCHOR) ? (Integer)var2.get(RespawnAnchorBlock.CHARGES) : 0;
   }

   private Item MAsItem() {
      return this.shieldBlock.pAsBoolean("Obsidian") ? Items.OBSIDIAN : Items.GLOWSTONE;
   }

   private Item JAsItem() {
      ItemStack var1 = mc.player.getOffHandStack();
      ItemStack var2 = mc.player.getMainHandStack();
      if (!var2.isOf(Items.GLOWSTONE)) {
         return var2.isEmpty() ? Items.AIR : var2.getItem();
      } else {
         if (this.G.getValue()) {
            for (int var3 = 0; var3 < 9; var3++) {
               ItemStack var4 = mc.player.getInventory().getStack(var3);
               if (!var4.isOf(Items.GLOWSTONE) && (var4.isEmpty() ? !var1.isEmpty() : !var1.isOf(var4.getItem()))) {
                  return var4.isEmpty() ? Items.AIR : var4.getItem();
               }
            }
         }

         return null;
      }
   }

   private boolean MAsBoolean(LivingEntity var1) {
      if (var1 == mc.player
         || !var1.isAlive()
         || var1.isRemoved()
         || var1.isSpectator()
         || var1.getEntityWorld() != mc.world
         || (double)mc.player.distanceTo(var1) > this.e.getValue()
         || mc.player.isTeammate(var1)) {
         return false;
      } else if (var1 instanceof PlayerEntity var2) {
         if (this.c.getValue() && !var2.getAbilities().invulnerable) {
            MCFriend var3 = LarpClient.CAsLarpClient().mAsF().EAsModule(MCFriend.class);
            return var3 == null || !var3.SAsBoolean(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.X.getValue() && var1 instanceof Monster;
      }
   }

   private List<LivingEntity> EAsList() {
      ArrayList var1 = new ArrayList();

      for (Entity var3 : mc.world.getEntities()) {
         if (var3 instanceof LivingEntity) {
            LivingEntity var4 = (LivingEntity)var3;
            if (this.MAsBoolean(var4)) {
               var1.add(var4);
            }
         }
      }

      var1.sort(Comparator.comparingDouble(entity -> mc.player.squaredDistanceTo((net.minecraft.entity.Entity)entity)));
      return var1.size() > 4 ? new ArrayList<>(var1.subList(0, 4)) : var1;
   }

   private double BAsDouble() {
      return Math.min(this.w.getValue(), mc.player.getBlockInteractionRange());
   }

   private boolean DAsBoolean(double var1) {
      return !this.l.getValue() || NebulaSky.uAsBoolean(var1, this.z.getValue(), (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount()));
   }

   private AutoAnchor.Type_u IAsAutoAnchor_Type_u(BlockPos var1, BlockPos var2, List<LivingEntity> var3, double var4, double var6) {
      LivingEntity var8 = null;
      double var9 = 0.0;

      for (LivingEntity var12 : var3) {
         double var13 = (double)MusicSync.CAsFloat(var12, var1, var2);
         if (var13 > var9) {
            var9 = var13;
            var8 = var12;
         }
      }

      return var8 == null ? null : new AutoAnchor.Type_u(var1, var2, var8, var9, var4, var6);
   }

   private AutoAnchor.Type_u uAsAutoAnchor_Type_u(AutoAnchor.Type_u var1, List<LivingEntity> var2) {
      boolean var3 = mc.world.getBlockState(var1.anchor()).isOf(Blocks.RESPAWN_ANCHOR);
      if ((var3 || BlockPlacementController.jAsBoolean(var1.anchor())) && !this.sAsBoolean(var1.anchor())) {
         if (this.LAsBoolean(var1.anchor(), var3) && this.FAsBoolean(var1.anchor(), var3)) {
            BlockPos var4 = var1.shield() == null
                  || BlockPlacementController.jAsBoolean(var1.shield()) && (!this.dAsBoolean(var1.shield()) || !this.LAsBoolean(var1.anchor(), var1.shield()))
               ? null
               : var1.shield();
            float var5 = MusicSync.LAsFloat(mc.player, var1.anchor(), var4);
            double var6 = (double)MusicSync.pAsFloat(mc.player, var1.anchor(), var5);
            if (!this.DAsBoolean(var6)) {
               return null;
            } else {
               AutoAnchor.Type_u var8 = this.IAsAutoAnchor_Type_u(var1.anchor(), var4, var2, var6, (double)var5);
               return var8 != null && var8.damage() >= this.i.getValue() ? var8 : null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private AutoAnchor.Type_u vAsAutoAnchor_Type_u(List<LivingEntity> var1) {
      Vec3d var2 = mc.player.getEyePos();
      double var3 = this.BAsDouble();
      int var5 = BlockPlacementController.qAsInt(Items.RESPAWN_ANCHOR, this.G.getValue()) != -1 ? 1 : 0;
      int var6 = BlockPlacementController.qAsInt(Items.GLOWSTONE, this.G.getValue()) != -1 ? 1 : 0;
      HashSet var7 = new HashSet();
      ArrayList var8 = new ArrayList();
      int var9 = 0;

      record u(BlockPos pos, double bound) {
      }

      for (LivingEntity var11 : var1) {
         BlockPos var12 = var11.getBlockPos();

         for (int var13 = -1; var13 <= 2; var13++) {
            for (int var14 = -2; var14 <= 2; var14++) {
               for (int var15 = -2; var15 <= 2; var15++) {
                  BlockPos var16 = var12.add(var14, var13, var15);
                  if (var7.add(var16)
                     && mc.world.isChunkLoaded(var16)
                     && !(var2.squaredDistanceTo(Vec3d.ofCenter(var16)) > (var3 + 1.0) * (var3 + 1.0))
                     && !this.sAsBoolean(var16)) {
                     boolean var17 = mc.world.getBlockState(var16).isOf(Blocks.RESPAWN_ANCHOR);
                     if (var17) {
                        var9++;
                     }

                     if (var17 ? this.LAsBoolean(var16, true) : var5 != 0 && var6 != 0 && BlockPlacementController.jAsBoolean(var16)) {
                        double var18 = 0.0;

                        for (LivingEntity var21 : var1) {
                           var18 = Math.max(var18, (double)NebulaSky.DAsFloat(var21, NebulaSky.sAsFloat(var21.getEntityPos().distanceTo(Vec3d.ofCenter(var16)), 1.0, 5.0)));
                        }

                        if (var18 >= this.i.getValue()) {
                           var8.add(new u(var16, var18));
                        }
                     }
                  }
               }
            }
         }
      }

      if (var8.isEmpty()) {
         this.D = var5 == 0 && var9 == 0
            ? "No respawn anchors in hotbar or hands"
            : (var6 == 0 && var9 == 0 ? "No glowstone in hotbar or hands" : "No anchor spot in reach can reach Min Damage");
         return null;
      } else {
         var8.sort(Comparator.comparingDouble(u::bound).reversed());
         AutoAnchor.Type_u var28 = null;
         double var29 = Double.MAX_VALUE;
         double var30 = 0.0;

         for (u var32 : (Iterable<u>) (var8)) {
            if (var28 != null && var32.bound() <= var28.value()) {
               break;
            }

            BlockPos var33 = var32.pos();
            boolean var34 = mc.world.getBlockState(var33).isOf(Blocks.RESPAWN_ANCHOR);
            AutoAnchor.Type_u var19 = this.IAsAutoAnchor_Type_u(var33, null, var1, 0.0, 0.0);
            if (var19 != null) {
               var30 = Math.max(var30, var19.damage());
               if (!(var19.damage() < this.i.getValue()) && (var28 == null || !(var19.damage() <= var28.value()))) {
                  float var35 = MusicSync.LAsFloat(mc.player, var33, null);
                  ArrayList var36 = new ArrayList();
                  if (this.P.getValue() && var35 > 0.0F && BlockPlacementController.qAsInt(this.MAsItem(), this.G.getValue()) != -1) {
                     for (BlockPos var23 : MusicSync.fAsList(var33, mc.player.getEyePos())) {
                        if (BlockPlacementController.jAsBoolean(var23)) {
                           float var24 = MusicSync.LAsFloat(mc.player, var33, var23);
                           if (var24 < var35) {
                              var36.add(new AutoAnchor.Type_q(var23, var24));
                           }
                        }
                     }

                     var36.sort(Comparator.comparingDouble(AutoAnchor.Type_q::exposure));
                  }

                  var36.add(new AutoAnchor.Type_q(null, var35));
                  Boolean var37 = null;

                  for (AutoAnchor.Type_q var39 : (Iterable<AutoAnchor.Type_q>) (var36)) {
                     double var25 = (double)MusicSync.pAsFloat(mc.player, var33, var39.exposure());
                     AutoAnchor.Type_u var27 = var39.pos() == null
                        ? new AutoAnchor.Type_u(var33, null, var19.target(), var19.damage(), var25, (double)var35)
                        : this.IAsAutoAnchor_Type_u(var33, var39.pos(), var1, var25, (double)var39.exposure());
                     if (var27 != null) {
                        var29 = Math.min(var29, var27.self());
                        if (this.DAsBoolean(var27.self()) && !(var27.damage() < this.i.getValue()) && (var28 == null || !(var27.value() <= var28.value()))) {
                           if (var37 == null) {
                              var37 = this.FAsBoolean(var33, var34);
                           }

                           if (!var37) {
                              break;
                           }

                           if (var27.shield() == null || this.dAsBoolean(var27.shield()) && this.LAsBoolean(var33, var27.shield())) {
                              var28 = var27;
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }

         if (var28 == null) {
            this.D = var29 != Double.MAX_VALUE && !this.DAsBoolean(var29)
               ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var29, this.z.getValue())
               : (
                  var30 < this.i.getValue()
                     ? String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var30, this.i.getValue())
                     : (
                        this.rotation.pAsBoolean("Visible")
                           ? "No anchor spot with a reachable face or support"
                           : "Aim at an anchor or at a support beside an anchor spot"
                     )
               );
         } else {
            ClientLog.XAsVoid(
               "AutoAnchor",
               "Planned anchor at {}{}: {} HP on {}, {} HP to you",
               var28.anchor().toShortString(),
               var28.shield() == null ? "" : " with shield at " + var28.shield().toShortString(),
               String.format(Locale.ROOT, "%.1f", var28.damage()),
               var28.target().getName().getString(),
               String.format(Locale.ROOT, "%.1f", var28.self())
            );
         }

         return var28;
      }
   }

   private boolean LAsBoolean(BlockPos var1, boolean var2) {
      int var3 = BlockPlacementController.qAsInt(Items.GLOWSTONE, this.G.getValue()) != -1 ? 1 : 0;
      return !var2 ? var3 != 0 && BlockPlacementController.qAsInt(Items.RESPAWN_ANCHOR, this.G.getValue()) != -1 : this.KAsInt(var1) > 0 || var3 != 0;
   }

   private boolean FAsBoolean(BlockPos var1, boolean var2) {
      boolean var3 = this.rotation.pAsBoolean("Visible");
      if (!var2) {
         return BlockPlacementController.HAsBlockHitResult(var1, Items.RESPAWN_ANCHOR, this.w.getValue(), var3, this.G.getValue()) != null;
      } else if (!var3) {
         return ModuleImportBroker.TAsBlockHitResult(var1, this.BAsDouble()) != null;
      } else {
         Vec3d var4 = mc.player.getEyePos();

         for (Vec3d var6 : AutoCrystal.HAsList(var1, var4)) {
            if (ModuleImportBroker.eAsBlockHitResult(var1, this.BAsDouble(), var6.subtract(var4)) != null) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean dAsBoolean(BlockPos var1) {
      return BlockPlacementController.HAsBlockHitResult(var1, this.MAsItem(), this.w.getValue(), this.rotation.pAsBoolean("Visible"), this.G.getValue())
         != null;
   }

   private boolean LAsBoolean(BlockPos var1, BlockPos var2) {
      Vec3d var3 = mc.player.getEyePos();
      Box var4 = new Box(var2);
      double var5 = this.BAsDouble();

      for (Vec3d var8 : AutoCrystal.HAsList(var1, var3)) {
         if (!(var3.squaredDistanceTo(var8) > var5 * var5) && !var4.raycast(var3, var8).isPresent()) {
            BlockHitResult var9 = mc.world.raycast(new RaycastContext(var3, var8, ShapeType.OUTLINE, FluidHandling.NONE, mc.player));
            if (var9.getType() == Type.MISS || var9.getBlockPos().equals(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   private void ZAsVoid(String var1) {
      BlockPlacementController.WAsVoid(this);
      this.J = null;
      this.p = null;
      this.L = null;
      this.Y = this.s = 0L;
      this.f = var1;
   }

   private static record Type_q(BlockPos pos, float exposure) {
   }

   private static record Type_u(BlockPos anchor, BlockPos shield, LivingEntity target, double damage, double self, double exposure) {
      double value() {
         return this.damage - this.self * 0.25;
      }
   }
}
