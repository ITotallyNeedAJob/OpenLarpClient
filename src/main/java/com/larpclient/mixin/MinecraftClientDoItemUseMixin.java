package com.larpclient.mixin;

import com.larpclient.module.FastUse;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({MinecraftClient.class})
public class MinecraftClientDoItemUseMixin {
   public MinecraftClientDoItemUseMixin() {
   }

   @ModifyConstant(
      method = {"method_1583()V"},
      constant = {@Constant(
         intValue = 4
      )},
      require = 1,
      allow = 1
   )
   private int larpclient$modifyConstantDoItemUse(int var1) {
      return FastUse.HAsInt(var1);
   }
}
