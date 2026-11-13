package com.larpclient;

import net.minecraft.client.util.InputUtil.Type;
import org.lwjgl.glfw.GLFW;

public final class KeybindNames {
   public static final int G = -1;
   private static final int o = 1;
   private static final int B = 8;

   public static boolean KAsBoolean(int var0) {
      return var0 >= 1 && var0 < 9;
   }

   public static boolean jAsBoolean(int var0) {
      return var0 >= 32 && var0 <= 348;
   }

   public static boolean hAsBoolean(int var0) {
      return KAsBoolean(var0) || jAsBoolean(var0);
   }

   public static int ZAsInt(int var0) {
      return var0 >= 0 && var0 < 8 ? 1 + var0 : -1;
   }

   public static int hAsInt(int var0) {
      return KAsBoolean(var0) ? var0 - 1 : -1;
   }

   public static boolean RAsBoolean(int var0) {
      return var0 >= 2 && var0 < 8;
   }

   public static boolean sAsBoolean(long var0, int var2) {
      return KAsBoolean(var2) ? GLFW.glfwGetMouseButton(var0, hAsInt(var2)) == 1 : jAsBoolean(var2) && GLFW.glfwGetKey(var0, var2) == 1;
   }

   public static String pAsString(int var0) {
      return switch (hAsInt(var0)) {
         case 0 -> "Left Mouse";
         case 1 -> "Right Mouse";
         case 2 -> "Middle Mouse";
         default -> "Mouse " + (hAsInt(var0) + 1);
      };
   }

   public static String eAsString(int var0) {
      if (KAsBoolean(var0)) {
         return pAsString(var0);
      } else {
         return !jAsBoolean(var0) ? "Unbound" : Type.KEYSYM.createFromCode(var0).getLocalizedText().getString();
      }
   }

   private KeybindNames() {
   }
}
