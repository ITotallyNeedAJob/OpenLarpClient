package com.larpclient.mixin;

import com.larpclient.module.Backtrack;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net/minecraft/class_11980$class_11981"}
)
public abstract class EntryMixin {
   @Shadow
   @Final
   private PacketListener comp_4821;
   @Shadow
   @Final
   private Packet<?> comp_4822;

   public EntryMixin() {
   }

   @Shadow
   public abstract void method_74450();

   @Inject(
      method = {"method_74450()V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectApply(CallbackInfo var1) {
      if (!Backtrack.WAsBoolean() && Backtrack.MAsBoolean(this.comp_4822, this.comp_4821, this::method_74450)) {
         var1.cancel();
      }
   }
}
