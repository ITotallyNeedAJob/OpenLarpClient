package com.larpclient.module;

import java.util.List;
import java.util.Set;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmor extends Module {
   private int u = 0;
   private final BooleanSetting diamond = new BooleanSetting("Diamond", "Equip diamond armor upgrades", true);
   private final BooleanSetting netherite = new BooleanSetting("Netherite", "Equip netherite armor upgrades", true);
   private final BooleanSetting gold = new BooleanSetting("Gold", "Equip gold armor upgrades", true);
   private final BooleanSetting iron = new BooleanSetting("Iron", "Equip iron armor upgrades", true);
   private static final Set<Item> b = Set.of(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS);
   private static final Set<Item> T = Set.of(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS);
   private static final Set<Item> i = Set.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS);
   private static final Set<Item> q = Set.of(Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS);
   private static final Set<Item> f = Set.of(
      Items.CHAINMAIL_HELMET,
      Items.CHAINMAIL_CHESTPLATE,
      Items.CHAINMAIL_LEGGINGS,
      Items.CHAINMAIL_BOOTS,
      Items.COPPER_HELMET,
      Items.COPPER_CHESTPLATE,
      Items.COPPER_LEGGINGS,
      Items.COPPER_BOOTS,
      Items.TURTLE_HELMET
   );

   public AutoArmor() {
      super("AutoArmor", "Automatically equip better allowed armor", Category.PLAYER, -1);
      this.addSetting(this.diamond);
      this.addSetting(this.netherite);
      this.addSetting(this.gold);
      this.addSetting(this.iron);
   }

   public List<Setting<?>> mAsList() {
      return List.of(this.diamond, this.netherite, this.gold, this.iron);
   }

   public boolean xAsBoolean(ItemStack var1, EquipmentSlot var2) {
      if (!pAsBoolean(var1, var2)) {
         return false;
      } else {
         Item var3 = var1.getItem();
         if (b.contains(var3)) {
            return this.diamond.getValue();
         } else if (T.contains(var3)) {
            return this.netherite.getValue();
         } else if (i.contains(var3)) {
            return this.gold.getValue();
         } else {
            return q.contains(var3) ? this.iron.getValue() : f.contains(var3);
         }
      }
   }

   @Override
   public void onDisable() {
      this.u = 0;
   }

   @Override
   public void onTick() {
      if (this.isInGame() && mc.currentScreen == null) {
         if (this.u > 0) {
            this.u--;
         } else {
            for (EquipmentSlot var4 : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
               int var5 = this.RAsInt(var4);
               ItemStack var6 = mc.player.getEquippedStack(var4);
               int var7 = HAsInt(var6, var4);
               int var8 = -1;
               int var9 = var7;

               for (int var10 = 0; var10 < 36; var10++) {
                  ItemStack var11 = mc.player.getInventory().getStack(var10);
                  if (this.xAsBoolean(var11, var4)) {
                     int var12 = HAsInt(var11, var4);
                     if (var12 > var9) {
                        var9 = var12;
                        var8 = var10;
                     }
                  }
               }

               if (var8 != -1) {
                  int var13 = mc.player.currentScreenHandler.syncId;
                  int var14 = var8 < 9 ? var8 + 36 : var8;
                  mc.interactionManager.clickSlot(var13, var14, 0, SlotActionType.PICKUP, mc.player);
                  mc.interactionManager.clickSlot(var13, var5, 0, SlotActionType.PICKUP, mc.player);
                  if (!var6.isEmpty()) {
                     mc.interactionManager.clickSlot(var13, var14, 0, SlotActionType.PICKUP, mc.player);
                  }

                  this.u = 3;
                  return;
               }
            }
         }
      }
   }

   private static boolean pAsBoolean(ItemStack var0, EquipmentSlot var1) {
      if (var0.isEmpty()) {
         return false;
      } else {
         Item var2 = var0.getItem();
         if (!b.contains(var2) && !T.contains(var2) && !i.contains(var2) && !q.contains(var2) && !f.contains(var2)) {
            return false;
         } else {
            EquippableComponent var3 = (EquippableComponent)var0.get(DataComponentTypes.EQUIPPABLE);
            return var3 != null && var3.slot() == var1;
         }
      }
   }

   private static int HAsInt(ItemStack var0, EquipmentSlot var1) {
      if (!var0.isEmpty() && pAsBoolean(var0, var1)) {
         double[] var2 = new double[]{0.0};
         var0.applyAttributeModifiers(var1, (var1x, var2xx) -> {
            if (var1x.matches(EntityAttributes.ARMOR) && var2xx.operation() == Operation.ADD_VALUE) {
               var2[0] += var2xx.value();
            }
         });
         return (int)Math.round(var2[0]);
      } else {
         return -1;
      }
   }

   private int RAsInt(EquipmentSlot var1) {
      return switch (var1) {
         case HEAD -> 5;
         case CHEST -> 6;
         case LEGS -> 7;
         case FEET -> 8;
         default -> -1;
      };
   }
}
