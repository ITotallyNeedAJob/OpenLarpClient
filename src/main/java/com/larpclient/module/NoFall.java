package com.larpclient.module;

import java.util.Set;
import java.util.UUID;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class NoFall extends Module {
   private static NoFall e;
   private volatile int A;

   public NoFall() {
      super("NoFall", "Try to reduce fall damage", Category.MOVEMENT, -1);
      e = this;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   public static boolean UAsBoolean(boolean var0) {
      if (e != null && e.isEnabled()) {
         ClientPlayerEntity var1 = getMc().player;
         return var0
            || e != null
               && e.isRuntimeActive()
               && var1 != null
               && var1.isAlive()
               && var1.fallDistance > 2.0
               && var1.getVelocity().y < 0.0
               && !var1.isGliding()
               && !var1.getAbilities().flying
               && !var1.hasVehicle()
               && !var1.isTouchingWater()
               && !var1.isInLava();
      } else {
         return var0;
      }
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? "Fall protection active" : "Inactive";
   }

   @Override
   public void onEnable() {
      this.A++;
   }

   @Override
   public void onDisable() {
      this.A++;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         IntegratedServer var1 = mc.getServer();
         if (var1 != null) {
            UUID var2 = mc.player.getUuid();
            int var3 = this.A;
            var1.execute(() -> {
               ServerPlayerEntity var4 = var1.getPlayerManager().getPlayer(var2);
               if (this.A == var3 && var4 != null && var4.isAlive()) {
                  var4.fallDistance = 0.0;
               }
            });
         }
      }
   }
}
