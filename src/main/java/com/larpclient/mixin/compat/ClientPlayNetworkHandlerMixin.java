package com.larpclient.mixin.compat;

import com.larpclient.FakeSpawnerController;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.ChunkData;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayNetworkHandler.class})
public abstract class ClientPlayNetworkHandlerMixin {
   @Shadow
   private ClientWorld field_3699;

   public ClientPlayNetworkHandlerMixin() {
   }

   @Inject(
      method = {"method_38539(IILnet/minecraft/class_6603;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV(int var1, int var2, ChunkData var3, CallbackInfo var4) {
      FakeSpawnerController var5 = FakeSpawnerController.ZAsMa();
      if (this.field_3699 != null && var5.XAsBoolean()) {
         WorldChunk var6 = this.field_3699.getChunk(var1, var2);
         var5.DAsVoid(var6, var6.getBlockEntities().keySet());
      }
   }
}
