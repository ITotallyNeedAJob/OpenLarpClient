package com.larpclient.module;

import com.larpclient.ModuleKeybindChecker;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Speed extends Module {
   private static Speed C;
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Speed multiplier", 1.5, 1.0, 10.0, 0.1));
   private final ModeSetting mode = this.addSetting(
      new ModeSetting("Mode", "Speed method", "Vanilla", "Vanilla", "BHop", "Strafe", "LowHop", "Entity Collision")
   );
   private final BooleanSetting autoJump = this.addSetting(new BooleanSetting("Auto Jump", "Jump automatically", true));
   private final BooleanSetting onlyOnGround = this.addSetting(new BooleanSetting("Only On Ground", "Only boost when grounded", false));
   private final BooleanSetting damageBoost = this.addSetting(new BooleanSetting("Damage Boost", "Speed after taking damage", false));
   private final NumberSetting strafe = this.addSetting(new NumberSetting("Strafe", "Air strafe strength", 0.8, 0.1, 2.0, 0.1));
   private final BooleanSetting smoothAccel = this.addSetting(new BooleanSetting("Smooth Accel", "Gradual speed increase", true));
   private final NumberSetting collisionBoost = this.addSetting(
      new NumberSetting("Collision Boost", "Entity Collision only: horizontal boost per nearby entity", 0.02, 0.01, 0.06, 0.01)
   );
   private final NumberSetting collisionLimit = this.addSetting(
      new NumberSetting("Collision Limit", "Entity Collision only: maximum entities contributing to the boost", 1.0, 1.0, 3.0, 1.0)
   );
   private double g;
   private String B = "";
   private boolean J;
   private int h;
   private boolean l;
   private String Y = "";
   private int a;
   private int x;

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   public Speed() {
      super("Speed", "Move faster", Category.MOVEMENT, -1);
      C = this;
      this.collisionBoost.visibleWhen(() -> this.mode.pAsBoolean("Entity Collision"));
      this.collisionLimit.visibleWhen(() -> this.mode.pAsBoolean("Entity Collision"));
      this.speed.visibleWhen(() -> !this.mode.pAsBoolean("Entity Collision"));
      this.autoJump.visibleWhen(() -> this.mode.pAsBoolean("BHop") || this.mode.pAsBoolean("Strafe") || this.mode.pAsBoolean("LowHop"));
      this.onlyOnGround.visibleWhen(() -> !this.mode.pAsBoolean("Entity Collision"));
      this.damageBoost.visibleWhen(() -> !this.mode.pAsBoolean("Entity Collision"));
      this.strafe.visibleWhen(() -> this.mode.pAsBoolean("Strafe"));
      this.smoothAccel.visibleWhen(() -> !this.mode.pAsBoolean("Entity Collision"));
   }

   @Override
   public void onEnable() {
      this.onDisable();
      this.Y = this.mode.getValue();
   }

   @Override
   public void onDisable() {
      this.JAsVoid();
      this.Y = "";
      this.a = 0;
      this.x = 0;
   }

   private void JAsVoid() {
      this.g = 0.0;
      this.B = "";
      this.h = 0;
      this.l = false;
      this.J = false;
   }

   public static void dAsVoid(ClientPlayerEntity var0) {
      if (C != null && C.isRuntimeActive() && var0 == getMc().player) {
         C.a = 20;
         C.x = 0;
      }
   }

   @Override
   public void onTick() {
      this.x = 0;
      int var1 = this.a > 0 ? 1 : 0;
      if (var1 != 0) {
         this.a--;
      }

      if (this.isInGame()) {
         if (!this.Y.equals(this.mode.getValue())) {
            this.JAsVoid();
            this.Y = this.mode.getValue();
         }

         if (r6.VAsBoolean()
            && !TargetStrafe.HAsBoolean()
            && mc.currentScreen == null
            && !mc.player.isTouchingWater()
            && !mc.player.isInLava()
            && !mc.player.isClimbing()) {
            Vec3d var2 = r6.JAsVec3d();
            boolean var3 = mc.player.isOnGround();
            if (this.mode.pAsBoolean("Entity Collision")) {
               if (var1 == 0) {
                  this.bAsVoid(var2);
               }
            } else if (var2.lengthSquared() != 0.0 && (!this.onlyOnGround.getValue() || var3)) {
               if (!this.B.equals(this.mode.getValue())) {
                  this.g = 0.0;
                  this.B = this.mode.getValue();
               }

               int var4 = mc.player.hurtTime > 0 ? 1 : 0;
               if (this.damageBoost.getValue() && var4 != 0 && !this.l) {
                  this.h = 10;
               }

               int var16;
               label133: {
                  this.l = var4 != 0;
                  if (this.damageBoost.getValue()) {
                     var16 = this.h--;
                     if (var16 > 0) {
                        var16 = 1;
                        break label133;
                     }
                  }

                  var16 = 0;
               }

               this.J = var16 != 0;
               if (this.h < 0) {
                  this.h = 0;
               }

               if (!this.mode.pAsBoolean("Vanilla") || var3) {
                  String var14 = this.mode.getValue();

                  double var5 = switch (var14) {
                     case "BHop" -> 0.26;
                     case "Strafe" -> 0.28;
                     case "LowHop" -> 0.22;
                     default -> 0.13;
                  };
                  double var14_r2 = var5 * this.speed.getValue() * (this.J ? 1.15 : 1.0);
                  this.g = this.smoothAccel.getValue() ? this.g + (var14_r2 - this.g) * 0.3 : var14_r2;
                  Vec3d var9 = mc.player.getVelocity();
                  Vec3d var10 = var2.multiply(this.g);
                  Vec3d var11 = new Vec3d(var9.x, 0.0, var9.z);
                  double var12 = (this.mode.pAsBoolean("Strafe") && !var3 ? 0.04 * this.strafe.getValue() : 0.08) * yAsDouble(this.speed.getValue());
                  var11 = r6.PAsVec3d(var11, var10, var12);
                  mc.player.setVelocity(var11.x, var9.y, var11.z);
                  if (var3 && this.autoJump.getValue() && !this.mode.pAsBoolean("Vanilla") && var9.y <= 0.0) {
                     if (this.mode.pAsBoolean("LowHop")) {
                        mc.player.setVelocity(var11.x, 0.32, var11.z);
                     } else {
                        mc.player.jump();
                     }
                  }
               }
            } else {
               this.g = 0.0;
            }
         } else {
            this.JAsVoid();
         }
      }
   }

   public static double yAsDouble(double var0) {
      return Math.max(1.0, var0 - 4.0);
   }

   private void bAsVoid(Vec3d var1) {
      ClientPlayerEntity var2 = mc.player;
      if (var1.lengthSquared() != 0.0
         && !var2.hasVehicle()
         && !var2.isSpectator()
         && !var2.getAbilities().flying
         && !var2.isGliding()
         && !var2.isInSneakingPose()
         && var2.hurtTime <= 0) {
         Box var3 = var2.getBoundingBox().expand(0.19);

         for (Entity var5 : mc.world
            .getOtherEntities(
               var2,
               var3,
               var0 -> var0 instanceof LivingEntity
                     && !(var0 instanceof ArmorStandEntity)
                     && var0.isAlive()
                     && !var0.isSpectator()
                     && !var0.hasVehicle()
                     && var0.isPushable()
            )) {
            if (EntityPredicates.canBePushedBy(var5).test(var2) && ++this.x >= this.collisionLimit.getValue().intValue()) {
               break;
            }
         }

         if (this.x != 0) {
            double var7 = Math.min(0.08, this.collisionBoost.getValue() * (double)this.x);
            Vec3d var6 = var2.getVelocity();
            var2.setVelocity(var6.x + var1.x * var7, var6.y, var6.z + var1.z * var7);
         }
      }
   }

   @Override
   public String getStatus() {
      if (!this.mode.pAsBoolean("Entity Collision")) {
         return "";
      } else if (!this.isRuntimeActive()) {
         return "Inactive";
      } else if (this.a > 0) {
         return "Collision boost recovering";
      } else {
         return this.x == 0 ? "Waiting for collision" : "Collision boost active";
      }
   }
}
