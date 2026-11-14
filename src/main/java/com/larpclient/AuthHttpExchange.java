package com.larpclient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodySubscriber;
import java.nio.ByteBuffer;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.Flow.Subscription;
import javax.net.ssl.SSLException;

public final class AuthHttpExchange {
   private static final HttpClient u = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8L)).followRedirects(Redirect.NEVER).build();

   private AuthHttpExchange() {
   }

   public static JsonObject SAsJsonObject(String var0, String var1, String var2, String var3) {
      JsonObject var4 = new JsonObject();
      if (!var1.isEmpty()) {
         var4.addProperty(var0, var1);
      }

      var4.addProperty("hwid", var2);
      var4.addProperty("mcUsername", var3);
      return var4;
   }

   public static AuthHttpExchange.Type_h gAsRz_Type_h(URI var0, JsonObject var1, boolean var2) throws IOException {
      return UAsRz_Type_h(var0, var1, var2, Duration.ofSeconds(8L));
   }

   static AuthHttpExchange.Type_h UAsRz_Type_h(URI var0, JsonObject var1, boolean var2, Duration var3) throws IOException {
      if (!var3.isNegative() && !var3.isZero()) {
         long var4 = System.nanoTime();
         HttpRequest var6 = HttpRequest.newBuilder(var0)
            .timeout(var3)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .POST(BodyPublishers.ofString(var1.toString(), StandardCharsets.UTF_8))
            .build();
         AuthHttpExchange.Type_l var7 = new AuthHttpExchange.Type_l();
         CompletableFuture var8 = u.sendAsync(var6, var1x -> var7);

         try {
            long var9 = var3.toNanos() - (System.nanoTime() - var4);
            if (var9 <= 0L) {
               throw new TimeoutException();
            } else {
               HttpResponse var17 = (HttpResponse)var8.get(var9, TimeUnit.NANOSECONDS);
               return yAsRz_Type_h(var17.statusCode(), (String)var17.body(), var2);
            }
         } catch (TimeoutException var13) {
            var7.NAsVoid(new SocketTimeoutException("Authentication exchange deadline exceeded"));
            var8.cancel(true);
            throw new SocketTimeoutException("Authentication exchange deadline exceeded");
         } catch (InterruptedException var14) {
            var7.NAsVoid(new InterruptedIOException("Authentication exchange interrupted"));
            var8.cancel(true);
            Thread.currentThread().interrupt();
            throw new InterruptedIOException("Authentication exchange interrupted");
         } catch (ExecutionException var151) {
            Throwable var10 = var151.getCause();
            if (var10 instanceof HttpTimeoutException) {
               throw new SocketTimeoutException("Authentication exchange deadline exceeded");
            } else {
               for (Throwable var11 = var10; var11 != null; var11 = var11.getCause()) {
                  if (var11 instanceof UnknownHostException var19) {
                     throw var19;
                  }

                  if (var11 instanceof UnresolvedAddressException) {
                     UnknownHostException var18 = new UnknownHostException(var0.getHost());
                     var18.initCause(var10);
                     throw var18;
                  }

                  if (var11 instanceof SSLException var12) {
                     throw var12;
                  }
               }

               if (var10 instanceof IOException var16) {
                  throw var16;
               } else {
                  throw new IOException("Authentication transport failed", var10);
               }
            }
         }
      } else {
         throw new IllegalArgumentException("Positive auth deadline required");
      }
   }

   public static AuthHttpExchange.Type_h yAsRz_Type_h(int var0, String var1, boolean var2) {
      if (var0 >= 500 || var0 == 429 || var0 >= 300 && var0 < 400) {
         return new AuthHttpExchange.Type_h(false, "", "", AuthErrorMessages.oAsString(var0), false, "", var0 >= 500 || var0 == 429);
      } else {
         try {
            JsonObject var3 = JsonParser.parseString(var1).getAsJsonObject();
            int var4 = var0 == 200
                  && var3.has("valid")
                  && var3.get("valid").isJsonPrimitive()
                  && var3.getAsJsonPrimitive("valid").isBoolean()
                  && var3.get("valid").getAsBoolean()
               ? 1
               : 0;
            if (var4 == 0) {
               String var7 = XAsString(var3, "error");
               return new AuthHttpExchange.Type_h(
                  false, "", "", var7.isBlank() ? AuthErrorMessages.oAsString(var0) : var7, var3.has("expired") && var3.get("expired").getAsBoolean()
               );
            } else {
               String var5 = XAsString(var3, "token");
               return var2 && !var5.matches("[a-f0-9]{64}")
                  ? new AuthHttpExchange.Type_h(false, "", "", "Invalid activation response; missing install token", false)
                  : new AuthHttpExchange.Type_h(true, XAsString(var3, "user"), var5, "", false, XAsString(var3, "grant"), false);
            }
         } catch (RuntimeException var61) {
            return new AuthHttpExchange.Type_h(false, "", "", var0 == 200 ? "Invalid auth server response" : AuthErrorMessages.oAsString(var0), false);
         }
      }
   }

   private static String XAsString(JsonObject var0, String var1) {
      return var0.has(var1) && !var0.get(var1).isJsonNull() ? var0.get(var1).getAsString() : "";
   }

   public static record Type_h(boolean valid, String user, String token, String error, boolean expired, String grant, boolean transientFailure) {
      public Type_h(boolean var1, String var2, String var3, String var4, boolean var5) {
         this(var1, var2, var3, var4, var5, "", false);
      }
   }

   private static final class Type_l implements BodySubscriber<String> {
      private final CompletableFuture<String> D = new CompletableFuture<>();
      private final ByteArrayOutputStream e = new ByteArrayOutputStream();
      private volatile Subscription k;
      private volatile boolean v;

      private Type_l() {
      }

      @Override
      public CompletionStage<String> getBody() {
         return this.D;
      }

      @Override
      public void onSubscribe(Subscription var1) {
         this.k = var1;
         if (this.v) {
            var1.cancel();
         } else {
            var1.request(1L);
         }
      }

      private void NAsVoid(IOException var1) {
         this.v = true;
         Subscription var2 = this.k;
         if (var2 != null) {
            var2.cancel();
         }

         this.D.completeExceptionally(var1);
      }

      public void onNext(List<ByteBuffer> var1) {
         if (!this.v) {
            for (ByteBuffer var3 : var1) {
               if (this.e.size() + var3.remaining() > 65536) {
                  this.NAsVoid(new IOException("Authentication response exceeds size limit"));
                  return;
               }

               byte[] var4 = new byte[var3.remaining()];
               var3.get(var4);
               this.e.writeBytes(var4);
            }

            if (!this.v) {
               this.k.request(1L);
            }
         }
      }

      @Override
      public void onError(Throwable var1) {
         this.D.completeExceptionally(var1);
      }

      @Override
      public void onComplete() {
         this.D.complete(this.e.toString(StandardCharsets.UTF_8));
      }
   }
}
