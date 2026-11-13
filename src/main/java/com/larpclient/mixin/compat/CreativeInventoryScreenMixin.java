package com.larpclient.mixin.compat;

import com.larpclient.ChatMessages;
import com.larpclient.ClientLog;
import com.larpclient.FakeSpawnerController;
import com.larpclient.VisualInventory;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({CreativeInventoryScreen.class})
public abstract class CreativeInventoryScreenMixin {
   @Shadow
   private Slot field_2889;

   public CreativeInventoryScreenMixin() {
   }

   @WrapMethod(
      method = {"method_2383(Lnet/minecraft/class_1735;IILnet/minecraft/class_1713;)V"}
   )
   private void M(Slot var1, int var2, int var3, SlotActionType var4, Operation<Void> var5) {
      FakeSpawnerController var6 = FakeSpawnerController.ZAsMa();
      if (!VisualInventory.jAsBoolean()) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else if (!VisualInventory.DAsBoolean()) {
         MinecraftClient var7 = MinecraftClient.getInstance();
         Slot var8 = VisualInventory.UAsSlot(var1);
         if (var8 != null && !(var8 instanceof VisualInventory.Type_m) && var8.inventory == var7.player.getInventory()) {
            for (Slot var10 : var7.player.playerScreenHandler.slots) {
               if (var10.inventory == var8.inventory && var10.getIndex() == var8.getIndex()) {
                  var8 = var10;
                  break;
               }
            }
         }

         boolean var13 = false;
         int var14 = var4 == SlotActionType.SWAP && var8 instanceof VisualInventory.Type_m && var3 >= 0 && var3 < 9 ? VisualInventory.rAsInt(var3) : var3;
         if (var1 == this.field_2889 && var4 == SlotActionType.QUICK_MOVE) {
            for (Slot var12 : var7.player.playerScreenHandler.slots) {
               if (!var6.eAsBoolean(var7.player.playerScreenHandler, var12.id, var3, var4)) {
                  var13 = true;
                  break;
               }
            }
         }

         if (var4 != SlotActionType.PICKUP || !var6.jAsBoolean(var7.player.playerScreenHandler, var8, var3)) {
            if (!var13
               && !VisualInventory.YAsBoolean(var8)
               && (var8 == null || var8.inventory != var7.player.getInventory() || var6.eAsBoolean(var7.player.playerScreenHandler, var8.id, var14, var4))
               && (var4 != SlotActionType.PICKUP_ALL || !var6.hAsBoolean(var7.player.playerScreenHandler.getCursorStack()))) {
               if (var4 == SlotActionType.SWAP && var3 >= 0 && var3 < 9 && var8 instanceof VisualInventory.Type_m) {
                  VisualInventory.OAsVoid(var7.player.playerScreenHandler, var8, var3);
               } else {
                  VisualInventory.ZAsVoid(var8);
                  var6.MAsVoid(() -> var5.call(new Object[]{var1, var2, var3, var4}));
               }
            } else {
               ClientLog.XAsVoid(
                  "FakeSpawner",
                  "Creative input blocked type={} nativeSlot={} physical={} clearReserved={}",
                  var4,
                  var8 == null ? -1 : var8.id,
                  var8 == null ? -1 : var8.getIndex(),
                  var13
               );
               ChatMessages.RAsVoid(Text.literal("This slot is reserved or awaiting real items. Move an available stack instead."), true);
            }
         }
      }
   }
}
