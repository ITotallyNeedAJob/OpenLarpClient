package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.mixin.ClientPlayerInteractionManagerAccessor;
import java.util.Set;

public final class NoBreakDelay extends Module {
   public NoBreakDelay() {
      super("No Break Delay", "Remove the delay between breaking blocks", Category.PLAYER, -1);
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? "Ready immediately" : "";
   }

   @Override
   public void onEnable() {
      ClientLog.XAsVoid("NoBreakDelay", "Enabled; the native post-break delay is cleared each tick");
   }

   @Override
   public void onTick() {
      if (mc.interactionManager instanceof ClientPlayerInteractionManagerAccessor var1 && var1.larpDestroyDelay() > 0) {
         var1.larpDestroyDelay(0);
      }
   }
}
