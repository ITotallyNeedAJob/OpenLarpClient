package com.larpclient.mixin;

import com.larpclient.ClientLog;
import com.larpclient.CosmeticSubmissionHandler;
import com.larpclient.module.SelfVisuals;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({PlayerEntityRenderer.class})
public class PlayerEntityRendererRenderArmMixin {
   public PlayerEntityRendererRenderArmMixin() {
   }

   @Redirect(
      method = {"method_23205(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_2960;Lnet/minecraft/class_630;Z)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_11659;method_73491(Lnet/minecraft/class_630;Lnet/minecraft/class_4587;Lnet/minecraft/class_1921;IILnet/minecraft/class_1058;)V"
      )}
   )
   private void larpclient$redirectZV(OrderedRenderCommandQueue var1, ModelPart var2, MatrixStack var3, RenderLayer var4, int var5, int var6, Sprite var7) {
      try {
         if (CosmeticSubmissionHandler.TAsBoolean(var2, var3, var1, var4, var5, var6, var7)) {
            return;
         }
      } catch (RuntimeException var10) {
         ClientLog.hAsVoid("SelfVisuals", "First-person arm stage=material shader=model/material failed; restoring vanilla arms", var10);
         SelfVisuals var9 = SelfVisuals.zAsSelfVisuals();
         if (var9 != null) {
            var9.disable();
         }
      }

      var1.submitModelPart(var2, var3, var4, var5, var6, var7);
   }
}
