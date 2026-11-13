package com.larpclient;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

public final class PlayerCosmeticsFeature extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
   public PlayerCosmeticsFeature(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> var1) {
      super(var1);
   }

   public void render(MatrixStack var1, OrderedRenderCommandQueue var2, int var3, PlayerEntityRenderState var4, float var5, float var6) {
      la var7 = ((mc)var4).larp$korblox();
      if (var7 != null && !var4.invisible && !var4.spectator) {
         int var8 = LivingEntityRenderer.getOverlay(var4, 0.0F);
         PlayerEntityModel var9 = (PlayerEntityModel)this.getContextModel();
         if (var7.right()) {
            KorbloxLegRenderer.JAsVoid(var1, var2, var9.rightLeg, var7, var3, var8, false);
         }

         if (var7.left()) {
            KorbloxLegRenderer.JAsVoid(var1, var2, var9.leftLeg, var7, var3, var8, true);
         }
      }
   }
}
