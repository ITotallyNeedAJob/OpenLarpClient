package com.larpclient.mixin;

import com.larpclient.ChatOverlayEditor;
import com.larpclient.ClientLog;
import com.larpclient.HudEditorController;
import com.larpclient.HudRenderer;
import java.util.List;
import net.minecraft.client.font.DrawnTextConsumer.ClickHandler;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ChatScreen.class})
public abstract class ChatScreenMixin extends Screen {
   @Shadow
   protected TextFieldWidget field_2382;
   @Shadow
   private ChatInputSuggestor field_21616;
   @Unique
   private final HudEditorController K = new HudEditorController();
   @Unique
   private final ChatOverlayEditor M = new ChatOverlayEditor();
   @Unique
   private Object j;

   protected ChatScreenMixin(Text var1) {
      super(var1);
   }

   @Unique
   private boolean D() {
      return ((Object)this).getClass() == ChatScreen.class && this.client.player != null && this.client.world != null;
   }

   @Unique
   private boolean z(double var1, double var3) {
      if (!(var3 >= (double)(this.height - 14)) && !this.field_2382.isMouseOver(var1, var3)) {
         if (this.field_21616.window != null && this.field_21616.window.area.contains((int)var1, (int)var3)) {
            return true;
         } else {
            ClickHandler var5 = new ClickHandler(this.getTextRenderer(), (int)var1, (int)var3).insert(true);
            this.client.inGameHud.getChatHud().render(var5, this.height, this.client.inGameHud.getTicks(), true);
            return var5.getStyle() != null;
         }
      } else {
         return true;
      }
   }

   @Inject(
      method = {"method_25426()V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectInit(CallbackInfo var1) {
      this.K.eAsVoid(this.client.isWindowFocused(), this.j == null || this.j == this.client.world);
      this.K.xAsVoid();
      this.M.NAsVoid();
   }

   @Inject(
      method = {"method_25426()V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectInit2(CallbackInfo var1) {
      this.j = this.client.world;
      if (this.D()) {
         ClientLog.XAsVoid("HUD editor", "Chat mouse editing ready");
      }
   }

   @Inject(
      method = {"method_25402(Lnet/minecraft/class_11909;Z)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectZZ(Click var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      this.K.eAsVoid(this.client.isWindowFocused(), this.j == this.client.world);
      if (!this.D() || !this.K.eAsBoolean() && !this.M.SAsBoolean()) {
         if (this.D() && this.z(var1.x(), var1.y())) {
            this.K.WAsVoid();
            this.M.vAsVoid();
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(
      method = {"method_25402(Lnet/minecraft/class_11909;Z)Z"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectZZ2(Click var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      if (!(Boolean)var3.getReturnValue() && this.D() && this.client.isWindowFocused()) {
         List var4 = HudRenderer.pAsList();
         boolean var5 = this.z(var1.x(), var1.y());
         if (!var5 && !this.K.SAsBoolean(var1.x(), var1.y(), this.width, this.height, var4) && this.M.uAsBoolean(var1.x(), var1.y(), var1.button())) {
            this.K.pAsVoid(null);
            var3.setReturnValue(true);
         } else {
            this.M.vAsVoid();
            if (this.K.OAsBoolean(var1.x(), var1.y(), var1.button(), this.width, this.height, var4, var5)) {
               var3.setReturnValue(true);
            }
         }
      }
   }

   public boolean mouseDragged(Click click, double offsetX, double offsetY) {
      this.K.eAsVoid(this.client.isWindowFocused(), this.j == this.client.world);
      if (this.D() && this.M.OAsBoolean(click.x(), click.y(), click.button())) {
         return true;
      } else {
         return this.D() && this.K.QAsBoolean(click.x(), click.y(), click.button(), this.width, this.height, this.client.isAltPressed())
            ? true
            : super.mouseDragged(click, offsetX, offsetY);
      }
   }

   public boolean mouseReleased(Click click) {
      this.K.eAsVoid(this.client.isWindowFocused(), this.j == this.client.world);
      if (this.D() && this.M.sAsBoolean(click.button())) {
         return true;
      } else {
         return this.D() && this.K.NAsBoolean(click.button()) ? true : super.mouseReleased(click);
      }
   }

   @Inject(
      method = {"method_25401(DDDD)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectMouseScrolled(double var1, double var3, double var5, double var7, CallbackInfoReturnable<Boolean> var9) {
      if (this.D() && this.client.isWindowFocused()) {
         boolean var10 = this.z(var1, var3);
         if (this.K.GAsBoolean(var1, var3, var7, var10)
            || !var10 && !this.K.SAsBoolean(var1, var3, this.width, this.height, HudRenderer.pAsList()) && this.M.rAsBoolean(var1, var3, var7)) {
            var9.setReturnValue(true);
         }
      }
   }

   @Inject(
      method = {"method_25404(Lnet/minecraft/class_11908;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectZ(KeyInput var1, CallbackInfoReturnable<Boolean> var2) {
      if (this.D() && this.client.isWindowFocused() && this.K.GAsBoolean(var1.key())) {
         var2.setReturnValue(true);
      }
   }

   public boolean charTyped(CharInput input) {
      return this.D() && this.client.isWindowFocused() && this.K.lAsBoolean(input.codepoint()) ? true : super.charTyped(input);
   }

   public void tick() {
      super.tick();
      this.K.eAsVoid(this.client.isWindowFocused(), this.j == this.client.world);
      this.M.rAsVoid(this.client.isWindowFocused());
   }

   @Inject(
      method = {"method_25394(Lnet/minecraft/class_332;IIF)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectIIFV(DrawContext var1, int var2, int var3, float var4, CallbackInfo var5) {
      this.K.eAsVoid(this.client.isWindowFocused(), this.j == this.client.world);
      if (this.D() && this.client.isWindowFocused()) {
         var1.createNewRootLayer();
         boolean var6 = this.z((double)var2, (double)var3);
         this.M.sAsVoid(var1, var2, var3, var6 || this.K.SAsBoolean((double)var2, (double)var3, this.width, this.height, HudRenderer.pAsList()));
         this.K.mAsVoid(var1, var2, var3, this.width, this.height, var6);
         var1.createNewRootLayer();
      }
   }

   @Inject(
      method = {"method_25432()V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectRemoved(CallbackInfo var1) {
      this.K.eAsVoid(true, true);
      this.K.xAsVoid();
      this.M.NAsVoid();
   }
}
