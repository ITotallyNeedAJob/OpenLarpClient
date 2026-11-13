package com.larpclient.module;

public class NoWeather extends Module {
   private static boolean F = false;

   public NoWeather() {
      super("NoWeather", "Hide rain and snow", Category.RENDER, -1);
   }

   @Override
   public void onEnable() {
      F = true;
   }

   @Override
   public void onDisable() {
      F = false;
   }

   public static boolean iAsBoolean() {
      return F;
   }
}
