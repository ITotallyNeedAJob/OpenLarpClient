package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.CosmeticSubmissionHandler;
import com.larpclient.EffectGeometryRenderer;
import com.larpclient.HoldHistory;
import com.larpclient.LarpClient;
import com.larpclient.ParticleEmitterEngine;
import com.larpclient.RibbonCapacity;
import com.larpclient.SharedRenderEffects;
import com.larpclient.FadeCurve;
import com.larpclient.ColorUtil;
import com.larpclient.MediaSession;
import com.larpclient.OpenScreen;
import com.larpclient.QueueNode;
import com.larpclient.EffectRenderParams;
import io.netty.channel.ChannelFutureListener;
import java.util.ArrayDeque;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPosition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TrackedPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkSentS2CPacket;
import net.minecraft.network.packet.s2c.play.DebugSampleS2CPacket;
import net.minecraft.network.packet.s2c.play.EndCombatS2CPacket;
import net.minecraft.network.packet.s2c.play.EnterCombatS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionSyncS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySetHeadYawS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.ProfilelessChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.StartChunkSendS2CPacket;
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

public final class Backtrack extends Module implements QueueNode {
   private static Backtrack D;
   private static final int y = 65536;
   private static final long z = System.nanoTime();
   private final ModeSetting version = this.addSetting(new ModeSetting("Version", "Packet delay algorithm", "New", "Old", "New"));
   private final NumberSetting rangeMin = this.addSetting(new NumberSetting("Range Min", "Lower bound of the box distance to the target", 1.0, 0.0, 10.0, 0.1));
   private final NumberSetting rangeMax = this.addSetting(new NumberSetting("Range Max", "Upper bound of the box distance to the target", 3.0, 0.0, 10.0, 0.1));
   private final NumberSetting delayMin = this.addSetting(new NumberSetting("Delay Min", "Shortest hold window in milliseconds", 280.0, 0.0, 2000.0, 1.0));
   private final NumberSetting delayMax = this.addSetting(new NumberSetting("Delay Max", "Longest hold window in milliseconds", 480.0, 0.0, 2000.0, 1.0));
   private final NumberSetting nextBacktrackDelayMin = this.addSetting(
      new NumberSetting("Next Backtrack Delay Min", "Shortest wait before the next window in milliseconds", 0.0, 0.0, 2000.0, 1.0)
   );
   private final NumberSetting nextBacktrackDelayMax = this.addSetting(
      new NumberSetting("Next Backtrack Delay Max", "Longest wait before the next window in milliseconds", 10.0, 0.0, 2000.0, 1.0)
   );
   private final NumberSetting trackingBuffer = this.addSetting(
      new NumberSetting("Tracking Buffer", "Keep windows available after the target leaves range, in milliseconds", 500.0, 0.0, 2000.0, 1.0)
   );
   private final NumberSetting chance = this.addSetting(new NumberSetting("Chance", "Chance that an attack allows windows, in percent", 50.0, 0.0, 100.0, 1.0));
   private final BooleanSetting pauseOnHurtTime = this.addSetting(new BooleanSetting("Pause On Hurt Time", "Skip windows while the target is hurt", false));
   private final NumberSetting hurtTime = this.addSetting(new NumberSetting("Hurt Time", "Target hurt time that pauses windows", 3.0, 0.0, 10.0, 1.0));
   private final ModeSetting targetMode = this.addSetting(
      new ModeSetting("Target Mode", "Use the attacked entity or the nearest one in range", "Attack", "Attack", "Range")
   );
   private final NumberSetting timeSinceLastAttack = this.addSetting(
      new NumberSetting("Time Since Last Attack", "How long an attack keeps windows available, in milliseconds", 1000.0, 0.0, 5000.0, 1.0)
   );
   private final BooleanSetting releaseOutOfReach = this.addSetting(
      new BooleanSetting("Release Out Of Reach", "Release held packets as soon as the target's server position is too far to hit", true)
   );
   private final NumberSetting releaseDistance = this.addSetting(
      new NumberSetting(
         "Release Distance", "Server position distance from your eyes that releases held packets. Vanilla ignores hits past 6", 6.0, 1.0, 10.0, 0.1
      )
   );
   private final ModeSetting esp = this.addSetting(new ModeSetting("ESP", "Draw the target's server position", "Box", "Box", "Wireframe", "Fill", "None"));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Server position box color", -1));
   private final ColorSetting gradient = this.addSetting(new ColorSetting("Gradient", "Second ESP color, blended toward the top", -6862849));
   private final BooleanSetting ghostModel = this.addSetting(new BooleanSetting("Ghost Model", "Draw a hologram of the target at its server position", true));
   private final BooleanSetting tether = this.addSetting(new BooleanSetting("Tether", "Link the displayed target to its server position", true));
   private final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Glow pass for the ESP", true));
   private final BooleanSetting throughWalls = this.addSetting(new BooleanSetting("Through Walls", "Keep a faint outline visible behind blocks", true));
   private final NumberSetting newRangeMin = this.addSetting(
      new NumberSetting("New Range Min", "Lower bound of the distance to the target player", 0.0, 0.0, 10.0, 0.01)
   );
   private final NumberSetting newRangeMax = this.addSetting(
      new NumberSetting("New Range Max", "Upper bound of the distance to the target player", 10.0, 0.0, 10.0, 0.01)
   );
   private final NumberSetting newDelay = this.addSetting(
      new NumberSetting("New Delay", "How long each held update waits, in milliseconds", 15000.0, 0.0, 15000.0, 1.0)
   );
   private final BooleanSetting onlyAttack = this.addSetting(new BooleanSetting("Only Attack", "Release held updates when the target moves closer", true));
   private Entity C;
   private String d = "Old";
   private final HoldHistory R = new HoldHistory(65536);
   private final ArrayDeque<Runnable> e = new ArrayDeque<>();
   private final TrackedPosition x = new TrackedPosition();
   private boolean P;
   private boolean Wa;
   private long WY;
   private long g;
   private long q;
   private int b;
   private final ArrayDeque<Runnable> u = new ArrayDeque<>();
   private final ArrayDeque<Long> WL = new ArrayDeque<>();
   private TrackedPosition U;
   private boolean S;
   private ClientPlayNetworkHandler E;
   private ClientWorld W0;
   private volatile int Q;
   private static final double w = 0.55;
   private static final int[][] G = new int[][]{{0, 1}, {2, 3}, {4, 5}, {6, 7}, {0, 2}, {1, 3}, {4, 6}, {5, 7}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};
   private final RibbonCapacity.Type_v j = new RibbonCapacity.Type_v(25);
   private boolean WC;
   private boolean r;
   private Object n;
   private int F = -1;
   private int m;
   private int i;
   private Vec3d c;
   private double T;
   private double I;
   private double f;
   private double Ww;
   private double k;
   private double t = -1.0;
   private EntityRenderState WO;
   private Vec3d W6;
   private double W9;
   private double W_;
   private double Wv = -10.0;

   public Backtrack() {
      super("Backtrack", "Backtracks player position to a hittable state", Category.COMBAT, -1);
      D = this;
      this.rangeMin.WAsNumberSetting(this.rangeMax).displayName("Range");
      this.delayMin.WAsNumberSetting(this.delayMax).displayName("Delay");
      this.nextBacktrackDelayMin.WAsNumberSetting(this.nextBacktrackDelayMax).displayName("Next Backtrack Delay");
      this.newRangeMin.WAsNumberSetting(this.newRangeMax).displayName("New Range");
      this.trackingBuffer.dAsNumberSetting("ms");
      this.chance.dAsNumberSetting("%");
      this.hurtTime.dAsNumberSetting("");
      this.timeSinceLastAttack.dAsNumberSetting("ms").displayName("Last Attack Time To Work");
      this.newDelay.dAsNumberSetting("ms");

      for (Setting var4 : new Setting[]{
         this.rangeMin,
         this.rangeMax,
         this.delayMin,
         this.delayMax,
         this.nextBacktrackDelayMin,
         this.nextBacktrackDelayMax,
         this.trackingBuffer,
         this.chance,
         this.pauseOnHurtTime,
         this.targetMode,
         this.timeSinceLastAttack
      }) {
         var4.visibleWhen(() -> this.version.pAsBoolean("Old"));
      }

      this.hurtTime.visibleWhen(() -> this.version.pAsBoolean("Old") && this.pauseOnHurtTime.getValue());
      this.releaseDistance.visibleWhen(this.releaseOutOfReach::getValue);

      for (Setting var11 : new Setting[]{this.color, this.gradient, this.ghostModel, this.tether, this.bloom, this.throughWalls}) {
         var11.visibleWhen(() -> !this.esp.pAsBoolean("None"));
      }

      for (Setting var12 : new Setting[]{this.newRangeMin, this.newRangeMax, this.newDelay, this.onlyAttack}) {
         var12.visibleWhen(() -> this.version.pAsBoolean("New"));
      }

      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.xAsVoid());
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(com.larpclient.module.k.SERVER_STATE_MODIFYING);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      } else if (this.C == null) {
         return "Waiting for target";
      } else {
         int var1 = this.R.NAsInt() + this.u.size();
         return "Tracking " + this.C.getName().getString() + (var1 > 0 ? " · " + var1 + " held" : "");
      }
   }

   @Override
   public void onEnable() {
      this.d = this.version.getValue();
      this.b = ThreadLocalRandom.current().nextInt(0, 101);
      this.IAsVoid(false, false, false, null);
      this.GAsVoid(false, false, null);
      this.clear();
      this.FAsVoid();
   }

   @Override
   public void onDisable() {
      this.Q = 0;
      this.IAsVoid(true, false, true, "Stopped");
      this.GAsVoid(true, false, "Stopped");
      this.clear();
   }

   private void FAsVoid() {
      this.Q = !this.isRuntimeActive() ? 0 : (this.version.pAsBoolean("Old") ? (this.C == null && this.R.NAsInt() <= 0 ? 0 : 1) : (!this.u.isEmpty() ? 2 : 0));
   }

   public static void KAsVoid(Packet<?> var0, PacketListener var1) {
      if (D != null && D.isEnabled()) {
         Backtrack var2 = D;
         if (var2 != null && var2.Q != 0 && var1 instanceof ClientPlayNetworkHandler var3) {
            MinecraftClient var4 = getMc();
            if (var4 != null && !var4.isOnThread() && (var2.Q == 1 ? tAsBoolean(var0) : var0 instanceof DisconnectS2CPacket)) {
               NetworkThreadUtils.forceMainThread((net.minecraft.network.packet.Packet)var0, var3, var4.getPacketApplyBatcher());
            }
         }
      }
   }

   static boolean tAsBoolean(Packet<?> var0) {
      return var0 instanceof KeepAliveS2CPacket
         || var0 instanceof DisconnectS2CPacket
         || var0 instanceof CustomPayloadS2CPacket
         || var0 instanceof StartChunkSendS2CPacket
         || var0 instanceof ChunkSentS2CPacket
         || var0 instanceof DebugSampleS2CPacket
         || var0 instanceof EnterCombatS2CPacket
         || var0 instanceof EndCombatS2CPacket
         || var0 instanceof PingResultS2CPacket;
   }

   public static boolean pAsBoolean() {
      Backtrack var0 = D;
      return var0 != null && var0.R.NAsInt() > 0;
   }

   private static long dAsLong() {
      return (System.nanoTime() - z) / 1000000L;
   }

   private void uAsVoid() {
      String var1 = this.version.getValue();
      if (!var1.equals(this.d)) {
         this.IAsVoid(true, false, false, "Version changed");
         this.GAsVoid(true, false, "Version changed");
         this.d = var1;
      }
   }

   private boolean VAsBoolean() {
      MinecraftClient var1 = getMc();
      return this.E != null && var1.getNetworkHandler() == this.E && var1.world == this.W0 && this.E.getConnection().isOpen();
   }

   private void MAsVoid() {
      if (this.R.NAsInt() <= 0 && this.e.isEmpty() && this.u.isEmpty()) {
         this.E = mc.getNetworkHandler();
         this.W0 = mc.world;
      }
   }

   public static boolean WAsBoolean() {
      Backtrack var0 = D;
      return var0 != null && (var0.R.VAsBoolean() || var0.S || var0.Wa);
   }

   public static boolean MAsBoolean(Packet<?> var0, PacketListener var1, Runnable var2) {
      if (D != null && D.isEnabled()) {
         Backtrack var3 = D;
         MinecraftClient var4 = getMc();
         if (var3 != null
            && !WAsBoolean()
            && var4.isOnThread()
            && !(var0 instanceof BundleS2CPacket)
            && var1 instanceof ClientPlayNetworkHandler
            && var1 == var4.getNetworkHandler()
            && var3.isRuntimeActive()) {
            boolean var5;
            try {
               var3.uAsVoid();
               var5 = var3.version.pAsBoolean("New") ? var3.CAsBoolean(var0, var2) : var3.JAsBoolean(var0, var2);
            } finally {
               var3.FAsVoid();
            }

            return var5;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean zAsBoolean(ClientConnection var0, Packet<?> var1, ChannelFutureListener var2, boolean var3) {
      if (D != null && D.isEnabled()) {
         Backtrack var4 = D;
         MinecraftClient var5 = getMc();
         if (var4 != null
            && !WAsBoolean()
            && var5 != null
            && var5.isOnThread()
            && var4.isRuntimeActive()
            && var5.player != null
            && var5.world != null
            && var5.getNetworkHandler() != null
            && !var5.isInSingleplayer()
            && var0 == var5.getNetworkHandler().getConnection()) {
            var4.uAsVoid();
            if (!var4.version.pAsBoolean("New") && var1 instanceof CommonPongC2SPacket && var4.R.NAsInt() != 0) {
               var4.e.addLast(() -> var0.send(var1, var2, var3));
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean zAsBoolean() {
      Backtrack var0 = D;
      return var0 != null && var0.Wa;
   }

   public static void EAsVoid(Entity var0) {
      if (D != null && D.isEnabled()) {
         Backtrack var1 = D;
         if (var1 != null && var0 != null && var1.isRuntimeActive() && var1.isInGame()) {
            try {
               var1.uAsVoid();
               if (var1.version.pAsBoolean("New")) {
                  var1.JAsVoid(var0);
                  return;
               }

               var1.g = dAsLong();
               var1.b = ThreadLocalRandom.current().nextInt(0, 101);
               if (var1.targetMode.pAsBoolean("Attack")) {
                  var1.iAsVoid(var0);
               }
            } finally {
               var1.FAsVoid();
            }

            return;
         }
      }
   }

   @Override
   public void onTick() {
      try {
         this.TAsVoid();
      } finally {
         this.FAsVoid();
      }
   }

   private void TAsVoid() {
      this.uAsVoid();
      if (this.version.pAsBoolean("New")) {
         this.cAsVoid();
      } else if (mc.player != null && mc.world != null) {
         if (this.targetMode.pAsBoolean("Range")) {
            Entity var1 = this.EAsEntity();
            if (var1 == null) {
               this.IAsVoid(true, false, true, "No target in range");
            } else {
               this.iAsVoid(var1);
            }
         }

         int var2 = this.R.NAsInt() > 0 ? 1 : 0;
         if (var2 == 0 || !this.OAsBoolean(this.TAsBox())) {
            if (this.iAsBoolean()) {
               if (var2 != 0 && this.R.eAsBoolean(dAsLong())) {
                  this.iAsVoid("Window ended");
                  this.IAsVoid(false, false, true, null);
               }
            } else if (var2 != 0) {
               this.iAsVoid("Target no longer eligible");
               this.IAsVoid(false, false, true, null);
            }
         }
      } else {
         this.IAsVoid(false, true, false, "World unavailable");
      }
   }

   private boolean JAsBoolean(Packet<?> var1, Runnable var2) {
      if (mc.player == null || mc.world == null || mc.getNetworkHandler() == null || mc.isInSingleplayer()) {
         return false;
      } else if (kAsBoolean(var1)) {
         return false;
      } else if (this.XAsBoolean(var1)) {
         this.IAsVoid(true, false, true, "Server boundary packet");
         return false;
      } else {
         boolean var3 = this.iAsBoolean();
         int var4 = this.R.NAsInt() > 0 ? 1 : 0;
         if (var4 == 0 && !var3) {
            return false;
         } else {
            Entity var5 = this.C;
            if (var4 == 0 && !(var1 instanceof CommonPingS2CPacket)) {
               this.dAsVec3d(var1, var5);
               return false;
            } else {
               if (var5 != null) {
                  Vec3d var6 = this.dAsVec3d(var1, var5);
                  if (var6 != null && this.OAsBoolean(var5.getType().getDimensions().getBoxAt(var6))) {
                     return false;
                  }

                  if (var6 != null && this.iAsBoolean(var5, var6)) {
                     this.iAsVoid("Target moved closer");
                     return false;
                  }
               }

               if (var4 == 0) {
                  if (var5 != null && this.QAsBoolean(this.TAsBox())) {
                     return false;
                  }

                  this.MAsVoid();
                  long var8 = dAsLong(this.delayMin, this.delayMax);
                  if (this.R.UAsBoolean()) {
                     this.R.xAsVoid();
                  }

                  this.R.JAsVoid(dAsLong(), var8);
                  ClientLog.XAsVoid("Backtrack", "Old window opened for {} ms", var8);
               }

               if (this.R.aAsBoolean(var2)) {
                  return true;
               } else {
                  this.iAsVoid("Packet capacity reached");
                  return false;
               }
            }
         }
      }
   }

   private static boolean kAsBoolean(Packet<?> var0) {
      if (var0 instanceof ChatMessageS2CPacket
         || var0 instanceof GameMessageS2CPacket
         || var0 instanceof ProfilelessChatMessageS2CPacket
         || var0 instanceof OverlayMessageS2CPacket) {
         return true;
      } else {
         if (var0 instanceof PlaySoundS2CPacket var1 && var1.getSound().value() == SoundEvents.ENTITY_PLAYER_HURT) {
            return true;
         }

         return false;
      }
   }

   private boolean XAsBoolean(Packet<?> var1) {
      if (var1 instanceof PlayerPositionLookS2CPacket || var1 instanceof DisconnectS2CPacket) {
         return true;
      } else {
         if (var1 instanceof HealthUpdateS2CPacket var3 && var3.getHealth() <= 0.0F
            || var1 instanceof EntitiesDestroyS2CPacket var2 && this.C != null && var2.getEntityIds().contains(this.C.getId())) {
            return true;
         }

         return false;
      }
   }

   private Vec3d dAsVec3d(Packet<?> var1, Entity var2) {
      return var2 != null && mc.world != null ? yAsVec3d(this.x, var1, mc.world, var2) : null;
   }

   static Vec3d yAsVec3d(TrackedPosition var0, Packet<?> var1, ClientWorld var2, Entity var3) {
      Vec3d var4;
      label37: {
         var4 = null;
         if (var1 instanceof EntityS2CPacket var5 && var5.isPositionChanged() && var5.getEntity(var2) == var3) {
            var4 = var0.withDelta((long)var5.getDeltaX(), (long)var5.getDeltaY(), (long)var5.getDeltaZ());
            break label37;
         }

         if (var1 instanceof EntityPositionS2CPacket var6 && var6.entityId() == var3.getId()) {
            var4 = var6.relatives().isEmpty()
               ? var6.change().position()
               : EntityPosition.apply(new EntityPosition(var0.getPos(), var3.getVelocity(), var3.getYaw(), var3.getPitch()), var6.change(), var6.relatives())
                  .position();
            break label37;
         }

         if (var1 instanceof EntityPositionSyncS2CPacket var7 && var7.id() == var3.getId()) {
            var4 = var7.values().position();
         }
      }

      if (var4 != null) {
         var0.setPos(var4);
      }

      return var4;
   }

   private boolean iAsBoolean(Entity var1, Vec3d var2) {
      Box var3 = var1.getType().getDimensions().getBoxAt(var2);
      Box var4 = mc.player.getBoundingBox();
      return cAsDouble(var4, var3) < cAsDouble(var4, var1.getBoundingBox());
   }

   private boolean iAsBoolean() {
      return this.C != null && this.C.isAlive() && this.uAsBoolean(this.C);
   }

   private boolean uAsBoolean(Entity var1) {
      if (mc.player != null && var1 != null) {
         boolean var2 = this.pAsBoolean(var1);
         long var3 = dAsLong();
         if (var2) {
            this.WY = var3;
         }

         return (var2 || this.WY != 0L && var3 - this.WY <= this.trackingBuffer.getValue().longValue())
            && this.NAsBoolean(var1)
            && mc.player.age > 10
            && (double)this.b < this.chance.getValue()
            && var3 >= this.q
            && (!this.pauseOnHurtTime.getValue() || !this.P)
            && this.g != 0L
            && var3 - this.g <= this.timeSinceLastAttack.getValue().longValue();
      } else {
         return false;
      }
   }

   private boolean NAsBoolean(Entity var1) {
      if (!(var1 instanceof LivingEntity var2) || mc.player == null || var1 == mc.player) {
         return false;
      }

      if (!var2.isAlive() || var2.isRemoved() || var2.isSpectator() || !var2.canHit()) {
         return false;
      } else if (!(var1 instanceof PlayerEntity)) {
         return true;
      } else {
         MCFriend var3 = LarpClient.CAsLarpClient().mAsF().EAsModule(MCFriend.class);
         return var3 == null || !var3.SAsBoolean(var1.getName().getString());
      }
   }

   private boolean pAsBoolean(Entity var1) {
      double var2 = Math.sqrt(cAsDouble(mc.player.getBoundingBox(), var1.getBoundingBox()));
      return var2 >= Math.min(this.rangeMin.getValue(), this.rangeMax.getValue()) && var2 <= Math.max(this.rangeMin.getValue(), this.rangeMax.getValue());
   }

   private Entity EAsEntity() {
      double var1 = Math.min(this.rangeMin.getValue(), this.rangeMax.getValue());
      double var3 = Math.max(this.rangeMin.getValue(), this.rangeMax.getValue());
      Box var5 = mc.player.getBoundingBox();
      Entity var6 = null;
      double var7 = Double.MAX_VALUE;

      for (Entity var10 : mc.world.getOtherEntities(mc.player, var5.expand(var3 + 1.0), this::NAsBoolean)) {
         double var11 = Math.sqrt(cAsDouble(var5, var10.getBoundingBox()));
         if (var11 >= var1 && var11 <= var3 && var11 < var7) {
            var6 = var10;
            var7 = var11;
         }
      }

      return var6;
   }

   private void iAsVoid(Entity var1) {
      boolean var10001;
      label25: {
         if (var1 instanceof LivingEntity var2 && (double)var2.hurtTime >= this.hurtTime.getValue()) {
            var10001 = true;
            break label25;
         }

         var10001 = false;
      }

      this.P = var10001;
      if (this.uAsBoolean(var1)) {
         if (var1 != this.C) {
            this.IAsVoid(true, false, false, "Target changed");
            this.x.setPos(var1.getTrackedPosition().getPos());
         }

         this.C = var1;
      }
   }

   private void IAsVoid(boolean var1, boolean var2, boolean var3, String var4) {
      if (var1 && !var2) {
         this.iAsVoid(var4);
      } else if (var2) {
         this.ZAsVoid(var4);
      }

      if (this.C != null && var3) {
         this.q = dAsLong() + dAsLong(this.nextBacktrackDelayMin, this.nextBacktrackDelayMax);
      }

      this.C = null;
      this.P = false;
      this.x.setPos(Vec3d.ZERO);
   }

   private void iAsVoid(String var1) {
      int var2 = this.R.NAsInt() + this.e.size();
      if (var2 == 0) {
         this.R.xAsVoid();
      } else if (mc.getNetworkHandler() != null && this.VAsBoolean()) {
         this.Wa = true;

         try {
            while (!this.e.isEmpty() && this.VAsBoolean()) {
               this.e.removeFirst().run();
            }
         } finally {
            this.Wa = false;
            this.e.clear();
         }

         this.R.IAsVoid(this::VAsBoolean);
         if (var1 != null) {
            ClientLog.XAsVoid("Backtrack", "Released {} held packets: {}", var2, var1);
         }
      } else {
         this.ZAsVoid(var1 + "; session changed");
      }
   }

   private void ZAsVoid(String var1) {
      int var2 = this.R.NAsInt() + this.e.size();
      this.R.xAsVoid();
      this.e.clear();
      if (var2 > 0 && var1 != null) {
         ClientLog.XAsVoid("Backtrack", "Discarded {} held packets: {}", var2, var1);
      }
   }

   private void cAsVoid() {
      if (mc.player == null || mc.world == null) {
         this.GAsVoid(true, true, "World unavailable");
      } else if (!this.lAsBoolean()) {
         this.GAsVoid(true, false, "Target left New range");
      } else if (!this.OAsBoolean(this.TAsBox())) {
         this.OAsVoid(false, null);
      }
   }

   private boolean CAsBoolean(Packet<?> var1, Runnable var2) {
      if (mc.player == null || mc.world == null) {
         this.GAsVoid(true, true, "World unavailable");
         return false;
      } else {
         label42:
         if (!(var1 instanceof PlayerPositionLookS2CPacket) && !(var1 instanceof DisconnectS2CPacket)) {
            if (var1 instanceof HealthUpdateS2CPacket var3 && var3.getHealth() <= 0.0F) {
               break label42;
            }

            if (this.u.isEmpty() && !this.lAsBoolean()) {
               return false;
            }

            this.EAsVoid(var1);
            if (this.C != null && !zAsBoolean(var1) && TAsBoolean(var1)) {
               if (this.u.size() >= 65536) {
                  this.OAsVoid(true, "Packet capacity reached");
                  return false;
               }

               if (this.u.isEmpty()) {
                  this.MAsVoid();
                  ClientLog.XAsVoid("Backtrack", "New queue opened with {} ms delay", this.newDelay.getValue().longValue());
               }

               this.u.addLast(var2);
               this.WL.addLast(dAsLong());
               return true;
            }

            return false;
         }

         this.GAsVoid(true, false, "Server boundary packet");
         return false;
      }
   }

   private static boolean zAsBoolean(Packet<?> var0) {
      return var0 instanceof PlaySoundS2CPacket || var0 instanceof HealthUpdateS2CPacket || var0 instanceof EntityTrackerUpdateS2CPacket;
   }

   private static boolean TAsBoolean(Packet<?> var0) {
      return var0 instanceof CommonPingS2CPacket
         || var0 instanceof EntitySetHeadYawS2CPacket
         || var0 instanceof EntityPositionS2CPacket
         || var0 instanceof EntityPositionSyncS2CPacket
         || var0 instanceof EntityVelocityUpdateS2CPacket
         || var0 instanceof EntityS2CPacket
         || var0 instanceof ParticleS2CPacket
         || var0 instanceof ExplosionS2CPacket;
   }

   private void EAsVoid(Packet<?> var1) {
      if (mc.player != null && mc.world != null && this.C != null) {
         Vec3d var2;
         label48: {
            var2 = null;
            if (var1 instanceof EntityS2CPacket var3 && var3.isPositionChanged() && var3.getEntity(mc.world) == this.C && this.U != null) {
               var2 = this.U.withDelta((long)var3.getDeltaX(), (long)var3.getDeltaY(), (long)var3.getDeltaZ());
               break label48;
            }

            if (var1 instanceof EntityPositionS2CPacket var4 && var4.entityId() == this.C.getId()) {
               var2 = var4.change().position();
               break label48;
            }

            if (var1 instanceof EntityPositionSyncS2CPacket var5 && var5.id() == this.C.getId()) {
               var2 = var5.values().position();
            }
         }

         if (var2 != null) {
            if (this.U == null) {
               this.U = new TrackedPosition();
            }

            this.U.setPos(var2);
            Box var6 = this.C.getBoundingBox().offset(var2.subtract(this.C.getEntityPos()));
            if (!this.OAsBoolean(var6) && this.onlyAttack.getValue()) {
               Vec3d var7 = mc.player.getEntityPos();
               if (rAsDouble(var7, var6) < rAsDouble(var7, this.C.getBoundingBox())) {
                  this.OAsVoid(true, "Target moved closer");
               }
            }
         }
      }
   }

   private boolean lAsBoolean() {
      return this.C != null && this.C.isAlive() && this.XAsBoolean(this.C);
   }

   private boolean XAsBoolean(Entity var1) {
      if (mc.player != null && var1 instanceof PlayerEntity && var1 != mc.player) {
         double var2 = rAsDouble(mc.player.getEntityPos(), var1.getBoundingBox());
         double var4 = Math.min(this.newRangeMin.getValue(), this.newRangeMax.getValue());
         double var6 = Math.max(this.newRangeMin.getValue(), this.newRangeMax.getValue());
         return var2 >= var4 * var4 && var2 <= var6 * var6;
      } else {
         return false;
      }
   }

   private void JAsVoid(Entity var1) {
      if (this.XAsBoolean(var1)) {
         if (var1 != this.C) {
            this.GAsVoid(true, false, "Target changed");
            this.U = new TrackedPosition();
            this.U.setPos(var1.getEntityPos());
            ClientLog.XAsVoid("Backtrack", "New target {}", var1.getName().getString());
         }

         this.C = var1;
      }
   }

   private void GAsVoid(boolean var1, boolean var2, String var3) {
      if (var2) {
         this.eAsVoid(var3);
      } else if (var1) {
         this.OAsVoid(true, var3);
      }

      this.C = null;
      this.U = null;
   }

   private void OAsVoid(boolean var1, String var2) {
      if (!this.S && !this.u.isEmpty()) {
         if (mc.getNetworkHandler() != null && this.VAsBoolean()) {
            long var3 = dAsLong();
            long var5 = this.newDelay.getValue().longValue();
            int var7 = 0;
            this.S = true;

            try {
               while (!this.u.isEmpty() && (var1 || var3 - this.WL.peekFirst() >= var5) && this.VAsBoolean()) {
                  this.WL.removeFirst();
                  this.u.removeFirst().run();
                  var7++;
               }

               if (!this.VAsBoolean()) {
                  this.eAsVoid("Session changed during release");
               }
            } catch (Error | RuntimeException var12) {
               this.u.clear();
               this.WL.clear();
               throw var12;
            } finally {
               this.S = false;
            }

            if (var7 > 0 && var2 != null) {
               ClientLog.XAsVoid("Backtrack", "Released {} held updates: {}", var7, var2);
            }
         } else {
            this.eAsVoid(var2 == null ? "Session changed" : var2 + "; session changed");
         }
      }
   }

   private void eAsVoid(String var1) {
      int var2 = this.u.size();
      this.u.clear();
      this.WL.clear();
      if (var2 > 0 && var1 != null) {
         ClientLog.XAsVoid("Backtrack", "Discarded {} held updates: {}", var2, var1);
      }
   }

   static long dAsLong(NumberSetting var0, NumberSetting var1) {
      int var2 = Math.round((float)Math.min(var0.getValue(), var1.getValue()));
      int var3 = Math.round((float)Math.max(var0.getValue(), var1.getValue()));
      return var3 > var2 ? (long)ThreadLocalRandom.current().nextInt(var2, var3 + 1) : (long)Math.max(0, var2);
   }

   static double cAsDouble(Box var0, Box var1) {
      double var2 = Math.max(0.0, Math.max(var0.minX - var1.maxX, var1.minX - var0.maxX));
      double var4 = Math.max(0.0, Math.max(var0.minY - var1.maxY, var1.minY - var0.maxY));
      double var6 = Math.max(0.0, Math.max(var0.minZ - var1.maxZ, var1.minZ - var0.maxZ));
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   static double rAsDouble(Vec3d var0, Box var1) {
      return var0.squaredDistanceTo(
         new Vec3d(
            MathHelper.clamp(var0.x, var1.minX, var1.maxX), MathHelper.clamp(var0.y, var1.minY, var1.maxY), MathHelper.clamp(var0.z, var1.minZ, var1.maxZ)
         )
      );
   }

   private boolean QAsBoolean(Box var1) {
      if (var1 != null && mc.player != null && this.releaseOutOfReach.getValue()) {
         double var2 = this.releaseDistance.getValue();
         return rAsDouble(mc.player.getEyePos(), var1) > var2 * var2;
      } else {
         return false;
      }
   }

   private boolean OAsBoolean(Box var1) {
      if (!this.QAsBoolean(var1)) {
         return false;
      } else {
         String var2 = String.format(Locale.ROOT, "Target out of reach (%.1f blocks)", Math.sqrt(rAsDouble(mc.player.getEyePos(), var1)));
         if (this.R.NAsInt() + this.u.size() > 0) {
            this.JAsVoid(var1);
         }

         if (this.version.pAsBoolean("New")) {
            this.GAsVoid(true, false, var2);
         } else {
            this.iAsVoid(var2);
            this.IAsVoid(false, false, true, null);
         }

         return true;
      }
   }

   private Box TAsBox() {
      if (this.C == null) {
         return null;
      } else if (this.version.pAsBoolean("New")) {
         return this.U == null ? null : this.C.getBoundingBox().offset(this.U.getPos().subtract(this.C.getEntityPos()));
      } else {
         return this.C.isAlive() && !this.x.getPos().equals(Vec3d.ZERO) ? this.C.getType().getDimensions().getBoxAt(this.x.getPos()) : null;
      }
   }

   private void JAsVoid(Box var1) {
      this.W6 = new Vec3d((var1.minX + var1.maxX) / 2.0, var1.minY, (var1.minZ + var1.maxZ) / 2.0);
      this.W9 = Math.max(var1.getLengthX(), var1.getLengthZ()) / 2.0;
      this.W_ = var1.getLengthY();
      this.Wv = MediaSession.RAsDouble();
   }

   @Override
   public String effectName() {
      return "Backtrack";
   }

   @Override
   public void tick(MinecraftClient var1) {
      if (this.isEnabled()) {
      }
   }

   @Override
   public boolean glowing() {
      return this.isEnabled() ? this.WC && this.r : false;
   }

   @Override
   public void clear() {
      this.WC = false;
      this.c = this.W6 = null;
      this.WO = null;
      this.n = null;
      this.F = -1;
      this.k = this.Ww = 0.0;
      this.t = -1.0;
      this.Wv = -10.0;
      this.j.rAsVoid();
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.WC = false;
         this.WO = null;
         this.j.rAsVoid();
         if (this.isInGame() && !this.esp.pAsBoolean("None")) {
            double var3 = MediaSession.RAsDouble();
            double var5 = MediaSession.YAsDouble();
            this.r = this.bloom.getValue();
            this.m = this.color.getValue();
            this.i = this.gradient.getValue();
            this.t = this.W6 != null && var3 - this.Wv < 0.55 ? Math.max(0.0, var3 - this.Wv) / 0.55 : -1.0;
            Box var7 = this.TAsBox();
            if (var7 == null) {
               this.c = null;
               this.F = -1;
               this.k = 0.0;
               this.WC = this.t >= 0.0;
            } else {
               Vec3d var8 = new Vec3d((var7.minX + var7.maxX) / 2.0, var7.minY, (var7.minZ + var7.maxZ) / 2.0);
               int var9 = this.c != null && this.F == this.C.getId() && this.n == mc.world && this.c.squaredDistanceTo(var8) < 16.0 ? 1 : 0;
               this.c = var9 != 0 ? this.c.lerp(var8, 1.0 - Math.exp(-var5 * 24.0)) : var8;
               this.F = this.C.getId();
               this.n = mc.world;
               this.T = var7.getLengthX() / 2.0;
               this.I = var7.getLengthZ() / 2.0;
               this.f = var7.getLengthY();
               this.k = this.k + ((double)(this.R.NAsInt() + this.u.size() > 0 ? 1 : 0) - this.k) * (1.0 - Math.exp(-var5 * 10.0));
               Vec3d var10 = this.C.getLerpedPos(var1);
               this.Ww = FadeCurve.RAsDouble((var10.distanceTo(this.c) - 0.05) / 0.45);
               if (this.ghostModel.getValue() && this.Ww > 0.01) {
                  EntityRenderState var11 = CosmeticSubmissionHandler.oAsEntityRenderState(this.C, var1);
                  var11.x = this.c.x;
                  var11.y = this.c.y;
                  var11.z = this.c.z;
                  var11.displayName = null;
                  if (var11 instanceof PlayerEntityRenderState var12) {
                     var12.playerName = null;
                  }

                  var11.leashDatas = null;
                  var11.onFire = false;
                  var11.shadowPieces.clear();
                  var11.outlineColor = 0;
                  this.WO = var11;
               }

               if (this.tether.getValue() && this.Ww > 0.01) {
                  Vec3d var22 = var10.add(0.0, this.f * 0.55, 0.0);
                  Vec3d var23 = this.c.add(0.0, this.f * 0.55, 0.0);
                  double var13 = Math.min(0.3, var22.distanceTo(var23) * 0.12);

                  for (int var15 = 0; var15 <= 24; var15++) {
                     double var16 = (double)var15 / 24.0;
                     double var18 = Math.pow(0.5 + 0.5 * Math.cos((var16 * 2.5 - var3 * 1.6) * Math.PI * 2.0), 6.0);
                     double var20 = FadeCurve.RAsDouble(var16 / 0.12) * FadeCurve.RAsDouble((1.0 - var16) / 0.12);
                     this.j
                        .bAsVoid(
                           var22.lerp(var23, var16).add(0.0, Math.sin(Math.PI * var16) * var13, 0.0),
                           0.03 * (1.0 + 0.7 * var18) * (0.35 + 0.65 * var20),
                           ColorUtil.pAsInt(ColorUtil.xAsInt(this.m, this.i, var16), this.Ww * (0.3 + 0.7 * var18))
                        );
                  }

                  this.j.aAsVoid(var2.pos);
               }

               this.WC = true;
            }
         } else {
            this.clear();
         }
      }
   }

   @Override
   public void render(EffectGeometryRenderer var1, boolean var2) {
      if (this.isEnabled() && this.WC && (!var2 || this.r)) {
         long var3 = OpenScreen.RAsLong();
         double var5 = MediaSession.RAsDouble();
         if (this.c != null) {
            double var7 = 0.65 + 0.35 * this.k;
            Box var9 = new Box(this.c.x - this.T, this.c.y, this.c.z - this.I, this.c.x + this.T, this.c.y + this.f, this.c.z + this.I).expand(0.03);
            if (this.WO != null) {
               var1.SAsVoid(this.WO, CosmeticSubmissionHandler.Type_n.HOLOGRAM, ColorUtil.xAsInt(this.m, this.i, 0.4), 0.55 * this.Ww * (var2 ? 0.5 : 1.0));
            }

            var1.blend(EffectRenderParams.Type_g.ADDITIVE);
            if (!this.esp.pAsBoolean("Fill")) {
               this.yAsVoid(var1, var9, var5, var7);
            }

            if (!this.esp.pAsBoolean("Wireframe")) {
               this.wAsVoid(var1, var9, var5, var7);
            }

            this.zAsVoid(var1, var9, var5, var7);
            this.ZAsVoid(var1, var9, var5, var7, var2);
            if (this.j.PAsInt() > 1) {
               var1.hAsVoid(this.j, true);
            }

            var1.blend(EffectRenderParams.Type_g.ALPHA);
            if (!var2 && !this.esp.pAsBoolean("Wireframe")) {
               this.fAsVoid(var1, var9, var7);
            }
         }

         if (this.t >= 0.0) {
            var1.blend(EffectRenderParams.Type_g.ADDITIVE);
            this.kAsVoid(var1, this.t);
            var1.blend(EffectRenderParams.Type_g.ALPHA);
         }

         OpenScreen.aAsVoid("Backtrack ESP", var3);
      }
   }

   private static Vec3d DAsVec3d(Box var0, int var1) {
      return new Vec3d((var1 & 1) == 0 ? var0.minX : var0.maxX, (var1 & 2) == 0 ? var0.minY : var0.maxY, (var1 & 4) == 0 ? var0.minZ : var0.maxZ);
   }

   private void yAsVoid(EffectGeometryRenderer var1, Box var2, double var3, double var5) {
      Vec3d var7 = var2.getCenter();

      for (int[] var11 : G) {
         Vec3d var12 = DAsVec3d(var2, var11[0]);
         Vec3d var13 = DAsVec3d(var2, var11[1]);

         for (int var14 = 0; var14 < 6; var14++) {
            Vec3d var15 = var12.lerp(var13, (double)var14 / 6.0);
            Vec3d var16 = var12.lerp(var13, (double)(var14 + 1) / 6.0);
            var1.zAsVoid(
               var15.x,
               var15.y,
               var15.z,
               var16.x,
               var16.y,
               var16.z,
               0.045,
               0.045,
               this.zAsInt(var2, var7, var15, var3, var5),
               this.zAsInt(var2, var7, var16, var3, var5)
            );
         }
      }
   }

   private int zAsInt(Box var1, Vec3d var2, Vec3d var3, double var4, double var6) {
      double var8 = Math.clamp((var3.y - var1.minY) / Math.max(1.0E-6, var1.getLengthY()), 0.0, 1.0);
      double var10 = Math.atan2(var3.z - var2.z, var3.x - var2.x);
      double var12 = Math.pow(0.5 + 0.5 * Math.cos(var10 * 2.0 + var8 * 4.0 - var4 * 3.2), 10.0);
      int var14 = ColorUtil.xAsInt(this.m, this.i, var8);
      return ColorUtil.pAsInt(ColorUtil.xAsInt(var14, var14 | 16777215, var12 * 0.6), var6 * (0.6 + 0.4 * var12));
   }

   private void wAsVoid(EffectGeometryRenderer var1, Box var2, double var3, double var5) {
      double var7 = var3 * 1.6;
      double var9 = 0.5 - 0.5 * Math.cos(var7);
      double var11 = Math.sin(var7);

      for (int var13 = 0; var13 < 3; var13++) {
         double var14 = Math.clamp(var9 - Math.signum(var11) * (double)var13 * 0.07, 0.0, 1.0);
         double var16 = var13 == 0 ? 0.85 : 0.4 / (double)var13 * Math.abs(var11);
         int var18 = ColorUtil.pAsInt(ColorUtil.xAsInt(this.m, this.i, var14), var16 * var5);
         double var19 = var2.minY + var2.getLengthY() * var14;
         double var21 = var13 == 0 ? 0.04 : 0.025;
         var1.zAsVoid(var2.minX, var19, var2.minZ, var2.maxX, var19, var2.minZ, var21, var21, var18, var18);
         var1.zAsVoid(var2.maxX, var19, var2.minZ, var2.maxX, var19, var2.maxZ, var21, var21, var18, var18);
         var1.zAsVoid(var2.maxX, var19, var2.maxZ, var2.minX, var19, var2.maxZ, var21, var21, var18, var18);
         var1.zAsVoid(var2.minX, var19, var2.maxZ, var2.minX, var19, var2.minZ, var21, var21, var18, var18);
      }
   }

   private void zAsVoid(EffectGeometryRenderer var1, Box var2, double var3, double var5) {
      Vec3d var7 = var2.getCenter();
      double var8 = Math.max(var2.getLengthX(), var2.getLengthZ()) * 0.75;
      double var10 = var2.minY + 0.02;
      double var12 = var3 / 1.3 - Math.floor(var3 / 1.3);
      var1.bAsVoid(var7.x, var10, var7.z, var8 * (1.0 + 0.04 * Math.sin(var3 * 3.0)), 0.14, ColorUtil.pAsInt(this.i, 0.7 * var5));
      var1.bAsVoid(var7.x, var10, var7.z, var8 * (1.0 + var12 * 1.1), 0.1, ColorUtil.pAsInt(this.m, 0.5 * var5 * (1.0 - var12) * (1.0 - var12)));
   }

   private void ZAsVoid(EffectGeometryRenderer var1, Box var2, double var3, double var5, boolean var7) {
      for (int var8 = 0; var8 < 8; var8++) {
         Vec3d var9 = DAsVec3d(var2, var8);
         double var10 = 0.5 + 0.5 * Math.sin(var3 * 4.5 + (double)var8 * 1.9);
         int var12 = ColorUtil.xAsInt((var8 & 2) == 0 ? this.m : this.i, -1, 0.45);
         var1.cAsVoid(
            var9.x, var9.y, var9.z, var7 ? 0.22 : 0.16, 0.0, ColorUtil.pAsInt(var12, (var7 ? 0.35 : 0.2) * var5), ParticleEmitterEngine.Type_a.Glow, 0.0, 0.0, 0.0
         );
         if (!var7) {
            var1.BAsVoid(var9.x, var9.y, var9.z, 0.05 + 0.035 * var10, var3 * 0.8 + (double)var8, ColorUtil.pAsInt(var12, var5 * (0.55 + 0.45 * var10)));
         }
      }
   }

   private void fAsVoid(EffectGeometryRenderer var1, Box var2, double var3) {
      double var5 = this.esp.pAsBoolean("Fill") ? 1.8 : 1.0;
      int var7 = ColorUtil.pAsInt(this.m, 0.16 * var5 * var3);
      int var8 = ColorUtil.pAsInt(this.i, 0.035 * var5 * var3);
      double[][] var9 = new double[][]{
         {var2.minX, var2.minZ, var2.maxX, var2.minZ},
         {var2.maxX, var2.minZ, var2.maxX, var2.maxZ},
         {var2.maxX, var2.maxZ, var2.minX, var2.maxZ},
         {var2.minX, var2.maxZ, var2.minX, var2.minZ}
      };

      for (double[] var13 : var9) {
         var1.vertex(var13[0], var2.minY, var13[1], var7);
         var1.vertex(var13[2], var2.minY, var13[3], var7);
         var1.vertex(var13[2], var2.maxY, var13[3], var8);
         var1.vertex(var13[0], var2.maxY, var13[1], var8);
      }

      var1.vertex(var2.minX, var2.maxY, var2.minZ, var8);
      var1.vertex(var2.maxX, var2.maxY, var2.minZ, var8);
      var1.vertex(var2.maxX, var2.maxY, var2.maxZ, var8);
      var1.vertex(var2.minX, var2.maxY, var2.maxZ, var8);
   }

   private void kAsVoid(EffectGeometryRenderer var1, double var2) {
      double var4 = 1.0 - Math.pow(1.0 - var2, 3.0);
      double var6 = Math.pow(1.0 - var2, 1.5);
      Vec3d var8 = this.W6;
      double var9 = this.W9 * 1.5 * (1.0 + var4 * 2.2);
      double var11 = var8.y + 0.03;
      var1.bAsVoid(var8.x, var11, var8.z, var9, 0.05 + 0.22 * (1.0 - var2), ColorUtil.pAsInt(this.m, 0.9 * var6));
      var1.bAsVoid(var8.x, var11, var8.z, var9 * 0.7, 0.12, ColorUtil.pAsInt(this.i, 0.6 * var6));
      Box var13 = new Box(var8.x - this.W9, var8.y, var8.z - this.W9, var8.x + this.W9, var8.y + this.W_, var8.z + this.W9).expand(0.35 * var4);
      int var14 = ColorUtil.pAsInt(ColorUtil.xAsInt(this.m, this.i, 0.5), var6 * var6);

      for (int[] var18 : G) {
         Vec3d var19 = DAsVec3d(var13, var18[0]);
         Vec3d var20 = DAsVec3d(var13, var18[1]);
         var1.zAsVoid(var19.x, var19.y, var19.z, var20.x, var20.y, var20.z, 0.05, 0.05, var14, var14);
      }

      int var23 = ColorUtil.pAsInt(ColorUtil.xAsInt(this.i, -1, 0.5), var6);

      for (int var24 = 0; var24 < 12; var24++) {
         double var25 = (double)var24 * Math.PI / 6.0 + var2;
         double var26 = Math.cos(var25);
         double var21 = Math.sin(var25);
         var1.cAsVoid(
            var8.x + var26 * var9,
            var11 + 0.1 + (double)(var24 % 2) * var4 * 0.4,
            var8.z + var21 * var9,
            0.02 + 0.06 * (1.0 - var2),
            var25,
            var23,
            ParticleEmitterEngine.Type_a.Spark,
            var26,
            0.0,
            var21
         );
      }
   }

   private void xAsVoid() {
      if (this.isRuntimeActive() && this.isInGame() && this.C != null && !this.esp.pAsBoolean("None")) {
         int var1 = SharedRenderEffects.RAsBoolean() && SharedRenderEffects.ZAsBoolean(this.effectName()) ? 1 : 0;
         Box var2 = var1 != 0 && this.c != null && this.F == this.C.getId()
            ? new Box(this.c.x - this.T, this.c.y, this.c.z - this.I, this.c.x + this.T, this.c.y + this.f, this.c.z + this.I)
            : this.TAsBox();
         if (var2 != null) {
            int var3 = this.color.getValue() & 16777215;
            if (var1 == 0) {
               int var4 = this.esp.pAsBoolean("Fill") ? 0 : -520093696 | var3;
               int var5 = this.esp.pAsBoolean("Wireframe") ? 0 : 754974720 | var3;
               GizmoDrawing.box(var2, DrawStyle.filledAndStroked(var4, 1.0F, var5)).ignoreOcclusion();
            } else if (this.throughWalls.getValue()) {
               int var6 = this.esp.pAsBoolean("Fill") ? 0 : 1174405120 | var3;
               int var7 = this.esp.pAsBoolean("Fill") ? 503316480 | var3 : 0;
               GizmoDrawing.box(var2, DrawStyle.filledAndStroked(var6, 1.0F, var7)).ignoreOcclusion();
            }
         }
      }
   }
}
