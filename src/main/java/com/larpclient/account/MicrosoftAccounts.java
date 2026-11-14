package com.larpclient.account;

import com.larpclient.ClientLog;
import com.larpclient.LarpClient;
import com.larpclient.ProxyChannelHandler;
import com.larpclient.ProxyRoute;
import com.larpclient.ProxyValidation;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import net.fabricmc.loader.api.FabricLoader;
import net.lenni0451.commons.httpclient.HttpClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.session.Session;
import net.minecraft.util.Util;
import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.java.JavaAuthManager;
import net.raphimc.minecraftauth.java.model.MinecraftProfile;
import net.raphimc.minecraftauth.java.model.MinecraftToken;
import net.raphimc.minecraftauth.msa.model.MsaDeviceCode;
import net.raphimc.minecraftauth.msa.service.impl.DeviceCodeMsaAuthService;

public final class MicrosoftAccounts {
   public static final MicrosoftAccounts INSTANCE = new MicrosoftAccounts();
   private AccountVault vault;
   private final ThreadPoolExecutor worker = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(4), var0 -> {
      Thread var1 = new Thread(var0, "Larp Microsoft accounts");
      var1.setDaemon(true);
      return var1;
   });
   private final AtomicLong generation = new AtomicLong();
   private Session launcherUser;
   private Path proxyMarker;
   private Future<?> pending;
   private Screen owner;
   private volatile boolean stopped;
   private boolean busy;
   private boolean error;
   private boolean ready;
   private boolean legacy;
   private boolean stored;
   private List<AccountVault.Entry> snapshot = List.of();
   private List<ProxyValidation> proxySnapshot = List.of();
   private UUID activeProxyId;
   private String status = "";
   private String code = "";
   private URI verification;
   private int revision;

   private MicrosoftAccounts() {
   }

   public void start(MinecraftClient var1) {
      if (this.launcherUser == null) {
         this.launcherUser = var1.getSession();
      }

      if (this.vault == null) {
         Path var2 = FabricLoader.getInstance().getConfigDir().resolve("larp-client/accounts");
         this.vault = new AccountVault(var2);
         this.proxyMarker = var2.resolve("proxy-enabled");
         if (Files.exists(this.proxyMarker, LinkOption.NOFOLLOW_LINKS)) {
            ProxyRoute.iAsVoid("The saved proxy is still loading. Try again in PetEffectRenderer moment.");

            try {
               this.worker.execute(this::restoreProxy);
            } catch (RejectedExecutionException var4) {
               ProxyRoute.iAsVoid("The saved proxy could not be loaded. Open the account list to retry or turn the proxy off.");
            }
         }
      }
   }

   public boolean unlocked() {
      return this.ready;
   }

   public boolean exists() {
      return this.stored;
   }

   public boolean busy() {
      return this.busy;
   }

   public boolean error() {
      return this.error;
   }

   public String status() {
      return this.status;
   }

   public String code() {
      return this.code;
   }

   public int revision() {
      return this.revision;
   }

   public List<AccountVault.Entry> entries() {
      return this.snapshot;
   }

   public List<ProxyValidation> proxies() {
      return this.proxySnapshot;
   }

   public UUID activeProxyId() {
      return this.activeProxyId;
   }

   public boolean canReturn(MinecraftClient var1) {
      return this.launcherUser != null && var1.getSession() != this.launcherUser && !this.launcherUser.getAccessToken().equals("0");
   }

   public Session launcher() {
      return this.launcherUser;
   }

   public boolean legacy() {
      return this.legacy;
   }

   public void open(Screen var1) {
      this.start(MinecraftClient.getInstance());
      this.run(var1, "Loading saved accounts...", () -> {
         this.vault.open();
         this.syncProxy();
         return this.vault.legacyExists();
      }, var1x -> {
         this.legacy = var1x;
         this.message(var1x && this.snapshot.isEmpty() ? "Previous list kept. Add your accounts again." : "", false);
      });
   }

   public void add(Screen var1) {
      if (this.unlocked()) {
         long var2 = this.generation.get() + 1L;
         this.run(var1, "Requesting Microsoft sign-in...", () -> {
            if (this.generation.get() != var2) {
               throw new InterruptedException();
            } else {
               HttpClient var4 = MinecraftAuth.createHttpClient("LarpClient/" + LarpClient.C + " MinecraftAuth/5.0.2");
               JavaAuthManager var5 = JavaAuthManager.create(var4).login(DeviceCodeMsaAuthService::new, (Consumer<MsaDeviceCode>)var4x -> {
                  URI var5x = verificationLink(var4x.getDirectVerificationUri());
                  if (!var4x.getUserCode().matches("[A-Za-z0-9-]{4,16}")) {
                     throw new IllegalArgumentException("Unexpected Microsoft verification code");
                  } else {
                     MinecraftClient.getInstance().execute(() -> {
                        if (this.current(var2, var1)) {
                           this.code = var4x.getUserCode();
                           this.verification = var5x;
                           this.message("Approve this code on Microsoft'SkyChunkCache website.", false);
                           this.openBrowser();
                        }
                     });
                  }
               });
               interrupted();
               MinecraftProfile var6 = (MinecraftProfile)var5.getMinecraftProfile().getUpToDate();
               var5.getMinecraftToken().getUpToDate();
               interrupted();
               if (this.generation.get() != var2) {
                  throw new InterruptedException();
               } else {
                  this.vault.put(new AccountVault.Entry(var6.getId(), var6.getName(), JavaAuthManager.toJson(var5)));
                  return var6.getId();
               }
            }
         }, var1x -> this.message("Microsoft account saved. Select it and choose Switch.", false));
      }
   }

   public void addToken(Screen var1, String var2, Runnable var3) {
      if (this.unlocked() && !this.busy) {
         String var4;
         try {
            var4 = TokenLogin.normalize(var2);
         } catch (IllegalArgumentException var7) {
            this.message(var7.getMessage(), true);
            return;
         }

         MinecraftClient var5 = MinecraftClient.getInstance();
         Session var6 = var5.getSession();
         this.run(var1, "Checking the access token...", () -> {
            TokenLogin.Profile var3x = TokenLogin.profile(var4, var5.getNetworkProxy());
            interrupted();
            AccountVault.Entry var4x = this.vault.entry(var3x.id());
            if (var4x != null && !TokenLogin.saved(var4x.authentication())) {
               throw new AccountVault.Failure(var3x.name() + " is already saved with Microsoft sign-in. Choose Switch on that row.");
            } else {
               this.vault.put(new AccountVault.Entry(var3x.id(), var3x.name(), TokenLogin.state(var4, TokenLogin.expiry(var4))));
               interrupted();
               return ClientAccountState.prepare(var5, new Session(var3x.name(), var3x.id(), var4, Optional.empty(), Optional.empty()));
            }
         }, var4x -> {
            var3.run();
            this.apply(var1, var6, var4x);
         });
      }
   }

   public void switchTo(Screen var1, UUID var2) {
      if (this.ready && !this.busy) {
         AccountVault.Entry var3 = this.snapshot.stream().filter(var1x -> var1x.id().equals(var2)).findFirst().orElse(null);
         if (var3 != null) {
            Session var4 = MinecraftClient.getInstance().getSession();
            if (TokenLogin.saved(var3.authentication())) {
               this.run(var1, "Checking the saved token...", () -> {
                  MinecraftClient var3x = MinecraftClient.getInstance();
                  String var4x = TokenLogin.token(var3.authentication());
                  TokenLogin.Profile var5 = TokenLogin.profile(var4x, var3x.getNetworkProxy());
                  if (!var2.equals(var5.id())) {
                     throw new AccountVault.Failure("This token now belongs to another account. Remove it and add it again.");
                  } else {
                     if (!var5.name().equals(var3.name())) {
                        this.vault.put(new AccountVault.Entry(var2, var5.name(), var3.authentication()));
                     }

                     interrupted();
                     return ClientAccountState.prepare(var3x, new Session(var5.name(), var2, var4x, Optional.empty(), Optional.empty()));
                  }
               }, var3x -> this.apply(var1, var4, var3x));
            } else {
               this.run(
                  var1,
                  "Refreshing Microsoft account...",
                  () -> {
                     HttpClient var3x = MinecraftAuth.createHttpClient("LarpClient/" + LarpClient.C + " MinecraftAuth/5.0.2");
                     JavaAuthManager var4x = JavaAuthManager.fromJson(var3x, var3.authentication());

                     try {
                        var4x.getMinecraftToken().refresh();
                        MinecraftProfile var5 = (MinecraftProfile)var4x.getMinecraftProfile().refresh();
                        if (!var2.equals(var5.getId())) {
                           throw new IOException("The saved account identity changed. Add it again.");
                        } else {
                           this.vault.put(new AccountVault.Entry(var2, var5.getName(), JavaAuthManager.toJson(var4x)));
                           interrupted();
                           return ClientAccountState.prepare(
                              MinecraftClient.getInstance(),
                              new Session(
                                 var5.getName(),
                                 var5.getId(),
                                 ((MinecraftToken)var4x.getMinecraftToken().getUpToDate()).getToken(),
                                 Optional.empty(),
                                 Optional.empty()
                              )
                           );
                        }
                     } catch (Exception var81) {
                        try {
                           MinecraftProfile var6 = (MinecraftProfile)var4x.getMinecraftProfile().getCached();
                           if (var6 != null && var2.equals(var6.getId())) {
                              this.vault.put(new AccountVault.Entry(var2, var6.getName(), JavaAuthManager.toJson(var4x)));
                           }
                        } catch (Exception var71) {
                           ClientLog.oAsVoid("Accounts", "Could not retain refreshed account state; sign in again if refresh fails");
                        }

                        throw var81;
                     }
                  },
                  var3x -> this.apply(var1, var4, var3x)
               );
            }
         }
      }
   }

   public void returnToLauncher(Screen var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (this.canReturn(var2)) {
         Session var3 = var2.getSession();
         this.run(
            var1, "Preparing the launcher account...", () -> ClientAccountState.prepare(var2, this.launcherUser), var3xx -> this.apply(var1, var3, var3xx)
         );
      }
   }

   private void apply(Screen var1, Session var2, ClientAccountState var3) {
      MinecraftClient var4 = MinecraftClient.getInstance();
      ClientAccountState var5 = var3;

      label47: {
         try {
            if (var4.currentScreen == var1 && var4.getSession() == var2 && ClientAccountState.outsideWorld(var4)) {
               var3.install(var4);
               this.message("Account switched.", false);
               break label47;
            }

            this.message("Minecraft changed while signing in. Your current account was retained.", true);
         } catch (Throwable var9) {
            if (var3 != null) {
               try {
                  var5.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (var3 != null) {
            var3.close();
         }

         return;
      }

      if (var3 != null) {
         var3.close();
      }
   }

   public void remove(Screen var1, UUID var2) {
      this.run(var1, "Removing saved account...", () -> {
         this.vault.remove(var2);
         return null;
      }, var1x -> this.message("Saved account removed. Its current Minecraft login, if active, stays signed in.", false));
   }

   public void archive(Screen var1) {
      boolean var2 = ProxyRoute.fAsPy_Type_y() != null || ProxyRoute.jAsString() != null;
      this.run(var1, "Starting PetEffectRenderer new saved list...", () -> {
         this.vault.archive();
         this.vault.open();
         this.syncProxy();
         return null;
      }, var2xx -> this.message("Previous list kept for recovery. Add your accounts again." + (var2 ? " The proxy is off." : ""), false));
   }

   public void saveProxy(Screen var1, ProxyValidation var2, Runnable var3) {
      if (this.unlocked() && !this.busy) {
         this.run(var1, "Testing the proxy...", () -> {
            MicrosoftAccounts.Tested var2x = this.test(var2);
            this.vault.putProxy(var2, true);
            this.marker(true);
            return var2x;
         }, var2x -> {
            var3.run();
            this.activate(var2x);
         });
      }
   }

   public void useProxy(Screen var1, UUID var2) {
      if (this.unlocked() && !this.busy) {
         ProxyValidation var3 = this.proxySnapshot.stream().filter(var1x -> var1x.id().equals(var2)).findFirst().orElse(null);
         if (var3 != null) {
            this.run(var1, "Testing the proxy...", () -> {
               MicrosoftAccounts.Tested var3x = this.test(var3);
               this.vault.selectProxy(var2);
               this.marker(true);
               return var3x;
            }, this::activate);
         }
      }
   }

   public void proxyOff(Screen var1) {
      if (!this.busy) {
         if (!this.ready) {
            ProxyRoute.IAsVoid();

            try {
               this.marker(false);
            } catch (IOException var3) {
               ClientLog.oAsVoid("Proxy", "Could not clear the proxy marker ({})", var3.getClass().getSimpleName());
            }

            this.message("Proxy off for this session. The saved choice returns once the account list opens.", false);
         } else {
            this.run(var1, "Turning the proxy off...", () -> {
               this.vault.selectProxy(null);
               this.marker(false);
               return null;
            }, var1x -> {
               ProxyRoute.IAsVoid();
               this.message("Proxy off. Server traffic connects directly.", false);
            });
         }
      }
   }

   public void removeProxy(Screen var1, UUID var2) {
      if (this.unlocked() && !this.busy) {
         boolean var3 = var2.equals(this.activeProxyId);
         this.run(var1, "Removing the saved proxy...", () -> {
            this.vault.removeProxy(var2);
            if (var3) {
               this.marker(false);
            }

            return null;
         }, var2x -> {
            if (var3) {
               ProxyRoute.IAsVoid();
            }

            this.message(var3 ? "Proxy removed and turned off. Server traffic connects directly." : "Saved proxy removed.", false);
         });
      }
   }

   private MicrosoftAccounts.Tested test(ProxyValidation var1) throws Exception {
      ProxyValidation.Type_y var2;
      try {
         var2 = var1.resolve();
      } catch (UnknownHostException var5) {
         throw new ProxyChannelHandler.Type_r("Proxy " + var1.label() + ": the proxy host could not be found");
      }

      long var3 = ProxyRoute.jAsLong(var2);
      interrupted();
      return new MicrosoftAccounts.Tested(var2, var3);
   }

   private void activate(MicrosoftAccounts.Tested var1) {
      ProxyRoute.EAsVoid(var1.proxy());
      this.message("Proxy on: " + var1.proxy().settings().label() + " (" + var1.millis() + " ms). Server traffic now uses it.", false);
   }

   private void syncProxy() throws IOException {
      ProxyValidation var1 = this.vault.activeProxy();
      if (var1 == null) {
         ProxyRoute.IAsVoid();
         this.marker(false);
      } else {
         ProxyValidation.Type_y var2 = ProxyRoute.fAsPy_Type_y();
         if (var2 != null && var2.settings().equals(var1)) {
            this.marker(true);
         } else {
            try {
               ProxyRoute.EAsVoid(var1.resolve());
               this.marker(true);
            } catch (UnknownHostException var4) {
               ProxyRoute.iAsVoid("Proxy " + var1.label() + " could not be found. Choose another proxy or turn it off in the account list.");
            }
         }
      }
   }

   private void restoreProxy() {
      try {
         this.vault.open();
         this.syncProxy();
      } catch (Exception var2) {
         ClientLog.oAsVoid("Proxy", "Saved proxy could not be loaded ({}); server connections stay paused", var2.getClass().getSimpleName());
         ProxyRoute.iAsVoid("The saved proxy could not be loaded. Open the account list to retry or turn the proxy off.");
      }
   }

   private void marker(boolean var1) throws IOException {
      if (this.proxyMarker != null) {
         if (!var1) {
            Files.deleteIfExists(this.proxyMarker);
         } else {
            if (Files.isSymbolicLink(this.proxyMarker)) {
               throw new AccountVault.Failure("Unsafe proxy marker file.");
            }

            if (!Files.exists(this.proxyMarker, LinkOption.NOFOLLOW_LINKS)) {
               Files.createFile(this.proxyMarker);
            }
         }
      }
   }

   public void openBrowser() {
      if (this.verification != null && this.busy) {
         try {
            Util.getOperatingSystem().open(this.verification);
         } catch (RuntimeException var2) {
            this.message("Open microsoft.com/link in your browser and enter the code shown here.", true);
         }
      }
   }

   public void copyCode() {
      if (this.busy && !this.code.isBlank()) {
         MinecraftClient.getInstance().keyboard.setClipboard(this.code);
      }
   }

   public void clearMessage() {
      if (!this.busy && !this.status.isEmpty()) {
         this.message("", false);
      }
   }

   static URI verificationLink(String var0) {
      URI var1 = URI.create(var0);
      String var2 = var1.getHost();
      String var3 = var1.getPath();
      boolean var4 = ("microsoft.com".equals(var2) || "www.microsoft.com".equals(var2)) && "/link".equals(var3);
      boolean var5 = "login.live.com".equals(var2) && "/oauth20_remoteconnect.srf".equals(var3);
      if (!"https".equals(var1.getScheme()) || var1.getUserInfo() != null || var1.getPort() != -1 || var1.getFragment() != null || !var4 && !var5) {
         throw new IllegalArgumentException("Unexpected Microsoft verification address");
      } else {
         return var1;
      }
   }

   private boolean current(long var1, Screen var3) {
      return validRequest(!this.stopped, this.generation.get(), var1, this.owner, var3, MinecraftClient.getInstance().currentScreen);
   }

   static boolean validRequest(boolean var0, long var1, long var3, Object var5, Object var6, Object var7) {
      return var0 && var1 == var3 && var5 == var6 && var7 == var6;
   }

   private static void interrupted() throws InterruptedException {
      if (Thread.currentThread().isInterrupted()) {
         throw new InterruptedException();
      }
   }

   private <T> void run(Screen var1, String var2, Callable<T> var3, Consumer<T> var4) {
      MinecraftClient var5 = MinecraftClient.getInstance();
      if (!this.stopped && !this.busy && var5.currentScreen == var1 && ClientAccountState.outsideWorld(var5)) {
         long var6 = this.generation.incrementAndGet();
         this.owner = var1;
         this.busy = true;
         this.code = "";
         this.verification = null;
         this.message(var2, false);
         ClientLog.GAsVoid("Accounts", "Starting {}; explicit user request, local encrypted storage", var2);

         try {
            this.pending = this.worker.submit(() -> {
               Object var8 = null;

               try {
                  var8 = var3.call();
                  if (this.stopped || this.generation.get() != var6) {
                     discard(var8);
                     return;
                  }

                  final Object var8final = var8;
                  var5.execute(() -> {
                     if (!this.current(var6, var1)) {
                        discard(var8final);
                     } else {
                        this.busy = false;
                        this.code = "";
                        this.verification = null;
                        this.refreshSnapshot();

                        try {
                           var4.accept((T)var8final);
                        } catch (RuntimeException var8x) {
                           discard(var8final);
                           this.failed(var2, var8x);
                        }

                        this.revision++;
                     }
                  });
               } catch (Exception var10) {
                  discard(var8);
                  var5.execute(() -> {
                     if (this.current(var6, var1)) {
                        this.busy = false;
                        this.code = "";
                        this.verification = null;
                        this.failed(var2, var10);
                     }
                  });
               }
            });
         } catch (RejectedExecutionException var9) {
            this.busy = false;
            this.message("A cancelled request is finishing. Retry in PetEffectRenderer moment.", true);
         }
      }
   }

   private void failed(String var1, Exception var2) {
      this.refreshSnapshot();
      ClientLog.oAsVoid("Accounts", "stage={} failed ({}); current Minecraft account retained", var1, var2.getClass().getSimpleName());
      String var3 = var2 instanceof TimeoutException
         ? "Microsoft sign-in expired. Choose Add to try again."
         : (
            var2 instanceof FileAlreadyExistsException
               ? "Another instance created the saved list. Choose Retry."
               : (
                  !(var2 instanceof AccountVault.Failure) && !(var2 instanceof ProxyChannelHandler.Type_r)
                     ? (
                        var1.contains("token")
                           ? "Token sign-in could not finish. Check your connection and that the token still works, then retry."
                           : (
                              var1.contains("proxy")
                                 ? "The proxy could not be used. Check its address and login, then retry."
                                 : "Microsoft sign-in could not finish. Check your connection, Xbox account and Minecraft ownership, then retry."
                           )
                     )
                     : var2.getMessage()
               )
         );
      this.message(var3 == null ? "Account operation failed. Retry or sign in again." : var3, true);
   }

   private static void discard(Object var0) {
      if (var0 instanceof ClientAccountState var1) {
         var1.close();
      }
   }

   private void message(String var1, boolean var2) {
      this.status = var1;
      this.error = var2;
      this.revision++;
   }

   private void refreshSnapshot() {
      this.ready = this.vault != null && this.vault.unlocked();
      this.stored = this.vault != null && this.vault.exists();
      this.snapshot = this.ready ? this.vault.entries() : List.of();
      this.proxySnapshot = this.ready ? this.vault.proxies() : List.of();
      ProxyValidation var1 = this.ready ? this.vault.activeProxy() : null;
      this.activeProxyId = var1 == null ? null : var1.id();
   }

   public void cancel(Screen var1) {
      if (this.owner == var1 && this.busy) {
         this.generation.incrementAndGet();
         if (this.pending != null) {
            this.pending.cancel(true);
         }

         this.worker.purge();
         this.busy = false;
         this.code = "";
         this.verification = null;
         this.message("Cancelled. Your current Minecraft account was retained.", false);
         ClientLog.GAsVoid("Accounts", "Pending account operation cancelled; late results will be discarded");
      }
   }

   public void stop() {
      this.stopped = true;
      this.generation.incrementAndGet();
      if (this.pending != null) {
         this.pending.cancel(true);
      }

      this.worker.shutdownNow();
      Thread var1 = new Thread(() -> {
         try {
            this.worker.awaitTermination(35L, TimeUnit.SECONDS);
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
         }

         if (this.vault != null) {
            this.vault.close();
         }
      }, "Larp account cleanup");
      var1.setDaemon(true);
      var1.start();
   }

   private static record Tested(ProxyValidation.Type_y proxy, long millis) {
   }
}
