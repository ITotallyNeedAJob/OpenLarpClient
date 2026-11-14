package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ParticleGeometry;
import com.larpclient.ParticleKind;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public final class ShieldBreaker extends Module {
   private static ShieldBreaker K;
   private final BooleanSetting swapBack = this.addSetting(
      new BooleanSetting("Swap Back", "Return to your previous hotbar slot on the tick after the hit", true)
   );
   private final ParticleGeometry g = new ParticleGeometry(this);
   private String l = "Ready";
   private PlayerEntity t;
   private int z;

   public ShieldBreaker() {
      super("Shield Breaker", "Axe hit swap: your hits on a raised shield go out with your best axe, then your slot returns", Category.COMBAT, -1);
      K = this;
   }

   @Override
   public String getDisplayName() {
      return "Axe Shield Breaker";
   }

   public static void uAsVoid(ClientPlayerEntity var0, Entity var1) {
      if (K != null && K.isEnabled()) {
         ShieldBreaker var2 = K;
         if (var2 != null
            && var2.isRuntimeActive()
            && !ModuleImportBroker.pAsBoolean()
            && var1 instanceof PlayerEntity var3
            && var0 == getMc().player
            && !var2.g.FAsBoolean()
            && ParticleKind.PAsBoolean(var3, var0)
            && !ParticleKind.NAsBoolean(var3)) {
            Mace var4 = Mace.yAsMace();
            if (var4 != null && var4.lAsBoolean(var0, var3)) {
               var2.l = "Stun Slam by Auto Mace";
            } else {
               int var5 = ParticleKind.JAsInt(var0);
               if (var5 < 0) {
                  var2.l = "No axe in hotbar";
               } else if (!var2.g.uAsBoolean(var0, var5)) {
                  var2.l = "Hotbar busy";
               } else {
                  ParticleKind.wAsVoid(var3);
                  var2.t = var3;
                  var2.z = var3.age;
                  var2.l = "Shield break hit";
                  ClientLog.XAsVoid(
                     "ShieldBreaker",
                     "Hit {} through a raised shield with hotbar slot {}, shield used {} ticks, ping {} ticks",
                     var3.getId(),
                     var5 + 1,
                     var3.getItemUseTime(),
                     ParticleKind.WAsInt(var0)
                  );
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
      return this.isEnabled() ? this.l : "";
   }

   @Override
   public void onEnable() {
      this.l = "Ready";
      this.t = null;
      ClientLog.GAsVoid("ShieldBreaker", "Enabled; swap back {}", this.swapBack.getValue());
   }

   @Override
   public void onDisable() {
      if (this.g.FAsBoolean()) {
         this.g.vAsVoid(getMc(), this.swapBack.getValue());
      }

      this.t = null;
   }

   @Override
   public void onTick() {
      this.GAsVoid();
      if (this.g.FAsBoolean()) {
         this.g.IAsVoid(mc, this.swapBack.getValue());
         if (!this.g.FAsBoolean()) {
            this.l = "Ready";
         }
      }
   }

   private void GAsVoid() {
      PlayerEntity var1 = this.t;
      if (var1 != null) {
         int var2 = var1.age - this.z;
         if (var1.isRemoved() || var1.getEntityWorld() != mc.world) {
            this.t = null;
         } else if (!var1.isUsingItem()) {
            this.t = null;
            ClientLog.XAsVoid("ShieldBreaker", "Shield of {} went down {} ticks after the axe hit", var1.getId(), var2);
         } else if (!ParticleKind.NAsBoolean(var1)) {
            this.t = null;
            this.l = "Retrying";
            ClientLog.GAsVoid("ShieldBreaker", "Shield of {} still up {} ticks after the axe hit; the next hit retries", var1.getId(), var2);
         }
      }
   }
}
