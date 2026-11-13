package com.larpclient.mixin;

import com.larpclient.ClientLog;
import com.larpclient.module.Aura;
import com.larpclient.module.Chams;
import com.larpclient.module.ESP;
import com.larpclient.module.Mace;
import com.larpclient.module.Module3DUI;
import com.larpclient.module.SpinBot;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntityRenderer.class})
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
   public LivingEntityRendererMixin() {
   }

   @Inject(
      method = {"method_62355(Lnet/minecraft/class_1309;Lnet/minecraft/class_10042;F)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectFV(T var1, S var2, float var3, CallbackInfo var4) {
      try {
         Chams var5 = Chams.mAsChams();
         int var6 = var5 == null ? 0 : var5.uAsInt(var1);
         if (var6 == 0) {
            var6 = ESP.HAsInt(var1);
         }

         if (var6 != 0) {
            var2.outlineColor = var6;
         }

         Aura var7 = Aura.fAsAura();
         Mace var8 = Mace.yAsMace();
         if ((var7 != null || var8 != null) && var1 == MinecraftClient.getInstance().player) {
            float var9 = var2.bodyYaw + var2.relativeHeadYaw;
            if (var8 != null && var8.rAsBoolean()) {
               var2.bodyYaw = var8.UAsFloat(var2.bodyYaw, var9);
            } else if (var7 != null) {
               var2.bodyYaw = var7.BAsFloat(var2.bodyYaw, var9);
            }

            var2.relativeHeadYaw = MathHelper.wrapDegrees(var9 - var2.bodyYaw);
         }

         SpinBot var11 = SpinBot.GAsSpinBot();
         if (var11 != null && var1 == MinecraftClient.getInstance().player) {
            var2.bodyYaw = var11.gAsFloat(var2.bodyYaw, var3);
            var2.relativeHeadYaw = var11.gAsFloat(var2.relativeHeadYaw);
         }

         Module3DUI.nAsVoid(var1, var2, var3);
      } catch (Exception var101) {
         ClientLog.hAsVoid("Render", "LivingEntityRendererMixin callback failed", var101);
      }
   }
}
