package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.KeybindNames;
import com.larpclient.SessionManager;
import com.larpclient.ParticleEmitter;
import com.larpclient.ModuleKeybindChecker;
import java.util.Set;
import net.minecraft.client.MinecraftClient;

public class Freelook extends Module {
   private final NumberSetting distance = this.addSetting(new NumberSetting("Distance", "Camera distance", 4.0, 1.0, 10.0, 0.5));
   private final ModeSetting activation = this.addSetting(
      new ModeSetting("Activation", "Toggle the module key or keep it held to look around", "Toggle", "Toggle", "Hold")
   );
   private static Freelook k;
   private boolean W;

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.CAMERA;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(com.larpclient.module.k.VANILLA_INPUT);
   }

   public Freelook() {
      super("Freelook", "Look around without turning your player", Category.RENDER, -1);
      k = this;
      this.activation.onChanged(var1 -> {
         if (this.bAsBoolean() || this.W) {
            this.setEnabled(false);
         }
      });
   }

   public boolean bAsBoolean() {
      return this.activation.pAsBoolean("Hold");
   }

   private boolean EAsBoolean() {
      int var1 = this.getKeyBind();
      return KeybindNames.hAsBoolean(var1) && KeybindNames.sAsBoolean(getMc().getWindow().getHandle(), var1);
   }

   public static boolean HAsBoolean(boolean var0, boolean var1, boolean var2, boolean var3) {
      return var0 && !var1 && var2 && var3;
   }

   public boolean zAsBoolean() {
      if (this.isEnabled()) {
         if (this.bAsBoolean() && this.EAsBoolean()) {
            this.W = true;
            this.enable();
            return this.isEnabled();
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public void enable() {
      if (!this.bAsBoolean() || this.W) {
         super.enable();
      }
   }

   @Override
   public void disable() {
      this.W = false;
      super.disable();
   }

   @Override
   public void suspendForDisconnect() {
      if (this.bAsBoolean()) {
         this.disable();
      } else {
         super.suspendForDisconnect();
      }
   }

   public static void lAsVoid() {
      Freelook var0 = k;
      if (var0 != null && var0.bAsBoolean() && var0.isEnabled()) {
         MinecraftClient var1 = getMc();
         int var2 = var1 != null && var1.player != null && var1.world != null && var1.player.isAlive() ? 1 : 0;
         int var3 = var2 != 0 && var1.currentScreen == null && var1.getOverlay() == null && var1.isWindowFocused() ? 0 : 1;
         if (!HAsBoolean(var2 != 0, var3 != 0, SessionManager.HAsBoolean(), var3 == 0 && var0.EAsBoolean())) {
            var0.setEnabled(false);
            ClientLog.XAsVoid("Freelook", "Hold ended by release, screen, focus or world change; camera returned");
         }
      }
   }

   @Override
   public void setKeyBind(int var1) {
      if (this.bAsBoolean() && var1 != this.getKeyBind()) {
         this.setEnabled(false);
      }

      super.setKeyBind(var1);
   }

   @Override
   public void onEnable() {
      this.OAsVoid();
      ClientLog.XAsVoid("Freelook", "Camera passes through blocks at the selected orbit distance; activation={}", this.activation.getValue());
   }

   @Override
   public void onDisable() {
      this.W = false;
      p5.GAsVoid(p5.Type_u.FREELOOK);
   }

   @Override
   public void onTick() {
      lAsVoid();
      if (this.isRuntimeActive()) {
         this.OAsVoid();
      }
   }

   private void OAsVoid() {
      if (!r6.NAsBoolean("Freecam")) {
         p5.jAsVoid(p5.Type_u.FREELOOK);
      }
   }

   public static boolean HAsBoolean() {
      return k != null && k.isEnabled() ? p5.NAsP5_Type_u() == p5.Type_u.FREELOOK : false;
   }

   public static float DAsFloat() {
      return p5.JAsFloat();
   }

   public static float LAsFloat() {
      return p5.mAsFloat();
   }

   public static float iAsFloat() {
      return k == null ? 4.0F : k.distance.getValue().floatValue();
   }
}
