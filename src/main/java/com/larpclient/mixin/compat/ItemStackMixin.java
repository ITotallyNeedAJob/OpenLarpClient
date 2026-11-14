package com.larpclient.mixin.compat;

import com.larpclient.AuctionMenuText;
import com.larpclient.FakeSpawnerController;
import com.larpclient.Sha256Digest;
import com.larpclient.SpawnerEntityCatalog;
import com.larpclient.RifleJoint;
import com.larpclient.WaterOverlay;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ItemStack.class})
public abstract class ItemStackMixin implements Sha256Digest.Type_c, w0.Type_l {
   @Unique
   private boolean R;
   @Unique
   private boolean y;
   @Unique
   private boolean E;

   public ItemStackMixin() {
   }

   @Override
   public boolean fakeSpawner$isPreview() {
      return this.E;
   }

   @Override
   public void fakeSpawner$setPreview(boolean var1) {
      this.E = var1;
   }

   @Override
   public boolean fakeElytra$isRealPurchase() {
      return this.R;
   }

   @Override
   public void fakeElytra$setRealPurchase(boolean var1) {
      this.R = var1;
   }

   @Override
   public boolean fakeSpawner$isWithdrawnBone() {
      return this.y;
   }

   @Override
   public void fakeSpawner$setWithdrawnBone(boolean var1) {
      this.y = var1;
   }

   @Inject(
      method = {"method_7972()Lnet/minecraft/class_1799;", "method_46651(I)Lnet/minecraft/class_1799;", "method_7971(I)Lnet/minecraft/class_1799;"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectHandler(CallbackInfoReturnable<ItemStack> var1) {
      Sha256Digest.RAsVoid((ItemStack)var1.getReturnValue(), this.R);
      w0.HAsVoid((ItemStack)var1.getReturnValue(), this.y);
      if (this.E) {
         Sha256Digest.fAsItemStack((ItemStack)var1.getReturnValue());
      }
   }

   @Inject(
      method = {"method_7964()Lnet/minecraft/class_2561;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler2(CallbackInfoReturnable<Text> var1) {
      FakeSpawnerController var2 = FakeSpawnerController.ZAsMa();
      ItemStack var3 = (ItemStack)(Object)this;
      if (var2.DAsBoolean(var3)) {
         var1.setReturnValue(var2.RAsText(var3));
      }
   }

   @Inject(
      method = {"method_7958()Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHasGlint(CallbackInfoReturnable<Boolean> var1) {
      FakeSpawnerController var2 = FakeSpawnerController.ZAsMa();
      if (var2.DAsBoolean((ItemStack)(Object)this)) {
         var1.setReturnValue(var2.qAsBoolean());
      }
   }

   @Inject(
      method = {"method_7950(Lnet/minecraft/class_1792$class_9635;Lnet/minecraft/class_1657;Lnet/minecraft/class_1836;)Ljava/util/List;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler(TooltipContext var1, PlayerEntity var2, TooltipType var3, CallbackInfoReturnable<List<Text>> var4) {
      ItemStack var5 = (ItemStack)(Object)this;
      FakeSpawnerController var6 = FakeSpawnerController.ZAsMa();
      if (var6.DAsBoolean(var5)) {
         ArrayList var7 = new ArrayList();
         var7.add(var6.RAsText(var5));
         var7.addAll(var6.SAsList(var5, !rj.NAsBoolean(), var3 != null && var3.isAdvanced()));
         var4.setReturnValue(var7);
      } else if (var6.lAsBoolean(var5)) {
         var4.setReturnValue(var6.BAsList(var5, false));
      }
   }

   @Inject(
      method = {"method_7950(Lnet/minecraft/class_1792$class_9635;Lnet/minecraft/class_1657;Lnet/minecraft/class_1836;)Ljava/util/List;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectHandler2(TooltipContext var1, PlayerEntity var2, TooltipType var3, CallbackInfoReturnable<List<Text>> var4) {
      ItemStack var5 = (ItemStack)(Object)this;
      FakeSpawnerController var6 = FakeSpawnerController.ZAsMa();
      if (var6.XAsBoolean() && !AuctionMenuText.nAsBoolean(var5) && !rj.NAsBoolean()) {
         long var7 = AuctionMenuText.WAsBoolean(var5)
            ? AuctionMenuText.yAsLong()
            : (!SpawnerEntityCatalog.oAsBoolean(var5.getItem()) && !var5.contains(DataComponentTypes.CONTAINER) ? 0L : var6.QAsLong(var5));
         if (var7 > 0L) {
            ArrayList var9 = new ArrayList((Collection)var4.getReturnValue());
            var9.removeIf(var0 -> {
               String var1x = ((Text)var0).getString().trim().toLowerCase(Locale.ROOT);
               return var1x.startsWith("worth:") || var1x.startsWith("~$") || var1x.contains("$");
            });
            var9.add(Math.min(1, var9.size()), var6.OAsText(var7));
            var4.setReturnValue(var9);
         }
      }
   }
}
