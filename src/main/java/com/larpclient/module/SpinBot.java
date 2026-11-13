package com.larpclient.module;

import com.larpclient.ClientLog;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.MathHelper;

public final class SpinBot extends Module {
   private static SpinBot H;
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Model spin speed in degrees per second", 360.0, 15.0, 1440.0, 15.0));
   private final ModeSetting direction = this.addSetting(new ModeSetting("Direction", "Model spin direction", "Clockwise", "Clockwise", "Counterclockwise"));
   private float u;
   private float B;

   public SpinBot() {
      super("Spin Bot", "Spin your player model on your screen only; real facing and movement are unchanged", Category.COMBAT, -1);
      H = this;
   }

   public static SpinBot GAsSpinBot() {
      return H;
   }

   @Override
   public void onEnable() {
      getMc();
      this.u = this.B = mc.player == null ? 0.0F : mc.player.bodyYaw;
      ClientLog.GAsVoid(this.getName(), "Enabled: client-side model spin at {} degrees/s, {}", this.speed.getValue(), this.direction.getValue());
   }

   @Override
   public void onDisable() {
      ClientLog.XAsVoid(this.getName(), "Disabled: native model facing restored");
   }

   @Override
   public void onTick() {
      this.B = this.u;
      this.u = MathHelper.wrapDegrees(this.u + this.speed.getValue().floatValue() / 20.0F * (float)(this.direction.pAsBoolean("Clockwise") ? 1 : -1));
   }

   public float gAsFloat(float var1, float var2) {
      if (this.isEnabled()) {
         return this.mAsBoolean() ? MathHelper.wrapDegrees(MathHelper.lerpAngleDegrees(var2, this.B, this.u)) : var1;
      } else {
         return var1;
      }
   }

   public float gAsFloat(float var1) {
      if (this.isEnabled()) {
         return this.mAsBoolean() ? 0.0F : var1;
      } else {
         return var1;
      }
   }

   private boolean mAsBoolean() {
      if (!this.isRuntimeActive() || mc == null || mc.player == null) {
         return false;
      } else if (mc.options != null && !mc.options.getPerspective().isFirstPerson()) {
         Screen var1 = mc.currentScreen;
         return var1 == null || var1 instanceof ChatScreen;
      } else {
         return false;
      }
   }
}
