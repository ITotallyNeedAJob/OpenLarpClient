package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ModuleKeybindChecker;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.Vec3d;

public final class ElytraMotion extends Module {
   private static ElytraMotion A;
   private final NumberSetting distance = this.addSetting(new NumberSetting("Distance", "Distance from your eyes to the Kill Aura target", 3.0, 0.1, 6.0, 0.1));
   private final BooleanSetting swapChestplate = this.addSetting(
      new BooleanSetting("Swap Chestplate", "Equip a chestplate while holding near the target", true)
   );
   private final BooleanSetting useFirework = this.addSetting(new BooleanSetting("Use Firework", "Use a hotbar firework when returning to flight", true));
   private final BooleanSetting legit = this.addSetting(new BooleanSetting("Legit", "Retained reference preference; takeoff uses normal client actions", false));
   private ClientPlayerEntity c;
   private ClientPlayerEntity k;
   private ClientWorld t;
   private ClientWorld I;
   private int x = -1;
   private ItemStack g = ItemStack.EMPTY;
   private ItemStack l = ItemStack.EMPTY;
   private boolean Q;
   private boolean e;

   public ElytraMotion() {
      super("ElytraMotion", "Stay near your Kill Aura target with elytra", Category.MOVEMENT, -1);
      A = this;
      this.useFirework.visibleWhen(this.swapChestplate::getValue);
      this.legit.visibleWhen(() -> false);
   }

   public static ElytraMotion vAsElytraMotion() {
      return A;
   }

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.CONSTRAINT;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(com.larpclient.module.k.SERVER_STATE_MODIFYING);
   }

   private boolean lAsBoolean() {
      LivingEntity var1 = ElytraHelper.pAsLivingEntity();
      return var1 != null && mc.player.getEyePos().distanceTo(var1.getEntityPos()) < this.distance.getValue();
   }

   public boolean ZAsBoolean() {
      return this.isEnabled()
         ? this.isRuntimeActive()
            && this.OAsBoolean()
            && ModuleKeybindChecker.VAsBoolean()
            && !mc.player.hasVehicle()
            && this.lAsBoolean()
            && (this.x >= 0 && this.qAsBoolean() || mc.player.isGliding())
         : false;
   }

   @Override
   public void onEnable() {
      this.Q = false;
      this.e = false;
      this.JAsVoid();
      this.c = mc.player;
      this.t = mc.world;
   }

   private boolean OAsBoolean() {
      return this.isInGame() && mc.player == this.c && mc.world == this.t;
   }

   private boolean qAsBoolean() {
      return this.isInGame() && mc.player == this.k && mc.world == this.I;
   }

   @Override
   public void onDisable() {
      ModuleImportBroker.KAsVoid(this);
      if (this.RAsBoolean() && this.oAsBoolean()) {
         this.mAsVoid();
      } else if (this.x >= 0) {
         ClientLog.oAsVoid("ElytraMotion", "Cleanup kept current equipment: owned swap no longer verifiable");
      }

      this.Q = false;
      this.e = false;
      this.JAsVoid();
      this.c = null;
      this.t = null;
   }

   @Override
   public void onTick() {
      if (!this.OAsBoolean()) {
         ModuleImportBroker.KAsVoid(this);
         this.Q = false;
         this.e = false;
         this.JAsVoid();
      } else if (ModuleKeybindChecker.VAsBoolean()) {
         if (this.x >= 0 && !this.RAsBoolean()) {
            this.JAsVoid();
            this.Q = false;
            ClientLog.XAsVoid("ElytraMotion", "Equipment changed externally; released swap ownership");
         }

         boolean var1 = this.ZAsBoolean();
         if (var1) {
            mc.player.setVelocity(Vec3d.ZERO);
            if (!this.Q) {
               ClientLog.XAsVoid("ElytraMotion", "Holding near Aura target");
            }

            this.Q = true;
            if (this.swapChestplate.getValue() && this.x < 0 && mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA) && this.jAsBoolean()) {
               ModuleImportBroker.UAsVoid(
                  this,
                  this::sAsVoid,
                  () -> this.ZAsBoolean()
                        && this.swapChestplate.getValue()
                        && this.jAsBoolean()
                        && mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)
               );
            }
         } else {
            if (this.Q) {
               this.Q = false;
               this.e = this.x >= 0;
               ClientLog.XAsVoid("ElytraMotion", "Target left hold range; restoring flight");
            }

            if (this.x >= 0 && this.jAsBoolean()) {
               ModuleImportBroker.UAsVoid(this, this::mAsVoid, () -> !this.ZAsBoolean() && this.RAsBoolean() && this.jAsBoolean());
            } else if (this.e && this.x < 0) {
               if (!this.VAsBoolean()) {
                  this.e = false;
                  return;
               }

               ModuleImportBroker.UAsVoid(this, () -> {
                  if (this.VAsBoolean()) {
                     ElytraHelper.mAsVoid();
                     if (this.useFirework.getValue()) {
                        ElytraHelper.DAsVoid(() -> {
                        });
                     }

                     this.e = false;
                     ClientLog.XAsVoid("ElytraMotion", "Flight restored; firework={}", this.useFirework.getValue());
                  }
               }, this::VAsBoolean);
            }
         }
      }
   }

   private boolean VAsBoolean() {
      AirStuck var1 = AirStuck.zAsAirStuck();
      return this.OAsBoolean()
         && ModuleKeybindChecker.VAsBoolean()
         && ModuleImportBroker.jAsBoolean()
         && !mc.player.getAbilities().flying
         && !mc.player.isOnGround()
         && !mc.player.isTouchingWater()
         && !mc.player.isInLava()
         && !mc.player.hasVehicle()
         && !this.lAsBoolean()
         && mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)
         && (var1 == null || !var1.wAsBoolean());
   }

   private boolean jAsBoolean() {
      return this.oAsBoolean() && ModuleImportBroker.jAsBoolean();
   }

   private boolean oAsBoolean() {
      return this.OAsBoolean()
         && mc.interactionManager != null
         && mc.player.isAlive()
         && !mc.player.isUsingItem()
         && mc.player.currentScreenHandler == mc.player.playerScreenHandler
         && mc.player.playerScreenHandler.getCursorStack().isEmpty();
   }

   private void sAsVoid() {
      int var1 = this.FAsInt();
      if (var1 >= 0) {
         ItemStack var2 = mc.player.getEquippedStack(EquipmentSlot.CHEST).copy();
         ItemStack var3 = mc.player.playerScreenHandler.getSlot(var1).getStack().copy();
         this.bAsVoid(var1);
         if (ItemStack.areItemsAndComponentsEqual(mc.player.getEquippedStack(EquipmentSlot.CHEST), var3)
            && ItemStack.areItemsAndComponentsEqual(mc.player.playerScreenHandler.getSlot(var1).getStack(), var2)) {
            this.x = var1;
            this.g = var2;
            this.l = var3;
            this.k = mc.player;
            this.I = mc.world;
            ClientLog.XAsVoid("ElytraMotion", "Equipped chestplate from slot {}", var1);
         } else {
            ClientLog.oAsVoid("ElytraMotion", "Chest swap prediction differed; kept current equipment");
         }
      }
   }

   private int FAsInt() {
      Item[] var1 = new Item[]{
         Items.NETHERITE_CHESTPLATE,
         Items.DIAMOND_CHESTPLATE,
         Items.IRON_CHESTPLATE,
         Items.GOLDEN_CHESTPLATE,
         Items.CHAINMAIL_CHESTPLATE,
         Items.LEATHER_CHESTPLATE
      };

      for (int var2 = 9; var2 < 45; var2++) {
         for (Item var6 : var1) {
            if (mc.player.playerScreenHandler.getSlot(var2).getStack().isOf(var6)) {
               return var2;
            }
         }
      }

      return -1;
   }

   private boolean RAsBoolean() {
      return this.x >= 0
         && this.qAsBoolean()
         && NAsBoolean(mc.player.getEquippedStack(EquipmentSlot.CHEST), this.l)
         && NAsBoolean(mc.player.playerScreenHandler.getSlot(this.x).getStack(), this.g);
   }

   private static boolean NAsBoolean(ItemStack var0, ItemStack var1) {
      ItemStack var2 = var0.copy();
      var2.setDamage(var1.getDamage());
      return ItemStack.areEqual(var2, var1);
   }

   private void mAsVoid() {
      if (this.RAsBoolean() && this.oAsBoolean()) {
         this.bAsVoid(this.x);
         if (mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)) {
            ClientLog.XAsVoid("ElytraMotion", "Restored owned elytra");
            this.JAsVoid();
         } else {
            ClientLog.oAsVoid("ElytraMotion", "Elytra restore prediction differed; retaining ownership for retry");
         }
      }
   }

   private void bAsVoid(int var1) {
      int var2 = mc.player.playerScreenHandler.syncId;
      mc.interactionManager.clickSlot(var2, var1, 0, SlotActionType.PICKUP, mc.player);
      mc.interactionManager.clickSlot(var2, 6, 0, SlotActionType.PICKUP, mc.player);
      mc.interactionManager.clickSlot(var2, var1, 0, SlotActionType.PICKUP, mc.player);
   }

   private void JAsVoid() {
      this.x = -1;
      this.g = ItemStack.EMPTY;
      this.l = ItemStack.EMPTY;
      this.k = null;
      this.I = null;
   }

   @Override
   public String getStatus() {
      return this.Q ? "Holding" : (this.e ? "Resume" : "");
   }
}
