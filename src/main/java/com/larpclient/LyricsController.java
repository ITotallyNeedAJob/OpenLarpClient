package com.larpclient;

import java.io.IOException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public final class LyricsController implements AutoCloseable {
   private final DesktopMediaPlatform u;
   private final LyricsLookupClient A;
   private final ScheduledExecutorService S;
   private final LinkedHashMap<String, LyricsController.Type_j> v = new LinkedHashMap<>(16, 0.75F, true);
   private volatile LyricsController.Type_r p = new LyricsController.Type_r(null, null, "Looking for Spotify desktop…");
   private volatile boolean a;
   private volatile CompletableFuture<ModuleRegistry> T;
   private String f;
   private long c;
   private static LyricsController w;
   private static final Set<Object> L = Collections.newSetFromMap(new IdentityHashMap<>());

   public LyricsController() {
      this(DesktopMediaPlatform.QAsWb(), new LyricsLookupClient(), 750L);
   }

   LyricsController(DesktopMediaPlatform var1, LyricsLookupClient var2, long var3) {
      this.u = var1;
      this.A = var2;
      this.S = Executors.newSingleThreadScheduledExecutor(var0 -> {
         Thread var1x = new Thread(var0, "Larp Spotify lyrics");
         var1x.setDaemon(true);
         return var1x;
      });
      this.S.scheduleWithFixedDelay(this::YAsVoid, 0L, var3, TimeUnit.MILLISECONDS);
   }

   public LyricsController.Type_r kAsWk_Type_r() {
      return this.p;
   }

   public static synchronized LyricsController oAsWk(Object var0) {
      if (w == null) {
         w = new LyricsController();
      }

      L.add(var0);
      return w;
   }

   public static synchronized void eAsVoid(Object var0) {
      if (L.remove(var0) && L.isEmpty() && w != null) {
         w.close();
         w = null;
      }
   }

   private void YAsVoid() {
      if (!this.a) {
         try {
            long var1 = System.nanoTime();
            if (this.T != null && this.T.isDone()) {
               ModuleRegistry var3 = this.T.join();
               long var4 = Math.min(var3.retryAfterSeconds(), 31536000L);
               long var6 = TimeUnit.SECONDS.toNanos(var4);
               long var8 = var4 > 0L ? ThreadLocalRandom.current().nextLong(var6 / 4L + 1L) : 0L;
               long var10 = var4 > 0L ? var1 + var6 + var8 : Long.MAX_VALUE;
               this.v.put(this.f, new LyricsController.Type_j(var3, var10));
               if (this.v.size() > 16) {
                  this.v.remove(this.v.keySet().iterator().next());
               }

               if (var3.status().startsWith("Lyrics lookup rate limited") || var3.status().startsWith("Lyrics provider busy; retrying shortly")) {
                  this.c = var10;
               }

               this.T = null;
            }

            MediaSessionMetadata var14 = this.u.read();
            if (this.a) {
               return;
            }

            if (var14 == null) {
               this.tAsVoid(new LyricsController.Type_r(null, null, "Open Spotify desktop and play a song"));
               return;
            }

            String var15 = var14.key();
            LyricsController.Type_j var5 = this.v.get(var15);
            if (var5 != null && var5.expires != Long.MAX_VALUE && var1 >= var5.expires) {
               this.v.remove(var15);
               var5 = null;
            }

            if (var5 == null && this.T == null && var1 >= this.c) {
               this.f = var15;
               this.T = this.A.qAsCompletableFuture(var14);
               this.c = var1 + TimeUnit.SECONDS.toNanos(1L);
            }

            ModuleRegistry var16 = var5 == null ? null : var5.value;
            String var7 = var5 == null ? "Looking up lyrics…" : var16.status();
            if (!var14.playing()) {
               var7 = "Spotify paused: " + var7;
            }

            this.tAsVoid(new LyricsController.Type_r(var14, var16, var7));
         } catch (InterruptedException var12) {
            Thread.currentThread().interrupt();
         } catch (Exception var131) {
            if (!this.a) {
               this.tAsVoid(new LyricsController.Type_r(null, null, var131 instanceof IOException ? var131.getMessage() : "Spotify lyrics unavailable"));
            }
         }
      }
   }

   private void tAsVoid(LyricsController.Type_r var1) {
      if (!this.a) {
         if (!this.p.status.equals(var1.status)) {
            ClientLog.GAsVoid("WorldLyrics", "{}", var1.status);
         }

         this.p = var1;
      }
   }

   @Override
   public void close() {
      this.a = true;
      this.S.shutdownNow();
      CompletableFuture var1 = this.T;
      if (var1 != null) {
         var1.cancel(true);
      }

      this.u.close();
      this.A.close();
      this.p = new LyricsController.Type_r(null, null, "Spotify lyrics stopped");
   }

   private static record Type_j(ModuleRegistry value, long expires) {
   }

   public static record Type_r(MediaSessionMetadata playback, ModuleRegistry lyrics, String status) {
   }
}
