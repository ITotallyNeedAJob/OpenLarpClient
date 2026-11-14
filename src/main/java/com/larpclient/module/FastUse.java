package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ModuleImportBroker;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;

public final class FastUse extends Module {
   public static final int Q = 4;
   private static FastUse G;
   private final NumberSetting useInterval = this.addSetting(
      new NumberSetting("Use Interval", "Ticks between uses while Use is held. Vanilla is 4", 1.0, 1.0, 4.0, 1.0)
   );
   private final BooleanSetting blocks = this.addSetting(new BooleanSetting("Blocks", "Place blocks faster", true));
   private final BooleanSetting projectiles = this.addSetting(
      new BooleanSetting("Projectiles", "Throw experience bottles, pearls, snowballs, eggs, potions, wind charges and fireworks faster", true)
   );
   private final BooleanSetting otherItems = this.addSetting(new BooleanSetting("Other Items", "Use every other item faster", true));

   public FastUse() {
      super("Fast Use", "Remove the delay between item uses", Category.PLAYER, -1);
      G = this;
   }

   public static int clamp(int var0) {
      return Math.clamp((long)var0, 1, 4);
   }

   public static boolean lAsBoolean(ItemStack var0, boolean var1, boolean var2, boolean var3) {
      if (var0 != null && !var0.isEmpty()) {
         Item var4 = var0.getItem();
         if (var4 instanceof BlockItem) {
            return var1;
         } else {
            return !(var4 instanceof ProjectileItem) && !(var4 instanceof EnderPearlItem) ? var3 : var2;
         }
      } else {
         return false;
      }
   }

   public static int HAsInt(int var0) {
      if (G != null && G.isEnabled()) {
         FastUse var1 = G;
         if (var1 != null && var1.isRuntimeActive() && !ModuleImportBroker.EAsBoolean()) {
            ClientPlayerEntity var2 = getMc().player;
            if (var2 == null) {
               return var0;
            } else {
               boolean var3 = var1.blocks.getValue();
               boolean var4 = var1.projectiles.getValue();
               boolean var5 = var1.otherItems.getValue();
               return !lAsBoolean(var2.getMainHandStack(), var3, var4, var5) && !lAsBoolean(var2.getOffHandStack(), var3, var4, var5)
                  ? var0
                  : Math.min(var0, clamp(var1.useInterval.getValue().intValue()));
            }
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   @Override
   public void onEnable() {
      ClientLog.XAsVoid(
         "FastUse",
         "Enabled; use interval {} ticks (blocks {}, projectiles {}, other items {})",
         this.useInterval.getValue().intValue(),
         this.blocks.getValue(),
         this.projectiles.getValue(),
         this.otherItems.getValue()
      );
   }

   @Override
   public String getStatus() {
      int var1 = clamp(this.useInterval.getValue().intValue());
      return var1 == 1 ? "Every tick" : var1 + " ticks";
   }
}
