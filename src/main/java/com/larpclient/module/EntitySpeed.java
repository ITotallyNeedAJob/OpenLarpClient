package com.larpclient.module;

import com.larpclient.ModuleKeybindChecker;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class EntitySpeed extends Module {
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Entity speed multiplier", 2.0, 1.0, 5.0, 0.1));

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   public EntitySpeed() {
      super("EntitySpeed", "Ride entities faster", Category.MOVEMENT, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame() && r6.VAsBoolean() && mc.player.hasVehicle()) {
         Entity var1 = mc.player.getVehicle();
         if (var1 != null) {
            float var2 = mc.player.getYaw();
            double var3 = Math.toRadians((double)var2);
            double var5 = 0.0;
            double var7 = 0.0;
            if (mc.options.forwardKey.isPressed()) {
               var5 = 1.0;
            }

            if (mc.options.backKey.isPressed()) {
               var5--;
            }

            if (mc.options.leftKey.isPressed()) {
               var7 = 1.0;
            }

            if (mc.options.rightKey.isPressed()) {
               var7--;
            }

            double var19;
            if (((var19 = var5 - 0.0) == 0.0 ? 0 : (var19 < 0.0 ? -1 : 1)) != 0 || var7 != 0.0) {
               double var9 = this.speed.getValue() * 0.3;
               double var11 = -Math.sin(var3) * var5 + Math.cos(var3) * var7;
               double var13 = Math.cos(var3) * var5 + Math.sin(var3) * var7;
               double var15 = Math.sqrt(var11 * var11 + var13 * var13);
               double var18;
               if (((var18 = var15 - 0.0) == 0.0 ? 0 : (var18 < 0.0 ? -1 : 1)) > 0) {
                  var11 = var11 / var15 * var9;
                  var13 = var13 / var15 * var9;
               }

               Vec3d var17 = var1.getVelocity();
               var1.setVelocity(var11, var17.y, var13);
            }
         }
      }
   }
}
