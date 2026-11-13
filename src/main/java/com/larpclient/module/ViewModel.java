package com.larpclient.module;

import com.larpclient.ClientLog;
import java.util.List;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;

public final class ViewModel extends Module {
   public static final double k = 0.1;
   public static final double F = 2.0;
   private final ViewModel.Type_u b = this.WAsViewModel_Type_u("Main", "main hand");
   private final ViewModel.Type_u W = this.WAsViewModel_Type_u("Off", "offhand");
   private static ViewModel P;

   public ViewModel() {
      super("ViewModel", "Resize, move and rotate each hand's item. Drag hands in chat to place them", Category.VISUALS, -1);
      P = this;
   }

   public static ViewModel qAsViewModel() {
      return P;
   }

   public List<Setting<?>> uAsList() {
      return this.b.settings();
   }

   public List<Setting<?>> hAsList() {
      return this.W.settings();
   }

   private ViewModel.Type_u jAsViewModel_Type_u(Hand var1) {
      return var1 == Hand.OFF_HAND ? this.W : this.b;
   }

   public void GAsVoid(MatrixStack var1, Hand var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModel.Type_u var4 = this.jAsViewModel_Type_u(var2);
         var1.translate((double)(var3 ? -1 : 1) * var4.x().getValue(), var4.y().getValue(), var4.z().getValue());
      }
   }

   public void yAsVoid(MatrixStack var1, Hand var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModel.Type_u var4 = this.jAsViewModel_Type_u(var2);
         float var5 = var3 ? -1.0F : 1.0F;
         var1.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var4.pitch().getValue().floatValue()));
         var1.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(var5 * var4.yaw().getValue().floatValue()));
         var1.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(var5 * var4.roll().getValue().floatValue()));
         float var6 = var4.scale().getValue().floatValue();
         var1.scale(var6, var6, var6);
      }
   }

   public void nAsVoid(MatrixStack var1, Hand var2, boolean var3) {
      if (this.isEnabled()) {
         float var4 = (float)(var3 ? 6 : -6) / 16.0F;
         var1.translate(var4, 0.75F, 0.0F);
         this.yAsVoid(var1, var2, var3);
         var1.translate(-var4, -0.75F, 0.0F);
      }
   }

   public double RAsDouble(Hand var1) {
      return this.jAsViewModel_Type_u(var1).scale().getValue();
   }

   public double HAsDouble(Hand var1) {
      return this.jAsViewModel_Type_u(var1).x().getValue();
   }

   public double NAsDouble(Hand var1) {
      return this.jAsViewModel_Type_u(var1).y().getValue();
   }

   public void IAsVoid(Hand var1, double var2, double var4) {
      ViewModel.Type_u var6 = this.jAsViewModel_Type_u(var1);
      var6.x().setClampedValue(var2);
      var6.y().setClampedValue(var4);
   }

   public void WAsVoid(Hand var1, double var2) {
      ViewModel.Type_u var4 = this.jAsViewModel_Type_u(var1);
      var4.scale().setClampedValue(var4.scale().getValue() + var2 * 0.05);
   }

   public void UAsVoid(Hand var1) {
      ViewModel.Type_u var2 = this.jAsViewModel_Type_u(var1);

      for (NumberSetting var4 : List.of(var2.scale(), var2.x(), var2.y(), var2.z())) {
         var4.reset();
      }
   }

   @Override
   public void onEnable() {
      ClientLog.GAsVoid("ViewModel", "Active: main {}x, offhand {}x", this.b.scale().getValue(), this.W.scale().getValue());
   }

   @Override
   public void onDisable() {
      ClientLog.GAsVoid("ViewModel", "Hands returned to vanilla placement");
   }

   private ViewModel.Type_u WAsViewModel_Type_u(String var1, String var2) {
      return new ViewModel.Type_u(
         this.IAsNumberSetting(var1 + " Scale", "Size", "Size of the " + var2 + " item or arm", 1.0, 0.1, 2.0, 0.05),
         this.IAsNumberSetting(var1 + " X", "Sideways", "Positive moves the " + var2 + " outward", 0.0, -2.5, 2.5, 0.01),
         this.IAsNumberSetting(var1 + " Y", "Height", "Vertical offset of the " + var2, 0.0, -2.0, 2.0, 0.01),
         this.IAsNumberSetting(var1 + " Z", "Depth", "Negative moves the " + var2 + " away from you", 0.0, -2.0, 1.0, 0.01),
         this.IAsNumberSetting(var1 + " Pitch", "Pitch", "Tilt of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.IAsNumberSetting(var1 + " Yaw", "Yaw", "Turn of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.IAsNumberSetting(var1 + " Roll", "Roll", "Roll of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0)
      );
   }

   private NumberSetting IAsNumberSetting(String var1, String var2, String var3, double var4, double var6, double var8, double var10) {
      NumberSetting var12 = this.addSetting(new NumberSetting(var1, var3, var4, var6, var8, var10));
      var12.displayName(var2);
      return var12;
   }

   private static record Type_u(
      NumberSetting scale, NumberSetting x, NumberSetting y, NumberSetting z, NumberSetting pitch, NumberSetting yaw, NumberSetting roll
   ) {
      List<Setting<?>> settings() {
         return List.of(this.scale, this.x, this.y, this.z, this.pitch, this.yaw, this.roll);
      }
   }
}
