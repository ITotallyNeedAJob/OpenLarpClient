package com.larpclient.mixin.compat;

import com.larpclient.DonutAuctionApi;
import com.larpclient.FakeSpawnerController;
import com.larpclient.FakeSpawnerTransaction;
import com.larpclient.RealStackExemptions;
import com.larpclient.Sha256Digest;
import com.larpclient.ShopPurchaseController;
import com.larpclient.AuctionNotifier;
import com.larpclient.r2;
import com.larpclient.w0;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.ProfilelessChatMessageS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayNetworkHandler.class})
public abstract class ClientPlayNetworkHandlerOnItemPickupAnimationMixin {
   @Unique
   private final Map<Integer, ItemStack> Y = new HashMap<>();

   public ClientPlayNetworkHandlerOnItemPickupAnimationMixin() {
   }

   @WrapOperation(
      method = {"method_11150(Lnet/minecraft/class_2775;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_638;method_8486(DDDLnet/minecraft/class_3414;Lnet/minecraft/class_3419;FFZ)V"
      )}
   )
   private void larpclient$wrapOperationV(
      ClientWorld var1,
      double var2,
      double var4,
      double var6,
      SoundEvent var8,
      SoundCategory var9,
      float var10,
      float var11,
      boolean var12,
      Operation<Void> var13,
      ItemPickupAnimationS2CPacket var14
   ) {
      ClientPlayerEntity var15 = MinecraftClient.getInstance().player;
      boolean var16 = var15 != null
         && var14.getCollectorEntityId() == var15.getId()
         && var1.getEntityById(var14.getEntityId()) instanceof ItemEntity var17
         && FakeSpawnerController.ZAsMa().FAsBoolean(var17);
      if (!var16) {
         var13.call(new Object[]{var1, var2, var4, var6, var8, var9, var10, var11, var12});
      }
   }

   @Inject(
      method = {"method_11150(Lnet/minecraft/class_2775;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV(ItemPickupAnimationS2CPacket var1, CallbackInfo var2) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      if (var3.isOnThread()
         && var3.player != null
         && var3.world != null
         && var1.getCollectorEntityId() == var3.player.getId()
         && var3.world.getEntityById(var1.getEntityId()) instanceof ItemEntity var4) {
         r2.tAsVoid(var4);
         if (Sha256Digest.RAsBoolean(var4.getStack())) {
            RealStackExemptions.SAsVoid(var4.getStack(), var1.getStackAmount());
         }
      }
   }

   @Inject(
      method = {"method_11153(Lnet/minecraft/class_2649;)V", "method_11109(Lnet/minecraft/class_2653;)V", "method_61188(Lnet/minecraft/class_9835;)V", "method_61187(Lnet/minecraft/class_9834;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV(CallbackInfo var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2.isOnThread()) {
         this.Y.clear();
         if (var2.player != null) {
            for (int var3 = 0; var3 < var2.player.getInventory().size(); var3++) {
               ItemStack var4 = var2.player.getInventory().getStack(var3);
               if (w0.dAsBoolean(var4)) {
                  this.Y.put(var3, var4.copy());
               }
            }

            ItemStack var5 = var2.player.currentScreenHandler.getCursorStack();
            if (w0.dAsBoolean(var5)) {
               this.Y.put(-1, var5.copy());
            }
         }
      }
   }

   @Unique
   private void w() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1.isOnThread() && var1.player != null) {
         for (Entry var3 : this.Y.entrySet()) {
            ItemStack var4 = ((Integer)var3.getKey()) == -1
               ? var1.player.currentScreenHandler.getCursorStack()
               : var1.player.getInventory().getStack((Integer)var3.getKey());
            if (ItemStack.areItemsAndComponentsEqual(var4, (ItemStack)var3.getValue()) && var4.getCount() <= ((ItemStack)var3.getValue()).getCount()) {
               w0.HAsVoid(var4, true);
            }
         }

         this.Y.clear();
      }
   }

   @Inject(
      method = {"method_11153(Lnet/minecraft/class_2649;)V", "method_11109(Lnet/minecraft/class_2653;)V", "method_61188(Lnet/minecraft/class_9835;)V", "method_61187(Lnet/minecraft/class_9834;)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectV2(CallbackInfo var1) {
      this.w();
      FakeSpawnerController.ZAsMa().gAsVoid();
      FakeSpawnerTransaction.cAsVoid();
      FakeSpawnerController.ZAsMa().PAsVoid();
      ShopPurchaseController.YAsVoid();
      RealStackExemptions.yAsVoid();
   }

   @Inject(
      method = {"method_43596(Lnet/minecraft/class_7439;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(GameMessageS2CPacket var1, CallbackInfo var2) {
      if (FakeSpawnerController.ZAsMa().XAsBoolean()) {
         DonutAuctionApi.ZAsA6().IAsVoid(var1.content());
      }

      if (ShopPurchaseController.pAsBoolean(var1.content(), var1.overlay()) || AuctionNotifier.KAsBoolean(var1.content(), var1.overlay())) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"method_45724(Lnet/minecraft/class_7827;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(ProfilelessChatMessageS2CPacket var1, CallbackInfo var2) {
      if (FakeSpawnerController.ZAsMa().XAsBoolean()) {
         DonutAuctionApi.ZAsA6().IAsVoid(var1.message());
      }

      if (ShopPurchaseController.pAsBoolean(var1.message(), false) || AuctionNotifier.KAsBoolean(var1.message(), false)) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"method_34076(Lnet/minecraft/class_5894;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(OverlayMessageS2CPacket var1, CallbackInfo var2) {
      if (ShopPurchaseController.pAsBoolean(var1.text(), true) || AuctionNotifier.KAsBoolean(var1.text(), true)) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"method_17587(Lnet/minecraft/class_3944;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(OpenScreenS2CPacket var1, CallbackInfo var2) {
      if (ShopPurchaseController.TAsBoolean(var1)) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"method_11146(Lnet/minecraft/class_2767;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(PlaySoundS2CPacket var1, CallbackInfo var2) {
      ClientPlayerEntity var3 = MinecraftClient.getInstance().player;
      if (ShopPurchaseController.DAsBoolean() && var3 != null && var3.squaredDistanceTo(var1.getX(), var1.getY(), var1.getZ()) <= 16.0) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"method_11125(Lnet/minecraft/class_2765;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(PlaySoundFromEntityS2CPacket var1, CallbackInfo var2) {
      ClientPlayerEntity var3 = MinecraftClient.getInstance().player;
      if (ShopPurchaseController.DAsBoolean() && var3 != null && var1.getEntityId() == var3.getId()) {
         var2.cancel();
      }
   }
}
