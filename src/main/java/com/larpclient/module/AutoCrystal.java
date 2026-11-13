package com.larpclient.module;

import com.larpclient.BlockPlacementController;
import com.larpclient.ClientLog;
import com.larpclient.LarpClient;
import com.larpclient.PlacementPreview;
import com.larpclient.SharedRenderEffects;
import com.larpclient.b_;
import com.larpclient.dl;
import com.larpclient.dq;
import com.larpclient.nf;
import com.larpclient.o5;
import com.larpclient.p5;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.ActionResult;
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

public final class AutoCrystal extends Module implements PlacementPreview.Type_e {
   private final PlacementPreview a;
   private ClientPlayerEntity KS;
   private ClientWorld c;
   private final NumberSetting L = this.rAsNumberSetting("Range", "Maximum crystal interaction distance", 4.5, 2.0, 6.0, 0.5);
   private final NumberSetting Kj = this.rAsNumberSetting("Target Range", "Search distance for opponents", 10.0, 2.0, 16.0, 1.0);
   private final NumberSetting y = this.rAsNumberSetting("Break Delay", "Ticks between executed attacks", 2.0, 0.0, 10.0, 1.0);
   private final NumberSetting R = this.rAsNumberSetting("Place Delay", "Ticks between placement attempts", 2.0, 0.0, 10.0, 1.0);
   private final NumberSetting F = this.rAsNumberSetting(
      "Min Damage", "Minimum estimated target damage in HP; a hit that kills or pops always qualifies", 4.0, 0.0, 20.0, 0.5
   );
   private final NumberSetting M = this.rAsNumberSetting(
      "Face Place Health", "Once a target's health and absorption fall to this, crystals down to 1.5 HP qualify; 0 turns it off", 8.0, 0.0, 36.0, 0.5
   );
   private final NumberSetting i = this.rAsNumberSetting(
      "Predict Ticks", "Score new crystals and obsidian against where a moving target will be after this many ticks", 2.0, 0.0, 10.0, 1.0
   );
   private final NumberSetting x = this.rAsNumberSetting("Max Self Damage", "Maximum estimated self damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final BooleanSetting N = this.EAsBooleanSetting("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final BooleanSetting W = this.EAsBooleanSetting("Players", "Target other survival players", true);
   private final BooleanSetting e = this.EAsBooleanSetting("Mobs", "Also target hostile mobs for combat or testing", false);
   private final BooleanSetting d = this.EAsBooleanSetting("Auto Switch", "Use hotbar crystals and restore the selected slot", true);
   private final BooleanSetting f = this.EAsBooleanSetting("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final BooleanSetting k = this.EAsBooleanSetting("Place", "Place crystals on existing obsidian or bedrock", true);
   private final BooleanSetting KB = this.EAsBooleanSetting("Place Obsidian", "Place an obsidian base first when no existing base is good enough", true);
   private final ModeSetting obsidianMode = this.addSetting(
      new ModeSetting(
         "Obsidian Mode",
         "Best places obsidian when no base qualifies, or when a new one kills or adds Obsidian Gain; Lethal only where its crystal kills or pops; Damage only when no existing base qualifies",
         "Best",
         "Best",
         "Lethal",
         "Damage"
      )
   );
   private final NumberSetting G = this.rAsNumberSetting(
      "Obsidian Gain", "Extra target damage in HP a new obsidian base must add over the best existing base", 3.0, 0.0, 20.0, 0.5
   );
   private final NumberSetting Kz = this.rAsNumberSetting("Obsidian Delay", "Ticks after placing obsidian before the crystal goes on it", 1.0, 0.0, 10.0, 1.0);
   private final BooleanSetting KE = this.EAsBooleanSetting("Break", "Attack crystals that can damage a target", true);
   private final ModeSetting rotation = this.addSetting(
      new ModeSetting("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final NumberSetting T = this.rAsNumberSetting("Turn Speed", "Maximum visible turn in degrees per tick", 45.0, 5.0, 180.0, 5.0);
   private final NumberSetting H = this.rAsNumberSetting("Aim Hold", "Consecutive aligned ticks before acting", 1.0, 1.0, 5.0, 1.0);
   private final NumberSetting K_ = this.rAsNumberSetting("Place Range", "Placement reach, capped by Range and native block reach", 4.5, 2.0, 6.0, 0.5);
   private final NumberSetting Z = this.rAsNumberSetting("Break Range", "Attack reach, capped by Range and native entity reach", 3.0, 2.0, 6.0, 0.5);
   private final NumberSetting I = this.rAsNumberSetting("Scan Interval", "Ticks to retain a valid selected action before rescoring", 2.0, 1.0, 10.0, 1.0);
   private final NumberSetting Kw = this.rAsNumberSetting(
      "Response Timeout", "Ticks to wait for server crystal spawn or removal before retrying", 10.0, 2.0, 40.0, 1.0
   );
   private final BooleanSetting m = this.EAsBooleanSetting("Pause Weakness", "Pause placement too when weakness prevents crystal attacks", true);
   private final BooleanSetting l = this.EAsBooleanSetting(
      "Pause On Kill", "Stop placing and breaking after a nearby opponent dies, so explosions spare their drops", false
   );
   private final NumberSetting Y = this.rAsNumberSetting("Kill Pause", "Seconds to stay paused after a kill", 10.0, 1.0, 60.0, 1.0);
   private final BooleanSetting Ks = this.EAsBooleanSetting("Work with Crystal", "Start while holding an end crystal", true);
   private final BooleanSetting U = this.EAsBooleanSetting("Work with Sword", "Start while holding a sword", true);
   private final BooleanSetting r = this.EAsBooleanSetting("Work with Totem", "Start while holding a totem", true);
   private final BooleanSetting p = this.EAsBooleanSetting("Work with Fist", "Start with an empty main hand", true);
   private final NumberSetting B = this.DAsNumberSetting("Switch Delay Min", 1.0);
   private final NumberSetting Q = this.DAsNumberSetting("Switch Delay Max", 50.0);
   private final NumberSetting Kc = this.DAsNumberSetting("Place Delay Min", 50.0);
   private final NumberSetting n = this.DAsNumberSetting("Place Delay Max", 100.0);
   private final NumberSetting V = this.DAsNumberSetting("Break Delay Min", 50.0);
   private final NumberSetting o = this.DAsNumberSetting("Break Delay Max", 100.0);
   private final BooleanSetting D = this.EAsBooleanSetting("Swap Back", "Restore your starting slot after releasing use", true);
   private final NumberSetting h = this.DAsNumberSetting("Swap Back Delay Min", 50.0);
   private final NumberSetting z = this.DAsNumberSetting("Swap Back Delay Max", 100.0);
   private static final double[][] g = new double[][]{{0.5, 0.5}, {0.15, 0.15}, {0.15, 0.85}, {0.85, 0.15}, {0.85, 0.85}};
   private static final Direction[] E = new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN};
   private static final int J = 24;
   private static final int w = 96;
   public static final double K6 = 1.5;
   private AutoCrystal.Type_g KD;
   private long K;
   private String Kb = "";
   private final Map<LivingEntity, Vec3d> q = new IdentityHashMap<>();
   private BlockPos KO;
   private LivingEntity S;
   private Object P;
   private Object v;
   private int KY;
   private String K8 = "";
   private Object j;
   private LivingEntity s;
   private long O;
   private long X;
   private final com.larpclient.t A = new com.larpclient.t();
   private static AutoCrystal Kn;
   private final dl t = new dl();
   private String b = "Waiting for a target";

   @Override
   public PlacementPreview placementHighlight() {
      return this.a;
   }

   public static AutoCrystal vAsAutoCrystal() {
      return Kn;
   }

   public List<Setting<?>> HAsList() {
      return List.of(this.k, this.KE, this.L, this.K_, this.Z, this.R, this.y, this.Kw, this.I, this.d);
   }

   public List<Setting<?>> NAsList() {
      return List.of(this.KB, this.obsidianMode, this.G, this.Kz);
   }

   public List<Setting<?>> zAsList() {
      return List.of(this.W, this.e, this.Kj, this.i);
   }

   public List<Setting<?>> kAsList() {
      return List.of(this.rotation, this.T, this.H);
   }

   public List<Setting<?>> VAsList() {
      return List.of(this.F, this.M, this.N, this.x, this.f, this.m, this.l, this.Y);
   }

   public AutoCrystal() {
      super("AutoCrystal", "Automatically place and break end crystals", Category.COMBAT, -1);
      Kn = this;
      this.T.visibleWhen(() -> this.rotation.pAsBoolean("Visible"));
      this.K_.visibleWhen(this.k::getValue);
      this.R.visibleWhen(this.k::getValue);
      this.KB.visibleWhen(this.k::getValue);
      this.obsidianMode.visibleWhen(() -> this.k.getValue() && this.KB.getValue());
      this.G.visibleWhen(() -> this.k.getValue() && this.KB.getValue() && this.obsidianMode.pAsBoolean("Best"));
      this.Kz.visibleWhen(() -> this.k.getValue() && this.KB.getValue());
      this.Z.visibleWhen(this.KE::getValue);
      this.y.visibleWhen(this.KE::getValue);
      this.x.visibleWhen(this.N::getValue);
      this.Y.visibleWhen(this.l::getValue);
      this.l.onChanged(var1 -> {
         if (!var1) {
            this.X = 0L;
         }
      });
      this.rotation.onChanged(var1 -> {
         this.eAsVoid();
         this.tAsVoid();
      });

      for (Setting var2 : List.of(this.Ks, this.U, this.r, this.p, this.B, this.Q, this.Kc, this.n, this.V, this.o, this.D, this.h, this.z)) {
         var2.visibleWhen(() -> false);
      }

      this.a = new PlacementPreview(this, var1 -> this.addSetting(var1), 0.5, 0.3);
   }

   private NumberSetting rAsNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   private BooleanSetting EAsBooleanSetting(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   private NumberSetting DAsNumberSetting(String var1, double var2) {
      return this.rAsNumberSetting(var1, "Random delay endpoint in milliseconds; reversed endpoints are ordered automatically", var2, 0.0, 500.0, 1.0);
   }

   public static long yAsLong(double var0, double var2, double var4) {
      long var6 = Math.round(Math.min(var0, var2));
      long var8 = Math.round(Math.max(var0, var2));
      return var6 + Math.min(var8 - var6, (long)(Math.clamp(var4, 0.0, 1.0) * (double)(var8 - var6 + 1L)));
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
      this.ZAsVoid();
      this.a.SAsVoid();
      ClientLog.GAsVoid(
         "AutoCrystal",
         "Autonomous target selection enabled; native reach, damage limits and final look-ray validation; obsidian bases {}",
         this.KB.getValue() ? this.obsidianMode.getValue() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.ZAsVoid();
      ClientLog.GAsVoid("AutoCrystal", "Autonomous actions cancelled; target and server-response waits cleared");
   }

   private void ZAsVoid() {
      this.t.zAsVoid();
      this.eAsVoid();
      this.tAsVoid();
      this.a.rAsVoid();
      this.KS = null;
      this.c = null;
      this.KO = null;
      this.S = null;
      this.X = 0L;
      this.K = 0L;
      this.q.clear();
      this.b = "Waiting for a target";
   }

   private boolean wAsBoolean() {
      return this.isRuntimeActive()
         && this.isInGame()
         && b_.jAsBoolean()
         && mc.isWindowFocused()
         && this.yAsBoolean()
         && (!this.f.getValue() || !mc.player.isUsingItem())
         && (!this.m.getValue() || !this.hAsBoolean());
   }

   private boolean yAsBoolean() {
      p5.Type_u var1 = p5.NAsP5_Type_u();
      return this.rotation.pAsBoolean("Visible") ? p5.fAsBoolean(p5.Type_u.AUTOCRYSTAL) : var1 == p5.Type_u.NONE;
   }

   private void tAsVoid() {
      this.P = this.v = null;
      p5.GAsVoid(p5.Type_u.AUTOCRYSTAL);
   }

   public boolean zAsBoolean() {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive()
         && this.rotation.pAsBoolean("Visible")
         && mc.player != null
         && this.P == mc.player
         && this.v == mc.world
         && mc.player.age >= this.KY
         && (!this.f.getValue() || !mc.player.isUsingItem())) {
         int var1 = mc.player.age - this.KY;
         return var1 <= 1 || (this.t.JAsBoolean() || this.t.UAsBoolean()) && (double)var1 <= this.Kw.getValue();
      } else {
         return false;
      }
   }

   public float DAsFloat(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && p5.NAsP5_Type_u() == p5.Type_u.AUTOCRYSTAL) {
            mc.player.setHeadYaw(mc.player.getYaw());
            return mc.player.getYaw();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private boolean UAsBoolean(LivingEntity var1) {
      return var1.isAlive() && !var1.isRemoved() && this.dAsBoolean(var1);
   }

   private boolean dAsBoolean(LivingEntity var1) {
      if (var1 == mc.player
         || var1.isSpectator()
         || var1.getEntityWorld() != mc.world
         || (double)mc.player.distanceTo(var1) > this.Kj.getValue()
         || mc.player.isTeammate(var1)) {
         return false;
      } else if (var1 instanceof PlayerEntity var2) {
         if (this.W.getValue() && !var2.getAbilities().invulnerable) {
            MCFriend var3 = LarpClient.CAsLarpClient().mAsF().EAsModule(MCFriend.class);
            return var3 == null || !var3.SAsBoolean(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.e.getValue() && var1 instanceof Monster;
      }
   }

   public static double hAsDouble(double var0, double var2, double var4) {
      double var6 = var4 > 0.0 && var0 <= var4 ? Math.min(var2, 1.5) : var2;
      return Math.min(var6, var0);
   }

   public static boolean HAsBoolean(String var0, double var1, boolean var3, double var4, boolean var6, double var7) {
      if (Double.isNaN(var1)) {
         return true;
      } else if (var3) {
         return false;
      } else {
         return switch (var0) {
            case "Lethal" -> var6;
            case "Damage" -> false;
            default -> var6 || var4 >= var1 + var7;
         };
      }
   }

   private AutoCrystal.Type_b NAsAutoCrystal_Type_b(Vec3d var1, List<LivingEntity> var2) {
      return this.qAsAutoCrystal_Type_b(var1, var2, null, null, false, false);
   }

   private static double aAsDouble(LivingEntity var0) {
      return (double)(var0.getHealth() + var0.getAbsorptionAmount());
   }

   private double GAsDouble(LivingEntity var1) {
      return hAsDouble(aAsDouble(var1), this.F.getValue(), this.M.getValue());
   }

   private Vec3d BAsVec3d(LivingEntity var1) {
      int var2 = this.i.getValue().intValue();
      return var2 <= 0 ? Vec3d.ZERO : this.q.computeIfAbsent(var1, var1x -> {
         Vec3d var2x = new Vec3d(var1x.getX() - var1x.lastX, 0.0, var1x.getZ() - var1x.lastZ);
         if (!(var2x.lengthSquared() < 1.0E-6) && !(var2x.lengthSquared() > 1.0)) {
            Vec3d var3 = var2x.multiply((double)var2);
            return mc.world.isSpaceEmpty(var1x, var1x.getBoundingBox().offset(var3)) ? var3 : Vec3d.ZERO;
         } else {
            return Vec3d.ZERO;
         }
      });
   }

   private AutoCrystal.Type_b qAsAutoCrystal_Type_b(Vec3d var1, List<LivingEntity> var2, AutoCrystal.Type_h var3, BlockPos var4, boolean var5, boolean var6) {
      double var7 = (double)nf.aAsFloat(mc.player, var1, var4);
      if (this.N.getValue() && !nf.uAsBoolean(var7, this.x.getValue(), (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount()))) {
         if (var3 != null) {
            var3.E++;
            var3.Y = Math.min(var3.Y, var7);
         }

         return null;
      } else {
         AutoCrystal.Type_b var9 = null;

         for (LivingEntity var11 : var2) {
            if (this.UAsBoolean(var11)) {
               double var12 = (double)nf.DAsFloat(var11, var1, var4, var5 ? this.BAsVec3d(var11) : Vec3d.ZERO);
               if (!(var12 <= 0.0) && !(var12 < this.GAsDouble(var11))) {
                  if (!var6 || !(var12 < aAsDouble(var11))) {
                     AutoCrystal.Type_b var14 = new AutoCrystal.Type_b(var11, var12, var7, var12 >= aAsDouble(var11));
                     if (var9 == null || var14.value() > var9.value()) {
                        var9 = var14;
                     }
                  } else if (var3 != null && (Double.isNaN(var3.H) || var12 - aAsDouble(var11) > var3.k - var3.H)) {
                     var3.k = var12;
                     var3.H = aAsDouble(var11);
                  }
               } else if (var3 != null) {
                  var3.r = Math.max(var3.r, var12);
               }
            }
         }

         return var9;
      }
   }

   private Optional<Vec3d> PAsOptional(EndCrystalEntity var1, Vec3d var2) {
      return b_.nAsOptional(var1, this.ZAsDouble(), var2 == null ? var1.getBoundingBox().getCenter() : var2, var2);
   }

   public static Box ZAsBox(BlockPos var0) {
      return EntityType.END_CRYSTAL.getDimensions().getBoxAt(Vec3d.ofBottomCenter(var0.up()));
   }

   public static List<Vec3d> HAsList(BlockPos var0, Vec3d var1) {
      ArrayList var2 = new ArrayList(15);
      Vec3d var3 = Vec3d.ofCenter(var0);

      for (Direction var7 : E) {
         int var8 = var7.getOffsetX();
         int var9 = var7.getOffsetY();
         int var10 = var7.getOffsetZ();
         if (!(
            (var1.x - var3.x - (double)var8 * 0.5) * (double)var8
                  + (var1.y - var3.y - (double)var9 * 0.5) * (double)var9
                  + (var1.z - var3.z - (double)var10 * 0.5) * (double)var10
               <= 0.0
         )) {
            for (double[] var14 : g) {
               double var15 = var14[0];
               double var17 = var14[1];

               var2.add(switch (var7.getAxis()) {
                  case Y -> new Vec3d((double)var0.getX() + var15, (double)(var0.getY() + (var9 > 0 ? 1 : 0)), (double)var0.getZ() + var17);
                  case X -> new Vec3d((double)(var0.getX() + (var8 > 0 ? 1 : 0)), (double)var0.getY() + var15, (double)var0.getZ() + var17);
                  case Z -> new Vec3d((double)var0.getX() + var15, (double)var0.getY() + var17, (double)(var0.getZ() + (var10 > 0 ? 1 : 0)));
                  default -> throw new MatchException(null, null);
               });
            }
         }
      }

      return var2;
   }

   @Override
   public void onTick() {
      this.a.mAsVoid(null);
      if (!this.isInGame()) {
         this.ZAsVoid();
      } else {
         if (this.KS != mc.player || this.c != mc.world) {
            this.ZAsVoid();
            this.KS = mc.player;
            this.c = mc.world;
         }

         this.hAsVoid();
         String var1 = this.b.contains(":") ? this.b.substring(0, this.b.indexOf(58)) : this.b;
         if (!var1.equals(this.K8)) {
            this.K8 = var1;
            ClientLog.XAsVoid("AutoCrystal", "Status: {}", this.b);
         }
      }
   }

   private void hAsVoid() {
      this.q.clear();
      if (!this.wAsBoolean()) {
         this.eAsVoid();
         this.b = "Paused: held-use optimizer, screen, focus, camera, item use or weakness";
      } else {
         long var1 = mc.world.getTime();
         if (var1 < this.X) {
            this.eAsVoid();
            this.b = String.format(Locale.ROOT, "Paused after kill: %.1f s left", (double)(this.X - var1) / 20.0);
         } else {
            this.t
               .AAsVoid(
                  var1,
                  var0 -> mc.world.getEntityById(var0) != null,
                  var0 -> !mc.world
                        .getOtherEntities((Entity)null, new Box(var0.up()).stretch(0.0, 1.0, 0.0), var0x -> var0x instanceof EndCrystalEntity)
                        .isEmpty()
               );
            if (!this.t.JAsBoolean() && !this.t.UAsBoolean()) {
               this.KO = null;
            } else {
               this.a.mAsVoid(this.KO);
               if (this.S != null && this.UAsBoolean(this.S)) {
                  SharedRenderEffects.UAsVoid(this.S);
               }
            }

            if (this.KD != null && (double)(var1 - this.KD.created()) < this.I.getValue() && this.aAsBoolean(this.KD)) {
               this.IAsVoid(this.KD, var1);
            } else {
               this.KD = null;
               ArrayList var3 = new ArrayList();

               for (Entity var5 : mc.world.getEntities()) {
                  if (var5 instanceof LivingEntity) {
                     LivingEntity var6 = (LivingEntity)var5;
                     if (this.UAsBoolean(var6)) {
                        var3.add(var6);
                     }
                  }
               }

               var3.sort(Comparator.comparingDouble(entity -> mc.player.squaredDistanceTo((net.minecraft.entity.Entity)entity)));
               if (var3.size() > 8) {
                  var3 = new ArrayList(var3.subList(0, 8));
               }

               if (var3.isEmpty()) {
                  this.b = "No target (enable Mobs to test on hostile mobs)";
               } else {
                  if (this.KE.getValue() && !this.hAsBoolean()) {
                     EndCrystalEntity var14 = null;
                     AutoCrystal.Type_b var16 = null;
                     Vec3d var18 = null;

                     for (Entity var8 : mc.world.getEntities()) {
                        if (var8 instanceof EndCrystalEntity) {
                           EndCrystalEntity var9 = (EndCrystalEntity)var8;
                           if (this.t.cAsBoolean(var1, var8.getId()) && !(b_.HAsDouble(var8) > this.ZAsDouble())) {
                              AutoCrystal.Type_b var10 = this.NAsAutoCrystal_Type_b(var8.getEntityPos(), var3);
                              if (var10 != null && (var16 == null || !(var10.value() <= var16.value()))) {
                                 Optional var11 = this.PAsOptional(var9, null);
                                 if (!var11.isEmpty()) {
                                    var14 = var9;
                                    var16 = var10;
                                    var18 = (Vec3d)var11.get();
                                 }
                              }
                           }
                        }
                     }

                     if (var14 != null) {
                        this.KD = new AutoCrystal.Type_g(null, var14, var16.target(), var1, var18, null);
                        this.IAsVoid(this.KD, var1);
                        return;
                     }
                  }

                  if (this.t.UAsBoolean()) {
                     this.b = "Waiting for crystal removal";
                  } else if (!this.k.getValue()) {
                     this.b = "No suitable crystal to break";
                  } else if (this.t.JAsBoolean()) {
                     this.b = "Waiting for crystal spawn";
                  } else if (!this.t.IAsBoolean(var1)) {
                     this.b = "Placement cooldown";
                  } else if (this.jAsInt() == -1) {
                     this.b = "No crystals in hands or hotbar";
                  } else {
                     BlockHitResult var15 = null;
                     AutoCrystal.Type_b var17 = null;
                     AutoCrystal.Type_h var19 = new AutoCrystal.Type_h();
                     int var20 = (int)Math.ceil(this.JAsDouble());
                     BlockPos var21 = BlockPos.ofFloored(mc.player.getEyePos());

                     for (BlockPos var24 : BlockPos.iterate(var21.add(-var20, -var20, -var20), var21.add(var20, var20, var20))) {
                        BlockPos var25 = var24.toImmutable();
                        BlockHitResult var12 = this.gAsBlockHitResult(var25, null);
                        if (var12 != null) {
                           var19.C++;
                           AutoCrystal.Type_b var13 = this.qAsAutoCrystal_Type_b(Vec3d.ofBottomCenter(var25.up()), var3, var19, null, true, false);
                           if (var13 != null && (var17 == null || var13.value() > var17.value())) {
                              var15 = var12;
                              var17 = var13;
                           }
                        }
                     }

                     if (this.KB.getValue() && (var17 == null || !var17.lethal() && !this.obsidianMode.pAsBoolean("Damage"))) {
                        AutoCrystal.Type_g var23 = this.WAsAutoCrystal_Type_g(var3, var17, var1, var19);
                        if (var23 != null) {
                           this.KD = var23;
                           this.IAsVoid(this.KD, var1);
                           return;
                        }

                        if (var15 == null) {
                           this.b = this.Kb;
                           return;
                        }
                     }

                     if (var15 == null) {
                        this.b = this.NAsString(var19);
                     } else {
                        this.KD = new AutoCrystal.Type_g(var15.getBlockPos(), null, var17.target(), var1, var15.getPos(), null);
                        this.IAsVoid(this.KD, var1);
                     }
                  }
               }
            }
         }
      }
   }

   private AutoCrystal.Type_g WAsAutoCrystal_Type_g(List<LivingEntity> var1, AutoCrystal.Type_b var2, long var3, AutoCrystal.Type_h var5) {
      boolean var6 = this.obsidianMode.pAsBoolean("Lethal");
      double var7 = var2 == null ? Double.NaN : var2.damage();
      if (var3 < this.K) {
         this.Kb = "Waiting for a crystal on the new obsidian";
         return null;
      } else if (BlockPlacementController.qAsInt(Items.OBSIDIAN, this.d.getValue()) == -1) {
         this.Kb = this.NAsString(var5) + "; no obsidian in hands or hotbar";
         return null;
      } else {
         Vec3d var9 = mc.player.getEyePos();
         double var10 = this.JAsDouble();
         int var12 = (int)Math.ceil(var10);
         BlockPos var13 = BlockPos.ofFloored(var9);
         ArrayList var14 = new ArrayList();

         for (BlockPos var16 : BlockPos.iterate(var13.add(-var12, -var12, -var12), var13.add(var12, var12, var12))) {
            if (this.VAsBoolean(var16, var9, var10)) {
               Vec3d var17 = Vec3d.ofBottomCenter(var16.up());
               double var18 = Double.MAX_VALUE;

               for (LivingEntity var21 : var1) {
                  double var22 = var21.getEntityPos().add(this.BAsVec3d(var21)).distanceTo(var17);
                  double var24 = var6 ? aAsDouble(var21) : this.GAsDouble(var21);
                  if (var2 != null && this.obsidianMode.pAsBoolean("Best")) {
                     var24 = Math.max(var24, Math.min(aAsDouble(var21), var7 + this.G.getValue()));
                  }

                  if ((double)nf.vAsFloat(var22, 1.0) * 1.5 >= var24) {
                     var18 = Math.min(var18, var22);
                  }
               }

               if (var18 < Double.MAX_VALUE) {
                  var14.add(new AutoCrystal.Type_y(var16.toImmutable(), var18));
               }
            }
         }

         var14.sort(Comparator.comparingDouble(AutoCrystal.Type_y::distance));
         AutoCrystal.Type_h var26 = new AutoCrystal.Type_h();
         AutoCrystal.Type_b var27 = null;
         BlockHitResult var28 = null;
         BlockPos var29 = null;
         int var19 = 0;

         for (AutoCrystal.Type_y var31 : (Iterable<AutoCrystal.Type_y>) (var14.subList(0, Math.min(96, var14.size())))) {
            if (var19 >= 24) {
               break;
            }

            BlockPos var32 = var31.pos();
            if (this.ZAsBoolean(var32) && this.XAsBoolean(var32, var9, var10)) {
               BlockHitResult var23 = BlockPlacementController.HAsBlockHitResult(var32, Items.OBSIDIAN, var10, true, this.d.getValue());
               if (var23 != null) {
                  var26.C++;
                  var19++;
                  AutoCrystal.Type_b var33 = this.qAsAutoCrystal_Type_b(Vec3d.ofBottomCenter(var32.up()), var1, var26, var32, true, var6);
                  if (var33 != null
                     && HAsBoolean(this.obsidianMode.getValue(), var7, false, var33.damage(), var33.lethal(), this.G.getValue())
                     && (var27 == null || !(var33.value() <= var27.value()))) {
                     var27 = var33;
                     var28 = var23;
                     var29 = var32;
                  }
               }
            }
         }

         if (var27 == null) {
            this.Kb = this.jAsString(var26, var6);
            return null;
         } else {
            return new AutoCrystal.Type_g(var29, null, var27.target(), var3, var28.getPos(), var28);
         }
      }
   }

   private boolean VAsBoolean(BlockPos var1, Vec3d var2, double var3) {
      if (!(var2.squaredDistanceTo(Vec3d.ofCenter(var1)) > (var3 + 1.0) * (var3 + 1.0))
         && BlockPlacementController.jAsBoolean(var1)
         && mc.world.isAir(var1.up())) {
         if (this.KE.getValue() && o5.lAsDouble(var2, ZAsBox(var1)) > this.ZAsDouble()) {
            return false;
         } else {
            for (Direction var8 : Direction.values()) {
               if (BlockPlacementController.CAsBoolean(var1.offset(var8))) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   private boolean ZAsBoolean(BlockPos var1) {
      return mc.world.getOtherEntities((Entity)null, new Box(var1).stretch(0.0, 2.0, 0.0)).isEmpty();
   }

   private boolean XAsBoolean(BlockPos var1, Vec3d var2, double var3) {
      for (Vec3d var6 : HAsList(var1, var2)) {
         if (!(var2.squaredDistanceTo(var6) > var3 * var3)
            && mc.world.raycast(new RaycastContext(var2, var6, ShapeType.OUTLINE, FluidHandling.NONE, mc.player)).getType() == Type.MISS) {
            return true;
         }
      }

      return false;
   }

   private BlockHitResult JAsBlockHitResult(BlockHitResult var1, BlockPos var2, Vec3d var3) {
      BlockHitResult var4 = BlockPlacementController.BAsBlockHitResult(var1.getBlockPos(), var3, this.JAsDouble());
      if (var4 != null && var4.getSide() == var1.getSide() && BlockPlacementController.CAsBoolean(var4.getBlockPos())) {
         BlockPlacementController.Type_i var5 = BlockPlacementController.vAsRe_Type_i(var4, Items.OBSIDIAN, this.d.getValue());
         return var5 != null && var5.target().equals(var2) ? var4 : null;
      } else {
         return null;
      }
   }

   private String jAsString(AutoCrystal.Type_h var1, boolean var2) {
      if (var1.C == 0) {
         return "No reachable obsidian or bedrock base, or open spot to place one";
      } else {
         return var2 && !Double.isNaN(var1.H) && var1.E < var1.C
            ? String.format(Locale.ROOT, "No lethal obsidian spot: best %.1f HP against %.1f HP health", var1.k, var1.H)
            : this.NAsString(var1);
      }
   }

   public void AAsVoid(EntityStatusS2CPacket var1) {
      if (this.isEnabled()
         && var1.getStatus() == 3
         && this.l.getValue()
         && this.isRuntimeActive()
         && this.isInGame()
         && var1.getEntity(mc.world) instanceof LivingEntity var2
         && this.dAsBoolean(var2)) {
         this.X = mc.world.getTime() + Math.round(this.Y.getValue() * 20.0);
         this.t.zAsVoid();
         this.eAsVoid();
         this.tAsVoid();
         this.KO = null;
         this.S = null;
         this.b = String.format(Locale.ROOT, "Paused after kill: %.1f s left", this.Y.getValue());
         ClientLog.GAsVoid(
            "AutoCrystal", "{} died nearby; placing and breaking paused for {} s to protect drops", var2.getName().getString(), this.Y.getValue().intValue()
         );
      }
   }

   private String NAsString(AutoCrystal.Type_h var1) {
      if (var1.C == 0) {
         return "No reachable obsidian or bedrock base";
      } else {
         return var1.E == var1.C
            ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var1.Y, this.x.getValue())
            : String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var1.r, this.F.getValue());
      }
   }

   private void eAsVoid() {
      this.KD = null;
      this.j = null;
      this.s = null;
      this.A.VAsVoid();
      b_.KAsVoid(this);
   }

   private double JAsDouble() {
      return Math.min(this.L.getValue(), Math.min(this.K_.getValue(), mc.player.getBlockInteractionRange()));
   }

   private double ZAsDouble() {
      return Math.min(this.L.getValue(), Math.min(this.Z.getValue(), mc.player.getEntityInteractionRange()));
   }

   private boolean hAsBoolean() {
      return mc.player.hasStatusEffect(StatusEffects.WEAKNESS) && mc.player.getAttributeValue(EntityAttributes.ATTACK_DAMAGE) <= 0.0;
   }

   private boolean aAsBoolean(AutoCrystal.Type_g var1) {
      if (this.wAsBoolean() && this.UAsBoolean(var1.victim())) {
         long var2 = mc.world.getTime();
         if (var1.crystal() != null) {
            return this.KE.getValue()
               && !this.hAsBoolean()
               && this.t.cAsBoolean(var2, var1.crystal().getId())
               && this.PAsOptional(var1.crystal(), var1.aim()).isPresent()
               && this.NAsAutoCrystal_Type_b(var1.crystal().getEntityPos(), List.of(var1.victim())) != null;
         } else if (var1.support() != null) {
            Vec3d var4 = mc.player.getEyePos();
            return this.k.getValue()
               && this.KB.getValue()
               && this.t.IAsBoolean(var2)
               && var2 >= this.K
               && this.jAsInt() != -1
               && BlockPlacementController.qAsInt(Items.OBSIDIAN, this.d.getValue()) != -1
               && this.VAsBoolean(var1.base(), var4, this.JAsDouble())
               && this.ZAsBoolean(var1.base())
               && this.XAsBoolean(var1.base(), var4, this.JAsDouble())
               && this.JAsBlockHitResult(var1.support(), var1.base(), var1.aim().subtract(var4)) != null
               && this.qAsAutoCrystal_Type_b(
                     Vec3d.ofBottomCenter(var1.base().up()), List.of(var1.victim()), null, var1.base(), true, this.obsidianMode.pAsBoolean("Lethal")
                  )
                  != null;
         } else {
            return this.k.getValue()
               && this.t.IAsBoolean(var2)
               && this.jAsInt() != -1
               && this.gAsBlockHitResult(var1.base(), var1.aim()) != null
               && this.qAsAutoCrystal_Type_b(Vec3d.ofBottomCenter(var1.base().up()), List.of(var1.victim()), null, null, true, false) != null;
         }
      } else {
         return false;
      }
   }

   private boolean UAsBoolean(AutoCrystal.Type_g var1) {
      if (var1.support() != null) {
         return this.JAsBlockHitResult(var1.support(), var1.base(), mc.player.getRotationVector()) != null;
      } else {
         return var1.crystal() != null ? b_.nAsBoolean(var1.crystal(), this.ZAsDouble()) : b_.TAsBlockHitResult(var1.base(), this.JAsDouble()) != null;
      }
   }

   public void LAsVoid() {
      if (this.isEnabled() && this.isRuntimeActive() && this.isInGame() && this.KD != null && this.O == mc.world.getTime()) {
         if (b_.nAsBoolean(this) && this.aAsBoolean(this.KD)) {
            if (this.rotation.pAsBoolean("Visible")) {
               if (!p5.fAsBoolean(p5.Type_u.AUTOCRYSTAL)) {
                  this.A.VAsVoid();
                  return;
               }

               p5.jAsVoid(p5.Type_u.AUTOCRYSTAL);
               this.P = mc.player;
               this.v = mc.world;
               this.KY = mc.player.age;
               float[] var1 = dq.QAsFloatArray(mc.player.getEyePos(), this.KD.aim());
               float[] var2 = com.larpclient.t.JAsFloatArray(mc.player.getYaw(), mc.player.getPitch(), var1[0], var1[1], this.T.getValue());
               dq.yAsVoid(dq.Type_s.AUTOCRYSTAL, var2[0], var2[1], 1.0);
               dq.aAsVoid();
            }

            this.A.hAsVoid((long)mc.player.age, this.UAsBoolean(this.KD));
            this.b = this.A.EAsBoolean((long)mc.player.age, this.H.getValue().intValue())
               ? "Aim ready"
               : (
                  !this.rotation.pAsBoolean("Visible")
                     ? "Waiting for your crosshair"
                     : (this.KD.support() != null ? "Aiming at obsidian spot" : "Aiming at crystal action")
               );
         } else {
            this.A.VAsVoid();
         }
      }
   }

   private boolean qAsBoolean(AutoCrystal.Type_g var1) {
      return this.KD == var1
         && this.O == mc.world.getTime()
         && this.aAsBoolean(var1)
         && this.A.EAsBoolean((long)mc.player.age, this.H.getValue().intValue())
         && this.UAsBoolean(var1);
   }

   private void IAsVoid(AutoCrystal.Type_g var1, long var2) {
      BlockPos var4 = var1.base() != null ? var1.base() : var1.crystal().getBlockPos().down();
      this.a.mAsVoid(var4);
      SharedRenderEffects.UAsVoid(var1.victim());
      this.S = var1.victim();
      this.O = var2;
      Object var5 = var1.support() != null ? new AutoCrystal.Type_z(var1.base()) : (var1.crystal() == null ? var1.base() : var1.crystal());
      if (!Objects.equals(this.j, var5) || this.s != var1.victim()) {
         this.A.VAsVoid();
      }

      this.j = var5;
      this.s = var1.victim();
      if (var1.crystal() != null) {
         b_.tAsVoid(this, var1.crystal(), this.ZAsDouble(), false, () -> this.qAsBoolean(var1), 1, () -> {
            this.t.gAsVoid(mc.world.getTime(), var1.crystal().getId(), this.y.getValue().intValue(), this.Kw.getValue().intValue());
            this.KO = var4;
            this.b = "Waiting for crystal removal";
            this.eAsVoid();
         }, var2x -> {
            if (this.qAsBoolean(var1)) {
               this.b = "Waiting: " + var2x;
            }
         });
      } else if (var1.support() != null) {
         b_.UAsVoid(this, () -> this.tAsVoid(var1), () -> this.qAsBoolean(var1));
      } else {
         b_.UAsVoid(this, () -> this.XAsVoid(var1.base()), () -> this.qAsBoolean(var1));
      }
   }

   private void tAsVoid(AutoCrystal.Type_g var1) {
      BlockHitResult var2 = this.JAsBlockHitResult(var1.support(), var1.base(), mc.player.getRotationVector());
      int var3 = BlockPlacementController.qAsInt(Items.OBSIDIAN, this.d.getValue());
      if (var2 != null && var3 != -1) {
         int var4 = mc.player.getInventory().getSelectedSlot();
         int var5 = var3 >= 0 && var3 != var4 ? 1 : 0;

         try {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var3);
            }

            Hand var6 = var3 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND;
            ActionResult var7 = mc.interactionManager.interactBlock(mc.player, var6, var2);
            long var8 = mc.world.getTime();
            if (var7.isAccepted()) {
               mc.player.swingHand(var6);
               this.t.nAsVoid(var8, this.Kz.getValue().intValue());
               this.K = var8 + (long)this.Kw.getValue().intValue();
               this.b = "Placed obsidian; crystal next";
               ClientLog.XAsVoid(
                  "AutoCrystal",
                  "Obsidian base at {} for {} ({} mode)",
                  var1.base().toShortString(),
                  var1.victim().getName().getString(),
                  this.obsidianMode.getValue()
               );
            } else {
               this.t.nAsVoid(var8, this.R.getValue().intValue());
               this.b = "Obsidian placement rejected; retrying";
            }

            this.eAsVoid();
         } finally {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var4);
            }
         }
      }
   }

   private int jAsInt() {
      if (mc.player.getOffHandStack().isOf(Items.END_CRYSTAL)) {
         return -2;
      } else {
         int var1 = mc.player.getInventory().getSelectedSlot();
         if (mc.player.getMainHandStack().isOf(Items.END_CRYSTAL)) {
            return var1;
         } else {
            if (this.d.getValue()) {
               for (int var2 = 0; var2 < 9; var2++) {
                  if (mc.player.getInventory().getStack(var2).isOf(Items.END_CRYSTAL)) {
                     return var2;
                  }
               }
            }

            return -1;
         }
      }
   }

   private BlockHitResult gAsBlockHitResult(BlockPos var1, Vec3d var2) {
      if (mc.world.isChunkLoaded(var1) && mc.world.getWorldBorder().contains(var1)) {
         BlockState var3 = mc.world.getBlockState(var1);
         if ((var3.isOf(Blocks.OBSIDIAN) || var3.isOf(Blocks.BEDROCK))
            && mc.world.isAir(var1.up())
            && mc.world.getOtherEntities(null, new Box(var1.up()).stretch(0.0, 1.0, 0.0)).isEmpty()) {
            Vec3d var4 = mc.player.getEyePos();
            double var5 = this.JAsDouble();
            if (var4.squaredDistanceTo(Vec3d.ofCenter(var1)) > (var5 + 1.0) * (var5 + 1.0)) {
               return null;
            } else if (this.KE.getValue() && o5.lAsDouble(var4, ZAsBox(var1)) > this.ZAsDouble()) {
               return null;
            } else {
               if (var2 != null) {
                  BlockHitResult var7 = b_.eAsBlockHitResult(var1, var5, var2.subtract(var4));
                  if (var7 != null) {
                     return var7;
                  }
               }

               for (Vec3d var8 : HAsList(var1, var4)) {
                  BlockHitResult var9 = b_.eAsBlockHitResult(var1, var5, var8.subtract(var4));
                  if (var9 != null) {
                     return var9;
                  }
               }

               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void XAsVoid(BlockPos var1) {
      BlockHitResult var2 = b_.TAsBlockHitResult(var1, this.JAsDouble());
      int var3 = this.jAsInt();
      if (var2 != null && var3 != -1 && this.gAsBlockHitResult(var1, var2.getPos()) != null) {
         int var4 = mc.player.getInventory().getSelectedSlot();
         int var5 = var3 >= 0 && var3 != var4 ? 1 : 0;

         try {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var3);
            }

            Hand var6 = var3 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND;
            ActionResult var7 = mc.interactionManager.interactBlock(mc.player, var6, var2);
            this.t.xAsVoid(mc.world.getTime(), var1, this.R.getValue().intValue(), var7.isAccepted(), this.Kw.getValue().intValue());
            if (var7.isAccepted()) {
               mc.player.swingHand(var6);
               this.KO = var1;
               this.K = 0L;
               this.b = "Waiting for crystal spawn";
            } else {
               this.b = "Placement rejected; retrying";
            }

            this.eAsVoid();
         } finally {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var4);
            }
         }
      }
   }

   private static record Type_b(LivingEntity target, double damage, double self, boolean lethal) {
      double value() {
         return this.lethal ? 1000.0 - this.self : this.damage - this.self * 0.25;
      }
   }

   private static record Type_g(BlockPos base, EndCrystalEntity crystal, LivingEntity victim, long created, Vec3d aim, BlockHitResult support) {
   }

   private static final class Type_h {
      int C;
      int E;
      double Y = Double.MAX_VALUE;
      double r;
      double k;
      double H = Double.NaN;

      private Type_h() {
      }
   }

   private static record Type_y(BlockPos pos, double distance) {
   }

   private static record Type_z(BlockPos pos) {
   }
}
