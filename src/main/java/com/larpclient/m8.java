package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class m8 {
   public static final long V = 60000L;
   public static final long SkyChunkCache = 120000L;
   public static final int TextLayout = 32;
   private final m8.Type_t e;
   private final Consumer<String> k;
   private final LinkedHashMap<String, JsonObject> SmoothInput = new LinkedHashMap<>();
   private final Map<UUID, Long> PriceDetector = new LinkedHashMap<>();
   private String w = "";
   private String Y = "";
   private String PetEffectRenderer = "";
   private boolean d;
   private boolean G;
   private boolean h;
   private long x;
   private long P = -1L;
   private long v;
   private long Q;
   private long Z;
   private long K;
   private int L;
   private long y;
   private m8.Type_y D;
   private String R;
   private String W;
   private JsonObject B;

   public m8(m8.Type_t var1, Consumer<String> var2, JsonObject var3) {
      this.e = var1;
      this.k = var2;
      this.B = var3;
   }

   public synchronized void bAsVoid(String var1, String var2, boolean var3, long var4, long var6) {
      if (!Objects.equals(this.w, var1)) {
         this.EAsVoid();
         this.w = var1;
         if (this.B != null && var1.equals(QAsString(this.B, "scope"))) {
            try {
               this.Y = QAsString(this.B, "owner");
               JsonArray var8 = this.B.getAsJsonArray("pending");
               if (var8.size() > 32) {
                  throw new IllegalArgumentException();
               }

               for (JsonElement var10 : var8) {
                  JsonObject var11 = var10.getAsJsonObject();
                  BAsVoid(var11, var6);
                  this.t.put(QAsString(var11, "sessionId"), var11.deepCopy());
               }
            } catch (RuntimeException var14) {
               this.t.clear();
               this.Y = "";
               this.k.accept("Invalid queued activity discarded");
            }
         }

         this.B = null;
         this.k.accept(var1.isEmpty() ? "Collection stopped: account unavailable" : "Checking dashboard consent");
      }

      if (var1.isEmpty()) {
         this.P = var4;
      } else {
         int var15 = this.d && var4 < this.Q ? 1 : 0;
         String var16 = KAsString(var2);
         if (!Objects.equals(this.R, var16)) {
            this.R = null;
            this.W = null;
         }

         long var17 = this.K + Math.max(0L, var4 - this.Z);
         int var12 = this.D != null && var17 / 86400000L != this.D.I / 86400000L ? 1 : 0;
         if (this.D != null && var15 != 0 && this.D.X.equals(var16) && var3 && this.h && this.P >= 0L) {
            this.D.L = this.D.L + Math.max(0L, Math.min(2000L, var4 - this.P));
         }

         if (this.D != null && (var16 == null || !this.D.X.equals(var16) || var15 == 0 || var12 != 0)) {
            int var13 = var15 != 0 && this.D.X.equals(var16) ? 1 : 0;
            this.DAsVoid(var13 == 0);
         }

         if (var15 != 0 && var16 != null && this.D == null) {
            if (this.W == null) {
               this.W = UUID.randomUUID().toString();
               this.R = var16;
            }

            this.D = new m8.Type_y(var16, var17, this.W);
            if (var12 == 0) {
               this.m.clear();
            }

            this.h = false;
         }

         this.h = var3;
         this.P = var4;
         if ((var16 != null || !this.t.isEmpty()) && var4 >= this.v && !this.G) {
            JsonObject var18 = var15 != 0 ? this.yAsJsonObject() : null;
            if (var18 != null || var15 == 0 || var16 != null || var4 + 60000L >= this.Q) {
               this.VAsVoid(var18, var4);
            }
         }
      }
   }

   private JsonObject yAsJsonObject() {
      if (!this.t.isEmpty()) {
         return this.t.values().iterator().next().deepCopy();
      } else {
         return this.D != null ? this.D.MAsJsonObject() : null;
      }
   }

   private void VAsVoid(JsonObject var1, long var2) {
      this.G = true;
      this.v = var2 + 60000L;
      long var4 = this.x;

      CompletableFuture var6;
      try {
         var6 = this.e.VAsCompletableFuture(this.w, var1);
      } catch (RuntimeException var8) {
         var6 = CompletableFuture.failedFuture(var8);
      }

      var6.whenComplete(
         (var6x, var7) -> {
            synchronized (this) {
               if (var4 == this.x) {
                  this.G = false;
                  this.y++;
                  long var9 = Math.max(var2, this.P);
                  if (var7 != null) {
                     this.L = Math.min(5, this.L + 1);
                     this.v = var9 + Math.min(900000L, 60000L << this.L - 1);
                     if (this.L == 1) {
                        this.k.accept("Activity sync unavailable; retaining bounded queue and backing off");
                     }

                     AuthException var16 = PlatformService.BAsAh((Throwable)var7);
                     if (var16.vAsInt() == 401 || var16.vAsInt() == 403) {
                        this.d = false;
                        this.KAsVoid();
                        this.Q = 0L;
                     } else if (var1 != null && (var16.vAsInt() == 400 || var16.vAsInt() == 409 || var16.vAsInt() == 413)) {
                        this.t.remove(QAsString(var1, "sessionId"));
                        if (this.D != null && this.D.w.equals(QAsString(var1, "sessionId"))) {
                           this.D = null;
                        }

                        this.v = var9 + 900000L;
                        this.k.accept("Activity snapshot rejected; collection paused before retrying consent");
                        this.Q = 0L;
                     }
                  } else {
                     try {
                        this.hAsVoid((JsonObject)var6x, var9);
                        if (var1 != null
                           && (((JsonObject)var6x).get("accepted").getAsBoolean() || "STALE_SNAPSHOT".equals(QAsString((JsonObject)var6x, "reason")))) {
                           String var11 = QAsString(var1, "sessionId");
                           JsonObject var12 = this.t.get(var11);
                           if (var12 != null && var12.equals(var1)) {
                              this.t.remove(var11);
                           }
                        }

                        if (this.L > 0) {
                           this.k.accept("Activity sync recovered");
                        }

                        this.L = 0;
                     } catch (RuntimeException var141) {
                        this.Q = 0L;
                        this.v = var9 + 60000L;
                        this.k.accept("Invalid activity consent response; collection suspended");
                     }
                  }
               }
            }
         }
      );
   }

   private void hAsVoid(JsonObject var1, long var2) {
      String var4 = QAsString(var1, "ownerId");
      if (!var4.matches("[0-9]{17,20}")) {
         throw new IllegalArgumentException();
      } else {
         int var5 = var1.getAsJsonPrimitive("enabled").isBoolean() && var1.get("enabled").getAsBoolean() ? 1 : 0;
         long var6 = Instant.parse(QAsString(var1, "serverTime")).toEpochMilli();
         String var8 = var5 != 0 ? QAsString(var1, "collectionStartsAt") : "";
         long var9 = var5 != 0 ? Instant.parse(var8).toEpochMilli() : 0L;
         if (var5 != 0 && var9 > var6) {
            throw new IllegalArgumentException();
         } else {
            if (!this.Y.isEmpty() && !this.Y.equals(var4)) {
               this.t.clear();
               this.D = null;
               this.m.clear();
               this.W = null;
               this.R = null;
            }

            this.Y = var4;
            if (var5 == 0) {
               if (this.d || !this.t.isEmpty()) {
                  this.k.accept("Dashboard tracking off; queued gameplay erased");
               }

               this.t.clear();
               this.D = null;
               this.m.clear();
               this.W = null;
               this.R = null;
            } else if (!this.a.equals(var8)) {
               this.t.values().removeIf(var2x -> Instant.parse(QAsString(var2x, "startedAt")).toEpochMilli() < var9);
               if (this.D != null && this.D.I < var9) {
                  this.D = null;
                  this.m.clear();
               }

               if (!this.d) {
                  this.k.accept("Dashboard tracking on; collecting private gameplay activity");
               }
            }

            this.a = var8;
            this.d = var5 != 0;
            this.Q = var2 + 120000L;
            this.K = var6;
            this.Z = var2;
         }
      }
   }

   public synchronized void DAsVoid(UUID var1, boolean var2, long var3) {
      if (this.D != null && this.d && var3 < this.Q) {
         this.m.remove(var1);
         if (var2) {
            this.m.put(var1, var3);
            if (this.m.size() > 128) {
               this.m.remove(this.m.keySet().iterator().next());
            }
         }
      }
   }

   public synchronized void bAsVoid(UUID var1, long var2) {
      Long var4 = this.m.remove(var1);
      if (this.D != null && this.d && var2 < this.Q && var4 != null && var2 >= var4 && var2 - var4 <= 15000L) {
         this.D.O = Math.min(100000, this.D.O + 1);
         this.k.accept("Server-attributed player kill queued for next activity snapshot");
      }
   }

   public synchronized void JAsVoid() {
      this.m.clear();
   }

   public synchronized void qAsVoid() {
      this.KAsVoid();
      this.h = false;
      this.W = null;
      this.R = null;
   }

   private void KAsVoid() {
      this.DAsVoid(true);
   }

   private void DAsVoid(boolean var1) {
      if (this.D != null && (this.D.L >= 1000L || this.D.O > 0)) {
         this.t.put(this.D.w, this.D.MAsJsonObject());
         if (this.t.size() > 32) {
            this.t.remove(this.t.keySet().iterator().next());
            this.k.accept("Activity queue full; oldest unsent session discarded");
         }
      }

      this.D = null;
      if (var1) {
         this.m.clear();
      }

      this.h = false;
   }

   private void EAsVoid() {
      this.y++;
      this.x++;
      this.t.clear();
      this.D = null;
      this.m.clear();
      this.Y = "";
      this.a = "";
      this.d = false;
      this.G = false;
      this.Q = 0L;
      this.v = 0L;
      this.L = 0;
      this.h = false;
      this.P = -1L;
      this.W = null;
      this.R = null;
   }

   public synchronized long eAsLong() {
      return this.y;
   }

   public synchronized JsonObject fAsJsonObject() {
      if (this.B != null && this.w.isEmpty()) {
         return this.B.deepCopy();
      } else {
         JsonObject var1 = new JsonObject();
         var1.addProperty("format", 1);
         var1.addProperty("scope", this.w);
         var1.addProperty("owner", this.Y);
         JsonArray var2 = new JsonArray();
         this.t.values().forEach(var1x -> var2.add(var1x.deepCopy()));
         if (this.D != null && (this.D.L >= 1000L || this.D.O > 0)) {
            if (var2.size() == 32) {
               var2.remove(0);
            }

            var2.add(this.D.MAsJsonObject());
         }

         var1.add("pending", var2);
         return var1;
      }
   }

   static String QAsString(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && !var2.isJsonNull() ? var2.getAsString() : "";
   }

   public static String KAsString(String var0) {
      if (var0 != null && var0.length() <= 253) {
         String var1 = var0.trim().toLowerCase(Locale.ROOT);
         if (!var1.matches("(?:[a-z0-9](?:[a-z0-9.-]*[a-z0-9])?|\\[[0-9a-f:]+\\])(?::[0-9]{1,5})?")) {
            return null;
         } else {
            int var2 = var1.startsWith("[") ? var1.indexOf(58, var1.indexOf(93)) : var1.indexOf(58);
            if (var2 >= 0) {
               int var3 = Integer.parseInt(var1.substring(var2 + 1));
               if (var3 < 1 || var3 > 65535) {
                  return null;
               }

               if (var3 == 25565) {
                  var1 = var1.substring(0, var2);
               }
            }

            return var1;
         }
      } else {
         return null;
      }
   }

   static void BAsVoid(JsonObject var0, long var1) {
      UUID.fromString(QAsString(var0, "sessionId"));
      if (var0.has("connectionId")) {
         UUID.fromString(QAsString(var0, "connectionId"));
      }

      if (KAsString(QAsString(var0, "server")) == null) {
         throw new IllegalArgumentException();
      } else {
         long var3 = Instant.parse(QAsString(var0, "startedAt")).toEpochMilli();
         if (var3 >= var1 - 7776000000L && var3 <= var1 + 86400000L) {
            int var5 = var0.get("playedSeconds").getAsInt();
            int var6 = var0.get("kills").getAsInt();
            if (var5 < 0 || var5 > 86400 || var6 < 0 || var6 > 100000) {
               throw new IllegalArgumentException();
            }
         } else {
            throw new IllegalArgumentException();
         }
      }
   }

   public interface Type_t {
      CompletableFuture<JsonObject> VAsCompletableFuture(String var1, JsonObject var2);
   }

   private static final class Type_y {
      final String w = UUID.randomUUID().toString();
      final String X;
      final String Z;
      final long I;
      long L;
      int O;

      Type_y(String var1, long var2, String var4) {
         this.X = var1;
         this.I = var2;
         this.Z = var4 == null ? this.w : var4;
      }

      JsonObject MAsJsonObject() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("sessionId", this.w);
         var1.addProperty("server", this.X);
         var1.addProperty("connectionId", this.Z);
         var1.addProperty("startedAt", Instant.ofEpochMilli(this.I).toString());
         var1.addProperty("playedSeconds", this.L / 1000L);
         var1.addProperty("kills", this.O);
         return var1;
      }
   }
}
