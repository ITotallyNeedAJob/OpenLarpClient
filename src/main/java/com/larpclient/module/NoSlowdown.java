package com.larpclient.module;

import java.util.Set;

public class NoSlowdown extends Module {
   private final BooleanSetting items = this.addSetting(new BooleanSetting("Items", "No item use slowdown", true));
   private final BooleanSetting webs = this.addSetting(new BooleanSetting("Webs", "No cobweb slowdown", true));
   private final BooleanSetting soulSand = this.addSetting(new BooleanSetting("Soul Sand", "No soul sand slowdown", true));
   private static NoSlowdown j;

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   public NoSlowdown() {
      super("NoSlowdown", "Reduce item and block slowdown", Category.MOVEMENT, -1);
      j = this;
   }

   public static NoSlowdown yAsNoSlowdown() {
      return j;
   }

   public boolean MAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.items.getValue() : false;
   }

   public boolean YAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.webs.getValue() : false;
   }

   public boolean OAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.soulSand.getValue() : false;
   }
}
