package com.larpclient;

import java.util.random.RandomGenerator;

public final class OfferPanel {
   private OfferPanel.Type_r Z = OfferPanel.Type_r.IDLE;
   private int Y;
   private int w = 1;

   public OfferPanel() {
   }

   public OfferPanel.Type_r NAsOe_Type_r() {
      return this.Z;
   }

   public int eAsInt() {
      return this.Y;
   }

   public boolean YAsBoolean() {
      return this.Y >= this.w;
   }

   public boolean wAsBoolean() {
      return this.Y >= this.w + 20;
   }

   public void mAsVoid() {
      if (this.Y < Integer.MAX_VALUE) {
         this.Y++;
      }
   }

   public void QAsVoid(OfferPanel.Type_r var1, int var2) {
      this.Z = var1;
      this.w = Math.max(0, var2);
      this.Y = 0;
   }

   public void CAsVoid() {
      this.QAsVoid(OfferPanel.Type_r.IDLE, 1);
   }

   public static int lAsInt(int var0, int var1, boolean var2, RandomGenerator var3) {
      int var4 = Math.min(var0, var1);
      int var5 = Math.max(var0, var1);
      return Math.max(0, var2 ? var3.nextInt(var4, var5 + 1) + var3.nextInt(-2, 3) : var4);
   }

   public static int SAsInt(int var0, boolean var1, RandomGenerator var2) {
      return Math.max(0, var0 + (var1 ? var2.nextInt(-1, 2) : 0));
   }

   public static boolean mAsBoolean(int var0, int var1, long var2, long var4) {
      return var0 != var1 && var2 != var4;
   }

   public static boolean dAsBoolean(String var0, String var1) {
      return var0 != null && var1 != null && var0.trim().equalsIgnoreCase(var1.trim());
   }

   public static boolean vAsBoolean(String var0) {
      return "lime_stained_glass_pane".equals(var0) || "green_stained_glass_pane".equals(var0);
   }

   public static boolean pAsBoolean(String var0) {
      if (var0 != null) {
         switch (var0) {
            case "air":
            case "lime_stained_glass_pane":
            case "green_stained_glass_pane":
            case "red_stained_glass_pane":
            case "black_stained_glass_pane":
            case "gray_stained_glass_pane":
               break;
            default:
               return true;
         }
      }

      return false;
   }

   public static enum Type_r {
      IDLE,
      SEND_COMMAND,
      WAIT_MENU_OPEN,
      FILLING_ITEMS,
      POST_FILL_WAIT,
      CLICK_SELL,
      POST_SELL_WAIT,
      CLOSE_MENU,
      COOLDOWN,
      REOPEN_MENU;

      private Type_r() {
      }
   }

   public static final class Type_z {
      private long f;
      private int j;

      public Type_z() {
      }

      public void qAsVoid(int var1, long var2) {
         if (var1 >= 1 && var1 <= 5) {
            this.j = var1;
            this.f = var2;
         } else {
            throw new IllegalArgumentException("Home slot");
         }
      }

      public int BAsInt(long var1) {
         if (this.j != 0 && var1 - this.f >= 1000000000L) {
            int var3 = this.j;
            this.AAsVoid();
            return var3;
         } else {
            return 0;
         }
      }

      public void AAsVoid() {
         this.j = 0;
         this.f = 0L;
      }

      public boolean TAsBoolean() {
         return this.j != 0;
      }
   }
}
