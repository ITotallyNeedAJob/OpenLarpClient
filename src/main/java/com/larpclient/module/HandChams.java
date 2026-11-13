package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.b8;
import com.larpclient.bp;

public final class HandChams extends Module {
   private static HandChams d;
   public final bp t = new bp(var1 -> this.addSetting(var1));

   public HandChams() {
      super("Hand Chams", "Animated gradient outlines, fill and glow on your hands and held items", Category.RENDER, -1);
      d = this;
   }

   public static HandChams eAsHandChams() {
      return d;
   }

   @Override
   public void onEnable() {
      ClientLog.GAsVoid("HandChams", "Echo gradient enabled for native hands and held items");
   }

   @Override
   public void onDisable() {
      b8.lAsVoid();
      this.t.EAsVoid();
   }

   @Override
   public void onTick() {
      b8.yAsVoid();
   }
}
