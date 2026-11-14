package com.larpclient.module;

import com.larpclient.ModuleKeybindChecker;
import java.util.Set;
import net.minecraft.util.math.Vec3d;

public final class AirStuck extends Module {
   private static AirStuck a;

   public AirStuck() {
      super("AirStuck", "Hold your position", Category.MOVEMENT, -1);
      a = this;
   }

   public static AirStuck zAsAirStuck() {
      return a;
   }

   public boolean wAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.isInGame() && ModuleKeybindChecker.VAsBoolean() && !mc.player.hasVehicle() : false;
   }

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.CONSTRAINT;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   @Override
   public void onTick() {
      if (this.wAsBoolean()) {
         mc.player.setVelocity(Vec3d.ZERO);
      }
   }

   @Override
   public String getStatus() {
      return this.wAsBoolean() ? "Holding" : "";
   }
}
