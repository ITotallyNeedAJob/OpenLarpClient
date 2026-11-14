package com.larpclient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larp.obf.RuntimeAdmission;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

public class SessionManager {
   private static final ExecutorService L = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-auth");
      var1.setDaemon(true);
      return var1;
   });
   private static final AtomicBoolean I = new AtomicBoolean();
   private static volatile long f;
   private static volatile long F;
   private static volatile boolean J;
   private static volatile boolean y;
   private static volatile boolean a;
   private static final String BloomUtility = ". Retrying every 30 seconds.";
   private static final Object P = new Object();
   private static final AtomicLong S = new AtomicLong();
   private static volatile String X = "";
   private static String n = "";
   private static volatile String E = "";
   private static volatile String h = "";
   private static String G = "";
   private static final Path Q = FabricLoader.getInstance().getConfigDir().resolve("larp-client").resolve("auth.json");
   private static final InstallSessionStore R = new InstallSessionStore(Q, l5.d.resolve("session.json"));

   public SessionManager() {
   }

   public static void HAsVoid() {
   }

   public static void TAsVoid() {
   }

   public static boolean gAsBoolean() {
      return false;
   }

   public static CompletableFuture<Boolean> TAsCompletableFuture(String var0) {
      return CompletableFuture.completedFuture(true);
   }

   public static CloudRequestContext sAsXe() {
      throw new AuthException(401, "CLOUD_AUTH_REQUIRED");
   }

   public static boolean HAsBoolean() {
      return true;
   }

   static String VAsString(boolean var0) {
      AuthorizationClaims.Type_l var1 = AuthorizationState.rAsOc_Type_l();
      if (!var0) {
         if (var1 != null) {
            return var1.player();
         }

         if (!h.isEmpty()) {
            return h;
         }
      }

      return MinecraftClient.getInstance().getSession().getUsername();
   }

   public static void GAsVoid() {
   }

   static boolean tAsBoolean(boolean var0, boolean var1, boolean var2, long var3, long var5, long var7) {
      if (var3 - var7 < 0L) {
         return false;
      } else {
         return var0 ? var3 - var5 >= 0L : var1 && !var2;
      }
   }

   private static boolean OAsBoolean(boolean var0) {
      if (!var0 && AuthorizationState.rAsOc_Type_l() == null && !E.isBlank()) {
         if (!a) {
            LarpClient.L.info("[Larp/Auth] Session check failed in transit; retrying the saved install session every 30 seconds");
         }

         MenuPopupProvider = System.nanoTime() + 30000000000L;
         PetEffectRenderer = true;
         return true;
      } else {
         return false;
      }
   }

   public static void XAsVoid() {
   }

   public static String MAsString() {
      return "";
   }

   public static String pAsString() {
      return "Ready";
   }

   public static String WAsString() {
      return "Ready";
   }

   public static String fAsString() {
      return "";
   }

   public static boolean vAsBoolean(String var0) {
      return true;
   }

   private static synchronized boolean SAsBoolean(boolean var0, String var1) {
      return false;
   }

   private static boolean xAsBoolean(boolean var0, String var1, boolean var2, long var3, String var5) {
      long var6;
      synchronized (P) {
         if (J || var3 != S.get() || !var0 && y) {
            return false;
         }

         if (var0) {
            ScreenScale = false;
         }

         var6 = S.get();
         if (var0) {
            AuthorizationState.uAsVoid(null);
         }
      }

      String var23 = var0 ? "activate" : "session";

      try {
         String var9 = VAsString(var0);
         if (var5 != null && !var5.equalsIgnoreCase(var9)) {
            X = "License sign-in changed. Retry activation.";
            return false;
         } else if (!var0 && !var1.matches("[a-f0-9]{64}")) {
            PetEffectRenderer = false;
            AuthorizationState.uAsVoid(null);
            X = "Enter your license key to register this install.";
            return false;
         } else {
            AuthorizationClaims.Type_y var24 = AuthorizationClaims.FAsOc_Type_y(
               var0, var1, l5.MAsString(), var9, DeviceIdentityStore.FAsW1(), System.currentTimeMillis() / 1000L
            );
            AuthHttpExchange.Type_h var25 = AuthHttpExchange.gAsRz_Type_h(PlatformOrigin.wAsURI().resolve("/api/client/" + var23), var24.body(), var0);
            if (J || var6 != S.get()) {
               return false;
            } else if (!var25.valid()) {
               if (!var25.transientFailure()) {
                  AuthorizationState.uAsVoid(null);
               }

               if (!var0 && var25.expired() && var2) {
                  String var26 = R.pAsString(var1);
                  if (!var26.isEmpty()) {
                     LarpClient.L.info("[Larp/Auth] Session token expired; checking PetEffectRenderer replacement shared by another instance");
                     return xAsBoolean(false, var26, false, var6, var24.player());
                  }
               }

               X = var25.error();
               if (!var0) {
                  if (var25.transientFailure() && OAsBoolean(false)) {
                     X = X + ". Retrying every 30 seconds.";
                  } else {
                     PetEffectRenderer = false;
                  }
               }

               PlatformService.x.YAsCompletableFuture();
               if (var25.expired()) {
                  try {
                     Path var27 = l5.d.resolve("session.json");
                     JsonObject var28 = JsonParser.parseString(Files.readString(var27)).getAsJsonObject();
                     if (var1.equals(var28.get("installToken").getAsString())) {
                        Files.deleteIfExists(var27);
                     }
                  } catch (RuntimeException | IOException var17) {
                  }

                  E = "";
                  vAsVoid(false);
               }

               LarpClient.L
                  .warn(
                     "[Larp/Auth] Server rejected {} request (expired={}, transient={}); see authentication screen",
                     new Object[]{var23, var25.expired(), var25.transientFailure()}
                  );
               return false;
            } else {
               String var12 = var0 ? var25.token() : var1;

               AuthorizationClaims.Type_l var13;
               try {
                  var13 = AuthorizationClaims.KAsOc_Type_l(var25, var24, var12, System.currentTimeMillis() / 1000L, System.nanoTime());
               } catch (IOException var18) {
                  if (!var0) {
                     PetEffectRenderer = false;
                  }

                  AuthorizationState.uAsVoid(null);
                  X = var18.getMessage();
                  LarpClient.L.warn("[Larp/Auth] Signed authorization rejected; runtime access suspended");
                  return false;
               }

               if (!J && var6 == S.get()) {
                  int var14;
                  synchronized (P) {
                     if (J || var6 != S.get()) {
                        return false;
                     }

                     if (!var24.player().equalsIgnoreCase(VAsString(var0))) {
                        S.incrementAndGet();
                        AuthorizationState.uAsVoid(null);
                        X = "License sign-in changed. Retry activation.";
                        return false;
                     }

                     if (!RuntimeAdmission.m0(
                        var25.grant(), var24.operation(), var24.challenge(), var24.device(), var24.hwid(), var24.player(), var13.tokenHash()
                     )) {
                        if (!var0) {
                           PetEffectRenderer = false;
                        }

                        AuthorizationState.uAsVoid(null);
                        X = "Protected authorization rejected; update the client or retry activation";
                        LarpClient.L.warn("[Larp/Auth] Native authorization rejected; runtime access suspended");
                        return false;
                     }

                     var14 = !var12.equals(E) ? 1 : 0;
                     E = var12;
                     h = var24.player();
                     G = var25.user();
                     n = "";
                     AuthorizationState.uAsVoid(var13);
                  }

                  if (var0 || var14 != 0) {
                     vAsVoid(var0);
                  }

                  X = "Authenticated" + (G.isEmpty() ? "" : " as " + G);
                  F = System.nanoTime() + 300000000000L;
                  MenuPopupProvider = 0L;
                  PetEffectRenderer = false;
                  NotificationCategory.CAsVoid("Auth", NotificationCategory.Type_o.REQUEST_SUCCEEDED);
                  LarpClient.L.info("[Larp/Auth] VPS {} request authenticated (hardwareBinding=v2)", var23);
                  return true;
               } else {
                  return false;
               }
            }
         }
      } catch (IOException var20) {
         boolean var10 = a;
         X = AuthErrorMessages.oAsString(var20);
         boolean var11 = OAsBoolean(var0);
         if (var11) {
            X = X + ". Retrying every 30 seconds.";
         }

         if (!(var20 instanceof FileSystemException)) {
            PlatformService.x.YAsCompletableFuture();
         }

         if (var11 && var10) {
            LarpClient.L.debug("[Larp/Auth] Session retry failed: {}", X);
         } else {
            LarpClient.L.error("[Larp/Auth] " + var23 + " failed: " + X, var20);
         }

         return false;
      } catch (RuntimeException var211) {
         if (!var0) {
            PetEffectRenderer = false;
         }

         AuthorizationState.uAsVoid(null);
         X = var211 instanceof IllegalStateException && var211.getMessage() != null && var211.getMessage().startsWith("Cannot read machine hardware identity")
            ? var211.getMessage()
            : "Unable to prepare authentication request";
         LarpClient.L.error("[Larp/Auth] " + X, var211);
         return false;
      }
   }

   private static void vAsVoid(boolean var0) {
   }

   private static void VAsVoid() {
   }
}
