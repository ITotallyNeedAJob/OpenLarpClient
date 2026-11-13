package com.larpclient;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ClientLog {
   private static final Logger z = LoggerFactory.getLogger("Larp");
   private static final Set<String> H = new HashSet<>();

   public ClientLog() {
   }

   public static void GAsVoid(String var0, String var1, Object... var2) {
      NotificationCategory.KAsVoid(var0, "Info", null);
      z.info("[Larp/" + var0 + "] " + var1, var2);
   }

   public static void XAsVoid(String var0, String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         GAsVoid(var0, var1, var2);
      } else {
         z.debug("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void oAsVoid(String var0, String var1, Object... var2) {
      NotificationCategory.KAsVoid(var0, "Warning", null);
      z.warn("[Larp/" + var0 + "] " + var1, var2);
   }

   public static void lAsVoid(String var0, String var1, Throwable var2) {
      NotificationCategory.KAsVoid(var0, "Error", var2);
      z.error("[Larp/" + var0 + "] " + var1, var2);
   }

   public static void hAsVoid(String var0, String var1, Throwable var2) {
      if (H.add(var0 + var1)) {
         lAsVoid(var0, var1, var2);
      }
   }
}
