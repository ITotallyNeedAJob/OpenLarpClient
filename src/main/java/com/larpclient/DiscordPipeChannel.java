package com.larpclient;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

final class DiscordPipeChannel implements ByteChannel {
   private final HANDLE U;
   private final IntByReference q = new IntByReference();
   private final IntByReference j = new IntByReference();
   private volatile boolean L = true;

   DiscordPipeChannel(String var1) throws IOException {
      this.U = Kernel32.INSTANCE.CreateFile(var1, -1073741824, 0, null, 3, 0, null);
      if (WinBase.INVALID_HANDLE_VALUE.equals(this.U)) {
         throw cAsIOException("open");
      } else if (!Kernel32.INSTANCE.SetNamedPipeHandleState(this.U, new IntByReference(1), null, null)) {
         IOException var2 = cAsIOException("nonblocking mode");
         this.close();
         throw var2;
      }
   }

   @Override
   public synchronized int read(ByteBuffer var1) throws IOException {
      if (!this.L) {
         return -1;
      } else if (!var1.hasRemaining()) {
         return 0;
      } else if (!Kernel32.INSTANCE.PeekNamedPipe(this.U, null, 0, null, this.q, null)) {
         throw cAsIOException("peek");
      } else {
         int var2 = Math.min(var1.remaining(), this.q.getValue());
         if (var2 == 0) {
            return 0;
         } else {
            byte[] var3 = new byte[var2];
            if (!Kernel32.INSTANCE.ReadFile(this.U, var3, var2, this.j, null)) {
               throw cAsIOException("read");
            } else {
               var1.put(var3, 0, this.j.getValue());
               return this.j.getValue();
            }
         }
      }
   }

   @Override
   public synchronized int write(ByteBuffer var1) throws IOException {
      if (!this.L) {
         throw new IOException("Discord pipe closed");
      } else {
         byte[] var2 = new byte[var1.remaining()];
         var1.duplicate().get(var2);
         if (!Kernel32.INSTANCE.WriteFile(this.U, var2, var2.length, this.j, null)) {
            throw cAsIOException("write");
         } else {
            var1.position(var1.position() + this.j.getValue());
            return this.j.getValue();
         }
      }
   }

   @Override
   public boolean isOpen() {
      return this.L;
   }

   @Override
   public synchronized void close() throws IOException {
      if (this.L) {
         this.L = false;
         if (!Kernel32.INSTANCE.CloseHandle(this.U)) {
            throw cAsIOException("close");
         }
      }
   }

   private static IOException cAsIOException(String var0) {
      return new IOException("Discord pipe " + var0 + " failed (Win32 " + Kernel32.INSTANCE.GetLastError() + ")");
   }
}
