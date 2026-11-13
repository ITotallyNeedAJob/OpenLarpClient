package com.larpclient.mixin;

import com.larpclient.CashFanRenderer;
import com.larpclient.ClientLog;
import com.larpclient.RiflePropRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HeldItemFeatureRenderer.class})
public abstract class HeldItemFeatureRendererRenderMixin {
   public HeldItemFeatureRendererRenderMixin() {
   }

   @Inject(
      method = {"method_17162(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_10426;FF)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectFFV(
      MatrixStack var1, OrderedRenderCommandQueue var2, int var3, ArmedEntityRenderState var4, float var5, float var6, CallbackInfo var7
   ) {
      if (var4 instanceof PlayerEntityRenderState var8 && ((FeatureRenderer)(Object)this).getContextModel() instanceof PlayerEntityModel var9) {
         try {
            if (CashFanRenderer.WAsBoolean(var9, var8, var1, var2, var3)) {
               var7.cancel();
               return;
            }
         } catch (RuntimeException var12) {
            ClientLog.hAsVoid("Emotes", "Cash fan submission failed; held items stay visible", var12);
         }

         try {
            if (RiflePropRenderer.vAsBoolean(var9, var8, var1, var2, var3)) {
               var7.cancel();
            }
         } catch (RuntimeException var11) {
            ClientLog.hAsVoid("Emotes", "Rifle submission failed; held items stay visible", var11);
         }
      }
   }
}
