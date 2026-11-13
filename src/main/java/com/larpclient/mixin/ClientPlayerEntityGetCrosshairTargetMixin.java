package com.larpclient.mixin;

import com.larpclient.module.AntiTrap;
import com.larpclient.module.IgnoreEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.function.Predicate;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ClientPlayerEntity.class})
public class ClientPlayerEntityGetCrosshairTargetMixin {
   public ClientPlayerEntityGetCrosshairTargetMixin() {
   }

   @ModifyExpressionValue(
      method = {"method_76763(Lnet/minecraft/class_1297;DDF)Lnet/minecraft/class_239;"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/class_1301;field_52443:Ljava/util/function/Predicate;"
      )}
   )
   private static Predicate<Entity> larpclient$modifyExpressionValueHandler(Predicate<Entity> var0) {
      return !MinecraftClient.getInstance().options.useKey.isPressed()
         ? var0
         : var0.and(var0x -> !AntiTrap.TAsBoolean(var0x) && !IgnoreEntity.yAsBoolean(var0x));
   }

   @ModifyExpressionValue(
      method = {"method_76762(FLnet/minecraft/class_1297;)Lnet/minecraft/class_239;"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/class_1301;field_52443:Ljava/util/function/Predicate;"
      )}
   )
   private Predicate<Entity> larpclient$modifyExpressionValueHandler2(Predicate<Entity> var1) {
      return larpclient$modifyExpressionValueHandler(var1);
   }
}
