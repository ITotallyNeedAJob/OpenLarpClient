package com.larpclient;

public final class mk {
   public static mk.Type_g UAsMk_Type_g(int var0, boolean var1, boolean var2) {
      if (var1 && var0 >= 0 && var0 <= 20) {
         return var2 ? mk.Type_g.READY : mk.Type_g.WAIT;
      } else {
         return mk.Type_g.CANCEL;
      }
   }

   private mk() {
   }

   public static enum Type_g {
      CANCEL,
      WAIT,
      READY;

      private Type_g() {
      }
   }
}
