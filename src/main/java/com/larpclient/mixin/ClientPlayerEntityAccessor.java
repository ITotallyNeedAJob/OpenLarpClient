package com.larpclient.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ClientPlayerEntity.class})
public interface ClientPlayerEntityAccessor {
   @Accessor("field_54156")
   PlayerInput larpLastSentInput();

   @Accessor("field_3919")
   boolean larpWasSprinting();

   @Accessor("field_3941")
   float larpYRotLast();

   @Accessor("field_3925")
   float larpXRotLast();
}
