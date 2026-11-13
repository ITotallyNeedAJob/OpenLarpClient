package com.larpclient.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({LivingEntity.class})
public interface LivingEntityAccessor {
   @Accessor("field_6273")
   int larpAttackStrengthTicker();

   @Accessor("field_6273")
   void larpAttackStrengthTicker(int var1);
}
