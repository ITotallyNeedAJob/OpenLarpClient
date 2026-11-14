package com.larpclient.mixin;

import com.larpclient.ClientLog;
import com.larpclient.EmotecraftJsonReader;
import com.larpclient.OptionPanel;
import com.larpclient.RefractionGeometry;
import com.larpclient.module.Emotes;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.PlayerLikeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerEntityRenderer.class})
public abstract class PlayerEntityRendererUpdateRenderStateMixin2 {
   public PlayerEntityRendererUpdateRenderStateMixin2() {
   }

   @Inject(
      method = {"method_62604(Lnet/minecraft/class_11890;Lnet/minecraft/class_10055;F)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectFV(PlayerLikeEntity var1, PlayerEntityRenderState var2, float var3, CallbackInfo var4) {
      try {
         Emotes.gAsVoid(var1, var2, var3);
      } catch (RuntimeException var6) {
         ((o6)var2).larp$setEmoteWeight(0.0F);
         ClientLog.hAsVoid("Emotes", "Emote sampling failed; native pose kept", var6);
      }
   }

   @Inject(
      method = {"method_4212(Lnet/minecraft/class_10055;Lnet/minecraft/class_4587;FF)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectFFV(PlayerEntityRenderState var1, MatrixStack var2, float var3, float var4, CallbackInfo var5) {
      o6 var6 = (o6)var1;
      if (var6.larp$emoteWeight() > 0.0F) {
         rg.IAsVoid(var2, var6.larp$emoteSample(), var6.larp$emoteWeight());
      }
   }

   @Mixin({PlayerEntityRenderState.class})
   public abstract static class PlayerEntityRenderStateMixin implements o6 {
      @Unique
      private final float[] W = new float[EmotecraftJsonReader.O];
      @Unique
      private float F;
      @Unique
      private float f;
      @Unique
      private String J;

      public PlayerEntityRenderStateMixin() {
      }

      @Override
      public float[] larp$emoteSample() {
         return this.W;
      }

      @Override
      public float larp$emoteWeight() {
         return this.F;
      }

      @Override
      public void larp$setEmoteWeight(float var1) {
         this.F = var1;
      }

      @Override
      public String larp$emoteProp() {
         return this.J;
      }

      @Override
      public float larp$emoteCycle() {
         return this.f;
      }

      @Override
      public void larp$setEmoteProp(String var1, float var2) {
         this.J = var1;
         this.f = var2;
      }
   }
}
