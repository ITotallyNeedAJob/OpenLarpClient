package com.larpclient;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

final class DbusSession implements AutoCloseable {
   static final int DropletTrail = 1048576;
   private static final int BloomUtility = 32;
   private static final int ChinaHatCodec = 65536;
   private final SocketChannel g;
   private final Selector C;
   private int q;
   private volatile boolean h;

   DbusSession(SocketChannel var1, String var2, long var3) throws IOException, InterruptedException {
      this.g = var1;
      var1.configureBlocking(false);
      this.C = Selector.open();

      try {
         var1.register(this.C, 0);
         this.GAsVoid(var2, var3);
         if (this.JAsList("org.freedesktop.DBus", "/org/freedesktop/DBus", "org.freedesktop.DBus", "Hello", var3) == null) {
            throw new IOException("D-Bus session refused Hello");
         }
      } catch (InterruptedException | RuntimeException | IOException var6) {
         this.C.close();
         throw var6;
      }
   }

   static DbusSession iAsRk(long var0) throws IOException, InterruptedException {
      String var2 = EAsString();
      IOException var3 = null;

      for (String var5 : wAsList(System.getenv("DBUS_SESSION_BUS_ADDRESS"), System.getenv("XDG_RUNTIME_DIR"), var2)) {
         if (Files.exists(Path.of(var5))) {
            SocketChannel var6 = null;

            try {
               var6 = SocketChannel.open(UnixDomainSocketAddress.of(var5));
               DbusSession var7 = new DbusSession(var6, var2, var0);
               var6 = null;
               return var7;
            } catch (IOException var121) {
               var3 = var121;
            } finally {
               if (var6 != null) {
                  var6.close();
               }
            }
         }
      }

      throw var3 != null ? var3 : new IOException("No D-Bus session socket found");
   }

   static List<String> wAsList(String var0, String var1, String var2) {
      LinkedHashSet var3 = new LinkedHashSet();
      if (var0 != null) {
         for (String var7 : var0.split(";")) {
            int var8 = var7.indexOf(58);
            if (var8 >= 0 && var7.substring(0, var8).equals("unix")) {
               for (String var12 : var7.substring(var8 + 1).split(",")) {
                  if (var12.startsWith("path=")) {
                     String var13 = pAsString(var12.substring(5));
                     if (var13 != null && !var13.isEmpty()) {
                        var3.add(var13);
                     }
                  }
               }
            }
         }
      }

      if (var1 != null && var1.startsWith("/")) {
         var3.add(var1.replaceAll("/+$", "") + "/bus");
      }

      if (var2 != null && var2.matches("\\d{1,10}")) {
         var3.add("/run/user/" + var2 + "/bus");
      }

      return List.copyOf(var3);
   }

   private static String pAsString(String var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();

      for (int var2 = 0; var2 < var0.length(); var2++) {
         char var3 = var0.charAt(var2);
         if (var3 != '%') {
            var1.write(var3);
         } else {
            if (var2 + 2 >= var0.length()) {
               return null;
            }

            int var4 = Character.digit(var0.charAt(var2 + 1), 16);
            int var5 = Character.digit(var0.charAt(var2 + 2), 16);
            if (var4 < 0 || var5 < 0) {
               return null;
            }

            var1.write(var4 << 4 | var5);
            var2 += 2;
         }
      }

      return var1.toString(StandardCharsets.UTF_8);
   }

   private static String EAsString() throws IOException {
      try {
         return String.valueOf(Files.getAttribute(Path.of("/proc/self"), "unix:uid"));
      } catch (IllegalArgumentException | UnsupportedOperationException var1) {
         throw new IOException("Cannot determine the Linux user for D-Bus", var1);
      }
   }

   private void GAsVoid(String var1, long var2) throws IOException, InterruptedException {
      StringBuilder var4 = new StringBuilder();

      for (byte var8 : var1.getBytes(StandardCharsets.US_ASCII)) {
         var4.append(String.format(Locale.ROOT, "%02x", var8));
      }

      this.xAsVoid(ByteBuffer.wrap(("\u0000AUTH EXTERNAL " + var4 + "\r\n").getBytes(StandardCharsets.US_ASCII)), var2);
      if (!this.WAsString(var2).startsWith("OK ")) {
         throw new IOException("D-Bus session rejected authentication");
      } else {
         this.xAsVoid(ByteBuffer.wrap("BEGIN\r\n".getBytes(StandardCharsets.US_ASCII)), var2);
      }
   }

   private String WAsString(long var1) throws IOException, InterruptedException {
      StringBuilder var3 = new StringBuilder();
      ByteBuffer var4 = ByteBuffer.allocate(1);

      while (var3.length() < 512) {
         this.nAsVoid(var4.clear(), var1);
         char var5 = (char)(var4.get(0) & 255);
         if (var5 == '\n') {
            return var3.toString().strip();
         }

         var3.append(var5);
      }

      throw new IOException("Oversized D-Bus authentication reply");
   }

   List<Object> JAsList(String var1, String var2, String var3, String var4, long var5, String... var7) throws IOException, InterruptedException {
      this.q = this.q == Integer.MAX_VALUE ? 1 : this.q + 1;
      int var8 = this.q;
      this.xAsVoid(XAsByteBuffer(var8, var1, var2, var3, var4, var7), var5);

      while (System.nanoTime() - var5 < 0L) {
         DbusSession.Type_k var9 = new DbusSession.Type_k(this.BAsByteBuffer(var5));
         if (var9.b == (long)var8 && (var9.q == 2 || var9.q == 3)) {
            return var9.q == 3 ? null : var9.xAsList();
         }
      }

      throw new IOException("D-Bus request timed out");
   }

   static ByteBuffer XAsByteBuffer(int var0, String var1, String var2, String var3, String var4, String... var5) {
      ByteBuffer var6 = ByteBuffer.allocate(4096).order(ByteOrder.LITTLE_ENDIAN);
      var6.put((byte)108).put((byte)1).put((byte)0).put((byte)1).putInt(0).putInt(var0).putInt(0);
      xAsVoid(var6, 1, "o", var2);
      xAsVoid(var6, 6, "s", var1);
      xAsVoid(var6, 2, "s", var3);
      xAsVoid(var6, 3, "s", var4);
      if (var5.length > 0) {
         xAsVoid(var6, 8, "g", "s".repeat(var5.length));
      }

      var6.putInt(12, var6.position() - 16);
      SAsVoid(var6, 8);
      int var7 = var6.position();

      for (String var11 : var5) {
         MAsVoid(var6, var11);
      }

      var6.putInt(4, var6.position() - var7);
      return var6.flip();
   }

   private static void xAsVoid(ByteBuffer var0, int var1, String var2, String var3) {
      SAsVoid(var0, 8);
      var0.put((byte)var1);
      EAsVoid(var0, var2);
      if (var2.equals("g")) {
         EAsVoid(var0, var3);
      } else {
         MAsVoid(var0, var3);
      }
   }

   private static void SAsVoid(ByteBuffer var0, int var1) {
      while (var0.position() % var1 != 0) {
         var0.put((byte)0);
      }
   }

   private static void MAsVoid(ByteBuffer var0, String var1) {
      byte[] var2 = var1.getBytes(StandardCharsets.UTF_8);
      SAsVoid(var0, 4);
      var0.putInt(var2.length).put(var2).put((byte)0);
   }

   private static void EAsVoid(ByteBuffer var0, String var1) {
      byte[] var2 = var1.getBytes(StandardCharsets.US_ASCII);
      var0.put((byte)var2.length).put(var2).put((byte)0);
   }

   private ByteBuffer BAsByteBuffer(long var1) throws IOException, InterruptedException {
      ByteBuffer var3 = ByteBuffer.allocate(16);
      this.nAsVoid(var3, var1);
      byte var4 = var3.get(0);
      if (var4 != 108 && var4 != 66) {
         throw new IOException("Invalid D-Bus message");
      } else {
         var3.order(var4 == 108 ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
         long var5 = (long)var3.getInt(12) & 4294967295L;
         long var7 = (long)var3.getInt(4) & 4294967295L;
         long var9 = (16L + var5 + 7L & -8L) + var7;
         if (var3.get(3) == 1 && var9 <= 1048576L) {
            ByteBuffer var11 = ByteBuffer.allocate((int)var9).order(var3.order());
            var11.put(var3.flip());
            this.nAsVoid(var11, var1);
            return var11.flip();
         } else {
            throw new IOException("Unsupported or oversized D-Bus message");
         }
      }
   }

   private void nAsVoid(ByteBuffer var1, long var2) throws IOException, InterruptedException {
      while (var1.hasRemaining()) {
         int var4 = this.g.read(var1);
         if (var4 < 0) {
            throw new EOFException("D-Bus session closed the connection");
         }

         if (var4 == 0) {
            this.yAsVoid(1, var2);
         }
      }
   }

   private void xAsVoid(ByteBuffer var1, long var2) throws IOException, InterruptedException {
      while (var1.hasRemaining()) {
         if (this.g.write(var1) == 0) {
            this.yAsVoid(4, var2);
         }
      }
   }

   private void yAsVoid(int var1, long var2) throws IOException, InterruptedException {
      if (this.h) {
         throw new IOException("D-Bus session closed");
      } else {
         long var4 = var2 - System.nanoTime();
         if (var4 <= 0L) {
            throw new IOException("D-Bus request timed out");
         } else {
            try {
               SelectionKey var6 = this.g.keyFor(this.C);
               if (var6 == null) {
                  throw new IOException("D-Bus session closed");
               }

               var6.interestOps(var1);
               this.C.select(Math.max(1L, TimeUnit.NANOSECONDS.toMillis(var4)));
               this.C.selectedKeys().clear();
            } catch (CancelledKeyException | ClosedSelectorException var71) {
               throw new IOException("D-Bus session closed", var71);
            }

            if (Thread.interrupted()) {
               throw new InterruptedException("D-Bus request interrupted");
            } else if (this.h) {
               throw new IOException("D-Bus session closed");
            }
         }
      }
   }

   @Override
   public void close() {
      this.h = true;

      try {
         this.g.close();
      } catch (IOException var2) {
      }

      try {
         this.C.close();
      } catch (IOException var1) {
      }
   }

   static final class Type_k {
      private final ByteBuffer w;
      private final int Y;
      private int c;
      private int I;
      final int q;
      long b;
      private String PriceDetector = "";

      Type_k(ByteBuffer var1) throws IOException {
         this.w = var1;
         if (var1.limit() < 16) {
            throw MAsIOException();
         } else {
            this.q = var1.get(1);
            this.Y = (int)(16L + ((long)var1.getInt(12) & 4294967295L) + 7L & -8L);
            this.c = 12;
            Object var3 = this.WAsObject("PetEffectRenderer(yv)", new int[1], 0);
            if (var3 instanceof List) {
               for (Object var4 : (List)var3) {
                  List var5 = (List)var4;
                  long var6 = (Long)var5.get(0);
                  if (var6 == 5L && var5.get(1) instanceof Long var8) {
                     this.b = var8;
                  }

                  if (var6 == 8L && var5.get(1) instanceof String var11) {
                     this.m = var11;
                  }

                  if (var6 == 9L && !Long.valueOf(0L).equals(var5.get(1))) {
                     throw new IOException("Unexpected D-Bus file descriptors");
                  }
               }

               if (this.c > this.Y || this.Y > var1.limit()) {
                  throw MAsIOException();
               }
            } else {
               throw MAsIOException();
            }
         }
      }

      List<Object> xAsList() throws IOException {
         this.c = this.Y;
         ArrayList var1 = new ArrayList();
         int[] var2 = new int[]{0};

         while (var2[0] < this.m.length()) {
            var1.add(this.WAsObject(this.m, var2, 0));
         }

         if (this.c != this.w.limit()) {
            throw MAsIOException();
         } else {
            return var1;
         }
      }

      private Object WAsObject(String var1, int[] var2, int var3) throws IOException {
         if (var3 <= 32 && var2[0] < var1.length() && ++this.I <= 65536) {
            char var4 = var1.charAt((int)(var2[0]++));
            switch (var4) {
               case '(':
                  this.LAsInt(8, 0);
                  ArrayList var15 = new ArrayList();

                  while (var2[0] < var1.length() && var1.charAt(var2[0]) != ')') {
                     var15.add(this.WAsObject(var1, var2, var3 + 1));
                  }

                  if (var2[0]++ < var1.length() && !var15.isEmpty()) {
                     return var15;
                  } else {
                     throw MAsIOException();
                  }
               case 'a':
                  long var14 = (long)this.w.getInt(this.LAsInt(4, 4)) & 4294967295L;
                  int var16 = jAsInt(var1, var2[0], var3 + 1);
                  String var8 = var1.substring(var2[0], var16);
                  var2[0] = var16;
                  this.LAsInt(hAsInt(var8.charAt(0)), 0);
                  if (var14 > (long)(this.w.limit() - this.c)) {
                     throw MAsIOException();
                  } else {
                     int var9 = this.c + (int)var14;
                     if (var8.charAt(0) == '{') {
                        LinkedHashMap var17 = new LinkedHashMap();

                        while (this.c < var9) {
                           this.LAsInt(8, 0);
                           int[] var11 = new int[]{1};
                           Object var12 = this.WAsObject(var8, var11, var3 + 1);
                           var17.put(var12, this.WAsObject(var8, var11, var3 + 1));
                           if (var11[0] != var8.length() - 1) {
                              throw MAsIOException();
                           }
                        }

                        if (this.c != var9) {
                           throw MAsIOException();
                        }

                        return var17;
                     } else {
                        ArrayList var10 = new ArrayList();

                        while (this.c < var9) {
                           var10.add(this.WAsObject(var8, new int[1], var3 + 1));
                        }

                        if (this.c != var9) {
                           throw MAsIOException();
                        }

                        return var10;
                     }
                  }
               case 'b':
                  int var13 = this.w.getInt(this.LAsInt(4, 4));
                  if (var13 != 0 && var13 != 1) {
                     throw MAsIOException();
                  }

                  return var13 == 1;
               case 'd':
                  return this.w.getDouble(this.LAsInt(8, 8));
               case 'g':
                  return this.nAsString((long)(this.w.get(this.LAsInt(1, 1)) & 255));
               case 'h':
               case 'u':
                  return (long)this.w.getInt(this.LAsInt(4, 4)) & 4294967295L;
               case 'i':
                  return (long)this.w.getInt(this.LAsInt(4, 4));
               case 'n':
                  return (long)this.w.getShort(this.LAsInt(2, 2));
               case 'o':
               case 's':
                  return this.nAsString((long)this.w.getInt(this.LAsInt(4, 4)) & 4294967295L);
               case 'q':
                  return (long)(this.w.getShort(this.LAsInt(2, 2)) & '\uffff');
               case 't':
               case 'x':
                  return this.w.getLong(this.LAsInt(8, 8));
               case 'v':
                  String var5 = this.nAsString((long)(this.w.get(this.LAsInt(1, 1)) & 255));
                  int[] var6 = new int[]{0};
                  Object var7 = this.WAsObject(var5, var6, var3 + 1);
                  if (var6[0] != var5.length()) {
                     throw MAsIOException();
                  }

                  return var7;
               case 'y':
                  return (long)(this.w.get(this.LAsInt(1, 1)) & 255);
               default:
                  throw MAsIOException();
            }
         } else {
            throw MAsIOException();
         }
      }

      private String nAsString(long var1) throws IOException {
         if (var1 > (long)this.w.limit()) {
            throw MAsIOException();
         } else {
            int var3 = this.LAsInt(1, (int)var1 + 1);
            if (this.w.get(var3 + (int)var1) != 0) {
               throw MAsIOException();
            } else {
               byte[] var4 = new byte[(int)var1];
               this.w.get(var3, var4);
               return new String(var4, StandardCharsets.UTF_8);
            }
         }
      }

      private int LAsInt(int var1, int var2) throws IOException {
         int var3 = this.c + var1 - 1 & -var1;
         if (var3 >= this.c && var3 <= this.w.limit() - var2) {
            this.c = var3 + var2;
            return var3;
         } else {
            throw MAsIOException();
         }
      }

      private static int jAsInt(String var0, int var1, int var2) throws IOException {
         if (var2 <= 32 && var1 < var0.length()) {
            char var3 = var0.charAt(var1);
            if (var3 == 'a') {
               return jAsInt(var0, var1 + 1, var2 + 1);
            } else if (var3 == '(' || var3 == '{') {
               int var4 = var3 == '(' ? 41 : 125;
               int var5 = var1 + 1;

               while (var5 < var0.length() && var0.charAt(var5) != var4) {
                  var5 = jAsInt(var0, var5, var2 + 1);
               }

               if (var5 < var0.length() && var5 != var1 + 1) {
                  return var5 + 1;
               } else {
                  throw MAsIOException();
               }
            } else if ("ybnqiuxtdsogvh".indexOf(var3) < 0) {
               throw MAsIOException();
            } else {
               return var1 + 1;
            }
         } else {
            throw MAsIOException();
         }
      }

      private static int hAsInt(char var0) {
         return switch (var0) {
            case '(', 'd', 't', 'x', '{' -> 8;
            case 'g', 'v', 'y' -> 1;
            case 'n', 'q' -> 2;
            default -> 4;
         };
      }

      private static IOException MAsIOException() {
         return new IOException("Invalid D-Bus message");
      }
   }
}
