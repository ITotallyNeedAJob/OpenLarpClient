package com.larpclient;

public record DiscordPresenceStatus(DiscordPresenceStatus.Type_d state, DiscordAvatarClient account) {
   private static volatile DiscordPresenceStatus current = new DiscordPresenceStatus(DiscordPresenceStatus.Type_d.OFF, null);

   public static DiscordPresenceStatus current() {
      return current;
   }

   static void publish(DiscordPresenceStatus.Type_d var0, DiscordAvatarClient var1) {
      current = new DiscordPresenceStatus(var0, var0 == DiscordPresenceStatus.Type_d.ONLINE ? var1 : null);
   }

   public static enum Type_d {
      OFF("Discord activity off", -8354674),
      WAITING("Discord offline", -8354674),
      ONLINE("Online", -14441126);

      private final String G;
      private final int Q;

      private Type_d(String var3, int var4) {
         this.G = var3;
         this.Q = var4;
      }

      public String TAsString() {
         return this.G;
      }

      public int WAsInt() {
         return this.Q;
      }
   }
}
