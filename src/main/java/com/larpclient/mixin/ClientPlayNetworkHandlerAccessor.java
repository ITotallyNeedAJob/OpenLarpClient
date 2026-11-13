package com.larpclient.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ClientPlayNetworkHandler.class})
public interface ClientPlayNetworkHandlerAccessor {
   @Accessor("field_19144")
   int larpServerChunkRadius();
}
