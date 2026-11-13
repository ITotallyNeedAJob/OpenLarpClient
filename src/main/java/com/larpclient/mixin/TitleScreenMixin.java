package com.larpclient.mixin;

import com.larpclient.ActivationScreen;
import com.larpclient.ClientTitleScreen;
import com.larpclient.LarpClient;
import com.larpclient.ModuleSearchPanel;
import com.larpclient.SessionManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TitleScreen.class})
public abstract class TitleScreenMixin extends Screen {
   protected TitleScreenMixin(Text var1) {
      super(var1);
   }

   @Inject(
      method = {"method_25426()V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectInit(CallbackInfo var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      LarpClient.F = true;
      if (SessionManager.HAsBoolean() && !ClientTitleScreen.QAsBoolean()) {
         this.addDrawableChild(
            ButtonWidget.builder(Text.literal("Larp client"), var2xx -> var2.setScreen(ModuleSearchPanel.MAsScreen(this))).dimensions(4, 4, 90, 20).build()
         );
      } else {
         var2.execute(() -> {
            if (var2.currentScreen instanceof TitleScreen) {
               if (!SessionManager.HAsBoolean()) {
                  var2.setScreen(new ActivationScreen());
               } else if (ClientTitleScreen.QAsBoolean()) {
                  var2.setScreen(new ClientTitleScreen());
               }
            }
         });
      }
   }
}
