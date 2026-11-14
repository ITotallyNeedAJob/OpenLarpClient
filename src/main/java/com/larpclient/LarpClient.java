package com.larpclient;

import com.larpclient.account.MicrosoftAccounts;
import com.larpclient.module.AimAssist;
import com.larpclient.module.AttackRaycast;
import com.larpclient.module.AutoPearlCatch;
import com.larpclient.module.BooleanSetting;
import com.larpclient.module.ClickPulse;
import com.larpclient.module.CustomCrosshair;
import com.larpclient.module.Freecam;
import com.larpclient.module.Freelook;
import com.larpclient.module.LungeSwap;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.Module;
import com.larpclient.module.ModuleManager;
import com.larpclient.module.RodSwap;
import com.larpclient.module.Setting;
import com.larpclient.module.SwordModels;
import com.larpclient.module.WTap;
import com.larpclient.module.WindChargeHop;
import com.larpclient.module.WorldMenu;
import com.larpclient.update.UpdateRuntime;
import com.larpclient.update.UpdateScreen;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.client.util.InputUtil.Type;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LarpClient implements ClientModInitializer {
   public static final String I = "larp-client";
   public static final String C = BuildMetadata.I;
   public static final Logger L = LoggerFactory.getLogger("larp-client");
   public static boolean F = false;
   private static LarpClient r;
   private ModuleManager E;
   private ProfileManager l;
   private DiscordRpcManager d;
   private HudRenderer T;
   private KeyBinding openGuiKey;
   private KeyBinding z;
   private final ShortcutTracker<Module> Y = new ShortcutTracker<>(349);
   private final ShortcutTracker<SettingShortcuts.Type_x> w = new ShortcutTracker<>(349);
   private boolean LineSegment = false;
   private boolean R = false;
   private volatile boolean U;
   public static final Category TextLayout = Category.create(Identifier.of("larp-client", "main"));
   public static final Category BloomUtility = Category.create(Identifier.of("larp-client", "editing"));

   public LarpClient() {
   }

   public void onInitializeClient() {
      r = this;
      L.info("LarpClient v{} by Tobias - loading...", C);
      SessionManager.HAsVoid();
      ActivityReporter.mAsVoid();
      this.E = new ModuleManager();
      this.E.aAsVoid();
      this.l = new ProfileManager();
      this.l.IAsVoid();
      MicrosoftAccounts.INSTANCE.start(MinecraftClient.getInstance());
      UpdateRuntime.start();
      this.d = new DiscordRpcManager(this.l.yAsN7().u);
      this.T = new HudRenderer();
      this.T.lAsVoid();
      NotificationHud.GAsW8().nAsVoid();
      AttackRaycast.kAsVoid();
      com.larpclient.module.g.hAsVoid();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var1 -> {
         this.d.close();
         ActivityReporter.lAsVoid();
         this.U = true;
         this.wAsVoid();
         this.l.hAsVoid();
         SessionManager.XAsVoid();
         MicrosoftAccounts.INSTANCE.stop();
         UpdateRuntime.stop();
      });
      WetReflectionEffect.xAsVoid();
      SharedRenderEffects.sAsVoid();
      this.openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.larp-client.open_gui", Type.KEYSYM, 344, j));
      this.z = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.larp-client.open_editor", Type.KEYSYM, -1, b));
      ClientLog.GAsVoid("Input", "Module shortcuts use ClickGUI/profile keybinds only; legacy Minecraft module shortcuts are ignored");
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var1, var2) -> {
         ActivityReporter.bAsVoid();
         if (!this.U) {
            var2.execute(() -> {
               if (!this.U && (var2.getNetworkHandler() == null || var2.getNetworkHandler() == var1)) {
                  this.wAsVoid();
               }
            });
         }
      });
      ClientTickEvents.END_CLIENT_TICK.register(this::yAsVoid);
      L.info("LarpClient v{} loaded! {} modules registered.", C, this.E.NAsList().size());
      L.info("Press RIGHT SHIFT to open the GUI.");
   }

   public void tAsVoid(boolean var1) {
      if (this.d != null) {
         this.d.rAsVoid(var1);
      }
   }

   private void wAsVoid() {
      this.Y.gAsVoid();
      this.w.gAsVoid();

      for (Module var2 : this.E.NAsList()) {
         var2.suspendForDisconnect();
      }

      this.l.lAsVoid();
   }

   public void vAsVoid(int var1) {
      KeystrokesHud.AAsVoid(var1);
      CustomCrosshair.MAsVoid(var1);
      ClickPulse.HAsVoid(var1);
      int var2 = KeybindNames.ZAsInt(var1);
      if (var2 != -1) {
         if (WorldMenu.FAsBoolean()) {
            this.Y.qAsVoid(var2);
            this.w.qAsVoid(var2);
         } else {
            this.OAsVoid(var2);
         }
      }
   }

   public void OAsVoid(int var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (!this.U
         && SessionManager.HAsBoolean()
         && var2.player != null
         && var2.currentScreen == null
         && var2.getOverlay() == null
         && var2.isWindowFocused()
         && !WorldMenu.IAsBoolean()) {
         this.Y.CAsVoid(var1);
         this.w.CAsVoid(var1);
         LungeSwap.SAsVoid(var1);
         RodSwap.WAsVoid(var1);
         AutoPearlCatch.JAsVoid(var1);
         WindChargeHop.qAsVoid(var1);
         SwordModels var3 = SwordModels.vAsSwordModels();
         if (var3 != null) {
            var3.oAsVoid(var1);
         }
      } else {
         this.Y.qAsVoid(var1);
         this.w.qAsVoid(var1);
      }
   }

   private void yAsVoid(MinecraftClient var1) {
      SessionManager.GAsVoid();
      Freecam.XAsVoid();
      Freelook.lAsVoid();

      for (Module var3 : this.E.NAsList()) {
         var3.validateHeldBind();
      }

      this.E.OAsVoid();
      AimAssist.aAsVoid();
      WTap.EAsVoid();
      HudRenderer.CAsVoid(var1);
      ActivityReporter.HAsVoid(var1);
      DisplayAliasManager.kAsVoid();
      if (SessionManager.HAsBoolean()) {
         PlatformService.x.WAsVoid();
      }

      if (var1.currentScreen instanceof TitleScreen) {
         F = true;
      }

      if (!this.R && var1.currentScreen instanceof TitleScreen) {
         this.R = true;
         if (!SessionManager.HAsBoolean()) {
            L.info("Not authenticated - redirecting to AuthScreen");
            var1.setScreen(new ActivationScreen());
            return;
         }
      }

      if (this.R && var1.currentScreen instanceof TitleScreen && !SessionManager.HAsBoolean()) {
         var1.setScreen(new ActivationScreen());
      } else {
         this.Y.xAsVoid();
         long var13 = var1.getWindow().getHandle();

         for (Module var5 : this.E.NAsList()) {
            int var6 = var5.getKeyBind();
            if (KeybindNames.hAsBoolean(var6)) {
               this.Y.AAsVoid(var5, var6, KeybindNames.sAsBoolean(var13, var6));
            }
         }

         List var14 = this.Y.CAsList();
         this.w.xAsVoid();

         for (SettingShortcuts.Type_x var17 : this.l.yAsN7().k.oAsList(this.E.NAsList())) {
            this.w.AAsVoid(var17, var17.code(), KeybindNames.sAsBoolean(var13, var17.code()));
         }

         List var16 = this.w.CAsList();
         int var18 = var1.currentScreen == null && var1.getOverlay() == null && var1.isWindowFocused() && !WorldMenu.IAsBoolean() ? 1 : 0;
         boolean var7 = this.openGuiKey.wasPressed();
         byte var8 = 0;

         while (this.z.wasPressed()) {
            var8 = 1;
         }

         if (!SessionManager.HAsBoolean()) {
            if (!SessionManager.gAsBoolean() && !(var1.currentScreen instanceof ActivationScreen) && !(var1.currentScreen instanceof UpdateScreen)) {
               var1.setScreen(new ActivationScreen());
               ClientLog.oAsVoid("Auth", "Authorization unavailable; modules suspended and preferences retained");
            }
         } else if (var1.player != null) {
            if (var8 != 0 && var18 != 0 && !var7 && var1.world != null) {
               var1.setScreen(new HudEditorScreen(null));
            } else {
               if (!this.e) {
                  this.e = true;
                  var1.options.getFovEffectScale().setValue(0.0);
                  var1.options.getBobView().setValue(false);
                  L.info("Applied LARP defaults: dynamic FOV off, view bobbing off");
               }

               if (var7) {
                  if (var1.currentScreen instanceof ClickGuiScreen var9) {
                     var9.close();
                  } else if (var1.currentScreen instanceof ModuleSearchPanel var10) {
                     var10.close();
                  } else if (!WorldMenu.nAsBoolean()) {
                     var1.setScreen(ModuleSearchPanel.MAsScreen(null));
                  }
               }

               if (var18 != 0 && !var7 && var1.currentScreen == null) {
                  for (Module var21 : (Iterable<Module>) (var14)) {
                     label175: {
                        if (var21 instanceof Freecam var24 && var24.eAsBoolean()) {
                           if (!var24.IAsBoolean()) {
                              continue;
                           }
                           break label175;
                        }

                        if (var21 instanceof Freelook var12 && var12.bAsBoolean()) {
                           if (!var12.zAsBoolean()) {
                              continue;
                           }
                           break label175;
                        }

                        if (var21.holdsKeybind()) {
                           if (!var21.activateHeldBind()) {
                              continue;
                           }
                        } else {
                           var21.toggle();
                        }
                     }

                     NotificationHud.GAsW8().KAsVoid(var21.getDisplayName(), var21.isEnabled());
                  }

                  for (SettingShortcuts.Type_x var22 : (Iterable<SettingShortcuts.Type_x>) (var16)) {
                     rAsVoid(var22);
                  }
               }
            }
         }
      }
   }

   private static void rAsVoid(SettingShortcuts.Type_x var0) {
      Setting var1 = var0.setting();
      String var2 = var0.module().getDisplayName() + " " + var1.getDisplayName();
      if (!var1.isVisible()) {
         ClientLog.XAsVoid("Input", "Setting shortcut {} skipped while the setting is hidden", var2);
      } else {
         try {
            if (var1 instanceof BooleanSetting var3) {
               var3.toggle();
               NotificationHud.GAsW8().KAsVoid(var2, var3.getValue());
            } else if (var1 instanceof ModeSetting var4) {
               var4.pAsVoid();
               NotificationHud.GAsW8().oAsVoid(var4.getModeLabel(var4.getValue()), var2, NotificationHud.Type_u.INFO, 2000L);
            }

            ClientLog.XAsVoid("Input", "Setting shortcut changed {} to {}", var2, var1.getValueAsString());
         } catch (RuntimeException var51) {
            ClientLog.oAsVoid("Input", "Setting shortcut for {} was rejected: {}", var2, var51.toString());
         }
      }
   }

   public static LarpClient CAsLarpClient() {
      return r;
   }

   public ModuleManager mAsF() {
      return this.E;
   }

   public ProfileManager fAsBo() {
      return this.l;
   }

   public int kAsInt() {
      if (this.openGuiKey == null) {
         return 344;
      } else {
         Key var1 = KeyBindingHelper.getBoundKeyOf(this.openGuiKey);

         return switch (var1.getCategory()) {
            case KEYSYM -> var1.getCode();
            case MOUSE -> KeybindNames.ZAsInt(var1.getCode());
            default -> -1;
         };
      }
   }

   public void sAsVoid(int var1) {
      if (this.openGuiKey != null && var1 >= -1 && var1 <= 348) {
         this.openGuiKey.setBoundKey(KeybindNames.KAsBoolean(var1) ? Type.MOUSE.createFromCode(KeybindNames.hAsInt(var1)) : Type.KEYSYM.createFromCode(var1));
         this.openGuiKey.setPressed(false);
         KeyBinding.updateKeysByCode();

         while (this.openGuiKey.wasPressed()) {
         }

         MinecraftClient.getInstance().options.write();
         ClientLog.GAsVoid("Input", "ClickUI shortcut changed to {}", this.openGuiKey.getBoundKeyLocalizedText().getString());
      }
   }
}
