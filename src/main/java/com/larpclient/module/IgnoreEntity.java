package com.larpclient.module;

import com.larpclient.ClientLog;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class IgnoreEntity extends Module {
   private static IgnoreEntity f;

   public IgnoreEntity() {
      super("IgnoreEntity", "Place blocks, crystals and fluids through entities", Category.COMBAT, -1);
      f = this;
   }

   public static boolean yAsBoolean(Entity var0) {
      if (f != null && f.isEnabled()) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         return f != null
            && f.isRuntimeActive()
            && var0 != null
            && var1.player != null
            && var1.world != null
            && var1.options.useKey.isPressed()
            && (yAsBoolean(var1.player.getMainHandStack()) || yAsBoolean(var1.player.getOffHandStack()));
      } else {
         return false;
      }
   }

   private static boolean yAsBoolean(ItemStack var0) {
      return !var0.isEmpty()
         && (var0.getItem() instanceof BlockItem || var0.isOf(Items.END_CRYSTAL) || var0.isOf(Items.WATER_BUCKET) || var0.isOf(Items.LAVA_BUCKET));
   }

   @Override
   public void onEnable() {
      ClientLog.XAsVoid("IgnoreEntity", "Placement entity pick filter active");
   }

   @Override
   public void onDisable() {
      ClientLog.XAsVoid("IgnoreEntity", "Placement entity pick filter released");
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.VANILLA_INPUT);
   }
}
