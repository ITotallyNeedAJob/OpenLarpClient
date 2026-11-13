package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.module.FakeSpawner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class DonutAuctionApi {
   private static final DonutAuctionApi z = new DonutAuctionApi();
   private static final String L = "https://api.donutsmp.net/v1/auction/list/";
   private static final long w = 10000L;
   private static final long p = 600L;
   private static final Pattern Y = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
   private static final Pattern S = Pattern.compile("(?<![A-Za-z0-9])[A-Za-z0-9]{16,}(?![A-Za-z0-9])");
   private final HttpClient c = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();
   private final Map<String, DonutAuctionApi.Type_i> O = new HashMap<>();
   private final ArrayDeque<DonutAuctionApi.Type_n> J = new ArrayDeque<>();
   private long D;
   private boolean k;

   private DonutAuctionApi() {
   }

   public static DonutAuctionApi ZAsA6() {
      return z;
   }

   public void IAsVoid(Text var1) {
      if (var1 != null) {
         String var2 = var1.getString();
         if (var2 != null && !var2.isBlank()) {
            String var3 = var2.toLowerCase(Locale.ROOT);
            if (var3.contains("api") && (var3.contains("key") || var3.contains("token"))) {
               String var4 = NAsString(var2);
               if (var4 != null && !var4.equals(FakeSpawner.kAsFakeSpawner().PAsString())) {
                  FakeSpawner var5 = FakeSpawner.kAsFakeSpawner();
                  var5.OAsVoid(var4);
                  var5.IAsVoid();
                  yAsVoid("DonutSMP API token captured", Formatting.GREEN);
               }
            }
         }
      }
   }

   public void AAsVoid() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      int var2 = var1 != null && var1.player != null && var1.world != null ? 1 : 0;
      this.k = var2 != 0;
      if (var2 != 0 && !FakeSpawner.kAsFakeSpawner().PAsString().isBlank()) {
         long var3 = System.currentTimeMillis();
         if (var3 - this.D >= 600L && !this.J.isEmpty()) {
            DonutAuctionApi.Type_n var5 = this.J.poll();
            this.D = var3;
            this.qAsVoid(var1, var5, 0);
         }
      }
   }

   public void vAsVoid(String var1, Consumer<Long> var2) {
      String var3 = GAsString(var1);
      if (!var3.isEmpty()) {
         DonutAuctionApi.Type_i var4 = this.O.get(var3);
         if (var4 != null && System.currentTimeMillis() - var4.at() < 10000L) {
            var2.accept(var4.price());
         } else {
            for (DonutAuctionApi.Type_n var6 : this.J) {
               if (var6.term().equals(var3)) {
                  return;
               }
            }

            if (!FakeSpawner.kAsFakeSpawner().PAsString().isBlank()) {
               this.PAsVoid(new DonutAuctionApi.Type_n(var3, var2));
            }
         }
      }
   }

   public long WAsLong(String var1) {
      DonutAuctionApi.Type_i var2 = this.O.get(GAsString(var1));
      return var2 == null ? 0L : var2.price();
   }

   private void PAsVoid(DonutAuctionApi.Type_n var1) {
      this.J.add(var1);
   }

   private void qAsVoid(MinecraftClient var1, DonutAuctionApi.Type_n var2, int var3) {
      String var4 = FakeSpawner.kAsFakeSpawner().PAsString();
      if (var4 != null && !var4.isBlank() && var3 <= 1) {
         Builder var5 = HttpRequest.newBuilder(URI.create("https://api.donutsmp.net/v1/auction/list/1"))
            .timeout(Duration.ofSeconds(15L))
            .header("Authorization", "Bearer " + var4)
            .header("Accept", "application/json");
         if (var3 == 0) {
            var5.header("Content-Type", "application/json")
               .method("GET", BodyPublishers.ofString("{\"search\":\"" + var2.term() + "\",\"sort\":\"lowest_price\"}"));
         } else {
            var5.GET();
         }

         this.c.sendAsync(var5.build(), BodyHandlers.ofString()).whenComplete((var4x, var5x) -> var1.execute(() -> {
               if (var5x == null && var4x.statusCode() != 401) {
                  if (var4x.statusCode() != 200) {
                     this.qAsVoid(var1, var2, var3 + 1);
                  } else {
                     long var6 = qAsLong((String)var4x.body(), var2.term());
                     this.O.put(var2.term(), new DonutAuctionApi.Type_i(var6, System.currentTimeMillis()));
                     var2.onResult().accept(var6);
                  }
               } else if (var4x != null && var4x.statusCode() == 401) {
                  this.O.put(var2.term(), new DonutAuctionApi.Type_i(0L, System.currentTimeMillis()));
                  var2.onResult().accept(0L);
               } else {
                  this.qAsVoid(var1, var2, var3 + 1);
               }
            }));
      } else {
         this.O.put(var2.term(), new DonutAuctionApi.Type_i(0L, System.currentTimeMillis()));
         var2.onResult().accept(0L);
      }
   }

   private static long qAsLong(String var0, String var1) {
      try {
         JsonElement var2 = JsonParser.parseString(var0);
         if (!var2.isJsonObject()) {
            return 0L;
         } else {
            JsonObject var3 = var2.getAsJsonObject();
            JsonElement var4 = var3.get("result");
            if (var4 != null && var4.isJsonArray()) {
               long var5 = Long.MAX_VALUE;

               for (JsonElement var8 : var4.getAsJsonArray()) {
                  if (var8.isJsonObject()) {
                     JsonObject var9 = var8.getAsJsonObject();
                     JsonElement var10 = var9.get("price");
                     if (var10 != null && !var10.isJsonNull()) {
                        double var11;
                        try {
                           var11 = var10.getAsDouble();
                        } catch (NumberFormatException var18) {
                           continue;
                        }

                        if (!(var11 <= 0.0)) {
                           JsonElement var13 = var9.get("item");
                           if (var13 != null && var13.isJsonObject()) {
                              JsonObject var14 = var13.getAsJsonObject();
                              if (XAsBoolean(var14, var1)) {
                                 long var15 = 1L;
                                 JsonElement var17 = var14.get("count");
                                 if (var17 != null && !var17.isJsonNull()) {
                                    var15 = Math.max(1L, var17.getAsLong());
                                 }

                                 var5 = Math.min(var5, Math.max(1L, Math.round(var11 / (double)var15)));
                              }
                           }
                        }
                     }
                  }
               }

               return var5 == Long.MAX_VALUE ? 0L : var5;
            } else {
               return 0L;
            }
         }
      } catch (Exception var191) {
         return 0L;
      }
   }

   private static boolean XAsBoolean(JsonObject var0, String var1) {
      String var2 = IAsString(var1);
      String var3 = GAsString(var0, "id");
      if (var3 != null) {
         return IAsString(var3).equals(var2);
      } else {
         String var4 = GAsString(var0, "display_name");
         return var4 != null && IAsString(var4).equals(var2);
      }
   }

   private static String IAsString(String var0) {
      String var1 = GAsString(var0).replace(' ', '_').replace('-', '_');
      int var2 = var1.indexOf(58);
      return var2 >= 0 ? var1.substring(var2 + 1) : var1;
   }

   private static String GAsString(String var0) {
      return var0 == null ? "" : var0.trim().toLowerCase(Locale.ROOT);
   }

   private static String GAsString(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      if (var2 != null && !var2.isJsonNull()) {
         String var3 = var2.getAsString();
         return var3 != null && !var3.isBlank() ? var3 : null;
      } else {
         return null;
      }
   }

   private static String NAsString(String var0) {
      Matcher var1 = Y.matcher(var0);
      if (var1.find()) {
         return var1.group();
      } else {
         Matcher var2 = S.matcher(var0);
         String var3 = null;

         while (var2.find()) {
            if (var3 == null || var2.group().length() > var3.length()) {
               var3 = var2.group();
            }
         }

         return var3;
      }
   }

   private static void yAsVoid(String var0, Formatting var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2 != null && var2.player != null) {
         var2.execute(() -> {
            if (var2.player != null) {
               ChatMessages.RAsVoid(Text.literal("[Fake Elytra] " + var0).formatted(var1), false);
            }
         });
      }
   }

   private static record Type_i(long price, long at) {
   }

   private static record Type_n(String term, Consumer<Long> onResult) {
   }
}
