package com.larpclient.mixin;

import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin({GameOptions.class})
public abstract class GameOptionsMixin {
   public GameOptionsMixin() {
   }

   @ModifyArg(
      method = {"<init>"},
      slice = {@Slice(
         from = @At(
            value = "CONSTANT",
            args = {"stringValue=options.guiScale"}
         ),
         to = @At(
            value = "FIELD",
            target = "Lnet/minecraft/class_315;field_1868:Lnet/minecraft/class_7172;"
         )
      )},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_7172;<init>(Ljava/lang/String;Lnet/minecraft/class_7172$class_7277;Lnet/minecraft/class_7172$class_7303;Lnet/minecraft/class_7172$class_7178;Ljava/lang/Object;Ljava/util/function/Consumer;)V"
      )},
      index = 4,
      require = 1,
      allow = 1
   )
   private Object larpclient$modifyArgInit(Object var1) {
      return 2;
   }
}
