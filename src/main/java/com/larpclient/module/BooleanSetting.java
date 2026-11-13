package com.larpclient.module;

public class BooleanSetting extends Setting<Boolean> {
   public BooleanSetting(String var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }

   public void toggle() {
      this.setValue(Boolean.valueOf(!this.getValue()));
   }

   @Override
   public String getValueAsString() {
      return this.getValue() ? "ON" : "OFF";
   }
}
