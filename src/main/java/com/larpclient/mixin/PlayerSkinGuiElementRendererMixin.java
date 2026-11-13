package com.larpclient.mixin;

import com.larpclient.AccountCosmeticsRenderer;
import net.minecraft.client.gui.render.PlayerSkinGuiElementRenderer;
import net.minecraft.client.gui.render.SpecialGuiElementRenderer;
import net.minecraft.client.gui.render.state.special.PlayerSkinGuiElementRenderState;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerSkinGuiElementRenderer.class})
public abstract class PlayerSkinGuiElementRendererMixin extends SpecialGuiElementRenderer<PlayerSkinGuiElementRenderState> {
   private static final String Q = "renderToTexture(Lnet/minecraft/client/gui/render/state/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V";
   @Unique
   private final Matrix4f O = new Matrix4f();

   protected PlayerSkinGuiElementRendererMixin(Immediate var1) {
      super(var1);
   }

   @Inject(
      method = {"method_70912(Lnet/minecraft/class_11255;Lnet/minecraft/class_4587;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV(PlayerSkinGuiElementRenderState var1, MatrixStack var2, CallbackInfo var3) {
      this.O.set(var2.peek().getPositionMatrix());
   }

   @Inject(
      method = {"method_70912(Lnet/minecraft/class_11255;Lnet/minecraft/class_4587;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_4597$class_4598;method_22993()V",
         shift = Shift.AFTER
      )}
   )
   private void larpclient$injectV2(PlayerSkinGuiElementRenderState var1, MatrixStack var2, CallbackInfo var3) {
      AccountCosmeticsRenderer.TAsVoid(var1, this.O, var2, super.vertexConsumers);
   }
}
