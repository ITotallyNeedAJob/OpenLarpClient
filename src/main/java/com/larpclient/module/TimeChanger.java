package com.larpclient.module;

import com.larpclient.ClientLog;

public class TimeChanger extends Module {
   private final NumberSetting time = this.addSetting(new NumberSetting("Time", "Local clock: 6000 is noon, 18000 is midnight", 6000.0, 0.0, 24000.0, 500.0));
   private static TimeChanger e;

   public TimeChanger() {
      super("TimeChanger", "Set the time shown in your world", Category.RENDER, -1);
      e = this;
   }

   public static TimeChanger QAsTimeChanger() {
      return e;
   }

   public long time() {
      return (long)this.time.getValue().doubleValue();
   }

   public static long xAsLong(long var0, long var2) {
      return Math.floorDiv(var0, 24000L) * 24000L + Math.floorMod(var2, 24000L);
   }

   @Override
   public void onEnable() {
      ClientLog.GAsVoid("TimeChanger", "Local overworld clock override enabled; time={}; server clock retained", this.time());
   }

   @Override
   public void onDisable() {
      ClientLog.XAsVoid("TimeChanger", "Local clock override released; native time resumes");
   }
}
