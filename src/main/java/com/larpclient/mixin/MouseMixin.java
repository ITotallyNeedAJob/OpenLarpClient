package com.larpclient.mixin;

import com.larpclient.aj;
import com.larpclient.aq;
import com.larpclient.MenuPopupProvider;
import com.larpclient.xt;
import com.larpclient.module.GUIAnimation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({Mouse.class})
public abstract class MouseMixin {
   public MouseMixin() {
   }

   @Unique
   private static float HexEdgeloop() {
      Screen var0 = MinecraftClient.getInstance().currentScreen;
      return var0 instanceof HandledScreen && var0 instanceof aj var1 ? var1.larpContainerScale() : 1.0F;
   }

   @Unique
   private static double[] L(double var0, double var2) {
      Screen var4 = MinecraftClient.getInstance().currentScreen;
      if (var4 == null) {
         return new double[]{var0, var2};
      } else {
         xt.Type_s var5 = xt.JAsXt_Type_s(var4);
         if (var5 != null) {
            double[] var6 = var5.inverse(var0, var2);
            var0 = var6[0];
            var2 = var6[1];
         }

         aq.Type_x var10 = MenuPopupProvider.bAsAq_Type_x(var4);
         var0 = var10.localX(var0, (double)var4.width / 2.0);
         var2 = var10.localY(var2, (double)var4.height / 2.0);
         GUIAnimation var7 = GUIAnimation.kAsGUIAnimation();
         return var7 == null
            ? new double[]{var0, var2}
            : new double[]{var7.uAsDouble(var0, (double)var4.width / 2.0, HexEdgeloop()), var7.uAsDouble(var2, (double)var4.height / 2.0, HexEdgeloop())};
      }
   }

   @Unique
   private static Click M(Click var0) {
      double[] var1 = L(var0.x(), var0.y());
      return new Click(var1[0], var1[1], var0.buttonInfo());
   }

   @ModifyArgs(
      method = {"method_1601(JLnet/minecraft/class_11910;I)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_437;method_25402(Lnet/minecraft/class_11909;Z)Z"
      )}
   )
   private void larpclient$modifyArgsIV(Args var1) {
      var1.set(0, M((Click)var1.get(0)));
   }

   @ModifyArgs(
      method = {"method_1601(JLnet/minecraft/class_11910;I)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_437;method_25406(Lnet/minecraft/class_11909;)Z"
      )}
   )
   private void larpclient$modifyArgsIV2(Args var1) {
      var1.set(0, M((Click)var1.get(0)));
   }

   @ModifyArgs(
      method = {"method_55793()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_437;method_25403(Lnet/minecraft/class_11909;DD)Z"
      )}
   )
   private void larpclient$modifyArgsTick(Args var1) {
      Click var2 = (Click)var1.get(0);
      double var3 = (Double)var1.get(1);
      double var5 = (Double)var1.get(2);
      double[] var7 = L(var2.x() - var3, var2.y() - var5);
      double[] var8 = L(var2.x(), var2.y());
      var1.set(0, M(var2));
      var1.set(1, var8[0] - var7[0]);
      var1.set(2, var8[1] - var7[1]);
   }

   @ModifyArgs(
      method = {"method_55793()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_437;method_16014(DD)V"
      )}
   )
   private void larpclient$modifyArgsTick2(Args var1) {
      double[] var2 = L((Double)var1.get(0), (Double)var1.get(1));
      var1.set(0, var2[0]);
      var1.set(1, var2[1]);
   }

   @ModifyArgs(
      method = {"method_1598(JDD)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_437;method_25401(DDDD)Z"
      )}
   )
   private void larpclient$modifyArgsOnMouseScroll(Args var1) {
      double[] var2 = L((Double)var1.get(0), (Double)var1.get(1));
      var1.set(0, var2[0]);
      var1.set(1, var2[1]);
   }
}
