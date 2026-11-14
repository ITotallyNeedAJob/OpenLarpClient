package com.larpclient.module;

import com.larpclient.AutoTunnel;
import com.larpclient.ClientLog;
import com.larpclient.ParticleEmitter;
import com.larpclient.ModuleKeybindChecker;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public final class TargetStrafe extends Module {
   private static TargetStrafe f;
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Horizontal orbit speed in blocks per tick", 1.0, 0.1, 1.2, 0.1));
   private final NumberSetting radius = this.addSetting(
      new NumberSetting("Radius", "Distance from the target center, capped inside actual attack reach", 1.0, 1.0, 3.0, 0.1)
   );
   private final ModeSetting direction = this.addSetting(
      new ModeSetting("Direction", "Starting orbit direction; reverse when the path is blocked", "Clockwise", "Clockwise", "Counterclockwise")
   );
   private final BooleanSetting autoJump = this.addSetting(new BooleanSetting("Auto Jump", "Jump while orbiting when grounded with clear headroom", false));
   private final BooleanSetting avoidEdges = this.addSetting(
      new BooleanSetting("Avoid Edges", "Require ground below the orbit path; sneak pauses automatic movement", true)
   );
   private ClientPlayerEntity n;
   private ClientPlayerEntity Q;
   private ClientWorld O;
   private LivingEntity N;
   private long i;
   private boolean x;
   private int V = 1;
   private int I;
   private long j;
   private String W = "";
   private String m = "Waiting for Kill Aura";
   private PlayerInput w;
   private PlayerInput u;
   private Vec2f D;
   private Vec2f z;

   public TargetStrafe() {
      super("Target Strafe", "Automatically circle Kill Aura's target at an adjustable speed", Category.MOVEMENT, -1);
      f = this;
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
      return this.isRuntimeActive() ? this.m : "Inactive";
   }

   @Override
   public void onEnable() {
      this.KAsVoid();
      this.I = 0;
      this.FAsVoid("Waiting for Kill Aura");
      ClientLog.XAsVoid(
         "TargetStrafe",
         "Enabled: speed={} blocks/tick, radius={}, auto jump={}, edge checks={}",
         this.speed.getValue(),
         this.radius.getValue(),
         this.autoJump.getValue(),
         this.avoidEdges.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.KAsVoid();
      this.I = 0;
      this.FAsVoid("Inactive");
   }

   private void KAsVoid() {
      this.yAsVoid();
      this.n = null;
      this.O = null;
      this.N = null;
      this.x = false;
      this.W = "";
   }

   private void FAsVoid(String var1) {
      if (!this.m.equals(var1)) {
         this.jAsVoid(var1);
      }

      this.m = var1;
   }

   private void jAsVoid(String var1) {
      long var2 = System.nanoTime();
      if (var2 - this.j >= 1000000000L) {
         ClientLog.XAsVoid("TargetStrafe", "{}", var1);
         this.j = var2;
      }
   }

   @Override
   public void onTick() {
      this.yAsVoid();
      this.x = false;
      if (this.I > 0) {
         this.I--;
         this.KAsVoid();
         this.FAsVoid("Recovering after position correction");
      } else {
         LivingEntity var1 = this.ZAsLivingEntity();
         if (var1 == null) {
            this.KAsVoid();
         } else {
            if (var1 != this.N || !this.direction.getValue().equals(this.W)) {
               this.V = this.direction.pAsBoolean("Clockwise") ? 1 : -1;
               this.W = this.direction.getValue();
               this.jAsVoid("Orbit target acquired: entity=" + var1.getId());
            }

            this.n = mc.player;
            this.O = mc.world;
            this.i = mc.world.getTime();
            this.N = var1;
         }
      }
   }

   private LivingEntity ZAsLivingEntity() {
      if (this.isInGame() && ModuleKeybindChecker.VAsBoolean() && mc.isWindowFocused() && mc.currentScreen == null && !mc.isPaused()) {
         ClientPlayerEntity var1 = mc.player;
         Fly var2 = Fly.bAsFly();
         AirStuck var3 = AirStuck.zAsAirStuck();
         ElytraMotion var4 = ElytraMotion.vAsElytraMotion();
         if ((var2 == null || !var2.vAsBoolean())
            && (var3 == null || !var3.wAsBoolean())
            && (var4 == null || !var4.ZAsBoolean())
            && AutoTunnel.SAsVec2f() == null
            && ParticleEmitter.NAsP5_Type_u() != ParticleEmitter.Type_u.AUTOTRAP
            && ParticleEmitter.NAsP5_Type_u() != ParticleEmitter.Type_u.AUTOWEB
            && ParticleEmitter.NAsP5_Type_u() != ParticleEmitter.Type_u.AUTOANCHOR) {
            if (var1.hasVehicle()
               || var1.isSpectator()
               || var1.getAbilities().flying
               || var1.isGliding()
               || var1.isTouchingWater()
               || var1.isInLava()
               || var1.isClimbing()
               || var1.isSleeping()
               || var1.hasBlindnessEffect()
               || var1.noClip
               || var1.isUsingItem()
               || var1.isInSneakingPose()
               || var1.isSneaking()
               || mc.options.sneakKey.isPressed()) {
               this.FAsVoid("Paused for player movement or item use");
               return null;
            } else if (var1.hurtTime > 0) {
               this.FAsVoid("Paused for knockback");
               return null;
            } else {
               Aura var5 = Aura.fAsAura();
               LivingEntity var6 = var5 == null ? null : var5.hAsLivingEntity();
               if (var6 == null) {
                  this.FAsVoid("Waiting for Kill Aura target");
                  return null;
               } else if (ZAsDouble(var1.getEyePos(), var6.getBoundingBox(), this.radius.getValue(), var5.uAsDouble()) <= 0.0) {
                  this.FAsVoid("Target is outside vertical attack reach");
                  return null;
               } else {
                  return var6;
               }
            }
         } else {
            this.FAsVoid("Paused for another movement controller");
            return null;
         }
      } else {
         this.FAsVoid("Waiting for gameplay");
         return null;
      }
   }

   private boolean RAsBoolean(ClientPlayerEntity var1) {
      return this.isRuntimeActive()
         && this.I == 0
         && var1 != null
         && var1 == this.n
         && var1 == getMc().player
         && this.O == mc.world
         && this.i == mc.world.getTime()
         && this.N != null
         && this.ZAsLivingEntity() == this.N;
   }

   public static boolean HAsBoolean() {
      if (f != null && f.isEnabled()) {
         TargetStrafe var0 = f;
         if (var0 == null) {
            return false;
         } else {
            try {
               return var0.RAsBoolean(getMc().player);
            } catch (RuntimeException var2) {
               var0.FAsVoid("movement ownership", var2);
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public static PlayerInput HAsPlayerInput(ClientPlayerEntity var0, PlayerInput var1) {
      if (f != null && f.isEnabled()) {
         TargetStrafe var2 = f;
         if (var2 == null) {
            return var1;
         } else {
            try {
               if (!var2.RAsBoolean(var0)) {
                  return var1;
               } else if (var1.sneak()) {
                  var2.KAsVoid();
                  var2.FAsVoid("Paused for sneak input");
                  return var1;
               } else {
                  Vec3d var3 = var2.EAsVec3d();
                  Vec2f var4 = HAsVec2f(var3, var0.getYaw());
                  int var5 = var3.lengthSquared() > 0.0
                        && var2.autoJump.getValue()
                        && var0.isOnGround()
                        && var0.getVelocity().y <= 0.0
                        && !var0.getAbilities().allowFlying
                        && mc.world.isSpaceEmpty(var0, var0.getBoundingBox().stretch(0.0, 1.3, 0.0))
                     ? 1
                     : 0;
                  var2.Q = var0;
                  var2.w = var1;
                  var2.D = var0.input.movementVector;
                  var2.z = var4;
                  var0.input.movementVector = var4;
                  var2.u = new PlayerInput(var4.y > 0.1F, var4.y < -0.1F, var4.x > 0.1F, var4.x < -0.1F, var1.jump() || var5 != 0, var1.sneak(), var1.sprint());
                  return var2.u;
               }
            } catch (RuntimeException var61) {
               var2.FAsVoid("input", var61);
               return var1;
            }
         }
      } else {
         return var1;
      }
   }

   public static boolean yAsBoolean() {
      if (f != null && f.isEnabled()) {
         TargetStrafe var0 = f;
         return var0 != null
            && var0.Q != null
            && var0.Q == getMc().player
            && var0.O == mc.world
            && var0.i == mc.world.getTime()
            && var0.Q.input.movementVector == var0.z;
      } else {
         return false;
      }
   }

   private void yAsVoid() {
      if (this.Q != null && this.Q == getMc().player && this.O == mc.world && this.i == mc.world.getTime()) {
         if (this.Q.input.playerInput == this.u) {
            this.Q.input.playerInput = this.w;
         }

         if (this.Q.input.movementVector == this.z) {
            this.Q.input.movementVector = this.D;
         }
      }

      this.Q = null;
      this.w = this.u = null;
      this.D = this.z = null;
   }

   public static Vec3d qAsVec3d(ClientPlayerEntity var0, Vec3d var1) {
      if (f != null && f.isEnabled()) {
         TargetStrafe var2 = f;
         if (var2 != null && !var2.x) {
            try {
               if (!var2.RAsBoolean(var0)) {
                  var2.yAsVoid();
                  return var1;
               } else {
                  var2.x = true;
                  Vec3d var3 = var2.EAsVec3d();
                  Vec3d var4 = new Vec3d(var3.x, var1.y, var3.z);
                  var0.setVelocity(var4);
                  return var4;
               }
            } catch (RuntimeException var51) {
               var2.FAsVoid("movement", var51);
               return var1;
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void FAsVoid(String var1, RuntimeException var2) {
      ClientLog.lAsVoid("TargetStrafe", "Failed during " + var1 + "; disabling and releasing orbit input", var2);
      this.disable();
   }

   public static void LAsVoid(ClientPlayerEntity var0) {
      TargetStrafe var1 = f;
      if (var1 != null && var1.isRuntimeActive() && var0 == getMc().player) {
         var1.KAsVoid();
         var1.I = 20;
         var1.FAsVoid("Recovering after position correction");
      }
   }

   private Vec3d EAsVec3d() {
      ClientPlayerEntity var1 = mc.player;
      double var2 = ZAsDouble(var1.getEyePos(), this.N.getBoundingBox(), this.radius.getValue(), Aura.fAsAura().uAsDouble());
      if (var2 <= 0.0) {
         return Vec3d.ZERO;
      } else {
         Vec3d var4 = this.N.getBoundingBox().getCenter();
         Vec3d var5 = AAsVec3d(var1.getEntityPos(), var4, var2, this.speed.getValue(), this.V, var1.getYaw());
         if (this.zAsBoolean(var5)) {
            this.FAsVoid("Orbiting");
            return var5;
         } else {
            Vec3d var6 = AAsVec3d(var1.getEntityPos(), var4, var2, this.speed.getValue(), -this.V, var1.getYaw());
            if (this.zAsBoolean(var6)) {
               this.V = -this.V;
               this.jAsVoid("Reversed orbit at blocked path or edge");
               this.FAsVoid("Orbiting");
               return var6;
            } else {
               this.FAsVoid("Orbit blocked by terrain or edge");
               return Vec3d.ZERO;
            }
         }
      }
   }

   private boolean zAsBoolean(Vec3d var1) {
      ClientPlayerEntity var2 = mc.player;
      Box var3 = var2.getBoundingBox().contract(0.001);
      if (!mc.world.isSpaceEmpty(var2, var3.stretch(var1.x, Math.max(0.0, var2.getVelocity().y), var1.z))) {
         return false;
      } else {
         int var4 = Math.max(1, (int)Math.ceil(var1.horizontalLength() / 0.25));
         double var5 = var2.isOnGround() ? 0.6 : 2.0;

         for (int var7 = 1; var7 <= var4; var7++) {
            double var8 = var2.getX() + var1.x * (double)var7 / (double)var4;
            double var10 = var2.getZ() + var1.z * (double)var7 / (double)var4;
            if (!mc.world.isChunkLoaded(BlockPos.ofFloored(var8, var2.getY(), var10))) {
               return false;
            }

            if (this.avoidEdges.getValue()
               && mc.world.isBlockSpaceEmpty(var2, new Box(var8 - 0.1, var3.minY - var5, var10 - 0.1, var8 + 0.1, var3.minY, var10 + 0.1))) {
               return false;
            }
         }

         return true;
      }
   }

   public static Vec3d AAsVec3d(Vec3d var0, Vec3d var1, double var2, double var4, int var6, float var7) {
      if (Double.isFinite(var2)
         && Double.isFinite(var4)
         && !(var2 <= 0.0)
         && !(var4 <= 0.0)
         && Double.isFinite(var0.x)
         && Double.isFinite(var0.z)
         && Double.isFinite(var1.x)
         && Double.isFinite(var1.z)
         && Float.isFinite(var7)) {
         double var8 = var0.x - var1.x;
         double var10 = var0.z - var1.z;
         double var12 = var8 * var8 + var10 * var10 < 1.0E-8 ? Math.toRadians((double)var7) : Math.atan2(var10, var8);
         var12 += (double)((var6 >= 0 ? 1 : -1) * 2) * Math.asin(Math.min(1.0, var4 / (2.0 * var2)));
         Vec3d var14 = new Vec3d(var1.x + Math.cos(var12) * var2 - var0.x, 0.0, var1.z + Math.sin(var12) * var2 - var0.z);
         double var15 = var14.length();
         return var15 > var4 ? var14.multiply(var4 / var15) : var14;
      } else {
         return Vec3d.ZERO;
      }
   }

   public static double ZAsDouble(Vec3d var0, Box var1, double var2, double var4) {
      double var6 = Math.max(var1.minY - var0.y, Math.max(0.0, var0.y - var1.maxY));
      double var8 = Math.max(0.0, var4 - 0.25);
      double var10 = var8 * var8 - var6 * var6;
      return Double.isFinite(var10) && !(var10 <= 0.0)
         ? Math.max(0.0, Math.min(var2, Math.sqrt(var10) + Math.min(var1.getLengthX(), var1.getLengthZ()) / 2.0))
         : 0.0;
   }

   public static Vec2f HAsVec2f(Vec3d var0, float var1) {
      if (var0.horizontalLengthSquared() < 1.0E-8) {
         return Vec2f.ZERO;
      } else {
         Vec3d var2 = var0.normalize();
         double var3 = Math.toRadians((double)var1);
         double var5 = Math.cos(var3);
         double var7 = Math.sin(var3);
         return new Vec2f((float)(var5 * var2.x + var7 * var2.z), (float)(-var7 * var2.x + var5 * var2.z));
      }
   }
}
