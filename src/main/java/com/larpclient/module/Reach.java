package com.larpclient.module;

import com.larpclient.b_;
import com.larpclient.rf;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class Reach extends Module {
   private final NumberSetting combat = this.addSetting(new NumberSetting("Combat", "Combat interaction range", 3.5, 3.0, 6.0, 0.1));
   private final NumberSetting block = this.addSetting(new NumberSetting("Block", "Block interaction range", 4.5, 4.5, 8.0, 0.1));
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "Fixed or nearby-target-limited range", "Normal", "Normal", "Dynamic"));
   private final BooleanSetting onlySword = this.addSetting(
      new BooleanSetting("OnlySword", "Only while holding a supported weapon (includes axes, trident and mace)", false)
   );
   private static Reach i;
   private final rf k = new rf();
   private final rf t = new rf();
   private Reach.Type_u R;

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(com.larpclient.module.k.SERVER_STATE_MODIFYING);
   }

   public Reach() {
      super("Reach", "Adjust attack and block reach", Category.COMBAT, -1);
      i = this;
   }

   public static Reach nAsReach() {
      return i;
   }

   public static boolean dAsBoolean() {
      ClientPlayerEntity var0 = getMc().player;
      if (var0 == null) {
         return false;
      } else {
         ItemStack var1 = var0.getMainHandStack();
         return var1.isOf(Items.WOODEN_SWORD)
            || var1.isOf(Items.STONE_SWORD)
            || var1.isOf(Items.IRON_SWORD)
            || var1.isOf(Items.GOLDEN_SWORD)
            || var1.isOf(Items.DIAMOND_SWORD)
            || var1.isOf(Items.NETHERITE_SWORD)
            || var1.isOf(Items.WOODEN_AXE)
            || var1.isOf(Items.STONE_AXE)
            || var1.isOf(Items.IRON_AXE)
            || var1.isOf(Items.GOLDEN_AXE)
            || var1.isOf(Items.DIAMOND_AXE)
            || var1.isOf(Items.NETHERITE_AXE)
            || var1.isOf(Items.TRIDENT)
            || var1.isOf(Items.MACE);
      }
   }

   private boolean VAsBoolean() {
      return this.isInGame();
   }

   private boolean rAsBoolean() {
      return this.isRuntimeActive() && this.VAsBoolean() && (!this.onlySword.getValue() || dAsBoolean());
   }

   @Override
   public String getStatus() {
      return this.rAsBoolean() ? "Configured range active" : "Waiting for player or weapon condition";
   }

   @Override
   public void onTick() {
      if (!this.rAsBoolean()) {
         this.onDisable();
      } else {
         double var1 = this.combat.getValue();
         if (this.mode.pAsBoolean("Dynamic")) {
            double var3 = var1;

            for (Entity var6 : mc.world.getEntities()) {
               if (var6 != mc.player && var6.isAlive() && var6.canHit()) {
                  var3 = Math.min(var3, b_.HAsDouble(var6) + 0.5);
               }
            }

            var1 = Math.max(3.0, var3);
         }

         this.k.dAsVoid(mc.player.getAttributeInstance(EntityAttributes.ENTITY_INTERACTION_RANGE), var1);
         this.t.dAsVoid(mc.player.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE), this.block.getValue());
         IntegratedServer var10 = mc.getServer();
         if (var10 == null) {
            this.rAsVoid();
         } else {
            if (this.R == null || this.R.K != var10) {
               this.rAsVoid();
               this.R = new Reach.Type_u(var10);
            }

            Reach.Type_u var4 = this.R;
            UUID var11 = mc.player.getUuid();
            double var12 = var1;
            double var8 = this.block.getValue();
            var10.execute(() -> {
               ServerPlayerEntity var7 = var10.getPlayerManager().getPlayer(var11);
               if (var4.z && var7 != null && var7.isAlive()) {
                  var4.Y.dAsVoid(var7.getAttributeInstance(EntityAttributes.ENTITY_INTERACTION_RANGE), var12);
                  var4.l.dAsVoid(var7.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE), var8);
               } else {
                  var4.aAsVoid();
               }
            });
         }
      }
   }

   @Override
   public void onDisable() {
      this.k.TAsVoid();
      this.t.TAsVoid();
      this.rAsVoid();
   }

   private void rAsVoid() {
      Reach.Type_u var1 = this.R;
      this.R = null;
      if (var1 != null) {
         var1.z = false;
         var1.K.execute(var1::aAsVoid);
      }
   }

   public void bAsVoid(EntityAttributeInstance var1) {
      this.k.jAsVoid(var1);
      this.t.jAsVoid(var1);
   }

   public double bAsDouble() {
      return getMc().player == null ? 3.0 : getMc().player.getEntityInteractionRange();
   }

   public double wAsDouble() {
      return this.bAsDouble();
   }

   public double xAsDouble() {
      return this.bAsDouble();
   }

   public double JAsDouble() {
      return getMc().player == null ? 4.5 : getMc().player.getBlockInteractionRange();
   }

   public boolean NAsBoolean(Entity var1) {
      return b_.nAsBoolean(var1, this.bAsDouble());
   }

   private static final class Type_u {
      final MinecraftServer K;
      final rf Y = new rf();
      final rf l = new rf();
      volatile boolean z = true;

      Type_u(MinecraftServer var1) {
         this.K = var1;
      }

      void aAsVoid() {
         this.Y.TAsVoid();
         this.l.TAsVoid();
      }
   }
}
