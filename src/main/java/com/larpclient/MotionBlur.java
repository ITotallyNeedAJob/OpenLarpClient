package com.larpclient;

import com.larpclient.module.HeadlessKorblox;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.item.ItemStack;

public final class MotionBlur {
   private MotionBlur() {
   }

   public static void RAsVoid(PlayerLikeEntity var0, PlayerEntityRenderState var1) {
      mc var2 = (mc)var1;
      var2.larp$roblox(0, null);
      HeadlessKorblox var3 = HeadlessKorblox.lAsHeadlessKorblox();
      int var4 = var3 == null ? 0 : var3.MAsInt(var0);
      la var5 = var3 == null ? null : var3.kAsLa(var0);
      if (var5 != null && !KorbloxLegRenderer.DAsBoolean()) {
         var5 = null;
      }

      var2.larp$roblox(var4, var5);
      if (var4 == 2) {
         PAsVoid(var1);
      }
   }

   public static void PAsVoid(BipedEntityRenderState var0) {
      var0.equippedHeadStack = ItemStack.EMPTY;
      var0.headItemRenderState.clear();
      var0.wearingSkullType = null;
      var0.wearingSkullProfile = null;
   }

   public static boolean gAsBoolean(PlayerEntityModel var0, boolean var1, int var2, la var3) {
      int var4 = var1 && var2 != 0 && var0.head.visible ? 1 : 0;
      if (var4 != 0) {
         var0.head.visible = false;
      }

      if (var3 != null) {
         if (var3.right()) {
            var0.rightLeg.visible = false;
         }

         if (var3.left()) {
            var0.leftLeg.visible = false;
         }
      }

      return var4 != 0;
   }

   public static void OAsVoid(PlayerEntityRenderState var0) {
      ((mc)var0).larp$roblox(0, null);
   }
}
