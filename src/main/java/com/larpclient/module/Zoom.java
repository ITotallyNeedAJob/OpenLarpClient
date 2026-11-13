package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.KeybindNames;
import com.larpclient.LarpClient;
import com.larpclient.mg;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.MinecraftClient;

public final class Zoom extends Module {
   private static Zoom i;
   public final KeybindSetting zoomKey = this.addSetting(new KeybindSetting("Zoom Key", "Key used while this module is enabled", 67));
   public final ModeSetting activation = this.addSetting(
      new ModeSetting("Activation", "Hold the zoom key or press it to toggle the zoom", "Hold", "Hold", "Toggle")
   );
   public final NumberSetting magnification = this.addSetting(
      new NumberSetting("Magnification", "Optical magnification before scroll adjustment", 4.0, 1.25, 16.0, 0.25)
   );
   public final BooleanSetting scrollZoom = this.addSetting(new BooleanSetting("Scroll Zoom", "Adjust magnification with the mouse wheel while zoomed", true));
   public final BooleanSetting rememberMagnification = this.addSetting(
      new BooleanSetting("Remember Magnification", "Keep the last wheel adjustment for the next zoom", false)
   );
   public final BooleanSetting reduceSensitivity = this.addSetting(
      new BooleanSetting("Reduce Sensitivity", "Scale mouse motion with optical magnification", true)
   );
   public final BooleanSetting smoothTransition = this.addSetting(
      new BooleanSetting("Smooth Transition", "Ease into and out of zoom without delaying input", true)
   );
   public final NumberSetting transitionTime = this.addSetting(
      new NumberSetting("Transition Time", "Seconds to reach approximately 99 percent of a new zoom", 0.15, 0.0, 0.5, 0.01)
   );
   private final mg m = new mg();
   private Object Y;
   private Object U;
   private boolean d;

   public Zoom() {
      super("Zoom", "Hold a key to zoom smoothly, with wheel adjustment and precise mouse control", Category.RENDER, -1);
      i = this;
      this.transitionTime.visibleWhen(this.smoothTransition::getValue);
      this.rememberMagnification.visibleWhen(this.scrollZoom::getValue);
      this.zoomKey.onChanged(var1 -> this.kAsVoid());
      this.activation.onChanged(var1 -> this.kAsVoid());
      this.magnification.onChanged(var1 -> this.kAsVoid());
   }

   public static Zoom JAsZoom() {
      return i;
   }

   public List<Setting<?>> jAsList() {
      return List.of(this.zoomKey, this.activation, this.scrollZoom, this.rememberMagnification);
   }

   public List<Setting<?>> FAsList() {
      return List.of(this.magnification, this.reduceSensitivity, this.smoothTransition, this.transitionTime);
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.RENDER_ONLY);
   }

   @Override
   public void onEnable() {
      this.kAsVoid();
      ClientLog.XAsVoid("Zoom", "Ready; activation={}, key={}", this.activation.getValue(), this.zoomKey.getValue());
   }

   @Override
   public void onDisable() {
      this.kAsVoid();
   }

   @Override
   public void onTick() {
      this.KAsVoid();
   }

   private void kAsVoid() {
      this.m.YAsVoid(true);
      this.Y = this.U = null;
      this.d = false;
   }

   private boolean oAsBoolean() {
      MinecraftClient var1 = getMc();
      return this.isRuntimeActive()
         && var1 != null
         && var1.player != null
         && var1.world != null
         && var1.player.isAlive()
         && !var1.player.isSleeping()
         && !var1.player.isUsingSpyglass()
         && var1.currentScreen == null
         && var1.getOverlay() == null
         && var1.isWindowFocused();
   }

   private void KAsVoid() {
      MinecraftClient var1 = getMc();
      if (!this.oAsBoolean()) {
         this.kAsVoid();
      } else {
         if (this.Y != var1.player || this.U != var1.world) {
            this.m.YAsVoid(true);
            this.Y = var1.player;
            this.U = var1.world;
         }

         boolean var2 = KeybindNames.sAsBoolean(var1.getWindow().getHandle(), this.zoomKey.getValue());
         this.m.LAsVoid(true, var2, this.activation.pAsBoolean("Hold"), this.magnification.getValue(), this.rememberMagnification.getValue());
         if (this.d != this.m.uAsBoolean()) {
            this.d = this.m.uAsBoolean();
            ClientLog.XAsVoid("Zoom", "{}; magnification={}", this.d ? "Engaged" : "Released", this.m.UAsDouble());
         }

         boolean var3 = this.smoothTransition.getValue();
         LarpClient var4 = LarpClient.CAsLarpClient();
         if (var4 != null && var4.fAsBo() != null) {
            var3 &= var4.fAsBo().yAsN7().Z;
         }

         this.m.BAsDouble(System.nanoTime(), this.transitionTime.getValue(), var3);
      }
   }

   public static float lAsFloat(float var0, boolean var1) {
      if (i != null && i.isEnabled()) {
         Zoom var2 = i;
         if (var2 != null && !var1) {
            var2.KAsVoid();
            return mg.AAsFloat(var0, var2.m.SAsDouble());
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static double EAsDouble() {
      if (i != null && i.isEnabled()) {
         Zoom var0 = i;
         if (var0 == null) {
            return 1.0;
         } else {
            var0.KAsVoid();
            return var0.reduceSensitivity.getValue() ? var0.m.SAsDouble() : 1.0;
         }
      } else {
         return 1.0;
      }
   }

   public static boolean hAsBoolean(double var0) {
      if (i != null && i.isEnabled()) {
         Zoom var2 = i;
         if (var2 == null) {
            return false;
         } else {
            var2.KAsVoid();
            return var2.scrollZoom.getValue() && var2.m.OAsBoolean(var0);
         }
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         return this.m.uAsBoolean() ? String.format(Locale.ROOT, "%.2fx", this.m.UAsDouble()) : "Waiting for zoom key";
      }
   }
}
