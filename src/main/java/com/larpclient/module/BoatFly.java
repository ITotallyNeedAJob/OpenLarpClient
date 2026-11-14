package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.DropAnimation;
import com.larpclient.ModuleKeybindChecker;
import java.util.Set;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.util.math.Vec3d;

public final class BoatFly extends Module {
   private static BoatFly E;
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Horizontal flight speed", 20.0, 2.0, 80.0, 1.0)).dAsNumberSetting(" b/s");
   private final NumberSetting verticalSpeed = this.addSetting(
         new NumberSetting("Vertical Speed", "Climb speed while holding Jump and descent speed while holding Sprint", 10.0, 1.0, 40.0, 1.0)
      )
      .dAsNumberSetting(" b/s");
   private final NumberSetting fallSpeed = this.addSetting(
         new NumberSetting("Fall Speed", "Slow descent with no vertical input; 0 hovers in place", 0.0, 0.0, 10.0, 0.1)
      )
      .dAsNumberSetting(" b/s");
   private final NumberSetting smoothing = this.addSetting(
      new NumberSetting("Smoothing", "Ticks to reach full speed or come to a stop; 0 is instant", 3.0, 0.0, 20.0, 1.0)
   );
   private final BooleanSetting antiKick = this.addSetting(
      new BooleanSetting("Anti Kick", "Dip slightly every 2 seconds in open air so a vanilla server does not kick you for floating", true)
   );
   private AbstractBoatEntity J;
   private DropAnimation t;
   private double N;
   private long a;
   private int O;
   private int X;
   private String o = "Ride a boat";

   public BoatFly() {
      super("Boat Fly", "Fly the boat you steer where you look; Jump climbs, Sprint descends", Category.MOVEMENT, -1);
      E = this;
   }

   public static BoatFly qAsBoatFly() {
      return E;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.o : "";
   }

   @Override
   public void onEnable() {
      this.LAsVoid(null);
      ClientLog.XAsVoid(
         "BoatFly",
         "Enabled: speed {} b/s, vertical {} b/s, fall {} b/s, smoothing {}, anti kick {}",
         this.speed.getValue(),
         this.verticalSpeed.getValue(),
         this.fallSpeed.getValue(),
         this.smoothing.getValue(),
         this.antiKick.getValue()
      );
   }

   @Override
   public void onTick() {
      if (this.J != null && (!this.isInGame() || mc.player.getVehicle() != this.J || this.J.getControllingPassenger() != mc.player)) {
         this.LAsVoid("Left the boat");
      }
   }

   @Override
   public void onDisable() {
      this.LAsVoid("Disabled");
   }

   public static boolean SAsBoolean(AbstractBoatEntity var0) {
      if (E != null && E.isEnabled()) {
         BoatFly var1 = E;
         if (var1 != null && var1.isRuntimeActive() && var0 != null) {
            try {
               MinecraftClient var2 = getMc();
               if (var2.player != null && var2.player.isAlive() && var0.getEntityWorld() == var2.world && var0.getControllingPassenger() == var2.player) {
                  var1.PAsVoid(var0);
                  return true;
               } else {
                  return false;
               }
            } catch (RuntimeException var31) {
               ClientLog.lAsVoid("BoatFly", "Boat control failed; disabling and returning native boat steering", var31);
               var1.disable();
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static void gAsVoid() {
      BoatFly var0 = E;
      if (var0 != null && var0.J != null && var0.t != null) {
         var0.t.aAsVoid();
         var0.O++;
         var0.X = 40;
         var0.o = "Server corrected the boat; try a lower speed";
         if (var0.O == 1 || var0.O % 20 == 0) {
            ClientLog.oAsVoid("BoatFly", "Server corrected the boat position ({} so far, speed {} b/s)", var0.O, var0.speed.getValue());
         }
      }
   }

   private void PAsVoid(AbstractBoatEntity var1) {
      if (var1 != this.J) {
         this.LAsVoid(this.J == null ? null : "Changed boats");
         this.J = var1;
         this.t = new DropAnimation();
         this.N = var1.getY();
         ClientLog.XAsVoid("BoatFly", "Flying {} at {}", var1.getName().getString(), var1.getBlockPos().toShortString());
      }

      this.a++;
      this.t.kAsVoid(var1.getY() - this.N, var1.groundCollision || !this.vAsBoolean(var1));
      this.N = var1.getY();
      GameOptions var2 = mc.options;
      int var3 = mc.currentScreen == null && ModuleKeybindChecker.VAsBoolean() ? 1 : 0;
      Vec3d var4 = var3 != 0 ? ModuleKeybindChecker.JAsVec3d() : Vec3d.ZERO;
      int var5 = var3 == 0 ? 0 : (var2.jumpKey.isPressed() ? 1 : 0) - (ModuleKeybindChecker.pAsBoolean(var2.sprintKey) ? 1 : 0);
      int var6 = this.t.JAsInt();
      Vec3d var7 = this.t
         .TAsVec3d(
            var4,
            this.speed.getValue() / 20.0,
            this.smoothing.getValue().intValue(),
            var5,
            this.verticalSpeed.getValue() / 20.0,
            this.fallSpeed.getValue() / 20.0,
            this.antiKick.getValue()
         );
      if (this.t.JAsInt() == 1 && var6 == 0) {
         ClientLog.XAsVoid("BoatFly", "Anti Kick dip after {} floating ticks; later dips are not logged", 40);
      }

      var1.setYaw(mc.player.getYaw());
      var1.setVelocity(var7);
      int var8 = this.t.eAsVec3d().lengthSquared() > 1.0E-4 ? 1 : 0;
      var1.setPaddlesMoving(var8 != 0, var8 != 0);
      if (this.X > 0) {
         this.X--;
      } else if (this.t.uAsBoolean()) {
         this.o = "Anti Kick dip";
      } else if (var8 == 0 && var5 == 0) {
         this.o = this.fallSpeed.getValue() > 0.0 ? "Gliding down" : "Hovering";
      } else {
         this.o = "Flying";
      }
   }

   private boolean vAsBoolean(AbstractBoatEntity var1) {
      return var1.getEntityWorld().getStatesInBox(var1.getBoundingBox().expand(0.0625).stretch(0.0, -0.55, 0.0)).allMatch(AbstractBlockState::isAir);
   }

   private void LAsVoid(String var1) {
      if (this.J != null && var1 != null) {
         ClientLog.XAsVoid("BoatFly", "{} after {} ticks: {} anti kick dips, {} server corrections", var1, this.a, this.t == null ? 0 : this.t.JAsInt(), this.O);
      }

      this.J = null;
      this.t = null;
      this.a = 0L;
      this.O = 0;
      this.X = 0;
      this.o = "Ride a boat";
   }
}
