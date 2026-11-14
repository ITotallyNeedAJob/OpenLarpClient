package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ModuleImportBroker;
import com.larpclient.OptifineBridge;
import com.larpclient.Debounce;
import java.util.Optional;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class TriggerBot extends Module {
   private final NumberSetting delay = this.addSetting(new NumberSetting("Delay", "Attack delay (ticks)", 0.0, 0.0, 10.0, 1.0));
   private final BooleanSetting playersOnly = this.addSetting(new BooleanSetting("Players Only", "Only attack players", false));
   private final BooleanSetting smartDelay = this.addSetting(new BooleanSetting("Smart Delay", "Wait for attack cooldown", true));
   private final pa U = new pa();
   private Entity c;
   private String I = "Waiting for a target";
   private long z;

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.INTENT;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.VANILLA_INPUT);
   }

   @Override
   public void onDisable() {
      this.uAsVoid("Waiting for a target");
      ClientLog.XAsVoid("TriggerBot", "Released target and queued attack");
   }

   private void uAsVoid(String var1) {
      this.c = null;
      this.U.KAsVoid();
      b_.KAsVoid(this);
      this.VAsVoid(var1);
   }

   public TriggerBot() {
      super("TriggerBot", "Attack the entity you aim at", Category.COMBAT, -1);
   }

   @Override
   public void onTick() {
      if (!this.JAsBoolean()) {
         this.uAsVoid("Paused: input unavailable");
      } else {
         Entity var1 = this.LAsEntity();
         if (!this.aAsBoolean(var1)) {
            this.uAsVoid("Waiting for a target");
         } else {
            String var2 = b_.MAsString(var1, mc.player.getEntityInteractionRange(), mc.player.getRotationVector());
            if (var2 != null) {
               this.uAsVoid("Waiting: " + var2);
            } else {
               this.c = var1;
               if (!this.U.bAsBoolean(var1, mc.world.getTime(), this.delay.getValue().intValue())) {
                  this.VAsVoid("Waiting for delay");
               } else if (this.smartDelay.getValue() && !b_.VAsBoolean()) {
                  this.VAsVoid("Waiting for attack cooldown");
               } else {
                  this.VAsVoid("Ready");
                  b_.tAsVoid(
                     this,
                     var1,
                     mc.player.getEntityInteractionRange(),
                     this.smartDelay.getValue(),
                     () -> this.JAsBoolean() && this.c == var1 && this.aAsBoolean(var1),
                     1,
                     () -> {
                        this.U.JAsVoid(var1, mc.world.getTime());
                        this.VAsVoid("Attacked " + var1.getName().getString());
                     },
                     var1x -> this.VAsVoid("Waiting: " + var1x)
                  );
               }
            }
         }
      }
   }

   private boolean JAsBoolean() {
      return this.isInGame()
         && b_.jAsBoolean()
         && mc.isWindowFocused()
         && mc.getOverlay() == null
         && !mc.player.isUsingItem()
         && !mc.options.useKey.isPressed();
   }

   private boolean aAsBoolean(Entity var1) {
      if (var1 instanceof LivingEntity var2
         && var2.isAlive()
         && !var1.isRemoved()
         && var1.getEntityWorld() == mc.world
         && var1 != mc.player
         && (!this.playersOnly.getValue() || var1 instanceof PlayerEntity)) {
         return true;
      }

      return false;
   }

   private Entity LAsEntity() {
      Vec3d var1 = mc.player.getEyePos();
      Vec3d var2 = mc.player.getRotationVector();
      double var3 = mc.player.getEntityInteractionRange();
      Vec3d var5 = var1.add(var2.multiply(var3));
      return jAsEntity(mc.player, var1, var2, var3, mc.world.getOtherEntities(mc.player, new Box(var1, var5).expand(1.0)));
   }

   private static Entity jAsEntity(Entity var0, Vec3d var1, Vec3d var2, double var3, Iterable<Entity> var5) {
      Entity var6 = null;
      double var7 = var3 * var3 + 1.0E-7;

      for (Entity var10 : var5) {
         if (var10 != var0 && var10.canHit() && !var10.isSpectator() && !var10.isRemoved() && var10.getRootVehicle() != var0.getRootVehicle()) {
            Optional var11 = o5.iAsOptional(var1, var2, var10.getBoundingBox(), var3);
            if (!var11.isEmpty()) {
               double var12 = var1.squaredDistanceTo((Vec3d)var11.get());
               if (var12 < var7) {
                  var6 = var10;
                  var7 = var12;
               }
            }
         }
      }

      return var6;
   }

   private void VAsVoid(String var1) {
      if (!this.I.equals(var1)) {
         this.I = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.z >= 1000L) {
            this.z = var2;
            ClientLog.XAsVoid("TriggerBot", "{}", var1);
         }
      }
   }

   @Override
   public void onEnable() {
      this.uAsVoid("Waiting for a target");
      ClientLog.XAsVoid(
         "TriggerBot",
         "Fresh body-ray trigger active; delay={}, cooldown={}, players={}",
         this.delay.getValue(),
         this.smartDelay.getValue(),
         this.playersOnly.getValue()
      );
   }

   @Override
   public String getStatus() {
      return this.I;
   }
}
