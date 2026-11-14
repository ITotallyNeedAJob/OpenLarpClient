package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class DiscordRpcManager implements AutoCloseable {
   public static final String u = "1491537245591572480";
   private static final long Y = TimeUnit.SECONDS.toNanos(10L);
   private final Object w = new Object();
   private final List<String> S;
   private final DiscordRpcManager.Type_c Q;
   private final Consumer<String> K;
   private final long C;
   private final long P = System.currentTimeMillis() / 1000L;
   private final ScheduledExecutorService A = Executors.newSingleThreadScheduledExecutor(
      var0 -> Thread.ofPlatform().daemon().name("Larp-Discord-Guard").unstarted(var0)
   );
   private volatile boolean r;
   private volatile boolean F;
   private volatile long c;
   private volatile DiscordRpcManager.Type_y e;
   private Thread s;

   public DiscordRpcManager(boolean var1) {
      this(
         DiscordPipeLocator.XAsList(System.getProperty("os.name", ""), System.getenv()),
         DiscordPipeLocator::iAsByteChannel,
         var0 -> ClientLog.GAsVoid("Discord", var0),
         15000L
      );
      this.rAsVoid(var1);
   }

   DiscordRpcManager(List<String> var1, DiscordRpcManager.Type_c var2, Consumer<String> var3, long var4) {
      this.S = List.copyOf(var1);
      this.Q = var2;
      this.K = var3;
      this.C = var4;
   }

   public void rAsVoid(boolean var1) {
      synchronized (this.w) {
         if (!this.F && this.r != var1) {
            this.r = var1;
            this.c++;
            DiscordPresenceStatus.publish(var1 ? DiscordPresenceStatus.Type_d.WAITING : DiscordPresenceStatus.Type_d.OFF, null);
            if (this.s == null && var1) {
               this.A.scheduleWithFixedDelay(this::OAsVoid, 0L, 100L, TimeUnit.MILLISECONDS);
               this.s = Thread.ofPlatform().daemon().name("Larp-Discord-RPC").start(this::CAsVoid);
            }

            this.w.notifyAll();
         }
      }
   }

   @Override
   public void close() {
      synchronized (this.w) {
         this.F = true;
         this.r = false;
         this.c++;
         DiscordPresenceStatus.publish(DiscordPresenceStatus.Type_d.OFF, null);
         this.w.notifyAll();
         if (this.s == null) {
            this.A.shutdown();
         }
      }
   }

   private boolean eAsBoolean(DiscordRpcManager.Type_y var1) {
      return !this.F && this.r && var1.D == this.c;
   }

   private void OAsVoid() {
      DiscordRpcManager.Type_y var1 = this.e;
      if (var1 != null && (!this.eAsBoolean(var1) || System.nanoTime() > var1.G)) {
         var1.close();
      }
   }

   private void CAsVoid() {
      byte var1 = 0;

      try {
         while (!this.F) {
            synchronized (this.w) {
               while (!this.r && !this.F) {
                  this.w.wait();
               }

               if (this.F) {
                  return;
               }
            }

            DiscordRpcManager.Type_y var34 = new DiscordRpcManager.Type_y(this.c);
            this.e = var34;
            String var3 = "desktop discovery";

            try {
               for (String var5 : this.S) {
                  if (!this.eAsBoolean(var34)) {
                     break;
                  }

                  try {
                     var34.t = this.Q.JAsByteChannel(var5);
                     break;
                  } catch (IOException var28) {
                  }
               }

               if (var34.t == null) {
                  throw new IOException("Discord desktop unavailable");
               }

               label478:
               if (this.eAsBoolean(var34)) {
                  var3 = "handshake";
                  var34.G = System.nanoTime() + Y;
                  JsonObject var35 = new JsonObject();
                  var35.addProperty("v", 1);
                  var35.addProperty("client_id", "1491537245591572480");
                  lAsVoid(var34, 0, var35);
                  JsonObject var37 = yAsJsonObject(var34);
                  if ("DISPATCH".equals(pAsString(var37, "cmd")) && "READY".equals(pAsString(var37, "evt"))) {
                     DiscordAvatarClient var6 = DiscordAvatarClient.read(lAsJsonObject(lAsJsonObject(var37, "data"), "user"));
                     if (!this.eAsBoolean(var34)) {
                        continue;
                     }

                     String var7 = UUID.randomUUID().toString();
                     var3 = "activity acknowledgement";
                     lAsVoid(var34, 1, CAsJsonObject(this.P, var7));
                     JsonObject var8 = yAsJsonObject(var34);
                     if ("SET_ACTIVITY".equals(pAsString(var8, "cmd")) && var7.equals(pAsString(var8, "nonce"))) {
                        if (!this.eAsBoolean(var34)) {
                           continue;
                        }

                        var34.G = Long.MAX_VALUE;
                        var1 = 0;
                        DiscordPresenceStatus.publish(DiscordPresenceStatus.Type_d.ONLINE, var6);
                        this.K
                           .accept("Rich Presence connected: Playing with larpclient.com" + (var6 == null ? "" : "; desktop account shown on the account card"));
                        var3 = "connected session";

                        while (true) {
                           if (!this.eAsBoolean(var34)) {
                              break label478;
                           }

                           yAsJsonObject(var34);
                        }
                     }

                     throw new IOException("Discord activity acknowledgement missing");
                  }

                  throw new IOException("Discord handshake rejected");
               }
            } catch (RuntimeException | IOException var29) {
               if (this.eAsBoolean(var34) && var1 == 0) {
                  this.K.accept("Rich Presence unavailable at " + var3 + "; retrying every 15 seconds while enabled");
                  var1 = 1;
               }
            } finally {
               var34.close();
               this.e = null;
               DiscordPresenceStatus.publish(this.r && !this.F ? DiscordPresenceStatus.Type_d.WAITING : DiscordPresenceStatus.Type_d.OFF, null);
               if (!this.r && !this.F && var34.t != null) {
                  this.K.accept("Rich Presence disabled; local IPC released");
               }
            }

            synchronized (this.w) {
               if (this.eAsBoolean(var34)) {
                  this.w.wait(this.C);
               }
            }
         }
      } catch (InterruptedException var32) {
         Thread.currentThread().interrupt();
      } finally {
         DiscordRpcManager.Type_y var12 = this.e;
         if (var12 != null) {
            var12.close();
         }

         this.A.shutdown();
         this.K.accept("Rich Presence stopped; local IPC released");
      }
   }

   static JsonObject CAsJsonObject(long var0, String var2) {
      JsonObject var3 = new JsonObject();
      var3.addProperty("type", 0);
      var3.addProperty("name", "larpclient.com");
      var3.addProperty("details", "Playing with larpclient.com");
      var3.addProperty("state", "Minecraft " + BuildMetadata.e);
      JsonObject var4 = new JsonObject();
      var4.addProperty("start", var0);
      var3.add("timestamps", var4);
      JsonObject var5 = new JsonObject();
      var5.addProperty("label", "larpclient.com");
      var5.addProperty("url", "https://larpclient.com");
      JsonArray var6 = new JsonArray();
      var6.add(var5);
      var3.add("buttons", var6);
      JsonObject var7 = new JsonObject();
      var7.addProperty("pid", ProcessHandle.current().pid());
      var7.add("activity", var3);
      JsonObject var8 = new JsonObject();
      var8.addProperty("cmd", "SET_ACTIVITY");
      var8.addProperty("nonce", var2);
      var8.add("args", var7);
      return var8;
   }

   private static void lAsVoid(DiscordRpcManager.Type_y var0, int var1, JsonObject var2) throws IOException {
      DiscordPipeLocator.NAsVoid(var0.t, var1, var2.toString().getBytes(StandardCharsets.UTF_8));
   }

   private static JsonObject yAsJsonObject(DiscordRpcManager.Type_y var0) throws IOException {
      while (true) {
         DiscordPipeLocator.Type_i var1 = DiscordPipeLocator.cAsXg_Type_i(var0.t);
         if (var1.opcode() == 3) {
            DiscordPipeLocator.NAsVoid(var0.t, 4, var1.data());
         } else if (var1.opcode() != 4) {
            if (var1.opcode() != 1) {
               throw new IOException("Discord closed IPC");
            }

            JsonObject var2 = JsonParser.parseString(new String(var1.data(), StandardCharsets.UTF_8)).getAsJsonObject();
            if ("ERROR".equals(pAsString(var2, "evt"))) {
               throw new IOException("Discord rejected RPC command");
            }

            return var2;
         }
      }
   }

   static JsonObject lAsJsonObject(JsonObject var0, String var1) {
      JsonElement var2 = var0 == null ? null : var0.get(var1);
      return var2 != null && var2.isJsonObject() ? var2.getAsJsonObject() : null;
   }

   static String pAsString(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && var2.isJsonPrimitive() && var2.getAsJsonPrimitive().isString() ? var2.getAsString() : "";
   }

   @FunctionalInterface
   interface Type_c {
      ByteChannel JAsByteChannel(String var1) throws IOException;
   }

   private static final class Type_y implements AutoCloseable {
      final long D;
      volatile ByteChannel t;
      volatile long G = Long.MAX_VALUE;

      Type_y(long var1) {
         this.D = var1;
      }

      @Override
      public void close() {
         ByteChannel var1 = this.t;
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var2) {
            }
         }
      }
   }
}
