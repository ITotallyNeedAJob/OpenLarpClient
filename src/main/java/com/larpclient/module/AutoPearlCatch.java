package com.larpclient.module;

import com.larpclient.BlockPlacementController;
import com.larpclient.ClientLog;
import com.larpclient.PearlCatchAttempt;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ModuleDetector;
import com.larpclient.OfferItem;
import com.larpclient.ParticleEmitter;
import com.larpclient.ParticleGeometry;
import com.larpclient.QueueItem;
import com.larpclient.mixin.ClientPlayerInteractionManagerAccessor;
import com.larpclient.mixin.MinecraftClientAccessor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class AutoPearlCatch extends Module {
   private static final int R = 20;
   private static final int o = 20;
   private static final int n = 20;
   private static final int A = 3;
   private static final int f = 4;
   private static final double I = 0.6;
   private static final double h = 5.0;
   private static final double y = 0.05;
   private static AutoPearlCatch V;
   private final KeybindSetting catchBind = this.addSetting(
      new KeybindSetting("Catch Bind", "Press for one pearl catch; pressing again while it throws cancels it. Separate from the module toggle", -1)
   );
   private final ModeSetting direction = this.addSetting(
      new ModeSetting("Direction", "Up throws straight up for height; Look throws where you aim for distance", "Up", "Up", "Look")
   );
   private final ModeSetting timing = this.addSetting(
      new ModeSetting(
         "Timing",
         "Instant throws both in one tick for a catch a few blocks out; Solved aims the wind charge a tick or more later for a farther catch; Fixed Delay throws it along the same look after Delay",
         "Instant",
         "Instant",
         "Solved",
         "Fixed Delay"
      )
   );
   private final NumberSetting delay = this.addSetting(
      new NumberSetting("Delay", "Ticks from the pearl to the wind charge; same-look catches work near straight up", 2.0, 1.0, 10.0, 1.0)
   );
   private final NumberSetting turnSpeed = this.addSetting(
      new NumberSetting("Turn Speed", "Maximum player turn in degrees per tick; your camera stays on your look. 180 turns at once", 90.0, 10.0, 180.0, 5.0)
   );
   private final BooleanSetting swapBack = this.addSetting(new BooleanSetting("Swap Back", "Return to your previous hotbar slot after each throw", true));
   private final pg Z = new pg(this);
   private final PearlCatchAttempt s = new PearlCatchAttempt();
   private final Set<Integer> a = new HashSet<>();
   private final List<AutoPearlCatch.Type_g> O = new ArrayList<>();
   private ClientPlayerEntity H;
   private ClientWorld t;
   private long j;
   private long i = Long.MIN_VALUE;
   private long z = -1L;
   private float[] X;
   private float S;
   private float r;
   private List<oi.Type_d> T;
   private Vec3d p;
   private Vec3d g = Vec3d.ZERO;
   private Vec3d N;
   private Vec3d B = Vec3d.ZERO;
   private Vec3d C = Vec3d.ZERO;
   private Vec3d G = Vec3d.ZERO;
   private Vec3d e = Vec3d.ZERO;
   private float E;
   private float c;
   private EnderPearlEntity L;
   private oi.Type_v l;
   private oi.Type_v Q;
   private String b = "Set a Catch Bind";

   public AutoPearlCatch() {
      super("Auto Pearl Catch", "Throw a pearl, then a wind charge it hits in flight, to teleport mid-air", Category.MOVEMENT, -1);
      V = this;
      this.delay.visibleWhen(this::zAsBoolean);
      this.direction.onChanged(var1 -> this.DAsVoid("Direction changed"));
      this.timing.onChanged(var1 -> this.DAsVoid("Timing changed"));
      this.catchBind.onChanged(var1 -> this.DAsVoid("Bind changed"));
   }

   public static AutoPearlCatch cAsAutoPearlCatch() {
      return V;
   }

   public List<Setting<?>> LAsList() {
      return List.of(this.catchBind, this.direction, this.timing, this.delay);
   }

   public List<Setting<?>> zAsList() {
      return List.of(this.turnSpeed, this.swapBack);
   }

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.INTENT;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.VANILLA_INPUT, k.SERVER_STATE_MODIFYING);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.b : "";
   }

   public boolean NAsBoolean() {
      return this.isEnabled()
         ? this.isRuntimeActive() && this.H != null && this.H == mc.player && this.t == mc.world && (this.s.kAsBoolean() || this.j == this.i)
         : false;
   }

   public static void JAsVoid(int var0) {
      if (V != null && V.isEnabled()) {
         AutoPearlCatch var1 = V;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.catchBind.getValue() && !var1.s.xAsBoolean()) {
            var1.DAsVoid("Cancelled with Catch Bind");
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.DAsVoid("Waiting for a world");
         this.O.clear();
      } else {
         if (this.H != mc.player || this.t != mc.world) {
            float[] var1 = !this.s.kAsBoolean() && this.j != this.i ? null : this.X;
            this.DAsVoid("World changed");
            this.O.clear();
            this.H = mc.player;
            this.t = mc.world;
            this.p = null;
            if (var1 != null) {
               this.H.setYaw(var1[0]);
               this.H.setPitch(var1[1]);
               this.H.lastYaw = var1[0];
               this.H.lastPitch = var1[1];
               ClientLog.GAsVoid(
                  "PearlCatch", "World changed during a catch; your look (yaw {} pitch {}) restored", sAsString((double)var1[0]), sAsString((double)var1[1])
               );
            }
         }

         this.j++;
         if (p5.NAsP5_Type_u() == p5.Type_u.PEARLCATCH) {
            this.X = p5.sAsFloatArray(p5.JAsFloat(), p5.mAsFloat(), mc.options.getPerspective().isFrontView());
         }

         Vec3d var5 = this.H.getEntityPos();
         double var6;
         this.g = this.p != null && ((var6 = var5.squaredDistanceTo(this.p) - 16.0) == 0.0 ? 0 : (var6 < 0.0 ? -1 : 1)) <= 0
            ? var5.subtract(this.p)
            : Vec3d.ZERO;
         this.p = var5;
         this.NAsVoid();
         byte var2 = (byte)(this.s.HAsBoolean() ? 1 : 0);
         PearlCatchAttempt.Type_w var3 = this.s.jAsWc_Type_w();
         if (var3 == PearlCatchAttempt.Type_w.IDLE) {
            if (var2 != 0 && !this.s.lAsBoolean()) {
               this.S = this.r = 0.0F;
               this.s.vAsVoid(this.j);
            }

            if (this.s.lAsBoolean()) {
               this.OAsVoid();
            }
         } else {
            String var4 = this.hAsString();
            if (var4 != null) {
               this.DAsVoid(var4);
            } else if (this.s.CAsBoolean(this.j)) {
               this.DAsVoid(
                  var3 == PearlCatchAttempt.Type_w.PEARL
                     ? "The view did not reach the throw angle in time"
                     : (this.zAsBoolean() ? "The wind charge could not be thrown on its tick" : "No reliable catch before the pearl came down")
               );
            } else if (var3 == PearlCatchAttempt.Type_w.PEARL) {
               this.zAsVoid();
            } else {
               this.oAsVoid();
            }
         }
      }
   }

   private void OAsVoid() {
      String var1 = this.hAsString();
      if (var1 == null) {
         var1 = this.NAsString();
      }

      if (var1 != null) {
         this.s.nAsVoid();
         this.dAsVoid(var1);
      } else {
         int var2 = this.nAsInt();
         if (var2 > 0) {
            if (var2 <= 30 && !this.s.uAsBoolean(this.j)) {
               this.b = String.format(Locale.ROOT, "Catch in %.2f s (cooldown)", (double)var2 / 20.0);
            } else {
               this.s.nAsVoid();
               this.dAsVoid(String.format(Locale.ROOT, "Ender pearl or wind charge is cooling down (%.1f s left)", (double)var2 / 20.0));
            }
         } else {
            this.s.nAsVoid();
            this.LAsVoid();
         }
      }
   }

   private int nAsInt() {
      float var1 = this.mAsFloat(Items.ENDER_PEARL);
      float var2 = this.mAsFloat(Items.WIND_CHARGE);
      int var3 = Math.max(mAsInt(var1, this.S, Items.ENDER_PEARL), mAsInt(var2, this.r, Items.WIND_CHARGE));
      this.S = var1;
      this.r = var2;
      return var3;
   }

   private float mAsFloat(Item var1) {
      int var2 = BlockPlacementController.qAsInt(var1, true);
      if (var2 == -1) {
         return 0.0F;
      } else {
         ItemStack var3 = var2 == -2 ? this.H.getOffHandStack() : this.H.getInventory().getStack(var2);
         return this.H.getItemCooldownManager().getCooldownProgress(var3, 0.0F);
      }
   }

   private static int mAsInt(float var0, float var1, Item var2) {
      if (var0 <= 0.0F) {
         return 0;
      } else {
         float var3 = var1 - var0;
         if (var3 > 0.0F) {
            return (int)Math.ceil((double)(var0 / var3));
         } else {
            UseCooldownComponent var4 = (UseCooldownComponent)new ItemStack(var2).get(DataComponentTypes.USE_COOLDOWN);
            return (int)Math.ceil((double)(var0 * (float)(var4 == null ? 20 : var4.getCooldownTicks())));
         }
      }
   }

   private void LAsVoid() {
      String var1 = this.hAsString();
      if (var1 == null) {
         var1 = this.NAsString();
      }

      if (var1 != null) {
         this.dAsVoid(var1);
      } else {
         float[] var2 = this.BAsFloatArray();
         float var3 = var2[0];
         float var4 = var2[1];
         float var5 = this.MAsBoolean() ? -90.0F : var4;
         Vec3d var6 = this.H.getEyePos();
         oi.Type_v var7;
         if (this.qAsBoolean()) {
            oi.Type_y var8 = oi.iAsOi_Type_y(var6, var3, var5, this.UAsVec3d(), this.zAsOi_Type_q(), 0.6);
            if (var8 == null) {
               this.dAsVoid(this.MAsBoolean() ? "No instant catch: clear the space above you" : "No instant catch along this look; clear the space ahead");
               return;
            }

            var5 = var8.pitch();
            var7 = var8.plan();
         } else {
            Vec3d var11 = oi.uAsVec3d(var3, var5);
            var7 = this.zAsBoolean()
               ? oi.uAsOi_Type_v(
                  this.VAsList(var6, var11, this.UAsVec3d(), this.delay.getValue().intValue()), var6, this.UAsVec3d(), var11, this.zAsOi_Type_q()
               )
               : this.FAsOi_Type_v(var6, var11, this.UAsVec3d());
            if (var7 == null || var7.reliability() < 0.6) {
               this.dAsVoid(this.rAsString(var7));
               return;
            }
         }

         this.E = var3;
         this.c = var5;
         this.l = var7;
         this.a.clear();

         for (Entity var9 : mc.world.getEntities()) {
            if (var9 instanceof EnderPearlEntity) {
               EnderPearlEntity var10 = (EnderPearlEntity)var9;
               if (var10.getOwner() == this.H) {
                  this.a.add(var10.getId());
               }
            }
         }

         this.s.vAsVoid(this.j, 20);
         this.b = this.rAsBoolean(var3, var5) ? "Throwing" : "Turning to throw";
         ClientLog.GAsVoid(
            "PearlCatch",
            "Catch started; direction={}, timing={}, pitch {}{}, forecast catch on step {} at {} ({}/{} samples)",
            this.direction.getValue(),
            this.timing.getValue(),
            sAsString((double)var5),
            !this.MAsBoolean() && var5 < var4 - 0.5F ? " raised from " + sAsString((double)var4) : "",
            var7.nominal().tick(),
            kAsString(var7.nominal().point()),
            var7.score(),
            var7.samples()
         );
         this.zAsVoid();
      }
   }

   private oi.Type_v FAsOi_Type_v(Vec3d var1, Vec3d var2, Vec3d var3) {
      oi.Type_v var4 = null;

      for (int var5 = 1; var5 <= 20; var5++) {
         oi.Type_v var6 = oi.SAsOi_Type_v(this.VAsList(var1, var2, var3, var5), var1, var3, this.zAsOi_Type_q());
         if (var6 != null && var6.reliability() >= 0.6) {
            return var6;
         }

         if (var6 != null && (var4 == null || var6.score() > var4.score())) {
            var4 = var6;
         }
      }

      return var4;
   }

   private List<oi.Type_d> VAsList(Vec3d var1, Vec3d var2, Vec3d var3, int var4) {
      return oi.NAsList(var1, var2, var3, var4, this.zAsOi_Type_q());
   }

   private String rAsString(oi.Type_v var1) {
      if (this.zAsBoolean() && !this.MAsBoolean()) {
         return "Fixed Delay only catches near straight up; use Instant or Solved for Look";
      } else if (var1 == null) {
         return this.MAsBoolean()
            ? "No catch: the pearl would come down first, so clear the space above you"
            : "No catch at this angle; aim higher or use Instant";
      } else {
         return String.format(
            Locale.ROOT,
            "Only %d of %d samples catch here; %s",
            var1.score(),
            var1.samples(),
            this.zAsBoolean() ? "try another Delay or Instant" : (this.MAsBoolean() ? "clear the space above you" : "aim higher or use Instant")
         );
      }
   }

   private void zAsVoid() {
      if (b_.nAsBoolean(this)) {
         this.SAsVoid(this.E, this.c);
      }

      b_.UAsVoid(this, this.qAsBoolean() ? this::DAsVoid : this::hAsVoid, this::HAsBoolean);
   }

   private boolean HAsBoolean() {
      if (!this.isRuntimeActive()
         || this.s.jAsWc_Type_w() != PearlCatchAttempt.Type_w.PEARL
         || this.hAsString() != null
         || !this.rAsBoolean(this.E, this.c)
         || !this.xAsBoolean(Items.ENDER_PEARL)
         || !this.xAsBoolean(Items.WIND_CHARGE)
         || !this.eAsBoolean(Items.ENDER_PEARL)
         || Aura.WAsBoolean()) {
         return false;
      } else if (!this.qAsBoolean()) {
         return true;
      } else if (!this.eAsBoolean(Items.WIND_CHARGE)) {
         return false;
      } else {
         Vec3d var1 = this.H.getEyePos();
         Vec3d var2 = oi.uAsVec3d(this.H.getYaw(), this.H.getPitch());
         oi.Type_v var3 = oi.uAsOi_Type_v(oi.jAsList(var1, var2, this.UAsVec3d(), this.zAsOi_Type_q()), var1, this.UAsVec3d(), var2, this.zAsOi_Type_q());
         if (var3 != null && !(var3.reliability() < 0.6)) {
            this.Q = var3;
            return true;
         } else {
            return false;
         }
      }
   }

   private void DAsVoid() {
      oi.Type_v var1 = this.Q;
      this.B = this.H.getEntityPos();
      byte var3 = 0;

      boolean var7;
      try {
         var7 = this.gAsBoolean(Items.ENDER_PEARL);
         if (var7) {
            var3 = (byte)(this.gAsBoolean(Items.WIND_CHARGE) ? 1 : 0);
         }
      } finally {
         this.tAsVoid();
      }

      if (!var7) {
         this.DAsVoid("The pearl throw was rejected");
      } else if (var3 == 0) {
         this.DAsVoid("The wind charge was rejected after the pearl, so the pearl flies on");
      } else {
         this.l = var1;
         this.i = this.j;
         this.s.jAsVoid(this.j, 1);
         this.s.vAsVoid();
         this.kAsVoid(var1);
         this.b = "Pearl and wind charge thrown together";
         ClientLog.GAsVoid(
            "PearlCatch",
            "Instant pair thrown at yaw {} pitch {} from {}; expected catch on step {} at {} ({}/{} samples)",
            sAsString((double)this.H.getYaw()),
            sAsString((double)this.H.getPitch()),
            kAsString(this.H.getEyePos()),
            var1 == null ? "?" : var1.nominal().tick(),
            var1 == null ? "?" : kAsString(var1.nominal().point()),
            var1 == null ? 0 : var1.score(),
            var1 == null ? 0 : var1.samples()
         );
      }
   }

   private void hAsVoid() {
      this.B = this.H.getEntityPos();
      Vec3d var1 = this.H.getEyePos();
      Vec3d var2 = this.UAsVec3d();
      if (!this.fAsBoolean(Items.ENDER_PEARL)) {
         this.DAsVoid("The pearl throw was rejected");
      } else {
         this.E = this.H.getYaw();
         this.c = this.H.getPitch();
         this.C = var1;
         this.G = oi.uAsVec3d(this.E, this.c);
         this.e = var2;
         this.s.jAsVoid(this.j, this.zAsBoolean() ? this.delay.getValue().intValue() + 1 : 21);
         this.b = this.zAsBoolean() ? "Pearl thrown; wind charge in " + this.delay.getValue().intValue() + " ticks" : "Pearl thrown; aiming the wind charge";
         ClientLog.GAsVoid("PearlCatch", "Pearl thrown at yaw {} pitch {} from {}", sAsString((double)this.E), sAsString((double)this.c), kAsString(var1));
      }
   }

   private void oAsVoid() {
      this.aAsVoid();
      if (this.L != null && this.L.isRemoved()) {
         this.DAsVoid("The pearl came down before the wind charge");
      } else if (this.zAsBoolean()) {
         if (b_.nAsBoolean(this)) {
            this.SAsVoid(this.E, this.c);
         }

         if (this.s.XAsLong(this.j) >= (long)this.delay.getValue().intValue()) {
            b_.UAsVoid(this, this::xAsVoid, this::TAsBoolean);
         }
      } else {
         List var1 = this.WAsList();
         if (this.N == null) {
            this.N = ((oi.Type_d)var1.get(0)).BAsVec3d();
         }

         oi.Type_v var2 = oi.SAsOi_Type_v(var1, this.H.getEyePos(), this.UAsVec3d(), this.zAsOi_Type_q());
         if (var2 != null && !(var2.reliability() < 0.6)) {
            this.l = var2;
            float[] var3 = dq.QAsFloatArray(Vec3d.ZERO, var2.direction());
            this.E = var3[0];
            this.c = MathHelper.clamp(var3[1], -90.0F, 90.0F);
            if (b_.nAsBoolean(this)) {
               this.SAsVoid(this.E, this.c);
            }

            b_.UAsVoid(this, this::xAsVoid, this::TAsBoolean);
            this.b = "Aiming wind charge";
         } else {
            this.b = "Waiting for a reliable catch line";
         }
      }
   }

   private boolean TAsBoolean() {
      if (this.isRuntimeActive()
         && this.s.jAsWc_Type_w() == PearlCatchAttempt.Type_w.CHARGE
         && this.hAsString() == null
         && this.xAsBoolean(Items.WIND_CHARGE)
         && this.eAsBoolean(Items.WIND_CHARGE)
         && !Aura.WAsBoolean()) {
         Vec3d var1 = oi.uAsVec3d(this.H.getYaw(), this.H.getPitch());
         oi.Type_v var2 = oi.uAsOi_Type_v(this.WAsList(), this.H.getEyePos(), this.UAsVec3d(), var1, this.zAsOi_Type_q());
         if (this.zAsBoolean()) {
            this.Q = var2;
            return this.s.XAsLong(this.j) == (long)this.delay.getValue().intValue() && this.rAsBoolean(this.E, this.c);
         } else if (var2 != null && !(var2.reliability() < 0.6)) {
            this.Q = var2;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void xAsVoid() {
      oi.Type_v var1 = this.Q;
      long var2 = this.s.XAsLong(this.j);
      if (!this.fAsBoolean(Items.WIND_CHARGE)) {
         this.DAsVoid("The wind charge throw was rejected");
      } else {
         this.l = var1;
         this.i = this.j;
         this.s.vAsVoid();
         this.kAsVoid(var1);
         this.b = "Wind charge thrown";
         if (var1 == null) {
            ClientLog.GAsVoid("PearlCatch", "Wind charge thrown {} ticks after the pearl; no catch expected along this look", var2);
         } else {
            ClientLog.GAsVoid(
               "PearlCatch",
               "Wind charge thrown {} ticks after the pearl at yaw {} pitch {}; expected catch on step {} at {} ({}/{} samples){}",
               var2,
               sAsString((double)this.H.getYaw()),
               sAsString((double)this.H.getPitch()),
               var1.nominal().tick(),
               kAsString(var1.nominal().point()),
               var1.score(),
               var1.samples(),
               var1.nominal().burst() ? ", charge bursts on you" : ""
            );
         }
      }
   }

   private void kAsVoid(oi.Type_v var1) {
      if (this.O.size() >= 4) {
         this.O.remove(0);
      }

      int var2 = (var1 == null ? 60 : var1.nominal().tick()) + this.DAsInt() + 20;
      AutoPearlCatch.Type_g var3 = new AutoPearlCatch.Type_g(new HashSet<>(this.a), this.B, var1, this.N, this.j, this.j + (long)var2);
      var3.F = this.L;
      this.O.add(var3);
      this.L = null;
      this.a.clear();
      this.N = null;
   }

   private void NAsVoid() {
      Iterator var1 = this.O.iterator();

      while (var1.hasNext()) {
         AutoPearlCatch.Type_g var2 = (AutoPearlCatch.Type_g)var1.next();
         if (var2.F == null) {
            var2.F = this.AAsEnderPearlEntity(var2.R);
         }

         EnderPearlEntity var3 = var2.F;
         if (var2.L < 0L && var3 != null && (var3.isRemoved() || mc.world.getEntityById(var3.getId()) != var3)) {
            var2.L = this.j;
         }

         if (var2.w == null && !var2.s && var2.L < 0L && var3 != null) {
            var2.w = oi.FAsOi_Type_d(new oi.Type_x(var3.getEntityPos(), var3.getVelocity(), 1 + var3.age), this.zAsOi_Type_q()).BAsVec3d();
            var2.s = true;
         }

         if (var2.L >= 0L && this.j > var2.L) {
            this.qAsVoid(var2);
            var1.remove();
         } else if (this.j - var2.A >= 0L) {
            String var4 = var3 == null ? "Pearl not seen; the outcome is unknown" : "No catch observed in time";
            ClientLog.GAsVoid("PearlCatch", "{}, {} ticks after the wind charge", var4, this.j - var2.W);
            if (!this.s.kAsBoolean()) {
               this.b = var4;
            }

            var1.remove();
         }
      }
   }

   private EnderPearlEntity AAsEnderPearlEntity(Set<Integer> var1) {
      for (Entity var3 : mc.world.getEntities()) {
         if (var3 instanceof EnderPearlEntity var4
            && !var4.isRemoved()
            && var4.getOwner() == this.H
            && !var1.contains(var4.getId())
            && this.O.stream().noneMatch(var1x -> var1x.F == var4)) {
            return var4;
         }
      }

      return null;
   }

   private void qAsVoid(AutoPearlCatch.Type_g var1) {
      Vec3d var2 = this.H.getEntityPos();
      Vec3d var3 = var1.m == null ? null : var1.m.nominal().point();
      double var4 = var3 == null ? Double.NaN : var2.distanceTo(var3);
      int var6 = var2.distanceTo(var1.a) > 1.0 ? 1 : 0;
      String var7;
      if (var1.w != null && var2.distanceTo(var1.w) <= 1.0) {
         var7 = "Missed: the pearl landed";
      } else if (var6 == 0 || var3 != null && !(var4 <= 3.0)) {
         if (var6 != 0) {
            var7 = String.format(
               Locale.ROOT,
               "Caught %.1f blocks out, %.1f up, %.1f blocks from the predicted point (a tick of server timing)",
               var2.distanceTo(var1.a),
               var2.y - var1.a.y,
               var4
            );
         } else {
            var7 = "No teleport: the pearl ended near where you threw it";
         }
      } else {
         var7 = String.format(Locale.ROOT, "Caught %.1f blocks out, %.1f up", var2.distanceTo(var1.a), var2.y - var1.a.y);
      }

      ClientLog.GAsVoid(
         "PearlCatch",
         "{}; now at {}, expected {} ({} blocks off), {} ticks after the wind charge",
         var7,
         kAsString(var2),
         var3 == null ? "unknown" : kAsString(var3),
         Double.isNaN(var4) ? "?" : sAsString(var4),
         this.j - var1.W
      );
      if (!this.s.kAsBoolean()) {
         this.b = var7;
      }
   }

   private List<oi.Type_d> WAsList() {
      if (this.z != this.j || this.T == null) {
         this.T = this.VAsList(this.C, this.G, this.e, (int)Math.max(1L, this.s.XAsLong(this.j)));
         this.z = this.j;
      }

      return this.T;
   }

   private void aAsVoid() {
      if (this.L == null) {
         for (Entity var2 : mc.world.getEntities()) {
            if (var2 instanceof EnderPearlEntity var3 && !var3.isRemoved() && var3.getOwner() == this.H && !this.a.contains(var3.getId())) {
               this.L = var3;
               ClientLog.XAsVoid("PearlCatch", "Own pearl seen {} ticks after the throw, {} client ticks old", this.s.XAsLong(this.j), var3.age);
               return;
            }
         }
      }
   }

   private String hAsString() {
      if (!b_.jAsBoolean() || mc.currentScreen != null) {
         return "Paused: a screen is open";
      } else if (!mc.isWindowFocused()) {
         return "Paused: the window lost focus";
      } else if (p5.NAsP5_Type_u() != p5.Type_u.PEARLCATCH && !p5.fAsBoolean(p5.Type_u.PEARLCATCH)) {
         return "Paused: Freecam or Freelook holds the view";
      } else if (this.H.hasVehicle()) {
         return "Not while riding";
      } else {
         return !this.H.isSubmergedIn(FluidTags.WATER) && !this.H.isInLava() ? null : "Not with your head in water or lava";
      }
   }

   private String NAsString() {
      if (this.H.isUsingItem()) {
         return "Stop using your item first";
      } else if (Aura.WAsBoolean()) {
         return "Paused while Kill Aura lowers your shield";
      } else if (BlockPlacementController.qAsInt(Items.ENDER_PEARL, true) == -1) {
         return "No ender pearls in your hotbar or hands";
      } else if (BlockPlacementController.qAsInt(Items.WIND_CHARGE, true) == -1) {
         return "No wind charges in your hotbar or hands";
      } else {
         return !this.H.getAbilities().invulnerable && (double)(this.H.getHealth() + this.H.getAbsorptionAmount()) <= 5.0
            ? "The 5 HP pearl hit would be lethal"
            : null;
      }
   }

   private boolean xAsBoolean(Item var1) {
      int var2 = BlockPlacementController.qAsInt(var1, true);
      if (var2 == -1) {
         return false;
      } else {
         ItemStack var3 = var2 == -2 ? this.H.getOffHandStack() : this.H.getInventory().getStack(var2);
         return var3.isItemEnabled(this.t.getEnabledFeatures()) && !this.H.getItemCooldownManager().isCoolingDown(var3);
      }
   }

   private boolean eAsBoolean(Item var1) {
      int var2 = BlockPlacementController.qAsInt(var1, true);
      return var2 == -2 || var2 == this.H.getInventory().getSelectedSlot() || var2 >= 0 && pg.yAsBoolean(this);
   }

   private boolean fAsBoolean(Item var1) {
      boolean var2;
      try {
         var2 = this.gAsBoolean(var1);
      } finally {
         this.tAsVoid();
      }

      return var2;
   }

   private boolean gAsBoolean(Item var1) {
      int var2 = BlockPlacementController.qAsInt(var1, true);
      if (var2 == -1) {
         return false;
      } else {
         Hand var3 = var2 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND;
         if (var2 >= 0 && var2 != this.H.getInventory().getSelectedSlot() && !this.Z.uAsBoolean(this.H, var2)) {
            return false;
         } else {
            boolean var4 = mc.interactionManager.interactItem(this.H, var3).isAccepted();
            if (var4) {
               this.H.swingHand(var3);
               ((MinecraftClientAccessor)mc).larpUseDelay(4);
            }

            return var4;
         }
      }
   }

   private void tAsVoid() {
      if (this.Z.FAsBoolean()) {
         int var1 = this.H == null ? -1 : this.H.getInventory().getSelectedSlot();
         this.Z.vAsVoid(getMc(), this.swapBack.getValue());
         if (this.H != null && this.H == mc.player && mc.interactionManager != null && var1 != this.H.getInventory().getSelectedSlot()) {
            ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).larpSyncCarriedItem();
         }
      }
   }

   private float[] BAsFloatArray() {
      return !p5.TAsBoolean()
         ? new float[]{this.H.getYaw(), this.H.getPitch()}
         : p5.sAsFloatArray(p5.JAsFloat(), p5.mAsFloat(), mc.options.getPerspective().isFrontView());
   }

   private void SAsVoid(float var1, float var2) {
      p5.jAsVoid(p5.Type_u.PEARLCATCH);
      if (p5.NAsP5_Type_u() == p5.Type_u.PEARLCATCH) {
         float[] var3 = com.larpclient.t.JAsFloatArray(this.H.getYaw(), this.H.getPitch(), var1, var2, this.turnSpeed.getValue());
         dq.RAsVoid(this, dq.Type_s.PEARLCATCH, var3[0], var3[1], 1.0);
      }
   }

   private boolean rAsBoolean(float var1, float var2) {
      return (double)Math.abs(MathHelper.wrapDegrees(this.H.getYaw() - var1)) <= 0.05 && (double)Math.abs(this.H.getPitch() - var2) <= 0.05;
   }

   private Vec3d UAsVec3d() {
      return this.H.isOnGround() ? new Vec3d(this.g.x, 0.0, this.g.z) : this.g;
   }

   private int DAsInt() {
      ClientPlayNetworkHandler var1 = mc.getNetworkHandler();
      PlayerListEntry var2 = var1 != null && this.H != null ? var1.getPlayerListEntry(this.H.getUuid()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round((float)var2.getLatency() / 50.0F));
   }

   private oi.Type_q zAsOi_Type_q() {
      return new qi(this.t, this.H);
   }

   private boolean MAsBoolean() {
      return this.direction.pAsBoolean("Up");
   }

   private boolean zAsBoolean() {
      return this.timing.pAsBoolean("Fixed Delay");
   }

   private boolean qAsBoolean() {
      return this.timing.pAsBoolean("Instant");
   }

   private static String sAsString(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String kAsString(Vec3d var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.x, var0.y, var0.z);
   }

   private void dAsVoid(String var1) {
      this.b = var1;
      ClientLog.GAsVoid("PearlCatch", "Catch refused: {}", var1);
   }

   private void DAsVoid(String var1) {
      boolean var2 = this.s.kAsBoolean();
      b_.KAsVoid(this);
      this.tAsVoid();
      this.GAsVoid();
      if (var2) {
         this.b = var1;
         ClientLog.GAsVoid("PearlCatch", "Catch cancelled: {}", var1);
      }
   }

   private void GAsVoid() {
      this.s.TAsVoid();
      p5.GAsVoid(p5.Type_u.PEARLCATCH);
      this.a.clear();
      this.L = null;
      this.l = this.Q = null;
      this.T = null;
      this.z = -1L;
      this.N = null;
   }

   @Override
   public void onEnable() {
      this.H = null;
      this.t = null;
      this.p = null;
      this.O.clear();
      this.i = Long.MIN_VALUE;
      this.GAsVoid();
      this.b = this.catchBind.getValue() < 0 ? "Set a Catch Bind" : "Ready";
      ClientLog.GAsVoid(
         "PearlCatch",
         "Enabled; direction={}, timing={}, delay={}, turn speed={}",
         this.direction.getValue(),
         this.timing.getValue(),
         this.delay.getValue().intValue(),
         this.turnSpeed.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.DAsVoid("Disabled");
      this.O.clear();
      this.H = null;
      this.t = null;
      ClientLog.GAsVoid("PearlCatch", "Disabled; pending throws and aim released");
   }

   private static final class Type_g {
      final Set<Integer> R;
      final Vec3d a;
      final oi.Type_v m;
      final long W;
      final long A;
      EnderPearlEntity F;
      Vec3d w;
      boolean s;
      long L = -1L;

      Type_g(Set<Integer> var1, Vec3d var2, oi.Type_v var3, Vec3d var4, long var5, long var7) {
         this.R = var1;
         this.a = var2;
         this.m = var3;
         this.w = var4;
         this.W = var5;
         this.A = var7;
      }
   }
}
