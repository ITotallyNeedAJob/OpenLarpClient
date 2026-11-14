package com.larpclient;

import java.util.List;

public record ModuleRegistry(TimedLyricsParser timed, List<String> plain, String status, long retryAfterSeconds) {
   public ModuleRegistry(TimedLyricsParser timed, List<String> plain, String status, long retryAfterSeconds) {
      plain = List.copyOf(plain);
      this.timed = timed;
      this.plain = plain;
      this.status = status;
      this.retryAfterSeconds = retryAfterSeconds;
   }

   public static mr unavailable(String var0, long var1) {
      return new ModuleRegistry(null, List.of(), var0, var1);
   }
}
