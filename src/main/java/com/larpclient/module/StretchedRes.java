package com.larpclient.module;

public final class StretchedRes extends Module {
   private static StretchedRes d;
   private final NumberSetting stretch = this.addSetting(
      new NumberSetting("Stretch", "Horizontal stretch multiplier; 1 is normal, 1.33 resembles 4:3 on 16:9", 1.33, 1.0, 2.0, 0.01)
   );

   public StretchedRes() {
      super("Stretched Res", "Widen the world view, keeping the HUD unchanged", Category.VISUALS, -1);
      d = this;
   }

   public static float hAsFloat() {
      if (d != null && d.isEnabled()) {
         return d != null && d.isOperational() ? d.stretch.getValue().floatValue() : 1.0F;
      } else {
         return 1.0F;
      }
   }
}
