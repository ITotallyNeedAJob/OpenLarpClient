package com.larpclient.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ClientPlayerInteractionManager.class})
public interface ClientPlayerInteractionManagerAccessor {
   @Invoker("method_2911")
   void larpSyncCarriedItem();

   @Accessor("field_3716")
   int larpDestroyDelay();

   @Accessor("field_3716")
   void larpDestroyDelay(int var1);
}
