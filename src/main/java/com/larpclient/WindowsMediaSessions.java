package com.larpclient;

import com.sun.jna.Function;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import java.io.IOException;
import java.time.Instant;
import java.util.Locale;

public final class WindowsMediaSessions implements DesktopMediaPlatform {
   private static final String b = "Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager";
   private static final String S = "2050C4EE-11A0-57DE-AED7-C97C70338245";
   private static final String K = "00000036-0000-0000-C000-000000000046";
   private static final long t = 4000000000L;
   private volatile boolean T;
   private boolean a;

   public WindowsMediaSessions() {
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public synchronized MediaSessionMetadata read() throws IOException, InterruptedException {
      if (this.T) {
         return null;
      } else if (!Platform.isWindows()) {
         throw new IOException("Windows media sessions require Windows");
      } else if (Thread.currentThread().isVirtual()) {
         throw new IOException("Windows media sessions require the lyrics platform worker");
      } else {
         byte var1 = 0;
         boolean var30 = false /* VF: Semaphore variable */;

         label359: {
            MediaSessionMetadata var12;
            try {
               var30 = true;
               AAsVoid(WindowsMediaSessions.Type_b.ZAsFunction("RoInitialize").invokeInt(new Object[]{1}), "initialize WinRT");
               var1 = 1;
               long var44 = System.nanoTime() + 4000000000L;

               try (
                  WindowsMediaSessions.Type_p var4 = this.zAsR1_Type_p();
                  WindowsMediaSessions.Type_p var5 = this.fAsR1_Type_p(var4.KAsR1_Type_p(6, "request media sessions"), var44);
                  WindowsMediaSessions.Type_p var6 = var5.KAsR1_Type_p(7, "enumerate media sessions");
               ) {
                  if (!this.a) {
                     ClientLog.GAsVoid("WorldLyrics", "Direct Windows media-session reader ready through JNA");
                     this.a = true;
                  }

                  label339:
                  try (WindowsMediaSessions.Type_p var7 = this.QAsR1_Type_p(var6)) {
                     if (var7 != null) {
                        try (
                           WindowsMediaSessions.Type_p var8 = this.fAsR1_Type_p(var7.KAsR1_Type_p(7, "request Spotify metadata"), var44);
                           WindowsMediaSessions.Type_p var9 = var7.KAsR1_Type_p(8, "read Spotify timeline");
                           WindowsMediaSessions.Type_p var10 = var7.KAsR1_Type_p(9, "read Spotify playback");
                        ) {
                           this.lAsVoid();
                           MediaSessionMetadata var11 = zAsX8(
                              var7.RAsString(6, 1024),
                              var8.RAsString(6, 512),
                              var8.RAsString(9, 512),
                              var8.RAsString(10, 512),
                              var9.PAsLong(7),
                              var9.PAsLong(10),
                              var9.PAsLong(11),
                              var10.TAsInt(7) == 4,
                              Instant.now(),
                              System.nanoTime()
                           );
                           var12 = this.T ? null : var11;
                           break label339;
                        }
                     }
                     break label359;
                  }
               }
            } catch (RuntimeException | LinkageError var45) {
               throw new IOException("Windows Spotify media access failed; re-enable WorldLyrics to retry", var45);
            } finally {
               if (var30) {
                  if (var1 != 0) {
                     WindowsMediaSessions.Type_b.ZAsFunction("RoUninitialize").invokeVoid(new Object[0]);
                  }
               }
            }

            WindowsMediaSessions.Type_b.ZAsFunction("RoUninitialize").invokeVoid(new Object[0]);
            return var12;
         }

         WindowsMediaSessions.Type_b.ZAsFunction("RoUninitialize").invokeVoid(new Object[0]);
         return null;
      }
   }

   private void lAsVoid() throws IOException, InterruptedException {
      if (Thread.currentThread().isInterrupted()) {
         throw new InterruptedException("Spotify reader interrupted");
      } else if (this.T) {
         throw new IOException("Windows Spotify reader stopped");
      }
   }

   private WindowsMediaSessions.Type_p zAsR1_Type_p() throws IOException {
      PointerByReference var1 = new PointerByReference();
      GUID var2 = new GUID("2050C4EE-11A0-57DE-AED7-C97C70338245");
      var2.write();
      Memory var3 = new Memory(142L);

      WindowsMediaSessions.Type_p var5;
      try {
         var3.setWideString(0L, "Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager");
         PointerByReference var4 = new PointerByReference();
         AAsVoid(WindowsMediaSessions.Type_b.ZAsFunction("WindowsCreateString").invokeInt(new Object[]{var3, 70, var4}), "create media class name");

         try {
            AAsVoid(
               WindowsMediaSessions.Type_b.ZAsFunction("RoGetActivationFactory").invokeInt(new Object[]{var4.getValue(), var2.getPointer(), var1}),
               "activate media sessions"
            );
            var5 = new WindowsMediaSessions.Type_p(var1.getValue());
         } finally {
            WindowsMediaSessions.Type_b.ZAsFunction("WindowsDeleteString").invokeInt(new Object[]{var4.getValue()});
         }
      } catch (Throwable var121) {
         try {
            var3.close();
         } catch (Throwable var10) {
            var121.addSuppressed(var10);
         }

         throw var121;
      }

      var3.close();
      return var5;
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private WindowsMediaSessions.Type_p QAsR1_Type_p(WindowsMediaSessions.Type_p var1) throws IOException, InterruptedException {
      WindowsMediaSessions.Type_p var2 = null;

      try {
         int var3 = var1.TAsInt(7);
         if (var3 >= 0 && var3 <= 256) {
            for (int var4 = 0; var4 < var3; var4++) {
               this.lAsVoid();
               WindowsMediaSessions.Type_p var5 = var1.KAsR1_Type_p(6, "read media session", var4);
               boolean var20 = false /* VF: Semaphore variable */;

               try {
                  var20 = true;
                  if (var5.RAsString(6, 1024).toLowerCase(Locale.ROOT).contains("spotify")) {
                     int var6;
                     try (WindowsMediaSessions.Type_p var7 = var5.KAsR1_Type_p(9, "read media playback")) {
                        var6 = var7.TAsInt(7) == 4 ? 1 : 0;
                     }

                     if (var2 != null && var6 == 0) {
                        var20 = false;
                     } else {
                        if (var2 != null) {
                           var2.close();
                        }

                        var2 = var5;
                        var5 = null;
                        if (var6 != 0) {
                           var20 = false;
                           break;
                        }

                        var20 = false;
                     }
                  } else {
                     var20 = false;
                  }
               } finally {
                  if (var20) {
                     if (var5 != null) {
                        var5.close();
                     }
                  }
               }

               if (var5 != null) {
                  var5.close();
               }
            }

            WindowsMediaSessions.Type_p var24 = var2;
            var2 = null;
            return var24;
         } else {
            throw new IOException("Too many Windows media sessions");
         }
      } finally {
         if (var2 != null) {
            var2.close();
         }
      }
   }

   WindowsMediaSessions.Type_p fAsR1_Type_p(WindowsMediaSessions.Type_p var1, long var2) throws IOException, InterruptedException {
      WindowsMediaSessions.Type_p var4 = var1;

      WindowsMediaSessions.Type_p var20;
      try (WindowsMediaSessions.Type_p var5 = var1.iAsR1_Type_p("00000036-0000-0000-C000-000000000046")) {
         byte var6 = 0;

         try {
            while (true) {
               this.lAsVoid();
               int var7 = var5.TAsInt(7);
               var6 = (byte)(var7 != 0 ? 1 : 0);
               if (var7 == 1) {
                  var20 = var1.KAsR1_Type_p(8, "complete media request");
                  break;
               }

               if (var7 == 2) {
                  throw new IOException("Windows Spotify media request cancelled");
               }

               if (var7 == 3) {
                  int var8 = var5.TAsInt(8);
                  throw new IOException("Windows Spotify media request failed (" + XAsString(var8) + ")");
               }

               if (var7 != 0) {
                  throw new IOException("Invalid Windows media request state");
               }

               if (System.nanoTime() - var2 >= 0L) {
                  throw new IOException("Windows Spotify media request timed out; re-enable WorldLyrics to retry");
               }

               Thread.sleep(10L);
            }
         } finally {
            if (var6 == 0) {
               var5.kAsInt(9);
            }

            if (var6 != 0 || var5.TAsInt(7) != 0) {
               var5.kAsInt(10);
            }
         }
      } catch (Throwable var19) {
         if (var1 != null) {
            try {
               var4.close();
            } catch (Throwable var15) {
               var19.addSuppressed(var15);
            }
         }

         throw var19;
      }

      if (var1 != null) {
         var1.close();
      }

      return var20;
   }

   static MediaSessionMetadata zAsX8(
      String var0, String var1, String var2, String var3, long var4, long var6, long var8, boolean var10, Instant var11, long var12
   ) {
      if (!var1.isBlank() && !var2.isBlank()) {
         double var14 = Math.max(0.0, (double)var4 / 1.0E7);
         double var16 = (double)var6 / 1.0E7;
         if (var10) {
            double var18 = (double)var11.getEpochSecond() + (double)var11.getNano() / 1.0E9;
            var16 += Math.max(0.0, var18 - ((double)var8 / 1.0E7 - 1.16444736E10));
         }

         var16 = Math.max(0.0, var14 > 0.0 ? Math.min(var14, var16) : var16);
         return new MediaSessionMetadata(var0, var1, var2, var3, var14, var16, var10, var12);
      } else {
         return null;
      }
   }

   @Override
   public void close() {
      this.T = true;
   }

   private static String XAsString(int var0) {
      return String.format(Locale.ROOT, "0x%08X", var0);
   }

   static void AAsVoid(int var0, String var1) throws IOException {
      if (var0 < 0) {
         throw new IOException("Windows Spotify: " + var1 + " failed (" + XAsString(var0) + "); re-enable WorldLyrics to retry");
      }
   }

   private static final class Type_b {
      private static final NativeLibrary O = NativeLibrary.getInstance("combase");

      private Type_b() {
      }

      static Function ZAsFunction(String var0) {
         return O.getFunction(var0, 63);
      }
   }

   static final class Type_p implements AutoCloseable {
      private Pointer G;

      Type_p(Pointer var1) throws IOException {
         if (var1 == null) {
            throw new IOException("Windows media API returned a missing interface");
         } else {
            this.G = var1;
         }
      }

      int kAsInt(int var1, Object... var2) {
         if (this.G == null) {
            throw new IllegalStateException("Released Windows media interface");
         } else {
            Object[] var3 = new Object[var2.length + 1];
            var3[0] = this.G;
            System.arraycopy(var2, 0, var3, 1, var2.length);
            Pointer var4 = this.G.getPointer(0L).getPointer((long)var1 * (long)Native.POINTER_SIZE);
            return Function.getFunction(var4, 63).invokeInt(var3);
         }
      }

      WindowsMediaSessions.Type_p KAsR1_Type_p(int var1, String var2, Object... var3) throws IOException {
         PointerByReference var4 = new PointerByReference();
         Object[] var5 = new Object[var3.length + 1];
         System.arraycopy(var3, 0, var5, 0, var3.length);
         var5[var3.length] = var4;
         WindowsMediaSessions.AAsVoid(this.kAsInt(var1, var5), var2);
         return new WindowsMediaSessions.Type_p(var4.getValue());
      }

      WindowsMediaSessions.Type_p iAsR1_Type_p(String var1) throws IOException {
         GUID var2 = new GUID(var1);
         var2.write();
         return this.KAsR1_Type_p(0, "query media request status", var2.getPointer());
      }

      int TAsInt(int var1) throws IOException {
         IntByReference var2 = new IntByReference();
         WindowsMediaSessions.AAsVoid(this.kAsInt(var1, var2), "read media value");
         return var2.getValue();
      }

      long PAsLong(int var1) throws IOException {
         LongByReference var2 = new LongByReference();
         WindowsMediaSessions.AAsVoid(this.kAsInt(var1, var2), "read media time");
         return var2.getValue();
      }

      String RAsString(int var1, int var2) throws IOException {
         PointerByReference var3 = new PointerByReference();
         WindowsMediaSessions.AAsVoid(this.kAsInt(var1, var3), "read media text");
         Pointer var4 = var3.getValue();

         try {
            IntByReference var5 = new IntByReference();
            Pointer var6 = WindowsMediaSessions.Type_b.ZAsFunction("WindowsGetStringRawBuffer").invokePointer(new Object[]{var4, var5});
            int var7 = var5.getValue();
            if (var7 < 0 || var7 > var2) {
               throw new IOException("Oversized Windows media metadata");
            }

            if (var7 != 0) {
               if (var6 == null) {
                  throw new IOException("Missing Windows media text buffer");
               }

               return new String(var6.getCharArray(0L, var7));
            }
         } finally {
            WindowsMediaSessions.Type_b.ZAsFunction("WindowsDeleteString").invokeInt(new Object[]{var4});
         }

         return "";
      }

      @Override
      public void close() {
         if (this.G != null) {
            this.kAsInt(2);
            this.G = null;
         }
      }
   }
}
