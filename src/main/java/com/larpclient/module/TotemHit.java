package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ParticleGeometry;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;

public final class TotemHit extends Module {
   private static TotemHit G;
   private final NumberSetting switchDelay = this.addSetting(
      new NumberSetting("Switch Delay", "Milliseconds before returning to your totem after the sword hit", 30.0, 10.0, 100.0, 1.0)
   );
   private final ParticleGeometry l = new ParticleGeometry(this);
   private long H;
   private String j = "Ready";

   public TotemHit() {
      super("Totem Hit", "Hit with your sword while holding a totem, then switch back", Category.COMBAT, -1);
      G = this;
   }

   public static int vAsInt(ClientPlayerEntity var0) {
      for (int var1 = 0; var1 < 9; var1++) {
         if (var0.getInventory().getStack(var1).isIn(ItemTags.SWORDS)) {
            return var1;
         }
      }

      return -1;
   }

   public static boolean zAsBoolean(long var0, long var2, double var4) {
      return var2 - var0 >= (long)(var4 * 1000000.0);
   }

   public static void fAsVoid(ClientPlayerEntity var0, Entity var1) {
      if (G != null && G.isEnabled()) {
         TotemHit var2 = G;
         if (var2 != null && var2.isRuntimeActive() && var1 != null && !ModuleImportBroker.pAsBoolean()) {
            MinecraftClient var3 = getMc();
            if (var0 == var3.player && !var2.l.FAsBoolean() && var0.getMainHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
               int var4 = vAsInt(var0);
               if (var4 < 0) {
                  var2.j = "No sword in hotbar";
               } else if (!var2.l.uAsBoolean(var0, var4)) {
                  var2.j = "Hotbar busy";
               } else {
                  var2.H = System.nanoTime();
                  var2.j = "Sword hit";
                  ClientLog.XAsVoid("TotemHit", "Hit {} with hotbar sword {}", var1.getId(), var4 + 1);
               }
            }
         }
      }
   }

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.INTENT;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.VANILLA_INPUT, k.SERVER_STATE_MODIFYING);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.j : "";
   }

   @Override
   public void onEnable() {
      this.j = "Ready";
      ClientLog.GAsVoid("TotemHit", "Enabled; switch delay {} ms", this.switchDelay.getValue().intValue());
   }

   @Override
   public void onDisable() {
      if (this.l.FAsBoolean()) {
         this.l.vAsVoid(getMc(), true);
      }
   }

   @Override
   public void onTick() {
      if (this.l.FAsBoolean() && zAsBoolean(this.H, System.nanoTime(), this.switchDelay.getValue())) {
         this.l.vAsVoid(mc, true);
         this.j = "Ready";
      }
   }
}
