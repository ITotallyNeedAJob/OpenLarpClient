package com.larpclient.mixin;

import com.larpclient.ClientLog;
import com.larpclient.LarpClient;
import com.larpclient.StartupPresentation;
import com.larpclient.o2;
import com.larpclient.oy;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.texture.TextureManager;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({SplashOverlay.class})
public class SplashOverlayMixin {
   @Shadow
   @Final
   private MinecraftClient field_18217;
   @Shadow
   @Final
   private boolean field_18219;
   @Shadow
   private long field_17771;
   @Unique
   private boolean t;
   @Unique
   private boolean a;
   @Unique
   private static boolean b;
   @Unique
   private static boolean B = true;

   public SplashOverlayMixin() {
   }

   @Inject(
      method = {"method_18819(Lnet/minecraft/class_1060;)V"},
      at = {@At("TAIL")}
   )
   private static void larpclient$injectV(TextureManager var0, CallbackInfo var1) {
      try {
         var0.registerTexture(StartupPresentation.t, new StartupPresentation());
         var0.registerTexture(StartupPresentation.m, new StartupPresentation(StartupPresentation.m, "textures/gui/startup-brand.png"));
         BloomUtility = true;
      } catch (RuntimeException var3) {
         var0.destroyTexture(StartupPresentation.t);
         var0.destroyTexture(StartupPresentation.m);
         BloomUtility = false;
         LoggerFactory.getLogger("Larp/Startup").warn("Intro assets unavailable; vanilla startup fallback retained", var3);
      }

      B = StartupPresentation.gAsW_Type_a(FabricLoader.getInstance().getConfigDir().resolve("larp-client/menu.json")).motion();
   }

   @Inject(
      method = {"<init>"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectInit(CallbackInfo var1) {
      this.t = !this.field_18219 && !LarpClient.F;
      if (!this.t) {
         ClientLog.XAsVoid("Loading", "Resource reload overlay hidden; vanilla completion retained");
      }
   }

   @Inject(
      method = {"method_74218()Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectIsInGracePeriod(CallbackInfoReturnable<Boolean> var1) {
      if (!this.t) {
         var1.setReturnValue(true);
      }
   }

   @Inject(
      method = {"method_74217()V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectTick(CallbackInfo var1) {
      if (this.field_17771 >= 0L && this.field_18217.getOverlay() == (Object)this && (!this.t || b)) {
         if (this.t) {
            long var2 = System.nanoTime();
            if (!this.a) {
               this.a = true;
               o2.W.NAsVoid(var2);
               ClientLog.GAsVoid("Startup", "Resources ready; finishing logo/title intro before menu reveal");
            }

            if (!o2.W.SAsBoolean(var2, B)) {
               return;
            }

            this.field_18217.getTextureManager().destroyTexture(StartupPresentation.t);
            this.field_18217.getTextureManager().destroyTexture(StartupPresentation.m);
            LarpClient.F = true;
            ClientLog.GAsVoid("Startup", "Black intro complete; destination visible and input released");
         }

         this.field_18217.setOverlay(null);
      }
   }

   @Inject(
      method = {"method_25394(Lnet/minecraft/class_332;IIF)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectIIFV(DrawContext var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (!this.t) {
         var5.cancel();
         Screen var10 = this.field_18217.currentScreen;
         if (var10 != null) {
            var10.renderWithTooltip(var1, var2, var3, var4);
         } else {
            this.field_18217.inGameHud.renderDeferredSubtitles();
         }
      } else if (b) {
         var5.cancel();
         long var6 = System.nanoTime();
         o2 var8 = o2.W;
         if (var8.RAsO2_Type_u(var6, B) == o2.Type_u.STARTUP) {
            ClientLog.GAsVoid("Startup", "Black intro started; logo spin, soft glow and typewriter title");
         }

         var8.rAsVoid(var6);
         if (var8.nAsDouble(var6, B) > 0.0) {
            Screen var9 = this.field_18217.currentScreen;
            if (var9 != null) {
               var9.renderWithTooltip(var1, -1, -1, var4);
            } else {
               this.field_18217.inGameHud.renderDeferredSubtitles();
            }

            var1.createNewRootLayer();
         }

         oy.AAsVoid(var1, var6, B);
      }
   }
}
