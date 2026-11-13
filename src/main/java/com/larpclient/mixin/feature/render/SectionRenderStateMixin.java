package com.larpclient.mixin.feature.render;

import com.larpclient.WetReflectionEffect;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import net.minecraft.client.render.SectionRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({SectionRenderState.class})
public abstract class SectionRenderStateMixin {
   public SectionRenderStateMixin() {
   }

   @Redirect(
      method = {"method_72170(Lnet/minecraft/class_11531;Lnet/minecraft/class_12137;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V"
      )}
   )
   private void larpclient$redirectV(RenderPass var1, RenderPipeline var2) {
      RenderPipeline var3 = WetReflectionEffect.tAsRenderPipeline(var2);
      var1.setPipeline(var3);
      WetReflectionEffect.EAsVoid(var1, var3);
   }
}
