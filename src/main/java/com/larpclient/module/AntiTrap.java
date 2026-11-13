package com.larpclient.module;

import com.larpclient.ClientLog;
import java.util.Set;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;

public final class AntiTrap extends Module {
   private static AntiTrap x;
   private final BooleanSetting J = this.yAsBooleanSetting("Paintings", true);
   private final BooleanSetting f = this.yAsBooleanSetting("Item Frames", true);
   private final BooleanSetting A = this.yAsBooleanSetting("Minecarts", true);
   private final BooleanSetting G = this.yAsBooleanSetting("Falling Blocks", false);
   private final BooleanSetting C = this.yAsBooleanSetting("Armor Stands", false);
   private final BooleanSetting B = this.yAsBooleanSetting("Banners", false);
   private final BooleanSetting n = this.yAsBooleanSetting("Villagers", false);
   private final BooleanSetting Q = this.yAsBooleanSetting("Horses", false);
   private final BooleanSetting T = this.yAsBooleanSetting("Signs", false);
   private final BooleanSetting L = this.yAsBooleanSetting("Boats", false);

   public AntiTrap() {
      super("AntiTrap", "Skip interactions with selected trap objects", Category.COMBAT, -1);
      x = this;
   }

   private BooleanSetting yAsBooleanSetting(String var1, boolean var2) {
      return this.addSetting(new BooleanSetting(var1, "Ignore " + var1.toLowerCase() + " when interacting", var2));
   }

   public static boolean TAsBoolean(Entity var0) {
      if (x != null && x.isEnabled()) {
         AntiTrap var1 = x;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (
               var0 instanceof PaintingEntity && var1.J.getValue()
                  || var0 instanceof ItemFrameEntity && var1.f.getValue()
                  || var0 instanceof AbstractMinecartEntity && var1.A.getValue()
                  || var0 instanceof FallingBlockEntity && var1.G.getValue()
                  || var0 instanceof ArmorStandEntity && var1.C.getValue()
                  || var0 instanceof VillagerEntity && var1.n.getValue()
                  || var0 instanceof AbstractHorseEntity && var1.Q.getValue()
                  || var0 instanceof AbstractBoatEntity && var1.L.getValue()
            );
      } else {
         return false;
      }
   }

   public static boolean YAsBoolean(BlockState var0) {
      if (x != null && x.isEnabled()) {
         AntiTrap var1 = x;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (var0.getBlock() instanceof AbstractBannerBlock && var1.B.getValue() || var0.getBlock() instanceof AbstractSignBlock && var1.T.getValue());
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      ClientLog.XAsVoid("AntiTrap", "Interaction trap filters active");
   }

   @Override
   public void onDisable() {
      ClientLog.XAsVoid("AntiTrap", "Interaction trap filters released");
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.VANILLA_INPUT);
   }
}
