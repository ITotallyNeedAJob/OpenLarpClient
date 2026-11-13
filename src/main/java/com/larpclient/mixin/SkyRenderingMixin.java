package com.larpclient.mixin;

import com.larpclient.SkyEffectManager;
import com.larpclient.module.CustomSky;
import com.larpclient.module.SkyShader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.SkyRendering;
import net.minecraft.client.render.state.SkyRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.MoonPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SkyRendering.class})
public class SkyRenderingMixin {
   @Unique
   private final SkyEffectManager r = new SkyEffectManager();

   public SkyRenderingMixin() {
   }

   @Inject(
      method = {"method_62302(I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectRenderTopSky(int var1, CallbackInfo var2) {
      if (this.r.QAsBoolean(MinecraftClient.getInstance().getFramebuffer())) {
         var2.cancel();
      } else if (CustomSky.yAsBoolean()) {
         if (this.r.SAsBoolean(MinecraftClient.getInstance().getFramebuffer(), CustomSky.NAsCustomSky())) {
            var2.cancel();
         }
      } else {
         SkyShader var3 = SkyShader.EAsSkyShader();
         if (var3 != null && var3.CAsBoolean() && var3.ZAsBoolean(MinecraftClient.getInstance().getFramebuffer())) {
            var2.cancel();
         }
      }
   }

   @Unique
   private static boolean x() {
      SkyShader var0 = SkyShader.EAsSkyShader();
      return SkyEffectManager.AAsBoolean() || var0 != null && var0.aAsBoolean();
   }

   @Inject(
      method = {"method_62307(Lnet/minecraft/class_4587;FFFLnet/minecraft/class_12131;FF)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectFFV(MatrixStack var1, float var2, float var3, float var4, MoonPhase var5, float var6, float var7, CallbackInfo var8) {
      if (x()) {
         var8.cancel();
      }
   }

   @Inject(
      method = {"method_74926(Lnet/minecraft/class_638;FLnet/minecraft/class_4184;Lnet/minecraft/class_12076;)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectV(ClientWorld var1, float var2, Camera var3, SkyRenderState var4, CallbackInfo var5) {
      if (CustomSky.yAsBoolean()) {
         var4.starBrightness = CustomSky.NAsCustomSky().uAsBoolean() ? 0.0F : CustomSky.NAsCustomSky().KAsFloat() * var4.rainGradient;
      }
   }

   @Inject(
      method = {"method_62303(FLnet/minecraft/class_4587;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(float var1, MatrixStack var2, CallbackInfo var3) {
      if (CustomSky.yAsBoolean() && !CustomSky.NAsCustomSky().showVanillaSun()) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"method_62304(Lnet/minecraft/class_12131;FLnet/minecraft/class_4587;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(MoonPhase var1, float var2, MatrixStack var3, CallbackInfo var4) {
      if (CustomSky.yAsBoolean() && !CustomSky.NAsCustomSky().showVanillaMoon()) {
         var4.cancel();
      }
   }

   @Inject(
      method = {"method_62305()V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectRenderSkyDark(CallbackInfo var1) {
      if (CustomSky.yAsBoolean() || x()) {
         var1.cancel();
      }
   }

   @Inject(
      method = {"method_62306(Lnet/minecraft/class_4587;FI)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectFIV(MatrixStack var1, float var2, int var3, CallbackInfo var4) {
      if (CustomSky.yAsBoolean() || x()) {
         var4.cancel();
      }
   }

   @Inject(
      method = {"close"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectClose(CallbackInfo var1) {
      this.r.close();
   }
}
