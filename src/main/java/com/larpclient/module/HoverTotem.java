package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.VisualInventory;
import com.larpclient.MixinCompat;
import com.larpclient.ParticleEmitter;
import com.larpclient.mixin.ClientPlayerEntityAccessor;
import com.larpclient.mixin.HandledScreenAccessor;
import java.util.Set;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.PlayerInput;

public final class HoverTotem extends Module {
   private static HoverTotem a;
   private final BooleanSetting hotbarRefill = this.addSetting(
      new BooleanSetting("Hotbar Refill", "After filling the offhand, move hovered totems into the chosen hotbar slot", false)
   );
   private final NumberSetting hotbarSlot = this.addSetting(
      new NumberSetting("Hotbar Slot", "Hotbar slot to refill, replacing its current item when needed", 1.0, 1.0, 9.0, 1.0)
   );

   public HoverTotem() {
      super("HoverTotem", "Move hovered totems into your offhand or chosen hotbar slot", Category.COMBAT, -1);
      this.hotbarSlot.visibleWhen(this.hotbarRefill::getValue);
      a = this;
   }

   public static HoverTotem pAsHoverTotem() {
      return a;
   }

   @Override
   public String getDisplayName() {
      return "Hover Totem";
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   @Override
   public void onEnable() {
      ClientLog.XAsVoid("HoverTotem", "Ready for native container hover swaps; AutoTotem has priority");
   }

   @Override
   public void onTick() {
      if (this.isInGame()
         && mc.interactionManager != null
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && !mc.player.isInCreativeMode()
         && !ParticleEmitter.oAsBoolean()
         && mc.isWindowFocused()
         && mc.currentScreen instanceof HandledScreen var1
         && !AutoTotem.NAsBoolean()) {
         ClientPlayerEntityAccessor var11 = (ClientPlayerEntityAccessor)mc.player;
         PlayerInput var3 = var11.larpLastSentInput();
         if (var3 != null && MixinCompat.uAsBoolean(var11.larpWasSprinting(), var3.forward(), var3.backward(), var3.left(), var3.right(), var3.jump(), var3.sneak())) {
            ScreenHandler var4 = var1.getScreenHandler();
            if (var4 == mc.player.currentScreenHandler
               && var4.canUse(mc.player)
               && var4.getCursorStack().isEmpty()
               && mc.player.playerScreenHandler.getCursorStack().isEmpty()) {
               Slot var5 = ((HandledScreenAccessor)var1).larpHoveredSlot();
               Slot var6 = XAsSlot(var4, var5);
               if (var6 != null && var6.isEnabled() && !var6.disablesDynamicDisplay() && var6.canTakeItems(mc.player)) {
                  int var7 = this.hotbarSlot.getValue().intValue();
                  int var8 = var6.inventory == mc.player.getInventory() ? var6.getIndex() : -1;
                  MixinCompat.Type_a var9 = MixinCompat.rAsMx_Type_a(
                     var6.id,
                     var6.getStack().isOf(Items.TOTEM_OF_UNDYING),
                     var8,
                     mc.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING),
                     this.hotbarRefill.getValue(),
                     var7,
                     mc.player.getInventory().getStack(var7 - 1).isOf(Items.TOTEM_OF_UNDYING)
                  );
                  if (var9 != null && this.LAsBoolean(var4, var9.button())) {
                     ItemStack var10 = mc.player.getInventory().getStack(var9.button());
                     if (var10.isEmpty() || var6.canInsert(var10) && var10.getCount() <= var6.getMaxItemCount(var10)) {
                        mc.interactionManager.clickSlot(var4.syncId, var9.menuSlot(), var9.button(), SlotActionType.SWAP, mc.player);
                        ClientLog.XAsVoid(
                           "HoverTotem",
                           "Swapped hovered menu slot {} to {} in container {}",
                           var9.menuSlot(),
                           var9.button() == 40 ? "offhand" : "hotbar " + (var9.button() + 1),
                           var4.syncId
                        );
                     }
                  }
               }
            }
         }
      }
   }

   private static Slot XAsSlot(ScreenHandler var0, Slot var1) {
      if (var1 != null && var1.getStack().isOf(Items.TOTEM_OF_UNDYING) && !VisualInventory.YAsBoolean(var1) && var1.id >= 0 && var1.id < var0.slots.size()) {
         Slot var2 = (Slot)var0.slots.get(var1.id);
         if (var2 != var1 && !(var1 instanceof VisualInventory.Type_m)) {
            return null;
         } else {
            return var2.inventory == var1.inventory && var2.getIndex() == var1.getIndex() && ItemStack.areEqual(var2.getStack(), var1.getStack()) ? var2 : null;
         }
      } else {
         return null;
      }
   }

   private boolean LAsBoolean(ScreenHandler var1, int var2) {
      if (var2 != 40 && VisualInventory.jAsBoolean()) {
         for (Slot var4 : var1.slots) {
            if (var4.inventory == mc.player.getInventory() && var4.getIndex() == var2) {
               Slot var5 = VisualInventory.eAsSlot(var1, var4);
               return var5.id == var4.id && !VisualInventory.YAsBoolean(var5) && ItemStack.areEqual(var5.getStack(), var4.getStack());
            }
         }

         return false;
      } else {
         return true;
      }
   }
}
