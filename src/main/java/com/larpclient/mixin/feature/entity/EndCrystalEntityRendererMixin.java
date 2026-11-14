package com.larpclient.mixin.feature.entity;

import com.larpclient.CrystalChams;
import com.larpclient.WidgetJob;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.command.ModelCommandRenderer.CrumblingOverlayCommand;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.state.EndCrystalEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EndCrystalEntityRenderer.class})
public class EndCrystalEntityRendererMixin {
   public EndCrystalEntityRendererMixin() {
   }

   @Redirect(
      method = {"method_3908(Lnet/minecraft/class_10014;Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;Lnet/minecraft/class_12075;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_4587;method_22905(FFF)V"
      )}
   )
   private void larpclient$redirectV(MatrixStack var1, float var2, float var3, float var4) {
      CrystalChams var5 = CrystalChams.hAsCrystalChams();
      float var6 = var5 == null ? 1.0F : var5.SAsFloat();
      var1.scale(var2 * var6, var3 * var6, var4 * var6);
   }

   @Redirect(
      method = {"method_3908(Lnet/minecraft/class_10014;Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;Lnet/minecraft/class_12075;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_11659;method_73489(Lnet/minecraft/class_3879;Ljava/lang/Object;Lnet/minecraft/class_4587;Lnet/minecraft/class_1921;IIILnet/minecraft/class_11683$class_11792;)V"
      )}
   )
   private <S> void larpclient$redirectV(
      OrderedRenderCommandQueue var1, Model<S> var2, S var3, MatrixStack var4, RenderLayer var5, int var6, int var7, int var8, CrumblingOverlayCommand var9
   ) {
      CrystalChams var10 = CrystalChams.hAsCrystalChams();
      if (var10 == null || !var10.YAsBoolean(var3)) {
         if (var10 != null && var10.AAsBoolean()) {
            Identifier var11 = Identifier.ofVanilla("textures/entity/end_crystal/end_crystal.png");
            RenderLayer var12 = var10.VAsBoolean()
               ? RenderLayers.entityTranslucentEmissive(var11)
               : (var10.JAsBoolean() ? RenderLayers.entityTranslucent(var11) : var2.getLayer(var11));
            int var13 = var3 instanceof EndCrystalEntityRenderState var14 ? (int)(var14.x * 7.0 + var14.z * 13.0) : 0;
            var1.submitModel(var2, var3, var4, var12, var10.VAsBoolean() ? 15728880 : var6, var7, var10.CAsInt(var13), null, var8, var9);
         } else {
            var1.submitModel(var2, var3, var4, var5, var6, var7, var8, var9);
         }
      }
   }

   @Inject(
      method = {"method_23155(F)F"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private static void larpclient$injectGetYOffset(float var0, CallbackInfoReturnable<Float> var1) {
      CrystalChams var2 = CrystalChams.hAsCrystalChams();
      if (var2 != null) {
         var1.setReturnValue(var1.getReturnValueF() * var2.kAsFloat());
      }
   }

   @Inject(
      method = {"method_62417(Lnet/minecraft/class_1511;Lnet/minecraft/class_10014;F)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectFV(EndCrystalEntity var1, EndCrystalEntityRenderState var2, float var3, CallbackInfo var4) {
      ((WidgetJob)var2).larpCrystalId(var1.getId());
      CrystalChams var5 = CrystalChams.hAsCrystalChams();
      Boolean var6 = var5 == null ? null : var5.xAsBoolean2();
      if (var6 != null) {
         var2.baseVisible = var6;
      }
   }
}
