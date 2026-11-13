package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.p5;
import com.larpclient.r6;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;

public final class NoJumpDelay extends Module {
   private static NoJumpDelay T;
   private final NumberSetting jumpCooldown = this.addSetting(
      new NumberSetting("Jump Cooldown", "Ticks before a held jump can rearm after a grounded jump", 0.0, 0.0, 10.0, 1.0)
   );

   public NoJumpDelay() {
      super("No Jump Delay", "Remove the delay between grounded jumps", Category.MOVEMENT, -1);
      T = this;
   }

   public static int VAsInt(int var0) {
      return Math.clamp((long)var0, 0, 10);
   }

   public static int vAsInt(LivingEntity var0, int var1) {
      if (T != null && T.isEnabled()) {
         NoJumpDelay var2 = T;
         if (var2 != null && var2.isRuntimeActive()) {
            MinecraftClient var3 = getMc();
            if (var0 == var3.player
               && var3.player != null
               && r6.VAsBoolean()
               && p5.NAsP5_Type_u() == p5.Type_u.NONE
               && var3.currentScreen == null
               && var3.isWindowFocused()
               && !var0.hasVehicle()
               && !var0.isTouchingWater()
               && !var0.isInLava()
               && !var3.player.getAbilities().flying
               && !var3.player.isGliding()) {
               AirStuck var4 = AirStuck.zAsAirStuck();
               ElytraMotion var5 = ElytraMotion.vAsElytraMotion();
               Fly var6 = Fly.bAsFly();
               return (var4 == null || !var4.wAsBoolean()) && (var5 == null || !var5.ZAsBoolean()) && (var6 == null || !var6.vAsBoolean())
                  ? VAsInt(var2.jumpCooldown.getValue().intValue())
                  : var1;
            } else {
               return var1;
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   @Override
   public void onEnable() {
      ClientLog.XAsVoid("NoJumpDelay", "Native grounded jump cooldown: {} ticks", this.jumpCooldown.getValue().intValue());
   }

   @Override
   public String getStatus() {
      int var1 = this.jumpCooldown.getValue().intValue();
      return var1 == 0 ? "Ready immediately" : var1 + (var1 == 1 ? " tick cooldown" : " ticks cooldown");
   }
}
