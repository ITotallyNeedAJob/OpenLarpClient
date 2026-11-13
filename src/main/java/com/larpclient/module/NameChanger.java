package com.larpclient.module;

public final class NameChanger extends Module {
   private static NameChanger d;
   private final TextSetting name = this.addSetting(new TextSetting("Name", "Shown instead of your username on your screen. Any text works", ""));

   public NameChanger() {
      super("Name Changer", "Show any name in place of your username. Only you see it", Category.PLAYER, -1);
      d = this;
      this.name.dAsE("Type any name");
   }

   public static NameChanger WAsNameChanger() {
      return d;
   }

   public String nAsString() {
      return this.isOperational() ? this.name.getValue().strip() : "";
   }

   @Override
   public String getStatus() {
      String var1 = this.name.getValue().strip();
      return var1.isEmpty() ? "Type a name to use" : "Showing as " + var1;
   }
}
