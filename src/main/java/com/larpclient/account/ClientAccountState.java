package com.larpclient.account;

import com.larpclient.ClientLog;
import com.larpclient.SessionManager;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.minecraft.UserApiService.UserProperties;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.realms.RealmsAvailability;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.client.realms.RealmsPeriodicCheckers;
import net.minecraft.client.session.ProfileKeys;
import net.minecraft.client.session.Session;
import net.minecraft.client.session.report.AbuseReportContext;
import net.minecraft.client.session.report.ReporterEnvironment;
import net.minecraft.client.session.telemetry.TelemetryManager;
import net.minecraft.util.ApiServices;

final class ClientAccountState implements AutoCloseable {
   private final Session user;
   private final ApiServices services;
   private final ProfileResult profile;
   private final UserApiService api;
   private final UserProperties properties;
   private final SocialInteractionsManager social;
   private final TelemetryManager telemetry;
   private final ProfileKeys keys;
   private final AbuseReportContext reporting;
   private final RealmsClient realms;
   private final RealmsPeriodicCheckers realmsData;
   private boolean installed;

   private ClientAccountState(
      MinecraftClient var1,
      Session var2,
      ApiServices var3,
      ProfileResult var4,
      UserApiService var5,
      UserProperties var6,
      SocialInteractionsManager var7,
      TelemetryManager var8
   ) {
      this.user = var2;
      this.services = var3;
      this.profile = var4;
      this.api = var5;
      this.properties = var6;
      this.social = var7;
      this.telemetry = var8;
      this.keys = ProfileKeys.create(var5, var2, var1.runDirectory.toPath());
      this.reporting = AbuseReportContext.create(ReporterEnvironment.ofIntegratedServer(), var5);
      this.realms = new RealmsClient(var2.getSessionId(), var2.getUsername(), var1);
      this.realmsData = new RealmsPeriodicCheckers(this.realms);
   }

   static ClientAccountState prepare(MinecraftClient var0, Session var1) throws Exception {
      YggdrasilAuthenticationService var2 = new YggdrasilAuthenticationService(var0.getNetworkProxy());
      ApiServices var3 = ApiServices.create(var2, var0.runDirectory);
      ProfileResult var4 = var3.sessionService().fetchProfile(var1.getUuidOrNull(), true);
      if (var4 == null) {
         throw new IOException("Minecraft profile is unavailable. Retry later.");
      } else {
         UserApiService var5 = var2.createUserApiService(var1.getAccessToken());
         UserProperties var6 = var5.fetchProperties();
         if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
         } else {
            TelemetryManager var7 = null;

            try {
               SocialInteractionsManager var8 = new SocialInteractionsManager(var0, var5);
               var7 = new TelemetryManager(var0, var5, var1);
               return new ClientAccountState(var0, var1, var3, var4, var5, var6, var8, var7);
            } catch (Throwable var91) {
               if (var7 != null) {
                  var7.close();
               }

               throw var91;
            }
         }
      }
   }

   static boolean outsideWorld(MinecraftClient var0) {
      return var0.world == null
         && var0.player == null
         && var0.getNetworkHandler() == null
         && var0.getServer() == null
         && var0.interactionManager == null
         && var0.getCameraEntity() == null;
   }

   void install(MinecraftClient var1) {
      if (var1.isOnThread() && outsideWorld(var1)) {
         TelemetryManager var2 = var1.telemetryManager;
         var1.apiServices = this.services;
         var1.gameProfileFuture = CompletableFuture.completedFuture(this.profile);
         var1.userApiService = this.api;
         var1.userPropertiesFuture = CompletableFuture.completedFuture(this.properties);
         var1.socialInteractionsManager = this.social;
         var1.telemetryManager = this.telemetry;
         var1.profileKeys = this.keys;
         var1.abuseReportContext = this.reporting;
         var1.realmsPeriodicCheckers = this.realmsData;
         RealmsClient.instance = this.realms;
         RealmsAvailability.currentFuture = null;
         var1.session = this.user;
         this.installed = true;

         try {
            var2.close();
         } catch (RuntimeException var5) {
            ClientLog.oAsVoid("Accounts", "Previous account service cleanup failed ({}); new account remains active", var5.getClass().getSimpleName());
         }

         SessionManager.GAsVoid();
         ClientLog.GAsVoid("Accounts", "Game account changed; existing client license retained, authenticated={}", SessionManager.HAsBoolean());

         try {
            var1.updateWindowTitle();
         } catch (RuntimeException var4) {
            ClientLog.oAsVoid("Accounts", "Window title refresh failed ({}); account switch completed", var4.getClass().getSimpleName());
         }

         ClientLog.GAsVoid("Accounts", "Account applied; profile, signing, privacy, friends and Realms refreshed");
      } else {
         throw new IllegalStateException("Account switching requires the main menu.");
      }
   }

   @Override
   public void close() {
      if (!this.installed) {
         this.telemetry.close();
      }
   }
}
