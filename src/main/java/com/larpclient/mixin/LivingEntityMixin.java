package com.larpclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({LivingEntity.class})
public class LivingEntityMixin {
   public LivingEntityMixin() {
   }

   @Redirect(
      method = {"method_6050()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1937;method_8406(Lnet/minecraft/class_2394;DDDDDD)V"
      )},
      require = 1
   )
   private void larpclient$redirectTickStatusEffects(
      World var1, ParticleEffect var2, double var3, double var5, double var7, double var9, double var11, double var13
   ) {
      if ((Object)this != MinecraftClient.getInstance().player) {
         var1.addParticleClient(var2, var3, var5, var7, var9, var11, var13);
      }
   }
}
