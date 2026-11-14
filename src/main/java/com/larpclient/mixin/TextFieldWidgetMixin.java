package com.larpclient.mixin;

import com.larpclient.TextReveal;
import com.larpclient.module.GUIAnimation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TextFieldWidget.class})
public abstract class TextFieldWidgetMixin {
   @Shadow
   private String field_2092;
   @Shadow
   private int field_60435;
   @Unique
   private final TextReveal j = new TextReveal();
   @Unique
   private int L;

   public TextFieldWidgetMixin() {
   }

   @Shadow
   public abstract int method_1859();

   @Inject(
      method = {"method_48579(Lnet/minecraft/class_332;IIF)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectIIFV(DrawContext var1, int var2, int var3, float var4, CallbackInfo var5) {
      GUIAnimation var6 = GUIAnimation.kAsGUIAnimation();
      this.j
         .zAsVoid(this.field_2092, System.nanoTime(), var6 != null && var6.HAsBoolean() && MinecraftClient.getInstance().currentScreen instanceof ChatScreen);
   }

   @WrapOperation(
      method = {"method_48579(Lnet/minecraft/class_332;IIF)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_342;method_73211(Ljava/lang/String;I)Lnet/minecraft/class_5481;"
      )}
   )
   private OrderedText larpclient$wrapOperationIIFV(TextFieldWidget var1, String var2, int var3, Operation<OrderedText> var4) {
      this.L = var3;
      return (OrderedText)var4.call(new Object[]{var1, var2, var3});
   }

   @WrapOperation(
      method = {"method_48579(Lnet/minecraft/class_332;IIF)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_332;method_51430(Lnet/minecraft/class_327;Lnet/minecraft/class_5481;IIIZ)V"
      )}
   )
   private void larpclient$wrapOperationIIFV(
      DrawContext var1, TextRenderer var2, OrderedText var3, int var4, int var5, int var6, boolean var7, Operation<Void> var8
   ) {
      if (!this.j.BAsBoolean()) {
         var8.call(new Object[]{var1, var2, var3, var4, var5, var6, var7});
      } else {
         float[] var9 = new float[]{0.0F};
         TextReveal.Type_x var10 = new TextReveal.Type_x(this.L);
         TextFieldWidget var11 = (TextFieldWidget)(Object)this;
         var1.enableScissor(this.field_60435, var11.getY(), this.field_60435 + this.method_1859(), var11.getY() + var11.getHeight());

         try {
            var3.accept((var10xx, var11x, var12) -> {
               int var13 = var10.sAsInt(var12);
               OrderedText var14x = OrderedText.styled(var12, var11x);
               var1.getMatrices().pushMatrix();
               var1.getMatrices().translate(var9[0], (float)this.j.WAsDouble(var13));

               try {
                  var8.call(new Object[]{var1, var2, var14x, var4, var5, this.j.gAsInt(var6, var13), var7});
               } finally {
                  var1.getMatrices().popMatrix();
               }

               var9[0] += var2.getTextHandler().getWidth(var14x);
               return true;
            });
         } finally {
            var1.disableScissor();
         }
      }
   }
}
