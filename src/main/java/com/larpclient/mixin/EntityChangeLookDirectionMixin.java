package com.larpclient.mixin;

import com.larpclient.ParticleEmitter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Entity.class})
public class EntityChangeLookDirectionMixin {
   public EntityChangeLookDirectionMixin() {
   }

   @Inject(
      method = {"method_5872(DD)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectChangeLookDirection(double var1, double var3, CallbackInfo var5) {
      Entity var6 = (Entity)(Object)this;
      MinecraftClient var7 = MinecraftClient.getInstance();
      if (p5.NAsP5_Type_u() != p5.Type_u.NONE && var6 == var7.player) {
         p5.kAsVoid(var1, var3);
         var5.cancel();
      }
   }
}
