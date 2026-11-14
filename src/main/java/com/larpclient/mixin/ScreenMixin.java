package com.larpclient.mixin;

import com.larpclient.ClickGuiScreen;
import com.larpclient.ClientLog;
import com.larpclient.ClientPreferences;
import com.larpclient.ClientTitleScreen;
import com.larpclient.GuiTextureDrawing;
import com.larpclient.LarpClient;
import com.larpclient.MenuNebulaRenderer;
import com.larpclient.UiStyle;
import com.larpclient.MenuPopupProvider;
import com.larpclient.XShader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.LevelLoadingScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Screen.class})
public abstract class ScreenMixin {
   @Unique
   private boolean A;

   public ScreenMixin() {
   }

   @Redirect(
      method = {"method_47413(Lnet/minecraft/class_332;IIF)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_437;method_25420(Lnet/minecraft/class_332;IIF)V"
      )}
   )
   private void larpclient$redirectIIFV(Screen var1, DrawContext var2, int var3, int var4, float var5) {
      boolean var6 = var1 instanceof LevelLoadingScreen;
      if ((MinecraftClient.getInstance().world == null || var6) && !(var1 instanceof ChatScreen) && LineSegment(var1)) {
         if (var6 && !this.A) {
            this.A = true;
            ClientLog.XAsVoid("Menu", "Terrain loading uses the shared custom menu background");
         }

         boolean var10000;
         label78: {
            if (var1 instanceof MenuPopupProvider var8 && var8.larpDrawingMenuPopup()) {
               var10000 = true;
               break label78;
            }

            var10000 = false;
         }

         boolean var7 = var10000;
         if (var7) {
            var2.getMatrices().pushMatrix();
            var2.getMatrices().mul(MenuPopupProvider.bAsAq_Type_x(var1).matrix((double)var1.width / 2.0, (double)var1.height / 2.0).invert());
         }

         try {
            ClientPreferences var13 = LarpClient.CAsLarpClient().fAsBo().yAsN7();
            if (var1 instanceof xs var9) {
               MenuNebulaRenderer.eAsVoid(var2, var1.width, var1.height, var9.atmosphere(), var9.dimAtmosphere(), var13.Z);
            } else {
               MenuNebulaRenderer.eAsVoid(var2, var1.width, var1.height, var13.Jf, var1.deferSubtitles(), var13.Z);
            }

            MinecraftClient.getInstance().inGameHud.renderDeferredSubtitles();
         } finally {
            if (var7) {
               var2.getMatrices().popMatrix();
            }
         }
      } else {
         var1.renderBackground(var2, var3, var4, var5);
      }
   }

   @Unique
   private static boolean LineSegment(Screen var0) {
      return ClientTitleScreen.QAsBoolean() ? true : var0.getClass().getName().startsWith("com.larpclient.") && !(var0 instanceof ClickGuiScreen);
   }

   @Inject(
      method = {"method_57737(Lnet/minecraft/class_332;Lnet/minecraft/class_2960;IIFFII)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void larpclient$injectIIFFIIV(
      DrawContext var0, Identifier var1, int var2, int var3, float var4, float var5, int var6, int var7, CallbackInfo var8
   ) {
      if (MinecraftClient.getInstance().world == null && ClientTitleScreen.QAsBoolean()) {
         GuiTextureDrawing.CAsVoid(var0, var2, var3, var6, var7, bd.X);
         var8.cancel();
      }
   }
}
