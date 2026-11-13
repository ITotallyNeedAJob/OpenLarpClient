package com.larpclient;

import java.util.List;

public record mr(TimedLyricsParser timed, List<String> plain, String status, long retryAfterSeconds) {
   public mr(TimedLyricsParser timed, List<String> plain, String status, long retryAfterSeconds) {
      plain = List.copyOf(plain);
      this.timed = timed;
      this.plain = plain;
      this.status = status;
      this.retryAfterSeconds = retryAfterSeconds;
   }

   public static mr unavailable(String var0, long var1) {
      return new mr(null, List.of(), var0, var1);
   }
}
