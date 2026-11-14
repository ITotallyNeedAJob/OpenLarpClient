package com.larpclient.update;

import com.larp.updater.UpdateManifest;
import com.larpclient.ClientLog;
import com.larpclient.ClientPreferences;
import com.larpclient.LarpClient;
import com.larpclient.ProfileManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Util;

public final class UpdateRuntime {
   private static UpdateService service;
   private static String unavailable = "Updates are unavailable. Use the official downloads page.";
   private static String dismissed = "";
   private static String shownReceipt = "";
   private static final int INSTALLER_LIMIT = 8388608;

   private UpdateRuntime() {
   }

   public static synchronized void start() {
   }

   public static UpdateService service() {
      return service;
   }

   public static String unavailable() {
      return unavailable;
   }

   private static ClientPreferences preferences() {
      return LarpClient.CAsLarpClient().fAsBo().yAsN7();
   }

   public static void open(Screen var0) {
      MinecraftClient.getInstance().setScreen(new UpdateScreen(var0));
   }

   public static void officialDownloads() {
      Util.getOperatingSystem().open(URI.create("https://larpclient.com/download"));
   }

   public static boolean automatic(boolean var0) {
      ProfileManager var1 = LarpClient.CAsLarpClient().fAsBo();
      boolean var2 = preferences().S;
      preferences().S = var0;
      if (!var1.jAsBoolean()) {
         preferences().S = var2;
         return false;
      } else {
         if (var0 && service != null) {
            service.check();
         }

         return true;
      }
   }

   public static boolean skip() {
      if (service != null && service.snapshot().release() != null) {
         ProfileManager var0 = LarpClient.CAsLarpClient().fAsBo();
         String var1 = preferences().J;
         preferences().J = service.snapshot().release().releaseId();
         if (!var0.jAsBoolean()) {
            preferences().J = var1;
            return false;
         } else {
            later();
            return true;
         }
      } else {
         return false;
      }
   }

   public static void later() {
      dismissed = noticeKey();
   }

   public static boolean notice() {
      return UpdateNotices.visible(noticeKey(), dismissed, preferences().J);
   }

   private static String noticeKey() {
      return UpdateNotices.key(service == null ? null : service.snapshot(), preferences().c, shownReceipt);
   }

   public static String noticeAction() {
      return noticeKey().startsWith("installed:") ? "What'SkyChunkCache new" : "View update";
   }

   public static String noticeDetail() {
      if (noticeKey().startsWith("installed:")) {
         return "What'SkyChunkCache new in this version";
      } else {
         UpdateManifest.Release var0 = service == null ? null : service.snapshot().release();
         return var0 == null ? "Client updates" : "Version " + var0.version() + " / " + var0.minecraft();
      }
   }

   public static String noticeTitle() {
      if (service == null) {
         return "Updates";
      } else {
         UpdateService.Snapshot var0 = service.snapshot();
         if (noticeKey().startsWith("installed:")) {
            return var0.installedNotice().startsWith("local-restore:") ? "Previous build restored" : "Updated to " + service.identity().version();
         } else {
            return switch (var0.state()) {
               case QUEUED -> "Updates on close";
               case READY -> "Update downloaded";
               case DOWNLOADING -> "Downloading update";
               default -> "Update available";
            };
         }
      }
   }

   public static void noticeShown() {
      if (noticeKey().startsWith("installed:")) {
         String var0 = service.snapshot().installedNotice();
         if (!var0.equals(shownReceipt)) {
            shownReceipt = var0;
            if (!var0.equals(preferences().c)) {
               ProfileManager var1 = LarpClient.CAsLarpClient().fAsBo();
               String var2 = preferences().c;
               preferences().c = var0;
               if (!var1.jAsBoolean()) {
                  preferences().c = var2;
               }
            }
         }
      }
   }

   public static void stop() {
      if (service != null) {
         boolean var0 = service.snapshot().state() == UpdateService.State.QUEUED;
         service.close();
         if (var0) {
            try (InputStream var1 = UpdateRuntime.class.getResourceAsStream("/assets/larp-client/updates/LarpUpdater.jar")) {
               if (var1 == null) {
                  throw new IOException("Bundled installer is missing");
               }

               byte[] var2 = var1.readNBytes(8388609);
               if (var2.length > 8388608) {
                  throw new IOException("Bundled installer size limit");
               }

               service.launchInstaller(var2, installerJava());
               ClientLog.GAsVoid(
                  "Updates",
                  "Installer started; version {} installs after Minecraft exits (config/larp-client/updates/installer.log)",
                  service.snapshot().release().version()
               );
            } catch (Exception var61) {
               ClientLog.lAsVoid("Updates", "Could not start the installer; the approved update stays queued for the next close", var61);
            }
         }
      }
   }

   static Path installerJava() {
      Path var0 = ProcessHandle.current()
         .info()
         .command()
         .map(var0x -> Path.of(var0x))
         .orElseGet(() -> Path.of(System.getProperty("java.home"), "bin", "java"));
      Path var1 = var0.resolveSibling("javaw.exe");
      return var0.getFileName().toString().equalsIgnoreCase("java.exe") && Files.isRegularFile(var1) ? var1 : var0;
   }
}
