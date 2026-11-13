package com.larpclient.module;

import com.larp.obf.AuthorizationChangedException;
import com.larp.obf.RuntimeAdmission;
import com.larpclient.AuthorizationState;
import com.larpclient.ClientLog;
import com.larpclient.KeybindNames;
import com.larpclient.SessionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public abstract class Module {
   private final String name;
   private final String description;
   private final Category category;
   private int keyBind;
   private boolean enabled;
   private boolean pendingEnable;
   private boolean heldByBind;
   private ModeSetting keybindMode;
   private final List<Setting<?>> settings = new ArrayList<>();
   protected static MinecraftClient mc = null;

   protected static MinecraftClient getMc() {
      if (mc == null) {
         mc = MinecraftClient.getInstance();
      }

      return mc;
   }

   public Module(String var1, String var2, Category var3, int var4) {
      this.name = var1;
      this.description = var2;
      this.category = var3;
      this.keyBind = var4;
      this.enabled = false;
   }

   public void toggle() {
      if (this.enabled) {
         this.disable();
      } else {
         this.enable();
      }
   }

   public void enable() {
      if (!this.enabled) {
         this.enabled = true;
         this.pendingEnable = true;
         this.activatePending();
         if (this.enabled) {
            ClientLog.GAsVoid("Module", "Enabled {}", this.name);
         }
      }
   }

   public void activatePending() {
      if (this.enabled && this.pendingEnable) {
         boolean var1 = this.isOperational();
         int var2 = var1 && this.isInGame() ? 1 : 0;
         int var3 = var2 != 0 && (getMc().player.isAlive() || this.canActivateWhileDead()) ? 1 : 0;
         int var4 = (this.enabled ? 1 : 0) | (this.pendingEnable ? 2 : 0) | (var1 ? 4 : 0) | (var2 != 0 ? 8 : 0) | (var3 != 0 ? 16 : 0);
         if (RuntimeAdmission.m4(var4)) {
            this.invokeActivation();
         }
      }
   }

   private void invokeActivation() {
      this.pendingEnable = false;

      try {
         this.onEnable();
      } catch (AuthorizationChangedException var4) {
         this.pendingEnable = true;
         ClientLog.XAsVoid("Auth", "Deferred {} activation until authorization renews", this.name);
      } catch (RuntimeException var5) {
         this.enabled = false;

         try {
            this.onDisable();
         } catch (RuntimeException var3) {
            var5.addSuppressed(var3);
         }

         ClientLog.lAsVoid("Module", "Enable failed: " + this.name, var5);
      }
   }

   public void disable() {
      this.heldByBind = false;
      if (this.enabled) {
         this.enabled = false;

         try {
            if (!this.pendingEnable) {
               this.onDisable();
            }
         } catch (RuntimeException var2) {
            ClientLog.lAsVoid("Module", "Disable failed: " + this.name, var2);
         }

         this.pendingEnable = false;
         ClientLog.GAsVoid("Module", "Disabled {}", this.name);
      }
   }

   public void suspendForDisconnect() {
      if (this.heldByBind) {
         this.disable();
      } else if (this.enabled && !this.pendingEnable) {
         try {
            this.onDisable();
         } catch (RuntimeException var2) {
            ClientLog.lAsVoid("Module", "Disconnect cleanup failed: " + this.name, var2);
         }

         this.pendingEnable = true;
      }
   }

   protected boolean canActivateWhileDead() {
      return false;
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void onTick() {
   }

   public boolean allowsScreenInteractions() {
      return false;
   }

   public Module.Type_s getTickPhase() {
      return Module.Type_s.SIMULATION;
   }

   protected <T extends Setting<?>> T addSetting(T var1) {
      this.settings.add(var1);
      return var1;
   }

   public List<Setting<?>> getSettings() {
      return this.settings;
   }

   public boolean hasSettings() {
      return !this.settings.isEmpty();
   }

   protected ClientPlayerEntity getPlayer() {
      MinecraftClient var1 = getMc();
      return var1 != null ? var1.player : null;
   }

   protected boolean isInGame() {
      MinecraftClient var1 = getMc();
      return var1 != null && var1.player != null && var1.world != null;
   }

   public String getName() {
      return this.name;
   }

   public String getDisplayName() {
      return this.name;
   }

   public List<String> getSearchAliases() {
      return List.of();
   }

   public String getDescription() {
      return this.description;
   }

   public Category getCategory() {
      return this.category;
   }

   public int getKeyBind() {
      return this.keyBind;
   }

   public void setKeyBind(int var1) {
      if (var1 != this.keyBind) {
         this.endHeldBind("Keybind changed");
      }

      this.keyBind = var1;
   }

   public final void offerHoldKeybind() {
      if (this.keybindMode() == null) {
         this.keybindMode = this.addSetting(
            new ModeSetting(
               "Keybind Mode", "Toggle switches the module with each press; Hold keeps it on only while the key is held", "Toggle", "Toggle", "Hold"
            )
         );
         this.keybindMode.onChanged(var1 -> this.endHeldBind("Keybind Mode changed"));
      }
   }

   public ModeSetting keybindMode() {
      return this.keybindMode;
   }

   public boolean holdsKeybind() {
      return this.keybindMode != null && this.keybindMode.pAsBoolean("Hold");
   }

   public boolean activateHeldBind() {
      MinecraftClient var1 = getMc();
      if (this.holdsKeybind() && var1 != null && var1.getWindow() != null && KeybindNames.sAsBoolean(var1.getWindow().getHandle(), this.keyBind)) {
         boolean var2 = this.enabled;
         this.heldByBind = true;
         if (var2) {
            ClientLog.XAsVoid("Input", "{} shortcut now holds the enabled module", this.name);
            return false;
         } else {
            this.enable();
            if (!this.enabled) {
               this.heldByBind = false;
            }

            return this.enabled;
         }
      } else {
         return false;
      }
   }

   public void validateHeldBind() {
      if (this.heldByBind) {
         if (!this.enabled) {
            this.heldByBind = false;
         } else {
            MinecraftClient var1 = getMc();
            String var2 = null;
            if (!this.holdsKeybind()) {
               var2 = "Keybind Mode changed";
            } else if (var1.player == null || var1.world == null || !var1.player.isAlive()) {
               var2 = "no live player";
            } else if (var1.currentScreen != null || var1.getOverlay() != null) {
               var2 = "screen opened";
            } else if (!var1.isWindowFocused()) {
               var2 = "window lost focus";
            } else if (!SessionManager.HAsBoolean()) {
               var2 = "authorization unavailable";
            } else if (!KeybindNames.sAsBoolean(var1.getWindow().getHandle(), this.keyBind)) {
               var2 = "key released";
            }

            if (var2 != null) {
               this.endHeldBind(var2);
            }
         }
      }
   }

   private void endHeldBind(String var1) {
      if (this.heldByBind) {
         ClientLog.XAsVoid("Input", "{} hold ended: {}", this.name, var1);
         this.disable();
      }
   }

   public boolean isEnabledPreference() {
      return this.enabled && !this.heldByBind;
   }

   public boolean isRuntimeActive() {
      return this.isEnabled() ? this.isOperational() && !this.pendingEnable : false;
   }

   public boolean isOperational() {
      return this.enabled && AuthorizationState.ZAsBoolean() && RuntimeAdmission.m1();
   }

   public String getStatus() {
      return "";
   }

   public Set<k> getCurrentNetworkImpacts() {
      return this.getNetworkImpacts();
   }

   public Set<k> getNetworkImpacts() {
      return Set.of();
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      if (var1) {
         this.enable();
      } else {
         this.disable();
      }
   }

   public static enum Type_s {
      CAMERA,
      INTENT,
      SIMULATION,
      CONSTRAINT;

      private Type_s() {
      }
   }
}
