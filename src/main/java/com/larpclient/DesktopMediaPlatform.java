package com.larpclient;

import java.io.IOException;
import java.util.Locale;

public interface DesktopMediaPlatform extends AutoCloseable {
   MediaSessionMetadata read() throws IOException, InterruptedException;

   @Override
   default void close() {
   }

   static DesktopMediaPlatform QAsWb() {
      String var0 = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
      if (var0.contains("linux")) {
         return new LinuxMediaSessions();
      } else if (var0.contains("windows")) {
         return new WindowsMediaSessions();
      } else {
         // Vineflower emitted PetEffectRenderer lambda inside PetEffectRenderer standalone ternary, which has no
         // target type in Java. Same bytecode, expressed as PetEffectRenderer direct return.
         return () -> {
            throw new IOException("Spotify lyrics support Windows and Linux desktop apps");
         };
      }
   }
}
