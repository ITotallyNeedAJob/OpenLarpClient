package com.larpclient;

import java.util.ArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.util.Hand;

public final class WindowState {
   private static final ChamsOutlineEffect d = new ChamsOutlineEffect("ViewModel outline");
   private static Hand j;
   private static Hand o;
   private static boolean C;

   public static void gAsVoid(Hand var0, Hand var1) {
      j = var0;
      o = var1;
   }

   private static boolean nAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      return LayerIndex.nAsBoolean() && var0.options.getPerspective().isFirstPerson() && !var0.options.hudHidden;
   }

   public static boolean FAsBoolean() {
      if (!nAsBoolean()) {
         eAsVoid();
         return false;
      } else {
         C = true;
         return d.HAsBoolean();
      }
   }

   public static void qAsVoid() {
      if (!nAsBoolean()) {
         eAsVoid();
      } else {
         Window var0 = MinecraftClient.getInstance().getWindow();
         double var1 = (double)var0.getFramebufferWidth() / (double)Math.max(1, var0.getScaledWidth());
         float var3 = (float)Math.max(2.0, var1);
         int var4 = (int)Math.ceil((double)(var3 * 2.0F)) + 2;
         ArrayList var5 = new ArrayList(2);

         for (Hand var9 : Hand.values()) {
            LayerIndex.Type_w var10 = LayerIndex.YAsLi_Type_w(var9);
            if (var10 != null) {
               int var11 = var9 == j ? ColorUtil.XAsInt(UiStyle.w, 0.95) : (var9 == o ? -922746881 : 1627389951);
               var5.add(
                  new ChamsOutlineEffect.Type_k(
                     (int)Math.floor((double)var10.x() * var1) - var4,
                     (int)Math.floor((double)var10.y() * var1) - var4,
                     (int)Math.ceil((double)var10.width() * var1) + var4 * 2,
                     (int)Math.ceil((double)var10.height() * var1) + var4 * 2,
                     new BridgeRenderer.Type_e(var11, var11, var3, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, false)
                  )
               );
            }
         }

         d.tAsVoid(var5);
      }
   }

   public static void eAsVoid() {
      if (C) {
         C = false;
         o = null;
         j = null;
         d.close();
      }
   }

   private WindowState() {
   }
}
