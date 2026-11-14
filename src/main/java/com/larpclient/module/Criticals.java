package com.larpclient.module;

import com.larpclient.ModuleImportBroker;
import com.larpclient.ModuleKeybindChecker;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;

public class Criticals extends Module {
   private final ModeSetting mode = this.addSetting(
      new ModeSetting("Mode", "Actual jump assistance; waits for falling eligibility", "Jump", "Jump", "MiniJump")
   );
   private static Criticals R;

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.VANILLA_INPUT, k.SERVER_STATE_MODIFYING);
   }

   public Criticals() {
      super("Criticals", "Help time jumps for critical hits", Category.COMBAT, -1);
      R = this;
   }

   @Override
   public Set<k> getCurrentNetworkImpacts() {
      return Set.of(this.mode.pAsBoolean("Jump") ? k.VANILLA_INPUT : k.SERVER_STATE_MODIFYING);
   }

   public static boolean dAsBoolean() {
      if (R == null || !R.isEnabled()) {
         return false;
      } else if (R != null && R.isRuntimeActive() && b_.jAsBoolean() && r6.hAsBoolean()) {
         ClientPlayerEntity var0 = getMc().player;
         return !var0.isTouchingWater()
            && !var0.isInLava()
            && !var0.isClimbing()
            && !var0.hasVehicle()
            && !var0.isSprinting()
            && !var0.getAbilities().flying
            && !var0.isGliding()
            && !var0.hasBlindnessEffect()
            && !var0.isUsingItem();
      } else {
         return false;
      }
   }

   public static boolean qAsBoolean() {
      if (R == null || !R.isEnabled()) {
         return false;
      } else if (dAsBoolean() && !yAsBoolean()) {
         ClientPlayerEntity var0 = getMc().player;
         return !var0.isOnGround() && var0.getVelocity().y > 0.0 && var0.getAttackCooldownProgress(0.0F) >= 1.0F ? true : bAsBoolean();
      } else {
         return false;
      }
   }

   public static boolean bAsBoolean() {
      if (R == null || !R.isEnabled()) {
         return false;
      } else if (!dAsBoolean()) {
         return false;
      } else {
         ClientPlayerEntity var0 = getMc().player;
         if (var0.isOnGround() && !(var0.getVelocity().y > 0.0) && !(var0.getAttackCooldownProgress(0.0F) < 1.0F)) {
            if (R.mode.pAsBoolean("Jump")) {
               var0.jump();
            } else {
               var0.setVelocity(var0.getVelocity().x, 0.1, var0.getVelocity().z);
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public static boolean yAsBoolean() {
      if (R == null || !R.isEnabled()) {
         return false;
      } else if (!dAsBoolean()) {
         return false;
      } else {
         ClientPlayerEntity var0 = getMc().player;
         return !var0.isOnGround() && var0.fallDistance > 0.0 && var0.getVelocity().y < 0.0 && var0.getAttackCooldownProgress(0.0F) >= 1.0F;
      }
   }

   @Override
   public void onDisable() {
      b_.AAsVoid();
   }
}
