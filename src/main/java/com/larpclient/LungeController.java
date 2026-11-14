package com.larpclient;

import java.util.List;
import java.util.Locale;

public final class LungeController {
   public static final int U = 5;
   public static final int K = 4;
   public static final int ChinaHatCodec = 1000;

   public static int NAsInt(boolean var0) {
      return var0 ? 4 : 0;
   }

   public static int ZAsInt(int var0, float var1, float var2, int var3) {
      if (!(var2 <= 0.0F) && var1 > 0.0F) {
         for (int var4 = 0; var4 < 1000; var4++) {
            if (!((float)(var0 + var4 + var3) / var1 < var2)) {
               return var4;
            }
         }

         return 1000;
      } else {
         return 0;
      }
   }

   public static boolean LAsBoolean(LungeController.Type_c var0, boolean var1, LungeController.Type_n var2) {
      return !var1 && !var0.sameItem() && var2 == LungeController.Type_n.RESET;
   }

   public static int qAsInt(LungeController.Type_c var0, boolean var1, LungeController.Type_n var2, int var3, float var4, boolean var5) {
      return !LAsBoolean(var0, var1, var2) ? ZAsInt(var3, var4, var0.minimum(), NAsInt(var5)) : 1 + ZAsInt(0, var0.delay(), var0.minimum(), var5 ? 5 : 0);
   }

   public static int NAsInt(float var0, float var1) {
      return ZAsInt(0, var0, var1, 5);
   }

   public static LungeController.Type_g fAsMh_Type_g(
      List<LungeController.Type_c> var0, int var1, LungeController.Type_n var2, int var3, float var4, boolean var5
   ) {
      LungeController.Type_c var6 = null;
      int var7 = 1000;

      for (LungeController.Type_c var9 : var0) {
         int var10 = var9.slot() == var1 ? 1 : 0;
         int var11 = qAsInt(var9, var10 != 0, var2, var3, var4, var5);
         if (var6 == null || uAsBoolean(var9, var10 != 0, var11, var6, var6.slot() == var1, var7)) {
            var6 = var9;
            var7 = var11;
         }
      }

      return var6 == null
         ? null
         : new LungeController.Type_g(var6.slot(), var7, var6.slot() != var1, var6.lunge(), LAsBoolean(var6, var6.slot() == var1, var2));
   }

   private static boolean uAsBoolean(LungeController.Type_c var0, boolean var1, int var2, LungeController.Type_c var3, boolean var4, int var5) {
      if (var2 != var5) {
         return var2 < var5;
      } else if (var1 != var4) {
         return var1;
      } else if (var0.breaks() != var3.breaks()) {
         return !var0.breaks();
      } else if (var0.lunge() != var3.lunge()) {
         return var0.lunge() > var3.lunge();
      } else {
         return var0.durability() != var3.durability() ? var0.durability() > var3.durability() : var0.slot() < var3.slot();
      }
   }

   public static String NAsString(int var0, boolean var1, boolean var2, boolean var3, boolean var4) {
      if (var3) {
         return "Lunge does not work while riding";
      } else if (var4) {
         return "Lunge does not work while gliding";
      } else if (var2) {
         return "Lunge does not work in water";
      } else {
         return !var1 && var0 < 7 ? "Lunge needs 7 food (you have " + var0 + ")" : null;
      }
   }

   public static String kAsString(int var0) {
      return String.format(Locale.ROOT, "%.2f s", (double)Math.max(0, var0) / 20.0);
   }

   private LungeController() {
   }

   public static record Type_c(int slot, float delay, float minimum, int lunge, int durability, boolean breaks, boolean sameItem) {
   }

   public static record Type_g(int slot, int ticks, boolean swap, int lunge, boolean resets) {
   }

   public static enum Type_n {
      KEPT,
      RESET;

      private Type_n() {
      }
   }
}
