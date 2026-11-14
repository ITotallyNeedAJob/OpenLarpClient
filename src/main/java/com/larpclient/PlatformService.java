package com.larpclient;

import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpRequest.Builder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public final class PlatformService {
   public static final PlatformService AuraModule = new PlatformService();
   public final PlatformOrigin o;
   private volatile boolean Q;
   private volatile boolean z;
   private volatile boolean n;
   private volatile ProfileShareCodec.Type_m V;
   private volatile long L;
   private CompletableFuture<JsonObject> h;

   public PlatformService() {
      this(new PlatformOrigin(PlatformOrigin.wAsURI(), SessionManager::sAsXe));
   }

   public PlatformService(PlatformOrigin var1) {
      this.o = var1;
   }

   public static void wAsVoid(Builder var0, URI var1) {
      if ("https".equals(var1.getScheme())
         && "larpclient.com".equals(var1.getHost())
         && (var1.getPort() == -1 || var1.getPort() == 443)
         && var1.getUserInfo() == null
         && var1.getRawQuery() == null
         && var1.getRawFragment() == null
         && var1.getRawPath().matches("/api/updates/artifacts/[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
         var0.uri(var1).GET();
         SessionManager.sAsXe().yAsVoid(var0, "GET", var1, new byte[0]);
      } else {
         throw new IllegalArgumentException("Update credentials require the official artifact endpoint");
      }
   }

   public ProfileShareCodec.Type_m GAsWv_Type_m() {
      return System.currentTimeMillis() - this.L < 300000L ? this.V : null;
   }

   public boolean SAsBoolean() {
      return this.n && !this.z;
   }

   public synchronized void WAsVoid() {
      if (!this.Q) {
         this.Q = true;
         this.YAsCompletableFuture();
         this.JAsCompletableFuture().exceptionally(var0 -> null);
      }
   }

   public synchronized CompletableFuture<JsonObject> YAsCompletableFuture() {
      if (this.h != null && (!this.h.isDone() || System.currentTimeMillis() - this.L < 60000L)) {
         return this.h;
      } else {
         this.L = System.currentTimeMillis();
         CompletableFuture var1 = this.o.cAsCompletableFuture("/api/status", false);
         this.h = var1.thenApply(var1x -> {
            this.V = ProfileShareCodec.AAsWv_Type_m((JsonObject)var1x);
            ClientLog.XAsVoid("Status", "Service status received");
            return (JsonObject)var1x;
         });
         this.h.whenComplete((var2, var3) -> {
            if (var3 != null) {
               this.V = null;
            }

            if (this.h.isCancelled()) {
               var1.cancel(true);
            }
         });
         return this.h;
      }
   }

   public CompletableFuture<JsonObject> JAsCompletableFuture() {
      return this.oAsCompletableFuture("GET", "/api/profiles", null, null);
   }

   public CompletableFuture<JsonObject> oAsCompletableFuture(String var1, String var2, JsonObject var3, String var4) {
      if (this.z) {
         return CompletableFuture.failedFuture(new AuthException(403, "FORBIDDEN"));
      } else {
         CompletableFuture var5 = this.o.FAsCompletableFuture(var1, var2, var3, true, var4);
         CompletableFuture var6 = var5.whenComplete((var3x, var4x) -> {
            if (var4x == null) {
               if (var2.equals("/api/profiles") && var1.equals("GET")) {
                  try {
                     ProfileShareCodec.dAsList((JsonObject)var3x, true);
                     this.n = true;
                  } catch (RuntimeException var6x) {
                     this.n = false;
                  }
               }
            } else if (BAsAh((Throwable)var4x).vAsInt() == 403) {
               this.z = true;
               this.n = false;
            }
         });
         var6.whenComplete((var2x, var3x) -> {
            if (var6.isCancelled()) {
               var5.cancel(true);
            }
         });
         return var6;
      }
   }

   public static AuthException BAsAh(Throwable var0) {
      while ((var0 instanceof CompletionException || var0 instanceof ExecutionException) && var0.getCause() != null) {
         var0 = var0.getCause();
      }

      return var0 instanceof AuthException var1 ? var1 : PlatformOrigin.ZAsAh(var0);
   }

   public String SAsString(Throwable var1) {
      AuthException var2 = BAsAh(var1);
      ProfileShareCodec.Type_m var3 = this.GAsWv_Type_m();
      return var3 != null && var3.authIssue() && (var2.vAsInt() == 401 || var2.vAsInt() == 503 || var2.GAsString().equals("NETWORK"))
         ? var2.getMessage() + " Larp authentication is currently reporting PetEffectRenderer service issue."
         : var2.getMessage();
   }
}
