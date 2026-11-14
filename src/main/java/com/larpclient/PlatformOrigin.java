package com.larpclient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodySubscriber;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public final class PlatformOrigin {
   private static final ExecutorService SmoothInput = new ThreadPoolExecutor(2, 4, 30L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(64), var0 -> {
      Thread var1 = new Thread(var0, "Larp-platform");
      var1.setDaemon(true);
      return var1;
   }, new AbortPolicy());
   private final URI w;
   private final Supplier<CloudRequestContext> O;
   private final Duration p;
   private final HttpClient n;
   private final AtomicInteger L = new AtomicInteger();

   public PlatformOrigin(URI var1, Supplier<CloudRequestContext> var2) {
      this(var1, var2, Duration.ofSeconds(15L));
   }

   public PlatformOrigin(URI var1, Supplier<CloudRequestContext> var2, Duration var3) {
      this.w = eAsURI(var1);
      this.O = var2;
      this.p = var3;
      this.n = HttpClient.newBuilder().executor(t).connectTimeout(Duration.ofSeconds(8L)).followRedirects(Redirect.NEVER).build();
   }

   public static URI wAsURI() {
      return URI.create("https://larpclient.com");
   }

   public static URI eAsURI(URI var0) {
      boolean var1 = List.of("localhost", "127.0.0.1", "[::1]").contains(var0.getHost());
      if (var0.getHost() == null
         || var0.getUserInfo() != null
         || var0.getQuery() != null
         || var0.getFragment() != null
         || !var0.getPath().isEmpty() && !var0.getPath().equals("/")
         || !"https".equals(var0.getScheme()) && (!var1 || !"http".equals(var0.getScheme()))) {
         throw new IllegalArgumentException("API origin requires HTTPS (HTTP is allowed only on loopback).");
      } else {
         return var0;
      }
   }

   public CompletableFuture<JsonObject> FAsCompletableFuture(String var1, String var2, JsonObject var3, boolean var4, String var5) {
      return this.xAsCompletableFuture(var1, var2, var3, var4, var5, null);
   }

   public CompletableFuture<JsonObject> xAsCompletableFuture(String var1, String var2, JsonObject var3, boolean var4, String var5, CloudRequestContext var6) {
      if (this.L.incrementAndGet() > 8) {
         this.L.decrementAndGet();
         return CompletableFuture.failedFuture(new AuthException(429, "RATE_LIMITED"));
      } else {
         try {
            if (var2.startsWith("/api/") && !var2.contains("..") && !var2.contains("#")) {
               byte[] var7 = var3 == null ? new byte[0] : var3.toString().getBytes(StandardCharsets.UTF_8);
               if (var7.length > (var2.equals("/api/reports") ? '耀' : 131072)) {
                  throw new AuthException(413, "PAYLOAD_TOO_LARGE");
               } else {
                  Builder var8 = HttpRequest.newBuilder(this.w.resolve(var2)).timeout(this.p).header("Accept", "application/json");
                  if (var4) {
                     (var6 == null ? this.O.get() : var6).yAsVoid(var8, var1, this.w.resolve(var2), var7);
                  }

                  if (var5 != null) {
                     var8.header("If-Match", var5);
                  }

                  if (var3 != null) {
                     var8.header("Content-Type", "application/json");
                  }

                  var8.method(var1, var3 == null ? BodyPublishers.noBody() : BodyPublishers.ofByteArray(var7));
                  CompletableFuture var9 = this.n.sendAsync(var8.build(), var0 -> new PlatformOrigin.Type_j(8388608));
                  CompletableFuture var10 = new CompletableFuture();
                  CompletableFuture.delayedExecutor(this.p.toMillis(), TimeUnit.MILLISECONDS).execute(() -> {
                     if (var10.completeExceptionally(new AuthException(0, "TIMEOUT"))) {
                        var9.cancel(true);
                     }
                  });
                  var9.whenComplete((var1x, var2x) -> {
                     if (var2x != null) {
                        var10.completeExceptionally(ZAsAh((Throwable)var2x));
                     } else {
                        try {
                           var10.complete(PAsJsonObject(((HttpResponse)var1x).statusCode(), (String)((HttpResponse)var1x).body()));
                        } catch (RuntimeException var4x) {
                           var10.completeExceptionally(var4x);
                        }
                     }
                  });
                  var10.whenComplete((var3x, var4x) -> {
                     this.L.decrementAndGet();
                     if (var10.isCancelled()) {
                        var9.cancel(true);
                     }
                  });
                  return var10;
               }
            } else {
               throw AuthException.rAsAh();
            }
         } catch (RuntimeException var111) {
            this.L.decrementAndGet();
            return CompletableFuture.failedFuture((Throwable)(var111 instanceof AuthException ? var111 : AuthException.rAsAh()));
         }
      }
   }

   public CompletableFuture<JsonObject> cAsCompletableFuture(String var1, boolean var2) {
      return this.FAsCompletableFuture("GET", var1, null, var2, null);
   }

   public static AuthException ZAsAh(Throwable var0) {
      while (var0.getCause() != null && var0 instanceof CompletionException) {
         var0 = var0.getCause();
      }

      return var0 instanceof AuthException var1 ? var1 : new AuthException(0, var0 instanceof HttpTimeoutException ? "TIMEOUT" : "NETWORK");
   }

   public static JsonObject PAsJsonObject(int var0, String var1) {
      JsonObject var2;
      try {
         var2 = JsonParser.parseString(var1).getAsJsonObject();
      } catch (RuntimeException var6) {
         throw var0 >= 200 && var0 < 300 ? AuthException.rAsAh() : new AuthException(var0, "HTTP_ERROR");
      }

      if (var0 < 200 || var0 >= 300) {
         String var3 = "HTTP_ERROR";

         try {
            String var4 = var2.getAsJsonObject("error").get("code").getAsString();
            if (List.of("REVISION_CONFLICT", "QUOTA_EXCEEDED", "INVALID_INPUT", "UNAUTHENTICATED", "FORBIDDEN", "RATE_LIMITED", "DISCORD_LIMIT").contains(var4)
               )
             {
               var3 = var4;
            }
         } catch (RuntimeException var5) {
         }

         throw new AuthException(var0, var3);
      } else if (var2.has("ok")
         && var2.get("ok").isJsonPrimitive()
         && var2.getAsJsonPrimitive("ok").isBoolean()
         && var2.get("ok").getAsBoolean()
         && var2.has("data")
         && var2.get("data").isJsonObject()) {
         return var2.getAsJsonObject("data");
      } else {
         throw AuthException.rAsAh();
      }
   }

   private static final class Type_j implements BodySubscriber<String> {
      private final CompletableFuture<String> MenuPopupProvider = new CompletableFuture<>();
      private final ByteArrayOutputStream K = new ByteArrayOutputStream();
      private final int V;
      private Subscription d;

      Type_j(int var1) {
         this.V = var1;
      }

      @Override
      public CompletionStage<String> getBody() {
         return this.f;
      }

      @Override
      public void onSubscribe(Subscription var1) {
         this.d = var1;
         var1.request(1L);
      }

      public void onNext(List<ByteBuffer> var1) {
         for (ByteBuffer var3 : var1) {
            if (this.K.size() + var3.remaining() > this.V) {
               this.d.cancel();
               this.f.completeExceptionally(AuthException.rAsAh());
               return;
            }

            byte[] var4 = new byte[var3.remaining()];
            var3.get(var4);
            this.K.writeBytes(var4);
         }

         this.d.request(1L);
      }

      @Override
      public void onError(Throwable var1) {
         this.f.completeExceptionally(var1);
      }

      @Override
      public void onComplete() {
         this.f.complete(this.K.toString(StandardCharsets.UTF_8));
      }
   }
}
