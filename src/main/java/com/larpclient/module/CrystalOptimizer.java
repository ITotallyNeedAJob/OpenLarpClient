package com.larpclient.module;

import com.larpclient.ClientLog;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;

public final class CrystalOptimizer extends Module {
   private static CrystalOptimizer v;
   private String x = "Ready";
   private boolean s;

   public CrystalOptimizer() {
      super("Crystal Optimizer", "Crystals you break disappear at once client-side for faster placement", Category.COMBAT, -1);
      v = this;
   }

   public static boolean lAsBoolean(double var0, int var2) {
      return var0 > 0.0 || var2 > 0;
   }

   public static void OAsVoid(PlayerEntity var0, Entity var1) {
      if (v != null && v.isEnabled()) {
         CrystalOptimizer var2 = v;
         if (var2 != null && var2.isRuntimeActive() && var1 instanceof EndCrystalEntity var3) {
            MinecraftClient var4 = getMc();
            if (var0 == var4.player
               && var4.world != null
               && var4.interactionManager != null
               && !var4.interactionManager.isFlyingLocked()
               && var3.getEntityWorld() == var4.world
               && !var3.isRemoved()
               && !var3.isInvulnerable()) {
               ItemStack var5 = var0.getWeaponStack();
               int var6 = var4.world
                  .getRegistryManager()
                  .getOrThrow(RegistryKeys.ENCHANTMENT)
                  .getOptional(Enchantments.SHARPNESS)
                  .map(var1x -> EnchantmentHelper.getLevel(var1x, var5))
                  .orElse(0);
               if (!lAsBoolean(var0.getAttributeValue(EntityAttributes.ATTACK_DAMAGE), var6)) {
                  var2.x = "Your hit deals no damage";
               } else {
                  var4.world.removeEntity(var3.getId(), RemovalReason.KILLED);
                  var2.x = "Ready";
                  if (!var2.s) {
                     ClientLog.XAsVoid("CrystalOptimizer", "First crystal {} removed locally after the attack packet", var3.getId());
                     var2.s = true;
                  }
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.x : "";
   }

   @Override
   public void onEnable() {
      this.x = "Ready";
      this.s = false;
      ClientLog.GAsVoid("CrystalOptimizer", "Enabled; crystals you break are removed client-side after the native attack");
   }
}
