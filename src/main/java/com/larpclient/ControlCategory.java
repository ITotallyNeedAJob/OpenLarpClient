package com.larpclient;

import com.larpclient.module.Aura;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;

public final class ControlCategory {
   public ControlCategory() {
   }

   public static LivingEntity CAsLivingEntity(String var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      LivingEntity var2 = null;
      if (var0.equals("Auto") || var0.equals("Aura")) {
         Aura var3 = Aura.fAsAura();
         if (var3 != null) {
            var2 = var3.hAsLivingEntity();
         }
      }

      if (var2 == null && (var0.equals("Auto") || var0.equals("Combat"))) {
         var2 = SharedRenderEffects.HAsLivingEntity();
      }

      if (var2 == null
         && (var0.equals("Auto") || var0.equals("Crosshair"))
         && var1.crosshairTarget instanceof EntityHitResult var6
         && var6.getEntity() instanceof LivingEntity var4) {
         var2 = var4;
      }

      return var2 != null && var2.isAlive() && var2.getEntityWorld() == var1.world && var1.player != null && var2.squaredDistanceTo(var1.player) < 4096.0
         ? var2
         : null;
   }
}
