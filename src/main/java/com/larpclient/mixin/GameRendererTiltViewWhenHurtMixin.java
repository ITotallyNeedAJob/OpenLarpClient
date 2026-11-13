package com.larpclient.mixin;

import com.larpclient.module.GUIAnimation;
import com.larpclient.module.Removals;
import com.larpclient.module.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GameRenderer.class})
public class GameRendererTiltViewWhenHurtMixin {
   public GameRendererTiltViewWhenHurtMixin() {
   }

   @Inject(
      method = {"method_3198(Lnet/minecraft/class_4587;F)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectFV(MatrixStack var1, float var2, CallbackInfo var3) {
      if (Removals.xAsBoolean("hurtCam")) {
         var3.cancel();
      }
   }

   @Redirect(
      method = {"method_3188(Lnet/minecraft/class_9779;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_3532;method_16439(FFF)F"
      )},
      require = 1,
      allow = 1
   )
   private float larpclient$redirectV(float var1, float var2, float var3) {
      return Removals.KAsBoolean() ? 0.0F : MathHelper.lerp(var1, var2, var3);
   }

   @Redirect(
      method = {"method_3188(Lnet/minecraft/class_9779;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_746;method_66279(Lnet/minecraft/class_6880;F)F"
      )},
      require = 1,
      allow = 1
   )
   private float larpclient$redirectV(ClientPlayerEntity var1, RegistryEntry<StatusEffect> var2, float var3) {
      return var2.equals(StatusEffects.NAUSEA) && Removals.KAsBoolean() ? 0.0F : var1.getEffectFadeFactor(var2, var3);
   }

   @Inject(
      method = {"method_3196(Lnet/minecraft/class_4184;FZ)F"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectFZF(Camera var1, float var2, boolean var3, CallbackInfoReturnable<Float> var4) {
      GameRenderer var5 = (GameRenderer)(Object)this;
      if (!var5.isRenderingPanorama()) {
         if (Removals.xAsBoolean("fov")) {
            var4.setReturnValue(var3 ? ((Integer)MinecraftClient.getInstance().options.getFov().getValue()).floatValue() : 70.0F);
         } else {
            GUIAnimation var6 = GUIAnimation.kAsGUIAnimation();
            if (var6 != null && var3) {
               var4.setReturnValue(var6.qAsFloat((Float)var4.getReturnValue()));
            }

            var4.setReturnValue(Zoom.lAsFloat((Float)var4.getReturnValue(), var5.isRenderingPanorama()));
         }
      }
   }
}
