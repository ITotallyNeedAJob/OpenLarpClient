package com.larpclient.module;

import com.larpclient.r6;
import java.util.Set;
import net.minecraft.util.math.Vec3d;

public class ElytraFly extends Module {
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Target flight speed", 1.5, 0.5, 5.0, 0.1));
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "Bounded elytra assistance", "Boost", "Boost", "Glide", "Control"));

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   public ElytraFly() {
      super("ElytraFly", "Control elytra speed and flight", Category.MOVEMENT, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame() && mc.player.isGliding() && r6.VAsBoolean()) {
         Vec3d var1 = mc.player.getVelocity();
         Vec3d var2 = mc.player.getRotationVector().multiply(this.speed.getValue());
         if (this.mode.pAsBoolean("Glide")) {
            Vec3d var3 = new Vec3d(var1.x, 0.0, var1.z);
            double var4;
            if (((var4 = var3.length() - this.speed.getValue()) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1)) > 0) {
               var3 = var3.normalize().multiply(this.speed.getValue());
            }

            var2 = new Vec3d(var3.x, Math.max(var1.y, -0.02), var3.z);
         }

         mc.player.setVelocity(r6.PAsVec3d(var1, var2, this.mode.pAsBoolean("Boost") ? 0.04 : 0.08));
      }
   }
}
