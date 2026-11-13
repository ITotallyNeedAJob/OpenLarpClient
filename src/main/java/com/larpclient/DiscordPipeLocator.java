package com.larpclient;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

final class DiscordPipeLocator {
   static final int q = 65536;

   static List<String> XAsList(String var0, Map<String, String> var1) {
      ArrayList var2 = new ArrayList();
      if (var0.toLowerCase(Locale.ROOT).startsWith("windows")) {
         for (int var3 = 0; var3 < 10; var3++) {
            var2.add("\\\\?\\pipe\\discord-ipc-" + var3);
         }
      } else {
         LinkedHashSet var9 = new LinkedHashSet();

         for (String var5 : List.of("XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP")) {
            String var6 = var1.get(var5);
            if (var6 != null && !var6.isBlank()) {
               var9.add(var6);
            }
         }

         var9.add("/tmp");

         for (String var11 : (Iterable<String>) (var9)) {
            for (String var7 : List.of("", "app/com.discordapp.Discord", "snap.discord")) {
               for (int var8 = 0; var8 < 10; var8++) {
                  var2.add(Path.of(var11).resolve(var7).resolve("discord-ipc-" + var8).toString());
               }
            }
         }
      }

      return List.copyOf(var2);
   }

   static ByteChannel iAsByteChannel(String var0) throws IOException {
      if (var0.startsWith("\\\\?\\pipe\\")) {
         return new DiscordPipeChannel(var0);
      } else {
         SocketChannel var1 = SocketChannel.open(StandardProtocolFamily.UNIX);

         try {
            var1.configureBlocking(false);
            var1.connect(UnixDomainSocketAddress.of(var0));
            long var2 = System.nanoTime() + TimeUnit.SECONDS.toNanos(1L);

            while (!var1.finishConnect()) {
               if (System.nanoTime() >= var2) {
                  throw new IOException("Discord socket connect timed out");
               }

               AAsVoid();
            }

            var1.configureBlocking(true);
            return var1;
         } catch (RuntimeException | IOException var41) {
            var1.close();
            throw var41;
         }
      }
   }

   static void NAsVoid(ByteChannel var0, int var1, byte[] var2) throws IOException {
      if (var2.length > 65536) {
         throw new IOException("Oversized Discord IPC frame");
      } else {
         ByteBuffer var3 = ByteBuffer.allocate(8 + var2.length).order(ByteOrder.LITTLE_ENDIAN);
         var3.putInt(var1).putInt(var2.length).put(var2).flip();

         while (var3.hasRemaining()) {
            if (var0.write(var3) == 0) {
               AAsVoid();
            }
         }
      }
   }

   static DiscordPipeLocator.Type_i cAsXg_Type_i(ByteChannel var0) throws IOException {
      ByteBuffer var1 = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
      RAsVoid(var0, var1);
      var1.flip();
      int var2 = var1.getInt();
      int var3 = var1.getInt();
      if (var2 >= 0 && var2 <= 4 && var3 >= 0 && var3 <= 65536) {
         ByteBuffer var4 = ByteBuffer.allocate(var3);
         RAsVoid(var0, var4);
         return new DiscordPipeLocator.Type_i(var2, var4.array());
      } else {
         throw new IOException("Invalid Discord IPC frame header");
      }
   }

   private static void RAsVoid(ByteChannel var0, ByteBuffer var1) throws IOException {
      while (var1.hasRemaining()) {
         int var2 = var0.read(var1);
         if (var2 < 0) {
            throw new EOFException("Discord IPC closed");
         }

         if (var2 == 0) {
            AAsVoid();
         }
      }
   }

   private static void AAsVoid() throws InterruptedIOException {
      try {
         Thread.sleep(25L);
      } catch (InterruptedException var1) {
         Thread.currentThread().interrupt();
         throw new InterruptedIOException("Discord IPC interrupted");
      }
   }

   private DiscordPipeLocator() {
   }

   static record Type_i(int opcode, byte[] data) {
   }
}
