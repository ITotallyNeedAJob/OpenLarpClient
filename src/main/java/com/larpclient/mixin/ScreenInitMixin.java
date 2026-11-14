package com.larpclient.mixin;

import com.larpclient.ContainerScaleSupplier;
import com.larpclient.PopupScale;
import com.larpclient.MenuPopupProvider;
import com.larpclient.WidgetPopup;
import com.larpclient.XTexture;
import com.larpclient.module.GUIAnimation;
import com.larpclient.module.Module3DUI;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Screen.class})
public abstract class ScreenInitMixin implements aj, MenuPopupProvider {
   @Shadow
   public int field_22789;
   @Shadow
   public int field_22790;
   @Unique
   private long d;
   @Unique
   private float J = 1.0F;
   @Unique
   private boolean e;
   @Unique
   private boolean t;
   @Unique
   private wz W;
   @Unique
   private boolean L;

   public ScreenInitMixin() {
   }

   @Override
   public void larpOpenMenuPopup() {
      this.W = new wz();
   }

   @Override
   public wz larpMenuPopup() {
      return this.W;
   }

   @Override
   public boolean larpDrawingMenuPopup() {
      return this.L;
   }

   @Inject(
      method = {"method_25423(II)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectInit(int var1, int var2, CallbackInfo var3) {
      this.d = System.nanoTime();
      this.J = 1.0F;
   }

   @Override
   public float larpContainerScale() {
      return this.J;
   }

   @WrapMethod(
      method = {"method_47413(Lnet/minecraft/class_332;IIF)V"}
   )
   private void U(DrawContext var1, int var2, int var3, float var4, Operation<Void> var5) {
      MinecraftClient var6 = MinecraftClient.getInstance();
      if (this.W != null && var6.world == null) {
         aq.Type_x var7 = this.W.XAsAq_Type_x(System.nanoTime(), var6.getOverlay() == null && var6.isWindowFocused(), MenuPopupProvider.CAsBoolean());
         if (!this.W.LAsBoolean()) {
            var1.getMatrices().pushMatrix();
            var1.getMatrices().mul(var7.matrix((double)this.field_22789 / 2.0, (double)this.field_22790 / 2.0));
            this.L = true;

            try {
               var5.call(
                  new Object[]{
                     var1,
                     (int)Math.round(var7.localX((double)var2, (double)this.field_22789 / 2.0)),
                     (int)Math.round(var7.localY((double)var3, (double)this.field_22790 / 2.0)),
                     var4
                  }
               );
            } finally {
               this.L = false;
               var1.getMatrices().popMatrix();
            }

            return;
         }
      }

      xt.Type_n var16 = Module3DUI.jAsXt_Type_n((Screen)(Object)this);
      if (var16 == null) {
         this.A(var1, var2, var3, var4, var5);
      } else {
         var1.createNewRootLayer();
         Module3DUI.BAsVoid(var1, this.field_22789, this.field_22790);
         MinecraftClient.getInstance().inGameHud.renderDeferredSubtitles();
         double[] var8 = var16.inverse((double)var2, (double)var3);

         try {
            xt.oAsVoid(var1, var16, true, () -> this.A(var1, (int)Math.floor(var8[0]), (int)Math.floor(var8[1]), var4, var5));
         } finally {
            var1.createNewRootLayer();
            xt.cAsVoid((double)this.J, (double)this.field_22789 / 2.0, (double)this.field_22790 / 2.0);
         }
      }
   }

   @Unique
   private void A(DrawContext var1, int var2, int var3, float var4, Operation<Void> var5) {
      GUIAnimation var6 = GUIAnimation.kAsGUIAnimation();
      this.e = (Object)this instanceof HandledScreen && var6 != null && var6.XAsBoolean();
      this.J = this.e ? var6.wAsFloat(this.d) : 1.0F;
      if (!this.e) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else {
         var1.getMatrices().pushMatrix();
         this.V(var1, this.J);
         int var7 = (int)Math.round(var6.uAsDouble((double)var2, (double)this.field_22789 / 2.0, this.J));
         int var8 = (int)Math.round(var6.uAsDouble((double)var3, (double)this.field_22790 / 2.0, this.J));

         try {
            var5.call(new Object[]{var1, var7, var8, var4});
         } finally {
            var1.getMatrices().popMatrix();
            this.e = false;
            this.t = false;
         }
      }
   }

   @Unique
   private void V(DrawContext var1, float var2) {
      var1.getMatrices().translate((float)this.field_22789 / 2.0F, (float)this.field_22790 / 2.0F);
      var1.getMatrices().scale(var2, var2);
      var1.getMatrices().translate((float)(-this.field_22789) / 2.0F, (float)(-this.field_22790) / 2.0F);
   }

   @Inject(
      method = {"method_52752(Lnet/minecraft/class_332;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(DrawContext var1, CallbackInfo var2) {
      if (xt.JAsXt_Type_s(this) != null) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"method_25420(Lnet/minecraft/class_332;IIF)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectIIFV(DrawContext var1, int var2, int var3, float var4, CallbackInfo var5) {
      this.t = this.e;
      if (this.t) {
         var1.getMatrices().pushMatrix();
         this.V(var1, 1.0F / this.J);
      }
   }

   @Inject(
      method = {"method_25420(Lnet/minecraft/class_332;IIF)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectIIFV2(DrawContext var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (this.t) {
         var1.getMatrices().popMatrix();
         this.t = false;
      }
   }
}
