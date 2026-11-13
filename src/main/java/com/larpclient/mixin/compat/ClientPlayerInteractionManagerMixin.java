package com.larpclient.mixin.compat;

import com.larpclient.AuctionConfirmScreen;
import com.larpclient.FakeSpawnerController;
import com.larpclient.FakeSpawnerTransaction;
import com.larpclient.Sha256Digest;
import com.larpclient.SpawnerLootScreen;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientPlayerInteractionManager.class})
public abstract class ClientPlayerInteractionManagerMixin {
   @Unique
   private int m;

   public ClientPlayerInteractionManagerMixin() {
   }

   @Inject(
      method = {"method_2909(Lnet/minecraft/class_1799;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectIV(ItemStack var1, int var2, CallbackInfo var3) {
      if (Sha256Digest.oAsBoolean(var1)) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"method_2915(Lnet/minecraft/class_1799;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(ItemStack var1, CallbackInfo var2) {
      if (Sha256Digest.oAsBoolean(var1)) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"method_2919(Lnet/minecraft/class_1657;Lnet/minecraft/class_1268;)Lnet/minecraft/class_1269;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler(PlayerEntity var1, Hand var2, CallbackInfoReturnable<ActionResult> var3) {
      if (var1 == MinecraftClient.getInstance().player) {
         FakeSpawnerController var4 = FakeSpawnerController.ZAsMa();
         if (var4.vAsBoolean(var2 == Hand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40) || FakeSpawnerTransaction.aAsBoolean()) {
            var3.setReturnValue(ActionResult.PASS);
         }
      }
   }

   @WrapMethod(
      method = {"method_2906(IIILnet/minecraft/class_1713;Lnet/minecraft/class_1657;)V"}
   )
   private void G(int var1, int var2, int var3, SlotActionType var4, PlayerEntity var5, Operation<Void> var6) {
      if (var1 >= 0 && var5 == MinecraftClient.getInstance().player) {
         if (FakeSpawnerController.ZAsMa().AAsBoolean(var1, var2, var3, var4)) {
            FakeSpawnerController.ZAsMa().MAsVoid(() -> var6.call(new Object[]{var1, var2, var3, var4, var5}));
         }
      } else {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
      }
   }

   @Inject(
      method = {"method_2896(Lnet/minecraft/class_746;Lnet/minecraft/class_1268;Lnet/minecraft/class_3965;)Lnet/minecraft/class_1269;"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectHandler(ClientPlayerEntity var1, Hand var2, BlockHitResult var3, CallbackInfoReturnable<ActionResult> var4) {
      this.m = var1.getStackInHand(var2).getCount();
   }

   @Inject(
      method = {"method_2910(Lnet/minecraft/class_2338;Lnet/minecraft/class_2350;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectZ(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      FakeSpawnerController var4 = FakeSpawnerController.ZAsMa();
      if (!var4.KAsBoolean(var1) && (!var4.vAsBoolean(var1) || !var4.lAsBoolean(var1))) {
         if (var4.XAsBoolean() && var4.sAsBoolean(var1)) {
            var3.setReturnValue(true);
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(
      method = {"method_2902(Lnet/minecraft/class_2338;Lnet/minecraft/class_2350;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectZ2(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      FakeSpawnerController var4 = FakeSpawnerController.ZAsMa();
      boolean var5 = var4.vAsBoolean(var1);
      if (!var4.KAsBoolean(var1) && (!var5 || !var4.lAsBoolean(var1))) {
         if (var4.XAsBoolean() && var4.KAsBoolean(var1, var2)) {
            if (var5 && !var4.vAsBoolean(var1)) {
               MinecraftClient.getInstance().worldRenderer.scheduleChunkRenders3x3x3(var1.getX() >> 4, var1.getY() >> 4, var1.getZ() >> 4);
            }

            var3.setReturnValue(true);
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(
      method = {"method_2896(Lnet/minecraft/class_746;Lnet/minecraft/class_1268;Lnet/minecraft/class_3965;)Lnet/minecraft/class_1269;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler2(ClientPlayerEntity var1, Hand var2, BlockHitResult var3, CallbackInfoReturnable<ActionResult> var4) {
      FakeSpawnerController var5 = FakeSpawnerController.ZAsMa();
      if (var5.XAsBoolean() && var1 != null && var3 != null) {
         if (FakeSpawnerTransaction.aAsBoolean() || var5.vAsBoolean(var2 == Hand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40)) {
            var4.setReturnValue(ActionResult.PASS);
            return;
         }

         BlockPos var6 = var3.getBlockPos().toImmutable();
         int var7 = var2 == Hand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40;
         if (var5.vAsBoolean(var6)) {
            if (var5.lAsBoolean(var6)) {
               var4.setReturnValue(ActionResult.SUCCESS);
               return;
            }

            if (!var5.lAsBoolean(var6, var1.getStackInHand(var2), var7, var1.isSneaking() || MinecraftClient.getInstance().options.sneakKey.isPressed())) {
               MinecraftClient.getInstance().execute(() -> var5.lAsVoid(var6));
            } else if (var1.isSneaking() || MinecraftClient.getInstance().options.sneakKey.isPressed()) {
               var5.mAsVoid(var6.offset(var3.getSide()));
            }

            var4.setReturnValue(ActionResult.SUCCESS);
            return;
         }

         if (!var5.DAsBoolean(var1.getStackInHand(var2)) && var5.LAsNa(var1.getStackInHand(var2).getItem()) != null) {
            ItemPlacementContext var9 = new ItemPlacementContext(var1, var2, var1.getStackInHand(var2), var3);
            if (var9.canPlace()) {
               var5.eAsVoid(var9.getBlockPos().toImmutable());
            }

            return;
         }

         if (var5.DAsBoolean(var1.getStackInHand(var2))) {
            ItemPlacementContext var8 = new ItemPlacementContext(var1, var2, var1.getStackInHand(var2), var3);
            if (var8.canPlace()) {
               var5.gAsVoid(
                  var8.getBlockPos().toImmutable(),
                  var1.getStackInHand(var2),
                  var7,
                  var1.isSneaking() || MinecraftClient.getInstance().options.sneakKey.isPressed()
               );
            }

            return;
         }
      }
   }

   @Inject(
      method = {"method_2896(Lnet/minecraft/class_746;Lnet/minecraft/class_1268;Lnet/minecraft/class_3965;)Lnet/minecraft/class_1269;"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectHandler3(ClientPlayerEntity var1, Hand var2, BlockHitResult var3, CallbackInfoReturnable<ActionResult> var4) {
      FakeSpawnerController var5 = FakeSpawnerController.ZAsMa();
      if (((ActionResult)var4.getReturnValue()).isAccepted()) {
         var5.WAsVoid();
      }

      boolean var6 = var5.DAsBoolean(var1.getStackInHand(var2));
      if (var6
         && ((ActionResult)var4.getReturnValue()).isAccepted()
         && this.m > 1
         && var1.getStackInHand(var2).getCount() == this.m - 1
         && var5.DAsBoolean(var1.getStackInHand(var2))) {
         var5.WAsVoid(var2);
      }
   }

   @Inject(
      method = {"method_2899(Lnet/minecraft/class_2338;)Z"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectZ(BlockPos var1, CallbackInfoReturnable<Boolean> var2) {
      FakeSpawnerController var3 = FakeSpawnerController.ZAsMa();
      if (var3.XAsBoolean() && (Boolean)var2.getReturnValue()) {
         var3.hAsVoid(var1);
      }
   }

   @WrapOperation(
      method = {"method_2910(Lnet/minecraft/class_2338;Lnet/minecraft/class_2350;)Z", "method_2902(Lnet/minecraft/class_2338;Lnet/minecraft/class_2350;)Z"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_638;method_8320(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2680;"
      )}
   )
   private BlockState larpclient$wrapOperationZ(ClientWorld var1, BlockPos var2, Operation<BlockState> var3) {
      return FakeSpawnerController.ZAsMa().ZAsBlockState(var1, var2, (BlockState)var3.call(new Object[]{var1, var2}));
   }

   @WrapOperation(
      method = {"method_2899(Lnet/minecraft/class_2338;)Z"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1937;method_8320(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2680;"
      )}
   )
   private BlockState larpclient$wrapOperationZ(World var1, BlockPos var2, Operation<BlockState> var3) {
      return FakeSpawnerController.ZAsMa().ZAsBlockState(var1, var2, (BlockState)var3.call(new Object[]{var1, var2}));
   }

   @Inject(
      method = {"method_2906(IIILnet/minecraft/class_1713;Lnet/minecraft/class_1657;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(int var1, int var2, int var3, SlotActionType var4, PlayerEntity var5, CallbackInfo var6) {
      if (MinecraftClient.getInstance().currentScreen instanceof SpawnerLootScreen
         || MinecraftClient.getInstance().currentScreen instanceof AuctionConfirmScreen) {
         var6.cancel();
      } else if (var1 < 0 && var5 != null) {
         var5.currentScreenHandler.onSlotClick(var2, var3, var4, var5);
         var6.cancel();
      }
   }
}
