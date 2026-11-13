package com.larpclient.module;

import com.larpclient.ClientLog;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

public class AutoRespawn extends Module {
   private ClientPlayerEntity S;
   private ClientWorld c;

   public AutoRespawn() {
      super("AutoRespawn", "Respawn automatically", Category.PLAYER, -1);
   }

   @Override
   protected boolean canActivateWhileDead() {
      return true;
   }

   @Override
   public void onEnable() {
      this.EAsVoid();
      ClientLog.GAsVoid("AutoRespawn", "Active; waiting for death or respawn confirmation");
   }

   @Override
   public void onDisable() {
      this.EAsVoid();
   }

   @Override
   public void suspendForDisconnect() {
      this.EAsVoid();
      super.suspendForDisconnect();
   }

   private void EAsVoid() {
      if (!this.isInGame() || mc.player != this.S || mc.world != this.c || mc.player.isAlive()) {
         this.S = null;
         this.c = null;
      }
   }

   @Override
   public void onTick() {
      this.EAsVoid();
      if (this.isInGame() && mc.player.isDead() && this.S == null) {
         this.S = mc.player;
         this.c = mc.world;
         mc.player.requestRespawn();
         mc.setScreen(null);
         ClientLog.GAsVoid("AutoRespawn", "Requested respawn; waiting for server confirmation");
      }
   }
}
