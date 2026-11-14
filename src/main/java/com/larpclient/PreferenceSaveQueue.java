package com.larpclient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

final class PreferenceSaveQueue implements AutoCloseable {
   private final PreferenceSaveQueue.Type_l Y;
   private final Map<Path, String> d = new LinkedHashMap<>();
   private final ScheduledThreadPoolExecutor M;
   private ScheduledFuture<?> Z;
   private volatile String r = "";
   private boolean U;

   PreferenceSaveQueue(PreferenceSaveQueue.Type_l var1) {
      this.Y = var1;
      this.M = new ScheduledThreadPoolExecutor(1, var0 -> {
         Thread var1x = new Thread(var0, "Larp preferences");
         var1x.setDaemon(true);
         return var1x;
      });
      this.M.setRemoveOnCancelPolicy(true);
      this.M.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
   }

   synchronized void PAsVoid(Path var1, String var2) {
      if (this.U) {
         try {
            this.Y.SAsVoid(var1, var2);
            ClientLog.GAsVoid("Config", "Save requested after shutdown began; wrote {} directly", var1.getFileName());
         } catch (RuntimeException | IOException var4) {
            this.r = "Save failed while closing; see the Config log.";
            ClientLog.lAsVoid("Config", "Save after shutdown failed for " + var1.getFileName(), var4);
         }
      } else {
         this.d.put(var1, var2);
         if (this.Z != null) {
            this.Z.cancel(false);
         }

         this.Z = this.M.schedule((Runnable)this::kAsVoid, 250L, TimeUnit.MILLISECONDS);
      }
   }

   String iAsString() {
      return this.r;
   }

   private void kAsVoid() {
      LinkedHashMap var1;
      synchronized (this) {
         var1 = new LinkedHashMap<>(this.d);
         this.d.clear();
      }

      for (Entry var3 : (Iterable<Entry>) (var1.entrySet())) {
         try {
            this.Y.SAsVoid((Path)var3.getKey(), (String)var3.getValue());
         } catch (RuntimeException | IOException var10) {
            synchronized (this) {
               this.d.putIfAbsent((Path)var3.getKey(), (String)var3.getValue());
            }

            if (this.r.isEmpty()) {
               ClientLog.lAsVoid("Config", "Deferred save failed; snapshot retained for retry", var10);
            }

            this.r = "Save failed. Changes are retained for retry; see the Config log.";
         }
      }

      synchronized (this) {
         if (this.d.isEmpty()) {
            this.r = "";
         }
      }
   }

   void lAsVoid(Path var1, String var2) throws IOException {
      synchronized (this) {
         if (this.U) {
            this.d.remove(var1);
            this.Y.SAsVoid(var1, var2);
            return;
         }
      }

      kAsVoid(this.M.submit(() -> {
         synchronized (this) {
            this.d.remove(var1);
         }

         this.Y.SAsVoid(var1, var2);
         return null;
      }));
      synchronized (this) {
         if (this.d.isEmpty()) {
            this.r = "";
         }
      }
   }

   void tAsVoid() throws IOException {
      boolean var1;
      synchronized (this) {
         var1 = this.U;
      }

      if (var1) {
         this.kAsVoid();
      } else {
         kAsVoid(this.M.submit((Runnable)this::kAsVoid));
      }

      synchronized (this) {
         if (!this.d.isEmpty()) {
            throw new IOException(this.r);
         }
      }
   }

   private static void kAsVoid(Future<?> var0) throws IOException {
      try {
         var0.get();
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
         throw new IOException("Save interrupted", var2);
      } catch (ExecutionException var3) {
         throw new IOException("Save failed", var3.getCause());
      }
   }

   @Override
   public void close() throws IOException {
      try {
         this.tAsVoid();
      } finally {
         synchronized (this) {
            this.U = true;
         }

         this.M.shutdown();
      }
   }

   @FunctionalInterface
   interface Type_l {
      void SAsVoid(Path var1, String var2) throws IOException;
   }
}
