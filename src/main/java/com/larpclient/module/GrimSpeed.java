package com.larpclient.module;

import com.larpclient.AutoTunnel;
import com.larpclient.ClientLog;
import com.larpclient.ModuleKeybindChecker;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class GrimSpeed extends Module {
   private static GrimSpeed f;
   private final NumberSetting collisionBoost = this.addSetting(
      new NumberSetting("Collision Boost", "Horizontal impulse per colliding entity, up to 0.08 total", 0.02, 0.01, 0.06, 0.01) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      }
   );
   private final NumberSetting collisionLimit = this.addSetting(new NumberSetting("Collision Limit", "Maximum contributing entities", 1.0, 1.0, 3.0, 1.0));
   private final BooleanSetting sprintJump = this.addSetting(
      new BooleanSetting("Sprint Jump", "Sprint and jump normally while moving forward, including in open space", true)
   );
   private int k;
   private String B = "Waiting for collision";
   private ClientPlayerEntity h;
   private ClientWorld w;
   private int z = -1;
   private Box M;
   private boolean x;

   public GrimSpeed() {
      super("Grim Speed", "Native sprint jumps with an experimental entity collision boost", Category.MOVEMENT, -1);
      f = this;
   }

   public static double NAsDouble(double var0, int var2) {
      return !Double.isFinite(var0) ? 0.0 : Math.min(0.08, Math.max(0.0, var0) * (double)Math.max(0, var2));
   }

   public static Box yAsBox(Box var0, Box var1) {
      return var0.union(var1).expand(0.19);
   }

   public static Vec3d sAsVec3d(float var0, float var1, float var2) {
      if (Float.isFinite(var0) && Float.isFinite(var1) && Float.isFinite(var2)) {
         double var3 = Math.toRadians((double)var2);
         return new Vec3d(Math.cos(var3) * (double)var0 - Math.sin(var3) * (double)var1, 0.0, Math.cos(var3) * (double)var1 + Math.sin(var3) * (double)var0)
            .normalize();
      } else {
         return Vec3d.ZERO;
      }
   }

   public static boolean zAsBoolean(Entity var0) {
      return (var0 instanceof LivingEntity || var0 instanceof AbstractBoatEntity || var0 instanceof AbstractMinecartEntity)
         && !(var0 instanceof ArmorStandEntity)
         && !(var0 instanceof BatEntity)
         && !(var0 instanceof ParrotEntity)
         && !var0.noClip
         && var0.isAlive()
         && !var0.isSpectator()
         && !var0.hasVehicle()
         && var0.isPushable();
   }

   public static void PAsVoid(ClientPlayerEntity var0) {
      if (f != null && f.isRuntimeActive() && var0 == getMc().player) {
         if (f.k == 0) {
            ClientLog.XAsVoid("GrimSpeed", "Position correction: suspending sprint assistance and collision boost for 20 ticks");
         }

         f.k = 20;
         f.AAsVoid();
         f.B = "Recovering after correction";
      }
   }

   @Override
   public void onEnable() {
      this.onDisable();
      ClientLog.XAsVoid(
         "GrimSpeed",
         "Enabled: sprint jump={}, collision boost={}, limit={}, total collision cap=0.08",
         this.sprintJump.getValue(),
         this.collisionBoost.getValue(),
         this.collisionLimit.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.k = 0;
      this.x = false;
      this.AAsVoid();
      this.B = "Waiting for collision";
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(com.larpclient.module.k.SERVER_STATE_MODIFYING);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.B : "Inactive";
   }

   @Override
   public void onTick() {
      this.x = false;
      this.AAsVoid();
      if (this.k > 0) {
         if (--this.k == 0) {
            ClientLog.XAsVoid("GrimSpeed", "Correction recovery complete; checking contact next tick");
         }

         this.B = "Recovering after correction";
      } else if (this.yAsBoolean()) {
         this.B = "Waiting for collision";
         this.h = mc.player;
         this.w = mc.world;
         this.z = this.h.age;
         this.M = this.h.getBoundingBox();
      }
   }

   private void AAsVoid() {
      this.h = null;
      this.w = null;
      this.z = -1;
      this.M = null;
   }

   private boolean yAsBoolean() {
      if (!this.isInGame() || !r6.VAsBoolean()) {
         this.B = "Movement unavailable";
         return false;
      } else if (TargetStrafe.HAsBoolean()) {
         this.B = "Pause: Target Strafe is orbiting";
         return false;
      } else if (r6.NAsBoolean("Speed")) {
         this.B = "Pause: Speed is enabled";
         return false;
      } else {
         Fly var1 = Fly.bAsFly();
         if (var1 != null && var1.vAsBoolean()) {
            this.B = "Pause: Fly is enabled";
            return false;
         } else {
            AirStuck var2 = AirStuck.zAsAirStuck();
            if (var2 != null && var2.wAsBoolean()) {
               this.B = "Pause: AirStuck is enabled";
               return false;
            } else {
               ElytraMotion var3 = ElytraMotion.vAsElytraMotion();
               if (var3 != null && var3.ZAsBoolean()) {
                  this.B = "Pause: ElytraMotion is holding";
                  return false;
               } else {
                  ClientPlayerEntity var4 = mc.player;
                  if (mc.currentScreen != null
                     || var4.isTouchingWater()
                     || var4.isInLava()
                     || var4.isClimbing()
                     || var4.hasVehicle()
                     || var4.isSpectator()
                     || var4.getAbilities().flying
                     || var4.isGliding()
                     || var4.isInSneakingPose()
                     || var4.isSneaking()
                     || var4.isSleeping()
                     || var4.hasBlindnessEffect()
                     || var4.noClip
                     || var4.hurtTime > 0) {
                     this.B = "Waiting for normal movement";
                     return false;
                  } else if (var4.isUsingItem()) {
                     this.B = "Pause: using item";
                     return false;
                  } else {
                     return true;
                  }
               }
            }
         }
      }
   }

   public static PlayerInput xAsPlayerInput(ClientPlayerEntity var0, PlayerInput var1) {
      if (f != null && f.isEnabled()) {
         GrimSpeed var2 = f;
         if (var0 != null
            && var2 != null
            && var2.isRuntimeActive()
            && var2.sprintJump.getValue()
            && var2.h == var0
            && var0 == getMc().player
            && var2.w == mc.world
            && var2.z == var0.age) {
            try {
               if (!var2.yAsBoolean()
                  || !var1.forward()
                  || var1.backward()
                  || var1.sneak()
                  || var0.getHungerManager().getFoodLevel() <= 6
                  || AutoTunnel.SAsVec2f() != null) {
                  return var1;
               } else if (Aura.rAsBoolean()) {
                  return var1;
               } else {
                  int var3 = var0.isSprinting()
                        && var0.isOnGround()
                        && !var0.horizontalCollision
                        && !var0.getAbilities().allowFlying
                        && var0.getVelocity().y <= 0.0
                        && mc.world.isSpaceEmpty(var0, var0.getBoundingBox().stretch(0.0, 1.3, 0.0))
                     ? 1
                     : 0;
                  var2.x = true;
                  return new PlayerInput(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump() || var3 != 0, var1.sneak(), true);
               }
            } catch (RuntimeException var41) {
               ClientLog.lAsVoid("GrimSpeed", "Input assistance failed; disabling and keeping native input", var41);
               var2.disable();
               return var1;
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static void yAsVoid(ClientPlayerEntity var0) {
      if (f != null && f.isEnabled()) {
         GrimSpeed var1 = f;
         if (var0 != null && var1 != null && var1.isRuntimeActive() && var1.h == var0) {
            try {
               var1.mAsVoid(var0);
            } catch (RuntimeException var3) {
               ClientLog.lAsVoid("GrimSpeed", "Collision phase failed; disabling and clearing pending boost", var3);
               var1.disable();
            }
         }
      }
   }

   private void mAsVoid(ClientPlayerEntity var1) {
      Box var2 = this.M;
      int var3 = var1 == mc.player && this.w == mc.world && this.z == var1.age ? 1 : 0;
      this.AAsVoid();
      if (var3 == 0 || var2 == null) {
         this.B = "Movement unavailable";
      } else if (this.yAsBoolean()) {
         Vec3d var4 = sAsVec3d(var1.sidewaysSpeed, var1.forwardSpeed, var1.getYaw());
         if (var4.lengthSquared() == 0.0) {
            this.B = "Waiting for movement input";
         } else {
            int var5 = 0;

            for (Entity var7 : mc.world.getOtherEntities(var1, yAsBox(var2, var1.getBoundingBox()), GrimSpeed::zAsBoolean)) {
               if (EntityPredicates.canBePushedBy(var7).test(var1)) {
                  if (++var5 >= this.collisionLimit.getValue().intValue()) {
                     break;
                  }
               }
            }

            if (var5 == 0) {
               this.B = this.x ? "Sprint assist" : "Waiting for collision";
            } else {
               Vec3d var9 = var1.getVelocity();
               double var10 = NAsDouble(this.collisionBoost.getValue(), var5);
               var1.setVelocity(var9.x + var4.x * var10, var9.y, var9.z + var4.z * var10);
               this.B = String.format(Locale.ROOT, "Boosting: %d %s (+%.2f)", var5, var5 == 1 ? "entity" : "entities", var10);
            }
         }
      }
   }
}
