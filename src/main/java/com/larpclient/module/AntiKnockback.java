package com.larpclient.module;

import java.util.Set;

public class AntiKnockback extends Module {
   private final NumberSetting horizontal = this.addSetting(new NumberSetting("Horizontal", "Horizontal KB multiplier", 0.0, 0.0, 1.0, 0.05));
   private final NumberSetting vertical = this.addSetting(new NumberSetting("Vertical", "Vertical KB multiplier", 0.0, 0.0, 1.0, 0.05));

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   public AntiKnockback() {
      super("AntiKnockback", "Reduce knockback from hits and explosions", Category.COMBAT, -1);
   }

   public double CAsDouble() {
      return this.isEnabled() ? this.horizontal.getValue() : 1.0;
   }

   public double nAsDouble() {
      return this.isEnabled() ? this.vertical.getValue() : 1.0;
   }
}
