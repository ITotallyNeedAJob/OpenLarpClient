package com.larpclient.module;

import com.larpclient.ModuleKeybindChecker;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.util.math.Vec3d;

public class Fly extends Module {
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Flight speed", 2.0, 0.5, 10.0, 0.1));
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "Vanilla flight or gradual glide descent", "Vanilla", "Vanilla", "Glide"));
   private final NumberSetting glideSpeed = this.addSetting(new NumberSetting("Glide Speed", "Target descent speed", 0.02, 0.0, 0.1, 0.005));
   private static Fly d;
   private ClientPlayerEntity J;
   private boolean c;
   private boolean C;
   private boolean S;
   private float R;
   private float T;

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   public Fly() {
      super("Fly", "Fly or glide slowly", Category.MOVEMENT, -1);
      d = this;
      this.glideSpeed.visibleWhen(() -> this.mode.pAsBoolean("Glide"));
   }

   public static Fly bAsFly() {
      return d;
   }

   public boolean vAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && getMc().player != null && getMc().player.isAlive() : false;
   }

   @Override
   public String getStatus() {
      if (!this.isEnabled()) {
         return "Vanilla flight or gradual glide descent";
      } else {
         return this.vAsBoolean() ? "Flight active" : "Waiting for player";
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame() || !this.vAsBoolean() || !ModuleKeybindChecker.VAsBoolean()) {
         this.YAsVoid();
      } else if (this.mode.pAsBoolean("Vanilla")) {
         PlayerAbilities var1 = mc.player.getAbilities();
         if (this.J != mc.player) {
            this.YAsVoid();
            this.J = mc.player;
            this.S = var1.allowFlying;
            this.c = var1.flying;
            this.R = var1.flySpeed;
         }

         if (var1.flying != this.C) {
            this.c = var1.flying;
         }

         if (var1.flySpeed != this.T) {
            this.R = var1.flySpeed;
         }

         if (!var1.allowFlying) {
            this.S = false;
         }

         int var2 = !var1.flying ? 1 : 0;
         var1.allowFlying = true;
         var1.flying = this.C = true;
         var1.flySpeed = this.T = this.speed.getValue().floatValue() * 0.05F;
         if (var2 != 0) {
            mc.player.sendAbilitiesUpdate();
         }
      } else {
         this.YAsVoid();
         Vec3d var4 = mc.player.getVelocity();
         if (!mc.player.isOnGround() && var4.y < 0.0 && !mc.player.isTouchingWater() && !mc.player.isInLava()) {
            Vec3d var5 = ModuleKeybindChecker.JAsVec3d().multiply(this.speed.getValue() * 0.15);
            Vec3d var3 = ModuleKeybindChecker.PAsVec3d(new Vec3d(var4.x, 0.0, var4.z), var5, 0.04);
            mc.player.setVelocity(var3.x, Math.min(0.0, var4.y + Math.min(0.08, -this.glideSpeed.getValue() - var4.y)), var3.z);
         }
      }
   }

   private void YAsVoid() {
      if (this.J != null) {
         PlayerAbilities var1 = this.J.getAbilities();
         boolean var2 = var1.flying;
         if (var1.allowFlying) {
            var1.allowFlying = this.S;
         }

         if (var1.flying == this.C) {
            var1.flying = var1.allowFlying && this.c;
         }

         if (var1.flySpeed == this.T) {
            var1.flySpeed = this.R;
         }

         if (var2 != var1.flying && this.J == getMc().player && this.J.isAlive()) {
            this.J.sendAbilitiesUpdate();
         }

         this.J = null;
      }
   }

   public void jAsVoid() {
      this.J = null;
   }

   @Override
   public void onDisable() {
      this.YAsVoid();
   }
}
