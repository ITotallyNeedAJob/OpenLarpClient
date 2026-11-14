package com.larpclient;

public final class BlockHighlight {
   public static final String I = "Larp Client";
   public static final int z = 512;
   public static final int s = 96;
   public static final int B = 256;
   public static final int a = 1152;
   public static final int m = a + 256;

   private BlockHighlight() {
   }

   public static BlockHighlight.Type_t TAsB3_Type_t(int var0, int var1) {
      int var2 = Math.max(12, Math.min(Math.min(104, var1 / 5), var0 / 3));
      int var3 = Math.max(1, Math.min(256, var0 - 32));
      int var4 = Math.max(1, var3 * 96 / 512);
      int var5 = Math.max(8, var2 / 6);
      int var6 = (var1 - var2 - var5 - var4) / 2;
      int var7 = Math.min(Math.min(var0, var1), var2 * 2);
      MenuLayout.Type_c var8 = new MenuLayout.Type_c((var0 - var2) / 2, var6, var2, var2);
      return new BlockHighlight.Type_t(
         var8,
         new MenuLayout.Type_c((var0 - var3) / 2, var6 + var2 + var5, var3, var4),
         new MenuLayout.Type_c((var0 - var7) / 2, var6 + (var2 - var7) / 2, var7, var7)
      );
   }

   public static record Type_t(MenuLayout.Type_c logo, MenuLayout.Type_c title, MenuLayout.Type_c glow) {
   }
}
