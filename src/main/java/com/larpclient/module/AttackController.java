package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ModuleImportBroker;
import com.larpclient.FallDamage;
import com.larpclient.ParticleGeometry;
import com.larpclient.ParticleKind;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

public final class AttackController {
   private final Module W;
   private final ParticleGeometry i;
   private int H;
   private boolean x;
   private long Z;
   private long S;
   private long l;
   private long s;
   private boolean v = true;

   public AttackController(Module var1) {
      this.W = var1;
      this.i = new ParticleGeometry(var1);
   }

   public void wAsVoid(MinecraftClient var1) {
      this.i.IAsVoid(var1, true);
   }

   public void GAsVoid(MinecraftClient var1) {
      if (this.i.FAsBoolean()) {
         this.i.vAsVoid(var1, true);
      }
   }

   public void VAsVoid(ClientPlayerEntity var1) {
      this.S = 0L;
      Aura.Y = false;
   }

   public boolean KAsBoolean(ClientPlayerEntity var1) {
      if (!Aura.Y) {
         return false;
      } else if (var1 != null && !var1.isOnGround()) {
         if (System.currentTimeMillis() <= this.S) {
            return true;
         } else {
            this.VAsVoid(var1);
            return false;
         }
      } else {
         this.VAsVoid(var1);
         return false;
      }
   }

   public boolean sAsBoolean() {
      return this.KAsBoolean(MinecraftClient.getInstance().player);
   }

   public boolean tAsBoolean(ClientPlayerEntity var1, AttackController.Type_b var2) {
      LivingEntity var3 = var2.target();
      if (var1 != null && var3 != null && var3.isAlive()) {
         this.dAsVoid(var1, var2.crit(), var2.sprint());
         if (var2.pauseWhileEating() && y.WAsBoolean()) {
            this.VAsVoid(var1);
            return false;
         } else {
            Box var4 = var2.box() != null
               ? var2.box().expand((double)var3.getTargetingMargin())
               : var3.getBoundingBox().expand((double)var3.getTargetingMargin());
            if (y.sAsDouble(var1, var4) > var2.range()) {
               this.VAsVoid(var1);
               return false;
            } else {
               boolean var5 = this.DAsBoolean(var1, var2.crit(), var2.predictCrits(), var2.blatant());
               if (var2.sprint() != AttackController.Type_h.NONE && var5 && !this.KAsBoolean(var1)) {
                  int var6 = !var1.isSprinting() && !FAsBoolean() ? 0 : 1;
                  this.OAsVoid(var1);
                  if (var6 != 0 && var2.sprint() == AttackController.Type_h.LEGIT) {
                     return false;
                  }
               }

               if (this.PAsBoolean(var1, var2.legacy(), var2.tpsSync(), var2.randomize(), var2.blatant()) && var2.entityHit()) {
                  int var8 = var2.ignoreWalls() && !var2.raycastWalls() ? 1 : 0;
                  if (!y.BAsBoolean(var1, var4, var2.yaw(), var2.pitch(), var2.range(), var8 != 0)) {
                     if (!var5 && !this.KAsBoolean(var1)) {
                        this.VAsVoid(var1);
                     }

                     return false;
                  } else if (var2.autoJump() && var2.crit() != AttackController.Type_s.NONE && OAsBoolean(var1)) {
                     var1.jump();
                     return false;
                  } else if (!SAsBoolean(var1, var2.crit())) {
                     if (!var5 && !this.KAsBoolean(var1)) {
                        this.VAsVoid(var1);
                     }

                     return false;
                  } else if (!FallDamage.mAsBoolean(var1.isSprinting())) {
                     if (!this.x) {
                        ClientLog.XAsVoid("Aura", "Attack deferred: waiting for vanilla sprint report; localSprint=" + var1.isSprinting());
                     }

                     this.x = true;
                     return false;
                  } else {
                     this.x = false;
                     boolean var7 = ModuleImportBroker.WAsBoolean(this.W, var3, () -> {
                        if (var2.beforeAttack() != null) {
                           var2.beforeAttack().run();
                        }

                        this.CAsVoid(var1, var3, var2.shieldBreaker());
                     });
                     if (!var7) {
                        return false;
                     } else {
                        this.l = 0L;
                        if (var2.legacy()) {
                           this.FAsVoid(var2.minCps(), var2.maxCps(), var2.tpsSync(), var2.blatant());
                        }

                        Aura.Y = false;
                        this.S = 0L;
                        return true;
                     }
                  }
               } else {
                  if (!var5 && !this.KAsBoolean(var1)) {
                     this.VAsVoid(var1);
                  }

                  return false;
               }
            }
         }
      } else {
         this.VAsVoid(var1);
         return false;
      }
   }

   private void CAsVoid(ClientPlayerEntity var1, LivingEntity var2, boolean var3) {
      MinecraftClient var4 = MinecraftClient.getInstance();
      if (var3 && !this.i.FAsBoolean() && var2 instanceof PlayerEntity var5 && ParticleKind.PAsBoolean(var5, var1) && !ParticleKind.NAsBoolean(var5)) {
         int var6 = ParticleKind.JAsInt(var1);
         if (var6 >= 0 && this.i.uAsBoolean(var1, var6)) {
            ParticleKind.wAsVoid(var5);
            ClientLog.XAsVoid("Aura", "Shield Breaker: hit " + var5.getId() + " with hotbar slot " + (var6 + 1));
         }
      }

      Aura.bAsVoid(var1, "attack-before");
      var4.interactionManager.attackEntity(var1, var2);
      Aura.bAsVoid(var1, "attack-after");
      var1.swingHand(Hand.MAIN_HAND);
      this.v = false;
      this.H++;
   }

   public void CAsVoid() {
      this.v = true;
      this.l = 0L;
   }

   public boolean PAsBoolean(ClientPlayerEntity var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      if (var1 == null) {
         return false;
      } else if (var2) {
         return System.nanoTime() >= this.Z;
      } else {
         boolean var6 = y.lAsBoolean();
         int var7 = !y.iAsBoolean(var1) && (!var6 || var1.isOnGround()) ? 0 : 1;
         float var8 = !var5 && var7 == 0 ? yAsFloat(var3, var4, var5) : 0.9F;
         if (var1.getAttackCooldownProgress(0.5F) < var8) {
            this.l = 0L;
            return false;
         } else if (!var5 && !this.v && var7 == 0) {
            if (this.l == 0L) {
               this.l = System.nanoTime();
               this.s = (long)((5.0 + Math.random() * 15.0) * 1000000.0);
            }

            return System.nanoTime() - this.l >= this.s;
         } else {
            this.l = 0L;
            return true;
         }
      }
   }

   public float wAsFloat(ClientPlayerEntity var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      if (var2) {
         return Math.max(0.0F, (float)(this.Z - System.nanoTime()) / 5.0E7F);
      } else if (var1 == null) {
         return Float.POSITIVE_INFINITY;
      } else {
         double var6 = var1.getAttributeValue(EntityAttributes.ATTACK_SPEED);
         if (var6 <= 0.0) {
            return Float.POSITIVE_INFINITY;
         } else {
            float var8 = (float)(20.0 / var6);
            return Math.max(0.0F, (yAsFloat(var3, var4, var5) - var1.getAttackCooldownProgress(0.5F)) * var8);
         }
      }
   }

   private void OAsVoid(ClientPlayerEntity var1) {
      this.S = System.currentTimeMillis() + 1200L;
      Aura.Y = true;
      gAsVoid(var1);
   }

   private void dAsVoid(ClientPlayerEntity var1, AttackController.Type_s var2, AttackController.Type_h var3) {
      if (var3 == AttackController.Type_h.NONE || var2 == AttackController.Type_s.NONE || var1.getMainHandStack().isOf(Items.MACE)) {
         this.VAsVoid(var1);
      } else if (this.KAsBoolean(var1)) {
         gAsVoid(var1);
      }
   }

   private static void gAsVoid(ClientPlayerEntity var0) {
      if (var0 != null) {
         var0.setSprinting(false);
      }
   }

   private static boolean FAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      return var0.options != null && var0.options.sprintKey.isPressed();
   }

   private static float AAsFloat() {
      return MathHelper.clamp(20.0F / q.aAsFloat(), 1.0F, 3.0F);
   }

   private static float yAsFloat(boolean var0, boolean var1, boolean var2) {
      if (var2) {
         return 1.0F;
      } else {
         float var3 = 1.0F;
         if (var1) {
            var3 = 0.97F + (float)(Math.random() * 0.03F);
         }

         return Math.min(var3, 1.0F);
      }
   }

   private void FAsVoid(float var1, float var2, boolean var3, boolean var4) {
      float var5 = Math.max(1.0F, Math.min(var1, var2));
      float var6 = Math.max(var5, Math.max(var1, var2));
      float var7 = var4 ? var6 : var5 + (float)(Math.random() * (double)(var6 - var5));
      long var8 = (long)(1.0E9F / var7);
      if (var3) {
         var8 = Math.max(50000000L, (long)((float)var8 * AAsFloat()));
      }

      this.Z = System.nanoTime() + var8;
   }

   private static boolean SAsBoolean(ClientPlayerEntity var0, AttackController.Type_s var1) {
      if (!var0.getMainHandStack().isOf(Items.MACE) && var1 != AttackController.Type_s.NONE && y.vAsBoolean(var0)) {
         boolean var2 = y.iAsBoolean(var0);
         if (var1 == AttackController.Type_s.ALWAYS) {
            return var2;
         } else if (var2) {
            return true;
         } else if (!var0.isOnGround()) {
            return false;
         } else {
            MinecraftClient var3 = MinecraftClient.getInstance();
            return var3.options == null || !var3.options.jumpKey.isPressed();
         }
      } else {
         return true;
      }
   }

   private boolean DAsBoolean(ClientPlayerEntity var1, AttackController.Type_s var2, boolean var3, boolean var4) {
      if (var2 == AttackController.Type_s.NONE || var1.getMainHandStack().isOf(Items.MACE)) {
         return false;
      } else if (mAsBoolean(var1)) {
         return false;
      } else if (Aura.Y && !var1.isOnGround()) {
         return true;
      } else if (!var1.isOnGround() && var1.fallDistance > 0.0) {
         return true;
      } else if (!var3) {
         return !var1.isOnGround() && var1.getVelocity().y < 0.0;
      } else {
         int var5 = HAsInt(var1);
         return var5 >= 0 && var5 <= (var4 ? 1 : 2);
      }
   }

   private static int HAsInt(ClientPlayerEntity var0) {
      if (y.iAsBoolean(var0)) {
         return 0;
      } else {
         double var1 = var0.getVelocity().y;
         if (var0.isOnGround()) {
            MinecraftClient var3 = MinecraftClient.getInstance();
            if (var3.options == null || !var3.options.jumpKey.isPressed()) {
               return -1;
            }

            var1 = 0.42;
         }

         double var6 = var0.fallDistance;

         for (int var5 = 1; var5 <= 8; var5++) {
            var1 = (var1 - 0.08) * 0.98;
            if (var1 < 0.0) {
               var6 += -var1;
               if (var6 > 0.0) {
                  return var5;
               }
            }
         }

         return -1;
      }
   }

   private static boolean mAsBoolean(ClientPlayerEntity var0) {
      return var0.isClimbing()
         || var0.isTouchingWater()
         || var0.isInLava()
         || var0.hasVehicle()
         || var0.getAbilities().flying
         || var0.isGliding()
         || var0.hasStatusEffect(StatusEffects.BLINDNESS)
         || var0.hasStatusEffect(StatusEffects.LEVITATION)
         || var0.hasStatusEffect(StatusEffects.SLOW_FALLING);
   }

   private static boolean OAsBoolean(ClientPlayerEntity var0) {
      return var0.isOnGround() && !var0.isClimbing() && !var0.isTouchingWater() && !var0.isInLava() && !var0.hasVehicle() && !var0.getAbilities().flying;
   }

   public int ZAsInt() {
      return this.H;
   }

   public void LAsVoid() {
      this.H = 0;
      this.v = true;
      this.Z = 0L;
      this.x = false;
      this.S = 0L;
      Aura.Y = false;
   }

   public static record Type_b(
      LivingEntity target,
      Box box,
      float yaw,
      float pitch,
      double range,
      boolean ignoreWalls,
      AttackController.Type_s crit,
      AttackController.Type_h sprint,
      boolean shieldBreaker,
      boolean pauseWhileEating,
      boolean raycastWalls,
      boolean entityHit,
      boolean autoJump,
      boolean predictCrits,
      Runnable beforeAttack,
      boolean legacy,
      boolean tpsSync,
      boolean randomize,
      boolean blatant,
      float minCps,
      float maxCps
   ) {
   }

   public static enum Type_h {
      LEGIT,
      KEEP,
      NONE;

      private Type_h() {
      }
   }

   public static enum Type_s {
      NONE,
      ADAPTIVE,
      ALWAYS;

      private Type_s() {
      }
   }
}
