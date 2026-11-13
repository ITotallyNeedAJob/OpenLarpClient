package com.larpclient.mixin;

import com.larpclient.ChunkLightCapture;
import com.larpclient.module.MotionScanner;
import com.larpclient.module.PrimeChunkFinder;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.LightUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayNetworkHandler.class})
public abstract class ClientPlayNetworkHandlerOnChunkDataMixin {
   @Shadow
   private ClientWorld field_3699;

   public ClientPlayNetworkHandlerOnChunkDataMixin() {
   }

   @Inject(
      method = {"method_11128(Lnet/minecraft/class_2672;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV(ChunkDataS2CPacket var1, CallbackInfo var2) {
      PrimeChunkFinder.mAsVoid(this.field_3699, var1.getChunkX(), var1.getChunkZ(), var1.getLightData(), true);
      ChunkLightCapture.RAsVoid(this.field_3699, var1.getChunkX(), var1.getChunkZ(), var1.getLightData());
      MotionScanner.oAsVoid(this.field_3699, var1.getChunkX(), var1.getChunkZ(), var1.getLightData());
   }

   @Inject(
      method = {"method_11143(Lnet/minecraft/class_2676;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV(LightUpdateS2CPacket var1, CallbackInfo var2) {
      PrimeChunkFinder.mAsVoid(this.field_3699, var1.getChunkX(), var1.getChunkZ(), var1.getData(), false);
      ChunkLightCapture.RAsVoid(this.field_3699, var1.getChunkX(), var1.getChunkZ(), var1.getData());
      MotionScanner.oAsVoid(this.field_3699, var1.getChunkX(), var1.getChunkZ(), var1.getData());
   }
}
