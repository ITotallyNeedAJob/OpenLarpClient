package com.larpclient.mixin;

import com.larpclient.ClientLog;
import com.larpclient.PlayerCosmeticsFeature;
import com.larpclient.la;
import com.larpclient.mc;
import com.larpclient.mj;
import com.larpclient.xk;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.PlayerLikeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerEntityRenderer.class})
public abstract class PlayerEntityRendererMixin {
   public PlayerEntityRendererMixin() {
   }

   @Inject(
      method = {"<init>(Lnet/minecraft/class_5617$class_5618;Z)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectZV(Context var1, boolean var2, CallbackInfo var3) {
      PlayerEntityRenderer var4 = (PlayerEntityRenderer)(Object)this;
      ((xk)var4.getModel()).larp$markSkin();
      ((LivingEntityRendererInvoker)(Object)this).larp$addLayer(new PlayerCosmeticsFeature(var4));
   }

   @Inject(
      method = {"method_62604(Lnet/minecraft/class_11890;Lnet/minecraft/class_10055;F)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectFV(PlayerLikeEntity var1, PlayerEntityRenderState var2, float var3, CallbackInfo var4) {
      try {
         mj.RAsVoid(var1, var2);
      } catch (RuntimeException var6) {
         mj.OAsVoid(var2);
         ClientLog.hAsVoid("Roblox", "Headless/Korblox extraction failed; native head and legs kept", var6);
      }
   }

   @Mixin({PlayerEntityRenderState.class})
   public abstract static class PlayerEntityRenderStateMixin implements mc {
      @Unique
      private int X;
      @Unique
      private la u;

      public PlayerEntityRenderStateMixin() {
      }

      @Override
      public int larp$headless() {
         return this.X;
      }

      @Override
      public la larp$korblox() {
         return this.u;
      }

      @Override
      public void larp$roblox(int var1, la var2) {
         this.X = var1;
         this.u = var2;
      }
   }
}
