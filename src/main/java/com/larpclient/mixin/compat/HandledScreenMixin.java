package com.larpclient.mixin.compat;

import com.larpclient.AuctionMenuText;
import com.larpclient.ChatMessages;
import com.larpclient.FakeSpawnerController;
import com.larpclient.VisualInventory;
import com.larpclient.AuctionNotifier;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Set;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({HandledScreen.class})
public abstract class HandledScreenMixin {
   @Shadow
   protected ScreenHandler field_2797;
   @Shadow
   protected Slot field_2787;
   @Shadow
   @Final
   protected Set<Slot> field_2793;

   public HandledScreenMixin() {
   }

   @ModifyVariable(
      method = {"method_2385(Lnet/minecraft/class_332;Lnet/minecraft/class_1735;II)V"},
      at = {@At("HEAD")},
      argsOnly = true
   )
   private Slot larpclient$modifyVariableIIV(Slot var1) {
      return VisualInventory.eAsSlot(this.field_2797, var1);
   }

   @Inject(
      method = {"method_64240(DD)Lnet/minecraft/class_1735;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectHandler(double var1, double var3, CallbackInfoReturnable<Slot> var5) {
      if (var5.getReturnValue() != null) {
         var5.setReturnValue(VisualInventory.tAsSlot(this.field_2797, (Slot)var5.getReturnValue(), this.field_2793));
      }
   }

   @WrapOperation(
      method = {"method_2385(Lnet/minecraft/class_332;Lnet/minecraft/class_1735;II)V", "method_71083(Lnet/minecraft/class_332;II)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1799;method_46651(I)Lnet/minecraft/class_1799;"
      )}
   )
   private ItemStack larpclient$wrapOperationIIV(ItemStack var1, int var2, Operation<ItemStack> var3) {
      return FakeSpawnerController.ZAsMa().MAsItemStack(var1, (ItemStack)var3.call(new Object[]{var1, var2}));
   }

   @Inject(
      method = {"method_64508(Lnet/minecraft/class_332;II)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectIIV(DrawContext var1, int var2, int var3, CallbackInfo var4) {
      if (this.field_2787 != null) {
         this.field_2787 = VisualInventory.eAsSlot(this.field_2797, this.field_2787);
      }
   }

   @Inject(
      method = {"method_2383(Lnet/minecraft/class_1735;IILnet/minecraft/class_1713;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(Slot var1, int var2, int var3, SlotActionType var4, CallbackInfo var5) {
      if (VisualInventory.DAsBoolean()) {
         var5.cancel();
      } else if (var4 == SlotActionType.PICKUP && FakeSpawnerController.ZAsMa().jAsBoolean(this.field_2797, var1, var3)) {
         var5.cancel();
      } else if (VisualInventory.YAsBoolean(var1)) {
         ChatMessages.RAsVoid(Text.literal("This slot is reserved or awaiting real items. Free PetEffectRenderer physical slot or finish the operation first."), true);
         var5.cancel();
      } else if (var4 == SlotActionType.PICKUP_ALL && FakeSpawnerController.ZAsMa().hAsBoolean(this.field_2797.getCursorStack())) {
         ChatMessages.RAsVoid(Text.literal("These backing stacks are reserved by placed spawners. Move visible stacks individually."), true);
         var5.cancel();
      } else if (var4 == SlotActionType.SWAP && var3 >= 0 && var3 < 9 && VisualInventory.jAsBoolean()) {
         VisualInventory.OAsVoid(this.field_2797, var1, var3);
         var5.cancel();
      } else {
         if (var4 == SlotActionType.QUICK_MOVE) {
            FakeSpawnerController.ZAsMa().iAsVoid(var1);
         }

         if (var4 == SlotActionType.SWAP && var1 != null && var1.getStack().isOf(Items.BONE) && var3 >= 0 && var3 < 9) {
            FakeSpawnerController.ZAsMa().nAsVoid(VisualInventory.rAsInt(var3));
         }

         FakeSpawnerController.ZAsMa().jAsVoid(this.field_2797, var1);
         VisualInventory.ZAsVoid(var1);
      }
   }

   @Inject(
      method = {"method_25432()V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectRemoved(CallbackInfo var1) {
      FakeSpawnerController.ZAsMa().wAsVoid(((Screen)(Object)this).getTitle(), this.field_2797);
   }

   @Inject(
      method = {"method_2383(Lnet/minecraft/class_1735;IILnet/minecraft/class_1713;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV2(Slot var1, int var2, int var3, SlotActionType var4, CallbackInfo var5) {
      if (var1 != null && var4 == SlotActionType.PICKUP && AuctionMenuText.WAsBoolean(var1.getStack())) {
         long var6 = AuctionMenuText.yAsLong();
         if (var6 > 0L) {
            AuctionNotifier.qAsVoid(var6);
         }
      }
   }
}
