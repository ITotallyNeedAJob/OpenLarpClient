package com.larpclient.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({MinecraftClient.class})
public interface MinecraftClientAccessor {
   @Accessor("field_1771")
   int larpAttackDelay();

   @Accessor("field_1771")
   void larpAttackDelay(int var1);

   @Accessor("field_1752")
   int larpUseDelay();

   @Accessor("field_1752")
   void larpUseDelay(int var1);
}
