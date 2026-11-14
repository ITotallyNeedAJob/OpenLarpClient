package com.larpclient.mixin;

import com.larpclient.RoundedShapePipeline;
import com.larpclient.XTexture;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({GuiRenderer.class})
public abstract class GuiRendererMixin {
   public GuiRendererMixin() {
   }

   @WrapOperation(
      method = {"method_71287(Lnet/minecraft/class_11244;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_11244;comp_4055()Lcom/mojang/blaze3d/pipeline/RenderPipeline;"
      )}
   )
   private RenderPipeline larpclient$wrapOperationV(SimpleGuiElementRenderState var1, Operation<RenderPipeline> var2) {
      RenderPipeline var3 = (RenderPipeline)var2.call(new Object[]{var1});
      if (xt.rAsXt_Type_s() == null) {
         return var3;
      } else {
         RenderPipeline var4 = RoundedShapePipeline.RAsRenderPipeline(var3);
         return var4 != null ? var4 : var3;
      }
   }

   @WrapOperation(
      method = {"method_71287(Lnet/minecraft/class_11244;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_11244;method_70917(Lnet/minecraft/class_4588;)V"
      )}
   )
   private void larpclient$wrapOperationV(SimpleGuiElementRenderState var1, VertexConsumer var2, Operation<Void> var3) {
      xt.Type_s var4 = xt.rAsXt_Type_s();
      if (var4 == null) {
         var3.call(new Object[]{var1, var2});
      } else {
         RenderPipeline var5 = var1.pipeline();
         xt.Type_d var6 = !RoundedShapePipeline.PAsBoolean(var5)
            ? xt.Type_d.PLAIN
            : (RoundedShapePipeline.RAsRenderPipeline(var5) != null ? xt.Type_d.SIZED : xt.Type_d.CORNERS);
         xt.Type_m var7 = xt.IAsXt_Type_m(var2, var4, var5.getVertexFormatMode() == DrawMode.QUADS, var1.scissorArea(), var6);

         try {
            var3.call(new Object[]{var1, var7});
         } finally {
            var7.uAsVoid();
         }
      }
   }

   @WrapOperation(
      method = {"method_71287(Lnet/minecraft/class_11244;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_11244;comp_4069()Lnet/minecraft/class_8030;"
      )}
   )
   private ScreenRect larpclient$wrapOperationV2(SimpleGuiElementRenderState var1, Operation<ScreenRect> var2) {
      xt.Type_s var3 = xt.rAsXt_Type_s();
      ScreenRect var4 = (ScreenRect)var2.call(new Object[]{var1});
      if (var3 != null && var4 != null) {
         Window var5 = MinecraftClient.getInstance().getWindow();
         return var3.mAsScreenRect(var4, var5.getScaledWidth(), var5.getScaledHeight());
      } else {
         return var4;
      }
   }
}
