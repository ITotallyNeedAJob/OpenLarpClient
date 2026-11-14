package com.larpclient.module;

import com.larpclient.AutoTunnel;
import com.larpclient.ClientLog;
import com.larpclient.ModuleKeybindChecker;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.PlayerInput;

public final class AutoSprint extends Module {
   private static AutoSprint Y;
   private String s = "Waiting for movement";

   public AutoSprint() {
      super("Auto Sprint", "Sprint automatically without holding the sprint key", Category.MOVEMENT, -1);
      Y = this;
   }

   public static AutoSprint OAsAutoSprint() {
      return Y;
   }

   public static PlayerInput gAsPlayerInput(PlayerInput var0) {
      return var0.sprint() ? var0 : new PlayerInput(var0.forward(), var0.backward(), var0.left(), var0.right(), var0.jump(), var0.sneak(), true);
   }

   public static PlayerInput qAsPlayerInput(ClientPlayerEntity var0, PlayerInput var1) {
      if (Y != null && Y.isEnabled()) {
         AutoSprint var2 = Y;
         if (var2 != null && var2.isRuntimeActive() && var0 != null) {
            try {
               if (var0 != getMc().player || !ModuleKeybindChecker.VAsBoolean()) {
                  var2.s = "Movement unavailable";
                  return var1;
               } else if (AutoTunnel.SAsVec2f() != null) {
                  var2.s = "Pause: AutoTunnel is steering";
                  return var1;
               } else if (Aura.rAsBoolean()) {
                  var2.s = "Pause: critical sprint release";
                  return var1;
               } else {
                  var2.s = var0.isSprinting() ? "Sprinting" : "Waiting for movement";
                  return gAsPlayerInput(var1);
               }
            } catch (RuntimeException var4) {
               ClientLog.lAsVoid("AutoSprint", "Input assistance failed; disabling and keeping native input", var4);
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

   @Override
   public void onEnable() {
      this.s = "Waiting for movement";
      ClientLog.XAsVoid("AutoSprint", "Enabled: holding sprint input, native sprint eligibility");
   }

   @Override
   public void onDisable() {
      this.s = "Waiting for movement";
      ClientLog.XAsVoid("AutoSprint", "Disabled: sprint follows the physical key again");
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.VANILLA_INPUT);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.s : "Inactive";
   }
}
