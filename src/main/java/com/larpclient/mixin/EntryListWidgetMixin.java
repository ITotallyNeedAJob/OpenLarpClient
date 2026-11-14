package com.larpclient.mixin;

import com.larpclient.ClientTitleScreen;
import com.larpclient.GuiTextureDrawing;
import com.larpclient.UiStyle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntryListWidget.class})
public abstract class EntryListWidgetMixin {
   public EntryListWidgetMixin() {
   }

   @Inject(
      method = {"method_57715(Lnet/minecraft/class_332;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(DrawContext var1, CallbackInfo var2) {
      if (MinecraftClient.getInstance().world == null && ClientTitleScreen.QAsBoolean()) {
         EntryListWidget var3 = (EntryListWidget)(Object)this;
         GuiTextureDrawing.CAsVoid(var1, var3.getX(), var3.getY(), var3.getWidth(), var3.getHeight(), UiStyle.X);
         var2.cancel();
      }
   }

   @Inject(
      method = {"method_57713(Lnet/minecraft/class_332;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV2(DrawContext var1, CallbackInfo var2) {
      if (MinecraftClient.getInstance().world == null && ClientTitleScreen.QAsBoolean()) {
         EntryListWidget var3 = (EntryListWidget)(Object)this;
         int var4 = UiStyle.A;
         GuiTextureDrawing.CAsVoid(var1, var3.getX(), var3.getY() - 1, var3.getWidth(), 1, var4);
         GuiTextureDrawing.CAsVoid(var1, var3.getX(), var3.getBottom(), var3.getWidth(), 1, var4);
         var2.cancel();
      }
   }
}
