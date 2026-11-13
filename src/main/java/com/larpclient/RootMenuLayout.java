package com.larpclient;

public record RootMenuLayout(int width, int height, int rowHeight, int menuWidth, int menuY, int logoSize, int wordmarkWidth) {
   public static final int WORDMARK_WIDTH = 57;

   public static RootMenuLayout calculate(int var0, int var1) {
      return calculate(var0, var1, 57);
   }

   public static RootMenuLayout calculate(int var0, int var1, int var2) {
      int var3 = var1 < 220 ? 20 : (var1 < 300 ? 22 : 28);
      int var4 = var1 < 220 ? 16 : (var1 < 300 ? 22 : (var1 < 360 ? 30 : 38));
      int var5 = utilityHeight(var1);
      int var6 = gap(var1);
      int var7 = groupGap(var1);
      int var8 = var4 + brandGap(var1);
      int var9 = var8 + var3 * 2 + var6 + var7 + var5 * 2 + var6 + quitGap(var1) + quitHeight(var1);
      int var10 = Math.max(34, (var1 - var9) / 2 - 4);
      return new RootMenuLayout(var0, var1, var3, Math.min(208, var0 - 40), var10 + var8, var4, Math.max(1, var2));
   }

   private static int gap(int var0) {
      return var0 < 220 ? 2 : 4;
   }

   private static int groupGap(int var0) {
      return var0 < 220 ? 5 : (var0 < 300 ? 8 : 12);
   }

   private static int utilityHeight(int var0) {
      return var0 < 220 ? 20 : (var0 < 300 ? 22 : 28);
   }

   private static int quitGap(int var0) {
      return var0 < 220 ? 4 : (var0 < 300 ? 8 : 12);
   }

   private static int quitHeight(int var0) {
      return var0 < 220 ? 14 : 18;
   }

   private static int brandGap(int var0) {
      return var0 < 220 ? 8 : (var0 < 300 ? 12 : 24);
   }

   public float brandScale() {
      return this.height < 220 ? 1.0F : (this.height < 300 ? 1.25F : 1.75F);
   }

   public int lockupGap() {
      return Math.round((float)this.logoSize * 0.26F);
   }

   public MenuLayout.Type_c navigation(int var1) {
      int var2 = (this.width - this.menuWidth) / 2;
      int var3 = this.menuY + this.rowHeight * 2 + gap(this.height) + groupGap(this.height);

      return switch (var1) {
         case 0, 1 -> new MenuLayout.Type_c(var2, this.menuY + var1 * (this.rowHeight + gap(this.height)), this.menuWidth, this.rowHeight);
         case 2, 3 -> new MenuLayout.Type_c(
         var2, var3 + (var1 - 2) * (utilityHeight(this.height) + gap(this.height)), this.menuWidth, utilityHeight(this.height)
      );
         case 4 -> new MenuLayout.Type_c(
         this.width / 2 - 50, var3 + utilityHeight(this.height) * 2 + gap(this.height) + quitGap(this.height), 100, quitHeight(this.height)
      );
         default -> throw new IllegalArgumentException("Unknown root menu action");
      };
   }

   public MenuLayout.Type_c logo() {
      int var1 = (int)Math.ceil((double)((float)this.wordmarkWidth * this.brandScale()));
      return new MenuLayout.Type_c(
         (this.width - this.logoSize - this.lockupGap() - var1) / 2, this.menuY - brandGap(this.height) - this.logoSize, this.logoSize, this.logoSize
      );
   }

   public MenuLayout.Type_c title() {
      MenuLayout.Type_c var1 = this.logo();
      int var2 = (int)Math.ceil((double)((float)this.wordmarkWidth * this.brandScale()));
      int var3 = (int)Math.ceil((double)(9.0F * this.brandScale()));
      return new MenuLayout.Type_c(var1.x() + this.logoSize + this.lockupGap(), var1.y() + (this.logoSize - var3) / 2, var2, var3);
   }

   public MenuLayout.Type_c account() {
      return new MenuLayout.Type_c(12, 10, Math.min(140, (this.width - 32) / 2), 22);
   }
}
