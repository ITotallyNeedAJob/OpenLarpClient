package com.larpclient.mixin;

import com.larpclient.ClickGuiScreen;
import com.larpclient.MenuPopupProvider;
import com.larpclient.WidgetPopup;
import com.larpclient.module.WorldMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MinecraftClient.class})
public abstract class MinecraftClientMixin {
   public MinecraftClientMixin() {
   }

   @Inject(
      method = {"method_1507(Lnet/minecraft/class_437;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV(Screen var1, CallbackInfo var2) {
      Screen var3 = ((MinecraftClient)(Object)this).currentScreen;
      if (var3 != var1) {
         WidgetPopup var4 = MenuPopupProvider.pAsWz(var3);
         if (var4 != null) {
            var4.OAsVoid();
         }
      }
   }

   @Inject(
      method = {"method_1507(Lnet/minecraft/class_437;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV2(Screen var1, CallbackInfo var2) {
      if (var1 instanceof ClickGuiScreen var3 && WorldMenu.gAsBoolean(var3)) {
         var2.cancel();
         MinecraftClient var4 = (MinecraftClient)(Object)this;
         if (var4.currentScreen != null) {
            var4.setScreen(null);
         }
      }
   }

   @Mixin({GameRenderer.class})
   public abstract static class GameRendererOverlayMixin {
      @Shadow
      @Final
      private MinecraftClient field_4015;
      @Shadow
      @Final
      private GuiRenderState field_59966;

      public GameRendererOverlayMixin() {
      }

      @Inject(
         method = {"method_3192"},
         at = {@At(
            value = "INVOKE",
            shift = Shift.AFTER,
            target = "Lnet/minecraft/class_11246;method_70926()V"
         )}
      )
      private void larpclient$injectRender(RenderTickCounter var1, boolean var2, CallbackInfo var3) {
         WorldMenu.WAsVoid(new DrawContext(this.field_4015, this.field_59966, -10000, -10000), var1.getTickProgress(false));
      }

      @Inject(
         method = {"method_3192"},
         at = {@At(
            value = "INVOKE",
            target = "Lnet/minecraft/class_11228;method_70890(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"
         )}
      )
      private void larpclient$injectRender2(RenderTickCounter var1, boolean var2, CallbackInfo var3) {
         if (ClickGuiScreen.MAsBoolean()) {
            ClickGuiScreen.kAsVoid(new DrawContext(this.field_4015, this.field_59966, -10000, -10000), var1.getTickProgress(false));
         }
      }
   }
}
