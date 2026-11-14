package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.NumberSetting;
import java.util.Set;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

abstract class NodeRenderer extends Module {
   private String ScreenScale = "Waiting";

   NodeRenderer(String var1, String var2) {
      super(var1, var2, Category.PLAYER, -1);
   }

   final BooleanSetting QAsBooleanSetting(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   final NumberSetting HAsNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   final boolean fAsBoolean() {
      if (!this.isEnabled()) {
         return false;
      } else {
         getMc();
         return this.isRuntimeActive()
            && b_.tAsBoolean(this)
            && mc.getOverlay() == null
            && mc.player.networkHandler.isLoaded()
            && (this.allowsScreenInteractions() || mc.isWindowFocused())
            && !mc.player.isUsingItem()
            && (!mc.isWindowFocused() || mc.currentScreen != null || !mc.options.attackKey.isPressed());
      }
   }

   void aimAt(Vec3d var1, float var2) {
      if (this.isEnabled()) {
         float[] var3 = dq.QAsFloatArray(mc.player.getEyePos(), var1);
         double var4 = (double)Math.max(Math.abs(MathHelper.wrapDegrees(var3[0] - mc.player.getYaw())), Math.abs(var3[1] - mc.player.getPitch()));
         dq.RAsVoid(this, dq.Type_s.PLACEMENT, var3[0], var3[1], var4 <= (double)var2 ? 1.0 : (double)var2 / var4);
      }
   }

   final void SAsVoid(String var1) {
      if (!var1.equals(this.y)) {
         this.y = var1;
         ClientLog.XAsVoid(this.getName(), "State: {}", var1);
      }
   }

   @Override
   public String getStatus() {
      return this.y;
   }

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.INTENT;
   }

   @Override
   public Set<com.larpclient.module.k> getNetworkImpacts() {
      return Set.of(com.larpclient.module.k.SERVER_STATE_MODIFYING);
   }

   @Override
   public void onEnable() {
      getMc();
      this.SAsVoid("Waiting");
      ClientLog.GAsVoid(
         this.getName(),
         this.allowsScreenInteractions()
            ? "World automation enabled; mining continues in screens and while unfocused; manual movement, mining, item use and freecam yield control"
            : "World automation enabled; manual mining, item use, screens and focus loss pause actions"
      );
   }

   @Override
   public void onDisable() {
      b_.KAsVoid(this);
      MiningProgress.cAsVoid(this);
      this.SAsVoid("Waiting");
   }
}
